package es.uca.spacelasertag.ui.reserva

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.uca.spacelasertag.R
import es.uca.spacelasertag.databinding.FragmentReservaBinding
import es.uca.spacelasertag.ui.API
import es.uca.spacelasertag.ui.Reserva
import es.uca.spacelasertag.ui.ReservaAdapter
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ReservaFragment : Fragment() {

private var _binding: FragmentReservaBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!
    private val apiServicios = API()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val reservaViewModel =
            ViewModelProvider(this).get(ReservaViewModel::class.java)

    _binding = FragmentReservaBinding.inflate(inflater, container, false)
    val root: View = binding.root

      val buttonPost = root.findViewById<Button>(R.id.buttonPost)
      val buttonGet = root.findViewById<Button>(R.id.buttonGet)
      val textResponse = root.findViewById<TextView>(R.id.textResponse)


      buttonPost.setOnClickListener {
          findNavController().navigate(R.id.formulario)

      }

      buttonGet.setOnClickListener {
          // Realiza una solicitud GET al endpoint de persona
          val id = "6643b944d59e7f3755e8fc4e"
          lifecycleScope.launch(Dispatchers.IO) {
              try {
                  val response = apiServicios.getReservas()
                  val jsonArray = Json.parseToJsonElement(response.bodyAsText()).jsonArray

                  // Mapea la lista de detalles como una lista de objetos Reserva
                  val detailsList = jsonArray.map { element ->
                      val id = element.jsonObject["_id"]?.jsonPrimitive?.content ?: "N/A"
                      val sala = element.jsonObject["sala"]?.jsonPrimitive?.content ?: "N/A"
                      val nickname = element.jsonObject["nickname"]?.jsonPrimitive?.content ?: "N/A"
                      val numeroCarnet = element.jsonObject["numeroCarnet"]?.jsonPrimitive?.content ?: "N/A"
                      val telefono = element.jsonObject["telefono"]?.jsonPrimitive?.content ?: "N/A"
                      val email = element.jsonObject["email"]?.jsonPrimitive?.content ?: "N/A"
                      val fecha = element.jsonObject["fecha"]?.jsonPrimitive?.content ?: "N/A"
                      val horaInicio = element.jsonObject["horaInicio"]?.jsonPrimitive?.content ?: "N/A"
                      val horaFin = element.jsonObject["horaFin"]?.jsonPrimitive?.content ?: "N/A"
                      val numPersonas = element.jsonObject["numPersonas"]?.jsonPrimitive?.content ?: "N/A"
                      val comentario = element.jsonObject["comentario"]?.jsonPrimitive?.content ?: "N/A"

                      Reserva(id, sala, nickname, numeroCarnet, telefono, email, fecha, horaInicio, horaFin, numPersonas, comentario)
                  }.toMutableList()

                  launch(Dispatchers.Main) {
                      val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
                      val layoutManager = LinearLayoutManager(requireContext())
                      recyclerView.layoutManager = layoutManager
                      val adapter = ReservaAdapter(detailsList, lifecycleScope, requireContext())
                      recyclerView.adapter = adapter
                      textResponse.text = "Reservas:"
                  }

              } catch (e: ClientRequestException) {
                  launch(Dispatchers.Main) {
                      textResponse.text = "Error: ${e.response.status.description}"
                  }
              } catch (e: Exception) {
                  launch(Dispatchers.Main) {
                      textResponse.text = "Error al procesar la solicitud: ${e.localizedMessage}"
                  }
              }
          }
      }

    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}