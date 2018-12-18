/**
 * @fileoverview FTC robot blocks related to Position.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// positionIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['position_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['DistanceUnit', 'DistanceUnit'],
        ['X', 'X'],
        ['Y', 'Y'],
        ['Z', 'Z'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('Position'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('POSITION').setCheck('Position')
        .appendField('position')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['DistanceUnit', 'Returns the DistanceUnit of the given Position object.'],
        ['X', 'Returns the X of the given Position object.'],
        ['Y', 'Returns the Y of the given Position object.'],
        ['Z', 'Returns the Z of the given Position object.'],
        ['AcquisitionTime', 'Returns the AcquisitionTime of the given Position object.'],
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

Blockly.JavaScript['position_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var position = Blockly.JavaScript.valueToCode(
      block, 'POSITION', Blockly.JavaScript.ORDER_NONE);
  var code = positionIdentifierForJavaScript + '.get' + property + '(' + position + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['position_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var position = Blockly.FtcJava.valueToCode(
      block, 'POSITION', Blockly.FtcJava.ORDER_MEMBER);
  var code;
  switch (property) {
    case 'DistanceUnit':
      code = position + '.unit';
      break;
    case 'X':
    case 'Y':
    case 'Z':
    case 'AcquisitionTime':
      code = position + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property);
      break;
    default:
      throw 'Unexpected property ' + property + ' (position_getProperty).';
  }
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['position_getProperty_DistanceUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['DistanceUnit', 'DistanceUnit'],
    ];
    this.setOutput(true, 'DistanceUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('Position'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('POSITION').setCheck('Position')
        .appendField('position')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['DistanceUnit', 'Returns the DistanceUnit of the given Position object.'],
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

Blockly.JavaScript['position_getProperty_DistanceUnit'] =
    Blockly.JavaScript['position_getProperty'];

Blockly.FtcJava['position_getProperty_DistanceUnit'] =
    Blockly.FtcJava['position_getProperty'];

Blockly.Blocks['position_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['X', 'X'],
        ['Y', 'Y'],
        ['Z', 'Z'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('Position'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('POSITION').setCheck('Position')
        .appendField('position')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['X', 'Returns the X of the given Position object.'],
        ['Y', 'Returns the Y of the given Position object.'],
        ['Z', 'Returns the Z of the given Position object.'],
        ['AcquisitionTime', 'Returns the AcquisitionTime of the given Position object.'],
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
          throw 'Unexpected property ' + property + ' (position_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['position_getProperty_Number'] =
    Blockly.JavaScript['position_getProperty'];

Blockly.FtcJava['position_getProperty_Number'] =
    Blockly.FtcJava['position_getProperty'];

// Functions

Blockly.Blocks['position_create'] = {
  init: function() {
    this.setOutput(true, 'Position');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Position'));
    this.setColour(functionColor);
    this.setTooltip('Creates a new Position object.');
  }
};

Blockly.JavaScript['position_create'] = function(block) {
  var code = positionIdentifierForJavaScript + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['position_create'] = function(block) {
  var code = 'new Position()';
  Blockly.FtcJava.generateImport_('Position');
  return [code, Blockly.FtcJava.ORDER_NEW];
};

Blockly.Blocks['position_create_withArgs'] = {
  init: function() {
    this.setOutput(true, 'Position');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Position'));
    this.appendValueInput('DISTANCE_UNIT').setCheck('DistanceUnit')
        .appendField('distanceUnit')
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
    this.setTooltip('Creates a new Position object.');
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

Blockly.JavaScript['position_create_withArgs'] = function(block) {
  var distanceUnit = Blockly.JavaScript.valueToCode(
      block, 'DISTANCE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var x = Blockly.JavaScript.valueToCode(
      block, 'X', Blockly.JavaScript.ORDER_COMMA);
  var y = Blockly.JavaScript.valueToCode(
      block, 'Y', Blockly.JavaScript.ORDER_COMMA);
  var z = Blockly.JavaScript.valueToCode(
      block, 'Z', Blockly.JavaScript.ORDER_COMMA);
  var acquisitionTime = Blockly.JavaScript.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.JavaScript.ORDER_COMMA);
  var code = positionIdentifierForJavaScript + '.create_withArgs(' + distanceUnit + ', ' + x + ', ' +
      y + ', ' + z + ', ' + acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['position_create_withArgs'] = function(block) {
  var distanceUnit = Blockly.FtcJava.valueToCode(
      block, 'DISTANCE_UNIT', Blockly.FtcJava.ORDER_COMMA);
  var x = Blockly.FtcJava.valueToCode(
      block, 'X', Blockly.FtcJava.ORDER_COMMA);
  var y = Blockly.FtcJava.valueToCode(
      block, 'Y', Blockly.FtcJava.ORDER_COMMA);
  var z = Blockly.FtcJava.valueToCode(
      block, 'Z', Blockly.FtcJava.ORDER_COMMA);
  var acquisitionTime = Blockly.FtcJava.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.FtcJava.ORDER_COMMA);
  var code = 'new Position(' + distanceUnit + ', ' + x + ', ' + y + ', ' + z + ', ' +
      acquisitionTime + ')';
  Blockly.FtcJava.generateImport_('Position');
  return [code, Blockly.FtcJava.ORDER_NEW];
};

Blockly.Blocks['position_toDistanceUnit'] = {
  init: function() {
    this.setOutput(true, 'Position');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Position'))
        .appendField('.')
        .appendField(createNonEditableField('toDistanceUnit'));
    this.appendValueInput('POSITION').setCheck('Position')
        .appendField('position')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DISTANCE_UNIT').setCheck('DistanceUnit')
        .appendField('distanceUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new Position object created from the given Position ' +
        'object and distance unit.');
  }
};

Blockly.JavaScript['position_toDistanceUnit'] = function(block) {
  var position = Blockly.JavaScript.valueToCode(
      block, 'POSITION', Blockly.JavaScript.ORDER_COMMA);
  var distanceUnit = Blockly.JavaScript.valueToCode(
      block, 'DISTANCE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var code = positionIdentifierForJavaScript + '.toDistanceUnit(' + position + ', ' + distanceUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['position_toDistanceUnit'] = function(block) {
  var position = Blockly.FtcJava.valueToCode(
      block, 'POSITION', Blockly.FtcJava.ORDER_MEMBER);
  var distanceUnit = Blockly.FtcJava.valueToCode(
      block, 'DISTANCE_UNIT', Blockly.FtcJava.ORDER_NONE);
  var code = position + '.toUnit(' + distanceUnit + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['position_toText'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Position'))
        .appendField('.')
        .appendField(createNonEditableField('toText'));
    this.appendValueInput('POSITION').setCheck('Position')
        .appendField('position')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a text representation of the given Position object.');
  }
};

Blockly.JavaScript['position_toText'] = function(block) {
  var position = Blockly.JavaScript.valueToCode(
      block, 'POSITION', Blockly.JavaScript.ORDER_NONE);
  var code = positionIdentifierForJavaScript + '.toText(' + position + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['position_toText'] = function(block) {
  var position = Blockly.FtcJava.valueToCode(
      block, 'POSITION', Blockly.FtcJava.ORDER_MEMBER);
  var code = position + '.toString()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
