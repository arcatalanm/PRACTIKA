package com.example.practika.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class TipoPregunta(val etiqueta: String) {
    ESCRITURA("Escritura"),
    ALTERNATIVAS("Alternativas"),
    VERDADERO_FALSO("Verdadero/Falso")
}

enum class Materia(val etiqueta: String) {
    LENGUAJE("Lenguaje"),
    MATEMATICA("Matemática"),
    CIENCIAS("Ciencias"),
    HISTORIA("Historia")
}

data class Alternativa(
    val texto: String = "",
    val esCorrecta: Boolean = false
)

data class Actividad(
    val id: Int,
    val materia: Materia = Materia.LENGUAJE,
    val enunciado: String = "",
    val tipo: TipoPregunta = TipoPregunta.ESCRITURA,
    val respuestaEscrita: String = "",
    val respuestaVerdaderoFalso: Boolean = true,
    val alternativas: List<Alternativa> = List(4) { Alternativa() }
)

fun letraAlternativa(index: Int): String = "${('a' + index)})"

class ActividadViewModel : ViewModel() {

    private var siguienteId = 1
    private val _formulario = MutableStateFlow(Actividad(id = siguienteId))
    val formulario: StateFlow<Actividad> = _formulario.asStateFlow()
    private val _actividades = MutableStateFlow<List<Actividad>>(emptyList())
    val actividades: StateFlow<List<Actividad>> = _actividades.asStateFlow()

    fun actualizarFormulario(actividad: Actividad) {
        _formulario.value = actividad
    }

    fun agregarActividad() {
        val actual = _formulario.value
        if (actual.enunciado.isBlank()) return

        _actividades.value = _actividades.value + actual
        siguienteId++
        _formulario.value = Actividad(id = siguienteId)
    }

    fun eliminarActividad(id: Int) {
        _actividades.value = _actividades.value.filterNot { it.id == id }
    }
}
