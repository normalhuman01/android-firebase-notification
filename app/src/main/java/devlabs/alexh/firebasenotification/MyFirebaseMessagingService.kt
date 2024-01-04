package devlabs.alexh.firebasenotification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "my_notification_channel"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Maneja la notificación aquí
        val title = remoteMessage.notification?.title
        val sound = remoteMessage.notification?.sound
        val cuerpo = remoteMessage.data["cuerpo"].toString()

        val splitList = cuerpo.split("&")


        val categoria_dato: String = splitList[0]
        val epicentro_dato: String = splitList[1]
        val fecha_dato: String = splitList[2]
        val hora_local_dato: String = splitList[3]
        val intensidad_dato: String = splitList[4]
        val latitud_dato: String = splitList[5]
        val longitud_dato: String = splitList[6]
        val magnitud: String = splitList[7]
        val profundo_dato: String = splitList[8]
        val referencia: String = splitList[9]
        val simulacro_dato: String = splitList[10]
        val tiporeporte_dato: String = splitList[11]

        Log.d("alexh", "Remote message")

        // Obtén la información de la notificación
        // Obtén la información de la notificación


        // Personaliza el sonido de la notificación

        // Personaliza el sonido de la notificación
        var soundUri: Uri = Uri.parse("android.resource://${packageName}/${R.raw.notify}")
        if (sound != null && sound.isNotEmpty()) {
            soundUri = Uri.parse(sound)
        }

        Log.i("alexh", "Channel: $CHANNEL_ID")
        Log.i("alexh", "Sound: ${soundUri}")

        // Crear un canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  // Build.VERSION_CODES.O  Android Oreo (O)
            val channelId = CHANNEL_ID // ID único del canal
            val channelName = "My Notification Channel" // Nombre del canal
            val importance = NotificationManager.IMPORTANCE_HIGH // Importancia del canal (puede ser cambiado según tus necesidades)
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                    setSound(soundUri, AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)  // Important ***
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build())
            }
            Log.i("alexh", "Android O at least")

            // Configurar otras propiedades del canal, como sonido, vibración, luces, etc.
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        // Crea una notificación con el sonido personalizado
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.green_icon)
            .setContentTitle(title ?: "Sismos Perú $magnitud")
            .setContentText(referencia)
            .setAutoCancel(true)

        // Muestra la notificación
        val notificationManager = NotificationManagerCompat.from(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.w("alexh", "There was no GRANTED")
            return
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build())

    }

}
