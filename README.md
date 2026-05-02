# Documentación — Task Hexagonal Architecture
> Proyecto Micronaut con arquitectura hexagonal | Java 21

---

## Descripción General

API REST para gestión de tareas construida con **Micronaut** siguiendo el patrón de **Arquitectura Hexagonal (Ports & Adapters)**. Permite crear, leer, actualizar y eliminar tareas, además de consultar información adicional desde una API externa.

---

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| Micronaut | 4.6.2 | Framework |
| Hibernate JPA | — | ORM / Persistencia |
| MySQL | — | Base de datos |
| Lombok | — | Reducción de boilerplate |
| Micronaut Serde | — | Serialización JSON |
| JSONPlaceholder | — | API externa de prueba |

---

## Estructura del Proyecto

```
src/main/java/com/example/
│
├── Application.java                         # Punto de entrada
│
├── domain/                                  # Capa de dominio (núcleo)
│   ├── models/
│   │   ├── Task.java                        # Modelo principal
│   │   └── AdditionalTaskInfo.java          # Modelo de info adicional
│   └── ports/
│       ├── in/                              # Puertos de entrada (casos de uso)
│       │   ├── CreateTaskUseCase.java
│       │   ├── DeleteTaskUseCase.java
│       │   ├── GetAdditionalTaskInfoUseCase.java
│       │   ├── RetrieveTaskUseCase.java
│       │   └── UpdateTaskUseCase.java
│       └── out/                             # Puertos de salida
│           ├── TaskRepositoryPort.java
│           └── ExternalServicePort.java
│
├── applicaion/                              # Capa de aplicación
│   ├── services/
│   │   └── TaskService.java                # Orquestador de casos de uso
│   └── usecases/                           # Implementaciones de casos de uso
│       ├── CreateTaskCaseUseImpl.java
│       ├── DeleteTaskUseCaseImpl.java
│       ├── GetAdditionalTaskInfoUseCaseImpl.java
│       ├── RetrieveTaskUseCaseImpl.java
│       └── UpdateTaskUseCaseImpl.java
│
└── infrastructure/                          # Capa de infraestructura
    ├── config/
    │   └── ApplicationConfig.java           # Fábrica de beans (@Factory)
    ├── controllers/
    │   └── TaskController.java              # Endpoints HTTP
    ├── entities/
    │   └── TaskEntity.java                  # Entidad JPA
    ├── repositoryes/
    │   ├── JpaTaskRepository.java           # Repositorio Micronaut Data
    │   └── JpaTaskRepositoryAdapter.java    # Adaptador de repositorio
    ├── adapters/
    │   └── ExternalServiceAdapter.java      # Adaptador de servicio externo
    └── ports/rest/
        ├── TaskInfoClient.java              # Cliente HTTP (@Client)
        ├── TaskInfoRestAdapter.java         # Adaptador REST
        └── dto/
            ├── JsonPlaceholderToDo.java     # DTO de API externa
            └── JsonPlaceholderToDoUser.java # DTO de API externa
```

---

## Arquitectura Hexagonal

```
                    ┌─────────────────────────────┐
                    │          DOMINIO             │
                    │                             │
   HTTP Request ───►│  Ports In   →  Models       │
                    │  (interfaces)  Task          │
                    │                AdditionalInfo│
                    │  Ports Out  ←               │
                    │  (interfaces)               │
                    └─────────────────────────────┘
                           ▲              ▲
                           │              │
              ┌────────────┘              └────────────┐
              │                                        │
    ┌─────────────────┐                    ┌─────────────────────┐
    │   APPLICATION   │                    │   INFRASTRUCTURE    │
    │                 │                    │                     │
    │  TaskService    │                    │  TaskController     │
    │  UseCases Impl  │                    │  JpaRepository      │
    │                 │                    │  ExternalAdapter    │
    └─────────────────┘                    │  TaskInfoClient     │
                                           └─────────────────────┘
```

**Regla clave:** El dominio no conoce ni importa nada de Micronaut, JPA, o cualquier framework externo.

---

## Capa de Dominio

### `Task.java` — Modelo principal

```java
@Data @Introspected @Serdeable
@AllArgsConstructor @NoArgsConstructor
public class Task {
    @Nullable private Long id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private boolean completed;
}
```

| Anotación | Por qué |
|---|---|
| `@Data` | Genera getters, setters, equals, hashCode, toString |
| `@Serdeable` | Habilita serialización/deserialización JSON |
| `@Introspected` | Micronaut puede inspeccionar la clase en tiempo de compilación |
| `@Nullable` en `id` | El id es null en creación (lo genera la BD) |
| `Long` (no `long`) | Acepta null — necesario para inserciones nuevas |

---

### `AdditionalTaskInfo.java` — Modelo inmutable

```java
@Value @Serdeable
public class AdditionalTaskInfo {
    private final Long userId;
    private final String userName;
    private final String userEmail;
}
```

