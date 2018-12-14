/**
 * @fileoverview Functions to generate code for the initialize method call for Tensor Flow Object Detection
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// The following are defined in vars.js:

function tfod_initialize_JavaScript(block, identifier, vuforiaIdentifier) {
  var minimumConfidence = Blockly.JavaScript.valueToCode(
      block, 'MINIMUM_CONFIDENCE', Blockly.JavaScript.ORDER_COMMA);
  var useObjectTracker = Blockly.JavaScript.valueToCode(
      block, 'USE_OBJECT_TRACKER', Blockly.JavaScript.ORDER_COMMA);
  var enableCameraMonitoring = Blockly.JavaScript.valueToCode(
      block, 'ENABLE_CAMERA_MONITORING', Blockly.JavaScript.ORDER_COMMA);
  return identifier + '.initialize(' + vuforiaIdentifier + ', ' +
      minimumConfidence + ', ' + useObjectTracker + ', ' + enableCameraMonitoring + ');\n';
}

function tfod_initialize_FtcJava(block, className, vuforiaClassName) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, className);
  var vuforiaIdentifier = Blockly.FtcJava.importDeclareAssign_(block, null, vuforiaClassName);
  var minimumConfidence = Blockly.FtcJava.valueToCode(
      block, 'MINIMUM_CONFIDENCE', Blockly.FtcJava.ORDER_COMMA);
  var useObjectTracker = Blockly.FtcJava.valueToCode(
      block, 'USE_OBJECT_TRACKER', Blockly.FtcJava.ORDER_COMMA);
  var enableCameraMonitoring = Blockly.FtcJava.valueToCode(
      block, 'ENABLE_CAMERA_MONITORING', Blockly.FtcJava.ORDER_COMMA);
  return identifier + '.initialize(' + vuforiaIdentifier + ', ' +
      minimumConfidence + ', ' + useObjectTracker + ', ' + enableCameraMonitoring + ');\n';
}
