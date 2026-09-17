package com.secretmafia.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.secretmafia.game.Role
import com.secretmafia.game.Team
import com.secretmafia.ui.theme.str

@Composable
fun RoleHelpButton(role: Role) {
    var open by remember { mutableStateOf(false) }
    val s = str()
    PixelButton(s.moreInfo) { open = true }
    if (open) RoleHelpDialog(role) { open = false }
}

@Composable
fun RoleHelpDialog(role: Role, onClose: () -> Unit) {
    val s = str()
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
                VSpace(18.dp)
                PixelText(s.roleMoreInfo(role), size = 16, align = TextAlign.Start)
                VSpace(28.dp)
                PixelButton(s.back, onClick = onClose)
            }
        }
    }
}
