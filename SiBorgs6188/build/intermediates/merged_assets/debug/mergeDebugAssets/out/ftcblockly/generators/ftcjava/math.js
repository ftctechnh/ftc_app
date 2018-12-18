/**
 * @license
 * Visual Blocks Language
 *
 * Copyright 2012 Google Inc.
 * https://developers.google.com/blockly/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @fileoverview Generating FTC Java for math blocks.
 * @author lizlooney@gmail.com (Liz Looney)
 *
 * based on Generating JavaScript for math blocks.
 * @author q.neutron@gmail.com (Quynh Neutron)
 */
'use strict';

goog.provide('Blockly.FtcJava.math');

goog.require('Blockly.FtcJava');


Blockly.FtcJava['math_number'] = function(block) {
  // Numeric value.
  var code = parseFloat(block.getFieldValue('NUM'));
  var order = code >= 0 ? Blockly.FtcJava.ORDER_ATOMIC :
      Blockly.FtcJava.ORDER_UNARY_NEGATION;
  return [code, order];
};

Blockly.FtcJava['math_arithmetic'] = function(block) {
  // Basic arithmetic operators, and power.
  var OPERATORS = {
    'ADD': [' + ', Blockly.FtcJava.ORDER_ADDITION],
    'MINUS': [' - ', Blockly.FtcJava.ORDER_SUBTRACTION],
    'MULTIPLY': [' * ', Blockly.FtcJava.ORDER_MULTIPLICATION],
    'DIVIDE': [' / ', Blockly.FtcJava.ORDER_DIVISION],
    'POWER': [null, Blockly.FtcJava.ORDER_COMMA]  // Handle power separately.
  };
  var tuple = OPERATORS[block.getFieldValue('OP')];
  var operator = tuple[0];
  var order = tuple[1];
  var argument0 = Blockly.FtcJava.valueToCode(block, 'A', order) || '0';
  var argument1 = Blockly.FtcJava.valueToCode(block, 'B', order) || '0';
  var code;
  // Power in Java requires a special case since it has no operator.
  if (!operator) {
    code = 'Math.pow(' + argument0 + ', ' + argument1 + ')';
    return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
  }
  code = argument0 + operator + argument1;
  return [code, order];
};

Blockly.FtcJava['math_single'] = function(block) {
  // Math operators with single operand.
  var operator = block.getFieldValue('OP');
  var code;
  var arg;
  if (operator == 'NEG') {
    // Negation is a special case given its different operator precedence.
    arg = Blockly.FtcJava.valueToCode(block, 'NUM',
        Blockly.FtcJava.ORDER_UNARY_NEGATION) || '0';
    if (arg[0] == '-') {
      // --3 is not legal.
      arg = ' ' + arg;
    }
    code = '-' + arg;
    return [code, Blockly.FtcJava.ORDER_UNARY_NEGATION];
  }
  if (operator == 'SIN' || operator == 'COS' || operator == 'TAN') {
    arg = Blockly.FtcJava.valueToCode(block, 'NUM',
        Blockly.FtcJava.ORDER_DIVISION) || '0';
  } else {
    arg = Blockly.FtcJava.valueToCode(block, 'NUM',
        Blockly.FtcJava.ORDER_NONE) || '0';
  }
  // First, handle cases which generate values that don't need parentheses
  // wrapping the code.
  switch (operator) {
    case 'ABS':
      code = 'Math.abs(' + arg + ')';
      break;
    case 'ROOT':
      code = 'Math.sqrt(' + arg + ')';
      break;
    case 'LN':
      code = 'Math.log(' + arg + ')';
      break;
    case 'EXP':
      code = 'Math.exp(' + arg + ')';
      break;
    case 'POW10':
      code = 'Math.pow(10, ' + arg + ')';
      break;
    case 'ROUND':
      code = 'Math.round(' + arg + ')';
      break;
    case 'ROUNDUP':
      code = 'Math.ceil(' + arg + ')';
      break;
    case 'ROUNDDOWN':
      code = 'Math.floor(' + arg + ')';
      break;
    case 'SIN':
      code = 'Math.sin(' + arg + ' / 180 * Math.PI)';
      break;
    case 'COS':
      code = 'Math.cos(' + arg + ' / 180 * Math.PI)';
      break;
    case 'TAN':
      code = 'Math.tan(' + arg + ' / 180 * Math.PI)';
      break;
  }
  if (code) {
    return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
  }
  // Second, handle cases which generate values that may need parentheses
  // wrapping the code.
  switch (operator) {
    case 'LOG10':
      code = 'Math.log(' + arg + ') / Math.log(10)';
      break;
    case 'ASIN':
      code = 'Math.asin(' + arg + ') / Math.PI * 180';
      break;
    case 'ACOS':
      code = 'Math.acos(' + arg + ') / Math.PI * 180';
      break;
    case 'ATAN':
      code = 'Math.atan(' + arg + ') / Math.PI * 180';
      break;
    default:
      throw 'Unknown math operator: ' + operator;
  }
  return [code, Blockly.FtcJava.ORDER_DIVISION];
};

