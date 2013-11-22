var MAX_CHECKS_TO_STORE = 20;
var connection;

Array.prototype.contains = function(obj) {
  return this.indexOf(obj) != -1;
};

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
  if (!message.addCheck) {
    console.log(message);
  }
  if (message.createAlert) {
    var createAlert = message.createAlert;
    var alert = {
      uuid: createAlert.uuid,
      name: createAlert.name,
      description: createAlert.description,
      command: createAlert.command,
      frequency: createAlert.frequency,
      tags: createAlert.tags,
      recipientUuids: createAlert.recipientUuids,
      checks: []
    }
    alerts[alert.uuid] = alert;
    refresh(); // TODO
  }
  if (message.addCheck) {
    var addCheck = message.addCheck;
    var check = {
      triggeredAtMillis: addCheck.triggeredAtMillis,
      durationMillis: addCheck.durationMillis,
      status: addCheck.status,
      details: addCheck.details
    };
    alert = alerts[addCheck.alertUuid];
    checks = alert.checks;
    if (checks.length >= MAX_CHECKS_TO_STORE) {
      checks.shift();
    }
    checks.push(check);

    updateAlertTable(alert);
    if (getRequestedResource().startsWith('/alert/' + alert.uuid)) {
      var checkTables = document.getElementsByClassName('checks-table');
      for (var i = 0; i < checkTables.length; ++i) {
        var checkTable = checkTables[i];
        checkTable.parentNode.replaceChild(generateCheckTable(alert.checks), checkTable);
      }
    }
  }
  if (message.addTag) {
    var addTag = message.addTag;
    var alert = alerts[addTag.alertUuid];
    alert.tags.push(addTag.tag);

    if (getRequestedResource().startsWith('/alert/' + alert.uuid)) {
      var li = document.createElement('li');
      li.appendChild(generateTagLink(addTag.tag));
      var tagList = document.getElementsByClassName('tag-list')[0];
      tagList.appendChild(li);
    }
  }
  if (message.editAlertName) {
    // TODO
    updateAlertTable(alert);
  }
  if (message.editAlertDescription) {
    // TODO
    updateAlertTable(alert);
  }
  if (message.editAlertCommand) {
    var editAlertCommand = message.editAlertCommand;
    var alert = alerts[editAlertCommand.alertUuid];
    alert.command = editAlertCommand.newCommand;

    updateAlertTable(alert);
    if (getRequestedResource().startsWith('/alert/' + alert.uuid)) {
      var editor = document.getElementsByClassName('command-editor')[0];
      editor.value = alert.command;
    }
  }
  if (message.jumpToAlert) {
    loadPage('/alert/' + message.jumpToAlert.alertUuid);
  }
}

