/**
 * @fileoverview FTC robot blocks related to VuforiaTrackableDefaultListener
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// vuforiaTrackableDefaultListenerIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// functionColor

Blockly.Blocks['vuforiaTrackableDefaultListener_setPhoneInformation'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackableDefaultListener'))
        .appendField('.')
        .appendField(createNonEditableField('setPhoneInformation'));
    this.appendValueInput('VUFORIA_TRACKABLE_DEFAULT_LISTENER').setCheck('VuforiaTrackableDefaultListener')
        .appendField('vuforiaTrackableDefaultListener')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('PHONE_LOCATION_ON_ROBOT').setCheck('OpenGLMatrix')
        .appendField('phoneLocationOnRobot')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_DIRECTION').setCheck('VuforiaLocalizer.CameraDirection')
        .appendField('cameraDirection')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Informs the listener of the location of the phone on the robot and the ' +
        'identity of the Android camera being used. This information is needed in order to ' +
        'compute the robot location. The phoneLocationOnRobot must be an OpenGLMatrix object.');
  }
};

Blockly.JavaScript['vuforiaTrackableDefaultListener_setPhoneInformation'] = function(block) {
  var vuforiaTrackableDefaultListener = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLE_DEFAULT_LISTENER', Blockly.JavaScript.ORDER_COMMA);
  var phoneLocationOnRobot = Blockly.JavaScript.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT', Blockly.JavaScript.ORDER_COMMA);
  var cameraDirection = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_DIRECTION', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaTrackableDefaultListenerIdentifierForJavaScript + '.setPhoneInformation(' +
      vuforiaTrackableDefaultListener + ', ' + phoneLocationOnRobot + ', ' +
      cameraDirection + ');\n';
};

Blockly.FtcJava['vuforiaTrackableDefaultListener_setPhoneInformation'] = function(block) {
  var vuforiaTrackableDefaultListener = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKABLE_DEFAULT_LISTENER', Blockly.FtcJava.ORDER_MEMBER);
  var phoneLocationOnRobot = Blockly.FtcJava.valueToCode(
      block, 'PHONE_LOCATION_ON_ROBOT', Blockly.FtcJava.ORDER_COMMA);
  var cameraDirection = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_DIRECTION', Blockly.FtcJava.ORDER_COMMA);
  return vuforiaTrackableDefaultListener + '.setPhoneInformation(' +
      phoneLocationOnRobot + ', ' + cameraDirection + ');\n';
};

Blockly.Blocks['vuforiaTrackableDefaultListener_setCameraLocationOnRobot'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackableDefaultListener'))
        .appendField('.')
        .appendField(createNonEditableField('setCameraLocationOnRobot'));
    this.appendValueInput('VUFORIA_TRACKABLE_DEFAULT_LISTENER').setCheck('VuforiaTrackableDefaultListener')
        .appendField('vuforiaTrackableDefaultListener')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_NAME').setCheck(['CameraName', 'WebcamName'])
        .appendField('camera')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('CAMERA_LOCATION_ON_ROBOT').setCheck('OpenGLMatrix')
        .appendField('cameraLocationOnRobot')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Informs the listener of the location of a particular camera on the robot. ' +
        'The cameraLocationOnRobot must be an OpenGLMatrix object.');
  }
};

Blockly.JavaScript['vuforiaTrackableDefaultListener_setCameraLocationOnRobot'] = function(block) {
  var vuforiaTrackableDefaultListener = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLE_DEFAULT_LISTENER', Blockly.JavaScript.ORDER_COMMA);
  var cameraName = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_NAME', Blockly.JavaScript.ORDER_COMMA);
  var cameraLocationOnRobot = Blockly.JavaScript.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT', Blockly.JavaScript.ORDER_COMMA);
  return vuforiaTrackableDefaultListenerIdentifierForJavaScript + '.setCameraLocationOnRobot(' +
      vuforiaTrackableDefaultListener + ', ' + cameraName + ', ' + cameraLocationOnRobot + ');\n';
};

Blockly.FtcJava['vuforiaTrackableDefaultListener_setCameraLocationOnRobot'] = function(block) {
  var vuforiaTrackableDefaultListener = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKABLE_DEFAULT_LISTENER', Blockly.FtcJava.ORDER_MEMBER);
  var cameraName = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_NAME', Blockly.FtcJava.ORDER_COMMA);
  var cameraLocationOnRobot = Blockly.FtcJava.valueToCode(
      block, 'CAMERA_LOCATION_ON_ROBOT', Blockly.FtcJava.ORDER_COMMA);
  return vuforiaTrackableDefaultListener + '.setCameraLocationOnRobot(' +
      cameraName + ', ' + cameraLocationOnRobot + ');\n';
};

Blockly.Blocks['vuforiaTrackableDefaultListener_isVisible'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackableDefaultListener'))
        .appendField('.')
        .appendField(createNonEditableField('isVisible'));
    this.appendValueInput('VUFORIA_TRACKABLE_DEFAULT_LISTENER').setCheck('VuforiaTrackableDefaultListener')
        .appendField('vuforiaTrackableDefaultListener')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns true if the associated trackable is visible, false otherwise.');
  }
};

Blockly.JavaScript['vuforiaTrackableDefaultListener_isVisible'] = function(block) {
  var vuforiaTrackableDefaultListener = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLE_DEFAULT_LISTENER', Blockly.JavaScript.ORDER_NONE);
  var code = vuforiaTrackableDefaultListenerIdentifierForJavaScript + '.isVisible(' +
      vuforiaTrackableDefaultListener + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforiaTrackableDefaultListener_isVisible'] = function(block) {
  var vuforiaTrackableDefaultListener = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKABLE_DEFAULT_LISTENER', Blockly.FtcJava.ORDER_MEMBER);
  var code = vuforiaTrackableDefaultListener + '.isVisible()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforiaTrackableDefaultListener_getUpdatedRobotLocation'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackableDefaultListener'))
        .appendField('.')
        .appendField(createNonEditableField('getUpdatedRobotLocation'));
    this.appendValueInput('VUFORIA_TRACKABLE_DEFAULT_LISTENER').setCheck('VuforiaTrackableDefaultListener')
        .appendField('vuforiaTrackableDefaultListener')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a matrix representing the location of the robot, but only if a new ' +
        'location has been detected since the last call to getUpdatedRobotLocation.');
  }
};

Blockly.JavaScript['vuforiaTrackableDefaultListener_getUpdatedRobotLocation'] = function(block) {
  var vuforiaTrackableDefaultListener = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLE_DEFAULT_LISTENER', Blockly.JavaScript.ORDER_NONE);
  var code = vuforiaTrackableDefaultListenerIdentifierForJavaScript + '.getUpdatedRobotLocation(' +
      vuforiaTrackableDefaultListener + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforiaTrackableDefaultListener_getUpdatedRobotLocation'] = function(block) {
  var vuforiaTrackableDefaultListener = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKABLE_DEFAULT_LISTENER', Blockly.FtcJava.ORDER_MEMBER);
  var code = vuforiaTrackableDefaultListener + '.getUpdatedRobotLocation()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforiaTrackableDefaultListener_getPose'] = {
  init: function() {
    this.setOutput(true, 'OpenGLMatrix');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackableDefaultListener'))
        .appendField('.')
        .appendField(createNonEditableField('getPose'));
    this.appendValueInput('VUFORIA_TRACKABLE_DEFAULT_LISTENER').setCheck('VuforiaTrackableDefaultListener')
        .appendField('vuforiaTrackableDefaultListener')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a matrix representing the pose of the associated trackable, if it is ' +
        'currently visible. If it is not currently visible, null is returned. The pose is the ' +
        'location of the trackable in the phone\'s coordinate system.');
  }
};

Blockly.JavaScript['vuforiaTrackableDefaultListener_getPose'] = function(block) {
  var vuforiaTrackableDefaultListener = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLE_DEFAULT_LISTENER', Blockly.JavaScript.ORDER_NONE);
  var code = vuforiaTrackableDefaultListenerIdentifierForJavaScript + '.getPose(' +
      vuforiaTrackableDefaultListener + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforiaTrackableDefaultListener_getPose'] = function(block) {
  var vuforiaTrackableDefaultListener = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKABLE_DEFAULT_LISTENER', Blockly.FtcJava.ORDER_MEMBER);
  var code = vuforiaTrackableDefaultListener + '.getPose()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforiaTrackableDefaultListener_getRelicRecoveryVuMark'] = {
  init: function() {
    this.setOutput(true, 'RelicRecoveryVuMark');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaTrackableDefaultListener'))
        .appendField('.')
        .appendField(createNonEditableField('getRelicRecoveryVuMark'));
    this.appendValueInput('VUFORIA_TRACKABLE_DEFAULT_LISTENER').setCheck('VuforiaTrackableDefaultListener')
        .appendField('vuforiaTrackableDefaultListener')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the RelicRecoveryVuMark of the associated trackable.');
  }
};

Blockly.JavaScript['vuforiaTrackableDefaultListener_getRelicRecoveryVuMark'] = function(block) {
  var vuforiaTrackableDefaultListener = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_TRACKABLE_DEFAULT_LISTENER', Blockly.JavaScript.ORDER_NONE);
  var code = vuforiaTrackableDefaultListenerIdentifierForJavaScript + '.getRelicRecoveryVuMark(' +
      vuforiaTrackableDefaultListener + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforiaTrackableDefaultListener_getRelicRecoveryVuMark'] = function(block) {
  var vuforiaTrackableDefaultListener = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_TRACKABLE_DEFAULT_LISTENER', Blockly.FtcJava.ORDER_NONE);
  var code = 'RelicRecoveryVuMark.from(' + vuforiaTrackableDefaultListener + ')';
  Blockly.FtcJava.generateImport_('RelicRecoveryVuMark');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
