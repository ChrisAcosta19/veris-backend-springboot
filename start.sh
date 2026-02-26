#!/usr/bin/env bash

# Script para iniciar la aplicación Spring Boot en desarrollo
# Usage: ./start.sh

echo "==================================="
echo "Veris Backend - Spring Boot"
echo "==================================="

# Verificar si Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven no está instalado. Por favor instala Maven 3.8 o superior."
    exit 1
fi

# Verificar si Java está instalado
if ! command -v java &> /dev/null; then
    echo "Error: Java no está instalado. Por favor instala Java 17 o superior."
    exit 1
fi

echo "Limpiando proyecto..."
mvn clean

echo "Descargando dependencias e instalando..."
mvn install -DskipTests

echo ""
echo "==================================="
echo "Iniciando aplicación en modo desarrollo..."
echo "==================================="
echo "La aplicación estará disponible en: http://localhost:8080"
echo "Swagger UI estará disponible en: http://localhost:8080/swagger-ui.html"
echo ""

mvn spring-boot:run