Blockly.FtcJava['math_constant'] = function(block) {
  // Constants: PI, E, the Golden Ratio, sqrt(2), 1/sqrt(2), INFINITY.
  var CONSTANTS = {
    'PI': ['Math.PI', Blockly.FtcJava.ORDER_MEMBER],
    'E': ['Math.E', Blockly.FtcJava.ORDER_MEMBER],
    'GOLDEN_RATIO':
        ['(1 + Math.sqrt(5)) / 2', Blockly.FtcJava.ORDER_DIVISION],
    'SQRT2': ['Math.sqrt(2)', Blockly.FtcJava.ORDER_FUNCTION_CALL],
    'SQRT1_2': ['Math.sqrt(0.5)', Blockly.FtcJava.ORDER_FUNCTION_CALL],
    'INFINITY': ['Double.POSITIVE_INFINITY', Blockly.FtcJava.ORDER_MEMBER]
  };
  return CONSTANTS[block.getFieldValue('CONSTANT')];
};

Blockly.FtcJava['math_number_property'] = function(block) {
  // Check if a number is even, odd, prime, whole, positive, or negative
  // or if it is divisible by certain number. Returns true or false.
  var number_to_check = Blockly.FtcJava.valueToCode(block, 'NUMBER_TO_CHECK',
      Blockly.FtcJava.ORDER_MODULUS) || '0';
  var dropdown_property = block.getFieldValue('PROPERTY');
  var code;
  if (dropdown_property == 'PRIME') {
    Blockly.FtcJava.generateImport_('JavaUtil');
    code = 'JavaUtil.isPrime(' + number_to_check + ')';
    return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
  }
  switch (dropdown_property) {
    case 'EVEN':
      code = number_to_check + ' % 2 == 0';
      break;
    case 'ODD':
      code = number_to_check + ' % 2 == 1';
      break;
    case 'WHOLE':
      code = number_to_check + ' % 1 == 0';
      break;
    case 'POSITIVE':
      code = number_to_check + ' > 0';
      break;
    case 'NEGATIVE':
      code = number_to_check + ' < 0';
      break;
    case 'DIVISIBLE_BY':
      var divisor = Blockly.FtcJava.valueToCode(block, 'DIVISOR',
          Blockly.FtcJava.ORDER_MODULUS) || '0';
      code = number_to_check + ' % ' + divisor + ' == 0';
      break;
  }
  return [code, Blockly.FtcJava.ORDER_EQUALITY];
};

