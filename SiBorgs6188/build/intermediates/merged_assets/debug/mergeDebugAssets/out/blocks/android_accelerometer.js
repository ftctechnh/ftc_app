/**
 * @fileoverview FTC robot blocks related to the Android Accelerometer.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// androidAccelerometerIdentifierForJavaScript
// The following are defined in vars.js:
// getPropertyColor
// functionColor

Blockly.Blocks['androidAccelerometer_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['DistanceUnit', 'DistanceUnit'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createNonEditableField('AndroidAccelerometer'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['DistanceUnit', 'Sets the DistanceUnit to be used.'],
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

Blockly.JavaScript['androidAccelerometer_setProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return androidAccelerometerIdentifierForJavaScript + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['androidAccelerometer_setProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidAccelerometer');
  var property = block.getFieldValue('PROP');
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['androidAccelerometer_setProperty_DistanceUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['DistanceUnit', 'DistanceUnit'],
    ];
    this.appendValueInput('VALUE').setCheck('DistanceUnit')
        .appendField('set')
        .appendField(createNonEditableField('AndroidAccelerometer'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['DistanceUnit', 'Sets the DistanceUnit to be used.'],
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

Blockly.JavaScript['androidAccelerometer_setProperty_DistanceUnit'] =
    Blockly.JavaScript['androidAccelerometer_setProperty'];

Blockly.FtcJava['androidAccelerometer_setProperty_DistanceUnit'] =
    Blockly.FtcJava['androidAccelerometer_setProperty'];

Blockly.Blocks['androidAccelerometer_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['X', 'X'],
        ['Y', 'Y'],
        ['Z', 'Z'],
        ['Acceleration', 'Acceleration'],
        ['DistanceUnit', 'DistanceUnit'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidAccelerometer'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['X', 'Returns the numeric value of acceleration in the x-axis.'],
        ['Y', 'Returns the numeric value of acceleration in the y-axis.'],
        ['Z', 'Returns the numeric value of acceleration in the z-axis.'],
        ['Acceleration', 'Returns an Acceleration object representing acceleration in X, Y and Z axes.'],
        ['DistanceUnit', 'Returns the DistanceUnit being used.'],
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

Blockly.JavaScript['androidAccelerometer_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var code = androidAccelerometerIdentifierForJavaScript + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['androidAccelerometer_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidAccelerometer');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['androidAccelerometer_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['X', 'X'],
        ['Y', 'Y'],
        ['Z', 'Z'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidAccelerometer'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['X', 'Returns the numeric value of acceleration in the x-axis.'],
        ['Y', 'Returns the numeric value of acceleration in the y-axis.'],
        ['Z', 'Returns the numeric value of acceleration in the z-axis.'],
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
        case 'X':
        case 'Y':
        case 'Z':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (androidAccelerometer_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['androidAccelerometer_getProperty_Number'] =
    Blockly.JavaScript['androidAccelerometer_getProperty'];

Blockly.FtcJava['androidAccelerometer_getProperty_Number'] =
    Blockly.FtcJava['androidAccelerometer_getProperty'];

Blockly.Blocks['androidAccelerometer_getProperty_Acceleration'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Acceleration', 'Acceleration'],
    ];
    this.setOutput(true, 'Acceleration');
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidAccelerometer'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Acceleration', 'Returns an Acceleration object representing acceleration in X, Y and Z axes.'],
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

Blockly.JavaScript['androidAccelerometer_getProperty_Acceleration'] =
    Blockly.JavaScript['androidAccelerometer_getProperty'];

Blockly.FtcJava['androidAccelerometer_getProperty_Acceleration'] =
    Blockly.FtcJava['androidAccelerometer_getProperty'];

Blockly.Blocks['androidAccelerometer_getProperty_DistanceUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['DistanceUnit', 'DistanceUnit'],
    ];
    this.setOutput(true, 'DistanceUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidAccelerometer'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['DistanceUnit', 'Returns the DistanceUnit being used.'],
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

Blockly.JavaScript['androidAccelerometer_getProperty_DistanceUnit'] =
    Blockly.JavaScript['androidAccelerometer_getProperty'];

Blockly.FtcJava['androidAccelerometer_getProperty_DistanceUnit'] =
    Blockly.FtcJava['androidAccelerometer_getProperty'];

// Functions

Blockly.Blocks['androidAccelerometer_isAvailable'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidAccelerometer'))
        .appendField('.')
        .appendField(createNonEditableField('isAvailable'));
    this.setColour(functionColor);
    this.setTooltip('Returns true if the Android device has an accelerometer.');
  }
};

Blockly.JavaScript['androidAccelerometer_isAvailable'] = function(block) {
  var code = androidAccelerometerIdentifierForJavaScript + '.isAvailable()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['androidAccelerometer_isAvailable'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidAccelerometer');
  var code = identifier + '.isAvailable()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['androidAccelerometer_startListening'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidAccelerometer'))
        .appendField('.')
        .appendField(createNonEditableField('startListening'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Start listening to events from the Android accelerometer.');
  }
};

Blockly.JavaScript['androidAccelerometer_startListening'] = function(block) {
  return androidAccelerometerIdentifierForJavaScript + '.startListening();\n';
};

Blockly.FtcJava['androidAccelerometer_startListening'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidAccelerometer');
  return identifier + '.startListening();\n';
};

Blockly.Blocks['androidAccelerometer_stopListening'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidAccelerometer'))
        .appendField('.')
        .appendField(createNonEditableField('stopListening'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Stop listening to events from the Android accelerometer.');
  }
};

Blockly.JavaScript['androidAccelerometer_stopListening'] = function(block) {
  return androidAccelerometerIdentifierForJavaScript + '.stopListening();\n';
};

Blockly.FtcJava['androidAccelerometer_stopListening'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidAccelerometer');
  return identifier + '.stopListening();\n';
};
