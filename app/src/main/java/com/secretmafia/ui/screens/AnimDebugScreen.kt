package com.secretmafia.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.secretmafia.game.ActionAnimation
import com.secretmafia.game.NightActionResult
import com.secretmafia.game.Role
import com.secretmafia.ui.components.PixelButton
import com.secretmafia.ui.components.PixelScreen
import com.secretmafia.ui.components.PixelText
import com.secretmafia.ui.components.RolePortrait
import com.secretmafia.ui.components.RoleReveal
import com.secretmafia.ui.components.VSpace
import com.secretmafia.ui.i18n.Str
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str

private val twoStepRoles = setOf(Role.LAWYER, Role.FRAMER)

@Composable
fun AnimDebugScreen(onBack: () -> Unit) {
    val s = str()
    val c = pal()
    var role by remember { mutableStateOf(Role.MAFIA) }
    var actionIndex by remember { mutableIntStateOf(0) }
    val samples = remember(role) { debugSamples(role, s) }
    val sample = samples[actionIndex.coerceIn(0, samples.lastIndex)]
    PixelScreen(scroll = true) {
        PixelText(s.animDebug, size = 28, bold = true)
        VSpace(12.dp)
        RolePortrait(role, size = 88.dp)
        VSpace(8.dp)
        PixelText(s.roleTitle(role), size = 20, bold = true)
        if (role in twoStepRoles) {
            PixelText(s.twoSteps, size = 14, color = c.accent)
        }
        VSpace(8.dp)
        androidx.compose.runtime.key(role, actionIndex) {
            RoleReveal(sample.result, s)
        }
        PixelText(sample.label, size = 16)
        if (samples.size > 1) {
            VSpace(8.dp)
            PixelButton("${sample.label}  ${actionIndex + 1}/${samples.size}") {
                actionIndex = (actionIndex + 1) % samples.size
            }
        }
        VSpace(18.dp)
        Role.entries.forEach { r ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, if (r == role) c.accent else c.fg)
                    .clickable {
                        role = r
                        actionIndex = 0
                    }
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                PixelText(
                    s.roleTitle(r),
                    size = 16,
                    bold = true,
                    align = TextAlign.Start,
                    modifier = Modifier.weight(1f),
                )
                if (r in twoStepRoles) {
                    PixelText(s.twoSteps, size = 13, color = c.accent)
                }
            }
            VSpace(8.dp)
        }
        PixelButton(s.back, onClick = onBack)
    }
}

private data class DebugSample(val label: String, val result: NightActionResult)

private fun debugSamples(role: Role, s: Str): List<DebugSample> {
    fun one(label: String, anim: ActionAnimation, good: Boolean? = null, seen: Role? = null) =
        DebugSample(
            label,
            NightActionResult(
                animation = anim,
                inspectGood = good,
                inspectRoleTitle = seen?.name,
                actorRole = role,
            ),
        )
    return when (role) {
        Role.MAFIA, Role.DON, Role.TWIN_MAFIA, Role.KILLER -> listOf(
            one(s.vote, ActionAnimation.KNIFE),
        )
        Role.VIGILANTE -> listOf(
            one(s.vote, ActionAnimation.KNIFE),
            one(s.keepShot, ActionAnimation.LIKE),
        )
        Role.LAWYER -> listOf(
            one(s.vote, ActionAnimation.KNIFE),
            one(s.shield, ActionAnimation.SHIELD),
        )
        Role.FRAMER -> listOf(
            one(s.vote, ActionAnimation.KNIFE),
            one(s.whoFrame, ActionAnimation.FRAME),
        )
        Role.HEALER -> listOf(one(s.roleTitle(Role.HEALER), ActionAnimation.HEAL))
        Role.COP -> listOf(
            one(s.good, ActionAnimation.INSPECT, good = true),
            one(s.bad, ActionAnimation.INSPECT, good = false),
        )
        Role.SEER -> listOf(one(s.roleTitle(Role.MAFIA), ActionAnimation.SEER, seen = Role.MAFIA))
        Role.BODYGUARD -> listOf(one(s.roleTitle(Role.BODYGUARD), ActionAnimation.GUARD))
        Role.HUNTER -> listOf(
            one(s.roleTitle(Role.HUNTER), ActionAnimation.HUNT),
            one(s.like, ActionAnimation.LIKE),
        )
        Role.WHORE -> listOf(one(s.roleTitle(Role.WHORE), ActionAnimation.SLEEP))
        Role.MAYOR -> listOf(
            one(s.roleTitle(Role.MAYOR), ActionAnimation.REVEAL),
            one(s.like, ActionAnimation.LIKE),
        )
        Role.NECROMANCER, Role.AMNESIAC -> listOf(
            one(s.roleTitle(role), ActionAnimation.RISE, seen = Role.COP),
            one(s.like, ActionAnimation.LIKE),
        )
        Role.POISONER -> listOf(
            one(s.roleTitle(Role.POISONER), ActionAnimation.POISON),
            one(s.skipTonight, ActionAnimation.LIKE),
        )
        Role.CIVILIAN -> listOf(
            one(s.like, ActionAnimation.LIKE),
            one(s.math, ActionAnimation.MATH),
        )
        Role.JOKER, Role.DRUNK, Role.LUNATIC, Role.TRAITOR, Role.CURSED,
        Role.SURVIVOR, Role.TWIN_CIVIL -> listOf(one(s.like, ActionAnimation.LIKE))
    }
}
