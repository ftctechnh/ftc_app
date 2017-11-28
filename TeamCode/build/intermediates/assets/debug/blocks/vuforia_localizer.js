/**
 * @fileoverview FTC robot blocks related to VuforiaLocalizer
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// vuforiaLocalizerIdentifier
// The following are defined in vars.js:
// createNonEditableField
// functionColor

Blockly.Blocks['vuforiaLocalizer_create'] = {
  init: function() {
    this.setOutput(true, 'VuforiaLocalizer');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('VuforiaLocalizer'));
    this.appendValueInput('VUFORIA_LOCALIZER_PARAMETERS').setCheck('VuforiaLocalizer.Parameters')
        .appendField('vuforiaLocalizerParameters')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip(
        'Create a new VuforiaLocalizer object with the given VuforiaLocalizer.Parameters.');
  }
};

Blockly.JavaScript['vuforiaLocalizer_create'] = function(block) {
  var vuforiaLocalizerParameters = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER_PARAMETERS', Blockly.JavaScript.ORDER_NONE);
  var code = vuforiaLocalizerIdentifier + '.create(' + vuforiaLocalizerParameters + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforiaLocalizer_loadTrackablesFromAsset'] = {
  init: function() {
    this.setOutput(true, 'VuforiaTrackables');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaLocalizer'))
        .appendField('.')
        .appendField(createNonEditableField('loadTrackablesFromAsset'));
    this.appendValueInput('VUFORIA_LOCALIZER').setCheck('VuforiaLocalizer')
        .appendField('vuforiaLocalizer')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ASSET_NAME').setCheck('String')
        .appendField('assetName')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Loads a Vuforia dataset from the indicated application asset, which must be ' +
        'of type .XML. The corresponding .DAT asset must be a sibling. Note that this operation ' +
        'can be extremely lengthy, possibly taking a few seconds to execute. Loading datasets ' +
        'from an asset you stored in your application APK is the recommended approach to ' +
        'packaging datasets so they always travel along with your code. Returns a ' +
        'VuforiaTrackables object.');
  }
};

Blockly.JavaScript['vuforiaLocalizer_loadTrackablesFromAsset'] = function(block) {
  var vuforiaLocalizer = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER', Blockly.JavaScript.ORDER_COMMA);
  var assetName = Blockly.JavaScript.valueToCode(
      block, 'ASSET_NAME', Blockly.JavaScript.ORDER_COMMA);
  var code = vuforiaLocalizerIdentifier + '.loadTrackablesFromAsset(' + vuforiaLocalizer + ', ' + assetName + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['vuforiaLocalizer_loadTrackablesFromFile'] = {
  init: function() {
    this.setOutput(true, 'VuforiaTrackables');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('VuforiaLocalizer'))
        .appendField('.')
        .appendField(createNonEditableField('loadTrackablesFromFile'));
    this.appendValueInput('VUFORIA_LOCALIZER').setCheck('VuforiaLocalizer')
        .appendField('vuforiaLocalizer')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ABSOLUTE_FILE_NAME').setCheck('String')
        .appendField('absoluteFileName')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Loads a Vuforia dataset from the indicated file, which must be a .XML file ' +
        'and contain the full file path. The corresponding .DAT file must be a sibling file in ' +
        'the same directory. Note that this operation can be extremely lengthy, possibly taking ' +
        'a few seconds to execute. Returns a VuforiaTrackables object.');
  }
};

Blockly.JavaScript['vuforiaLocalizer_loadTrackablesFromFile'] = function(block) {
  var vuforiaLocalizer = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER', Blockly.JavaScript.ORDER_COMMA);
  var absoluteFileName = Blockly.JavaScript.valueToCode(
      block, 'ABSOLUTE_FILE_NAME', Blockly.JavaScript.ORDER_COMMA);
  var code = vuforiaLocalizerIdentifier + '.loadTrackablesFromFile(' + vuforiaLocalizer + ', ' + absoluteFileName + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
