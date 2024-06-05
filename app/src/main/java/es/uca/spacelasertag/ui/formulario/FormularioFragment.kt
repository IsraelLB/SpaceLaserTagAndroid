package es.uca.spacelasertag.ui.formulario

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import es.uca.spacelasertag.R
import es.uca.spacelasertag.databinding.FragmentFormularioBinding
import es.uca.spacelasertag.databinding.FragmentReservaBinding
import es.uca.spacelasertag.ui.API
import es.uca.spacelasertag.ui.Reserva
import es.uca.spacelasertag.ui.ReservaAdapter
import es.uca.spacelasertag.ui.reserva.ReservaViewModel
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class FormularioFragment : Fragment() {

    private var _binding: FragmentFormularioBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val apiServicios = API()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val FormularioViewModel =
            ViewModelProvider(this).get(FormularioViewModel::class.java)

        _binding = FragmentFormularioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonPost = root.findViewById<Button>(R.id.buttonPostFormulario)
        val textResponse = root.findViewById<TextView>(R.id.textResponse)
        val editTextSala = root.findViewById<EditText>(R.id.editTextSala)
        val editTextNickname = root.findViewById<EditText>(R.id.editTextNickname)
        val editTextCarnet = root.findViewById<EditText>(R.id.editTextCarnet)
        val editTextTelefono = root.findViewById<EditText>(R.id.editTextTelefono)
        val editTextEmail = root.findViewById<EditText>(R.id.editTextEmail)
        val editTextFecha = root.findViewById<EditText>(R.id.editTextFecha)
        val editTextHoraInicio = root.findViewById<EditText>(R.id.editTextHoraInicio)
        val editTextHoraFin = root.findViewById<EditText>(R.id.editTextHoraFin)
        val ediTextNumPersonas = root.findViewById<EditText>(R.id.editTextNumPersonas)
        val editTextComentario = root.findViewById<EditText>(R.id.editTextComentario)





        buttonPost.setOnClickListener {

            val Sala = editTextSala.text.toString()
            val Nickname = editTextNickname.text.toString()
            val Carnet = editTextCarnet.text.toString()
            val Telefono = editTextTelefono.text.toString()
            val Email = editTextEmail.text.toString()
            val Fecha = editTextFecha.text.toString()
            val HoraInicio = editTextHoraInicio.text.toString()
            val HoraFin = editTextHoraFin.text.toString()
            val NumPersonas = ediTextNumPersonas.text.toString()
            val Comentario = editTextComentario.text.toString()


            if (Sala.isNotEmpty() && Nickname.isNotEmpty() && Carnet.isNotEmpty() && Telefono.isNotEmpty()
                && Email.isNotEmpty() && Fecha.isNotEmpty() && HoraInicio.isNotEmpty() && HoraFin.isNotEmpty()
                && NumPersonas.isNotEmpty() && Comentario.isNotEmpty()) {

                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val response = apiServicios.createReserva(Sala,Nickname,Carnet,Telefono,Email,Fecha,HoraInicio,HoraFin,NumPersonas,Comentario)
                        launch(Dispatchers.Main) {
                            //Si queremos ver el texto en pantalla en lugar de con el Snackbar
                            //textResponse.text = response.bodyAsText()

                            //Ejemplo de Snackbar
                            Snackbar.make(it, response.bodyAsText(), Snackbar.LENGTH_INDEFINITE)
                                .setAction("OK") { /* No se requiere ninguna acción obligatoria */ }
                                .show()
                        }

                        editTextSala.setText("")
                        editTextNickname.setText("")
                        editTextCarnet.setText("")
                        editTextTelefono.setText("")
                        editTextEmail.setText("")
                        editTextFecha.setText("")
                        editTextHoraInicio.setText("")
                        editTextHoraFin.setText("")
                        ediTextNumPersonas.setText("")
                        editTextComentario.setText("")


                    } catch (e: ClientRequestException) {
                        launch(Dispatchers.Main) {
                            textResponse.text = "Error: ${e.response.status.description}"
                        }
                    } catch (e: Exception) {
                        launch(Dispatchers.Main) {
                            textResponse.text = "Error: ${e.message}"
                        }
                    }
                }
            } else {
                textResponse.text = "Por favor, completa todos los campos correctamente"
            }

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}