package com.secretmafia.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CloudBlobTest {
    @Test
    fun blobRoundTrip() {
        val wallet = Wallet(
            blood = 2,
            town = 4,
            gold = 1,
            unlocked = setOf(Role.HUNTER, Role.DON),
        )
        val profile = Profile(name = "Vik", avatarId = 7)
        val decoded = CloudBlob.decode(CloudBlob.encode(wallet, profile))!!
        assertEquals(2, decoded.first.blood)
        assertEquals(4, decoded.first.town)
        assertEquals(1, decoded.first.gold)
        assertTrue(Role.HUNTER in decoded.first.unlocked)
        assertTrue(Role.DON in decoded.first.unlocked)
        assertEquals("Vik", decoded.second.name)
        assertEquals(7, decoded.second.avatarId)
        assertTrue(decoded.second.playSignedIn)
    }
}
