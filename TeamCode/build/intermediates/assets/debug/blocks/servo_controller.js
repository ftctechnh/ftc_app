/**
 * @fileoverview FTC robot blocks related to servo controller.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createServoControllerDropdown
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor

Blockly.Blocks['servoController_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['PwmStatus', 'PwmStatus'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createServoControllerDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['PwmStatus', 'Returns the enablement status of the collective set of servos connected ' +
            'to this controller.'],
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

Blockly.JavaScript['servoController_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['servoController_getProperty_PwmStatus'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['PwmStatus', 'PwmStatus'],
    ];
    this.setOutput(true, 'PwmStatus');
    this.appendDummyInput()
        .appendField(createServoControllerDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['PwmStatus', 'Returns the enablement status of the collective set of servos connected ' +
            'to this controller.'],
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

Blockly.JavaScript['servoController_getProperty_PwmStatus'] =
    Blockly.JavaScript['servoController_getProperty'];

// Enums

Blockly.Blocks['servoController_enum_pwmStatus'] = {
  init: function() {
    var PWM_STATUS_CHOICES = [
        ['ENABLED', 'ENABLED'],
        ['DISABLED', 'DISABLED'],
        ['MIXED', 'MIXED'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('PwmStatus'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PWM_STATUS_CHOICES), 'PWM_STATUS');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['ENABLED', 'The PwmStatus value ENABLED.'],
        ['DISABLED', 'The PwmStatus value DISABLED.'],
        ['MIXED', 'The PwmStatus value MIXED.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('PWM_STATUS');
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

Blockly.JavaScript['servoController_enum_pwmStatus'] = function(block) {
  var code = '"' + block.getFieldValue('PWM_STATUS') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['servoController_typedEnum_pwmStatus'] = {
  init: function() {
    var PWM_STATUS_CHOICES = [
        ['ENABLED', 'ENABLED'],
        ['DISABLED', 'DISABLED'],
        ['MIXED', 'MIXED'],
    ];
    this.setOutput(true, 'PwmStatus');
    this.appendDummyInput()
        .appendField(createNonEditableField('PwmStatus'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PWM_STATUS_CHOICES), 'PWM_STATUS');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['ENABLED', 'The PwmStatus value ENABLED.'],
        ['DISABLED', 'The PwmStatus value DISABLED.'],
        ['MIXED', 'The PwmStatus value MIXED.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('PWM_STATUS');
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

Blockly.JavaScript['servoController_typedEnum_pwmStatus'] =
    Blockly.JavaScript['servoController_enum_pwmStatus'];

// Functions

Blockly.Blocks['servoController_pwmEnable'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createServoControllerDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('pwmEnable'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Enables all of the servos connected to this controller.');
  }
};

Blockly.JavaScript['servoController_pwmEnable'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  return identifier + '.pwmEnable();\n';
};

Blockly.Blocks['servoController_pwmDisable'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createServoControllerDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('pwmDisable'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Disables all of the servos connected to this controller.');
  }
};

Blockly.JavaScript['servoController_pwmDisable'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  return identifier + '.pwmDisable();\n';
};
