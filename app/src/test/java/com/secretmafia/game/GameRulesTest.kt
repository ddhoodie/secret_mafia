package com.secretmafia.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameRulesTest {
    @Test
    fun tooManyEvilIgnoresWild() {
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
        assertTrue(GameRules.tooManyEvil(heavy))
        assertEquals(2, GameRules.maxEvilFor(6))
        val ok = RoleCounts(2, 1, 1, 2)
        assertTrue(!GameRules.tooManyEvil(ok))
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
            Player(id = "t1", name = "T1", role = Role.TWIN_CIVIL, twinId = "twin-civil"),
            Player(id = "t2", name = "T2", role = Role.TWIN_CIVIL, twinId = "twin-civil"),
            Player(id = "m1", name = "M1", role = Role.MAFIA),
        )
        val (next, deaths) = GameRules.applyNightDeaths(players, GameRules.NightHits("t1", null), null)
        assertTrue(next.none { it.alive && it.role == Role.TWIN_CIVIL })
        assertEquals(2, deaths.size)
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
}
