/**
 * @fileoverview FTC robot blocks related to telemetry.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// telemetryIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// functionColor

// Functions

Blockly.Blocks['telemetry_addNumericData'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Telemetry'))
        .appendField('.')
        .appendField(createNonEditableField('addData'));
    this.appendValueInput('KEY') // no type, for compatibility
        .appendField('key')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('NUMBER') // no type, for compatibility
        .appendField('number')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Add a numeric data point.');
  }
};

Blockly.JavaScript['telemetry_addNumericData'] = function(block) {
  var key = Blockly.JavaScript.valueToCode(
      block, 'KEY', Blockly.JavaScript.ORDER_COMMA);
  var number = Blockly.JavaScript.valueToCode(
      block, 'NUMBER', Blockly.JavaScript.ORDER_COMMA);
  return telemetryIdentifierForJavaScript + '.addNumericData(' + key + ', ' + number + ');\n';
};

Blockly.FtcJava['telemetry_addNumericData'] = function(block) {
  var key = Blockly.FtcJava.valueToCode(
      block, 'KEY', Blockly.FtcJava.ORDER_COMMA);
  var number = Blockly.FtcJava.valueToCode(
      block, 'NUMBER', Blockly.FtcJava.ORDER_COMMA);
  return 'telemetry.addData(' + key + ', ' + number + ');\n';
};

Blockly.Blocks['telemetry_addNumericData_Number'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Telemetry'))
        .appendField('.')
        .appendField(createNonEditableField('addData'));
    this.appendValueInput('KEY').setCheck('String')
        .appendField('key')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('NUMBER').setCheck('Number')
        .appendField('number')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Add a numeric data point.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'NUMBER':
          return 'double';
      }
      return '';
    };
  }
};

Blockly.JavaScript['telemetry_addNumericData_Number'] =
    Blockly.JavaScript['telemetry_addNumericData'];

Blockly.FtcJava['telemetry_addNumericData_Number'] =
    Blockly.FtcJava['telemetry_addNumericData'];

Blockly.Blocks['telemetry_addTextData'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Telemetry'))
        .appendField('.')
        .appendField(createNonEditableField('addData'));
    this.appendValueInput('KEY') // no type, for compatibility
        .appendField('key')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('TEXT') // no type, for compatibility
        .appendField('text')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Add a data point.');
  }
};

Blockly.JavaScript['telemetry_addTextData'] = function(block) {
  var key = Blockly.JavaScript.valueToCode(
      block, 'KEY', Blockly.JavaScript.ORDER_COMMA);
  var text = Blockly.JavaScript.valueToCode(
      block, 'TEXT', Blockly.JavaScript.ORDER_COMMA);
  return 'telemetryAddTextData(' + key + ', ' + text + ');\n';
};

Blockly.FtcJava['telemetry_addTextData'] = function(block) {
  var key = Blockly.FtcJava.valueToCode(
      block, 'KEY', Blockly.FtcJava.ORDER_COMMA);
  var text = Blockly.FtcJava.valueToCode(
      block, 'TEXT', Blockly.FtcJava.ORDER_COMMA);
  return 'telemetry.addData(' + key + ', ' + text + ');\n';
};

Blockly.Blocks['telemetry_addTextData_All'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Telemetry'))
        .appendField('.')
        .appendField(createNonEditableField('addData'));
    this.appendValueInput('KEY').setCheck('String')
        .appendField('key')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('TEXT') // all types allowed
        .appendField('text')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Add a data point.');
  }
};

Blockly.JavaScript['telemetry_addTextData_All'] =
    Blockly.JavaScript['telemetry_addTextData'];

Blockly.FtcJava['telemetry_addTextData_All'] =
    Blockly.FtcJava['telemetry_addTextData'];

Blockly.Blocks['telemetry_update'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Telemetry'))
        .appendField('.')
        .appendField(createNonEditableField('update'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Send data to the driver station.');
  }
};

Blockly.JavaScript['telemetry_update'] = function(block) {
  return telemetryIdentifierForJavaScript + '.update();\n';
};

Blockly.FtcJava['telemetry_update'] = function(block) {
  return 'telemetry.update();\n';
};
