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
        ['TargetPositionTolerance', 'TargetPositionTolerance'],
        ['Velocity', 'Velocity'],
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
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Sets the Direction for the motor.'],
        ['MaxSpeed', 'This block does nothing. MaxSpeed is deprecated.'],
        ['Mode', 'Sets the RunMode for the motor.'],
        ['Power', 'Sets the power for the motor. Valid values are between -1.0 and 1.0.'],
        ['TargetPosition', 'Sets the target position for the motor.'],
        ['TargetPositionTolerance', 'Sets the target positioning tolerance for the motor. ' +
            'Not all DcMotors support this feature.'],
        ['Velocity', 'Sets the velocity for the motor in ticks per second. ' +
            'Not all DcMotors support this feature.'],
        ['ZeroPowerBehavior', 'Sets the ZeroPowerBehavior for the motor. Valid values are BRAKE or FLOAT.'],
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

Blockly.JavaScript['dcMotor_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['dcMotor_setProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  if (property == 'MaxSpeed') {
    return '';
  }
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DcMotor');
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_NONE);
  if (property == 'TargetPositionTolerance' || property == 'Velocity') {
    // This java code will throw ClassCastException if the DcMotor is not a DcMotorEx.
    Blockly.FtcJava.generateImport_('DcMotorEx');
    return '((DcMotorEx) ' + identifier + ').set' + property + '(' + value + ');\n';
  } else {
    return identifier + '.set' + property + '(' + value + ');\n';
  }
};

Blockly.Blocks['dcMotor_setProperty_Direction'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
    ];
    this.appendValueInput('VALUE').setCheck('DcMotorSimple.Direction')
        .appendField('set')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Sets the Direction for the motor.'],
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

Blockly.JavaScript['dcMotor_setProperty_Direction'] =
    Blockly.JavaScript['dcMotor_setProperty'];

Blockly.FtcJava['dcMotor_setProperty_Direction'] =
    Blockly.FtcJava['dcMotor_setProperty'];

Blockly.Blocks['dcMotor_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['MaxSpeed', 'MaxSpeed'],
        ['Power', 'Power'],
        ['TargetPosition', 'TargetPosition'],
        ['TargetPositionTolerance', 'TargetPositionTolerance'],
        ['Velocity', 'Velocity'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MaxSpeed', 'This block does nothing. MaxSpeed is deprecated.'],
        ['Power', 'Sets the power for the motor. Valid values are between -1.0 and 1.0.'],
        ['TargetPosition', 'Sets the target position for the motor.'],
        ['TargetPositionTolerance', 'Sets the target positioning tolerance for the motor. ' +
            'Not all DcMotors support this feature.'],
        ['Velocity', 'Sets the velocity for the motor in ticks per second. ' +
            'Not all DcMotors support this feature.'],
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
          case 'MaxSpeed':
          case 'Power':
          case 'Velocity':
            return 'double';
          case 'TargetPosition':
          case 'TargetPositionTolerance':
            return 'int';
          default:
            throw 'Unexpected property ' + property + ' (dcMotor_setProperty_Number getArgumentType).';
        }
      }
      return '';
    };
  }
};

Blockly.JavaScript['dcMotor_setProperty_Number'] =
    Blockly.JavaScript['dcMotor_setProperty'];

Blockly.FtcJava['dcMotor_setProperty_Number'] =
    Blockly.FtcJava['dcMotor_setProperty'];

Blockly.Blocks['dcMotor_setProperty_RunMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.appendValueInput('VALUE').setCheck('DcMotor.RunMode')
        .appendField('set')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'Sets the RunMode for the motor.'],
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

Blockly.JavaScript['dcMotor_setProperty_RunMode'] =
    Blockly.JavaScript['dcMotor_setProperty'];

Blockly.FtcJava['dcMotor_setProperty_RunMode'] =
    Blockly.FtcJava['dcMotor_setProperty'];

