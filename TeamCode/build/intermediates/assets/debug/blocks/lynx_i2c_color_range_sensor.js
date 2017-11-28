/**
 * @fileoverview FTC robot blocks related to LynxI2cColorRangeSensor.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createLynxI2cColorRangeSensorDropdown
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor
// setPropertyColor

Blockly.Blocks['lynxI2cColorRangeSensor_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createLynxI2cColorRangeSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'The 7 bit I2C address of the REV color/range sensor.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the REV color/range sensor.'],
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['lynxI2cColorRangeSensor_setProperty_Number'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['lynxI2cColorRangeSensor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Alpha', 'Alpha'],
        ['Argb', 'Arbg'],
        ['Blue', 'Blue'],
        ['Green', 'Green'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
        ['LightDetected', 'LightDetected'],
        ['RawLightDetected', 'RawLightDetected'],
        ['RawLightDetectedMax', 'RawLightDetectedMax'],
        ['Red', 'Red'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createLynxI2cColorRangeSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Alpha', 'The amount of light detected by the REV color/range sensor.'],
        ['Argb', 'The color detected by the REV color/range sensor, as an ARGB value.'],
        ['Blue', 'The amount of blue detected by the REV color/range sensor.'],
        ['Green', 'The amount of green detected by the REV color/range sensor.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address of the REV color/range sensor.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the REV color/range sensor.'],
        ['LightDetected', 'Gets the amount of light detected by the REV color/range sensor. ' +
            'The value is between 0.0 and 1.0.'],
        ['RawLightDetected', 'Returns a signal whose strength is proportional to the intensity ' +
            'of the light measured'],
        ['RawLightDetectedMax', 'Returns the maximum value that can be returned for ' +
            'RawLightDetected'],
        ['Red', 'The amount of red detected by the REV color/range sensor.'],
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

Blockly.JavaScript['lynxI2cColorRangeSensor_getProperty_Number'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['lynxI2cColorRangeSensor_getDistance_Number'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createLynxI2cColorRangeSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('getDistance'));
    this.appendValueInput('UNIT').setCheck('DistanceUnit')
        .appendField('unit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setTooltip('Returns the current distance in the indicated distance units.');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['lynxI2cColorRangeSensor_getDistance_Number'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var unit = Blockly.JavaScript.valueToCode(
      block, 'UNIT', Blockly.JavaScript.ORDER_NONE);
  var code = identifier + '.getDistance(' + unit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['lynxI2cColorRangeSensor_getNormalizedColors'] = {
  init: function() {
    this.setOutput(true, 'NormalizedColors');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createLynxI2cColorRangeSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('getNormalizedColors'));
    this.setTooltip('Returns the color detected by the REV color/range sensor.');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['lynxI2cColorRangeSensor_getNormalizedColors'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = 'JSON.parse(' + identifier + '.getNormalizedColors())';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
