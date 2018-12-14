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
 * @fileoverview Helper functions for generating FTC Java for blocks.
 * @author lizlooney@google.com (Liz Looney)
 *
 * based onHelper functions for generating JavaScript for blocks.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.FtcJava');

goog.require('Blockly.Generator');


/**
 * FtcJava code generator.
 * @type {!Blockly.Generator}
 */
Blockly.FtcJava = new Blockly.Generator('FtcJava');

Blockly.FtcJava.INDENT_CONTINUE = '    ';

Blockly.FtcJava.CLASS_SCOPE = '<class>';

/**
 * List of illegal variable names.
 * This is not intended to be a security feature.  Blockly is 100% client-side,
 * so bypassing this list is trivial.  This is intended to prevent users from
 * accidentally clobbering a built-in object or function.
 * @private
 */
Blockly.FtcJava.addReservedWords(
    // Include all the classes that we could import (see generateImport_).
    // android.graphics
    'Color,' +
    // com.qualcomm.ftccommon
    'SoundPlayer,' +
    // com.qualcomm.hardware.modernrobotics
    'ModernRoboticsI2cCompassSensor,ModernRoboticsI2cGyro,ModernRoboticsI2cRangeSensor,' +
    // com.qualcomm.hardware.bosch
    'BNO055IMU,JustLoggingAccelerationIntegrator,' +
    // com.qualcomm.hardware.rev
    'RevBlinkinLedDriver', +
    // com.qualcomm.robotcore.eventloop
    'Autonomous,Disabled,LinearOpMode,TeleOp,' +
    // com.qualcomm.robotcore.hardware
    'AccelerationSensor,AnalogInput,AnalogOutput,CRServo,ColorSensor,CompassSensor,DcMotor,' +
    'DcMotorEx,DcMotorSimple,DigitalChannel,DistanceSensor,GyroSensor,Gyroscope,I2cAddr,' +
    'I2cAddrConfig,I2cAddressableDevice,IrSeekerSensor,LED,Light,LightSensor,MotorControlAlgorithm,' +
    'NormalizedColorSensor,NormalizedRGBA,OpticalDistanceSensor,OrientationSensor,PIDCoefficients,' +
    'PIDFCoefficients,PWMOutput,Servo,ServoController,SwitchableLight,TouchSensor,UltrasonicSensor,' +
    'VoltageSensor,' +
    // com.qualcomm.robotcore.util
    'ElapsedTime,Range,ReadWriteFile,RobotLog,' +
    // java.util
    'ArrayList,Collections,List,' +
    // org.firstinspires.ftc.robotcore.external
    'ClassFactory,JavaUtil,' +
    // org.firstinspires.ftc.robotcore.external.android
    'AndroidAccelerometer,AndroidGyroscope,AndroidOrientation,AndroidSoundPool,' +
    'AndroidTextToSpeech,' +
    // org.firstinspires.ftc.robotcore.external.hardware.camera
    'WebcamName,' +
    // org.firstinspires.ftc.robotcore.external.matrices
    'MatrixF,OpenGLMatrix,VectorF,' +
    // org.firstinspires.ftc.robotcore.external.navigation
    'Acceleration,AngleUnit,AngularVelocity,AxesOrder,AxesReference,Axis,DistanceUnit,' +
    'MagneticFlux,Orientation,Position,Quaternion,RelicRecoveryVuMark,Temperature,TempUnit,' +
    'UnnormalizedAngleUnit,Velocity,VuforiaBase,VuforiaLocalizer,VuforiaRelicRecovery,' +
    'VuforiaRoverRuckus,VuforiaTrackable,VuforiaTrackableDefaultListener,VuforiaTrackables,' +
    // org.firstinspires.ftc.robotcore.internal.system
    'AppUtil,' +
    // org.firstinspires.ftc.robotcore.external.tfod
    'Recognition,TfodBase,TfodRoverRuckus,' +
    // LinearOpMode members
    'waitForStart,idle,sleep,opModeIsActive,isStarted,' +
    'isStopRequested,init,init_loop,start,loop,stop,handleLoop,' +
    'LinearOpModeHelper,internalPostInitLoop,internalPostLoop,' +
    'waitOneFullHardwareCycle,waitForNextHardwareCycle,' +
    'OpMode,gamepad1,gamepad2,telemetry,time,requestOpModeStop,getRuntime,' +
    'resetStartTime,updateTelemetry,msStuckDetectInit,msStuckDetectInitLoop,' +
    'msStuckDetectStart,msStuckDetectLoop,msStuckDetectStop,internalPreInit,' +
    'internalOpModeServices,internalUpdateTelemetryNow,' +
    // https://docs.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html
    'abstract,assert,boolean,break,byte,case,catch,char,class,const,' +
    'continue,default,do,double,else,enum,extends,final,finally,float,' +
    'for,goto,if,implements,import,instanceof,int,interface,long,native,' +
    'new,package,private,protected,public,return,short,static,strictfp,super,' +
    'switch,synchronized,this,throw,throws,transient,try,void,volatile,while,' +
    // java.lang.*
    'AbstractMethodError,Appendable,ArithmeticException,' +
    'ArrayIndexOutOfBoundsException,ArrayStoreException,AssertionError,' +
    'AutoCloseable,Boolean,BootstrapMethodError,Byte,Character,' +
    'Character.Subset,Character.UnicodeBlock,Character.UnicodeScript,' +
    'CharSequence,Class,ClassCastException,ClassCircularityError,' +
    'ClassFormatError,ClassLoader,ClassNotFoundException,ClassValue,' +
    'Cloneable,CloneNotSupportedException,Comparable,Compiler,Deprecated,' +
    'Double,Enum,Enum,EnumConstantNotPresentException,Error,Exception,' +
    'ExceptionInInitializerError,Float,FunctionalInterface,' +
    'IllegalAccessError,IllegalAccessException,IllegalArgumentException,' +
    'IllegalMonitorStateException,IllegalStateException,' +
    'IllegalThreadStateException,IncompatibleClassChangeError,' +
    'IndexOutOfBoundsException,InheritableThreadLocal,InstantiationError,' +
    'InstantiationException,Integer,InternalError,InterruptedException,' +
    'Iterable,LinkageError,Long,Math,NegativeArraySizeException,' +
    'NoClassDefFoundError,NoSuchFieldError,NoSuchFieldException,' +
    'NoSuchMethodError,NoSuchMethodException,NullPointerException,Number,' +
    'NumberFormatException,Object,OutOfMemoryError,Override,Package,' +
    'Process,ProcessBuilder,ProcessBuilder.Redirect,' +
    'ProcessBuilder.Redirect.Type,Readable,ReflectiveOperationException,' +
    'Runnable,Runtime,RuntimeException,RuntimePermission,SafeVarargs,' +
    'SecurityException,SecurityManager,Short,StackOverflowError,' +
    'StackTraceElement,StrictMath,String,StringBuffer,StringBuilder,' +
    'StringIndexOutOfBoundsException,SuppressWarnings,System,Thread,' +
    'ThreadDeath,ThreadGroup,ThreadLocal,Thread.State,' +
    'Thread.UncaughtExceptionHandler,Throwable,TypeNotPresentException,' +
    'UnknownError,UnsatisfiedLinkError,UnsupportedClassVersionError,' +
    'UnsupportedOperationException,VerifyError,VirtualMachineError,Void');

