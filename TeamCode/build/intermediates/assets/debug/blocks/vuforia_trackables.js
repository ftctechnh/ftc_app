/**
 * @fileoverview FTC robot blocks related to VuforiaTrackables
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// vuforiaTrackablesIdentifier
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['vuforiaTrackables_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Size', 'Size'],
        ['Name', 'Name'],
        ['Localizer', 'Localizer'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackables'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKABLES').setCheck('VuforiaTrackables')
        .appendField('vuforiaTrackables')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Size', 'Returns the size of the given VuforiaTrackables.'],
        ['Name', 'Returns the user-specified name for the given VuforiaTrackables.'],
        ['Localizer', 'Returns the VuforiaLocalizer which manages the given VuforiaTrackables.'],
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

Blockly.JavaScript['vuforiaTrackables_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaTrackables = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLES', Blockly.JavaScript.ORDER_NONE);
  var code = vuforiaTrackablesIdentifier + '.get' + property + '(' + vuforiaTrackables + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforiaTrackables_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Size', 'Size'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackables'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKABLES').setCheck('VuforiaTrackables')
        .appendField('vuforiaTrackables')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Size', 'Returns the size of the given VuforiaTrackables.'],
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

Blockly.JavaScript['vuforiaTrackables_getProperty_Number'] =
    Blockly.JavaScript['vuforiaTrackables_getProperty'];

Blockly.Blocks['vuforiaTrackables_getProperty_String'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Name', 'Name'],
    ];
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackables'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKABLES').setCheck('VuforiaTrackables')
        .appendField('vuforiaTrackables')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Name', 'Returns the user-specified name for the given VuforiaTrackables.'],
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

Blockly.JavaScript['vuforiaTrackables_getProperty_String'] =
    Blockly.JavaScript['vuforiaTrackables_getProperty'];

Blockly.Blocks['vuforiaTrackables_getProperty_VuforiaLocalizer'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Localizer', 'Localizer'],
    ];
    this.setOutput(true, 'VuforiaLocalizer');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackables'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKABLES').setCheck('VuforiaTrackables')
        .appendField('vuforiaTrackables')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Localizer', 'Returns the VuforiaLocalizer which manages the given VuforiaTrackables.'],
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

Blockly.JavaScript['vuforiaTrackables_getProperty_VuforiaLocalizer'] =
    Blockly.JavaScript['vuforiaTrackables_getProperty'];

// Functions

Blockly.Blocks['vuforiaTrackables_get'] = {
  init: function() {
    this.setOutput(true, 'VuforiaTrackable');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackables'))
        .appendField('.')
        .appendField(createNonEditableField('get'));
    this.appendValueInput('VUFORIA_TRACKABLES').setCheck('VuforiaTrackables')
        .appendField('vuforiaTrackables')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('INDEX').setCheck('Number')
        .appendField('index')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a particular VuforiaTrackable of the given VuforiaTrackables.');
  }
};

Blockly.JavaScript['vuforiaTrackables_get'] = function(block) {
  var vuforiaTrackables = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLES', Blockly.JavaScript.ORDER_COMMA);
  var index = Blockly.JavaScript.valueToCode(
      block, 'INDEX', Blockly.JavaScript.ORDER_COMMA);
  var code = vuforiaTrackablesIdentifier + '.get(' + vuforiaTrackables + ', ' + index + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforiaTrackables_setName'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackables'))
        .appendField('.')
        .appendField(createNonEditableField('setName'));
    this.appendValueInput('VUFORIA_TRACKABLES').setCheck('VuforiaTrackables')
        .appendField('vuforiaTrackables')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('NAME').setCheck('String')
        .appendField('name')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the name for the given VuforiaTrackables and any of its contained ' +
        'trackables which do not already have a user-specified name.');
  }
};

Blockly.JavaScript['vuforiaTrackables_setName'] = function(block) {
  var vuforiaTrackables = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLES', Blockly.JavaScript.ORDER_COMMA);
  var name = Blockly.JavaScript.valueToCode(
      block, 'NAME', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaTrackablesIdentifier + '.setName(' + vuforiaTrackables + ', ' + name + ');\n';
};

Blockly.Blocks['vuforiaTrackables_activate'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackables'))
        .appendField('.')
        .appendField(createNonEditableField('activate'));
    this.appendValueInput('VUFORIA_TRACKABLES').setCheck('VuforiaTrackables')
        .appendField('vuforiaTrackables')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Activates the given VuforiaTrackables so that its localizer is actively ' +
        'seeking the presence of the trackables that it contains.');
  }
};

Blockly.JavaScript['vuforiaTrackables_activate'] = function(block) {
  var vuforiaTrackables = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLES', Blockly.JavaScript.ORDER_NONE);
  return vuforiaTrackablesIdentifier + '.activate(' + vuforiaTrackables + ');\n';
};

Blockly.Blocks['vuforiaTrackables_deactivate'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackables'))
        .appendField('.')
        .appendField(createNonEditableField('deactivate'));
    this.appendValueInput('VUFORIA_TRACKABLES').setCheck('VuforiaTrackables')
        .appendField('vuforiaTrackables')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Deactivates the given VuforiaTrackables, causing its localizer to no longer ' +
        'see the presence of the trackables it contains.');
  }
};

Blockly.JavaScript['vuforiaTrackables_deactivate'] = function(block) {
  var vuforiaTrackables = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLES', Blockly.JavaScript.ORDER_NONE);
  return vuforiaTrackablesIdentifier + '.deactivate(' + vuforiaTrackables + ');\n';
};
