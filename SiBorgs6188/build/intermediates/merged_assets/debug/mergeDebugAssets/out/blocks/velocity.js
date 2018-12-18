/**
 * @fileoverview FTC robot blocks related to Velocity.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// velocityIdentifierForJavaScript
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
        ['DistanceUnit', 'Returns the DistanceUnit of the given Velocity object.'],
        ['XVeloc', 'Returns the XVeloc numeric value of the given Velocity object.'],
        ['YVeloc', 'Returns the YVeloc numeric value of the given Velocity object.'],
        ['ZVeloc', 'Returns the ZVeloc numeric value of the given Velocity object.'],
        ['AcquisitionTime', 'Returns the AcquisitionTime of the given Velocity object.'],
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
  var code = velocityIdentifierForJavaScript + '.get' + property + '(' + velocity + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['velocity_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var velocity = Blockly.FtcJava.valueToCode(
      block, 'VELOCITY', Blockly.FtcJava.ORDER_MEMBER);
  var code;
  switch (property) {
    case 'DistanceUnit':
      code = velocity + '.unit';
      break;
    case 'XVeloc':
    case 'YVeloc':
    case 'ZVeloc':
    case 'AcquisitionTime':
      code = velocity + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property);
      break;
    default:
      throw 'Unexpected property ' + property + ' (temperature_getProperty).';
  }
  return [code, Blockly.FtcJava.ORDER_MEMBER];
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
        ['DistanceUnit', 'Returns the DistanceUnit of the given Velocity object.'],
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

Blockly.FtcJava['velocity_getProperty_DistanceUnit'] =
    Blockly.FtcJava['velocity_getProperty'];

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
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['XVeloc', 'Returns the XVeloc numeric value of the given Velocity object.'],
        ['YVeloc', 'Returns the YVeloc numeric value of the given Velocity object.'],
        ['ZVeloc', 'Returns the ZVeloc numeric value of the given Velocity object.'],
        ['AcquisitionTime', 'Returns the AcquisitionTime of the given Velocity object.'],
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
        case 'XVeloc':
        case 'YVeloc':
        case 'ZVeloc':
          return 'double';
        case 'AcquisitionTime':
          return 'long';
        default:
          throw 'Unexpected property ' + property + ' (velocity_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['velocity_getProperty_Number'] =
    Blockly.JavaScript['velocity_getProperty'];

Blockly.FtcJava['velocity_getProperty_Number'] =
    Blockly.FtcJava['velocity_getProperty'];

// Functions

Blockly.Blocks['velocity_create'] = {
  init: function() {
    this.setOutput(true, 'Velocity');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Velocity'));
    this.setColour(functionColor);
    this.setTooltip('Creates a new Velocity object.');
  }
};

Blockly.JavaScript['velocity_create'] = function(block) {
  var code = velocityIdentifierForJavaScript + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['velocity_create'] = function(block) {
  var code = 'new Velocity()';
  Blockly.FtcJava.generateImport_('Velocity');
  return [code, Blockly.FtcJava.ORDER_NEW];
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
        .appendField('xVeloc')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Y_VELOC').setCheck('Number')
        .appendField('yVeloc')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('Z_VELOC').setCheck('Number')
        .appendField('zVeloc')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ACQUISITION_TIME').setCheck('Number')
        .appendField('acquisitionTime')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Creates a new Velocity object.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'X_VELOC':
        case 'Y_VELOC':
        case 'Z_VELOC':
          return 'double';
        case 'ACQUISITION_TIME':
          return 'long';
      }
      return '';
    };
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
  var code = velocityIdentifierForJavaScript + '.create_withArgs(' + distanceUnit + ', ' + xVeloc + ', ' +
      yVeloc + ', ' + zVeloc + ', ' + acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['velocity_create_withArgs'] = function(block) {
  var distanceUnit = Blockly.FtcJava.valueToCode(
      block, 'DISTANCE_UNIT', Blockly.FtcJava.ORDER_COMMA);
  var xVeloc = Blockly.FtcJava.valueToCode(
      block, 'X_VELOC', Blockly.FtcJava.ORDER_COMMA);
  var yVeloc = Blockly.FtcJava.valueToCode(
      block, 'Y_VELOC', Blockly.FtcJava.ORDER_COMMA);
  var zVeloc = Blockly.FtcJava.valueToCode(
      block, 'Z_VELOC', Blockly.FtcJava.ORDER_COMMA);
  var acquisitionTime = Blockly.FtcJava.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.FtcJava.ORDER_COMMA);
  var code = 'new Velocity(' + distanceUnit + ', ' + xVeloc + ', ' +
      yVeloc + ', ' + zVeloc + ', ' + acquisitionTime + ')';
  Blockly.FtcJava.generateImport_('Velocity');
  return [code, Blockly.FtcJava.ORDER_NEW];
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
  var code = velocityIdentifierForJavaScript + '.toDistanceUnit(' + velocity + ', ' + distanceUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['velocity_toDistanceUnit'] = function(block) {
  var velocity = Blockly.FtcJava.valueToCode(
      block, 'VELOCITY', Blockly.FtcJava.ORDER_MEMBER);
  var distanceUnit = Blockly.FtcJava.valueToCode(
      block, 'DISTANCE_UNIT', Blockly.FtcJava.ORDER_NONE);
  var code = velocity + '.toUnit(' + distanceUnit + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
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
  var code = velocityIdentifierForJavaScript + '.toText(' + velocity + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['velocity_toText'] = function(block) {
  var velocity = Blockly.FtcJava.valueToCode(
      block, 'VELOCITY', Blockly.FtcJava.ORDER_MEMBER);
  var code = velocity + '.toString()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
