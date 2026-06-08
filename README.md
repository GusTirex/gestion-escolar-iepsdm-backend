# Gestión Escolar IEPSDM Backend

Backend en Spring Boot para el sistema de gestión escolar.

## Requisitos

- Java 21 o la versión compatible con el proyecto
- Maven Wrapper incluido en el repositorio
- MySQL instalado y en ejecución local
- Un archivo `.env` en la raíz del proyecto

## 1. Crear el archivo `.env`

Toma como base el archivo `.env.example` y crea un archivo `.env` en la raíz del proyecto.

Ejemplo para desarrollo local con MySQL:

```env
DB_URL=jdbc:mysql://localhost:3306/iepsdm?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=root
DB_DRIVER=com.mysql.cj.jdbc.Driver
SERVER_PORT=8080
SERVER_CONTEXT_PATH=/api
SPRING_PROFILES_ACTIVE=dev
TIMEZONE=America/Lima
```

## 2. Crear la base de datos en MySQL

Ejecuta en MySQL:

```sql
CREATE DATABASE iepsdm;
```

Si usas otro nombre de base de datos, actualiza `DB_URL` en el archivo `.env`.

## 3. Ejecutar el proyecto

Desde la raíz del proyecto:

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

## 4. Verificar la aplicación

La API queda disponible en:

```text
http://localhost:8080/api
```

La documentación Swagger/OpenAPI queda en:

```text
http://localhost:8080/api/api-docs-ui
```

## Notas

- El perfil activo por defecto es `dev`.
- La configuración de conexión a MySQL se lee desde las variables de entorno definidas en `.env`.
- Si cambias el puerto, actualiza `SERVER_PORT`.