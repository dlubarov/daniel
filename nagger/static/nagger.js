var connection;

Array.prototype.last = function() {
  return this[this.length - 1];
};

String.prototype.startsWith = function(str) {
  return this.slice(0, str.length) == str;
};

function startWS() {
  if (!webSocketSupported()) {
    alert("Please use a browser which supports WebSocket.");
  }

  var uri = "ws" + document.URL.substring(4);
  connection = new WebSocket(uri);
  connection.onopen = function() {
    console.log("Connected to server.");
  };
  connection.onclose = function() {
    console.log("Connection was closed.");
  };
  connection.onerror = function(error) {
    console.log("Error detected: " + error);
  };
  connection.onmessage = function(e) {
    var message = JSON.parse(e.data);
    handleMessage(message);
  }
}

function webSocketSupported() {
  return 'WebSocket' in window;
}

window.onload = function() {
  startWS();
  loadPage(getRequestedResource());
};

function handleMessage(message) {
  console.info(message);
  if (message.createAlert) {
    var createAlert = message.createAlert;
    var alert = {
      uuid: createAlert.uuid,
      name: createAlert.name,
      description: createAlert.description,
      command: createAlert.command,
      frequency: createAlert.frequency,
      tags: [],
      recipientUuids: [],
      checks: []
    }
    refresh();
  }
  if (message.addCheck) {
    var addCheck = message.addCheck;
    var check = {
      triggeredAtMillis: addCheck.triggeredAtMillis,
      durationMillis: addCheck.durationMillis,
      status: addCheck.status,
      details: addCheck.details
    };
    alerts[addCheck.alertUuid].checks.push(check);
    refreshAlert(alerts[addCheck.alertUuid]);
  }
  if (message.addTag) {
    ;
  }
  if (message.editAlertName) {
    ;
  }
  if (message.editAlertDescription) {
    ;
  }
  if (message.editAlertCommand) {
    ;
  }
}

function refreshAlert(alert) {
  var rows = document.getElementsByClassName('alert-' + alert.uuid);
  for (var i = 0; i < rows.length; ++i) {
    rows[i].parentNode.replaceChild(rows[i], createRow(alert));
  }
}

function getRequestedResource() {
  // TODO: This isn't entirely robust.
  return window.location.href.split(window.location.host)[1]
}

function loadPage(resource) {
  var content = document.createElement('div');
  content.appendChild(getContent(resource));
  content.className = 'fullwidth';

  var body = document.getElementsByTagName('body')[0];
  while (body.firstChild) {
    body.removeChild(body.firstChild);
  }
  body.appendChild(getTitle());
  body.appendChild(content);

  window.history.pushState("Foo", "Nagger - " + resource, resource);
}

function getTitle() {
  var titleLink = document.createElement('a');
  titleLink.href = 'javascript:loadPage("/");';
  titleLink.appendChild(document.createTextNode('Nagger'));

  var titleHeader = document.createElement('h1');
  titleHeader.appendChild(titleLink);
  return titleHeader;
}

function refresh() {
  loadPage(getRequestedResource());
}

function getContent(resource) {
  if (resource == '/') {
    return generateTable();
  }
  if (resource.startsWith('/alert/')) {
    return generateAlertView(resource.substring('/alert/'.length));
  }
  return generate404();
}

function generateAlertView(alertUuid) {
  var alert = alerts[alertUuid];
  if (!alert) {
    return generate404();
  }

  var title = document.createElement('h2');
  title.appendChild(document.createTextNode(alert.name));

  var desc = document.createElement('p');
  desc.appendChild(document.createTextNode(alert.description));

  var div = document.createElement('div');
  div.appendChild(title);
  div.appendChild(desc);
  div.appendChild(generateTagSection(alert.tags));
  return div;
}

function generateTagLink(tag) {
  var link = document.createElement('a');
  link.href = '/tag/' + tag;
  link.appendChild(document.createTextNode(tag));
  return link;
}

function generateTagSection(tags) {
  var title = document.createElement('h3');
  title.appendChild(document.createTextNode('Tags'));

  var textbox = document.createElement('input');
  textbox.placeholder = 'new tag';

  var div = document.createElement('div');
  div.className = 'tags';
  div.appendChild(title);
  div.appendChild(generateTagList(tags));
  div.appendChild(textbox);
  return div;
}

function generateTagList(tags) {
  var list = document.createElement('ul');
  for (var i = 0; i < tags.length; ++i) {
    var li = document.createElement('li');
    li.appendChild(generateTagLink(tags[i]));
    list.appendChild(li);
  }
  return list;
}

function generate404() {
  var p = document.createElement('p');
  p.appendChild(document.createTextNode('Not found.'));
  return p;
}

function generateTable() {
  var thead = generateThead();
  var tbody = generateTbody();

  var table = document.createElement('table');
  table.className = 'alerts';
  table.appendChild(thead);
  table.appendChild(tbody);
  return table;
}

function generateThead() {
  var tr = document.createElement('tr');
  tr.appendChild(generateTh('alert'));
  tr.appendChild(generateTh('status'));
  tr.appendChild(generateTh('last check'));
  var thead = document.createElement('thead');
  thead.appendChild(tr);
  return thead;
}

function generateTh(text) {
  var th = document.createElement('th');
  th.appendChild(document.createTextNode(text));
  return th;
}

function generateTbody() {
  var tbody = document.createElement('tbody');
  for (var alertUuid in alerts) {
    tbody.appendChild(createRow(alerts[alertUuid]));
  }
  return tbody;
}

function createRow(alert) {
  var lastCheck = alert.checks.last();
  var status = lastCheck ? lastCheck.status : '';
  var since = lastCheck ? reprInterval(new Date().getTime() - lastCheck.triggeredAtMillis) : '';

  var link = document.createElement('a');
  link.appendChild(document.createTextNode(alert.name));
  link.href = 'javascript:loadPage("/alert/' + alert.uuid + '");';
  var tdName = document.createElement('td');
  tdName.appendChild(link);

  var tdStatus = document.createElement('td');
  if (status) {
    tdStatus.className = "status-" + status.toLowerCase();
  }
  tdStatus.appendChild(document.createTextNode(status));

  var tdSince = document.createElement('td');
  tdSince.appendChild(document.createTextNode(since));

  var tr = document.createElement('tr');
  tr.classNames = 'alert-' + alert.uuid;
  tr.appendChild(tdName);
  tr.appendChild(tdStatus);
  tr.appendChild(tdSince);
  return tr;
}

function reprInterval(millis) {
  var seconds = Math.floor(millis / 1000);
  var minutes = Math.floor(seconds / 60);
  var hours = Math.floor(minutes / 60);
  var days = Math.floor(hours / 24);

  if (days) {
    return days + ' day' + plural(days) + ' ago';
  }
  if (hours) {
    return hours + ' hour' + plural(hours) + ' ago';
  }
  if (minutes) {
    return minutes + ' minute' + plural(minutes) + ' ago';
  }
  if (seconds) {
    return seconds + ' second' + plural(seconds) + ' ago';
  }

  return "a moment ago";
}

function plural(n) {
  return n == 1 ? '' : 's';
}
