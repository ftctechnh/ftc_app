/**
 * @fileoverview FTC robot blocks related to color.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// colorIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

Blockly.Blocks['color_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Red', 'Red'],
        ['Green', 'Green'],
        ['Blue', 'Blue'],
        ['Alpha', 'Alpha'],
        ['Hue', 'Hue'],
        ['Saturation', 'Saturation'],
        ['Value', 'Value'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('Color'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('COLOR') // no type, for compatibility
        .appendField('color')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Red', 'Returns the red component of the given color'],
        ['Green', 'Returns the green component of the given color.'],
        ['Blue', 'Returns the blue component of the given color.'],
        ['Alpha', 'Returns the alpha component of the given color.'],
        ['Hue', 'Returns the hue component of the given color'],
        ['Saturation', 'Returns the saturation component of the given color.'],
        ['Value', 'Returns the value component of the given color.'],
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

Blockly.JavaScript['color_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var color = Blockly.JavaScript.valueToCode(
      block, 'COLOR', Blockly.JavaScript.ORDER_NONE);
  var code = colorIdentifierForJavaScript + '.get' + property + '(' + color + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['color_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var color = Blockly.FtcJava.valueToCode(
      block, 'COLOR', Blockly.FtcJava.ORDER_NONE);
  var code;
  var type;
  switch (property) {
    case 'Red':
    case 'Green':
    case 'Blue':
    case 'Alpha':
      code = 'Color.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property) + '(' + color + ')';
      type = 'Color';
      break;
    case 'Hue':
    case 'Saturation':
    case 'Value':
      code = 'JavaUtil.colorTo' + property + '(' + color + ')';
      type = 'JavaUtil';
      break;
    default:
      throw 'Unexpected property ' + property + ' (color_getProperty).';
  }
  Blockly.FtcJava.generateImport_(type);
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['color_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Red', 'Red'],
        ['Green', 'Green'],
        ['Blue', 'Blue'],
        ['Alpha', 'Alpha'],
        ['Hue', 'Hue'],
        ['Saturation', 'Saturation'],
        ['Value', 'Value'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('Color'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('COLOR').setCheck('Number')
        .appendField('color')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Red', 'Returns the red component of the given color'],
        ['Green', 'Returns the green component of the given color.'],
        ['Blue', 'Returns the blue component of the given color.'],
        ['Alpha', 'Returns the alpha component of the given color.'],
        ['Hue', 'Returns the hue component of the given color'],
        ['Saturation', 'Returns the saturation component of the given color.'],
        ['Value', 'Returns the value component of the given color.'],
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
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'COLOR':
          return 'int';
      }
      return '';
    };
    this.getFtcJavaOutputType = function() {
      var property = thisBlock.getFieldValue('PROP');
      switch (property) {
        case 'Red':
        case 'Green':
        case 'Blue':
        case 'Alpha':
          return 'int'
        case 'Hue':
        case 'Saturation':
        case 'Value':
          return 'float';
        default:
          throw 'Unexpected property ' + property + ' (color_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['color_getProperty_Number'] =
    Blockly.JavaScript['color_getProperty'];

Blockly.FtcJava['color_getProperty_Number'] =
    Blockly.FtcJava['color_getProperty'];

// Functions

Blockly.Blocks['color_rgbToColor'] = {
  init: function() {
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Color'))
        .appendField('.')
        .appendField(createNonEditableField('rgbToColor'));
    this.appendValueInput('RED') // no type, for compatibility
        .appendField('red')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('GREEN') // no type, for compatibility
        .appendField('green')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('BLUE') // no type, for compatibility
        .appendField('blue')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a color made from the given red, green, and blue components.');
  }
};

Blockly.JavaScript['color_rgbToColor'] = function(block) {
  var red = Blockly.JavaScript.valueToCode(
      block, 'RED', Blockly.JavaScript.ORDER_COMMA);
  var green = Blockly.JavaScript.valueToCode(
      block, 'GREEN', Blockly.JavaScript.ORDER_COMMA);
  var blue = Blockly.JavaScript.valueToCode(
      block, 'BLUE', Blockly.JavaScript.ORDER_COMMA);
  var code = colorIdentifierForJavaScript + '.rgbToColor(' + red + ', ' + green + ', ' + blue + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['color_rgbToColor'] = function(block) {
  var red = Blockly.FtcJava.valueToCode(
      block, 'RED', Blockly.FtcJava.ORDER_COMMA);
  var green = Blockly.FtcJava.valueToCode(
      block, 'GREEN', Blockly.FtcJava.ORDER_COMMA);
  var blue = Blockly.FtcJava.valueToCode(
      block, 'BLUE', Blockly.FtcJava.ORDER_COMMA);
  var code = 'Color.rgb(' + red + ', ' + green + ', ' + blue + ')';
  Blockly.FtcJava.generateImport_('Color');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['color_rgbToColor_Number'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Color'))
        .appendField('.')
        .appendField(createNonEditableField('rgbToColor'));
    this.appendValueInput('RED').setCheck('Number')
        .appendField('red')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('GREEN').setCheck('Number')
        .appendField('green')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('BLUE').setCheck('Number')
        .appendField('blue')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a color made from the given red, green, and blue components.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'RED':
        case 'GREEN':
        case 'BLUE':
          return 'int';
      }
      return '';
    };
    this.getFtcJavaOutputType = function() {
      return 'int';
    };
  }
};

Blockly.JavaScript['color_rgbToColor_Number'] =
    Blockly.JavaScript['color_rgbToColor'];

Blockly.FtcJava['color_rgbToColor_Number'] =
    Blockly.FtcJava['color_rgbToColor'];

Blockly.Blocks['color_argbToColor'] = {
  init: function() {
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Color'))
        .appendField('.')
        .appendField(createNonEditableField('argbToColor'));
    this.appendValueInput('ALPHA') // no type, for compatibility
        .appendField('alpha')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('RED') // no type, for compatibility
        .appendField('red')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('GREEN') // no type, for compatibility
        .appendField('green')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('BLUE') // no type, for compatibility
        .appendField('blue')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a color made from the given alpha, red, green, and blue components.');
  }
};

Blockly.JavaScript['color_argbToColor'] = function(block) {
  var alpha = Blockly.JavaScript.valueToCode(
      block, 'ALPHA', Blockly.JavaScript.ORDER_COMMA);
  var red = Blockly.JavaScript.valueToCode(
      block, 'RED', Blockly.JavaScript.ORDER_COMMA);
  var green = Blockly.JavaScript.valueToCode(
      block, 'GREEN', Blockly.JavaScript.ORDER_COMMA);
  var blue = Blockly.JavaScript.valueToCode(
      block, 'BLUE', Blockly.JavaScript.ORDER_COMMA);
  var code = colorIdentifierForJavaScript + '.argbToColor(' + alpha + ', ' + red + ', ' + green + ', ' + blue + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['color_argbToColor'] = function(block) {
  var alpha = Blockly.FtcJava.valueToCode(
      block, 'ALPHA', Blockly.FtcJava.ORDER_COMMA);
  var red = Blockly.FtcJava.valueToCode(
      block, 'RED', Blockly.FtcJava.ORDER_COMMA);
  var green = Blockly.FtcJava.valueToCode(
      block, 'GREEN', Blockly.FtcJava.ORDER_COMMA);
  var blue = Blockly.FtcJava.valueToCode(
      block, 'BLUE', Blockly.FtcJava.ORDER_COMMA);
  var code = 'Color.argb(' + alpha + ', ' + red + ', ' + green + ', ' + blue + ')';
  Blockly.FtcJava.generateImport_('Color');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['color_argbToColor_Number'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Color'))
        .appendField('.')
        .appendField(createNonEditableField('argbToColor'));
    this.appendValueInput('ALPHA').setCheck('Number')
        .appendField('alpha')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('RED').setCheck('Number')
        .appendField('red')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('GREEN').setCheck('Number')
        .appendField('green')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('BLUE').setCheck('Number')
        .appendField('blue')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a color made from the given alpha, red, green, and blue components.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'ALPHA':
        case 'RED':
        case 'GREEN':
        case 'BLUE':
          return 'int';
      }
      return '';
    };
    this.getFtcJavaOutputType = function() {
      return 'int';
    };
  }
};

Blockly.JavaScript['color_argbToColor_Number'] =
    Blockly.JavaScript['color_argbToColor'];

Blockly.FtcJava['color_argbToColor_Number'] =
    Blockly.FtcJava['color_argbToColor'];

Blockly.Blocks['color_hsvToColor'] = {
  init: function() {
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Color'))
        .appendField('.')
        .appendField(createNonEditableField('hsvToColor'));
    this.appendValueInput('HUE') // no type, for compatibility
        .appendField('hue')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SATURATION') // no type, for compatibility
        .appendField('saturation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('value')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a color made from the given hue, saturation, and value components.');
  }
};

Blockly.JavaScript['color_hsvToColor'] = function(block) {
  var hue = Blockly.JavaScript.valueToCode(
      block, 'HUE', Blockly.JavaScript.ORDER_COMMA);
  var saturation = Blockly.JavaScript.valueToCode(
      block, 'SATURATION', Blockly.JavaScript.ORDER_COMMA);
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_COMMA);
  var code = colorIdentifierForJavaScript + '.hsvToColor(' + hue + ', ' + saturation + ', ' + value + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['color_hsvToColor'] = function(block) {
  var hue = Blockly.FtcJava.valueToCode(
      block, 'HUE', Blockly.FtcJava.ORDER_COMMA);
  var saturation = Blockly.FtcJava.valueToCode(
      block, 'SATURATION', Blockly.FtcJava.ORDER_COMMA);
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_COMMA);
  var code = 'JavaUtil.hsvToColor(' + hue + ', ' + saturation + ', ' + value + ')';
  Blockly.FtcJava.generateImport_('JavaUtil');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['color_hsvToColor_Number'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Color'))
        .appendField('.')
        .appendField(createNonEditableField('hsvToColor'));
    this.appendValueInput('HUE').setCheck('Number')
        .appendField('hue')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SATURATION').setCheck('Number')
        .appendField('saturation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('value')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a color made from the given hue, saturation, and value components.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'HUE':
        case 'SATURATION':
        case 'VALUE':
          return 'float';
      }
      return '';
    };
    this.getFtcJavaOutputType = function() {
      return 'int';
    };
  }
};

Blockly.JavaScript['color_hsvToColor_Number'] =
    Blockly.JavaScript['color_hsvToColor'];

Blockly.FtcJava['color_hsvToColor_Number'] =
    Blockly.FtcJava['color_hsvToColor'];

Blockly.Blocks['color_ahsvToColor'] = {
  init: function() {
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Color'))
        .appendField('.')
        .appendField(createNonEditableField('ahsvToColor'));
    this.appendValueInput('ALPHA') // no type, for compatibility
        .appendField('alpha')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('HUE') // no type, for compatibility
        .appendField('hue')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SATURATION') // no type, for compatibility
        .appendField('saturation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('value')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a color made from the given alpha, hue, saturation, and value components.');
  }
};

Blockly.JavaScript['color_ahsvToColor'] = function(block) {
  var alpha = Blockly.JavaScript.valueToCode(
      block, 'ALPHA', Blockly.JavaScript.ORDER_COMMA);
  var hue = Blockly.JavaScript.valueToCode(
      block, 'HUE', Blockly.JavaScript.ORDER_COMMA);
  var saturation = Blockly.JavaScript.valueToCode(
      block, 'SATURATION', Blockly.JavaScript.ORDER_COMMA);
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_COMMA);
  var code = colorIdentifierForJavaScript + '.ahsvToColor(' + alpha + ', ' + hue + ', ' + saturation + ', ' + value + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['color_ahsvToColor'] = function(block) {
  var alpha = Blockly.FtcJava.valueToCode(
      block, 'ALPHA', Blockly.FtcJava.ORDER_COMMA);
  var hue = Blockly.FtcJava.valueToCode(
      block, 'HUE', Blockly.FtcJava.ORDER_COMMA);
  var saturation = Blockly.FtcJava.valueToCode(
      block, 'SATURATION', Blockly.FtcJava.ORDER_COMMA);
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_COMMA);
  var code = 'JavaUtil.ahsvToColor(' + alpha + ', ' + hue + ', ' + saturation + ', ' + value + ')';
  Blockly.FtcJava.generateImport_('JavaUtil');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['color_ahsvToColor_Number'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Color'))
        .appendField('.')
        .appendField(createNonEditableField('ahsvToColor'));
    this.appendValueInput('ALPHA').setCheck('Number')
        .appendField('alpha')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('HUE').setCheck('Number')
        .appendField('hue')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('SATURATION').setCheck('Number')
        .appendField('saturation')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('value')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a color made from the given alpha, hue, saturation, and value components.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'ALPHA':
        case 'HUE':
        case 'SATURATION':
        case 'VALUE':
          return 'float';
      }
      return '';
    };
    this.getFtcJavaOutputType = function() {
      return 'int';
    };
  }
};

Blockly.JavaScript['color_ahsvToColor_Number'] =
    Blockly.JavaScript['color_ahsvToColor'];

Blockly.FtcJava['color_ahsvToColor_Number'] =
    Blockly.FtcJava['color_ahsvToColor'];

Blockly.Blocks['color_textToColor'] = {
  init: function() {
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Color'))
        .appendField('.')
        .appendField(createNonEditableField('textToColor'));
    this.appendValueInput('TEXT') // no type, for compatibility
        .appendField('text')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a color made from the given text.');
  }
};

Blockly.JavaScript['color_textToColor'] = function(block) {
  var text = Blockly.JavaScript.valueToCode(
      block, 'TEXT', Blockly.JavaScript.ORDER_NONE);
  var code = colorIdentifierForJavaScript + '.textToColor(' + text + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['color_textToColor'] = function(block) {
  var text = Blockly.FtcJava.valueToCode(
      block, 'TEXT', Blockly.FtcJava.ORDER_NONE);
  var code = 'Color.parseColor(' + text + ')';
  Blockly.FtcJava.generateImport_('Color');
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['color_textToColor_Number'] = {
  init: function() {
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('Color'))
        .appendField('.')
        .appendField(createNonEditableField('textToColor'));
    this.appendValueInput('TEXT').setCheck('String')
        .appendField('text')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a color made from the given text.');
    this.getFtcJavaOutputType = function() {
      return 'int';
    };
  }
};

Blockly.JavaScript['color_textToColor_Number'] =
    Blockly.JavaScript['color_textToColor'];

Blockly.FtcJava['color_textToColor_Number'] =
    Blockly.FtcJava['color_textToColor'];


Blockly.Blocks['normalizedColors_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Red', 'Red'],
        ['Green', 'Green'],
        ['Blue', 'Blue'],
        ['Alpha', 'Alpha'],
        ['Color', 'Color'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('NormalizedColors'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('NORMALIZED_COLORS').setCheck('NormalizedRGBA')
        .appendField('normalizedColors')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Red', 'Returns the Red value of the given NormalizedRGBA object.'],
        ['Green', 'Returns the Green value of the given NormalizedRGBA object.'],
        ['Blue', 'Returns the Blue value of the given NormalizedRGBA object.'],
        ['Alpha', 'Returns the Alpha value of the given NormalizedRGBA object.'],
        ['Color', 'Returns the Android color integer.'],
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
        case 'Red':
        case 'Green':
        case 'Blue':
        case 'Alpha':
          return 'float';
        case 'Color':
          return 'int';
        default:
          throw 'Unexpected property ' + property + ' (normalizedColors_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['normalizedColors_getProperty_Number'] = function(block) {
  var property = block.getFieldValue('PROP');
  var normalizedColors = Blockly.JavaScript.valueToCode(
      block, 'NORMALIZED_COLORS', Blockly.JavaScript.ORDER_MEMBER);
  var code = normalizedColors + '.' + property;
  return [code, Blockly.JavaScript.ORDER_MEMBER];
};

Blockly.FtcJava['normalizedColors_getProperty_Number'] = function(block) {
  var property = block.getFieldValue('PROP');
  var normalizedColors = Blockly.FtcJava.valueToCode(
      block, 'NORMALIZED_COLORS', Blockly.FtcJava.ORDER_MEMBER);
  var code;
  switch (property) {
    case 'Red':
    case 'Green':
    case 'Blue':
    case 'Alpha':
      code = Blockly.FtcJava.makeFirstLetterLowerCase_(property);
      break;
    case 'Color':
      code = 'toColor()';
      break;
    default:
      throw 'Unexpected property ' + property + ' (normalizedColors_getProperty_Number).';
  }
  code = normalizedColors + '.' + code;
  if (code.endsWith(')')) { // toColor() is a method.
    return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
  }
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};
