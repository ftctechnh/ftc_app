/**
 * @fileoverview FTC robot blocks related to ultrasonic sensor.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createUltrasonicSensorDropdown
// The following are defined in vars.js:
// getPropertyColor

Blockly.Blocks['ultrasonicSensor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['UltrasonicLevel', 'UltrasonicLevel'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createUltrasonicSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['UltrasonicLevel', 'Returns the ultrasonic level from this sensor.'],
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

Blockly.JavaScript['ultrasonicSensor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['ultrasonicSensor_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'UltrasonicSensor');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['ultrasonicSensor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['UltrasonicLevel', 'UltrasonicLevel'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createUltrasonicSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['UltrasonicLevel', 'Returns the ultrasonic level from this sensor.'],
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
        case 'UltrasonicLevel':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (ultrasonicSensor_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['ultrasonicSensor_getProperty_Number'] =
    Blockly.JavaScript['ultrasonicSensor_getProperty'];

Blockly.FtcJava['ultrasonicSensor_getProperty_Number'] =
    Blockly.FtcJava['ultrasonicSensor_getProperty'];
