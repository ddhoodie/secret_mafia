package com.secretmafia.game

object CloudBlob {
    fun encode(wallet: Wallet, profile: Profile): String {
        val roles = wallet.unlocked.joinToString(",") { it.name }
        return buildString {
            appendLine("v1")
            appendLine("blood=${wallet.blood}")
            appendLine("town=${wallet.town}")
            appendLine("gold=${wallet.gold}")
            appendLine("unlocked=$roles")
            appendLine("name=${profile.name.replace('\n', ' ').take(16)}")
            appendLine("avatar=${profile.avatarId.coerceIn(0, Progress.AVATAR_COUNT - 1)}")
        }
    }

    fun decode(raw: String): Pair<Wallet, Profile>? {
        val lines = raw.lineSequence().map { it.trim() }.filter { it.isNotEmpty() }.toList()
        if (lines.firstOrNull() != "v1") return null
        val map = lines.drop(1).mapNotNull { line ->
            val i = line.indexOf('=')
            if (i <= 0) null else line.substring(0, i) to line.substring(i + 1)
        }.toMap()
        val wallet = Wallet(
            blood = map["blood"]?.toIntOrNull()?.coerceAtLeast(0) ?: 0,
            town = map["town"]?.toIntOrNull()?.coerceAtLeast(0) ?: 0,
            gold = map["gold"]?.toIntOrNull()?.coerceAtLeast(0) ?: 0,
            unlocked = map["unlocked"].orEmpty()
                .split(",")
                .mapNotNull { runCatching { Role.valueOf(it) }.getOrNull() }
                .toSet(),
        )
        val profile = Profile(
            name = map["name"].orEmpty().take(16),
            avatarId = map["avatar"]?.toIntOrNull()?.coerceIn(0, Progress.AVATAR_COUNT - 1) ?: 0,
            playSignedIn = true,
        )
        return wallet to profile
    }
}
