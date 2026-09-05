package com.secretmafia.game

object GameRules {
    const val MIN_PLAYERS = 4
    const val MAX_PLAYERS = 16

    private val recommendedCore: Map<Int, RoleCounts> = mapOf(
        4 to RoleCounts(1, 1, 1, 1),
        5 to RoleCounts(1, 1, 1, 2),
        6 to RoleCounts(2, 1, 1, 2),
        7 to RoleCounts(2, 1, 1, 3),
        8 to RoleCounts(2, 1, 1, 4),
        9 to RoleCounts(3, 1, 1, 4),
        10 to RoleCounts(3, 1, 1, 5),
        11 to RoleCounts(3, 1, 1, 6),
        12 to RoleCounts(3, 1, 1, 7),
        13 to RoleCounts(3, 1, 1, 8),
        14 to RoleCounts(4, 1, 1, 8),
        15 to RoleCounts(4, 1, 1, 9),
        16 to RoleCounts(4, 1, 1, 10),
    )

    fun recommendedCounts(playerCount: Int): RoleCounts {
        recommendedCore[playerCount]?.let { return it }
        val mafia = (playerCount / 4).coerceAtLeast(1)
        return RoleCounts(mafia, 1, 1, (playerCount - mafia - 2).coerceAtLeast(0))
    }

    fun assignRoles(names: List<String>, counts: RoleCounts): List<Player> {
        require(names.size == counts.total) {
            "Role total ${counts.total} must match ${names.size} players"
        }
        require(isTwinCountOk(counts[Role.TWIN_CIVIL])) { "Civilian twins must be 0 or 2" }
        require(isTwinCountOk(counts[Role.TWIN_MAFIA])) { "Mafia twins must be 0 or 2" }
        val bag = buildList {
            Role.entries.forEach { role -> repeat(counts[role]) { add(role) } }
        }.shuffled()
        return names.mapIndexed { index, name ->
            val role = bag[index]
            Player(
                name = name.trim(),
                role = role,
                twinId = when (role) {
                    Role.TWIN_CIVIL -> "twin-civil"
                    Role.TWIN_MAFIA -> "twin-mafia"
                    else -> null
                },
            )
        }
    }

    fun isTwinCountOk(n: Int) = n == 0 || n == 2

    /** Only good vs evil. Wild / neutrals are the host's call. */
    fun tooManyEvil(counts: RoleCounts): Boolean =
        counts.evil > 0 && counts.good < counts.evil * 1.5

    fun maxEvilFor(playerCount: Int): Int {
        val rec = recommendedCounts(playerCount)
        return rec.evil.coerceAtLeast(1)
    }

    fun livingInOrder(players: List<Player>, startIndex: Int): List<Player> {
        if (players.isEmpty()) return emptyList()
        val n = players.size
        val start = startIndex.mod(n)
        return (0 until n).map { players[(start + it) % n] }.filter { it.alive }
    }

    fun nextStartIndex(players: List<Player>, currentStart: Int): Int {
        if (players.none { it.alive }) return currentStart
        val n = players.size
        var idx = (currentStart + 1).mod(n)
        repeat(n) {
            if (players[idx].alive) return idx
            idx = (idx + 1).mod(n)
        }
        return currentStart
    }

    fun winner(players: List<Player>): Winner? {
        val living = players.filter { it.alive }
        if (living.size == 1 && living.first().role == Role.KILLER) return Winner.KILLER
        val evil = living.count { it.role?.team == Team.EVIL }
        val good = living.count { it.role?.team == Team.GOOD }
        val killerAlive = living.any { it.role == Role.KILLER }
        if (evil == 0 && !killerAlive) return Winner.GOOD
        if (evil > 0 && evil >= good) return Winner.MAFIA
        return null
    }

    fun pickMafiaVictim(players: List<Player>, votes: Map<String, String>): String? {
        if (votes.isEmpty()) return null
        val counts = votes.values.groupingBy { it }.eachCount()
        val max = counts.values.maxOrNull() ?: return null
        val tied = counts.filter { it.value == max }.keys.toList()
        if (tied.size == 1) return tied.first()
        val statusByTarget = tied.associateWith { targetId ->
            votes.filter { it.value == targetId }
                .maxOf { (voterId, _) -> players.firstOrNull { it.id == voterId }?.mafiaStatus ?: 0 }
        }
        val bestStatus = statusByTarget.values.maxOrNull() ?: 0
        return statusByTarget.filter { it.value == bestStatus }.keys.toList().random()
    }

    data class NightHits(
        val mafiaVictimId: String?,
        val killerVictimId: String?,
    )

    fun planNightHits(
        players: List<Player>,
        mafiaVotes: Map<String, String>,
        healTargetId: String?,
        bodyguardTargetId: String?,
        killerTargetId: String?,
        nightNumber: Int,
        firstNightKill: Boolean,
    ): NightHits {
        val rawMafia = if (nightNumber == 1 && !firstNightKill) {
            null
        } else {
            pickMafiaVictim(players, mafiaVotes)
        }
        val mafiaAfterHeal = if (rawMafia != null && rawMafia == healTargetId) null else rawMafia
        val killerActive = nightNumber % 2 == 0
        val rawKiller = if (killerActive) killerTargetId else null
        val killerAfterHeal = if (rawKiller != null && rawKiller == healTargetId) null else rawKiller
        return NightHits(mafiaAfterHeal, killerAfterHeal)
    }

