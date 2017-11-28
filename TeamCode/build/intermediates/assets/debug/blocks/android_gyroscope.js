/**
 * @fileoverview FTC robot blocks related to the Android Gyroscope.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// androidGyroscopeIdentifier
// The following are defined in vars.js:
// getPropertyColor
// functionColor

Blockly.Blocks['androidGyroscope_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngleUnit', 'AngleUnit'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createNonEditableField('AndroidGyroscope'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngleUnit', 'Sets the AngleUnit to be used.'],
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

Blockly.JavaScript['androidGyroscope_setProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return androidGyroscopeIdentifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['androidGyroscope_setProperty_AngleUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngleUnit', 'AngleUnit'],
    ];
    this.appendValueInput('VALUE').setCheck('AngleUnit')
        .appendField('set')
        .appendField(createNonEditableField('AndroidGyroscope'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngleUnit', 'Sets the AngleUnit to be used.'],
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

Blockly.JavaScript['androidGyroscope_setProperty_AngleUnit'] =
    Blockly.JavaScript['androidGyroscope_setProperty'];

Blockly.Blocks['androidGyroscope_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['X', 'X'],
        ['Y', 'Y'],
        ['Z', 'Z'],
        ['AngularVelocity', 'AngularVelocity'],
        ['AngleUnit', 'AngleUnit'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidGyroscope'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['X', 'Returns the angular speed around the x-axis.'],
        ['Y', 'Returns the angular speed around the y-axis.'],
        ['Z', 'Returns the angular speed around the z-axis.'],
        ['AngularVelocity',
            'Returns an AngularVelocity object representing the rate of rotation around the device\'s local X, Y and Z axis.'],
        ['AngleUnit', 'Returns the AngleUnit being used.'],
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

Blockly.JavaScript['androidGyroscope_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var code = androidGyroscopeIdentifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['androidGyroscope_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['X', 'X'],
        ['Y', 'Y'],
        ['Z', 'Z'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidGyroscope'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['X', 'Returns the angular speed around the x-axis.'],
        ['Y', 'Returns the angular speed around the y-axis.'],
        ['Z', 'Returns the angular speed around the z-axis.'],
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

Blockly.JavaScript['androidGyroscope_getProperty_Number'] =
    Blockly.JavaScript['androidGyroscope_getProperty'];

Blockly.Blocks['androidGyroscope_getProperty_AngularVelocity'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngularVelocity', 'AngularVelocity'],
    ];
    this.setOutput(true, 'AngularVelocity');
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidGyroscope'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngularVelocity',
            'Returns an AngularVelocity object representing the rate of rotation around the device\'s local X, Y and Z axis.'],
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

Blockly.JavaScript['androidGyroscope_getProperty_AngularVelocity'] =
    Blockly.JavaScript['androidGyroscope_getProperty'];

Blockly.Blocks['androidGyroscope_getProperty_AngleUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngleUnit', 'AngleUnit'],
    ];
    this.setOutput(true, 'AngleUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidGyroscope'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['AngleUnit', 'Returns the AngleUnit being used.'],
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

Blockly.JavaScript['androidGyroscope_getProperty_AngleUnit'] =
    Blockly.JavaScript['androidGyroscope_getProperty'];

// Functions

Blockly.Blocks['androidGyroscope_isAvailable'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidGyroscope'))
        .appendField('.')
        .appendField(createNonEditableField('isAvailable'));
    this.setTooltip(
        'Returns true if the Android device has a gyroscope.');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['androidGyroscope_isAvailable'] = function(block) {
  var code = androidGyroscopeIdentifier + '.isAvailable()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['androidGyroscope_startListening'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidGyroscope'))
        .appendField('.')
        .appendField(createNonEditableField('startListening'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip(
        'Start listening to events from the Android gyroscope.');
  }
};

Blockly.JavaScript['androidGyroscope_startListening'] = function(block) {
  return androidGyroscopeIdentifier + '.startListening();\n';
};

Blockly.Blocks['androidGyroscope_stopListening'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidGyroscope'))
        .appendField('.')
        .appendField(createNonEditableField('stopListening'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip(
        'Stop listening to events from the Android gyroscope.');
  }
};

Blockly.JavaScript['androidGyroscope_stopListening'] = function(block) {
  return androidGyroscopeIdentifier + '.stopListening();\n';
};
