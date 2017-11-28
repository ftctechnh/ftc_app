/**
 * @fileoverview FTC robot blocks related to AngularVelocity.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// angularVelocityIdentifier
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['angularVelocity_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngleUnit', 'AngleUnit'],
        ['XRotationRate', 'XRotationRate'],
        ['YRotationRate', 'YRotationRate'],
        ['ZRotationRate', 'ZRotationRate'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('AngularVelocity'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ANGULAR_VELOCITY').setCheck('AngularVelocity')
        .appendField('angularVelocity')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngleUnit', 'The angular unit in which angular rates are expressed.'],
        ['XRotationRate', 'The instantaneous body-referenced rotation rate about the x-axis.'],
        ['YRotationRate', 'The instantaneous body-referenced rotation rate about the y-axis.'],
        ['ZRotationRate', 'The instantaneous body-referenced rotation rate about the y-axis.'],
        ['AcquisitionTime', 'The time on the System.nanoTime() clock at which the data was acquired.'],
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

Blockly.JavaScript['angularVelocity_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var angularVelocity = Blockly.JavaScript.valueToCode(
      block, 'ANGULAR_VELOCITY', Blockly.JavaScript.ORDER_NONE);
  var code = angularVelocityIdentifier + '.get' + property + '(' + angularVelocity + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['angularVelocity_getProperty_AngleUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngleUnit', 'AngleUnit'],
    ];
    this.setOutput(true, 'AngleUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('AngularVelocity'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ANGULAR_VELOCITY').setCheck('AngularVelocity')
        .appendField('angularVelocity')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngleUnit', 'The angular unit in which angular rates are expressed.'],
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

Blockly.JavaScript['angularVelocity_getProperty_AngleUnit'] =
    Blockly.JavaScript['angularVelocity_getProperty'];

Blockly.Blocks['angularVelocity_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['XRotationRate', 'XRotationRate'],
        ['YRotationRate', 'YRotationRate'],
        ['ZRotationRate', 'ZRotationRate'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('AngularVelocity'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ANGULAR_VELOCITY').setCheck('AngularVelocity')
        .appendField('angularVelocity')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['XRotationRate', 'The instantaneous body-referenced rotation rate about the x-axis.'],
        ['YRotationRate', 'The instantaneous body-referenced rotation rate about the y-axis.'],
        ['ZRotationRate', 'The instantaneous body-referenced rotation rate about the y-axis.'],
        ['AcquisitionTime', 'The time on the System.nanoTime() clock at which the data was acquired.'],
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

Blockly.JavaScript['angularVelocity_getProperty_Number'] =
    Blockly.JavaScript['angularVelocity_getProperty'];

// Functions

Blockly.Blocks['angularVelocity_create'] = {
  init: function() {
    this.setOutput(true, 'AngularVelocity');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('AngularVelocity'));
    this.setColour(functionColor);
    this.setTooltip('Create a new AngularVelocity object.');
  }
};

Blockly.JavaScript['angularVelocity_create'] = function(block) {
  var code = angularVelocityIdentifier + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['angularVelocity_create_withArgs'] = {
  init: function() {
    this.setOutput(true, 'AngularVelocity');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('AngularVelocity'));
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('X_ROTATION_RATE').setCheck('Number')
        .appendField('XRotationRate')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Y_ROTATION_RATE').setCheck('Number')
        .appendField('YRotationRate')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Z_ROTATION_RATE').setCheck('Number')
        .appendField('ZRotationRate')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ACQUISITION_TIME').setCheck('Number')
        .appendField('acquisitionTime')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new AngularVelocity object.');
  }
};

Blockly.JavaScript['angularVelocity_create_withArgs'] = function(block) {
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var xRotationRate = Blockly.JavaScript.valueToCode(
      block, 'X_ROTATION_RATE', Blockly.JavaScript.ORDER_COMMA);
  var yRotationRate = Blockly.JavaScript.valueToCode(
      block, 'Y_ROTATION_RATE', Blockly.JavaScript.ORDER_COMMA);
  var zRotationRate = Blockly.JavaScript.valueToCode(
      block, 'Z_ROTATION_RATE', Blockly.JavaScript.ORDER_COMMA);
  var acquisitionTime = Blockly.JavaScript.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.JavaScript.ORDER_COMMA);
  var code = angularVelocityIdentifier + '.create_withArgs(' +
      angleUnit + ', ' + xRotationRate + ', ' + yRotationRate + ', ' +
      zRotationRate + ', ' + acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['angularVelocity_toAngleUnit'] = {
  init: function() {
    this.setOutput(true, 'AngularVelocity');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AngularVelocity'))
        .appendField('.')
        .appendField(createNonEditableField('toAngleUnit'));
    this.appendValueInput('ANGULAR_VELOCITY').setCheck('AngularVelocity')
        .appendField('angularVelocity')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new AngularVelocity object created from the given AngularVelocity ' +
        'object and angle unit.');
  }
};

Blockly.JavaScript['angularVelocity_toAngleUnit'] = function(block) {
  var angularVelocity = Blockly.JavaScript.valueToCode(
      block, 'ANGULAR_VELOCITY', Blockly.JavaScript.ORDER_COMMA);
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var code = angularVelocityIdentifier + '.toAngleUnit(' + angularVelocity + ', ' + angleUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['angularVelocity_getRotationRate'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AngularVelocity'))
        .appendField('.')
        .appendField(createNonEditableField('getRotationRate'));
    this.appendValueInput('ANGULAR_VELOCITY').setCheck('AngularVelocity')
        .appendField('angularVelocity')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('AXIS').setCheck('Axis')
        .appendField('axis')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the rotation rate from the given AngularVelocity object for the given Axis.');
  }
};

Blockly.JavaScript['angularVelocity_getRotationRate'] = function(block) {
  var angularVelocity = Blockly.JavaScript.valueToCode(
      block, 'ANGULAR_VELOCITY', Blockly.JavaScript.ORDER_COMMA);
  var axis = Blockly.JavaScript.valueToCode(
      block, 'AXIS', Blockly.JavaScript.ORDER_COMMA);
  var code = angularVelocityIdentifier + '.getRotationRate(' + angularVelocity + ', ' + axis + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

