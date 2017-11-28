/**
 * @fileoverview FTC robot blocks related to Velocity.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// velocityIdentifier
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['velocity_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['DistanceUnit', 'DistanceUnit'],
        ['XVeloc', 'XVeloc'],
        ['YVeloc', 'YVeloc'],
        ['ZVeloc', 'ZVeloc'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('Velocity'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VELOCITY').setCheck('Velocity')
        .appendField('velocity')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['DistanceUnit', 'The DistanceUnit of the given Velocity object.'],
        ['XVeloc', 'The XVeloc of the given Velocity object.'],
        ['YVeloc', 'The YVeloc of the given Velocity object.'],
        ['ZVeloc', 'The ZVeloc of the given Velocity object.'],
        ['AcquisitionTime', 'The AcquisitionTime of the given Velocity object.'],
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

Blockly.JavaScript['velocity_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var velocity = Blockly.JavaScript.valueToCode(
      block, 'VELOCITY', Blockly.JavaScript.ORDER_NONE);
  var code = velocityIdentifier + '.get' + property + '(' + velocity + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['velocity_getProperty_DistanceUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['DistanceUnit', 'DistanceUnit'],
    ];
    this.setOutput(true, 'DistanceUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('Velocity'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VELOCITY').setCheck('Velocity')
        .appendField('velocity')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['DistanceUnit', 'The DistanceUnit of the given Velocity object.'],
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

Blockly.JavaScript['velocity_getProperty_DistanceUnit'] =
    Blockly.JavaScript['velocity_getProperty'];

Blockly.Blocks['velocity_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['XVeloc', 'XVeloc'],
        ['YVeloc', 'YVeloc'],
        ['ZVeloc', 'ZVeloc'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('Velocity'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VELOCITY').setCheck('Velocity')
        .appendField('velocity')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['XVeloc', 'The XVeloc of the given Velocity object.'],
        ['YVeloc', 'The YVeloc of the given Velocity object.'],
        ['ZVeloc', 'The ZVeloc of the given Velocity object.'],
        ['AcquisitionTime', 'The AcquisitionTime of the given Velocity object.'],
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

Blockly.JavaScript['velocity_getProperty_Number'] =
    Blockly.JavaScript['velocity_getProperty'];

// Functions

Blockly.Blocks['velocity_create'] = {
  init: function() {
    this.setOutput(true, 'Velocity');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Velocity'));
    this.setColour(functionColor);
    this.setTooltip('Create a new Velocity object.');
  }
};

Blockly.JavaScript['velocity_create'] = function(block) {
  var code = velocityIdentifier + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['velocity_create_withArgs'] = {
  init: function() {
    this.setOutput(true, 'Velocity');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Velocity'));
    this.appendValueInput('DISTANCE_UNIT').setCheck('DistanceUnit')
        .appendField('distanceUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('X_VELOC').setCheck('Number')
        .appendField('XVeloc')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Y_VELOC').setCheck('Number')
        .appendField('YVeloc')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Z_VELOC').setCheck('Number')
        .appendField('ZVeloc')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ACQUISITION_TIME').setCheck('Number')
        .appendField('acquisitionTime')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new Velocity object.');
  }
};

Blockly.JavaScript['velocity_create_withArgs'] = function(block) {
  var distanceUnit = Blockly.JavaScript.valueToCode(
      block, 'DISTANCE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var xVeloc = Blockly.JavaScript.valueToCode(
      block, 'X_VELOC', Blockly.JavaScript.ORDER_COMMA);
  var yVeloc = Blockly.JavaScript.valueToCode(
      block, 'Y_VELOC', Blockly.JavaScript.ORDER_COMMA);
  var zVeloc = Blockly.JavaScript.valueToCode(
      block, 'Z_VELOC', Blockly.JavaScript.ORDER_COMMA);
  var acquisitionTime = Blockly.JavaScript.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.JavaScript.ORDER_COMMA);
  var code = velocityIdentifier + '.create_withArgs(' + distanceUnit + ', ' + xVeloc + ', ' +
      yVeloc + ', ' + zVeloc + ', ' + acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['velocity_toDistanceUnit'] = {
  init: function() {
    this.setOutput(true, 'Velocity');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Velocity'))
        .appendField('.')
        .appendField(createNonEditableField('toDistanceUnit'));
    this.appendValueInput('VELOCITY').setCheck('Velocity')
        .appendField('velocity')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DISTANCE_UNIT').setCheck('DistanceUnit')
        .appendField('distanceUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new Velocity object created from the given Velocity ' +
        'object and distance unit.');
  }
};

Blockly.JavaScript['velocity_toDistanceUnit'] = function(block) {
  var velocity = Blockly.JavaScript.valueToCode(
      block, 'VELOCITY', Blockly.JavaScript.ORDER_COMMA);
  var distanceUnit = Blockly.JavaScript.valueToCode(
      block, 'DISTANCE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var code = velocityIdentifier + '.toDistanceUnit(' + velocity + ', ' + distanceUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['velocity_toText'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Velocity'))
        .appendField('.')
        .appendField(createNonEditableField('toText'));
    this.appendValueInput('VELOCITY').setCheck('Velocity')
        .appendField('velocity')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a text representation of the given Velocity object.');
  }
};

Blockly.JavaScript['velocity_toText'] = function(block) {
  var velocity = Blockly.JavaScript.valueToCode(
      block, 'VELOCITY', Blockly.JavaScript.ORDER_NONE);
  var code = velocityIdentifier + '.toText(' + velocity + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
