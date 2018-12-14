/**
 * @fileoverview FTC robot blocks related to ElapsedTime.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// elapsedTimeIdentifierForJavaScript
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
    this.setTooltip('Creates an ElapsedTime object representing a timer with SECONDS resolution, ' +
        'initialized with the current time.');
  }
};

Blockly.JavaScript['elapsedTime2_create'] = function(block) {
  var code = elapsedTimeIdentifierForJavaScript + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['elapsedTime2_create'] = function(block) {
  var code = 'new ElapsedTime()';
  Blockly.FtcJava.generateImport_('ElapsedTime');
  return [code, Blockly.FtcJava.ORDER_NEW];
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
    this.setTooltip('Creates an ElapsedTime object representing a timer with SECONDS resolution, ' +
        'initialized with the given start time.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'START_TIME':
          return 'long';
      }
      return '';
    };
  }
};

Blockly.JavaScript['elapsedTime2_create_withStartTime'] = function(block) {
  var startTime = Blockly.JavaScript.valueToCode(
      block, 'START_TIME', Blockly.JavaScript.ORDER_NONE);
  var code = elapsedTimeIdentifierForJavaScript + '.create_withStartTime(' + startTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['elapsedTime2_create_withStartTime'] = function(block) {
  var startTime = Blockly.FtcJava.valueToCode(
      block, 'START_TIME', Blockly.FtcJava.ORDER_NONE);
  var code = 'new ElapsedTime(' + startTime + ')';
  Blockly.FtcJava.generateImport_('ElapsedTime');
  return [code, Blockly.FtcJava.ORDER_NEW];
};

Blockly.Blocks['elapsedTime2_create_withResolution'] = {
  init: function() {
    this.setOutput(true, 'ElapsedTime');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('ElapsedTime'));
    this.appendValueInput('RESOLUTION').setCheck('ElapsedTime.Resolution')
        .appendField('resolution')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Creates an ElapsedTime object representing a timer with the given resolution, ' +
        'initialized with the current time.');
  }
};

Blockly.JavaScript['elapsedTime2_create_withResolution'] = function(block) {
  var resolution = Blockly.JavaScript.valueToCode(
      block, 'RESOLUTION', Blockly.JavaScript.ORDER_NONE);
  var code = elapsedTimeIdentifierForJavaScript + '.create_withResolution(' + resolution + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['elapsedTime2_create_withResolution'] = function(block) {
  var resolution = Blockly.FtcJava.valueToCode(
      block, 'RESOLUTION', Blockly.FtcJava.ORDER_NONE);
  var code = 'new ElapsedTime(' + resolution + ')';
  Blockly.FtcJava.generateImport_('ElapsedTime');
  return [code, Blockly.FtcJava.ORDER_NEW];
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
        ['StartTime', 'Returns a numeric value representing the time at which this timer was last reset.'],
        ['Time', 'Returns the duration that has elapsed since the last reset of this timer.'],
        ['Seconds', 'Returns the duration that has elapsed since the last reset of this timer, ' +
            'in seconds.'],
        ['Milliseconds', 'Returns the duration that has elapsed since the last reset of this timer, ' +
            'in milliseconds.'],
        ['Resolution', 'Returns the Resolution with which the timer was created.'],
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
  var code = elapsedTimeIdentifierForJavaScript + '.get' + property + '(' + elapsedTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['elapsedTime2_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var elapsedTime = Blockly.FtcJava.valueToCode(
      block, 'ELAPSED_TIME', Blockly.FtcJava.ORDER_MEMBER);
  var code;
  switch (property) {
    case 'StartTime':
    case 'Time':
    case 'Seconds':
    case 'Milliseconds':
      code = elapsedTime + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property) + '()';
      break;
    case 'Resolution':
      code = elapsedTime + '.get' + property + '()';
      break;
    default:
      throw 'Unexpected property ' + property + ' (elapsedTime2_getProperty).';
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
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
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['StartTime', 'Returns a numeric value representing the time at which this timer was last reset.'],
        ['Time', 'Returns the duration that has elapsed since the last reset of this timer.'],
        ['Seconds', 'Returns the duration that has elapsed since the last reset of this timer, ' +
            'in seconds.'],
        ['Milliseconds', 'Returns the duration that has elapsed since the last reset of this timer, ' +
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
    this.getFtcJavaOutputType = function() {
      var property = thisBlock.getFieldValue('PROP');
      switch (property) {
        case 'StartTime':
        case 'Time':
        case 'Seconds':
        case 'Milliseconds':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (elapsedTime2_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['elapsedTime2_getProperty_Number'] =
    Blockly.JavaScript['elapsedTime2_getProperty'];

Blockly.FtcJava['elapsedTime2_getProperty_Number'] =
    Blockly.FtcJava['elapsedTime2_getProperty'];

Blockly.Blocks['elapsedTime2_getProperty_Resolution'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Resolution', 'Resolution'],
    ];
    this.setOutput(true, 'ElapsedTime.Resolution');
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
        ['Resolution', 'Returns the Resolution with which the timer was created.'],
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

Blockly.FtcJava['elapsedTime2_getProperty_Resolution'] =
    Blockly.FtcJava['elapsedTime2_getProperty'];

// Enums

Blockly.Blocks['elapsedTime2_enum_resolution'] = {
  init: function() {
    var RESOLUTION_CHOICES = [
        ['SECONDS', 'SECONDS'],
        ['MILLISECONDS', 'MILLISECONDS'],
    ];
    this.setOutput(true, 'ElapsedTime.Resolution');
    this.appendDummyInput()
        .appendField(createNonEditableField('Resolution'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(RESOLUTION_CHOICES), 'RESOLUTION');
    this.setColour(getPropertyColor);
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
  }
};

Blockly.JavaScript['elapsedTime2_enum_resolution'] = function(block) {
  var code = '"' + block.getFieldValue('RESOLUTION') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['elapsedTime2_enum_resolution'] = function(block) {
  var code = 'ElapsedTime.Resolution.' + block.getFieldValue('RESOLUTION');
  Blockly.FtcJava.generateImport_('ElapsedTime');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['elapsedTime2_typedEnum_resolution'] =
    Blockly.Blocks['elapsedTime2_enum_resolution'];

Blockly.JavaScript['elapsedTime2_typedEnum_resolution'] =
    Blockly.JavaScript['elapsedTime2_enum_resolution'];

Blockly.FtcJava['elapsedTime2_typedEnum_resolution'] =
    Blockly.FtcJava['elapsedTime2_enum_resolution'];

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
  return elapsedTimeIdentifierForJavaScript + '.reset(' + elapsedTime + ');\n';
};

Blockly.FtcJava['elapsedTime2_reset'] = function(block) {
  var elapsedTime = Blockly.FtcJava.valueToCode(
      block, 'ELAPSED_TIME', Blockly.FtcJava.ORDER_MEMBER);
  return elapsedTime + '.reset();\n';
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
  return elapsedTimeIdentifierForJavaScript + '.log(' + elapsedTime + ', ' + label + ');\n';
};

Blockly.FtcJava['elapsedTime2_log'] = function(block) {
  var elapsedTime = Blockly.FtcJava.valueToCode(
      block, 'ELAPSED_TIME', Blockly.FtcJava.ORDER_MEMBER);
  var label = Blockly.FtcJava.valueToCode(
      block, 'LABEL', Blockly.FtcJava.ORDER_NONE);
  return elapsedTime + '.log(' +  label + ');\n';
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
  var code = elapsedTimeIdentifierForJavaScript + '.toText(' + elapsedTime + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['elapsedTime2_toText'] = function(block) {
  var elapsedTime = Blockly.FtcJava.valueToCode(
      block, 'ELAPSED_TIME', Blockly.FtcJava.ORDER_MEMBER);
  var code = elapsedTime + '.toString()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
