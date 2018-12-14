/**
 * @fileoverview FTC robot blocks related to Vuforia
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// miscIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// functionColor
// getPropertyColor

function vuforia_initialize_withCameraDirection_JavaScript(block, vuforiaLicenseKey, identifier) {
  var cameraDirection = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_DIRECTION', Blockly.JavaScript.ORDER_COMMA);
  var useExtendedTracking = Blockly.JavaScript.valueToCode(
      block, 'USE_EXTENDED_TRACKING', Blockly.JavaScript.ORDER_COMMA);
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
  var useCompetitionFieldTargetLocations = Blockly.JavaScript.valueToCode(
      block, 'USE_COMPETITION_FIELD_TARGET_LOCATIONS', Blockly.JavaScript.ORDER_COMMA);
  // During the pull-request review, this block didn't always have a
  // USE_COMPETITION_FIELD_TARGET_LOCATIONS input. If useCompetitionFieldTargetLocations is '',
  // explicitly set it to 'true'.
  if (useCompetitionFieldTargetLocations == '') {
    useCompetitionFieldTargetLocations = 'true';
  }
  return identifier + '.initialize_withCameraDirection(' + vuforiaLicenseKey + ', ' +
      cameraDirection + ', ' + useExtendedTracking + ', ' +
      enableCameraMonitoring + ', ' + cameraMonitorFeedback + ', ' +
      dx + ', ' + dy + ', ' + dz + ', ' + xAngle + ', ' + yAngle + ', ' + zAngle + ', ' +
      useCompetitionFieldTargetLocations + ');\n';
}

function vuforia_initialize_withCameraDirection_FtcJava(block, vuforiaLicenseKey, className) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, className);
  var cameraDirection = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_DIRECTION', Blockly.FtcJava.ORDER_COMMA);
  var useExtendedTracking = Blockly.FtcJava.valueToCode(
      block, 'USE_EXTENDED_TRACKING', Blockly.FtcJava.ORDER_COMMA);
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
  var useCompetitionFieldTargetLocations = Blockly.FtcJava.valueToCode(
      block, 'USE_COMPETITION_FIELD_TARGET_LOCATIONS', Blockly.FtcJava.ORDER_COMMA);
  // During the pull-request review, this block didn't always have a
  // USE_COMPETITION_FIELD_TARGET_LOCATIONS input. If useCompetitionFieldTargetLocations is '',
  // explicitly set it to 'true'.
  if (useCompetitionFieldTargetLocations == '') {
    useCompetitionFieldTargetLocations = 'true';
  }
  return identifier + '.initialize(' + vuforiaLicenseKey + ', ' + cameraDirection + ',\n' +
      Blockly.FtcJava.INDENT_CONTINUE +
      useExtendedTracking + ', ' + enableCameraMonitoring + ', ' + cameraMonitorFeedback + ',\n' +
      Blockly.FtcJava.INDENT_CONTINUE +
      dx + ', ' + dy + ', ' + dz + ', ' + xAngle + ', ' + yAngle + ', ' + zAngle + ', ' + useCompetitionFieldTargetLocations + ');\n';
}

function vuforia_initialize_withWebcam_JavaScript(block, vuforiaLicenseKey, identifier) {
  var cameraName = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_NAME', Blockly.JavaScript.ORDER_COMMA);
  var webcamCalibrationFilename = Blockly.JavaScript.valueToCode(
      block, 'WEBCAM_CALIBRATION_FILE', Blockly.JavaScript.ORDER_COMMA);
  var useExtendedTracking = Blockly.JavaScript.valueToCode(
      block, 'USE_EXTENDED_TRACKING', Blockly.JavaScript.ORDER_COMMA);
  var enableCameraMonitoring = Blockly.JavaScript.valueToCode(
      block, 'ENABLE_CAMERA_MONITORING', Blockly.JavaScript.ORDER_COMMA);
  var cameraMonitorFeedback = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_MONITOR_FEEDBACK', Blockly.JavaScript.ORDER_COMMA);
  var dx = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT_DX', Blockly.JavaScript.ORDER_COMMA);
  var dy = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT_DY', Blockly.JavaScript.ORDER_COMMA);
  var dz = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT_DZ', Blockly.JavaScript.ORDER_COMMA);
  var xAngle = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT_X_ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var yAngle = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT_Y_ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var zAngle = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT_Z_ANGLE', Blockly.JavaScript.ORDER_COMMA);
  var useCompetitionFieldTargetLocations = Blockly.JavaScript.valueToCode(
      block, 'USE_COMPETITION_FIELD_TARGET_LOCATIONS', Blockly.JavaScript.ORDER_COMMA);
  return identifier + '.initialize_withWebcam(' +
      cameraName + ', ' + webcamCalibrationFilename + ', ' + useExtendedTracking + ', ' +
      enableCameraMonitoring + ', ' + cameraMonitorFeedback + ', ' +
      dx + ', ' + dy + ', ' + dz + ', ' + xAngle + ', ' + yAngle + ', ' + zAngle + ', ' +
      useCompetitionFieldTargetLocations + ');\n';
}

function vuforia_initialize_withWebcam_FtcJava(block, className) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, className);
  var cameraName = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_NAME', Blockly.FtcJava.ORDER_COMMA);
  var webcamCalibrationFilename = Blockly.FtcJava.valueToCode(
      block, 'WEBCAM_CALIBRATION_FILE', Blockly.FtcJava.ORDER_COMMA);
  var useExtendedTracking = Blockly.FtcJava.valueToCode(
      block, 'USE_EXTENDED_TRACKING', Blockly.FtcJava.ORDER_COMMA);
  var enableCameraMonitoring = Blockly.FtcJava.valueToCode(
      block, 'ENABLE_CAMERA_MONITORING', Blockly.FtcJava.ORDER_COMMA);
  var cameraMonitorFeedback = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_MONITOR_FEEDBACK', Blockly.FtcJava.ORDER_COMMA);
  var dx = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT_DX', Blockly.FtcJava.ORDER_COMMA);
  var dy = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT_DY', Blockly.FtcJava.ORDER_COMMA);
  var dz = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT_DZ', Blockly.FtcJava.ORDER_COMMA);
  var xAngle = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT_X_ANGLE', Blockly.FtcJava.ORDER_COMMA);
  var yAngle = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT_Y_ANGLE', Blockly.FtcJava.ORDER_COMMA);
  var zAngle = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT_Z_ANGLE', Blockly.FtcJava.ORDER_COMMA);
  var useCompetitionFieldTargetLocations = Blockly.FtcJava.valueToCode(
      block, 'USE_COMPETITION_FIELD_TARGET_LOCATIONS', Blockly.FtcJava.ORDER_COMMA);
  return identifier + '.initialize("", ' + cameraName + ', ' + webcamCalibrationFilename + ',\n' +
      Blockly.FtcJava.INDENT_CONTINUE +
      useExtendedTracking + ', ' + enableCameraMonitoring + ', ' + cameraMonitorFeedback + ',\n' +
      Blockly.FtcJava.INDENT_CONTINUE +
      dx + ', ' + dy + ', ' + dz + ', ' + xAngle + ', ' + yAngle + ', ' + zAngle + ', ' + useCompetitionFieldTargetLocations + ');';
}

// TrackingResults

Blockly.Blocks['vuforiaTrackingResults_getProperty_String'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Name', 'Name'],
    ];
    this.setOutput(true, 'String');
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
        ['Name', 'Returns the name of the associated trackable.'],
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

Blockly.JavaScript['vuforiaTrackingResults_getProperty_String'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaTrackingResults = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.JavaScript.ORDER_MEMBER);
  var code = vuforiaTrackingResults + '.' + property;
  return [code, Blockly.JavaScript.ORDER_MEMBER];
};

Blockly.FtcJava['vuforiaTrackingResults_getProperty_String'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaTrackingResults = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.FtcJava.ORDER_MEMBER);
  var code = vuforiaTrackingResults + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property);
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['vuforiaTrackingResults_getProperty_Boolean'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['IsVisible', 'IsVisible'],
        ['IsUpdatedRobotLocation', 'IsUpdatedRobotLocation'],
    ];
    this.setOutput(true, 'Boolean');
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
        ['IsVisible', 'Returns true if the associated trackable is visible, false otherwise.'],
        ['IsUpdatedRobotLocation', 'Returns true if these results contain an updated robot location, false otherwise.'],
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

Blockly.JavaScript['vuforiaTrackingResults_getProperty_Boolean'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaTrackingResults = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.JavaScript.ORDER_MEMBER);
  var code = vuforiaTrackingResults + '.' + property;
  return [code, Blockly.JavaScript.ORDER_MEMBER];
};

Blockly.FtcJava['vuforiaTrackingResults_getProperty_Boolean'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaTrackingResults = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.FtcJava.ORDER_MEMBER);
  var code = vuforiaTrackingResults + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property);
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};


Blockly.Blocks['vuforiaTrackingResults_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['X', 'X'],
        ['Y', 'Y'],
        ['Z', 'Z'],
        ['XAngle', 'XAngle'],
        ['YAngle', 'YAngle'],
        ['ZAngle', 'ZAngle'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackingResults'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKING_RESULTS').setCheck('VuforiaBase.TrackingResults')
        .appendField('vuforiaTrackingResults')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['X', 'Returns the X value of the given VuforiaTrackingResults object.'],
        ['Y', 'Returns the Y value of the given VuforiaTrackingResults object.'],
        ['Z', 'Returns the Z value of the given VuforiaTrackingResults object.'],
        ['XAngle', 'Returns the X rotation angle of the given VuforiaTrackingResults object.'],
        ['YAngle', 'Returns the Y rotation angle of the given VuforiaTrackingResults object.'],
        ['ZAngle', 'Returns the Z rotation angle of the given VuforiaTrackingResults object.'],
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
        case 'X':
        case 'Y':
        case 'Z':
        case 'XAngle':
        case 'YAngle':
        case 'ZAngle':
          return 'float';
        default:
          throw 'Unexpected property ' + property + ' (vuforiaTrackingResults_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['vuforiaTrackingResults_getProperty_Number'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaTrackingResults = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.JavaScript.ORDER_MEMBER);
  var code = vuforiaTrackingResults + '.' + property;
  return [code, Blockly.JavaScript.ORDER_MEMBER];
};

Blockly.FtcJava['vuforiaTrackingResults_getProperty_Number'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaTrackingResults = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.FtcJava.ORDER_MEMBER);
  var code = vuforiaTrackingResults + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property);
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['vuforiaTrackingResults_getUpdatedRobotLocation'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackingResults'))
        .appendField('.')
        .appendField(createNonEditableField('getUpdatedRobotLocation'));
    this.appendValueInput('VUFORIA_TRACKING_RESULTS').setCheck('VuforiaBase.TrackingResults')
        .appendField('vuforiaTrackingResults')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a matrix representing the location of the robot.');
  }
};

Blockly.JavaScript['vuforiaTrackingResults_getUpdatedRobotLocation'] = function(block) {
  var vuforiaTrackingResults = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.JavaScript.ORDER_MEMBER);
  var code = vuforiaTrackingResults + '.IsUpdatedRobotLocation ' +
      '? ' + miscIdentifierForJavaScript + '.getUpdatedRobotLocation(' +
          vuforiaTrackingResults + '.X, ' + vuforiaTrackingResults + '.Y, ' + vuforiaTrackingResults + '.Z, ' +
          vuforiaTrackingResults + '.XAngle, ' + vuforiaTrackingResults + '.YAngle, ' + vuforiaTrackingResults + '.ZAngle) ' +
      ': ' + miscIdentifierForJavaScript + '.getNull()';
  return [code, Blockly.JavaScript.ORDER_CONDITIONAL];
};

Blockly.FtcJava['vuforiaTrackingResults_getUpdatedRobotLocation'] = function(block) {
  var vuforiaTrackingResults = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.FtcJava.ORDER_MEMBER);
  var code = vuforiaTrackingResults + '.isUpdatedRobotLocation ' +
      '? ' + vuforiaTrackingResults + '.matrix' +
      ': null';
  return [code, Blockly.FtcJava.ORDER_CONDITIONAL];
};

Blockly.Blocks['vuforiaTrackingResults_formatAsTransform'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackingResults'))
        .appendField('.')
        .appendField(createNonEditableField('formatAsTransform'));
    this.appendValueInput('VUFORIA_TRACKING_RESULTS').setCheck('VuforiaBase.TrackingResults')
        .appendField('vuforiaTrackingResults')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a text representation of the given VuforiaTrackingResults.');
  }
};

Blockly.JavaScript['vuforiaTrackingResults_formatAsTransform'] = function(block) {
  var vuforiaTrackingResults = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.JavaScript.ORDER_MEMBER);
  var code = '"{EXTRINSIC XYZ " + ' +
      vuforiaTrackingResults + '.XAngle.toFixed(0) + " " + ' +
      vuforiaTrackingResults + '.YAngle.toFixed(0) + " " + ' +
      vuforiaTrackingResults + '.ZAngle.toFixed(0) + "} {" + ' +
      vuforiaTrackingResults + '.X.toFixed(2) + " " + ' +
      vuforiaTrackingResults + '.Y.toFixed(2) + " " + ' +
      vuforiaTrackingResults + '.Z.toFixed(2) + "}"';
  return [code, Blockly.JavaScript.ORDER_ADDITION];
};

Blockly.FtcJava['vuforiaTrackingResults_formatAsTransform'] = function(block) {
  var vuforiaTrackingResults = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.FtcJava.ORDER_MEMBER);
  var code = vuforiaTrackingResults + '.formatAsTransform()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforiaTrackingResults_toText'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackingResults'))
        .appendField('.')
        .appendField(createNonEditableField('toText'));
    this.appendValueInput('VUFORIA_TRACKING_RESULTS').setCheck('VuforiaBase.TrackingResults')
        .appendField('vuforiaTrackingResults')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a text representation of the given VuforiaTrackingResults.');
  }
};

Blockly.JavaScript['vuforiaTrackingResults_toText'] = function(block) {
  var vuforiaTrackingResults = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.JavaScript.ORDER_NONE);
  var code = 'JSON.stringify(' + vuforiaTrackingResults + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforiaTrackingResults_toText'] = function(block) {
  var vuforiaTrackingResults = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.FtcJava.ORDER_MEMBER);
  var code = vuforiaTrackingResults + '.toString()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