/**
 * Order of operation ENUMs.
 * https://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html
 */
Blockly.FtcJava.ORDER_ATOMIC = 0;           // 0 "" ...
Blockly.FtcJava.ORDER_NEW = 1.1;            // new
Blockly.FtcJava.ORDER_MEMBER = 1.2;         // .
Blockly.FtcJava.ORDER_FUNCTION_CALL = 2;    // ()
Blockly.FtcJava.ORDER_POSTFIX = 3;          // expr++ expr--
Blockly.FtcJava.ORDER_PREFIX = 4;           // ++expr --expr
Blockly.FtcJava.ORDER_BITWISE_NOT = 4.1;    // ~
Blockly.FtcJava.ORDER_UNARY_PLUS = 4.2;     // +expr
Blockly.FtcJava.ORDER_UNARY_NEGATION = 4.3; // -expr
Blockly.FtcJava.ORDER_LOGICAL_NOT = 4.4;    // !
Blockly.FtcJava.ORDER_DIVISION = 5.1;       // /
Blockly.FtcJava.ORDER_MULTIPLICATION = 5.2; // *
Blockly.FtcJava.ORDER_MODULUS = 5.3;        // %
Blockly.FtcJava.ORDER_SUBTRACTION = 6.1;    // -
Blockly.FtcJava.ORDER_ADDITION = 6.2;       // +
Blockly.FtcJava.ORDER_BITWISE_SHIFT = 7;    // << >> >>>
Blockly.FtcJava.ORDER_RELATIONAL = 8;       // < <= > >=
Blockly.FtcJava.ORDER_INSTANCEOF = 8;       // instanceof
Blockly.FtcJava.ORDER_EQUALITY = 9;         // == !=
Blockly.FtcJava.ORDER_BITWISE_AND = 10;     // &
Blockly.FtcJava.ORDER_BITWISE_XOR = 11;     // ^
Blockly.FtcJava.ORDER_BITWISE_OR = 12;      // |
Blockly.FtcJava.ORDER_LOGICAL_AND = 13;     // &&
Blockly.FtcJava.ORDER_LOGICAL_OR = 14;      // ||
Blockly.FtcJava.ORDER_CONDITIONAL = 15;     // ? :
Blockly.FtcJava.ORDER_ASSIGNMENT = 16;      // = += -= *= /= %= &= ^= |= <<= >>= >>>=
Blockly.FtcJava.ORDER_COMMA = 17;           // ,
Blockly.FtcJava.ORDER_NONE = 99;            // (...)

/**
 * List of outer-inner pairings that do NOT require parentheses.
 * @type {!Array.<!Array.<number>>}
 */
Blockly.FtcJava.ORDER_OVERRIDES = [
  // (foo()).bar -> foo().bar
  // (foo())[0] -> foo()[0]
  [Blockly.FtcJava.ORDER_FUNCTION_CALL, Blockly.FtcJava.ORDER_MEMBER],
  // (foo())() -> foo()()
  [Blockly.FtcJava.ORDER_FUNCTION_CALL, Blockly.FtcJava.ORDER_FUNCTION_CALL],
  // (foo.bar).baz -> foo.bar.baz
  // (foo.bar)[0] -> foo.bar[0]
  // (foo[0]).bar -> foo[0].bar
  // (foo[0])[1] -> foo[0][1]
  [Blockly.FtcJava.ORDER_MEMBER, Blockly.FtcJava.ORDER_MEMBER],
  // (foo.bar)() -> foo.bar()
  // (foo[0])() -> foo[0]()
  [Blockly.FtcJava.ORDER_MEMBER, Blockly.FtcJava.ORDER_FUNCTION_CALL],

  // !(!foo) -> !!foo
  [Blockly.FtcJava.ORDER_LOGICAL_NOT, Blockly.FtcJava.ORDER_LOGICAL_NOT],
  // a * (b * c) -> a * b * c
  [Blockly.FtcJava.ORDER_MULTIPLICATION, Blockly.FtcJava.ORDER_MULTIPLICATION],
  // a + (b + c) -> a + b + c
  [Blockly.FtcJava.ORDER_ADDITION, Blockly.FtcJava.ORDER_ADDITION],
  // a && (b && c) -> a && b && c
  [Blockly.FtcJava.ORDER_LOGICAL_AND, Blockly.FtcJava.ORDER_LOGICAL_AND],
  // a || (b || c) -> a || b || c
  [Blockly.FtcJava.ORDER_LOGICAL_OR, Blockly.FtcJava.ORDER_LOGICAL_OR]
];

/**
 * Initialise the database of variable names.
 * @param {!Blockly.Workspace} workspace Workspace to generate code from.
 */
