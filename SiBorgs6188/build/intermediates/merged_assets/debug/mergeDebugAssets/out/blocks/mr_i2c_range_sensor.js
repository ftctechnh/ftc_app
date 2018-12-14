/**
 * @fileoverview FTC robot blocks related to ModernRoboticsI2cRangeSensor.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createMrI2cRangeSensorDropdown
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor
// setPropertyColor

Blockly.Blocks['mrI2cRangeSensor_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createMrI2cRangeSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'Sets the 7 bit I2C address of the range sensor.'],
        ['I2cAddress8Bit', 'Sets the 8 bit I2C address of the range sensor.'],
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

Blockly.JavaScript['mrI2cRangeSensor_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['mrI2cRangeSensor_setProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DistanceSensor');
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
      throw 'Unexpected property ' + property + ' (mrI2cRangeSensor_setProperty).';
  }
  return code;
};

Blockly.Blocks['mrI2cRangeSensor_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createMrI2cRangeSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'Sets the 7 bit I2C address of the range sensor.'],
        ['I2cAddress8Bit', 'Sets the 8 bit I2C address of the range sensor.'],
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
            throw 'Unexpected property ' + property + ' (mrI2cRangeSensor_setProperty_Number getArgumentType).';
        }
      }
      return '';
    };
  }
};

Blockly.JavaScript['mrI2cRangeSensor_setProperty_Number'] =
    Blockly.JavaScript['mrI2cRangeSensor_setProperty'];

Blockly.FtcJava['mrI2cRangeSensor_setProperty_Number'] =
    Blockly.FtcJava['mrI2cRangeSensor_setProperty'];

Blockly.Blocks['mrI2cRangeSensor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['CmOptical', 'CmOptical'],
        ['CmUltrasonic', 'CmUltrasonic'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
        ['LightDetected', 'LightDetected'],
        ['RawLightDetected', 'RawLightDetected'],
        ['RawLightDetectedMax', 'RawLightDetectedMax'],
        ['RawOptical', 'RawOptical'],
        ['RawUltrasonic', 'RawUltrasonic'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createMrI2cRangeSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CmOptical', 'Returns the distance in centimeters, according to the optical sensor.'],
        ['CmUltrasonic', 'Returns the distance in centimeters, according to the ultrasonic sensor.'],
        ['I2cAddress7Bit', 'Returns the 7 bit I2C address of the range sensor.'],
        ['I2cAddress8Bit', 'Returns the 8 bit I2C address of the range sensor.'],
        ['LightDetected', 'Returns a numeric value between 0.0 and 1.0 representing the amount ' +
            'of light detected by the range sensor.'],
        ['RawLightDetected', 'Returns a signal whose strength is proportional to the intensity ' +
            'of the light measured.'],
        ['RawLightDetectedMax', 'Returns the maximum value that can be returned for ' +
            'RawLightDetected.'],
        ['RawOptical', 'Returns the raw optical value.'],
        ['RawUltrasonic', 'Returns the raw ultrasonic value.'],
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

Blockly.JavaScript['mrI2cRangeSensor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['mrI2cRangeSensor_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DistanceSensor');
  var property = block.getFieldValue('PROP');
  var code;
  switch (property) {
    case 'CmOptical':
    case 'CmUltrasonic':
    case 'RawOptical':
    case 'RawUltrasonic':
      Blockly.FtcJava.generateImport_('ModernRoboticsI2cRangeSensor');
      code = '((ModernRoboticsI2cRangeSensor) ' + identifier + ').' +
          Blockly.FtcJava.makeFirstLetterLowerCase_(property) + '()';
      break;
    case 'I2cAddress7Bit':
      Blockly.FtcJava.generateImport_('I2cAddrConfig');
      code = '((I2cAddrConfig) ' + identifier + ').getI2cAddress().get7Bit()';
      break;
    case 'I2cAddress8Bit':
      Blockly.FtcJava.generateImport_('I2cAddrConfig');
      code = '((I2cAddrConfig) ' + identifier + ').getI2cAddress().get8Bit()';
      break;
    case 'LightDetected':
    case 'RawLightDetected':
    case 'RawLightDetectedMax':
      Blockly.FtcJava.generateImport_('OpticalDistanceSensor');
      code = '((OpticalDistanceSensor) ' + identifier + ').get' + property + '()';
      break;
    default:
      throw 'Unexpected property ' + property + ' (mrI2cRangeSensor_getProperty).';
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['mrI2cRangeSensor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['CmOptical', 'CmOptical'],
        ['CmUltrasonic', 'CmUltrasonic'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
        ['LightDetected', 'LightDetected'],
        ['RawLightDetected', 'RawLightDetected'],
        ['RawLightDetectedMax', 'RawLightDetectedMax'],
        ['RawOptical', 'RawOptical'],
        ['RawUltrasonic', 'RawUltrasonic'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createMrI2cRangeSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CmOptical', 'Returns the distance in centimeters, according to the optical sensor.'],
        ['CmUltrasonic', 'Returns the distance in centimeters, according to the ultrasonic sensor.'],
        ['I2cAddress7Bit', 'Returns the 7 bit I2C address of the range sensor.'],
        ['I2cAddress8Bit', 'Returns the 8 bit I2C address of the range sensor.'],
        ['LightDetected', 'Returns a numeric value between 0.0 and 1.0 representing the amount ' +
            'of light detected by the range sensor.'],
        ['RawLightDetected', 'Returns a signal whose strength is proportional to the intensity ' +
            'of the light measured.'],
        ['RawLightDetectedMax', 'Returns the maximum value that can be returned for ' +
            'RawLightDetected.'],
        ['RawOptical', 'Returns the raw optical value.'],
        ['RawUltrasonic', 'Returns the raw ultrasonic value.'],
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
        case 'CmOptical':
        case 'CmUltrasonic':
        case 'LightDetected':
        case 'RawLightDetected':
        case 'RawLightDetectedMax':
          return 'double';
        case 'RawOptical':
        case 'RawUltrasonic':
        case 'I2cAddress7Bit':
        case 'I2cAddress8Bit':
          return 'int';
        default:
          throw 'Unexpected property ' + property + ' (mrI2cRangeSensor_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['mrI2cRangeSensor_getProperty_Number'] =
    Blockly.JavaScript['mrI2cRangeSensor_getProperty'];

Blockly.FtcJava['mrI2cRangeSensor_getProperty_Number'] =
    Blockly.FtcJava['mrI2cRangeSensor_getProperty'];

// Enums

Blockly.Blocks['mrI2cRangeSensor_enum_distanceUnit'] = {
  init: function() {
    var DISTANCE_UNIT_CHOICES = [
        ['METER', 'METER'],
        ['CM', 'CM'],
        ['MM', 'MM'],
        ['INCH', 'INCH'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('DistanceUnit'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(DISTANCE_UNIT_CHOICES), 'DISTANCE_UNIT');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['METER', 'The DistanceUnit value METER.'],
        ['CM', 'The DistanceUnit value CM (centimeter).'],
        ['MM', 'The DistanceUnit value MM (millimeter).'],
        ['INCH', 'The DistanceUnit value INCH.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('DISTANCE_UNIT');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['mrI2cRangeSensor_enum_distanceUnit'] = function(block) {
  var code = '"' + block.getFieldValue('DISTANCE_UNIT') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['mrI2cRangeSensor_enum_distanceUnit'] = function(block) {
  var code = 'DistanceUnit.' + block.getFieldValue('DISTANCE_UNIT');
  Blockly.FtcJava.generateImport_('DistanceUnit');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['mrI2cRangeSensor_typedEnum_distanceUnit'] = {
  init: function() {
    var DISTANCE_UNIT_CHOICES = [
        ['METER', 'METER'],
        ['CM', 'CM'],
        ['MM', 'MM'],
        ['INCH', 'INCH'],
    ];
    this.setOutput(true, 'DistanceUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('DistanceUnit'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(DISTANCE_UNIT_CHOICES), 'DISTANCE_UNIT');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['METER', 'The DistanceUnit value METER.'],
        ['CM', 'The DistanceUnit value CM (centimeter).'],
        ['MM', 'The DistanceUnit value MM (millimeter).'],
        ['INCH', 'The DistanceUnit value INCH.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('DISTANCE_UNIT');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['mrI2cRangeSensor_typedEnum_distanceUnit'] =
    Blockly.JavaScript['mrI2cRangeSensor_enum_distanceUnit'];

Blockly.FtcJava['mrI2cRangeSensor_typedEnum_distanceUnit'] =
    Blockly.FtcJava['mrI2cRangeSensor_enum_distanceUnit'];

// Functions

Blockly.Blocks['mrI2cRangeSensor_getDistance'] = {
  init: function() {
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField('call')
        .appendField(createMrI2cRangeSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('getDistance'));
    this.appendValueInput('UNIT') // no type, for compatibility
        .appendField('unit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the current distance in the given distance units.');
  }
};

Blockly.JavaScript['mrI2cRangeSensor_getDistance'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var unit = Blockly.JavaScript.valueToCode(
      block, 'UNIT', Blockly.JavaScript.ORDER_NONE);
  var code = identifier + '.getDistance(' + unit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['mrI2cRangeSensor_getDistance'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DistanceSensor');
  var unit = Blockly.FtcJava.valueToCode(
      block, 'UNIT', Blockly.FtcJava.ORDER_NONE);
  var code = identifier + '.getDistance(' + unit + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['mrI2cRangeSensor_getDistance_Number'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createMrI2cRangeSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('getDistance'));
    this.appendValueInput('UNIT').setCheck('DistanceUnit')
        .appendField('unit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the current distance in the given distance units.');
    this.getFtcJavaOutputType = function() {
      return 'double';
    };
  }
};

Blockly.JavaScript['mrI2cRangeSensor_getDistance_Number'] =
    Blockly.JavaScript['mrI2cRangeSensor_getDistance'];

Blockly.FtcJava['mrI2cRangeSensor_getDistance_Number'] =
    Blockly.FtcJava['mrI2cRangeSensor_getDistance'];
