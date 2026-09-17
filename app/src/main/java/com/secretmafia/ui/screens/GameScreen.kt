package com.secretmafia.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.secretmafia.game.ActionAnimation
import com.secretmafia.game.CoinKind
import com.secretmafia.game.DeathRecord
import com.secretmafia.game.DummyActionType
import com.secretmafia.game.GamePhase
import com.secretmafia.game.GameRules
import com.secretmafia.game.GameState
import com.secretmafia.game.GameViewModel
import com.secretmafia.game.MatchReward
import com.secretmafia.game.NightActionResult
import com.secretmafia.game.PassKind
import com.secretmafia.game.Player
import com.secretmafia.game.Progress
import com.secretmafia.game.Role
import com.secretmafia.game.Wallet
import com.secretmafia.game.WhoreAlign
import com.secretmafia.game.Winner
import com.secretmafia.ui.Feedback
import com.secretmafia.ui.Narrator
import com.secretmafia.ui.NarratorLine
import com.secretmafia.ui.components.CoinLine
import com.secretmafia.ui.components.DrainBar
import com.secretmafia.ui.components.GameBar
import com.secretmafia.ui.components.HoldUnlock
import com.secretmafia.ui.components.PixelButton
import com.secretmafia.ui.components.PixelScreen
import com.secretmafia.ui.components.PixelText
import com.secretmafia.ui.components.RoleHelpButton
import com.secretmafia.ui.components.RolePortrait
import com.secretmafia.ui.components.RoleReveal
import com.secretmafia.ui.components.VSpace
import com.secretmafia.ui.i18n.Str
import com.secretmafia.ui.theme.ProvideAppStyle
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    vm: GameViewModel,
    state: GameState,
    wallet: Wallet,
    onQuit: () -> Unit,
    onNarratorChange: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val cue = Narrator.cueKey(state)
    var lastCue by remember { mutableStateOf<String?>(null) }
    DisposableEffect(Unit) {
        onDispose { Narrator.stop() }
    }
    LaunchedEffect(cue, state.settings.narratorEnabled) {
        if (!state.settings.narratorEnabled) {
            Narrator.stop()
            lastCue = null
            return@LaunchedEffect
        }
        if (cue == null || cue == lastCue) return@LaunchedEffect
        lastCue = cue
        Narrator.lineFor(state)?.let { Narrator.play(context, it) }
    }
    var confirmQuit by remember { mutableStateOf(false) }
    BackHandler {
        if (confirmQuit) confirmQuit = false else confirmQuit = true
    }
    ProvideAppStyle(settings = state.settings, inGame = true) {
        val c = pal()
        Box(Modifier.fillMaxSize().background(c.bg)) {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(1f)) {
                    when (val phase = state.phase) {
                        GamePhase.MafiaConfer -> MafiaConferPane(state) { vm.finishMafiaConfer() }
                        is GamePhase.Handoff -> HandoffPane(state, phase) { vm.finishHandoff() }
                        is GamePhase.Unlock -> UnlockPane(state, phase) { vm.unlock() }
                        is GamePhase.Action -> ActionPane(vm, state, phase)
                        is GamePhase.RevealWait -> RevealPane(state) { vm.finishReveal() }
                        GamePhase.NightSummary -> SummaryPane(
                            state = state,
                            night = true,
                            onContinue = vm::continueFromNightSummary,
                        )
                        GamePhase.Discuss -> DiscussPane(state, vm::continueFromDiscuss)
                        GamePhase.DayVoteLive -> DayVoteLivePane(state, onPick = vm::submitLiveExile)
                        GamePhase.DaySummary -> SummaryPane(
                            state = state,
                            night = false,
                            onContinue = vm::continueFromDaySummary,
                        )
                        is GamePhase.GameOver -> GameOverPane(
                            state = state,
                            winner = phase.winner,
                            wallet = wallet,
                        )
                    }
                }
                GameBar(
                    narratorOn = state.settings.narratorEnabled,
                    onNarrator = { onNarratorChange(!state.settings.narratorEnabled) },
                    onHome = { confirmQuit = true },
                )
            }
            if (confirmQuit) {
                LeaveConfirm(
                    onStay = { confirmQuit = false },
                    onLeave = onQuit,
                )
            }
        }
    }
}

