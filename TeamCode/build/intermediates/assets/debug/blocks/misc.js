/**
 * @fileoverview Comment blocks.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// miscIdentifier
// The following are defined in vars.js:
// commentColor
// functionColor

Blockly.Blocks['comment'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldTextInput(''), 'COMMENT');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(commentColor);
  }
};

Blockly.JavaScript['comment'] = function(block) {
  return '';
};

Blockly.Blocks['misc_null'] = {
  init: function() {
    this.setOutput(true); // no type for null
    this.appendDummyInput()
        .appendField(createNonEditableField('null'));
    this.setColour(functionColor);
    this.setTooltip('null');
  }
};

Blockly.JavaScript['misc_null'] = function(block) {
  var code = miscIdentifier + '.getNull()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['misc_isNull'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('isNull'));
    this.appendValueInput('VALUE') // all types allowed
        .appendField('value')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns true if the given value is null, false otherwise.');
  }
};

Blockly.JavaScript['misc_isNull'] = function(block) {
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  var code = miscIdentifier + '.isNull(' + value + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['misc_isNotNull'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('isNotNull'));
    this.appendValueInput('VALUE') // all types allowed
        .appendField('value')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns true if the given value is not null, false otherwise.');
  }
};

Blockly.JavaScript['misc_isNotNull'] = function(block) {
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  var code = miscIdentifier + '.isNotNull(' + value + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['misc_atan2'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('atan2');
    this.appendValueInput('Y').setCheck('Number')
        .appendField('y')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('X').setCheck('Number')
        .appendField('x')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(230);
    this.setTooltip('Returns a numerical value between -180 and +180 degrees, representing ' +
        'the counterclockwise angle between the positive X axis, and the point (x, y).');
  }
};

Blockly.JavaScript['misc_atan2'] = function(block) {
  var y = Blockly.JavaScript.valueToCode(
      block, 'Y', Blockly.JavaScript.ORDER_COMMA);
  var x = Blockly.JavaScript.valueToCode(
      block, 'X', Blockly.JavaScript.ORDER_COMMA);
  var code = 'Math.atan2(' + y + ', ' + x + ') / Math.PI * 180';
  return [code, Blockly.JavaScript.ORDER_DIVISION];
};