function updateAlertTable(alert) {
  if (getRequestedResource().startsWith('/search/')) {
    var query = decodeURI(getRequestedResource().substring('/search/'.length));
    var tables = document.getElementsByClassName('alert-table');
    for (var i = 0; i < tables.length; ++i) {
      tables[i].parentNode.replaceChild(generateAlertTable(query), tables[i]);
    }
    return;
  }
  var rows = document.getElementsByClassName('alert-' + alert.uuid);
  for (var i = 0; i < rows.length; ++i) {
    var row = rows[i];
    row.parentNode.replaceChild(createRow(alert), row);
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
  body.appendChild(getCreateAlertLink());

  window.history.pushState("Foo", "Nagger - " + resource, resource);
}

function getTitle() {
  var titleLink = document.createElement('a');
  titleLink.href = 'javascript:loadPage("/");'; // TODO
  titleLink.appendChild(document.createTextNode('Nagger'));

  var titleHeader = document.createElement('h1');
  titleHeader.appendChild(titleLink);
  return titleHeader;
}

function getCreateAlertLink() {
  var a = document.createElement('a');
  a.href = 'javascript:loadPage("/create-alert");'; // TODO
  a.appendChild(document.createTextNode('Create New Alert'));

  var p = document.createElement('p');
  p.style.textAlign = 'center';
  p.style.marginTop = '0.5em';
  p.style.marginBottom = '1em';
  p.appendChild(a);
  return p;
}

// TODO: Should never do this since we lose input.
function refresh() {
  loadPage(getRequestedResource());
}

function getContent(resource) {
  if (resource == '/') {
    return generateAlertTable('true');
  }
  if (resource.startsWith('/search/')) {
    var query = decodeURI(resource.substring('/search/'.length));
    return generateSearchView(query);
  }
  if (resource.startsWith('/create-alert')) {
    return generateCreateAlertForm();
  }
  if (resource.startsWith('/alert/')) {
    return generateAlertView(resource.substring('/alert/'.length));
  }
  return generate404();
}

function generateSearchView(query) {
  var textbox = document.createElement('input');
  textbox.type = 'text';
  textbox.value = query;
  textbox.style.display = 'block';
  textbox.style.width = '100%';

  // Wrapper with overflow:hidden is need to get <input> to display correctly.
  var textboxWrapper = document.createElement('div');
  textboxWrapper.appendChild(textbox);
  textboxWrapper.style.overflow = 'hidden';

  var submitButton = document.createElement('button');
  submitButton.appendChild(document.createTextNode('Update Search'));
  submitButton.style.display = 'block';
  submitButton.style.cssFloat = 'right';
  submitButton.style.marginLeft = '1em';

  submitButton.onclick = function() {
    loadPage('/search/' + textbox.value);
  }

  var searchArea = document.createElement('div');
  searchArea.style.marginTop = '0.5em';
  searchArea.style.marginBottom = '0.5em';
  searchArea.appendChild(submitButton);
  searchArea.appendChild(textboxWrapper);

  var searchView = document.createElement('div');
  searchView.className = 'search';
  searchView.appendChild(searchArea);
  searchView.appendChild(generateAlertTable(query));
  return searchView;
}

function generateCreateAlertForm() {
  var header = document.createElement('h2');
  header.appendChild(document.createTextNode('Create New Alert'));

  var nameBox = document.createElement('input');
  nameBox.type = 'text';
  nameBox.placeholder = 'alert name';

  var descriptionBox = document.createElement('input');
  descriptionBox.type = 'text';
  descriptionBox.placeholder = 'description';

  var commandBox = document.createElement('textarea');
  commandBox.rows = 5;
  commandBox.className = 'command-editor';
  commandBox.placeholder = 'command to run';

  var frequencyBox = document.createElement('input');
  frequencyBox.type = 'text';
  frequencyBox.placeholder = 'frequency';

  var createButton = document.createElement('button');
  createButton.appendChild(document.createTextNode('Create'));
  createButton.onclick = function() {
    var message = {
      createAlert: {
        name: nameBox.value,
        description: descriptionBox.value,
        command: commandBox.value,
        frequency: frequencyBox.value,
        tags: [], // TODO
        recipientUuids: [] // TODO
      }
    };
    connection.send(JSON.stringify(message));

    // TODO: gray out form.
  }

  var div = document.createElement('div');
  div.className = 'create-alert';
  div.appendChild(header);
  div.appendChild(nameBox);
  div.appendChild(descriptionBox);
  div.appendChild(commandBox);
  div.appendChild(frequencyBox);
  div.appendChild(createButton);
  return div;
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
  div.appendChild(generateCommandSection(alert));
  div.appendChild(generateTagSection(alert));
  div.appendChild(generateChecksSection(alert.checks));
  return div;
}

function generateCommandSection(alert) {
  var title = document.createElement('h3');
  title.appendChild(document.createTextNode('Command'));

  var editor = document.createElement('textarea');
  editor.className = 'command-editor';
  editor.value = alert.command;
  editor.rows = 5;

  var saveButton = document.createElement('button');
  saveButton.appendChild(document.createTextNode('Save Command'));
  saveButton.onclick = function() {
    var message = {
      editAlertCommand: {
        alertUuid: alert.uuid,
        newCommand: editor.value
      }
    };
    connection.send(JSON.stringify(message));
  };

  var div = document.createElement('div');
  div.className = 'command';
  div.appendChild(title);
  div.appendChild(editor);
  div.appendChild(saveButton);
  return div;
}

function generateTagSection(alert) {
  var title = document.createElement('h3');
  title.appendChild(document.createTextNode('Tags'));

  var textbox = document.createElement('input');
  textbox.type = 'text';
  textbox.placeholder = 'new tag';
  textbox.onkeypress = function(e) {
    e = e || window.event;
    var keyCode = e.keyCode || e.which;
    if (keyCode == '13') {
      var message = {
        addTag: {
          alertUuid: alert.uuid,
          tag: textbox.value
        }
      };
      connection.send(JSON.stringify(message));
      textbox.value = '';
      return false;
    }
  }

  var div = document.createElement('div');
  div.className = 'tags';
  div.appendChild(title);
  div.appendChild(generateTagList(alert.tags));
  div.appendChild(textbox);
  return div;
}

function generateTagList(tags) {
  var list = document.createElement('ul');
  list.className = 'tag-list';
  for (var i = 0; i < tags.length; ++i) {
    var li = document.createElement('li');
    li.appendChild(generateTagLink(tags[i]));
    list.appendChild(li);
  }
  return list;
}

function generateTagLink(tag) {
  var query = 'tags.contains(\'' + tag + '\')';
  var link = document.createElement('a');
  link.href = '/search/' + query;
  link.appendChild(document.createTextNode(tag));
  return link;
}

function generateChecksSection(checks) {
  var title = document.createElement('h3');
  title.appendChild(document.createTextNode('Recent Checks'));

  var div = document.createElement('div');
  div.className = 'checks';
  div.appendChild(title);
  div.appendChild(generateCheckTable(checks));
  return div;
}

function generateCheckTable(checks) {
  var headRow = document.createElement('tr');
  headRow.appendChild(generateTh('Triggered'));
  headRow.appendChild(generateTh('Duration'));
  headRow.appendChild(generateTh('Status'));
  headRow.appendChild(generateTh('Details'));
  var thead = document.createElement('thead');
  thead.appendChild(headRow);

  var tbody = document.createElement('tbody');
  for (var i = checks.length - 1; i >= 0; --i) {
    tbody.appendChild(generateCheckRow(checks[i]));
  }

  var table = document.createElement('table');
  table.className = 'hairlined checks-table';
  table.appendChild(thead);
  table.appendChild(tbody);
  return table;
}

function generateCheckRow(check) {
  var sinceTriggered = new Date().getTime() - check.triggeredAtMillis;
  var tdTriggeredAt = document.createElement('td');
  tdTriggeredAt.appendChild(document.createTextNode(reprInterval(sinceTriggered) + ' ago'));

  var tdDurationMillis = document.createElement('td');
  tdDurationMillis.appendChild(document.createTextNode(reprInterval(check.durationMillis)));

  var tdStatus = document.createElement('td');
  if (check.status) {
    tdStatus.className = "status-" + check.status.toLowerCase();
  }
  tdStatus.appendChild(document.createTextNode(check.status));

  var tdDetails = document.createElement('td');
  tdDetails.appendChild(document.createTextNode(check.details));

  var tr = document.createElement('tr');
  tr.appendChild(tdTriggeredAt);
  tr.appendChild(tdDurationMillis);
  tr.appendChild(tdStatus);
  tr.appendChild(tdDetails);
  return tr;
}

function generate404() {
  var p = document.createElement('p');
  p.appendChild(document.createTextNode('Not found.'));
  return p;
}

function generateAlertTable(query) {
  var thead = generateAlertThead();
  var tbody = generateAlertTbody(query);

  var table = document.createElement('table');
  table.className = 'alert-table hairlined';
  table.appendChild(thead);
  table.appendChild(tbody);
  return table;
}

function generateAlertThead() {
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

function generateAlertTbody(query) {
  var tbody = document.createElement('tbody');
  for (var alertUuid in alerts) {
    var alert = alerts[alertUuid];
    if (!query || isQuerySatisfied(query, alert)) {
      tbody.appendChild(createRow(alert));
    }
  }
  return tbody;
}

function createRow(alert) {
  var lastCheck = alert.checks.last();
  var status = lastCheck ? lastCheck.status : '';
  var since = lastCheck ? reprInterval(new Date().getTime() - lastCheck.triggeredAtMillis) + ' ago' : '';

  var link = document.createElement('a');
  link.appendChild(document.createTextNode(alert.name));
  link.href = '/alert/' + alert.uuid;
  link.onclick = function() {
    loadPage('/alert/' + alert.uuid);
    return false;
  };
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
  tr.className = 'alert-' + alert.uuid;
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
    return days + ' day' + plural(days);
  }
  if (hours) {
    return hours + ' hour' + plural(hours);
  }
  if (minutes) {
    return minutes + ' minute' + plural(minutes);
  }
  if (seconds) {
    return seconds + ' second' + plural(seconds);
  }
  if (millis) {
    return millis + ' ms';
  }

  return 'a moment';
}

function plural(n) {
  return n == 1 ? '' : 's';
}

function isQuerySatisfied(query, alert) {
  var tags = alert.tags;
  var status = alert.checks ? alert.checks.last.status : '';
  return eval(query);
}
