/**
 * @fileoverview FTC robot blocks related to Acceleration.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// accelerationIdentifier
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

// TODO(lizlooney) Acceleration.earthGravity constant?

Blockly.Blocks['acceleration_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['DistanceUnit', 'DistanceUnit'],
        ['XAccel', 'XAccel'],
        ['YAccel', 'YAccel'],
        ['ZAccel', 'ZAccel'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('Acceleration'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ACCELERATION').setCheck('Acceleration')
        .appendField('acceleration')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['DistanceUnit', 'The DistanceUnit of the given Acceleration object.'],
        ['XAccel', 'The XAccel of the given Acceleration object.'],
        ['YAccel', 'The YAccel of the given Acceleration object.'],
        ['ZAccel', 'The ZAccel of the given Acceleration object.'],
        ['AcquisitionTime', 'The AcquisitionTime of the given Acceleration object.'],
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

Blockly.JavaScript['acceleration_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var acceleration = Blockly.JavaScript.valueToCode(
      block, 'ACCELERATION', Blockly.JavaScript.ORDER_NONE);
  var code = accelerationIdentifier + '.get' + property + '(' + acceleration + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['acceleration_getProperty_DistanceUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['DistanceUnit', 'DistanceUnit'],
    ];
    this.setOutput(true, 'DistanceUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('Acceleration'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ACCELERATION').setCheck('Acceleration')
        .appendField('acceleration')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['DistanceUnit', 'The DistanceUnit of the given Acceleration object.'],
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

Blockly.JavaScript['acceleration_getProperty_DistanceUnit'] =
    Blockly.JavaScript['acceleration_getProperty'];

Blockly.Blocks['acceleration_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['XAccel', 'XAccel'],
        ['YAccel', 'YAccel'],
        ['ZAccel', 'ZAccel'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('Acceleration'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ACCELERATION').setCheck('Acceleration')
        .appendField('acceleration')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['XAccel', 'The XAccel of the given Acceleration object.'],
        ['YAccel', 'The YAccel of the given Acceleration object.'],
        ['ZAccel', 'The ZAccel of the given Acceleration object.'],
        ['AcquisitionTime', 'The AcquisitionTime of the given Acceleration object.'],
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

Blockly.JavaScript['acceleration_getProperty_Number'] =
    Blockly.JavaScript['acceleration_getProperty'];

// Functions

Blockly.Blocks['acceleration_create'] = {
  init: function() {
    this.setOutput(true, 'Acceleration');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Acceleration'));
    this.setColour(functionColor);
    this.setTooltip('Create a new Acceleration object.');
  }
};

Blockly.JavaScript['acceleration_create'] = function(block) {
  var code = accelerationIdentifier + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['acceleration_create_withArgs'] = {
  init: function() {
    this.setOutput(true, 'Acceleration');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Acceleration'));
    this.appendValueInput('DISTANCE_UNIT').setCheck('DistanceUnit')
        .appendField('distanceUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('X_ACCEL').setCheck('Number')
        .appendField('xAccel')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Y_ACCEL').setCheck('Number')
        .appendField('yAccel')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Z_ACCEL').setCheck('Number')
        .appendField('zAccel')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ACQUISITION_TIME').setCheck('Number')
        .appendField('acquisitionTime')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new Acceleration object.');
  }
};

Blockly.JavaScript['acceleration_create_withArgs'] = function(block) {
  var distanceUnit = Blockly.JavaScript.valueToCode(
      block, 'DISTANCE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var xAccel = Blockly.JavaScript.valueToCode(
      block, 'X_ACCEL', Blockly.JavaScript.ORDER_COMMA);
  var yAccel = Blockly.JavaScript.valueToCode(
      block, 'Y_ACCEL', Blockly.JavaScript.ORDER_COMMA);
  var zAccel = Blockly.JavaScript.valueToCode(
      block, 'Z_ACCEL', Blockly.JavaScript.ORDER_COMMA);
  var acquisitionTime = Blockly.JavaScript.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.JavaScript.ORDER_COMMA);
  var code = accelerationIdentifier + '.create_withArgs(' + distanceUnit + ', ' + xAccel + ', ' +
      yAccel + ', ' + zAccel + ', ' + acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['acceleration_fromGravity'] = {
  init: function() {
    this.setOutput(true, 'Acceleration');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Acceleration'))
        .appendField('.')
        .appendField(createNonEditableField('fromGravity'));
    this.appendValueInput('GX').setCheck('Number')
        .appendField('gx')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('GY').setCheck('Number')
        .appendField('gy')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('GZ').setCheck('Number')
        .appendField('gz')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ACQUISITION_TIME').setCheck('Number')
        .appendField('acquisitionTime')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new Acceleration object constructed from measures in units of ' +
        'earth\'s gravity rather than explicit distance units.');
  }
};

Blockly.JavaScript['acceleration_fromGravity'] = function(block) {
  var gx = Blockly.JavaScript.valueToCode(
      block, 'GX', Blockly.JavaScript.ORDER_COMMA);
  var gy = Blockly.JavaScript.valueToCode(
      block, 'GY', Blockly.JavaScript.ORDER_COMMA);
  var gz = Blockly.JavaScript.valueToCode(
      block, 'GZ', Blockly.JavaScript.ORDER_COMMA);
  var acquisitionTime = Blockly.JavaScript.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.JavaScript.ORDER_COMMA);
  var code = accelerationIdentifier + '.fromGravity(' + gx + ', ' + gy + ', ' + gz + ', ' +
      acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['acceleration_toDistanceUnit'] = {
  init: function() {
    this.setOutput(true, 'Acceleration');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Acceleration'))
        .appendField('.')
        .appendField(createNonEditableField('toDistanceUnit'));
    this.appendValueInput('ACCELERATION').setCheck('Acceleration')
        .appendField('acceleration')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DISTANCE_UNIT').setCheck('DistanceUnit')
        .appendField('distanceUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new Acceleration object created from the given Acceleration ' +
        'object and distance unit.');
  }
};

Blockly.JavaScript['acceleration_toDistanceUnit'] = function(block) {
  var acceleration = Blockly.JavaScript.valueToCode(
      block, 'ACCELERATION', Blockly.JavaScript.ORDER_COMMA);
  var distanceUnit = Blockly.JavaScript.valueToCode(
      block, 'DISTANCE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var code = accelerationIdentifier + '.toDistanceUnit(' + acceleration + ', ' + distanceUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['acceleration_toText'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Acceleration'))
        .appendField('.')
        .appendField(createNonEditableField('toText'));
    this.appendValueInput('ACCELERATION').setCheck('Acceleration')
        .appendField('acceleration')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a text representation of the given Acceleration object.');
  }
};

Blockly.JavaScript['acceleration_toText'] = function(block) {
  var acceleration = Blockly.JavaScript.valueToCode(
      block, 'ACCELERATION', Blockly.JavaScript.ORDER_NONE);
  var code = accelerationIdentifier + '.toText(' + acceleration + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

