# 📱 Caja - Aplicación POS Móvil

Una aplicación móvil moderna desarrollada con **Kotlin** y **Jetpack Compose** que permite realizar ventas directamente desde tu dispositivo móvil. Diseñada para pequeños y medianos negocios que necesitan un sistema de punto de venta portátil y eficiente.

## ✨ Características

- 🛍️ **Gestión de Ventas**: Realiza transacciones rápidas y seguras
- 📊 **Reportes en Tiempo Real**: Visualiza tus ventas instantáneamente
- 💳 **Métodos de Pago Múltiples**: Efectivo, tarjeta, transferencia y más
- 📦 **Control de Inventario**: Gestiona tu stock de productos
- 👥 **Gestión de Clientes**: Registra y administra información de clientes
- 🔒 **Seguridad**: Autenticación y encriptación de datos
- 📲 **Interfaz Intuitiva**: Diseño moderno con Jetpack Compose

## 🛠️ Tecnologías

- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Base de Datos**: Room (SQLite)
- **Networking**: Retrofit/OkHttp
- **Dependency Injection**: Hilt
- **Coroutines**: Para operaciones asincrónicas

## 📋 Requisitos Previos

- Android Studio Arctic Fox o superior
- Android API 24 (Android 7.0) o superior
- Gradle 7.0+
- Java 11+

## 🚀 Instalación

### 1. Clonar el repositorio
```bash
git clone https://github.com/jhuaman818/caja.git
cd caja
```

### 2. Abrir en Android Studio
- Abre Android Studio
- Selecciona "Open an existing Android Studio project"
- Navega a la carpeta del proyecto y selecciónala

### 3. Sincronizar Gradle
- Android Studio sincronizará automáticamente las dependencias
- Si es necesario, selecciona "File" > "Sync Now"

### 4. Compilar y ejecutar
```bash
# Compilar debug
./gradlew assembleDebug

# Instalar en dispositivo/emulador conectado
./gradlew installDebug
```

## 📁 Estructura del Proyecto

```
caja/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/caja/
│   │   │   │   ├── ui/          # Pantallas con Compose
│   │   │   │   ├── viewmodel/   # ViewModels
│   │   │   │   ├── data/        # Modelos y repositorios
│   │   │   │   └── database/    # Room entities y DAOs
│   │   │   └── res/             # Recursos (strings, colores, etc)
│   │   └── test/                # Tests unitarios
│   └── build.gradle             # Configuración del módulo app
└── build.gradle                 # Configuración raíz
```

## 💻 Uso

### Iniciar sesión
1. Abre la aplicación
2. Ingresa tus credenciales
3. Accede al panel principal

### Realizar una venta
1. Selecciona "Nueva Venta"
2. Busca y agrega productos
3. Ingresa el método de pago
4. Confirma la transacción

### Ver reportes
1. Ve a la sección "Reportes"
2. Selecciona el período deseado
3. Visualiza tus ventas y ganancias

## 🔧 Configuración

Edita el archivo `local.properties` para configurar:
```properties
# Servidor backend (si aplica)
API_BASE_URL=https://tu-servidor.com/api
API_KEY=tu_clave_api
```

## 📚 Dependencias Principales

```gradle
dependencies {
    // Jetpack Compose
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.material3:material3'
    
    // Room
    implementation 'androidx.room:room-runtime'
    kapt 'androidx.room:room-compiler'
    
    // Hilt
    implementation 'com.google.dagger:hilt-android'
    kapt 'com.google.dagger:hilt-compiler'
    
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android'
    
    // Retrofit
    implementation 'com.squareup.retrofit2:retrofit'
    implementation 'com.squareup.retrofit2:converter-gson'
}
```

## 🧪 Pruebas

Ejecutar tests unitarios:
```bash
./gradlew test
```

Ejecutar tests de instrumentación:
```bash
./gradlew connectedAndroidTest
```

## 📦 Build y Distribución

### Generar APK de debug
```bash
./gradlew assembleDebug
```
El APK se encontrará en `app/build/outputs/apk/debug/`

### Generar APK de release
```bash
./gradlew assembleRelease
```

### Generar AAB (Android App Bundle)
```bash
./gradlew bundleRelease
```

## 🐛 Reporte de Errores

Si encuentras un bug, por favor:
1. Abre un issue en GitHub
2. Incluye una descripción detallada del problema
3. Proporciona pasos para reproducirlo
4. Adjunta logs si es posible

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para grandes cambios:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

**jhuaman818**
- GitHub: [@jhuaman818](https://github.com/jhuaman818)

## 🙏 Agradecimientos

- Comunidad de Kotlin
- Jetpack Compose Documentation
- Contribuidores del proyecto

## 📞 Soporte

Para obtener soporte, contacta a través de:
- Issues de GitHub
- Correo electrónico: [tu-email]

---

**Última actualización**: Julio 2026