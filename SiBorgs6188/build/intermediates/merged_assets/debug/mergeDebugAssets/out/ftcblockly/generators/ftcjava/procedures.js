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
 * @fileoverview Generating FTC Java for procedure blocks.
 * @author lizlooney@google.com (Liz Looney)
 *
 * based on Generating JavaScript for procedure blocks.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.FtcJava.procedures');

goog.require('Blockly.FtcJava');

Blockly.FtcJava.buildArguments_ = function(procedureBlock, functionName) {
  var args = '';
  var argDelimiter = '';
  for (var i = 0; i < procedureBlock.arguments_.length; i++) {
    var argumentName = Blockly.FtcJava.variableDB_.getName(procedureBlock.arguments_[i],
        Blockly.Variables.NAME_TYPE);
    var type = Blockly.FtcJava.procedureArgumentTypes_[functionName][i];
    if (type != '') {
      Blockly.FtcJava.generateImport_(type);
      args += argDelimiter + type + ' ' + argumentName;
      argDelimiter = ', ';
    } else {
      if (argDelimiter == '') {
        args += '\n' + Blockly.FtcJava.INDENT_CONTINUE;
      } else {
        args += ',\n' + Blockly.FtcJava.INDENT_CONTINUE;
      }
      args += '// TODO: Enter the type for argument named ' + argumentName + '\n' +
          Blockly.FtcJava.INDENT_CONTINUE + 'UNKNOWN_TYPE ' + argumentName;
      argDelimiter = ',\n' + Blockly.FtcJava.INDENT_CONTINUE;
    }
  }
  return args;
}

Blockly.FtcJava['procedures_defreturn'] = function(block) {
  // Define a procedure with a return value.
  var functionName = Blockly.FtcJava.getFunctionName_(block);
  var args = Blockly.FtcJava.buildArguments_(block, functionName);
  var returnType = Blockly.FtcJava.procedureReturnTypes_[functionName];
  var branch = Blockly.FtcJava.statementToCode(block, 'STACK');
  var returnValue = Blockly.FtcJava.valueToCode(block, 'RETURN', Blockly.FtcJava.ORDER_NONE) || '';
  if (returnValue) {
    returnValue = Blockly.FtcJava.INDENT + 'return ' + returnValue + ';\n';
  }

  var code;
  if (returnType != '') {
    code = 'private ' + returnType + ' ';
  } else {
    code = '// TODO: Enter the correct return type for function named ' + functionName + '\n' +
        'private UNKNOWN_TYPE ';
  }
  code += functionName + '(' + args + ') {\n' + branch + returnValue + '}';
  code = Blockly.FtcJava.scrub_(block, code);
  // Add % so as not to collide with helper functions in definitions list.
  Blockly.FtcJava.definitions_['%' + functionName] = code;
  return null;
};

Blockly.FtcJava['procedures_defnoreturn'] = function(block) {
  // Define a procedure with no return value.
  var functionName = Blockly.FtcJava.getFunctionName_(block);
  var args = Blockly.FtcJava.buildArguments_(block, functionName);
  var branch = Blockly.FtcJava.statementToCode(block, 'STACK');

  var code;
  // Special case for runOpmode.
  if (functionName == 'runOpMode' && args == '') {
    code = '@Override\npublic void ';
  } else {
    code = 'private void ';
  }
  code += functionName + '(' + args + ') {\n' + branch + '}';
  code = Blockly.FtcJava.scrub_(block, code);
  // Add % so as not to collide with helper functions in definitions list.
  Blockly.FtcJava.definitions_['%' + functionName] = code;
  return null;
};

Blockly.FtcJava['procedures_callreturn'] = function(block) {
  // Call a procedure with a return value.
  var functionName = Blockly.FtcJava.getFunctionName_(block);
  var args = [];
  for (var i = 0; i < block.arguments_.length; i++) {
    args[i] = Blockly.FtcJava.valueToCode(block, 'ARG' + i,
        Blockly.FtcJava.ORDER_COMMA) || 'null';
  }
  var code = functionName + '(' + args.join(', ') + ')';
  return [code, Blockly.FtcJava.ORDER_FUNCTION_CALL];
};

Blockly.FtcJava['procedures_callnoreturn'] = function(block) {
  // Call a procedure with no return value.
  var functionName = Blockly.FtcJava.getFunctionName_(block);
  var args = [];
  for (var i = 0; i < block.arguments_.length; i++) {
    args[i] = Blockly.FtcJava.valueToCode(block, 'ARG' + i,
        Blockly.FtcJava.ORDER_COMMA) || 'null';
  }
  var code = functionName + '(' + args.join(', ') + ');\n';
  return code;
};

Blockly.FtcJava['procedures_ifreturn'] = function(block) {
  // Conditionally return value from a procedure.
  var condition = Blockly.FtcJava.valueToCode(block, 'CONDITION',
      Blockly.FtcJava.ORDER_NONE) || 'false';
  var code = 'if (' + condition + ') {\n';
  if (block.hasReturnValue_) {
    var value = Blockly.FtcJava.valueToCode(block, 'VALUE',
        Blockly.FtcJava.ORDER_NONE) || 'null';
    code += Blockly.FtcJava.INDENT + 'return ' + value + ';\n';
  } else {
    code += Blockly.FtcJava.INDENT + 'return;\n';
  }
  code += '}\n';
  return code;
};
