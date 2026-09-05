package com.secretmafia.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.secretmafia.game.ActionAnimation
import com.secretmafia.game.DeathRecord
import com.secretmafia.game.DummyActionType
import com.secretmafia.game.GamePhase
import com.secretmafia.game.GameRules
import com.secretmafia.game.GameState
import com.secretmafia.game.GameViewModel
import com.secretmafia.game.PassKind
import com.secretmafia.game.Player
import com.secretmafia.game.Role
import com.secretmafia.game.Winner
import com.secretmafia.ui.Feedback
import com.secretmafia.ui.components.DrainBar
import com.secretmafia.ui.components.HoldUnlock
import com.secretmafia.ui.components.PixelButton
import com.secretmafia.ui.components.PixelScreen
import com.secretmafia.ui.components.PixelText
import com.secretmafia.ui.components.VSpace
import com.secretmafia.ui.i18n.Str
import com.secretmafia.ui.theme.ProvideAppStyle
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str
import kotlinx.coroutines.delay

@Composable
fun GameScreen(vm: GameViewModel, state: GameState, onQuit: () -> Unit) {
    ProvideAppStyle(settings = state.settings, inGame = true) {
        when (val phase = state.phase) {
            is GamePhase.Handoff -> HandoffPane(state, phase) { vm.finishHandoff() }
            is GamePhase.Unlock -> UnlockPane(state, phase) { vm.unlock() }
            is GamePhase.Action -> ActionPane(vm, state, phase)
            is GamePhase.RevealWait -> RevealPane(state) { vm.finishReveal() }
            GamePhase.NightSummary -> SummaryPane(state, night = true, onContinue = vm::continueFromNightSummary)
            GamePhase.Discuss -> DiscussPane(state, onContinue = vm::continueFromDiscuss)
            GamePhase.DayVoteLive -> DayVoteLivePane(state, onPick = vm::submitLiveExile)
            GamePhase.DaySummary -> SummaryPane(state, night = false, onContinue = vm::continueFromDaySummary)
            is GamePhase.GameOver -> GameOverPane(state, phase.winner, onQuit)
        }
    }
}

private fun passLabel(s: Str, state: GameState, kind: PassKind) = when (kind) {
    PassKind.NIGHT -> s.nightN(state.nightNumber)
    PassKind.DAY_VOTE -> s.dayVoteTitle
    PassKind.HUNTER -> s.roleTitle(Role.HUNTER)
}

