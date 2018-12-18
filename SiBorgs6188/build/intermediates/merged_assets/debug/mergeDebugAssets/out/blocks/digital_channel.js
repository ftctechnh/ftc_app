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
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'Sets the mode. Valid values are Mode_INPUT or Mode_OUTPUT.'],
        ['State', 'Sets the channel state. Valid values are true or false.'],
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

Blockly.JavaScript['digitalChannel_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['digitalChannel_setProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DigitalChannel');
  var property = block.getFieldValue('PROP');
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['digitalChannel_setProperty_DigitalChannelMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.appendValueInput('VALUE').setCheck('DigitalChannel.Mode')
        .appendField('set')
        .appendField(createDigitalChannelDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'Sets the mode. Valid values are Mode_INPUT or Mode_OUTPUT.'],
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

Blockly.JavaScript['digitalChannel_setProperty_DigitalChannelMode'] =
    Blockly.JavaScript['digitalChannel_setProperty'];

Blockly.FtcJava['digitalChannel_setProperty_DigitalChannelMode'] =
    Blockly.FtcJava['digitalChannel_setProperty'];

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
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['State', 'Sets the channel state. Valid values are true or false.'],
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

Blockly.JavaScript['digitalChannel_setProperty_Boolean'] =
    Blockly.JavaScript['digitalChannel_setProperty'];

Blockly.FtcJava['digitalChannel_setProperty_Boolean'] =
    Blockly.FtcJava['digitalChannel_setProperty'];

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
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'Returns the mode: INPUT or OUTPUT.'],
        ['State', 'Returns the channel state: true or false.'],
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

Blockly.JavaScript['digitalChannel_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['digitalChannel_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'DigitalChannel');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['digitalChannel_getProperty_DigitalChannelMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.setOutput(true, 'DigitalChannel.Mode');
    this.appendDummyInput()
        .appendField(createDigitalChannelDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'Returns the mode: INPUT or OUTPUT.'],
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

Blockly.JavaScript['digitalChannel_getProperty_DigitalChannelMode'] =
    Blockly.JavaScript['digitalChannel_getProperty'];

Blockly.FtcJava['digitalChannel_getProperty_DigitalChannelMode'] =
    Blockly.FtcJava['digitalChannel_getProperty'];

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
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['State', 'Returns the channel state: true or false.'],
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

Blockly.JavaScript['digitalChannel_getProperty_Boolean'] =
    Blockly.JavaScript['digitalChannel_getProperty'];

Blockly.FtcJava['digitalChannel_getProperty_Boolean'] =
    Blockly.FtcJava['digitalChannel_getProperty'];

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
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['INPUT', 'The DigitalChannel Mode value INPUT.'],
        ['OUTPUT', 'The DigitalChannel Mode value OUTPUT.'],
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
  }
};

Blockly.JavaScript['digitalChannel_enum_mode'] = function(block) {
  var code = '"' + block.getFieldValue('MODE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['digitalChannel_enum_mode'] = function(block) {
  var code = 'DigitalChannel.Mode.' + block.getFieldValue('MODE');
  Blockly.FtcJava.generateImport_('DigitalChannel');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['digitalChannel_typedEnum_mode'] = {
  init: function() {
    var MODE_CHOICES = [
        ['INPUT', 'INPUT'],
        ['OUTPUT', 'OUTPUT'],
    ];
    this.setOutput(true, 'DigitalChannel.Mode');
    this.appendDummyInput()
        .appendField(createNonEditableField('Mode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(MODE_CHOICES), 'MODE');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['INPUT', 'The DigitalChannel Mode value INPUT.'],
        ['OUTPUT', 'The DigitalChannel Mode value OUTPUT.'],
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
  }
};

Blockly.JavaScript['digitalChannel_typedEnum_mode'] =
    Blockly.JavaScript['digitalChannel_enum_mode'];

Blockly.FtcJava['digitalChannel_typedEnum_mode'] =
    Blockly.FtcJava['digitalChannel_enum_mode'];
