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
 * @fileoverview Generating FTC Java for loop blocks.
 * @author lizlooney@google.com (Liz Looney)
 *
 * based on Generating JavaScript for loop blocks.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.FtcJava.loops');

goog.require('Blockly.FtcJava');


Blockly.FtcJava['controls_repeat_ext'] = function(block) {
  // Repeat n times.
  if (block.getField('TIMES')) {
    // Internal number.
    var repeats = String(Number(block.getFieldValue('TIMES')));
  } else {
    // External number.
    var repeats = Blockly.FtcJava.valueToCode(block, 'TIMES',
        Blockly.FtcJava.ORDER_ASSIGNMENT) || '0';
  }
  var branch = Blockly.FtcJava.statementToCode(block, 'DO');
  branch = Blockly.FtcJava.addLoopTrap(branch, block.id);
  var code = '';
  var loopVar = Blockly.FtcJava.variableDB_.getDistinctName(
      'count', Blockly.Variables.NAME_TYPE);
  var endVar = repeats;
  if (!repeats.match(/^\w+$/) && !Blockly.isNumber(repeats)) {
    var endVar = Blockly.FtcJava.variableDB_.getDistinctName(
        'end', Blockly.Variables.NAME_TYPE);
    code += 'int ' + endVar + ' = ' + repeats + ';\n';
  }
  code += 'for (int ' + loopVar + ' = 0; ' +
      loopVar + ' < ' + endVar + '; ' +
      loopVar + '++) {\n' +
      branch + '}\n';
  return code;
};

Blockly.FtcJava['controls_repeat'] =
    Blockly.FtcJava['controls_repeat_ext'];

Blockly.FtcJava['controls_whileUntil'] = function(block) {
  // Do while/until loop.
  var until = block.getFieldValue('MODE') == 'UNTIL';
  var argument0 = Blockly.FtcJava.valueToCode(block, 'BOOL',
      until ? Blockly.FtcJava.ORDER_LOGICAL_NOT :
      Blockly.FtcJava.ORDER_NONE) || 'false';
  var branch = Blockly.FtcJava.statementToCode(block, 'DO');
  branch = Blockly.FtcJava.addLoopTrap(branch, block.id);
  if (until) {
    argument0 = '!' + argument0;
  }
  return 'while (' + argument0 + ') {\n' + branch + '}\n';
};

Blockly.FtcJava['controls_for'] = function(block) {
  // For loop.
  var variable0 = Blockly.FtcJava.variableDB_.getName(
      block.getFieldValue('VAR'), Blockly.Variables.NAME_TYPE);
  var argument0 = Blockly.FtcJava.valueToCode(block, 'FROM',
      Blockly.FtcJava.ORDER_ASSIGNMENT) || '0';
  var argument1 = Blockly.FtcJava.valueToCode(block, 'TO',
      Blockly.FtcJava.ORDER_ASSIGNMENT) || '0';
  var increment = Blockly.FtcJava.valueToCode(block, 'BY',
      Blockly.FtcJava.ORDER_ASSIGNMENT) || '1';
  var branch = Blockly.FtcJava.statementToCode(block, 'DO');
  branch = Blockly.FtcJava.addLoopTrap(branch, block.id);
  var code;
  if (Blockly.isNumber(argument0) && Blockly.isNumber(argument1) &&
      Blockly.isNumber(increment)) {
    // All arguments are simple numbers.
    var up = parseFloat(argument0) <= parseFloat(argument1);
    code = 'for (' + variable0 + ' = ' + argument0 + '; ' +
        variable0 + (up ? ' <= ' : ' >= ') + argument1 + '; ' +
        variable0;
    var step = Math.abs(parseFloat(increment));
    if (step == 1) {
      code += up ? '++' : '--';
    } else {
      code += (up ? ' += ' : ' -= ') + step;
    }
    code += ') {\n' + branch + '}\n';
  } else {
    code = '';
    // Cache non-trivial values to variables to prevent repeated look-ups.
    var startVar = argument0;
    if (!argument0.match(/^\w+$/) && !Blockly.isNumber(argument0)) {
      startVar = Blockly.FtcJava.variableDB_.getDistinctName(
          variable0 + '_start', Blockly.Variables.NAME_TYPE);
      code += 'double ' + startVar + ' = ' + argument0 + ';\n';
    }
    var endVar = argument1;
    if (!argument1.match(/^\w+$/) && !Blockly.isNumber(argument1)) {
      var endVar = Blockly.FtcJava.variableDB_.getDistinctName(
          variable0 + '_end', Blockly.Variables.NAME_TYPE);
      code += 'double ' + endVar + ' = ' + argument1 + ';\n';
    }
    // Determine loop direction at start, in case one of the bounds
    // changes during loop execution.
    var incVar = Blockly.FtcJava.variableDB_.getDistinctName(
        variable0 + '_inc', Blockly.Variables.NAME_TYPE);
    code += 'double ' + incVar + ' = ';
    if (Blockly.isNumber(increment)) {
      code += Math.abs(increment) + ';\n';
    } else {
      code += 'Math.abs(' + increment + ');\n';
    }
    code += 'if (' + startVar + ' > ' + endVar + ') {\n';
    code += Blockly.FtcJava.INDENT + incVar + ' = -' + incVar + ';\n';
    code += '}\n';
    code += 'for (' + variable0 + ' = ' + startVar + '; ' +
        incVar + ' >= 0 ? ' +
        variable0 + ' <= ' + endVar + ' : ' +
        variable0 + ' >= ' + endVar + '; ' +
        variable0 + ' += ' + incVar + ') {\n' +
        branch + '}\n';
  }
  return code;
};

Blockly.FtcJava['controls_forEach'] = function(block) {
  // For each loop.
  var variable0 = Blockly.FtcJava.variableDB_.getName(
      block.getFieldValue('VAR'), Blockly.Variables.NAME_TYPE);
  var argument0 = Blockly.FtcJava.valueToCode(block, 'LIST',
      Blockly.FtcJava.ORDER_ASSIGNMENT);
  if (!argument0) {
    Blockly.FtcJava.generateImport_('Collections');
    argument0 = 'Collections.emptyList()';
  }
  var branch = Blockly.FtcJava.statementToCode(block, 'DO');
  branch = Blockly.FtcJava.addLoopTrap(branch, block.id);
  var code = '';
  // Cache non-trivial values to variables to prevent repeated look-ups.
  var listVar = argument0;
  if (!argument0.match(/^\w+$/)) {
    listVar = Blockly.FtcJava.variableDB_.getDistinctName(
        variable0 + '_list', Blockly.Variables.NAME_TYPE);
    Blockly.FtcJava.generateImport_('List');
    code += 'List ' + listVar + ' = ' + argument0 + ';\n';
  }
  code += '// TODO: Enter the type for variable named ' + variable0 + '\n' +
      'for (UNKNOWN_TYPE ' + variable0 + ' : ' + listVar + ') {\n' + branch + '}\n';
  return code;
};

Blockly.FtcJava['controls_flow_statements'] = function(block) {
  // Flow statements: continue, break.
  switch (block.getFieldValue('FLOW')) {
    case 'BREAK':
      return 'break;\n';
    case 'CONTINUE':
      return 'continue;\n';
  }
  throw 'Unknown flow statement.';
};
