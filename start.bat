@echo off
REM Script para iniciar la aplicación Spring Boot en Windows
REM Requiere: Java 21 LTS y Maven 3.8+
REM Usage: start.bat

setlocal enabledelayedexpansion

echo.
echo ===================================
echo Veris Backend - Spring Boot
echo ===================================
echo.

REM Verificar si Java está instalado
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Java no está instalado o no está en el PATH
    echo.
    echo Por favor instala Java 21 LTS y configura JAVA_HOME
    echo https://www.oracle.com/java/technologies/downloads/
    echo.
    pause
    exit /b 1
)

echo [OK] Java encontrado
java -version
echo.

REM Verificar si Maven está instalado
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Maven no está instalado o no está en el PATH
    echo.
    echo SOLUCIÓN RÁPIDA - Ejecuta el script de instalación:
    echo   .\install-maven.ps1
    echo.
    echo O consulta README.md sección "Requisitos de Sistema" para
    echo instrucciones detalladas de instalación manual.
    echo.
    pause
    exit /b 1
)

echo [OK] Maven encontrado
call mvn -version
echo.

REM Compilar y ejecutar
echo [INFO] Limpiando proyecto...
call mvn clean -q

echo [INFO] Instalando dependencias...
call mvn install -DskipTests -q

if %errorlevel% neq 0 (
    echo [ERROR] Falló la compilación. Por favor revisa los errores arriba.
    pause
    exit /b 1
)

echo [INFO] Iniciando aplicación...
echo.
call mvn spring-boot:run

pause

echo Iniciando aplicación en modo desarrollo...
echo ===================================
echo La aplicación estará disponible en: http://localhost:8080
echo Swagger UI estará disponible en: http://localhost:8080/swagger-ui.html
echo.

call mvn spring-boot:run

pause
