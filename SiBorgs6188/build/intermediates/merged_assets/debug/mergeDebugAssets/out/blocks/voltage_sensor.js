/**
 * @fileoverview FTC robot blocks related to voltage sensor.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createVoltageSensorDropdown
// The following are defined in vars.js:
// getPropertyColor

Blockly.Blocks['voltageSensor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Voltage', 'Voltage'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createVoltageSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Voltage', 'Returns the voltage.'],
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

Blockly.JavaScript['voltageSensor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['voltageSensor_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'VoltageSensor');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['voltageSensor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Voltage', 'Voltage'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createVoltageSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Voltage', 'Returns the voltage.'],
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
    this.getFtcJavaOutputType = function() {
      var property = thisBlock.getFieldValue('PROP');
      switch (property) {
        case 'voltage':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (voltageSensor_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['voltageSensor_getProperty_Number'] =
    Blockly.JavaScript['voltageSensor_getProperty'];

Blockly.FtcJava['voltageSensor_getProperty_Number'] =
    Blockly.FtcJava['voltageSensor_getProperty'];
