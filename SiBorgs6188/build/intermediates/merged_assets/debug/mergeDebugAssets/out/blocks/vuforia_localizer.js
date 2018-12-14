/**
 * @fileoverview FTC robot blocks related to VuforiaLocalizer
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// vuforiaLocalizerIdentifierForJavaScript
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
    this.setTooltip('Creates a new VuforiaLocalizer object with the given VuforiaLocalizer.Parameters.');
  }
};

Blockly.JavaScript['vuforiaLocalizer_create'] = function(block) {
  var vuforiaLocalizerParameters = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER_PARAMETERS', Blockly.JavaScript.ORDER_NONE);
  var code = vuforiaLocalizerIdentifierForJavaScript + '.create(' + vuforiaLocalizerParameters + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforiaLocalizer_create'] = function(block) {
  var vuforiaLocalizerParameters = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_LOCALIZER_PARAMETERS', Blockly.FtcJava.ORDER_NONE);
  var code = 'ClassFactory.createVuforiaLocalizer(' + vuforiaLocalizerParameters + ')';
  Blockly.FtcJava.generateImport_('ClassFactory');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
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
    this.setTooltip('Loads a Vuforia dataset from the .XML asset with the given name. The ' +
        'corresponding .DAT asset must also be present. Note that this operation can take a few ' +
        'seconds to execute. Returns a VuforiaTrackables object.');
  }
};

Blockly.JavaScript['vuforiaLocalizer_loadTrackablesFromAsset'] = function(block) {
  var vuforiaLocalizer = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER', Blockly.JavaScript.ORDER_COMMA);
  var assetName = Blockly.JavaScript.valueToCode(
      block, 'ASSET_NAME', Blockly.JavaScript.ORDER_COMMA);
  var code = vuforiaLocalizerIdentifierForJavaScript + '.loadTrackablesFromAsset(' + vuforiaLocalizer + ', ' + assetName + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforiaLocalizer_loadTrackablesFromAsset'] = function(block) {
  var vuforiaLocalizer = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_LOCALIZER', Blockly.FtcJava.ORDER_MEMBER);
  var assetName = Blockly.FtcJava.valueToCode(
      block, 'ASSET_NAME', Blockly.FtcJava.ORDER_NONE);
  var code = vuforiaLocalizer + '.loadTrackablesFromAsset(' + assetName + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
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
    this.setTooltip('Loads a Vuforia dataset from the .XML file with the given absolute file ' +
        'name. The corresponding .DAT file must also be present. Note that this operation can ' +
        'take a few seconds to execute. Returns a VuforiaTrackables object.');
  }
};

Blockly.JavaScript['vuforiaLocalizer_loadTrackablesFromFile'] = function(block) {
  var vuforiaLocalizer = Blockly.JavaScript.valueToCode(
      block, 'VUFORIA_LOCALIZER', Blockly.JavaScript.ORDER_COMMA);
  var absoluteFileName = Blockly.JavaScript.valueToCode(
      block, 'ABSOLUTE_FILE_NAME', Blockly.JavaScript.ORDER_COMMA);
  var code = vuforiaLocalizerIdentifierForJavaScript + '.loadTrackablesFromFile(' + vuforiaLocalizer + ', ' + absoluteFileName + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['vuforiaLocalizer_loadTrackablesFromFile'] = function(block) {
  var vuforiaLocalizer = Blockly.FtcJava.valueToCode(
      block, 'VUFORIA_LOCALIZER', Blockly.FtcJava.ORDER_MEMBER);
  var absoluteFileName = Blockly.FtcJava.valueToCode(
      block, 'ABSOLUTE_FILE_NAME', Blockly.FtcJava.ORDER_NONE);
  var code = vuforiaLocalizer + '.loadTrackablesFromFile(' + absoluteFileName + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