@Composable
private fun HandoffPane(state: GameState, phase: GamePhase.Handoff, onDone: () -> Unit) {
    val player = state.players.first { it.id == phase.playerId }
    val context = LocalContext.current
    val s = str()
    val c = pal()
    var count by remember(phase.playerId, phase.kind) { mutableIntStateOf(3) }
    LaunchedEffect(phase.playerId, phase.kind) {
        count = 3
        while (count > 0) {
            Feedback.tick(context)
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
            Feedback.unlock(context)
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
        PassKind.HUNTER -> PickList(s.roleTitle(Role.HUNTER), actor.name, s.hunterHint, state.players.filter { it.alive }) {
            vm.submitNightTarget(it)
        }
        PassKind.NIGHT -> when {
            state.lawyerPickingShield -> LawyerShield(vm, actor)
            actor.role?.isMafiaVoter == true -> MafiaAction(vm, state, actor)
            actor.role == Role.HEALER -> ProtectAction(vm, state, actor, s.roleTitle(Role.HEALER), pal().heal)
            actor.role == Role.BODYGUARD -> ProtectAction(vm, state, actor, s.roleTitle(Role.BODYGUARD), pal().fg)
            actor.role == Role.COP -> PickList(s.roleTitle(Role.COP), actor.name, s.whoInspect, vm.nightTargets(actor)) {
                vm.submitNightTarget(it)
            }
            actor.role == Role.SEER -> PickList(s.roleTitle(Role.SEER), actor.name, s.whoInspect, vm.nightTargets(actor)) {
                vm.submitNightTarget(it)
            }
            actor.role == Role.KILLER && GameRules.killerActsTonight(state.nightNumber) ->
                PickList(s.roleTitle(Role.KILLER), actor.name, s.whoDies, vm.nightTargets(actor), pal().accent) {
                    vm.submitNightTarget(it)
                }
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
    onPick: (String) -> Unit,
) {
    PixelScreen(scroll = true) {
        PixelText(title, size = 28, bold = true, color = titleColor)
        PixelText(name.uppercase(), size = 16)
        VSpace()
        PixelText(prompt, size = 16)
        VSpace(10.dp)
        targets.forEach { t ->
            PixelButton(t.name) { onPick(t.id) }
            VSpace(8.dp)
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
private fun DummyPane(vm: GameViewModel, state: GameState, actor: Player) {
    val s = str()
    val shown = GameRules.shownRole(actor, state.nightNumber)
    val title = if (actor.role == Role.DRUNK && state.nightNumber < 3) s.unknownRole else s.roleTitle(shown)
    val dummy = state.dummyOverride ?: DummyActionType.LIKE
    if (dummy == DummyActionType.MATH) {
        val puzzle = remember { GameRules.mathPuzzle() }
        PixelScreen {
            PixelText(title, size = 28, bold = true)
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
        ActionAnimation.KNIFE, ActionAnimation.HUNT -> c.accent
        ActionAnimation.HEAL, ActionAnimation.GUARD, ActionAnimation.SHIELD -> c.heal
        else -> c.fg
    }
    PixelScreen {
        when (action?.animation) {
            ActionAnimation.KNIFE -> FlashMark("X", c.accent)
            ActionAnimation.HEAL, ActionAnimation.GUARD -> HealBurst(c.heal)
            ActionAnimation.SHIELD -> FlashMark("OK", c.heal)
            ActionAnimation.INSPECT -> {
                PixelText((action.inspectName ?: "").uppercase(), size = 18)
                VSpace(8.dp)
                if (action.inspectGood == true) PixelText(s.good, size = 48, bold = true)
                else PixelText(s.bad, size = 48, bold = true, color = c.accent)
            }
            ActionAnimation.SEER -> {
                PixelText((action.inspectName ?: "").uppercase(), size = 18)
                VSpace(8.dp)
                val role = action.inspectRoleTitle?.let { runCatching { Role.valueOf(it) }.getOrNull() }
                PixelText(s.roleTitle(role), size = 36, bold = true)
            }
            ActionAnimation.LIKE -> FlashMark("<3", c.fg)
            ActionAnimation.MATH -> FlashMark("OK", c.fg)
            ActionAnimation.VOTE, ActionAnimation.HUNT -> FlashMark(s.vote, c.fg)
            null -> PixelText("...", size = 24)
        }
        VSpace(28.dp)
        DrainBar(color = bar, onFinished = onDone)
    }
}

@Composable
private fun FlashMark(text: String, color: Color) {
    val pulse = rememberInfiniteTransition(label = "pulse")
    val scale by pulse.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(280), RepeatMode.Reverse),
        label = "s",
    )
    PixelText(text, size = (64 * scale).toInt().coerceAtLeast(40), bold = true, color = color)
}

@Composable
private fun HealBurst(color: Color) {
    Box(Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
        PixelText("+", size = 72, bold = true, color = color)
        PixelText("+", size = 28, color = color, modifier = Modifier.offset(x = (-70).dp, y = (-40).dp))
        PixelText("+", size = 22, color = color, modifier = Modifier.offset(x = 80.dp, y = (-24).dp))
        PixelText("+", size = 26, color = color, modifier = Modifier.offset(x = 40.dp, y = 50.dp))
    }
}

@Composable
private fun SummaryPane(state: GameState, night: Boolean, onContinue: () -> Unit) {
    val s = str()
    val c = pal()
    val context = LocalContext.current
    val deaths = if (night) state.lastNightDeaths else state.lastDayDeaths
    LaunchedEffect(deaths) {
        if (deaths.any { it.cause != com.secretmafia.game.DeathCause.BLOCKED }) {
            Feedback.death(context)
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
private fun DiscussPane(state: GameState, onContinue: () -> Unit) {
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
        Feedback.alarm(context, state.settings.soundEnabled)
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
private fun GameOverPane(state: GameState, winner: Winner, onQuit: () -> Unit) {
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
        PixelButton(s.mainMenu, onClick = onQuit)
    }
}
