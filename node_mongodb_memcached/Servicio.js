var MongoClient = require('mongodb').MongoClient,
    ObjectId = require('mongodb').ObjectID,
    memcache = require('memcache');

var Servicio = function() {
    var self = this;
    var memcached = new memcache.Client(11211, 'localhost');
    memcached.connect();
    memcached.on('connect', function() {
        console.log('Conexión con memcached creada.');
        self.memcached = memcached;
    });
};

Servicio.prototype._accederColeccionEventos = function(cb) {
    MongoClient.connect('mongodb://localhost:27017/eventosdb', function(err, db) {
        if (err) throw err;
        db.createCollection('eventos', function(err, collection) {
            if (err) throw err;
            cb(collection);
        });
    });    
};

Servicio.prototype.registrarNuevoEvento = function(doc, cb) {
    if (doc.fecha.getTime() < new Date().getTime()) { // <-- Reglas de negocio.
        cb(new Error('No disponemos de máquinas del tiempo.'));
    } else {
        this._accederColeccionEventos(function(eventos) {
            eventos.insert(doc, { w : 1 /* Espera ack de inserción. */}, function(err, result) {
                cb(err, result);
            });
        });
    }
};

Servicio.prototype.actualizarEvento = function(doc, cb) {
    // TODO  
};

Servicio.prototype.obtenerEventos = function(offset, cb, fechaMin, fechaMax) {
    this._accederColeccionEventos(function(eventos) {
        var query;        
        if (fechaMin && fechaMax) {
            query = {  
                fecha : {   
                    $gte: ISODate(fechaMin),
                    $lt: ISODate(fechaMax)
                }
            };
        } else {
            query = {};
        }
        var options = { skip : offset, limit : 5, sort: '_id' };
        var cursor = eventos.find(query, options);
        cursor.toArray(function(err, eventos) {
            cb(err, eventos);
        });
    });
};

Servicio.prototype.obtenerEventoPorId = function(id, cb) {
    // Comprobación de parámetros críticos
    if (!id || id.length != '53b3341a3daa8f141dac4b43'.length) {
        cb(new Error('Id incorrecto: ' + id + '.'));
    } else {
        var self = this;
        // Intento de encontrar el evento en memcached
        this.memcached.get('evento_' + id, function(err, evento) {
            if (err) {
                cb(err, null);
            } else if (evento != null) {
                console.log('Evento recuperado de la caché.');
                cb(undefined, JSON.parse(evento));
            } else {
                // Recuperación de evento desde mongodb
                self._accederColeccionEventos(function(eventos) {
                    var query = {'_id': new ObjectId(id)};        
                    eventos.findOne(query, function(err, evento) {                    
                        cb(err, evento);
                        if (evento) {
                            // Actualización en la caché.
                            console.log('Almacenando evento %s en caché.', id);
                            self.memcached.set('evento_' + id, JSON.stringify(evento));
                        }
                    });
                });
            }
        });
    }
};

module.exports.servicio = new Servicio();