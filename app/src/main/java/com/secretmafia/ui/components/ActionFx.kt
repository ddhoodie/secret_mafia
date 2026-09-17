package com.secretmafia.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.secretmafia.game.ActionAnimation
import com.secretmafia.game.NightActionResult
import com.secretmafia.game.Role
import com.secretmafia.ui.i18n.Str
import com.secretmafia.ui.theme.Gold
import com.secretmafia.ui.theme.TownBlue
import com.secretmafia.ui.theme.pal
import kotlin.math.sin

private val SeerBlue = Color(0xFF3D7EFF)
private val SeerLight = Color(0xFF9AD8FF)
private val SeerPurple = Color(0xFFC44DFF)
private val SeerPink = Color(0xFFFF5FB8)
private val ShieldBlue = Color(0xFF9AD8FF)
private val DrunkGreen = Color(0xFF3DFF6A)
private val WhorePink = Color(0xFFFF5FB8)
private val WhorePurple = Color(0xFFB14DFF)
private val NecroGreen = Color(0xFF146B3A)
private val CursedPurpleLite = Color(0xFFE0A8FF)
private val SurviveOrange = Color(0xFFFF8A00)

private enum class BitKind { DROP, DIAMOND, TEXT }

private data class Bit(
    val kind: BitKind,
    val color: Color,
    val x: Float,
    val speed: Float,
    val delay: Float,
    val size: Float,
    val glyph: String = "",
    val rise: Boolean = false,
)

@Composable
fun RoleReveal(action: NightActionResult, s: Str) {
    val c = pal()
    val hide = c.accent == c.fg
    val blood = c.accent
    val cash = c.heal
    val bits = remember(action.actorRole, action.animation, action.inspectGood, hide, blood, cash, c.fg) {
        bitsFor(action.actorRole, action.animation, action.inspectGood, hide, blood, cash, c.fg)
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center,
    ) {
        ParticleField(bits)
        when {
            action.actorRole == Role.BODYGUARD -> ShieldPulse(if (hide) c.fg else ShieldBlue)
            action.actorRole == Role.LAWYER -> PulseMark("$", cash, 64)
            action.animation == ActionAnimation.INSPECT -> {
                val good = action.inspectGood == true
                val col = if (hide) c.fg else if (good) TownBlue else blood
                PulseMark(if (good) s.good else s.bad, col, 42)
            }
            action.animation == ActionAnimation.SEER -> {
                val role = action.inspectRoleTitle?.let { runCatching { Role.valueOf(it) }.getOrNull() }
                PulseMark(s.roleTitle(role), c.fg, 32)
            }
            action.animation == ActionAnimation.HEAL -> PulseMark("+", c.heal, 68)
            action.actorRole == Role.JOKER -> PulseMark(":P", if (hide) c.fg else Gold, 56)
            action.actorRole == Role.LUNATIC -> RainbowMark("o_O", 52, hide, c.fg)
            action.actorRole == Role.DRUNK -> PulseMark(":)", if (hide) c.fg else DrunkGreen, 56)
            action.actorRole == Role.TWIN_CIVIL -> PulseMark(":) (:", c.fg, 40)
            action.actorRole == Role.TWIN_MAFIA -> PulseMark(";) (;", blood, 40)
            action.actorRole == Role.WHORE -> PulseMark("ZZ", if (hide) c.fg else WhorePurple, 60)
            action.actorRole == Role.CIVILIAN && action.animation == ActionAnimation.LIKE ->
                PulseMark("<3", c.fg, 60)
            action.actorRole == Role.CURSED -> PulseMark("*", if (hide) c.fg else CursedPurpleLite, 56)
            action.actorRole == Role.SURVIVOR -> CycleMark(
                "!",
                if (hide) c.fg else Gold,
                if (hide) c.fg else SurviveOrange,
                56,
            )
            action.actorRole == Role.NECROMANCER && action.animation == ActionAnimation.RISE -> {
                val role = action.inspectRoleTitle?.let { runCatching { Role.valueOf(it) }.getOrNull() }
                PulseMark(s.roleTitle(role), c.fg, 32)
            }
            action.actorRole == Role.NECROMANCER -> PulseMark("*", c.fg, 52)
            action.animation == ActionAnimation.LIKE -> PulseMark("<3", c.fg, 60)
            action.animation == ActionAnimation.MATH -> PulseMark("OK", c.fg, 52)
            action.animation == ActionAnimation.VOTE -> PulseMark(s.vote, c.fg, 40)
            action.animation == ActionAnimation.SLEEP -> PulseMark("ZZ", c.fg, 60)
            action.animation == ActionAnimation.REVEAL -> PulseMark("!!", c.fg, 56)
            action.animation == ActionAnimation.POISON -> PulseMark("XX", blood, 56)
            action.animation == ActionAnimation.FRAME -> PulseMark("!!", blood, 52)
            action.animation == ActionAnimation.RISE -> {
                val role = action.inspectRoleTitle?.let { runCatching { Role.valueOf(it) }.getOrNull() }
                PulseMark(s.roleTitle(role), c.fg, 32)
            }
            action.animation == ActionAnimation.SHIELD -> PulseMark("$", cash, 56)
            action.animation == ActionAnimation.KNIFE ||
                action.animation == ActionAnimation.HUNT -> PulseMark("X", blood, 64)
            else -> PulseMark("...", c.fg, 36)
        }
    }
}

