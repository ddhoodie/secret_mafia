package com.secretmafia.ui

import android.content.Context
import android.media.MediaPlayer
import com.secretmafia.R
import com.secretmafia.game.DeathCause
import com.secretmafia.game.DeathRecord
import com.secretmafia.game.GamePhase
import com.secretmafia.game.GameState
import com.secretmafia.game.PassKind
import com.secretmafia.game.Winner
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

enum class NarratorLine {
    START,
    DEATH,
    DEATH_DAY,
    LIFE,
    LIFE_DAY,
    DISCUSS,
    DISCUSS_OVER,
    CIVIL_WIN,
    MAFIA_WIN,
    SPECIAL_WIN,
    SLEEP,
    MAFIA_WAKEUP,
    MAFIA_SLEEP,
    WAKEUP,
}

object Narrator {
    private var player: MediaPlayer? = null
    private var pendingDone: (() -> Unit)? = null
    private val lastClip = mutableMapOf<NarratorLine, Int>()

    fun cueKey(state: GameState): String? = when (val phase = state.phase) {
        is GamePhase.Handoff ->
            if (phase.kind == PassKind.NIGHT && !state.settings.mafiaConfer) {
                "night_open_${state.nightNumber}"
            } else {
                null
            }
        GamePhase.NightSummary -> "night_sum_${state.nightNumber}"
        GamePhase.Discuss -> "discuss_${state.nightNumber}"
        GamePhase.DaySummary -> "day_sum_${state.nightNumber}"
        is GamePhase.GameOver -> "over_${phase.winner}"
        else -> null
    }

    fun lineFor(state: GameState): NarratorLine? = when (val phase = state.phase) {
        is GamePhase.Handoff -> if (phase.kind != PassKind.NIGHT || state.settings.mafiaConfer) {
            null
        } else if (state.nightNumber == 1) {
            NarratorLine.START
        } else {
            NarratorLine.DISCUSS_OVER
        }
        GamePhase.NightSummary ->
            if (hasDeath(state.lastNightDeaths)) NarratorLine.DEATH_DAY else NarratorLine.LIFE_DAY
        GamePhase.Discuss -> NarratorLine.DISCUSS
        GamePhase.DaySummary ->
            if (hasDeath(state.lastDayDeaths)) NarratorLine.DEATH else NarratorLine.LIFE
        is GamePhase.GameOver -> when (phase.winner) {
            Winner.GOOD -> NarratorLine.CIVIL_WIN
            Winner.MAFIA -> NarratorLine.MAFIA_WIN
            Winner.JOKER, Winner.KILLER, Winner.WHORE -> NarratorLine.SPECIAL_WIN
        }
        else -> null
    }

    fun play(context: Context, line: NarratorLine, onDone: (() -> Unit)? = null) {
        val previous = pendingDone
        pendingDone = onDone
        releasePlayer()
        previous?.invoke()
        val clips = clipsFor(line)
        if (clips.isEmpty()) {
            finish()
            return
        }
        val mp = MediaPlayer.create(context.applicationContext, pick(line, clips))
        if (mp == null) {
            finish()
            return
        }
        player = mp
        mp.setOnCompletionListener { done ->
            if (player === done) player = null
            runCatching { done.release() }
            finish()
        }
        runCatching { mp.start() }.onFailure { finish() }
    }

    suspend fun await(context: Context, line: NarratorLine) {
        suspendCancellableCoroutine { cont ->
            play(context, line) {
                if (cont.isActive) cont.resume(Unit)
            }
            cont.invokeOnCancellation { stop() }
        }
    }

    fun stop() {
        val done = pendingDone
        pendingDone = null
        releasePlayer()
        done?.invoke()
    }

    private fun finish() {
        val done = pendingDone
        pendingDone = null
        done?.invoke()
    }

    private fun releasePlayer() {
        val mp = player
        player = null
        mp?.runCatching {
            setOnCompletionListener(null)
            if (isPlaying) stop()
            release()
        }
    }

    private fun pick(line: NarratorLine, clips: IntArray): Int {
        val last = lastClip[line]
        val pool = if (clips.size > 1 && last != null) clips.filter { it != last } else clips.toList()
        val chosen = pool.random()
        lastClip[line] = chosen
        return chosen
    }

    private fun hasDeath(deaths: List<DeathRecord>) =
        deaths.any { it.cause != DeathCause.BLOCKED && it.cause != DeathCause.SAVED }

    private fun clipsFor(line: NarratorLine): IntArray = when (line) {
        NarratorLine.START -> intArrayOf(
            R.raw.voice_start_0, R.raw.voice_start_1, R.raw.voice_start_2,
        )
        NarratorLine.DEATH -> intArrayOf(
            R.raw.voice_death_0, R.raw.voice_death_1, R.raw.voice_death_2, R.raw.voice_death_3,
        )
        NarratorLine.DEATH_DAY -> intArrayOf(
            R.raw.voice_death_day_0, R.raw.voice_death_day_1, R.raw.voice_death_day_2,
            R.raw.voice_death_day_3, R.raw.voice_death_day_4, R.raw.voice_death_day_5,
            R.raw.voice_death_day_6,
        )
        NarratorLine.LIFE -> intArrayOf(R.raw.voice_life_0, R.raw.voice_life_1)
        NarratorLine.LIFE_DAY -> intArrayOf(
            R.raw.voice_life_day_0, R.raw.voice_life_day_1,
            R.raw.voice_life_day_2, R.raw.voice_life_day_3,
        )
        NarratorLine.DISCUSS -> intArrayOf(
            R.raw.voice_discuss_0, R.raw.voice_discuss_1,
            R.raw.voice_discuss_2, R.raw.voice_discuss_3,
        )
        NarratorLine.DISCUSS_OVER -> intArrayOf(
            R.raw.voice_discuss_over_0, R.raw.voice_discuss_over_1, R.raw.voice_discuss_over_2,
        )
        NarratorLine.CIVIL_WIN -> intArrayOf(
            R.raw.voice_civil_win_0, R.raw.voice_civil_win_1,
            R.raw.voice_civil_win_2, R.raw.voice_civil_win_3,
        )
        NarratorLine.MAFIA_WIN -> intArrayOf(
            R.raw.voice_mafia_win_0, R.raw.voice_mafia_win_1,
            R.raw.voice_mafia_win_2, R.raw.voice_mafia_win_3,
        )
        NarratorLine.SPECIAL_WIN -> intArrayOf(
            R.raw.voice_special_win_0, R.raw.voice_special_win_1, R.raw.voice_special_win_2,
            R.raw.voice_special_win_3, R.raw.voice_special_win_4, R.raw.voice_special_win_5,
        )
        NarratorLine.SLEEP -> intArrayOf(R.raw.voice_sleep_0, R.raw.voice_sleep_1)
        NarratorLine.MAFIA_WAKEUP -> intArrayOf(R.raw.voice_mafia_wakeup_0, R.raw.voice_mafia_wakeup_1)
        NarratorLine.MAFIA_SLEEP -> intArrayOf(R.raw.voice_mafia_sleep_0, R.raw.voice_mafia_sleep_1)
        NarratorLine.WAKEUP -> intArrayOf(R.raw.voice_wakeup_0)
    }
}
