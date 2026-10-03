package com.secretmafia.game

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _state = MutableStateFlow<GameState?>(null)
    val state: StateFlow<GameState?> = _state

    fun start(
        names: List<String>,
        counts: RoleCounts,
        settings: AppSettings,
        chances: RoleChances = RoleChances(),
    ) {
        val players = GameRules.assignRoles(names, counts, chances).map { p ->
            if (p.role == Role.WHORE && settings.whoreAlign != WhoreAlign.PICK) {
                p.copy(align = settings.whoreAlign)
            } else {
                p
            }
        }
        val order = nightOrderIds(players, 0, settings)
        val firstId = order.first()
        _state.value = GameState(
            settings = settings,
            players = players,
            nightNumber = 1,
            startIndex = 0,
            nightOrder = order,
            phase = nightOpenPhase(settings.mafiaConfer, firstId),
            dummyOverride = GameRules.dummyFor(settings),
            startedAtMillis = System.currentTimeMillis(),
        )
    }

    fun clear() {
        _state.value = null
    }

    fun patchSettings(transform: (AppSettings) -> AppSettings) {
        _state.update { it?.copy(settings = transform(it.settings)) }
    }

    fun markMatchPaid() {
        _state.update { it?.copy(matchPaid = true) }
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
                framerPicking = false,
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
                    lastAction = NightActionResult(ActionAnimation.VOTE, actorRole = actor.role),
                    phase = GamePhase.RevealWait(actor.id, PassKind.DAY_VOTE),
                )
            }
            PassKind.HUNTER -> {
                val (players, extra) = GameRules.applyHunterShot(s.players, targetId)
                finishHunter(s, players, extra)
            }
            PassKind.NIGHT -> when (actor.role) {
                Role.MAFIA, Role.DON -> recordMafiaVote(s, actor, targetId, goShield = false, goFrame = false)
                Role.LAWYER -> recordMafiaVote(s, actor, targetId, goShield = !s.lawyerShieldUsed, goFrame = false)
                Role.FRAMER -> recordMafiaVote(s, actor, targetId, goShield = false, goFrame = true)
                Role.HEALER -> reveal(s, actor, s.copy(healTargetId = targetId), ActionAnimation.HEAL)
                Role.BODYGUARD -> reveal(s, actor, s.copy(bodyguardTargetId = targetId), ActionAnimation.GUARD)
                Role.KILLER -> reveal(s, actor, s.copy(killerTargetId = targetId), ActionAnimation.KNIFE)
                Role.VIGILANTE -> reveal(
                    s, actor,
                    s.copy(vigilanteTargetId = targetId, vigilanteUsed = true),
                    ActionAnimation.KNIFE,
                )
                Role.POISONER -> reveal(s, actor, s.copy(poisonTargetId = targetId), ActionAnimation.POISON)
                Role.WHORE -> reveal(s, actor, s.copy(whoreTargetId = targetId), ActionAnimation.SLEEP)
                Role.COP -> {
                    val target = s.players.first { it.id == targetId }
                    reveal(
                        s, actor, s,
                        ActionAnimation.INSPECT,
                        inspectGood = !GameRules.copSeesEvil(target, lookFrame(s)),
                        inspectName = target.name,
                    )
                }
                Role.SEER -> {
                    val target = s.players.first { it.id == targetId }
                    val seen = GameRules.roleSeerSees(
                        target,
                        lookFrame(s),
                        s.settings.framerFoolsSeer,
                        s.settings.seerSeesTraitor,
                    )
                    reveal(
                        s, actor, s,
                        ActionAnimation.SEER,
                        inspectName = target.name,
                        inspectRole = seen?.name,
                    )
                }
                Role.NECROMANCER -> {
                    val target = s.players.first { it.id == targetId }
                    reveal(
                        s, actor, s,
                        ActionAnimation.SEER,
                        inspectName = target.name,
                        inspectRole = target.role?.name,
                    )
                }
                Role.AMNESIAC -> {
                    val target = s.players.first { it.id == targetId }
                    val taken = target.role
                    if (!GameRules.amnesiacCanTake(taken)) return
                    val players = s.players.map { p ->
                        if (p.id == actor.id) p.copy(role = taken, twinId = null) else p
                    }
                    reveal(
                        s, actor, s.copy(players = players),
                        ActionAnimation.RISE,
                        inspectName = target.name,
                        inspectRole = taken?.name,
                    )
                }
                else -> { }
            }
        }
    }

    fun submitWhoreAlign(align: WhoreAlign) {
        val s = _state.value ?: return
        val phase = s.phase as? GamePhase.Action ?: return
        if (align == WhoreAlign.PICK) return
        _state.update {
            it?.copy(
                players = s.players.map { p ->
                    if (p.id == phase.playerId) p.copy(align = align) else p
                },
            )
        }
    }

    fun submitMayorReveal() {
        val s = _state.value ?: return
        val phase = s.phase as? GamePhase.Action ?: return
        val actor = s.players.first { it.id == phase.playerId }
        reveal(s, actor, s.copy(mayorRevealed = true), ActionAnimation.REVEAL)
    }

    fun submitFrame(targetId: String?) {
        val s = _state.value ?: return
        val phase = s.phase as? GamePhase.Action ?: return
        val actor = s.players.first { it.id == phase.playerId }
        _state.update {
            it?.copy(
                framedId = targetId,
                framerPicking = false,
                lastAction = NightActionResult(
                    animation = if (targetId != null) ActionAnimation.FRAME else ActionAnimation.KNIFE,
                    actorRole = actor.role,
                ),
                phase = GamePhase.RevealWait(actor.id, PassKind.NIGHT),
            )
        }
    }

    fun submitSkipNight() = dummy(ActionAnimation.LIKE)

    fun submitLawyerShield(targetId: String?) {
        val s = _state.value ?: return
        val phase = s.phase as? GamePhase.Action ?: return
        val actor = s.players.first { it.id == phase.playerId }
        _state.update {
            it?.copy(
                lawyerShieldTargetId = targetId,
                lawyerShieldUsed = targetId != null || s.lawyerShieldUsed,
                lawyerPickingShield = false,
                lastAction = NightActionResult(
                    animation = if (targetId != null) ActionAnimation.SHIELD else ActionAnimation.KNIFE,
                    actorRole = actor.role,
                ),
                phase = GamePhase.RevealWait(actor.id, PassKind.NIGHT),
            )
        }
    }

    fun submitDummyLike() = dummy(ActionAnimation.LIKE)
    fun submitDummyMath() = dummy(ActionAnimation.MATH)

    private fun dummy(anim: ActionAnimation) {
        val s = _state.value ?: return
        val phase = s.phase as? GamePhase.Action ?: return
        val actor = s.players.first { it.id == phase.playerId }
        _state.update {
            it?.copy(
                lastAction = NightActionResult(anim, actorRole = actor.role),
                phase = GamePhase.RevealWait(phase.playerId, phase.kind),
            )
        }
    }

    private fun recordMafiaVote(
        s: GameState,
        actor: Player,
        targetId: String,
        goShield: Boolean,
        goFrame: Boolean,
    ) {
        val next = s.copy(mafiaVotes = s.mafiaVotes + (actor.id to targetId))
        when {
            goShield -> _state.update {
                next.copy(
                    lawyerPickingShield = true,
                    lastAction = NightActionResult(ActionAnimation.KNIFE, actorRole = actor.role),
                )
            }
            goFrame -> _state.update {
                next.copy(
                    framerPicking = true,
                    lastAction = NightActionResult(ActionAnimation.KNIFE, actorRole = actor.role),
                )
            }
            else -> reveal(s, actor, next, ActionAnimation.KNIFE)
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
                lastAction = NightActionResult(
                    animation = anim,
                    inspectGood = inspectGood,
                    inspectName = inspectName,
                    inspectRoleTitle = inspectRole,
                    actorRole = actor.role,
                ),
                phase = GamePhase.RevealWait(actor.id, PassKind.NIGHT),
            )
        }
    }

    fun finishReveal() {
        val s = _state.value ?: return
        val phase = s.phase as? GamePhase.RevealWait ?: return
        if (phase.kind == PassKind.HUNTER) return
        val queue = passQueue(s, phase.kind)
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
        val blocked = GameRules.nightAfterBlock(
            players = s.players,
            blockedId = s.whoreTargetId,
            mafiaVotes = s.mafiaVotes,
            healTargetId = s.healTargetId,
            bodyguardTargetId = s.bodyguardTargetId,
            killerTargetId = s.killerTargetId,
            vigilanteTargetId = s.vigilanteTargetId,
            poisonTargetId = s.poisonTargetId,
            framedId = s.framedId,
        )
        val hits = GameRules.planNightHits(
            players = s.players,
            mafiaVotes = blocked.mafiaVotes,
            healTargetId = blocked.healTargetId,
            bodyguardTargetId = blocked.bodyguardTargetId,
            killerTargetId = blocked.killerTargetId,
            nightNumber = s.nightNumber,
            firstNightKill = s.settings.firstNightKill,
            vigilanteTargetId = blocked.vigilanteTargetId,
            poisonDueId = s.poisonDueId,
        )
        val (players, deaths) = GameRules.applyNightDeaths(s.players, hits, blocked.bodyguardTargetId)
        val whoreRole = s.players.firstOrNull { it.id == s.whoreTargetId }?.role
        val armed = GameRules.armLook(
            s.settings.inspectSameNight,
            blocked.framedId,
            s.whoreTargetId,
            whoreRole,
        )
        val hunter = GameRules.firstDeadHunter(s.players, players)
        val cleared = s.copy(
            players = players,
            lastNightDeaths = deaths,
            lastHealTargetId = blocked.healTargetId,
            lastBodyguardTargetId = blocked.bodyguardTargetId,
            lastWhoreTargetId = s.whoreTargetId ?: s.lastWhoreTargetId,
            healTargetId = null,
            bodyguardTargetId = null,
            killerTargetId = null,
            whoreTargetId = null,
            framedId = null,
            activeFrameId = armed.first,
            sleepingId = armed.second,
            vigilanteTargetId = null,
            poisonTargetId = blocked.poisonTargetId,
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
        val next = s.copy(players = players)
        _state.value = if (winner != null) next.endWith(winner) else next.copy(phase = GamePhase.NightSummary)
    }

    private fun finishHunter(s: GameState, players: List<Player>, extra: List<DeathRecord>) {
        val resume = s.hunterResume ?: HunterResume.NIGHT
        val nightDeaths = if (resume == HunterResume.NIGHT) s.lastNightDeaths + extra else s.lastNightDeaths
        val dayDeaths = if (resume == HunterResume.DAY) s.lastDayDeaths + extra else s.lastDayDeaths
        val winner = GameRules.winner(players)
        val next = s.copy(
            players = players,
            lastNightDeaths = nightDeaths,
            lastDayDeaths = dayDeaths,
            pendingHunterId = null,
            hunterResume = null,
            lastAction = NightActionResult(ActionAnimation.HUNT, actorRole = Role.HUNTER),
        )
        _state.value = when {
            winner != null -> next.endWith(winner)
            resume == HunterResume.NIGHT -> next.copy(phase = GamePhase.NightSummary)
            else -> next.copy(phase = GamePhase.DaySummary)
        }
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
        applyDayDeath(s, GameRules.resolveDayPhoneVote(s.dayVotes, s.mayorRevealedId()))
    }

    private fun applyDayDeath(s: GameState, victimId: String?) {
        if (victimId != null && s.players.firstOrNull { it.id == victimId }?.role == Role.JOKER) {
            val (players, deaths) = GameRules.applyDayExile(s.players, victimId, null)
            _state.value = s.copy(
                players = players,
                lastDayDeaths = deaths,
                dayVotes = emptyMap(),
                lastAction = null,
            ).endWith(Winner.JOKER)
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
        _state.value = if (winner != null) cleared.endWith(winner) else cleared.copy(phase = GamePhase.DaySummary)
    }

    private fun GameState.mayorRevealedId(): String? =
        if (!mayorRevealed) null else players.firstOrNull { it.alive && it.role == Role.MAYOR }?.id

    private fun GameState.endWith(winner: Winner): GameState {
        val reward = matchReward ?: Progress.evaluateMatch(System.currentTimeMillis() - startedAtMillis, winner)
        val lived = players.any { it.alive && it.role == Role.SURVIVOR }
        return copy(phase = GamePhase.GameOver(winner), matchReward = reward, survivorLived = lived)
    }

    fun continueFromDaySummary() {
        val s = _state.value ?: return
        val start = GameRules.nextStartIndex(s.players, s.startIndex)
        val order = nightOrderIds(s.players, start, s.settings)
        val first = order.first()
        _state.update {
            it?.copy(
                nightNumber = s.nightNumber + 1,
                startIndex = start,
                nightOrder = order,
                mafiaVotes = emptyMap(),
                healTargetId = null,
                bodyguardTargetId = null,
                killerTargetId = null,
                whoreTargetId = null,
                poisonDueId = s.poisonTargetId,
                poisonTargetId = null,
                framedId = null,
                dayVotes = emptyMap(),
                lastAction = null,
                dummyOverride = GameRules.dummyFor(s.settings),
                phase = nightOpenPhase(s.settings.mafiaConfer, first),
            )
        }
    }

    fun finishMafiaConfer() {
        val s = _state.value ?: return
        if (s.phase !is GamePhase.MafiaConfer) return
        val first = s.nightOrder.firstOrNull()
            ?: GameRules.livingInOrder(s.players, s.startIndex).firstOrNull()?.id
            ?: return
        _state.update { it?.copy(phase = GamePhase.Handoff(first, PassKind.NIGHT)) }
    }

    private fun nightOpenPhase(confer: Boolean, firstId: String): GamePhase =
        if (confer) GamePhase.MafiaConfer else GamePhase.Handoff(firstId, PassKind.NIGHT)

    private fun nightOrderIds(players: List<Player>, start: Int, settings: AppSettings): List<String> =
        GameRules.nightPass(players, start, settings.inspectSameNight).map { it.id }

    private fun passQueue(s: GameState, kind: PassKind): List<Player> {
        if (kind == PassKind.NIGHT && s.nightOrder.isNotEmpty()) {
            return s.nightOrder.mapNotNull { id -> s.players.firstOrNull { it.id == id && it.alive } }
        }
        return GameRules.livingInOrder(s.players, s.startIndex)
    }

    private fun lookFrame(s: GameState): String? {
        val whoreRole = s.players.firstOrNull { it.id == s.whoreTargetId }?.role
        return GameRules.frameForInspect(
            s.settings.inspectSameNight,
            s.framedId,
            s.activeFrameId,
            whoreRole,
        )
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
            Role.NECROMANCER -> GameRules.deadPlayers(s.players)
            Role.AMNESIAC -> GameRules.deadTakeable(s.players)
            Role.VIGILANTE, Role.POISONER, Role.KILLER -> living.filter { it.id != actor.id }
            Role.WHORE -> GameRules.whoreTargets(living, actor.id, s.lastWhoreTargetId)
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

    fun lookBlocked(actor: Player): Boolean {
        val s = _state.value ?: return false
        if (actor.role != Role.COP && actor.role != Role.SEER) return false
        return if (s.settings.inspectSameNight) s.whoreTargetId == actor.id else s.sleepingId == actor.id
    }

    fun usesDummy(actor: Player): Boolean {
        val s = _state.value ?: return true
        val vision = actor.role == Role.COP || actor.role == Role.SEER
        if (vision && !s.settings.inspectSameNight) {
            if (s.sleepingId == actor.id) return true
        } else if (s.whoreTargetId == actor.id) {
            return true
        }
        return when (actor.role) {
            Role.WHORE -> nightTargets(actor).isEmpty()
            Role.MAFIA, Role.DON, Role.LAWYER, Role.FRAMER,
            Role.HEALER, Role.COP, Role.SEER, Role.BODYGUARD, Role.POISONER, Role.TWIN_MAFIA -> false
            Role.KILLER -> !GameRules.killerActsTonight(s.nightNumber)
            Role.MAYOR -> s.mayorRevealed
            Role.NECROMANCER -> GameRules.deadPlayers(s.players).isEmpty()
            Role.VIGILANTE -> s.vigilanteUsed
            Role.AMNESIAC -> GameRules.deadTakeable(s.players).isEmpty()
            Role.HUNTER, Role.JOKER, Role.CIVILIAN, Role.LUNATIC, Role.DRUNK, Role.TWIN_CIVIL,
            Role.TRAITOR, Role.CURSED, Role.SURVIVOR, null -> true
        }
    }
}