@Composable
private fun PulseMark(text: String, color: Color, size: Int) {
    val pulse = rememberInfiniteTransition(label = "mark")
    val scale by pulse.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(tween(280), RepeatMode.Reverse),
        label = "s",
    )
    PixelText(text, size = (size * scale).toInt().coerceAtLeast(26), bold = true, color = color)
}

@Composable
private fun CycleMark(text: String, a: Color, b: Color, size: Int) {
    val pulse = rememberInfiniteTransition(label = "cycle")
    val scale by pulse.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(tween(280), RepeatMode.Reverse),
        label = "s",
    )
    val mix by pulse.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(480), RepeatMode.Reverse),
        label = "mix",
    )
    PixelText(
        text,
        size = (size * scale).toInt().coerceAtLeast(26),
        bold = true,
        color = lerp(a, b, mix),
    )
}

@Composable
private fun RainbowMark(text: String, size: Int, hide: Boolean, fallback: Color) {
    val pulse = rememberInfiniteTransition(label = "rainbow")
    val scale by pulse.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(tween(280), RepeatMode.Reverse),
        label = "s",
    )
    val hue by pulse.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing), RepeatMode.Restart),
        label = "h",
    )
    PixelText(
        text,
        size = (size * scale).toInt().coerceAtLeast(26),
        bold = true,
        color = if (hide) fallback else Color.hsv(hue, 0.72f, 1f),
    )
}

@Composable
private fun ShieldPulse(color: Color) {
    val pulse = rememberInfiniteTransition(label = "shield")
    val flex by pulse.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(tween(380), RepeatMode.Reverse),
        label = "f",
    )
    Canvas(Modifier.fillMaxWidth().height(200.dp)) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val w = 52f * flex
        val h = 68f * flex
        val path = Path().apply {
            moveTo(cx, cy - h * 0.52f)
            lineTo(cx + w * 0.52f, cy - h * 0.28f)
            lineTo(cx + w * 0.52f, cy + h * 0.06f)
            quadraticTo(cx + w * 0.18f, cy + h * 0.52f, cx, cy + h * 0.54f)
            quadraticTo(cx - w * 0.18f, cy + h * 0.52f, cx - w * 0.52f, cy + h * 0.06f)
            lineTo(cx - w * 0.52f, cy - h * 0.28f)
            close()
        }
        drawPath(path, color.copy(alpha = 0.18f))
        drawPath(path, color, style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawLine(color, Offset(cx, cy - h * 0.28f), Offset(cx, cy + h * 0.22f), 5f, StrokeCap.Round)
        drawLine(color, Offset(cx - w * 0.22f, cy - h * 0.04f), Offset(cx + w * 0.22f, cy - h * 0.04f), 5f, StrokeCap.Round)
    }
}

@Composable
private fun ParticleField(bits: List<Bit>) {
    val clock = rememberInfiniteTransition(label = "fall")
    val t by clock.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Restart),
        label = "t",
    )
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val w = maxWidth
        val h = maxHeight
        Canvas(Modifier.fillMaxSize()) {
            bits.forEach { bit ->
                val phase = ((t * bit.speed) + bit.delay) % 1f
                val y = if (bit.rise) size.height * (1f - phase) else size.height * phase
                val x = size.width * bit.x + sin((phase + bit.delay) * 6.2f) * 10f
                when (bit.kind) {
                    BitKind.DROP -> drawDrop(Offset(x, y), bit.size, bit.color)
                    BitKind.DIAMOND -> drawDiamond(Offset(x, y), bit.size, bit.color)
                    BitKind.TEXT -> { }
                }
            }
        }
        bits.filter { it.kind == BitKind.TEXT }.forEach { bit ->
            val phase = ((t * bit.speed) + bit.delay) % 1f
            val yFrac = if (bit.rise) 1f - phase else phase
            val xFrac = bit.x + (sin((phase + bit.delay) * 6.2f).toFloat() * 0.03f)
            PixelText(
                bit.glyph,
                size = bit.size.toInt(),
                bold = true,
                color = bit.color,
                modifier = Modifier.offset(x = w * xFrac, y = h * yFrac),
            )
        }
    }
}

