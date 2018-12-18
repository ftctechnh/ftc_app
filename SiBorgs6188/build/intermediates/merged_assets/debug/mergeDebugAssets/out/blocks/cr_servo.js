/**
 * @fileoverview FTC robot blocks related to continuous rotation servos.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createCRServoDropdown
// The following are defined in vars.js:
// getPropertyColor
// setPropertyColor

Blockly.Blocks['crServo_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
        ['Power', 'Power'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createCRServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Sets the Direction for the CR servo.'],
        ['Power', 'Sets the power for the CR servo. Valid values are between -1.0 and 1.0.'],
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

Blockly.JavaScript['crServo_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['crServo_setProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'CRServo');
  var property = block.getFieldValue('PROP');
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['crServo_setProperty_Direction'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
    ];
    this.appendValueInput('VALUE').setCheck('DcMotorSimple.Direction')
        .appendField('set')
        .appendField(createCRServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Sets the Direction for the CR servo.'],
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

Blockly.JavaScript['crServo_setProperty_Direction'] =
    Blockly.JavaScript['crServo_setProperty'];

Blockly.FtcJava['crServo_setProperty_Direction'] =
    Blockly.FtcJava['crServo_setProperty'];

Blockly.Blocks['crServo_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Power', 'Power'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createCRServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Power', 'Sets the power for the CR servo. Valid values are between -1.0 and 1.0.'],
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
          case 'Power':
            return 'double';
          default:
            throw 'Unexpected property ' + property + ' (crServo_setProperty_Number getArgumentType).';
        }
      }
      return '';
    };
  }
};

Blockly.JavaScript['crServo_setProperty_Number'] =
    Blockly.JavaScript['crServo_setProperty'];

Blockly.FtcJava['crServo_setProperty_Number'] =
    Blockly.FtcJava['crServo_setProperty'];

Blockly.Blocks['crServo_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
        ['Power', 'Power'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createCRServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Returns the Direction of the CR servo.'],
        ['Power', 'Returns the power of the CR servo.'],
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

Blockly.JavaScript['crServo_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['crServo_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'CRServo');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['crServo_getProperty_Direction'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
    ];
    this.setOutput(true, 'DcMotorSimple.Direction');
    this.appendDummyInput()
        .appendField(createCRServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Returns the Direction of the CR servo.'],
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

Blockly.JavaScript['crServo_getProperty_Direction'] =
    Blockly.JavaScript['crServo_getProperty'];

Blockly.FtcJava['crServo_getProperty_Direction'] =
    Blockly.FtcJava['crServo_getProperty'];

Blockly.Blocks['crServo_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Power', 'Power'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createCRServoDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Power', 'Returns the power of the CR servo.'],
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
        case 'Power':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (crServo_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['crServo_getProperty_Number'] =
    Blockly.JavaScript['crServo_getProperty'];

Blockly.FtcJava['crServo_getProperty_Number'] =
    Blockly.FtcJava['crServo_getProperty'];

// Enums

Blockly.Blocks['crServo_enum_direction'] = {
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

Blockly.JavaScript['crServo_enum_direction'] = function(block) {
  var code = '"' + block.getFieldValue('DIRECTION') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['crServo_enum_direction'] = function(block) {
  var code = 'DcMotorSimple.Direction.' + block.getFieldValue('DIRECTION');
  Blockly.FtcJava.generateImport_('DcMotorSimple');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['crServo_typedEnum_direction'] = {
  init: function() {
    var DIRECTION_CHOICES = [
        ['REVERSE', 'REVERSE'],
        ['FORWARD', 'FORWARD'],
    ];
    this.setOutput(true, 'DcMotorSimple.Direction');
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

Blockly.JavaScript['crServo_typedEnum_direction'] =
    Blockly.JavaScript['crServo_enum_direction'];

Blockly.FtcJava['crServo_typedEnum_direction'] =
    Blockly.FtcJava['crServo_enum_direction'];
