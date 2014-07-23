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
    var offset = req.query.offset ? req.query.offset : 0;
    console.log('Recuperando eventos desde %s.', offset);
    servicio.obtenerEventos(offset, function(err, eventos) {
        if (err) {
            console.warn('Error: %s.', err);
            res.json(500, [err.message]);
        } else {
            res.json(200, eventos);
        }
    });
});

app.get('/eventos/:id', function(req, res) {
    var id = req.params.id;
    console.log('Recuperando evento %s.', id);
    servicio.obtenerEventoPorId(id, function(err, evento) {
        if (err) {
            console.warn('Error: %s.', err);
            res.json(500, [err.message]);
        } else {
            res.json(200, evento);
        }
    });
});

app.post('/eventos', function(req, res) {
    var evento = req.body;
    // transformaciones y comprobaciones aquí
    var doc = evento; 
    doc.fecha = new Date(doc.fecha);
    // Invocando servicio
    console.log('Registrando nuevo evento: %s.', JSON.stringify(doc));
    servicio.registrarNuevoEvento(doc, function(err, result) {
        if (err) {
            console.warn('Error: %s.', err);
            res.json(500, [err.message]);
        } else {
            res.json(200, result);
        }
    });
});


var server  = http.createServer(app);
server.listen(8080, function() {
  console.log('Servidor arrancado en 8080.');
});
