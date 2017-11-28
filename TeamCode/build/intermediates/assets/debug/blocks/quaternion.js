/**
 * @fileoverview FTC robot blocks related to Quaternion.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// quaternionIdentifier
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['quaternion_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['W', 'W'],
        ['X', 'X'],
        ['Y', 'Y'],
        ['Z', 'Z'],
        ['AcquisitionTime', 'AcquisitionTime'],
        ['Magnitude', 'Magnitude'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('Quaternion'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('QUATERNION').setCheck('Quaternion')
        .appendField('quaternion')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['W', 'The W of the given Quaternion object.'],
        ['X', 'The X of the given Quaternion object.'],
        ['Y', 'The Y of the given Quaternion object.'],
        ['Z', 'The Z of the given Quaternion object.'],
        ['AcquisitionTime', 'The AcquisitionTime of the given Quaternion object.'],
        ['Magnitude', 'Returns the magnitude of the given Quaternion object.'],
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

Blockly.JavaScript['quaternion_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var quaternion = Blockly.JavaScript.valueToCode(
      block, 'QUATERNION', Blockly.JavaScript.ORDER_NONE);
  var code = quaternionIdentifier + '.get' + property + '(' + quaternion + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
}

Blockly.Blocks['quaternion_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['W', 'W'],
        ['X', 'X'],
        ['Y', 'Y'],
        ['Z', 'Z'],
        ['AcquisitionTime', 'AcquisitionTime'],
        ['Magnitude', 'Magnitude'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('Quaternion'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('QUATERNION').setCheck('Quaternion')
        .appendField('quaternion')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['W', 'The W of the given Quaternion object.'],
        ['X', 'The X of the given Quaternion object.'],
        ['Y', 'The Y of the given Quaternion object.'],
        ['Z', 'The Z of the given Quaternion object.'],
        ['AcquisitionTime', 'The AcquisitionTime of the given Quaternion object.'],
        ['Magnitude', 'Returns the magnitude of the given Quaternion object.'],
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

Blockly.JavaScript['quaternion_getProperty_Number'] =
    Blockly.JavaScript['quaternion_getProperty'];

  // Functions

Blockly.Blocks['quaternion_create'] = {
  init: function() {
    this.setOutput(true, 'Quaternion');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Quaternion'));
    this.setColour(functionColor);
    this.setTooltip('Create a new Quaternion object.');
  }
};

Blockly.JavaScript['quaternion_create'] = function(block) {
  var code = quaternionIdentifier + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['quaternion_create_withArgs'] = {
  init: function() {
    this.setOutput(true, 'Quaternion');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Quaternion'));
    this.appendValueInput('W').setCheck('Number')
        .appendField('w')
        .setAlign(Blockly.ALIGN_RIGHT);
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
    this.setTooltip('Create a new Quaternion object.');
  }
};

Blockly.JavaScript['quaternion_create_withArgs'] = function(block) {
  var w = Blockly.JavaScript.valueToCode(
      block, 'W', Blockly.JavaScript.ORDER_COMMA);
  var x = Blockly.JavaScript.valueToCode(
      block, 'X', Blockly.JavaScript.ORDER_COMMA);
  var y = Blockly.JavaScript.valueToCode(
      block, 'Y', Blockly.JavaScript.ORDER_COMMA);
  var z = Blockly.JavaScript.valueToCode(
      block, 'Z', Blockly.JavaScript.ORDER_COMMA);
  var acquisitionTime = Blockly.JavaScript.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.JavaScript.ORDER_COMMA);
  var code = quaternionIdentifier + '.create_withArgs(' + w + ', ' + x + ', ' + y + ', ' + z +
      ', ' + acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['quaternion_normalized'] = {
  init: function() {
    this.setOutput(true, 'Quaternion');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Quaternion'))
        .appendField('.')
        .appendField(createNonEditableField('normalized'));
    this.appendValueInput('QUATERNION').setCheck('Quaternion')
        .appendField('quaternion')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new Quaternion that is normalized from the given Quaternion object.');
  }
};

Blockly.JavaScript['quaternion_normalized'] = function(block) {
  var quaternion = Blockly.JavaScript.valueToCode(
      block, 'QUATERNION', Blockly.JavaScript.ORDER_NONE);
  var code = quaternionIdentifier + '.normalized(' + quaternion + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['quaternion_congugate'] = {
  init: function() {
    this.setOutput(true, 'Quaternion');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Quaternion'))
        .appendField('.')
        .appendField(createNonEditableField('congugate'));
    this.appendValueInput('QUATERNION').setCheck('Quaternion')
        .appendField('quaternion')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new Quaternion that is congugate from the given Quaternion object.');
  }
};

Blockly.JavaScript['quaternion_congugate'] = function(block) {
  var quaternion = Blockly.JavaScript.valueToCode(
      block, 'QUATERNION', Blockly.JavaScript.ORDER_NONE);
  var code = quaternionIdentifier + '.congugate(' + quaternion + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
