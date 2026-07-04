package com.example.smarthealthmonitor

import android.app.Application
import com.example.smarthealthmonitor.data.models.SmartHealthRepository
import mx.utng.smarthealthmonitor.data.sync.NeonSyncWorker
import mx.utng.smarthealthmonitor.mqtt.MqttAppService

class SmartHealthApplication : Application() {
    lateinit var mqttService: MqttAppService

    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)
        mqttService = MqttAppService(
            context = this,
            fcFlow = SmartHealthRepository.mutableFcFlow
        )
        mqttService.connect()

        // Programar sync periódico con Neon
        NeonSyncWorker.schedule(this)
    }
}

