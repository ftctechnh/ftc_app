/**
 * @fileoverview FTC robot blocks related to the Android Orientation.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// androidOrientationIdentifierForJavaScript
// The following are defined in vars.js:
// getPropertyColor
// functionColor

Blockly.Blocks['androidOrientation_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngleUnit', 'AngleUnit'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createNonEditableField('AndroidOrientation'))
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

Blockly.JavaScript['androidOrientation_setProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return androidOrientationIdentifierForJavaScript + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['androidOrientation_setProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidOrientation');
  var property = block.getFieldValue('PROP');
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['androidOrientation_setProperty_AngleUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngleUnit', 'AngleUnit'],
    ];
    this.appendValueInput('VALUE').setCheck('AngleUnit')
        .appendField('set')
        .appendField(createNonEditableField('AndroidOrientation'))
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

Blockly.JavaScript['androidOrientation_setProperty_AngleUnit'] =
    Blockly.JavaScript['androidOrientation_setProperty'];

Blockly.FtcJava['androidOrientation_setProperty_AngleUnit'] =
    Blockly.FtcJava['androidOrientation_setProperty'];

Blockly.Blocks['androidOrientation_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Azimuth', 'Azimuth'],
        ['Pitch', 'Pitch'],
        ['Roll', 'Roll'],
        ['Angle', 'Angle'],
        ['Magnitude', 'Magnitude'],
        ['AngleUnit', 'AngleUnit'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidOrientation'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Azimuth', 'Returns the azimuth.'],
        ['Pitch', 'Returns the pitch.'],
        ['Roll', 'Returns the roll.'],
        ['Angle', 'Returns the angle in which the orientation sensor is tilted, treating Roll as the x-coordinate and Pitch as the y-coordinate.'],
        ['Magnitude', 'Returns a number between 0 and 1, indicating how much the device is tilted.'],
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
  }
};

Blockly.JavaScript['androidOrientation_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var code = androidOrientationIdentifierForJavaScript + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['androidOrientation_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidOrientation');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['androidOrientation_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Azimuth', 'Azimuth'],
        ['Pitch', 'Pitch'],
        ['Roll', 'Roll'],
        ['Angle', 'Angle'],
        ['Magnitude', 'Magnitude'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidOrientation'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Azimuth', 'Returns the azimuth.'],
        ['Pitch', 'Returns the pitch.'],
        ['Roll', 'Returns the roll.'],
        ['Angle', 'Returns the angle in which the orientation sensor is tilted, treating Roll as the x-coordinate and Pitch as the y-coordinate.'],
        ['Magnitude', 'Returns a number between 0 and 1, indicating how much the device is tilted.'],
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
        case 'Azimuth':
        case 'Pitch':
        case 'Roll':
        case 'Angle':
        case 'Magnitude':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (androidOrientation_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['androidOrientation_getProperty_Number'] =
    Blockly.JavaScript['androidOrientation_getProperty'];

Blockly.FtcJava['androidOrientation_getProperty_Number'] =
    Blockly.FtcJava['androidOrientation_getProperty'];

Blockly.Blocks['androidOrientation_getProperty_AngleUnit'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['AngleUnit', 'AngleUnit'],
    ];
    this.setOutput(true, 'AngleUnit');
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidOrientation'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
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
  }
};

Blockly.JavaScript['androidOrientation_getProperty_AngleUnit'] =
    Blockly.JavaScript['androidOrientation_getProperty'];

Blockly.FtcJava['androidOrientation_getProperty_AngleUnit'] =
    Blockly.FtcJava['androidOrientation_getProperty'];

// Functions

Blockly.Blocks['androidOrientation_isAvailable'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidOrientation'))
        .appendField('.')
        .appendField(createNonEditableField('isAvailable'));
    this.setColour(functionColor);
    this.setTooltip('Returns true if the Android device has the sensors required for orientation.');
  }
};

Blockly.JavaScript['androidOrientation_isAvailable'] = function(block) {
  var code = androidOrientationIdentifierForJavaScript + '.isAvailable()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['androidOrientation_isAvailable'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidOrientation');
  var code = identifier + '.isAvailable()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['androidOrientation_startListening'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidOrientation'))
        .appendField('.')
        .appendField(createNonEditableField('startListening'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Start listening to events from the Android sensors.');
  }
};

Blockly.JavaScript['androidOrientation_startListening'] = function(block) {
  return androidOrientationIdentifierForJavaScript + '.startListening();\n';
};

Blockly.FtcJava['androidOrientation_startListening'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidOrientation');
  return identifier + '.startListening();\n';
};

Blockly.Blocks['androidOrientation_stopListening'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidOrientation'))
        .appendField('.')
        .appendField(createNonEditableField('stopListening'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Stop listening to events from the Android sensors.');
  }
};

Blockly.JavaScript['androidOrientation_stopListening'] = function(block) {
  return androidOrientationIdentifierForJavaScript + '.stopListening();\n';
};

Blockly.FtcJava['androidOrientation_stopListening'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidOrientation');
  return identifier + '.stopListening();\n';
};
