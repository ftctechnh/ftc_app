/**
 * @fileoverview FTC robot blocks related to Tensor Flow Object Detection for Rover Ruckus (2018-2019)
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createRoverRuckusTfodLabelDropdown
// ROVER_RUCKUS_TFOD_LABEL_TOOLTIPS
// tfodRoverRuckusIdentifierForJavaScript
// vuforiaRoverRuckusIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor

Blockly.Blocks['tfodRoverRuckus_initialize'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('TensorFlowObjectDetection'))
        .appendField('.')
        .appendField(createNonEditableField('initialize'));
    this.appendValueInput('MINIMUM_CONFIDENCE').setCheck('Number')
        .appendField('minimumConfidence')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('USE_OBJECT_TRACKER').setCheck('Boolean')
        .appendField('useObjectTracker')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ENABLE_CAMERA_MONITORING').setCheck('Boolean')
        .appendField('enableCameraMonitoring')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Initialize Tensor Flow Object Detection for Rover Ruckus.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'MINIMUM_CONFIDENCE':
          return 'double';
      }
      return '';
    };
  }
};

Blockly.JavaScript['tfodRoverRuckus_initialize'] = function(block) {
  return tfod_initialize_JavaScript(block, tfodRoverRuckusIdentifierForJavaScript, vuforiaRoverRuckusIdentifierForJavaScript);
};

Blockly.FtcJava['tfodRoverRuckus_initialize'] = function(block) {
  return tfod_initialize_FtcJava(block, 'TfodRoverRuckus', 'VuforiaRoverRuckus');
};

Blockly.Blocks['tfodRoverRuckus_activate'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('TensorFlowObjectDetection'))
        .appendField('.')
        .appendField(createNonEditableField('activate'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Activates object detection.');
  }
};

Blockly.JavaScript['tfodRoverRuckus_activate'] = function(block) {
  return tfodRoverRuckusIdentifierForJavaScript + '.activate();\n';
};

Blockly.FtcJava['tfodRoverRuckus_activate'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'TfodRoverRuckus');
  return identifier + '.activate();\n';
};

Blockly.Blocks['tfodRoverRuckus_deactivate'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('TensorFlowObjectDetection'))
        .appendField('.')
        .appendField(createNonEditableField('deactivate'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Deactivates object detection.');
  }
};

Blockly.JavaScript['tfodRoverRuckus_deactivate'] = function(block) {
  return tfodRoverRuckusIdentifierForJavaScript + '.deactivate();\n';
};

Blockly.FtcJava['tfodRoverRuckus_deactivate'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'TfodRoverRuckus');
  return identifier + '.deactivate();\n';
};

Blockly.Blocks['tfodRoverRuckus_getRecognitions'] = {
  init: function() {
    this.setOutput(true, 'Array');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('TensorFlowObjectDetection'))
        .appendField('.')
        .appendField(createNonEditableField('getRecognitions'));
    this.setColour(functionColor);
    this.setTooltip('Returns a List of the current recognitions.');
  }
};

Blockly.JavaScript['tfodRoverRuckus_getRecognitions'] = function(block) {
  var code = 'JSON.parse(' + tfodRoverRuckusIdentifierForJavaScript + '.getRecognitions())';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['tfodRoverRuckus_getRecognitions'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'TfodRoverRuckus');
  var code = identifier + '.getRecognitions()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['tfodRoverRuckus_typedEnum_label'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField(createNonEditableField('Label'))
        .appendField('.')
        .appendField(createRoverRuckusTfodLabelDropdown(), 'LABEL');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = ROVER_RUCKUS_TFOD_LABEL_TOOLTIPS;
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('LABEL');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['tfodRoverRuckus_typedEnum_label'] = function(block) {
  var code = '"' + block.getFieldValue('LABEL') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['tfodRoverRuckus_typedEnum_label'] = function(block) {
  // Even in Java, a label is actually just a string, not an enum.
  var code = '"' + block.getFieldValue('LABEL') + '"';
  return [code, Blockly.FtcJava.ORDER_ATOMIC];
};
