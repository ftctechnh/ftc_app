/**
 * @fileoverview FTC robot blocks related to AngularVelocity.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// angularVelocityIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor

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
        ['AngleUnit', 'Returns the angular unit in which angular rates are expressed.'],
        ['XRotationRate', 'Returns the XRotationRate numeric value of the given AngularVelocity object.'],
        ['YRotationRate', 'Returns the YRotationRate numeric value of the given AngularVelocity object.'],
        ['ZRotationRate', 'Returns the ZRotationRate numeric value of the given AngularVelocity object.'],
        ['AcquisitionTime', 'Returns the AcquisitionTime of the given AngularVelocity object.'],
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
  var code = angularVelocityIdentifierForJavaScript + '.get' + property + '(' + angularVelocity + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['angularVelocity_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var angularVelocity = Blockly.FtcJava.valueToCode(
      block, 'ANGULAR_VELOCITY', Blockly.FtcJava.ORDER_MEMBER);
  switch (property) {
    case 'AngleUnit':
      property = 'unit';
      break;
    case 'XRotationRate':
    case 'YRotationRate':
    case 'ZRotationRate':
    case 'AcquisitionTime':
      property = Blockly.FtcJava.makeFirstLetterLowerCase_(property);
      break;
    default:
      throw 'Unexpected property ' + property + ' (angularVelocity_getProperty).';
  }
  var code = angularVelocity + '.' + property;
  return [code, Blockly.FtcJava.ORDER_MEMBER];
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
        ['AngleUnit', 'Returns the angular unit in which angular rates are expressed.'],
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

Blockly.FtcJava['angularVelocity_getProperty_AngleUnit'] =
    Blockly.FtcJava['angularVelocity_getProperty'];

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
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['XRotationRate', 'Returns the XRotationRate numeric value of the given AngularVelocity object.'],
        ['YRotationRate', 'Returns the YRotationRate numeric value of the given AngularVelocity object.'],
        ['ZRotationRate', 'Returns the ZRotationRate numeric value of the given AngularVelocity object.'],
        ['AcquisitionTime', 'Returns the AcquisitionTime of the given AngularVelocity object.'],
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
        case 'XRotationRate':
        case 'YRotationRate':
        case 'ZRotationRate':
          return 'float';
        case 'AcquisitionTime':
          return 'long';
        default:
          throw 'Unexpected property ' + property + ' (angularVelocity_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['angularVelocity_getProperty_Number'] =
    Blockly.JavaScript['angularVelocity_getProperty'];

Blockly.FtcJava['angularVelocity_getProperty_Number'] =
    Blockly.FtcJava['angularVelocity_getProperty'];

// Functions

Blockly.Blocks['angularVelocity_create'] = {
  init: function() {
    this.setOutput(true, 'AngularVelocity');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('AngularVelocity'));
    this.setColour(functionColor);
    this.setTooltip('Creates a new AngularVelocity object.');
  }
};

Blockly.JavaScript['angularVelocity_create'] = function(block) {
  var code = angularVelocityIdentifierForJavaScript + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['angularVelocity_create'] = function(block) {
  var code = 'new AngularVelocity()';
  Blockly.FtcJava.generateImport_('AngularVelocity');
  return [code, Blockly.FtcJava.ORDER_NEW];
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
        .appendField('xRotationRate')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Y_ROTATION_RATE').setCheck('Number')
        .appendField('yRotationRate')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Z_ROTATION_RATE').setCheck('Number')
        .appendField('zRotationRate')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ACQUISITION_TIME').setCheck('Number')
        .appendField('acquisitionTime')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Creates a new AngularVelocity object.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'X_ROTATION_RATE':
        case 'Y_ROTATION_RATE':
        case 'Z_ROTATION_RATE':
          return 'float';
        case 'ACQUISITION_TIME':
          return 'long';
      }
      return '';
    };
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
  var code = angularVelocityIdentifierForJavaScript + '.create_withArgs(' +
      angleUnit + ', ' + xRotationRate + ', ' + yRotationRate + ', ' +
      zRotationRate + ', ' + acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['angularVelocity_create_withArgs'] = function(block) {
  var angleUnit = Blockly.FtcJava.valueToCode(
      block, 'ANGLE_UNIT', Blockly.FtcJava.ORDER_COMMA);
  var xRotationRate = Blockly.FtcJava.valueToCode(
      block, 'X_ROTATION_RATE', Blockly.FtcJava.ORDER_COMMA);
  var yRotationRate = Blockly.FtcJava.valueToCode(
      block, 'Y_ROTATION_RATE', Blockly.FtcJava.ORDER_COMMA);
  var zRotationRate = Blockly.FtcJava.valueToCode(
      block, 'Z_ROTATION_RATE', Blockly.FtcJava.ORDER_COMMA);
  var acquisitionTime = Blockly.FtcJava.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.FtcJava.ORDER_COMMA);
  var code = 'new AngularVelocity(' + angleUnit + ', ' + xRotationRate + ', ' +
      yRotationRate + ', ' + zRotationRate + ', ' + acquisitionTime + ')';
  Blockly.FtcJava.generateImport_('AngularVelocity');
  return [code, Blockly.FtcJava.ORDER_NEW];
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
  var code = angularVelocityIdentifierForJavaScript + '.toAngleUnit(' + angularVelocity + ', ' + angleUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['angularVelocity_toAngleUnit'] = function(block) {
  var angularVelocity = Blockly.FtcJava.valueToCode(
      block, 'ANGULAR_VELOCITY', Blockly.FtcJava.ORDER_MEMBER);
  var angleUnit = Blockly.FtcJava.valueToCode(
      block, 'ANGLE_UNIT', Blockly.FtcJava.ORDER_NONE);
  var code = angularVelocity + '.toAngleUnit(' +  angleUnit + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
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
    this.getFtcJavaOutputType = function() {
      return 'double';
    };
  }
};

Blockly.JavaScript['angularVelocity_getRotationRate'] = function(block) {
  var angularVelocity = Blockly.JavaScript.valueToCode(
      block, 'ANGULAR_VELOCITY', Blockly.JavaScript.ORDER_COMMA);
  var axis = Blockly.JavaScript.valueToCode(
      block, 'AXIS', Blockly.JavaScript.ORDER_COMMA);
  var code = angularVelocityIdentifierForJavaScript + '.getRotationRate(' + angularVelocity + ', ' + axis + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['angularVelocity_getRotationRate'] = function(block) {
  var angularVelocity = Blockly.FtcJava.valueToCode(
      block, 'ANGULAR_VELOCITY', Blockly.FtcJava.ORDER_COMMA);
  var axis = Blockly.FtcJava.valueToCode(
      block, 'AXIS', Blockly.FtcJava.ORDER_COMMA);
  var property;
  switch (axis) {
    case 'Axis.X':
    case 'X':
      property = 'xRotationRate';
      break;
    case 'Axis.Y':
    case 'Y':
      property = 'yRotationRate';
      break;
    case 'Axis.Z':
    case 'Z':
      property = 'zRotationRate';
      break;
    default:
      throw 'Unexpected axis ' + axis + ' (angularVelocity_getRotationRate).';
  }
  var code = angularVelocity + '.' + property;
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};
