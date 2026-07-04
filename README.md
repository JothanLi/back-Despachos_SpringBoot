# Back Despachos - Spring Boot API REST

API REST desarrollada con Spring Boot para gestionar despachos asociados a ventas. El servicio permite registrar despachos, consultar su estado, actualizar información de entrega y eliminar registros. La información se persiste en MySQL y el proyecto incluye Swagger, Docker Compose y manifiestos Kubernetes para despliegue.

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.4
- Spring Web
- Spring Data JPA
- Bean Validation
- MySQL 8.4
- Lombok
- Springdoc OpenAPI / Swagger UI
- Maven Wrapper
- Docker / Docker Compose
- Kubernetes / EKS

## Estructura principal

```text
Springboot-API-REST-DESPACHO/
├── src/main/java/com/citt/
│   ├── controller/          # Controladores REST
│   ├── persistence/entity/   # Entidades JPA
│   ├── persistence/repository/ # Repositorios Spring Data
│   ├── persistence/services/ # Lógica de negocio
│   ├── exceptions/           # Manejo de errores
│   └── config/               # CORS y Swagger/OpenAPI
├── src/main/resources/
│   ├── application.properties
│   └── application-docker.properties
├── k8s/                      # Manifiestos Kubernetes
├── Dockerfile
├── docker-compose.yml
├── docker-compose.prod.yml
└── pom.xml
```

## Funcionalidades

- Crear despachos asociados a una compra o venta.
- Listar todos los despachos.
- Buscar un despacho por ID.
- Actualizar estado e información de un despacho.
- Eliminar despachos.
- Controlar intentos de entrega.
- Marcar un despacho como entregado o pendiente.
- Exponer documentación Swagger para prueba de endpoints.

## Modelo de datos: Despacho

La entidad principal es `Despacho` y contiene los siguientes campos:

| Campo | Tipo | Descripción |
|---|---|---|
| `idDespacho` | Long | Identificador único generado automáticamente. |
| `fechaDespacho` | LocalDate | Fecha planificada o registrada del despacho. |
| `patenteCamion` | String | Patente del camión asignado. |
| `intento` | int | Número de intentos de entrega. |
| `idCompra` | Long | Identificador de la compra o venta asociada. |
| `direccionCompra` | String | Dirección de entrega. |
| `valorCompra` | Long | Valor de la compra asociada. |
| `despachado` | boolean | Indica si el despacho fue entregado. |

## Endpoints principales

Base URL local:

```text
http://localhost:8082/api/v1/despachos
```

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/despachos` | Lista todos los despachos. |
| `GET` | `/api/v1/despachos/{idDespacho}` | Obtiene un despacho por ID. |
| `POST` | `/api/v1/despachos` | Crea un nuevo despacho. |
| `PUT` | `/api/v1/despachos/{idDespacho}` | Actualiza un despacho existente. |
| `DELETE` | `/api/v1/despachos/{idDespacho}` | Elimina un despacho. |

## Ejemplos de uso

### Crear un despacho

```bash
curl -X POST http://localhost:8082/api/v1/despachos \
  -H "Content-Type: application/json" \
  -d '{
    "fechaDespacho": "2026-07-05",
    "patenteCamion": "ABCD12",
    "intento": 0,
    "idCompra": 1,
    "direccionCompra": "Av. Providencia 1234, Santiago",
    "valorCompra": 45990,
    "despachado": false
  }'
```

### Listar despachos

```bash
curl http://localhost:8082/api/v1/despachos
```

### Buscar despacho por ID

```bash
curl http://localhost:8082/api/v1/despachos/1
```

### Actualizar un despacho

```bash
curl -X PUT http://localhost:8082/api/v1/despachos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "fechaDespacho": "2026-07-05",
    "patenteCamion": "ABCD12",
    "intento": 1,
    "idCompra": 1,
    "direccionCompra": "Av. Providencia 1234, Santiago",
    "valorCompra": 45990,
    "despachado": true
  }'
