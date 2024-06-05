package es.uca.spacelasertag.ui.salas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SalashowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slalasFragment"
    }
    val text: LiveData<String> = _text
}