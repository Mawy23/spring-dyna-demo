# 🐾 Spring Dyna Demo

Este proyecto es una aplicación web construida con **Spring Boot**, que expone una API REST básica para gestionar mascotas. Está completamente instrumentada con **OpenTelemetry y Dynatrace** para monitorización y trazabilidad avanzada.

---

## 🚀 Características

- API REST para crear, leer, actualizar y eliminar mascotas (`CRUD`)
- Conexión a base de datos PostgreSQL
- Instrumentación con OpenTelemetry Java Agent
- Visualización completa de trazas, logs y métricas en Dynatrace

---

## 🛠 Requisitos

- Java 17+
- Maven
- PostgreSQL
- Dynatrace (entorno con token y URL OTLP)
- (Opcional) Docker o WSL2

---

## 📦 Instalación y ejecución

### 1. Clona el repositorio

```bash
git clone https://github.com/Mawy23/spring-dyna-demo.git
cd spring-dyna-demo
```

### 2. Configura PostgreSQL

Crea una base de datos llamada `petclinic`:

```bash
sudo -u postgres createdb petclinic
```

(O usa tu método favorito)

### 3. Descarga el agente OpenTelemetry

```bash
mkdir agent
curl -L https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar -o agent/opentelemetry-javaagent.jar
```

### 4. Define las variables necesarias

```bash
export JAVA_TOOL_OPTIONS="-javaagent:./agent/opentelemetry-javaagent.jar"
export OTEL_SERVICE_NAME="spring-dyna-demo"
export OTEL_EXPORTER_OTLP_ENDPOINT="https://{TU_URL}.live.dynatrace.com/api/v2/otlp"
export OTEL_EXPORTER_OTLP_HEADERS="Authorization=Api-Token {TU_API_TOKEN}"
export OTEL_RESOURCE_ATTRIBUTES="service.name=spring-dyna-demo,env=dev"
export OTEL_EXPORTER_OTLP_METRICS_TEMPORALITY_PREFERENCE=delta
export OTEL_LOGS_EXPORTER=otlp
```

### 5. Compila y ejecuta

```bash
mvn clean package -DskipTests
java -jar target/spring-dyna-demo-1.0.0.jar
```

La aplicación estará disponible en:  
👉 `http://localhost:8080`

---

## 📡 Endpoints REST

| Método | Ruta         | Descripción                      |
|--------|--------------|----------------------------------|
| GET    | `/pets`      | Listar todas las mascotas        |
| POST   | `/pets`      | Crear nueva mascota              |
| GET    | `/pets/{id}` | Obtener mascota por ID           |
| PUT    | `/pets/{id}` | Actualizar mascota por ID        |
| DELETE | `/pets/{id}` | Eliminar mascota por ID          |
| GET    | `/dbtest`    | Consulta monitorizada (PostgreSQL) |
| GET    | `/hello`     | Respuesta simple de prueba       |

---

## 📊 Observabilidad con Dynatrace

Una vez lanzado, el agente enviará trazas, logs y métricas a tu entorno Dynatrace.  
Desde Dynatrace puedes:

- Ver trazas distribuidas
- Consultar logs por span
- Crear dashboards personalizados
- Analizar rendimiento de endpoints y errores

