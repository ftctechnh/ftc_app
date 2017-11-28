/**
 * @fileoverview FTC robot blocks related to analog input.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createAnalogInputDropdown
// The following are defined in vars.js:
// getPropertyColor

Blockly.Blocks['analogInput_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Voltage', 'Voltage'],
        ['MaxVoltage', 'MaxVoltage'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createAnalogInputDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Voltage', 'Returns the current voltage of this analog input.'],
        ['MaxVoltage', 'Returns the maximum voltage of this analog input.'],
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['analogInput_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['analogInput_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Voltage', 'Voltage'],
        ['MaxVoltage', 'MaxVoltage'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createAnalogInputDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Voltage', 'Returns the current voltage of this analog input.'],
        ['MaxVoltage', 'Returns the maximum voltage of this analog input.'],
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['analogInput_getProperty_Number'] =
    Blockly.JavaScript['analogInput_getProperty'];
