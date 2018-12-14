/**
 * @fileoverview FTC robot blocks related to BNO055IMU.Parameters.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// bno055imuParametersIdentifierForJavaScript
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
        ['AccelUnit', 'Returns the AccelUnit of the given IMU-BNO055.Parameters.'],
        ['AccelerationIntegrationAlgorithm', 'Returns the AccelerationIntegrationAlgorithm of the given IMU-BNO055.Parameters.'],
        ['AngleUnit', 'Returns the AngleUnit of the given IMU-BNO055.Parameters.'],
        ['CalibrationDataFile', 'Returns the calibration data file for the given IMU-BNO055.Parameters.'],
        ['I2cAddress7Bit', 'Returns the 7 bit I2C address of the given IMU-BNO055.Parameters.'],
        ['I2cAddress8Bit', 'Returns the 8 bit I2C address of the given IMU-BNO055.Parameters.'],
        ['LoggingEnabled', 'Returns true if logging is enabled in the given IMU-BNO055.Parameters, false otherwise.'],
        ['LoggingTag', 'Returns the logging tag of the given IMU-BNO055.Parameters.'],
        ['SensorMode', 'Returns the SensorMode of the given IMU-BNO055.Parameters.'],
        ['TempUnit', 'Returns the TempUnit of the given IMU-BNO055.Parameters.'],
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
  var code = bno055imuParametersIdentifierForJavaScript + '.get' + property + '(' +
      bno055imuParameters + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['bno055imuParameters_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var bno055imuParameters = Blockly.FtcJava.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.FtcJava.ORDER_MEMBER);
  var code;
  switch (property) {
    case 'AngleUnit':
      code = bno055imuParameters + '.angleUnit.toAngleUnit()';
      return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
    case 'I2cAddress7Bit':
      code = bno055imuParameters + '.i2cAddr.get7Bit()';
      return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
    case 'I2cAddress8Bit':
      code = bno055imuParameters + '.i2cAddr.get8Bit()';
      return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
    case 'TempUnit':
      code = bno055imuParameters + '.temperatureUnit.toTempUnit()';
      return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
    case 'SensorMode':
      property = 'mode';
      break;
    case 'AccelUnit':
    case 'AccelerationIntegrationAlgorithm':
    case 'CalibrationDataFile':
    case 'LoggingEnabled':
    case 'LoggingTag':
      property = Blockly.FtcJava.makeFirstLetterLowerCase_(property);
      break;
    default:
      throw 'Unexpected property ' + property + ' (bno055imuParameters_getProperty).';
  }
  code = bno055imuParameters + '.' + property;
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['bno055imuParameters_getProperty_AccelUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AccelUnit', 'AccelUnit'],
    ];
    this.setOutput(true, 'BNO055IMU.AccelUnit');
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
        ['AccelUnit', 'Returns the AccelUnit of the given IMU-BNO055.Parameters.'],
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

Blockly.FtcJava['bno055imuParameters_getProperty_AccelUnit'] =
    Blockly.FtcJava['bno055imuParameters_getProperty'];

Blockly.Blocks['bno055imuParameters_getProperty_AccelerationIntegrationAlgorithm'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AccelerationIntegrationAlgorithm', 'AccelerationIntegrationAlgorithm'],
    ];
    this.setOutput(true, 'BNO055IMU.AccelerationIntegrator');
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
        ['AccelerationIntegrationAlgorithm', 'Returns the AccelerationIntegrationAlgorithm of the given IMU-BNO055.Parameters.'],
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

Blockly.FtcJava['bno055imuParameters_getProperty_AccelerationIntegrationAlgorithm'] =
    Blockly.FtcJava['bno055imuParameters_getProperty'];

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
        ['AngleUnit', 'Returns the AngleUnit of the given IMU-BNO055.Parameters.'],
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

Blockly.FtcJava['bno055imuParameters_getProperty_AngleUnit'] =
    Blockly.FtcJava['bno055imuParameters_getProperty'];

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
        ['CalibrationDataFile', 'Returns the calibration data file for the given IMU-BNO055.Parameters.'],
        ['LoggingTag', 'Returns the logging tag of the given IMU-BNO055.Parameters.'],
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

Blockly.FtcJava['bno055imuParameters_getProperty_String'] =
    Blockly.FtcJava['bno055imuParameters_getProperty'];

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
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'Returns the 7 bit I2C address of the given IMU-BNO055.Parameters.'],
        ['I2cAddress8Bit', 'Returns the 8 bit I2C address of the given IMU-BNO055.Parameters.'],
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
        default:
          throw 'Unexpected property ' + property + ' (bno055imuParameters_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['bno055imuParameters_getProperty_Number'] =
    Blockly.JavaScript['bno055imuParameters_getProperty'];

Blockly.FtcJava['bno055imuParameters_getProperty_Number'] =
    Blockly.FtcJava['bno055imuParameters_getProperty'];

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
        ['LoggingEnabled', 'Returns true if logging is enabled in the given IMU-BNO055.Parameters, false otherwise.'],
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

Blockly.FtcJava['bno055imuParameters_getProperty_Boolean'] =
    Blockly.FtcJava['bno055imuParameters_getProperty'];

Blockly.Blocks['bno055imuParameters_getProperty_SensorMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['SensorMode', 'SensorMode'],
    ];
    this.setOutput(true, 'BNO055IMU.SensorMode');
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
        ['SensorMode', 'Returns the SensorMode of the given IMU-BNO055.Parameters.'],
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

Blockly.FtcJava['bno055imuParameters_getProperty_SensorMode'] =
    Blockly.FtcJava['bno055imuParameters_getProperty'];

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
        ['TempUnit', 'Returns the TempUnit of the given IMU-BNO055.Parameters.'],
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

Blockly.FtcJava['bno055imuParameters_getProperty_TempUnit'] =
    Blockly.FtcJava['bno055imuParameters_getProperty'];

// Functions

Blockly.Blocks['bno055imuParameters_create'] = {
  init: function() {
    this.setOutput(true, 'BNO055IMU.Parameters');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('IMU-BNO055.Parameters'));
    this.setColour(functionColor);
    this.setTooltip('Creates a new IMU-BNO055.Parameters object.');
  }
};

Blockly.JavaScript['bno055imuParameters_create'] = function(block) {
  var code = bno055imuParametersIdentifierForJavaScript + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['bno055imuParameters_create'] = function(block) {
  var code = 'new BNO055IMU.Parameters()';
  Blockly.FtcJava.generateImport_('BNO055IMU');
  return [code, Blockly.FtcJava.ORDER_NEW];
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
    this.appendValueInput('ACCEL_UNIT').setCheck('BNO055IMU.AccelUnit')
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
  return bno055imuParametersIdentifierForJavaScript + '.setAccelUnit(' +
      bno055imuParameters + ', ' + accelUnit + ');\n';
};

Blockly.FtcJava['bno055imuParameters_setAccelUnit'] = function(block) {
  var bno055imuParameters = Blockly.FtcJava.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.FtcJava.ORDER_MEMBER);
  var accelUnit = Blockly.FtcJava.valueToCode(
      block, 'ACCEL_UNIT', Blockly.FtcJava.ORDER_ASSIGNMENT);
  return bno055imuParameters + '.accelUnit = ' + accelUnit + ';\n';
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
    this.appendValueInput('ACCELERATION_INTEGRATION_ALGORITHM').setCheck('BNO055IMU.AccelerationIntegrator')
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
  return bno055imuParametersIdentifierForJavaScript + '.setAccelerationIntegrationAlgorithm(' +
      bno055imuParameters + ', ' + accelerationIntegrationAlgorithm + ');\n';
};

Blockly.FtcJava['bno055imuParameters_setAccelerationIntegrationAlgorithm'] = function(block) {
  var bno055imuParameters = Blockly.FtcJava.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.FtcJava.ORDER_MEMBER);
  var accelerationIntegrationAlgorithm = Blockly.FtcJava.valueToCode(
      block, 'ACCELERATION_INTEGRATION_ALGORITHM', Blockly.FtcJava.ORDER_ASSIGNMENT);
  return bno055imuParameters + '.accelerationIntegrationAlgorithm = ' +
      accelerationIntegrationAlgorithm + ';\n';
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
  return bno055imuParametersIdentifierForJavaScript + '.setAngleUnit(' +
      bno055imuParameters + ', ' + angleUnit + ');\n';
};

Blockly.FtcJava['bno055imuParameters_setAngleUnit'] = function(block) {
  var bno055imuParameters = Blockly.FtcJava.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.FtcJava.ORDER_MEMBER);
  var angleUnit = Blockly.FtcJava.valueToCode(
      block, 'ANGLE_UNIT', Blockly.FtcJava.ORDER_ASSIGNMENT);
  if (angleUnit.startsWith('AngleUnit.')) {
    angleUnit = 'BNO055IMU.' + angleUnit;
  } else {
    angleUnit = Blockly.FtcJava.valueToCode(
        block, 'ANGLE_UNIT', Blockly.FtcJava.ORDER_NONE);
    angleUnit = 'BNO055IMU.AngleUnit.fromAngleUnit(' + angleUnit + ')';
  }
  Blockly.FtcJava.generateImport_('BNO055IMU');
  return bno055imuParameters + '.angleUnit = ' + angleUnit + ';\n';
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
  return bno055imuParametersIdentifierForJavaScript + '.setCalibrationDataFile(' +
      bno055imuParameters + ', ' + calibrationDataFile + ');\n';
};

Blockly.FtcJava['bno055imuParameters_setCalibrationDataFile'] = function(block) {
  var bno055imuParameters = Blockly.FtcJava.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.FtcJava.ORDER_MEMBER);
  var calibrationDataFile = Blockly.FtcJava.valueToCode(
      block, 'CALIBRATION_DATA_FILE', Blockly.FtcJava.ORDER_ASSIGNMENT);
  return bno055imuParameters + '.calibrationDataFile = ' + calibrationDataFile + ';\n';
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
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'I2C_ADDRESS_7_BIT':
          return 'int';
      }
      return '';
    };
  }
};

Blockly.JavaScript['bno055imuParameters_setI2cAddress7Bit'] = function(block) {
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var i2cAddress7Bit = Blockly.JavaScript.valueToCode(
      block, 'I2C_ADDRESS_7_BIT', Blockly.JavaScript.ORDER_COMMA);
  return bno055imuParametersIdentifierForJavaScript + '.setI2cAddress7Bit(' +
      bno055imuParameters + ', ' + i2cAddress7Bit + ');\n';
};

Blockly.FtcJava['bno055imuParameters_setI2cAddress7Bit'] = function(block) {
  var bno055imuParameters = Blockly.FtcJava.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.FtcJava.ORDER_MEMBER);
  var i2cAddress7Bit = Blockly.FtcJava.valueToCode(
      block, 'I2C_ADDRESS_7_BIT', Blockly.FtcJava.ORDER_NONE);
  Blockly.FtcJava.generateImport_('I2cAddr');
  return bno055imuParameters + '.i2cAddr = I2cAddr.create7bit(' + i2cAddress7Bit + ');\n';
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
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'I2C_ADDRESS_8_BIT':
          return 'int';
      }
      return '';
    };
  }
};

Blockly.JavaScript['bno055imuParameters_setI2cAddress8Bit'] = function(block) {
  var bno055imuParameters = Blockly.JavaScript.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.JavaScript.ORDER_COMMA);
  var i2cAddress8Bit = Blockly.JavaScript.valueToCode(
      block, 'I2C_ADDRESS_8_BIT', Blockly.JavaScript.ORDER_COMMA);
  return bno055imuParametersIdentifierForJavaScript + '.setI2cAddress8Bit(' +
      bno055imuParameters + ', ' + i2cAddress8Bit + ');\n';
};

Blockly.FtcJava['bno055imuParameters_setI2cAddress8Bit'] = function(block) {
  var bno055imuParameters = Blockly.FtcJava.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.FtcJava.ORDER_MEMBER);
  var i2cAddress8Bit = Blockly.FtcJava.valueToCode(
      block, 'I2C_ADDRESS_8_BIT', Blockly.FtcJava.ORDER_NONE);
  Blockly.FtcJava.generateImport_('I2cAddr');
  return bno055imuParameters + '.i2cAddr = I2cAddr.create8bit(' + i2cAddress8Bit + ');\n';
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
  return bno055imuParametersIdentifierForJavaScript + '.setLoggingEnabled(' +
      bno055imuParameters + ', ' + loggingEnabled + ');\n';
};

Blockly.FtcJava['bno055imuParameters_setLoggingEnabled'] = function(block) {
  var bno055imuParameters = Blockly.FtcJava.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.FtcJava.ORDER_MEMBER);
  var loggingEnabled = Blockly.FtcJava.valueToCode(
      block, 'LOGGING_ENABLED', Blockly.FtcJava.ORDER_ASSIGNMENT);
  return bno055imuParameters + '.loggingEnabled = ' + loggingEnabled + ';\n';
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
  return bno055imuParametersIdentifierForJavaScript + '.setLoggingTag(' +
      bno055imuParameters + ', ' + loggingTag + ');\n';
};

Blockly.FtcJava['bno055imuParameters_setLoggingTag'] = function(block) {
  var bno055imuParameters = Blockly.FtcJava.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.FtcJava.ORDER_MEMBER);
  var loggingTag = Blockly.FtcJava.valueToCode(
      block, 'LOGGING_TAG', Blockly.FtcJava.ORDER_ASSIGNMENT);
  return bno055imuParameters + '.loggingTag = ' + loggingTag + ';\n';
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
    this.appendValueInput('SENSOR_MODE').setCheck('BNO055IMU.SensorMode')
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
  return bno055imuParametersIdentifierForJavaScript + '.setSensorMode(' +
      bno055imuParameters + ', ' + sensorMode + ');\n';
};

Blockly.FtcJava['bno055imuParameters_setSensorMode'] = function(block) {
  var bno055imuParameters = Blockly.FtcJava.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.FtcJava.ORDER_MEMBER);
  var sensorMode = Blockly.FtcJava.valueToCode(
      block, 'SENSOR_MODE', Blockly.FtcJava.ORDER_ASSIGNMENT);
  return bno055imuParameters + '.mode = ' + sensorMode + ';\n';
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
  return bno055imuParametersIdentifierForJavaScript + '.setTempUnit(' +
      bno055imuParameters + ', ' + tempUnit + ');\n';
};

Blockly.FtcJava['bno055imuParameters_setTempUnit'] = function(block) {
  var bno055imuParameters = Blockly.FtcJava.valueToCode(
      block, 'BNO055IMU_PARAMETERS', Blockly.FtcJava.ORDER_MEMBER);
  var tempUnit = Blockly.FtcJava.valueToCode(
      block, 'TEMP_UNIT', Blockly.FtcJava.ORDER_ASSIGNMENT);
  if (tempUnit.startsWith('TempUnit.')) {
    tempUnit = 'BNO055IMU.' + tempUnit;
  } else {
    tempUnit = Blockly.FtcJava.valueToCode(
        block, 'TEMP_UNIT', Blockly.FtcJava.ORDER_NONE);
    tempUnit = 'BNO055IMU.TempUnit.fromTempUnit(' + tempUnit + ')';
  }
  Blockly.FtcJava.generateImport_('BNO055IMU');
  return bno055imuParameters + '.temperatureUnit = ' + tempUnit + ';\n';
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
    this.setColour(getPropertyColor);
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
  }
};

Blockly.JavaScript['bno055imuParameters_enum_accelUnit'] = function(block) {
  var code = '"' + block.getFieldValue('ACCEL_UNIT') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['bno055imuParameters_enum_accelUnit'] = function(block) {
  var code = 'BNO055IMU.AccelUnit.' + block.getFieldValue('ACCEL_UNIT');
  Blockly.FtcJava.generateImport_('BNO055IMU');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['bno055imuParameters_typedEnum_accelUnit'] = {
  init: function() {
    var ACCEL_UNIT_CHOICES = [
        ['METERS_PERSEC_PERSEC', 'METERS_PERSEC_PERSEC'],
        ['MILLI_EARTH_GRAVITY', 'MILLI_EARTH_GRAVITY'],
    ];
    this.setOutput(true, 'BNO055IMU.AccelUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('AccelUnit'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(ACCEL_UNIT_CHOICES), 'ACCEL_UNIT');
    this.setColour(getPropertyColor);
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
  }
};

Blockly.JavaScript['bno055imuParameters_typedEnum_accelUnit'] =
    Blockly.JavaScript['bno055imuParameters_enum_accelUnit'];

Blockly.FtcJava['bno055imuParameters_typedEnum_accelUnit'] =
    Blockly.FtcJava['bno055imuParameters_enum_accelUnit'];

Blockly.Blocks['bno055imuParameters_enum_accelerationIntegrationAlgorithm'] = {
  init: function() {
    var ACCELERATION_INTEGRATION_ALGORITHM_CHOICES = [
        ['NAIVE', 'NAIVE'],
        ['JUST_LOGGING', 'JUST_LOGGING'],
    ];
    this.setOutput(true, 'BNO055IMU.AccelerationIntegrator');
    this.appendDummyInput()
        .appendField(createNonEditableField('AccelerationIntegrationAlgorithm'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(ACCELERATION_INTEGRATION_ALGORITHM_CHOICES),
            'ACCELERATION_INTEGRATION_ALGORITHM');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['NAIVE', 'Specifies an acceleration integration algorithm that just does the basic ' +
            'physics.'],
        ['JUST_LOGGING', 'Specifies an acceleration integration algorithm that doesn\'t actually ' +
            'integrate accelerations, but merely reports them in the logcat log.'],
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

Blockly.FtcJava['bno055imuParameters_enum_accelerationIntegrationAlgorithm'] = function(block) {
  var algorithm = block.getFieldValue('ACCELERATION_INTEGRATION_ALGORITHM');
  var code;
  switch (algorithm) {
    case 'NAIVE':
      code = 'null';
      return [code, Blockly.FtcJava.ORDER_ATOMIC];
    case 'JUST_LOGGING':
      code = 'new JustLoggingAccelerationIntegrator()';
      Blockly.FtcJava.generateImport_('JustLoggingAccelerationIntegrator');
      return [code, Blockly.FtcJava.ORDER_NEW];
    default:
      throw 'Unexpected AccelerationIntegrationAlgorithm ' + algorithm + ' (bno055imuParameters_enum_accelerationIntegrationAlgorithm).';
  }
};

Blockly.Blocks['bno055imuParameters_typedEnum_accelerationIntegrationAlgorithm'] =
    Blockly.Blocks['bno055imuParameters_enum_accelerationIntegrationAlgorithm'];

Blockly.JavaScript['bno055imuParameters_typedEnum_accelerationIntegrationAlgorithm'] =
    Blockly.JavaScript['bno055imuParameters_enum_accelerationIntegrationAlgorithm'];

Blockly.FtcJava['bno055imuParameters_typedEnum_accelerationIntegrationAlgorithm'] =
    Blockly.FtcJava['bno055imuParameters_enum_accelerationIntegrationAlgorithm'];

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
    this.setOutput(true, 'BNO055IMU.SensorMode');
    this.appendDummyInput()
        .appendField(createNonEditableField('SensorMode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(SENSOR_MODE_CHOICES),
            'SENSOR_MODE');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['ACCONLY', 'The IMU-BNO055 SensorMode value ACCONLY'],
        ['MAGONLY', 'The IMU-BNO055 SensorMode value MAGONLY'],
        ['GYRONLY', 'The IMU-BNO055 SensorMode value GYRONLY'],
        ['ACCMAG', 'The IMU-BNO055 SensorMode value ACCMAG'],
        ['ACCGYRO', 'The IMU-BNO055 SensorMode value ACCGYRO'],
        ['MAGGYRO', 'The IMU-BNO055 SensorMode value MAGGYRO'],
        ['AMG', 'The IMU-BNO055 SensorMode value AMG'],
        ['IMU', 'The IMU-BNO055 SensorMode value IMU'],
        ['COMPASS', 'The IMU-BNO055 SensorMode value COMPASS'],
        ['M4G', 'The IMU-BNO055 SensorMode value M4G'],
        ['NDOF_FMC_OFF', 'The IMU-BNO055 SensorMode value NDOF_FMC_OFF'],
        ['NDOF', 'The IMU-BNO055 SensorMode value NDOF'],
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

Blockly.FtcJava['bno055imuParameters_enum_sensorMode'] = function(block) {
  var code = 'BNO055IMU.SensorMode.' + block.getFieldValue('SENSOR_MODE');
  Blockly.FtcJava.generateImport_('BNO055IMU');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['bno055imuParameters_typedEnum_sensorMode'] =
    Blockly.Blocks['bno055imuParameters_enum_sensorMode'];

Blockly.JavaScript['bno055imuParameters_typedEnum_sensorMode'] =
    Blockly.JavaScript['bno055imuParameters_enum_sensorMode'];

Blockly.FtcJava['bno055imuParameters_typedEnum_sensorMode'] =
    Blockly.FtcJava['bno055imuParameters_enum_sensorMode'];
