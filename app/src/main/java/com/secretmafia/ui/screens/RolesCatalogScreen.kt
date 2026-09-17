package com.secretmafia.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.secretmafia.game.CoinKind
import com.secretmafia.game.Progress
import com.secretmafia.game.Role
import com.secretmafia.game.Wallet
import com.secretmafia.game.WhoreAlign
import com.secretmafia.ui.components.CoinIcon
import com.secretmafia.ui.components.CoinLine
import com.secretmafia.ui.components.coinTint
import com.secretmafia.ui.components.PixelButton
import com.secretmafia.ui.components.PixelScreen
import com.secretmafia.ui.components.PixelText
import com.secretmafia.ui.components.RoleHelpButton
import com.secretmafia.ui.components.RolePortrait
import com.secretmafia.ui.components.VSpace
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str

private val goodRoles = listOf(
    Role.CIVILIAN, Role.HEALER, Role.COP,
    Role.HUNTER, Role.SEER, Role.BODYGUARD, Role.MAYOR, Role.NECROMANCER, Role.VIGILANTE, Role.TWIN_CIVIL,
)
private val evilRoles = listOf(
    Role.MAFIA, Role.DON, Role.LAWYER, Role.FRAMER, Role.TRAITOR, Role.POISONER,
    Role.LUNATIC, Role.TWIN_MAFIA,
)
private val wildRoles = listOf(
    Role.JOKER, Role.KILLER, Role.DRUNK, Role.WHORE, Role.CURSED, Role.SURVIVOR, Role.AMNESIAC,
)

@Composable
fun RolesCatalogScreen(
    wallet: Wallet,
    adWatches: Int,
    onRole: (Role) -> Unit,
    onWatchAd: () -> Unit,
    onPickAdCoin: (CoinKind) -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    val c = pal()
    val pickReady = Progress.adPickReady(adWatches)
    PixelScreen(scroll = true) {
        PixelText(s.roles, size = 28, bold = true)
        VSpace(8.dp)
        CoinLine(wallet)
        VSpace(6.dp)
        PixelText(s.unlockHint, size = 13)
        VSpace(8.dp)
        PixelText(s.adCoinHint, size = 13, color = c.muted)
        VSpace(12.dp)
        if (pickReady) {
            PixelText(s.pickAdCoin, size = 16, bold = true)
            VSpace(10.dp)
            listOf(CoinKind.TOWN, CoinKind.BLOOD, CoinKind.GOLD).forEach { kind ->
                PixelButton(
                    label = s.takeCoin(s.coinName(kind)),
                    tint = coinTint(kind),
                ) { onPickAdCoin(kind) }
                VSpace(8.dp)
            }
        } else {
            PixelButton(s.watchAd(adWatches, Progress.ADS_FOR_COIN)) {
                onWatchAd()
            }
        }
        VSpace(18.dp)
        PixelText(s.goodRoles, size = 16, bold = true)
        VSpace(10.dp)
        goodRoles.forEach { RoleCatalogRow(it, wallet) { onRole(it) } }
        VSpace(18.dp)
        PixelText(s.evilRoles, size = 16, bold = true)
        VSpace(10.dp)
        evilRoles.forEach { RoleCatalogRow(it, wallet) { onRole(it) } }
        VSpace(18.dp)
        PixelText(s.wildRoles, size = 16, bold = true)
        VSpace(10.dp)
        wildRoles.forEach { RoleCatalogRow(it, wallet) { onRole(it) } }
        VSpace(16.dp)
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
fun RoleUnlockDetail(
    role: Role,
    wallet: Wallet,
    whoreAlign: WhoreAlign = WhoreAlign.PICK,
    onCycleWhoreAlign: () -> Unit = {},
    onUnlock: () -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    val owned = wallet.owns(role)
    val kind = Progress.costOf(role)
    PixelScreen(scroll = true) {
        RolePortrait(role, size = 140.dp)
        VSpace(16.dp)
        PixelText(s.roleTitle(role), size = 28, bold = true)
        VSpace(8.dp)
        PixelText(
            when {
                role.isFree -> s.freeRole
                owned -> s.unlockedLabel
                else -> s.locked
            },
            size = 14,
        )
        VSpace(16.dp)
        PixelText(s.roleBlurb(role), size = 16, align = TextAlign.Start)
        VSpace(12.dp)
        RoleHelpButton(role)
        if (role == Role.WHORE) {
            VSpace(20.dp)
            PixelText(s.whoreHow, size = 14, bold = true)
            VSpace(10.dp)
            PixelButton(s.whoreAlignLabel(whoreAlign), onClick = onCycleWhoreAlign)
        }
        VSpace(28.dp)
        if (!role.isFree && !owned) {
            val can = Progress.canUnlock(wallet, role)
            PixelButton(
                label = if (can) {
                    s.unlockFor(Progress.UNLOCK_COST, s.coinName(kind, Progress.UNLOCK_COST))
                } else {
                    s.needCoins(Progress.UNLOCK_COST, s.coinName(kind, Progress.UNLOCK_COST))
                },
                enabled = can,
                tint = coinTint(kind),
                onClick = onUnlock,
            )
            VSpace(8.dp)
            CoinLine(wallet)
            VSpace(12.dp)
        }
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
private fun RoleCatalogRow(role: Role, wallet: Wallet, onClick: () -> Unit) {
    val s = str()
    val c = pal()
    val kind = Progress.costOf(role)
    val status = when {
        role.isFree -> s.freeRole
        wallet.owns(role) -> s.unlockedLabel
        else -> "${Progress.UNLOCK_COST} ${s.coinName(kind, Progress.UNLOCK_COST)}"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .border(2.dp, c.fg)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RolePortrait(role, size = 52.dp)
        Column(Modifier.weight(1f)) {
            PixelText(s.roleTitle(role), size = 16, bold = true, align = TextAlign.Start)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (!role.isFree && !wallet.owns(role)) CoinIcon(kind, size = 14.dp)
                PixelText(
                    status,
                    size = 12,
                    color = if (!role.isFree && !wallet.owns(role)) coinTint(kind) else c.muted,
                    align = TextAlign.Start,
                )
            }
        }
    }
}
