package es.uca.spacelasertag.ui
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import es.uca.spacelasertag.R
import es.uca.spacelasertag.ui.Reserva
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservaAdapter(private val detallesReservas: MutableList<Reserva>, private val lifecycleScope: LifecycleCoroutineScope,private val context: Context) :
    RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {
    private val apiServicios = API()

    inner class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewId: TextView = itemView.findViewById(R.id.textViewId)
        val textViewSala: TextView = itemView.findViewById(R.id.textViewSala)
        val textViewNickname: TextView = itemView.findViewById(R.id.textViewNickname)
        val textViewNumeroCarnet: TextView = itemView.findViewById(R.id.textViewNumeroCarnet)
        val textViewTelefono: TextView = itemView.findViewById(R.id.textViewTelefono)
        val textViewEmail: TextView = itemView.findViewById(R.id.textViewEmail)
        val textViewFecha: TextView = itemView.findViewById(R.id.textViewFecha)
        val textViewHoraInicio: TextView = itemView.findViewById(R.id.textViewHoraInicio)
        val textViewHoraFin: TextView = itemView.findViewById(R.id.textViewHoraFin)
        val textViewNumPersonas: TextView = itemView.findViewById(R.id.textViewNumPersonas)
        val textViewComentario: TextView = itemView.findViewById(R.id.textViewComentario)
        val imageViewView: ImageView = itemView.findViewById(R.id.imageViewView)
        val imageViewEditar: ImageView = itemView.findViewById(R.id.imageViewEditar)
        val imageViewEliminar: ImageView = itemView.findViewById(R.id.imageViewEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val detallesReserva = detallesReservas[position]
        when (position % 3) {  //para cambiar el color
            0 -> holder.itemView.setBackgroundResource(R.drawable.round_verde)
            1 -> holder.itemView.setBackgroundResource(R.drawable.round_amarillo)
            2 -> holder.itemView.setBackgroundResource(R.drawable.round_azul)
        }
        holder.textViewId.text = "ID: " + detallesReserva._id
        holder.textViewSala.text = "Sala: " + detallesReserva.sala
        holder.textViewNickname.text = "Nickname: " + detallesReserva.nickname
        holder.textViewNumeroCarnet.text = "Número de Carnet: " + detallesReserva.numeroCarnet
        holder.textViewTelefono.text = "Teléfono: " + detallesReserva.telefono
        holder.textViewEmail.text = "Email: " + detallesReserva.email
        holder.textViewFecha.text = "Fecha: " + detallesReserva.fecha
        holder.textViewHoraInicio.text = "Hora de inicio: " + detallesReserva.horaInicio
        holder.textViewHoraFin.text = "Hora de fin: " + detallesReserva.horaFin
        holder.textViewNumPersonas.text = "Número de personas: " + detallesReserva.numPersonas
        holder.textViewComentario.text = "Comentario: " + detallesReserva.comentario

        holder.imageViewView.setOnClickListener {
            //En detallesReserva tienes la lista de una sola reserva pasaselo a la activity y ya desde ahi lo
            //muestras rescatando los datso de ahi
        }

        // Configurar clics en los iconos
        holder.imageViewEditar.setOnClickListener {
            //En detallesReserva tienes la lista de una sola reserva pasaselo a la activity y ya desde ahi lo
            //pides un formulario como el de post pero con los datos ya puestos rescatados de detallesReserva
            // y como ya tienes la id en detallesReserva llamas al put con esa id y con el formulario
        }



        holder.imageViewEliminar.setOnClickListener {
            val idReserva = detallesReserva._id
            // Llamar al método eliminarReserva de la API
            lifecycleScope.launch(Dispatchers.IO) {
                val response = apiServicios.eliminarReserva(idReserva)
                if (response.status.isSuccess()) {
                    detallesReservas.removeAt(position)
                    // Mostrar un Toast indicando que la reserva se ha eliminado con éxito
                    withContext(Dispatchers.Main) {
                        // Crea un Snackbar con el mensaje deseado y la duración
                        val snackbar = Snackbar.make(holder.itemView, "Reserva eliminada con éxito", Snackbar.LENGTH_SHORT)

                            // Muestra el Snackbar
                        snackbar.show()
                        //recarga el adapter:
                        notifyDataSetChanged()
                    }

                } else {
                    // Si la eliminación no fue exitosa, mostrar un Toast con un mensaje de error
                    withContext(Dispatchers.Main) {
                        val snackbar = Snackbar.make(holder.itemView, "Fallo al eliminar la reserva", Snackbar.LENGTH_SHORT)

                        // Muestra el Snackbar
                        snackbar.show()
                    }
                }

            }
        }
    }

    override fun getItemCount() = detallesReservas.size
}