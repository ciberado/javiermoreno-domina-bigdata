/* Require de modulos */

var Servicio = function() {
    /* Inicialización de Memcached */
};

Servicio.prototype._accederColeccionEventos = function(cb) {
    /* Obtener conexión a colección */
};

Servicio.prototype.registrarNuevoEvento = function(doc, cb) {
    /* Reglas de negocio */
    /* Insertar documento en MongoDB*/
};

Servicio.prototype.actualizarEvento = function(doc, cb) {
    /* Reglas de negocio */
    /* Actualizar documento en MongoDB */
};

Servicio.prototype.obtenerEventos = function(offset, cb, fechaMin, fechaMax) {
    /* Consultar documentos */
};

Servicio.prototype.obtenerEventoPorId = function(id, cb) {
    /* Reglas de negocio */
    /* Recuperar de Memcached */
    /* Recuperar de MongoDB */
    /* Guardar en Memcached */
};

module.exports.servicio = new Servicio();