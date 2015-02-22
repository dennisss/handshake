'use strict';

var express = require('express'),
	bodyParser = require('body-parser');

var app = express()

app.use(bodyParser.json());



app.post('/shake', function (req, res) {
	console.log(req.body)

	res.send({la: 'fdfdf'});
})



var server = app.listen(80, function () {
	console.log('Now listening');

})
