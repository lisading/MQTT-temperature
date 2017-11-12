/* global client, Paho */

// Javascript MQTT Web Application
// Code referenced from https://eclipse.org/paho/clients/js/
// Another code reference: https://www.hivemq.com/blog/build-javascript-mqtt-web-application

var subscribe = "temperature/pittsburgh/#";

// Create a client instance
client = new Paho.MQTT.Client("localhost", Number(9002), "TemperatureSubscriberProject");

// set callback handlers
client.onConnectionLost = onConnectionLost;
client.onMessageArrived = onMessageArrived;

// connect the client
client.connect({onSuccess: onConnect});

// called when the client connects
function onConnect() {
    // Once a connection has been made, make a subscription to MouseTracker.
    
    console.log("onConnect");
    
    client.subscribe(subscribe, {qos: 2});
    
    // Send a message
    //message = new Paho.MQTT.Message("Hello");
    //message.destinationName = "World";
    //client.send(message);
}

// called when the client loses its connection
function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
        console.log("onConnectionLost: " + responseObject.errorMessage);
    }
}

// called when a message arrives
function onMessageArrived(message) {
    console.log("onMessageArrived: " + message.payloadString);
    
    var json = JSON.parse(message.payloadString);
    var str = "Temperature: " + json.temperature + " degrees , Time: " + json.time + "</br>";

    document.getElementById("demo").innerHTML = str; // display the x and y coordinates (at id demo)
}

function getTemp() {
    var checked = document.querySelector('input[name = "temp"]:checked').value;
    console.log("checkedValue: " + checked);
    
    client.unsubscribe(subscribe);
    console.log("Unsubscribed");
    document.getElementById('demo').innerHTML = "";
    
    if (checked == "allTemps") {
        subscribe = "temperature/pittsburgh/#";
        client.subscribe(subscribe, {qos: 2});
    }
    if (checked == "coldTemps") {
        subscribe = "temperature/pittsburgh/coldTemps";        
        client.subscribe(subscribe, {qos: 2});
    }
    if (checked == "niceTemps") {
        subscribe = "temperature/pittsburgh/niceTemps";
        client.subscribe(subscribe, {qos: 2});
    }
    if (checked == "hotTemps") {
        subscribe = "temperature/pittsburgh/hotTemps";        
        client.subscribe(subscribe, {qos: 2});
    }
    
    document.getElementById('demo').innerHTML = "";    
    console.log("Subscribed");
}