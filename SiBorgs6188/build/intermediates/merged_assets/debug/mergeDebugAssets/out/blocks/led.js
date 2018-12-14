/**
 * @fileoverview FTC robot blocks related to LED.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createLedDropdown
// The following are defined in vars.js:
// createNonEditableField
// functionColor

// Functions

Blockly.Blocks['led_enableLed'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createLedDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('enableLed'));
    this.appendValueInput('ENABLE') // no type, for compatibility
        .appendField('enable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Enable the LED light.');
  }
};

Blockly.JavaScript['led_enableLed'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var enable = Blockly.JavaScript.valueToCode(
      block, 'ENABLE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.enableLed(' + enable + ');\n';
};

Blockly.FtcJava['led_enableLed'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'LED');
  var enable = Blockly.FtcJava.valueToCode(
      block, 'ENABLE', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.enable(' + enable + ');\n';
};

Blockly.Blocks['led_enableLed_Boolean'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createLedDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('enableLed'));
    this.appendValueInput('ENABLE').setCheck('Boolean')
        .appendField('enable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Enable the LED light.');
  }
};

Blockly.JavaScript['led_enableLed_Boolean'] =
    Blockly.JavaScript['led_enableLed'];

Blockly.FtcJava['led_enableLed_Boolean'] =
    Blockly.FtcJava['led_enableLed'];

Blockly.Blocks['led_isLightOn'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createLedDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isLightOn'));
    this.setColour(functionColor);
    this.setTooltip('Returns true if the light is on.');
  }
};

Blockly.JavaScript['led_isLightOn'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isLightOn()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['led_isLightOn'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'LED');
  var code = identifier + '.isLightOn()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
