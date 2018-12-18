/**
 * @fileoverview FTC robot blocks related to the Android TextToSpeech.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// androidTextToSpeechIdentifierForJavaScript
// The following are defined in vars.js:
// getPropertyColor
// functionColor

Blockly.Blocks['androidTextToSpeech_initialize'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(createNonEditableField('initialize'));
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Initialize the TextToSpeech engine.');
  }
};

Blockly.JavaScript['androidTextToSpeech_initialize'] = function(block) {
  return androidTextToSpeechIdentifierForJavaScript + '.initialize();\n';
};

Blockly.FtcJava['androidTextToSpeech_initialize'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidTextToSpeech');
  return identifier + '.initialize();\n';
};

/*
 * Deprecated. See androidTextToSpeech_getProperty_String and
 * androidTextToSpeech_getProperty_Boolean
 */
Blockly.Blocks['androidTextToSpeech_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Status', 'Status'],
        ['LanguageCode', 'LanguageCode'],
        ['CountryCode', 'CountryCode'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Status', 'Returns a text value representing the TextToSpeech initialization status.'],
        ['LanguageCode', 'Returns a text value representing the current language code.'],
        ['CountryCode', 'Returns a text value representing the current country code.'],
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

Blockly.JavaScript['androidTextToSpeech_getProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var code = androidTextToSpeechIdentifierForJavaScript + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['androidTextToSpeech_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidTextToSpeech');
  var property = block.getFieldValue('PROP');
  var code = identifier;
  if (property == 'IsSpeaking') {
    code += '.isSpeaking()';
  } else {
    code += '.get' + property + '()';
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['androidTextToSpeech_getProperty_String'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Status', 'Status'],
        ['LanguageCode', 'LanguageCode'],
        ['CountryCode', 'CountryCode'],
    ];
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Status', 'Returns a text value representing the TextToSpeech initialization status.'],
        ['LanguageCode', 'Returns a text value representing the current language code.'],
        ['CountryCode', 'Returns a text value representing the current country code.'],
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

Blockly.JavaScript['androidTextToSpeech_getProperty_String'] =
    Blockly.JavaScript['androidTextToSpeech_getProperty'];

Blockly.FtcJava['androidTextToSpeech_getProperty_String'] =
    Blockly.FtcJava['androidTextToSpeech_getProperty'];

Blockly.Blocks['androidTextToSpeech_getProperty_Boolean'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['IsSpeaking', 'IsSpeaking'],
    ];
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['IsSpeaking', 'Returns true if the TextToSpeech engine is busy speaking.'],
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

Blockly.JavaScript['androidTextToSpeech_getProperty_Boolean'] =
    Blockly.JavaScript['androidTextToSpeech_getProperty'];

Blockly.FtcJava['androidTextToSpeech_getProperty_Boolean'] =
    Blockly.FtcJava['androidTextToSpeech_getProperty'];

Blockly.Blocks['androidTextToSpeech_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Pitch', 'Pitch'],
        ['SpeechRate', 'SpeechRate'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Pitch', 'Sets the speech pitch. 1.0 is the normal pitch. Lower values will lower the ' +
            'tone of the synthesized voice. Greater values will increase the tone of the ' +
            'synthesized voice.'],
        ['SpeechRate', 'Sets the speech rate. 1.0 is the normal speech rate. Lower values will ' +
            'slow down the speech (0.5 is half the normal speech rate). Greater values will ' +
            'accelerate the speech (2.0 is twice the normal speech rate).'],
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

Blockly.JavaScript['androidTextToSpeech_setProperty'] = function(block) {
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return androidTextToSpeechIdentifierForJavaScript + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['androidTextToSpeech_setProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidTextToSpeech');
  var property = block.getFieldValue('PROP');
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.Blocks['androidTextToSpeech_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Pitch', 'Pitch'],
        ['SpeechRate', 'SpeechRate'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Pitch', 'Sets the speech pitch. 1.0 is the normal pitch. Lower values will lower the ' +
            'tone of the synthesized voice. Greater values will increase the tone of the ' +
            'synthesized voice.'],
        ['SpeechRate', 'Sets the speech rate. 1.0 is the normal speech rate. Lower values will ' +
            'slow down the speech (0.5 is half the normal speech rate). Greater values will ' +
            'accelerate the speech (2.0 is twice the normal speech rate).'],
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
      if (inputName == 'VALUE') {
        var property = thisBlock.getFieldValue('PROP');
        switch (property) {
          case 'Pitch':
          case 'SpeechRate':
            return 'float';
          default:
            throw 'Unexpected property ' + property + ' (androidTextToSpeech_setProperty_Number getArgumentType).';
        }
      }
      return '';
    };
  }
};

Blockly.JavaScript['androidTextToSpeech_setProperty_Number'] =
    Blockly.JavaScript['androidTextToSpeech_setProperty'];

Blockly.FtcJava['androidTextToSpeech_setProperty_Number'] =
    Blockly.FtcJava['androidTextToSpeech_setProperty'];

Blockly.Blocks['androidTextToSpeech_isLanguageAvailable'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(createNonEditableField('isLanguageAvailable'));
    this.appendValueInput('LANGUAGE_CODE') // no type, for compatibility
        .appendField('languageCode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns true if the given language is supported. ' +
        'The languageCode must be an ISO 639 alpha-2 or alpha-3 language code, or a language ' +
        'subtag up to 8 characters in length.');
  }
};

