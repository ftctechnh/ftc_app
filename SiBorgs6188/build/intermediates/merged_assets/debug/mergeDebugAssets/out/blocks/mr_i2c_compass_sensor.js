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
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'Sets the 7 bit I2C address of the compass sensor.'],
        ['I2cAddress8Bit', 'Sets the 8 bit I2C address of the compass sensor.'],
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

Blockly.JavaScript['mrI2cCompassSensor_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['mrI2cCompassSensor_setProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'CompassSensor');
  var property = block.getFieldValue('PROP');
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_NONE);
  var code;
  switch (property) {
    case 'I2cAddress7Bit':
      Blockly.FtcJava.generateImport_('I2cAddrConfig');
      Blockly.FtcJava.generateImport_('I2cAddr');
      code = '((I2cAddrConfig) ' + identifier + ').setI2cAddress(I2cAddr.create7bit(' + value + '));\n';
      break;
    case 'I2cAddress8Bit':
      Blockly.FtcJava.generateImport_('I2cAddrConfig');
      Blockly.FtcJava.generateImport_('I2cAddr');
      code = '((I2cAddrConfig) ' + identifier + ').setI2cAddress(I2cAddr.create8bit(' + value + '));\n';
      break;
    default:
      throw 'Unexpected property ' + property + ' (mrI2cCompassSensor_setProperty).';
  }
  return code;
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
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'Sets the 7 bit I2C address of the compass sensor.'],
        ['I2cAddress8Bit', 'Sets the 8 bit I2C address of the compass sensor.'],
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
    this.getFtcJavaInputType = function(inputName) {
      if (inputName == 'VALUE') {
        var property = thisBlock.getFieldValue('PROP');
        switch (property) {
          case 'I2cAddress7Bit':
          case 'I2cAddress8Bit':
            return 'int';
          default:
            throw 'Unexpected property ' + property + ' (mrI2cCompassSensor_setProperty_Number getArgumentType).';
        }
      }
      return '';
    };
  }
};

Blockly.JavaScript['mrI2cCompassSensor_setProperty_Number'] =
    Blockly.JavaScript['mrI2cCompassSensor_setProperty'];

Blockly.FtcJava['mrI2cCompassSensor_setProperty_Number'] =
    Blockly.FtcJava['mrI2cCompassSensor_setProperty'];

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
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Returns the current direction, in degrees.'],
        ['I2cAddress7Bit', 'Returns the 7 bit I2C address of the compass sensor.'],
        ['I2cAddress8Bit', 'Returns the 8 bit I2C address of the compass sensor.'],
        ['XAccel', 'Returns the X Acceleration, in G\'s.'],
        ['YAccel', 'Returns the Y Acceleration, in G\'s.'],
        ['ZAccel', 'Returns the Z Acceleration, in G\'s.'],
        ['XMagneticFlux', 'Returns the amount of magnetic flux in the X direction, in tesla.'],
        ['YMagneticFlux', 'Returns the amount of magnetic flux in the Y direction, in tesla.'],
        ['ZMagneticFlux', 'Returns the amount of magnetic flux in the Z direction, in tesla.'],
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

Blockly.JavaScript['mrI2cCompassSensor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['mrI2cCompassSensor_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'CompassSensor');
  var property = block.getFieldValue('PROP');
  var code;
  var order = Blockly.FtcJava.ORDER_FUNCTION_CALL;
  switch (property) {
    case 'Direction':
      code = identifier + '.get' + property + '()';
      break;
    case 'XAccel':
    case 'YAccel':
    case 'ZAccel':
      Blockly.FtcJava.generateImport_('ModernRoboticsI2cCompassSensor');
      code = '((ModernRoboticsI2cCompassSensor) ' + identifier + ').getAcceleration().' +
          Blockly.FtcJava.makeFirstLetterLowerCase_(property);
      order = Blockly.FtcJava.ORDER_MEMBER;
      break;
    case 'XMagneticFlux':
    case 'YMagneticFlux':
    case 'ZMagneticFlux':
      Blockly.FtcJava.generateImport_('ModernRoboticsI2cCompassSensor');
      code = '((ModernRoboticsI2cCompassSensor) ' + identifier + ').getMagneticFlux().' +
          property[0].toLowerCase();
      break;
    case 'I2cAddress7Bit':
      Blockly.FtcJava.generateImport_('I2cAddrConfig');
      code = '((I2cAddrConfig) ' + identifier + ').getI2cAddress().get7Bit()';
      break;
    case 'I2cAddress8Bit':
      Blockly.FtcJava.generateImport_('I2cAddrConfig');
      code = '((I2cAddrConfig) ' + identifier + ').getI2cAddress().get8Bit()';
      break;
    default:
      throw 'Unexpected property ' + property + ' (mrI2cCompassSensor_getProperty).';
  }
  return [code, order];
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
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Returns the current direction, in degrees.'],
        ['I2cAddress7Bit', 'Returns the 7 bit I2C address of the compass sensor.'],
        ['I2cAddress8Bit', 'Returns the 8 bit I2C address of the compass sensor.'],
        ['XAccel', 'Returns the X Acceleration, in G\'s.'],
        ['YAccel', 'Returns the Y Acceleration, in G\'s.'],
        ['ZAccel', 'Returns the Z Acceleration, in G\'s.'],
        ['XMagneticFlux', 'Returns the amount of magnetic flux in the X direction, in tesla.'],
        ['YMagneticFlux', 'Returns the amount of magnetic flux in the Y direction, in tesla.'],
        ['ZMagneticFlux', 'Returns the amount of magnetic flux in the Z direction, in tesla.'],
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
        case 'I2cAddress7Bit':
        case 'I2cAddress8Bit':
          return 'int';
        case 'Direction':
        case 'XAccel':
        case 'YAccel':
        case 'ZAccel':
        case 'XMagneticFlux':
        case 'YMagneticFlux':
        case 'ZMagneticFlux':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (mrI2cCompassSensor_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['mrI2cCompassSensor_getProperty_Number'] =
    Blockly.JavaScript['mrI2cCompassSensor_getProperty'];

Blockly.FtcJava['mrI2cCompassSensor_getProperty_Number'] =
    Blockly.FtcJava['mrI2cCompassSensor_getProperty'];

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
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MEASUREMENT_MODE', 'The CompassMode value MEASUREMENT_MODE.'],
        ['CALIBRATION_MODE', 'The CompassMode value CALIBRATION_MODE.'],
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
  }
};

