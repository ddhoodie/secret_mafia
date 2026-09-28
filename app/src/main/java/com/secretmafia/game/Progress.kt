package com.secretmafia.game

enum class CoinKind { BLOOD, TOWN, GOLD }

enum class MatchReward { TOO_FAST, BLOOD, TOWN, GOLD }

object Progress {
    const val UNLOCK_COST = 1
    const val AD_REWARD = 1
    const val ADS_FOR_COIN = 4
    const val MATCH_REWARD = 1
    const val MATCH_MIN_MS = 5 * 60 * 1000L
    const val DEBUG_START_STACK = 100
    const val AVATAR_COUNT = 8

    /** Temporary. Flip true when AdMob rewarded is hooked up. */
    const val adsAvailable: Boolean = false

    fun adPickReady(watches: Int): Boolean = watches >= ADS_FOR_COIN

    fun bumpAdWatches(current: Int): Int {
        if (adPickReady(current)) return current
        return (current + 1).coerceAtMost(ADS_FOR_COIN)
    }

    fun costOf(role: Role): CoinKind = when (role) {
        Role.DRUNK, Role.JOKER, Role.KILLER, Role.WHORE -> CoinKind.GOLD
        else -> when (role.team) {
            Team.GOOD -> CoinKind.TOWN
            Team.EVIL -> CoinKind.BLOOD
            Team.NEUTRAL -> CoinKind.GOLD
        }
    }

    fun canUnlock(wallet: Wallet, role: Role): Boolean {
        if (role.isFree || role in wallet.unlocked) return false
        return wallet.amount(costOf(role)) >= UNLOCK_COST
    }

    fun unlock(wallet: Wallet, role: Role): Wallet? {
        if (!canUnlock(wallet, role)) return null
        return wallet.spend(costOf(role), UNLOCK_COST)?.copy(unlocked = wallet.unlocked + role)
    }

    fun grant(wallet: Wallet, kind: CoinKind, n: Int = 1): Wallet = when (kind) {
        CoinKind.BLOOD -> wallet.copy(blood = wallet.blood + n)
        CoinKind.TOWN -> wallet.copy(town = wallet.town + n)
        CoinKind.GOLD -> wallet.copy(gold = wallet.gold + n)
    }

    fun evaluateMatch(elapsedMs: Long, winner: Winner): MatchReward {
        if (elapsedMs < MATCH_MIN_MS) return MatchReward.TOO_FAST
        return when (winner) {
            Winner.GOOD -> MatchReward.TOWN
            Winner.MAFIA, Winner.KILLER -> MatchReward.BLOOD
            Winner.JOKER, Winner.WHORE -> MatchReward.GOLD
        }
    }

    fun coinFrom(reward: MatchReward): CoinKind? = when (reward) {
        MatchReward.BLOOD -> CoinKind.BLOOD
        MatchReward.TOWN -> CoinKind.TOWN
        MatchReward.GOLD -> CoinKind.GOLD
        MatchReward.TOO_FAST -> null
    }
}