```

### Eliminar un despacho

```bash
curl -X DELETE http://localhost:8082/api/v1/despachos/1
```

## Requisitos previos

Para ejecutar el proyecto localmente se necesita:

- Java 17 instalado.
- Maven o Maven Wrapper incluido en el proyecto.
- MySQL disponible, o Docker instalado para levantar la base de datos automáticamente.

## Variables de entorno

La configuración principal usa variables de entorno para conectarse a MySQL:

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DB_ENDPOINT` | Host de la base de datos. | `localhost` |
| `DB_PORT` | Puerto de MySQL. | `3308` |
| `DB_NAME` | Nombre de la base de datos. | `despachosdb` |
| `DB_USERNAME` | Usuario de MySQL. | `despachosuser` |
| `DB_PASSWORD` | Contraseña de MySQL. | `despachospass123` |

En Docker se usan estas variables:

| Variable | Valor por defecto |
|---|---|
| `MYSQL_DATABASE` | `despachosdb` |
| `MYSQL_USER` | `despachosuser` |
| `MYSQL_PASSWORD` | `despachospass123` |
| `MYSQL_ROOT_PASSWORD` | `rootpass123` |
| `SPRING_PROFILES_ACTIVE` | `docker` |

## Ejecución local con Maven

Desde la carpeta raíz del proyecto:

```bash
./mvnw clean package
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

Según `application.properties`, el backend local usa el puerto:

```text
http://localhost:8081
```

Con Docker Compose se publica normalmente en:

```text
http://localhost:8082
```

## Ejecución con Docker Compose

El archivo `docker-compose.yml` levanta MySQL y el backend.

```bash
docker compose up --build -d
```

Ver contenedores:

```bash
docker ps
```

Ver logs del backend:

```bash
docker logs backend-despachos -f
```

Detener servicios:

```bash
docker compose down
```

Eliminar también los datos persistidos:

```bash
docker compose down -v
```

Con Docker Compose, los puertos principales son:

| Servicio | Puerto local | Puerto interno |
|---|---:|---:|
| Backend despachos | `8082` | `8081` en compose local / `8080` en compose prod |
| MySQL despachos | `3308` | `3306` |

## Swagger / OpenAPI

La documentación de la API está disponible en:

```text
http://localhost:8082/swagger-ui.html
```

Si se ejecuta directamente con Maven y puerto `8081`, usar:

```text
http://localhost:8081/swagger-ui.html
```

## Pruebas

Ejecutar pruebas:

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

## Despliegue en Kubernetes / EKS

El proyecto incluye manifiestos en la carpeta `k8s/`:

```text
k8s/
├── despachos-deployment.yaml
├── despachos-hpa.yaml
├── despachos-service.yaml
├── mysql-despachos-deployment.yaml
├── mysql-despachos-secret.yaml
└── mysql-despachos-service.yaml
```

Aplicar manifiestos:

```bash
kubectl apply -f k8s/
```

Ver recursos:

```bash
kubectl get pods -n innovatech
kubectl get svc -n innovatech
kubectl get hpa -n innovatech
```

El servicio de despachos queda expuesto internamente dentro del cluster como:

```text
despachos-service:8080
```

## Imagen Docker

Construir imagen local:

```bash
docker build -t backend-despachos:latest .
```

Ejecutar imagen manualmente:

```bash
docker run -p 8082:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3308/despachosdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
  -e SPRING_DATASOURCE_USERNAME=despachosuser \
  -e SPRING_DATASOURCE_PASSWORD=despachospass123 \
  backend-despachos:latest
```

## Notas importantes

- El proyecto utiliza `spring.jpa.hibernate.ddl-auto=update`, por lo que Hibernate crea o actualiza las tablas automáticamente.
- La base de datos principal es `despachosdb`.
- El endpoint `/api/v1/despachos` también se usa como ruta de health check en Kubernetes.
- Si se ejecuta junto al frontend en Kubernetes, Nginx redirige `/api/v1/despachos` hacia `despachos-service:8080`.