Blockly.Blocks['dcMotor_setProperty_ZeroPowerBehavior'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['ZeroPowerBehavior', 'ZeroPowerBehavior'],
    ];
    this.appendValueInput('VALUE').setCheck('DcMotor.ZeroPowerBehavior')
        .appendField('set')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['ZeroPowerBehavior', 'Sets the ZeroPowerBehavior for the motor. Valid values are BRAKE or FLOAT.'],
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

Blockly.JavaScript['dcMotor_setProperty_ZeroPowerBehavior'] =
    Blockly.JavaScript['dcMotor_setProperty'];

Blockly.FtcJava['dcMotor_setProperty_ZeroPowerBehavior'] =
    Blockly.FtcJava['dcMotor_setProperty'];

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
        ['TargetPositionTolerance', 'TargetPositionTolerance'],
        ['Velocity', 'Velocity'],
        ['ZeroPowerBehavior', 'ZeroPowerBehavior'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CurrentPosition', 'Returns the current position of the motor.'],
        ['Direction', 'Returns the Direction of the motor.'],
        ['MaxSpeed', 'This block always returns 0. MaxSpeed is deprecated.'],
        ['Mode', 'Returns the RunMode of the motor.'],
        ['Power', 'Returns the power of the motor.'],
        ['PowerFloat', 'Returns true if the motor is currently in a float power level.'],
        ['TargetPosition', 'Returns the target position of the motor.'],
        ['TargetPositionTolerance', 'Returns the current target positioning tolerance of the motor. ' +
            'Not all DcMotors support this feature.'],
        ['Velocity', 'Returns the current velocity of the motor, in ticks per second. ' +
            'Not all DcMotors support this feature.'],
        ['ZeroPowerBehavior', 'Returns the current ZeroPowerBehavior of the motor: BRAKE or FLOAT.'],
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

Blockly.JavaScript['dcMotor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['dcMotor_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  if (property == 'MaxSpeed') {
    return ['0', Blockly.FtcJava.ORDER_ATOMIC];
  }
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DcMotor');
  var code;
  if (property == 'TargetPositionTolerance' || property == 'Velocity') {
    // This java code will throw ClassCastException if the DcMotor is not a DcMotorEx.
    Blockly.FtcJava.generateImport_('DcMotorEx');
    code = '((DcMotorEx) ' + identifier + ').get' + property + '()';
  } else {
    code = identifier + '.get' + property + '()';
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['dcMotor_getProperty_Direction'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Direction', 'Direction'],
    ];
    this.setOutput(true, 'DcMotorSimple.Direction');
    this.appendDummyInput()
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Direction', 'Returns the Direction of the motor.'],
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

Blockly.JavaScript['dcMotor_getProperty_Direction'] =
    Blockly.JavaScript['dcMotor_getProperty'];

Blockly.FtcJava['dcMotor_getProperty_Direction'] =
    Blockly.FtcJava['dcMotor_getProperty'];

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
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['PowerFloat', 'Returns true if the motor is currently in a float power level.'],
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

Blockly.JavaScript['dcMotor_getProperty_Boolean'] =
    Blockly.JavaScript['dcMotor_getProperty'];

Blockly.FtcJava['dcMotor_getProperty_Boolean'] =
    Blockly.FtcJava['dcMotor_getProperty'];

Blockly.Blocks['dcMotor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['CurrentPosition', 'CurrentPosition'],
        ['MaxSpeed', 'MaxSpeed'],
        ['Power', 'Power'],
        ['TargetPosition', 'TargetPosition'],
        ['TargetPositionTolerance', 'TargetPositionTolerance'],
        ['Velocity', 'Velocity'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['CurrentPosition', 'Returns the current position of the motor.'],
        ['MaxSpeed', 'This block always returns 0. MaxSpeed is deprecated.'],
        ['Power', 'Returns the power of the motor.'],
        ['TargetPosition', 'Returns the target position of the motor.'],
        ['TargetPositionTolerance', 'Returns the current target positioning tolerance of the motor. ' +
            'Not all DcMotors support this feature.'],
        ['Velocity', 'Returns the current velocity of the motor, in ticks per second. ' +
            'Not all DcMotors support this feature.'],
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
        case 'MaxSpeed':
        case 'Power':
        case 'Velocity':
          return 'double';
        case 'CurrentPosition':
        case 'TargetPosition':
        case 'TargetPositionTolerance':
          return 'int';
        default:
          throw 'Unexpected property ' + property + ' (dcMotor_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['dcMotor_getProperty_Number'] =
    Blockly.JavaScript['dcMotor_getProperty'];

Blockly.FtcJava['dcMotor_getProperty_Number'] =
    Blockly.FtcJava['dcMotor_getProperty'];

Blockly.Blocks['dcMotor_getProperty_RunMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.setOutput(true, 'DcMotor.RunMode');
    this.appendDummyInput()
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'Returns the RunMode of the motor.'],
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

Blockly.JavaScript['dcMotor_getProperty_RunMode'] =
    Blockly.JavaScript['dcMotor_getProperty'];

Blockly.FtcJava['dcMotor_getProperty_RunMode'] =
    Blockly.FtcJava['dcMotor_getProperty'];

Blockly.Blocks['dcMotor_getProperty_ZeroPowerBehavior'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['ZeroPowerBehavior', 'ZeroPowerBehavior'],
    ];
    this.setOutput(true, 'DcMotor.ZeroPowerBehavior');
    this.appendDummyInput()
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['ZeroPowerBehavior', 'Returns the current ZeroPowerBehavior of the motor: BRAKE or FLOAT.'],
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

Blockly.JavaScript['dcMotor_getProperty_ZeroPowerBehavior'] =
    Blockly.JavaScript['dcMotor_getProperty'];

Blockly.FtcJava['dcMotor_getProperty_ZeroPowerBehavior'] =
    Blockly.FtcJava['dcMotor_getProperty'];

// Dual property setters

Blockly.Blocks['dcMotor_setDualProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['MaxSpeed', 'MaxSpeed'],
        ['Mode', 'Mode'],
        ['Power', 'Power'],
        ['TargetPosition', 'TargetPosition'],
        ['TargetPositionTolerance', 'TargetPositionTolerance'],
        ['Velocity', 'Velocity'],
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
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MaxSpeed', 'This block does nothing. MaxSpeed is deprecated.'],
        ['Mode', 'Sets the RunMode for two motors.'],
        ['Power', 'Sets the power for two motors. Valid values are between -1.0 and 1.0.'],
        ['TargetPosition', 'Sets the target position for two motors.'],
        ['TargetPositionTolerance', 'Sets the target positioning tolerance for two motors. ' +
            'Not all DcMotors support this feature.'],
        ['Velocity', 'Sets the velocity for two motors in ticks per second. ' +
            'Not all DcMotors support this feature.'],
        ['ZeroPowerBehavior', 'Sets the ZeroPowerBehavior for two motors. Valid values are BRAKE or FLOAT.'],
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

Blockly.FtcJava['dcMotor_setDualProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  if (property == 'MaxSpeed') {
    return '';
  }
  var identifier1 = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER1', 'DcMotor');
  var identifier2 = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER2', 'DcMotor');
  var value1 = Blockly.FtcJava.valueToCode(
      block, 'VALUE1', Blockly.FtcJava.ORDER_COMMA);
  var value2 = Blockly.FtcJava.valueToCode(
      block, 'VALUE2', Blockly.FtcJava.ORDER_COMMA);
  if (property == 'TargetPositionTolerance' || property == 'Velocity') {
    // This java code will throw ClassCastException if the DcMotor is not a DcMotorEx.
    Blockly.FtcJava.generateImport_('DcMotorEx');
    return '((DcMotorEx) ' + identifier1 + ').set' + property + '(' + value1 + ');\n' +
        '((DcMotorEx) ' + identifier2 + ').set' + property + '(' + value2 + ');\n';
  } else {
    return identifier1 + '.set' + property + '(' + value1 + ');\n' +
        identifier2 + '.set' + property + '(' + value2 + ');\n';
  }
};

Blockly.Blocks['dcMotor_setDualProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['MaxSpeed', 'MaxSpeed'],
        ['Power', 'Power'],
        ['TargetPosition', 'TargetPosition'],
        ['TargetPositionTolerance', 'TargetPositionTolerance'],
        ['Velocity', 'Velocity'],
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
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MaxSpeed', 'This block does nothing. MaxSpeed is deprecated.'],
        ['Power', 'Sets the power for two motors. Valid values are between -1.0 and 1.0.'],
        ['TargetPosition', 'Sets the target position for two motors.'],
        ['TargetPositionTolerance', 'Sets the target positioning tolerance for two motors. ' +
            'Not all DcMotors support this feature.'],
        ['Velocity', 'Sets the velocity for two motors in ticks per second. ' +
            'Not all DcMotors support this feature.'],
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
      if (inputName == 'VALUE1' || inputName == 'VALUE2') {
        var property = thisBlock.getFieldValue('PROP');
        switch (property) {
          case 'MaxSpeed':
          case 'Power':
          case 'Velocity':
            return 'double';
          case 'TargetPosition':
          case 'TargetPositionTolerance':
            return 'int';
          default:
            throw 'Unexpected property ' + property + ' (dcMotor_setDualProperty_Number getArgumentType).';
        }
      }
      return '';
    };
  }
};

Blockly.JavaScript['dcMotor_setDualProperty_Number'] =
    Blockly.JavaScript['dcMotor_setDualProperty'];

Blockly.FtcJava['dcMotor_setDualProperty_Number'] =
    Blockly.FtcJava['dcMotor_setDualProperty'];


Blockly.Blocks['dcMotor_setDualProperty_RunMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.appendDummyInput()
        .appendField('set')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
    this.appendValueInput('VALUE1').setCheck('DcMotor.RunMode')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER1')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE2').setCheck('DcMotor.RunMode')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER2')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'Sets the RunMode for two motors.'],
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

Blockly.JavaScript['dcMotor_setDualProperty_RunMode'] =
    Blockly.JavaScript['dcMotor_setDualProperty'];

Blockly.FtcJava['dcMotor_setDualProperty_RunMode'] =
    Blockly.FtcJava['dcMotor_setDualProperty'];

Blockly.Blocks['dcMotor_setDualProperty_ZeroPowerBehavior'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['ZeroPowerBehavior', 'ZeroPowerBehavior'],
    ];
    this.appendDummyInput()
        .appendField('set')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
    this.appendValueInput('VALUE1').setCheck('DcMotor.ZeroPowerBehavior')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER1')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE2').setCheck('DcMotor.ZeroPowerBehavior')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER2')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['ZeroPowerBehavior', 'Sets the ZeroPowerBehavior for two motors. Valid values are BRAKE or FLOAT.'],
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

Blockly.JavaScript['dcMotor_setDualProperty_ZeroPowerBehavior'] =
    Blockly.JavaScript['dcMotor_setDualProperty'];

Blockly.FtcJava['dcMotor_setDualProperty_ZeroPowerBehavior'] =
    Blockly.FtcJava['dcMotor_setDualProperty'];

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
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['RUN_WITHOUT_ENCODER', 'The DcMotor RunMode value RUN_WITHOUT_ENCODER.'],
        ['RUN_USING_ENCODER', 'The DcMotor RunMode value RUN_USING_ENCODER.'],
        ['RUN_TO_POSITION', 'The DcMotor RunMode value RUN_TO_POSITION.'],
        ['STOP_AND_RESET_ENCODER', 'The DcMotor RunMode value STOP_AND_RESET_ENCODER.'],
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
  }
};

