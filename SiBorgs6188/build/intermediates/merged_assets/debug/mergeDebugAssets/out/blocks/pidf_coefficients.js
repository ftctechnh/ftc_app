/**
 * @fileoverview FTC robot blocks related to PIDFCoefficients.
 * @author lizlooney@google.com (Liz Looney)
 */

// The following are generated dynamically in HardwareUtil.fetchJavaScriptForHardware():
// pidfCoefficientsIdentifierForJavaScript
// The following are defined in vars.js:
// createNonEditableField
// getPropertyColor
// functionColor

// Constructors

Blockly.Blocks['pidfCoefficients_create'] = {
  init: function() {
    this.setOutput(true, 'PIDFCoefficients');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('PIDFCoefficients'));
    this.setColour(functionColor);
    this.setTooltip('Creates a new PIDFCoefficients object.');
  }
};

Blockly.JavaScript['pidfCoefficients_create'] = function(block) {
  var code = pidfCoefficientsIdentifierForJavaScript + '.create()';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['pidfCoefficients_create'] = function(block) {
  var code = 'new PIDFCoefficients()';
  Blockly.FtcJava.generateImport_('PIDFCoefficients');
  return [code, Blockly.FtcJava.ORDER_NEW];
};

Blockly.Blocks['pidfCoefficients_create_withPIDFAlgorithm'] = {
  init: function() {
    this.setOutput(true, 'PIDFCoefficients');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('PIDFCoefficients'));
    this.appendValueInput('P').setCheck('Number')
        .appendField('p')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('I').setCheck('Number')
        .appendField('i')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('D').setCheck('Number')
        .appendField('d')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('F').setCheck('Number')
        .appendField('f')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('ALGORITHM').setCheck('MotorControlAlgorithm')
        .appendField('algorithm')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Creates a new PIDFCoefficients object with the specified ' +
        'proportional, integral derivative, and feed-forward terms (denoted P, I, D, and F respectively), ' +
        'and the specified algorithm.');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'P':
        case 'I':
        case 'D':
        case 'F':
          return 'double';
      }
      return '';
    };
  }
};

Blockly.JavaScript['pidfCoefficients_create_withPIDFAlgorithm'] = function(block) {
  var p = Blockly.JavaScript.valueToCode(
      block, 'P', Blockly.JavaScript.ORDER_COMMA);
  var i = Blockly.JavaScript.valueToCode(
      block, 'I', Blockly.JavaScript.ORDER_COMMA);
  var d = Blockly.JavaScript.valueToCode(
      block, 'D', Blockly.JavaScript.ORDER_COMMA);
  var f = Blockly.JavaScript.valueToCode(
      block, 'F', Blockly.JavaScript.ORDER_COMMA);
  var algorithm = Blockly.JavaScript.valueToCode(
      block, 'ALGORITHM', Blockly.JavaScript.ORDER_COMMA);
  var code = pidfCoefficientsIdentifierForJavaScript + '.create_withPIDFAlgorithm(' +
      p + ', ' + i + ', ' + d + ', ' + f + ', ' + algorithm + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['pidfCoefficients_create_withPIDFAlgorithm'] = function(block) {
  var p = Blockly.FtcJava.valueToCode(
      block, 'P', Blockly.FtcJava.ORDER_COMMA);
  var i = Blockly.FtcJava.valueToCode(
      block, 'I', Blockly.FtcJava.ORDER_COMMA);
  var d = Blockly.FtcJava.valueToCode(
      block, 'D', Blockly.FtcJava.ORDER_COMMA);
  var f = Blockly.FtcJava.valueToCode(
      block, 'F', Blockly.FtcJava.ORDER_COMMA);
  var algorithm = Blockly.FtcJava.valueToCode(
      block, 'ALGORITHM', Blockly.FtcJava.ORDER_COMMA);
  var code = 'new PIDFCoefficients(' + p + ', ' + i + ', ' + d + ', ' + f + ', ' + algorithm + ')';
  Blockly.FtcJava.generateImport_('PIDFCoefficients');
  return [code, Blockly.FtcJava.ORDER_NEW];
};

Blockly.Blocks['pidfCoefficients_create_withPIDF'] = {
  init: function() {
    this.setOutput(true, 'PIDFCoefficients');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('PIDFCoefficients'));
    this.appendValueInput('P').setCheck('Number')
        .appendField('p')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('I').setCheck('Number')
        .appendField('i')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('D').setCheck('Number')
        .appendField('d')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('F').setCheck('Number')
        .appendField('f')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Creates a new PIDFCoefficients object with the specified ' +
        'proportional, integral derivative, and feed-forward terms (denoted P, I, D, and F respectively).');
    this.getFtcJavaInputType = function(inputName) {
      switch (inputName) {
        case 'P':
        case 'I':
        case 'D':
        case 'F':
          return 'double';
      }
      return '';
    };
  }
};

