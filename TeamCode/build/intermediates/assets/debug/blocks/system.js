/**
 * @fileoverview FTC robot blocks related to System.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// systemIdentifier
// The following are defined in vars.js:
// functionColor

Blockly.Blocks['system_nanoTime'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('System'))
        .appendField('.')
        .appendField(createNonEditableField('nanoTime'));
    this.setColour(functionColor);
    this.setTooltip('Returns the current value of the running Java Virtual Machine\'s ' +
        'high-resolution time source, in nanoseconds.');
  }
};

Blockly.JavaScript['system_nanoTime'] = function(block) {
  var code = systemIdentifier + '.nanoTime()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
