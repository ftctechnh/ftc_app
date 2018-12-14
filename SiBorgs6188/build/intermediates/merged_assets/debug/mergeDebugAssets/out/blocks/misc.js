/**
 * @fileoverview Comment blocks.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// miscIdentifierForJavaScript
// The following are defined in vars.js:
// commentColor
// functionColor

Blockly.Blocks['comment'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldTextInput(''), 'COMMENT');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(commentColor);
  }
};

Blockly.JavaScript['comment'] = function(block) {
  return '';
};

Blockly.FtcJava['comment'] = function(block) {
  return '// ' + block.getFieldValue('COMMENT') + '\n';
};

Blockly.Blocks['misc_null'] = {
  init: function() {
    this.setOutput(true); // no type for null
    this.appendDummyInput()
        .appendField(createNonEditableField('null'));
    this.setColour(functionColor);
    this.setTooltip('null');
  }
};

Blockly.JavaScript['misc_null'] = function(block) {
  var code = miscIdentifierForJavaScript + '.getNull()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['misc_null'] = function(block) {
  return ['null', Blockly.FtcJava.ORDER_ATOMIC];
};

Blockly.Blocks['misc_isNull'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('isNull'));
    this.appendValueInput('VALUE') // all types allowed
        .appendField('value')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns true if the given value is null, false otherwise.');
  }
};

Blockly.JavaScript['misc_isNull'] = function(block) {
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  var code = miscIdentifierForJavaScript + '.isNull(' + value + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['misc_isNull'] = function(block) {
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_EQUALITY);
  var code = value + ' == null';
  return [code, Blockly.FtcJava.ORDER_EQUALITY];
};

Blockly.Blocks['misc_isNotNull'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('isNotNull'));
    this.appendValueInput('VALUE') // all types allowed
        .appendField('value')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns true if the given value is not null, false otherwise.');
  }
};

Blockly.JavaScript['misc_isNotNull'] = function(block) {
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  var code = miscIdentifierForJavaScript + '.isNotNull(' + value + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['misc_isNotNull'] = function(block) {
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_EQUALITY);
  var code = value + ' != null';
  return [code, Blockly.FtcJava.ORDER_EQUALITY];
};

Blockly.Blocks['misc_atan2'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('atan2');
    this.appendValueInput('Y').setCheck('Number')
        .appendField('y')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('X').setCheck('Number')
        .appendField('x')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(Blockly.Msg.MATH_HUE);
    this.setTooltip('Returns a numerical value between -180 and +180 degrees, representing ' +
        'the counterclockwise angle between the positive X axis, and the point (x, y).');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'Y':
        case 'X':
          return 'double';
      }
      return '';
    };
    this.getFtcJavaOutputType = function() {
      return 'double';
    };
  }
};

Blockly.JavaScript['misc_atan2'] = function(block) {
  var y = Blockly.JavaScript.valueToCode(
      block, 'Y', Blockly.JavaScript.ORDER_COMMA);
  var x = Blockly.JavaScript.valueToCode(
      block, 'X', Blockly.JavaScript.ORDER_COMMA);
  var code = 'Math.atan2(' + y + ', ' + x + ') / Math.PI * 180';
  return [code, Blockly.JavaScript.ORDER_DIVISION];
};

Blockly.FtcJava['misc_atan2'] = function(block) {
  var y = Blockly.FtcJava.valueToCode(
      block, 'Y', Blockly.FtcJava.ORDER_COMMA);
  var x = Blockly.FtcJava.valueToCode(
      block, 'X', Blockly.FtcJava.ORDER_COMMA);
  var code = 'Math.atan2(' + y + ', ' + x + ') / Math.PI * 180';
  return [code, Blockly.FtcJava.ORDER_MULTIPLICATION];
};

Blockly.Blocks['misc_formatNumber'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('formatNumber'));
    this.appendValueInput('NUMBER').setCheck('Number')
        .appendField('number')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('PRECISION').setCheck('Number')
        .appendField('precision')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a text value of the given number formatted with the given precision, padded with zeros if necessary.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'NUMBER':
          return 'double';
        case 'PRECISION':
          return 'int';
      }
      return '';
    };
  }
};

Blockly.JavaScript['misc_formatNumber'] = function(block) {
  var number = Blockly.JavaScript.valueToCode(
      block, 'NUMBER', Blockly.JavaScript.ORDER_COMMA);
  var precision = Blockly.JavaScript.valueToCode(
      block, 'PRECISION', Blockly.JavaScript.ORDER_COMMA);
  var code = miscIdentifierForJavaScript + '.formatNumber(' + number + ', ' + precision + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['misc_formatNumber'] = function(block) {
  var number = Blockly.FtcJava.valueToCode(
      block, 'NUMBER', Blockly.FtcJava.ORDER_COMMA);
  var precision = Blockly.FtcJava.valueToCode(
      block, 'PRECISION', Blockly.FtcJava.ORDER_COMMA);
  // Due to issues with floating point precision, we always call the JavaUtil method.
  Blockly.FtcJava.generateImport_('JavaUtil');
  var code = 'JavaUtil.formatNumber(' + number + ', ' + precision + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['misc_roundDecimal'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('roundDecimal'));
    this.appendValueInput('NUMBER').setCheck('Number')
        .appendField('number')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('PRECISION').setCheck('Number')
        .appendField('precision')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a numeric value of the given number rounded to the given precision.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'NUMBER':
          return 'double';
        case 'PRECISION':
          return 'int';
      }
      return '';
    };
  }
};

Blockly.JavaScript['misc_roundDecimal'] = function(block) {
  var number = Blockly.JavaScript.valueToCode(
      block, 'NUMBER', Blockly.JavaScript.ORDER_COMMA);
  var precision = Blockly.JavaScript.valueToCode(
      block, 'PRECISION', Blockly.JavaScript.ORDER_COMMA);
  var code = miscIdentifierForJavaScript + '.roundDecimal(' + number + ', ' + precision + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['misc_roundDecimal'] = function(block) {
  var number = Blockly.FtcJava.valueToCode(
      block, 'NUMBER', Blockly.FtcJava.ORDER_COMMA);
  var precision = Blockly.FtcJava.valueToCode(
      block, 'PRECISION', Blockly.FtcJava.ORDER_COMMA);
  // Due to issues with floating point precision, we always call the JavaUtil method.
  Blockly.FtcJava.generateImport_('JavaUtil');
  var code = 'Double.parseDouble(JavaUtil.formatNumber(' + number + ', ' + precision + '))';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['misc_addItemToList'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('add item');
    this.appendValueInput('ITEM');
    this.appendDummyInput()
        .appendField('to list');
    this.appendValueInput('LIST')
        .setCheck('Array');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(Blockly.Msg.LISTS_HUE);
    this.setTooltip('Add the item to the end of the list.');
  }
};

Blockly.JavaScript['misc_addItemToList'] = function(block) {
  var item = Blockly.JavaScript.valueToCode(
      block, 'ITEM', Blockly.JavaScript.ORDER_NONE);
  var list = Blockly.JavaScript.valueToCode(
      block, 'LIST', Blockly.JavaScript.ORDER_MEMBER);
  return list + '.push(' + item + ')';
};

Blockly.FtcJava['misc_addItemToList'] = function(block) {
  var item = Blockly.FtcJava.valueToCode(
      block, 'ITEM', Blockly.FtcJava.ORDER_NONE);
  var list = Blockly.FtcJava.valueToCode(
      block, 'LIST', Blockly.FtcJava.ORDER_MEMBER);
  return list + '.add(' + item + ')';
};
