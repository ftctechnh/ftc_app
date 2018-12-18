/**
 * @fileoverview FTC robot blocks related to Acceleration.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// accelerationIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor

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
        ['DistanceUnit', 'Returns the DistanceUnit of the given Acceleration object.'],
        ['XAccel', 'Returns the XAccel of the given Acceleration object.'],
        ['YAccel', 'Returns the YAccel of the given Acceleration object.'],
        ['ZAccel', 'Returns the ZAccel of the given Acceleration object.'],
        ['AcquisitionTime', 'Returns the AcquisitionTime of the given Acceleration object.'],
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
  var code = accelerationIdentifierForJavaScript + '.get' + property + '(' + acceleration + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['acceleration_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  switch (property) {
    case 'DistanceUnit':
      property = 'unit';
      break;
    case 'XAccel':
    case 'YAccel':
    case 'ZAccel':
    case 'AcquisitionTime':
      property = Blockly.FtcJava.makeFirstLetterLowerCase_(property);
      break;
    default:
      throw 'Unexpected property ' + property + ' (acceleration_getProperty).';
  }
  var acceleration = Blockly.FtcJava.valueToCode(
      block, 'ACCELERATION', Blockly.FtcJava.ORDER_MEMBER);
  var code = acceleration + '.' + property;
  return [code, Blockly.FtcJava.ORDER_MEMBER];
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
        ['DistanceUnit', 'Returns the DistanceUnit of the given Acceleration object.'],
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

Blockly.FtcJava['acceleration_getProperty_DistanceUnit'] =
    Blockly.FtcJava['acceleration_getProperty'];

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
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['XAccel', 'Returns the XAccel of the given Acceleration object.'],
        ['YAccel', 'Returns the YAccel of the given Acceleration object.'],
        ['ZAccel', 'Returns the ZAccel of the given Acceleration object.'],
        ['AcquisitionTime', 'Returns the AcquisitionTime of the given Acceleration object.'],
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
        case 'XAccel':
        case 'YAccel':
        case 'ZAccel':
          return 'double';
        case 'AcquisitionTime':
          return 'long';
        default:
          throw 'Unexpected property ' + property + ' (acceleration_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['acceleration_getProperty_Number'] =
    Blockly.JavaScript['acceleration_getProperty'];

Blockly.FtcJava['acceleration_getProperty_Number'] =
    Blockly.FtcJava['acceleration_getProperty'];

// Functions

Blockly.Blocks['acceleration_create'] = {
  init: function() {
    this.setOutput(true, 'Acceleration');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Acceleration'));
    this.setColour(functionColor);
    this.setTooltip('Creates a new Acceleration object.');
  }
};

Blockly.JavaScript['acceleration_create'] = function(block) {
  var code = accelerationIdentifierForJavaScript + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['acceleration_create'] = function(block) {
  var code = 'new Acceleration()';
  Blockly.FtcJava.generateImport_('Acceleration');
  return [code, Blockly.FtcJava.ORDER_NEW];
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
    this.setTooltip('Creates a new Acceleration object.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'X_ACCEL':
        case 'Y_ACCEL':
        case 'Z_ACCEL':
          return 'double';
        case 'ACQUISITION_TIME':
          return 'long';
      }
      return '';
    };
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
  var code = accelerationIdentifierForJavaScript + '.create_withArgs(' + distanceUnit + ', ' + xAccel + ', ' +
      yAccel + ', ' + zAccel + ', ' + acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['acceleration_create_withArgs'] = function(block) {
  var distanceUnit = Blockly.FtcJava.valueToCode(
      block, 'DISTANCE_UNIT', Blockly.FtcJava.ORDER_COMMA);
  var xAccel = Blockly.FtcJava.valueToCode(
      block, 'X_ACCEL', Blockly.FtcJava.ORDER_COMMA);
  var yAccel = Blockly.FtcJava.valueToCode(
      block, 'Y_ACCEL', Blockly.FtcJava.ORDER_COMMA);
  var zAccel = Blockly.FtcJava.valueToCode(
      block, 'Z_ACCEL', Blockly.FtcJava.ORDER_COMMA);
  var acquisitionTime = Blockly.FtcJava.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.FtcJava.ORDER_COMMA);
  var code = 'new Acceleration(' + distanceUnit + ', ' + xAccel + ', ' + yAccel + ', ' + zAccel +
      ', ' + acquisitionTime + ')';
  Blockly.FtcJava.generateImport_('Acceleration');
  return [code, Blockly.FtcJava.ORDER_NEW];
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
    this.setTooltip('Returns a new Acceleration object created from measures in units of ' +
        'earth\'s gravity rather than explicit distance units.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'GX':
        case 'GY':
        case 'GZ':
          return 'double';
        case 'ACQUISITION_TIME':
          return 'long';
      }
      return '';
    };
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
  var code = accelerationIdentifierForJavaScript + '.fromGravity(' + gx + ', ' + gy + ', ' + gz + ', ' +
      acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['acceleration_fromGravity'] = function(block) {
  var gx = Blockly.FtcJava.valueToCode(
      block, 'GX', Blockly.FtcJava.ORDER_COMMA);
  var gy = Blockly.FtcJava.valueToCode(
      block, 'GY', Blockly.FtcJava.ORDER_COMMA);
  var gz = Blockly.FtcJava.valueToCode(
      block, 'GZ', Blockly.FtcJava.ORDER_COMMA);
  var acquisitionTime = Blockly.FtcJava.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.FtcJava.ORDER_COMMA);
  var code = 'Acceleration.fromGravity(' + gx + ', ' + gy + ', ' + gz + ', ' + acquisitionTime + ')';
  Blockly.FtcJava.generateImport_('Acceleration');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
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
  var code = accelerationIdentifierForJavaScript + '.toDistanceUnit(' + acceleration + ', ' + distanceUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['acceleration_toDistanceUnit'] = function(block) {
  var acceleration = Blockly.FtcJava.valueToCode(
      block, 'ACCELERATION', Blockly.FtcJava.ORDER_MEMBER);
  var distanceUnit = Blockly.FtcJava.valueToCode(
      block, 'DISTANCE_UNIT', Blockly.FtcJava.ORDER_NONE);
  var code = acceleration + '.toUnit(' + distanceUnit + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
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
  var code = accelerationIdentifierForJavaScript + '.toText(' + acceleration + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['acceleration_toText'] = function(block) {
  var acceleration = Blockly.FtcJava.valueToCode(
      block, 'ACCELERATION', Blockly.FtcJava.ORDER_MEMBER);
  var code = acceleration + '.toString()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
