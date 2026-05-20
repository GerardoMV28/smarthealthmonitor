package com.example.smarthealthmonitor.wear.presentation

import android.content.Context
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class WearDataSender(
    private val context: Context
) {

    companion object {
        const val PATH_FC = "/smarthealthmonitor/fc"
    }

    suspend fun enviarFC(bpm: Int) {

        val nodes =
            Wearable
                .getNodeClient(context)
                .connectedNodes
                .await()

        nodes.forEach { node ->

            Wearable
                .getMessageClient(context)
                .sendMessage(
                    node.id,
                    PATH_FC,
                    bpm.toString().toByteArray()
                )
                .await()
        }
    }
}