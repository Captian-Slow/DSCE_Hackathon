const express = require('express');
const app = express();
const mongoose = require('mongoose');
const bodyParser = require('body-parser');


app.set('view engine', 'jade');
app.use(bodyParser.json());
app.use(require('./routes/routes'));

//Error handling middleware
app.use(function(err, req, res, next) {
     //console.log("ERROR: " + err);
     res.status(422).send({
          error: err.message
     });
});

//Connect to mongodb
mongoose.connect('mongoDb://localhost/do_something_db', { useMongoClient: true });﻿
mongoose.Promise = global.Promise;

app.listen(process.env.port || 4000, function() {

     console.log("Server listening on PORT: 4000");
})
