/**
 * @fileoverview FTC robot blocks related to VuforiaTrackable
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// vuforiaTrackableIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['vuforiaTrackable_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Location', 'Location'],
        ['UserData', 'UserData'],
        ['Trackables', 'Trackables'],
        ['Name', 'Name'],
        ['Listener', 'Listener'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackable'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKABLE').setCheck('VuforiaTrackable')
        .appendField('vuforiaTrackable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Location', 'Returns a matrix representing the location of the given VuforiaTrackable ' +
            'in the FTC field coordinate system.'],
        ['UserData', 'Retreives user data previously associated with the given ' +
            'VuforableTrackable.'],
        ['Trackables', 'Returns the VuforiaTrackables object of which the given VuforiaTrackable is a member.'],
        ['Name', 'Returns the user-specified name for the given VuforiaTrackable.'],
        ['Listener', 'Returns the VuforiaTrackableDefaultListener associated with the given VuforiaTrackable.'],
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

Blockly.JavaScript['vuforiaTrackable_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaTrackable = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLE', Blockly.JavaScript.ORDER_NONE);
  var code = vuforiaTrackableIdentifierForJavaScript + '.get' + property + '(' + vuforiaTrackable + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforiaTrackable_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaTrackable = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKABLE', Blockly.FtcJava.ORDER_MEMBER);
  var code;
  var order;
  switch (property) {
    case 'Listener':
      Blockly.FtcJava.generateImport_('VuforiaTrackableDefaultListener');
      code = '((VuforiaTrackableDefaultListener) ' + vuforiaTrackable + '.get' + property + '())';
      order = Blockly.FtcJava.ORDER_NONE;
      break;
    default:
      code = vuforiaTrackable + '.get' + property + '()';
      order = Blockly.FtcJava.ORDER_FUNCTION_CALL;
      break;
  }
  return [code, order];
};

Blockly.Blocks['vuforiaTrackable_getProperty_OpenGLMatrix'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Location', 'Location'],
    ];
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackable'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKABLE').setCheck('VuforiaTrackable')
        .appendField('vuforiaTrackable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Location', 'Returns a matrix representing the location of the given VuforiaTrackable ' +
            'in the FTC field coordinate system.'],
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

Blockly.JavaScript['vuforiaTrackable_getProperty_OpenGLMatrix'] =
    Blockly.JavaScript['vuforiaTrackable_getProperty'];

Blockly.FtcJava['vuforiaTrackable_getProperty_OpenGLMatrix'] =
    Blockly.FtcJava['vuforiaTrackable_getProperty'];

Blockly.Blocks['vuforiaTrackable_getProperty_Object'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['UserData', 'UserData'],
    ];
    this.setOutput(true); // no type for Object
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackable'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKABLE').setCheck('VuforiaTrackable')
        .appendField('vuforiaTrackable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['UserData', 'Retreives user data previously associated with the given ' +
            'VuforableTrackable.'],
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

Blockly.JavaScript['vuforiaTrackable_getProperty_Object'] =
    Blockly.JavaScript['vuforiaTrackable_getProperty'];

Blockly.FtcJava['vuforiaTrackable_getProperty_Object'] =
    Blockly.FtcJava['vuforiaTrackable_getProperty'];

Blockly.Blocks['vuforiaTrackable_getProperty_VuforiaTrackables'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Trackables', 'Trackables'],
    ];
    this.setOutput(true, 'VuforiaTrackables');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackable'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKABLE').setCheck('VuforiaTrackable')
        .appendField('vuforiaTrackable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Trackables', 'Returns the VuforiaTrackables object of which the given VuforiaTrackable is a member.'],
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

Blockly.JavaScript['vuforiaTrackable_getProperty_VuforiaTrackables'] =
    Blockly.JavaScript['vuforiaTrackable_getProperty'];

Blockly.FtcJava['vuforiaTrackable_getProperty_VuforiaTrackables'] =
    Blockly.FtcJava['vuforiaTrackable_getProperty'];

Blockly.Blocks['vuforiaTrackable_getProperty_String'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Name', 'Name'],
    ];
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackable'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKABLE').setCheck('VuforiaTrackable')
        .appendField('vuforiaTrackable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Name', 'Returns the user-specified name for the given VuforiaTrackable.'],
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

Blockly.JavaScript['vuforiaTrackable_getProperty_String'] =
    Blockly.JavaScript['vuforiaTrackable_getProperty'];

Blockly.FtcJava['vuforiaTrackable_getProperty_String'] =
    Blockly.FtcJava['vuforiaTrackable_getProperty'];

Blockly.Blocks['vuforiaTrackable_getProperty_VuforiaTrackableDefaultListener'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Listener', 'Listener'],
    ];
    this.setOutput(true, 'VuforiaTrackableDefaultListener');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackable'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKABLE').setCheck('VuforiaTrackable')
        .appendField('vuforiaTrackable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Listener', 'Returns the VuforiaTrackableDefaultListener associated with the given VuforiaTrackable.'],
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

Blockly.JavaScript['vuforiaTrackable_getProperty_VuforiaTrackableDefaultListener'] =
    Blockly.JavaScript['vuforiaTrackable_getProperty'];

Blockly.FtcJava['vuforiaTrackable_getProperty_VuforiaTrackableDefaultListener'] =
    Blockly.FtcJava['vuforiaTrackable_getProperty'];

// Functions

Blockly.Blocks['vuforiaTrackable_setLocation'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackable'))
        .appendField('.')
        .appendField(createNonEditableField('setLocation'));
    this.appendValueInput('VUFORIA_TRACKABLE').setCheck('VuforiaTrackable')
        .appendField('vuforiaTrackable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('MATRIX').setCheck('OpenGLMatrix')
        .appendField('matrix')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the location of the given VuforiaTrackable in the FTC field ' +
        'coordinate system, to the given OpenGLMatrix.');
  }
};

Blockly.JavaScript['vuforiaTrackable_setLocation'] = function(block) {
  var vuforiaTrackable = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLE', Blockly.JavaScript.ORDER_COMMA);
  var matrix = Blockly.JavaScript.valueToCode(
      block, 'MATRIX', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaTrackableIdentifierForJavaScript + '.setLocation(' + vuforiaTrackable + ', ' + matrix + ');\n';
};

Blockly.FtcJava['vuforiaTrackable_setLocation'] = function(block) {
  var vuforiaTrackable = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKABLE', Blockly.FtcJava.ORDER_MEMBER);
  var matrix = Blockly.FtcJava.valueToCode(
      block, 'MATRIX', Blockly.FtcJava.ORDER_NONE);
  return vuforiaTrackable + '.setLocation(' + matrix + ');\n';
};

Blockly.Blocks['vuforiaTrackable_setUserData'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackable'))
        .appendField('.')
        .appendField(createNonEditableField('setUserData'));
    this.appendValueInput('VUFORIA_TRACKABLE').setCheck('VuforiaTrackable')
        .appendField('vuforiaTrackable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('USER_DATA') // all types allowed
        .appendField('userData')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets user data to be associated with the given VuforiaTrackable.');
  }
};

Blockly.JavaScript['vuforiaTrackable_setUserData'] = function(block) {
  var vuforiaTrackable = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLE', Blockly.JavaScript.ORDER_COMMA);
  var userData = Blockly.JavaScript.valueToCode(
      block, 'USER_DATA', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaTrackableIdentifierForJavaScript + '.setUserData(' + vuforiaTrackable + ', ' + userData + ');\n';
};

Blockly.FtcJava['vuforiaTrackable_setUserData'] = function(block) {
  var vuforiaTrackable = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKABLE', Blockly.FtcJava.ORDER_MEMBER);
  var userData = Blockly.FtcJava.valueToCode(
      block, 'USER_DATA', Blockly.FtcJava.ORDER_NONE);
  return vuforiaTrackable + '.setUserData(' + userData + ');\n';
};

Blockly.Blocks['vuforiaTrackable_setName'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackable'))
        .appendField('.')
        .appendField(createNonEditableField('setName'));
    this.appendValueInput('VUFORIA_TRACKABLE').setCheck('VuforiaTrackable')
        .appendField('vuforiaTrackable')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('NAME').setCheck('String')
        .appendField('name')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets a user-determined name associated with the given VuforiaTrackable. ' +
        'This is mostly useful for debugging.');
  }
};

Blockly.JavaScript['vuforiaTrackable_setName'] = function(block) {
  var vuforiaTrackable = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLE', Blockly.JavaScript.ORDER_COMMA);
  var name = Blockly.JavaScript.valueToCode(
      block, 'NAME', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaTrackableIdentifierForJavaScript + '.setName(' + vuforiaTrackable + ', ' + name + ');\n';
};

Blockly.FtcJava['vuforiaTrackable_setName'] = function(block) {
  var vuforiaTrackable = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKABLE', Blockly.FtcJava.ORDER_MEMBER);
  var name = Blockly.FtcJava.valueToCode(
      block, 'NAME', Blockly.FtcJava.ORDER_NONE);
  return vuforiaTrackable + '.setName(' + name + ');\n';
};
