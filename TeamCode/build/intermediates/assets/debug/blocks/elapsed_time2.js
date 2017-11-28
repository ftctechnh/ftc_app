/**
 * @fileoverview FTC robot blocks related to ElapsedTime.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// elapsedTimeIdentifier
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['elapsedTime2_create'] = {
  init: function() {
    this.setOutput(true, 'ElapsedTime');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('ElapsedTime'));
    this.setColour(functionColor);
    this.setTooltip(
        'Creates a timer with SECONDS resolution that is initialized with the current time.');
  }
};

Blockly.JavaScript['elapsedTime2_create'] = function(block) {
  var code = elapsedTimeIdentifier + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['elapsedTime2_create_withStartTime'] = {
  init: function() {
    this.setOutput(true, 'ElapsedTime');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('ElapsedTime'));
    this.appendValueInput('START_TIME').setCheck('Number')
        .appendField('startTime')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip(
        'Creates a timer with SECONDS resolution that is initialized with the given start time.');
  }
};

Blockly.JavaScript['elapsedTime2_create_withStartTime'] = function(block) {
  var startTime = Blockly.JavaScript.valueToCode(
      block, 'START_TIME', Blockly.JavaScript.ORDER_NONE);
  var code = elapsedTimeIdentifier + '.create_withStartTime(' + startTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['elapsedTime2_create_withResolution'] = {
  init: function() {
    this.setOutput(true, 'ElapsedTime');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('ElapsedTime'));
    this.appendValueInput('RESOLUTION').setCheck('Resolution')
        .appendField('resolution')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip(
        'Creates a timer with the given resolution that is initialized with the current time.');
  }
};

Blockly.JavaScript['elapsedTime2_create_withResolution'] = function(block) {
  var resolution = Blockly.JavaScript.valueToCode(
      block, 'RESOLUTION', Blockly.JavaScript.ORDER_NONE);
  var code = elapsedTimeIdentifier + '.create_withResolution(' + resolution + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

// Properties

Blockly.Blocks['elapsedTime2_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['StartTime', 'StartTime'],
        ['Time', 'Time'],
        ['Seconds', 'Seconds'],
        ['Milliseconds', 'Milliseconds'],
        ['Resolution', 'Resolution'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('ElapsedTime'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ELAPSED_TIME').setCheck('ElapsedTime')
        .appendField('timer')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['StartTime', 'The time at which this timer was last reset.'],
        ['Time', 'The duration that has elapsed since the last reset of this timer.'],
        ['Seconds', 'The duration that has elapsed since the last reset of this timer, ' +
            'in seconds.'],
        ['Milliseconds', 'The duration that has elapsed since the last reset of this timer, ' +
            'in milliseconds.'],
        ['Resolution', 'The resolution with which the timer was created.'],
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

Blockly.JavaScript['elapsedTime2_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var elapsedTime = Blockly.JavaScript.valueToCode(
      block, 'ELAPSED_TIME', Blockly.JavaScript.ORDER_NONE);
  var code = elapsedTimeIdentifier + '.get' + property + '(' + elapsedTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['elapsedTime2_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['StartTime', 'StartTime'],
        ['Time', 'Time'],
        ['Seconds', 'Seconds'],
        ['Milliseconds', 'Milliseconds'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('ElapsedTime'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ELAPSED_TIME').setCheck('ElapsedTime')
        .appendField('timer')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['StartTime', 'The time at which this timer was last reset.'],
        ['Time', 'The duration that has elapsed since the last reset of this timer.'],
        ['Seconds', 'The duration that has elapsed since the last reset of this timer, ' +
            'in seconds.'],
        ['Milliseconds', 'The duration that has elapsed since the last reset of this timer, ' +
            'in milliseconds.'],
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

Blockly.JavaScript['elapsedTime2_getProperty_Number'] =
    Blockly.JavaScript['elapsedTime2_getProperty'];

Blockly.Blocks['elapsedTime2_getProperty_Resolution'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Resolution', 'Resolution'],
    ];
    this.setOutput(true, 'Resolution');
    this.appendDummyInput()
        .appendField(createNonEditableField('ElapsedTime'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('ELAPSED_TIME').setCheck('ElapsedTime')
        .appendField('timer')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Resolution', 'The resolution with which the timer was created.'],
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

Blockly.JavaScript['elapsedTime2_getProperty_Resolution'] =
    Blockly.JavaScript['elapsedTime2_getProperty'];

// Enums

Blockly.Blocks['elapsedTime2_enum_resolution'] = {
  init: function() {
    var RESOLUTION_CHOICES = [
        ['SECONDS', 'SECONDS'],
        ['MILLISECONDS', 'MILLISECONDS'],
    ];
    this.setOutput(true, 'Resolution');
    this.appendDummyInput()
        .appendField(createNonEditableField('Resolution'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(RESOLUTION_CHOICES), 'RESOLUTION');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['SECONDS', 'The Resolution value SECONDS.'],
        ['MILLISECONDS', 'The Resolution value MILLISECONDS.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('RESOLUTION');
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

Blockly.JavaScript['elapsedTime2_enum_resolution'] = function(block) {
  var code = '"' + block.getFieldValue('RESOLUTION') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.Blocks['elapsedTime2_typedEnum_resolution'] =
    Blockly.Blocks['elapsedTime2_enum_resolution'];

Blockly.JavaScript['elapsedTime2_typedEnum_resolution'] =
    Blockly.JavaScript['elapsedTime2_enum_resolution'];

// Functions

Blockly.Blocks['elapsedTime2_reset'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('ElapsedTime'))
        .appendField('.')
        .appendField(createNonEditableField('reset'));
    this.appendValueInput('ELAPSED_TIME').setCheck('ElapsedTime')
        .appendField('timer')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Reset this timer.');
  }
};

Blockly.JavaScript['elapsedTime2_reset'] = function(block) {
  var elapsedTime = Blockly.JavaScript.valueToCode(
      block, 'ELAPSED_TIME', Blockly.JavaScript.ORDER_NONE);
  return elapsedTimeIdentifier + '.reset(' + elapsedTime + ');\n';
};

Blockly.Blocks['elapsedTime2_log'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('ElapsedTime'))
        .appendField('.')
        .appendField(createNonEditableField('log'));
    this.appendValueInput('ELAPSED_TIME').setCheck('ElapsedTime')
        .appendField('timer')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('LABEL').setCheck('String')
        .appendField('label')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Log a message stating how long the timer has been running.');
  }
};

Blockly.JavaScript['elapsedTime2_log'] = function(block) {
  var elapsedTime = Blockly.JavaScript.valueToCode(
      block, 'ELAPSED_TIME', Blockly.JavaScript.ORDER_COMMA);
  var label = Blockly.JavaScript.valueToCode(
      block, 'LABEL', Blockly.JavaScript.ORDER_COMMA);
  return elapsedTimeIdentifier + '.log(' + elapsedTime + ', ' + label + ');\n';
};

Blockly.Blocks['elapsedTime2_toText'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('ElapsedTime'))
        .appendField('.')
        .appendField(createNonEditableField('toText'));
    this.appendValueInput('ELAPSED_TIME').setCheck('ElapsedTime')
        .appendField('timer')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns text indicating the current elapsed time of the timer.');
  }
};

Blockly.JavaScript['elapsedTime2_toText'] = function(block) {
  var elapsedTime = Blockly.JavaScript.valueToCode(
      block, 'ELAPSED_TIME', Blockly.JavaScript.ORDER_NONE);
  var code = elapsedTimeIdentifier + '.toText(' + elapsedTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