Blockly.JavaScript['pidfCoefficients_create_withPIDF'] = function(block) {
  var p = Blockly.JavaScript.valueToCode(
      block, 'P', Blockly.JavaScript.ORDER_COMMA);
  var i = Blockly.JavaScript.valueToCode(
      block, 'I', Blockly.JavaScript.ORDER_COMMA);
  var d = Blockly.JavaScript.valueToCode(
      block, 'D', Blockly.JavaScript.ORDER_COMMA);
  var f = Blockly.JavaScript.valueToCode(
      block, 'F', Blockly.JavaScript.ORDER_COMMA);
  var code = pidfCoefficientsIdentifierForJavaScript + '.create_withPIDF(' +
      p + ', ' + i + ', ' + d + ', ' + f + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['pidfCoefficients_create_withPIDF'] = function(block) {
  var p = Blockly.FtcJava.valueToCode(
      block, 'P', Blockly.FtcJava.ORDER_COMMA);
  var i = Blockly.FtcJava.valueToCode(
      block, 'I', Blockly.FtcJava.ORDER_COMMA);
  var d = Blockly.FtcJava.valueToCode(
      block, 'D', Blockly.FtcJava.ORDER_COMMA);
  var f = Blockly.FtcJava.valueToCode(
      block, 'F', Blockly.FtcJava.ORDER_COMMA);
  var code = 'new PIDFCoefficients(' + p + ', ' + i + ', ' + d + ', ' + f + ')';
  Blockly.FtcJava.generateImport_('PIDFCoefficients');
  return [code, Blockly.FtcJava.ORDER_NEW];
};

Blockly.Blocks['pidfCoefficients_create_withPIDFCoefficients'] = {
  init: function() {
    this.setOutput(true, 'PIDFCoefficients');
    this.appendDummyInput()
        .appendField('new')
        .appendField(createNonEditableField('PIDFCoefficients'));
    this.appendValueInput('PIDF_COEFFICIENTS').setCheck('PIDFCoefficients')
        .appendField('pidfCoefficients')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Creates a new PIDFCoefficients object from the given PIDFCoefficients object.');
  }
};

Blockly.JavaScript['pidfCoefficients_create_withPIDFCoefficients'] = function(block) {
  var pidfCoefficients = Blockly.JavaScript.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.JavaScript.ORDER_NONE);
  var code = pidfCoefficientsIdentifierForJavaScript + '.create_withPIDFCoefficients(' +
      pidfCoefficients + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['pidfCoefficients_create_withPIDFCoefficients'] = function(block) {
  var pidfCoefficients = Blockly.FtcJava.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.FtcJava.ORDER_NONE);
  var code = 'new PIDFCoefficients(' + pidfCoefficients + ')';
  Blockly.FtcJava.generateImport_('PIDFCoefficients');
  return [code, Blockly.FtcJava.ORDER_NEW];
};

// Properties

Blockly.Blocks['pidfCoefficients_setProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['P', 'P'],
        ['I', 'I'],
        ['D', 'D'],
        ['F', 'F'],
    ];
    this.appendDummyInput()
        .appendField('set')
        .appendField(createNonEditableField('PIDFCoefficients'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('PIDF_COEFFICIENTS').setCheck('PIDFCoefficients')
        .appendField('pidfCoefficients')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE').setCheck('Number')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['P', 'Sets the proportional term, P, of the given PIDFCoefficients object.'],
        ['I', 'Sets the integral term, I, of the given PIDFCoefficients object.'],
        ['D', 'Sets the derivative term, D, of the given PIDFCoefficients object.'],
        ['F', 'RetuSets the feed-forward term, F, of the given PIDFCoefficients object.'],
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
          case 'P':
          case 'I':
          case 'D':
          case 'F':
            return 'double';
          default:
            throw 'Unexpected property ' + property + ' (pidfCoefficients_setProperty_Number getArgumentType).';
        }
      }
      return '';
    };
  }
};

