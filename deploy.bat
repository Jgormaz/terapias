@echo off
echo Cerrando cualquier proceso en el puerto 8080...
for /F "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do (
    echo Terminando proceso con PID %%a
    taskkill /PID %%a /F
)

echo Iniciando la aplicación...
cd target
start "" /B java -jar terapias-0.0.1-SNAPSHOT.jar > app.log 2>&1
echo Aplicación desplegada en el puerto 8080.
