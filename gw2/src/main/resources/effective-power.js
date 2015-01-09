function getEffectivePower(power, precision, ferocity, might, fury) {
  var powerWithMight = power + might * 35;
  var criticalChance = getCriticalChance(precision, fury);
  var criticalPower = getCriticalPower(powerWithMight, ferocity);
  return (1 - criticalChance) * powerWithMight + criticalChance * criticalPower;
}

function getCriticalPower(power, ferocity) {
  var multiplier = 1.5 + ferocity / 1500;
  return power * multiplier;
}

function getCriticalChance(precision, fury) {
  var criticalChanceNoFury = clamp01((precision - 832) / 2116);
  var criticalChanceFury = clamp01(criticalChanceNoFury + 0.20);
  return (1 - fury) * criticalChanceNoFury + fury * criticalChanceFury;
}

function clamp01(x) {
  x = Math.min(x, 1);
  x = Math.max(x, 0);
  return x;
}

function refresh() {
  var power = getInput("power");
  var precision = getInput("precision");
  var ferocity = getInput("ferocity");
  var might = getInput("might");
  var furyPercentage = getInput("fury");

  var errors = [];
  if (power < 0)
    errors.push("Power cannot be negative.");
  if (precision < 0)
    errors.push("Precision cannot be negative.");
  if (ferocity < 0)
    errors.push("Ferocity cannot be negative.");
  if (might < 0)
    errors.push("Might stacks cannot be negative.");
  if (might > 25)
    errors.push("Might stacks cannot exceed 25.");
  if (furyPercentage < 0)
    errors.push("Fury uptime cannot be negative.");
  if (furyPercentage > 100)
    errors.push("Fury uptime cannot exceed 100%.");
  if (errors.length > 0) {
    // TODO: replace the alert with html.
    alert(errors.join('\n'));
    return;
  }

  var powerWithMight = power + might * 35;
  var fury = furyPercentage / 100;
  var criticalChancePercent = getCriticalChance(precision, fury) * 100;
  var criticalPower = getCriticalPower(powerWithMight, ferocity);
  var effectivePower = getEffectivePower(power, precision, ferocity, might, fury)

  var effectivePower1MorePower = getEffectivePower(power + 1, precision, ferocity, might, fury) - effectivePower;
  var effectivePower1MorePrecision = getEffectivePower(power, precision + 1, ferocity, might, fury) - effectivePower;
  var effectivePower1MoreFerocity = getEffectivePower(power, precision, ferocity + 1, might, fury) - effectivePower;

  setOutput("non_critical_power", powerWithMight.toFixed(0));
  setOutput("critical_power", criticalPower.toFixed(0));
  setOutput("critical_chance", criticalChancePercent.toFixed(0) + '%');
  setOutput("effective_power", effectivePower.toFixed(0));
  setOutput("increase_power", effectivePower1MorePower.toFixed(2));
  setOutput("increase_precision", effectivePower1MorePrecision.toFixed(2));
  setOutput("increase_ferocity", effectivePower1MoreFerocity.toFixed(2));
}

function getInput(id) {
  return parseInt(document.getElementById(id).value);
}

function setOutput(id, result) {
  var td = document.getElementById(id);
  while (td.firstChild)
    td.removeChild(td.firstChild);
  td.appendChild(document.createTextNode(result));
}

window.addEventListener('load', function() {
  refresh();
}, false);