Blockly.JavaScript['pidfCoefficients_setProperty_Number'] = function(block) {
  var property = block.getFieldValue('PROP');
  var pidfCoefficients = Blockly.JavaScript.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.JavaScript.ORDER_COMMA);
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_COMMA);
  return pidfCoefficientsIdentifierForJavaScript + '.set' + property + '(' + pidfCoefficients + ', ' + value + ');\n';
};

Blockly.FtcJava['pidfCoefficients_setProperty_Number'] = function(block) {
  var property = block.getFieldValue('PROP');
  var pidfCoefficients = Blockly.FtcJava.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.FtcJava.ORDER_MEMBER);
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_ASSIGNMENT);
  return pidfCoefficients + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property) + ' = ' + value + ';\n';
};

Blockly.Blocks['pidfCoefficients_setProperty_MotorControlAlgorithm'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Algorithm', 'Algorithm'],
    ];
    this.appendDummyInput()
        .appendField('set')
        .appendField(createNonEditableField('PIDFCoefficients'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('PIDF_COEFFICIENTS').setCheck('PIDFCoefficients')
        .appendField('pidfCoefficients')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.appendValueInput('VALUE').setCheck('MotorControlAlgorithm')
        .appendField('to')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setColour(setPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Algorithm', 'Sets the algorithm of the given PIDFCoefficients object.'],
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

Blockly.JavaScript['pidfCoefficients_setProperty_MotorControlAlgorithm'] = function(block) {
  var property = block.getFieldValue('PROP');
  var pidfCoefficients = Blockly.JavaScript.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.JavaScript.ORDER_COMMA);
  var value = Blockly.JavaScript.valueToCode(
      block, 'VALUE', Blockly.JavaScript.ORDER_COMMA);
  return pidfCoefficientsIdentifierForJavaScript + '.set' + property + '(' + pidfCoefficients + ', ' + value + ');\n';
};

Blockly.FtcJava['pidfCoefficients_setProperty_MotorControlAlgorithm'] = function(block) {
  var property = block.getFieldValue('PROP');
  var pidfCoefficients = Blockly.FtcJava.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.FtcJava.ORDER_MEMBER);
  var value = Blockly.FtcJava.valueToCode(
      block, 'VALUE', Blockly.FtcJava.ORDER_ASSIGNMENT);
  return pidfCoefficients + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property) + ' = ' + value + ';\n';
};

Blockly.Blocks['pidfCoefficients_getProperty_Number'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['P', 'P'],
        ['I', 'I'],
        ['D', 'D'],
        ['F', 'F'],
    ];
    this.setOutput(true, 'Number');
    this.appendDummyInput()
        .appendField(createNonEditableField('PIDFCoefficients'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('PIDF_COEFFICIENTS').setCheck('PIDFCoefficients')
        .appendField('pidfCoefficients')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the closures below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['P', 'Returns the proportional term, P, of the given PIDFCoefficients object.'],
        ['I', 'Returns the integral term, I, of the given PIDFCoefficients object.'],
        ['D', 'Returns the derivative term, D, of the given PIDFCoefficients object.'],
        ['F', 'Returns the feed-forward term, F, of the given PIDFCoefficients object.'],
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
        case 'P':
        case 'I':
        case 'D':
        case 'F':
          return 'double';
        default:
          throw 'Unexpected property ' + property + ' (pidfCoefficients_getProperty_Number getOutputType).';
      }
    };
  }
};

Blockly.JavaScript['pidfCoefficients_getProperty_Number'] = function(block) {
  var property = block.getFieldValue('PROP');
  var pidfCoefficients = Blockly.JavaScript.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.JavaScript.ORDER_NONE);
  var code = pidfCoefficientsIdentifierForJavaScript + '.get' + property + '(' +
      pidfCoefficients + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['pidfCoefficients_getProperty_Number'] = function(block) {
  var property = block.getFieldValue('PROP');
  var pidfCoefficients = Blockly.FtcJava.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.FtcJava.ORDER_NONE);
  var code = pidfCoefficients + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property);
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

