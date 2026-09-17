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
    WHORE(Team.NEUTRAL, 0, false, false),
    MAYOR(Team.GOOD, 0, false, false),
    NECROMANCER(Team.GOOD, 0, false, false),
    VIGILANTE(Team.GOOD, 0, false, false),
    FRAMER(Team.EVIL, 1, true, false),
    TRAITOR(Team.EVIL, 0, false, false),
    POISONER(Team.EVIL, 0, false, false),
    CURSED(Team.NEUTRAL, 0, false, false),
    SURVIVOR(Team.NEUTRAL, 0, false, false),
    AMNESIAC(Team.NEUTRAL, 0, false, false),
    ;

    val isMafiaVoter: Boolean get() = mafiaKnown
    val isFree: Boolean get() = core || this == JOKER
}

enum class WhoreAlign {
    GOOD, EVIL, PEST, SOLO, PICK;

    val team: Team? get() = when (this) {
        GOOD -> Team.GOOD
        EVIL -> Team.EVIL
        PEST, SOLO, PICK -> null
    }

    fun next(): WhoreAlign = when (this) {
        GOOD -> EVIL
        EVIL -> PEST
        PEST -> SOLO
        SOLO -> PICK
        PICK -> GOOD
    }
}

enum class DummyActionType { LIKE, MATH, RANDOM }

enum class DayVoteMode { LIVE, PHONE }

enum class AppLang { EN, SR, IT, ES, DE, FR, ZH;

    fun next(): AppLang = entries[(ordinal + 1) % entries.size]

    val nativeName: String get() = when (this) {
        EN -> "ENGLISH"
        SR -> "SRPSKI"
        IT -> "ITALIANO"
        ES -> "ESPAÑOL"
        DE -> "DEUTSCH"
        FR -> "FRANÇAIS"
        ZH -> "中文"
    }
}

enum class Winner { GOOD, MAFIA, JOKER, KILLER, WHORE }

enum class DeathCause { KILLED, EXILED, TWIN, BODYGUARD, HUNTER, SAVED, BLOCKED }

data class Player(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val role: Role? = null,
    val alive: Boolean = true,
    val twinId: String? = null,
    val femaleArt: Boolean = false,
    val align: WhoreAlign? = null,
) {
    val mafiaStatus: Int get() = role?.mafiaStatus ?: 0
    val side: Team get() = when {
        role != Role.WHORE -> role?.team ?: Team.NEUTRAL
        align == WhoreAlign.GOOD -> Team.GOOD
        align == WhoreAlign.EVIL -> Team.EVIL
        else -> Team.NEUTRAL
    }
    val soloWhore: Boolean get() = role == Role.WHORE && align == WhoreAlign.SOLO
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
    val neutral: Int get() = Role.entries.filter { it.team == Team.NEUTRAL }.sumOf { this[it] }
    val advancedTotal: Int get() = Role.entries.filter { !it.core }.sumOf { this[it] }
    val balanced: Boolean get() = evil == 0 || (total - evil) > evil

    fun withoutAdvanced(): RoleCounts {
        var next = this
        Role.entries.filter { !it.core }.forEach { role -> next = next.with(role, 0) }
        return next
    }

    fun encode(): String =
        Role.entries.filter { this[it] > 0 }.joinToString(",") { "${it.name}:${this[it]}" }

    fun onlyOwned(owns: (Role) -> Boolean): RoleCounts {
        var next = this
        Role.entries.filter { !it.core }.forEach { role ->
            if (!owns(role) && next[role] > 0) {
                next = next.with(role, 0)
            }
        }
        return next
    }

    companion object {
        fun decode(raw: String): RoleCounts? {
            if (raw.isBlank()) return null
            val map = mutableMapOf<Role, Int>()
            raw.split(",").forEach { part ->
                val bits = part.split(":")
                if (bits.size != 2) return@forEach
                val role = runCatching { Role.valueOf(bits[0]) }.getOrNull() ?: return@forEach
                val n = bits[1].toIntOrNull()?.coerceAtLeast(0) ?: return@forEach
                if (n > 0) map[role] = n
            }
            return if (map.isEmpty()) null else RoleCounts(map)
        }
    }
}