Blockly.JavaScript['dcMotor_enum_runMode'] = function(block) {
  var code = '"' + block.getFieldValue('RUN_MODE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['dcMotor_enum_runMode'] = function(block) {
  var code = 'DcMotor.RunMode.' + block.getFieldValue('RUN_MODE');
  Blockly.FtcJava.generateImport_('DcMotor');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['dcMotor_typedEnum_runMode'] = {
  init: function() {
    var RUN_MODE_CHOICES = [
        ['RUN_WITHOUT_ENCODER', 'RUN_WITHOUT_ENCODER'],
        ['RUN_USING_ENCODER', 'RUN_USING_ENCODER'],
        ['RUN_TO_POSITION', 'RUN_TO_POSITION'],
        ['STOP_AND_RESET_ENCODER', 'STOP_AND_RESET_ENCODER'],
    ];
    this.setOutput(true, 'DcMotor.RunMode');
    this.appendDummyInput()
        .appendField(createNonEditableField('RunMode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(RUN_MODE_CHOICES), 'RUN_MODE');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['RUN_WITHOUT_ENCODER', 'The DcMotor RunMode value RUN_WITHOUT_ENCODER.'],
        ['RUN_USING_ENCODER', 'The DcMotor RunMode value RUN_USING_ENCODER.'],
        ['RUN_TO_POSITION', 'The DcMotor RunMode value RUN_TO_POSITION.'],
        ['STOP_AND_RESET_ENCODER', 'The DcMotor RunMode value STOP_AND_RESET_ENCODER.'],
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
  }
};

Blockly.JavaScript['dcMotor_typedEnum_runMode'] =
    Blockly.JavaScript['dcMotor_enum_runMode'];

Blockly.FtcJava['dcMotor_typedEnum_runMode'] =
    Blockly.FtcJava['dcMotor_enum_runMode'];

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

Blockly.JavaScript['dcMotor_enum_direction'] = function(block) {
  var code = '"' + block.getFieldValue('DIRECTION') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['dcMotor_enum_direction'] = function(block) {
  var code = 'DcMotorSimple.Direction.' + block.getFieldValue('DIRECTION');
  Blockly.FtcJava.generateImport_('DcMotorSimple');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['dcMotor_typedEnum_direction'] = {
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

Blockly.JavaScript['dcMotor_typedEnum_direction'] =
    Blockly.JavaScript['dcMotor_enum_direction'];

Blockly.FtcJava['dcMotor_typedEnum_direction'] =
    Blockly.FtcJava['dcMotor_enum_direction'];

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
    this.setColour(getPropertyColor);
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
  }
};

Blockly.JavaScript['dcMotor_enum_zeroPowerBehavior'] = function(block) {
  var code = '"' + block.getFieldValue('ZERO_POWER_BEHAVIOR') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['dcMotor_enum_zeroPowerBehavior'] = function(block) {
  var code = 'DcMotor.ZeroPowerBehavior.' + block.getFieldValue('ZERO_POWER_BEHAVIOR');
  Blockly.FtcJava.generateImport_('DcMotor');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['dcMotor_typedEnum_zeroPowerBehavior'] = {
  init: function() {
    var ZERO_POWER_BEHAVIOR_CHOICES = [
        ['BRAKE', 'BRAKE'],
        ['FLOAT', 'FLOAT'],
    ];
    this.setOutput(true, 'DcMotor.ZeroPowerBehavior');
    this.appendDummyInput()
        .appendField(createNonEditableField('ZeroPowerBehavior'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(ZERO_POWER_BEHAVIOR_CHOICES), 'ZERO_POWER_BEHAVIOR');
    this.setColour(getPropertyColor);
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
  }
};

Blockly.JavaScript['dcMotor_typedEnum_zeroPowerBehavior'] =
    Blockly.JavaScript['dcMotor_enum_zeroPowerBehavior'];

Blockly.FtcJava['dcMotor_typedEnum_zeroPowerBehavior'] =
    Blockly.FtcJava['dcMotor_enum_zeroPowerBehavior'];

// Functions

Blockly.Blocks['dcMotor_isBusy'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createDcMotorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isBusy'));
    this.setColour(functionColor);
    this.setTooltip('Returns true if the motor is currently advancing or retreating to a target position.');
  }
};

Blockly.JavaScript['dcMotor_isBusy'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isBusy()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['dcMotor_isBusy'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DcMotor');
  var code = identifier + '.isBusy()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['dcMotor_setMotorEnable'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createDcMotorExDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('setMotorEnable'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Individually energizes this particular motor. ' +
        'Not all DcMotors support this feature.');
  }
};

Blockly.JavaScript['dcMotor_setMotorEnable'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  return identifier + '.setMotorEnable();\n';
};

Blockly.FtcJava['dcMotor_setMotorEnable'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DcMotor');
  // This java code will throw ClassCastException if the DcMotor is not a DcMotorEx.
  Blockly.FtcJava.generateImport_('DcMotorEx');
  return '((DcMotorEx) ' + identifier + ').setMotorEnable();\n';
};

Blockly.Blocks['dcMotor_setMotorDisable'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createDcMotorExDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('setMotorDisable'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Individually de-energizes this particular motor. ' +
        'Not all DcMotors support this feature.');
  }
};

Blockly.JavaScript['dcMotor_setMotorDisable'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  return identifier + '.setMotorDisable();\n';
};

Blockly.FtcJava['dcMotor_setMotorDisable'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DcMotor');
  // This java code will throw ClassCastException if the DcMotor is not a DcMotorEx.
  Blockly.FtcJava.generateImport_('DcMotorEx');
  return '((DcMotorEx) ' + identifier + ').setMotorDisable();\n';
};

Blockly.Blocks['dcMotor_isMotorEnabled'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createDcMotorExDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('isMotorEnabled'));
    this.setColour(functionColor);
    this.setTooltip('Returns true if the motor is energized. ' +
        'Not all DcMotors support this feature.');
  }
};

Blockly.JavaScript['dcMotor_isMotorEnabled'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var code = identifier + '.isMotorEnabled()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['dcMotor_isMotorEnabled'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DcMotor');
  // This java code will throw ClassCastException if the DcMotor is not a DcMotorEx.
  Blockly.FtcJava.generateImport_('DcMotorEx');
  var code = '((DcMotorEx) ' + identifier + ').isMotorEnabled()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['dcMotor_setVelocity_withAngleUnit'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createDcMotorExDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('setVelocity'));
    this.appendValueInput('ANGULAR_RATE').setCheck('Number')
        .appendField('angularRate')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the velocity of the motor. ' +
        'Not all DcMotors support this feature.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'ANGULAR_RATE':
          return 'double';
      }
      return '';
    };
  }
};

