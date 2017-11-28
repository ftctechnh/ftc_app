/**
 * @fileoverview FTC robot blocks related to linear op mode.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// linearOpModeIdentifier
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// currentProjectName

// Functions

Blockly.Blocks['linearOpMode_waitForStart'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField(currentProjectName))
        .appendField('.')
        .appendField(createNonEditableField('waitForStart'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Wait until start has been pressed.');
  }
};

Blockly.JavaScript['linearOpMode_waitForStart'] = function(block) {
  return linearOpModeIdentifier + '.waitForStart();\n';
};

Blockly.Blocks['linearOpMode_idle'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField(currentProjectName))
        .appendField('.')
        .appendField(createNonEditableField('idle'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Put the current thread to sleep for a bit, allowing other threads in the ' +
        'system to run.');
  }
};

Blockly.JavaScript['linearOpMode_idle'] = function(block) {
  return linearOpModeIdentifier + '.idle();\n';
};

Blockly.Blocks['linearOpMode_sleep'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField(currentProjectName))
        .appendField('.')
        .appendField(createNonEditableField('sleep'));
    this.appendValueInput('MILLISECONDS') // no type, for compatibility
        .appendField('milliseconds')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sleep for the given amount of milliseconds.');
  }
};

Blockly.JavaScript['linearOpMode_sleep'] = function(block) {
  var millis = Blockly.JavaScript.valueToCode(
      block, 'MILLISECONDS', Blockly.JavaScript.ORDER_NONE);
  return linearOpModeIdentifier + '.sleep(' + millis + ');\n';
};

Blockly.Blocks['linearOpMode_sleep_Number'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField(currentProjectName))
        .appendField('.')
        .appendField(createNonEditableField('sleep'));
    this.appendValueInput('MILLISECONDS').setCheck('Number')
        .appendField('milliseconds')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sleep for the given amount of milliseconds.');
  }
};

Blockly.JavaScript['linearOpMode_sleep_Number'] =
    Blockly.JavaScript['linearOpMode_sleep'];

Blockly.Blocks['linearOpMode_opModeIsActive'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField(currentProjectName))
        .appendField('.')
        .appendField(createNonEditableField('opModeIsActive'));
    this.setTooltip('Return true if this opMode is active.');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['linearOpMode_opModeIsActive'] = function(block) {
  var code = linearOpModeIdentifier + '.opModeIsActive()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['linearOpMode_isStarted'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField(currentProjectName))
        .appendField('.')
        .appendField(createNonEditableField('isStarted'));
    this.setTooltip('Return true if this opMode has been started.');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['linearOpMode_isStarted'] = function(block) {
  var code = linearOpModeIdentifier + '.isStarted()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['linearOpMode_isStopRequested'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField(currentProjectName))
        .appendField('.')
        .appendField(createNonEditableField('isStopRequested'));
    this.setTooltip('Return true if stopping of this opMode has been requested.');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['linearOpMode_isStopRequested'] = function(block) {
  var code = linearOpModeIdentifier + '.isStopRequested()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['linearOpMode_getRuntime'] = {
  init: function() {
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField(currentProjectName))
        .appendField('.')
        .appendField(createNonEditableField('getRuntime'));
    this.setTooltip('Get the number of seconds this op mode has been running.');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['linearOpMode_getRuntime'] = function(block) {
  var code = linearOpModeIdentifier + '.getRuntime()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['linearOpMode_getRuntime_Number'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField(currentProjectName))
        .appendField('.')
        .appendField(createNonEditableField('getRuntime'));
    this.setTooltip('Get the number of seconds this op mode has been running.');
    this.setColour(functionColor);
  }
};

Blockly.JavaScript['linearOpMode_getRuntime_Number'] =
    Blockly.JavaScript['linearOpMode_getRuntime'];