private fun passLabel(s: Str, state: GameState, kind: PassKind) = when (kind) {
    PassKind.NIGHT -> s.nightN(state.nightNumber)
    PassKind.DAY_VOTE -> s.dayVoteTitle
    PassKind.HUNTER -> s.roleTitle(Role.HUNTER)
}

@Composable
private fun LeaveConfirm(onStay: () -> Unit, onLeave: () -> Unit) {
    val s = str()
    val c = pal()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            )
            .padding(horizontal = 22.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PixelText(s.leaveTitle, size = 26, bold = true)
            VSpace(12.dp)
            PixelText(s.leaveHint, size = 14)
            VSpace(28.dp)
            PixelButton(s.leave, accent = true, onClick = onLeave)
            VSpace(8.dp)
            PixelButton(s.stay, onClick = onStay)
        }
    }
}

@Composable
private fun HandoffPane(
    state: GameState,
    phase: GamePhase.Handoff,
    onDone: () -> Unit,
) {
    val player = state.players.first { it.id == phase.playerId }
    val context = LocalContext.current
    val s = str()
    val c = pal()
    var count by remember(phase.playerId, phase.kind) { mutableIntStateOf(3) }
    LaunchedEffect(phase.playerId, phase.kind) {
        count = 3
        while (count > 0) {
            Feedback.tick(context, state.settings.vibrationEnabled)
            delay(900)
            count--
        }
        onDone()
    }
    PixelScreen {
        PixelText(passLabel(s, state, phase.kind), size = 16)
        VSpace(28.dp)
        PixelText(s.passTo, size = 18)
        VSpace(8.dp)
        PixelText(player.name.uppercase(), size = 40, bold = true)
        VSpace(36.dp)
        PixelText(if (count > 0) count.toString() else "", size = 72, bold = true, color = c.accent)
    }
}

@Composable
private fun MafiaConferPane(state: GameState, onDone: () -> Unit) {
    val s = str()
    val c = pal()
    val context = LocalContext.current
    val skipTalk = remember { java.util.concurrent.atomic.AtomicBoolean(false) }
    var beat by remember { mutableStateOf(ConferBeat.SLEEP) }
    var left by remember { mutableIntStateOf(state.settings.mafiaConferSeconds) }

    LaunchedEffect(Unit) {
        skipTalk.set(false)
        suspend fun speak(line: NarratorLine) {
            if (state.settings.narratorEnabled) Narrator.await(context, line) else delay(1100)
        }
        beat = ConferBeat.SLEEP
        speak(NarratorLine.SLEEP)
        beat = ConferBeat.MAFIA_WAKE
        speak(NarratorLine.MAFIA_WAKEUP)
        beat = ConferBeat.CONFER
        val totalMs = state.settings.mafiaConferSeconds * 1000L
        val started = System.currentTimeMillis()
        while (!skipTalk.get()) {
            val remain = totalMs - (System.currentTimeMillis() - started)
            if (remain <= 0) break
            left = ((remain + 999) / 1000).toInt().coerceAtLeast(1)
            delay(100)
        }
        left = 0
        beat = ConferBeat.MAFIA_SLEEP
        speak(NarratorLine.MAFIA_SLEEP)
        beat = ConferBeat.PAUSE
        delay(3000)
        beat = ConferBeat.WAKE
        speak(NarratorLine.WAKEUP)
        onDone()
    }

    val title = when (beat) {
        ConferBeat.SLEEP -> s.everyoneSleep
        ConferBeat.MAFIA_WAKE, ConferBeat.CONFER -> s.mafiaWake
        ConferBeat.MAFIA_SLEEP, ConferBeat.PAUSE -> s.mafiaSleep
        ConferBeat.WAKE -> s.everyoneWake
    }
    val fx = when (beat) {
        ConferBeat.SLEEP, ConferBeat.MAFIA_SLEEP, ConferBeat.PAUSE ->
            NightActionResult(ActionAnimation.SLEEP, actorRole = Role.CIVILIAN)
        ConferBeat.MAFIA_WAKE, ConferBeat.CONFER ->
            NightActionResult(ActionAnimation.VOTE, actorRole = Role.MAFIA)
        ConferBeat.WAKE ->
            NightActionResult(ActionAnimation.LIKE, actorRole = Role.CIVILIAN)
    }
    PixelScreen {
        PixelText(s.nightN(state.nightNumber), size = 16)
        VSpace(16.dp)
        androidx.compose.runtime.key(beat) { RoleReveal(fx, s) }
        PixelText(title, size = 28, bold = true)
        if (beat == ConferBeat.CONFER) {
            VSpace(10.dp)
            PixelText(s.mafiaTalkHint, size = 16)
            VSpace(16.dp)
            PixelText(s.mafiaTalk, size = 18)
            PixelText(
                left.toString(),
                size = 48,
                bold = true,
                color = if (left <= 3) c.accent else c.fg,
            )
            VSpace(16.dp)
            PixelButton(s.skip) { skipTalk.set(true) }
        }
    }
}

