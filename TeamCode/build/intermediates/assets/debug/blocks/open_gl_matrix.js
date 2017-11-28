/**
 * @fileoverview FTC robot blocks related to OpenGLMatrix.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// openGLMatrixIdentifier
// The following are defined in vars.js:
// createNonEditableField
// functionColor

Blockly.Blocks['openGLMatrix_create'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('OpenGLMatrix'));
    this.setColour(functionColor);
    this.setTooltip('Create a new OpenGLMatrix object.');
  }
};

Blockly.JavaScript['openGLMatrix_create'] = function(block) {
  var code = openGLMatrixIdentifier + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['openGLMatrix_create_withMatrixF'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('OpenGLMatrix'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new OpenGLMatrix object whose values are initialized from the given matrix.');
  }
};

Blockly.JavaScript['openGLMatrix_create_withMatrixF'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_NONE);
  var code = openGLMatrixIdentifier + '.create_withMatrixF(' + matrix + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['openGLMatrix_rotation'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('rotation'));
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE').setCheck('Number')
        .appendField('angle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DX').setCheck('Number')
        .appendField('dx')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DY').setCheck('Number')
        .appendField('dy')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DZ').setCheck('Number')
        .appendField('dz')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new OpenGLMatrix object for rotation by the indicated angle around ' +
        'the indicated vector.');
  }
};

Blockly.JavaScript['openGLMatrix_rotation'] = function(block) {
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var angle = Blockly.JavaScript.valueToCode(
      block, 'ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var dx = Blockly.JavaScript.valueToCode(
      block, 'DX', Blockly.JavaScript.ORDER_COMMA);
  var dy = Blockly.JavaScript.valueToCode(
      block, 'DY', Blockly.JavaScript.ORDER_COMMA);
  var dz = Blockly.JavaScript.valueToCode(
      block, 'DZ', Blockly.JavaScript.ORDER_COMMA);
  var code = openGLMatrixIdentifier + '.rotation(' + angleUnit + ', ' + angle + ', ' +
      dx + ', ' + dy + ', ' + dz + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['openGLMatrix_rotation_withAxesArgs'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('rotation'));
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
        .appendField('firstAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SECOND_ANGLE').setCheck('Number')
        .appendField('secondAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('THIRD_ANGLE').setCheck('Number')
        .appendField('thirdAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new OpenGLMatrix object for a rotation specified by three ' +
        'successive rotation angles.');
  }
};

Blockly.JavaScript['openGLMatrix_rotation_withAxesArgs'] = function(block) {
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
  var code = openGLMatrixIdentifier + '.rotation_withAxesArgs(' + axesReference + ', ' +
      axesOrder + ', ' + angleUnit + ', ' + firstAngle + ', ' + secondAngle + ', ' +
      thirdAngle + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['openGLMatrix_translation'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('translation'));
    this.appendValueInput('DX').setCheck('Number')
        .appendField('dx')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DY').setCheck('Number')
        .appendField('dy')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DZ').setCheck('Number')
        .appendField('dz')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new OpenGLMatrix object for translation.');
  }
};

Blockly.JavaScript['openGLMatrix_translation'] = function(block) {
  var dx = Blockly.JavaScript.valueToCode(
      block, 'DX', Blockly.JavaScript.ORDER_COMMA);
  var dy = Blockly.JavaScript.valueToCode(
      block, 'DY', Blockly.JavaScript.ORDER_COMMA);
  var dz = Blockly.JavaScript.valueToCode(
      block, 'DZ', Blockly.JavaScript.ORDER_COMMA);
  var code = openGLMatrixIdentifier + '.translation(' + dx + ', ' + dy + ', ' + dz + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['openGLMatrix_identityMatrix'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('identityMatrix'));
    this.setColour(functionColor);
    this.setTooltip('Returns an identity matrix. ' +
        'An identity matrix is zero everywhere except on the diagonal, where it is one.');
  }
};

Blockly.JavaScript['openGLMatrix_identityMatrix'] = function(block) {
  var code = openGLMatrixIdentifier + '.identityMatrix()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['openGLMatrix_scale_with3'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('scale'));
    this.appendValueInput('MATRIX').setCheck('OpenGLMatrix')
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE_X').setCheck('Number')
        .appendField('scaleX')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE_Y').setCheck('Number')
        .appendField('scaleY')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE_Z').setCheck('Number')
        .appendField('scaleZ')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Scales the given matrix.');
  }
};

Blockly.JavaScript['openGLMatrix_scale_with3'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var scaleX = Blockly.JavaScript.valueToCode(
      block, 'SCALE_X', Blockly.JavaScript.ORDER_COMMA);
  var scaleY = Blockly.JavaScript.valueToCode(
      block, 'SCALE_Y', Blockly.JavaScript.ORDER_COMMA);
  var scaleZ = Blockly.JavaScript.valueToCode(
      block, 'SCALE_Z', Blockly.JavaScript.ORDER_COMMA);
  return openGLMatrixIdentifier + '.scale_with3(' + matrix + ', ' +
      scaleX + ', ' + scaleY + ', ' + scaleZ + ');\n';
};

Blockly.Blocks['openGLMatrix_scale_with1'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('scale'));
    this.appendValueInput('MATRIX').setCheck('OpenGLMatrix')
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE').setCheck('Number')
        .appendField('scale')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Scales the given matrix.');
  }
};

Blockly.JavaScript['openGLMatrix_scale_with1'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var scale = Blockly.JavaScript.valueToCode(
      block, 'SCALE', Blockly.JavaScript.ORDER_COMMA);
  return openGLMatrixIdentifier + '.scale_with1(' + matrix + ', ' + scale + ');\n';
};

Blockly.Blocks['openGLMatrix_translate'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('translate'));
    this.appendValueInput('MATRIX').setCheck('OpenGLMatrix')
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DX').setCheck('Number')
        .appendField('dx')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DY').setCheck('Number')
        .appendField('dy')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DZ').setCheck('Number')
        .appendField('dz')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Translates the given matrix.');
  }
};

Blockly.JavaScript['openGLMatrix_translate'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var dx = Blockly.JavaScript.valueToCode(
      block, 'DX', Blockly.JavaScript.ORDER_COMMA);
  var dy = Blockly.JavaScript.valueToCode(
      block, 'DY', Blockly.JavaScript.ORDER_COMMA);
  var dz = Blockly.JavaScript.valueToCode(
      block, 'DZ', Blockly.JavaScript.ORDER_COMMA);
  return openGLMatrixIdentifier + '.translate(' + matrix + ', ' +
      dx + ', ' + dy + ', ' + dz + ');\n';
};

Blockly.Blocks['openGLMatrix_rotate'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('rotate'));
    this.appendValueInput('MATRIX').setCheck('OpenGLMatrix')
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE').setCheck('Number')
        .appendField('angle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DX').setCheck('Number')
        .appendField('dx')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DY').setCheck('Number')
        .appendField('dy')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DZ').setCheck('Number')
        .appendField('dz')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Rotates the given matrix.');
  }
};

Blockly.JavaScript['openGLMatrix_rotate'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var angle = Blockly.JavaScript.valueToCode(
      block, 'ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var dx = Blockly.JavaScript.valueToCode(
      block, 'DX', Blockly.JavaScript.ORDER_COMMA);
  var dy = Blockly.JavaScript.valueToCode(
      block, 'DY', Blockly.JavaScript.ORDER_COMMA);
  var dz = Blockly.JavaScript.valueToCode(
      block, 'DZ', Blockly.JavaScript.ORDER_COMMA);
  return openGLMatrixIdentifier + '.rotate(' + matrix + ', ' + angleUnit + ', ' + angle + ', ' +
      dx + ', ' + dy + ', ' + dz + ');\n';
};

Blockly.Blocks['openGLMatrix_rotate_withAxesArgs'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('rotate'));
    this.appendValueInput('MATRIX').setCheck('OpenGLMatrix')
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
    this.appendValueInput('FIRST_ANGLE').setCheck('Number')
        .appendField('firstAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SECOND_ANGLE').setCheck('Number')
        .appendField('secondAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('THIRD_ANGLE').setCheck('Number')
        .appendField('thirdAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Rotates the given matrix.');
  }
};

Blockly.JavaScript['openGLMatrix_rotate_withAxesArgs'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
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
  return openGLMatrixIdentifier + '.rotate_withAxesArgs(' + matrix + ', ' + axesReference + ', ' +
      axesOrder + ', ' + angleUnit + ', ' + firstAngle + ', ' + secondAngle + ', ' +
      thirdAngle + ');\n';
};

Blockly.Blocks['openGLMatrix_scaled_with3'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('scaled'));
    this.appendValueInput('MATRIX').setCheck('OpenGLMatrix')
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE_X').setCheck('Number')
        .appendField('scaleX')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE_Y').setCheck('Number')
        .appendField('scaleY')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE_Z').setCheck('Number')
        .appendField('scaleZ')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new OpenGLMatrix object by scaling the given matrix.');
  }
};

Blockly.JavaScript['openGLMatrix_scaled_with3'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var scaleX = Blockly.JavaScript.valueToCode(
      block, 'SCALE_X', Blockly.JavaScript.ORDER_COMMA);
  var scaleY = Blockly.JavaScript.valueToCode(
      block, 'SCALE_Y', Blockly.JavaScript.ORDER_COMMA);
  var scaleZ = Blockly.JavaScript.valueToCode(
      block, 'SCALE_Z', Blockly.JavaScript.ORDER_COMMA);
  var code = openGLMatrixIdentifier + '.scaled_with3(' + matrix + ', ' +
      scaleX + ', ' + scaleY + ', ' + scaleZ + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['openGLMatrix_scaled_with1'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('scaled'));
    this.appendValueInput('MATRIX').setCheck('OpenGLMatrix')
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE').setCheck('Number')
        .appendField('scale')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new OpenGLMatrix object by scaling the given matrix.');
  }
};

Blockly.JavaScript['openGLMatrix_scaled_with1'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var scale = Blockly.JavaScript.valueToCode(
      block, 'SCALE', Blockly.JavaScript.ORDER_COMMA);
  var code = openGLMatrixIdentifier + '.scaled_with1(' + matrix + ', ' + scale + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['openGLMatrix_translated'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('translated'));
    this.appendValueInput('MATRIX').setCheck('OpenGLMatrix')
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DX').setCheck('Number')
        .appendField('dx')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DY').setCheck('Number')
        .appendField('dy')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DZ').setCheck('Number')
        .appendField('dz')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new OpenGLMatrix object by translating the given matrix.');
  }
};

Blockly.JavaScript['openGLMatrix_translated'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var dx = Blockly.JavaScript.valueToCode(
      block, 'DX', Blockly.JavaScript.ORDER_COMMA);
  var dy = Blockly.JavaScript.valueToCode(
      block, 'DY', Blockly.JavaScript.ORDER_COMMA);
  var dz = Blockly.JavaScript.valueToCode(
      block, 'DZ', Blockly.JavaScript.ORDER_COMMA);
  var code = openGLMatrixIdentifier + '.translated(' + matrix + ', ' +
      dx + ', ' + dy + ', ' + dz + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['openGLMatrix_rotated'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('rotated'));
    this.appendValueInput('MATRIX').setCheck('OpenGLMatrix')
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE').setCheck('Number')
        .appendField('angle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DX').setCheck('Number')
        .appendField('dx')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DY').setCheck('Number')
        .appendField('dy')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('DZ').setCheck('Number')
        .appendField('dz')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new OpenGLMatrix object by rotating the given matrix.');
  }
};

Blockly.JavaScript['openGLMatrix_rotated'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var angle = Blockly.JavaScript.valueToCode(
      block, 'ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var dx = Blockly.JavaScript.valueToCode(
      block, 'DX', Blockly.JavaScript.ORDER_COMMA);
  var dy = Blockly.JavaScript.valueToCode(
      block, 'DY', Blockly.JavaScript.ORDER_COMMA);
  var dz = Blockly.JavaScript.valueToCode(
      block, 'DZ', Blockly.JavaScript.ORDER_COMMA);
  var code = openGLMatrixIdentifier + '.rotated(' + matrix + ', ' + angleUnit + ', ' +
      angle + ', ' + dx + ', ' + dy + ', ' + dz + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['openGLMatrix_rotated_withAxesArgs'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('rotated'));
    this.appendValueInput('MATRIX').setCheck('OpenGLMatrix')
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
    this.appendValueInput('FIRST_ANGLE').setCheck('Number')
        .appendField('firstAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SECOND_ANGLE').setCheck('Number')
        .appendField('secondAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('THIRD_ANGLE').setCheck('Number')
        .appendField('thirdAngle')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new OpenGLMatrix object by rotating the given matrix.');
  }
};

Blockly.JavaScript['openGLMatrix_rotated_withAxesArgs'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
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
  var code = openGLMatrixIdentifier + '.rotated_withAxesArgs(' + matrix + ', ' +
      axesReference + ', ' + axesOrder + ', ' + angleUnit + ', ' +
      firstAngle + ', ' + secondAngle + ', ' + thirdAngle + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['openGLMatrix_multiplied'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('multiplied'));
    this.appendValueInput('MATRIX1').setCheck('OpenGLMatrix')
        .appendField('matrix1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MATRIX2').setCheck('OpenGLMatrix')
        .appendField('matrix2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Create a new OpenGLMatrix object by multiplying matrix1 by matrix2.');
  }
};

Blockly.JavaScript['openGLMatrix_multiplied'] = function(block) {
  var matrix1 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX1', Blockly.JavaScript.ORDER_COMMA);
  var matrix2 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX2', Blockly.JavaScript.ORDER_COMMA);
  var code = openGLMatrixIdentifier + '.multiplied(' + matrix1 + ', ' + matrix2 + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['openGLMatrix_multiply'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('OpenGLMatrix'))
        .appendField('.')
        .appendField(createNonEditableField('multiply'));
    this.appendValueInput('MATRIX1').setCheck('OpenGLMatrix')
        .appendField('matrix1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MATRIX2').setCheck('OpenGLMatrix')
        .appendField('matrix2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates matrix1 by multiplying it by matrix2.');
  }
};

Blockly.JavaScript['openGLMatrix_multiply'] = function(block) {
  var matrix1 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX1', Blockly.JavaScript.ORDER_COMMA);
  var matrix2 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX2', Blockly.JavaScript.ORDER_COMMA);
  return openGLMatrixIdentifier + '.multiply(' + matrix1 + ', ' + matrix2 + ');\n';
};
