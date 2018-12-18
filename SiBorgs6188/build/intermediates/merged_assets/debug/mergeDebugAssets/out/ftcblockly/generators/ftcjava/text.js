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
 * @fileoverview Generating FTC Java for text blocks.
 * @author lizlooney@google.com (Liz Looney)
 *
 * based on Generating JavaScript for text blocks.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.FtcJava.texts');

goog.require('Blockly.FtcJava');


Blockly.FtcJava['text'] = function(block) {
  // Text value.
  var code = Blockly.FtcJava.quote_(block.getFieldValue('TEXT'));
  return [code, Blockly.FtcJava.ORDER_ATOMIC];
};

Blockly.FtcJava['text_join'] = function(block) {
  // Create a string made up of any number of elements of any type.
  if (block.itemCount_ == 0) {
    return ['""', Blockly.FtcJava.ORDER_ATOMIC];
  }

  var order = Blockly.FtcJava.ORDER_ATOMIC;
  var code = Blockly.FtcJava.valueToCode(block, 'ADD0',
      Blockly.FtcJava.ORDER_ADDITION) || '""';
  var zeroOrOneIsString = code.startsWith('"');
  for (var i = 1; i < block.itemCount_; i++) {
    var element = Blockly.FtcJava.valueToCode(block, 'ADD' + i,
        Blockly.FtcJava.ORDER_ADDITION) || '""';
    if (i == 1 && element.startsWith('"')) {
      zeroOrOneIsString = true;
    }
    code += ' + ' + element;
    order = Blockly.FtcJava.ORDER_ADDITION;
  }
  if (!zeroOrOneIsString) {
    code = '"" + ' + code;
    order = Blockly.FtcJava.ORDER_ADDITION;
  }
  return [code, order];
};

Blockly.FtcJava['text_append'] = function(block) {
  // Append to a variable in place.
  var varName = Blockly.FtcJava.variableDB_.getName(
      block.getFieldValue('VAR'), Blockly.Variables.NAME_TYPE);
  var value = Blockly.FtcJava.valueToCode(block, 'TEXT',
      Blockly.FtcJava.ORDER_ASSIGNMENT) || '""';
  // TODO(lizlooney): What if the var is not a string?
  return varName + ' += ' + value + ';\n';
};

Blockly.FtcJava['text_length'] = function(block) {
  // String length.
  var text = Blockly.FtcJava.valueToCode(block, 'VALUE',
      Blockly.FtcJava.ORDER_MEMBER) || '""';
  return [text + '.length()', Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['text_isEmpty'] = function(block) {
  // Is the string empty?
  var text = Blockly.FtcJava.valueToCode(block, 'VALUE',
      Blockly.FtcJava.ORDER_MEMBER) || '""';
  return [text + '.isEmpty()', Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['text_indexOf'] = function(block) {
  // Search the text for a substring.
  var operator = block.getFieldValue('END') == 'FIRST' ?
      'indexOf' : 'lastIndexOf';
  var substring = Blockly.FtcJava.valueToCode(block, 'FIND',
      Blockly.FtcJava.ORDER_NONE) || '""';
  var text = Blockly.FtcJava.valueToCode(block, 'VALUE',
      Blockly.FtcJava.ORDER_MEMBER) || '""';
  var code = text + '.' + operator + '(' + substring + ')';
  // Adjust index if using one-based indices.
  if (block.workspace.options.oneBasedIndex) {
    return [code + ' + 1', Blockly.FtcJava.ORDER_ADDITION];
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['text_charAt'] = function(block) {
  // Get letter at index.
  // Note: Until January 2013 this block did not have the WHERE input.
  var where = block.getFieldValue('WHERE') || 'FROM_START';
  var text = Blockly.FtcJava.valueToCode(block, 'VALUE',
      Blockly.FtcJava.ORDER_COMMA) || '""';
  var at;
  switch (where) {
    case 'FROM_START':
    case 'FROM_END':
      at = Blockly.FtcJava.getAdjusted(block, 'AT', Blockly.FtcJava.ORDER_COMMA);
      break;
    default:
      at = '0';
      break;
  }
  Blockly.FtcJava.generateImport_('JavaUtil');
  var code = 'JavaUtil.inTextGetLetter(' + text + ', JavaUtil.AtMode.' + where + ', ' + at + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['text_getSubstring'] = function(block) {
  // Get substring.
  var text = Blockly.FtcJava.valueToCode(block, 'STRING',
      Blockly.FtcJava.ORDER_COMMA) || '""';
  var where1 = block.getFieldValue('WHERE1');
  var at1;
  switch (where1) {
    case 'FROM_START':
    case 'FROM_END':
      at1 = Blockly.FtcJava.getAdjusted(block, 'AT1', Blockly.FtcJava.ORDER_COMMA);
      break;
    default:
      at1 = '0';
      break;
  }
  var where2 = block.getFieldValue('WHERE2');
  var at2;
  switch (where2) {
    case 'FROM_START':
    case 'FROM_END':
      at2 = Blockly.FtcJava.getAdjusted(block, 'AT2', Blockly.FtcJava.ORDER_COMMA);
      break;
    default:
      at2 = '0';
      break;
  }
  Blockly.FtcJava.generateImport_('JavaUtil');
  var code = 'JavaUtil.inTextGetSubstring(' + text +
      ', JavaUtil.AtMode.' + where1 + ', ' + at1 +
      ', JavaUtil.AtMode.' + where2 + ', ' + at2 + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['text_changeCase'] = function(block) {
  // Change capitalization.
  var OPERATORS = {
    'UPPERCASE': '.toUpperCase()',
    'LOWERCASE': '.toLowerCase()',
    'TITLECASE': null
  };
  var operator = OPERATORS[block.getFieldValue('CASE')];
  var textOrder = operator ? Blockly.FtcJava.ORDER_MEMBER :
      Blockly.FtcJava.ORDER_NONE;
  var text = Blockly.FtcJava.valueToCode(block, 'TEXT',
      textOrder) || '""';
  var code;
  if (operator) {
    // Upper and lower case are functions built into Java.
    code = text + operator;
  } else {
    Blockly.FtcJava.generateImport_('JavaUtil');
    code = 'JavaUtil.toTitleCase(' + text + ')';
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['text_trim'] = function(block) {
  // Trim spaces.
  var mode = block.getFieldValue('MODE');
  var textOrder = (mode == 'BOTH') ? Blockly.FtcJava.ORDER_MEMBER :
      Blockly.FtcJava.ORDER_NONE;
  var text = Blockly.FtcJava.valueToCode(block, 'TEXT',
      textOrder) || '""';
  var code;
  if (mode == 'BOTH') {
    code = text + '.trim()';
  } else {
    Blockly.FtcJava.generateImport_('JavaUtil');
    code = 'JavaUtil.textTrim(' + text + ', JavaUtil.TrimMode.' + mode + ')';
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