Blockly.JavaScript['dcMotor_setVelocity_withAngleUnit'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var angularRate = Blockly.JavaScript.valueToCode(
      block, 'ANGULAR_RATE', Blockly.JavaScript.ORDER_COMMA);
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  return identifier + '.setVelocity_withAngleUnit(' + angularRate + ', ' + angleUnit + ');\n';
};

Blockly.FtcJava['dcMotor_setVelocity_withAngleUnit'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DcMotor');
  var angularRate = Blockly.FtcJava.valueToCode(
      block, 'ANGULAR_RATE', Blockly.FtcJava.ORDER_COMMA);
  var angleUnit = Blockly.FtcJava.valueToCode(
      block, 'ANGLE_UNIT', Blockly.FtcJava.ORDER_COMMA);
  // This java code will throw ClassCastException if the DcMotor is not a DcMotorEx.
  Blockly.FtcJava.generateImport_('DcMotorEx');
  return '((DcMotorEx) ' + identifier + ').setVelocity(' + angularRate + ', ' + angleUnit + ');\n';
};

Blockly.Blocks['dcMotor_getVelocity_withAngleUnit'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createDcMotorExDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('getVelocity'));
    this.appendValueInput('ANGLE_UNIT').setCheck('AngleUnit')
        .appendField('angleUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the current velocity of the motor, in angular units per second. ' +
        'Not all DcMotors support this feature.');
    this.getFtcJavaOutputType = function() {
      return 'double';
    };
  }
};

