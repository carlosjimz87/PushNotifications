package com.carlosjimz87.pushnotifications.firebase

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.carlosjimz87.pushnotifications.TAG
import com.carlosjimz87.pushnotifications.dataStore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

data class MessageEvent(val message: String)
class NotificationsService : FirebaseMessagingService(){


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.v(TAG, "Message from: ${message.from}")

        if(message.data.isNotEmpty()){
            Log.v(TAG, "Message data: ${message.data}")
        }

        // check if message contains a notification payload
        message.data.let{ pair->
            Log.v(TAG, "Message body: ${pair["body"]}.")
            sendMessageToMainApp(pair["body"] ?: "Empty Message")
        }

        message.notification?.let {
            Log.v(TAG, "Notification $it")
            Log.v(TAG, "Notification Title ${it.title}")
            Log.v(TAG, "Notification Body ${it.body}")

            sendMessageToMainApp("${it.title} : ${it.body}")
        }

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

    private fun sendMessageToMainApp(message: String){
        EventBus.getDefault().post(MessageEvent(message))
    }
}