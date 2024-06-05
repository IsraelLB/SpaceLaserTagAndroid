'use strict';

const express = require('express');
const router = express.Router();
const reservasService = require('./reservas-service');

router.get('/', function (req, res) {
    reservasService.getAll((err, reservas) => {
            if (err) {
                res.status(500).send({
                    msg: err
                });
            } else if (reservas.length == 0){
            	res.status(500).send({
                    msg: "reservas null"
                });
            } else {
                res.status(200).send(reservas);
            }
        }
    );
});

router.post('/', function (req, res) {
    let reserva = req.body;
    if (Object.entries(reserva).length === 0){
        res.status(400).send({
            msg: 'reservas vacia'
        });
    }
	else{
		reservasService.add(reserva, (err, reserva) => {
            if (err) {
                res.status(500).send({
                    msg: err
                });
            } 
			else
			{
                res.status(201).send({
                    msg: 'reserva creada'
                });
            }
        });
	}
});


router.delete('/', function (req, res) {
    reservasService.removeAll((err) => {
        if (err) {
            res.status(500).send({
                msg: err
            });
        } else {
            res.status(200).send({
                msg: 'reservas eliminadas'
            });
        }
    });
});


router.get('/:_id', function (req, res) {
    let _id = req.params._id;
    reservasService.get(_id, (err, reserva) => {
            if (err) {
                res.status(500).send({
                	msg: err
            	});
            } else if (reserva.length == 0){
            	res.status(500).send({
                    msg: "reserva is null"
                });
            } else {
                res.status(200).send(reserva);
            }
        }
    );
});


router.put('/:_id', function (req, res) {
    const _id = req.params._id;
    const updatedreserva = req.body;
    reservasService.update(_id, updatedreserva, (err, numUpdates) => {
        if (err) {
            res.status(500).send({
                msg: err
            });
	} else if(numUpdates.modifiedCount === 0) {
            res.status(500).send({
                msg: "No se ha actualizado la reserva"
            });
        } else {
            res.status(200).send({
                msg: 'reserva actualizada'
            });
        }
    });
});



router.delete('/:_id', function (req, res) {
    let _id = req.params._id;
    reservasService.remove(_id, (err,deletedCount) => {
        if (err) {
            res.status(500).send({
                msg: "Error eliminando reserva"
            });
        }else if(deletedCount.deletedCount === 0){
            res.status(500).send({
                msg: "No se ha eliminado la reserva"
            });
        }else {
            res.status(200).send({
                msg: 'reserva eliminada'
            });
        }
    });
});

router.get('/email/:email', function (req, res) {
    let email = req.params.email;
    reservasService.getByEmail(email, (err, reservas) => {
        if (err) {
            res.status(500).send({
                msg: err
            });
        } else if (reservas.length == 0){
            res.status(404).send({
                msg: "No hay reservas para este email"
            });
        } else {
            let mensaje = reservas.map((reserva, index) => {
                return (index + 1) + '. _id:' + reserva._id + ', Nombre: ' + reserva.Nombre + ', email: ' 
                + reserva.email + ', numero de reservas: ' + reserva['numero de reservas'] + ', pagado: ' + reserva.pagado + '.<br>';
            })
            res.status(200).send(mensaje);//haciendo esto el mensaje se manda de manera mas clara y con saltos de línea
        }
    });
});

module.exports = router;
