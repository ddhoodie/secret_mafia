package com.secretmafia.game

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object WalletGuard {
    fun seal(wallet: Wallet, deviceId: String): String {
        val body = bodyOf(wallet)
        return "v1.$body.${mac(body, deviceId)}"
    }

    fun open(blob: String?, deviceId: String): Wallet? {
        if (blob.isNullOrBlank()) return null
        val parts = blob.split('.')
        if (parts.size < 3 || parts[0] != "v1") return null
        val mac = parts.last()
        val body = parts.drop(1).dropLast(1).joinToString(".")
        if (mac != mac(body, deviceId)) return null
        val bits = body.split('|')
        if (bits.size != 4) return null
        val blood = bits[0].toIntOrNull() ?: return null
        val town = bits[1].toIntOrNull() ?: return null
        val gold = bits[2].toIntOrNull() ?: return null
        if (blood < 0 || town < 0 || gold < 0) return null
        val unlocked = bits[3].split(',')
            .mapNotNull { runCatching { Role.valueOf(it) }.getOrNull() }
            .toSet()
        return Wallet(blood, town, gold, unlocked)
    }

    private fun bodyOf(wallet: Wallet): String {
        val roles = wallet.unlocked.map { it.name }.sorted().joinToString(",")
        return "${wallet.blood}|${wallet.town}|${wallet.gold}|$roles"
    }

    private fun mac(body: String, deviceId: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(keyMaterial(), "HmacSHA256"))
        val raw = mac.doFinal("v1|$deviceId|$body".toByteArray(Charsets.UTF_8))
        return raw.joinToString("") { b -> "%02x".format(b.toInt() and 0xFF) }
    }

    private fun keyMaterial(): ByteArray {
        val a = intArrayOf(
            0x6A, 0x11, 0xC4, 0x8E, 0x2B, 0x57, 0x90, 0xD3,
            0x4F, 0x1A, 0xE8, 0x73, 0x05, 0xB9, 0x6C, 0x22,
        )
        val b = intArrayOf(
            0x91, 0x3C, 0xF0, 0x18, 0xA7, 0x5E, 0x2D, 0x44,
            0xCB, 0x07, 0x9A, 0x61, 0xDE, 0x33, 0x8F, 0x14,
        )
        return ByteArray(32) { i ->
            (a[i % a.size] xor b[(i * 3) % b.size] xor (i * 17 + 43)).toByte()
        }
    }
}
