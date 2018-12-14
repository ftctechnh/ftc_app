/**
 * @fileoverview FTC robot blocks related to elapsed time.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// elapsedTimeIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

var elapsedTimeDefaultVarName = 'timer';

Blockly.Blocks['elapsedTime_create'] = {
  init: function() {
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('ElapsedTime'));
    this.setColour(functionColor);
    this.setTooltip('Creates a new ElapsedTime object.');
  }
};

Blockly.JavaScript['elapsedTime_create'] = function(block) {
  var code = elapsedTimeIdentifierForJavaScript + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['elapsedTime_create'] = function(block) {
  Blockly.FtcJava.generateImport_('ElapsedTime');
  var code = 'new ElapsedTime()';
  return [code, Blockly.FtcJava.ORDER_NEW];
};

// Properties

Blockly.Blocks['elapsedTime_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['StartTime', 'StartTime'],
        ['Seconds', 'Seconds'],
        ['Milliseconds', 'Milliseconds'],
        ['AsText', 'AsText'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(new Blockly.FieldVariable(elapsedTimeDefaultVarName), 'VAR')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['StartTime', 'Returns a numeric value representing the time at which this timer was last reset.'],
        ['Seconds', 'Returns the duration that has elapsed since the last reset of this timer, ' +
            'in seconds.'],
        ['Milliseconds', 'Returns the duration that has elapsed since the last reset of this timer, ' +
            'in milliseconds.'],
        ['AsText', 'Returns text indicating the current elapsed time of the timer.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('PROP');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['elapsedTime_getProperty'] = function(block) {
  var varName = Blockly.JavaScript.variableDB_.getName(
      block.getFieldValue('VAR'), Blockly.Variables.NAME_TYPE);
  var property = block.getFieldValue('PROP');
  var code = elapsedTimeIdentifierForJavaScript + '.get' + property + '(' + varName + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['elapsedTime_getProperty'] = function(block) {
  var varName = Blockly.FtcJava.variableDB_.getName(
      block.getFieldValue('VAR'), Blockly.Variables.NAME_TYPE);
  var property = block.getFieldValue('PROP');
  var code;
  switch (property) {
    case 'StartTime':
    case 'Seconds':
    case 'Milliseconds':
      code = varName + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property) + '()';
      break;
    case 'AsText':
      code = varName + '.toString()';
      break;
    default:
      throw 'Unexpected property ' + property + ' (elapsedTime_getProperty).';
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

// Functions

Blockly.Blocks['elapsedTime_reset'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(new Blockly.FieldVariable(elapsedTimeDefaultVarName), 'VAR')
        .appendField('.')
        .appendField(createNonEditableField('reset'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Reset this timer.');
  }
};

Blockly.JavaScript['elapsedTime_reset'] = function(block) {
  var varName = Blockly.JavaScript.variableDB_.getName(
      block.getFieldValue('VAR'), Blockly.Variables.NAME_TYPE);
  return elapsedTimeIdentifierForJavaScript + '.reset(' + varName + ');\n';
};

Blockly.FtcJava['elapsedTime_reset'] = function(block) {
  var varName = Blockly.FtcJava.variableDB_.getName(
      block.getFieldValue('VAR'), Blockly.Variables.NAME_TYPE);
  return varName + '.reset();\n';
};