Blockly.JavaScript['dcMotor_getVelocity_withAngleUnit'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var angleUnit = Blockly.JavaScript.valueToCode(
      block, 'ANGLE_UNIT', Blockly.JavaScript.ORDER_NONE);
  var code = identifier + '.getVelocity_withAngleUnit(' + angleUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['dcMotor_getVelocity_withAngleUnit'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DcMotor');
  var angleUnit = Blockly.FtcJava.valueToCode(
      block, 'ANGLE_UNIT', Blockly.FtcJava.ORDER_NONE);
  // This java code will throw ClassCastException if the DcMotor is not a DcMotorEx.
  Blockly.FtcJava.generateImport_('DcMotorEx');
  var code = '((DcMotorEx) ' + identifier + ').getVelocity(' + angleUnit + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['dcMotor_setPIDFCoefficients'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createDcMotorExDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('setPIDFCoefficients'));
    this.appendValueInput('RUN_MODE').setCheck('DcMotor.RunMode')
        .appendField('runMode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('PIDF_COEFFICIENTS').setCheck('PIDFCoefficients')
        .appendField('pidfCoefficients')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the PIDF control coefficients for one of the PIDF modes of this motor. ' +
        'Note that in some controller implementations, setting the PIDF coefficients for one ' +
        'mode on a motor might affect other modes on that motor, or might affect the PIDF ' +
        'coefficients used by other motors on the same controller. ' +
        'The runMode value can be either RUN_USING_ENCODER or RUN_TO_POSITION. ' +
        'Not all DcMotors support this feature.');
  }
};

