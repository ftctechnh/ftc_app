/**
 * @fileoverview FTC robot blocks related to the REV Robotics Blinkin LED Driver.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createRevBlinkinLedDriverDropdown
// BLINKIN_PATTERN_TOOLTIPS
// BLINKIN_PATTERN_FROM_TEXT_TOOLTIP
// The following are defined in vars.js:
// createFieldScrollableDropdown
// functionColor
// getPropertyColor
// setPropertyColor


Blockly.Blocks['revBlinkinLedDriver_setProperty_BlinkinPattern'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Pattern', 'Pattern'],
    ];
    this.appendValueInput('VALUE').setCheck(['RevBlinkinLedDriver.BlinkinPattern'])
        .appendField('set')
        .appendField(createRevBlinkinLedDriverDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Pattern', 'Sets the Pattern for the RevBlinkinLEDDriver.'],
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

Blockly.JavaScript['revBlinkinLedDriver_setProperty_BlinkinPattern'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['revBlinkinLedDriver_setProperty_BlinkinPattern'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'RevBlinkinLedDriver');
  var property = block.getFieldValue('PROP');
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

// Enums

Blockly.Blocks['revBlinkinLedDriver_typedEnum_blinkinPatternByCategoryPalette'] = {
  init: function() {
    var CATEGORY_CHOICES = [
        ['RAINBOW', 'RAINBOW'],
        ['SINELON', 'SINELON'],
        ['BEATS_PER_MINUTE', 'BEATS_PER_MINUTE'],
        ['TWINKLES', 'TWINKLES'],
        ['COLOR_WAVES', 'COLOR_WAVES'],
    ];
    var PALETTE_CHOICES = [
        ['RAINBOW_PALETTE', 'RAINBOW_PALETTE'],
        ['PARTY_PALETTE', 'PARTY_PALETTE'],
        ['OCEAN_PALETTE', 'OCEAN_PALETTE'],
        ['LAVA_PALETTE', 'LAVA_PALETTE'],
        ['FOREST_PALETTE', 'FOREST_PALETTE'],
    ];
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(CATEGORY_CHOICES), 'CATEGORY')
        .appendField(new Blockly.FieldDropdown(PALETTE_CHOICES), 'PALETTE');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('CATEGORY') + '_' + thisBlock.getFieldValue('PALETTE');
      for (var i = 0; i < BLINKIN_PATTERN_TOOLTIPS.length; i++) {
        if (BLINKIN_PATTERN_TOOLTIPS[i][0] == key) {
          return BLINKIN_PATTERN_TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['revBlinkinLedDriver_typedEnum_blinkinPatternByCategoryPalette'] = function(block) {
  var code = '"' + block.getFieldValue('CATEGORY') + '_' + block.getFieldValue('PALETTE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['revBlinkinLedDriver_typedEnum_blinkinPatternByCategoryPalette'] = function(block) {
  var code = 'RevBlinkinLedDriver.BlinkinPattern.' +
      block.getFieldValue('CATEGORY') + '_' + block.getFieldValue('PALETTE');
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['revBlinkinLedDriver_typedEnum_blinkinPatternRainbowWithGlitter'] = {
  init: function() {
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('RAINBOW_WITH_GLITTER'));
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var key = 'RAINBOW_WITH_GLITTER';
      for (var i = 0; i < BLINKIN_PATTERN_TOOLTIPS.length; i++) {
        if (BLINKIN_PATTERN_TOOLTIPS[i][0] == key) {
          return BLINKIN_PATTERN_TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['revBlinkinLedDriver_typedEnum_blinkinPatternRainbowWithGlitter'] = function(block) {
  var code = '"RAINBOW_WITH_GLITTER"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['revBlinkinLedDriver_typedEnum_blinkinPatternRainbowWithGlitter'] = function(block) {
  var code = 'RevBlinkinLedDriver.BlinkinPattern.RAINBOW_WITH_GLITTER';
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['revBlinkinLedDriver_typedEnum_blinkinPatternConfetti'] = {
  init: function() {
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('CONFETTI'));
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var key = 'CONFETTI';
      for (var i = 0; i < BLINKIN_PATTERN_TOOLTIPS.length; i++) {
        if (BLINKIN_PATTERN_TOOLTIPS[i][0] == key) {
          return BLINKIN_PATTERN_TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['revBlinkinLedDriver_typedEnum_blinkinPatternConfetti'] = function(block) {
  var code = '"CONFETTI"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['revBlinkinLedDriver_typedEnum_blinkinPatternConfetti'] = function(block) {
  var code = 'RevBlinkinLedDriver.BlinkinPattern.CONFETTI';
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['revBlinkinLedDriver_typedEnum_blinkinPatternShot'] = {
  init: function() {
    var COLOR_CHOICES = [
        ['RED', 'RED'],
        ['BLUE', 'BLUE'],
        ['WHITE', 'WHITE'],
    ];
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('SHOT'))
        .appendField(new Blockly.FieldDropdown(COLOR_CHOICES), 'COLOR');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var key = 'SHOT_' + thisBlock.getFieldValue('COLOR');
      for (var i = 0; i < BLINKIN_PATTERN_TOOLTIPS.length; i++) {
        if (BLINKIN_PATTERN_TOOLTIPS[i][0] == key) {
          return BLINKIN_PATTERN_TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['revBlinkinLedDriver_typedEnum_blinkinPatternShot'] = function(block) {
  var code = '"SHOT_' + block.getFieldValue('COLOR') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['revBlinkinLedDriver_typedEnum_blinkinPatternShot'] = function(block) {
  var code = 'RevBlinkinLedDriver.BlinkinPattern.SHOT_' + block.getFieldValue('COLOR');
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['revBlinkinLedDriver_typedEnum_blinkinPatternFire'] = {
  init: function() {
    var SIZE_CHOICES = [
        ['MEDIUM', 'MEDIUM'],
        ['LARGE', 'LARGE'],
    ];
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('FIRE'))
        .appendField(new Blockly.FieldDropdown(SIZE_CHOICES), 'SIZE');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var key = 'FIRE_' + thisBlock.getFieldValue('SIZE');
      for (var i = 0; i < BLINKIN_PATTERN_TOOLTIPS.length; i++) {
        if (BLINKIN_PATTERN_TOOLTIPS[i][0] == key) {
          return BLINKIN_PATTERN_TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['revBlinkinLedDriver_typedEnum_blinkinPatternFire'] = function(block) {
  var code = '"FIRE_' + block.getFieldValue('SIZE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['revBlinkinLedDriver_typedEnum_blinkinPatternFire'] = function(block) {
  var code = 'RevBlinkinLedDriver.BlinkinPattern.FIRE_' + block.getFieldValue('SIZE');
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['revBlinkinLedDriver_typedEnum_blinkinPatternLarsonScanner'] = {
  init: function() {
    var COLOR_CHOICES = [
        ['RED', 'RED'],
        ['GRAY', 'GRAY'],
    ];
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('LARSON_SCANNER'))
        .appendField(new Blockly.FieldDropdown(COLOR_CHOICES), 'COLOR');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var key = 'LARSON_SCANNER_' + thisBlock.getFieldValue('COLOR');
      for (var i = 0; i < BLINKIN_PATTERN_TOOLTIPS.length; i++) {
        if (BLINKIN_PATTERN_TOOLTIPS[i][0] == key) {
          return BLINKIN_PATTERN_TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['revBlinkinLedDriver_typedEnum_blinkinPatternLarsonScanner'] = function(block) {
  var code = '"LARSON_SCANNER_' + block.getFieldValue('COLOR') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['revBlinkinLedDriver_typedEnum_blinkinPatternLarsonScanner'] = function(block) {
  var code = 'RevBlinkinLedDriver.BlinkinPattern.LARSON_SCANNER_' + block.getFieldValue('COLOR');
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['revBlinkinLedDriver_typedEnum_blinkinPatternByCategoryColor'] = {
  init: function() {
    var CATEGORY_CHOICES = [
        ['LIGHT_CHASE', 'LIGHT_CHASE'],
        ['BREATH', 'BREATH'],
    ];
    var COLOR_CHOICES = [
        ['RED', 'RED'],
        ['BLUE', 'BLUE'],
        ['GRAY', 'GRAY'],
    ];
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(CATEGORY_CHOICES), 'CATEGORY')
        .appendField(new Blockly.FieldDropdown(COLOR_CHOICES), 'COLOR');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('CATEGORY') + '_' + thisBlock.getFieldValue('COLOR');
      for (var i = 0; i < BLINKIN_PATTERN_TOOLTIPS.length; i++) {
        if (BLINKIN_PATTERN_TOOLTIPS[i][0] == key) {
          return BLINKIN_PATTERN_TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['revBlinkinLedDriver_typedEnum_blinkinPatternByCategoryColor'] = function(block) {
  var code = '"' + block.getFieldValue('CATEGORY') + '_' + block.getFieldValue('COLOR') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['revBlinkinLedDriver_typedEnum_blinkinPatternByCategoryColor'] = function(block) {
  var code = 'RevBlinkinLedDriver.BlinkinPattern.' +
      block.getFieldValue('CATEGORY') + '_' + block.getFieldValue('COLOR');
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['revBlinkinLedDriver_typedEnum_blinkinPatternHeartbeat'] = {
  init: function() {
    var COLOR_CHOICES = [
        ['RED', 'RED'],
        ['BLUE', 'BLUE'],
        ['WHITE', 'WHITE'],
        ['GRAY', 'GRAY'],
    ];
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('HEARTBEAT'))
        .appendField(new Blockly.FieldDropdown(COLOR_CHOICES), 'COLOR');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var key = 'HEARTBEAT_' + thisBlock.getFieldValue('COLOR');
      for (var i = 0; i < BLINKIN_PATTERN_TOOLTIPS.length; i++) {
        if (BLINKIN_PATTERN_TOOLTIPS[i][0] == key) {
          return BLINKIN_PATTERN_TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['revBlinkinLedDriver_typedEnum_blinkinPatternHeartbeat'] = function(block) {
  var code = '"HEARTBEAT_' + block.getFieldValue('COLOR') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['revBlinkinLedDriver_typedEnum_blinkinPatternHeartbeat'] = function(block) {
  var code = 'RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_' + block.getFieldValue('COLOR');
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['revBlinkinLedDriver_typedEnum_blinkinPatternStrobe'] = {
  init: function() {
    var COLOR_CHOICES = [
        ['RED', 'RED'],
        ['BLUE', 'BLUE'],
        ['GOLD', 'GOLD'],
        ['WHITE', 'WHITE'],
    ];
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('STROBE'))
        .appendField(new Blockly.FieldDropdown(COLOR_CHOICES), 'COLOR');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var key = 'STROBE_' + thisBlock.getFieldValue('COLOR');
      for (var i = 0; i < BLINKIN_PATTERN_TOOLTIPS.length; i++) {
        if (BLINKIN_PATTERN_TOOLTIPS[i][0] == key) {
          return BLINKIN_PATTERN_TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['revBlinkinLedDriver_typedEnum_blinkinPatternStrobe'] = function(block) {
  var code = '"STROBE_' + block.getFieldValue('COLOR') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['revBlinkinLedDriver_typedEnum_blinkinPatternStrobe'] = function(block) {
  var code = 'RevBlinkinLedDriver.BlinkinPattern.STROBE_' + block.getFieldValue('COLOR');
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['revBlinkinLedDriver_typedEnum_blinkinPatternByCategoryPattern'] = {
  init: function() {
    var CATEGORY_CHOICES = [
        ['CP1', 'CP1'],
        ['CP2', 'CP2'],
    ];
    var PATTERN_CHOICES = [
        ['END_TO_END_BLEND_TO_BLACK', 'END_TO_END_BLEND_TO_BLACK'],
        ['LARSON_SCANNER', 'LARSON_SCANNER'],
        ['LIGHT_CHASE', 'LIGHT_CHASE'],
        ['HEARTBEAT_SLOW', 'HEARTBEAT_SLOW'],
        ['HEARTBEAT_MEDIUM', 'HEARTBEAT_MEDIUM'],
        ['HEARTBEAT_FAST', 'HEARTBEAT_FAST'],
        ['BREATH_SLOW', 'BREATH_SLOW'],
        ['BREATH_FAST', 'BREATH_FAST'],
        ['SHOT', 'SHOT'],
        ['STROBE', 'STROBE'],
    ];
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(CATEGORY_CHOICES), 'CATEGORY')
        .appendField(new Blockly.FieldDropdown(PATTERN_CHOICES), 'PATTERN');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('CATEGORY') + '_' + thisBlock.getFieldValue('PATTERN');
      for (var i = 0; i < BLINKIN_PATTERN_TOOLTIPS.length; i++) {
        if (BLINKIN_PATTERN_TOOLTIPS[i][0] == key) {
          return BLINKIN_PATTERN_TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['revBlinkinLedDriver_typedEnum_blinkinPatternByCategoryPattern'] = function(block) {
  var code = '"' + block.getFieldValue('CATEGORY') + '_' + block.getFieldValue('PATTERN') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['revBlinkinLedDriver_typedEnum_blinkinPatternByCategoryPattern'] = function(block) {
  var code = 'RevBlinkinLedDriver.BlinkinPattern.' +
      block.getFieldValue('CATEGORY') + '_' + block.getFieldValue('PATTERN');
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['revBlinkinLedDriver_typedEnum_blinkinPatternCP1_2'] = {
  init: function() {
    var PATTERN_CHOICES = [
        ['SPARKLE_1_ON_2', 'SPARKLE_1_ON_2'],
        ['SPARKLE_2_ON_1', 'SPARKLE_2_ON_1'],
        ['COLOR_GRADIENT', 'COLOR_GRADIENT'],
        ['BEATS_PER_MINUTE', 'BEATS_PER_MINUTE'],
        ['END_TO_END_BLEND_1_TO_2', 'END_TO_END_BLEND_1_TO_2'],
        ['END_TO_END_BLEND', 'END_TO_END_BLEND'],
        ['NO_BLENDING', 'NO_BLENDING'],
        ['TWINKLES', 'TWINKLES'],
        ['COLOR_WAVES', 'COLOR_WAVES'],
        ['SINELON', 'SINELON'],
    ];
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('CP1_2'))
        .appendField(new Blockly.FieldDropdown(PATTERN_CHOICES), 'PATTERN');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var key = 'CP1_2_' + thisBlock.getFieldValue('PATTERN');
      for (var i = 0; i < BLINKIN_PATTERN_TOOLTIPS.length; i++) {
        if (BLINKIN_PATTERN_TOOLTIPS[i][0] == key) {
          return BLINKIN_PATTERN_TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['revBlinkinLedDriver_typedEnum_blinkinPatternCP1_2'] = function(block) {
  var code = '"CP1_2_' + block.getFieldValue('PATTERN') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['revBlinkinLedDriver_typedEnum_blinkinPatternCP1_2'] = function(block) {
  var code = 'RevBlinkinLedDriver.BlinkinPattern.CP1_2_' + block.getFieldValue('PATTERN');
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['revBlinkinLedDriver_typedEnum_blinkinPatternSolid'] = {
  init: function() {
    var CHOICES = [
        ['HOT_PINK', 'HOT_PINK'],
        ['DARK_RED', 'DARK_RED'],
        ['RED', 'RED'],
        ['RED_ORANGE', 'RED_ORANGE'],
        ['ORANGE', 'ORANGE'],
        ['GOLD', 'GOLD'],
        ['YELLOW', 'YELLOW'],
        ['LAWN_GREEN', 'LAWN_GREEN'],
        ['LIME', 'LIME'],
        ['DARK_GREEN', 'DARK_GREEN'],
        ['GREEN', 'GREEN'],
        ['BLUE_GREEN', 'BLUE_GREEN'],
        ['AQUA', 'AQUA'],
        ['SKY_BLUE', 'SKY_BLUE'],
        ['DARK_BLUE', 'DARK_BLUE'],
        ['BLUE', 'BLUE'],
        ['BLUE_VIOLET', 'BLUE_VIOLET'],
        ['VIOLET', 'VIOLET'],
        ['WHITE', 'WHITE'],
        ['GRAY', 'GRAY'],
        ['DARK_GRAY', 'DARK_GRAY'],
        ['BLACK', 'BLACK'],
    ];
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(CHOICES), 'BLINKIN_PATTERN');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('BLINKIN_PATTERN');
      for (var i = 0; i < BLINKIN_PATTERN_TOOLTIPS.length; i++) {
        if (BLINKIN_PATTERN_TOOLTIPS[i][0] == key) {
          return BLINKIN_PATTERN_TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['revBlinkinLedDriver_typedEnum_blinkinPatternSolid'] = function(block) {
  var code = '"' + block.getFieldValue('BLINKIN_PATTERN') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['revBlinkinLedDriver_typedEnum_blinkinPatternSolid'] = function(block) {
  var code = 'RevBlinkinLedDriver.BlinkinPattern.' + block.getFieldValue('BLINKIN_PATTERN');
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['revBlinkinLedDriver_blinkinPattern_fromNumber'] = {
  init: function() {
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('fromNumber'));
    this.appendValueInput('NUMBER').setCheck('Number')
        .appendField('number')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the pattern associated with the given number.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'NUMBER':
          return 'int';
      }
      return '';
    };
  }
};

Blockly.JavaScript['revBlinkinLedDriver_blinkinPattern_fromNumber'] = function(block) {
  var number = Blockly.JavaScript.valueToCode(
      block, 'NUMBER', Blockly.JavaScript.ORDER_NONE);
  var code = blinkinPatternIdentifierForJavaScript + '.fromNumber(' + number + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['revBlinkinLedDriver_blinkinPattern_fromNumber'] = function(block) {
  var number = Blockly.FtcJava.valueToCode(
      block, 'NUMBER', Blockly.FtcJava.ORDER_NONE);
  var code = 'RevBlinkinLedDriver.BlinkinPattern.fromNumber(' + number + ')';
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['revBlinkinLedDriver_blinkinPattern_toNumber'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('toNumber'));
    this.appendValueInput('BLINKIN_PATTERN').setCheck('RevBlinkinLedDriver.BlinkinPattern')
        .appendField('blinkinPattern')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the number associated with the given pattern.');
    this.getFtcJavaOutputType = function() {
      return 'int';
    };
  }
};

Blockly.JavaScript['revBlinkinLedDriver_blinkinPattern_toNumber'] = function(block) {
  var blinkinPattern = Blockly.JavaScript.valueToCode(
      block, 'BLINKIN_PATTERN', Blockly.JavaScript.ORDER_NONE);
  var code = blinkinPatternIdentifierForJavaScript + '.toNumber(' + blinkinPattern + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['revBlinkinLedDriver_blinkinPattern_toNumber'] = function(block) {
  var blinkinPattern = Blockly.FtcJava.valueToCode(
      block, 'BLINKIN_PATTERN', Blockly.FtcJava.ORDER_MEMBER);
  var code = blinkinPattern + '.ordinal()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['revBlinkinLedDriver_blinkinPattern_fromText'] = {
  init: function() {
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('fromText'));
    this.appendValueInput('TEXT').setCheck('String')
        .appendField('text')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip(BLINKIN_PATTERN_FROM_TEXT_TOOLTIP);
  }
};

Blockly.JavaScript['revBlinkinLedDriver_blinkinPattern_fromText'] = function(block) {
  var text = Blockly.JavaScript.valueToCode(
      block, 'TEXT', Blockly.JavaScript.ORDER_NONE);
  var code = blinkinPatternIdentifierForJavaScript + '.fromText(' + text + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['revBlinkinLedDriver_blinkinPattern_fromText'] = function(block) {
  var text = Blockly.FtcJava.valueToCode(
      block, 'TEXT', Blockly.FtcJava.ORDER_NONE);
  var code = 'RevBlinkinLedDriver.BlinkinPattern.valueOf(' + text + ')';
  Blockly.FtcJava.generateImport_('RevBlinkinLedDriver');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['revBlinkinLedDriver_blinkinPattern_toText'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('toText'));
    this.appendValueInput('BLINKIN_PATTERN').setCheck('RevBlinkinLedDriver.BlinkinPattern')
        .appendField('blinkinPattern')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the text associated with the given pattern.');
    this.getFtcJavaOutputType = function() {
      return 'int';
    };
  }
};

Blockly.JavaScript['revBlinkinLedDriver_blinkinPattern_toText'] = function(block) {
  var blinkinPattern = Blockly.JavaScript.valueToCode(
      block, 'BLINKIN_PATTERN', Blockly.JavaScript.ORDER_NONE);
  var code = blinkinPatternIdentifierForJavaScript + '.toText(' + blinkinPattern + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['revBlinkinLedDriver_blinkinPattern_toText'] = function(block) {
  var blinkinPattern = Blockly.FtcJava.valueToCode(
      block, 'BLINKIN_PATTERN', Blockly.FtcJava.ORDER_MEMBER);
  var code = blinkinPattern + '.toString()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['revBlinkinLedDriver_blinkinPattern_next'] = {
  init: function() {
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('next'));
    this.appendValueInput('BLINKIN_PATTERN').setCheck('RevBlinkinLedDriver.BlinkinPattern')
        .appendField('blinkinPattern')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the pattern after the given pattern.');
  }
};

Blockly.JavaScript['revBlinkinLedDriver_blinkinPattern_next'] = function(block) {
  var blinkinPattern = Blockly.JavaScript.valueToCode(
      block, 'BLINKIN_PATTERN', Blockly.JavaScript.ORDER_NONE);
  var code = blinkinPatternIdentifierForJavaScript + '.next(' + blinkinPattern + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['revBlinkinLedDriver_blinkinPattern_next'] = function(block) {
  var blinkinPattern = Blockly.FtcJava.valueToCode(
      block, 'BLINKIN_PATTERN', Blockly.FtcJava.ORDER_MEMBER);
  var code = blinkinPattern + '.next()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['revBlinkinLedDriver_blinkinPattern_previous'] = {
  init: function() {
    this.setOutput(true, 'RevBlinkinLedDriver.BlinkinPattern');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('BlinkinPattern'))
        .appendField('.')
        .appendField(createNonEditableField('previous'));
    this.appendValueInput('BLINKIN_PATTERN').setCheck('RevBlinkinLedDriver.BlinkinPattern')
        .appendField('blinkinPattern')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns the pattern before the given pattern.');
  }
};

Blockly.JavaScript['revBlinkinLedDriver_blinkinPattern_previous'] = function(block) {
  var blinkinPattern = Blockly.JavaScript.valueToCode(
      block, 'BLINKIN_PATTERN', Blockly.JavaScript.ORDER_NONE);
  var code = blinkinPatternIdentifierForJavaScript + '.previous(' + blinkinPattern + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['revBlinkinLedDriver_blinkinPattern_previous'] = function(block) {
  var blinkinPattern = Blockly.FtcJava.valueToCode(
      block, 'BLINKIN_PATTERN', Blockly.FtcJava.ORDER_MEMBER);
  var code = blinkinPattern + '.previous()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
