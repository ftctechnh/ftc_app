/**
 * @fileoverview FTC robot blocks related to DbgLog.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// dbgLogIdentifier
// The following are defined in vars.js:
// functionColor

// Functions

Blockly.Blocks['dbgLog_msg'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('DbgLog'))
        .appendField('.')
        .appendField(createNonEditableField('msg'));
    this.appendValueInput('MESSAGE').setCheck('String')
        .appendField('message')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Log a debug message.');
  }
};

Blockly.JavaScript['dbgLog_msg'] = function(block) {
  var message = Blockly.JavaScript.valueToCode(
      block, 'MESSAGE', Blockly.JavaScript.ORDER_COMMA);
  return dbgLogIdentifier + '.msg(' + message + ');\n';
};

Blockly.Blocks['dbgLog_error'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('DbgLog'))
        .appendField('.')
        .appendField(createNonEditableField('error'));
    this.appendValueInput('MESSAGE').setCheck('String')
        .appendField('message')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Log a debug message.');
  }
};

Blockly.JavaScript['dbgLog_error'] = function(block) {
  var message = Blockly.JavaScript.valueToCode(
      block, 'MESSAGE', Blockly.JavaScript.ORDER_COMMA);
  return dbgLogIdentifier + '.error(' + message + ');\n';
};
