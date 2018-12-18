/**
 * @fileoverview FTC robot blocks related to gyro sensor.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createGyroSensorDropdown
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor
// setPropertyColor

/*
 * Deprecated. See gyroSensor_setProperty_HeadingMode and gyroSensor_setProperty_Number
 */
Blockly.Blocks['gyroSensor_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['HeadingMode', 'HeadingMode'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['HeadingMode', 'Sets the heading mode. ' +
            'Valid values are HeadingMode_CARTESIAN or HeadingMode_CARDINAL. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress7Bit', 'Sets the 7 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress8Bit', 'Sets the 8 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
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

Blockly.JavaScript['gyroSensor_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['gyroSensor_setProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'GyroSensor');
  var property = block.getFieldValue('PROP');
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_NONE);
  var code;
  switch (property) {
    case 'HeadingMode':
      // This java code will throw ClassCastException if the GyroSensor is not a ModernRoboticsI2cGyro.
      Blockly.FtcJava.generateImport_('ModernRoboticsI2cGyro');
      code = '((ModernRoboticsI2cGyro) ' + identifier + ').setHeadingMode(' + value + ');\n';
      break;
    case 'I2cAddress7Bit':
      // This java code will throw ClassCastException if the GyroSensor is not an I2cAddrConfig.
      Blockly.FtcJava.generateImport_('I2cAddrConfig');
      Blockly.FtcJava.generateImport_('I2cAddr');
      code = '((I2cAddrConfig) ' + identifier + ').setI2cAddress(I2cAddr.create7bit(' + value + '));\n';
      break;
    case 'I2cAddress8Bit':
      // This java code will throw ClassCastException if the GyroSensor is not an I2cAddrConfig.
      Blockly.FtcJava.generateImport_('I2cAddrConfig');
      Blockly.FtcJava.generateImport_('I2cAddr');
      code = '((I2cAddrConfig) ' + identifier + ').setI2cAddress(I2cAddr.create8bit(' + value + '));\n';
      break;
    default:
      throw 'Unexpected property ' + property + ' (gyroSensor_setProperty).';
  }
  return code;
};

Blockly.Blocks['gyroSensor_setProperty_HeadingMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['HeadingMode', 'HeadingMode'],
    ];
    this.appendValueInput('VALUE').setCheck('ModernRoboticsI2cGyro.HeadingMode')
        .appendField('set')
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['HeadingMode', 'Sets the heading mode. ' +
            'Valid values are HeadingMode_CARTESIAN or HeadingMode_CARDINAL. ' +
            'Not all gyro sensors support this feature.'],
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

Blockly.JavaScript['gyroSensor_setProperty_HeadingMode'] =
    Blockly.JavaScript['gyroSensor_setProperty'];

Blockly.FtcJava['gyroSensor_setProperty_HeadingMode'] =
    Blockly.FtcJava['gyroSensor_setProperty'];

Blockly.Blocks['gyroSensor_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'Sets the 7 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress8Bit', 'Sets the 8 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
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
            throw 'Unexpected property ' + property + ' (gyroSensor_setProperty_Number getArgumentType).';
        }
      }
      return '';
    };
  }
};

Blockly.JavaScript['gyroSensor_setProperty_Number'] =
    Blockly.JavaScript['gyroSensor_setProperty'];

Blockly.FtcJava['gyroSensor_setProperty_Number'] =
    Blockly.FtcJava['gyroSensor_setProperty'];

/*
 * Deprecated. See gyroSensor_getProperty_HeadingMode and gyroSensor_getProperty_Number
 */
