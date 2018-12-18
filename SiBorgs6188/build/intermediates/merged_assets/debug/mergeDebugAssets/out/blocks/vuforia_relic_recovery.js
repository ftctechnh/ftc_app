/**
 * @fileoverview FTC robot blocks related to Vuforia for Relic Recovery (2017-2018)
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// vuforiaIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor

/**
 * Deprecated. See vuforia_initializeExtendedNoKey.
 */
Blockly.Blocks['vuforia_initialize'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Vuforia'))
        .appendField('.')
        .appendField(createNonEditableField('initialize'));
    this.appendValueInput('VUFORIA_LICENSE_KEY').setCheck('String')
        .appendField('vuforiaLicenseKey')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_DIRECTION').setCheck('VuforiaLocalizer.CameraDirection')
        .appendField('cameraDirection')
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
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Initialize Vuforia for Relic Recovery.');
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

/**
 * Deprecated. See vuforia_initializeExtendedNoKey.
 */
Blockly.JavaScript['vuforia_initialize'] = function(block) {
  var vuforiaLicenseKey = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LICENSE_KEY', Blockly.JavaScript.ORDER_COMMA);
  var cameraDirection = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_DIRECTION', Blockly.JavaScript.ORDER_COMMA);
  var enableCameraMonitoring = Blockly.JavaScript.valueToCode(
      block, 'ENABLE_CAMERA_MONITORING', Blockly.JavaScript.ORDER_COMMA);
  var cameraMonitorFeedback = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_MONITOR_FEEDBACK', Blockly.JavaScript.ORDER_COMMA);
  var dx = Blockly.JavaScript.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT_DX', Blockly.JavaScript.ORDER_COMMA);
  var dy = Blockly.JavaScript.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT_DY', Blockly.JavaScript.ORDER_COMMA);
  var dz = Blockly.JavaScript.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT_DZ', Blockly.JavaScript.ORDER_COMMA);
  var xAngle = Blockly.JavaScript.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT_X_ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var yAngle = Blockly.JavaScript.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT_Y_ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var zAngle = Blockly.JavaScript.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT_Z_ANGLE', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaIdentifierForJavaScript + '.initialize(' + vuforiaLicenseKey + ', ' +
      cameraDirection + ', ' + enableCameraMonitoring + ', ' + cameraMonitorFeedback + ', ' +
      dx + ', ' + dy + ', ' + dz + ', ' + xAngle + ', ' + yAngle + ', ' + zAngle + ');\n';
};

/**
 * Deprecated. See vuforia_initializeExtendedNoKey.
 */
Blockly.FtcJava['vuforia_initialize'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'VuforiaRelicRecovery');
  var vuforiaLicenseKey = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_LICENSE_KEY', Blockly.FtcJava.ORDER_COMMA);
  var cameraDirection = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_DIRECTION', Blockly.FtcJava.ORDER_COMMA);
  var useExtendedTracking = 'true';
  var enableCameraMonitoring = Blockly.FtcJava.valueToCode(
      block, 'ENABLE_CAMERA_MONITORING', Blockly.FtcJava.ORDER_COMMA);
  var cameraMonitorFeedback = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_MONITOR_FEEDBACK', Blockly.FtcJava.ORDER_COMMA);
  var dx = Blockly.FtcJava.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT_DX', Blockly.FtcJava.ORDER_COMMA);
  var dy = Blockly.FtcJava.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT_DY', Blockly.FtcJava.ORDER_COMMA);
  var dz = Blockly.FtcJava.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT_DZ', Blockly.FtcJava.ORDER_COMMA);
  var xAngle = Blockly.FtcJava.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT_X_ANGLE', Blockly.FtcJava.ORDER_COMMA);
  var yAngle = Blockly.FtcJava.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT_Y_ANGLE', Blockly.FtcJava.ORDER_COMMA);
  var zAngle = Blockly.FtcJava.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT_Z_ANGLE', Blockly.FtcJava.ORDER_COMMA);
  var useCompetitionFieldTargetLocations = 'true';
  return identifier + '.initialize(' + vuforiaLicenseKey + ', ' + cameraDirection + ',\n' +
      Blockly.FtcJava.INDENT_CONTINUE +
      useExtendedTracking + ', ' + enableCameraMonitoring + ', ' + cameraMonitorFeedback + ',\n' +
      Blockly.FtcJava.INDENT_CONTINUE +
      dx + ', ' + dy + ', ' + dz + ', ' + xAngle + ', ' + yAngle + ', ' + zAngle + ', ' + useCompetitionFieldTargetLocations + ');\n';
};

/**
 * Deprecated. See vuforia_initializeExtendedNoKey.
 */
Blockly.Blocks['vuforia_initializeExtended'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Vuforia'))
        .appendField('.')
        .appendField(createNonEditableField('initialize'));
    this.appendValueInput('VUFORIA_LICENSE_KEY').setCheck('String')
        .appendField('vuforiaLicenseKey')
        .setAlign(Blockly.ALIGN_RIGHT);
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
    this.setTooltip('Initialize Vuforia for Relic Recovery.');
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

/**
 * Deprecated. See vuforia_initializeExtendedNoKey.
 */
Blockly.JavaScript['vuforia_initializeExtended'] = function(block) {
  var vuforiaLicenseKey = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LICENSE_KEY', Blockly.JavaScript.ORDER_COMMA);
  return vuforia_initialize_withCameraDirection_JavaScript(block, vuforiaLicenseKey, vuforiaIdentifierForJavaScript);
};

/**
 * Deprecated. See vuforia_initializeExtendedNoKey.
 */
Blockly.FtcJava['vuforia_initializeExtended'] = function(block) {
  var vuforiaLicenseKey = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_LICENSE_KEY', Blockly.FtcJava.ORDER_COMMA);
  return vuforia_initialize_withCameraDirection_FtcJava(block, vuforiaLicenseKey, 'VuforiaRelicRecovery');
};

Blockly.Blocks['vuforia_initializeExtendedNoKey'] = {
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
    this.setTooltip('Initialize Vuforia for Relic Recovery.');
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

Blockly.JavaScript['vuforia_initializeExtendedNoKey'] = function(block) {
  return vuforia_initialize_withCameraDirection_JavaScript(block, '""', vuforiaIdentifierForJavaScript);
};

Blockly.FtcJava['vuforia_initializeExtendedNoKey'] = function(block) {
  return vuforia_initialize_withCameraDirection_FtcJava(block, '""', 'VuforiaRelicRecovery');
};

Blockly.Blocks['vuforia_initialize_withWebcam'] = {
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
    this.setTooltip('Initialize Vuforia for Relic Recovery.');
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

Blockly.JavaScript['vuforia_initialize_withWebcam'] = function(block) {
  return vuforia_initialize_withWebcam_JavaScript(block, '""', vuforiaIdentifierForJavaScript);
};

Blockly.FtcJava['vuforia_initialize_withWebcam'] = function(block) {
  return vuforia_initialize_withWebcam_FtcJava(block, 'VuforiaRelicRecovery');
};

Blockly.Blocks['vuforia_activate'] = {
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

Blockly.JavaScript['vuforia_activate'] = function(block) {
  return vuforiaIdentifierForJavaScript + '.activate();\n';
};

Blockly.FtcJava['vuforia_activate'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'VuforiaRelicRecovery');
  return identifier + '.activate();\n';
};

Blockly.Blocks['vuforia_deactivate'] = {
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

Blockly.JavaScript['vuforia_deactivate'] = function(block) {
  return vuforiaIdentifierForJavaScript + '.deactivate();\n';
};

Blockly.FtcJava['vuforia_deactivate'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'VuforiaRelicRecovery');
  return identifier + '.deactivate();\n';
};

Blockly.Blocks['vuforia_track'] = {
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

Blockly.JavaScript['vuforia_track'] = function(block) {
  var trackableName = Blockly.JavaScript.valueToCode(
      block, 'TRACKABLE_NAME', Blockly.JavaScript.ORDER_NONE);
  var code = 'JSON.parse(' + vuforiaIdentifierForJavaScript + '.track(' + trackableName + '))';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforia_track'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'VuforiaRelicRecovery');
  var trackableName = Blockly.FtcJava.valueToCode(
      block, 'TRACKABLE_NAME', Blockly.FtcJava.ORDER_NONE);
  var code = identifier + '.track(' + trackableName + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforia_trackPose'] = {
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

Blockly.JavaScript['vuforia_trackPose'] = function(block) {
  var trackableName = Blockly.JavaScript.valueToCode(
      block, 'TRACKABLE_NAME', Blockly.JavaScript.ORDER_NONE);
  var code = 'JSON.parse(' + vuforiaIdentifierForJavaScript + '.trackPose(' + trackableName + '))';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforia_trackPose'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'VuforiaRelicRecovery');
  var trackableName = Blockly.FtcJava.valueToCode(
      block, 'TRACKABLE_NAME', Blockly.FtcJava.ORDER_NONE);
  var code = identifier + '.trackPose(' + trackableName + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforia_typedEnum_trackableName'] = {
  init: function() {
    var TRACKABLE_NAME_CHOICES = [
        ['RELIC', 'RELIC'],
    ];
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField(createNonEditableField('TrackableName'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(TRACKABLE_NAME_CHOICES), 'TRACKABLE_NAME');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['RELIC', 'The TrackableName value RELIC.'],
    ];
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

Blockly.JavaScript['vuforia_typedEnum_trackableName'] = function(block) {
  var code = '"' + block.getFieldValue('TRACKABLE_NAME') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['vuforia_typedEnum_trackableName'] = function(block) {
  // Event in Java, trackable name is actually just a string, not an enum.
  var code = '"' + block.getFieldValue('TRACKABLE_NAME') + '"';
  return [code, Blockly.FtcJava.ORDER_ATOMIC];
};

Blockly.Blocks['vuforiaTrackingResults_getProperty_RelicRecoveryVuMark'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['RelicRecoveryVuMark', 'RelicRecoveryVuMark'],
    ];
    this.setOutput(true, 'RelicRecoveryVuMark');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackingResults'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKING_RESULTS').setCheck('VuforiaBase.TrackingResults')
        .appendField('vuforiaTrackingResults')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['RelicRecoveryVuMark', 'Returns the RelicRecoveryVuMark of the given VuforiaTrackingResults object.'],
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

Blockly.JavaScript['vuforiaTrackingResults_getProperty_RelicRecoveryVuMark'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaTrackingResults = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.JavaScript.ORDER_MEMBER);
  var code = vuforiaTrackingResults + '.' + property;
  return [code, Blockly.JavaScript.ORDER_MEMBER];
};

Blockly.FtcJava['vuforiaTrackingResults_getProperty_RelicRecoveryVuMark'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaTrackingResults = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.FtcJava.ORDER_MEMBER);
  // This java code will throw ClassCastException if the vuforiaTrackingResults is not a
  // VuforiaRelicRecovery.TrackingResults.
  Blockly.FtcJava.generateImport_('VuforiaRelicRecovery');
  var code = '((VuforiaRelicRecovery.TrackingResults) ' + vuforiaTrackingResults + ').' +
      Blockly.FtcJava.makeFirstLetterLowerCase_(property);
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['vuMarks_typedEnum_relicRecoveryVuMark'] = {
  init: function() {
    var RELIC_RECOVERY_VU_MARK_CHOICES = [
        ['UNKNOWN', 'UNKNOWN'],
        ['LEFT', 'LEFT'],
        ['CENTER', 'CENTER'],
        ['RIGHT', 'RIGHT'],
    ];
    this.setOutput(true, 'RelicRecoveryVuMark');
    this.appendDummyInput()
        .appendField(createNonEditableField('RelicRecoveryVuMark'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(RELIC_RECOVERY_VU_MARK_CHOICES), 'RELIC_RECOVERY_VU_MARK');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['UNKNOWN', 'The RelicRecoveryVuMark value UNKNOWN.'],
        ['LEFT', 'The RelicRecoveryVuMark value LEFT.'],
        ['CENTER', 'The RelicRecoveryVuMark value CENTER.'],
        ['RIGHT', 'The RelicRecoveryVuMark value RIGHT.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('RELIC_RECOVERY_VU_MARK');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['vuMarks_typedEnum_relicRecoveryVuMark'] = function(block) {
  var code = '"' + block.getFieldValue('RELIC_RECOVERY_VU_MARK') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['vuMarks_typedEnum_relicRecoveryVuMark'] = function(block) {
  var code = 'RelicRecoveryVuMark.' + block.getFieldValue('RELIC_RECOVERY_VU_MARK');
  Blockly.FtcJava.generateImport_('RelicRecoveryVuMark');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};
