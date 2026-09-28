package com.secretmafia.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.secretmafia.game.CoinKind
import com.secretmafia.game.MatchReward
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

@Composable
fun MatchPayout(
    reward: MatchReward?,
    survivorLived: Boolean,
    wallet: Wallet,
) {
    val s = str()
    val c = pal()
    val kinds = buildList {
        Progress.coinFrom(reward ?: MatchReward.TOO_FAST)?.let(::add)
        if (survivorLived) add(CoinKind.GOLD)
    }
    val pop = remember { Animatable(0.25f) }
    LaunchedEffect(reward, survivorLived) {
        pop.snapTo(0.25f)
        pop.animateTo(1.18f, tween(480, easing = FastOutSlowInEasing))
        pop.animateTo(1f, tween(180))
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when (reward) {
            MatchReward.TOO_FAST, null -> PixelText(s.tooFast, size = 15, color = c.accent)
            MatchReward.BLOOD, MatchReward.TOWN, MatchReward.GOLD -> {
                val kind = Progress.coinFrom(reward)!!
                PixelText(s.earnedCoin, size = 13, color = c.muted)
                VSpace(8.dp)
                PixelText(s.matchCoin(Progress.MATCH_REWARD, s.coinName(kind)), size = 22, bold = true)
            }
        }
        if (survivorLived) {
            VSpace(8.dp)
            PixelText(s.survivorBonus(s.coinName(CoinKind.GOLD)), size = 15)
        }
        if (kinds.isNotEmpty()) {
            VSpace(16.dp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.graphicsLayer {
                    scaleX = pop.value
                    scaleY = pop.value
                },
            ) {
                kinds.forEach { CoinIcon(it, size = 52.dp) }
            }
        }
        VSpace(18.dp)
        PixelText(s.youHave, size = 13, color = c.muted)
        VSpace(8.dp)
        CoinLine(wallet, size = 15)
    }
}

/**
 * Preset faces in `res/drawable/avatar_0.png` … `avatar_7.png`.
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
