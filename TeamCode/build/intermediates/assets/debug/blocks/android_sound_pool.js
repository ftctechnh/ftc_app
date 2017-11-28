/**
 * @fileoverview FTC robot blocks related to the Android SoundPool.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// androidSoundPoolIdentifier
// The following are defined in vars.js:
// getPropertyColor
// functionColor

Blockly.Blocks['androidSoundPool_initialize'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidSoundPool'))
        .appendField('.')
        .appendField(createNonEditableField('initialize'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Initialize the SoundPool.');
  }
};

Blockly.JavaScript['androidSoundPool_initialize'] = function(block) {
  return androidSoundPoolIdentifier + '.initialize();\n';
};

Blockly.Blocks['androidSoundPool_preloadSound'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidSoundPool'))
        .appendField('.')
        .appendField(createNonEditableField('preloadSound'));
    this.appendValueInput('SOUND_NAME').setCheck('String')
        .appendField('soundName')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip(
        'Preloads the sound with the given name. Returns true if sound is successfully preloaded.');
  }
};

Blockly.JavaScript['androidSoundPool_preloadSound'] = function(block) {
  var soundName = Blockly.JavaScript.valueToCode(
      block, 'SOUND_NAME', Blockly.JavaScript.ORDER_NONE);
  var code = androidSoundPoolIdentifier + '.preloadSound(' + soundName + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};


Blockly.Blocks['androidSoundPool_play'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidSoundPool'))
        .appendField('.')
        .appendField(createNonEditableField('play'));
    this.appendValueInput('SOUND_NAME').setCheck('String')
        .appendField('soundName')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Play the sound with the given name.');
  }
};

Blockly.JavaScript['androidSoundPool_play'] = function(block) {
  var soundName = Blockly.JavaScript.valueToCode(
      block, 'SOUND_NAME', Blockly.JavaScript.ORDER_NONE);
  return androidSoundPoolIdentifier + '.play(' + soundName + ');\n';
};

Blockly.Blocks['androidSoundPool_stop'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidSoundPool'))
        .appendField('.')
        .appendField(createNonEditableField('stop'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Stop the playback.');
  }
};

Blockly.JavaScript['androidSoundPool_stop'] = function(block) {
  return androidSoundPoolIdentifier + '.stop();\n';
};

Blockly.Blocks['androidSoundPool_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Volume', 'Volume'],
        ['Rate', 'Rate'],
        ['Loop', 'Loop'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createNonEditableField('AndroidSoundPool'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Volume', 'Sets the volume. Volume range is 0.0 to 1.0.'],
        ['Rate', 'Sets the playback rate. Rate range is 0.5 to 2.0. Normal rate is 1.0.'],
        ['Loop', 'Sets the number of repeats. Loop 0 means no repeats. Loop -1 means repeat indefinitely.'],
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

Blockly.JavaScript['androidSoundPool_setProperty_Number'] = function(block) {
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return androidSoundPoolIdentifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['androidSoundPool_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Volume', 'Volume'],
        ['Rate', 'Rate'],
        ['Loop', 'Loop'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidSoundPool'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Volume', 'Returns the current volume.'],
        ['Rate', 'Returns the playback rate.'],
        ['Loop', 'Returns the number of repeats.'],
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
    this.setColour(getPropertyColor);
  }
};

Blockly.JavaScript['androidSoundPool_getProperty_Number'] = function(block) {
  var property = block.getFieldValue('PROP');
  var code = androidSoundPoolIdentifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};
