package com.example.practika.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Tipo de pregunta de una actividad. */
enum class TipoPregunta(val etiqueta: String) {
    ESCRITURA("Escritura"),
    ALTERNATIVAS("Alternativas"),
    VERDADERO_FALSO("Verdadero/Falso")
}

/** Asignatura a la que pertenece la actividad. */
enum class Materia(val etiqueta: String) {
    LENGUAJE("Lenguaje"),
    MATEMATICA("Matemática"),
    CIENCIAS("Ciencias"),
    HISTORIA("Historia")
}

/** Una opción de una pregunta de alternativas. La letra (a, b, c, d) se genera por posición. */
data class Alternativa(
    val texto: String = "",
    val esCorrecta: Boolean = false
)

/** Datos de una actividad (una pregunta con sus respuestas). */
data class Actividad(
    val id: Int,
    val materia: Materia = Materia.LENGUAJE,
    val enunciado: String = "",
    val tipo: TipoPregunta = TipoPregunta.ESCRITURA,
    val respuestaEscrita: String = "",
    val respuestaVerdaderoFalso: Boolean = true,
    val alternativas: List<Alternativa> = List(4) { Alternativa() }
)

/** Letra por defecto (a, b, c, d...) para una alternativa. */
fun letraAlternativa(index: Int): String = "${('a' + index)})"

/**
 * Mantiene el estado del formulario de actividad y la lista de actividades creadas.
 * El [id] de cada actividad es autoincrementable y lo administra este ViewModel.
 */
class ActividadViewModel : ViewModel() {

    private var siguienteId = 1

    private val _formulario = MutableStateFlow(Actividad(id = siguienteId))
    val formulario: StateFlow<Actividad> = _formulario.asStateFlow()

    private val _actividades = MutableStateFlow<List<Actividad>>(emptyList())
    val actividades: StateFlow<List<Actividad>> = _actividades.asStateFlow()

    /** Reemplaza el formulario en curso (cada campo emite una copia nueva). */
    fun actualizarFormulario(actividad: Actividad) {
        _formulario.value = actividad
    }

    /** Agrega la actividad actual a la lista y abre una nueva con el siguiente id. */
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
