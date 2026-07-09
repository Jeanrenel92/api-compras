# API Compras

Microservicio de gestión de órdenes de compra — CloudTech
Puerto: `28099`

---

## 1. Requisitos

- Java 21+
- Spring Boot 4.x
- Oracle Database
- Maven

---

## 2. Creación de Base de Datos

Tabla `ORDEN_DE_COMPRAS`

```sql
DROP TABLE ORDEN_DE_COMPRAS CASCADE CONSTRAINTS;

CREATE TABLE ORDEN_DE_COMPRAS (
    ID               NUMBER          GENERATED ALWAYS AS IDENTITY,
    ID_FABRICANTE    VARCHAR2(50)    NOT NULL,
    CANTIDAD         NUMBER(10)      NOT NULL CHECK (CANTIDAD > 0),
    ESTADO           VARCHAR2(20)    NOT NULL
                     CHECK (ESTADO IN ('PENDIENTE','EN_TRANSITO','ADUANA','ENTREGADA')),
    NOMBRE_PROVEEDOR VARCHAR2(100)   NOT NULL,
    FECHA_SOLICITUD  DATE            NOT NULL,
    CONSTRAINT PK_ORDEN PRIMARY KEY (ID)
);
```

---

## 3. Endpoints

Base URL: `http://localhost:28099/api/v1/proveedores`

| Método | Endpoint                     | Descripción                       |
|--------|-------------------------------|------------------------------------|
| POST   | `/`                            | Crear nueva orden                 |
| GET    | `/`                             | Listar todas las órdenes          |
| GET    | `/{id}`                         | Buscar orden por ID               |
| GET    | `/fabricante/{idFabricante}`    | Buscar órdenes por fabricante     |
| PUT    | `/{id}`                         | Actualizar orden                  |
| DELETE | `/{id}`                         | Eliminar orden por ID             |

---

## 4. Insertar orden

POST `http://localhost:28099/api/v1/proveedores`

```json
{
  "idFabricante": "FAB-003",
  "unidad": 10,
  "estado": "EN_TRANSITO",
  "nomProveedor": "ElectroSur Ltda",
  "fechaOrden": "2025-01-15"
}
```

> Estados válidos: `PENDIENTE` | `EN_TRANSITO` | `ADUANA` | `ENTREGADA`

---

## 5. Endpoint usado por api-inventario

`api-inventario` consulta este endpoint automáticamente cuando un componente tiene menos de 3 unidades:

```
GET http://localhost:28099/api/v1/proveedores/fabricante/{idFabricante}
```

Retorna todas las órdenes del fabricante. `api-inventario` filtra internamente por `EN_TRANSITO` y `ADUANA`.

> Para que la integración funcione debe existir al menos una orden con el mismo `idFabricante` del componente y estado `EN_TRANSITO` o `ADUANA`.

---

## 6. Errores comunes

| Código | Causa                  | Solución                                                         |
|--------|------------------------|-------------------------------------------------------------------|
| 400    | Validación fallida     | Revisar campos obligatorios y formatos                           |
| 400    | Estado inválido        | Usar: `PENDIENTE`, `EN_TRANSITO`, `ADUANA`, `ENTREGADA`           |
| 404    | ID no existe           | Verificar ID con `GET /`                                          |
| 405    | Método HTTP no soportado | Verificar verbo HTTP usado contra el endpoint                  |
| 409    | Violación de integridad de datos | Revisar restricciones de la tabla (CHECK, NOT NULL, etc.) |
| 500    | Error interno del servidor | Revisar logs del servicio                                     |

---

## 7. Manejo de excepciones

`GlobalExceptionHandler` centraliza las respuestas de error:

- `MethodArgumentNotValidException` → 400, detalle de campos inválidos
- `HttpMessageNotReadableException` → 400, JSON mal formado
- `IllegalArgumentException` → 400
- `NoSuchElementException` → 404
- `DataIntegrityViolationException` → 409
- `HttpRequestMethodNotSupportedException` → 405
- `RuntimeException` (genérico, ej. orden no encontrada) → 404
- `Exception` (cualquier otro error) → 500

---

## 8. Logging de peticiones

`RequestLoggingFilter` agrega un `traceId` (UUID corto) por petición vía MDC, y registra método, URI, IP de origen, código de estado y duración en ms. Nivel de log según código de respuesta:

- `>= 500` → ERROR
- `>= 400` → WARN
- resto → INFO

---

## 9. Documentación OpenAPI / Swagger

Disponible vía `springdoc-openapi` (configurado en `OpenApiConfig`).

- Swagger UI: `http://localhost:28099/swagger-ui.html`


Proyecto académico —
Duoc UC.

Autores:

Bayron Cerda / Jean Renel Darius / Matias Milanesi
