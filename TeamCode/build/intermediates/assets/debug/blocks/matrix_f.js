/**
 * @fileoverview FTC robot blocks related to MatrixF.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// matrixFIdentifier
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['matrixF_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['NumRows', 'NumRows'],
        ['NumCols', 'NumCols'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['NumRows', 'The number of rows in the given matrix.'],
        ['NumCols', 'The number of columns in the given matrix.'],
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

Blockly.JavaScript['matrixF_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_NONE);
  var code = matrixFIdentifier + '.get' + property + '(' + matrix + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['NumRows', 'NumRows'],
        ['NumCols', 'NumCols'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['NumRows', 'The number of rows in the given matrix.'],
        ['NumCols', 'The number of columns in the given matrix.'],
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

Blockly.JavaScript['matrixF_getProperty_Number'] =
    Blockly.JavaScript['matrixF_getProperty']


// Functions

Blockly.Blocks['matrixF_slice'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('slice'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ROW').setCheck('Number')
        .appendField('row')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('COL').setCheck('Number')
        .appendField('col')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('NUM_ROWS').setCheck('Number')
        .appendField('numRows')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('NUM_COLS').setCheck('Number')
        .appendField('numCols')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a matrix which is a submatrix of the given matrix.');
  }
};

Blockly.JavaScript['matrixF_slice'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var row = Blockly.JavaScript.valueToCode(
      block, 'ROW', Blockly.JavaScript.ORDER_COMMA);
  var col = Blockly.JavaScript.valueToCode(
      block, 'COL', Blockly.JavaScript.ORDER_COMMA);
  var numRows = Blockly.JavaScript.valueToCode(
      block, 'NUM_ROWS', Blockly.JavaScript.ORDER_COMMA);
  var numCols = Blockly.JavaScript.valueToCode(
      block, 'NUM_COLS', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.slice(' + matrix + ', ' +
      row  + ', ' + col + ', ' + numRows + ', ' + numCols + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_identityMatrix'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('identityMatrix'));
    this.appendValueInput('DIM').setCheck('Number')
        .appendField('dim')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns an identity matrix of the indicated dimension. ' +
        'An identity matrix is zero everywhere except on the diagonal, where it is one.');
  }
};

Blockly.JavaScript['matrixF_identityMatrix'] = function(block) {
  var dim = Blockly.JavaScript.valueToCode(
      block, 'DIM', Blockly.JavaScript.ORDER_NONE);
  var code = matrixFIdentifier + '.identityMatrix(' + dim + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_diagonalMatrix'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('diagonalMatrix'));
    this.appendValueInput('DIM').setCheck('Number')
        .appendField('dim')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE').setCheck('Number')
        .appendField('scale')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new matrix which is zero everywhere except ' +
        'on the diagonal, where it has the indicated value.');
  }
};

Blockly.JavaScript['matrixF_diagonalMatrix'] = function(block) {
  var dim = Blockly.JavaScript.valueToCode(
      block, 'DIM', Blockly.JavaScript.ORDER_COMMA);
  var scale = Blockly.JavaScript.valueToCode(
      block, 'SCALE', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.diagonalMatrix(' + dim + ', ' + scale + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_diagonalMatrix_withVector'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('diagonalMatrix'));
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new matrix which is zero everywhere, except ' +
        'on the diagonal, where its values are taken from an indicated vector.');
  }
};

Blockly.JavaScript['matrixF_diagonalMatrix_withVector'] = function(block) {
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_NONE);
  var code = matrixFIdentifier + '.diagonalMatrix_withVector(' + vector + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_get'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('get'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ROW').setCheck('Number')
        .appendField('row')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('COL').setCheck('Number')
        .appendField('col')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a particular element of the given matrix.');
  }
};

Blockly.JavaScript['matrixF_get'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var row = Blockly.JavaScript.valueToCode(
      block, 'ROW', Blockly.JavaScript.ORDER_COMMA);
  var col = Blockly.JavaScript.valueToCode(
      block, 'COL', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.get(' + matrix + ', ' + row  + ', ' + col + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_put'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('put'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ROW').setCheck('Number')
        .appendField('row')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('COL').setCheck('Number')
        .appendField('col')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('value')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates a particular element of the given matrix.');
  }
};

Blockly.JavaScript['matrixF_put'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var row = Blockly.JavaScript.valueToCode(
      block, 'ROW', Blockly.JavaScript.ORDER_COMMA);
  var col = Blockly.JavaScript.valueToCode(
      block, 'COL', Blockly.JavaScript.ORDER_COMMA);
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_COMMA);
  return matrixFIdentifier + '.put(' + matrix + ', ' + row + ', ' + col + ', ' + value + ');\n';
};

Blockly.Blocks['matrixF_getRow'] = {
  init: function() {
    this.setOutput(true, 'VectorF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('getRow'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ROW').setCheck('Number')
        .appendField('row')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a vector containing data of a particular row of the given matrix.');
  }
};

Blockly.JavaScript['matrixF_getRow'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var row = Blockly.JavaScript.valueToCode(
      block, 'ROW', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.getRow(' + matrix + ', ' + row + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_getColumn'] = {
  init: function() {
    this.setOutput(true, 'VectorF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('getColumn'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('COL').setCheck('Number')
        .appendField('col')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a vector containing data of a particular column of the given matrix.');
  }
};

Blockly.JavaScript['matrixF_getColumn'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var col = Blockly.JavaScript.valueToCode(
      block, 'COL', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.getColumn(' + matrix + ', ' + col + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_toText'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('toText'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a text representation of the given matrix.');
  }
};

Blockly.JavaScript['matrixF_toText'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_NONE);
  var code = matrixFIdentifier + '.toText(' + matrix + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_transform'] = {
  init: function() {
    this.setOutput(true, 'VectorF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('transform'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Transforms the given vector according to the given matrix interpreted as a ' +
        'transformation matrix.');
  }
};

Blockly.JavaScript['matrixF_transform'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.transform(' + matrix + ', ' + vector + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_formatAsTransform'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('formatAsTransform'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a text representation of the given matrix.');
  }
};

Blockly.JavaScript['matrixF_formatAsTransform'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_NONE);
  var code = matrixFIdentifier + '.formatAsTransform(' + matrix + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_formatAsTransform_withArgs'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('formatAsTransform'));
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
    this.setTooltip('Returns a text representation of the given matrix.');
  }
};

Blockly.JavaScript['matrixF_formatAsTransform_withArgs'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var axesReference = Blockly.JavaScript.valueToCode(
      block, 'AXES_REFERENCE', Blockly.JavaScript.ORDER_COMMA);
  var axesOrder = Blockly.JavaScript.valueToCode(
      block, 'AXES_ORDER', Blockly.JavaScript.ORDER_COMMA);
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.formatAsTransform_withArgs(' + matrix + ', ' +
      axesReference + ', ' + axesOrder + ', ' + angleUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_transposed'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('transposed'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a matrix which is the transposition of the given matrix.');
  }
};

Blockly.JavaScript['matrixF_transposed'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_NONE);
  var code = matrixFIdentifier + '.transposed(' + matrix + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_multiplied_withMatrix'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('multiplied'));
    this.appendValueInput('MATRIX1').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MATRIX2').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Multiplies matrix1 by matrix2.');
  }
};

Blockly.JavaScript['matrixF_multiplied_withMatrix'] = function(block) {
  var matrix1 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX1', Blockly.JavaScript.ORDER_COMMA);
  var matrix2 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX2', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.multiplied_withMatrix(' + matrix1 + ', ' + matrix2 + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_multiplied_withScale'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('multiplied'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE').setCheck('Number')
        .appendField('scale')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Multiplies the given matrix by the given scale.');
  }
};

Blockly.JavaScript['matrixF_multiplied_withScale'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var scale = Blockly.JavaScript.valueToCode(
      block, 'SCALE', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.multiplied_withScale(' + matrix + ', ' + scale + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_multiplied_withVector'] = {
  init: function() {
    this.setOutput(true, 'VectorF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('multiplied'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Multiplies the given matrix by the given vector, considered as a column matrix.');
  }
};

Blockly.JavaScript['matrixF_multiplied_withVector'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.multiplied_withVector(' + matrix + ', ' + vector + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_multiply_withMatrix'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('multiply'));
    this.appendValueInput('MATRIX1').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MATRIX2').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates matrix1 by multiplying it by matrix2.');
  }
};

Blockly.JavaScript['matrixF_multiply_withMatrix'] = function(block) {
  var matrix1 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX1', Blockly.JavaScript.ORDER_COMMA);
  var matrix2 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX2', Blockly.JavaScript.ORDER_COMMA);
  return matrixFIdentifier + '.multiply_withMatrix(' + matrix1 + ', ' + matrix2 + ');\n';
};

Blockly.Blocks['matrixF_multiply_withScale'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('multiply'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE').setCheck('Number')
        .appendField('scale')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates the given matrix to be the product of itself and the given scale.');
  }
};

Blockly.JavaScript['matrixF_multiply_withScale'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var scale = Blockly.JavaScript.valueToCode(
      block, 'SCALE', Blockly.JavaScript.ORDER_COMMA);
  return matrixFIdentifier + '.multiply_withScale(' + matrix + ', ' + scale + ');\n';
};

Blockly.Blocks['matrixF_multiply_withVector'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('multiply'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates the given matrix to be the product of itself and the given vector.');
  }
};

Blockly.JavaScript['matrixF_multiply_withVector'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  return matrixFIdentifier + '.multiply_withVector(' + matrix + ', ' + vector + ');\n';
};

Blockly.Blocks['matrixF_toVector'] = {
  init: function() {
    this.setOutput(true, 'VectorF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('toVector'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('If the given matrix is one-dimensional in one of its dimensions, ' +
        'returns a vector containing the data of the given matrix.');
  }
};

Blockly.JavaScript['matrixF_toVector'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_NONE);
  var code = matrixFIdentifier + '.toVector(' + matrix + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_added_withMatrix'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('added'));
    this.appendValueInput('MATRIX1').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MATRIX2').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new matrix whose elements are the sum of the ' +
        'corresponding elements of matrix1 and matrix2.');
  }
};

Blockly.JavaScript['matrixF_added_withMatrix'] = function(block) {
  var matrix1 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX1', Blockly.JavaScript.ORDER_COMMA);
  var matrix2 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX2', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.added_withMatrix(' + matrix1 + ', ' + matrix2 + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_added_withVector'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('added'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new matrix whose elements are the sum of the ' +
        'corresponding elements of the given matrix and the given vector.');
  }
};

Blockly.JavaScript['matrixF_added_withVector'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.added_withVector(' + matrix + ', ' + vector + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_add_withMatrix'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('add'));
    this.appendValueInput('MATRIX1').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MATRIX2').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates matrix1 to be the sum of itself and matrix2.');
  }
};

Blockly.JavaScript['matrixF_add_withMatrix'] = function(block) {
  var matrix1 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX1', Blockly.JavaScript.ORDER_COMMA);
  var matrix2 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX2', Blockly.JavaScript.ORDER_COMMA);
  return matrixFIdentifier + '.add_withMatrix(' + matrix1 + ', ' + matrix2 + ');\n';
};

Blockly.Blocks['matrixF_add_withVector'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('add'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates the given matrix to be the sum of itself and the given vector.');
  }
};

Blockly.JavaScript['matrixF_add_withVector'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  return matrixFIdentifier + '.add_withVector(' + matrix + ', ' + vector + ');\n';
};

Blockly.Blocks['matrixF_subtracted_withMatrix'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('subtracted'));
    this.appendValueInput('MATRIX1').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MATRIX2').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new matrix whose elements are the difference of the ' +
        'corresponding elements of matrix1 and matrix2.');
  }
};

Blockly.JavaScript['matrixF_subtracted_withMatrix'] = function(block) {
  var matrix1 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX1', Blockly.JavaScript.ORDER_COMMA);
  var matrix2 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX2', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.subtracted_withMatrix(' + matrix1 + ', ' + matrix2 + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_subtracted_withVector'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('subtracted'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new matrix whose elements are the difference of the ' +
        'corresponding elements of the given matrix and the given vector.');
  }
};

Blockly.JavaScript['matrixF_subtracted_withVector'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  var code = matrixFIdentifier + '.subtracted_withVector(' + matrix + ', ' + vector + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_subtract_withMatrix'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('subtract'));
    this.appendValueInput('MATRIX1').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MATRIX2').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates matrix1 to be the difference of itself and matrix2.');
  }
};

Blockly.JavaScript['matrixF_subtract_withMatrix'] = function(block) {
  var matrix1 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX1', Blockly.JavaScript.ORDER_COMMA);
  var matrix2 = Blockly.JavaScript.valueToCode(
      block, 'MATRIX2', Blockly.JavaScript.ORDER_COMMA);
  return matrixFIdentifier + '.subtract_withMatrix(' + matrix1 + ', ' + matrix2 + ');\n';
};

Blockly.Blocks['matrixF_subtract_withVector'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('subtract'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates the given matrix to be the diference of itself and the given vector.');
  }
};

Blockly.JavaScript['matrixF_subtract_withVector'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  return matrixFIdentifier + '.subtract_withVector(' + matrix + ', ' + vector + ');\n';
};

Blockly.Blocks['matrixF_getTranslation'] = {
  init: function() {
    this.setOutput(true, 'VectorF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('getTranslation'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Assumes that the given matrix is non-perspective transformation matrix. ' +
        'Returns the translation component of the transformation.');
  }
};

Blockly.JavaScript['matrixF_getTranslation'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_NONE);
  var code = matrixFIdentifier + '.getTranslation(' + matrix + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['matrixF_inverted'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('MatrixF'))
        .appendField('.')
        .appendField(createNonEditableField('inverted'));
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a matrix which is the matrix-multiplication inverse of the given matrix.');
  }
};

Blockly.JavaScript['matrixF_inverted'] = function(block) {
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_NONE);
  var code = matrixFIdentifier + '.inverted(' + matrix + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
