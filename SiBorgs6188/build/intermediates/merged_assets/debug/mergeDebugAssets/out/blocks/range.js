/**
 * @fileoverview Range blocks.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// rangeIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// functionColor

// Functions

Blockly.Blocks['range_clip'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Range'))
        .appendField('.')
        .appendField(createNonEditableField('clip'));
    this.appendValueInput('NUMBER').setCheck('Number')
        .appendField('number')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MIN').setCheck('Number')
        .appendField('min')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MAX').setCheck('Number')
        .appendField('max')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Clip a number if it is less than min or greater than max.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'NUMBER':
        case 'MIN':
        case 'MAX':
          return 'double';
      }
      return '';
    };
    this.getFtcJavaOutputType = function() {
      return 'double';
    };
  }
};

Blockly.JavaScript['range_clip'] = function(block) {
  var number = Blockly.JavaScript.valueToCode(
      block, 'NUMBER', Blockly.JavaScript.ORDER_COMMA);
  var min = Blockly.JavaScript.valueToCode(
      block, 'MIN', Blockly.JavaScript.ORDER_COMMA);
  var max = Blockly.JavaScript.valueToCode(
      block, 'MAX', Blockly.JavaScript.ORDER_COMMA);
  var code = rangeIdentifierForJavaScript + '.clip(' + number + ', ' + min + ', ' + max + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['range_clip'] = function(block) {
  var number = Blockly.FtcJava.valueToCode(
      block, 'NUMBER', Blockly.FtcJava.ORDER_COMMA);
  var min = Blockly.FtcJava.valueToCode(
      block, 'MIN', Blockly.FtcJava.ORDER_COMMA);
  var max = Blockly.FtcJava.valueToCode(
      block, 'MAX', Blockly.FtcJava.ORDER_COMMA);
  var code = 'Range.clip(' + number + ', ' + min + ', ' + max + ')';
  Blockly.FtcJava.generateImport_('Range');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['range_scale'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Range'))
        .appendField('.')
        .appendField(createNonEditableField('scale'));
    this.appendValueInput('NUMBER').setCheck('Number')
        .appendField('number')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('X1').setCheck('Number')
        .appendField('x1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('X2').setCheck('Number')
        .appendField('x2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Y1').setCheck('Number')
        .appendField('y1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Y2').setCheck('Number')
        .appendField('y2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Scale a number in the range of x1 to x2, to the range of y1 to y2.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'NUMBER':
        case 'X1':
        case 'X2':
        case 'Y1':
        case 'Y2':
          return 'double';
      }
      return '';
    };
    this.getFtcJavaOutputType = function() {
      return 'double';
    };
  }
};

Blockly.JavaScript['range_scale'] = function(block) {
  var number = Blockly.JavaScript.valueToCode(
      block, 'NUMBER', Blockly.JavaScript.ORDER_COMMA);
  var x1 = Blockly.JavaScript.valueToCode(
      block, 'X1', Blockly.JavaScript.ORDER_COMMA);
  var x2 = Blockly.JavaScript.valueToCode(
      block, 'X2', Blockly.JavaScript.ORDER_COMMA);
  var y1 = Blockly.JavaScript.valueToCode(
      block, 'Y1', Blockly.JavaScript.ORDER_COMMA);
  var y2 = Blockly.JavaScript.valueToCode(
      block, 'Y2', Blockly.JavaScript.ORDER_COMMA);
  var code = rangeIdentifierForJavaScript + '.scale(' + number + ', ' + x1 + ', ' + x2 + ', ' + y1 + ', ' + y2 + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['range_scale'] = function(block) {
  var number = Blockly.FtcJava.valueToCode(
      block, 'NUMBER', Blockly.FtcJava.ORDER_COMMA);
  var x1 = Blockly.FtcJava.valueToCode(
      block, 'X1', Blockly.FtcJava.ORDER_COMMA);
  var x2 = Blockly.FtcJava.valueToCode(
      block, 'X2', Blockly.FtcJava.ORDER_COMMA);
  var y1 = Blockly.FtcJava.valueToCode(
      block, 'Y1', Blockly.FtcJava.ORDER_COMMA);
  var y2 = Blockly.FtcJava.valueToCode(
      block, 'Y2', Blockly.FtcJava.ORDER_COMMA);
  var code = 'Range.scale(' + number + ', ' + x1 + ', ' + x2 + ', ' + y1 + ', ' + y2 + ')';
  Blockly.FtcJava.generateImport_('Range');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
