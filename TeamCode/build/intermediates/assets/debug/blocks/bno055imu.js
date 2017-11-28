/**
 * @fileoverview FTC robot blocks related to BNO055IMU.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createBNO055IMUDropdown
// The following are defined in vars.js:
// getPropertyColor
// functionColor

Blockly.JavaScript['bno055imu_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['bno055imu_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'The 7 bit I2C address of the sensor.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the gyro sensor.'],
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

Blockly.JavaScript['bno055imu_setProperty_Number'] =
    Blockly.JavaScript['bno055imu_setProperty'];

Blockly.Blocks['bno055imu_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Acceleration', 'Acceleration'],
        ['AngularOrientation', 'AngularOrientation'],
        ['AngularVelocity', 'AngularVelocity'],
        ['CalibrationStatus', 'CalibrationStatus'],
        ['Gravity', 'Gravity'],
        ['LinearAcceleration', 'LinearAcceleration'],
        ['MagneticFieldStrength', 'MagneticFieldStrength'],
        ['OverallAcceleration', 'OverallAcceleration'],
        ['Parameters', 'Parameters'],
        ['Position', 'Position'],
        ['QuaternionOrientation', 'QuaternionOrientation'],
        ['SystemError', 'SystemError'],
        ['SystemStatus', 'SystemStatus'],
        ['Temperature', 'Temperature'],
        ['Velocity', 'Velocity'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Acceleration',
            'Returns an Acceleration object representing the last observed acceleration of the ' +
            'sensor. Note that this does not communicate with the sensor, but rather returns the ' +
            'most recent value reported to the acceleration integration algorithm.'],
        ['AngularOrientation',
            'Returns an Orientation object representing the absolute orientation of the sensor ' +
            'as a set three angles.'],
        ['AngularVelocity',
            'Returns an AngularVelocity object representing the rate of change of the absolute ' +
            'orientation of the sensor.'],
        ['CalibrationStatus', 'Returns the calibration status of the IMU.'],
        ['Gravity',
            'Returns an Acceleration object representing the direction of the force of gravity ' +
            'relative to the sensor.'],
        ['LinearAcceleration',
            'Returns an Acceleration object representing the acceleration experienced by the ' +
            'sensor due to the movement of the sensor.'],
        ['MagneticFieldStrength',
            'Returns a MagneticFlux object representing the magnetic field strength experienced ' +
            'by the sensor.'],
        ['OverallAcceleration',
            'Returns an Acceleration object representing the overall acceleration experienced by ' +
            'the sensor. This is composed of a component due to the movement of the sensor and a ' +
            'component due to the force of gravity.'],
        ['Parameters',
            'Returns the parameters with which initialization was last attempted.'],
        ['Position',
            'Returns a Position object representing the current position of the sensor as ' +
            'calculated by doubly integrating the observed sensor accelerations.'],
        ['QuaternionOrientation',
            'Returns a Quaternion object representing the absolute orientation of the sensor.'],
        ['SystemError',
            'If SystemStatus is "SYSTEM_ERROR", returns particulars regarding that error.'],
        ['SystemStatus', 'Returns the current status of the system.'],
        ['Temperature', 'Returns a Temperature object representing the current temperature.'],
        ['Velocity',
            'Returns a Velocity object representing the current velocity of the sensor as ' +
            'calculated by integrating the observed sensor accelerations.'],
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

Blockly.JavaScript['bno055imu_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['bno055imu_getProperty_Acceleration'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Acceleration', 'Acceleration'],
        ['Gravity', 'Gravity'],
        ['LinearAcceleration', 'LinearAcceleration'],
        ['OverallAcceleration', 'OverallAcceleration'],
    ];
    this.setOutput(true, 'Acceleration');
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Acceleration',
            'Returns an Acceleration object representing the last observed acceleration of the ' +
            'sensor. Note that this does not communicate with the sensor, but rather returns the ' +
            'most recent value reported to the acceleration integration algorithm.'],
        ['Gravity',
            'Returns an Acceleration object representing the direction of the force of gravity ' +
            'relative to the sensor.'],
        ['LinearAcceleration',
            'Returns an Acceleration object representing the acceleration experienced by the ' +
            'sensor due to the movement of the sensor.'],
        ['OverallAcceleration',
            'Returns an Acceleration object representing the overall acceleration experienced by ' +
            'the sensor. This is composed of a component due to the movement of the sensor and a ' +
            'component due to the force of gravity.'],
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

Blockly.JavaScript['bno055imu_getProperty_Acceleration'] =
    Blockly.JavaScript['bno055imu_getProperty'];

Blockly.Blocks['bno055imu_getProperty_Orientation'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngularOrientation', 'AngularOrientation'],
    ];
    this.setOutput(true, 'Orientation');
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngularOrientation',
            'Returns an Orientation object representing the absolute orientation of the sensor ' +
            'as a set three angles.'],
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

Blockly.JavaScript['bno055imu_getProperty_Orientation'] =
    Blockly.JavaScript['bno055imu_getProperty'];

Blockly.Blocks['bno055imu_getProperty_AngularVelocity'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngularVelocity', 'AngularVelocity'],
    ];
    this.setOutput(true, 'AngularVelocity');
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngularVelocity',
            'Returns an AngularVelocity object representing the rate of change of the absolute ' +
            'orientation of the sensor.'],
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

Blockly.JavaScript['bno055imu_getProperty_AngularVelocity'] =
    Blockly.JavaScript['bno055imu_getProperty'];

Blockly.Blocks['bno055imu_getProperty_String'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['CalibrationStatus', 'CalibrationStatus'],
        ['SystemError', 'SystemError'],
        ['SystemStatus', 'SystemStatus'],
    ];
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CalibrationStatus', 'Returns the calibration status of the IMU.'],
        ['SystemError',
            'If SystemStatus is "SYSTEM_ERROR", returns particulars regarding that error.'],
        ['SystemStatus', 'Returns the current status of the system.'],
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

Blockly.JavaScript['bno055imu_getProperty_String'] =
    Blockly.JavaScript['bno055imu_getProperty'];

Blockly.Blocks['bno055imu_getProperty_MagneticFlux'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['MagneticFieldStrength', 'MagneticFieldStrength'],
    ];
    this.setOutput(true, 'MagneticFlux');
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MagneticFieldStrength',
            'Returns a MagneticFlux object representing the magnetic field strength experienced ' +
            'by the sensor.'],
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

Blockly.JavaScript['bno055imu_getProperty_MagneticFlux'] =
    Blockly.JavaScript['bno055imu_getProperty'];

Blockly.Blocks['bno055imu_getProperty_Parameters'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Parameters', 'Parameters'],
    ];
    this.setOutput(true, 'Parameters');
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Parameters',
            'Returns the parameters with which initialization was last attempted.'],
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

Blockly.JavaScript['bno055imu_getProperty_Parameters'] =
    Blockly.JavaScript['bno055imu_getProperty'];

Blockly.Blocks['bno055imu_getProperty_Position'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Position', 'Position'],
    ];
    this.setOutput(true, 'Position');
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Position',
            'Returns a Position object representing the current position of the sensor as ' +
            'calculated by doubly integrating the observed sensor accelerations.'],
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

Blockly.JavaScript['bno055imu_getProperty_Position'] =
    Blockly.JavaScript['bno055imu_getProperty'];

Blockly.Blocks['bno055imu_getProperty_Quaternion'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['QuaternionOrientation', 'QuaternionOrientation'],
    ];
    this.setOutput(true, 'Quaternion');
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['QuaternionOrientation',
            'Returns a Quaternion object representing the absolute orientation of the sensor.'],
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

Blockly.JavaScript['bno055imu_getProperty_Quaternion'] =
    Blockly.JavaScript['bno055imu_getProperty'];

Blockly.Blocks['bno055imu_getProperty_Temperature'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Temperature', 'Temperature'],
    ];
    this.setOutput(true, 'Temperature');
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Temperature', 'Returns a Temperature object representing the current temperature.'],
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

Blockly.JavaScript['bno055imu_getProperty_Temperature'] =
    Blockly.JavaScript['bno055imu_getProperty'];

Blockly.Blocks['bno055imu_getProperty_Velocity'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Velocity', 'Velocity'],
    ];
    this.setOutput(true, 'Velocity');
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Velocity',
            'Returns a Velocity object representing the current velocity of the sensor as ' +
            'calculated by integrating the observed sensor accelerations.'],
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

Blockly.JavaScript['bno055imu_getProperty_Velocity'] =
    Blockly.JavaScript['bno055imu_getProperty'];

Blockly.Blocks['bno055imu_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'The 7 bit I2C address of the sensor.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the sensor.'],
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

Blockly.JavaScript['bno055imu_getProperty_Number'] =
    Blockly.JavaScript['bno055imu_getProperty'];

Blockly.Blocks['bno055imu_getProperty_Array'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngularOrientationAxes', 'AngularOrientationAxes'],
        ['AngularVelocityAxes', 'AngularVelocityAxes'],
    ];
    this.setOutput(true, 'Array');
    this.appendDummyInput()
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngularOrientationAxes', 'Returns a list of the axes on which the sensor measures ' +
            'angular orientation..'],
        ['AngularVelocityAxes', 'Returns a list of the axes on which the sensor measures ' +
            'angular velocity. Some sensors measure angular velocity on all three axes ' +
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['bno055imu_getProperty_Array'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = 'JSON.parse(' + identifier + '.get' + property + '())';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

// Functions

Blockly.Blocks['bno055imu_initialize'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('initialize'));
    this.appendValueInput('PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip(
        'Initialize the sensor using the indicated set of parameters. Note that the execution of ' +
        'this method can take a fairly long while, possibly several tens of milliseconds.');
  }
};

Blockly.JavaScript['bno055imu_initialize'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var parameters = Blockly.JavaScript.valueToCode(
      block, 'PARAMETERS', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.initialize(' + parameters + ');\n';
};

Blockly.Blocks['bno055imu_startAccelerationIntegration_with1'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('startAccelerationIntegration'));
    this.appendValueInput('MS_POLL_INTERVAL').setCheck('Number')
        .appendField('msPollInterval')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip(
        'Start (or re-start) a thread that continuously at intervals polls the current linear ' +
        'acceleration of the sensor and integrates it to provide velocity and position ' +
        'information.');
  }
};

Blockly.JavaScript['bno055imu_startAccelerationIntegration_with1'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var msPollInterval = Blockly.JavaScript.valueToCode(
      block, 'MS_POLL_INTERVAL', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.startAccelerationIntegration_with1(' + msPollInterval + ');\n';
};

Blockly.Blocks['bno055imu_startAccelerationIntegration_with3'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('startAccelerationIntegration'));
    this.appendValueInput('INITIAL_POSITION').setCheck('Position')
        .appendField('initialPosition')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('INITIAL_VELOCITY').setCheck('Velocity')
        .appendField('initialVelocity')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MS_POLL_INTERVAL').setCheck('Number')
        .appendField('msPollInterval')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip(
        'Start (or re-start) a thread that continuously at intervals polls the current linear ' +
        'acceleration of the sensor and integrates it to provide velocity and position ' +
        'information.');
  }
};

Blockly.JavaScript['bno055imu_startAccelerationIntegration_with3'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var initialPosition = Blockly.JavaScript.valueToCode(
      block, 'INITIAL_POSITION', Blockly.JavaScript.ORDER_COMMA);
  var initialVelocity = Blockly.JavaScript.valueToCode(
      block, 'INITIAL_VELOCITY', Blockly.JavaScript.ORDER_COMMA);
  var msPollInterval = Blockly.JavaScript.valueToCode(
      block, 'MS_POLL_INTERVAL', Blockly.JavaScript.ORDER_COMMA);
  return identifier + '.startAccelerationIntegration_with3(' + initialPosition + ', ' +
      initialVelocity + ', ' + msPollInterval + ');\n';
};

Blockly.Blocks['bno055imu_stopAccelerationIntegration'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('stopAccelerationIntegration'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip(
        'Stop the integration thread if it is currently running.');
  }
};

Blockly.JavaScript['bno055imu_stopAccelerationIntegration'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  return identifier + '.stopAccelerationIntegration();\n';
};

Blockly.Blocks['bno055imu_isSystemCalibrated'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isSystemCalibrated'));
    this.setTooltip(
        'Answers as to whether the system is fully calibrated. The system is fully calibrated ' +
        'if the gyro, accelerometer, and magnetometer are fully calibrated.');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['bno055imu_isSystemCalibrated'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isSystemCalibrated()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['bno055imu_isGyroCalibrated'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isGyroCalibrated'));
    this.setTooltip(
        'Answers as to whether the gyro is fully calibrated.');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['bno055imu_isGyroCalibrated'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isGyroCalibrated()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['bno055imu_isAccelerometerCalibrated'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isAccelerometerCalibrated'));
    this.setTooltip(
        'Answers as to whether the accelerometer is fully calibrated.');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['bno055imu_isAccelerometerCalibrated'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isAccelerometerCalibrated()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['bno055imu_isMagnetometerCalibrated'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isMagnetometerCalibrated'));
    this.setTooltip(
        'Answers as to whether the magnetometer is fully calibrated.');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['bno055imu_isMagnetometerCalibrated'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isMagnetometerCalibrated()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['bno055imu_saveCalibrationData'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('saveCalibrationData'));
    this.appendValueInput('FILE_NAME').setCheck('String')
        .appendField('fileName')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip(
        'Saves the calibration data to the given file.');
  }
};

Blockly.JavaScript['bno055imu_saveCalibrationData'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var fileName = Blockly.JavaScript.valueToCode(
      block, 'FILE_NAME', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.saveCalibrationData(' + fileName + ');\n';
};

Blockly.Blocks['bno055imu_getAngularVelocity'] = {
  init: function() {
    this.setOutput(true, 'AngularVelocity');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('getAngularVelocity'));
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the angular rotation rate across all the axes measured by the sensor. ' +
        'Axes on which angular velocity is not measured are reported as zero.');
  }
};

Blockly.JavaScript['bno055imu_getAngularVelocity'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_NONE);
  var code = identifier + '.getAngularVelocity(' + angleUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['bno055imu_getAngularOrientation'] = {
  init: function() {
    this.setOutput(true, 'Orientation');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createBNO055IMUDropdown(), 'IDENTIFIER')
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
    this.setTooltip('Returns the absolute orientation of the sensor as a set three angles. ' +
        'Axes on which absolute orientation is not measured are reported as zero.');
  }
};

Blockly.JavaScript['bno055imu_getAngularOrientation'] = function(block) {
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
