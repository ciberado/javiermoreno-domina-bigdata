var express   = require('express'),
    http      = require('http'),
    
    servicio  = require('./Servicio.js').servicio;


var app = express();

app.configure(function () {
    app.use(express.json());
    app.use(express.urlencoded());
    app.use(express.methodOverride()); 
    app.use(app.router); 
});

app.get('/test', function(req, res) {
  res.send('["Todo ok."]');
});

app.get('/eventos', function(req, res) {
    /* Recuperar offset de querystring */
    /* Invocar servicio */
});

app.get('/eventos/:id', function(req, res) {
    /* Recuperar id de parámetros url */
    /* Invocar servicio */
});

app.post('/eventos', function(req, res) {
    /* Recuperar objeto de body */
    /* Transformar en modelo */
    /* Invocar servicio */
});


var server  = http.createServer(app);
server.listen(8080, function() {
  console.log('Servidor arrancado en 8080.');
});