    fun applyNightDeaths(
        players: List<Player>,
        hits: NightHits,
        bodyguardTargetId: String?,
    ): Pair<List<Player>, List<DeathRecord>> {
        val deaths = linkedMapOf<String, DeathCause>()
        fun mark(id: String?, cause: DeathCause) {
            if (id != null) deaths.putIfAbsent(id, cause)
        }

        if (hits.mafiaVictimId != null) {
            if (bodyguardTargetId != null && bodyguardTargetId == hits.mafiaVictimId) {
                val guard = players.firstOrNull { it.role == Role.BODYGUARD && it.alive }
                if (guard != null && guard.id != hits.mafiaVictimId) {
                    mark(guard.id, DeathCause.BODYGUARD)
                } else {
                    mark(hits.mafiaVictimId, DeathCause.KILLED)
                }
            } else {
                mark(hits.mafiaVictimId, DeathCause.KILLED)
            }
        }
        if (hits.killerVictimId != null && hits.killerVictimId !in deaths) {
            mark(hits.killerVictimId, DeathCause.KILLED)
        }
        addTwinDeaths(players, deaths)

        val records = deaths.map { (id, cause) ->
            val p = players.first { it.id == id }
            DeathRecord(p.name, p.role, cause)
        }
        val next = players.map { if (it.id in deaths) it.copy(alive = false) else it }
        return next to records
    }

    fun applyDayExile(
        players: List<Player>,
        victimId: String?,
        lawyerShieldTargetId: String?,
    ): Pair<List<Player>, List<DeathRecord>> {
        if (victimId == null) return players to emptyList()
        if (victimId == lawyerShieldTargetId) {
            val p = players.first { it.id == victimId }
            return players to listOf(DeathRecord(p.name, p.role, DeathCause.BLOCKED))
        }
        val deaths = linkedMapOf(victimId to DeathCause.EXILED)
        addTwinDeaths(players, deaths)
        val records = deaths.map { (id, cause) ->
            val p = players.first { it.id == id }
            DeathRecord(p.name, p.role, cause)
        }
        val next = players.map { if (it.id in deaths) it.copy(alive = false) else it }
        return next to records
    }

    fun applyHunterShot(players: List<Player>, targetId: String): Pair<List<Player>, List<DeathRecord>> {
        val deaths = linkedMapOf(targetId to DeathCause.HUNTER)
        addTwinDeaths(players, deaths)
        val records = deaths.map { (id, cause) ->
            val p = players.first { it.id == id }
            DeathRecord(p.name, p.role, cause)
        }
        val next = players.map { if (it.id in deaths) it.copy(alive = false) else it }
        return next to records
    }

    private fun addTwinDeaths(players: List<Player>, deaths: MutableMap<String, DeathCause>) {
        var changed = true
        while (changed) {
            changed = false
            val extra = players.filter { p ->
                p.alive && p.id !in deaths && p.twinId != null &&
                    players.any { o -> o.twinId == p.twinId && o.id != p.id && o.id in deaths }
            }
            extra.forEach {
                deaths[it.id] = DeathCause.TWIN
                changed = true
            }
        }
    }

    fun firstDeadHunter(before: List<Player>, after: List<Player>): Player? {
        val newlyDead = before.filter { old ->
            old.alive && after.none { it.id == old.id && it.alive }
        }
        return newlyDead.firstOrNull { it.role == Role.HUNTER }
    }

    fun mafiaVoteCounts(players: List<Player>, votes: Map<String, String>): List<Pair<String, Int>> {
        return votes.values
            .groupingBy { it }
            .eachCount()
            .map { (id, count) ->
                val name = players.firstOrNull { it.id == id }?.name ?: "?"
                name to count
            }
            .sortedByDescending { it.second }
    }

    fun resolveDayPhoneVote(votes: Map<String, String>): String? {
        if (votes.isEmpty()) return null
        val counts = votes.values.groupingBy { it }.eachCount()
        val max = counts.values.maxOrNull() ?: return null
        val tied = counts.filter { it.value == max }.keys.toList()
        return if (tied.size == 1) tied.first() else null
    }

    fun mathPuzzle(): MathPuzzle {
        val left = (1..9).random()
        val right = (1..9).random()
        val correct = left + right
        val wrong = generateSequence { (correct + (-4..4).random()).coerceAtLeast(0) }
            .filter { it != correct }
            .distinct()
            .take(2)
            .toList()
        return MathPuzzle(left, right, (wrong + correct).shuffled(), correct)
    }

    fun dummyFor(settings: AppSettings): DummyActionType {
        return if (settings.dummyActionType == DummyActionType.RANDOM) {
            if ((0..1).random() == 0) DummyActionType.LIKE else DummyActionType.MATH
        } else {
            settings.dummyActionType
        }
    }

    fun shownRole(player: Player, nightNumber: Int): Role? {
        return when (player.role) {
            Role.LUNATIC -> Role.CIVILIAN
            Role.DRUNK -> if (nightNumber >= 3) Role.DRUNK else null
            else -> player.role
        }
    }

    fun copSeesEvil(target: Player): Boolean = target.role?.team == Team.EVIL

    fun killerActsTonight(nightNumber: Int): Boolean = nightNumber % 2 == 0
}
