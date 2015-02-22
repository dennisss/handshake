var express = require('express'),
	bodyParser = require('body-parser');
	EventEmitter = require('events').EventEmitter

var app = express()

app.use(bodyParser.json({limit: '50mb'}));

var e = new EventEmitter();
var data = {};
var i = 0;

app.post('/shake', function (req, res) {

	console.log('SHAKE')

	var me = (i++) + "";
	data[me] = req.body;

	function sendother(){
		var keys = Object.keys(data);

		for(var j = 0; j < keys.length; j++){
			if(keys[j] != me){
				var d = data[ keys[j] ];
				res.send(d);
				delete data[ keys[j] ];
				return true;
			}
		}

		return false;
	}

	// Will trigger the first client
	e.emit('wake');

	// Will succeed if this was the second client to connect
	if(sendother())
		return;

	// Will run for the first client
	e.once('wake', function(){
		sendother();
	})
})

app.post('/echo', function(req, res){
	res.send(req.body);
});



var server = app.listen(80, function () {
	console.log('Now listening');

})