| Anotación | Por qué |
|---|---|
| `@Value` | Genera constructor, getters sin setters (inmutable) |
| `@Serdeable` | Necesario para serializar la respuesta JSON |

---

### Puertos de Entrada (`ports/in`)

Son las interfaces que definen los casos de uso. El dominio no sabe quién las implementa.

| Interfaz | Método |
|---|---|
| `CreateTaskUseCase` | `Task createTask(Task task)` |
| `DeleteTaskUseCase` | `Boolean deletetask(Long id)` |
| `RetrieveTaskUseCase` | `Optional<Task> getTaskByIdOptional(Long id)` / `List<Task> getAllTasks()` |
| `UpdateTaskUseCase` | `Optional<Task> updateTaskOptional(Long id, Task task)` |
| `GetAdditionalTaskInfoUseCase` | `AdditionalTaskInfo getAdditionalTaskInfo(Long id)` |

### Puertos de Salida (`ports/out`)

Interfaces que la infraestructura implementa para que el dominio acceda a recursos externos.

| Interfaz | Responsabilidad |
|---|---|
| `TaskRepositoryPort` | Operaciones CRUD contra la BD |
| `ExternalServicePort` | Obtener información adicional de API externa |

---

## Capa de Aplicación

### `TaskService.java` — Orquestador

Clase que agrupa todos los casos de uso y sirve como fachada para el controller. No tiene anotaciones de Micronaut porque es gestionada por el `@Factory`.

```java
public class TaskService {
    private CreateTaskUseCase createTaskUseCase;
    private DeleteTaskUseCase deleteTaskUseCase;
    private RetrieveTaskUseCase retiveTaskUseCase;
    private UpdateTaskUseCase updateTaskUseCase;
    private GetAdditionalTaskInfoUseCase getAditionalTaskInfoUseCase;

    // constructor + métodos delegados
}
```

**Patrón usado:** Cada método delega directamente al use case correspondiente, sin lógica propia.

---

### Implementaciones de Casos de Uso

Cada implementación recibe el puerto de salida que necesita y ejecuta la operación:

```
CreateTaskCaseUseImpl   → taskRepositoryPort.save(task)
DeleteTaskUseCaseImpl   → taskRepositoryPort.deleteById(id)
RetrieveTaskUseCaseImpl → taskRepositoryPort.findById(id) / findAll()
UpdateTaskUseCaseImpl   → taskRepositoryPort.update(task)
GetAdditionalTaskInfoUseCaseImpl → externalServicePort.getAdditionalTaskInfo(id)
```

**Nota:** No tienen `@Singleton` porque son instanciadas manualmente en el `@Factory`.

---

## Capa de Infraestructura

### `ApplicationConfig.java` — Fábrica de Beans

Centraliza la construcción de todos los beans del sistema.

```java
@Factory
public class ApplicationConfig {

    @Bean
    public TaskService taskService(TaskRepositoryPort repo, GetAdditionalTaskInfoUseCase info) {
        return new TaskService(
            new CreateTaskCaseUseImpl(repo),
            new DeleteTaskUseCaseImpl(repo),
            new RetrieveTaskUseCaseImpl(repo),
            new UpdateTaskUseCaseImpl(repo),
            info
        );
    }

    @Bean
    public TaskRepositoryPort taskRepositoryPort(JpaTaskRepositoryAdapter adapter) {
        return adapter;
    }

    @Bean
    public GetAdditionalTaskInfoUseCase additionalTaskInfoUseCase(ExternalServicePort port) {
        return new GetAdditionalTaskInfoUseCaseImpl(port);
    }
}
```

**Regla clave:** Si una clase está en el `@Factory` como `@Bean`, NO debe tener `@Singleton` — de lo contrario Micronaut la registra dos veces.

---

### `TaskController.java` — Endpoints HTTP

```
Base URL: /api/task
```

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| POST | `/api/task` | Crear tarea | 201 Created |
| GET | `/api/task` | Obtener todas las tareas | 200 OK |
| GET | `/api/task/{taskId}` | Obtener tarea por ID | 200 OK / 404 |
| PUT | `/api/task/{taskId}` | Actualizar tarea | 200 OK / 404 |
| DELETE | `/api/task/{taskId}` | Eliminar tarea | 204 No Content / 404 |
| GET | `/api/task/{taskId}/aditionalInfo` | Info adicional de API externa | 200 OK |

**Nota importante:** El endpoint `/aditionalInfo` tiene `@ExecuteOn(TaskExecutors.BLOCKING)` porque hace una llamada síncrona a una API externa, lo que bloquearía el hilo de Netty sin esta anotación.

---

### `TaskEntity.java` — Entidad JPA

```java
@Data @Entity @NoArgsConstructor @AllArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private boolean completed;

    public static TaskEntity fromDomainModel(Task task) { ... }
    public Task toDomainModel() { ... }
}
```

