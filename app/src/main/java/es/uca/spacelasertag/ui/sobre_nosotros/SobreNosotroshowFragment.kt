package es.uca.spacelasertag.ui.sobre_nosotros

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.uca.spacelasertag.R
import es.uca.spacelasertag.databinding.FragmentSobreNosotrosBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class SobreNosotroshowFragment : Fragment() {

private var _binding: FragmentSobreNosotrosBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!
    private lateinit var mapView: MapView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val sobrenosotroshowViewModel =
            ViewModelProvider(this).get(SobreNosotroshowViewModel::class.java)

    _binding = FragmentSobreNosotrosBinding.inflate(inflater, container, false)
    val root: View = binding.root

      val autobus = root.findViewById<TextView>(R.id.autobus)
      autobus.setOnClickListener {
          val url = "https://cmtbc.es/administracion/imagenes/Cadiz_ESI.pdf"
          val i = Intent(Intent.ACTION_VIEW)
          i.data = Uri.parse(url)
          startActivity(i)
      }
      val tren = root.findViewById<TextView>(R.id.tren)
      tren.setOnClickListener {
          val url = "https://www.renfe.com/es/es/cercanias/cercanias-cadiz/horarios"
          val i = Intent(Intent.ACTION_VIEW)
          i.data = Uri.parse(url)
          startActivity(i)
      }
      // Configurar la configuración de OpenStreetMap
      Configuration.getInstance().load(
          context,
          context?.let { androidx.preference.PreferenceManager.getDefaultSharedPreferences(it) }
      )

      mapView = root.findViewById(R.id.mapView)
      mapView.setTileSource(TileSourceFactory.MAPNIK) // Establecer el origen de los azulejos del mapa
      mapView.setMultiTouchControls(true) // Habilitar el zoom multitáctil
      val ubicacion = GeoPoint(36.537546258944005, -6.201637461551152)  //coordenadas
      // Establecer la posición y el zoom inicial del mapa
      mapView.controller.setZoom(15.0)
      mapView.controller.setCenter(ubicacion)

    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}