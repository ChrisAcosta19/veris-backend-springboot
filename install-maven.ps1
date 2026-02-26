#!/usr/bin/env powershell
#
# Script para instalar Maven automáticamente en Windows
# Uso: .\install-maven.ps1
#

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Instalador de Maven para Veris Backend" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar si estamos corriendo como administrador
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")
if (-not $isAdmin) {
    Write-Host "[AVISO] Se recomienda ejecutar como Administrador para instalar variables de entorno globales" -ForegroundColor Yellow
}

# Detectar arquitectura
$architecture = if ([Environment]::Is64BitProcess) { "x64" } else { "x86" }
Write-Host "[INFO] Arquitectura detectada: $architecture" -ForegroundColor Green

# Descargar Maven
$MAVEN_VERSION = "3.9.8"
$MAVEN_HOME = "C:\apache-maven-$MAVEN_VERSION"
$MAVEN_URL = "https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip"
$MAVEN_ZIP = "$env:TEMP\apache-maven-$MAVEN_VERSION-bin.zip"

if (Test-Path $MAVEN_HOME) {
    Write-Host "[OK] Maven ya está instalado en: $MAVEN_HOME" -ForegroundColor Green
} else {
    Write-Host "[INFO] Descargando Maven $MAVEN_VERSION..." -ForegroundColor Yellow
    try {
        (New-Object System.Net.WebClient).DownloadFile($MAVEN_URL, $MAVEN_ZIP)
        Write-Host "[OK] Descarga completada" -ForegroundColor Green

        Write-Host "[INFO] Extrayendo Maven..." -ForegroundColor Yellow
        Expand-Archive -Path $MAVEN_ZIP -DestinationPath "C:\"
        Write-Host "[OK] Maven extraído en: $MAVEN_HOME" -ForegroundColor Green

        Remove-Item $MAVEN_ZIP
    } catch {
        Write-Host "[ERROR] Falló la descarga: $_" -ForegroundColor Red
        Write-Host ""
        Write-Host "Descarga manualmente desde:" -ForegroundColor Yellow
        Write-Host $MAVEN_URL
        exit 1
    }
}

# Configurar variables de entorno
Write-Host "[INFO] Configurando variables de entorno..." -ForegroundColor Yellow

# Variables de usuario (no requiere admin)
[Environment]::SetEnvironmentVariable("MAVEN_HOME", $MAVEN_HOME, "User")
Write-Host "[OK] MAVEN_HOME = $MAVEN_HOME" -ForegroundColor Green

# Agregar Maven\bin al PATH
$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
$mavnBin = "$MAVEN_HOME\bin"
if (-not $currentPath.Contains($mavnBin)) {
    $newPath = "$currentPath;$mavnBin"
    [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
    Write-Host "[OK] Agregado $mavnBin al PATH" -ForegroundColor Green
} else {
    Write-Host "[OK] $mavnBin ya está en el PATH" -ForegroundColor Green
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "¡Maven instalado correctamente!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "[IMPORTANTE] Reinicia PowerShell o CMD para aplicar los cambios" -ForegroundColor Yellow
Write-Host ""
Write-Host "Para verificar la instalación, abre una nueva terminal y ejecuta:" -ForegroundColor Cyan
Write-Host "  mvn -version" -ForegroundColor White
Write-Host ""
Write-Host "Luego, en la carpeta del proyecto, ejecuta:" -ForegroundColor Cyan
Write-Host "  mvn clean install" -ForegroundColor White
Write-Host ""
Write-Host "Para más información, consulta el README.md" -ForegroundColor Green
Write-Host ""