Contiene dos métodos de conversión para mantener la separación entre la entidad de infraestructura y el modelo de dominio.

---

### `JpaTaskRepositoryAdapter.java` — Adaptador de Repositorio

Implementa `TaskRepositoryPort` usando `JpaTaskRepository`. Convierte entre `Task` (dominio) y `TaskEntity` (infraestructura).

| Operación | Método JPA usado | Nota |
|---|---|---|
| Guardar | `jpaTaskRepository.save()` | Solo para entidades nuevas |
| Buscar por ID | `jpaTaskRepository.findById()` | Retorna `Optional` |
| Buscar todos | `jpaTaskRepository.findAll()` | — |
| Actualizar | `jpaTaskRepository.update()` | Usar `update()` no `save()` para evitar `detached entity` |
| Eliminar | `jpaTaskRepository.deleteById()` | Verifica existencia primero |

---

### `ExternalServiceAdapter.java` — Adaptador de API Externa

Implementa `ExternalServicePort` usando `TaskInfoClient`.

**Flujo:**
```
taskId → GET /todos/{id} → userId → GET /users/{userId} → AdditionalTaskInfo
```

```java
@Singleton
public class ExternalServiceAdapter implements ExternalServicePort {

    @Inject
    public ExternalServiceAdapter(TaskInfoClient taskInfoClient) { ... }

    public AdditionalTaskInfo getAdditionalTaskInfo(Long taskId) {
        JsonPlaceholderToDo toDo = taskInfoClient.getToDo(taskId);
        JsonPlaceholderToDoUser user = taskInfoClient.getUser(toDo.getUserId());
        return new AdditionalTaskInfo(user.getId(), user.getName(), user.getEmail());
    }
}
```

---

### `TaskInfoClient.java` — Cliente HTTP

```java
@Client("https://jsonplaceholder.typicode.com")
public interface TaskInfoClient {
    @Get("/todos/{id}")
    JsonPlaceholderToDo getToDo(Long id);

    @Get("/users/{id}")
    JsonPlaceholderToDoUser getUser(Long id);
}
```

**Reglas importantes para `@Client`:**
- Todos los métodos deben tener una anotación HTTP (`@Get`, `@Post`, etc.)
- La dependencia `micronaut-http-client` debe ser `implementation`, no `compileOnly`
- Los DTOs de retorno deben tener `@Serdeable`
- No instanciar manualmente en el `@Factory` — Micronaut lo gestiona solo

---

## Dependencias (`build.gradle.kts`)

```groovy
dependencies {
    annotationProcessor("io.micronaut.data:micronaut-data-processor")
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    annotationProcessor("org.projectlombok:lombok")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut:micronaut-http-client")  // ← implementation, no compileOnly
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.mysql:mysql-connector-j")
}
```

---

## Ejemplos de Peticiones

### Crear tarea (POST)
```json
POST /api/task
{
  "title": "Mi primera tarea",
  "description": "Descripción de la tarea",
  "creationDate": "2024-01-15T10:00:00",
  "completed": false
}
```

### Actualizar tarea (PUT)
```json
PUT /api/task/1
{
  "title": "Tarea actualizada",
  "description": "Nueva descripción",
  "creationDate": "2024-01-15T10:00:00",
  "completed": true
}
```

### Obtener info adicional (GET)
```
GET /api/task/1/aditionalInfo

Respuesta:
{
  "userId": 1,
  "userName": "Leanne Graham",
  "userEmail": "Sincere@april.biz"
}
```

---

## Errores Encontrados y Resueltos

| Error | Causa | Solución |
|---|---|---|
| `strategy is undefined` | Import de Micronaut Data en `@GeneratedValue` | Usar `jakarta.persistence.GeneratedValue` |
| `Multiple possible bean candidates` | `@Singleton` y `@Bean` en `@Factory` al mismo tiempo | Quitar `@Singleton` si ya existe `@Bean` |
| `No bean exists for qualifier` | `@Named` incorrecto al inyectar | Verificar nombre exacto |
| `StackOverflowError` | Método `@Override` se llamaba a sí mismo | Delegar al campo inyectado |
| `No bean introspection` | Falta `@Serdeable` o `@Introspected` | Agregar `@Serdeable` |
| `@Introduction method interceptor` | Método sin `@Get`/`@Post` en `@Client` | Eliminar método sin anotación HTTP |
| `BlockingHttpClient on event loop` | Llamada síncrona en hilo Netty | Agregar `@ExecuteOn(TaskExecutors.BLOCKING)` |
| `detached entity passed to persist` | Usar `save()` para actualizar entidad existente | Usar `update()` en lugar de `save()` |
| `compileOnly` en http-client | Dependencia no disponible en runtime | Cambiar a `implementation` |
| `No serializable introspection` | Falta `@Serdeable` en clase de respuesta | Agregar `@Serdeable` a `AdditionalTaskInfo` |