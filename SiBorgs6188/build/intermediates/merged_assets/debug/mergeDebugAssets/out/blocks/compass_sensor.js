/**
 * @fileoverview FTC robot blocks related to compass sensor.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createCompassSensorDropdown
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor


/*
 * Deprecated. See compassSensor_getProperty_Number and compassSensor_getProperty_Boolean
 */
Blockly.Blocks['compassSensor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
        ['CalibrationFailed', 'CalibrationFailed'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Returns the current direction, in degrees.'],
        ['CalibrationFailed', 'Returns true if calibration failed.'],
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

Blockly.JavaScript['compassSensor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['compassSensor_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'CompassSensor');
  var property = block.getFieldValue('PROP');
  var code;
  switch (property) {
    case 'Direction':
      code = identifier + '.get' + property + '()';
      break;
    case 'CalibrationFailed':
      code = identifier + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property) + '()';
      break;
    default:
      throw 'Unexpected property ' + property + ' (compassSensor_getProperty).';
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['compassSensor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Returns the current direction, in degrees.'],
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
        case 'DIRECTION':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (compassSensor_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['compassSensor_getProperty_Number'] =
    Blockly.JavaScript['compassSensor_getProperty'];

Blockly.FtcJava['compassSensor_getProperty_Number'] =
    Blockly.FtcJava['compassSensor_getProperty'];


Blockly.Blocks['compassSensor_getProperty_Boolean'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['CalibrationFailed', 'CalibrationFailed'],
    ];
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField(createCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CalibrationFailed', 'Returns true if calibration failed.'],
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

Blockly.JavaScript['compassSensor_getProperty_Boolean'] =
    Blockly.JavaScript['compassSensor_getProperty'];

Blockly.FtcJava['compassSensor_getProperty_Boolean'] =
    Blockly.FtcJava['compassSensor_getProperty'];

// Enums

Blockly.Blocks['compassSensor_enum_compassMode'] = {
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

Blockly.JavaScript['compassSensor_enum_compassMode'] = function(block) {
  var code = '"' + block.getFieldValue('COMPASS_MODE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['compassSensor_enum_compassMode'] = function(block) {
  var code = 'CompassSensor.CompassMode.' + block.getFieldValue('COMPASS_MODE');
  Blockly.FtcJava.generateImport_('CompassSensor');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['compassSensor_typedEnum_compassMode'] = {
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

Blockly.JavaScript['compassSensor_typedEnum_compassMode'] =
    Blockly.JavaScript['compassSensor_enum_compassMode'];

Blockly.FtcJava['compassSensor_typedEnum_compassMode'] =
    Blockly.FtcJava['compassSensor_enum_compassMode'];

// Functions

Blockly.Blocks['compassSensor_setMode'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createCompassSensorDropdown(), 'IDENTIFIER')
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

Blockly.JavaScript['compassSensor_setMode'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var compassMode = Blockly.JavaScript.valueToCode(
      block, 'COMPASS_MODE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.setMode(' + compassMode + ');\n';
};

Blockly.FtcJava['compassSensor_setMode'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'CompassSensor');
  var compassMode = Blockly.FtcJava.valueToCode(
      block, 'COMPASS_MODE', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.setMode(' + compassMode + ');\n';
};

Blockly.Blocks['compassSensor_setMode_CompassMode'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createCompassSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('setMode'));
    this.appendValueInput('COMPASS_MODE').setCheck('CompassSensor.CompassMode')
        .appendField('compassMode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Change to calibration or measurement mode.');
  }
};

Blockly.JavaScript['compassSensor_setMode_CompassMode'] =
    Blockly.JavaScript['compassSensor_setMode'];

Blockly.FtcJava['compassSensor_setMode_CompassMode'] =
    Blockly.FtcJava['compassSensor_setMode'];
