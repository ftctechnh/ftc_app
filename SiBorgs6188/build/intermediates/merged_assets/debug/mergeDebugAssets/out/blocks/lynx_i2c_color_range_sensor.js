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
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'Sets the 7 bit I2C address of the REV color/range sensor.'],
        ['I2cAddress8Bit', 'Sets the 8 bit I2C address of the REV color/range sensor.'],
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
            throw 'Unexpected property ' + property + ' (lynxI2cColorRangeSensor_setProperty_Number getArgumentType).';
        }
      }
      return '';
    };
  }
};

Blockly.JavaScript['lynxI2cColorRangeSensor_setProperty_Number'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['lynxI2cColorRangeSensor_setProperty_Number'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'ColorSensor');
  var property = block.getFieldValue('PROP');
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_NONE);
  var code;
  switch (property) {
    case 'I2cAddress7Bit':
      Blockly.FtcJava.generateImport_('I2cAddr');
      code = identifier + '.setI2cAddress(I2cAddr.create7bit(' + value + '));\n';
      break;
    case 'I2cAddress8Bit':
      Blockly.FtcJava.generateImport_('I2cAddr');
      code = identifier + '.setI2cAddress(I2cAddr.create8bit(' + value + '));\n';
      break;
    default:
      throw 'Unexpected property ' + property + ' (lynxI2cColorRangeSensor_setProperty_Number).';
  }
  return code;
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
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Alpha', 'Returns the alpha component of the color detected by the REV color/range sensor.'],
        ['Argb', 'Returns an ARGB value representing the color detected by the REV color/range sensor.'],
        ['Blue', 'Returns the blue component of the color detected by the REV color/range sensor.'],
        ['Green', 'Returns the green component of the color detected by the REV color/range sensor.'],
        ['I2cAddress7Bit', 'Returns the 7 bit I2C address of the REV color/range sensor.'],
        ['I2cAddress8Bit', 'Returns the 8 bit I2C address of the REV color/range sensor.'],
        ['LightDetected', 'Returns a numeric value between 0.0 and 1.0 representing the amount ' +
            'of light detected by the REV color/range sensor.'],
        ['RawLightDetected', 'Returns a signal whose strength is proportional to the intensity ' +
            'of the light measured.'],
        ['RawLightDetectedMax', 'Returns the maximum value that can be returned for ' +
            'RawLightDetected.'],
        ['Red', 'Returns the red component of the color detected by the REV color/range sensor.'],
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
        case 'Alpha':
        case 'Argb':
        case 'Blue':
        case 'Green':
        case 'Red':
        case 'I2cAddress7Bit':
        case 'I2cAddress8Bit':
          return 'int';
        case 'LightDetected':
        case 'RawLightDetected':
        case 'RawLightDetectedMax':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (lynxI2cColorRangeSensor_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['lynxI2cColorRangeSensor_getProperty_Number'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['lynxI2cColorRangeSensor_getProperty_Number'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'ColorSensor');
  var property = block.getFieldValue('PROP');
  var code;
  switch (property) {
    case 'Alpha':
    case 'Argb':
    case 'Blue':
    case 'Green':
    case 'Red':
      code = identifier + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property) + '()';
      break;
    case 'I2cAddress7Bit':
      code = identifier + '.getI2cAddress().get7Bit()';
      break;
    case 'I2cAddress8Bit':
      code = identifier + '.getI2cAddress().get8Bit()';
      break;
    case 'LightDetected':
    case 'RawLightDetected':
    case 'RawLightDetectedMax':
      Blockly.FtcJava.generateImport_('OpticalDistanceSensor');
      code = '((OpticalDistanceSensor) ' + identifier + ').get' + property + '()';
      break;
    default:
      throw 'Unexpected property ' + property + ' (lynxI2cColorRangeSensor_getProperty_Number).';
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
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
    this.setColour(functionColor);
    this.setTooltip('Returns the current distance in the given distance units.');
    this.getFtcJavaOutputType = function() {
      return 'double';
    };
  }
};

Blockly.JavaScript['lynxI2cColorRangeSensor_getDistance_Number'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var unit = Blockly.JavaScript.valueToCode(
      block, 'UNIT', Blockly.JavaScript.ORDER_NONE);
  var code = identifier + '.getDistance(' + unit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['lynxI2cColorRangeSensor_getDistance_Number'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'ColorSensor');
  var unit = Blockly.FtcJava.valueToCode(
      block, 'UNIT', Blockly.FtcJava.ORDER_NONE);
  Blockly.FtcJava.generateImport_('DistanceSensor');
  var code = '((DistanceSensor) ' + identifier + ').getDistance(' + unit + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['lynxI2cColorRangeSensor_getNormalizedColors'] = {
  init: function() {
    this.setOutput(true, 'NormalizedRGBA');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createLynxI2cColorRangeSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('getNormalizedColors'));
    this.setColour(functionColor);
    this.setTooltip('Returns a NormalizedRGBA object representing the color detected by the REV color/range sensor.');
  }
};

Blockly.JavaScript['lynxI2cColorRangeSensor_getNormalizedColors'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = 'JSON.parse(' + identifier + '.getNormalizedColors())';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['lynxI2cColorRangeSensor_getNormalizedColors'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'ColorSensor');
  Blockly.FtcJava.generateImport_('NormalizedColorSensor');
  var code = '((NormalizedColorSensor) ' + identifier + ').getNormalizedColors()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
