package com.secretmafia.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressTest {
    @Test
    fun coreRolesAreFree() {
        val empty = Wallet()
        assertTrue(empty.owns(Role.MAFIA))
        assertTrue(empty.owns(Role.CIVILIAN))
        assertTrue(empty.owns(Role.JOKER))
        assertFalse(empty.owns(Role.HUNTER))
        assertFalse(empty.owns(Role.DRUNK))
        assertNull(Progress.unlock(empty, Role.JOKER))
    }

    @Test
    fun coinsFollowTeam() {
        assertEquals(CoinKind.TOWN, Progress.costOf(Role.HUNTER))
        assertEquals(CoinKind.BLOOD, Progress.costOf(Role.DON))
        assertEquals(CoinKind.GOLD, Progress.costOf(Role.JOKER))
        assertEquals(CoinKind.GOLD, Progress.costOf(Role.KILLER))
        assertEquals(CoinKind.GOLD, Progress.costOf(Role.DRUNK))
        assertEquals(CoinKind.GOLD, Progress.costOf(Role.WHORE))
        assertEquals(CoinKind.BLOOD, Progress.costOf(Role.LUNATIC))
        assertEquals(CoinKind.TOWN, Progress.costOf(Role.MAYOR))
        assertEquals(CoinKind.TOWN, Progress.costOf(Role.NECROMANCER))
        assertEquals(CoinKind.TOWN, Progress.costOf(Role.VIGILANTE))
        assertEquals(CoinKind.BLOOD, Progress.costOf(Role.FRAMER))
        assertEquals(CoinKind.BLOOD, Progress.costOf(Role.TRAITOR))
        assertEquals(CoinKind.BLOOD, Progress.costOf(Role.POISONER))
        assertEquals(CoinKind.GOLD, Progress.costOf(Role.CURSED))
        assertEquals(CoinKind.GOLD, Progress.costOf(Role.SURVIVOR))
        assertEquals(CoinKind.GOLD, Progress.costOf(Role.AMNESIAC))
        val townPaid = Progress.unlock(Wallet(town = 1), Role.HUNTER)!!
        assertEquals(0, townPaid.town)
        assertTrue(townPaid.owns(Role.HUNTER))
        assertNull(Progress.unlock(Wallet(gold = 20), Role.HUNTER))
        val bloodPaid = Progress.unlock(Wallet(blood = 1), Role.DON)!!
        assertTrue(bloodPaid.owns(Role.DON))
        val jestPaid = Progress.unlock(Wallet(gold = 1), Role.DRUNK)!!
        assertTrue(jestPaid.owns(Role.DRUNK))
        assertEquals(0, jestPaid.gold)
    }

    @Test
    fun fourAdsUnlockAnyCoinPick() {
        assertEquals(1, Progress.bumpAdWatches(0))
        assertEquals(3, Progress.bumpAdWatches(2))
        assertEquals(4, Progress.bumpAdWatches(3))
        assertEquals(4, Progress.bumpAdWatches(4))
        assertFalse(Progress.adPickReady(3))
        assertTrue(Progress.adPickReady(4))
    }

    @Test
    fun matchPayoutByWinner() {
        assertEquals(MatchReward.TOO_FAST, Progress.evaluateMatch(4 * 60 * 1000L, Winner.GOOD))
        assertEquals(MatchReward.TOWN, Progress.evaluateMatch(5 * 60 * 1000L, Winner.GOOD))
        assertEquals(MatchReward.BLOOD, Progress.evaluateMatch(5 * 60 * 1000L, Winner.MAFIA))
        assertEquals(MatchReward.BLOOD, Progress.evaluateMatch(5 * 60 * 1000L, Winner.KILLER))
        assertEquals(MatchReward.GOLD, Progress.evaluateMatch(5 * 60 * 1000L, Winner.JOKER))
        assertEquals(MatchReward.GOLD, Progress.evaluateMatch(5 * 60 * 1000L, Winner.WHORE))
    }

    @Test
    fun lockedRolesDumpToCivilian() {
        val counts = RoleCounts(2, 1, 1, 2).with(Role.HUNTER, 1).with(Role.CIVILIAN, 1)
        val stripped = counts.onlyOwned { it.core }
        assertEquals(0, stripped[Role.HUNTER])
        assertEquals(2, stripped[Role.CIVILIAN])
        assertEquals(counts.total, stripped.total)
    }
}
