@echo off
REM Script de diagnóstico para verificar la configuración del proyecto
REM Uso: diagnose.bat

setlocal enabledelayedexpansion

echo.
echo =============================================
echo Diagnóstico de Veris Backend - Spring Boot
echo =============================================
echo.

REM Variables para control de errores
set ERRORS=0

REM 1. Verificar Java
echo [CHECK] Verificando Java...
where java >nul 2>nul
if %errorlevel% equ 0 (
    echo   [OK] Java encontrado en PATH
    java -version
) else (
    echo   [ERROR] Java NO encontrado. Instala Java 21 LTS
    set /a ERRORS+=1
)
echo.

REM 2. Verificar JAVA_HOME
echo [CHECK] Verificando variable JAVA_HOME...
if defined JAVA_HOME (
    echo   [OK] JAVA_HOME=%JAVA_HOME%
) else (
    echo   [ERROR] JAVA_HOME no está definido
    set /a ERRORS+=1
)
echo.

REM 3. Verificar Maven
echo [CHECK] Verificando Maven...
where mvn >nul 2>nul
if %errorlevel% equ 0 (
    echo   [OK] Maven encontrado en PATH
    call mvn -version
) else (
    echo   [ERROR] Maven NO encontrado
    echo   [INFO] Consulta README.md sección "Requisitos de Sistema" para instalar Maven
    set /a ERRORS+=1
)
echo.

REM 4. Verificar MAVEN_HOME
echo [CHECK] Verificando variable MAVEN_HOME...
if defined MAVEN_HOME (
    echo   [OK] MAVEN_HOME=%MAVEN_HOME%
) else (
    echo   [AVISO] MAVEN_HOME no está definido (no es requerido si Maven está en PATH)
)
echo.

REM 5. Verificar git (opcional)
echo [CHECK] Verificando Git (opcional)...
where git >nul 2>nul
if %errorlevel% equ 0 (
    echo   [OK] Git encontrado
    git --version
) else (
    echo   [AVISO] Git no encontrado (no es requerido)
)
echo.

REM 6. Verificar que estamos en el directorio correcto
echo [CHECK] Verificando estructura del proyecto...
if exist pom.xml (
    echo   [OK] pom.xml encontrado
) else (
    echo   [ERROR] No estamos en el directorio correcto del proyecto
    set /a ERRORS+=1
)

if exist src (
    echo   [OK] Carpeta src encontrada
) else (
    echo   [ERROR] Carpeta src no encontrada
    set /a ERRORS+=1
)

if exist .env.example (
    echo   [OK] Archivo .env.example encontrado
) else (
    echo   [ERROR] Archivo .env.example no encontrado
)
echo.

REM Resumen
echo =============================================
if %ERRORS% equ 0 (
    echo [OK] Todo está configurado correctamente!
    echo.
    echo Puedes ejecutar:
    echo   mvn clean install
    echo   mvn spring-boot:run
    echo.
    echo O simplemente:
    echo   .\start.bat
) else (
    echo [ERROR] Se encontraron !ERRORS! problema(s)
    echo.
    echo Por favor, resuelve los errores marcados arriba
    echo Consulta MAVEN-SETUP.md o README.md para más información
)
echo =============================================
echo.

pause
