/**
 * @fileoverview FTC robot blocks related to game pads.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createGamepadDropdown
// The following are defined in vars.js:
// getPropertyColor

Blockly.Blocks['gamepad_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['A', 'A'],
        ['AtRest', 'AtRest'],
        ['B', 'B'],
        ['Back', 'Back'],
        ['DpadDown', 'DpadDown'],
        ['DpadLeft', 'DpadLeft'],
        ['DpadRight', 'DpadRight'],
        ['DpadUp', 'DpadUp'],
        ['Guide', 'Guide'],
        ['LeftBumper', 'LeftBumper'],
        ['LeftStickButton', 'LeftStickButton'],
        ['LeftStickX', 'LeftStickX'],
        ['LeftStickY', 'LeftStickY'],
        ['LeftTrigger', 'LeftTrigger'],
        ['RightBumper', 'RightBumper'],
        ['RightStickButton', 'RightStickButton'],
        ['RightStickX', 'RightStickX'],
        ['RightStickY', 'RightStickY'],
        ['RightTrigger', 'RightTrigger'],
        ['Start', 'Start'],
        ['X', 'X'],
        ['Y', 'Y'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createGamepadDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['A', 'The value of the A button: true if pressed, false otherwise.'],
        ['AtRest', 'Are all analog sticks and triggers in their rest position? True or false.'],
        ['B', 'The value of the B button: true if pressed, false otherwise.'],
        ['Back', 'The value of the Back button: true if pressed, false otherwise.'],
        ['DpadDown', 'The dpad down value: true if pressed, false otherwise.'],
        ['DpadLeft', 'The dpad left value: true if pressed, false otherwise.'],
        ['DpadRight', 'The dpad right value: true if pressed, false otherwise.'],
        ['DpadUp', 'The dpad up value: true if pressed, false otherwise.'],
        ['Guide', 'The value of the Guide button: true if pressed, false otherwise. The Guide button is often the large button in the middle of the controller.'],
        ['LeftBumper', 'The left bumper value: true if pressed, false otherwise.'],
        ['LeftStickButton', 'The value of the left stick button: true if pressed, false otherwise.'],
        ['LeftStickX', 'The left analog stick horizontal axis value, as a numeric value between -1.0 and +1.0.'],
        ['LeftStickY', 'The left analog stick vertical axis value, as a numeric value between -1.0 and +1.0.'],
        ['LeftTrigger', 'The left trigger value, as a numeric value between 0.0 and +1.0.'],
        ['RightBumper', 'The right bumper value: true if pressed, false otherwise.'],
        ['RightStickButton', 'The value of the right stick button: true if pressed, false otherwise.'],
        ['RightStickX', 'The right analog stick horizontal axis value, as a numeric value between -1.0 and +1.0.'],
        ['RightStickY', 'The right analog stick vertical axis value, as a numeric value between -1.0 and +1.0.'],
        ['RightTrigger', 'The right trigger value, as a numeric value between 0.0 and +1.0.'],
        ['Start', 'The value of the Start button: true if pressed, false otherwise.'],
        ['X', 'The value of the X button: true if pressed, false otherwise.'],
        ['Y', 'The value of the Y button: true if pressed, false otherwise.'],
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

Blockly.JavaScript['gamepad_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['gamepad_getProperty_Boolean'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['A', 'A'],
        ['AtRest', 'AtRest'],
        ['B', 'B'],
        ['Back', 'Back'],
        ['DpadDown', 'DpadDown'],
        ['DpadLeft', 'DpadLeft'],
        ['DpadRight', 'DpadRight'],
        ['DpadUp', 'DpadUp'],
        ['Guide', 'Guide'],
        ['LeftBumper', 'LeftBumper'],
        ['LeftStickButton', 'LeftStickButton'],
        ['RightBumper', 'RightBumper'],
        ['RightStickButton', 'RightStickButton'],
        ['Start', 'Start'],
        ['X', 'X'],
        ['Y', 'Y'],
    ];
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField(createGamepadDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['A', 'The value of the A button: true if pressed, false otherwise.'],
        ['AtRest', 'Are all analog sticks and triggers in their rest position? True or false.'],
        ['B', 'The value of the B button: true if pressed, false otherwise.'],
        ['Back', 'The value of the Back button: true if pressed, false otherwise.'],
        ['DpadDown', 'The dpad down value: true if pressed, false otherwise.'],
        ['DpadLeft', 'The dpad left value: true if pressed, false otherwise.'],
        ['DpadRight', 'The dpad right value: true if pressed, false otherwise.'],
        ['DpadUp', 'The dpad up value: true if pressed, false otherwise.'],
        ['Guide', 'The value of the Guide button: true if pressed, false otherwise. The Guide button is often the large button in the middle of the controller.'],
        ['LeftBumper', 'The left bumper value: true if pressed, false otherwise.'],
        ['LeftStickButton', 'The value of the left stick button: true if pressed, false otherwise.'],
        ['RightBumper', 'The right bumper value: true if pressed, false otherwise.'],
        ['RightStickButton', 'The value of the right stick button: true if pressed, false otherwise.'],
        ['Start', 'The value of the Start button: true if pressed, false otherwise.'],
        ['X', 'The value of the X button: true if pressed, false otherwise.'],
        ['Y', 'The value of the Y button: true if pressed, false otherwise.'],
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

Blockly.JavaScript['gamepad_getProperty_Boolean'] =
    Blockly.JavaScript['gamepad_getProperty'];


Blockly.Blocks['gamepad_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['LeftStickX', 'LeftStickX'],
        ['LeftStickY', 'LeftStickY'],
        ['LeftTrigger', 'LeftTrigger'],
        ['RightStickX', 'RightStickX'],
        ['RightStickY', 'RightStickY'],
        ['RightTrigger', 'RightTrigger'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createGamepadDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['LeftStickX', 'The left analog stick horizontal axis value, as a numeric value between -1.0 and +1.0.'],
        ['LeftStickY', 'The left analog stick vertical axis value, as a numeric value between -1.0 and +1.0.'],
        ['LeftTrigger', 'The left trigger value, as a numeric value between 0.0 and +1.0.'],
        ['RightStickX', 'The right analog stick horizontal axis value, as a numeric value between -1.0 and +1.0.'],
        ['RightStickY', 'The right analog stick vertical axis value, as a numeric value between -1.0 and +1.0.'],
        ['RightTrigger', 'The right trigger value, as a numeric value between 0.0 and +1.0.'],
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

Blockly.JavaScript['gamepad_getProperty_Number'] =
    Blockly.JavaScript['gamepad_getProperty'];