Blockly.JavaScript['dcMotor_setPIDFCoefficients'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var runMode = Blockly.JavaScript.valueToCode(
      block, 'RUN_MODE', Blockly.JavaScript.ORDER_COMMA);
  var pidfCoefficients = Blockly.JavaScript.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.JavaScript.ORDER_COMMA);
  return identifier + '.setPIDFCoefficients(' + runMode + ', ' + pidfCoefficients + ');\n';
};

Blockly.FtcJava['dcMotor_setPIDFCoefficients'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DcMotor');
  var runMode = Blockly.FtcJava.valueToCode(
      block, 'RUN_MODE', Blockly.FtcJava.ORDER_COMMA);
  var pidfCoefficients = Blockly.FtcJava.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.FtcJava.ORDER_COMMA);
  // This java code will throw ClassCastException if the DcMotor is not a DcMotorEx.
  Blockly.FtcJava.generateImport_('DcMotorEx');
  return '((DcMotorEx) ' + identifier + ').setPIDFCoefficients(' + runMode + ', ' +
      pidfCoefficients + ');\n';
};

Blockly.Blocks['dcMotor_getPIDFCoefficients'] = {
  init: function() {
    this.setOutput(true, 'PIDFCoefficients');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createDcMotorExDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('getPIDFCoefficients'));
    this.appendValueInput('RUN_MODE').setCheck('DcMotor.RunMode')
        .appendField('runMode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the PIDF control coefficients used when running in the indicated mode ' +
        'on this motor. The runMode value can be either RUN_USING_ENCODER or RUN_TO_POSITION. ' +
        'Not all DcMotors support this feature.');
  }
};

