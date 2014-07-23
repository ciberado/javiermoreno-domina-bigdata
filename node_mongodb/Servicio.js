var MongoClient = require('mongodb').MongoClient,
    ObjectId = require('mongodb').ObjectID;

var Servicio = function() {
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
    if (!id || id.length != '111111111111111111111111'.length) {
        cb(new Error('Id incorrecto: ' + id + '.'));
    } else {
        var self = this;
        // Recuperación de evento desde mongodb
        self._accederColeccionEventos(function(eventos) {
            var query = {'_id': new ObjectId(id)};        
            eventos.findOne(query, function(err, evento) {                    
                cb(err, evento);
            });
        });
    }
};

module.exports.servicio = new Servicio();