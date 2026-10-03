package com.secretmafia.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.secretmafia.game.Progress
import com.secretmafia.game.Role
import com.secretmafia.game.Team
import com.secretmafia.game.Wallet
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str

@Composable
fun BuyRoleDialog(
    role: Role,
    wallet: Wallet,
    onUnlock: () -> Unit,
    onClose: () -> Unit,
) {
    val s = str()
    val kind = Progress.costOf(role)
    val can = Progress.canUnlock(wallet, role)
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(Modifier.fillMaxSize()) {
            PixelScreen(scroll = true) {
                RolePortrait(role, size = 120.dp)
                VSpace(14.dp)
                PixelText(s.roleTitle(role), size = 28, bold = true)
                VSpace(6.dp)
                PixelText(
                    when (role.team) {
                        Team.GOOD -> s.goodRoles
                        Team.EVIL -> s.evilRoles
                        Team.NEUTRAL -> s.wildRoles
                    },
                    size = 14,
                )
                VSpace(14.dp)
                PixelText(s.roleMoreInfo(role), size = 16, align = TextAlign.Start)
                VSpace(18.dp)
                PixelText(s.balance, size = 13, color = pal().muted)
                VSpace(6.dp)
                CoinLine(wallet)
                VSpace(12.dp)
                PixelButton(
                    label = if (can) {
                        s.unlockFor(Progress.UNLOCK_COST, s.coinName(kind, Progress.UNLOCK_COST))
                    } else {
                        s.needCoins(Progress.UNLOCK_COST, s.coinName(kind, Progress.UNLOCK_COST))
                    },
                    enabled = can,
                    tint = coinTint(kind),
                    onClick = {
                        onUnlock()
                        onClose()
                    },
                )
                VSpace(8.dp)
                PixelButton(s.back, onClick = onClose)
            }
        }
    }
}
