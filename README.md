# API Compras
**Microservicio de gestión de órdenes de compra — CloudTech**  
Puerto: `28099`

---

## 1. Requisitos
- Java 21+
- Spring Boot 4.x
- Oracle Database
- Maven

---

## 2. Creación de Base de Datos

### Tabla ORDEN_DE_COMPRAS
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

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/` | Crear nueva orden |
| GET | `/` | Listar todas las órdenes |
| GET | `/{id}` | Buscar orden por ID |
| GET | `/fabricante/{idFabricante}` | Buscar órdenes por fabricanteId |
| PUT | `/{id}` | Actualizar orden |
| DELETE | `/{id}` | Eliminar orden por ID |

---

## 4. Insertar orden

**POST** `http://localhost:28099/api/v1/proveedores`

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

api-inventario consulta este endpoint automáticamente cuando un componente tiene **menos de 3 unidades**:

```
GET http://localhost:28099/api/v1/proveedores/fabricante/{idFabricante}
```

Retorna todas las órdenes del fabricante. api-inventario filtra internamente por `EN_TRANSITO` y `ADUANA`.

> Para que la integración funcione debe existir al menos una orden con el mismo `idFabricante` del componente y estado `EN_TRANSITO` o `ADUANA`.

---

## 6. Errores comunes

| Código | Causa | Solución |
|--------|-------|----------|
| 406 | Validación fallida | Revisar campos obligatorios y formatos |
| 404 | ID no existe | Verificar ID con `GET /` |
| 400 | Estado inválido | Usar: `PENDIENTE`, `EN_TRANSITO`, `ADUANA`, `ENTREGADA` |