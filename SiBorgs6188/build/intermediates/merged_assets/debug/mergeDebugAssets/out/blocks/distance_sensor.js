/**
 * @fileoverview Robot blocks related to distance sensors.
 * @author austinshalit@gmail.com (Austin Shalit)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createDistanceSensorDropdown
// The following are defined in vars.js:
// functionColor

Blockly.Blocks['distanceSensor_getDistance'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createDistanceSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(createNonEditableField('getDistance'));
    this.appendValueInput('DISTANCE_UNIT').setCheck('DistanceUnit')
        .appendField('distanceUnit')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Get the distance reported by the sensor in the provided unit.');
  }
};

Blockly.JavaScript['distanceSensor_getDistance'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var distanceUnit = Blockly.JavaScript.valueToCode(
        block, 'DISTANCE_UNIT', Blockly.JavaScript.ORDER_COMMA);
  var code = identifier + '.getDistance(' + distanceUnit + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