Blockly.FtcJava.init = function(workspace) {
  // Create a dictionary of definitions to be printed before the code.
  Blockly.FtcJava.definitions_ = Object.create(null);
  // Create a dictionary mapping desired function names in definitions_
  // to actual function names (to avoid collisions with user functions).
  Blockly.FtcJava.functionNames_ = Object.create(null);

  if (!Blockly.FtcJava.variableDB_) {
    Blockly.FtcJava.variableDB_ = new Blockly.Names(Blockly.FtcJava.RESERVED_WORDS_);
  } else {
    Blockly.FtcJava.variableDB_.reset();
  }

  Blockly.FtcJava.variableDeclarationsByScope_ = Object.create(null);

  // Figure out type and scope of each variable, type of each argument, and return type of each function.
  Blockly.FtcJava.procedureDefBlocks_ = Object.create(null);
  Blockly.FtcJava.blocksInProcedures_ = Object.create(null);
  Blockly.FtcJava.procedureArgumentGetterBlocks_ = Object.create(null);
  Blockly.FtcJava.procedureArgumentSetterBlocks_ = Object.create(null);
  Blockly.FtcJava.procedureCallBlocks_ = Object.create(null);
  Blockly.FtcJava.procedureArgumentNames_ = Object.create(null);
  Blockly.FtcJava.procedureArgumentTypes_ = Object.create(null);
  Blockly.FtcJava.procedureReturnTypes_ = Object.create(null);
  Blockly.FtcJava.variableScopes_ = Object.create(null);
  Blockly.FtcJava.variableGetterBlocks_ = Object.create(null);
  Blockly.FtcJava.variableSetterBlocks_ = Object.create(null);
  Blockly.FtcJava.variableTypes_ = Object.create(null);

  var allBlocks = workspace.getAllBlocks();
  var countUnknownTypes = 0;

  var topBlocks = workspace.getTopBlocks(false);
  for (var iBlock = 0, block; block = topBlocks[iBlock]; iBlock++) {
    if (Blockly.FtcJava.isFunctionDefinitionBlock_(block)) {
      var procedureBlock = block;
      var functionName = Blockly.FtcJava.getFunctionName_(procedureBlock);
      Blockly.FtcJava.procedureDefBlocks_[functionName] = procedureBlock;

      var argumentNames = [];
      var argumentTypes = [];
      var argumentGetterBlocks = [];
      var argumentSetterBlocks = [];
      for (var iArgument = 0; iArgument < procedureBlock.arguments_.length; iArgument++) {
        var argumentName = Blockly.FtcJava.variableDB_.getName(procedureBlock.arguments_[iArgument],
            Blockly.Variables.NAME_TYPE);
        argumentNames.push(argumentName);
        argumentTypes.push('');
        countUnknownTypes++;
        argumentGetterBlocks.push([]);
        argumentSetterBlocks.push([]);
      }
      Blockly.FtcJava.procedureArgumentNames_[functionName] = argumentNames;
      Blockly.FtcJava.procedureArgumentTypes_[functionName] = argumentTypes;

      var blocksInProcedure = [];
      var callBlocks = [];
      for (var iOtherBlock = 0, otherBlock; otherBlock = allBlocks[iOtherBlock]; iOtherBlock++) {
        if (otherBlock.getRootBlock() == procedureBlock) {
          blocksInProcedure.push(otherBlock);
          if (otherBlock.type == 'variables_get') {
            var index = Blockly.FtcJava.getArgumentIndex_(otherBlock.getFieldValue('VAR'), procedureBlock);
            if (index != -1) {
              argumentGetterBlocks[index].push(otherBlock);
            }
          } else if (otherBlock.type == 'variables_set') {
            var index = Blockly.FtcJava.getArgumentIndex_(otherBlock.getFieldValue('VAR'), procedureBlock);
            if (index != -1) {
              argumentSetterBlocks[index].push(otherBlock);
            }
          }
        }
        if (otherBlock.type == 'procedures_callreturn' || otherBlock.type == 'procedures_callnoreturn') {
          if (Blockly.FtcJava.getFunctionName_(otherBlock) == functionName) {
            callBlocks.push(otherBlock);
          }
        }
      }
      Blockly.FtcJava.procedureArgumentGetterBlocks_[functionName] = argumentGetterBlocks;
      Blockly.FtcJava.procedureArgumentSetterBlocks_[functionName] = argumentSetterBlocks;
      Blockly.FtcJava.blocksInProcedures_[functionName] = blocksInProcedure;
      Blockly.FtcJava.procedureCallBlocks_[functionName] = callBlocks;
      if (procedureBlock.type == 'procedures_defreturn') {
        Blockly.FtcJava.procedureReturnTypes_[functionName] = '';
        countUnknownTypes++;
      }
    }
  }

  var variables = workspace.getAllVariables();
  if (variables.length) {
    for (var iVariable = 0; iVariable < variables.length; iVariable++) {
      var variable = variables[iVariable];
      var variableName = Blockly.FtcJava.variableDB_.getName(variable.name, Blockly.Variables.NAME_TYPE);
      var functionNames = [];
      var variableGetterBlocks = [];
      var variableSetterBlocks = [];
      for (var iBlock = 0, block; block = allBlocks[iBlock]; iBlock++) {
        var variableNames = block.getVars();
        if (variableNames.indexOf(variable.name) == -1) {
          continue;
        }
        // Look at the function that contains this block.
        var rootBlock = block.getRootBlock();
        // Ignore the block if the rootBlock isn't a function definition.
        if (Blockly.FtcJava.isFunctionDefinitionBlock_(rootBlock)) {
          // Check whether the variable is an argument of the function.
          if (rootBlock.arguments_.indexOf(variable.name) > -1) {
            // Skip procedure arguments.
            continue;
          }
          // Check whether the variable is a for-each loop variable.
          var isForEachLoopVariable = false;
          var b = block;
          do {
            if (b.type == 'controls_forEach') {
              if (b.getFieldValue('VAR') == variable.name) {
                isForEachLoopVariable = true;
                break;
              }
            }
            b = b.getParent();
          } while (b);
          if (isForEachLoopVariable) {
            // Skip procedure arguments.
            continue;
          }


          var functionName = Blockly.FtcJava.getFunctionName_(rootBlock);
          if (functionNames.indexOf(functionName) == -1) {
            functionNames.push(functionName);
          }
          if (block.type == 'variables_get') {
            variableGetterBlocks.push(block);
          } else if (block.type == 'variables_set') {
            variableSetterBlocks.push(block);
          }
        }
      }
      if (functionNames.length == 0) {
        // This variable is never used, or it's only used as a procedure argument.
        continue;
      }
      Blockly.FtcJava.variableScopes_[variableName] = (functionNames.length == 1)
          ? functionNames[0] : Blockly.FtcJava.CLASS_SCOPE;
      Blockly.FtcJava.variableGetterBlocks_[variableName] = variableGetterBlocks;
      Blockly.FtcJava.variableSetterBlocks_[variableName] = variableSetterBlocks;
      var variableType = variables[iVariable].type;
      Blockly.FtcJava.variableTypes_[variableName] = variableType;
      if (variableType == '') {
        countUnknownTypes++;
      }
    }
  }

  while (countUnknownTypes > 0) {
    var typesChanged = false;
    for (var functionName in Blockly.FtcJava.procedureDefBlocks_) {
      var procedureBlock = Blockly.FtcJava.procedureDefBlocks_[functionName];
      var argumentTypes = Blockly.FtcJava.procedureArgumentTypes_[functionName];
      for (var iArgument = 0; iArgument < procedureBlock.arguments_.length; iArgument++) {
        var argumentType = argumentTypes[iArgument];
        if (argumentType == '') {
          argumentType = Blockly.FtcJava.determineArgumentType_(
              iArgument,
              Blockly.FtcJava.procedureArgumentGetterBlocks_[functionName][iArgument],
              Blockly.FtcJava.procedureArgumentSetterBlocks_[functionName][iArgument],
              Blockly.FtcJava.procedureCallBlocks_[functionName]);
          if (argumentType != '') {
            argumentTypes[iArgument] = argumentType;
            countUnknownTypes--;
            typesChanged = true;
          }
        }
      }

      if (functionName in Blockly.FtcJava.procedureReturnTypes_) {
        var returnType = Blockly.FtcJava.procedureReturnTypes_[functionName];
        if (returnType == '') {
          returnType = Blockly.FtcJava.determineProcedureReturnType_(
              procedureBlock,
              Blockly.FtcJava.blocksInProcedures_[functionName],
              Blockly.FtcJava.procedureCallBlocks_[functionName]);
          if (returnType != '') {
            Blockly.FtcJava.procedureReturnTypes_[functionName] = returnType;
            countUnknownTypes--;
            typesChanged = true;
          }
        }
      }
    }
    for (var variableName in Blockly.FtcJava.variableTypes_) {
      var variableType = Blockly.FtcJava.variableTypes_[variableName];
      if (variableType == '') {
        variableType = Blockly.FtcJava.determineVariableType_(
            Blockly.FtcJava.variableGetterBlocks_[variableName],
            Blockly.FtcJava.variableSetterBlocks_[variableName]);
        if (variableType != '') {
          Blockly.FtcJava.variableTypes_[variableName] = variableType;
          countUnknownTypes--;
          typesChanged = true;
        }
      }
    }
    if (!typesChanged) {
      break;
    }
  }
  if (countUnknownTypes > 0) {
    console.log('Unable to determine ' + countUnknownTypes + ' types');
  }

  for (var variableName in Blockly.FtcJava.variableTypes_) {
    Blockly.FtcJava.addVariableDefinition_(variableName,
        Blockly.FtcJava.variableTypes_[variableName],
        Blockly.FtcJava.variableScopes_[variableName]);
  }

  Blockly.FtcJava.generateImport_('LinearOpMode');
};

