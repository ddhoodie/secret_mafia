package com.secretmafia.game

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _state = MutableStateFlow<GameState?>(null)
    val state: StateFlow<GameState?> = _state

    fun start(names: List<String>, counts: RoleCounts, settings: AppSettings) {
        val players = GameRules.assignRoles(names, counts)
        val first = GameRules.livingInOrder(players, 0).first()
        _state.value = GameState(
            settings = settings,
            players = players,
            nightNumber = 1,
            startIndex = 0,
            phase = GamePhase.Handoff(first.id, PassKind.NIGHT),
            dummyOverride = GameRules.dummyFor(settings),
        )
    }

    fun clear() {
        _state.value = null
    }

    fun finishHandoff() {
        val s = _state.value ?: return
        val phase = s.phase as? GamePhase.Handoff ?: return
        _state.update { it?.copy(phase = GamePhase.Unlock(phase.playerId, phase.kind)) }
    }

    fun unlock() {
        val s = _state.value ?: return
        val phase = s.phase as? GamePhase.Unlock ?: return
        _state.update {
            it?.copy(
                phase = GamePhase.Action(phase.playerId, phase.kind),
                lastAction = null,
                lawyerPickingShield = false,
                dummyOverride = GameRules.dummyFor(s.settings),
            )
        }
    }

    fun submitNightTarget(targetId: String) {
        val s = _state.value ?: return
        val phase = s.phase as? GamePhase.Action ?: return
        val actor = s.players.first { it.id == phase.playerId }
        when (phase.kind) {
            PassKind.DAY_VOTE -> _state.update {
                it?.copy(
                    dayVotes = s.dayVotes + (actor.id to targetId),
                    lastAction = NightActionResult(ActionAnimation.VOTE),
                    phase = GamePhase.RevealWait(actor.id, PassKind.DAY_VOTE),
                )
            }
            PassKind.HUNTER -> {
                val (players, extra) = GameRules.applyHunterShot(s.players, targetId)
                finishHunter(s, players, extra)
            }
            PassKind.NIGHT -> when (actor.role) {
                Role.MAFIA, Role.DON -> recordMafiaVote(s, actor, targetId, goShield = false)
                Role.LAWYER -> recordMafiaVote(s, actor, targetId, goShield = !s.lawyerShieldUsed)
                Role.HEALER -> reveal(s, actor, s.copy(healTargetId = targetId), ActionAnimation.HEAL)
                Role.BODYGUARD -> reveal(s, actor, s.copy(bodyguardTargetId = targetId), ActionAnimation.GUARD)
                Role.KILLER -> reveal(s, actor, s.copy(killerTargetId = targetId), ActionAnimation.KNIFE)
                Role.COP -> {
                    val target = s.players.first { it.id == targetId }
                    reveal(
                        s, actor, s,
                        ActionAnimation.INSPECT,
                        inspectGood = !GameRules.copSeesEvil(target),
                        inspectName = target.name,
                    )
                }
                Role.SEER -> {
                    val target = s.players.first { it.id == targetId }
                    reveal(
                        s, actor, s,
                        ActionAnimation.SEER,
                        inspectName = target.name,
                        inspectRole = target.role?.name,
                    )
                }
                else -> { }
            }
        }
    }

    fun submitLawyerShield(targetId: String?) {
        val s = _state.value ?: return
        val phase = s.phase as? GamePhase.Action ?: return
        val actor = s.players.first { it.id == phase.playerId }
        _state.update {
            it?.copy(
                lawyerShieldTargetId = targetId,
                lawyerShieldUsed = targetId != null || s.lawyerShieldUsed,
                lawyerPickingShield = false,
                lastAction = NightActionResult(if (targetId != null) ActionAnimation.SHIELD else ActionAnimation.KNIFE),
                phase = GamePhase.RevealWait(actor.id, PassKind.NIGHT),
            )
        }
    }

    fun submitDummyLike() = dummy(ActionAnimation.LIKE)
    fun submitDummyMath() = dummy(ActionAnimation.MATH)

    private fun dummy(anim: ActionAnimation) {
        val s = _state.value ?: return
        val phase = s.phase as? GamePhase.Action ?: return
        _state.update {
            it?.copy(
                lastAction = NightActionResult(anim),
                phase = GamePhase.RevealWait(phase.playerId, phase.kind),
            )
        }
    }

    private fun recordMafiaVote(s: GameState, actor: Player, targetId: String, goShield: Boolean) {
        val next = s.copy(mafiaVotes = s.mafiaVotes + (actor.id to targetId))
        if (goShield) {
            _state.update {
                next.copy(lawyerPickingShield = true, lastAction = NightActionResult(ActionAnimation.KNIFE))
            }
        } else {
            reveal(s, actor, next, ActionAnimation.KNIFE)
        }
    }

    private fun reveal(
        s: GameState,
        actor: Player,
        next: GameState,
        anim: ActionAnimation,
        inspectGood: Boolean? = null,
        inspectName: String? = null,
        inspectRole: String? = null,
    ) {
        _state.update {
            next.copy(
                lastAction = NightActionResult(anim, inspectGood, inspectName, inspectRole),
                phase = GamePhase.RevealWait(actor.id, PassKind.NIGHT),
            )
        }
    }

    fun finishReveal() {
        val s = _state.value ?: return
        val phase = s.phase as? GamePhase.RevealWait ?: return
        if (phase.kind == PassKind.HUNTER) return
        val queue = GameRules.livingInOrder(s.players, s.startIndex)
        val idx = queue.indexOfFirst { it.id == phase.playerId }
        val next = queue.getOrNull(idx + 1)
        if (next != null) {
            _state.update { it?.copy(phase = GamePhase.Handoff(next.id, phase.kind), lastAction = null) }
            return
        }
        when (phase.kind) {
            PassKind.NIGHT -> resolveNight()
            PassKind.DAY_VOTE -> resolvePhoneDay()
            PassKind.HUNTER -> { }
        }
    }

    private fun resolveNight() {
        val s = _state.value ?: return
        val hits = GameRules.planNightHits(
            players = s.players,
            mafiaVotes = s.mafiaVotes,
            healTargetId = s.healTargetId,
            bodyguardTargetId = s.bodyguardTargetId,
            killerTargetId = s.killerTargetId,
            nightNumber = s.nightNumber,
            firstNightKill = s.settings.firstNightKill,
        )
        val (players, deaths) = GameRules.applyNightDeaths(s.players, hits, s.bodyguardTargetId)
        val hunter = GameRules.firstDeadHunter(s.players, players)
        val cleared = s.copy(
            players = players,
            lastNightDeaths = deaths,
            lastHealTargetId = s.healTargetId,
            lastBodyguardTargetId = s.bodyguardTargetId,
            healTargetId = null,
            bodyguardTargetId = null,
            killerTargetId = null,
            mafiaVotes = emptyMap(),
            lastAction = null,
        )
        if (hunter != null) {
            _state.value = cleared.copy(
                pendingHunterId = hunter.id,
                hunterResume = HunterResume.NIGHT,
                phase = GamePhase.Handoff(hunter.id, PassKind.HUNTER),
            )
            return
        }
        finishAfterNight(cleared, players)
    }

    private fun finishAfterNight(s: GameState, players: List<Player>) {
        val winner = GameRules.winner(players)
        _state.value = s.copy(
            players = players,
            phase = if (winner != null) GamePhase.GameOver(winner) else GamePhase.NightSummary,
        )
    }

    private fun finishHunter(s: GameState, players: List<Player>, extra: List<DeathRecord>) {
        val resume = s.hunterResume ?: HunterResume.NIGHT
        val nightDeaths = if (resume == HunterResume.NIGHT) s.lastNightDeaths + extra else s.lastNightDeaths
        val dayDeaths = if (resume == HunterResume.DAY) s.lastDayDeaths + extra else s.lastDayDeaths
        val winner = GameRules.winner(players)
        _state.value = s.copy(
            players = players,
            lastNightDeaths = nightDeaths,
            lastDayDeaths = dayDeaths,
            pendingHunterId = null,
            hunterResume = null,
            lastAction = NightActionResult(ActionAnimation.HUNT),
            phase = when {
                winner != null -> GamePhase.GameOver(winner)
                resume == HunterResume.NIGHT -> GamePhase.NightSummary
                else -> GamePhase.DaySummary
            },
        )
    }

    fun continueFromNightSummary() {
        _state.update { it?.copy(phase = GamePhase.Discuss) }
    }

    fun continueFromDiscuss() {
        val s = _state.value ?: return
        if (s.settings.dayVoteMode == DayVoteMode.LIVE) {
            _state.update { it?.copy(phase = GamePhase.DayVoteLive, dayVotes = emptyMap()) }
        } else {
            val start = GameRules.nextStartIndex(s.players, s.startIndex)
            val first = GameRules.livingInOrder(s.players, start).first()
            _state.update {
                it?.copy(
                    startIndex = start,
                    dayVotes = emptyMap(),
                    phase = GamePhase.Handoff(first.id, PassKind.DAY_VOTE),
                )
            }
        }
    }

    fun submitLiveExile(targetId: String?) {
        val s = _state.value ?: return
        applyDayDeath(s, targetId)
    }

    private fun resolvePhoneDay() {
        val s = _state.value ?: return
        applyDayDeath(s, GameRules.resolveDayPhoneVote(s.dayVotes))
    }

    private fun applyDayDeath(s: GameState, victimId: String?) {
        if (victimId != null && s.players.firstOrNull { it.id == victimId }?.role == Role.JOKER) {
            val (players, deaths) = GameRules.applyDayExile(s.players, victimId, null)
            _state.value = s.copy(
                players = players,
                lastDayDeaths = deaths,
                dayVotes = emptyMap(),
                lastAction = null,
                phase = GamePhase.GameOver(Winner.JOKER),
            )
            return
        }
        val (players, deaths) = GameRules.applyDayExile(s.players, victimId, s.lawyerShieldTargetId)
        val shieldUsedNow = victimId != null && victimId == s.lawyerShieldTargetId
        val hunter = GameRules.firstDeadHunter(s.players, players)
        val cleared = s.copy(
            players = players,
            lastDayDeaths = deaths,
            dayVotes = emptyMap(),
            lawyerShieldTargetId = if (shieldUsedNow) null else s.lawyerShieldTargetId,
            lastAction = null,
        )
        if (hunter != null) {
            _state.value = cleared.copy(
                pendingHunterId = hunter.id,
                hunterResume = HunterResume.DAY,
                phase = GamePhase.Handoff(hunter.id, PassKind.HUNTER),
            )
            return
        }
        val winner = GameRules.winner(players)
        _state.value = cleared.copy(
            phase = if (winner != null) GamePhase.GameOver(winner) else GamePhase.DaySummary,
        )
    }

    fun continueFromDaySummary() {
        val s = _state.value ?: return
        val start = GameRules.nextStartIndex(s.players, s.startIndex)
        val first = GameRules.livingInOrder(s.players, start).first()
        _state.update {
            it?.copy(
                nightNumber = s.nightNumber + 1,
                startIndex = start,
                mafiaVotes = emptyMap(),
                healTargetId = null,
                bodyguardTargetId = null,
                killerTargetId = null,
                dayVotes = emptyMap(),
                lastAction = null,
                dummyOverride = GameRules.dummyFor(s.settings),
                phase = GamePhase.Handoff(first.id, PassKind.NIGHT),
            )
        }
    }

    fun livingMafiaBesides(playerId: String): List<Player> {
        val s = _state.value ?: return emptyList()
        return s.players.filter { it.alive && it.role?.mafiaKnown == true && it.id != playerId }
    }

    fun livingMafiaInOrder(): List<Player> {
        val s = _state.value ?: return emptyList()
        return GameRules.livingInOrder(s.players, 0).filter { it.role?.mafiaKnown == true }
    }

    fun isLastMafia(playerId: String): Boolean = livingMafiaInOrder().lastOrNull()?.id == playerId

    fun mafiaBefore(playerId: String): List<Player> {
        val order = livingMafiaInOrder()
        val idx = order.indexOfFirst { it.id == playerId }
        if (idx <= 0) return emptyList()
        return order.take(idx)
    }

    fun currentVictimPreview(): Player? {
        val s = _state.value ?: return null
        val id = GameRules.pickMafiaVictim(s.players, s.mafiaVotes) ?: return null
        return s.players.firstOrNull { it.id == id }
    }

    fun nightTargets(actor: Player): List<Player> {
        val s = _state.value ?: return emptyList()
        val living = s.players.filter { it.alive }
        return when (actor.role) {
            Role.COP, Role.SEER -> living.filter { it.id != actor.id }
            Role.HEALER -> {
                if (s.settings.healerMayRepeatTarget || s.lastHealTargetId == null) living
                else living.filter { it.id != s.lastHealTargetId }
            }
            Role.BODYGUARD -> {
                val pool = living.filter { it.id != actor.id }
                if (s.settings.healerMayRepeatTarget || s.lastBodyguardTargetId == null) pool
                else pool.filter { it.id != s.lastBodyguardTargetId }
            }
            else -> living
        }
    }

    fun twinOf(playerId: String): Player? {
        val s = _state.value ?: return null
        val me = s.players.firstOrNull { it.id == playerId } ?: return null
        val pair = me.twinId ?: return null
        return s.players.firstOrNull { it.id != playerId && it.twinId == pair }
    }

    fun usesDummy(actor: Player): Boolean {
        val s = _state.value ?: return true
        return when (actor.role) {
            Role.MAFIA, Role.DON, Role.LAWYER, Role.HEALER, Role.COP, Role.SEER, Role.BODYGUARD -> false
            Role.KILLER -> !GameRules.killerActsTonight(s.nightNumber)
            Role.HUNTER, Role.JOKER, Role.CIVILIAN, Role.LUNATIC, Role.DRUNK, Role.TWIN_CIVIL, null -> true
            Role.TWIN_MAFIA -> false
        }
    }
}