Blockly.Blocks['pidfCoefficients_getProperty_MotorControlAlgorithm'] = {
  init: function() {
    var PROPERTY_CHOICES = [
        ['Algorithm', 'Algorithm'],
    ];
    this.setOutput(true, 'MotorControlAlgorithm');
    this.appendDummyInput()
        .appendField(createNonEditableField('PIDFCoefficients'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(PROPERTY_CHOICES), 'PROP');
    this.appendValueInput('PIDF_COEFFICIENTS').setCheck('PIDFCoefficients')
        .appendField('pidfCoefficients')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['Algorithm', 'Returns the algorithm of the given PIDFCoefficients object.'],
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

Blockly.JavaScript['pidfCoefficients_getProperty_MotorControlAlgorithm'] = function(block) {
  var property = block.getFieldValue('PROP');
  var pidfCoefficients = Blockly.JavaScript.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.JavaScript.ORDER_NONE);
  var code = pidfCoefficientsIdentifierForJavaScript + '.get' + property + '(' +
      pidfCoefficients + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['pidfCoefficients_getProperty_MotorControlAlgorithm'] = function(block) {
  var property = block.getFieldValue('PROP');
  var pidfCoefficients = Blockly.FtcJava.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.FtcJava.ORDER_NONE);
  var code = pidfCoefficients + '.' + Blockly.FtcJava.makeFirstLetterLowerCase_(property);
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};

// Functions

Blockly.Blocks['pidfCoefficients_toText'] = {
  init: function() {
    this.setOutput(true, 'String');
    this.appendDummyInput()
        .appendField('call')
        .appendField(createNonEditableField('PIDFCoefficients'))
        .appendField('.')
        .appendField(createNonEditableField('toText'));
    this.appendValueInput('PIDF_COEFFICIENTS').setCheck('PIDFCoefficients')
        .appendField('pidfCoefficients')
        .setAlign(Blockly.ALIGN_RIGHT);
    this.setColour(functionColor);
    this.setTooltip('Returns a text representation of the given PIDFCoefficients object.');
  }
};

Blockly.JavaScript['pidfCoefficients_toText'] = function(block) {
  var pidfCoefficients = Blockly.JavaScript.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.JavaScript.ORDER_NONE);
  var code = pidfCoefficientsIdentifierForJavaScript + '.toText(' + pidfCoefficients + ')';
  return [code, Blockly.JavaScript.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['pidfCoefficients_toText'] = function(block) {
  var pidfCoefficients = Blockly.FtcJava.valueToCode(
      block, 'PIDF_COEFFICIENTS', Blockly.FtcJava.ORDER_MEMBER);
  var code = pidfCoefficients + '.toString()';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

// enums

Blockly.Blocks['pidfCoefficients_typedEnum_motorControlAlgorithm'] = {
  init: function() {
    var MOTOR_CONTROL_ALGORITHM_CHOICES = [
        ['LegacyPID', 'LegacyPID'],
        ['PIDF', 'PIDF'],
    ];
    this.setOutput(true, 'MotorControlAlgorithm');
    this.appendDummyInput()
        .appendField(createNonEditableField('MotorControlAlgorithm'))
        .appendField('.')
        .appendField(new Blockly.FieldDropdown(MOTOR_CONTROL_ALGORITHM_CHOICES), 'MOTOR_CONTROL_ALGORITHM');
    this.setColour(getPropertyColor);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    var TOOLTIPS = [
        ['LegacyPID', 'The MotorControlAlgorithm value LegacyPID.'],
        ['PIDF', 'The MotorControlAlgorithm value PIDF.'],
    ];
    this.setTooltip(function() {
      var key = thisBlock.getFieldValue('MOTOR_CONTROL_ALGORITHM');
      for (var i = 0; i < TOOLTIPS.length; i++) {
        if (TOOLTIPS[i][0] == key) {
          return TOOLTIPS[i][1];
        }
      }
      return '';
    });
  }
};

Blockly.JavaScript['pidfCoefficients_typedEnum_motorControlAlgorithm'] = function(block) {
  var code = '"' + block.getFieldValue('MOTOR_CONTROL_ALGORITHM') + '"';
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.FtcJava['pidfCoefficients_typedEnum_motorControlAlgorithm'] = function(block) {
  var code = 'MotorControlAlgorithm.' + block.getFieldValue('MOTOR_CONTROL_ALGORITHM');
  Blockly.FtcJava.generateImport_('MotorControlAlgorithm');
  return [code, Blockly.FtcJava.ORDER_MEMBER];
};
