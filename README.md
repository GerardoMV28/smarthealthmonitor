# SmartHealth Monitor 🩺⌚📺

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-purple.svg?logo=kotlin)](https://kotlinlang.org/)
[![Android Gradle Plugin](https://img.shields.io/badge/AGP-8.8.0-brightgreen.svg?logo=android)](https://developer.android.com/studio/releases/gradle-plugin)
[![Compose Multiplatform](https://img.shields.io/badge/Jetpack_Compose-2024.12.01-blue.svg?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Wear OS](https://img.shields.io/badge/Wear_OS-4.0+-black.svg?logo=wearos)](https://developer.android.com/wear)
[![Android TV](https://img.shields.io/badge/Android_TV-Media3_ExoPlayer-orange.svg?logo=android)](https://developer.android.com/tv)

**SmartHealth Monitor** es una solución integral y multiplataforma de salud digital desarrollada en **Android** que sincroniza y monitoriza la frecuencia cardíaca en tiempo real a través de tres entornos: dispositivos móviles (**Mobile**), relojes inteligentes (**Wear OS**) y pantallas de televisión (**Android TV**), integrando persistencia local, transmisión inalámbrica por Chromecast y reproducción multimedia.

---

## 👨‍💻 Autor

**Gerardo Manzano Villafaña**  
Universidad Tecnológica del Norte de Guanajuato (**UTNG**)  
*Ingeniería en Desarrollo y Gestión de Software*

---

## 📱 Módulos del Proyecto

El proyecto está estructurado de forma modular en tres submódulos independientes:

```
SmartHealthMonitor/
├── app/        # Aplicación Móvil (Android Phone / Tablet)
├── wear/       # Aplicación para Relojes Inteligentes (Wear OS)
└── tv/         # Aplicación para Televisores Inteligentes (Android TV)
```

---

### 1. 📱 Módulo Móvil (`:app`)
Aplicación central para teléfonos Android que actúa como nodo concentrador de datos y visualizador.

- **Dashboard en Tiempo Real (`DashboardScreen`)**: Muestra la frecuencia cardíaca actual recibida desde el smartwatch con actualización reactiva (`StateFlow`).
- **Historial Clínico (`HistorialScreen`)**: Consulta y listado de mediciones históricas persistidas localmente.
- **Base de Datos Local (Room DB)**: Almacenamiento seguro mediante `Room`, DAOs y Coroutines (`LecturaFC`).
- **Google Cast SDK**: Soporte integrado para transmisión de datos a dispositivos Chromecast a través del botón `CastButton` en la barra superior (`TopBar`).
- **Servicio Receptor BLE (`WearListenerService`)**: Servicio en segundo plano conectado a la API `MessageClient` de Google Play Services para recibir paquetes de datos desde el reloj.

---

### 2. ⌚ Módulo Wear OS (`:wear`)
Aplicación diseñada para relojes inteligentes con interfaz optimizada para pantallas circulares y bajo consumo de energía.

- **WearDashboardScreen**: Panel principal con `ScalingLazyColumn` y `TimeText` para visualización clara de la frecuencia cardíaca en tiempo real.
- **WearRespiracionScreen**: Módulo de **Respiración Guiada y Biofeedback** (técnica Box Breathing 4-4-4) con animación circular fluida, temporizador por fases y cálculo de reducción de FC.
- **WearHistorialScreen**: Navegación por historial optimizada para coronas giratorias físicas mediante **Rotary Input** (`Modifier.rotaryScrollable`).
- **WearAlertaScreen**: Notificaciones y alertas en pantalla con botones circulares para confirmación rápida ante mediciones fuera de rango.
- **SmartHealth WatchFace Nativo (`SmartHealthWatchFaceService`)**: Carátula digital personalizada que renderiza la hora, batería y frecuencia cardíaca directamente en la pantalla de bloqueo/inicio del reloj.
- **Health Services API**: Lectura directa del sensor fotopletismógrafo (PPG) y envío continuo hacia el smartphone.

| Pantalla Wear OS         | Descripción                                                    |
| ------------------------ | -------------------------------------------------------------- |
| `WearDashboardScreen`    | FC en tiempo real con ScalingLazyColumn y TimeText             |
| `WearRespiracionScreen`  | Respiración guiada 4-4-4, animación rítmica y biofeedback      |
| `WearHistorialScreen`    | Lista con soporte para Rotary Input (corona física)            |
| `WearAlertaScreen`       | Botones circulares de confirmación y alerta                    |
| `SmartHealthWatchFace`   | WatchFace nativo con hora y FC sincronizada                    |

---

### 3. 📺 Módulo Android TV (`:tv`)
Experiencia para pantallas de gran formato y salas de monitoreo mediante control remoto (D-Pad).

- **Compose for TV**: Interfaz construida con las librerías oficiales `androidx.tv:tv-material` y `androidx.tv:tv-foundation`.
- **TvCatalogScreen / Dashboard**: Catálogo de visualización de pacientes y métricas con tarjetas enfocables (`FcCardItem`).
- **TvDetailScreen**: Vista de detalle con botones enfocables optimizados para control remoto.
- **TvPlaybackScreen (Media3 / ExoPlayer)**: Reproductor de video de alta eficiencia con soporte de ciclo de vida (`DisposableEffect`) y componentes de `AndroidView`.

---

## 🏗️ Arquitectura y Flujo de Datos

```
[Sensor PPG (Wear OS)]
       │  (Health Services API)
       ▼
[HealthDataService / PassiveListener (wear)]
       │  (MessageClient / Bluetooth LE)
       ▼
[WearListenerService (app)]
       │
       ▼
[SmartHealthRepository] ──► [Room Database (LecturaFC)]
       │                                  │
       ▼                                  ▼
[StateFlow<Int> (fcActual)]       [Flow<List<LecturaFC>>]
       │                                  │
  ┌────┴────────────────────────┐    ┌────┴────────────────────────┐
  ▼                             ▼    ▼                             ▼
[DashboardScreen (app)]  [TvCatalog (tv)]  [Historial (app)]  [TvCatalog (tv)]
  └──► Cast SDK (Chromecast)
```

---

---

## 🌟 Funcionalidades e Implementaciones del Proyecto

El proyecto implementa una arquitectura integral y multiplataforma dividida en componentes especializados:

### 1. 🐘 Base de Datos en la Nube (PostgreSQL en Neon Serverless)
- **Esquema Relacional Optimizado (`neon_schema.sql`)**:
  - Tabla `lecturas_fc`: Registro principal de mediciones con `bpm`, `estado`, `dispositivo` (`wear` | `app` | `tv`), `hora`, `fecha`, flag de `sincronizado` y `created_at`.
  - Tabla `alertas`: Almacén de eventos críticos (`FC_ALTA`, `FC_BAJA`, `FC_NORMAL`) y estado de atención (`atendida`).
  - Tabla `dispositivos`: Control de dispositivos registrados (`ultimo_sync`, `activo`).
  - Índices B-Tree: Consultas aceleradas por fecha (`idx_lecturas_fecha`), dispositivo (`idx_lecturas_dispositivo`) y estado de alertas (`idx_alertas_atendida`).
- **Cliente HTTP REST Serverless (`NeonClient` & `NeonApiService`)**:
  - Comunicación directa con el endpoint Serverless HTTP de Neon (`/sql`) vía Retrofit 2 y OkHttp 3.
  - Autenticación segura mediante cabeceras `Neon-Connection-String` y `Authorization`.

### 2. 🔄 Arquitectura Híbrida & Patrón Offline-First (Room + Neon)
- **Persistencia Local (Room DB v2)**: Fuente de verdad inmediata en el smartphone mediante `SmartHealthDatabase`, `LecturaFC` y `LecturaFcDao`.
- **Coordinador de Datos (`SyncRepository`)**:
  - **Escritura Local Inmediata**: Guarda primero en Room local (cero latencia y sin dependencia de conexión).
  - **PUSH a la Nube**: Transmite la lectura a Neon en segundo plano y marca el registro como sincronizado (`sincronizado = true`).
  - **PULL desde la Nube**: Descarga los registros más recientes de Neon e inserta en Room mediante `upsert` para evitar duplicados.
  - **Recuperación de Pendientes (`enviarPendientes`)**: Sube automáticamente las lecturas acumuladas durante periodos sin conexión a internet.

### 3. ⚙️ Sincronización en Segundo Plano con WorkManager (`NeonSyncWorker`)
- Tarea en background periódica programada cada **30 minutos**.
- Restricción de red (`NetworkType.CONNECTED`) para optimizar el consumo de batería y datos.
- Política de reintentos automáticos con retroceso exponencial (`BackoffPolicy.EXPONENTIAL`, 5 minutos).
- Inicialización en el arranque de la aplicación desde [SmartHealthApplication.kt](file:///C:/Users/gerar/OneDrive/Desktop/Ejercicio-2.1/app/src/main/java/com/example/smarthealthmonitor/SmartHealthApplication.kt).

### 4. 📱 Módulo Móvil (`:app`) — Control y Visualización
- **Dashboard en Tiempo Real**: Panel central con métricas en vivo (`StateFlow`), tarjetas informativas y botón de **Sincronización Manual (`↺ Sync`)** en la barra superior.
- **Historial Clínico con Indicadores de Nube**: Cada elemento en `FilaHistorial` muestra el estado de sincronización visual:
  - ☁️ `CloudDone` (Verde/Primario): Lectura sincronizada y respaldada en Neon PostgreSQL.
  - ⏳ `CloudQueue` (Gris/Contorno): Lectura almacenada localmente en Room, pendiente de sincronización.
- **Google Cast Framework**: Transmisión inalámbrica de métricas hacia pantallas externas compatibles con Chromecast.

### 5. ⌚ Módulo Wear OS (`:wear`) — Reloj Inteligente
- **Publicación Ligera (`WearNeonRepository`)**: Envío directo de lecturas PPG a Neon Serverless sin consumo excesivo de memoria en el smartwatch.
- **🌬️ Ejercicio de Respiración Guiada con Biofeedback (`WearRespiracionScreen`)**:
  - Técnica **Box Breathing 4-4-4**: Guía visual animada con expansión y contracción circular en 3 fases: *Inhalar (4s)*, *Sostener (4s)* y *Exhalar (4s)*.
  - **Biofeedback en Vivo**: Monitorea el ritmo cardíaco en tiempo real y calcula la reducción de estrés (comparativa de FC Inicial vs FC Final).
  - **Registro Automático**: Al completar los 4 ciclos (1 minuto), la sesión se registra en Neon PostgreSQL y MQTT bajo el estado `"Relajación"`.
- **Carátula Nativa (`SmartHealthWatchFaceService`)**: Watch Face personalizado con renderizado de hora, batería y FC sincronizada.
- **Navegación Wear OS**: Transiciones adaptadas a pantallas circulares con soporte para descarte por gesto (`SwipeDismissableNavHost`).

### 6. 📺 Módulo Android TV (`:tv`) — Sala de Monitoreo
- **Catálogo Multi-Fila (`TvCatalogScreen`)**:
  - **Fila 1 (Estado Consolidado)**: Estadísticas en tiempo real agrupadas por dispositivo con promedios de ritmo cardíaco (`ROUND(AVG(bpm))`).
  - **Fila 2 (Historial Global)**: Últimas 50 lecturas consolidadas de los 3 dispositivos combinados.
  - **Fila 3 (Alertas Fuera de Rango)**: Detección analítica de taquicardia (>100 bpm) o bradicardia (<60 bpm) registradas en las últimas 24 horas.
- **Reproductor Multimedia (`TvPlaybackScreen`)**: Integración con AndroidX Media3 y ExoPlayer para visualización de material educativo/salud.
- **Navegación por Control Remoto (D-Pad)**: Componentes enfocables optimizados con `androidx.tv:tv-material`.

### 7. 📡 Comunicación en Tiempo Real Multiplataforma (MQTT)
- Integración de cliente Eclipse Paho MQTT para publicación y suscripción de eventos en tiempo real entre los 3 dispositivos.

---

### 🔑 Configuración de Credenciales (`local.properties`)

Las credenciales para conectar a la API HTTP de Neon están configuradas en `local.properties`:

```properties
# Neon Serverless PostgreSQL
NEON_API_KEY=tu_api_key_de_neon
NEON_HOST=ep-ancient-shape-b4z955q2-pooler.c-6.us-east-2.aws.neon.tech
NEON_DB=neondb
```

---

## 🛠️ Stack Tecnológico y Dependencias

- **Lenguaje**: Kotlin 2.0.0
- **Build System**: Gradle 8.10.2 con Kotlin DSL (`.kts`)
- **Android Gradle Plugin (AGP)**: 8.8.0
- **Target / Compile SDK**: Android 15 (API 35)
- **Min SDK**: 
  - `:app` -> API 30
  - `:wear` -> API 30
  - `:tv` -> API 23
- **UI Frameworks**:
  - Jetpack Compose (BOM 2024.12.01)
  - Wear Compose (`androidx.wear.compose`)
  - Android TV Compose (`androidx.tv:tv-material`)
- **Persistencia**: Room Database 2.6.1 + KSP (Kotlin Symbol Processing)
- **Conectividad & Multimedia**:
  - Google Play Services Wearable 18.2.0
  - Google Cast Framework 21.5.0 & MediaRouter 1.7.0
  - AndroidX Media3 / ExoPlayer 1.4.1
- **Asincronía**: Kotlin Coroutines & Flow (`StateFlow`, `collectAsState`)

---

## 🚀 Requisitos e Instalación

### Requisitos Previos
1. **Android Studio** (Ladybug / Meerkat o superior).
2. **Java Development Kit (JDK)**: **JDK 17** o **JDK 21 (LTS)** configurado como Gradle JDK.
3. **Android SDK** con soporte para API 35.
4. Emuladores o dispositivos físicos:
   - Dispositivo / Emulador móvil (Android 11+).
   - Reloj / Emulador Wear OS (Wear OS 3.0+).
   - Emulador Android TV (Android TV 7.0+).

### Pasos para compilar y ejecutar

1. **Clonar o abrir el repositorio**:
   ```bash
   git clone https://github.com/<tu-usuario>/Ejercicio-2.1.git
   ```
2. **Abrir en Android Studio**:
   - Selecciona la carpeta raíz del proyecto.
3. **Configurar el JDK en Android Studio**:
   - Ve a `File` > `Settings` (o `Preferences` en macOS) > `Build, Execution, Deployment` > `Build Tools` > `Gradle`.
   - En el campo **Gradle JDK**, selecciona **JDK 17** o **JDK 21** (si no lo tienes, elige *Download JDK...* y selecciona Eclipse Temurin o Amazon Corretto 17/21).
4. **Sincronizar Gradle**:
   - Presiona el botón de **Sync Project with Gradle Files** (icono del elefante).
5. **Ejecutar el módulo deseado**:
   - En el selector de configuración de ejecución, selecciona `app`, `wear` o `tv` y presiona **Run** (`Shift + F10`).

---

## 📄 Licencia

Este proyecto fue desarrollado con fines académicos y de demostración técnica para la **Universidad Tecnológica del Norte de Guanajuato (UTNG)**.
