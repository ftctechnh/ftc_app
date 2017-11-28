/**
 * @fileoverview FTC robot blocks related to color sensor.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createColorSensorDropdown
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor
// setPropertyColor

/**
 * Deprecated. See colorSensor_setProperty_Number.
 */
Blockly.Blocks['colorSensor_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createColorSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'The 7 bit I2C address of the color sensor. ' +
            'Not all color sensors support this feature.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the color sensor. ' +
            'Not all color sensors support this feature.'],
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

Blockly.JavaScript['colorSensor_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['colorSensor_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createColorSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['I2cAddress7Bit', 'The 7 bit I2C address of the color sensor. ' +
            'Not all color sensors support this feature.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the color sensor. ' +
            'Not all color sensors support this feature.'],
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

Blockly.JavaScript['colorSensor_setProperty_Number'] =
    Blockly.JavaScript['colorSensor_setProperty'];

/**
 * Deprecated. See colorSensor_getProperty_Number.
 */
Blockly.Blocks['colorSensor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Alpha', 'Alpha'],
        ['Argb', 'Arbg'],
        ['Blue', 'Blue'],
        ['Green', 'Green'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
        ['Red', 'Red'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createColorSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Alpha', 'The amount of light detected by the sensor.'],
        ['Argb', 'The color detected by the sensor, as an ARGB value.'],
        ['Blue', 'The amount of blue detected by the sensor.'],
        ['Green', 'The amount of green detected by the sensor.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address of the color sensor. ' +
            'Not all color sensors support this feature.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the color sensor. ' +
            'Not all color sensors support this feature.'],
        ['Red', 'The amount of red detected by the sensor.'],
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

Blockly.JavaScript['colorSensor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['colorSensor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Alpha', 'Alpha'],
        ['Argb', 'Arbg'],
        ['Blue', 'Blue'],
        ['Green', 'Green'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
        ['Red', 'Red'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createColorSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Alpha', 'The amount of light detected by the sensor.'],
        ['Argb', 'The color detected by the sensor, as an ARGB value.'],
        ['Blue', 'The amount of blue detected by the sensor.'],
        ['Green', 'The amount of green detected by the sensor.'],
        ['I2cAddress7Bit', 'The 7 bit I2C address of the color sensor. ' +
            'Not all color sensors support this feature.'],
        ['I2cAddress8Bit', 'The 8 bit I2C address of the color sensor. ' +
            'Not all color sensors support this feature.'],
        ['Red', 'The amount of red detected by the sensor.'],
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

Blockly.JavaScript['colorSensor_getProperty_Number'] =
    Blockly.JavaScript['colorSensor_getProperty'];


// TODO(lizlooney): Should we provide constants for MIN_I2C_ADDRESS_7_BIT, MAX_I2C_ADDRESS_7_BIT,
// MIN_I2C_ADDRESS_8_BIT, and MAX_I2C_ADDRESS_8_BIT? If so, where do they belong?

// Functions

/*
 * Deprecated. See colorSensor_enableLed_boolean
 */
Blockly.Blocks['colorSensor_enableLed'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createColorSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('enableLed'));
    this.appendValueInput('ENABLE') // no type, for compatibility
        .appendField('enable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Enable the LED light. Not all color sensors support this feature.');
  }
};

Blockly.JavaScript['colorSensor_enableLed'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var enable = Blockly.JavaScript.valueToCode(
      block, 'ENABLE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.enableLed(' + enable + ');\n';
};

Blockly.Blocks['colorSensor_enableLed_Boolean'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createColorSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('enableLed'));
    this.appendValueInput('ENABLE').setCheck('Boolean')
        .appendField('enable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Enable the LED light. Not all color sensors support this feature.');
  }
};

Blockly.JavaScript['colorSensor_enableLed_Boolean'] =
    Blockly.JavaScript['colorSensor_enableLed'];

Blockly.Blocks['colorSensor_enableLight'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createColorSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('enableLight'));
    this.appendValueInput('ENABLE').setCheck('Boolean')
        .appendField('enable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Enable the light. Not all color sensors support this feature.');
  }
};

Blockly.JavaScript['colorSensor_enableLight'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var enable = Blockly.JavaScript.valueToCode(
      block, 'ENABLE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.enableLight(' + enable + ');\n';
};


Blockly.Blocks['colorSensor_isLightOn'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createColorSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isLightOn'));
    this.setColour(functionColor);
    this.setTooltip('Returns true if the light is on.');
  }
};

Blockly.JavaScript['colorSensor_isLightOn'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isLightOn()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['colorSensor_toText'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createColorSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('toText'));
    this.setColour(functionColor);
    this.setTooltip('Returns a text representation of the color detected by the sensor.');
  }
};

Blockly.JavaScript['colorSensor_toText'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.toText()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['colorSensor_getNormalizedColors'] = {
  init: function() {
    this.setOutput(true, 'NormalizedColors');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createColorSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('getNormalizedColors'));
    this.setColour(functionColor);
    this.setTooltip('Returns the color detected by the sensor.');
  }
};

Blockly.JavaScript['colorSensor_getNormalizedColors'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = 'JSON.parse(' + identifier + '.getNormalizedColors())';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
