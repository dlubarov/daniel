var connection;

function send() {
  if (!webSocketSupported()) {
    addMessage("Your browser does not support WebSocket.");
    return;
  }

  var messageBox = document.getElementById("message");
  var name = document.getElementById("name").value;
  var message = {
    "name": name,
    "message": messageBox.value
  };
  connection.send(JSON.stringify(message));
  messageBox.value = "";
}

function addMessage(message) {
  var p = document.createElement("p");
  p.appendChild(document.createTextNode(message));
  document.getElementById("messages").appendChild(p);
}

function addChatMessage(name, message) {
  var nameElem = document.createElement("strong");
  nameElem.appendChild(document.createTextNode(name));
  var p = document.createElement("p");
  p.appendChild(nameElem);
  p.appendChild(document.createTextNode(": "));
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
    var message = JSON.parse(e.data);
    addChatMessage(message["name"], message["message"]);
  }
}

window.onload = start;
