# Proyecto: Aplicación de Detección de Objetos

## Descripción del Proyecto
Este proyecto es una aplicación basada en Spring Boot que permite la detección de objetos y la gestión de dispositivos conectados. La aplicación almacena los registros en una base de datos PostgreSQL y se ejecuta dentro de un contenedor Docker.

## Tecnologías Utilizadas
- Java 21
- Spring Boot
- PostgreSQL 17
- Docker y Docker Compose
- Swagger para documentación de API

## Instrucciones de Despliegue
### 1. Acceder al repositorio en GitHub y descargar el archivo docker-compose.yml

https://github.com/VicBoss10/detect.git
 

### 2. Construir y Levantar los Contenedores

Ejecuta el siguiente comando para construir y ejecutar la aplicación con Docker Compose:

docker-compose up 

Esto iniciará:
- La base de datos PostgreSQL en el puerto `5432`
- La aplicación en el puerto `8081`


### 3. Acceder a la API
Una vez iniciada la aplicación, puedes acceder a los endpoints documentados con Swagger en:

http://localhost:8081/swagger-ui.html






## Capturas de los Endpoints Documentados
A continuación se presentan capturas de pantalla de los endpoints documentados con Swagger:

![Swagger UI - Lista de Endpoints](Capturas/LISTA_EDNPOINTS.png)

### Usuarios
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Usuarios/GET.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Usuarios/POST.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Usuarios/GET_ID.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Usuarios/DELETE.png)

### Dispositivos
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Dispositivos/GET.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Dispositivos/POST.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Dispositivos/GET_ID.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Dispositivos/DELETE.png)

### Detecciones
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Detecciones/GET.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Detecciones/POST.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Detecciones/GET_ID.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Detecciones/DELETE.png)

### Logs
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Logs/GET.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Logs/POST.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Logs/GET_ID.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Logs/DELETE.png)

### Alertas
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Alerta/GET.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Alerta/POST.png)
![Ejemplo de Endpoint - Obtener Dispositivos](Capturas/Alerta/PUT.png)