private enum class ConferBeat { SLEEP, MAFIA_WAKE, CONFER, MAFIA_SLEEP, PAUSE, WAKE }

@Composable
private fun UnlockPane(state: GameState, phase: GamePhase.Unlock, onUnlock: () -> Unit) {
    val player = state.players.first { it.id == phase.playerId }
    val context = LocalContext.current
    val s = str()
    PixelScreen {
        PixelText(passLabel(s, state, phase.kind), size = 16)
        VSpace(20.dp)
        PixelText(player.name.uppercase(), size = 36, bold = true)
        VSpace(12.dp)
        PixelText(s.nobodyLooks, size = 14)
        VSpace(40.dp)
        HoldUnlock(s.holdUnlock) {
            Feedback.unlock(context, state.settings.vibrationEnabled)
            onUnlock()
        }
    }
}

@Composable
private fun ActionPane(vm: GameViewModel, state: GameState, phase: GamePhase.Action) {
    val actor = state.players.first { it.id == phase.playerId }
    val s = str()
    when (phase.kind) {
        PassKind.DAY_VOTE -> PickList(s.vote, actor.name, s.whoExile, state.players.filter { it.alive }) {
            vm.submitNightTarget(it)
        }
        PassKind.HUNTER -> PickList(
            s.roleTitle(Role.HUNTER), actor.name, s.hunterHint, state.players.filter { it.alive },
            role = Role.HUNTER,
        ) {
            vm.submitNightTarget(it)
        }
        PassKind.NIGHT -> when {
            actor.role == Role.WHORE && actor.align == null -> WhoreAlignPane(vm, actor)
            vm.usesDummy(actor) -> DummyPane(vm, state, actor)
            state.lawyerPickingShield -> LawyerShield(vm, actor)
            state.framerPicking -> FramePane(vm, state, actor)
            actor.role == Role.WHORE -> WhoreSleepPane(vm, state, actor)
            actor.role?.isMafiaVoter == true -> MafiaAction(vm, state, actor)
            actor.role == Role.HEALER -> ProtectAction(vm, state, actor, s.roleTitle(Role.HEALER), pal().heal)
            actor.role == Role.BODYGUARD -> ProtectAction(vm, state, actor, s.roleTitle(Role.BODYGUARD), pal().fg)
            actor.role == Role.COP -> PickList(
                s.roleTitle(Role.COP), actor.name, s.whoInspect, vm.nightTargets(actor),
                role = Role.COP,
            ) { vm.submitNightTarget(it) }
            actor.role == Role.SEER -> PickList(
                s.roleTitle(Role.SEER), actor.name, s.whoInspect, vm.nightTargets(actor),
                role = Role.SEER,
            ) { vm.submitNightTarget(it) }
            actor.role == Role.NECROMANCER -> PickList(
                s.roleTitle(Role.NECROMANCER), actor.name, s.whoGrave, vm.nightTargets(actor),
                role = Role.NECROMANCER,
            ) { vm.submitNightTarget(it) }
            actor.role == Role.AMNESIAC -> PickList(
                s.roleTitle(Role.AMNESIAC), actor.name, s.whoBecome, vm.nightTargets(actor),
                role = Role.AMNESIAC,
                skipLabel = s.skipTonight,
                onSkip = vm::submitSkipNight,
            ) { vm.submitNightTarget(it) }
            actor.role == Role.MAYOR -> MayorPane(vm, actor)
            actor.role == Role.VIGILANTE -> PickList(
                s.roleTitle(Role.VIGILANTE), actor.name, s.whoShoot, vm.nightTargets(actor), pal().accent,
                role = Role.VIGILANTE,
                skipLabel = s.keepShot,
                onSkip = vm::submitSkipNight,
            ) { vm.submitNightTarget(it) }
            actor.role == Role.POISONER -> PickList(
                s.roleTitle(Role.POISONER), actor.name, s.whoPoison, vm.nightTargets(actor), pal().accent,
                role = Role.POISONER,
                skipLabel = s.skipTonight,
                onSkip = vm::submitSkipNight,
            ) { vm.submitNightTarget(it) }
            actor.role == Role.KILLER && GameRules.killerActsTonight(state.nightNumber) ->
                PickList(
                    s.roleTitle(Role.KILLER), actor.name, s.whoDies, vm.nightTargets(actor), pal().accent,
                    role = Role.KILLER,
                ) { vm.submitNightTarget(it) }
            else -> DummyPane(vm, state, actor)
        }
    }
}

