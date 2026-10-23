# PRACTIKA

App Android educativa para estudiantes de educación básica.

Permite registrarse, iniciar sesión, ver datos del estudiante, cambiar la foto de perfil y armar actividades/preguntas.

## Funciones

- Registro e inicio de sesión (contraseña guardada con hash BCrypt).
- Sesión persistente
- Home con 3 secciones: Inicio, Asignaturas y Perfil.
- Foto de perfil desde galería o cámara.
- Formulario de actividades del docente.

## Tecnologías

Kotlin · Jetpack Compose · Material 3 · Navigation Compose · Room (SQLite) · Coil · BCrypt · Gradle.

## Estructura

```
app/src/main/java/com/example/practika/
├─ model/       → datos de la UI
├─ viewmodel/   → lógica y estado (MVVM)
├─ data/        → Room + archivos (fotos)
├─ util/        → cámara y hashing
└─ ui/          → pantallas, componentes, tema y navegación
```

## Notas

- La base de datos es local (`mpaz_database.db`),.
- Solo se usan datos ficticios, no hay usuarios reales.