Blockly.Blocks['gyroSensor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Heading', 'Heading'],
        ['HeadingMode', 'HeadingMode'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
        ['RawX', 'RawX'],
        ['RawY', 'RawY'],
        ['RawZ', 'RawZ'],
        ['RotationFraction', 'RotationFraction'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Heading', 'Returns a numeric value between 0 and 360 representing the integrated Z ' +
            'axis as a cartesian or cardinal heading. ' +
            'Not all gyro sensors support this feature.'],
        ['HeadingMode', 'Returns the heading mode: HEADING_CARTESIAN or HEADING_CARDINAL. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress7Bit', 'Returns the 7 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress8Bit', 'Returns the 8 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
        ['RawX', 'Returns the gyro sensor\'s raw X value. ' +
            'Not all gyro sensors support this feature.'],
        ['RawY', 'Returns the gyro sensor\'s raw Y value. ' +
            'Not all gyro sensors support this feature.'],
        ['RawZ', 'Returns the gyro sensor\'s raw Z value. ' +
            'Not all gyro sensors support this feature.'],
        ['RotationFraction', 'Returns a numeric value between 0.0 and 1.0 representing the ' +
            'current fractional rotation of the gyro sensor. ' +
            'Not all gyro sensors support this feature'],
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

Blockly.JavaScript['gyroSensor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['gyroSensor_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'GyroSensor');
  var property = block.getFieldValue('PROP');
  var code;
  switch (property) {
    case 'Heading':
    case 'RotationFraction':
      code = identifier + '.get' + property + '()';
      break;
    case 'HeadingMode':
    case 'IntegratedZValue':
      // This java code will throw ClassCastException if the GyroSensor is not a ModernRoboticsI2cGyro.
      Blockly.FtcJava.generateImport_('ModernRoboticsI2cGyro');
      code = '((ModernRoboticsI2cGyro) ' + identifier + ').get' + property + '()';
      break;
    case 'I2cAddress7Bit':
      // This java code will throw ClassCastException if the GyroSensor is not an I2cAddressableDevice.
      Blockly.FtcJava.generateImport_('I2cAddressableDevice');
      code = '((I2cAddressableDevice) ' + identifier + ').getI2cAddress().get7Bit()';
      break;
    case 'I2cAddress8Bit':
      // This java code will throw ClassCastException if the GyroSensor is not an I2cAddressableDevice.
      Blockly.FtcJava.generateImport_('I2cAddressableDevice');
      code = '((I2cAddressableDevice) ' + identifier + ').getI2cAddress().get8Bit()';
      break;
    case 'RawX':
    case 'RawY':
    case 'RawZ':
      code = identifier + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property) + '()';
      break;
    default:
      throw 'Unexpected property ' + property + ' (gyroSensor_getProperty).';
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['gyroSensor_getProperty_HeadingMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['HeadingMode', 'HeadingMode'],
    ];
    this.setOutput(true, 'ModernRoboticsI2cGyro.HeadingMode');
    this.appendDummyInput()
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['HeadingMode', 'Returns the heading mode: HEADING_CARTESIAN or HEADING_CARDINAL. ' +
            'Not all gyro sensors support this feature.'],
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

Blockly.JavaScript['gyroSensor_getProperty_HeadingMode'] =
    Blockly.JavaScript['gyroSensor_getProperty'];

Blockly.FtcJava['gyroSensor_getProperty_HeadingMode'] =
    Blockly.FtcJava['gyroSensor_getProperty'];

Blockly.Blocks['gyroSensor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Heading', 'Heading'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
        ['IntegratedZValue', 'IntegratedZValue'],
        ['RawX', 'RawX'],
        ['RawY', 'RawY'],
        ['RawZ', 'RawZ'],
        ['RotationFraction', 'RotationFraction'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Heading', 'Returns a numeric value between 0 and 360 representing the integrated Z ' +
            'axis as a cartesian or cardinal heading. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress7Bit', 'Returns the 7 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress8Bit', 'Returns the 8 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
        ['IntegratedZValue', 'Returns the gyro sensor\'s integrated Z value. ' +
            'Not all gyro sensors support this feature.'],
        ['RawX', 'Returns the gyro sensor\'s raw X value. ' +
            'Not all gyro sensors support this feature.'],
        ['RawY', 'Returns the gyro sensor\'s raw Y value. ' +
            'Not all gyro sensors support this feature.'],
        ['RawZ', 'Returns the gyro sensor\'s raw Z value. ' +
            'Not all gyro sensors support this feature.'],
        ['RotationFraction', 'Returns a numeric value between 0.0 and 1.0 representing the ' +
            'current fractional rotation of the gyro sensor. ' +
            'Not all gyro sensors support this feature'],
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
        case 'Heading':
        case 'I2cAddress7Bit':
        case 'I2cAddress8Bit':
        case 'IntegratedZValue':
        case 'RawX':
        case 'RawY':
        case 'RawZ':
          return 'int';
        case 'RotationFraction':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (gyroSensor_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['gyroSensor_getProperty_Number'] =
    Blockly.JavaScript['gyroSensor_getProperty'];

Blockly.FtcJava['gyroSensor_getProperty_Number'] =
    Blockly.FtcJava['gyroSensor_getProperty'];

Blockly.Blocks['gyroSensor_getProperty_Array'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngularOrientationAxes', 'AngularOrientationAxes'],
        ['AngularVelocityAxes', 'AngularVelocityAxes'],
    ];
    this.setOutput(true, 'Array');
    this.appendDummyInput()
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngularOrientationAxes', 'Returns a List of the axes on which the gyroscope measures ' +
            'angular orientation. Not all gyro sensors support this feature.'],
        ['AngularVelocityAxes', 'Returns a List of the axes on which the gyroscope measures ' +
            'angular velocity. Some gyroscopes measure angular velocity on all three axes ' +
            '(X, Y, & Z) while others measure on only a subset, typically the Z axis. This block ' +
            'allows you to determine what information is usefully returned through the ' +
            '\'get AngularVelocity\' block.'],
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

Blockly.JavaScript['gyroSensor_getProperty_Array'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = 'JSON.parse(' + identifier + '.get' + property + '())';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['gyroSensor_getProperty_Array'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'GyroSensor');
  var property = block.getFieldValue('PROP');
  var code;
  switch (property) {
    case 'AngularOrientationAxes':
      // This java code will throw ClassCastException if the GyroSensor is not an OrientationSensor.
      Blockly.FtcJava.generateImport_('ArrayList');
      Blockly.FtcJava.generateImport_('Axis');
      Blockly.FtcJava.generateImport_('OrientationSensor');
      code = 'new ArrayList<Axis>(((OrientationSensor) ' + identifier + ').get' + property + '())';
      break;
    case 'AngularVelocityAxes':
      // This java code will throw ClassCastException if the GyroSensor is not a Gyroscope.
      Blockly.FtcJava.generateImport_('ArrayList');
      Blockly.FtcJava.generateImport_('Axis');
      Blockly.FtcJava.generateImport_('Gyroscope');
      code = 'new ArrayList<Axis>(((Gyroscope) ' + identifier + ').get' + property + '())';
      break;
    default:
      throw 'Unexpected property ' + property + ' (gyroSensor_getProperty_array).';
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

// Enums

/*
 * Deprecated. See gyroSensor_typedEnum_headingMode.
 */
Blockly.Blocks['gyroSensor_enum_headingMode'] = {
  init: function() {
    var HEADING_MODE_CHOICES = [
        ['HEADING_CARTESIAN', 'HEADING_CARTESIAN'],
        ['HEADING_CARDINAL', 'HEADING_CARDINAL'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('HeadingMode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(HEADING_MODE_CHOICES), 'HEADING_MODE');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['HEADING_CARTESIAN', 'The HeadingMode value HEADING_CARTESIAN.'],
        ['HEADING_CARDINAL', 'The HeadingMode value HEADING_CARDINAL.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('HEADING_MODE');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['gyroSensor_enum_headingMode'] = function(block) {
  var code = '"' + block.getFieldValue('HEADING_MODE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['gyroSensor_enum_headingMode'] = function(block) {
  var code = 'ModernRoboticsI2cGyro.HeadingMode.' + block.getFieldValue('HEADING_MODE');
  Blockly.FtcJava.generateImport_('ModernRoboticsI2cGyro');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['gyroSensor_typedEnum_headingMode'] = {
  init: function() {
    var HEADING_MODE_CHOICES = [
        ['HEADING_CARTESIAN', 'HEADING_CARTESIAN'],
        ['HEADING_CARDINAL', 'HEADING_CARDINAL'],
    ];
    this.setOutput(true, 'ModernRoboticsI2cGyro.HeadingMode');
    this.appendDummyInput()
        .appendField(createNonEditableField('HeadingMode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(HEADING_MODE_CHOICES), 'HEADING_MODE');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['HEADING_CARTESIAN', 'The HeadingMode value HEADING_CARTESIAN.'],
        ['HEADING_CARDINAL', 'The HeadingMode value HEADING_CARDINAL.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('HEADING_MODE');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['gyroSensor_typedEnum_headingMode'] =
    Blockly.JavaScript['gyroSensor_enum_headingMode'];

Blockly.FtcJava['gyroSensor_typedEnum_headingMode'] =
    Blockly.FtcJava['gyroSensor_enum_headingMode'];

// Functions

Blockly.Blocks['gyroSensor_calibrate'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('calibrate'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Calibrate the gyro. ' +
      'Not all gyro sensors support this feature. ' +
      'For the Modern Robotics device this will reset the Z axis heading.');
  }
};

Blockly.JavaScript['gyroSensor_calibrate'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  return identifier + '.calibrate();\n';
};

Blockly.FtcJava['gyroSensor_calibrate'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'GyroSensor');
  return identifier + '.calibrate();\n';
};

Blockly.Blocks['gyroSensor_isCalibrating'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isCalibrating'));
    this.setColour(functionColor);
    this.setTooltip('Returns true if the gyro is performing a calibration operation. ' +
        'Not all gyro sensors support this feature.');
  }
};

Blockly.JavaScript['gyroSensor_isCalibrating'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isCalibrating()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['gyroSensor_isCalibrating'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'GyroSensor');
  var code = identifier + '.isCalibrating()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['gyroSensor_resetZAxisIntegrator'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('resetZAxisIntegrator'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Set the integrated Z axis to zero. ' +
        'Not all gyro sensors support this feature.');
  }
};

Blockly.JavaScript['gyroSensor_resetZAxisIntegrator'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  return identifier + '.resetZAxisIntegrator();\n';
};

Blockly.FtcJava['gyroSensor_resetZAxisIntegrator'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'GyroSensor');
  return identifier + '.resetZAxisIntegrator();\n';
};

Blockly.Blocks['gyroSensor_getAngularVelocity'] = {
  init: function() {
    this.setOutput(true, 'AngularVelocity');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('getAngularVelocity'));
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns an AngularVelocity object representing the angular rotation rate across all the axes measured by the gyro. ' +
        'Axes on which angular velocity is not measured are reported as zero.');
  }
};

Blockly.JavaScript['gyroSensor_getAngularVelocity'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_NONE);
  var code = identifier + '.getAngularVelocity(' + angleUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['gyroSensor_getAngularVelocity'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'GyroSensor');
  var angleUnit = Blockly.FtcJava.valueToCode(
      block, 'ANGLE_UNIT', Blockly.FtcJava.ORDER_NONE);
  // This java code will throw ClassCastException if the GyroSensor is not a Gyroscope.
  Blockly.FtcJava.generateImport_('Gyroscope');
  var code = '((Gyroscope) ' + identifier + ').getAngularVelocity(' + angleUnit + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['gyroSensor_getAngularOrientation'] = {
  init: function() {
    this.setOutput(true, 'Orientation');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('getAngularOrientation'));
    this.appendValueInput('AXES_REFERENCE').setCheck('AxesReference')
        .appendField('axesReference')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('AXES_ORDER').setCheck('AxesOrder')
        .appendField('axesOrder')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns an Orienation object representing the absolute orientation of the sensor as a set three angles. ' +
        'Axes on which absolute orientation is not measured are reported as zero.');
  }
};

Blockly.JavaScript['gyroSensor_getAngularOrientation'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var axesReference = Blockly.JavaScript.valueToCode(
      block, 'AXES_REFERENCE', Blockly.JavaScript.ORDER_COMMA);
  var axesOrder = Blockly.JavaScript.valueToCode(
      block, 'AXES_ORDER', Blockly.JavaScript.ORDER_COMMA);
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var code = identifier + '.getAngularOrientation(' + axesReference + ', ' + axesOrder + ', ' + angleUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['gyroSensor_getAngularOrientation'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'GyroSensor');
  var axesReference = Blockly.FtcJava.valueToCode(
      block, 'AXES_REFERENCE', Blockly.FtcJava.ORDER_COMMA);
  var axesOrder = Blockly.FtcJava.valueToCode(
      block, 'AXES_ORDER', Blockly.FtcJava.ORDER_COMMA);
  var angleUnit = Blockly.FtcJava.valueToCode(
      block, 'ANGLE_UNIT', Blockly.FtcJava.ORDER_COMMA);
  // This java code will throw ClassCastException if the GyroSensor is not a OrientationSensor.
  Blockly.FtcJava.generateImport_('OrientationSensor');
  var code = '((OrientationSensor) ' + identifier + ').getAngularOrientation(' + axesReference + ', ' + axesOrder + ', ' + angleUnit + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