Blockly.JavaScript['mrI2cCompassSensor_enum_compassMode'] = function(block) {
  var code = '"' + block.getFieldValue('COMPASS_MODE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['mrI2cCompassSensor_enum_compassMode'] = function(block) {
  var code = 'CompassSensor.CompassMode.' + block.getFieldValue('COMPASS_MODE');
  Blockly.FtcJava.generateImport_('CompassSensor');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['mrI2cCompassSensor_typedEnum_compassMode'] = {
  init: function() {
    var COMPASS_MODE_CHOICES = [
        ['MEASUREMENT_MODE', 'MEASUREMENT_MODE'],
        ['CALIBRATION_MODE', 'CALIBRATION_MODE'],
    ];
    this.setOutput(true, 'CompassSensor.CompassMode');
    this.appendDummyInput()
        .appendField(createNonEditableField('CompassMode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(COMPASS_MODE_CHOICES), 'COMPASS_MODE');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MEASUREMENT_MODE', 'The CompassMode value MEASUREMENT_MODE.'],
        ['CALIBRATION_MODE', 'The CompassMode value CALIBRATION_MODE.'],
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
  }
};

Blockly.JavaScript['mrI2cCompassSensor_typedEnum_compassMode'] =
    Blockly.JavaScript['mrI2cCompassSensor_enum_compassMode'];

Blockly.FtcJava['mrI2cCompassSensor_typedEnum_compassMode'] =
    Blockly.FtcJava['mrI2cCompassSensor_enum_compassMode'];

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
    this.setTooltip('Sets the CompassMode. Valid values are MEASUREMENT_MODE or CALIBRATION_MODE.');
  }
};

Blockly.JavaScript['mrI2cCompassSensor_setMode'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var compassMode = Blockly.JavaScript.valueToCode(
      block, 'COMPASS_MODE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.setMode(' + compassMode + ');\n';
};

Blockly.FtcJava['mrI2cCompassSensor_setMode'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'CompassSensor');
  var compassMode = Blockly.FtcJava.valueToCode(
      block, 'COMPASS_MODE', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.setMode(' + compassMode + ');\n';
};

Blockly.Blocks['mrI2cCompassSensor_setMode_CompassMode'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createMrI2cCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('setMode'));
    this.appendValueInput('COMPASS_MODE').setCheck('CompassSensor.CompassMode')
        .appendField('compassMode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the CompassMode. Valid values are MEASUREMENT_MODE or CALIBRATION_MODE.');
  }
};

Blockly.JavaScript['mrI2cCompassSensor_setMode_CompassMode'] =
    Blockly.JavaScript['mrI2cCompassSensor_setMode'];

Blockly.FtcJava['mrI2cCompassSensor_setMode_CompassMode'] =
    Blockly.FtcJava['mrI2cCompassSensor_setMode'];


Blockly.Blocks['mrI2cCompassSensor_isCalibrating'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createMrI2cCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isCalibrating'));
    this.setColour(functionColor);
    this.setTooltip('Returns true if the compass sensor is performing a calibration operation.');
  }
};

Blockly.JavaScript['mrI2cCompassSensor_isCalibrating'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isCalibrating()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['mrI2cCompassSensor_isCalibrating'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'CompassSensor');
  Blockly.FtcJava.generateImport_('ModernRoboticsI2cCompassSensor');
  var code = '((ModernRoboticsI2cCompassSensor) ' + identifier + ').isCalibrating()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['mrI2cCompassSensor_calibrationFailed'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createMrI2cCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('calibrationFailed'));
    this.setColour(functionColor);
    this.setTooltip('Returns true if calibration failed.');
  }
};

Blockly.JavaScript['mrI2cCompassSensor_calibrationFailed'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.calibrationFailed()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['mrI2cCompassSensor_calibrationFailed'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'CompassSensor');
  var code = identifier + '.calibrationFailed()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
