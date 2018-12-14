/**
 * @fileoverview FTC robot blocks related to Vuforia for Rover Ruckus (2018-2019)
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// vuforiaRoverRuckusIdentifierForJavaScript
// createRoverRuckusTrackableNameDropdown
// ROVER_RUCKUS_TRACKABLE_NAME_TOOLTIPS
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor

Blockly.Blocks['vuforiaRoverRuckus_initialize_withCameraDirection'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Vuforia'))
        .appendField('.')
        .appendField(createNonEditableField('initialize'));
    this.appendValueInput('CAMERA_DIRECTION').setCheck('VuforiaLocalizer.CameraDirection')
        .appendField('cameraDirection')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('USE_EXTENDED_TRACKING').setCheck('Boolean')
        .appendField('useExtendedTracking')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ENABLE_CAMERA_MONITORING').setCheck('Boolean')
        .appendField('enableCameraMonitoring')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_MONITOR_FEEDBACK').setCheck('VuforiaLocalizer.Parameters.CameraMonitorFeedback')
        .appendField('cameraMonitorFeedback')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('PHONE_LOCATION_ON_ROBOT_DX').setCheck('Number')
        .appendField('phoneLocationOnRobot translation dx')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('PHONE_LOCATION_ON_ROBOT_DY').setCheck('Number')
        .appendField('phoneLocationOnRobot translation dy')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('PHONE_LOCATION_ON_ROBOT_DZ').setCheck('Number')
        .appendField('phoneLocationOnRobot translation dz')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('PHONE_LOCATION_ON_ROBOT_X_ANGLE').setCheck('Number')
        .appendField('phoneLocationOnRobot rotation x')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('PHONE_LOCATION_ON_ROBOT_Y_ANGLE').setCheck('Number')
        .appendField('phoneLocationOnRobot rotation y')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('PHONE_LOCATION_ON_ROBOT_Z_ANGLE').setCheck('Number')
        .appendField('phoneLocationOnRobot rotation z')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('USE_COMPETITION_FIELD_TARGET_LOCATIONS').setCheck('Boolean')
        .appendField('useCompetitionFieldTargetLocations')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Initialize Vuforia for Rover Ruckus.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'PHONE_LOCATION_ON_ROBOT_DX':
        case 'PHONE_LOCATION_ON_ROBOT_DY':
        case 'PHONE_LOCATION_ON_ROBOT_DZ':
        case 'PHONE_LOCATION_ON_ROBOT_X_ANGLE':
        case 'PHONE_LOCATION_ON_ROBOT_Y_ANGLE':
        case 'PHONE_LOCATION_ON_ROBOT_Z_ANGLE':
          return 'float';
      }
      return '';
    };
  }
};

Blockly.JavaScript['vuforiaRoverRuckus_initialize_withCameraDirection'] = function(block) {
  return vuforia_initialize_withCameraDirection_JavaScript(block, '""', vuforiaRoverRuckusIdentifierForJavaScript);
};

Blockly.FtcJava['vuforiaRoverRuckus_initialize_withCameraDirection'] = function(block) {
  return vuforia_initialize_withCameraDirection_FtcJava(block, '""', 'VuforiaRoverRuckus');
};

Blockly.Blocks['vuforiaRoverRuckus_initialize_withWebcam'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Vuforia'))
        .appendField('.')
        .appendField(createNonEditableField('initialize'));
    this.appendValueInput('CAMERA_NAME').setCheck(['CameraName', 'WebcamName'])
        .appendField('cameraName')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('WEBCAM_CALIBRATION_FILE').setCheck('String')
        .appendField('Webcam Calibration Filename')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('USE_EXTENDED_TRACKING').setCheck('Boolean')
        .appendField('useExtendedTracking')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ENABLE_CAMERA_MONITORING').setCheck('Boolean')
        .appendField('enableCameraMonitoring')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_MONITOR_FEEDBACK').setCheck('VuforiaLocalizer.Parameters.CameraMonitorFeedback')
        .appendField('cameraMonitorFeedback')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_LOCATION_ON_ROBOT_DX').setCheck('Number')
        .appendField('cameraLocationOnRobot translation dx')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_LOCATION_ON_ROBOT_DY').setCheck('Number')
        .appendField('cameraLocationOnRobot translation dy')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_LOCATION_ON_ROBOT_DZ').setCheck('Number')
        .appendField('cameraLocationOnRobot translation dz')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_LOCATION_ON_ROBOT_X_ANGLE').setCheck('Number')
        .appendField('cameraLocationOnRobot rotation x')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_LOCATION_ON_ROBOT_Y_ANGLE').setCheck('Number')
        .appendField('cameraLocationOnRobot rotation y')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_LOCATION_ON_ROBOT_Z_ANGLE').setCheck('Number')
        .appendField('cameraLocationOnRobot rotation z')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('USE_COMPETITION_FIELD_TARGET_LOCATIONS').setCheck('Boolean')
        .appendField('useCompetitionFieldTargetLocations')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Initialize Vuforia for Rover Ruckus.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'CAMERA_LOCATION_ON_ROBOT_DX':
        case 'CAMERA_LOCATION_ON_ROBOT_DY':
        case 'CAMERA_LOCATION_ON_ROBOT_DZ':
        case 'CAMERA_LOCATION_ON_ROBOT_X_ANGLE':
        case 'CAMERA_LOCATION_ON_ROBOT_Y_ANGLE':
        case 'CAMERA_LOCATION_ON_ROBOT_Z_ANGLE':
          return 'float';
      }
      return '';
    };
  }
};

Blockly.JavaScript['vuforiaRoverRuckus_initialize_withWebcam'] = function(block) {
  return vuforia_initialize_withWebcam_JavaScript(block, '""', vuforiaRoverRuckusIdentifierForJavaScript);
};

Blockly.FtcJava['vuforiaRoverRuckus_initialize_withWebcam'] = function(block) {
  return vuforia_initialize_withWebcam_FtcJava(block, 'VuforiaRoverRuckus');
};

Blockly.Blocks['vuforiaRoverRuckus_activate'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Vuforia'))
        .appendField('.')
        .appendField(createNonEditableField('activate'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Activates all trackables, so that it is actively seeking their presence.');
  }
};

Blockly.JavaScript['vuforiaRoverRuckus_activate'] = function(block) {
  return vuforiaRoverRuckusIdentifierForJavaScript + '.activate();\n';
};

Blockly.FtcJava['vuforiaRoverRuckus_activate'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'VuforiaRoverRuckus');
  return identifier + '.activate();\n';
};

Blockly.Blocks['vuforiaRoverRuckus_deactivate'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Vuforia'))
        .appendField('.')
        .appendField(createNonEditableField('deactivate'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Deactivates all trackables, causing it to no longer see their presence.');
  }
};

Blockly.JavaScript['vuforiaRoverRuckus_deactivate'] = function(block) {
  return vuforiaRoverRuckusIdentifierForJavaScript + '.deactivate();\n';
};

Blockly.FtcJava['vuforiaRoverRuckus_deactivate'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'VuforiaRoverRuckus');
  return identifier + '.deactivate();\n';
};

Blockly.Blocks['vuforiaRoverRuckus_track'] = {
  init: function() {
    this.setOutput(true, 'VuforiaBase.TrackingResults');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Vuforia'))
        .appendField('.')
        .appendField(createNonEditableField('track'));
    this.appendValueInput('TRACKABLE_NAME').setCheck('String')
        .appendField('trackableName')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the VuforiaTrackingResults of the trackable with the given name.');
  }
};

Blockly.JavaScript['vuforiaRoverRuckus_track'] = function(block) {
  var trackableName = Blockly.JavaScript.valueToCode(
      block, 'TRACKABLE_NAME', Blockly.JavaScript.ORDER_NONE);
  var code = 'JSON.parse(' + vuforiaRoverRuckusIdentifierForJavaScript + '.track(' + trackableName + '))';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforiaRoverRuckus_track'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'VuforiaRoverRuckus');
  var trackableName = Blockly.FtcJava.valueToCode(
      block, 'TRACKABLE_NAME', Blockly.FtcJava.ORDER_NONE);
  var code = identifier + '.track(' + trackableName + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforiaRoverRuckus_trackPose'] = {
  init: function() {
    this.setOutput(true, 'VuforiaBase.TrackingResults');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Vuforia'))
        .appendField('.')
        .appendField(createNonEditableField('trackPose'));
    this.appendValueInput('TRACKABLE_NAME').setCheck('String')
        .appendField('trackableName')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the VuforiaTrackingResults of the pose of the trackable with the given name. ' +
        'The pose is the location of the trackable in the phone\'s coordinate system.');
  }
};

Blockly.JavaScript['vuforiaRoverRuckus_trackPose'] = function(block) {
  var trackableName = Blockly.JavaScript.valueToCode(
      block, 'TRACKABLE_NAME', Blockly.JavaScript.ORDER_NONE);
  var code = 'JSON.parse(' + vuforiaRoverRuckusIdentifierForJavaScript + '.trackPose(' + trackableName + '))';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforiaRoverRuckus_trackPose'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'VuforiaRoverRuckus');
  var trackableName = Blockly.FtcJava.valueToCode(
      block, 'TRACKABLE_NAME', Blockly.FtcJava.ORDER_NONE);
  var code = identifier + '.trackPose(' + trackableName + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforiaRoverRuckus_typedEnum_trackableName'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField(createNonEditableField('TrackableName'))
        .appendField('.')
        .appendField(createRoverRuckusTrackableNameDropdown(), 'TRACKABLE_NAME');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = ROVER_RUCKUS_TRACKABLE_NAME_TOOLTIPS;
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('TRACKABLE_NAME');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['vuforiaRoverRuckus_typedEnum_trackableName'] = function(block) {
  var code = '"' + block.getFieldValue('TRACKABLE_NAME') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['vuforiaRoverRuckus_typedEnum_trackableName'] = function(block) {
  // Even in Java, a trackable name is actually just a string, not an enum.
  var code = '"' + block.getFieldValue('TRACKABLE_NAME') + '"';
  return [code, Blockly.FtcJava.ORDER_ATOMIC];
};
