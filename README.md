# Backend de Despachos

API REST Spring Boot para crear órdenes de despacho y actualizar sus intentos y estado de entrega.

## Tecnologías

- Java 17 y Spring Boot 3.4.4
- Spring Web, Validation, JPA y Actuator
- MySQL 8.4 en desarrollo y producción
- H2 para pruebas automatizadas
- Docker multietapa y Docker Compose
- Kubernetes/EKS con Deployment, Service, HPA y MySQL persistente mediante StatefulSet
- GitHub Actions: test, package, Docker build, Trivy, ECR, deploy y smoke test

## Estructura

```text
.github/workflows/deploy-eks.yml
back-Despachos_SpringBoot/Springboot-API-REST-DESPACHO/
├── src/
├── Dockerfile
├── docker-compose.yml
├── .env.example
└── k8s/
```

## Endpoints

| Método | Ruta | Uso |
|---|---|---|
| GET | `/api/v1/despachos` | Listar despachos |
| GET | `/api/v1/despachos/{id}` | Consultar despacho |
| POST | `/api/v1/despachos` | Crear despacho |
| PUT | `/api/v1/despachos/{id}` | Reemplazar todos los datos |
| PATCH | `/api/v1/despachos/{id}/estado` | Cambiar solamente intentos y estado |
| DELETE | `/api/v1/despachos/{id}` | Eliminar despacho |
| GET | `/actuator/health/readiness` | Readiness probe |
| GET | `/actuator/health/liveness` | Liveness probe |
| GET | `/swagger-ui.html` | Swagger UI |

El endpoint PATCH evita borrar fecha, patente, compra, dirección o valor al cerrar un despacho.

## Ejecución local

```bash
cd back-Despachos_SpringBoot/Springboot-API-REST-DESPACHO
cp .env.example .env
docker compose up --build -d
docker compose ps
```

- API: `http://localhost:8082/api/v1/despachos`
- Swagger: `http://localhost:8082/swagger-ui.html`
- MySQL: `localhost:3308`

## Pruebas

```bash
cd back-Despachos_SpringBoot/Springboot-API-REST-DESPACHO
./mvnw clean test
```

En Windows:

```powershell
.\mvnw.cmd clean test
```

## Pipeline CI/CD

Se ejecuta en `main` y `deploy`. En Push realiza pruebas, build, análisis Trivy, publicación en ECR, despliegue EKS y smoke test.

### GitHub Secrets requeridos

```text
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_SESSION_TOKEN
AWS_REGION
EKS_CLUSTER_NAME
DESPACHOS_DB_USER
DESPACHOS_DB_PASSWORD
DESPACHOS_DB_ROOT_PASSWORD
```

No se versionan contraseñas reales. `k8s/mysql-despachos-secret.example.yaml` es solamente una plantilla.

## Persistencia y escalabilidad

MySQL utiliza un StatefulSet y almacenamiento persistente de 5 GiB. La API posee probes Actuator, requests/limits, rolling update y un HPA entre 1 y 3 réplicas.
