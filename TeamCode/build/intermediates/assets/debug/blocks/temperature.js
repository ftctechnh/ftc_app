/**
 * @fileoverview FTC robot blocks related to Temperature.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// temperatureIdentifier
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['temperature_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['TempUnit', 'TempUnit'],
        ['Temperature', 'Temperature'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('Temperature'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('TEMPERATURE').setCheck('Temperature')
        .appendField('temperature')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['TempUnit', 'The TempUnit of the given Temperature object.'],
        ['Temperature', 'The Temperature of the given Temperature object.'],
        ['AcquisitionTime', 'The AcquisitionTime of the given Temperature object.'],
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

Blockly.JavaScript['temperature_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var temperature = Blockly.JavaScript.valueToCode(
      block, 'TEMPERATURE', Blockly.JavaScript.ORDER_NONE);
  var code = temperatureIdentifier + '.get' + property + '(' + temperature + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['temperature_getProperty_TempUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['TempUnit', 'TempUnit'],
    ];
    this.setOutput(true, 'TempUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('Temperature'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('TEMPERATURE').setCheck('Temperature')
        .appendField('temperature')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['TempUnit', 'The TempUnit of the given Temperature object.'],
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

Blockly.JavaScript['temperature_getProperty_TempUnit'] =
    Blockly.JavaScript['temperature_getProperty'];

Blockly.Blocks['temperature_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Temperature', 'Temperature'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('Temperature'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('TEMPERATURE').setCheck('Temperature')
        .appendField('temperature')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Temperature', 'The Temperature of the given Temperature object.'],
        ['AcquisitionTime', 'The AcquisitionTime of the given Temperature object.'],
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

Blockly.JavaScript['temperature_getProperty_Number'] =
    Blockly.JavaScript['temperature_getProperty'];

// Functions

Blockly.Blocks['temperature_create'] = {
  init: function() {
    this.setOutput(true, 'Temperature');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Temperature'));
    this.setColour(functionColor);
    this.setTooltip('Create a new Temperature object.');
  }
};

Blockly.JavaScript['temperature_create'] = function(block) {
  var code = temperatureIdentifier + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['temperature_create_withArgs'] = {
  init: function() {
    this.setOutput(true, 'Temperature');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Temperature'));
    this.appendValueInput('TEMP_UNIT').setCheck('TempUnit')
        .appendField('tempUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('TEMPERATURE').setCheck('Number')
        .appendField('temperature')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ACQUISITION_TIME').setCheck('Number')
        .appendField('acquisitionTime')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new Temperature object.');
  }
};

Blockly.JavaScript['temperature_create_withArgs'] = function(block) {
  var tempUnit = Blockly.JavaScript.valueToCode(
      block, 'TEMP_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var temperature = Blockly.JavaScript.valueToCode(
      block, 'TEMPERATURE', Blockly.JavaScript.ORDER_COMMA);
  var acquisitionTime = Blockly.JavaScript.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.JavaScript.ORDER_COMMA);
  var code = temperatureIdentifier + '.create_withArgs(' + tempUnit + ', ' + temperature + ', ' +
      acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['temperature_toTempUnit'] = {
  init: function() {
    this.setOutput(true, 'Temperature');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Temperature'))
        .appendField('.')
        .appendField(createNonEditableField('toTempUnit'));
    this.appendValueInput('TEMPERATURE').setCheck('Temperature')
        .appendField('temperature')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('TEMP_UNIT').setCheck('TempUnit')
        .appendField('tempUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new Temperature object created from the given Temperature ' +
        'object and temp unit.');
  }
};

Blockly.JavaScript['temperature_toTempUnit'] = function(block) {
  var temperature = Blockly.JavaScript.valueToCode(
      block, 'TEMPERATURE', Blockly.JavaScript.ORDER_COMMA);
  var tempUnit = Blockly.JavaScript.valueToCode(
      block, 'TEMP_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var code = temperatureIdentifier + '.toTempUnit(' + temperature + ', ' + tempUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
