/**
 * @fileoverview FTC robot blocks related to MagneticFlux.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// magneticFluxIdentifierForJavaScript
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
        ['X', 'Returns the X of the given MagneticFlux object.'],
        ['Y', 'Returns the Y of the given MagneticFlux object.'],
        ['Z', 'Returns the Z of the given MagneticFlux object.'],
        ['AcquisitionTime', 'Returns the AcquisitionTime of the given MagneticFlux object.'],
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
  var code = magneticFluxIdentifierForJavaScript + '.get' + property + '(' + magneticFlux + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['magneticFlux_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var magneticFlux = Blockly.FtcJava.valueToCode(
      block, 'MAGNETIC_FLUX', Blockly.FtcJava.ORDER_MEMBER);
  var code = magneticFlux + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property);
  return [code, Blockly.FtcJava.ORDER_MEMBER];
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
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['X', 'Returns the X of the given MagneticFlux object.'],
        ['Y', 'Returns the Y of the given MagneticFlux object.'],
        ['Z', 'Returns the Z of the given MagneticFlux object.'],
        ['AcquisitionTime', 'Returns the AcquisitionTime of the given MagneticFlux object.'],
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
        case 'X':
        case 'Y':
        case 'Z':
          return 'double';
        case 'AcquisitionTime':
          return 'long';
        default:
          throw 'Unexpected property ' + property + ' (magneticFlux_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['magneticFlux_getProperty_Number'] =
    Blockly.JavaScript['magneticFlux_getProperty'];

Blockly.FtcJava['magneticFlux_getProperty_Number'] =
    Blockly.FtcJava['magneticFlux_getProperty'];


// Functions

Blockly.Blocks['magneticFlux_create'] = {
  init: function() {
    this.setOutput(true, 'MagneticFlux');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('MagneticFlux'));
    this.setColour(functionColor);
    this.setTooltip('Creates a new MagneticFlux object.');
  }
};

Blockly.JavaScript['magneticFlux_create'] = function(block) {
  var code = magneticFluxIdentifierForJavaScript + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['magneticFlux_create'] = function(block) {
  var code = 'new MagneticFlux()';
  Blockly.FtcJava.generateImport_('MagneticFlux');
  return [code, Blockly.FtcJava.ORDER_NEW];
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
    this.setTooltip('Creates a new MagneticFlux object.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'X':
        case 'Y':
        case 'Z':
          return 'double';
        case 'ACQUISITION_TIME':
          return 'long';
      }
      return '';
    };
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
  var code = magneticFluxIdentifierForJavaScript + '.create_withArgs(' + x + ', ' + y + ', ' + z + ', ' +
      acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['magneticFlux_create_withArgs'] = function(block) {
  var x = Blockly.FtcJava.valueToCode(
      block, 'X', Blockly.FtcJava.ORDER_COMMA);
  var y = Blockly.FtcJava.valueToCode(
      block, 'Y', Blockly.FtcJava.ORDER_COMMA);
  var z = Blockly.FtcJava.valueToCode(
      block, 'Z', Blockly.FtcJava.ORDER_COMMA);
  var acquisitionTime = Blockly.FtcJava.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.FtcJava.ORDER_COMMA);
  var code = 'new MagneticFlux(' + x + ', ' + y + ', ' + z + ', ' + acquisitionTime + ')';
  Blockly.FtcJava.generateImport_('MagneticFlux');
  return [code, Blockly.FtcJava.ORDER_NEW];
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
    this.setTooltip('Returns a text representation of the given MagneticFlux object.');
  }
};

Blockly.JavaScript['magneticFlux_toText'] = function(block) {
  var magneticFlux = Blockly.JavaScript.valueToCode(
      block, 'MAGNETIC_FLUX', Blockly.JavaScript.ORDER_NONE);
  var code = magneticFluxIdentifierForJavaScript + '.toText(' + magneticFlux + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['magneticFlux_toText'] = function(block) {
  var magneticFlux = Blockly.FtcJava.valueToCode(
      block, 'MAGNETIC_FLUX', Blockly.FtcJava.ORDER_MEMBER);
  var code = magneticFlux + '.toString()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
