/**
 * @fileoverview FTC robot blocks related to light sensor.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createLightSensorDropdown
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor

Blockly.Blocks['lightSensor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['LightDetected', 'LightDetected'],
        ['RawLightDetected', 'RawLightDetected'],
        ['RawLightDetectedMax', 'RawLightDetectedMax'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createLightSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['LightDetected', 'Returns a numeric value between 0.0 and 1.0 representing the amount ' +
            'of light detected by the light sensor.'],
        ['RawLightDetected', 'Returns a signal whose strength is proportional to the intensity ' +
            'of the light measured.'],
        ['RawLightDetectedMax', 'Returns the maximum value that can be returned for ' +
            'RawLightDetected.'],
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

Blockly.JavaScript['lightSensor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['lightSensor_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'LightSensor');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['lightSensor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['LightDetected', 'LightDetected'],
        ['RawLightDetected', 'RawLightDetected'],
        ['RawLightDetectedMax', 'RawLightDetectedMax'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createLightSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['LightDetected', 'Returns a numeric value between 0.0 and 1.0 representing the amount ' +
            'of light detected by the light sensor.'],
        ['RawLightDetected', 'Returns a signal whose strength is proportional to the intensity ' +
            'of the light measured.'],
        ['RawLightDetectedMax', 'Returns the maximum value that can be returned for ' +
            'RawLightDetected.'],
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
        case 'LightDetected':
        case 'RawLightDetected':
        case 'RawLightDetectedMax':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (lightSensor_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['lightSensor_getProperty_Number'] =
    Blockly.JavaScript['lightSensor_getProperty'];

Blockly.FtcJava['lightSensor_getProperty_Number'] =
    Blockly.FtcJava['lightSensor_getProperty'];


// Functions

Blockly.Blocks['lightSensor_enableLed'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createLightSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('enableLed'));
    this.appendValueInput('ENABLE') // no type, for compatibility
        .appendField('enable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Enable the LED light. Not all light sensors support this feature.');
  }
};

Blockly.JavaScript['lightSensor_enableLed'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var enable = Blockly.JavaScript.valueToCode(
      block, 'ENABLE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.enableLed(' + enable + ');\n';
};

Blockly.FtcJava['lightSensor_enableLed'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'LightSensor');
  var enable = Blockly.FtcJava.valueToCode(
      block, 'ENABLE', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.enableLed(' + enable + ');\n';
};

Blockly.Blocks['lightSensor_enableLed_Boolean'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createLightSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('enableLed'));
    this.appendValueInput('ENABLE').setCheck('Boolean')
        .appendField('enable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Enable the LED light. Not all light sensors support this feature.');
  }
};

Blockly.JavaScript['lightSensor_enableLed_Boolean'] =
    Blockly.JavaScript['lightSensor_enableLed'];

Blockly.FtcJava['lightSensor_enableLed_Boolean'] =
    Blockly.FtcJava['lightSensor_enableLed'];