Blockly.FtcJava.addVariableDefinition_ = function(variableName, variableType, scope) {
  var variableDefinition;
  if (variableType == '') {
    variableDefinition =
        Blockly.FtcJava.INDENT + '// TODO: Enter the type for variable named ' + variableName + '\n' +
        Blockly.FtcJava.INDENT + 'UNKNOWN_TYPE ' + variableName + ';';
  } else {
    Blockly.FtcJava.generateImport_(variableType);
    variableDefinition = Blockly.FtcJava.INDENT + variableType + ' ' + variableName + ';';
  }
  var variableDefinitions;
  if (scope in Blockly.FtcJava.variableDeclarationsByScope_) {
    variableDefinitions = Blockly.FtcJava.variableDeclarationsByScope_[scope];
  } else {
    variableDefinitions = [];
    Blockly.FtcJava.variableDeclarationsByScope_[scope] = variableDefinitions;
  }
  variableDefinitions.push(variableDefinition);
};

Blockly.FtcJava.determineVariableType_ = function(getterBlocks, setterBlocks) {
  var typesExpectedFromGetters = [];
  var typesUsedInSetters = [];
  Blockly.FtcJava.collectVariableTypes_(getterBlocks, setterBlocks, typesExpectedFromGetters, typesUsedInSetters);
  return Blockly.FtcJava.chooseBestType_(typesUsedInSetters, typesExpectedFromGetters);
};

Blockly.FtcJava.determineArgumentType_ = function(indexOfArg, getterBlocks, setterBlocks, callBlocks) {
  var typesUsedInSetters = [];
  var typesExpectedFromGetters = [];

  Blockly.FtcJava.collectVariableTypes_(getterBlocks, setterBlocks, typesExpectedFromGetters, typesUsedInSetters);

  // Look at 'procedures_callreturn' or 'procedures_callnoreturn' blocks.
  for (var iCallBlock = 0, callBlock; callBlock = callBlocks[iCallBlock]; iCallBlock++) {
    // Look at the block that is plugged into the argument socket.
    var outputType = Blockly.FtcJava.getOutputType_(callBlock.getInputTargetBlock('ARG' + indexOfArg));
    if (outputType != '') {
      typesUsedInSetters.push(outputType);
    }
  }

  return Blockly.FtcJava.chooseBestType_(typesUsedInSetters, typesExpectedFromGetters);
}

Blockly.FtcJava.collectVariableTypes_ = function(getterBlocks, setterBlocks, typesExpectedFromGetters, typesUsedInSetters) {
  for (var iGetterBlock = 0, block; block = getterBlocks[iGetterBlock]; iGetterBlock++) {
    // Look at the block that this getter is plugged into.
    if (block.outputConnection && block.outputConnection.targetConnection) {
      var sourceBlock = block.outputConnection.targetConnection.getSourceBlock();
      var expectedType = Blockly.FtcJava.getExpectedType_(
          sourceBlock, block.outputConnection.targetConnection);
      if (expectedType != '') {
        typesExpectedFromGetters.push(expectedType);
      }
    }
  }
  for (var iSetterBlock = 0, block; block = setterBlocks[iSetterBlock]; iSetterBlock++) {
    // Look at the block that is plugged into this setter's VALUE socket.
    var outputType = Blockly.FtcJava.getOutputType_(block.getInputTargetBlock('VALUE'));
    if (outputType != '') {
      typesUsedInSetters.push(outputType);
    }
  }
};

Blockly.FtcJava.determineProcedureReturnType_ = function(procedureBlock, blocksInProcedure, callBlocks) {
  var typesReturned = [];
  var typesExpectedByCallers = [];

  // Look at the block that is plugged into the RETURN socket.
  var returnBlock = procedureBlock.getInputTargetBlock('RETURN');
  var outputType = Blockly.FtcJava.getOutputType_(returnBlock);
  if (outputType != '') {
    typesReturned.push(outputType);
  }

  // Look at VALUE socket of 'procedures_ifreturn' blocks.
  for (var iBlock = 0, block; block = blocksInProcedure[iBlock]; iBlock++) {
    if (block.type != 'procedures_ifreturn') {
      continue;
    }
    var outputType = Blockly.FtcJava.getOutputType_(block.getInputTargetBlock('VALUE'));
    if (outputType != '') {
      typesReturned.push(outputType);
    }
  }

  // Look at 'procedures_callreturn' blocks.
  for (var iCallBlock = 0, callBlock; callBlock = callBlocks[iCallBlock]; iCallBlock++) {
    // Look at the block that this call block is plugged into.
    if (callBlock.outputConnection && callBlock.outputConnection.targetConnection) {
      var sourceBlock = callBlock.outputConnection.targetConnection.getSourceBlock();
      var expectedType = Blockly.FtcJava.getExpectedType_(
          sourceBlock, callBlock.outputConnection.targetConnection);
      if (expectedType != '') {
        typesExpectedByCallers.push(expectedType);
      }
    }
  }

  return Blockly.FtcJava.chooseBestType_(typesReturned, typesExpectedByCallers);
}

Blockly.FtcJava.chooseBestType_ = function(types, expectedTypes) {
  var bestType = '';

  // Check whether we only have one type.
  var uniqueTypes = Object.create(null);
  for (var iType = 0; iType < types.length; iType++) {
    uniqueTypes[types[iType]] = true;
  }
  for (var iExpectedType = 0; iExpectedType < expectedTypes.length; iExpectedType++) {
    uniqueTypes[expectedTypes[iExpectedType]] = true;
  }
  if (Object.keys(uniqueTypes).length == 1) {
    bestType = Object.keys(uniqueTypes)[0];
  } else {
    // Use the type that is the least specific type (has the lowest value from getTypeValue_) that
    // fits in all expected types. However, we ignore Number when looking at types used.
    var typesByValue = [];
    for (var iType = 0; iType < types.length; iType++) {
      var type = types[iType];
      if (type != 'Number') {
        var value = Blockly.FtcJava.getTypeValue_(type);
        typesByValue[value] = type;
      }
    }
    var keys = Object.keys(typesByValue);
    for (var iKey = 0; iKey < keys.length; iKey++) {
      var type = typesByValue[keys[iKey]];
      var fitsAll = true;
      for (var iExpectedType = 0; iExpectedType < expectedTypes.length; iExpectedType++) {
        var expectedType = expectedTypes[iExpectedType];
        if (!Blockly.FtcJava.checkTypes_(type, expectedType)) {
          fitsAll = false;
          break;
        }
      }
      if (fitsAll) {
        bestType = type;
        break;
      }
    }
    if (bestType == '') {
      // Use the most specific expected type.
      var expectedTypesByValue = [];
      for (var iExpectedType = 0; iExpectedType < expectedTypes.length; iExpectedType++) {
        var expectedType = expectedTypes[iExpectedType];
        var value = Blockly.FtcJava.getTypeValue_(expectedType);
        expectedTypesByValue[value] = expectedType;
      }
      keys = Object.keys(expectedTypesByValue);
      if (keys.length > 0) {
        bestType = expectedTypesByValue[keys[keys.length - 1]];
      }
    }
  }

  uniqueTypes = null;

  return Blockly.FtcJava.convertBlocksTypeToFtcJavaType_(bestType);
};

