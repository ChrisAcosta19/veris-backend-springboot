#!/bin/sh
# Apache Maven Wrapper script for Unix systems

DIRNAME=`dirname "$0"`
APP_HOME=`cd "$DIRNAME" ; pwd -P`

JAVA_EXE=java

if ! command -v java &> /dev/null; then
    echo "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
    exit 1
fi

exec "$JAVA_EXE" $DEFAULT_JVM_OPTS -classpath "${APP_HOME}/.mvn/wrapper/maven-wrapper.jar" "-Dmaven.home=${APP_HOME}" "-Dmaven.multiModuleProjectDirectory=${APP_HOME}" org.apache.maven.wrapper.MavenWrapperMain "$@"
