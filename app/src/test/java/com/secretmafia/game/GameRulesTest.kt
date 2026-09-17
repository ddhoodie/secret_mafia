package com.secretmafia.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameRulesTest {
    @Test
    fun tooManyEvilNeedsMoreNonEvil() {
        val unbalancedCore = RoleCounts(4, 1, 1, 0)
        assertTrue(GameRules.tooManyEvil(unbalancedCore))
        val withAdvancedStillBad = unbalancedCore.with(Role.HUNTER, 1)
        assertTrue(GameRules.tooManyEvil(withAdvancedStillBad))
        val heavy = RoleCounts(
            mapOf(
                Role.MAFIA to 2,
                Role.DON to 1,
                Role.LAWYER to 1,
                Role.TWIN_MAFIA to 2,
                Role.HEALER to 1,
                Role.COP to 1,
                Role.JOKER to 1,
            ),
        )
        // 6 evil, 3 non-evil
        assertTrue(GameRules.tooManyEvil(heavy))
        val okAdvanced = RoleCounts(
            mapOf(
                Role.MAFIA to 2,
                Role.HEALER to 1,
                Role.COP to 1,
                Role.CIVILIAN to 2,
                Role.HUNTER to 1,
                Role.JOKER to 1,
            ),
        )
        // 2 evil, 6 non-evil
        assertTrue(!GameRules.tooManyEvil(okAdvanced))
        assertEquals(2, GameRules.maxEvilFor(6))
        assertEquals(3 to 7, GameRules.recommendedTeamSplit(10))
        val ok = RoleCounts(2, 1, 1, 2)
        assertTrue(!GameRules.tooManyEvil(ok))
        val equal = RoleCounts(3, 1, 1, 1)
        assertTrue(GameRules.tooManyEvil(equal))
    }

    @Test
    fun recommendedSixPlayers() {
        val counts = GameRules.recommendedCounts(6)
        assertEquals(2, counts[Role.MAFIA])
        assertEquals(1, counts[Role.HEALER])
        assertEquals(1, counts[Role.COP])
        assertEquals(2, counts[Role.CIVILIAN])
        assertTrue(counts.balanced)
    }

    @Test
    fun assignRolesMatchesCounts() {
        val names = listOf("A", "B", "C", "D", "E", "F")
        val players = GameRules.assignRoles(names, RoleCounts(2, 1, 1, 2))
        assertEquals(2, players.count { it.role == Role.MAFIA })
        assertEquals(1, players.count { it.role == Role.HEALER })
        assertEquals(1, players.count { it.role == Role.COP })
        assertEquals(2, players.count { it.role == Role.CIVILIAN })
    }

    @Test
    fun nightKillPicksMajority() {
        val players = listOf(
            Player(id = "m1", name = "M1", role = Role.MAFIA),
            Player(id = "m2", name = "M2", role = Role.MAFIA),
            Player(id = "c1", name = "C1", role = Role.CIVILIAN),
        )
        val hits = GameRules.planNightHits(
            players, mapOf("m1" to "c1", "m2" to "c1"),
            healTargetId = null, bodyguardTargetId = null, killerTargetId = null,
            nightNumber = 1, firstNightKill = true,
        )
        assertEquals("c1", hits.mafiaVictimId)
    }

    @Test
    fun healBlocksKill() {
        val players = listOf(
            Player(id = "m1", name = "M1", role = Role.MAFIA),
            Player(id = "c1", name = "C1", role = Role.CIVILIAN),
        )
        val hits = GameRules.planNightHits(
            players, mapOf("m1" to "c1"),
            healTargetId = "c1", bodyguardTargetId = null, killerTargetId = null,
            nightNumber = 2, firstNightKill = true,
        )
        assertNull(hits.mafiaVictimId)
    }

    @Test
    fun bodyguardDiesInstead() {
        val players = listOf(
            Player(id = "m1", name = "M1", role = Role.MAFIA),
            Player(id = "c1", name = "C1", role = Role.CIVILIAN),
            Player(id = "b1", name = "B1", role = Role.BODYGUARD),
        )
        val hits = GameRules.NightHits("c1", null)
        val (next, deaths) = GameRules.applyNightDeaths(players, hits, "c1")
        assertTrue(next.first { it.id == "c1" }.alive)
        assertTrue(!next.first { it.id == "b1" }.alive)
        assertEquals(DeathCause.BODYGUARD, deaths.first().cause)
    }

    @Test
    fun donBreaksTie() {
        val players = listOf(
            Player(id = "d1", name = "Don", role = Role.DON),
            Player(id = "m1", name = "M1", role = Role.MAFIA),
            Player(id = "c1", name = "C1", role = Role.CIVILIAN),
            Player(id = "c2", name = "C2", role = Role.CIVILIAN),
        )
        val victim = GameRules.pickMafiaVictim(players, mapOf("d1" to "c1", "m1" to "c2"))
        assertEquals("c1", victim)
    }

    @Test
    fun twinsDieTogether() {
        val players = listOf(
            Player(id = "t1", name = "T1", role = Role.TWIN_CIVIL, twinId = "twin-civil-0"),
            Player(id = "t2", name = "T2", role = Role.TWIN_CIVIL, twinId = "twin-civil-0"),
            Player(id = "m1", name = "M1", role = Role.MAFIA),
        )
        val (next, deaths) = GameRules.applyNightDeaths(players, GameRules.NightHits("t1", null), null)
        assertTrue(next.none { it.alive && it.role == Role.TWIN_CIVIL })
        assertEquals(2, deaths.size)
    }

    @Test
    fun twinPairsDieSeparately() {
        val players = listOf(
            Player(id = "a1", name = "A1", role = Role.TWIN_CIVIL, twinId = "twin-civil-0"),
            Player(id = "a2", name = "A2", role = Role.TWIN_CIVIL, twinId = "twin-civil-0"),
            Player(id = "b1", name = "B1", role = Role.TWIN_CIVIL, twinId = "twin-civil-1"),
            Player(id = "b2", name = "B2", role = Role.TWIN_CIVIL, twinId = "twin-civil-1"),
            Player(id = "m1", name = "M1", role = Role.MAFIA),
        )
        val (next, deaths) = GameRules.applyNightDeaths(players, GameRules.NightHits("a1", null), null)
        assertTrue(!next.first { it.id == "a1" }.alive)
        assertTrue(!next.first { it.id == "a2" }.alive)
        assertTrue(next.first { it.id == "b1" }.alive)
        assertTrue(next.first { it.id == "b2" }.alive)
        assertEquals(2, deaths.size)
    }

    @Test
    fun twinCountMustBeEven() {
        assertTrue(GameRules.isTwinCountOk(0))
        assertTrue(GameRules.isTwinCountOk(2))
        assertTrue(GameRules.isTwinCountOk(4))
        assertTrue(GameRules.isTwinCountOk(8))
        assertTrue(!GameRules.isTwinCountOk(1))
        assertTrue(!GameRules.isTwinCountOk(3))
    }

    @Test
    fun assignRolesPairsMultipleTwins() {
        val names = listOf("A", "B", "C", "D", "E", "F", "G", "H")
        val counts = RoleCounts(
            mapOf(
                Role.MAFIA to 2,
                Role.HEALER to 1,
                Role.COP to 1,
                Role.TWIN_CIVIL to 4,
            ),
        )
        val players = GameRules.assignRoles(
            names,
            counts,
            RoleChances().with(Role.TWIN_CIVIL, 100),
            rng = kotlin.random.Random(1),
        )
        val twins = players.filter { it.role == Role.TWIN_CIVIL }
        assertEquals(4, twins.size)
        val pairIds = twins.map { it.twinId }.toSet()
        assertEquals(2, pairIds.size)
        pairIds.forEach { id ->
            assertEquals(2, twins.count { it.twinId == id })
        }
    }

    @Test
    fun jokerDoesNotWinOnNightKill() {
        val players = listOf(
            Player(id = "j", name = "J", role = Role.JOKER, alive = false),
            Player(id = "m", name = "M", role = Role.MAFIA, alive = true),
            Player(id = "c", name = "C", role = Role.CIVILIAN, alive = true),
        )
        assertEquals(Winner.MAFIA, GameRules.winner(players))
    }

    @Test
    fun killerWinsAlone() {
        val players = listOf(
            Player(id = "k", name = "K", role = Role.KILLER, alive = true),
            Player(id = "c", name = "C", role = Role.CIVILIAN, alive = false),
        )
        assertEquals(Winner.KILLER, GameRules.winner(players))
    }

    @Test
    fun firstNightKillCanBeDisabled() {
        val players = listOf(
            Player(id = "m1", name = "M1", role = Role.MAFIA),
            Player(id = "c1", name = "C1", role = Role.CIVILIAN),
        )
        val hits = GameRules.planNightHits(
            players, mapOf("m1" to "c1"),
            healTargetId = null, bodyguardTargetId = null, killerTargetId = null,
            nightNumber = 1, firstNightKill = false,
        )
        assertNull(hits.mafiaVictimId)
    }

    @Test
    fun dayVoteTieNobody() {
        assertNull(GameRules.resolveDayPhoneVote(mapOf("a" to "x", "b" to "y")))
    }

    @Test
    fun lawyerBlocksExile() {
        val players = listOf(
            Player(id = "m1", name = "M1", role = Role.MAFIA),
            Player(id = "c1", name = "C1", role = Role.CIVILIAN),
        )
        val (next, deaths) = GameRules.applyDayExile(players, "m1", "m1")
        assertTrue(next.first { it.id == "m1" }.alive)
        assertEquals(DeathCause.BLOCKED, deaths.first().cause)
    }

    @Test
    fun lunaticCountsAsEvil() {
        val players = listOf(
            Player(id = "l", name = "L", role = Role.LUNATIC, alive = true),
            Player(id = "c", name = "C", role = Role.CIVILIAN, alive = true),
        )
        assertEquals(Winner.MAFIA, GameRules.winner(players))
    }

    @Test
    fun drunkHiddenUntilNightThree() {
        val drunk = Player(id = "d", name = "D", role = Role.DRUNK)
        assertNull(GameRules.shownRole(drunk, 1))
        assertEquals(Role.DRUNK, GameRules.shownRole(drunk, 3))
        assertEquals(Role.CIVILIAN, GameRules.shownRole(Player(id = "x", name = "X", role = Role.LUNATIC), 1))
    }

    @Test
    fun whoreCannotRepeatLastTarget() {
        val living = listOf(
            Player(id = "w", name = "W", role = Role.WHORE),
            Player(id = "a", name = "A", role = Role.HEALER),
            Player(id = "b", name = "B", role = Role.COP),
        )
        val first = GameRules.whoreTargets(living, "w", null)
        assertEquals(listOf("a", "b"), first.map { it.id })
        val second = GameRules.whoreTargets(living, "w", "a")
        assertEquals(listOf("b"), second.map { it.id })
    }

    @Test
    fun whoreBlockCancelsHeal() {
        val players = listOf(
            Player(id = "h", name = "H", role = Role.HEALER),
            Player(id = "m", name = "M", role = Role.MAFIA),
            Player(id = "c", name = "C", role = Role.CIVILIAN),
        )
        val blocked = GameRules.nightAfterBlock(
            players = players,
            blockedId = "h",
            mafiaVotes = mapOf("m" to "c"),
            healTargetId = "c",
            bodyguardTargetId = null,
            killerTargetId = null,
        )
        assertNull(blocked.healTargetId)
        assertEquals(mapOf("m" to "c"), blocked.mafiaVotes)
    }

    @Test
    fun whoreBlockCancelsMafiaVote() {
        val players = listOf(
            Player(id = "m", name = "M", role = Role.MAFIA),
            Player(id = "d", name = "D", role = Role.DON),
            Player(id = "c", name = "C", role = Role.CIVILIAN),
        )
        val blocked = GameRules.nightAfterBlock(
            players = players,
            blockedId = "m",
            mafiaVotes = mapOf("m" to "c", "d" to "c"),
            healTargetId = null,
            bodyguardTargetId = null,
            killerTargetId = null,
        )
        assertEquals(mapOf("d" to "c"), blocked.mafiaVotes)
    }

    @Test
    fun whoreSideWinsWithTownOrMafia() {
        val goodWhore = listOf(
            Player(id = "w", name = "W", role = Role.WHORE, align = WhoreAlign.GOOD),
            Player(id = "c", name = "C", role = Role.CIVILIAN),
            Player(id = "m", name = "M", role = Role.MAFIA, alive = false),
        )
        assertEquals(Winner.GOOD, GameRules.winner(goodWhore))
        val evilWhore = listOf(
            Player(id = "w", name = "W", role = Role.WHORE, align = WhoreAlign.EVIL),
            Player(id = "c", name = "C", role = Role.CIVILIAN),
        )
        assertEquals(Winner.MAFIA, GameRules.winner(evilWhore))
        assertTrue(GameRules.copSeesEvil(Player(id = "w", name = "W", role = Role.WHORE, align = WhoreAlign.EVIL)))
        assertTrue(!GameRules.copSeesEvil(Player(id = "w", name = "W", role = Role.WHORE, align = WhoreAlign.GOOD)))
    }

    @Test
    fun pestWhoreNeverWins() {
        val lastPest = listOf(
            Player(id = "w", name = "W", role = Role.WHORE, align = WhoreAlign.PEST),
            Player(id = "c", name = "C", role = Role.CIVILIAN, alive = false),
        )
        assertEquals(Winner.GOOD, GameRules.winner(lastPest))
        val pestAndMafia = listOf(
            Player(id = "w", name = "W", role = Role.WHORE, align = WhoreAlign.PEST),
            Player(id = "m", name = "M", role = Role.MAFIA),
        )
        assertEquals(Winner.MAFIA, GameRules.winner(pestAndMafia))
        assertTrue(!GameRules.copSeesEvil(Player(id = "w", name = "W", role = Role.WHORE, align = WhoreAlign.PEST)))
    }

    @Test
    fun soloWhoreWinsOneVOne() {
        val duel = listOf(
            Player(id = "w", name = "W", role = Role.WHORE, align = WhoreAlign.SOLO),
            Player(id = "c", name = "C", role = Role.CIVILIAN),
        )
        assertEquals(Winner.WHORE, GameRules.winner(duel))
        val last = listOf(
            Player(id = "w", name = "W", role = Role.WHORE, align = WhoreAlign.SOLO),
            Player(id = "c", name = "C", role = Role.CIVILIAN, alive = false),
        )
        assertEquals(Winner.WHORE, GameRules.winner(last))
        val crowded = listOf(
            Player(id = "w", name = "W", role = Role.WHORE, align = WhoreAlign.SOLO),
            Player(id = "c1", name = "C1", role = Role.CIVILIAN),
            Player(id = "c2", name = "C2", role = Role.CIVILIAN),
        )
        assertNull(GameRules.winner(crowded))
    }

    @Test
    fun cursedConvertsOnMafiaHit() {
        val players = listOf(
            Player(id = "m", name = "M", role = Role.MAFIA),
            Player(id = "c", name = "C", role = Role.CURSED),
            Player(id = "t", name = "T", role = Role.CIVILIAN),
        )
        val (next, deaths) = GameRules.applyNightDeaths(
            players, GameRules.NightHits("c", null), null,
        )
        assertTrue(next.first { it.id == "c" }.alive)
        assertEquals(Role.MAFIA, next.first { it.id == "c" }.role)
        assertTrue(deaths.isEmpty())
    }

    @Test
    fun traitorLooksGoodFramedLooksBad() {
        val traitor = Player(id = "t", name = "T", role = Role.TRAITOR)
        val town = Player(id = "c", name = "C", role = Role.CIVILIAN)
        assertTrue(!GameRules.copSeesEvil(traitor))
        assertTrue(GameRules.copSeesEvil(town, framedId = "c"))
        assertTrue(GameRules.copSeesEvil(Player(id = "p", name = "P", role = Role.POISONER)))
    }

    @Test
    fun mayorPhoneVoteCountsTwice() {
        val votes = mapOf("mayor" to "a", "b" to "c")
        assertEquals("a", GameRules.resolveDayPhoneVote(votes, "mayor"))
        assertNull(GameRules.resolveDayPhoneVote(mapOf("mayor" to "a", "b" to "c", "d" to "c"), "mayor"))
    }

    @Test
    fun vigilanteAndPoisonHits() {
        val players = listOf(
            Player(id = "v", name = "V", role = Role.VIGILANTE),
            Player(id = "p", name = "P", role = Role.CIVILIAN),
            Player(id = "x", name = "X", role = Role.CIVILIAN),
        )
        val hits = GameRules.planNightHits(
            players, emptyMap(),
            healTargetId = "p", bodyguardTargetId = null, killerTargetId = null,
            nightNumber = 1, firstNightKill = true,
            vigilanteTargetId = "x", poisonDueId = "p",
        )
        assertEquals("x", hits.vigVictimId)
        assertNull(hits.poisonVictimId)
        val raw = GameRules.planNightHits(
            players, emptyMap(),
            healTargetId = null, bodyguardTargetId = null, killerTargetId = null,
            nightNumber = 2, firstNightKill = true,
            poisonDueId = "p",
        )
        assertEquals("p", raw.poisonVictimId)
    }

    @Test
    fun survivorDoesNotBlockTownWin() {
        val players = listOf(
            Player(id = "s", name = "S", role = Role.SURVIVOR),
            Player(id = "c", name = "C", role = Role.CIVILIAN),
        )
        assertEquals(Winner.GOOD, GameRules.winner(players))
    }

    @Test
    fun amnesiacCannotTakeTwins() {
        assertTrue(!GameRules.amnesiacCanTake(Role.AMNESIAC))
        assertTrue(!GameRules.amnesiacCanTake(Role.WHORE))
        assertTrue(!GameRules.amnesiacCanTake(Role.TWIN_CIVIL))
        assertTrue(GameRules.amnesiacCanTake(Role.MAYOR))
        val pool = listOf(
            Player(id = "a", name = "A", role = Role.MAYOR, alive = false),
            Player(id = "b", name = "B", role = Role.WHORE, alive = false),
            Player(id = "c", name = "C", role = Role.CIVILIAN, alive = true),
        )
        assertEquals(listOf("a"), GameRules.deadTakeable(pool).map { it.id })
    }
}
