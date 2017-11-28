/**
 * @fileoverview FTC robot blocks related to Orientation.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// orientationIdentifier
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['orientation_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AxesReference', 'AxesReference'],
        ['AxesOrder', 'AxesOrder'],
        ['AngleUnit', 'AngleUnit'],
        ['FirstAngle', 'FirstAngle'],
        ['SecondAngle', 'SecondAngle'],
        ['ThirdAngle', 'ThirdAngle'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('Orientation'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ORIENTATION').setCheck('Orientation')
        .appendField('orientation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AxesReference', 'The AxesReference of the given Orientation object.'],
        ['AxesOrder', 'The AxesOrder of the given Orientation object.'],
        ['AngleUnit', 'The AngleUnit of the given Orientation object.'],
        ['FirstAngle', 'The FirstAngle of the given Orientation object.'],
        ['SecondAngle', 'The SecondAngle of the given Orientation object.'],
        ['ThirdAngle', 'The ThirdAngle of the given Orientation object.'],
        ['AcquisitionTime', 'The AcquisitionTime of the given Orientation object.'],
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

Blockly.JavaScript['orientation_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var orientation = Blockly.JavaScript.valueToCode(
      block, 'ORIENTATION', Blockly.JavaScript.ORDER_NONE);
  var code = orientationIdentifier + '.get' + property + '(' + orientation + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['orientation_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['FirstAngle', 'FirstAngle'],
        ['SecondAngle', 'SecondAngle'],
        ['ThirdAngle', 'ThirdAngle'],
        ['AcquisitionTime', 'AcquisitionTime'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('Orientation'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ORIENTATION').setCheck('Orientation')
        .appendField('orientation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['FirstAngle', 'The FirstAngle of the given Orientation object.'],
        ['SecondAngle', 'The SecondAngle of the given Orientation object.'],
        ['ThirdAngle', 'The ThirdAngle of the given Orientation object.'],
        ['AcquisitionTime', 'The AcquisitionTime of the given Orientation object.'],
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

Blockly.JavaScript['orientation_getProperty_Number'] =
    Blockly.JavaScript['orientation_getProperty'];

Blockly.Blocks['orientation_getProperty_AxesReference'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AxesReference', 'AxesReference'],
    ];
    this.setOutput(true, 'AxesReference');
    this.appendDummyInput()
        .appendField(createNonEditableField('Orientation'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ORIENTATION').setCheck('Orientation')
        .appendField('orientation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AxesReference', 'The AxesReference of the given Orientation object.'],
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

Blockly.JavaScript['orientation_getProperty_AxesReference'] =
    Blockly.JavaScript['orientation_getProperty'];

Blockly.Blocks['orientation_getProperty_AxesOrder'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AxesOrder', 'AxesOrder'],
    ];
    this.setOutput(true, 'AxesOrder');
    this.appendDummyInput()
        .appendField(createNonEditableField('Orientation'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ORIENTATION').setCheck('Orientation')
        .appendField('orientation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AxesOrder', 'The AxesOrder of the given Orientation object.'],
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

Blockly.JavaScript['orientation_getProperty_AxesOrder'] =
    Blockly.JavaScript['orientation_getProperty'];

Blockly.Blocks['orientation_getProperty_AngleUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngleUnit', 'AngleUnit'],
    ];
    this.setOutput(true, 'AngleUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('Orientation'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ORIENTATION').setCheck('Orientation')
        .appendField('orientation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngleUnit', 'The AngleUnit of the given Orientation object.'],
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

Blockly.JavaScript['orientation_getProperty_AngleUnit'] =
    Blockly.JavaScript['orientation_getProperty'];

// Functions

Blockly.Blocks['orientation_create'] = {
  init: function() {
    this.setOutput(true, 'Orientation');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Orientation'));
    this.setColour(functionColor);
    this.setTooltip('Create a new Orientation object.');
  }
};

Blockly.JavaScript['orientation_create'] = function(block) {
  var code = orientationIdentifier + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['orientation_create_withArgs'] = {
  init: function() {
    this.setOutput(true, 'Orientation');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('Orientation'));
    this.appendValueInput('AXES_REFERENCE').setCheck('AxesReference')
        .appendField('axesReference')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('AXES_ORDER').setCheck('AxesOrder')
        .appendField('axesOrder')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('FIRST_ANGLE').setCheck('Number')
        .appendField('FirstAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SECOND_ANGLE').setCheck('Number')
        .appendField('SecondAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('THIRD_ANGLE').setCheck('Number')
        .appendField('ThirdAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ACQUISITION_TIME').setCheck('Number')
        .appendField('acquisitionTime')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new Orientation object.');
  }
};

Blockly.JavaScript['orientation_create_withArgs'] = function(block) {
  var axesReference = Blockly.JavaScript.valueToCode(
      block, 'AXES_REFERENCE', Blockly.JavaScript.ORDER_COMMA);
  var axesOrder = Blockly.JavaScript.valueToCode(
      block, 'AXES_ORDER', Blockly.JavaScript.ORDER_COMMA);
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var firstAngle = Blockly.JavaScript.valueToCode(
      block, 'FIRST_ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var secondAngle = Blockly.JavaScript.valueToCode(
      block, 'SECOND_ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var thirdAngle = Blockly.JavaScript.valueToCode(
      block, 'THIRD_ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var acquisitionTime = Blockly.JavaScript.valueToCode(
      block, 'ACQUISITION_TIME', Blockly.JavaScript.ORDER_COMMA);
  var code = orientationIdentifier + '.create_withArgs(' + axesReference + ', ' +
      axesOrder + ', ' + angleUnit + ', ' + firstAngle + ', ' + secondAngle + ', ' +
      thirdAngle + ', ' + acquisitionTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['orientation_toAngleUnit'] = {
  init: function() {
    this.setOutput(true, 'Orientation');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Orientation'))
        .appendField('.')
        .appendField(createNonEditableField('toAngleUnit'));
    this.appendValueInput('ORIENTATION').setCheck('Orientation')
        .appendField('orientation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new Orientation object created from the given Orientation ' +
        'object and angle unit.');
  }
};

Blockly.JavaScript['orientation_toAngleUnit'] = function(block) {
  var orientation = Blockly.JavaScript.valueToCode(
      block, 'ORIENTATION', Blockly.JavaScript.ORDER_COMMA);
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var code = orientationIdentifier + '.toAngleUnit(' + orientation + ', ' + angleUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['orientation_toAxesReference'] = {
  init: function() {
    this.setOutput(true, 'Orientation');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Orientation'))
        .appendField('.')
        .appendField(createNonEditableField('toAxesReference'));
    this.appendValueInput('ORIENTATION').setCheck('Orientation')
        .appendField('orientation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('AXES_REFERENCE').setCheck('AxesReference')
        .appendField('axesReference')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new Orientation object created from the given Orientation ' +
        'object and axes reference.');
  }
};

Blockly.JavaScript['orientation_toAxesReference'] = function(block) {
  var orientation = Blockly.JavaScript.valueToCode(
      block, 'ORIENTATION', Blockly.JavaScript.ORDER_COMMA);
  var axesReference = Blockly.JavaScript.valueToCode(
      block, 'AXES_REFERENCE', Blockly.JavaScript.ORDER_COMMA);
  var code = orientationIdentifier + '.toAxesReference(' + orientation + ', ' + axesReference + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['orientation_toAxesOrder'] = {
  init: function() {
    this.setOutput(true, 'Orientation');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Orientation'))
        .appendField('.')
        .appendField(createNonEditableField('toAxesOrder'));
    this.appendValueInput('ORIENTATION').setCheck('Orientation')
        .appendField('orientation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('AXES_ORDER').setCheck('AxesOrder')
        .appendField('axesOrder')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new Orientation object created from the given Orientation ' +
        'object and axes order.');
  }
};

Blockly.JavaScript['orientation_toAxesOrder'] = function(block) {
  var orientation = Blockly.JavaScript.valueToCode(
      block, 'ORIENTATION', Blockly.JavaScript.ORDER_COMMA);
  var axesOrder = Blockly.JavaScript.valueToCode(
      block, 'AXES_ORDER', Blockly.JavaScript.ORDER_COMMA);
  var code = orientationIdentifier + '.toAxesOrder(' + orientation + ', ' + axesOrder + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['orientation_toText'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Orientation'))
        .appendField('.')
        .appendField(createNonEditableField('toText'));
    this.appendValueInput('ORIENTATION').setCheck('Orientation')
        .appendField('orientation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a text representation of the given Orientation.');
  }
};

Blockly.JavaScript['orientation_toText'] = function(block) {
  var orientation = Blockly.JavaScript.valueToCode(
      block, 'ORIENTATION', Blockly.JavaScript.ORDER_NONE);
  var code = orientationIdentifier + '.toText(' + orientation + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['orientation_getRotationMatrix'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Orientation'))
        .appendField('.')
        .appendField(createNonEditableField('getRotationMatrix'));
    this.appendValueInput('ORIENTATION').setCheck('Orientation')
        .appendField('orientation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the rotation Matrix associated with the given Orientation object.');
  }
};

Blockly.JavaScript['orientation_getRotationMatrix'] = function(block) {
  var orientation = Blockly.JavaScript.valueToCode(
      block, 'ORIENTATION', Blockly.JavaScript.ORDER_NONE);
  var code = orientationIdentifier + '.getRotationMatrix(' + orientation + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['orientation_getRotationMatrix_withArgs'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Orientation'))
        .appendField('.')
        .appendField(createNonEditableField('getRotationMatrix'));
    this.appendValueInput('AXES_REFERENCE').setCheck('AxesReference')
        .appendField('axesReference')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('AXES_ORDER').setCheck('AxesOrder')
        .appendField('axesOrder')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('FIRST_ANGLE').setCheck('Number')
        .appendField('FirstAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SECOND_ANGLE').setCheck('Number')
        .appendField('SecondAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('THIRD_ANGLE').setCheck('Number')
        .appendField('ThirdAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the rotation matrix associated with a particular set of three ' +
        'rotational angles.');
  }
};

Blockly.JavaScript['orientation_getRotationMatrix_withArgs'] = function(block) {
  var axesReference = Blockly.JavaScript.valueToCode(
      block, 'AXES_REFERENCE', Blockly.JavaScript.ORDER_COMMA);
  var axesOrder = Blockly.JavaScript.valueToCode(
      block, 'AXES_ORDER', Blockly.JavaScript.ORDER_COMMA);
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var firstAngle = Blockly.JavaScript.valueToCode(
      block, 'FIRST_ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var secondAngle = Blockly.JavaScript.valueToCode(
      block, 'SECOND_ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var thirdAngle = Blockly.JavaScript.valueToCode(
      block, 'THIRD_ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var code = orientationIdentifier + '.getRotationMatrix_withArgs(' + axesReference + ', ' +
      axesOrder + ', ' + angleUnit + ', ' + firstAngle + ', ' + secondAngle + ', ' +
      thirdAngle + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['orientation_getOrientation'] = {
  init: function() {
    this.setOutput(true, 'Orientation');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Orientation'))
        .appendField('.')
        .appendField(createNonEditableField('getOrientation'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('AXES_REFERENCE').setCheck('AxesReference')
        .appendField('axesReference')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('AXES_ORDER').setCheck('AxesOrder')
        .appendField('axesOrder')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Given a rotation matrix, and an AxesReference and AxesOrder, returns an ' +
        'Orientation object that would produce that rotation matrix.');
  }
};

Blockly.JavaScript['orientation_getOrientation'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var axesReference = Blockly.JavaScript.valueToCode(
      block, 'AXES_REFERENCE', Blockly.JavaScript.ORDER_COMMA);
  var axesOrder = Blockly.JavaScript.valueToCode(
      block, 'AXES_ORDER', Blockly.JavaScript.ORDER_COMMA);
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var code = orientationIdentifier + '.getOrientation(' + matrix + ', ' + axesReference + ', ' +
      axesOrder + ', ' + angleUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