Blockly.FtcJava.checkTypes_ = function(type, expectedType) {
  if (type == expectedType) {
    return true;
  }
  switch (expectedType) {
    case 'Number':
    case 'double':
      return type == 'Number' || type == 'double' || type == 'float' || type == 'long' || type == 'int' || type == 'short' || type == 'byte';
    case 'float':
      return type == 'float' || type == 'long' || type == 'int' || type == 'short' || type == 'byte';
    case 'long':
      return type == 'long' || type == 'int' || type == 'short' || type == 'byte';
    case 'int':
      return type == 'int' || type == 'short' || type == 'byte';
    case 'short':
      return type == 'short' || type == 'byte';

    case 'MatrixF':
      return type == 'MatrixF' || type == 'OpenGLMatrix';
  }
  return false;
};

Blockly.FtcJava.getOutputType_ = function(block) {
  if (block) {
    // If the block is a variable getter, see if we already know the type of that variable.
    if (block.type == 'variables_get') {
      var variableName = Blockly.FtcJava.getVariableName_(block);
      var variableType = Blockly.FtcJava.variableTypes_[variableName];
      if (variableType != '') {
        return variableType;
      }
    }

    // If the block is a procedures_callreturn, see if we already know the return type of that procedure.
    if (block.type == 'procedures_callreturn') {
      var functionName = Blockly.FtcJava.getFunctionName_(block);
      var returnType = Blockly.FtcJava.procedureReturnTypes_[functionName];
      if (returnType != '') {
        return returnType;
      }
    }

    // If it's an FTC block, ask it what the output type is.
    if (typeof block.getFtcJavaOutputType == 'function') {
      var outputType = block.getFtcJavaOutputType();
      if (outputType != '') {
        return outputType;
      }
    }

    // Look at the connection check.
    if (block.outputConnection && block.outputConnection.check_ &&
        block.outputConnection.check_.length == 1) {
      return Blockly.FtcJava.convertBlocksTypeToFtcJavaType_(block.outputConnection.check_[0]);
    }
  }

  return '';
};

Blockly.FtcJava.getExpectedType_ = function(block, connection) {
  // Blockly allows Array or String to be used in lists_isEmpty, lists_length, text_isEmpty, and
  // text_length. That's just lovely.
  if (block.type == 'lists_isEmpty' || block.type == 'lists_length') {
    return Blockly.FtcJava.convertBlocksTypeToFtcJavaType_('Array');
  } else if (block.type == 'text_isEmpty' || block.type == 'text_length') {
    return Blockly.FtcJava.convertBlocksTypeToFtcJavaType_('String');
  }

  // If the block is a variable setter, see if we already know the type of that variable.
  if (block.type == 'variables_set') {
    var variableName = Blockly.FtcJava.getVariableName_(block);
    var variableType = Blockly.FtcJava.variableTypes_[variableName];
    if (variableType != '') {
      return variableType;
    }
  }

  // If the block is a procedure call, see if we already know the type of the argument.
  if (block.type == 'procedures_callreturn' || block.type == 'procedures_callnoreturn') {
    // Look at the block that is plugged into the argument socket.
    for (var iInput = 0, input; input = block.inputList[iInput]; iInput++) {
      if (input.connection == connection) {
        var functionName = Blockly.FtcJava.getFunctionName_(block);
        var procedureBlock = Blockly.FtcJava.procedureDefBlocks_[functionName];
        var index = Blockly.FtcJava.getArgumentIndex_(input.name, procedureBlock);
        if (index != -1) {
          var argumentType = Blockly.FtcJava.procedureArgumentTypes_[functionName][index];
          if (argumentType != '') {
            return argumentType;
          }
        }
      }
    }
  }

  // If the block is a procedure return, see if we already know the return type of the procedure.
  if (block.type == 'procedures_defreturn') {
    var functionName = Blockly.FtcJava.getFunctionName_(block);
    var returnType = Blockly.FtcJava.procedureReturnTypes_[functionName];
    if (returnType != '') {
      return returnType;
    }
  } else if (block.type == 'procedures_ifreturn') {
    var rootBlock = block.getRootBlock();
    if (rootBlock.type == 'procedures_defreturn') {
      var functionName = Blockly.FtcJava.getFunctionName_(rootBlock);
      var returnType = Blockly.FtcJava.procedureReturnTypes_[functionName];
      if (returnType != '') {
        return returnType;
      }
    }
  }

  // If it's an FTC block, ask it what the input type is.
  if (typeof block.getFtcJavaInputType == 'function') {
    for (var iInput = 0, input; input = block.inputList[iInput]; iInput++) {
      if (input.connection == connection) {
        var inputType = block.getFtcJavaInputType(input.name);
        if (inputType != '') {
          return inputType;
        }
      }
    }
  }

  // Look at the connection check types.
  if (connection.check_) {
    if (connection.check_.length == 1) {
      return Blockly.FtcJava.convertBlocksTypeToFtcJavaType_(connection.check_[0]);
    } else if (connection.check_.length > 1) {
      // Use the least specific type, which is the one with the lowest value.
      var lowestValueValue = 1000;
      var leastSpecificType = '';
      for (var iCheck = 0; iCheck < connection.check_.length; iCheck++) {
        var type = Blockly.FtcJava.convertBlocksTypeToFtcJavaType_(connection.check_[iCheck]);
        var value = Blockly.FtcJava.getTypeValue_(type);
        if (value < lowestValueValue) {
          leastSpecificType = type;
        }
      }
      return leastSpecificType;
    }
  }

  return '';
};

Blockly.FtcJava.getTypeValue_ = function(type) {
  switch (type) {
    case 'Number':
    case 'double':
      return 0;
    case 'float':
      return 1;
    case 'long':
      return 2;
    case 'int':
      return 3;
    case 'short':
      return 4;
    case 'byte':
      return 5;

    case 'MatrixF':
      return 10;
    case 'OpenGLMatrix':
      return 11;
  }
  return 100;
};

Blockly.FtcJava.getVariableName_ = function(block) {
  if (block.type != 'variables_set' && block.type != 'variables_get') {
    throw 'Unexpected block in getVariableName_ - should be variables_set or variables_get';
  }
  return Blockly.FtcJava.variableDB_.getName(block.getFieldValue('VAR'), Blockly.Variables.NAME_TYPE);
};

Blockly.FtcJava.getArgumentIndex_ = function(rawVariableName, procedureBlock) {
  if (!Blockly.FtcJava.isFunctionDefinitionBlock_(procedureBlock)) {
    throw 'Unexpected procedureBlock in getArgumentIndex_ - should be procedures_defreturn or procedures_defnoreturn';
  }
  for (var iArgument = 0; iArgument < procedureBlock.arguments_.length; iArgument++) {
    if (rawVariableName == procedureBlock.arguments_[iArgument]) {
      return iArgument;
    }
  }
  return -1;
};

Blockly.FtcJava.isFunctionDefinitionBlock_ = function(block) {
  return block.type == 'procedures_defreturn'
      || block.type == 'procedures_defnoreturn';
}

Blockly.FtcJava.getFunctionName_ = function(block) {
  if (!Blockly.FtcJava.isFunctionDefinitionBlock_(block) &&
      block.type != 'procedures_callreturn' &&
      block.type != 'procedures_callnoreturn') {
    throw 'Unexpected block in getFunctionName_ - should be procedures_defnoreturn, procedures_defreturn, procedures_callreturn, or procedures_callnoreturn';
  }
  return Blockly.FtcJava.variableDB_.getName(block.getFieldValue('NAME'), Blockly.Procedures.NAME_TYPE);
};

/**
 * Prepend the generated code with the variable definitions.
 * @param {string} code Generated code.
 * @return {string} Completed code.
 */