Blockly.JavaScript['androidTextToSpeech_isLanguageAvailable'] = function(block) {
  var languageCode = Blockly.JavaScript.valueToCode(
      block, 'LANGUAGE_CODE', Blockly.JavaScript.ORDER_NONE);
  var code = androidTextToSpeechIdentifierForJavaScript + '.isLanguageAvailable(' + languageCode + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['androidTextToSpeech_isLanguageAvailable'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidTextToSpeech');
  var languageCode = Blockly.FtcJava.valueToCode(
      block, 'LANGUAGE_CODE', Blockly.FtcJava.ORDER_NONE);
  var code = identifier + '.isLanguageAvailable(' + languageCode + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['androidTextToSpeech_isLanguageAvailable_String'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(createNonEditableField('isLanguageAvailable'));
    this.appendValueInput('LANGUAGE_CODE').setCheck('String')
        .appendField('languageCode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns true if the given language is supported. ' +
        'The languageCode must be an ISO 639 alpha-2 or alpha-3 language code, or a language ' +
        'subtag up to 8 characters in length.');
  }
};

Blockly.JavaScript['androidTextToSpeech_isLanguageAvailable_String'] =
    Blockly.JavaScript['androidTextToSpeech_isLanguageAvailable'];

Blockly.FtcJava['androidTextToSpeech_isLanguageAvailable_String'] =
    Blockly.FtcJava['androidTextToSpeech_isLanguageAvailable'];

Blockly.Blocks['androidTextToSpeech_isLanguageAndCountryAvailable'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(createNonEditableField('isLanguageAvailable'));
    this.appendValueInput('LANGUAGE_CODE') // no type, for compatibility
        .appendField('languageCode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('COUNTRY_CODE') // no type, for compatibility
        .appendField('countryCode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns true if the given language is supported. ' +
        'The languageCode must be an ISO 639 alpha-2 or alpha-3 language code, or a language ' +
        'subtag up to 8 characters in length. ' +
        'The countryCode must be an ISO 3166 alpha-2 country code or a UN M.49 numeric-3 area ' +
        'code.');
  }
};

Blockly.JavaScript['androidTextToSpeech_isLanguageAndCountryAvailable'] = function(block) {
  var languageCode = Blockly.JavaScript.valueToCode(
      block, 'LANGUAGE_CODE', Blockly.JavaScript.ORDER_COMMA);
  var countryCode = Blockly.JavaScript.valueToCode(
      block, 'COUNTRY_CODE', Blockly.JavaScript.ORDER_COMMA);
  var code = androidTextToSpeechIdentifierForJavaScript + '.isLanguageAndCountryAvailable(' +
      languageCode + ', ' + countryCode + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['androidTextToSpeech_isLanguageAndCountryAvailable'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidTextToSpeech');
  var languageCode = Blockly.FtcJava.valueToCode(
      block, 'LANGUAGE_CODE', Blockly.FtcJava.ORDER_COMMA);
  var countryCode = Blockly.FtcJava.valueToCode(
      block, 'COUNTRY_CODE', Blockly.FtcJava.ORDER_COMMA);
  var code = identifier + '.isLanguageAndCountryAvailable(' +
      languageCode + ', ' + countryCode + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['androidTextToSpeech_isLanguageAndCountryAvailable_String'] = {
  init: function() {
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(createNonEditableField('isLanguageAvailable'));
    this.appendValueInput('LANGUAGE_CODE').setCheck('String')
        .appendField('languageCode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('COUNTRY_CODE').setCheck('String')
        .appendField('countryCode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns true if the given language is supported. ' +
        'The languageCode must be an ISO 639 alpha-2 or alpha-3 language code, or a language ' +
        'subtag up to 8 characters in length. ' +
        'The countryCode must be an ISO 3166 alpha-2 country code or a UN M.49 numeric-3 area ' +
        'code.');
  }
};

Blockly.JavaScript['androidTextToSpeech_isLanguageAndCountryAvailable_String'] =
    Blockly.JavaScript['androidTextToSpeech_isLanguageAndCountryAvailable'];

Blockly.FtcJava['androidTextToSpeech_isLanguageAndCountryAvailable_String'] =
    Blockly.FtcJava['androidTextToSpeech_isLanguageAndCountryAvailable'];

Blockly.Blocks['androidTextToSpeech_setLanguage'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(createNonEditableField('setLanguage'));
    this.appendValueInput('LANGUAGE_CODE') // no type, for compatibility
        .appendField('languageCode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the language. ' +
        'The languageCode must be an ISO 639 alpha-2 or alpha-3 language code, or a language ' +
        'subtag up to 8 characters in length.');
  }
};

Blockly.JavaScript['androidTextToSpeech_setLanguage'] = function(block) {
  var languageCode = Blockly.JavaScript.valueToCode(
      block, 'LANGUAGE_CODE', Blockly.JavaScript.ORDER_NONE);
  return androidTextToSpeechIdentifierForJavaScript + '.setLanguage(' + languageCode + ');\n';
};

Blockly.FtcJava['androidTextToSpeech_setLanguage'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidTextToSpeech');
  var languageCode = Blockly.FtcJava.valueToCode(
      block, 'LANGUAGE_CODE', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.setLanguage(' + languageCode + ');\n';
};

Blockly.Blocks['androidTextToSpeech_setLanguage_String'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(createNonEditableField('setLanguage'));
    this.appendValueInput('LANGUAGE_CODE').setCheck('String')
        .appendField('languageCode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the language. ' +
        'The languageCode must be an ISO 639 alpha-2 or alpha-3 language code, or a language ' +
        'subtag up to 8 characters in length.');
  }
};

Blockly.JavaScript['androidTextToSpeech_setLanguage_String'] =
    Blockly.JavaScript['androidTextToSpeech_setLanguage'];

Blockly.FtcJava['androidTextToSpeech_setLanguage_String'] =
    Blockly.FtcJava['androidTextToSpeech_setLanguage'];

Blockly.Blocks['androidTextToSpeech_setLanguageAndCountry'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(createNonEditableField('setLanguage'));
    this.appendValueInput('LANGUAGE_CODE') // no type, for compatibility
        .appendField('languageCode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('COUNTRY_CODE') // no type, for compatibility
        .appendField('countryCode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the language and country. ' +
        'The languageCode must be an ISO 639 alpha-2 or alpha-3 language code, or a language ' +
        'subtag up to 8 characters in length. ' +
        'The countryCode must be an ISO 3166 alpha-2 country code or a UN M.49 numeric-3 area ' +
        'code.');
  }
};

Blockly.JavaScript['androidTextToSpeech_setLanguageAndCountry'] = function(block) {
  var languageCode = Blockly.JavaScript.valueToCode(
      block, 'LANGUAGE_CODE', Blockly.JavaScript.ORDER_COMMA);
  var countryCode = Blockly.JavaScript.valueToCode(
      block, 'COUNTRY_CODE', Blockly.JavaScript.ORDER_COMMA);
  return androidTextToSpeechIdentifierForJavaScript + '.setLanguageAndCountry(' +
      languageCode + ', ' + countryCode + ');\n';
};

Blockly.FtcJava['androidTextToSpeech_setLanguageAndCountry'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidTextToSpeech');
  var languageCode = Blockly.FtcJava.valueToCode(
      block, 'LANGUAGE_CODE', Blockly.FtcJava.ORDER_COMMA);
  var countryCode = Blockly.FtcJava.valueToCode(
      block, 'COUNTRY_CODE', Blockly.FtcJava.ORDER_COMMA);
  return identifier + '.setLanguageAndCountry(' +
      languageCode + ', ' + countryCode + ');\n';
};

Blockly.Blocks['androidTextToSpeech_setLanguageAndCountry_String'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(createNonEditableField('setLanguage'));
    this.appendValueInput('LANGUAGE_CODE').setCheck('String')
        .appendField('languageCode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('COUNTRY_CODE').setCheck('String')
        .appendField('countryCode')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Sets the language and country. ' +
        'The languageCode must be an ISO 639 alpha-2 or alpha-3 language code, or a language ' +
        'subtag up to 8 characters in length. ' +
        'The countryCode must be an ISO 3166 alpha-2 country code or a UN M.49 numeric-3 area ' +
        'code.');
  }
};

Blockly.JavaScript['androidTextToSpeech_setLanguageAndCountry_String'] =
    Blockly.JavaScript['androidTextToSpeech_setLanguageAndCountry'];

Blockly.FtcJava['androidTextToSpeech_setLanguageAndCountry_String'] =
    Blockly.FtcJava['androidTextToSpeech_setLanguageAndCountry'];

Blockly.Blocks['androidTextToSpeech_speak'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(createNonEditableField('speak'));
    this.appendValueInput('TEXT') // no type, for compatibility
        .appendField('text')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Speaks the given text.');
  }
};

Blockly.JavaScript['androidTextToSpeech_speak'] = function(block) {
  var text = Blockly.JavaScript.valueToCode(
      block, 'TEXT', Blockly.JavaScript.ORDER_NONE);
  return androidTextToSpeechIdentifierForJavaScript + '.speak(' + text + ');\n';
};

Blockly.FtcJava['androidTextToSpeech_speak'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, null, 'AndroidTextToSpeech');
  var text = Blockly.FtcJava.valueToCode(
      block, 'TEXT', Blockly.FtcJava.ORDER_NONE);
  return identifier + '.speak(' + text + ');\n';
};

Blockly.Blocks['androidTextToSpeech_speak_String'] = {
  init: function() {
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('AndroidTextToSpeech'))
        .appendField('.')
        .appendField(createNonEditableField('speak'));
    this.appendValueInput('TEXT').setCheck('String')
        .appendField('text')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(functionColor);
    this.setTooltip('Speaks the given text.');
  }
};

Blockly.JavaScript['androidTextToSpeech_speak_String'] =
    Blockly.JavaScript['androidTextToSpeech_speak'];

Blockly.FtcJava['androidTextToSpeech_speak_String'] =
    Blockly.FtcJava['androidTextToSpeech_speak'];