@Composable
private fun PickList(
    title: String,
    name: String,
    prompt: String,
    targets: List<Player>,
    titleColor: Color = Color.Unspecified,
    role: Role? = null,
    skipLabel: String? = null,
    onSkip: (() -> Unit)? = null,
    onPick: (String) -> Unit,
) {
    PixelScreen(scroll = true) {
        PixelText(title, size = 28, bold = true, color = titleColor)
        PixelText(name.uppercase(), size = 16)
        if (role != null) {
            VSpace(10.dp)
            RoleHelpButton(role)
        }
        VSpace()
        PixelText(prompt, size = 16)
        VSpace(10.dp)
        targets.forEach { t ->
            PixelButton(t.name) { onPick(t.id) }
            VSpace(8.dp)
        }
        if (skipLabel != null && onSkip != null) {
            VSpace(8.dp)
            PixelButton(skipLabel, onClick = onSkip)
        }
    }
}

@Composable
private fun MafiaAction(vm: GameViewModel, state: GameState, actor: Player) {
    val s = str()
    val c = pal()
    val mates = vm.livingMafiaBesides(actor.id)
    val previous = vm.mafiaBefore(actor.id)
    val showCounts = state.settings.showMafiaVoteCount && previous.isNotEmpty()
    val preview = if (showCounts && vm.isLastMafia(actor.id)) vm.currentVictimPreview() else null
    val counts = if (showCounts) GameRules.mafiaVoteCounts(state.players, state.mafiaVotes) else emptyList()
    PixelScreen(scroll = true) {
        PixelText(s.roleTitle(actor.role), size = 28, bold = true, color = c.accent)
        PixelText(actor.name.uppercase(), size = 16)
        actor.role?.let {
            VSpace(10.dp)
            RoleHelpButton(it)
        }
        VSpace(12.dp)
        vm.twinOf(actor.id)?.let { twin ->
            PixelText(s.yourTwin, size = 13)
            PixelText(twin.name.uppercase(), size = 16, bold = true)
            VSpace(8.dp)
        }
        if (mates.isNotEmpty()) {
            PixelText(s.yourCrew, size = 13)
            PixelText(mates.joinToString("  ·  ") { it.name.uppercase() }, size = 16, bold = true)
            VSpace()
        }
        counts.forEach { (name, n) ->
            PixelText("$n ${s.votedKill} ${name.uppercase()}", size = 14)
        }
        if (preview != null) {
            VSpace(8.dp)
            PixelText(s.currentKill, size = 13, color = c.accent)
            PixelText(preview.name.uppercase(), size = 22, bold = true, color = c.accent)
        }
        VSpace()
        PixelText(s.whoDies, size = 16)
        VSpace(10.dp)
        vm.nightTargets(actor).forEach { t ->
            PixelButton(t.name) { vm.submitNightTarget(t.id) }
            VSpace(8.dp)
        }
    }
}

