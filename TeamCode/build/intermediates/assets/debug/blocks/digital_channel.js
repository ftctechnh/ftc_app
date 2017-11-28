/**
 * @fileoverview FTC robot blocks related to digital channel.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createDigitalChannelDropdown
// The following are defined in vars.js:
// getPropertyColor
// setPropertyColor

Blockly.Blocks['digitalChannel_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
        ['State', 'State'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createDigitalChannelDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'The mode. Valid values are Mode_INPUT or Mode_OUTPUT.'],
        ['State', 'The channel state, true or false.'],
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

Blockly.JavaScript['digitalChannel_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['digitalChannel_setProperty_DigitalChannelMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.appendValueInput('VALUE').setCheck('DigitalChannelMode')
        .appendField('set')
        .appendField(createDigitalChannelDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'The mode. Valid values are Mode_INPUT or Mode_OUTPUT.'],
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

Blockly.JavaScript['digitalChannel_setProperty_DigitalChannelMode'] =
    Blockly.JavaScript['digitalChannel_setProperty'];

Blockly.Blocks['digitalChannel_setProperty_Boolean'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['State', 'State'],
    ];
    this.appendValueInput('VALUE').setCheck('Boolean')
        .appendField('set')
        .appendField(createDigitalChannelDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['State', 'The channel state, true or false.'],
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

Blockly.JavaScript['digitalChannel_setProperty_Boolean'] =
    Blockly.JavaScript['digitalChannel_setProperty'];

Blockly.Blocks['digitalChannel_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
        ['State', 'State'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createDigitalChannelDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'The mode. Valid values are Mode_INPUT or Mode_OUTPUT.'],
        ['State', 'The channel state, true or false.'],
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

Blockly.JavaScript['digitalChannel_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['digitalChannel_getProperty_DigitalChannelMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.setOutput(true, 'DigitalChannelMode');
    this.appendDummyInput()
        .appendField(createDigitalChannelDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'The mode. Valid values are Mode_INPUT or Mode_OUTPUT.'],
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

Blockly.JavaScript['digitalChannel_getProperty_DigitalChannelMode'] =
    Blockly.JavaScript['digitalChannel_getProperty'];

Blockly.Blocks['digitalChannel_getProperty_Boolean'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['State', 'State'],
    ];
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField(createDigitalChannelDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['State', 'The channel state, true or false.'],
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

Blockly.JavaScript['digitalChannel_getProperty_Boolean'] =
    Blockly.JavaScript['digitalChannel_getProperty'];

// Enums

Blockly.Blocks['digitalChannel_enum_mode'] = {
  init: function() {
    var MODE_CHOICES = [
        ['INPUT', 'INPUT'],
        ['OUTPUT', 'OUTPUT'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('Mode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(MODE_CHOICES), 'MODE');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['INPUT', 'The Mode value INPUT.'],
        ['OUTPUT', 'The Mode value OUTPUT.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('MODE');
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

Blockly.JavaScript['digitalChannel_enum_mode'] = function(block) {
  var code = '"' + block.getFieldValue('MODE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['digitalChannel_typedEnum_mode'] = {
  init: function() {
    var MODE_CHOICES = [
        ['INPUT', 'INPUT'],
        ['OUTPUT', 'OUTPUT'],
    ];
    this.setOutput(true, 'DigitalChannelMode');
    this.appendDummyInput()
        .appendField(createNonEditableField('Mode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(MODE_CHOICES), 'MODE');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['INPUT', 'The Mode value INPUT.'],
        ['OUTPUT', 'The Mode value OUTPUT.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('MODE');
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

Blockly.JavaScript['digitalChannel_typedEnum_mode'] =
    Blockly.JavaScript['digitalChannel_enum_mode'];
