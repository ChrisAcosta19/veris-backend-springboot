# Veris Backend API (Spring Boot + Spring Data JPA + Oracle)

Este proyecto corresponde a la **API REST Backend para Gestión de Pacientes**. Construida sobre Spring Boot utilizando Spring Data JPA para el mapeo a base de datos Oracle, y Maven como gestor de dependencias. Esta es la adaptación del proyecto Node.js/Express a Spring Boot.

## Requisitos de Sistema

* **Java**: v21 LTS (21.0.9 2025-10-21 LTS o compatible) - [Descargar](https://www.oracle.com/java/technologies/downloads/)
* **Maven**: v3.8 o superior - [Descargar](https://maven.apache.org/download.cgi)
* **Oracle Database**: 11g o superior con esquema activo (CloudClusters remoto o local)
* **Git**: (Opcional, para clonar el repositorio)

### ⚠️ Si Maven no está instalado

Si al ejecutar `mvn -version` en terminal ves el error **"mvn no se reconoce como comando"**:

**Opción A - Instalación Automática (Windows)**:
```powershell
cd "D:\Trabajo Veris\veris-backend-springboot"
.\install-maven.ps1
# Reinicia PowerShell y verifica:
mvn -version
```

**Opción B - Instalación Manual**:

1. **Descarga Maven** desde https://maven.apache.org/download.cgi (apache-maven-3.9.8-bin.zip)
2. **Extrae** en: `C:\apache-maven-3.9.8`
3. **Configura variables de entorno**:
   - Abre: Propiedades del Sistema → Variables de Entorno
   - Nueva variable: `MAVEN_HOME = C:\apache-maven-3.9.8`
   - Edita `Path` y agrega: `%MAVEN_HOME%\bin`
4. **Reinicia** PowerShell/CMD y verifica:
   ```powershell
   mvn -version
   ```

**Opción C - Verificar configuración**:
```bash
# Ejecuta el script de diagnóstico
diagnose.bat
```

## Instalación

### 1. Clonar o descargar el repositorio

```bash
git clone <repositorio>
cd veris-backend-springboot
```

### 2. Configurar las variables de entorno

Renombre `.env.example` a `.env` e ingrese los datos de conexión a Oracle:

```bash
cp .env.example .env
```

Edite `.env` con tus credenciales de Oracle (CloudClusters o local):

```ini
# Para CloudClusters (usa SERVICE_NAME)
DB_HOST=tu-host-de-cloudclusters.net
DB_PORT=1521
DB_USER=tu_usuario
DB_PASSWORD=tu_password
DB_SERVICE_NAME=tu_service_name

# Para conexión local (usa SID) - descomentar si es necesario
# DB_SID=tu_sid

JWT_SECRET=tu_clave_secreta_jwt_segura
PORT=8080
```

**Descripción de parámetros `.env`**:
* `DB_HOST`: Dominio o IP del servidor Oracle (ej: tu-host-de-cloudclusters.net, localhost)
* `DB_PORT`: Puerto de conexión a Oracle (típicamente 1521)
* `DB_SERVICE_NAME`: Nombre del servicio Oracle (para CloudClusters - preferido)
* `DB_SID`: Identificador del sistema (SID) - usar solo si CloudClusters no proporciona SERVICE_NAME o para conexiones locales
* `DB_USER`: Usuario de acceso a Oracle
* `DB_PASSWORD`: Contraseña del usuario de Oracle
* `JWT_SECRET`: Clave secreta para encriptación de tokens Bearer JWT (mínimo 32 caracteres para producción)
* `PORT`: Puerto del servidor Spring Boot (default: 8080)

**Nota sobre la URL de conexión**: 
- Si `DB_SERVICE_NAME` está definido: `jdbc:oracle:thin:@host:port/service_name` (CloudClusters)
- Si solo `DB_SID` está definido: `jdbc:oracle:thin:@host:port:sid` (Conexión local)
- La clase `OracleDataSourceConfig` construye automáticamente la URL correcta

### 3. Preparar la Base de Datos

Si conectas a **CloudClusters remoto**, las tablas deben estar ya creadas. Si configuras una **conexión local**, ejecuta el siguiente script SQL en Oracle:

```sql
-- Crear secuencia para auto-incremento de pacientes
CREATE SEQUENCE MGM_SEQ_PACIENT
  START WITH 1
  INCREMENT BY 1
  NOCACHE;

-- Crear tabla de tipos de identificación
CREATE TABLE daf_tipos_identificacion (
  codigo_tipo_identificacion VARCHAR2(3) PRIMARY KEY,
  nombre_tipo_identificacion VARCHAR2(100) NOT NULL,
  estado VARCHAR2(1) DEFAULT 'A'
);

-- Crear tabla de pacientes
CREATE TABLE mgm_pacientes (
  id_paciente NUMBER PRIMARY KEY,
  codigo_tipo_identificacion VARCHAR2(3) NOT NULL,
  numero_identificacion VARCHAR2(20) NOT NULL UNIQUE,
  primer_nombre VARCHAR2(50) NOT NULL,
  segundo_nombre VARCHAR2(50),
  primer_apellido VARCHAR2(50) NOT NULL,
  segundo_apellido VARCHAR2(50),
  nombre_completo VARCHAR2(200) NOT NULL,
  email VARCHAR2(100),
  estado VARCHAR2(1) DEFAULT 'A',
  fecha_ingreso TIMESTAMP DEFAULT SYSDATE,
  usuario_ingreso VARCHAR2(50),
  fecha_modificacion TIMESTAMP,
  usuario_modificacion VARCHAR2(50),
  FOREIGN KEY (codigo_tipo_identificacion) REFERENCES daf_tipos_identificacion(codigo_tipo_identificacion)
);

-- Insertar tipos de identificación de prueba
INSERT INTO daf_tipos_identificacion VALUES ('CED', 'Cédula de Identidad', 'A');
INSERT INTO daf_tipos_identificacion VALUES ('PAS', 'Pasaporte', 'A');
INSERT INTO daf_tipos_identificacion VALUES ('RUC', 'RUC', 'A');

COMMIT;
```

**CloudClusters Remote**: Si la base de datos está en CloudClusters, verifica que el esquema y las tablas ya existan. Puedes validar la conexión una vez que el proyecto esté configurado.

### 4. Instalar dependencias

```bash
mvn clean install
```

Este comando descargará todas las dependencias Maven especificadas en `pom.xml`.

## Ejecución del Servidor

### Verificar configuración (Antes de ejecutar)

Primero, verifica que todo esté configurado correctamente:

**En Windows**:
```bash
diagnose.bat
```

Este script verificará:
- ✓ Java está instalado y en el PATH
- ✓ Maven está instalado y en el PATH
- ✓ Variables de entorno están configuradas
- ✓ Estructura del proyecto es correcta

### Modo Desarrollo (con recarga automática)

**Usando el script**:
```bash
start.bat
```

**O manualmente**:
```bash
mvn spring-boot:run
```

El servidor estará disponible en: `http://localhost:8080`

### Modo Producción

Compilar:
```bash
mvn clean package -DskipTests
```

Ejecutar:
```bash
java -jar target/veris-backend-springboot-1.0.0.jar
```

### Usando Scripts de Inicio

**En Linux/Mac:**
```bash
chmod +x start.sh
./start.sh
```

**En Windows:**
```bash
start.bat
```

## Documentación Interactiva (Swagger/OpenAPI)

El proyecto incluye documentación automática con Swagger UI. Para acceder:

```
http://localhost:8080/api/v1/swagger-ui/index.html
```

Para ver el JSON de OpenAPI:

```
http://localhost:8080/api/v1/v3/api-docs
```

## Flujo de Uso y Endpoints API

### 1. Obtener Token (Login)

**Linux/Mac:**
```bash
# Credenciales por defecto: usuario=VERIS, contraseña=PRUEBAS123
curl -X POST http://localhost:8080/api/v1/autenticacion/login \
  -H "Authorization: Basic $(echo -n 'VERIS:PRUEBAS123' | base64)" \
  -H "Content-Type: application/json"
```

**Windows (PowerShell):**
```powershell
$auth = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("VERIS:PRUEBAS123"))
curl -X POST http://localhost:8080/api/v1/autenticacion/login `
  -H "Authorization: Basic $auth" `
  -H "Content-Type: application/json"
```

**Respuesta esperada:**
```json
{
  "code": 200,
  "success": true,
  "message": "Authentication successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "expiresIn": 3600,
    "username": "VERIS"
  }
}
```

Guarde el token para usar en los siguientes requests.

### 2. Crear Paciente

**Linux/Mac:**
```bash
TOKEN="<tu_token_obtenido_arriba>"

curl -X POST http://localhost:8080/api/v1/pacientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "numero_identificacion": "1765432109",
    "codigo_tipo_identificacion": "CED",
    "primer_nombre": "Sofia",
    "segundo_nombre": null,
    "primer_apellido": "Torres",
    "segundo_apellido": null,
    "email": "sofia.torres@test.com"
  }'
```

**Windows (PowerShell):**
```powershell
$TOKEN = "<tu_token_obtenido_arriba>"
$body = @{
  numero_identificacion = "1765432109"
  codigo_tipo_identificacion = "CED"
  primer_nombre = "Sofia"
  segundo_nombre = $null
  primer_apellido = "Torres"
  segundo_apellido = $null
  email = "sofia.torres@test.com"
} | ConvertTo-Json

curl -X POST http://localhost:8080/api/v1/pacientes `
  -H "Authorization: Bearer $TOKEN" `
  -H "Content-Type: application/json" `
  -d $body
```

### 3. Listar Pacientes

**Sin filtros (todos los activos por defecto):**
```bash
curl -X GET "http://localhost:8080/api/v1/pacientes" \
  -H "Authorization: Bearer $TOKEN"
```

**Con paginación:**
```bash
curl -X GET "http://localhost:8080/api/v1/pacientes?page=1&limit=10" \
  -H "Authorization: Bearer $TOKEN"
```

**Con filtros:**
```bash
curl -X GET "http://localhost:8080/api/v1/pacientes?page=1&limit=10&nombre_completo=Sofia&estado=A" \
  -H "Authorization: Bearer $TOKEN"
```

**Parámetros de consulta:**
- `page`: Número de página (default: 1)
- `limit`: Registros por página (default: 10)
- `numero_identificacion`: Buscar por número de identificación (opcional)
- `nombre_completo`: Buscar por nombre (búsqueda parcial, opcional)
- `email`: Buscar por email (opcional)
- `estado`: A (Activo) o I (Inactivo), default: A

### 4. Obtener Paciente por ID

```bash
curl -X GET http://localhost:8080/api/v1/pacientes/1 \
  -H "Authorization: Bearer $TOKEN"
```

**Respuesta:**
```json
{
  "code": 200,
  "success": true,
  "message": "Paciente obtenido exitosamente",
  "data": {
    "idPaciente": 1,
    "numeroIdentificacion": "1765432109",
    "primerNombre": "Sofia",
    "nombreCompleto": "Sofia Torres",
    "email": "sofia.torres@test.com",
    "estado": "A",
    "tipoIdentificacion": {
      "codigoTipoIdentificacion": "CED",
      "nombreTipoIdentificacion": "Cédula de Identidad",
      "estado": "A"
    },
    "fechaIngreso": "2024-02-26T10:30:00",
    "usuarioIngreso": "VERIS",
    "fechaModificacion": null,
    "usuarioModificacion": null
  }
}
```

### 5. Actualizar Paciente

```bash
curl -X PUT http://localhost:8080/api/v1/pacientes/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "primer_nombre": "Sofia",
    "email": "sofia.nuevo@test.com"
  }'
```

**Notas:**
- No se pueden modificar `numero_identificacion` ni `codigo_tipo_identificacion`
- Los campos `segundo_nombre` y `segundo_apellido` pueden ser nulos
- Se registran automáticamente `usuario_modificacion` y `fecha_modificacion`

### 6. Eliminar Paciente (Baja Lógica)

```bash
curl -X DELETE http://localhost:8080/api/v1/pacientes/1 \
  -H "Authorization: Bearer $TOKEN"
```

**Nota:** El paciente no se elimina físicamente, solo se cambia su estado a 'I' (Inactivo).

## Validaciones Implementadas

La API valida automáticamente:

1. **Email**: Debe seguir formato válido (usuario@dominio.com)
   - Si está vacío, se acepta (campo opcional)
   - Si contiene caracteres inválidos, retorna error 400

2. **Tipo de Identificación**: Debe existir en la tabla `daf_tipos_identificacion`
   - Si no existe, retorna error 400

3. **Campos Requeridos en Creación**:
   - `numero_identificacion` ✓
   - `codigo_tipo_identificacion` ✓
   - `primer_nombre` ✓
   - `primer_apellido` ✓
   - Los campos `segundo_nombre` y `segundo_apellido` son opcionales

4. **Restricciones en Actualización**:
   - No se puede cambiar `numero_identificacion`
   - No se puede cambiar `codigo_tipo_identificacion`
   - Intento genera error 400

5. **Baja Lógica**:
   - Solo se pueden eliminar pacientes activos (estado='A')
   - Intentar eliminar uno inactivo retorna error 404

## Auditoría

Todos los registros de pacientes mantienen automáticamente:

- `usuario_ingreso`: Usuario que creó el registro (obtenido del token JWT)
- `fecha_ingreso`: Timestamp de creación automático
- `usuario_modificacion`: Usuario que modificó el registro (se actualiza en cambios)
- `fecha_modificacion`: Timestamp de última modificación automático

## Ejemplos de Respuestas

### Respuesta Exitosa

```json
{
  "code": 201,
  "success": true,
  "message": "Paciente creado exitosamente",
  "data": {
    "idPaciente": 1,
    "numeroIdentificacion": "1765432109",
    "primerNombre": "Sofia",
    "primerApellido": "Torres",
    "nombreCompleto": "Sofia Torres",
    "email": "sofia.torres@test.com",
    "estado": "A",
    "tipoIdentificacion": { ... }
  }
}
```

### Respuesta de Error

```json
{
  "code": 400,
  "success": false,
  "message": "Invalid email format",
  "errorData": {}
}
```

## Solución de Problemas

### "mvn" no se reconoce como comando

**Causa**: Maven no está instalado o no está en el PATH

**Soluciones**:
1. Corre el script de diagnóstico: `diagnose.bat`
2. Lee [MAVEN-SETUP.md](MAVEN-SETUP.md) para instrucciones de instalación
3. Opción rápida en PowerShell:
   ```powershell
   .\install-maven.ps1
   ```

### "Error de conexión a la base de datos"

**Causas comunes**:
- CloudClusters no está accesible: Verifica la conexión a internet y las credenciales
- DB_SERVICE_NAME incorrecto: Comprueba el valor en `.env`
- Credenciales incorrectas: Verifica `DB_USER` y `DB_PASSWORD`

**Solución**:
```bash
# Verifica la configuración
cat .env

# Si cambiaste .env, reinicia la aplicación
mvn spring-boot:run
```

### "Puerto 8080 ya está en uso"

**Solución:**
Cambia el puerto en `src/main/resources/application.properties`:

```properties
server.port=8081
```

O mediante variable de entorno:
```bash
export SERVER_PORT=8081
mvn spring-boot:run
```

### "404 Paciente no encontrado"

**Causas:**
- El ID de paciente no existe
- El paciente fue inactivado (estado='I')
- Error en el ID especificado

**Solución:**
- Verifica el ID: `GET /pacientes` para listar
- Revisa si el paciente estaba activo antes de operar

### "403 Token inválido o expirado"

**Causas:**
- Token expirado (válido por 3600 segundos)
- Token malformado
- Falta el header `Authorization: Bearer`

**Solución:**
```bash
# Obtén un nuevo token
curl -X POST http://localhost:8080/api/v1/autenticacion/login \
  -H "Authorization: Basic $(echo -n 'VERIS:PRUEBAS123' | base64)"
```

### "Email inválido"

**Validación esperada:**
- Formato: usuario@dominio.com
- Caracteres especiales solo permitidos: punto (.)
- No se permiten espacios

**Ejemplo válido:**
```
usuario@dominio.com
usuario.apellido@dominio.co
```

**Ejemplo inválido:**
```
usuario @dominio.com (espacio)
usuario.dominio.com (falta @)
usuario@dominio (falta dominio TLD)
```

## Credenciales de Prueba

**Autenticación API:**
- Usuario: `VERIS`
- Contraseña: `PRUEBAS123`
- Token válido por: 3600 segundos (1 hora)

**Base de Datos (ejemplo local):**
- Usuario: `system`
- Contraseña: `oracle`
- Host: `localhost:1521`
- SID: `ORCLCDB`

## Stack Tecnológico

- **Framework**: Spring Boot 3.5.11 (con soporte activo)
- **Persistencia**: Spring Data JPA + Hibernate
- **Seguridad**: Spring Security + JWT (JJWT 0.12.3)
- **Base de Datos**: Oracle Database (ojdbc8 v21.0.0.0)
- **Documentación**: Springdoc OpenAPI 2.1.0
- **Utilidades**: Lombok, dotenv-java
- **Build**: Maven 3.8+
- **Java**: OpenJDK 21 LTS (21.0.9 2025-10-21 o compatible)

## Diferencias con Node.js

| Aspecto | Node.js | Spring Boot |
|---------|---------|-------------|
| **Runtime** | Interpretado (event-loop) | JVM Compilado (JIT) |
| **Framework** | Express | Spring Boot |
| **ORM** | TypeORM | Spring Data JPA |
| **Seguridad** | Custom middleware | Spring Security |
| **Documentación** | swagger-autogen | springdoc-openapi |
| **Desarrollo** | ts-node-dev | Spring Boot DevTools |
| **Compilación** | TypeScript → JS | Maven → JAR |
| **Performance** | Variable | Más predecible (JIT) |

## Estructura del Código

```
src/main/java/com/veris/
├── VerisApplication.java          # Punto de entrada
├── controllers/                    # Endpoints REST
│   ├── AuthController.java
│   └── PacienteController.java
├── services/                       # Lógica de negocio
│   ├── AuthService.java
│   └── PacienteService.java
├── repositories/                   # Acceso a datos (JPA)
│   ├── PacienteRepository.java
│   └── TipoIdentificacionRepository.java
├── entities/                       # Entidades JPA
│   ├── Paciente.java
│   └── TipoIdentificacion.java
├── dto/                            # Data Transfer Objects
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── ApiResponse.java
│   ├── PacienteCreateRequest.java
│   ├── PacienteUpdateRequest.java
│   └── PacienteListResponse.java
├── security/                       # Autenticación y autorización
│   ├── SecurityConfig.java
│   ├── JwtUtils.java
│   └── JwtAuthenticationFilter.java
└── config/                         # Configuración adicional
    └── OpenApiConfig.java
```

## Deployment a Producción

### Build Release

```bash
mvn clean package -DskipTests
java -jar target/veris-backend-springboot-1.0.0.jar
```

### Con Docker

```bash
# Build
docker build -t veris-backend:1.0.0 .

# Run
docker run -d -p 8080:8080 \
  -e DB_HOST=oracle-host \
  -e DB_PORT=1521 \
  -e DB_SID=ORCLCDB \
  -e DB_USER=system \
  -e DB_PASSWORD=oracle \
  -e JWT_SECRET=secret_key \
  veris-backend:1.0.0
```

## Licencia

Este proyecto es parte del sistema Veris de Gestión Médica.

## Soporte

Para reportar problemas o hacer sugerencias, contacta al equipo de desarrollo.