@Composable
private fun LawyerShield(vm: GameViewModel, actor: Player) {
    val s = str()
    val mates = vm.livingMafiaBesides(actor.id) + actor
    PixelScreen(scroll = true) {
        PixelText(s.roleTitle(Role.LAWYER), size = 28, bold = true)
        VSpace(10.dp)
        RoleHelpButton(Role.LAWYER)
        VSpace()
        PixelText(s.shieldHint, size = 15)
        VSpace(12.dp)
        mates.distinctBy { it.id }.forEach { p ->
            PixelButton(p.name) { vm.submitLawyerShield(p.id) }
            VSpace(8.dp)
        }
        VSpace(8.dp)
        PixelButton(s.skipShield) { vm.submitLawyerShield(null) }
    }
}

@Composable
private fun FramePane(vm: GameViewModel, state: GameState, actor: Player) {
    val s = str()
    PixelScreen(scroll = true) {
        PixelText(s.roleTitle(Role.FRAMER), size = 28, bold = true)
        PixelText(actor.name.uppercase(), size = 16)
        VSpace(10.dp)
        RoleHelpButton(Role.FRAMER)
        VSpace()
        PixelText(s.whoFrame, size = 15)
        VSpace(12.dp)
        state.players.filter { it.alive && it.id != actor.id }.forEach { p ->
            PixelButton(p.name) { vm.submitFrame(p.id) }
            VSpace(8.dp)
        }
        VSpace(8.dp)
        PixelButton(s.skipFrame) { vm.submitFrame(null) }
    }
}

@Composable
private fun MayorPane(vm: GameViewModel, actor: Player) {
    val s = str()
    PixelScreen(scroll = true) {
        PixelText(s.roleTitle(Role.MAYOR), size = 28, bold = true)
        PixelText(actor.name.uppercase(), size = 16)
        VSpace(10.dp)
        RolePortrait(Role.MAYOR, size = 96.dp)
        VSpace(10.dp)
        RoleHelpButton(Role.MAYOR)
        VSpace()
        PixelText(s.mayorHint, size = 15)
        VSpace(12.dp)
        PixelButton(s.revealNow) { vm.submitMayorReveal() }
        VSpace(8.dp)
        PixelButton(s.stayHidden) { vm.submitSkipNight() }
    }
}

@Composable
private fun ProtectAction(
    vm: GameViewModel,
    state: GameState,
    actor: Player,
    title: String,
    titleColor: Color,
) {
    val s = str()
    val blockedId = if (actor.role == Role.HEALER) state.lastHealTargetId else state.lastBodyguardTargetId
    PixelScreen(scroll = true) {
        PixelText(title, size = 28, bold = true, color = titleColor)
        PixelText(actor.name.uppercase(), size = 16)
        actor.role?.let {
            VSpace(10.dp)
            RoleHelpButton(it)
        }
        VSpace()
        PixelText(s.whoProtect, size = 16)
        if (!state.settings.healerMayRepeatTarget && blockedId != null) {
            val blocked = state.players.firstOrNull { it.id == blockedId }?.name
            if (blocked != null) {
                VSpace(6.dp)
                PixelText("$blocked — ${s.notAgain}", size = 13)
            }
        }
        VSpace(10.dp)
        vm.nightTargets(actor).forEach { t ->
            PixelButton(t.name) { vm.submitNightTarget(t.id) }
            VSpace(8.dp)
        }
    }
}

