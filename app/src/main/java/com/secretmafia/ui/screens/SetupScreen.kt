package com.secretmafia.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.secretmafia.game.GameRules
import com.secretmafia.game.Role
import com.secretmafia.game.RoleCounts
import com.secretmafia.ui.components.PixelButton
import com.secretmafia.ui.components.PixelScreen
import com.secretmafia.ui.components.PixelText
import com.secretmafia.ui.components.Stepper
import com.secretmafia.ui.components.VSpace
import com.secretmafia.ui.theme.PixelFamily
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str

@Composable
fun SetupScreen(
    counts: RoleCounts,
    onCounts: (RoleCounts) -> Unit,
    onPlayerCount: (Int) -> Unit,
    onStart: (names: List<String>, counts: RoleCounts) -> Unit,
    onAdvanced: () -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    val c = pal()
    var names by remember { mutableStateOf(MutableList(6) { "" }) }

    LaunchedEffect(names.size) {
        onPlayerCount(names.size)
        val rec = GameRules.recommendedCounts(names.size)
        val advanced = Role.entries.filter { !it.core }.associateWith { counts[it] }
        var next = rec
        advanced.forEach { (role, n) ->
            if (n > 0) next = next.with(role, n).with(Role.CIVILIAN, (next[Role.CIVILIAN] - n).coerceAtLeast(0))
        }
        onCounts(next)
    }

    val filled = names.map { it.trim() }.filter { it.isNotEmpty() }
    val unique = filled.distinctBy { it.lowercase() }
    val namesOk = filled.size == names.size &&
        names.size >= GameRules.MIN_PLAYERS &&
        unique.size == names.size
    val rolesOk = counts.total == names.size &&
        GameRules.isTwinCountOk(counts[Role.TWIN_CIVIL]) &&
        GameRules.isTwinCountOk(counts[Role.TWIN_MAFIA])
    val canStart = namesOk && rolesOk

    PixelScreen(scroll = true) {
        PixelText(s.newGame, size = 28, bold = true)
        VSpace(8.dp)
        PixelText(s.hostHint, size = 13)
        VSpace()

        names.forEachIndexed { index, value ->
            NameField(
                label = if (index == 0) s.hostYou else s.seatN(index + 1),
                value = value,
                placeholder = s.name,
                onValue = { next -> names = names.toMutableList().also { it[index] = next } },
            )
            VSpace(8.dp)
        }

        PixelButton(s.addPlayer, enabled = names.size < GameRules.MAX_PLAYERS) {
            names = names.toMutableList().also { it.add("") }
        }
        VSpace(8.dp)
        PixelButton(s.removeLast, enabled = names.size > GameRules.MIN_PLAYERS) {
            names = names.toMutableList().also { it.removeAt(it.lastIndex) }
        }

        VSpace(22.dp)
        PixelText(s.roles, size = 22, bold = true)
        PixelText(s.recommendedFor(names.size), size = 13)
        VSpace(10.dp)
        CoreStepper(Role.MAFIA, counts, onCounts)
        VSpace(8.dp)
        CoreStepper(Role.HEALER, counts, onCounts)
        VSpace(8.dp)
        CoreStepper(Role.COP, counts, onCounts)
        VSpace(8.dp)
        CoreStepper(Role.CIVILIAN, counts, onCounts)
        VSpace(10.dp)
        PixelButton("${s.advanced}  (${counts.advancedTotal})", onClick = onAdvanced)
        VSpace(8.dp)
        PixelButton(s.resetRec) { onCounts(GameRules.recommendedCounts(names.size)) }

        if (counts.total != names.size) {
            VSpace(8.dp)
            PixelText(s.roleMismatch, size = 14, color = c.accent)
        }
        EvilBalanceHint(counts, names.size)

        VSpace(22.dp)
        PixelButton(
            label = if (GameRules.tooManyEvil(counts) && rolesOk) s.startAnyway else s.start,
            enabled = canStart,
            accent = GameRules.tooManyEvil(counts) && rolesOk,
        ) {
            onStart(names.map { it.trim() }, counts)
        }
        VSpace(8.dp)
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
private fun EvilBalanceHint(counts: RoleCounts, playerCount: Int) {
    if (!GameRules.tooManyEvil(counts)) return
    val s = str()
    val c = pal()
    VSpace(8.dp)
    PixelText(
        s.tooManyEvil(playerCount, counts.evil, counts.good, GameRules.maxEvilFor(playerCount)),
        size = 14,
        color = c.accent,
    )
}

@Composable
private fun CoreStepper(role: Role, counts: RoleCounts, onChange: (RoleCounts) -> Unit) {
    val s = str()
    Stepper(s.roleTitle(role), counts[role]) { onChange(counts.with(role, it)) }
}

@Composable
fun AdvancedRolesScreen(
    initial: RoleCounts,
    playerCount: Int,
    onDone: (RoleCounts) -> Unit,
) {
    val s = str()
    var counts by remember { mutableStateOf(initial) }

    fun bump(role: Role, next: Int) {
        val old = counts[role]
        val delta = next - old
        var n = counts.with(role, next)
        n = n.with(Role.CIVILIAN, (n[Role.CIVILIAN] - delta).coerceAtLeast(0))
        counts = n
    }

    PixelScreen(scroll = true) {
        PixelText(s.advanced, size = 26, bold = true)
        VSpace(8.dp)
        PixelText(s.noneSelected.takeIf { counts.advancedTotal == 0 } ?: "", size = 13)
        VSpace(12.dp)

        PixelText(s.goodRoles, size = 14)
        VSpace(8.dp)
        Adv(Role.HUNTER, counts, 0, 2) { bump(Role.HUNTER, it) }
        VSpace(8.dp)
        Adv(Role.SEER, counts, 0, 1) { bump(Role.SEER, it) }
        VSpace(8.dp)
        Adv(Role.BODYGUARD, counts, 0, 1) { bump(Role.BODYGUARD, it) }
        VSpace(8.dp)
        TwinStepper(Role.TWIN_CIVIL, counts) { bump(Role.TWIN_CIVIL, it) }

        VSpace(16.dp)
        PixelText(s.evilRoles, size = 14)
        VSpace(8.dp)
        Adv(Role.DON, counts, 0, 1) { bump(Role.DON, it) }
        VSpace(8.dp)
        Adv(Role.LAWYER, counts, 0, 1) { bump(Role.LAWYER, it) }
        VSpace(8.dp)
        TwinStepper(Role.TWIN_MAFIA, counts) { bump(Role.TWIN_MAFIA, it) }

        VSpace(16.dp)
        PixelText(s.wildRoles, size = 14)
        VSpace(8.dp)
        Adv(Role.JOKER, counts, 0, 1) { bump(Role.JOKER, it) }
        VSpace(8.dp)
        Adv(Role.KILLER, counts, 0, 1) { bump(Role.KILLER, it) }
        VSpace(8.dp)
        Adv(Role.LUNATIC, counts, 0, 1) { bump(Role.LUNATIC, it) }
        VSpace(8.dp)
        Adv(Role.DRUNK, counts, 0, 1) { bump(Role.DRUNK, it) }

        EvilBalanceHint(counts, playerCount)
        VSpace(22.dp)
        PixelButton(s.back) { onDone(counts) }
    }
}

@Composable
private fun Adv(role: Role, counts: RoleCounts, min: Int, max: Int, onChange: (Int) -> Unit) {
    val s = str()
    Stepper(s.roleTitle(role), counts[role], min = min, max = max, onChange = onChange)
}

@Composable
private fun TwinStepper(role: Role, counts: RoleCounts, onChange: (Int) -> Unit) {
    val s = str()
    val c = pal()
    Stepper(s.roleTitle(role), counts[role], min = 0, max = 2) { raw ->
        onChange(if (raw >= 2) 2 else 0)
    }
    PixelText(s.roleBlurb(role), size = 13, color = c.muted)
    VSpace(8.dp)
}

@Composable
private fun NameField(
    label: String,
    value: String,
    placeholder: String,
    onValue: (String) -> Unit,
) {
    val c = pal()
    Column(Modifier.fillMaxWidth()) {
        PixelText(label, size = 12, align = TextAlign.Start, modifier = Modifier.fillMaxWidth())
        VSpace(4.dp)
        BasicTextField(
            value = value,
            onValueChange = { if (it.length <= 16) onValue(it) },
            singleLine = true,
            cursorBrush = SolidColor(c.fg),
            textStyle = TextStyle(
                color = c.fg,
                fontFamily = PixelFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, c.fg)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    PixelText(placeholder, size = 16, color = c.muted, align = TextAlign.Start)
                }
                inner()
            },
        )
    }
}
