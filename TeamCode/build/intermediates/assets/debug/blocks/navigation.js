/**
 * @fileoverview FTC robot blocks related to navigation.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are defined in vars.js:
// navigationIdentifier
// getPropertyColor
// functionColor

// Enums

// AngleUnit
Blockly.Blocks['navigation_enum_angleUnit'] = {
  init: function() {
    var ANGLE_UNIT_CHOICES = [
        ['DEGREES', 'DEGREES'],
        ['RADIANS', 'RADIANS'],
    ];
    this.setOutput(true, 'AngleUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('AngleUnit'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(ANGLE_UNIT_CHOICES), 'ANGLE_UNIT');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['DEGREES', 'The AngleUnit value DEGREES.'],
        ['RADIANS', 'The AngleUnit value RADIANS.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('ANGLE_UNIT');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['navigation_enum_angleUnit'] = function(block) {
  var code = '"' + block.getFieldValue('ANGLE_UNIT') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['navigation_typedEnum_angleUnit'] =
    Blockly.Blocks['navigation_enum_angleUnit'];

Blockly.JavaScript['navigation_typedEnum_angleUnit'] =
    Blockly.JavaScript['navigation_enum_angleUnit'];

Blockly.Blocks['navigation_angleUnit_normalize'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AngleUnit'))
        .appendField('.')
        .appendField(createNonEditableField('normalize'));
    this.appendValueInput('ANGLE').setCheck('Number')
        .appendField('angle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Normalizes the given angle to the range of [-180,+180) ' +
        'degrees or [-\u03c0,+\u03c0) radians.');
  }
};

Blockly.JavaScript['navigation_angleUnit_normalize'] = function(block) {
  var angle = Blockly.JavaScript.valueToCode(
      block, 'ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var code = navigationIdentifier + '.angleUnit_normalize(' + angle + ', ' + angleUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['navigation_angleUnit_convert'] = {
  init: function() {
    this.setOutput(true, 'Number');
    var ANGLE_UNIT_TYPE_CHOICES = [
        ['AngleUnit', 'angleUnit'],
        ['UnnormalizedAngleUnit', 'unnormalizedAngleUnit'],
    ];
    this.appendDummyInput()
        .appendField('call')
        .appendField(new Blockly.FieldDropdown(ANGLE_UNIT_TYPE_CHOICES), 'ANGLE_UNIT_TYPE')
        .appendField('.')
        .appendField(createNonEditableField('convert'));
    this.appendValueInput('ANGLE').setCheck('Number')
        .appendField('angle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('FROM_ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('from')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('TO_ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    var thisBlock = this;
    var TOOLTIPS = [
        ['angleUnit', 'Converts the given angle and normalizes it to the range of [-180,+180) ' +
            'degrees or [-\u03c0,+\u03c0) radians.'],
        ['unnormalizedAngleUnit', 'Converts the given angle, without normalizing it.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('ANGLE_UNIT_TYPE');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['navigation_angleUnit_convert'] = function(block) {
  var angleUnitType = block.getFieldValue('ANGLE_UNIT_TYPE');
  var angle = Blockly.JavaScript.valueToCode(
      block, 'ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var fromAngleUnit = Blockly.JavaScript.valueToCode(
      block, 'FROM_ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var toAngleUnit = Blockly.JavaScript.valueToCode(
      block, 'TO_ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var code = navigationIdentifier + '.' + angleUnitType + '_convert(' +
      angle + ', ' + fromAngleUnit + ', ' + toAngleUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

// AxesOrder
Blockly.Blocks['navigation_enum_axesOrder'] = {
  init: function() {
    var AXES_ORDER_CHOICES = [
        ['XYX', 'XYX'],
        ['XYZ', 'XYZ'],
        ['XZX', 'XZX'],
        ['XZY', 'XZY'],
        ['YXY', 'YXY'],
        ['YXZ', 'YXZ'],
        ['YZX', 'YZX'],
        ['YZY', 'YZY'],
        ['ZXY', 'ZXY'],
        ['ZXZ', 'ZXZ'],
        ['ZYX', 'ZYX'],
        ['ZYZ', 'ZYZ'],
    ];
    this.setOutput(true, 'AxesOrder');
    this.appendDummyInput()
        .appendField(createNonEditableField('AxesOrder'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(AXES_ORDER_CHOICES), 'AXES_ORDER');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['XYX', 'The AxesOrder value XYX.'],
        ['XYZ', 'The AxesOrder value XYZ.'],
        ['XZX', 'The AxesOrder value XZX.'],
        ['XZY', 'The AxesOrder value XZY.'],
        ['YXY', 'The AxesOrder value YXY.'],
        ['YXZ', 'The AxesOrder value YXZ.'],
        ['YZX', 'The AxesOrder value YZX.'],
        ['YZY', 'The AxesOrder value YZY.'],
        ['ZXY', 'The AxesOrder value ZXY.'],
        ['ZXZ', 'The AxesOrder value ZXZ.'],
        ['ZYX', 'The AxesOrder value ZYX.'],
        ['ZYZ', 'The AxesOrder value ZYZ.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('AXES_ORDER');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['navigation_enum_axesOrder'] = function(block) {
  var code = '"' + block.getFieldValue('AXES_ORDER') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['navigation_typedEnum_axesOrder'] =
    Blockly.Blocks['navigation_enum_axesOrder'];

Blockly.JavaScript['navigation_typedEnum_axesOrder'] =
    Blockly.JavaScript['navigation_enum_axesOrder'];

// AxesReference
Blockly.Blocks['navigation_enum_axesReference'] = {
  init: function() {
    var AXES_REFERENCE_CHOICES = [
        ['EXTRINSIC', 'EXTRINSIC'],
        ['INTRINSIC', 'INTRINSIC'],
    ];
    this.setOutput(true, 'AxesReference');
    this.appendDummyInput()
        .appendField(createNonEditableField('AxesReference'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(AXES_REFERENCE_CHOICES), 'AXES_REFERENCE');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['EXTRINSIC', 'The AxesReference value EXTRINSIC.'],
        ['INTRINSIC', 'The AxesReference value INTRINSIC.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('AXES_REFERENCE');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['navigation_enum_axesReference'] = function(block) {
  var code = '"' + block.getFieldValue('AXES_REFERENCE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['navigation_typedEnum_axesReference'] =
    Blockly.Blocks['navigation_enum_axesReference'];

Blockly.JavaScript['navigation_typedEnum_axesReference'] =
    Blockly.JavaScript['navigation_enum_axesReference'];

// CameraDirection
Blockly.Blocks['navigation_enum_cameraDirection'] = {
  init: function() {
    var CAMERA_DIRECTION_CHOICES = [
        ['BACK', 'BACK'],
        ['FRONT', 'FRONT'],
    ];
    this.setOutput(true, 'CameraDirection');
    this.appendDummyInput()
        .appendField(createNonEditableField('CameraDirection'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(CAMERA_DIRECTION_CHOICES), 'CAMERA_DIRECTION');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['BACK', 'The CameraDirection value BACK.'],
        ['FRONT', 'The CameraDirection value FRONT.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('CAMERA_DIRECTION');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['navigation_enum_cameraDirection'] = function(block) {
  var code = '"' + block.getFieldValue('CAMERA_DIRECTION') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['navigation_typedEnum_cameraDirection'] =
    Blockly.Blocks['navigation_enum_cameraDirection'];

Blockly.JavaScript['navigation_typedEnum_cameraDirection'] =
    Blockly.JavaScript['navigation_enum_cameraDirection'];

// CameraMonitorFeedback
Blockly.Blocks['navigation_enum_cameraMonitorFeedback'] = {
  init: function() {
    var CAMERA_MONITOR_FEEDBACK_CHOICES = [
        ['DEFAULT', 'DEFAULT'],
        ['NONE', 'NONE'],
        ['AXES', 'AXES'],
        ['TEAPOT', 'TEAPOT'],
        ['BUILDINGS', 'BUILDINGS'],
    ];
    this.setOutput(true, 'CameraMonitorFeedback');
    this.appendDummyInput()
        .appendField(createNonEditableField('CameraMonitorFeedback'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(CAMERA_MONITOR_FEEDBACK_CHOICES), 'CAMERA_MONITOR_FEEDBACK');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['DEFAULT', 'The CameraMonitorFeedback value DEFAULT.'],
        ['NONE', 'The CameraMonitorFeedback value NONE.'],
        ['AXES', 'The CameraMonitorFeedback value AXES.'],
        ['TEAPOT', 'The CameraMonitorFeedback value TEAPOT.'],
        ['BUILDINGS', 'The CameraMonitorFeedback value BUILDINGS.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('CAMERA_MONITOR_FEEDBACK');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['navigation_enum_cameraMonitorFeedback'] = function(block) {
  var code = '"' + block.getFieldValue('CAMERA_MONITOR_FEEDBACK') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['navigation_typedEnum_cameraMonitorFeedback'] =
    Blockly.Blocks['navigation_enum_cameraMonitorFeedback'];

Blockly.JavaScript['navigation_typedEnum_cameraMonitorFeedback'] =
    Blockly.JavaScript['navigation_enum_cameraMonitorFeedback'];

// DistanceUnit
Blockly.Blocks['navigation_enum_distanceUnit'] = {
  init: function() {
    var DISTANCE_UNIT_CHOICES = [
        ['CM', 'CM'],
        ['INCH', 'INCH'],
        ['METER', 'METER'],
        ['MM', 'MM'],
    ];
    this.setOutput(true, 'DistanceUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('DistanceUnit'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(DISTANCE_UNIT_CHOICES), 'DISTANCE_UNIT');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CM', 'The DistanceUnit value CM.'],
        ['INCH', 'The DistanceUnit value INCH.'],
        ['METER', 'The DistanceUnit value METER.'],
        ['MM', 'The DistanceUnit value MM.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('DISTANCE_UNIT');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['navigation_enum_distanceUnit'] = function(block) {
  var code = '"' + block.getFieldValue('DISTANCE_UNIT') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['navigation_typedEnum_distanceUnit'] =
    Blockly.Blocks['navigation_enum_distanceUnit'];

Blockly.JavaScript['navigation_typedEnum_distanceUnit'] =
    Blockly.JavaScript['navigation_enum_distanceUnit'];

// TempUnit
Blockly.Blocks['navigation_enum_tempUnit'] = {
  init: function() {
    var TEMP_UNIT_CHOICES = [
        ['CELSIUS', 'CELSIUS'],
        ['FARENHEIT', 'FARENHEIT'],
        ['KELVIN', 'KELVIN'],
    ];
    this.setOutput(true, 'TempUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('TempUnit'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(TEMP_UNIT_CHOICES), 'TEMP_UNIT');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CELSIUS', 'The TempUnit value CELSIUS.'],
        ['FARENHEIT', 'The TempUnit value FARENHEIT.'],
        ['KELVIN', 'The TempUnit value KELVIN.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('TEMP_UNIT');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['navigation_enum_tempUnit'] = function(block) {
  var code = '"' + block.getFieldValue('TEMP_UNIT') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['navigation_typedEnum_tempUnit'] =
    Blockly.Blocks['navigation_enum_tempUnit'];

Blockly.JavaScript['navigation_typedEnum_tempUnit'] =
    Blockly.JavaScript['navigation_enum_tempUnit'];

// Axis
Blockly.Blocks['navigation_typedEnum_axis'] = {
  init: function() {
    var AXIS_CHOICES = [
        ['X', 'X'],
        ['Y', 'Y'],
        ['Z', 'Z'],
    ];
    this.setOutput(true, 'Axis');
    this.appendDummyInput()
        .appendField(createNonEditableField('Axis'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(AXIS_CHOICES), 'AXIS');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['X', 'The Axis value X.'],
        ['Y', 'The Axis value Y.'],
        ['Z', 'The Axis value Z.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('AXIS');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['navigation_typedEnum_axis'] = function(block) {
  var code = '"' + block.getFieldValue('AXIS') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};
