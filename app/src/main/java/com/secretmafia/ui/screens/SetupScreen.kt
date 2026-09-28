package com.secretmafia.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.secretmafia.game.GameRules
import com.secretmafia.game.Role
import com.secretmafia.game.RoleChances
import com.secretmafia.game.RoleCounts
import com.secretmafia.game.Team
import com.secretmafia.game.Wallet
import com.secretmafia.ui.components.BuyRoleDialog
import com.secretmafia.ui.components.MiniBtn
import com.secretmafia.ui.components.PixelButton
import com.secretmafia.ui.components.PixelScreen
import com.secretmafia.ui.components.PixelText
import com.secretmafia.ui.components.SettingRow
import com.secretmafia.ui.components.Stepper
import com.secretmafia.ui.components.VSpace
import com.secretmafia.ui.theme.LocalFont
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str

@Composable
fun SetupScreen(
    counts: RoleCounts,
    onCounts: (RoleCounts) -> Unit,
    chances: RoleChances,
    onChances: (RoleChances) -> Unit,
    advancedOpen: Boolean,
    onAdvancedOpen: (Boolean) -> Unit,
    savedNames: List<String>,
    onNamesChange: (List<String>) -> Unit,
    wallet: Wallet,
    onUnlockRole: (Role) -> Unit,
    narratorEnabled: Boolean,
    onNarratorToggle: () -> Unit,
    onClearSetup: () -> Unit,
    setupRevision: Int,
    onStart: (names: List<String>, counts: RoleCounts, chances: RoleChances) -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    val c = pal()
    var names by remember(setupRevision) { mutableStateOf(savedNames.toMutableList()) }
    var buyRole by remember { mutableStateOf<Role?>(null) }

    LaunchedEffect(names) {
        onNamesChange(names.toList())
    }

    fun setRole(role: Role, next: Int) {
        onCounts(counts.with(role, next))
    }

    val filled = names.map { it.trim() }.filter { it.isNotEmpty() }
    val unique = filled.distinctBy { it.lowercase() }
    val namesOk = filled.size == names.size &&
        names.size >= GameRules.MIN_PLAYERS &&
        unique.size == names.size
    val rolesOk = counts.total == names.size &&
        GameRules.isTwinCountOk(counts[Role.TWIN_CIVIL]) &&
        GameRules.isTwinCountOk(counts[Role.TWIN_MAFIA])
    val balanceOk = !GameRules.tooManyEvil(counts)
    val canStart = namesOk && rolesOk && balanceOk

    buyRole?.let { role ->
        BuyRoleDialog(
            role = role,
            wallet = wallet,
            onUnlock = { onUnlockRole(role) },
            onClose = { buyRole = null },
        )
    }

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
        if (!advancedOpen) {
            PixelText(s.recommendedFor(names.size), size = 13)
        }
        VSpace(10.dp)
        CoreStepper(Role.MAFIA, counts, onCounts)
        VSpace(8.dp)
        CoreStepper(Role.HEALER, counts, onCounts)
        VSpace(8.dp)
        CoreStepper(Role.COP, counts, onCounts)
        VSpace(8.dp)
        CoreStepper(Role.CIVILIAN, counts, onCounts)
        VSpace(10.dp)
        SettingRow(
            s.advanced,
            if (advancedOpen) s.on else s.off,
            s.advancedHint,
        ) { onAdvancedOpen(!advancedOpen) }

        if (advancedOpen) {
            VSpace(14.dp)
            PixelText(s.goodRoles, size = 14)
            VSpace(8.dp)
            AdvSlot(Role.HUNTER, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.SEER, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.BODYGUARD, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.MAYOR, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.NECROMANCER, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.VIGILANTE, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            TwinSlot(Role.TWIN_CIVIL, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })

            VSpace(16.dp)
            PixelText(s.evilRoles, size = 14)
            VSpace(8.dp)
            AdvSlot(Role.DON, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.LAWYER, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.FRAMER, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.TRAITOR, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.POISONER, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            TwinSlot(Role.TWIN_MAFIA, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })

            VSpace(16.dp)
            PixelText(s.wildRoles, size = 14)
            VSpace(8.dp)
            AdvSlot(Role.JOKER, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.KILLER, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.LUNATIC, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.DRUNK, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.WHORE, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.CURSED, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.SURVIVOR, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
            VSpace(8.dp)
            AdvSlot(Role.AMNESIAC, counts, chances, wallet, onBuy = { buyRole = it }, onCount = ::setRole, onChance = { r, p -> onChances(chances.with(r, p)) })
        }

        VSpace(10.dp)
        if (!advancedOpen) {
            PixelButton(s.resetRec) { onCounts(GameRules.recommendedCounts(names.size)) }
            VSpace(8.dp)
        }
        PixelButton(s.clearSetup, onClick = onClearSetup)

        if (counts.total != names.size) {
            VSpace(8.dp)
            PixelText(s.rolesVsPlayers(counts.total, names.size), size = 14, color = c.accent)
            PixelText(s.roleMismatch, size = 14, color = c.accent)
        }
        if (!GameRules.isTwinCountOk(counts[Role.TWIN_CIVIL]) ||
            !GameRules.isTwinCountOk(counts[Role.TWIN_MAFIA])
        ) {
            VSpace(8.dp)
            PixelText(s.twinsNeedPair, size = 14, color = c.accent)
        }
        RoleSplitHint(counts, names.size, showOptimal = !advancedOpen)

        VSpace(22.dp)
        SettingRow(s.narrator, if (narratorEnabled) s.on else s.off, s.narratorHint, onNarratorToggle)
        PixelButton(
            label = s.start,
            enabled = canStart,
        ) {
            onStart(names.map { it.trim() }, counts, chances)
        }
        VSpace(8.dp)
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
private fun RoleSplitHint(counts: RoleCounts, playerCount: Int, showOptimal: Boolean) {
    val s = str()
    val c = pal()
    VSpace(8.dp)
    if (showOptimal) {
        val (evil, good) = GameRules.recommendedTeamSplit(playerCount)
        PixelText(s.optimalSplit(playerCount, evil, good), size = 13)
    }
    PixelText(s.currentSplitFull(counts.evil, counts.good, counts.neutral), size = 13)
    if (!GameRules.tooManyEvil(counts)) return
    VSpace(4.dp)
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
private fun AdvSlot(
    role: Role,
    counts: RoleCounts,
    chances: RoleChances,
    wallet: Wallet,
    onBuy: (Role) -> Unit,
    onCount: (Role, Int) -> Unit,
    onChance: (Role, Int) -> Unit,
) {
    if (!wallet.owns(role)) {
        LockedRoleRow(role) { onBuy(role) }
        return
    }
    val s = str()
    Stepper(s.roleTitle(role), counts[role], min = 0, max = GameRules.MAX_PLAYERS) {
        onCount(role, it)
    }
    if (counts[role] > 0) {
        VSpace(4.dp)
        ChanceRow(role, chances[role], onChance)
    }
}

@Composable
private fun TwinSlot(
    role: Role,
    counts: RoleCounts,
    chances: RoleChances,
    wallet: Wallet,
    onBuy: (Role) -> Unit,
    onCount: (Role, Int) -> Unit,
    onChance: (Role, Int) -> Unit,
) {
    if (!wallet.owns(role)) {
        LockedRoleRow(role) { onBuy(role) }
        return
    }
    val s = str()
    Stepper(s.roleTitle(role), counts[role], min = 0, max = GameRules.MAX_PLAYERS, step = 2) { next ->
        onCount(role, next)
    }
    if (counts[role] > 0) {
        VSpace(4.dp)
        ChanceRow(role, chances[role], onChance)
    }
}

@Composable
private fun ChanceRow(role: Role, percent: Int, onChance: (Role, Int) -> Unit) {
    val s = str()
    val c = pal()
    val hint = when (role.team) {
        Team.EVIL -> s.spawnHintEvil
        Team.GOOD -> s.spawnHintGood
        Team.NEUTRAL -> s.spawnHintWild
    }
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, c.muted)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            PixelText(
                s.spawnChance(percent),
                size = 14,
                bold = true,
                align = TextAlign.Start,
                modifier = Modifier.weight(1f),
            )
            MiniBtn("-") { if (percent > 0) onChance(role, percent - 10) }
            Box(Modifier.width(48.dp), contentAlignment = Alignment.Center) {
                PixelText("$percent%", size = 16, bold = true)
            }
            MiniBtn("+") { if (percent < 100) onChance(role, percent + 10) }
        }
        VSpace(4.dp)
        PixelText(hint, size = 12, color = c.muted, align = TextAlign.Start)
    }
}

@Composable
private fun LockedRoleRow(role: Role, onBuy: () -> Unit) {
    val s = str()
    val c = pal()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, c.fg)
            .clickable(onClick = onBuy)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        PixelText(
            s.roleTitle(role),
            size = 16,
            bold = true,
            align = TextAlign.Start,
            modifier = Modifier.weight(1f),
        )
        PixelText(s.buyFirst, size = 16, bold = true, color = c.accent)
    }
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
                fontFamily = LocalFont.current,
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