Blockly.JavaScript['dcMotor_getPIDFCoefficients'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var runMode = Blockly.JavaScript.valueToCode(
      block, 'RUN_MODE', Blockly.JavaScript.ORDER_NONE);
  var code = identifier + '.getPIDFCoefficients(' + runMode + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['dcMotor_getPIDFCoefficients'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DcMotor');
  var runMode = Blockly.FtcJava.valueToCode(
      block, 'RUN_MODE', Blockly.FtcJava.ORDER_NONE);
  // This java code will throw ClassCastException if the DcMotor is not a DcMotorEx.
  Blockly.FtcJava.generateImport_('DcMotorEx');
  var code = '((DcMotorEx) ' + identifier + ').getPIDFCoefficients(' + runMode + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['dcMotor_setVelocityPIDFCoefficients'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createDcMotorExDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('setVelocityPIDFCoefficients'));
    this.appendValueInput('P').setCheck('Number')
        .appendField('p')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('I').setCheck('Number')
        .appendField('i')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('D').setCheck('Number')
        .appendField('d')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('F').setCheck('Number')
        .appendField('f')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('A shorthand for setting the PIDF coefficients for the RUN_USING_ENCODER mode. ' +
        'Not all DcMotors support this feature.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'P':
        case 'I':
        case 'D':
        case 'F':
          return 'double';
      }
      return '';
    };
  }
};