private fun DrawScope.drawDrop(at: Offset, size: Float, color: Color) {
    val path = Path().apply {
        moveTo(at.x, at.y - size)
        quadraticTo(at.x + size * 0.72f, at.y, at.x, at.y + size)
        quadraticTo(at.x - size * 0.72f, at.y, at.x, at.y - size)
        close()
    }
    drawPath(path, color)
}

private fun DrawScope.drawDiamond(at: Offset, size: Float, color: Color) {
    val path = Path().apply {
        moveTo(at.x, at.y - size)
        lineTo(at.x + size * 0.65f, at.y)
        lineTo(at.x, at.y + size)
        lineTo(at.x - size * 0.65f, at.y)
        close()
    }
    drawPath(path, color)
}

private fun bitsFor(
    role: Role?,
    anim: ActionAnimation,
    inspectGood: Boolean?,
    hide: Boolean,
    blood: Color,
    cash: Color,
    fg: Color,
): List<Bit> {
    val star = listOf(SeerBlue, SeerLight, SeerPurple, SeerPink).map { if (hide) blood else it }
    val gold = if (hide) blood else Gold
    val blue = if (hide) blood else TownBlue
    val green = if (hide) fg else DrunkGreen
    val pink = if (hide) fg else WhorePink
    val purple = if (hide) fg else SeerPurple
    val necro = if (hide) fg else NecroGreen
    val orange = if (hide) fg else SurviveOrange
    val lunaticGlyphs = listOf("A", "Z", "?", "!", "*", "#", "@", "&", "%", "+", "X", "~", "$", "/")
    return when {
        role == Role.LAWYER || anim == ActionAnimation.SHIELD -> rain(8) { i, x, sp, d ->
            Bit(BitKind.TEXT, cash, x, sp, d, if (i % 2 == 0) 22f else 16f, "$")
        }
        role == Role.DON -> rain(6) { _, x, sp, d ->
            Bit(BitKind.DROP, blood, x, sp, d, 12f)
        } + rain(5) { i, x, sp, d ->
            Bit(BitKind.TEXT, cash, (x + 0.08f) % 1f, sp * 0.9f, d, if (i % 2 == 0) 20f else 14f, "$")
        }
        role == Role.BODYGUARD -> rain(6) { i, x, sp, d ->
            Bit(BitKind.DIAMOND, if (hide) fg else ShieldBlue, x, sp, d, if (i % 2 == 0) 12f else 8f)
        }
        role == Role.SEER || anim == ActionAnimation.SEER -> rain(10) { i, x, sp, d ->
            Bit(BitKind.TEXT, star[i % star.size], x, sp, d, if (i % 3 == 0) 26f else 18f, "*")
        }
        role == Role.JOKER -> rain(8) { i, x, sp, d ->
            if (i % 2 == 0) Bit(BitKind.DIAMOND, gold, x, sp, d, 14f)
            else Bit(BitKind.TEXT, gold, x, sp, d, 18f, listOf("J", "A", "*")[i % 3])
        }
        role == Role.LUNATIC -> rain(12) { i, x, sp, d ->
            val col = if (hide) fg else Color.hsv((i * 37f) % 360f, 0.7f, 1f)
            Bit(BitKind.TEXT, col, x, sp, d, if (i % 3 == 0) 22f else 16f, lunaticGlyphs[i % lunaticGlyphs.size])
        }
        role == Role.DRUNK -> rain(8) { i, x, sp, d ->
            Bit(BitKind.TEXT, green, x, sp * 0.7f, d, if (i % 2 == 0) 22f else 14f, if (i % 3 == 0) "O" else "o", rise = true)
        }
        role == Role.WHORE -> rain(6) { i, x, sp, d ->
            Bit(BitKind.TEXT, pink, x, sp, d, 16f, "z")
        } + rain(5) { _, x, sp, d ->
            Bit(BitKind.TEXT, pink, x, sp, d, 16f, "<3", rise = true)
        }
        role == Role.TWIN_CIVIL || (role == Role.CIVILIAN && anim != ActionAnimation.SLEEP) -> rain(7) { _, x, sp, d ->
            Bit(BitKind.TEXT, fg, x, sp, d, 18f, "<3", rise = true)
        }
        role == Role.TWIN_MAFIA -> rain(6) { i, x, sp, d ->
            Bit(BitKind.TEXT, blood, x, sp, d, 18f, if (i % 2 == 0) ";)" else "(;")
        } + rain(5) { _, x, sp, d ->
            Bit(BitKind.DROP, blood, x, sp, d, 11f)
        }
        role == Role.CURSED -> rain(8) { i, x, sp, d ->
            val col = if (i % 2 == 0) green else purple
            Bit(BitKind.TEXT, col, x, sp, d, if (i % 3 == 0) 20f else 14f, if (i % 2 == 0) "*" else "?")
        }
        role == Role.SURVIVOR -> rain(8) { i, x, sp, d ->
            Bit(BitKind.TEXT, if (i % 2 == 0) gold else orange, x, sp, d, 18f, if (i % 2 == 0) "*" else "!")
        }
        role == Role.NECROMANCER -> rain(8) { i, x, sp, d ->
            Bit(BitKind.TEXT, necro, x, sp * 0.75f, d, 18f, if (i % 2 == 0) "*" else "o", rise = true)
        }
        anim == ActionAnimation.REVEAL || role == Role.MAYOR -> rain(7) { i, x, sp, d ->
            Bit(BitKind.TEXT, blue, x, sp, d, if (i % 2 == 0) 22f else 16f, if (i % 2 == 0) "!" else "*")
        }
        anim == ActionAnimation.POISON || role == Role.POISONER -> rain(8) { i, x, sp, d ->
            if (i % 2 == 0) Bit(BitKind.DROP, blood, x, sp, d, 11f)
            else Bit(BitKind.TEXT, cash, x, sp, d, 18f, "x")
        }
        anim == ActionAnimation.FRAME || role == Role.FRAMER -> rain(7) { _, x, sp, d ->
            Bit(BitKind.TEXT, blood, x, sp, d, 20f, "!")
        }
        anim == ActionAnimation.RISE || role == Role.AMNESIAC -> rain(8) { i, x, sp, d ->
            Bit(BitKind.TEXT, star[i % star.size], x, sp * 0.75f, d, 18f, if (i % 2 == 0) "*" else "o", rise = true)
        }
        role == Role.VIGILANTE -> rain(8) { _, x, sp, d ->
            Bit(BitKind.DROP, blood, x, sp, d, 13f)
        }
        role == Role.HEALER || anim == ActionAnimation.HEAL -> rain(8) { _, x, sp, d ->
            Bit(BitKind.TEXT, cash, x, sp, d, 22f, "+", rise = true)
        }
        anim == ActionAnimation.INSPECT -> {
            val col = if (hide) fg else if (inspectGood == true) blue else blood
            rain(6) { _, x, sp, d -> Bit(BitKind.TEXT, col, x, sp, d, 20f, "?") }
        }
        anim == ActionAnimation.LIKE -> rain(7) { _, x, sp, d ->
            Bit(BitKind.TEXT, blood, x, sp, d, 18f, "<3", rise = true)
        }
        anim == ActionAnimation.MATH -> rain(6) { i, x, sp, d ->
            Bit(BitKind.TEXT, blood, x, sp, d, 20f, listOf("1", "2", "3", "+", "=")[i % 5])
        }
        anim == ActionAnimation.VOTE -> rain(6) { _, x, sp, d ->
            Bit(BitKind.TEXT, blood, x, sp, d, 18f, "!")
        }
        anim == ActionAnimation.SLEEP -> rain(6) { i, x, sp, d ->
            Bit(BitKind.TEXT, fg, x, sp, d, 16f, if (i % 2 == 0) "z" else "Z")
        }
        anim == ActionAnimation.KNIFE || anim == ActionAnimation.HUNT ||
            role == Role.MAFIA || role == Role.KILLER || role == Role.HUNTER -> rain(8) { _, x, sp, d ->
            Bit(BitKind.DROP, blood, x, sp, d, 13f)
        }
        else -> emptyList()
    }
}

private fun rain(n: Int, make: (i: Int, x: Float, speed: Float, delay: Float) -> Bit): List<Bit> =
    List(n) { i ->
        make(
            i,
            (0.08f + (i * 0.11f) + (i % 3) * 0.04f).mod(1f),
            0.55f + (i % 4) * 0.18f,
            (i * 0.13f).mod(1f),
        )
    }
