/**
 * @fileoverview FTC robot blocks related to servos.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createServoDropdown
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor
// setPropertyColor

Blockly.Blocks['servo_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
        ['Position', 'Position'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Sets the direction for the servo.'],
        ['Position', 'Sets the position for the servo.'],
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

Blockly.JavaScript['servo_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['servo_setProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'Servo');
  var property = block.getFieldValue('PROP');
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['servo_setProperty_Direction'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
    ];
    this.appendValueInput('VALUE').setCheck('Servo.Direction')
        .appendField('set')
        .appendField(createServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Sets the direction for the servo.'],
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

Blockly.JavaScript['servo_setProperty_Direction'] =
    Blockly.JavaScript['servo_setProperty'];

Blockly.FtcJava['servo_setProperty_Direction'] =
    Blockly.FtcJava['servo_setProperty'];

Blockly.Blocks['servo_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Position', 'Position'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Position', 'Sets the position for the servo.'],
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
    this.getFtcJavaInputType = function(inputName) {
      if (inputName == 'VALUE') {
        var property = thisBlock.getFieldValue('PROP');
        switch (property) {
          case 'Position':
            return 'double';
          default:
            throw 'Unexpected property ' + property + ' (servo_setProperty_Number getArgumentType).';
        }
      }
      return '';
    };
  }
};

Blockly.JavaScript['servo_setProperty_Number'] =
    Blockly.JavaScript['servo_setProperty'];

Blockly.FtcJava['servo_setProperty_Number'] =
    Blockly.FtcJava['servo_setProperty'];

Blockly.Blocks['servo_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
        ['Position', 'Position'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Returns the direction of the servo.'],
        ['Position', 'Returns the position of the servo.'],
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

Blockly.JavaScript['servo_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['servo_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'Servo');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['servo_getProperty_Direction'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
    ];
    this.setOutput(true, 'Servo.Direction');
    this.appendDummyInput()
        .appendField(createServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Returns the direction of the servo.'],
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

Blockly.JavaScript['servo_getProperty_Direction'] =
    Blockly.JavaScript['servo_getProperty'];

Blockly.FtcJava['servo_getProperty_Direction'] =
    Blockly.FtcJava['servo_getProperty'];

Blockly.Blocks['servo_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Position', 'Position'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Position', 'Returns the position of the servo.'],
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
        case 'Position':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (servo_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['servo_getProperty_Number'] =
    Blockly.JavaScript['servo_getProperty'];

Blockly.FtcJava['servo_getProperty_Number'] =
    Blockly.FtcJava['servo_getProperty'];

// Enums

Blockly.Blocks['servo_enum_direction'] = {
  init: function() {
    var DIRECTION_CHOICES = [
        ['REVERSE', 'REVERSE'],
        ['FORWARD', 'FORWARD'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('Direction'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(DIRECTION_CHOICES), 'DIRECTION');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['REVERSE', 'The Direction value REVERSE.'],
        ['FORWARD', 'The Direction value FORWARD.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('DIRECTION');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['servo_enum_direction'] = function(block) {
  var code = '"' + block.getFieldValue('DIRECTION') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['servo_enum_direction'] = function(block) {
  var code = 'Servo.Direction.' + block.getFieldValue('DIRECTION');
  Blockly.FtcJava.generateImport_('Servo');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['servo_typedEnum_direction'] = {
  init: function() {
    var DIRECTION_CHOICES = [
        ['REVERSE', 'REVERSE'],
        ['FORWARD', 'FORWARD'],
    ];
    this.setOutput(true, 'Servo.Direction');
    this.appendDummyInput()
        .appendField(createNonEditableField('Direction'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(DIRECTION_CHOICES), 'DIRECTION');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['REVERSE', 'The Direction value REVERSE.'],
        ['FORWARD', 'The Direction value FORWARD.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('DIRECTION');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['servo_typedEnum_direction'] =
    Blockly.JavaScript['servo_enum_direction'];

Blockly.FtcJava['servo_typedEnum_direction'] =
    Blockly.FtcJava['servo_enum_direction'];

// Functions

Blockly.Blocks['servo_scaleRange'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('scaleRange'));
    this.appendValueInput('MIN') // no type, for compatibility
        .appendField('min')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MAX') // no type, for compatibility
        .appendField('max')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Scale the available movement range of the servo to be a subset of its ' +
        'maximum range.');
  }
};

Blockly.JavaScript['servo_scaleRange'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var min = Blockly.JavaScript.valueToCode(
      block, 'MIN', Blockly.JavaScript.ORDER_COMMA);
  var max = Blockly.JavaScript.valueToCode(
      block, 'MAX', Blockly.JavaScript.ORDER_COMMA);
  return identifier + '.scaleRange(' + min + ', ' + max + ');\n';
};

Blockly.FtcJava['servo_scaleRange'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'Servo');
  var min = Blockly.FtcJava.valueToCode(
      block, 'MIN', Blockly.FtcJava.ORDER_COMMA);
  var max = Blockly.FtcJava.valueToCode(
      block, 'MAX', Blockly.FtcJava.ORDER_COMMA);
  return identifier + '.scaleRange(' + min + ', ' + max + ');\n';
};

Blockly.Blocks['servo_scaleRange_Number'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('scaleRange'));
    this.appendValueInput('MIN').setCheck('Number')
        .appendField('min')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MAX').setCheck('Number')
        .appendField('max')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Scale the available movement range of the servo to be a subset of its ' +
        'maximum range.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'MIN':
        case 'MAX':
          return 'double';
      }
      return '';
    };
  }
};

Blockly.JavaScript['servo_scaleRange_Number'] =
    Blockly.JavaScript['servo_scaleRange'];

Blockly.FtcJava['servo_scaleRange_Number'] =
    Blockly.FtcJava['servo_scaleRange'];
