var connection;

function send() {
  if (!webSocketSupported()) {
    addMessage("Your browser does not support WebSocket.");
    return;
  }

  var messageBox = document.getElementById("message");
  var message = messageBox.value;
  connection.send(message);
  messageBox.value = "";
}

function addMessage(message) {
  var p = document.createElement("p");
  p.appendChild(document.createTextNode(message));
  document.getElementById("messages").appendChild(p);
}

function webSocketSupported() {
  return 'WebSocket' in window;
}

function start() {
  var uri = "ws" + document.URL.substring(4);
  connection = new WebSocket(uri);
  connection.onopen = function() {
    addMessage("Connected to server.");
  }
  connection.onclose = function() {
    addMessage("Connection was closed.");
  }
  connection.onerror = function(error) {
    addMessage("Error detected: " + error);
  }
  connection.onmessage = function(e) {
    addMessage(e.data);
  }
}

window.onload = start;
