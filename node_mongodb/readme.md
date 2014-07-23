Ejecución de la aplicación Rest:
===

- Instalar node.js

$restapi>npm install
$restapi>node server.js

- Prueba mediante curl:

$>curl -X GET http://localhost:8080/eventos
$>curl -X GET http://localhost:8080/eventos/<<código de evento>>
$>curl -X POST http://localhost:8080/eventos -d @fichero.json -H "Content-Type : application/json"

