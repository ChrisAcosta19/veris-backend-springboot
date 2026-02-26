@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM M2_HOME - location of maven's installed home (default is under this file's directory).
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a keystroke before ending
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@REM Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'
@echo off
@REM set title of command window
title %0
@REM enable delayed expansion enabledelayedexpansion

@REM To isolate internal variables from possible post scripts, we use setlocal
setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_HOME=%DIRNAME%

@REM Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@REM Add default JVM options here. You can also use MAVEN_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Xmx1024m" "-Xms1024m"

@REM Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >nul 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%\bin\java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@REM This is where the actual Maven start up script begins
@REM Evaluating the command line arguments and building the command line

set CMD_LINE_ARGS=

setlocal enabledelayedexpansion
for %%a in (%*) do (
    set "arg=%%a"
    if defined MAVEN_BATCH_ECHO (
      echo %arg%
    )
    set "CMD_LINE_ARGS=!CMD_LINE_ARGS! !arg!"
)
setlocal enabledelayedexpansion

%JAVA_EXE% %DEFAULT_JVM_OPTS% -classpath "%APP_HOME%\.mvn\wrapper\maven-wrapper.jar" "-Dmaven.home=%APP_HOME%" "-Dmaven.multiModuleProjectDirectory=%APP_HOME%" org.apache.maven.wrapper.MavenWrapperMain %CMD_LINE_ARGS%

if "%ERRORLEVEL%" == "0" goto mainEnd

:fail
rem Set variable MAVEN_BATCH_ECHO before running this batch file to see all the batch variables printed before the execution of the batch file.
if "%MAVEN_BATCH_ECHO%" == "on" echo %MAVEN_BATCH_ECHO%
echo Maven execution failed!
if not "%OS%"=="Windows_NT" echo.
goto error

:mainEnd
if "%OS%"=="Windows_NT" (
  endlocal
)
exit /B %ERRORLEVEL%

:error
endlocal & exit /B 1
