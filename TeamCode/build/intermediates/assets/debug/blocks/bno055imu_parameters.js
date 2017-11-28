/**
 * @fileoverview FTC robot blocks related to BNO055IMU.Parameters.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// bno055imuParametersIdentifier
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['bno055imuParameters_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AccelUnit', 'AccelUnit'],
        ['AccelerationIntegrationAlgorithm', 'AccelerationIntegrationAlgorithm'],
        ['AngleUnit', 'AngleUnit'],
        ['CalibrationDataFile', 'CalibrationDataFile'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
        ['LoggingEnabled', 'LoggingEnabled'],
        ['LoggingTag', 'LoggingTag'],
        ['SensorMode', 'SensorMode'],
        ['TempUnit', 'TempUnit'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AccelUnit', 'The AccelUnit of the given parameters.'],
        ['AccelerationIntegrationAlgorithm',
            'The AccelerationIntegrationAlgorithm of the given parameters.'],
        ['AngleUnit', 'The AngleUnit of the given parameters.'],
        ['CalibrationDataFile', 'The calibration data file for the given parameters.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address.'],
        ['LoggingEnabled', 'True if logging is enabled in the given parameters, false otherwise.'],
        ['LoggingTag', 'The logging tag of the given parameters.'],
        ['SensorMode', 'The SensorMode of the given parameters.'],
        ['TempUnit', 'The TempUnit of the given parameters.'],
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

Blockly.JavaScript['bno055imuParameters_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_NONE);
  var code = bno055imuParametersIdentifier + '.get' + property + '(' +
      bno055imuParameters + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['bno055imuParameters_getProperty_AccelUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AccelUnit', 'AccelUnit'],
    ];
    this.setOutput(true, 'AccelUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AccelUnit', 'The AccelUnit of the given parameters.'],
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

Blockly.JavaScript['bno055imuParameters_getProperty_AccelUnit'] =
    Blockly.JavaScript['bno055imuParameters_getProperty'];

Blockly.Blocks['bno055imuParameters_getProperty_AccelerationIntegrationAlgorithm'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AccelerationIntegrationAlgorithm', 'AccelerationIntegrationAlgorithm'],
    ];
    this.setOutput(true, 'AccelerationIntegrationAlgorithm');
    this.appendDummyInput()
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AccelerationIntegrationAlgorithm',
            'The AccelerationIntegrationAlgorithm of the given parameters.'],
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

Blockly.JavaScript['bno055imuParameters_getProperty_AccelerationIntegrationAlgorithm'] =
    Blockly.JavaScript['bno055imuParameters_getProperty'];

Blockly.Blocks['bno055imuParameters_getProperty_AngleUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngleUnit', 'AngleUnit'],
    ];
    this.setOutput(true, 'AngleUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngleUnit', 'The AngleUnit of the given parameters.'],
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

Blockly.JavaScript['bno055imuParameters_getProperty_AngleUnit'] =
    Blockly.JavaScript['bno055imuParameters_getProperty'];

Blockly.Blocks['bno055imuParameters_getProperty_String'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['CalibrationDataFile', 'CalibrationDataFile'],
        ['LoggingTag', 'LoggingTag'],
    ];
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CalibrationDataFile', 'The calibration data file for the given parameters.'],
        ['LoggingTag', 'The logging tag of the given parameters.'],
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

Blockly.JavaScript['bno055imuParameters_getProperty_String'] =
    Blockly.JavaScript['bno055imuParameters_getProperty'];

Blockly.Blocks['bno055imuParameters_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'The 7 bit I2C address.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address.'],
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

Blockly.JavaScript['bno055imuParameters_getProperty_Number'] =
    Blockly.JavaScript['bno055imuParameters_getProperty'];

Blockly.Blocks['bno055imuParameters_getProperty_Boolean'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['LoggingEnabled', 'LoggingEnabled'],
    ];
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['LoggingEnabled', 'True if logging is enabled in the given parameters, false otherwise.'],
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

Blockly.JavaScript['bno055imuParameters_getProperty_Boolean'] =
    Blockly.JavaScript['bno055imuParameters_getProperty'];

Blockly.Blocks['bno055imuParameters_getProperty_SensorMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['SensorMode', 'SensorMode'],
    ];
    this.setOutput(true, 'SensorMode');
    this.appendDummyInput()
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['SensorMode', 'The SensorMode of the given parameters.'],
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

Blockly.JavaScript['bno055imuParameters_getProperty_SensorMode'] =
    Blockly.JavaScript['bno055imuParameters_getProperty'];

Blockly.Blocks['bno055imuParameters_getProperty_TempUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['TempUnit', 'TempUnit'],
    ];
    this.setOutput(true, 'TempUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['TempUnit', 'The TempUnit of the given parameters.'],
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

Blockly.JavaScript['bno055imuParameters_getProperty_TempUnit'] =
    Blockly.JavaScript['bno055imuParameters_getProperty'];

// Functions

Blockly.Blocks['bno055imuParameters_create'] = {
  init: function() {
    this.setOutput(true, 'BNO055IMU.Parameters');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('IMU-BNO055.Parameters'));
    this.setColour(functionColor);
    this.setTooltip('Create a new parameters object.');
  }
};

Blockly.JavaScript['bno055imuParameters_create'] = function(block) {
  var code = bno055imuParametersIdentifier + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['bno055imuParameters_setAccelUnit'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setAccelUnit'));
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ACCEL_UNIT').setCheck('AccelUnit')
        .appendField('accelUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the AccelUnit for the IMU-BNO055 sensor.');
  }
};

Blockly.JavaScript['bno055imuParameters_setAccelUnit'] = function(block) {
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var accelUnit = Blockly.JavaScript.valueToCode(
      block, 'ACCEL_UNIT', Blockly.JavaScript.ORDER_COMMA);
  return bno055imuParametersIdentifier + '.setAccelUnit(' +
      bno055imuParameters + ', ' + accelUnit + ');\n';
};

Blockly.Blocks['bno055imuParameters_setAccelerationIntegrationAlgorithm'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setAccelerationIntegrationAlgorithm'));
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ACCELERATION_INTEGRATION_ALGORITHM').setCheck('AccelerationIntegrationAlgorithm')
        .appendField('accelerationIntegrationAlgorithm')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the acceleration integration algorithm for the IMU-BNO055 sensor.');
  }
};

Blockly.JavaScript['bno055imuParameters_setAccelerationIntegrationAlgorithm'] = function(block) {
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var accelerationIntegrationAlgorithm = Blockly.JavaScript.valueToCode(
      block, 'ACCELERATION_INTEGRATION_ALGORITHM', Blockly.JavaScript.ORDER_COMMA);
  return bno055imuParametersIdentifier + '.setAccelerationIntegrationAlgorithm(' +
      bno055imuParameters + ', ' + accelerationIntegrationAlgorithm + ');\n';
};

Blockly.Blocks['bno055imuParameters_setAngleUnit'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setAngleUnit'));
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the AngleUnit for the IMU-BNO055 sensor.');
  }
};

Blockly.JavaScript['bno055imuParameters_setAngleUnit'] = function(block) {
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  return bno055imuParametersIdentifier + '.setAngleUnit(' +
      bno055imuParameters + ', ' + angleUnit + ');\n';
};


Blockly.Blocks['bno055imuParameters_setCalibrationDataFile'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setCalibrationDataFile'));
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CALIBRATION_DATA_FILE').setCheck('String')
        .appendField('calibrationDataFile')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the calibration data file for the IMU-BNO055 sensor.');
  }
};

Blockly.JavaScript['bno055imuParameters_setCalibrationDataFile'] = function(block) {
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var calibrationDataFile = Blockly.JavaScript.valueToCode(
      block, 'CALIBRATION_DATA_FILE', Blockly.JavaScript.ORDER_COMMA);
  return bno055imuParametersIdentifier + '.setCalibrationDataFile(' +
      bno055imuParameters + ', ' + calibrationDataFile + ');\n';
};

Blockly.Blocks['bno055imuParameters_setI2cAddress7Bit'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setI2cAddress7Bit'));
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('I2C_ADDRESS_7_BIT').setCheck('Number')
        .appendField('i2cAddress7Bit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the 7 bit I2C address.');
  }
};

Blockly.JavaScript['bno055imuParameters_setI2cAddress7Bit'] = function(block) {
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var i2cAddress7Bit = Blockly.JavaScript.valueToCode(
      block, 'I2C_ADDRESS_7_BIT', Blockly.JavaScript.ORDER_COMMA);
  return bno055imuParametersIdentifier + '.setI2cAddress7Bit(' +
      bno055imuParameters + ', ' + i2cAddress7Bit + ');\n';
};

Blockly.Blocks['bno055imuParameters_setI2cAddress8Bit'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setI2cAddress8Bit'));
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('I2C_ADDRESS_8_BIT').setCheck('Number')
        .appendField('i2cAddress8Bit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the 8 bit I2C address.');
  }
};

Blockly.JavaScript['bno055imuParameters_setI2cAddress8Bit'] = function(block) {
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var i2cAddress8Bit = Blockly.JavaScript.valueToCode(
      block, 'I2C_ADDRESS_8_BIT', Blockly.JavaScript.ORDER_COMMA);
  return bno055imuParametersIdentifier + '.setI2cAddress8Bit(' +
      bno055imuParameters + ', ' + i2cAddress8Bit + ');\n';
};

Blockly.Blocks['bno055imuParameters_setLoggingEnabled'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setLoggingEnabled'));
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('LOGGING_ENABLED').setCheck('Boolean')
        .appendField('loggingEnabled')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets whether logging is enabled.');
  }
};

Blockly.JavaScript['bno055imuParameters_setLoggingEnabled'] = function(block) {
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var loggingEnabled = Blockly.JavaScript.valueToCode(
      block, 'LOGGING_ENABLED', Blockly.JavaScript.ORDER_COMMA);
  return bno055imuParametersIdentifier + '.setLoggingEnabled(' +
      bno055imuParameters + ', ' + loggingEnabled + ');\n';
};

Blockly.Blocks['bno055imuParameters_setLoggingTag'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setLoggingTag'));
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('LOGGING_TAG').setCheck('String')
        .appendField('loggingTag')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the logging tag for the IMU-BNO055 sensor.');
  }
};

Blockly.JavaScript['bno055imuParameters_setLoggingTag'] = function(block) {
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var loggingTag = Blockly.JavaScript.valueToCode(
      block, 'LOGGING_TAG', Blockly.JavaScript.ORDER_COMMA);
  return bno055imuParametersIdentifier + '.setLoggingTag(' +
      bno055imuParameters + ', ' + loggingTag + ');\n';
};
Blockly.Blocks['bno055imuParameters_setSensorMode'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setSensorMode'));
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SENSOR_MODE').setCheck('SensorMode')
        .appendField('sensorMode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the SensorMode for the IMU-BNO055 sensor.');
  }
};

Blockly.JavaScript['bno055imuParameters_setSensorMode'] = function(block) {
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var sensorMode = Blockly.JavaScript.valueToCode(
      block, 'SENSOR_MODE', Blockly.JavaScript.ORDER_COMMA);
  return bno055imuParametersIdentifier + '.setSensorMode(' +
      bno055imuParameters + ', ' + sensorMode + ');\n';
};

Blockly.Blocks['bno055imuParameters_setTempUnit'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('IMU-BNO055.Parameters'))
        .appendField('.')
        .appendField(createNonEditableField('setTempUnit'));
    this.appendValueInput('BNO055IMU_PARAMETERS').setCheck('BNO055IMU.Parameters')
        .appendField('parameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('TEMP_UNIT').setCheck('TempUnit')
        .appendField('tempUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the TempUnit for the IMU-BNO055 sensor.');
  }
};

Blockly.JavaScript['bno055imuParameters_setTempUnit'] = function(block) {
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var tempUnit = Blockly.JavaScript.valueToCode(
      block, 'TEMP_UNIT', Blockly.JavaScript.ORDER_COMMA);
  return bno055imuParametersIdentifier + '.setTempUnit(' +
      bno055imuParameters + ', ' + tempUnit + ');\n';
};

// Enums

Blockly.Blocks['bno055imuParameters_enum_accelUnit'] = {
  init: function() {
    var ACCEL_UNIT_CHOICES = [
        ['METERS_PERSEC_PERSEC', 'METERS_PERSEC_PERSEC'],
        ['MILLI_EARTH_GRAVITY', 'MILLI_EARTH_GRAVITY'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('AccelUnit'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(ACCEL_UNIT_CHOICES), 'ACCEL_UNIT');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['METERS_PERSEC_PERSEC', 'The AccelUnit value METERS_PERSEC_PERSEC.'],
        ['MILLI_EARTH_GRAVITY', 'The AccelUnit value MILLI_EARTH_GRAVITY.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('ACCEL_UNIT');
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

Blockly.JavaScript['bno055imuParameters_enum_accelUnit'] = function(block) {
  var code = '"' + block.getFieldValue('ACCEL_UNIT') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['bno055imuParameters_typedEnum_accelUnit'] = {
  init: function() {
    var ACCEL_UNIT_CHOICES = [
        ['METERS_PERSEC_PERSEC', 'METERS_PERSEC_PERSEC'],
        ['MILLI_EARTH_GRAVITY', 'MILLI_EARTH_GRAVITY'],
    ];
    this.setOutput(true, 'AccelUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('AccelUnit'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(ACCEL_UNIT_CHOICES), 'ACCEL_UNIT');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['METERS_PERSEC_PERSEC', 'The AccelUnit value METERS_PERSEC_PERSEC.'],
        ['MILLI_EARTH_GRAVITY', 'The AccelUnit value MILLI_EARTH_GRAVITY.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('ACCEL_UNIT');
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

Blockly.JavaScript['bno055imuParameters_typedEnum_accelUnit'] =
    Blockly.JavaScript['bno055imuParameters_enum_accelUnit'];

Blockly.Blocks['bno055imuParameters_enum_accelerationIntegrationAlgorithm'] = {
  init: function() {
    var ACCELERATION_INTEGRATION_ALGORITHM_CHOICES = [
        ['NAIVE', 'NAIVE'],
        ['JUST_LOGGING', 'JUST_LOGGING'],
    ];
    this.setOutput(true, 'AccelerationIntegrationAlgorithm');
    this.appendDummyInput()
        .appendField(createNonEditableField('AccelerationIntegrationAlgorithm'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(ACCELERATION_INTEGRATION_ALGORITHM_CHOICES),
            'ACCELERATION_INTEGRATION_ALGORITHM');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['NAIVE',
            'Use a very naive acceleration integration algorithm that just does the basic physics.'],
        ['JUST_LOGGING',
            'Use an acceleration integration algorithm that doesn\'t actually integrate ' +
            'accelerations, but merely reports them in the logcat log.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('ACCELERATION_INTEGRATION_ALGORITHM');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['bno055imuParameters_enum_accelerationIntegrationAlgorithm'] = function(block) {
  var code = '"' + block.getFieldValue('ACCELERATION_INTEGRATION_ALGORITHM') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['bno055imuParameters_typedEnum_accelerationIntegrationAlgorithm'] =
    Blockly.Blocks['bno055imuParameters_enum_accelerationIntegrationAlgorithm'];

Blockly.JavaScript['bno055imuParameters_typedEnum_accelerationIntegrationAlgorithm'] =
    Blockly.JavaScript['bno055imuParameters_enum_accelerationIntegrationAlgorithm'];

Blockly.Blocks['bno055imuParameters_enum_sensorMode'] = {
  init: function() {
    var SENSOR_MODE_CHOICES = [
        ['ACCONLY', 'ACCONLY'],
        ['MAGONLY', 'MAGONLY'],
        ['GYRONLY', 'GYRONLY'],
        ['ACCMAG', 'ACCMAG'],
        ['ACCGYRO', 'ACCGYRO'],
        ['MAGGYRO', 'MAGGYRO'],
        ['AMG', 'AMG'],
        ['IMU', 'IMU'],
        ['COMPASS', 'COMPASS'],
        ['M4G', 'M4G'],
        ['NDOF_FMC_OFF', 'NDOF_FMC_OFF'],
        ['NDOF', 'NDOF'],
    ];
    this.setOutput(true, 'SensorMode');
    this.appendDummyInput()
        .appendField(createNonEditableField('SensorMode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(SENSOR_MODE_CHOICES),
            'SENSOR_MODE');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['ACCONLY', 'The BNO055 Operation Mode ACCONLY'],
        ['MAGONLY', 'The BNO055 Operation Mode MAGONLY'],
        ['GYRONLY', 'The BNO055 Operation Mode GYRONLY'],
        ['ACCMAG', 'The BNO055 Operation Mode ACCMAG'],
        ['ACCGYRO', 'The BNO055 Operation Mode ACCGYRO'],
        ['MAGGYRO', 'The BNO055 Operation Mode MAGGYRO'],
        ['AMG', 'The BNO055 Operation Mode AMG'],
        ['IMU', 'The BNO055 Operation Mode IMU'],
        ['COMPASS', 'The BNO055 Operation Mode COMPASS'],
        ['M4G', 'The BNO055 Operation Mode M4G'],
        ['NDOF_FMC_OFF', 'The BNO055 Operation Mode NDOF_FMC_OFF'],
        ['NDOF', 'The BNO055 Operation Mode NDOF'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('SENSOR_MODE');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['bno055imuParameters_enum_sensorMode'] = function(block) {
  var code = '"' + block.getFieldValue('SENSOR_MODE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['bno055imuParameters_typedEnum_sensorMode'] =
    Blockly.Blocks['bno055imuParameters_enum_sensorMode'];

Blockly.JavaScript['bno055imuParameters_typedEnum_sensorMode'] =
    Blockly.JavaScript['bno055imuParameters_enum_sensorMode'];
