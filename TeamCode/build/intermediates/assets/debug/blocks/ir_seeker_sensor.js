/**
 * @fileoverview FTC robot blocks related to IR seeker sensor.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createIrSeekerSensorDropdown
// The following are defined in vars.js:
// getPropertyColor
// setPropertyColor

Blockly.Blocks['irSeekerSensor_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['SignalDetectedThreshold', 'SignalDetectedThreshold'],
        ['Mode', 'Mode'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['SignalDetectedThreshold',
            'The minimum threshold for a signal to be considered detected.'],
        ['Mode', 'The device mode: 600Hz or 1200Hz.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
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

Blockly.JavaScript['irSeekerSensor_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['irSeekerSensor_setProperty_IrSeekerSensorMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.appendValueInput('VALUE').setCheck('IrSeekerSensorMode')
        .appendField('set')
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'The device mode: 600Hz or 1200Hz.'],
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

Blockly.JavaScript['irSeekerSensor_setProperty_IrSeekerSensorMode'] =
    Blockly.JavaScript['irSeekerSensor_setProperty'];

Blockly.Blocks['irSeekerSensor_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['SignalDetectedThreshold', 'SignalDetectedThreshold'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['SignalDetectedThreshold',
            'The minimum threshold for a signal to be considered detected.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
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

Blockly.JavaScript['irSeekerSensor_setProperty_Number'] =
    Blockly.JavaScript['irSeekerSensor_setProperty'];

Blockly.Blocks['irSeekerSensor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['SignalDetectedThreshold', 'SignalDetectedThreshold'],
        ['Mode', 'Mode'],
        ['IsSignalDetected', 'IsSignalDetected'],
        ['Angle', 'Angle'],
        ['Strength', 'Strength'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['SignalDetectedThreshold',
            'The minimum threshold for a signal to be considered detected.'],
        ['Mode', 'The device mode: 600Hz or 1200Hz.'],
        ['IsSignalDetected', 'Is an IR signal detected?'],
        ['Angle', 'Estimated angle in which the signal is coming from.'],
        ['Strength', 'Detected IR signal strength, on a scale of 0.0 to 1.0.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
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

Blockly.JavaScript['irSeekerSensor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['irSeekerSensor_getProperty_IrSeekerSensorMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.setOutput(true, 'IrSeekerSensorMode');
    this.appendDummyInput()
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'The device mode: 600Hz or 1200Hz.'],
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

Blockly.JavaScript['irSeekerSensor_getProperty_IrSeekerSensorMode'] =
    Blockly.JavaScript['irSeekerSensor_getProperty']

Blockly.Blocks['irSeekerSensor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['SignalDetectedThreshold', 'SignalDetectedThreshold'],
        ['Angle', 'Angle'],
        ['Strength', 'Strength'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['SignalDetectedThreshold',
            'The minimum threshold for a signal to be considered detected.'],
        ['Angle', 'Estimated angle in which the signal is coming from.'],
        ['Strength', 'Detected IR signal strength, on a scale of 0.0 to 1.0.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
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

Blockly.JavaScript['irSeekerSensor_getProperty_Number'] =
    Blockly.JavaScript['irSeekerSensor_getProperty']

Blockly.Blocks['irSeekerSensor_getProperty_Boolean'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['IsSignalDetected', 'IsSignalDetected'],
    ];
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['IsSignalDetected', 'Is an IR signal detected?'],
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

Blockly.JavaScript['irSeekerSensor_getProperty_Boolean'] =
    Blockly.JavaScript['irSeekerSensor_getProperty']

// Enums

Blockly.Blocks['irSeekerSensor_enum_mode'] = {
  init: function() {
    var MODE_CHOICES = [
        ['MODE_600HZ', 'MODE_600HZ'],
        ['MODE_1200HZ', 'MODE_1200HZ'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('Mode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(MODE_CHOICES), 'MODE');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MODE_600HZ', 'The Mode value MODE_600HZ.'],
        ['MODE_1200HZ', 'The Mode value MODE_1200HZ.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('MODE');
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

Blockly.JavaScript['irSeekerSensor_enum_mode'] = function(block) {
  var code = '"' + block.getFieldValue('MODE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['irSeekerSensor_typedEnum_mode'] = {
  init: function() {
    var MODE_CHOICES = [
        ['MODE_600HZ', 'MODE_600HZ'],
        ['MODE_1200HZ', 'MODE_1200HZ'],
    ];
    this.setOutput(true, 'IrSeekerSensorMode');
    this.appendDummyInput()
        .appendField(createNonEditableField('Mode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(MODE_CHOICES), 'MODE');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MODE_600HZ', 'The Mode value MODE_600HZ.'],
        ['MODE_1200HZ', 'The Mode value MODE_1200HZ.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('MODE');
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

Blockly.JavaScript['irSeekerSensor_typedEnum_mode'] =
    Blockly.JavaScript['irSeekerSensor_enum_mode'];

// TODO(lizlooney): Should we provide constants for MIN_I2C_ADDRESS_7_BIT, MAX_I2C_ADDRESS_7_BIT,
// MIN_I2C_ADDRESS_8_BIT, and MAX_I2C_ADDRESS_8_BIT? If so, where do they belong?