Blockly.FtcJava.finish = function(code) {
  // FYI: We ignore code, which is not within a procedure definition.

  var packageName = 'org.firstinspires.ftc.teamcode';
  var completedCode =
      'package ' + packageName + ';\n\n';

  // The annotations for the class must be determined now, because getClassAnnotationsForFtcJava_()
  // will call generateImport_() for the annotation classes that are used.
  var annotations = Blockly.FtcJava.getClassAnnotationsForFtcJava_();
  // Convert the definitions dictionary into several lists.
  var imports = [];
  var fieldDeclarations = [];
  var fieldAssignments = [];
  var closingCode = [];
  var definitionsByName = Object.create(null);
  for (var name in Blockly.FtcJava.definitions_) {
    var def = Blockly.FtcJava.definitions_[name];
    if (name.match(/^import_/)) {
      imports.push(def);
    } else if (name.match(/^declare_field_/)) {
      fieldDeclarations.push(Blockly.FtcJava.prefixLines(def, Blockly.FtcJava.INDENT));
    } else if (name.match(/^assign_field_/)) {
      fieldAssignments.push(Blockly.FtcJava.prefixLines(def, Blockly.FtcJava.INDENT));
    } else if (name.match(/^closing_code_/)) {
      closingCode.push(Blockly.FtcJava.prefixLines(def, Blockly.FtcJava.INDENT));
    } else {
      definitionsByName[name] = def;
    }
  }

  if (imports.length) {
    imports.sort();
    completedCode += imports.join('\n') + '\n\n';
  }

  completedCode += annotations +
      'public class ' + Blockly.FtcJava.getClassNameForFtcJava_() + ' extends LinearOpMode {\n\n';
  if (fieldDeclarations.length) {
    completedCode += fieldDeclarations.join('\n') + '\n\n';
  }
  if (Blockly.FtcJava.CLASS_SCOPE in Blockly.FtcJava.variableDeclarationsByScope_) {
    // These variables are used in more than one method, so they are declared as fields.
    var moreFieldDeclarations = Blockly.FtcJava.variableDeclarationsByScope_[Blockly.FtcJava.CLASS_SCOPE];
    completedCode += moreFieldDeclarations.join('\n') + '\n\n';
  }

  var definitions = [];
  for (var name in definitionsByName) {
    var def = definitionsByName[name];
    if (name.startsWith('%')) {
      var functionName = name.substring(1);
      if (functionName == 'runOpMode') {
        if (fieldAssignments.length) {
          // Insert the field assignments immediately after after the opening brace.
          var position = def.indexOf('{');
          if (position > -1) {
            position++; // length of '{'
            def = def.slice(0, position) + '\n' + fieldAssignments.join('\n') + '\n' +
                def.slice(position);
          }
        }
        if (closingCode.length) {
          // Insert the closing code.
          position = def.lastIndexOf('}');
          // Find the newline before the last }.
          while (def.charAt(position) != '\n') {
            position--;
          }
          def = def.slice(0, position) + '\n\n' + closingCode.join('\n') +
              def.slice(position);
        }
      }
      if (functionName in Blockly.FtcJava.variableDeclarationsByScope_) {
        // There are local variables for this function.
        var variableDeclarations = Blockly.FtcJava.variableDeclarationsByScope_[functionName];
        // Insert the variables immediately after the opening brace, before the field assignments
        // if any were inserted above.
        var position = def.indexOf('{');
        if (position > -1) {
          position++; // length of '{'
          def = def.slice(0, position) + '\n' + variableDeclarations.join('\n') + '\n' +
              def.slice(position);
        } else {
          throw 'function ' + functionName + ' has no opening brace?';
        }
      }
    }
    definitions.push(Blockly.FtcJava.prefixLines(def, Blockly.FtcJava.INDENT));
  }
  completedCode += definitions.join('\n\n') + '\n';
  completedCode += '}\n';

  // Clean up temporary data.
  delete Blockly.FtcJava.definitions_;
  delete Blockly.FtcJava.functionNames_;
  delete Blockly.FtcJava.variableDeclarationsByScope_;
  delete Blockly.FtcJava.procedureDefBlocks_;
  delete Blockly.FtcJava.blocksInProcedures_;
  delete Blockly.FtcJava.procedureCallBlocks_;
  delete Blockly.FtcJava.procedureArgumentNames_;
  delete Blockly.FtcJava.procedureArgumentTypes_;
  delete Blockly.FtcJava.procedureArgumentGetterBlocks_;
  delete Blockly.FtcJava.procedureArgumentSetterBlocks_;
  delete Blockly.FtcJava.procedureReturnTypes_;
  delete Blockly.FtcJava.variableScopes_;
  delete Blockly.FtcJava.variableGetterBlocks_;
  delete Blockly.FtcJava.variableSetterBlocks_;
  delete Blockly.FtcJava.variableTypes_;
  Blockly.FtcJava.variableDB_.reset();

  return completedCode;
};

/**
 * Naked values are top-level blocks with outputs that aren't plugged into
 * anything.  A trailing semicolon is needed to make this legal.
 * @param {string} line Line of generated code.
 * @return {string} Legal line of code.
 */
Blockly.FtcJava.scrubNakedValue = function(line) {
  return line + ';\n';
};

/**
 * Encode a string as a properly escaped Java string, complete with
 * quotes.
 * @param {string} string Text to encode.
 * @return {string} Java string.
 * @private
 */
