/**
 * @fileoverview FTC robot blocks related to VectorF
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// vectorFIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['vectorF_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Length', 'Length'],
        ['Magnitude', 'Magnitude'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Length', 'Returns the length of the given vector.'],
        ['Magnitude', 'Returns the magnitude of the given vector.'],
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

Blockly.JavaScript['vectorF_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_NONE);
  var code = vectorFIdentifierForJavaScript + '.get' + property + '(' + vector + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vectorF_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vector = Blockly.FtcJava.valueToCode(
      block, 'VECTOR', Blockly.FtcJava.ORDER_MEMBER);
  var code = vector + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property) + '()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vectorF_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Length', 'Length'],
        ['Magnitude', 'Magnitude'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Length', 'Returns the length of the given vector.'],
        ['Magnitude', 'Returns the magnitude of the given vector.'],
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
        case 'Length':
          return 'int';
        case 'Magnitude':
          return 'float';
        default:
          throw 'Unexpected property ' + property + ' (vectorF_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['vectorF_getProperty_Number'] =
    Blockly.JavaScript['vectorF_getProperty'];

Blockly.FtcJava['vectorF_getProperty_Number'] =
    Blockly.FtcJava['vectorF_getProperty'];

// Functions

Blockly.Blocks['vectorF_create'] = {
  init: function() {
    this.setOutput(true, 'VectorF');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('VectorF'));
    this.appendValueInput('LENGTH').setCheck('Number')
        .appendField('length')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Creates a new Vector of the given length. The vector will contain zeros.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'LENGTH':
          return 'int';
      }
      return '';
    };
  }
};

Blockly.JavaScript['vectorF_create'] = function(block) {
  var length = Blockly.JavaScript.valueToCode(
      block, 'LENGTH', Blockly.JavaScript.ORDER_NONE);
  var code = vectorFIdentifierForJavaScript + '.create(' + length + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vectorF_create'] = function(block) {
  var length = Blockly.FtcJava.valueToCode(
      block, 'LENGTH', Blockly.FtcJava.ORDER_NONE);
  var code = 'VectorF.length(' + length + ')';
  Blockly.FtcJava.generateImport_('VectorF');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vectorF_get'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('get'));
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('INDEX').setCheck('Number')
        .appendField('index')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a particular element of the given vector.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'INDEX':
          return 'int';
      }
      return '';
    };
    this.getFtcJavaOutputType = function() {
      return 'float';
    };
  }
};

Blockly.JavaScript['vectorF_get'] = function(block) {
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  var index = Blockly.JavaScript.valueToCode(
      block, 'INDEX', Blockly.JavaScript.ORDER_COMMA);
  var code = vectorFIdentifierForJavaScript + '.get(' + vector + ', ' + index + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vectorF_get'] = function(block) {
  var vector = Blockly.FtcJava.valueToCode(
      block, 'VECTOR', Blockly.FtcJava.ORDER_MEMBER);
  var index = Blockly.FtcJava.valueToCode(
      block, 'INDEX', Blockly.FtcJava.ORDER_NONE);
  var code = vector + '.get(' + index + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vectorF_put'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('put'));
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('INDEX').setCheck('Number')
        .appendField('index')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('value')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates a particular element of the given vector.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'INDEX':
          return 'int';
        case 'VALUE':
          return 'float';
      }
      return '';
    };
  }
};

Blockly.JavaScript['vectorF_put'] = function(block) {
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  var index = Blockly.JavaScript.valueToCode(
      block, 'INDEX', Blockly.JavaScript.ORDER_COMMA);
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_COMMA);
  return vectorFIdentifierForJavaScript + '.put(' + vector + ', ' + index + ', ' + value + ');\n';
};

Blockly.FtcJava['vectorF_put'] = function(block) {
  var vector = Blockly.FtcJava.valueToCode(
      block, 'VECTOR', Blockly.FtcJava.ORDER_MEMBER);
  var index = Blockly.FtcJava.valueToCode(
      block, 'INDEX', Blockly.FtcJava.ORDER_COMMA);
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_COMMA);
  return vector + '.put(' + index + ', ' + value + ');\n';
};

Blockly.Blocks['vectorF_toText'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('toText'));
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a text representation of the given vector.');
  }
};

Blockly.JavaScript['vectorF_toText'] = function(block) {
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_NONE);
  var code = vectorFIdentifierForJavaScript + '.toText(' + vector + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vectorF_toText'] = function(block) {
  var vector = Blockly.FtcJava.valueToCode(
      block, 'VECTOR', Blockly.FtcJava.ORDER_MEMBER);
  var code = vector + '.toString()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vectorF_normalized3D'] = {
  init: function() {
    this.setOutput(true, 'VectorF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('normalized3D'));
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Consider the given vector as a 3D coordinate or 3D homogeneous ' +
        'coordinate, and, if the latter, return its normalized form. In either case, ' +
        'the result is a vector of length three, and contains coordinate values for x, y, and ' +
        'z at indices 0, 1, and 2 respectively.');
  }
};

Blockly.JavaScript['vectorF_normalized3D'] = function(block) {
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_NONE);
  var code = vectorFIdentifierForJavaScript + '.normalized3D(' + vector + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vectorF_normalized3D'] = function(block) {
  var vector = Blockly.FtcJava.valueToCode(
      block, 'VECTOR', Blockly.FtcJava.ORDER_MEMBER);
  var code = vector + '.normalized3D()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vectorF_dotProduct'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('dotProduct'));
    this.appendValueInput('VECTOR1').setCheck('VectorF')
        .appendField('vector1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VECTOR2').setCheck('VectorF')
        .appendField('vector2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a number representing the dot product of vector1 and vector2.');
    this.getFtcJavaOutputType = function() {
      return 'float';
    };
  }
};

Blockly.JavaScript['vectorF_dotProduct'] = function(block) {
  var vector1 = Blockly.JavaScript.valueToCode(
      block, 'VECTOR1', Blockly.JavaScript.ORDER_COMMA);
  var vector2 = Blockly.JavaScript.valueToCode(
      block, 'VECTOR2', Blockly.JavaScript.ORDER_COMMA);
  var code = vectorFIdentifierForJavaScript + '.dotProduct(' + vector1 + ', ' + vector2 + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vectorF_dotProduct'] = function(block) {
  var vector1 = Blockly.FtcJava.valueToCode(
      block, 'VECTOR1', Blockly.FtcJava.ORDER_MEMBER);
  var vector2 = Blockly.FtcJava.valueToCode(
      block, 'VECTOR2', Blockly.FtcJava.ORDER_NONE);
  var code = vector1 + '.dotProduct(' + vector2 + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vectorF_multiplied'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('multiplied'));
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a matrix representing the result of multiplying the given vector, taken ' +
        'as a row vector, against the given matrix.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'MATRIX':
          return 'MatrixF';
      }
      return '';
    };
  }
};

Blockly.JavaScript['vectorF_multiplied'] = function(block) {
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var code = vectorFIdentifierForJavaScript + '.multiplied(' + vector + ', ' + matrix + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vectorF_multiplied'] = function(block) {
  var vector = Blockly.FtcJava.valueToCode(
      block, 'VECTOR', Blockly.FtcJava.ORDER_MEMBER);
  var matrix = Blockly.FtcJava.valueToCode(
      block, 'MATRIX', Blockly.FtcJava.ORDER_NONE);
  var code = vector + '.multiplied(' + matrix + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vectorF_added_withMatrix'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('added'));
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a matrix representing the result of adding the given vector, taken as a ' +
        'row vector, to the given matrix.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'MATRIX':
          return 'MatrixF';
      }
      return '';
    };
  }
};

Blockly.JavaScript['vectorF_added_withMatrix'] = function(block) {
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var code = vectorFIdentifierForJavaScript + '.added_withMatrix(' + vector + ', ' + matrix + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vectorF_added_withMatrix'] = function(block) {
  var vector = Blockly.FtcJava.valueToCode(
      block, 'VECTOR', Blockly.FtcJava.ORDER_MEMBER);
  var matrix = Blockly.FtcJava.valueToCode(
      block, 'MATRIX', Blockly.FtcJava.ORDER_NONE);
  var code = vector + '.added(' + matrix + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vectorF_added_withVector'] = {
  init: function() {
    this.setOutput(true, 'VectorF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('added'));
    this.appendValueInput('VECTOR1').setCheck('VectorF')
        .appendField('vector1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VECTOR2').setCheck('VectorF')
        .appendField('vector2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a vector representing the result of adding vector1 to vector2.');
  }
};

Blockly.JavaScript['vectorF_added_withVector'] = function(block) {
  var vector1 = Blockly.JavaScript.valueToCode(
      block, 'VECTOR1', Blockly.JavaScript.ORDER_COMMA);
  var vector2 = Blockly.JavaScript.valueToCode(
      block, 'VECTOR2', Blockly.JavaScript.ORDER_COMMA);
  var code = vectorFIdentifierForJavaScript + '.added_withVector(' + vector1 + ', ' + vector2 + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vectorF_added_withVector'] = function(block) {
  var vector1 = Blockly.FtcJava.valueToCode(
      block, 'VECTOR1', Blockly.FtcJava.ORDER_MEMBER);
  var vector2 = Blockly.FtcJava.valueToCode(
      block, 'VECTOR2', Blockly.FtcJava.ORDER_NONE);
  var code = vector1 + '.added(' + vector2 + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vectorF_add_withVector'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('add'));
    this.appendValueInput('VECTOR1').setCheck('VectorF')
        .appendField('vector1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VECTOR2').setCheck('VectorF')
        .appendField('vector2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates vector1 to be the sum of itself and vector2.');
  }
};

Blockly.JavaScript['vectorF_add_withVector'] = function(block) {
  var vector1 = Blockly.JavaScript.valueToCode(
      block, 'VECTOR1', Blockly.JavaScript.ORDER_COMMA);
  var vector2 = Blockly.JavaScript.valueToCode(
      block, 'VECTOR2', Blockly.JavaScript.ORDER_COMMA);
  return vectorFIdentifierForJavaScript + '.add_withVector(' + vector1 + ', ' + vector2 + ');\n';
};

Blockly.FtcJava['vectorF_add_withVector'] = function(block) {
  var vector1 = Blockly.FtcJava.valueToCode(
      block, 'VECTOR1', Blockly.FtcJava.ORDER_MEMBER);
  var vector2 = Blockly.FtcJava.valueToCode(
      block, 'VECTOR2', Blockly.FtcJava.ORDER_NONE);
  return vector1 + '.add(' + vector2 + ');\n';
};

Blockly.Blocks['vectorF_subtracted_withMatrix'] = {
  init: function() {
    this.setOutput(true, 'MatrixF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('subtracted'));
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MATRIX').setCheck(['MatrixF', 'OpenGLMatrix'])
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a matrix representing the result of subtracting the given matrix from ' +
        'the given vector, taken as a row vector.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'MATRIX':
          return 'MatrixF';
      }
      return '';
    };
  }
};

Blockly.JavaScript['vectorF_subtracted_withMatrix'] = function(block) {
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  var code = vectorFIdentifierForJavaScript + '.subtracted_withMatrix(' + vector + ', ' + matrix + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vectorF_subtracted_withMatrix'] = function(block) {
  var vector = Blockly.FtcJava.valueToCode(
      block, 'VECTOR', Blockly.FtcJava.ORDER_MEMBER);
  var matrix = Blockly.FtcJava.valueToCode(
      block, 'MATRIX', Blockly.FtcJava.ORDER_NONE);
  var code = vector + '.subtracted(' + matrix + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vectorF_subtracted_withVector'] = {
  init: function() {
    this.setOutput(true, 'VectorF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('subtracted'));
    this.appendValueInput('VECTOR1').setCheck('VectorF')
        .appendField('vector1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VECTOR2').setCheck('VectorF')
        .appendField('vector2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a vector representing the result of subtracting vector2 from vector1.');
  }
};

Blockly.JavaScript['vectorF_subtracted_withVector'] = function(block) {
  var vector1 = Blockly.JavaScript.valueToCode(
      block, 'VECTOR1', Blockly.JavaScript.ORDER_COMMA);
  var vector2 = Blockly.JavaScript.valueToCode(
      block, 'VECTOR2', Blockly.JavaScript.ORDER_COMMA);
  var code = vectorFIdentifierForJavaScript + '.subtracted_withVector(' + vector1 + ', ' + vector2 + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vectorF_subtracted_withVector'] = function(block) {
  var vector1 = Blockly.FtcJava.valueToCode(
      block, 'VECTOR1', Blockly.FtcJava.ORDER_MEMBER);
  var vector2 = Blockly.FtcJava.valueToCode(
      block, 'VECTOR2', Blockly.FtcJava.ORDER_NONE);
  var code = vector1 + '.subtracted(' + vector2 + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vectorF_subtract_withVector'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('subtract'));
    this.appendValueInput('VECTOR1').setCheck('VectorF')
        .appendField('vector1')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VECTOR2').setCheck('VectorF')
        .appendField('vector2')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates vector1 to be the difference of itself and vector2.');
  }
};

Blockly.JavaScript['vectorF_subtract_withVector'] = function(block) {
  var vector1 = Blockly.JavaScript.valueToCode(
      block, 'VECTOR1', Blockly.JavaScript.ORDER_COMMA);
  var vector2 = Blockly.JavaScript.valueToCode(
      block, 'VECTOR2', Blockly.JavaScript.ORDER_COMMA);
  return vectorFIdentifierForJavaScript + '.subtract_withVector(' + vector1 + ', ' + vector2 + ');\n';
};

Blockly.FtcJava['vectorF_subtract_withVector'] = function(block) {
  var vector1 = Blockly.FtcJava.valueToCode(
      block, 'VECTOR1', Blockly.FtcJava.ORDER_MEMBER);
  var vector2 = Blockly.FtcJava.valueToCode(
      block, 'VECTOR2', Blockly.FtcJava.ORDER_NONE);
  return vector1 + '.subtract(' + vector2 + ');\n';
};

Blockly.Blocks['vectorF_multiplied_withScale'] = {
  init: function() {
    this.setOutput(true, 'VectorF');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('multiplied'));
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE').setCheck('Number')
        .appendField('scale')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a new vector containing the elements of this ' +
        'vector scaled by the given scale value.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'SCALE':
          return 'float';
      }
      return '';
    };
  }
};

Blockly.JavaScript['vectorF_multiplied_withScale'] = function(block) {
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  var scale = Blockly.JavaScript.valueToCode(
      block, 'SCALE', Blockly.JavaScript.ORDER_COMMA);
  var code = vectorFIdentifierForJavaScript + '.multiplied_withScale(' + vector + ', ' + scale + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vectorF_multiplied_withScale'] = function(block) {
  var vector = Blockly.FtcJava.valueToCode(
      block, 'VECTOR', Blockly.FtcJava.ORDER_MEMBER);
  var scale = Blockly.FtcJava.valueToCode(
      block, 'SCALE', Blockly.FtcJava.ORDER_NONE);
  var code = vector + '.multiplied(' + scale + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vectorF_multiply_withScale'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VectorF'))
        .appendField('.')
        .appendField(createNonEditableField('multiply'));
    this.appendValueInput('VECTOR').setCheck('VectorF')
        .appendField('vector')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SCALE').setCheck('Number')
        .appendField('scale')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Updates the given vector scaling its elements by the given scale value.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'SCALE':
          return 'float';
      }
      return '';
    };
  }
};

Blockly.JavaScript['vectorF_multiply_withScale'] = function(block) {
  var vector = Blockly.JavaScript.valueToCode(
      block, 'VECTOR', Blockly.JavaScript.ORDER_COMMA);
  var scale = Blockly.JavaScript.valueToCode(
      block, 'SCALE', Blockly.JavaScript.ORDER_COMMA);
  return vectorFIdentifierForJavaScript + '.multiply_withScale(' + vector + ', ' + scale + ');\n';
};

Blockly.FtcJava['vectorF_multiply_withScale'] = function(block) {
  var vector = Blockly.FtcJava.valueToCode(
      block, 'VECTOR', Blockly.FtcJava.ORDER_MEMBER);
  var scale = Blockly.FtcJava.valueToCode(
      block, 'SCALE', Blockly.FtcJava.ORDER_NONE);
  return vector + '.multiply(' + scale + ');\n';
};
