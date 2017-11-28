/**
 * @fileoverview FTC robot blocks related to ModernRoboticsI2cCompassSensor.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createMrI2cCompassSensorDropdown
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// setPropertyColor
// functionColor

Blockly.Blocks['mrI2cCompassSensor_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createMrI2cCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'The 7 bit I2C address of the compass sensor.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the compass sensor.'],
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

Blockly.JavaScript['mrI2cCompassSensor_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['mrI2cCompassSensor_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createMrI2cCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'The 7 bit I2C address of the compass sensor.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the compass sensor.'],
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

Blockly.JavaScript['mrI2cCompassSensor_setProperty_Number'] =
    Blockly.JavaScript['mrI2cCompassSensor_setProperty'];

Blockly.Blocks['mrI2cCompassSensor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
        ['XAccel', 'XAccel'],
        ['YAccel', 'YAccel'],
        ['ZAccel', 'ZAccel'],
        ['XMagneticFlux', 'XMagneticFlux'],
        ['YMagneticFlux', 'YMagneticFlux'],
        ['ZMagneticFlux', 'ZMagneticFlux'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createMrI2cCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Get the current direction, in degrees.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address of the compass sensor.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the compass sensor.'],
        ['XAccel', 'Returns the X Acceleration, in G\'s.'],
        ['YAccel', 'Returns the Y Acceleration, in G\'s.'],
        ['ZAccel', 'Returns the Z Acceleration, in G\'s.'],
        ['XMagneticFlux', 'The magnetic flux in the X direction, in tesla.'],
        ['YMagneticFlux', 'The magnetic flux in the Y direction, in tesla.'],
        ['ZMagneticFlux', 'The magnetic flux in the Z direction, in tesla.'],
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

Blockly.JavaScript['mrI2cCompassSensor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['mrI2cCompassSensor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
        ['XAccel', 'XAccel'],
        ['YAccel', 'YAccel'],
        ['ZAccel', 'ZAccel'],
        ['XMagneticFlux', 'XMagneticFlux'],
        ['YMagneticFlux', 'YMagneticFlux'],
        ['ZMagneticFlux', 'ZMagneticFlux'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createMrI2cCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Get the current direction, in degrees.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address of the compass sensor.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the compass sensor.'],
        ['XAccel', 'Returns the X Acceleration, in G\'s.'],
        ['YAccel', 'Returns the Y Acceleration, in G\'s.'],
        ['ZAccel', 'Returns the Z Acceleration, in G\'s.'],
        ['XMagneticFlux', 'The magnetic flux in the X direction, in tesla.'],
        ['YMagneticFlux', 'The magnetic flux in the Y direction, in tesla.'],
        ['ZMagneticFlux', 'The magnetic flux in the Z direction, in tesla.'],
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

Blockly.JavaScript['mrI2cCompassSensor_getProperty_Number'] =
    Blockly.JavaScript['mrI2cCompassSensor_getProperty']

// Enums

Blockly.Blocks['mrI2cCompassSensor_enum_compassMode'] = {
  init: function() {
    var COMPASS_MODE_CHOICES = [
        ['MEASUREMENT_MODE', 'MEASUREMENT_MODE'],
        ['CALIBRATION_MODE', 'CALIBRATION_MODE'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('CompassMode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(COMPASS_MODE_CHOICES), 'COMPASS_MODE');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MEASUREMENT_MODE', 'The compass mode for measurement.'],
        ['CALIBRATION_MODE', 'The compass mode for calibration.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('COMPASS_MODE');
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

Blockly.JavaScript['mrI2cCompassSensor_enum_compassMode'] = function(block) {
  var code = '"' + block.getFieldValue('COMPASS_MODE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['mrI2cCompassSensor_typedEnum_compassMode'] = {
  init: function() {
    var COMPASS_MODE_CHOICES = [
        ['MEASUREMENT_MODE', 'MEASUREMENT_MODE'],
        ['CALIBRATION_MODE', 'CALIBRATION_MODE'],
    ];
    this.setOutput(true, 'CompassMode');
    this.appendDummyInput()
        .appendField(createNonEditableField('CompassMode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(COMPASS_MODE_CHOICES), 'COMPASS_MODE');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MEASUREMENT_MODE', 'The compass mode for measurement.'],
        ['CALIBRATION_MODE', 'The compass mode for calibration.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('COMPASS_MODE');
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

Blockly.JavaScript['mrI2cCompassSensor_typedEnum_compassMode'] =
    Blockly.JavaScript['mrI2cCompassSensor_enum_compassMode'];

// Functions

Blockly.Blocks['mrI2cCompassSensor_setMode'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createMrI2cCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('setMode'));
    this.appendValueInput('COMPASS_MODE') // no type, for compatibility
        .appendField('compassMode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Change to calibration or measurement mode.');
  }
};

Blockly.JavaScript['mrI2cCompassSensor_setMode'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var compassMode = Blockly.JavaScript.valueToCode(
      block, 'COMPASS_MODE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.setMode(' + compassMode + ');\n';
};

Blockly.Blocks['mrI2cCompassSensor_setMode_CompassMode'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createMrI2cCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('setMode'));
    this.appendValueInput('COMPASS_MODE').setCheck('CompassMode')
        .appendField('compassMode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Change to calibration or measurement mode.');
  }
};

Blockly.JavaScript['mrI2cCompassSensor_setMode_CompassMode'] =
    Blockly.JavaScript['mrI2cCompassSensor_setMode']


Blockly.Blocks['mrI2cCompassSensor_isCalibrating'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createMrI2cCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isCalibrating'));
    this.setTooltip('Is the compass sensor performing a calibration operation?');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['mrI2cCompassSensor_isCalibrating'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isCalibrating()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['mrI2cCompassSensor_calibrationFailed'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createMrI2cCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('calibrationFailed'));
    this.setTooltip('Did the calibration operation fail?');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['mrI2cCompassSensor_calibrationFailed'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.calibrationFailed()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
