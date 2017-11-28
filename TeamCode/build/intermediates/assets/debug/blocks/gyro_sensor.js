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
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['HeadingMode', 'The heading mode. ' +
            'Valid values are HeadingMode_CARTESIAN or HeadingMode_CARDINAL. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the gyro sensor. ' +
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['gyroSensor_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['gyroSensor_setProperty_HeadingMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['HeadingMode', 'HeadingMode'],
    ];
    this.appendValueInput('VALUE').setCheck('HeadingMode')
        .appendField('set')
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['HeadingMode', 'The heading mode. ' +
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['gyroSensor_setProperty_HeadingMode'] =
    Blockly.JavaScript['gyroSensor_setProperty'];

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
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'The 7 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the gyro sensor. ' +
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['gyroSensor_setProperty_Number'] =
    Blockly.JavaScript['gyroSensor_setProperty'];

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
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Heading', 'The integrated Z axis as a cartesian or cardinal heading, ' +
            'as a numeric value between 0 and 360. ' +
            'Not all gyro sensors support this feature.'],
        ['HeadingMode', 'The heading mode. ' +
            'Value is either HeadingMode_CARTESIAN or HeadingMode_CARDINAL. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
        ['RawX', 'The gyro sensor\'s raw X value. ' +
            'Not all gyro sensors support this feature.'],
        ['RawY', 'The gyro sensor\'s raw Y value. ' +
            'Not all gyro sensors support this feature.'],
        ['RawZ', 'The gyro sensor\'s raw Z value. ' +
            'Not all gyro sensors support this feature.'],
        ['RotationFraction', 'The current fractional rotation of this gyro. ' +
            'The value is between 0.0 and 1.0. ' +
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['gyroSensor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['gyroSensor_getProperty_HeadingMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['HeadingMode', 'HeadingMode'],
    ];
    this.setOutput(true, 'HeadingMode');
    this.appendDummyInput()
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['HeadingMode', 'The heading mode. ' +
            'Value is either HeadingMode_CARTESIAN or HeadingMode_CARDINAL. ' +
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['gyroSensor_getProperty_HeadingMode'] =
    Blockly.JavaScript['gyroSensor_getProperty'];

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
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Heading', 'The integrated Z axis as a cartesian or cardinal heading, ' +
            'as a numeric value between 0 and 360. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the gyro sensor. ' +
            'Not all gyro sensors support this feature.'],
        ['IntegratedZValue', 'The gyro sensor\'s integrated Z value. ' +
            'Not all gyro sensors support this feature.'],
        ['RawX', 'The gyro sensor\'s raw X value. ' +
            'Not all gyro sensors support this feature.'],
        ['RawY', 'The gyro sensor\'s raw Y value. ' +
            'Not all gyro sensors support this feature.'],
        ['RawZ', 'The gyro sensor\'s raw Z value. ' +
            'Not all gyro sensors support this feature.'],
        ['RotationFraction', 'The current fractional rotation of this gyro. ' +
            'The value is between 0.0 and 1.0. ' +
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['gyroSensor_getProperty_Number'] =
    Blockly.JavaScript['gyroSensor_getProperty'];

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
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngularOrientationAxes', 'Returns a list of the axes on which the gyroscope measures ' +
            'angular orientation. Not all gyro sensors support this feature.'],
        ['AngularVelocityAxes', 'Returns a list of the axes on which the gyroscope measures ' +
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['gyroSensor_getProperty_Array'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = 'JSON.parse(' + identifier + '.get' + property + '())';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['gyroSensor_enum_headingMode'] = function(block) {
  var code = '"' + block.getFieldValue('HEADING_MODE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['gyroSensor_typedEnum_headingMode'] = {
  init: function() {
    var HEADING_MODE_CHOICES = [
        ['HEADING_CARTESIAN', 'HEADING_CARTESIAN'],
        ['HEADING_CARDINAL', 'HEADING_CARDINAL'],
    ];
    this.setOutput(true, 'HeadingMode');
    this.appendDummyInput()
        .appendField(createNonEditableField('HeadingMode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(HEADING_MODE_CHOICES), 'HEADING_MODE');
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['gyroSensor_typedEnum_headingMode'] =
    Blockly.JavaScript['gyroSensor_enum_headingMode'];

// TODO(lizlooney): Should we provide constants for MIN_I2C_ADDRESS_7_BIT, MAX_I2C_ADDRESS_7_BIT,
// MIN_I2C_ADDRESS_8_BIT, and MAX_I2C_ADDRESS_8_BIT? If so, where do they belong?

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

Blockly.Blocks['gyroSensor_isCalibrating'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createGyroSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isCalibrating'));
    this.setColour(functionColor);
    this.setTooltip('Is the gyro performing a calibration operation? ' +
        'Not all gyro sensors support this feature.');
  }
};

Blockly.JavaScript['gyroSensor_isCalibrating'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isCalibrating()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
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
    this.setTooltip('Returns the angular rotation rate across all the axes measured by the gyro. ' +
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
    this.setTooltip('Returns the absolute orientation of the sensor as a set three angles. ' +
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
