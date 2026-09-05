package com.secretmafia.game

import java.util.UUID

enum class Team { GOOD, EVIL, NEUTRAL }

enum class Role(
    val team: Team,
    val mafiaStatus: Int,
    val mafiaKnown: Boolean,
    val core: Boolean,
) {
    MAFIA(Team.EVIL, 1, true, true),
    DON(Team.EVIL, 2, true, false),
    LAWYER(Team.EVIL, 1, true, false),
    HEALER(Team.GOOD, 0, false, true),
    COP(Team.GOOD, 0, false, true),
    CIVILIAN(Team.GOOD, 0, false, true),
    HUNTER(Team.GOOD, 0, false, false),
    SEER(Team.GOOD, 0, false, false),
    BODYGUARD(Team.GOOD, 0, false, false),
    JOKER(Team.NEUTRAL, 0, false, false),
    KILLER(Team.NEUTRAL, 0, false, false),
    LUNATIC(Team.EVIL, 0, false, false),
    DRUNK(Team.GOOD, 0, false, false),
    TWIN_CIVIL(Team.GOOD, 0, false, false),
    TWIN_MAFIA(Team.EVIL, 1, true, false),
    ;

    val isMafiaVoter: Boolean get() = mafiaKnown
}

enum class DummyActionType { LIKE, MATH, RANDOM }

enum class DayVoteMode { LIVE, PHONE }

enum class AppLang { EN, SR }

enum class Winner { GOOD, MAFIA, JOKER, KILLER }

enum class DeathCause { KILLED, EXILED, TWIN, BODYGUARD, HUNTER, SAVED, BLOCKED }

data class Player(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val role: Role? = null,
    val alive: Boolean = true,
    val twinId: String? = null,
) {
    val mafiaStatus: Int get() = role?.mafiaStatus ?: 0
}

data class RoleCounts(val amounts: Map<Role, Int> = emptyMap()) {
    constructor(mafia: Int, healer: Int, cop: Int, civilian: Int) : this(
        mapOf(
            Role.MAFIA to mafia,
            Role.HEALER to healer,
            Role.COP to cop,
            Role.CIVILIAN to civilian,
        ),
    )

    operator fun get(role: Role): Int = amounts[role] ?: 0

    fun with(role: Role, value: Int): RoleCounts =
        copy(amounts = amounts + (role to value.coerceAtLeast(0)))

    val total: Int get() = Role.entries.sumOf { this[it] }
    val good: Int get() = Role.entries.filter { it.team == Team.GOOD }.sumOf { this[it] }
    val evil: Int get() = Role.entries.filter { it.team == Team.EVIL }.sumOf { this[it] }
    val advancedTotal: Int get() = Role.entries.filter { !it.core }.sumOf { this[it] }
    val balanced: Boolean get() = evil == 0 || good >= evil * 1.5
}

data class AppSettings(
    val showMafiaVoteCount: Boolean = true,
    val discussTimerMinutes: Int = 0,
    val dummyActionType: DummyActionType = DummyActionType.LIKE,
    val revealRoleOnDeath: Boolean = false,
    val revealRolesAtEnd: Boolean = true,
    val firstNightKill: Boolean = true,
    val healerMayRepeatTarget: Boolean = false,
    val dayVoteMode: DayVoteMode = DayVoteMode.LIVE,
    val soundEnabled: Boolean = true,
    val lightTheme: Boolean = false,
    val language: AppLang = AppLang.EN,
    val hideGameColors: Boolean = false,
)

data class MathPuzzle(
    val left: Int,
    val right: Int,
    val options: List<Int>,
    val correct: Int,
) {
    val prompt: String get() = "$left + $right"
}

sealed class GamePhase {
    data class Handoff(val playerId: String, val kind: PassKind) : GamePhase()
    data class Unlock(val playerId: String, val kind: PassKind) : GamePhase()
    data class Action(val playerId: String, val kind: PassKind) : GamePhase()
    data class RevealWait(val playerId: String, val kind: PassKind) : GamePhase()
    data object NightSummary : GamePhase()
    data object Discuss : GamePhase()
    data object DayVoteLive : GamePhase()
    data object DaySummary : GamePhase()
    data class GameOver(val winner: Winner) : GamePhase()
}

enum class PassKind { NIGHT, DAY_VOTE, HUNTER }

data class NightActionResult(
    val animation: ActionAnimation,
    val inspectGood: Boolean? = null,
    val inspectName: String? = null,
    val inspectRoleTitle: String? = null,
)

enum class ActionAnimation { KNIFE, HEAL, INSPECT, SEER, LIKE, MATH, VOTE, SHIELD, GUARD, HUNT }

data class DeathRecord(
    val playerName: String,
    val role: Role?,
    val cause: DeathCause,
)

data class GameState(
    val settings: AppSettings,
    val players: List<Player>,
    val nightNumber: Int,
    val startIndex: Int,
    val phase: GamePhase,
    val mafiaVotes: Map<String, String> = emptyMap(),
    val healTargetId: String? = null,
    val lastHealTargetId: String? = null,
    val bodyguardTargetId: String? = null,
    val lastBodyguardTargetId: String? = null,
    val killerTargetId: String? = null,
    val lawyerShieldTargetId: String? = null,
    val lawyerShieldUsed: Boolean = false,
    val lawyerPickingShield: Boolean = false,
    val dayVotes: Map<String, String> = emptyMap(),
    val lastNightDeaths: List<DeathRecord> = emptyList(),
    val lastDayDeaths: List<DeathRecord> = emptyList(),
    val pendingHunterId: String? = null,
    val hunterResume: HunterResume? = null,
    val lastAction: NightActionResult? = null,
    val dummyOverride: DummyActionType? = null,
)

enum class HunterResume { NIGHT, DAY }
