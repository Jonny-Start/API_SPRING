# Proyecto cards-api

## Descripción
Este es un proyecto de ejemplo para una API RESTful desarrollada con Spring Boot que gestiona una base de datos de tarjetas. Utiliza Java 18 y MySQL como base de datos.

## Requisitos Previos
- Java Development Kit (JDK) 18.0.2
- MySQL Server
- Maven

## Configuración de la Base de Datos
1. Asegúrate de tener MySQL Server instalado y en funcionamiento.
2. Crea una base de datos llamada `cards_api_db` utilizando el siguiente comando SQL:
   ```sql
   CREATE DATABASE cards_api_db;
   ```

## Ejecución del Proyecto
1. Clona este repositorio en tu máquina local o descárgalo como un archivo zip y extráelo.
2. Abre una terminal y navega hasta el directorio raíz del proyecto.
3. Ejecuta el siguiente comando para construir el proyecto:
   ```bash
   mvn clean install
   ```
4. Una vez que la construcción haya finalizado con éxito, ejecuta el siguiente comando para iniciar la aplicación:
   ```bash
   java -jar target/cards-api-1.0.0.jar
   ```
   La aplicación ahora estará en funcionamiento en `http://localhost:4000`.

## Uso de la API
La API proporciona endpoints para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en la base de datos de tarjetas.
Anexo link de Postman para su comprendimiento, ¡¡recordar que la base de dato tiene que estar previamente creada!!

https://web.postman.co/workspace/My-Workspace~64fc5d42-a21d-4d68-8c8a-75ee870ef176/documentation/26313914-1cf29ad8-f724-43d3-b34b-9002162e1fcd

## Contribución
Si quieres contribuir a este proyecto, ¡siéntete libre de abrir un PR (Pull Request)!

## Licencia
Este proyecto está bajo la Licencia [MIT](https://opensource.org/licenses/MIT).