Blockly.FtcJava.quote_ = function(string) {
  string = string.replace(/\\/g, '\\\\')
                 .replace(/\n/g, '\\\n')
                 .replace(/"/g, '\\\"');
  return '"' + string + '"';
};

/**
 * Common tasks for generating Java from blocks.
 * Handles comments for the specified block and any connected value blocks.
 * Calls any statements following this block.
 * @param {!Blockly.Block} block The current block.
 * @param {string} code The Java code created for this block.
 * @return {string} Java code with comments and subsequent blocks added.
 * @private
 */
Blockly.FtcJava.scrub_ = function(block, code) {
  var commentCode = '';
  // Only collect comments for blocks that aren't inline.
  if (!block.outputConnection || !block.outputConnection.targetConnection) {
    // Collect comment for this block.
    var comment = block.getCommentText();
    comment = Blockly.utils.wrap(comment, Blockly.FtcJava.COMMENT_WRAP - 3);
    if (comment) {
      if (block.getProcedureDef) {
        // Use a comment block for function comments.
        commentCode += '/**\n' +
                       Blockly.FtcJava.prefixLines(comment + '\n', ' * ') +
                       ' */\n';
      } else {
        commentCode += Blockly.FtcJava.prefixLines(comment + '\n', '// ');
      }
    }
    // Collect comments for all value arguments.
    // Don't collect comments for nested statements.
    for (var i = 0; i < block.inputList.length; i++) {
      if (block.inputList[i].type == Blockly.INPUT_VALUE) {
        var childBlock = block.inputList[i].connection.targetBlock();
        if (childBlock) {
          var comment = Blockly.FtcJava.allNestedComments(childBlock);
          if (comment) {
            commentCode += Blockly.FtcJava.prefixLines(comment, '// ');
          }
        }
      }
    }
  }
  var nextBlock = block.nextConnection && block.nextConnection.targetBlock();
  var nextCode = Blockly.FtcJava.blockToCode(nextBlock);
  return commentCode + code + nextCode;
};

/**
 * Gets a property and adjusts the value while taking into account indexing.
 * @param {!Blockly.Block} block The block.
 * @param {string} atId The property ID of the element to get.
 * @param {number=} opt_order The highest order acting on this value.
 * @return {string|number}
 */
Blockly.FtcJava.getAdjusted = function(block, atId, opt_order) {
  var order = opt_order || Blockly.FtcJava.ORDER_NONE;
  var delta = block.workspace.options.oneBasedIndex ? -1 : 0;
  var defaultAtIndex = block.workspace.options.oneBasedIndex ? '1' : '0';
  var at;
  if (delta < 0) {
    at = Blockly.FtcJava.valueToCode(block, atId, Blockly.FtcJava.ORDER_SUBTRACTION) || defaultAtIndex;
  } else {
    at = Blockly.FtcJava.valueToCode(block, atId, order) || defaultAtIndex;
  }

  if (Blockly.isNumber(at)) {
    // If the index is a naked number, adjust it right now.
    at = parseFloat(at) + delta;
  } else {
    // If the index is dynamic, adjust it in code.
    if (delta < 0) {
      at = at + ' - ' + -delta;
      var innerOrder = Blockly.FtcJava.ORDER_SUBTRACTION;
    }
    innerOrder = Math.floor(innerOrder);
    order = Math.floor(order);
    if (innerOrder && order >= innerOrder) {
      at = '(' + at + ')';
    }
  }
  return at;
};

Blockly.FtcJava.setClassNameForFtcJava_ = function(className) {
  Blockly.FtcJava.classNameForFtcJava_ = className;
}

Blockly.FtcJava.getClassNameForFtcJava_ = function() {
  return (Blockly.FtcJava.classNameForFtcJava_ || 'MyLinearOpMode');
};

Blockly.FtcJava.getClassAnnotationsForFtcJava_ = function() {
  var annotations = '';
  var flavor;
  var flavorSelect = document.getElementById('flavor');
  if (flavorSelect) {
    flavor = flavorSelect.options[flavorSelect.selectedIndex].value;
  } else {
    flavor = 'TELEOP';
  }
  if (flavor == 'AUTONOMOUS') {
    Blockly.FtcJava.generateImport_('Autonomous');
    annotations += '@Autonomous(';
  } else {
    Blockly.FtcJava.generateImport_('TeleOp');
    annotations += '@TeleOp(';
  }
  annotations += 'name = "' + Blockly.FtcJava.getOpModeNameForFtcJava_() + '"';

  var group;
  var groupTextInput = document.getElementById('group');
  if (groupTextInput) {
    group = groupTextInput.value;
  } else {
    group = '';
  }
  annotations += ', group = "' + group + '")\n';

  var enabledCheckbox = document.getElementById('project_enabled');
  if (enabledCheckbox && !enabledCheckbox.checked) {
    Blockly.FtcJava.generateImport_('Disabled');
    annotations += '@Disabled\n';
  }
  return annotations;
};

Blockly.FtcJava.convertBlocksTypeToFtcJavaType_ = function(type) {
  switch (type) {
    case 'Array':
      return 'List';
    case 'Boolean':
      return 'boolean';
    case 'Number':
      return 'double';
  }
  return type;
};

Blockly.FtcJava.isFtcJavaTypePrimitive_ = function(type) {
  return type == 'boolean' || type == 'double' || type == 'float'
      || type == 'long' || type == 'int' || type == 'short' || type == 'byte';
}

Blockly.FtcJava.generateImport_ = function(type) {
  var importCode = null;
  var dot = type.indexOf('.');
  if (dot > -1) {
    type = type.substring(0, dot);
  }
  // NOTE(lizlooney): If you add a case to this switch, you should also add that type to
  // Blockly.FtcJava.addReservedWords.
  switch (type) {
    case 'Color':
      importCode = 'import android.graphics.' + type + ';';
      break;
    case 'SoundPlayer':
      importCode = 'import com.qualcomm.ftccommon.' + type + ';';
      break;
    case 'BNO055IMU':
    case 'JustLoggingAccelerationIntegrator':
      importCode = 'import com.qualcomm.hardware.bosch.' + type + ';';
      break;
    case 'ModernRoboticsI2cCompassSensor':
    case 'ModernRoboticsI2cGyro':
    case 'ModernRoboticsI2cRangeSensor':
      importCode = 'import com.qualcomm.hardware.modernrobotics.' + type + ';';
      break;
    case 'RevBlinkinLedDriver':
      importCode = 'import com.qualcomm.hardware.rev.' + type + ';';
      break;
    case 'Autonomous':
    case 'Disabled':
    case 'LinearOpMode':
    case 'TeleOp':
      importCode = 'import com.qualcomm.robotcore.eventloop.opmode.' + type + ';';
      break;
    case 'AccelerationSensor':
    case 'AnalogInput':
    case 'AnalogOutput':
    case 'CRServo':
    case 'ColorSensor':
    case 'CompassSensor':
    case 'DcMotor':
    case 'DcMotorEx':
    case 'DcMotorSimple':
    case 'DigitalChannel':
    case 'DistanceSensor':
    case 'GyroSensor':
    case 'Gyroscope':
    case 'I2cAddr':
    case 'I2cAddrConfig':
    case 'I2cAddressableDevice':
    case 'IrSeekerSensor':
    case 'LED':
    case 'Light':
    case 'LightSensor':
    case 'MotorControlAlgorithm':
    case 'NormalizedColorSensor':
    case 'NormalizedRGBA':
    case 'OpticalDistanceSensor':
    case 'OrientationSensor':
    case 'PIDCoefficients':
    case 'PIDFCoefficients':
    case 'PWMOutput':
    case 'Servo':
    case 'ServoController':
    case 'SwitchableLight':
    case 'TouchSensor':
    case 'UltrasonicSensor':
    case 'VoltageSensor':
      importCode = 'import com.qualcomm.robotcore.hardware.' + type + ';';
      break;
    case 'ElapsedTime':
    case 'Range':
    case 'ReadWriteFile':
    case 'RobotLog':
      importCode = 'import com.qualcomm.robotcore.util.' + type + ';';
      break;
    case 'ArrayList':
    case 'Collections':
    case 'List':
      importCode = 'import java.util.' + type + ';';
      break;
    case 'ClassFactory':
    case 'JavaUtil':
      importCode = 'import org.firstinspires.ftc.robotcore.external.' + type + ';';
      break;
    case 'AndroidAccelerometer':
    case 'AndroidGyroscope':
    case 'AndroidOrientation':
    case 'AndroidSoundPool':
    case 'AndroidTextToSpeech':
      importCode = 'import org.firstinspires.ftc.robotcore.external.android.' + type + ';';
      break;
    case 'CameraName':
    case 'WebcamName':
      importCode = 'import org.firstinspires.ftc.robotcore.external.hardware.camera.' + type + ';';
      break;
    case 'MatrixF':
    case 'OpenGLMatrix':
    case 'VectorF':
      importCode = 'import org.firstinspires.ftc.robotcore.external.matrices.' + type + ';';
      break;
    case 'Acceleration':
    case 'AngleUnit':
    case 'AngularVelocity':
    case 'AxesOrder':
    case 'AxesReference':
    case 'Axis':
    case 'DistanceUnit':
    case 'MagneticFlux':
    case 'Orientation':
    case 'Position':
    case 'Quaternion':
    case 'RelicRecoveryVuMark':
    case 'Temperature':
    case 'TempUnit':
    case 'UnnormalizedAngleUnit':
    case 'Velocity':
    case 'VuforiaBase':
    case 'VuforiaLocalizer':
    case 'VuforiaRelicRecovery':
    case 'VuforiaRoverRuckus':
    case 'VuforiaTrackable':
    case 'VuforiaTrackableDefaultListener':
    case 'VuforiaTrackables':
      importCode = 'import org.firstinspires.ftc.robotcore.external.navigation.' + type + ';';
      break;
    case 'AppUtil':
      importCode = 'import org.firstinspires.ftc.robotcore.internal.system.' + type + ';';
      break;
    case 'Recognition':
    case 'TfodBase':
    case 'TfodRoverRuckus':
      importCode = 'import org.firstinspires.ftc.robotcore.external.tfod.' + type + ';';
  }
  if (importCode) {
    Blockly.FtcJava.definitions_['import_' + type] = importCode;
    return true;
  }
  return false;
};

Blockly.FtcJava.importDeclareAssign_ = function(block, identifierFieldName, javaType) {
  var identifier = null;
  var identifierForFtcJava = null;
  var rvalue = null;
  var needsToBeClosed = false;
  switch (javaType) {
    case 'AndroidAccelerometer':
      // androidAccelerometerIdentifierForFtcJava is defined dynamically in
      // HardwareUtil.fetchJavaScriptForHardware().
      identifierForFtcJava = identifier = androidAccelerometerIdentifierForFtcJava;
      rvalue = 'new ' + javaType + '()';
      break;
    case 'AndroidGyroscope':
      // androidGyroscopeIdentifierForFtcJava is defined dynamically in
      // HardwareUtil.fetchJavaScriptForHardware().
      identifierForFtcJava = identifier = androidGyroscopeIdentifierForFtcJava;
      rvalue = 'new ' + javaType + '()';
      break;
    case 'AndroidOrientation':
      // androidOrientationIdentifierForFtcJava is defined dynamically in
      // HardwareUtil.fetchJavaScriptForHardware().
      identifierForFtcJava = identifier = androidOrientationIdentifierForFtcJava;
      rvalue = 'new ' + javaType + '()';
      break;
    case 'AndroidSoundPool':
      // androidSoundPoolIdentifierForFtcJava is defined dynamically in
      // HardwareUtil.fetchJavaScriptForHardware().
      identifierForFtcJava = identifier = androidSoundPoolIdentifierForFtcJava;
      rvalue = 'new ' + javaType + '()';
      needsToBeClosed = true;
      break;
    case 'AndroidTextToSpeech':
      // androidTextToSpeechIdentifierForFtcJava is defined dynamically in
      // HardwareUtil.fetchJavaScriptForHardware().
      identifierForFtcJava = identifier = androidTextToSpeechIdentifierForFtcJava;
      rvalue = 'new ' + javaType + '()';
      needsToBeClosed = true;
      break;
    case 'TfodRoverRuckus':
      // tfodRoverRuckusIdentifierForFtcJava is defined dynamically in
      // HardwareUtil.fetchJavaScriptForHardware().
      identifierForFtcJava = identifier = tfodRoverRuckusIdentifierForFtcJava;
      rvalue = 'new ' + javaType + '()';
      needsToBeClosed = true;
      break;
    case 'VuforiaRelicRecovery':
      // vuforiaRelicRecoveryIdentifierForFtcJava is defined dynamically in
      // HardwareUtil.fetchJavaScriptForHardware().
      identifierForFtcJava = identifier = vuforiaRelicRecoveryIdentifierForFtcJava;
      rvalue = 'new ' + javaType + '()';
      needsToBeClosed = true;
      break;
    case 'VuforiaRoverRuckus':
      // vuforiaRoverRuckusIdentifierForFtcJava is defined dynamically in
      // HardwareUtil.fetchJavaScriptForHardware().
      identifierForFtcJava = identifier = vuforiaRoverRuckusIdentifierForFtcJava;
      rvalue = 'new ' + javaType + '()';
      needsToBeClosed = true;
      break;
    default:
      if (!identifierFieldName) {
        throw 'Unexpected situation (identifierFieldName is \'' + identifierFieldName + '\').';
      }
      identifier = block.getFieldValue(identifierFieldName);
      // getIdentifierForFtcJava is generated dynamically in
      // HardwareUtil.fetchJavaScriptForHardware().
      identifierForFtcJava = getIdentifierForFtcJava(identifier);
      // getHardwareItemDeviceName is generated dynamically in
      // HardwareUtil.fetchJavaScriptForHardware().
      var hardwareName = getHardwareItemDeviceName(identifier);
      var deviceMappingName = Blockly.FtcJava.getDeviceMappingName_(javaType);
      if (deviceMappingName != null) {
        rvalue = 'hardwareMap.' + deviceMappingName + '.get("' + hardwareName + '")';
      } else {
        rvalue = 'hardwareMap.get(' + javaType + '.class, "' + hardwareName + '")';
      }
      break;
  }

  Blockly.FtcJava.generateImport_(javaType);
  Blockly.FtcJava.definitions_['declare_field_' + identifier] =
      'private ' + javaType + ' ' + identifierForFtcJava + ';';

  Blockly.FtcJava.definitions_['assign_field_' + identifier] =
      identifierForFtcJava + ' = ' + rvalue + ';';

  if (needsToBeClosed) {
    Blockly.FtcJava.definitions_['closing_code_' + identifier] = identifierForFtcJava + '.close();';
  }

  return identifierForFtcJava;
};

Blockly.FtcJava.getDeviceMappingName_ = function(javaType) {
  switch (javaType) {
    case 'AccelerationSensor':
      return 'accelerationSensor';
    case 'AnalogInput':
      return 'analogInput';
    case 'AnalogOutput':
      return 'analogOutput';
    case 'ColorSensor':
      return 'colorSensor';
    case 'CompassSensor':
      return 'compassSensor';
    case 'CRServo':
      return 'crservo';
    case 'DcMotor':
      return 'dcMotor';
    case 'DigitalChannel':
      return 'digitalChannel';
    case 'GyroSensor':
      return 'gyroSensor';
    case 'IrSeekerSensor':
      return 'irSeekerSensor';
    case 'LED':
      return 'led';
    case 'LightSensor':
      return 'lightSensor';
    case 'OpticalDistanceSensor':
      return 'opticalDistanceSensor';
    case 'PWMOutput':
      return 'pwmOutput';
    case 'Servo':
      return 'servo';
    case 'ServoController':
      return 'servoController';
    case 'TouchSensor':
      return 'touchSensor';
    case 'UltrasonicSensor':
      return 'ultrasonicSensor';
    case 'VoltageSensor':
      return 'voltageSensor';
  }
  return null;
};

Blockly.FtcJava.getOpModeNameForFtcJava_ = function() {
  return Blockly.FtcJava.getClassNameForFtcJava_() + ' (Blocks to Java)';
};

Blockly.FtcJava.makeFirstLetterLowerCase_ = function(text) {
  return text[0].toLowerCase() + text.substring(1);
};

Blockly.FtcJava.makeFirstLetterUpperCase_ = function(text) {
  return text[0].toUpperCase() + text.substring(1);
};