@Composable
private fun WhoreAlignPane(vm: GameViewModel, actor: Player) {
    val s = str()
    PixelScreen(scroll = true) {
        PixelText(s.roleTitle(Role.WHORE), size = 28, bold = true)
        PixelText(actor.name.uppercase(), size = 16)
        VSpace(10.dp)
        RolePortrait(Role.WHORE, size = 96.dp)
        VSpace(10.dp)
        RoleHelpButton(Role.WHORE)
        VSpace()
        PixelText(s.pickSide, size = 16)
        VSpace(12.dp)
        PixelButton(s.sideGood) { vm.submitWhoreAlign(WhoreAlign.GOOD) }
        VSpace(8.dp)
        PixelButton(s.sideEvil) { vm.submitWhoreAlign(WhoreAlign.EVIL) }
        VSpace(8.dp)
        PixelButton(s.sidePest) { vm.submitWhoreAlign(WhoreAlign.PEST) }
        VSpace(8.dp)
        PixelButton(s.sideSolo) { vm.submitWhoreAlign(WhoreAlign.SOLO) }
    }
}

@Composable
private fun WhoreSleepPane(vm: GameViewModel, state: GameState, actor: Player) {
    val s = str()
    PixelScreen(scroll = true) {
        PixelText(s.roleTitle(Role.WHORE), size = 28, bold = true)
        PixelText(actor.name.uppercase(), size = 16)
        VSpace(10.dp)
        RoleHelpButton(Role.WHORE)
        VSpace()
        PixelText(s.whoSleep, size = 16)
        state.lastWhoreTargetId?.let { lastId ->
            val blocked = state.players.firstOrNull { it.id == lastId }?.name
            if (blocked != null) {
                VSpace(6.dp)
                PixelText("$blocked — ${s.notLastNight}", size = 13)
            }
        }
        VSpace(10.dp)
        vm.nightTargets(actor).forEach { t ->
            PixelButton(t.name) { vm.submitNightTarget(t.id) }
            VSpace(8.dp)
        }
    }
}

@Composable
private fun DummyPane(vm: GameViewModel, state: GameState, actor: Player) {
    val s = str()
    val shown = GameRules.shownRole(actor, state.nightNumber)
    val title = if (actor.role == Role.DRUNK && state.nightNumber < 3) s.unknownRole else s.roleTitle(shown)
    val dummy = state.dummyOverride ?: DummyActionType.LIKE
    if (dummy == DummyActionType.MATH) {
        val puzzle = remember { GameRules.mathPuzzle() }
        PixelScreen {
            PixelText(title, size = 28, bold = true)
            shown?.let {
                VSpace(10.dp)
                RolePortrait(it, size = 96.dp, female = actor.femaleArt)
                VSpace(10.dp)
                RoleHelpButton(it)
            }
            if (actor.role == Role.DRUNK && state.nightNumber < 3) {
                VSpace(8.dp)
                PixelText(s.tooDrunk, size = 14)
            }
            vm.twinOf(actor.id)?.let { twin ->
                VSpace(8.dp)
                PixelText("${s.yourTwin}: ${twin.name.uppercase()}", size = 14)
            }
            VSpace()
            PixelText(s.solveThis, size = 16)
            VSpace(16.dp)
            PixelText(puzzle.prompt, size = 42, bold = true)
            VSpace(20.dp)
            puzzle.options.forEach { option ->
                PixelButton(option.toString()) { vm.submitDummyMath() }
                VSpace(8.dp)
            }
        }
    } else {
        PixelScreen(scroll = true) {
            PixelText(title, size = 28, bold = true)
            shown?.let {
                VSpace(10.dp)
                RolePortrait(it, size = 96.dp, female = actor.femaleArt)
                VSpace(10.dp)
                RoleHelpButton(it)
            }
            if (actor.role == Role.DRUNK && state.nightNumber < 3) {
                VSpace(8.dp)
                PixelText(s.tooDrunk, size = 14)
            }
            vm.twinOf(actor.id)?.let { twin ->
                VSpace(8.dp)
                PixelText("${s.yourTwin}: ${twin.name.uppercase()}", size = 14)
            }
            VSpace()
            PixelText(s.sendLike, size = 16)
            VSpace(10.dp)
            state.players.filter { it.alive }.forEach { p ->
                PixelButton(p.name) { vm.submitDummyLike() }
                VSpace(8.dp)
            }
        }
    }
}

