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
 * @fileoverview Generating FTC Java for list blocks.
 * @author lizlooney@google.com (Liz Looney)
 *
 * based on Generating JavaScript for list blocks.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.FtcJava.lists');

goog.require('Blockly.FtcJava');


Blockly.FtcJava['lists_create_empty'] = function(block) {
  // Create an empty list.
  Blockly.FtcJava.generateImport_('ArrayList');
  return ['new ArrayList<>()', Blockly.FtcJava.ORDER_NEW];
};

Blockly.FtcJava['lists_create_with'] = function(block) {
  // Create a list with any number of elements of any type.
  var elements = new Array(block.itemCount_);
  for (var i = 0; i < block.itemCount_; i++) {
    elements[i] = Blockly.FtcJava.valueToCode(block, 'ADD' + i,
        Blockly.FtcJava.ORDER_COMMA) || 'null';
  }
  Blockly.FtcJava.generateImport_('JavaUtil');
  var code = 'JavaUtil.createListWith(' + elements.join(', ') + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['lists_repeat'] = function(block) {
  // Create a list with one element repeated.
  var element = Blockly.FtcJava.valueToCode(block, 'ITEM',
      Blockly.FtcJava.ORDER_COMMA) || 'null';
  var repeatCount = Blockly.FtcJava.valueToCode(block, 'NUM',
      Blockly.FtcJava.ORDER_COMMA) || '0';
  Blockly.FtcJava.generateImport_('JavaUtil');
  var code = 'JavaUtil.createListWithItemRepeated(' + element + ', ' + repeatCount + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['lists_length'] = function(block) {
  // List length.
  var list = Blockly.FtcJava.valueToCode(block, 'VALUE',
      Blockly.FtcJava.ORDER_FUNCTION_CALL);
  if (!list) {
    Blockly.FtcJava.generateImport_('Collections');
    list = 'Collections.emptyList()';
  }
  return [list + '.size()', Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['lists_isEmpty'] = function(block) {
  // Is the list empty?
  var list = Blockly.FtcJava.valueToCode(block, 'VALUE',
      Blockly.FtcJava.ORDER_FUNCTION_CALL);
  if (!list) {
    Blockly.FtcJava.generateImport_('Collections');
    list = 'Collections.emptyList()';
  }
  return [list + '.isEmpty()', Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['lists_indexOf'] = function(block) {
  // Find an item in the list.
  var operator = block.getFieldValue('END') == 'FIRST' ?
      'indexOf' : 'lastIndexOf';
  var item = Blockly.FtcJava.valueToCode(block, 'FIND',
      Blockly.FtcJava.ORDER_NONE) || '""';
  var list = Blockly.FtcJava.valueToCode(block, 'VALUE',
      Blockly.FtcJava.ORDER_MEMBER);
  if (!list) {
    Blockly.FtcJava.generateImport_('Collections');
    list = 'Collections.emptyList()';
  }
  var code = list + '.' + operator + '(' + item + ')';
  if (block.workspace.options.oneBasedIndex) {
    return [code + ' + 1', Blockly.FtcJava.ORDER_ADDITION];
  }
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['lists_getIndex'] = function(block) {
  // Get element at index.
  // Note: Until January 2013 this block did not have MODE or WHERE inputs.
  var list = Blockly.FtcJava.valueToCode(block, 'VALUE',
      Blockly.FtcJava.ORDER_COMMA) || 'null';
  if (!list) {
    Blockly.FtcJava.generateImport_('Collections');
    list = 'Collections.emptyList()';
  }
  var mode = block.getFieldValue('MODE') || 'GET';
  var where = block.getFieldValue('WHERE') || 'FROM_START';
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

  var remove = (mode == 'GET') ? false : true;
  Blockly.FtcJava.generateImport_('JavaUtil');
  var code = 'JavaUtil.inListGet(' + list + ', JavaUtil.AtMode.' + where + ', ' +
      at + ', ' + remove + ')';
  if (mode == 'GET' || mode == 'GET_REMOVE') {
    var order = Blockly.FtcJava.ORDER_FUNCTION_CALL;
    // Check if we need to cast the result or even unbox a primitive.
    if (block.outputConnection && block.outputConnection.targetConnection) {
      var sourceBlock = block.outputConnection.targetConnection.getSourceBlock();
      var expectedType = Blockly.FtcJava.getExpectedType_(
          sourceBlock, block.outputConnection.targetConnection);
      switch (expectedType) {
        case 'boolean':
        case 'byte':
        case 'double':
        case 'float':
        case 'long':
        case 'short':
          code = '((' + Blockly.FtcJava.makeFirstLetterUpperCase_(expectedType) + ') ' +
              code + ').' + expectedType + 'Value()';
          break;
        case 'int':
          code = '((Integer) ' + code + ').intValue()';
          break;
        case '':
          // We don't know the expected type. Not much we can do.
          break;
        default:
          code = '((' + expectedType + ') ' + code + ')';
          order = Blockly.FtcJava.ORDER_NONE;
          break;
      }
    }
    return [code, order];
  } else if (mode == 'REMOVE') {
    return code + ';\n';
  }
  throw 'Unhandled combination (lists_getIndex).';
};

Blockly.FtcJava['lists_setIndex'] = function(block) {
  // Set element at index.
  // Note: Until February 2013 this block did not have MODE or WHERE inputs.
  var list = Blockly.FtcJava.valueToCode(block, 'LIST',
      Blockly.FtcJava.ORDER_COMMA);
  if (!list) {
    Blockly.FtcJava.generateImport_('Collections');
    list = 'Collections.emptyList()';
  }
  var mode = block.getFieldValue('MODE') || 'SET';
  var where = block.getFieldValue('WHERE') || 'FROM_START';
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
  var value = Blockly.FtcJava.valueToCode(block, 'TO',
      Blockly.FtcJava.ORDER_COMMA) || 'null';

  if (mode == 'SET' || mode == 'INSERT') {
    Blockly.FtcJava.generateImport_('JavaUtil');
    var insert = (mode == 'SET') ? false : true;
    var code = 'JavaUtil.inListSet(' + list + ', JavaUtil.AtMode.' + where + ', ' +
        at + ', ' + insert + ', ' + value + ');\n';
    return code;
  }
  throw 'Unhandled combination (lists_setIndex).';
};

/**
 * Returns an expression calculating the index into a list.
 * @private
 * @param {string} listName Name of the list, used to calculate length.
 * @param {string} where The method of indexing, selected by dropdown in Blockly
 * @param {string=} opt_at The optional offset when indexing from start/end.
 * @return {string} Index expression.
 */
Blockly.FtcJava.lists.getIndex_ = function(listName, where, opt_at) {
  if (where == 'FIRST') {
    return '0';
  } else if (where == 'FROM_END') {
    return listName + '.length - 1 - ' + opt_at;
  } else if (where == 'LAST') {
    return listName + '.length - 1';
  } else {
    return opt_at;
  }
};

Blockly.FtcJava['lists_getSublist'] = function(block) {
  // Get sublist.
  var list = Blockly.FtcJava.valueToCode(block, 'LIST',
      Blockly.FtcJava.ORDER_COMMA) || 'null';
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
  var code = 'JavaUtil.inListGetSublist(' + list +
      ', JavaUtil.AtMode.' + where1 + ', ' + at1 +
      ', JavaUtil.AtMode.' + where2 + ', ' + at2 + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['lists_sort'] = function(block) {
  // Block for sorting a list.
  var list = Blockly.FtcJava.valueToCode(block, 'LIST',
      Blockly.FtcJava.ORDER_COMMA) || 'null';
  var direction = block.getFieldValue('DIRECTION') === '1' ? 'ASCENDING' : 'DESCENDING';
  var type = block.getFieldValue('TYPE');
  Blockly.FtcJava.generateImport_('JavaUtil');
  var code = 'JavaUtil.sort(' + list + ', JavaUtil.SortType.' + type +
      ', JavaUtil.SortDirection.' + direction + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['lists_split'] = function(block) {
  // Block for splitting text into a list, or joining a list into text.
  var input = Blockly.FtcJava.valueToCode(block, 'INPUT',
      Blockly.FtcJava.ORDER_COMMA) || null;
  var delimiter = Blockly.FtcJava.valueToCode(block, 'DELIM',
      Blockly.FtcJava.ORDER_COMMA) || '""';
  var mode = block.getFieldValue('MODE');
  var functionName;
  if (mode == 'SPLIT') {
    functionName = 'makeListFromText';
  } else if (mode == 'JOIN') {
    functionName = 'makeTextFromList';
  } else {
    throw 'Unknown mode: ' + mode;
  }
  Blockly.FtcJava.generateImport_('JavaUtil');
  var code = 'JavaUtil.' + functionName + '(' + input + ', ' + delimiter + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};
