package com.secretmafia.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.secretmafia.game.CoinKind
import com.secretmafia.game.Progress
import com.secretmafia.game.Wallet
import com.secretmafia.ui.theme.Blood
import com.secretmafia.ui.theme.Gold
import com.secretmafia.ui.theme.TownBlue
import com.secretmafia.ui.theme.pal
import com.secretmafia.ui.theme.str

fun coinTint(kind: CoinKind) = when (kind) {
    CoinKind.BLOOD -> Blood
    CoinKind.TOWN -> TownBlue
    CoinKind.GOLD -> Gold
}

@Composable
fun CoinIcon(kind: CoinKind, size: Dp = 18.dp) {
    val color = coinTint(kind)
    Canvas(Modifier.size(size)) {
        val w = this.size.minDimension
        when (kind) {
            CoinKind.BLOOD -> {
                val drop = Path().apply {
                    moveTo(w * 0.50f, w * 0.10f)
                    cubicTo(w * 0.84f, w * 0.38f, w * 0.84f, w * 0.72f, w * 0.50f, w * 0.92f)
                    cubicTo(w * 0.16f, w * 0.72f, w * 0.16f, w * 0.38f, w * 0.50f, w * 0.10f)
                    close()
                }
                drawPath(drop, color)
            }
            CoinKind.TOWN -> {
                // Shield: flat top, round bottom.
                val shield = Path().apply {
                    moveTo(w * 0.18f, w * 0.12f)
                    lineTo(w * 0.82f, w * 0.12f)
                    lineTo(w * 0.82f, w * 0.48f)
                    quadraticTo(w * 0.82f, w * 0.88f, w * 0.50f, w * 0.94f)
                    quadraticTo(w * 0.18f, w * 0.88f, w * 0.18f, w * 0.48f)
                    close()
                }
                drawPath(shield, color)
            }
            CoinKind.GOLD -> {
                // Playing-card diamond.
                val diamond = Path().apply {
                    moveTo(w * 0.50f, w * 0.08f)
                    lineTo(w * 0.88f, w * 0.50f)
                    lineTo(w * 0.50f, w * 0.92f)
                    lineTo(w * 0.12f, w * 0.50f)
                    close()
                }
                drawPath(diamond, color)
            }
        }
    }
}

@Composable
fun CoinLine(wallet: Wallet, size: Int = 13) {
    val s = str()
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        listOf(
            CoinKind.TOWN to s.townN(wallet.town),
            CoinKind.BLOOD to s.bloodN(wallet.blood),
            CoinKind.GOLD to s.goldN(wallet.gold),
        ).forEach { (kind, label) ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                CoinIcon(kind)
                PixelText(label, size = size)
            }
        }
    }
}

/**
 * Preset avatars only. Drop `res/drawable/avatar_0.png` … `avatar_5.png` later.
 * Until then, a numbered bust placeholder.
 */
@Composable
fun AvatarPortrait(avatarId: Int, size: Dp = 56.dp) {
    val c = pal()
    val context = LocalContext.current
    val id = avatarId.coerceIn(0, Progress.AVATAR_COUNT - 1)
    val resId = remember(id) {
        context.resources.getIdentifier("avatar_$id", "drawable", context.packageName)
    }
    Box(
        modifier = Modifier
            .size(size)
            .border(2.dp, c.fg),
        contentAlignment = Alignment.Center,
    ) {
        if (resId != 0) {
            Image(
                painter = painterResource(resId),
                contentDescription = "avatar $id",
                modifier = Modifier.fillMaxSize().padding(4.dp),
                contentScale = ContentScale.Fit,
            )
        } else {
            PixelText((id + 1).toString(), size = (size.value / 2.4f).toInt(), bold = true)
        }
    }
}