@Composable
private fun RevealPane(state: GameState, onDone: () -> Unit) {
    val s = str()
    val c = pal()
    val action = state.lastAction
    val bar = when (action?.animation) {
        ActionAnimation.KNIFE, ActionAnimation.HUNT, ActionAnimation.POISON -> c.accent
        ActionAnimation.HEAL, ActionAnimation.GUARD, ActionAnimation.SHIELD -> c.heal
        else -> c.fg
    }
    PixelScreen {
        if (action != null) {
            action.inspectName?.takeIf { it.isNotBlank() }?.let {
                PixelText(it.uppercase(), size = 18)
                VSpace(8.dp)
            }
            RoleReveal(action, s)
        } else {
            PixelText("...", size = 24)
        }
        VSpace(28.dp)
        DrainBar(color = bar, onFinished = onDone)
    }
}

@Composable
private fun ActionBurst(
    mark: String,
    extras: List<String>,
    color: Color,
    markSize: Int = 64,
    tilt: Boolean = false,
) {
    val pulse = rememberInfiniteTransition(label = "burst")
    val scale by pulse.animateFloat(
        initialValue = 0.90f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(tween(260), RepeatMode.Reverse),
        label = "s",
    )
    val bob by pulse.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(320), RepeatMode.Reverse),
        label = "bob",
    )
    val bob2 by pulse.animateFloat(
        initialValue = 10f,
        targetValue = -14f,
        animationSpec = infiniteRepeatable(tween(420), RepeatMode.Reverse),
        label = "bob2",
    )
    val spin by pulse.animateFloat(
        initialValue = -11f,
        targetValue = 11f,
        animationSpec = infiniteRepeatable(tween(280), RepeatMode.Reverse),
        label = "spin",
    )
    val spots = listOf(-72f to -44f, 78f to -28f, 36f to 52f, -48f to 40f)
    Box(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .graphicsLayer { rotationZ = if (tilt) spin else 0f },
        contentAlignment = Alignment.Center,
    ) {
        PixelText(
            mark,
            size = (markSize * scale).toInt().coerceAtLeast(28),
            bold = true,
            color = color,
        )
        extras.forEachIndexed { i, glyph ->
            val (x, y) = spots[i % spots.size]
            val lift = if (i % 2 == 0) bob else bob2
            PixelText(
                glyph,
                size = 22 + (i % 3) * 6,
                bold = true,
                color = color,
                modifier = Modifier.offset(x = x.dp, y = (y + lift).dp),
            )
        }
    }
}

@Composable
private fun SummaryPane(
    state: GameState,
    night: Boolean,
    onContinue: () -> Unit,
) {
    val s = str()
    val c = pal()
    val context = LocalContext.current
    val deaths = if (night) state.lastNightDeaths else state.lastDayDeaths
    LaunchedEffect(deaths) {
        if (deaths.any { it.cause != com.secretmafia.game.DeathCause.BLOCKED }) {
            Feedback.death(context, state.settings.vibrationEnabled)
        }
    }
    PixelScreen(scroll = true) {
        PixelText(if (night) s.nightN(state.nightNumber) else s.dayN(state.nightNumber), size = 16)
        VSpace(20.dp)
        PixelText(if (night) s.summary else s.exile, size = 28, bold = true)
        VSpace(16.dp)
        if (deaths.isEmpty()) {
            PixelText(if (night) s.nobodyDied else s.nobodyExiled, size = 22, bold = true)
        } else {
            deaths.forEach { d ->
                DeathLine(s, d, state.settings.revealRoleOnDeath, c.accent)
                VSpace(10.dp)
            }
        }
        VSpace(32.dp)
        HoldUnlock(if (night) s.holdContinue else s.holdNight, onUnlocked = onContinue)
    }
}

