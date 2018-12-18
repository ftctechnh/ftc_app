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
 * @fileoverview Generating FTC Java for logic blocks.
 * @author lizlooney@gmail.com (Liz Looney)
 *
 * based on Generating JavaScript for logic blocks.
 * @author q.neutron@gmail.com (Quynh Neutron)
 */
'use strict';

goog.provide('Blockly.FtcJava.logic');

goog.require('Blockly.FtcJava');


Blockly.FtcJava['controls_if'] = function(block) {
  // If/elseif/else condition.
  var n = 0;
  var code = '', branchCode, conditionCode;
  do {
    conditionCode = Blockly.FtcJava.valueToCode(block, 'IF' + n,
      Blockly.FtcJava.ORDER_NONE) || 'false';
    branchCode = Blockly.FtcJava.statementToCode(block, 'DO' + n);
    code += (n > 0 ? ' else ' : '') +
        'if (' + conditionCode + ') {\n' + branchCode + '}';

    ++n;
  } while (block.getInput('IF' + n));

  if (block.getInput('ELSE')) {
    branchCode = Blockly.FtcJava.statementToCode(block, 'ELSE');
    code += ' else {\n' + branchCode + '}';
  }
  return code + '\n';
};

Blockly.FtcJava['controls_ifelse'] = Blockly.FtcJava['controls_if'];

Blockly.FtcJava['logic_compare'] = function(block) {
  var opFieldValue = block.getFieldValue('OP');
  if (opFieldValue == 'EQ') {
    // Look at the block that is plugged into the A socket. Is its output non-primitive?
    var outputType = Blockly.FtcJava.getOutputType_(block.getInputTargetBlock('A'));
    if (outputType != '' && !Blockly.FtcJava.isFtcJavaTypePrimitive_(outputType)) {
      var argument0 = Blockly.FtcJava.valueToCode(block, 'A', Blockly.FtcJava.ORDER_MEMBER);
      var argument1 = Blockly.FtcJava.valueToCode(block, 'B', Blockly.FtcJava.ORDER_NONE);
      var code = argument0 + '.equals(' + argument1 + ')';
      return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
    }
  } else if (opFieldValue == 'NEQ') {
    // Look at the block that is plugged into the A socket. Is its output non-primitive?
    var outputType = Blockly.FtcJava.getOutputType_(block.getInputTargetBlock('A'));
    if (outputType != '' && !Blockly.FtcJava.isFtcJavaTypePrimitive_(outputType)) {
      var argument0 = Blockly.FtcJava.valueToCode(block, 'A', Blockly.FtcJava.ORDER_MEMBER);
      var argument1 = Blockly.FtcJava.valueToCode(block, 'B', Blockly.FtcJava.ORDER_NONE);
      var code = '!' + argument0 + '.equals(' + argument1 + ')';
      return [code, Blockly.FtcJava.ORDER_LOGICAL_NOT];
    }
  }
  // Comparison operator.
  var OPERATORS = {
    'EQ': '==',
    'NEQ': '!=',
    'LT': '<',
    'LTE': '<=',
    'GT': '>',
    'GTE': '>='
  };
  var operator = OPERATORS[opFieldValue];
  var order = (operator == '==' || operator == '!=') ?
      Blockly.FtcJava.ORDER_EQUALITY : Blockly.FtcJava.ORDER_RELATIONAL;
  var argument0 = Blockly.FtcJava.valueToCode(block, 'A', order) || '0';
  var argument1 = Blockly.FtcJava.valueToCode(block, 'B', order) || '0';
  var code = argument0 + ' ' + operator + ' ' + argument1;
  return [code, order];
};

Blockly.FtcJava['logic_operation'] = function(block) {
  // Operations 'and', 'or'.
  var operator = (block.getFieldValue('OP') == 'AND') ? '&&' : '||';
  var order = (operator == '&&') ? Blockly.FtcJava.ORDER_LOGICAL_AND :
      Blockly.FtcJava.ORDER_LOGICAL_OR;
  var argument0 = Blockly.FtcJava.valueToCode(block, 'A', order);
  var argument1 = Blockly.FtcJava.valueToCode(block, 'B', order);
  if (!argument0 && !argument1) {
    // If there are no arguments, then the return value is false.
    argument0 = 'false';
    argument1 = 'false';
  } else {
    // Single missing arguments have no effect on the return value.
    var defaultArgument = (operator == '&&') ? 'true' : 'false';
    if (!argument0) {
      argument0 = defaultArgument;
    }
    if (!argument1) {
      argument1 = defaultArgument;
    }
  }
  var code = argument0 + ' ' + operator + ' ' + argument1;
  return [code, order];
};

Blockly.FtcJava['logic_negate'] = function(block) {
  // Negation.
  var order = Blockly.FtcJava.ORDER_LOGICAL_NOT;
  var argument0 = Blockly.FtcJava.valueToCode(block, 'BOOL', order) ||
      'true';
  var code = '!' + argument0;
  return [code, order];
};

Blockly.FtcJava['logic_boolean'] = function(block) {
  // Boolean values true and false.
  var code = (block.getFieldValue('BOOL') == 'TRUE') ? 'true' : 'false';
  return [code, Blockly.FtcJava.ORDER_ATOMIC];
};

Blockly.FtcJava['logic_null'] = function(block) {
  // Null data type.
  return ['null', Blockly.FtcJava.ORDER_ATOMIC];
};

Blockly.FtcJava['logic_ternary'] = function(block) {
  // Ternary operator.
  var value_if = Blockly.FtcJava.valueToCode(block, 'IF',
      Blockly.FtcJava.ORDER_CONDITIONAL) || 'false';
  var value_then = Blockly.FtcJava.valueToCode(block, 'THEN',
      Blockly.FtcJava.ORDER_CONDITIONAL) || 'null';
  var value_else = Blockly.FtcJava.valueToCode(block, 'ELSE',
      Blockly.FtcJava.ORDER_CONDITIONAL) || 'null';
  var code = value_if + ' ? ' + value_then + ' : ' + value_else;
  return [code, Blockly.FtcJava.ORDER_CONDITIONAL];
};
