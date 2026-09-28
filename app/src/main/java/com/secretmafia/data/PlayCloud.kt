package com.secretmafia.data

import android.app.Activity
import android.content.Context
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.PlayGamesSdk
import com.google.android.gms.games.SnapshotsClient
import com.google.android.gms.games.snapshot.SnapshotMetadataChange
import com.secretmafia.R
import com.secretmafia.game.CloudBlob
import com.secretmafia.game.Profile
import com.secretmafia.game.Wallet
import kotlinx.coroutines.tasks.await

class PlayCloud(private val context: Context) {
    fun configured(): Boolean {
        val id = context.getString(R.string.game_services_project_id)
        return id.isNotBlank() && id != "0"
    }

    fun initSdk() {
        if (configured()) PlayGamesSdk.initialize(context)
    }

    suspend fun signedIn(activity: Activity): Boolean = try {
        PlayGames.getGamesSignInClient(activity).isAuthenticated.await().isAuthenticated
    } catch (_: Exception) {
        false
    }

    suspend fun signIn(activity: Activity): Boolean = try {
        val result = PlayGames.getGamesSignInClient(activity).signIn().await()
        result.isAuthenticated
    } catch (_: Exception) {
        false
    }

    suspend fun save(activity: Activity, wallet: Wallet, profile: Profile): Boolean {
        if (!signedIn(activity)) return false
        return try {
            val client = PlayGames.getSnapshotsClient(activity)
            val opened = client.open(
                SNAPSHOT,
                true,
                SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED,
            ).await()
            val snapshot = opened.data ?: return false
            snapshot.snapshotContents.writeBytes(CloudBlob.encode(wallet, profile).toByteArray(Charsets.UTF_8))
            client.commitAndClose(
                snapshot,
                SnapshotMetadataChange.Builder().setDescription("Secret Mafia coins").build(),
            ).await()
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun load(activity: Activity): Pair<Wallet, Profile>? {
        if (!signedIn(activity)) return null
        return try {
            val client = PlayGames.getSnapshotsClient(activity)
            val opened = client.open(
                SNAPSHOT,
                false,
                SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED,
            ).await()
            val snapshot = opened.data ?: return null
            val raw = snapshot.snapshotContents.readFully().toString(Charsets.UTF_8)
            CloudBlob.decode(raw)
        } catch (_: Exception) {
            null
        }
    }

    companion object {
        private const val SNAPSHOT = "secret_mafia_progress"
    }
}
