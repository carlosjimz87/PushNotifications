package com.carlosjimz87.pushnotifications.firebase

import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.carlosjimz87.pushnotifications.dataStore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationsService : FirebaseMessagingService(){
    // generate a notification
    // attach the notification created with the custom layout
    // show the notification
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        GlobalScope.launch {
            saveToken(token)
        }
    }

    private suspend fun saveToken(token: String) {
        val gckTokenKey = stringPreferencesKey("gck_token")
        baseContext.dataStore.edit {pref->
            pref[gckTokenKey] = token
        }
    }
}