Blockly.FtcJava['math_change'] = function(block) {
  // Add to a variable in place.
  var varName = Blockly.FtcJava.variableDB_.getName(
      block.getFieldValue('VAR'), Blockly.Variables.NAME_TYPE);
  var delta = Blockly.FtcJava.valueToCode(block, 'DELTA',
      Blockly.FtcJava.ORDER_ASSIGNMENT) || '0';
  // TODO(lizlooney): What if the var is not a number?
  return varName + ' += ' + delta + ';\n';
};

// Rounding functions have a single operand.
Blockly.FtcJava['math_round'] = Blockly.FtcJava['math_single'];
// Trigonometry functions have a single operand.
Blockly.FtcJava['math_trig'] = Blockly.FtcJava['math_single'];

Blockly.FtcJava['math_on_list'] = function(block) {
  // Math functions for lists.
  var op = block.getFieldValue('OP');
  var list = Blockly.FtcJava.valueToCode(block, 'LIST',
      Blockly.FtcJava.ORDER_NONE) || 'null';
  Blockly.FtcJava.generateImport_('JavaUtil');
  var code;
  switch (op) {
    case 'SUM':
      code = 'JavaUtil.sumOfList(' + list + ')';
      break;
    case 'MIN':
      code = 'JavaUtil.minOfList(' + list + ')';
      break;
    case 'MAX':
      code = 'JavaUtil.maxOfList(' + list + ')';
      break;
    case 'AVERAGE':
      // mathMean([null,null,1,3]) == 2.0.
      code = 'JavaUtil.averageOfList(' + list + ')';
      break;
    case 'MEDIAN':
      // mathMedian([null,null,1,3]) == 2.0.
      code = 'JavaUtil.medianOfList(' + list + ')';
      break;
    case 'MODE':
      // As a list of items can contain more than one mode,
      // the returned result is provided as a List.
      // Mode of [3, 'x', 'x', 1, 1, 2, '3'] -> ['x', 1].
      code = 'JavaUtil.modesOfList(' + list + ')';
      break;
    case 'STD_DEV':
      code = 'JavaUtil.standardDeviationOfList(' + list + ')';
      break;
    case 'RANDOM':
      code = 'JavaUtil.randomItemOfList(' + list + ')';
      break;
    default:
      throw 'Unknown operator: ' + func;
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['math_modulo'] = function(block) {
  // Remainder computation.
  var argument0 = Blockly.FtcJava.valueToCode(block, 'DIVIDEND',
      Blockly.FtcJava.ORDER_MODULUS) || '0';
  var argument1 = Blockly.FtcJava.valueToCode(block, 'DIVISOR',
      Blockly.FtcJava.ORDER_MODULUS) || '0';
  var code = argument0 + ' % ' + argument1;
  return [code, Blockly.FtcJava.ORDER_MODULUS];
};

Blockly.FtcJava['math_constrain'] = function(block) {
  // Constrain a number between two limits.
  var value = Blockly.FtcJava.valueToCode(block, 'VALUE',
      Blockly.FtcJava.ORDER_COMMA) || '0';
  var low = Blockly.FtcJava.valueToCode(block, 'LOW',
      Blockly.FtcJava.ORDER_COMMA) || '0';
  var high = Blockly.FtcJava.valueToCode(block, 'HIGH',
      Blockly.FtcJava.ORDER_COMMA) || 'Infinity';
  var code = 'Math.min(Math.max(' + value + ', ' + low + '), ' + high + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['math_random_int'] = function(block) {
  // Random integer between [X] and [Y].
  var argument0 = Blockly.FtcJava.valueToCode(block, 'FROM',
      Blockly.FtcJava.ORDER_COMMA) || '0';
  var argument1 = Blockly.FtcJava.valueToCode(block, 'TO',
      Blockly.FtcJava.ORDER_COMMA) || '0';
  Blockly.FtcJava.generateImport_('JavaUtil');
  var code = 'JavaUtil.randomInt(' + argument0 + ', ' + argument1 + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['math_random_float'] = function(block) {
  // Random fraction between 0 and 1.
  return ['Math.random()', Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
