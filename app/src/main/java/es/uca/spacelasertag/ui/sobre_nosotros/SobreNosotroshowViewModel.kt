package es.uca.spacelasertag.ui.sobre_nosotros

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SobreNosotroshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slalasFragment"
    }
    val text: LiveData<String> = _text
}