/**
 * @fileoverview FTC robot blocks related to IR seeker sensor.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// createIrSeekerSensorDropdown
// The following are defined in vars.js:
// getPropertyColor
// setPropertyColor

Blockly.Blocks['irSeekerSensor_setProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['SignalDetectedThreshold', 'SignalDetectedThreshold'],
        ['Mode', 'Mode'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE') // no type, for compatibility
        .appendField('set')
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['SignalDetectedThreshold', 'Sets the minimum threshold for a signal to be considered detected.'],
        ['Mode', 'Sets the device mode: MODE_600HZ or MODE_1200HZ.'],
        ['I2cAddress7Bit', 'Sets the 7 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
        ['I2cAddress8Bit', 'Sets the 8 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
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

Blockly.JavaScript['irSeekerSensor_setProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_NONE);
  return identifier + '.set' + property + '(' + value + ');\n';
};

Blockly.FtcJava['irSeekerSensor_setProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'IrSeekerSensor');
  var property = block.getFieldValue('PROP');
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_NONE);
  var code;
  switch (property) {
    case 'SignalDetectedThreshold':
    case 'Mode':
      code = identifier + '.set' + property + '(' + value + ');\n';
      break;
    case 'I2cAddress7Bit':
      Blockly.FtcJava.generateImport_('I2cAddr');
      code = identifier + '.setI2cAddress(I2cAddr.create7bit(' + value + '));\n';
      break;
    case 'I2cAddress8Bit':
      Blockly.FtcJava.generateImport_('I2cAddr');
      code = identifier + '.setI2cAddress(I2cAddr.create8bit(' + value + '));\n';
      break;
    default:
      throw 'Unexpected property ' + property + ' (irSeekerSensor_setProperty).';
  }
  return code;
};

Blockly.Blocks['irSeekerSensor_setProperty_IrSeekerSensorMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.appendValueInput('VALUE').setCheck('IrSeekerSensor.Mode')
        .appendField('set')
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'Sets the device mode: MODE_600HZ or MODE_1200HZ.'],
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

Blockly.JavaScript['irSeekerSensor_setProperty_IrSeekerSensorMode'] =
    Blockly.JavaScript['irSeekerSensor_setProperty'];

Blockly.FtcJava['irSeekerSensor_setProperty_IrSeekerSensorMode'] =
    Blockly.FtcJava['irSeekerSensor_setProperty'];

Blockly.Blocks['irSeekerSensor_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['SignalDetectedThreshold', 'SignalDetectedThreshold'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('set')
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP')
        .appendField('to');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['SignalDetectedThreshold', 'Sets the minimum threshold for a signal to be considered detected.'],
        ['I2cAddress7Bit', 'Sets the 7 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
        ['I2cAddress8Bit', 'Sets the 8 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
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
          case 'SignalDetectedThreshold':
            return 'double';
          case 'I2cAddress7Bit':
          case 'I2cAddress8Bit':
            return 'int';
          default:
            throw 'Unexpected property ' + property + ' (irSeekerSensor_setProperty_Number getArgumentType).';
        }
      }
      return '';
    };
  }
};

Blockly.JavaScript['irSeekerSensor_setProperty_Number'] =
    Blockly.JavaScript['irSeekerSensor_setProperty'];

Blockly.FtcJava['irSeekerSensor_setProperty_Number'] =
    Blockly.FtcJava['irSeekerSensor_setProperty'];

Blockly.Blocks['irSeekerSensor_getProperty'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['SignalDetectedThreshold', 'SignalDetectedThreshold'],
        ['Mode', 'Mode'],
        ['IsSignalDetected', 'IsSignalDetected'],
        ['Angle', 'Angle'],
        ['Strength', 'Strength'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['SignalDetectedThreshold', 'Returns the minimum threshold for a signal to be considered detected.'],
        ['Mode', 'Returns the device mode: MODE_600HZ or MODE_1200HZ.'],
        ['IsSignalDetected', 'Returns true if an IR signal is detected.'],
        ['Angle', 'Returns the estimated angle that the signal is coming from.'],
        ['Strength', 'Returns the strength of the detected IR signal, on a scale of 0.0 to 1.0.'],
        ['I2cAddress7Bit', 'Returns the 7 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
        ['I2cAddress8Bit', 'Returns the 8 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
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

Blockly.JavaScript['irSeekerSensor_getProperty'] = function(block) {
  var identifier = block.getFieldValue('IDENTIFIER');
  var property = block.getFieldValue('PROP');
  var code = identifier + '.get' + property + '()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['irSeekerSensor_getProperty'] = function(block) {
  var identifier = Blockly.FtcJava.importDeclareAssign_(block, 'IDENTIFIER', 'IrSeekerSensor');
  var property = block.getFieldValue('PROP');
  var code;
  switch (property) {
    case 'SignalDetectedThreshold':
    case 'Mode':
    case 'Angle':
    case 'Strength':
      code = identifier + '.get' + property + '()';
      break;
    case 'IsSignalDetected':
      code = identifier + '.signalDetected()';
      break;
    case 'I2cAddress7Bit':
      code = identifier + '.getI2cAddress().get7Bit()';
      break;
    case 'I2cAddress8Bit':
      code = identifier + '.getI2cAddress().get8Bit()';
      break;
    default:
      throw 'Unexpected property ' + property + ' (irSeekerSensor_getProperty).';
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.Blocks['irSeekerSensor_getProperty_IrSeekerSensorMode'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Mode', 'Mode'],
    ];
    this.setOutput(true, 'IrSeekerSensor.Mode');
    this.appendDummyInput()
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Mode', 'Returns the device mode: MODE_600HZ or MODE_1200HZ.'],
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

Blockly.JavaScript['irSeekerSensor_getProperty_IrSeekerSensorMode'] =
    Blockly.JavaScript['irSeekerSensor_getProperty'];

Blockly.FtcJava['irSeekerSensor_getProperty_IrSeekerSensorMode'] =
    Blockly.FtcJava['irSeekerSensor_getProperty'];

Blockly.Blocks['irSeekerSensor_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['SignalDetectedThreshold', 'SignalDetectedThreshold'],
        ['Angle', 'Angle'],
        ['Strength', 'Strength'],
        ['I2cAddress7Bit', 'I2cAddress7Bit'],
        ['I2cAddress8Bit', 'I2cAddress8Bit'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['SignalDetectedThreshold', 'Returns the minimum threshold for a signal to be considered detected.'],
        ['Angle', 'Returns the estimated angle that the signal is coming from.'],
        ['Strength', 'Returns the strength of the detected IR signal, on a scale of 0.0 to 1.0.'],
        ['I2cAddress7Bit', 'Returns the 7 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
        ['I2cAddress8Bit', 'Returns the 8 bit I2C address of the IR seeker sensor. ' +
            'Not all IR seeker sensors support this feature.'],
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
        case 'SignalDetectedThreshold':
        case 'Angle':
        case 'Strength':
          return 'double';
        case 'I2cAddress7Bit':
        case 'I2cAddress8Bit':
          return 'int';
        default:
          throw 'Unexpected property ' + property + ' (irSeekerSensor_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['irSeekerSensor_getProperty_Number'] =
    Blockly.JavaScript['irSeekerSensor_getProperty'];

Blockly.FtcJava['irSeekerSensor_getProperty_Number'] =
    Blockly.FtcJava['irSeekerSensor_getProperty'];

Blockly.Blocks['irSeekerSensor_getProperty_Boolean'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['IsSignalDetected', 'IsSignalDetected'],
    ];
    this.setOutput(true, 'Boolean');
    this.appendDummyInput()
        .appendField(createIrSeekerSensorDropdown(), 'IDENTIFIER')
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['IsSignalDetected', 'Returns true if an IR signal is detected.'],
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

Blockly.JavaScript['irSeekerSensor_getProperty_Boolean'] =
    Blockly.JavaScript['irSeekerSensor_getProperty'];

Blockly.FtcJava['irSeekerSensor_getProperty_Boolean'] =
    Blockly.FtcJava['irSeekerSensor_getProperty'];

// Enums

Blockly.Blocks['irSeekerSensor_enum_mode'] = {
  init: function() {
    var MODE_CHOICES = [
        ['MODE_600HZ', 'MODE_600HZ'],
        ['MODE_1200HZ', 'MODE_1200HZ'],
    ];
    this.setOutput(true); // no type, for compatibility
    this.appendDummyInput()
        .appendField(createNonEditableField('Mode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(MODE_CHOICES), 'MODE');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MODE_600HZ', 'The IrSeekerSensor Mode value MODE_600HZ.'],
        ['MODE_1200HZ', 'The IrSeekerSensor Mode value MODE_1200HZ.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('MODE');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['irSeekerSensor_enum_mode'] = function(block) {
  var code = '"' + block.getFieldValue('MODE') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['irSeekerSensor_enum_mode'] = function(block) {
  var code = 'IrSeekerSensor.Mode.' + block.getFieldValue('MODE');
  Blockly.FtcJava.generateImport_('IrSeekerSensor');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};


Blockly.Blocks['irSeekerSensor_typedEnum_mode'] = {
  init: function() {
    var MODE_CHOICES = [
        ['MODE_600HZ', 'MODE_600HZ'],
        ['MODE_1200HZ', 'MODE_1200HZ'],
    ];
    this.setOutput(true, 'IrSeekerSensor.Mode');
    this.appendDummyInput()
        .appendField(createNonEditableField('Mode'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(MODE_CHOICES), 'MODE');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['MODE_600HZ', 'The IrSeekerSensor Mode value MODE_600HZ.'],
        ['MODE_1200HZ', 'The IrSeekerSensor Mode value MODE_1200HZ.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('MODE');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['irSeekerSensor_typedEnum_mode'] =
    Blockly.JavaScript['irSeekerSensor_enum_mode'];

Blockly.FtcJava['irSeekerSensor_typedEnum_mode'] =
    Blockly.FtcJava['irSeekerSensor_enum_mode'];
