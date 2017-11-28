/**
 * @fileoverview FTC robot blocks related to DC motors.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createDcMotorDropdown
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor
// setPropertyColor

Blockly.Blocks['dcMotor_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
        ['MaxSpeed', 'MaxSpeed'],
        ['Mode', 'Mode'],
        ['Power', 'Power'],
        ['TargetPosition', 'TargetPosition'],
        ['ZeroPowerBehavior', 'ZeroPowerBehavior'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Sets the direction for the motor.'],
        ['MaxSpeed', 'This block does nothing. MaxSpeed is deprecated.'],
        ['Mode', 'Sets the run mode for the motor.'],
        ['Power', 'Sets the power for the motor. ' +
            'Power values must be between -1.0 and 1.0.'],
        ['TargetPosition', 'Sets the target position for the motor.'],
        ['ZeroPowerBehavior', 'Sets the zero power behavior for the motor.'],
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['dcMotor_setProperty_Direction'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
    ];
    this.appendValueInput('VALUE').setCheck('Direction')
        .appendField('set')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Sets the direction for the motor.'],
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_setProperty_Direction'] =
    Blockly.JavaScript['dcMotor_setProperty'];

Blockly.Blocks['dcMotor_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['MaxSpeed', 'MaxSpeed'],
        ['Power', 'Power'],
        ['TargetPosition', 'TargetPosition'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MaxSpeed', 'This block does nothing. MaxSpeed is deprecated.'],
        ['Power', 'Sets the power for the motor. ' +
            'Power values must be between -1.0 and 1.0.'],
        ['TargetPosition', 'Sets the target position for the motor.'],
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_setProperty_Number'] =
    Blockly.JavaScript['dcMotor_setProperty'];

Blockly.Blocks['dcMotor_setProperty_RunMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.appendValueInput('VALUE').setCheck('RunMode')
        .appendField('set')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'Sets the run mode for the motor.'],
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_setProperty_RunMode'] =
    Blockly.JavaScript['dcMotor_setProperty'];

Blockly.Blocks['dcMotor_setProperty_ZeroPowerBehavior'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['ZeroPowerBehavior', 'ZeroPowerBehavior'],
    ];
    this.appendValueInput('VALUE').setCheck('ZeroPowerBehavior')
        .appendField('set')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['ZeroPowerBehavior', 'Sets the zero power behavior for the motor.'],
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_setProperty_ZeroPowerBehavior'] =
    Blockly.JavaScript['dcMotor_setProperty'];

Blockly.Blocks['dcMotor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['CurrentPosition', 'CurrentPosition'],
        ['Direction', 'Direction'],
        ['MaxSpeed', 'MaxSpeed'],
        ['Mode', 'Mode'],
        ['Power', 'Power'],
        ['PowerFloat', 'PowerFloat'],
        ['TargetPosition', 'TargetPosition'],
        ['ZeroPowerBehavior', 'ZeroPowerBehavior'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CurrentPosition', 'Gets the current position of the motor.'],
        ['Direction', 'Gets the direction of the motor.'],
        ['MaxSpeed', 'This block always returns 0. MaxSpeed is deprecated.'],
        ['Mode', 'Gets the run mode of the motor.'],
        ['Power', 'Gets the power of the motor.'],
        ['PowerFloat', 'Tells whether the motor is currently in a float power level.'],
        ['TargetPosition', 'Gets the target position of the motor.'],
        ['ZeroPowerBehavior', 'Gets the current behavior of the motor were a power level of zero to be applied.'],
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['dcMotor_getProperty_Direction'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
    ];
    this.setOutput(true, 'Direction');
    this.appendDummyInput()
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Gets the direction of the motor.'],
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_getProperty_Direction'] =
    Blockly.JavaScript['dcMotor_getProperty'];

Blockly.Blocks['dcMotor_getProperty_Boolean'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['PowerFloat', 'PowerFloat'],
    ];
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['PowerFloat', 'Tells whether the motor is currently in a float power level.'],
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_getProperty_Boolean'] =
    Blockly.JavaScript['dcMotor_getProperty'];

Blockly.Blocks['dcMotor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['CurrentPosition', 'CurrentPosition'],
        ['MaxSpeed', 'MaxSpeed'],
        ['Power', 'Power'],
        ['TargetPosition', 'TargetPosition'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CurrentPosition', 'Gets the current position of the motor.'],
        ['MaxSpeed', 'This block always returns 0. MaxSpeed is deprecated.'],
        ['Power', 'Gets the power of the motor.'],
        ['TargetPosition', 'Gets the target position of the motor.'],
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_getProperty_Number'] =
    Blockly.JavaScript['dcMotor_getProperty'];

Blockly.Blocks['dcMotor_getProperty_RunMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.setOutput(true, 'RunMode');
    this.appendDummyInput()
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'Gets the run mode of the motor.'],
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_getProperty_RunMode'] =
    Blockly.JavaScript['dcMotor_getProperty'];

Blockly.Blocks['dcMotor_getProperty_ZeroPowerBehavior'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['ZeroPowerBehavior', 'ZeroPowerBehavior'],
    ];
    this.setOutput(true, 'ZeroPowerBehavior');
    this.appendDummyInput()
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['ZeroPowerBehavior', 'Gets the current behavior of the motor were a power level of zero to be applied.'],
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_getProperty_ZeroPowerBehavior'] =
    Blockly.JavaScript['dcMotor_getProperty'];

// Dual property setters

Blockly.Blocks['dcMotor_setDualProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['MaxSpeed', 'MaxSpeed'],
        ['Mode', 'Mode'],
        ['Power', 'Power'],
        ['TargetPosition', 'TargetPosition'],
        ['ZeroPowerBehavior', 'ZeroPowerBehavior'],
    ];
    this.appendDummyInput()
        .appendField('set')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
    this.appendValueInput('VALUE1') // no type, for compatibility
        .appendField(createDcMotorDropdown(), 'IDENTIFIER1')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE2') // no type, for compatibility
        .appendField(createDcMotorDropdown(), 'IDENTIFIER2')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MaxSpeed', 'This block does nothing. MaxSpeed is deprecated.'],
        ['Mode', 'Sets the run mode for two motors.'],
        ['Power', 'Sets the power for two motors. ' +
            'Power values must be between -1.0 and 1.0.'],
        ['TargetPosition', 'Sets the target position for two motors.'],
        ['ZeroPowerBehavior', 'Sets the zero power behavior for two motors.'],
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_setDualProperty'] = function(block) {
  var identifier1 = block.getFieldValue('IDENTIFIER1');
  var identifier2 = block.getFieldValue('IDENTIFIER2');
  var property = block.getFieldValue('PROP');
  var value1 = Blockly.JavaScript.valueToCode(
      block, 'VALUE1', Blockly.JavaScript.ORDER_COMMA);
  var value2 = Blockly.JavaScript.valueToCode(
      block, 'VALUE2', Blockly.JavaScript.ORDER_COMMA);
  return identifier1 + '.setDual' + property + '(' + value1 + ', ' +
      identifier2 + ', ' + value2 + ');\n';
};

Blockly.Blocks['dcMotor_setDualProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['MaxSpeed', 'MaxSpeed'],
        ['Power', 'Power'],
        ['TargetPosition', 'TargetPosition'],
    ];
    this.appendDummyInput()
        .appendField('set')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
    this.appendValueInput('VALUE1').setCheck('Number')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER1')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE2').setCheck('Number')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER2')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MaxSpeed', 'This block does nothing. MaxSpeed is deprecated.'],
        ['Power', 'Sets the power for two motors. ' +
            'Power values must be between -1.0 and 1.0.'],
        ['TargetPosition', 'Sets the target position for two motors.'],
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_setDualProperty_Number'] =
    Blockly.JavaScript['dcMotor_setDualProperty'];


Blockly.Blocks['dcMotor_setDualProperty_RunMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.appendDummyInput()
        .appendField('set')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
    this.appendValueInput('VALUE1').setCheck('RunMode')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER1')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE2').setCheck('RunMode')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER2')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'Sets the run mode for two motors.'],
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_setDualProperty_RunMode'] =
    Blockly.JavaScript['dcMotor_setDualProperty'];


Blockly.Blocks['dcMotor_setDualProperty_ZeroPowerBehavior'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['ZeroPowerBehavior', 'ZeroPowerBehavior'],
    ];
    this.appendDummyInput()
        .appendField('set')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
    this.appendValueInput('VALUE1').setCheck('ZeroPowerBehavior')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER1')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE2').setCheck('ZeroPowerBehavior')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER2')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['ZeroPowerBehavior', 'Sets the zero power behavior for two motors.'],
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
    this.setColour(setPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_setDualProperty_ZeroPowerBehavior'] =
    Blockly.JavaScript['dcMotor_setDualProperty'];

// Enums

Blockly.Blocks['dcMotor_enum_runMode'] = {
  init: function() {
    var RUN_MODE_CHOICES = [
        ['RUN_WITHOUT_ENCODER', 'RUN_WITHOUT_ENCODER'],
        ['RUN_USING_ENCODER', 'RUN_USING_ENCODER'],
        ['RUN_TO_POSITION', 'RUN_TO_POSITION'],
        ['STOP_AND_RESET_ENCODER', 'STOP_AND_RESET_ENCODER'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('RunMode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(RUN_MODE_CHOICES), 'RUN_MODE');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['RUN_WITHOUT_ENCODER', 'The RunMode value RUN_WITHOUT_ENCODER.'],
        ['RUN_USING_ENCODER', 'The RunMode value RUN_USING_ENCODER.'],
        ['RUN_TO_POSITION', 'The RunMode value RUN_TO_POSITION.'],
        ['STOP_AND_RESET_ENCODER', 'The RunMode value STOP_AND_RESET_ENCODER.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('RUN_MODE');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_enum_runMode'] = function(block) {
  var code = '"' + block.getFieldValue('RUN_MODE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['dcMotor_typedEnum_runMode'] = {
  init: function() {
    var RUN_MODE_CHOICES = [
        ['RUN_WITHOUT_ENCODER', 'RUN_WITHOUT_ENCODER'],
        ['RUN_USING_ENCODER', 'RUN_USING_ENCODER'],
        ['RUN_TO_POSITION', 'RUN_TO_POSITION'],
        ['STOP_AND_RESET_ENCODER', 'STOP_AND_RESET_ENCODER'],
    ];
    this.setOutput(true, 'RunMode');
    this.appendDummyInput()
        .appendField(createNonEditableField('RunMode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(RUN_MODE_CHOICES), 'RUN_MODE');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['RUN_WITHOUT_ENCODER', 'The RunMode value RUN_WITHOUT_ENCODER.'],
        ['RUN_USING_ENCODER', 'The RunMode value RUN_USING_ENCODER.'],
        ['RUN_TO_POSITION', 'The RunMode value RUN_TO_POSITION.'],
        ['STOP_AND_RESET_ENCODER', 'The RunMode value STOP_AND_RESET_ENCODER.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('RUN_MODE');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_typedEnum_runMode'] =
    Blockly.JavaScript['dcMotor_enum_runMode'];

Blockly.Blocks['dcMotor_enum_direction'] = {
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_enum_direction'] = function(block) {
  var code = '"' + block.getFieldValue('DIRECTION') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['dcMotor_typedEnum_direction'] = {
  init: function() {
    var DIRECTION_CHOICES = [
        ['REVERSE', 'REVERSE'],
        ['FORWARD', 'FORWARD'],
    ];
    this.setOutput(true, 'Direction');
    this.appendDummyInput()
        .appendField(createNonEditableField('Direction'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(DIRECTION_CHOICES), 'DIRECTION');
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_typedEnum_direction'] =
    Blockly.JavaScript['dcMotor_enum_direction'];

Blockly.Blocks['dcMotor_enum_zeroPowerBehavior'] = {
  init: function() {
    var ZERO_POWER_BEHAVIOR_CHOICES = [
        ['BRAKE', 'BRAKE'],
        ['FLOAT', 'FLOAT'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('ZeroPowerBehavior'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(ZERO_POWER_BEHAVIOR_CHOICES), 'ZERO_POWER_BEHAVIOR');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['BRAKE', 'The ZeroPowerBehavior value BRAKE.'],
        ['FLOAT', 'The ZeroPowerBehavior value FLOAT.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('ZERO_POWER_BEHAVIOR');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_enum_zeroPowerBehavior'] = function(block) {
  var code = '"' + block.getFieldValue('ZERO_POWER_BEHAVIOR') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['dcMotor_typedEnum_zeroPowerBehavior'] = {
  init: function() {
    var ZERO_POWER_BEHAVIOR_CHOICES = [
        ['BRAKE', 'BRAKE'],
        ['FLOAT', 'FLOAT'],
    ];
    this.setOutput(true, 'ZeroPowerBehavior');
    this.appendDummyInput()
        .appendField(createNonEditableField('ZeroPowerBehavior'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(ZERO_POWER_BEHAVIOR_CHOICES), 'ZERO_POWER_BEHAVIOR');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['BRAKE', 'The ZeroPowerBehavior value BRAKE.'],
        ['FLOAT', 'The ZeroPowerBehavior value FLOAT.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('ZERO_POWER_BEHAVIOR');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['dcMotor_typedEnum_zeroPowerBehavior'] =
    Blockly.JavaScript['dcMotor_enum_zeroPowerBehavior'];

// Functions

Blockly.Blocks['dcMotor_isBusy'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isBusy'));
    this.setTooltip('Is the motor is currently advancing or retreating to a target position?');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['dcMotor_isBusy'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isBusy()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
