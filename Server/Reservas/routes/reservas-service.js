'use strict';

const MongoClient = require('mongodb').MongoClient;
let db;
let ObjectId = require('mongodb').ObjectId;
const Reservas = function () {
};

Reservas.prototype.connectDb = function (callback) {
    MongoClient.connect("mongodb+srv://test-pnet:test-pnet@ilb-pnet-curso-2023-202.1actfyl.mongodb.net/?retryWrites=true&w=majority&appName=ilb-pnet-curso-2023-2024",
        {useNewUrlParser: true, useUnifiedTopology: true},
        function (err, database) {
            if (err) {
				console.log(err);
				callback(err);
            }

			db = database.db('ilb-pnet-curso-2023-2024').collection('Reservas');
			console.log("Conexión correcta");
            callback(err, database);
        });
};

Reservas.prototype.add = function (reserva, callback) {
    return db.insertOne(reserva, callback);
};

Reservas.prototype.get = function (_id, callback) {
    return db.find({_id: ObjectId(_id)}).toArray(callback);
};


Reservas.prototype.getAll = function (callback) {
    return db.find({}).toArray(callback);
};

Reservas.prototype.update = function (_id, updatedreserva, callback) {
    delete updatedReserva._id;
    return db.updateOne({_id: ObjectId(_id)}, {$set: updatedreserva}, callback);};

Reservas.prototype.remove = function (_id, callback) {
    return db.deleteOne({_id: ObjectId(_id)}, callback);
};

Reservas.prototype.removeAll = function (callback) {
    return db.deleteMany({}, callback);
};
Reservas.prototype.getByEmail = function (email, callback) {
    return db.find({email: email}).toArray(callback);
};

module.exports = new Reservas();


