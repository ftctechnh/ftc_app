/**
 * @fileoverview FTC robot blocks related to Vuforia
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// vuforiaIdentifier
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
    this.appendValueInput('CAMERA_DIRECTION').setCheck('CameraDirection')
        .appendField('cameraDirection')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ENABLE_CAMERA_MONITORING').setCheck('Boolean')
        .appendField('enableCameraMonitoring')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_MONITOR_FEEDBACK').setCheck('CameraMonitorFeedback')
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
    this.setTooltip('Initialize Vuforia.');
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
  return vuforiaIdentifier + '.initialize(' + vuforiaLicenseKey + ', ' +
      cameraDirection + ', ' + enableCameraMonitoring + ', ' + cameraMonitorFeedback + ', ' +
      dx + ', ' + dy + ', ' + dz + ', ' + xAngle + ', ' + yAngle + ', ' + zAngle + ');\n';
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
    this.appendValueInput('CAMERA_DIRECTION').setCheck('CameraDirection')
        .appendField('cameraDirection')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('USE_EXTENDED_TRACKING').setCheck('Boolean')
        .appendField('useExtendedTracking')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ENABLE_CAMERA_MONITORING').setCheck('Boolean')
        .appendField('enableCameraMonitoring')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_MONITOR_FEEDBACK').setCheck('CameraMonitorFeedback')
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
    this.setTooltip('Initialize Vuforia.');
  }
};

/**
 * Deprecated. See vuforia_initializeExtendedNoKey.
 */
Blockly.JavaScript['vuforia_initializeExtended'] = function(block) {
  var vuforiaLicenseKey = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LICENSE_KEY', Blockly.JavaScript.ORDER_COMMA);
  return vuforia_initializeExtended(block, vuforiaLicenseKey);
}

function vuforia_initializeExtended(block, vuforiaLicenseKey) {
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
  return vuforiaIdentifier + '.initializeExtended(' + vuforiaLicenseKey + ', ' +
      cameraDirection + ', ' + useExtendedTracking + ', ' +
      enableCameraMonitoring + ', ' + cameraMonitorFeedback + ', ' +
      dx + ', ' + dy + ', ' + dz + ', ' + xAngle + ', ' + yAngle + ', ' + zAngle + ', ' +
      useCompetitionFieldTargetLocations + ');\n';
};

Blockly.Blocks['vuforia_initializeExtendedNoKey'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Vuforia'))
        .appendField('.')
        .appendField(createNonEditableField('initialize'));
    this.appendValueInput('CAMERA_DIRECTION').setCheck('CameraDirection')
        .appendField('cameraDirection')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('USE_EXTENDED_TRACKING').setCheck('Boolean')
        .appendField('useExtendedTracking')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ENABLE_CAMERA_MONITORING').setCheck('Boolean')
        .appendField('enableCameraMonitoring')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_MONITOR_FEEDBACK').setCheck('CameraMonitorFeedback')
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
    this.setTooltip('Initialize Vuforia.');
  }
};

Blockly.JavaScript['vuforia_initializeExtendedNoKey'] = function(block) {
  return vuforia_initializeExtended(block, '"CzechWolf"');
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
  return vuforiaIdentifier + '.activate();\n';
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
  return vuforiaIdentifier + '.deactivate();\n';
};

Blockly.Blocks['vuforia_track'] = {
  init: function() {
    this.setOutput(true, 'VuforiaTrackingResults');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Vuforia'))
        .appendField('.')
        .appendField(createNonEditableField('track'));
    this.appendValueInput('TRACKABLE_NAME').setCheck('TrackableName')
        .appendField('trackableName')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the VuforiaTrackingResults of the trackable with the given name.');
  }
};

Blockly.JavaScript['vuforia_track'] = function(block) {
  var trackableName = Blockly.JavaScript.valueToCode(
      block, 'TRACKABLE_NAME', Blockly.JavaScript.ORDER_NONE);
  var code = 'JSON.parse(' + vuforiaIdentifier + '.track(' + trackableName + '))';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforia_trackPose'] = {
  init: function() {
    this.setOutput(true, 'VuforiaTrackingResults');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Vuforia'))
        .appendField('.')
        .appendField(createNonEditableField('trackPose'));
    this.appendValueInput('TRACKABLE_NAME').setCheck('TrackableName')
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
  var code = 'JSON.parse(' + vuforiaIdentifier + '.trackPose(' + trackableName + '))';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforia_typedEnum_trackableName'] = {
  init: function() {
    var TRACKABLE_NAME_CHOICES = [
        ['RELIC', 'RELIC'],
    ];
    this.setOutput(true, 'TrackableName');
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

Blockly.Blocks['vuforiaTrackingResults_getProperty_Boolean'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['IsVisible', 'IsVisible'],
    ];
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField(createNonEditableField('VuforiaTrackingResults'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('VUFORIA_TRACKING_RESULTS').setCheck('VuforiaTrackingResults')
        .appendField('vuforiaTrackingResults')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['IsVisible', 'Returns true if the associated trackable is visible, false otherwise.'],
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
    this.appendValueInput('VUFORIA_TRACKING_RESULTS').setCheck('VuforiaTrackingResults')
        .appendField('vuforiaTrackingResults')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
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
  }
};

Blockly.JavaScript['vuforiaTrackingResults_getProperty_Number'] = function(block) {
  var property = block.getFieldValue('PROP');
  var vuforiaTrackingResults = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKING_RESULTS', Blockly.JavaScript.ORDER_MEMBER);
  var code = vuforiaTrackingResults + '.' + property;
  return [code, Blockly.JavaScript.ORDER_MEMBER];
};

Blockly.Blocks['vuforiaTrackingResults_formatAsTransform'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackingResults'))
        .appendField('.')
        .appendField(createNonEditableField('formatAsTransform'));
    this.appendValueInput('VUFORIA_TRACKING_RESULTS').setCheck('VuforiaTrackingResults')
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
    this.appendValueInput('VUFORIA_TRACKING_RESULTS').setCheck('VuforiaTrackingResults')
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
