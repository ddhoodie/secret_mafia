package com.secretmafia.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WalletGuardTest {
    @Test
    fun roundTripKeepsCoinsAndRoles() {
        val wallet = Wallet(blood = 3, town = 2, gold = 1, unlocked = setOf(Role.HUNTER, Role.DON))
        val blob = WalletGuard.seal(wallet, "dev-1")
        val opened = WalletGuard.open(blob, "dev-1")
        assertNotNull(opened)
        assertEquals(3, opened!!.blood)
        assertEquals(2, opened.town)
        assertEquals(1, opened.gold)
        assertTrue(opened.owns(Role.HUNTER))
        assertTrue(opened.owns(Role.DON))
    }

    @Test
    fun tamperedAmountIsRejected() {
        val blob = WalletGuard.seal(Wallet(blood = 1), "dev-1")
        val broken = blob.replaceFirst("1|", "99|")
        assertNotEquals(blob, broken)
        assertNull(WalletGuard.open(broken, "dev-1"))
    }

    @Test
    fun otherDeviceIsRejected() {
        val blob = WalletGuard.seal(Wallet(gold = 4), "phone-a")
        assertNull(WalletGuard.open(blob, "phone-b"))
    }

    @Test
    fun junkBlobIsRejected() {
        assertNull(WalletGuard.open(null, "dev-1"))
        assertNull(WalletGuard.open("", "dev-1"))
        assertNull(WalletGuard.open("v1.nope", "dev-1"))
    }
}
