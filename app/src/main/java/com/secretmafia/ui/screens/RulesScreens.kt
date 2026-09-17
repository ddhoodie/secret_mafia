package com.secretmafia.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.secretmafia.game.Role
import com.secretmafia.ui.components.PixelButton
import com.secretmafia.ui.components.PixelScreen
import com.secretmafia.ui.components.PixelText
import com.secretmafia.ui.components.RoleHelpButton
import com.secretmafia.ui.components.RolePortrait
import com.secretmafia.ui.components.VSpace
import com.secretmafia.ui.theme.str

@Composable
fun RulesHub(
    onGeneral: () -> Unit,
    onRoles: () -> Unit,
    onNight: () -> Unit,
    onDay: () -> Unit,
    onWinning: () -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    PixelScreen {
        PixelText(s.rules, size = 28, bold = true)
        VSpace(24.dp)
        PixelButton(s.general, onClick = onGeneral)
        VSpace(12.dp)
        PixelButton(s.roles, onClick = onRoles)
        VSpace(12.dp)
        PixelButton(s.night, onClick = onNight)
        VSpace(12.dp)
        PixelButton(s.day, onClick = onDay)
        VSpace(12.dp)
        PixelButton(s.winning, onClick = onWinning)
        VSpace(28.dp)
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
fun RulesTextScreen(title: String, body: String, onBack: () -> Unit) {
    val s = str()
    PixelScreen(scroll = true) {
        PixelText(title, size = 28, bold = true)
        VSpace(20.dp)
        PixelText(body, size = 16)
        VSpace(28.dp)
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
fun RulesRolesList(
    onRole: (Role) -> Unit,
    onBack: () -> Unit,
) {
    val s = str()
    PixelScreen(scroll = true) {
        PixelText(s.roles, size = 28, bold = true)
        VSpace(18.dp)
        PixelText(s.goodRoles, size = 16, bold = true)
        VSpace(10.dp)
        listOf(
            Role.CIVILIAN, Role.HEALER, Role.COP,
            Role.HUNTER, Role.SEER, Role.BODYGUARD, Role.MAYOR, Role.NECROMANCER, Role.VIGILANTE, Role.TWIN_CIVIL,
        ).forEach {
            PixelButton(s.roleTitle(it)) { onRole(it) }
            VSpace(8.dp)
        }
        VSpace(18.dp)
        PixelText(s.evilRoles, size = 16, bold = true)
        VSpace(10.dp)
        listOf(
            Role.MAFIA, Role.DON, Role.LAWYER, Role.FRAMER, Role.TRAITOR, Role.POISONER,
            Role.LUNATIC, Role.TWIN_MAFIA,
        ).forEach {
            PixelButton(s.roleTitle(it)) { onRole(it) }
            VSpace(8.dp)
        }
        VSpace(18.dp)
        PixelText(s.wildRoles, size = 16, bold = true)
        VSpace(10.dp)
        listOf(
            Role.JOKER, Role.KILLER, Role.DRUNK, Role.WHORE, Role.CURSED, Role.SURVIVOR, Role.AMNESIAC,
        ).forEach {
            PixelButton(s.roleTitle(it)) { onRole(it) }
            VSpace(8.dp)
        }
        VSpace(16.dp)
        PixelButton(s.back, onClick = onBack)
    }
}

@Composable
fun RulesRoleDetail(role: Role, onBack: () -> Unit) {
    val s = str()
    PixelScreen(scroll = true) {
        RolePortrait(role, size = 140.dp)
        VSpace(16.dp)
        PixelText(s.roleTitle(role), size = 28, bold = true)
        VSpace(20.dp)
        PixelText(s.roleBlurb(role), size = 16, align = TextAlign.Start)
        VSpace(12.dp)
        RoleHelpButton(role)
        VSpace(28.dp)
        PixelButton(s.back, onClick = onBack)
    }
}
