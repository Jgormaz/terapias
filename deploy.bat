@echo off
echo Cerrando cualquier proceso en el puerto 8080...
for /F "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do (
    echo Terminando proceso con PID %%a
    taskkill /PID %%a /F
)

echo Iniciando la aplicación como proceso separado...
cd target

:: Ejecuta el .jar en segundo plano usando PowerShell
powershell -Command "Start-Process -FilePath 'java' -ArgumentList '-jar terapias-0.0.1-SNAPSHOT.jar' -WindowStyle Hidden -RedirectStandardOutput 'app.log' -RedirectStandardError 'error.log'"