@Composable
private fun DeathLine(s: Str, d: DeathRecord, reveal: Boolean, accent: Color) {
    PixelText(s.deathLine(d.playerName, d.cause), size = 20, bold = true, color = accent)
    if (reveal && d.role != null) {
        PixelText("${s.roles}: ${s.roleTitle(d.role)}", size = 15)
    }
}

@Composable
private fun DiscussPane(
    state: GameState,
    onContinue: () -> Unit,
) {
    val s = str()
    val c = pal()
    val context = LocalContext.current
    val minutes = state.settings.discussTimerMinutes
    var left by remember { mutableIntStateOf(minutes * 60) }
    LaunchedEffect(minutes) {
        if (minutes <= 0) return@LaunchedEffect
        left = minutes * 60
        while (left > 0) {
            delay(1000)
            left--
        }
        Feedback.alarm(
            context,
            sound = state.settings.soundEnabled && !state.settings.narratorEnabled,
            vibrate = state.settings.vibrationEnabled,
        )
        if (state.settings.narratorEnabled) {
            Narrator.play(context, NarratorLine.DISCUSS_OVER)
        }
    }
    PixelScreen {
        PixelText(s.dayN(state.nightNumber), size = 16)
        VSpace(24.dp)
        PixelText(s.discuss, size = 48, bold = true)
        VSpace(12.dp)
        PixelText(s.discussHint, size = 16)
        if (minutes > 0) {
            VSpace(20.dp)
            PixelText("%d:%02d".format(left / 60, left % 60), size = 36, bold = true, color = if (left == 0) c.accent else c.fg)
        }
        VSpace(40.dp)
        HoldUnlock(s.holdVote, onUnlocked = onContinue)
    }
}

@Composable
private fun DayVoteLivePane(state: GameState, onPick: (String?) -> Unit) {
    val s = str()
    PixelScreen(scroll = true) {
        PixelText(s.liveVote, size = 28, bold = true)
        VSpace(8.dp)
        PixelText(s.liveHint, size = 14)
        if (state.mayorRevealed) {
            VSpace(8.dp)
            PixelText(s.mayorLiveHint, size = 14)
        }
        VSpace()
        state.players.filter { it.alive }.forEach { p ->
            PixelButton(p.name) { onPick(p.id) }
            VSpace(8.dp)
        }
        VSpace(8.dp)
        PixelButton(s.nobody) { onPick(null) }
    }
}

@Composable
private fun GameOverPane(
    state: GameState,
    winner: Winner,
    wallet: Wallet,
) {
    val s = str()
    val c = pal()
    PixelScreen(scroll = true) {
        PixelText(s.gameOver, size = 18)
        VSpace(16.dp)
        PixelText(
            s.winnerTitle(winner),
            size = 32,
            bold = true,
            color = if (winner == Winner.MAFIA || winner == Winner.KILLER) c.accent else c.fg,
        )
        VSpace(24.dp)
        state.players.forEach { p ->
            val mark = if (p.alive) "" else "  [X]"
            val line = if (state.settings.revealRolesAtEnd) {
                "${p.name.uppercase()}  —  ${s.roleTitle(p.role)}$mark"
            } else {
                "${p.name.uppercase()}$mark"
            }
            PixelText(line, size = 15, align = TextAlign.Start)
            VSpace(6.dp)
        }
        VSpace(28.dp)
        when (val reward = state.matchReward) {
            MatchReward.TOO_FAST -> PixelText(s.tooFast, size = 15, color = c.accent)
            MatchReward.BLOOD, MatchReward.TOWN, MatchReward.GOLD -> {
                val kind = Progress.coinFrom(reward)!!
                PixelText(s.matchCoin(Progress.MATCH_REWARD, s.coinName(kind)), size = 15)
            }
            null -> Unit
        }
        if (state.survivorLived) {
            VSpace(8.dp)
            PixelText(s.survivorBonus(s.coinName(CoinKind.GOLD)), size = 15)
        }
        VSpace(10.dp)
        CoinLine(wallet)
    }
}