Blockly.JavaScript['dcMotor_setVelocityPIDFCoefficients'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var p = Blockly.JavaScript.valueToCode(
      block, 'P', Blockly.JavaScript.ORDER_COMMA);
  var i = Blockly.JavaScript.valueToCode(
      block, 'I', Blockly.JavaScript.ORDER_COMMA);
  var d = Blockly.JavaScript.valueToCode(
      block, 'D', Blockly.JavaScript.ORDER_COMMA);
  var f = Blockly.JavaScript.valueToCode(
      block, 'F', Blockly.JavaScript.ORDER_COMMA);
  return identifier + '.setVelocityPIDFCoefficients(' + p + ', ' + i + ', ' + d + ', ' + f + ');\n';
};

Blockly.FtcJava['dcMotor_setVelocityPIDFCoefficients'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DcMotor');
  var p = Blockly.FtcJava.valueToCode(
      block, 'P', Blockly.FtcJava.ORDER_COMMA);
  var i = Blockly.FtcJava.valueToCode(
      block, 'I', Blockly.FtcJava.ORDER_COMMA);
  var d = Blockly.FtcJava.valueToCode(
      block, 'D', Blockly.FtcJava.ORDER_COMMA);
  var f = Blockly.FtcJava.valueToCode(
      block, 'F', Blockly.FtcJava.ORDER_COMMA);
  // This java code will throw ClassCastException if the DcMotor is not a DcMotorEx.
  Blockly.FtcJava.generateImport_('DcMotorEx');
  return '((DcMotorEx) ' + identifier + ').setVelocityPIDFCoefficients(' + p + ', ' + i + ', ' + d + ', ' + f + ');\n';
};

Blockly.Blocks['dcMotor_setPositionPIDFCoefficients'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createDcMotorExDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('setPositionPIDFCoefficients'));
    this.appendValueInput('P').setCheck('Number')
        .appendField('p')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('A shorthand for setting the PIDF coefficients for the RUN_TO_POSITION mode. ' +
        'Not all DcMotors support this feature.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'P':
          return 'double';
      }
      return '';
    };
  }
};

Blockly.JavaScript['dcMotor_setPositionPIDFCoefficients'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var p = Blockly.JavaScript.valueToCode(
      block, 'P', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.setPositionPIDFCoefficients(' + p + ');\n';
};

Blockly.FtcJava['dcMotor_setPositionPIDFCoefficients'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DcMotor');
  var p = Blockly.FtcJava.valueToCode(
      block, 'P', Blockly.FtcJava.ORDER_NONE);
  // This java code will throw ClassCastException if the DcMotor is not a DcMotorEx.
  Blockly.FtcJava.generateImport_('DcMotorEx');
  return '((DcMotorEx) ' + identifier + ').setPositionPIDFCoefficients(' + p + ');\n';
};