/** Spawn % for advanced roles (0–100, steps of 10). Core roles are always 100%. */
data class RoleChances(val amounts: Map<Role, Int> = emptyMap()) {
    operator fun get(role: Role): Int =
        if (role.core) 100 else (amounts[role] ?: DEFAULT)

    fun with(role: Role, percent: Int): RoleChances {
        if (role.core) return this
        val stepped = ((percent.coerceIn(0, 100) + 5) / 10) * 10
        return copy(amounts = amounts + (role to stepped))
    }

    fun encode(): String =
        Role.entries.filter { !it.core && this[it] != DEFAULT }
            .joinToString(",") { "${it.name}:${this[it]}" }

    companion object {
        const val DEFAULT = 50

        fun decode(raw: String): RoleChances {
            if (raw.isBlank()) return RoleChances()
            val map = mutableMapOf<Role, Int>()
            raw.split(",").forEach { part ->
                val bits = part.split(":")
                if (bits.size != 2) return@forEach
                val role = runCatching { Role.valueOf(bits[0]) }.getOrNull() ?: return@forEach
                if (role.core) return@forEach
                val n = bits[1].toIntOrNull() ?: return@forEach
                map[role] = ((n.coerceIn(0, 100) + 5) / 10) * 10
            }
            return RoleChances(map)
        }
    }
}

data class Wallet(
    val blood: Int = 0,
    val town: Int = 0,
    val gold: Int = 0,
    val unlocked: Set<Role> = emptySet(),
) {
    fun owns(role: Role) = role.isFree || role in unlocked
    fun amount(kind: CoinKind) = when (kind) {
        CoinKind.BLOOD -> blood
        CoinKind.TOWN -> town
        CoinKind.GOLD -> gold
    }
    fun spend(kind: CoinKind, n: Int): Wallet? {
        val have = amount(kind)
        if (have < n) return null
        return when (kind) {
            CoinKind.BLOOD -> copy(blood = have - n)
            CoinKind.TOWN -> copy(town = have - n)
            CoinKind.GOLD -> copy(gold = have - n)
        }
    }
}

data class Profile(
    val name: String = "",
    val avatarId: Int = 0,
    val playSignedIn: Boolean = false,
) {
    val displayName: String get() = name.ifBlank { "" }
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
    val vibrationEnabled: Boolean = true,
    val narratorEnabled: Boolean = true,
    val lightTheme: Boolean = false,
    val language: AppLang = AppLang.EN,
    val hideGameColors: Boolean = false,
    val whoreAlign: WhoreAlign = WhoreAlign.PICK,
    val mafiaConfer: Boolean = false,
    val mafiaConferSeconds: Int = 12,
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
    data object MafiaConfer : GamePhase()
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
    val actorRole: Role? = null,
)

enum class ActionAnimation { KNIFE, HEAL, INSPECT, SEER, LIKE, MATH, VOTE, SHIELD, GUARD, HUNT, SLEEP, REVEAL, POISON, FRAME, RISE }

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
    val whoreTargetId: String? = null,
    val lastWhoreTargetId: String? = null,
    val lawyerShieldTargetId: String? = null,
    val lawyerShieldUsed: Boolean = false,
    val lawyerPickingShield: Boolean = false,
    val framedId: String? = null,
    val framerPicking: Boolean = false,
    val mayorRevealed: Boolean = false,
    val vigilanteTargetId: String? = null,
    val vigilanteUsed: Boolean = false,
    val poisonTargetId: String? = null,
    val poisonDueId: String? = null,
    val survivorLived: Boolean = false,
    val dayVotes: Map<String, String> = emptyMap(),
    val lastNightDeaths: List<DeathRecord> = emptyList(),
    val lastDayDeaths: List<DeathRecord> = emptyList(),
    val pendingHunterId: String? = null,
    val hunterResume: HunterResume? = null,
    val lastAction: NightActionResult? = null,
    val dummyOverride: DummyActionType? = null,
    val startedAtMillis: Long = 0L,
    val matchReward: MatchReward? = null,
    val matchPaid: Boolean = false,
)

enum class HunterResume { NIGHT, DAY }
