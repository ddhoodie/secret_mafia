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

    fun assignRoles(
        names: List<String>,
        counts: RoleCounts,
        chances: RoleChances = RoleChances(),
        rng: kotlin.random.Random = kotlin.random.Random.Default,
    ): List<Player> {
        require(names.size == counts.total) {
            "Role total ${counts.total} must match ${names.size} players"
        }
        require(isTwinCountOk(counts[Role.TWIN_CIVIL])) { "Civilian twins must be even" }
        require(isTwinCountOk(counts[Role.TWIN_MAFIA])) { "Mafia twins must be even" }
        val bag = buildList {
            Role.entries.forEach { role ->
                val n = counts[role]
                if (n <= 0) return@forEach
                when {
                    role.core -> repeat(n) { add(role) }
                    role == Role.TWIN_CIVIL || role == Role.TWIN_MAFIA -> {
                        repeat(n / 2) {
                            val keep = rng.nextInt(100) < chances[role]
                            val pick = if (keep) role else fallbackRole(role)
                            add(pick)
                            add(pick)
                        }
                    }
                    else -> repeat(n) {
                        add(if (rng.nextInt(100) < chances[role]) role else fallbackRole(role))
                    }
                }
            }
        }.shuffled(rng)
        val assigned = names.mapIndexed { index, name ->
            val role = bag[index]
            Player(
                name = name.trim(),
                role = role,
                femaleArt = when (role) {
                    Role.CIVILIAN, Role.LUNATIC -> rng.nextBoolean()
                    Role.WHORE -> true
                    else -> false
                },
            )
        }
        return pairTwins(assigned)
    }

    private fun pairTwins(players: List<Player>): List<Player> {
        val ids = mutableMapOf<String, String>()
        fun pairRole(role: Role, prefix: String) {
            players.filter { it.role == role }.chunked(2).forEachIndexed { i, pair ->
                if (pair.size < 2) return@forEachIndexed
                val tid = "$prefix-$i"
                pair.forEach { ids[it.id] = tid }
            }
        }
        pairRole(Role.TWIN_CIVIL, "twin-civil")
        pairRole(Role.TWIN_MAFIA, "twin-mafia")
        if (ids.isEmpty()) return players
        return players.map { p -> ids[p.id]?.let { p.copy(twinId = it) } ?: p }
    }

    fun fallbackRole(role: Role): Role = when (role.team) {
        Team.EVIL -> Role.MAFIA
        Team.GOOD, Team.NEUTRAL -> Role.CIVILIAN
    }

    fun isTwinCountOk(n: Int) = n >= 0 && n % 2 == 0

    /** Need strictly more non-evil than evil (neutrals count as non-evil). */
    fun tooManyEvil(counts: RoleCounts): Boolean {
        if (counts.total <= 0) return false
        val nonEvil = counts.total - counts.evil
        return counts.evil > 0 && nonEvil <= counts.evil
    }

    fun recommendedTeamSplit(playerCount: Int): Pair<Int, Int> {
        val rec = recommendedCounts(playerCount)
        return rec.evil to rec.good
    }

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
        if (living.any { it.soloWhore } && living.size <= 2) return Winner.WHORE
        if (living.size == 1 && living.first().role == Role.KILLER) return Winner.KILLER
        val evil = living.count { it.side == Team.EVIL }
        val good = living.count { it.side == Team.GOOD }
        val killerAlive = living.any { it.role == Role.KILLER }
        val soloAlive = living.any { it.soloWhore }
        if (evil == 0 && !killerAlive && !soloAlive) return Winner.GOOD
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
        val vigVictimId: String? = null,
        val poisonVictimId: String? = null,
    )

    fun planNightHits(
        players: List<Player>,
        mafiaVotes: Map<String, String>,
        healTargetId: String?,
        bodyguardTargetId: String?,
        killerTargetId: String?,
        nightNumber: Int,
        firstNightKill: Boolean,
        vigilanteTargetId: String? = null,
        poisonDueId: String? = null,
    ): NightHits {
        val rawMafia = if (nightNumber == 1 && !firstNightKill) {
            null
        } else {
            pickMafiaVictim(players, mafiaVotes)
        }
        fun afterHeal(id: String?) = if (id != null && id == healTargetId) null else id
        val killerActive = nightNumber % 2 == 0
        val rawKiller = if (killerActive) killerTargetId else null
        return NightHits(
            mafiaVictimId = afterHeal(rawMafia),
            killerVictimId = afterHeal(rawKiller),
            vigVictimId = afterHeal(vigilanteTargetId),
            poisonVictimId = afterHeal(poisonDueId),
        )
    }

    fun applyNightDeaths(
        players: List<Player>,
        hits: NightHits,
        bodyguardTargetId: String?,
    ): Pair<List<Player>, List<DeathRecord>> {
        val deaths = linkedMapOf<String, DeathCause>()
        fun mark(id: String?, cause: DeathCause) {
            if (id != null && players.any { it.id == id && it.alive }) deaths.putIfAbsent(id, cause)
        }

        if (hits.mafiaVictimId != null) {
            val cursed = players.firstOrNull { it.id == hits.mafiaVictimId && it.alive && it.role == Role.CURSED }
            val guarded = bodyguardTargetId != null && bodyguardTargetId == hits.mafiaVictimId
            if (cursed != null && !guarded) {
                // convert later via players map
            } else if (guarded) {
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
        listOf(hits.killerVictimId, hits.vigVictimId, hits.poisonVictimId).forEach { id ->
            if (id != null && id !in deaths) mark(id, DeathCause.KILLED)
        }
        addTwinDeaths(players, deaths)

        val records = deaths.map { (id, cause) ->
            val p = players.first { it.id == id }
            DeathRecord(p.name, p.role, cause)
        }
        val convertId = hits.mafiaVictimId?.takeIf { id ->
            id !in deaths &&
                bodyguardTargetId != id &&
                players.any { it.id == id && it.alive && it.role == Role.CURSED }
        }
        val next = players.map { p ->
            when {
                p.id in deaths -> p.copy(alive = false)
                p.id == convertId -> p.copy(role = Role.MAFIA)
                else -> p
            }
        }
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

    fun resolveDayPhoneVote(votes: Map<String, String>, doubleVoterId: String? = null): String? {
        if (votes.isEmpty()) return null
        val weighted = buildList {
            votes.forEach { (voter, target) ->
                add(target)
                if (voter == doubleVoterId) add(target)
            }
        }
        val counts = weighted.groupingBy { it }.eachCount()
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

    fun copSeesEvil(target: Player, framedId: String? = null): Boolean {
        if (target.id == framedId) return true
        if (target.role == Role.TRAITOR) return false
        return target.side == Team.EVIL
    }

    fun killerActsTonight(nightNumber: Int): Boolean = nightNumber % 2 == 0

    fun whoreTargets(living: List<Player>, actorId: String, lastTargetId: String?): List<Player> =
        living.filter { it.id != actorId && it.id != lastTargetId }

    fun nightAfterBlock(
        players: List<Player>,
        blockedId: String?,
        mafiaVotes: Map<String, String>,
        healTargetId: String?,
        bodyguardTargetId: String?,
        killerTargetId: String?,
        vigilanteTargetId: String? = null,
        poisonTargetId: String? = null,
        framedId: String? = null,
    ): NightActions {
        if (blockedId == null) {
            return NightActions(
                mafiaVotes, healTargetId, bodyguardTargetId, killerTargetId,
                vigilanteTargetId, poisonTargetId, framedId,
            )
        }
        val role = players.firstOrNull { it.id == blockedId }?.role
        return NightActions(
            mafiaVotes = mafiaVotes - blockedId,
            healTargetId = if (role == Role.HEALER) null else healTargetId,
            bodyguardTargetId = if (role == Role.BODYGUARD) null else bodyguardTargetId,
            killerTargetId = if (role == Role.KILLER) null else killerTargetId,
            vigilanteTargetId = if (role == Role.VIGILANTE) null else vigilanteTargetId,
            poisonTargetId = if (role == Role.POISONER) null else poisonTargetId,
            framedId = if (role == Role.FRAMER) null else framedId,
        )
    }

    data class NightActions(
        val mafiaVotes: Map<String, String>,
        val healTargetId: String?,
        val bodyguardTargetId: String?,
        val killerTargetId: String?,
        val vigilanteTargetId: String? = null,
        val poisonTargetId: String? = null,
        val framedId: String? = null,
    )

    fun amnesiacCanTake(role: Role?): Boolean {
        if (role == null) return false
        return role !in setOf(
            Role.AMNESIAC, Role.TWIN_CIVIL, Role.TWIN_MAFIA, Role.WHORE,
        )
    }

    fun deadTakeable(players: List<Player>): List<Player> =
        players.filter { !it.alive && amnesiacCanTake(it.role) }

    fun deadPlayers(players: List<Player>): List<Player> = players.filter { !it.alive }
}
