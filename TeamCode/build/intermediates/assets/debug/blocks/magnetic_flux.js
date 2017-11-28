/**
 * @fileoverview FTC robot blocks related to MagneticFlux.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// magneticFluxIdentifier
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['magneticFlux_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['X', 'X'],
        ['Y', 'Y'],
        ['Z', 'Z'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('MagneticFlux'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('MAGNETIC_FLUX').setCheck('MagneticFlux')
        .appendField('magneticFlux')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['X', 'The X of the given MagneticFlux object.'],
        ['Y', 'The Y of the given MagneticFlux object.'],
        ['Z', 'The Z of the given MagneticFlux object.'],
        ['AcquisitionTime', 'The AcquisitionTime of the given MagneticFlux object.'],
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

Blockly.JavaScript['magneticFlux_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var magneticFlux = Blockly.JavaScript.valueToCode(
      block, 'MAGNETIC_FLUX', Blockly.JavaScript.ORDER_NONE);
  var code = magneticFluxIdentifier + '.get' + property + '(' + magneticFlux + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['magneticFlux_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['X', 'X'],
        ['Y', 'Y'],
        ['Z', 'Z'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('MagneticFlux'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('MAGNETIC_FLUX').setCheck('MagneticFlux')
        .appendField('magneticFlux')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['X', 'The X of the given MagneticFlux object.'],
        ['Y', 'The Y of the given MagneticFlux object.'],
        ['Z', 'The Z of the given MagneticFlux object.'],
        ['AcquisitionTime', 'The AcquisitionTime of the given MagneticFlux object.'],
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

Blockly.JavaScript['magneticFlux_getProperty_Number'] =
    Blockly.JavaScript['magneticFlux_getProperty'];


// Functions

Blockly.Blocks['magneticFlux_create'] = {
  init: function() {
    this.setOutput(true, 'MagneticFlux');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('MagneticFlux'));
    this.setColour(functionColor);
    this.setTooltip('Create a new MagneticFlux object.');
  }
};

Blockly.JavaScript['magneticFlux_create'] = function(block) {
  var code = magneticFluxIdentifier + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['magneticFlux_create_withArgs'] = {
  init: function() {
    this.setOutput(true, 'MagneticFlux');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('MagneticFlux'));
    this.appendValueInput('X').setCheck('Number')
        .appendField('x')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Y').setCheck('Number')
        .appendField('y')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Z').setCheck('Number')
        .appendField('z')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ACQUISITION_TIME').setCheck('Number')
        .appendField('acquisitionTime')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new MagneticFlux object.');
  }
};

Blockly.JavaScript['magneticFlux_create_withArgs'] = function(block) {
  var x = Blockly.JavaScript.valueToCode(
      block, 'X', Blockly.JavaScript.ORDER_COMMA);
  var y = Blockly.JavaScript.valueToCode(
      block, 'Y', Blockly.JavaScript.ORDER_COMMA);
  var z = Blockly.JavaScript.valueToCode(
      block, 'Z', Blockly.JavaScript.ORDER_COMMA);
  var acquisitionTime = Blockly.JavaScript.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.JavaScript.ORDER_COMMA);
  var code = magneticFluxIdentifier + '.create_withArgs(' + x + ', ' + y + ', ' + z + ', ' +
      acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['magneticFlux_toText'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MagneticFlux'))
        .appendField('.')
        .appendField(createNonEditableField('toText'));
    this.appendValueInput('MAGNETIC_FLUX').setCheck('MagneticFlux')
        .appendField('magneticFlux')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a text representation of the given magneticFlux.');
  }
};

Blockly.JavaScript['magneticFlux_toText'] = function(block) {
  var magneticFlux = Blockly.JavaScript.valueToCode(
      block, 'MAGNETIC_FLUX', Blockly.JavaScript.ORDER_NONE);
  var code = magneticFluxIdentifier + '.toText(' + magneticFlux + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
