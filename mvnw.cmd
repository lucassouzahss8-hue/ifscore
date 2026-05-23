@echo off
setlocal

set "BASEDIR=%~dp0"
set "WRAPPER_PROPS=%BASEDIR%.mvn\wrapper\maven-wrapper.properties"

if not exist "%WRAPPER_PROPS%" (
  echo Maven Wrapper properties not found: %WRAPPER_PROPS%
  exit /b 1
)

for /f "tokens=1,* delims==" %%A in ('findstr /b "distributionUrl=" "%WRAPPER_PROPS%"') do set "DIST_URL=%%B"
if "%DIST_URL%"=="" (
  echo distributionUrl not found in %WRAPPER_PROPS%
  exit /b 1
)

for %%F in ("%DIST_URL%") do set "DIST_FILE=%%~nxF"
set "DIST_NAME=%DIST_FILE:-bin.zip=%"
set "WRAPPER_DIR=%USERPROFILE%\.m2\wrapper\dists\%DIST_NAME%"
set "MAVEN_HOME=%WRAPPER_DIR%\%DIST_NAME%"

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
  mkdir "%WRAPPER_DIR%" >nul 2>nul
  echo Downloading %DIST_URL%
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri '%DIST_URL%' -OutFile '%WRAPPER_DIR%\%DIST_FILE%'; Expand-Archive -LiteralPath '%WRAPPER_DIR%\%DIST_FILE%' -DestinationPath '%WRAPPER_DIR%' -Force"
  if errorlevel 1 exit /b 1
)

call "%MAVEN_HOME%\bin\mvn.cmd" %*
