@echo off
echo Cerrando cualquier proceso en el puerto 8080...
for /F "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do (
    echo Terminando proceso con PID %%a
    taskkill /PID %%a /F
)

echo Iniciando la aplicación usando VBS...
cscript //nologo launch-app.vbs
echo Aplicación lanzada.


