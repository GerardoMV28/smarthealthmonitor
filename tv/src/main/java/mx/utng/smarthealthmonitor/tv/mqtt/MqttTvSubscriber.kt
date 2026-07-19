package mx.utng.smarthealthmonitor.tv.mqtt

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import mx.utng.smarthealthmonitor.mqtt.*
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttTvSubscriber(
    private val context: Context,
    private val tvFlow: MutableStateFlow<TvMessage?>
) {
    private var client: MqttAsyncClient? = null

    fun connect() {
        try {
            client = MqttAsyncClient(
                MqttConfig.BROKER_URL,
                MqttConfig.CLIENT_TV,
                MemoryPersistence()
            )
            client?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, msg: MqttMessage) {
                    if (topic == MqttConfig.TOPIC_TV) {
                        try {
                            val tvMsg = Json.decodeFromString<TvMessage>(String(msg.payload))
                            tvFlow.value = tvMsg
                            android.util.Log.d("MQTT_TV", "📺 Recibido: {tvMsg.bpm} bpm")
                        } catch (e: Exception) {
                            android.util.Log.e("MQTT_TV", "❌ Error decodificando TvMessage: {e.message}")
                        }
                    }
                }

                override fun connectionLost(cause: Throwable?) {
                    android.util.Log.w("MQTT_TV", "Conexión perdida: {cause?.message}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })

            val options = MqttConnectOptions().apply {
                userName = MqttConfig.USERNAME
                password = MqttConfig.PASSWORD.toCharArray()
                isCleanSession = true
                socketFactory = javax.net.ssl.SSLSocketFactory.getDefault()
            }

            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    client?.subscribe(MqttConfig.TOPIC_TV, MqttConfig.QOS)
                    android.util.Log.d("MQTT_TV", "✅ TV suscrita a {MqttConfig.TOPIC_TV}")
                }

                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    android.util.Log.e("MQTT_TV", "❌ Error: {ex?.message}")
                }
            })
        } catch (e: Exception) {
            android.util.Log.e("MQTT_TV", "❌ Error inicializando MQTT TV: {e.message}")
        }
    }

    fun disconnect() {
        try {
            client?.disconnect()
        } catch (e: Exception) {
            android.util.Log.e("MQTT_TV", "Error desconectando: {e.message}")
        }
    }
}
