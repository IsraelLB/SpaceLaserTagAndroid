package es.uca.spacelasertag.ui.salas

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.uca.spacelasertag.R
import es.uca.spacelasertag.databinding.FragmentSalasBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class SalashowFragment : Fragment() {

private var _binding: FragmentSalasBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!
    private lateinit var salaAbiertaLayout: View
    private lateinit var salaLaberintoLayout: View
    private lateinit var salaDosplantasLayout: View
    private lateinit var cambioButton: Button

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val salashowViewModel =
            ViewModelProvider(this).get(SalashowViewModel::class.java)

    _binding = FragmentSalasBinding.inflate(inflater, container, false)
    val root: View = binding.root
      val listView = root.findViewById<ListView>(R.id.listView)

      val items = arrayOf("Servicios e Instalaciones:",
                " Espacio de 150m x 150m",
              "Capacidad para 15 personas",
             "Espacios abiertos",
              "Barricadas y obstáculos",
              "Sistema de puntuación automatizado",
              "Iluminacion temática",
              "Sonido envolvente",
              "Extintor de seguridad",
              "Alarma de incendios")
      val adapter = ArrayAdapter(requireContext(), R.layout.tabla_layout, items)
      listView.adapter = adapter

      val listViewlaberinto = root.findViewById<ListView>(R.id.listViewlaberinto )
      val itemslaberinto  = arrayOf("Servicios e Instalaciones:",
          "Espacio de 100m x 100m",
                  "Capacidad para 20 personas",
                  "Segunda planta de 40m x 40m",
                  "Espacios abiertos",
                  "Sistema de puntuacion automatizado",
                  "Iluminacion temática",
                  "Sonido envolvente",
                  "Extintor de seguridad",
                  "Alarma de incendios")
      val adapterlaberinto  = ArrayAdapter(requireContext(), R.layout.tabla_layout, itemslaberinto)
      listViewlaberinto.adapter = adapterlaberinto

      val listViewdosplantas = root.findViewById<ListView>(R.id.listViewdosplantas )
      val itemsdosplantas = arrayOf("Servicios e Instalaciones:",
                  "Espacio de 100m x 70m",
                  "Capacidad para 12 personas",
                  "Paredes movibles",
                  "Sistema de puntuacion automatizado",
                  "Iluminacion temática",
                  "Sonido envolvente",
                  "Extintor de seguridad",
                  "Alarma de incendios")
      val adapterdosplantas  = ArrayAdapter(requireContext(), R.layout.tabla_layout, itemsdosplantas)
      listViewdosplantas.adapter = adapterdosplantas

      salaAbiertaLayout = root.findViewById(R.id.salaabierta)
      salaLaberintoLayout = root.findViewById(R.id.salalaberinto)
      salaDosplantasLayout= root.findViewById(R.id.saladosplantas)
      cambioButton = root.findViewById(R.id.cambio)

      // Configurar el Listener para el botón
      cambioButton.setOnClickListener {
          toggleLayouts()
      }
      val downloadPdfTextView: TextView = root.findViewById(R.id.downloadPdfTextView)
      val downloadPdfTextView1: TextView = root.findViewById(R.id.downloadPdfTextView1)
      val downloadPdfTextView2: TextView = root.findViewById(R.id.downloadPdfTextView2)

      // Configurar OnClickListener para el TextView
      downloadPdfTextView.setOnClickListener {
          // Llamar al método para descargar el PDF
          copyRawPdfToDownloads(requireContext(), "salas_spacelaserbattles")
      }
      downloadPdfTextView1.setOnClickListener {
          // Llamar al método para descargar el PDF
          copyRawPdfToDownloads(requireContext(), "salas_spacelaserbattles")
      }
      downloadPdfTextView2.setOnClickListener {
          // Llamar al método para descargar el PDF
          copyRawPdfToDownloads(requireContext(), "salas_spacelaserbattles")
      }
      return root
  }

    // Método para cambiar la visibilidad de los layouts
    private fun toggleLayouts() {
        // Determinar cuál layout está visible actualmente
        when {
            salaAbiertaLayout.visibility == View.VISIBLE -> {
                salaAbiertaLayout.visibility = View.GONE
                salaLaberintoLayout.visibility = View.VISIBLE
                salaDosplantasLayout.visibility = View.GONE
            }
            salaLaberintoLayout.visibility == View.VISIBLE -> {
                salaAbiertaLayout.visibility = View.GONE
                salaLaberintoLayout.visibility = View.GONE
                salaDosplantasLayout.visibility = View.VISIBLE
            }
            else -> { // Si salaDosplantasLayout está visible
                salaAbiertaLayout.visibility = View.VISIBLE
                salaLaberintoLayout.visibility = View.GONE
                salaDosplantasLayout.visibility = View.GONE
            }
        }
    }
    // Define la función para copiar el PDF a la carpeta de descargas
    fun copyRawPdfToDownloads(context: Context, fileName: String) {
        try {
            // Obtiene el identificador del recurso PDF en la carpeta res/raw
            val resourceId = context.resources.getIdentifier(fileName, "raw", context.packageName)

            // Abre un InputStream para el recurso PDF en res/raw
            val inputStream = context.resources.openRawResource(resourceId)

            // Obtiene la carpeta de descargas del dispositivo
            val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            // Crea el archivo de salida en la carpeta de descargas
            val outputFile = File(downloadsFolder, "$fileName.pdf")

            // Crea flujos de entrada y salida para copiar el contenido
            val outputStream = FileOutputStream(outputFile)

            // Copia el contenido del recurso PDF al archivo de salida
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            // Cierra los flujos de entrada y salida
            outputStream.flush()
            outputStream.close()
            inputStream.close()

            // Muestra una notificación de descarga exitosa
            showDownloadNotification(context, fileName)
        } catch (e: IOException) {
            // Maneja cualquier excepción de E/S que pueda ocurrir
            e.printStackTrace()
        }
    }

    private fun showDownloadNotification(context: Context, fileName: String) {
        val channelId = "download_notification_channel"
        val notificationId = 1
        // Crear un canal de notificación para dispositivos Android O y superiores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Download Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Construir la notificación
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_download_24)
            .setContentTitle("Descarga completada")
            .setContentText("El archivo $fileName.pdf se ha descargado correctamente. Puede verlo en su carpeta de Descargas")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Mostrar la notificación
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
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
                return
            }
            notify(notificationId, builder.build())
        }
    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}