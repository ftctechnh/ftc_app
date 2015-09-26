/*!@addtogroup other
 * @{
 * @defgroup firgelli Firgelli Linear Actuator
 * Firgelli Linear Actuator driver
 * @{
 */
/*
 * $Id: firgelli-linearact.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __FLAC_H__
#define __FLAC_H__
/** \file firgelli-linearact.h
 * \brief Firgelli Linear Actuator driver
 *
 * firgelli-linearact.h provides an API for the Firgelli Linear Actuator.
 *
 * Changelog:
 * - 0.1: Initial release
 *
 * TODO:
 * - Add ramping support (being worked on, has a few bugs)
 *
 * Credits:
 * - Big thanks to Firgelli for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (mightor@gmail.com), version 0.1
 * \date 16 february 2010
 * \version 0.1
 * \example firgelli-linearact-test1.c
 */

#pragma systemFile

#define STALL_TIME_SLOW   1200             /*!< Counter to check if motor is stalled when motor speeds are < 50 */
#define STALL_TIME_FAST   800              /*!< Counter to check if motor is stalled when motor speeds are >= 50 */

long _encoderTarget[3] = {0, 0, 0};       /*!< Motor encoder targets - INTERNAL */
long _motorHighPower[3] = {0, 0, 0};      /*!< High Power - top speed of the motor - INTERNAL */
bool _stalled[3] = {false, false, false}; /*!< Are we stalling? - INTERNAL */
bool _inverted[3] = {false, false, false};/*!< Are we reversing the motors? - INTERNAL */


// tasks
task _FLACcontrolTaskA();
task _FLACcontrolTaskB();
task _FLACcontrolTaskC();

// Functions
void _FLACcontrolTasks(tMotor _motor, int _highPower, long _encTarget);

bool isDone(tMotor _motor);
void FLACstopLA(tMotor _motor);
void FLACextendLA(tMotor _motor, int _highPower);
void FLACextendLA(tMotor _motor, int _highPower, int distance);
void FLACtretractLA(tMotor _motor, int _highPower);
void FLACtretractLA(tMotor _motor, int _highPower, int distance);
void FLACmoveLA(tMotor _motor, int highpower, int pos);
void FLACsetInverse(tMotor _motor);


// Task to control motor A
task _FLACcontrolTaskA() {
  long _currentEncVal;                // _currentEncVal encoder value
  bool _reverse = false;
  bool _done = false;
  long _lastEncoderCount = 0;
  long _stallCounter = 0;

  // This has to be done to prevent the PID regulator from
  // messing with the motor speeds
  nMotorPIDSpeedCtrl[motorA] = mtrNoReg;

  // we're not stalled just yet, so reset the variable.
  _stalled[motorA] = false;

  _currentEncVal = nMotorEncoder[motorA];
  _stallCounter = 0;

  // Flip it and reverse it
  if (_encoderTarget[motorA] < _currentEncVal)
    _reverse = true;

  while (!_done) {
    _currentEncVal = nMotorEncoder[motorA];

    // Are we there yet?
    if (_reverse && (_currentEncVal <= _encoderTarget[motorA]))
      _done = true;
    else if (!_reverse && (_currentEncVal >= _encoderTarget[motorA]))
      _done = true;

    // Stall detection magic bits happening here.
    if (!_done && (abs(_lastEncoderCount - _currentEncVal) > 0)) {
      _stallCounter = 0;
    } else if (!_done && (_stallCounter > STALL_TIME_SLOW) && _motorHighPower[motorA] < 50) {
      _stalled[motorA] = true;
      _done = true;
    } else if (!_done && (_stallCounter > STALL_TIME_SLOW) && _motorHighPower[motorA] >= 50) {
      _stalled[motorA] = true;
      _done = true;
    } else {
      _stallCounter++;
    }

    // Only change the motor speed if we're not done yet
    if (!_done)
      motor[motorA] = (_reverse) ? -_motorHighPower[motorA] : _motorHighPower[motorA];

    // update the _lastEncoderCount for the stall detection
    _lastEncoderCount = _currentEncVal;
    EndTimeSlice();
  }
  motor[motorA] = 0; //turn motor off
}


// Task to control motor B
task _FLACcontrolTaskB() {
  long _currentEncVal;                // _currentEncVal encoder value
  bool _reverse = false;
  bool _done = false;
  long _lastEncoderCount = 0;
  long _stallCounter = 0;

  // This has to be done to prevent the PID regulator from
  // messing with the motor speeds
  nMotorPIDSpeedCtrl[motorB] = mtrNoReg;

  // we're not stalled just yet, so reset the variable.
  _stalled[motorB] = false;

  _currentEncVal = nMotorEncoder[motorB];
  _stallCounter = 0;

  // Flip it and reverse it
  if (_encoderTarget[motorB] < _currentEncVal)
    _reverse = true;

  while (!_done) {
    _currentEncVal = nMotorEncoder[motorB];

    // Are we there yet?
    if (_reverse && (_currentEncVal <= _encoderTarget[motorB]))
      _done = true;
    else if (!_reverse && (_currentEncVal >= _encoderTarget[motorB]))
      _done = true;

    // Stall detection magic bits happening here.
    if (!_done && (abs(_lastEncoderCount - _currentEncVal) > 0)) {
      _stallCounter = 0;
    } else if (!_done && (_stallCounter > STALL_TIME_SLOW) && _motorHighPower[motorB] < 50) {
      _stalled[motorB] = true;
      _done = true;
    } else if (!_done && (_stallCounter > STALL_TIME_SLOW) && _motorHighPower[motorB] >= 50) {
      _stalled[motorB] = true;
      _done = true;
    } else {
      _stallCounter++;
    }

    // Only change the motor speed if we're not done yet
    if (!_done)
      motor[motorB] = (_reverse) ? -_motorHighPower[motorB] : _motorHighPower[motorB];

    // update the _lastEncoderCount for the stall detection
    _lastEncoderCount = _currentEncVal;
    EndTimeSlice();
  }
  motor[motorB] = 0; //turn motor off
}


// Task to control motor C
task _FLACcontrolTaskC() {
  long _currentEncVal;                // _currentEncVal encoder value
  bool _reverse = false;
  bool _done = false;
  long _lastEncoderCount = 0;
  long _stallCounter = 0;

  // This has to be done to prevent the PID regulator from
  // messing with the motor speeds
  nMotorPIDSpeedCtrl[motorC] = mtrNoReg;

  // we're not stalled just yet, so reset the variable.
  _stalled[motorC] = false;

  _currentEncVal = nMotorEncoder[motorC];
  _stallCounter = 0;

  // Flip it and reverse it
  if (_encoderTarget[motorC] < _currentEncVal)
    _reverse = true;

  while (!_done) {
    _currentEncVal = nMotorEncoder[motorC];

    // Are we there yet?
    if (_reverse && (_currentEncVal <= _encoderTarget[motorC]))
      _done = true;
    else if (!_reverse && (_currentEncVal >= _encoderTarget[motorC]))
      _done = true;

    // Stall detection magic bits happening here.
    if (!_done && (abs(_lastEncoderCount - _currentEncVal) > 0)) {
      _stallCounter = 0;
    } else if (!_done && (_stallCounter > STALL_TIME_SLOW) && _motorHighPower[motorC] < 50) {
      _stalled[motorC] = true;
      _done = true;
    } else if (!_done && (_stallCounter > STALL_TIME_SLOW) && _motorHighPower[motorC] >= 50) {
      _stalled[motorC] = true;
      _done = true;
    } else {
      _stallCounter++;
    }

    // Only change the motor speed if we're not done yet
    if (!_done)
      motor[motorC] = (_reverse) ? -_motorHighPower[motorC] : _motorHighPower[motorC];

    // update the _lastEncoderCount for the stall detection
    _lastEncoderCount = _currentEncVal;
    EndTimeSlice();
  }
  motor[motorC] = 0; //turn motor off
}


/**
 * Stop and start the motor control tasks and set their parameters.
 *
 * Note: this is an internal function and should not be called directly.
 * @param _motor the motor to be controlled
 * @param _highPower the highest speed the motor should turn at
 * @param _encTarget the target the motor should move to
 */
void _FLACcontrolTasks(tMotor _motor, int _highPower, long _encTarget) {
  switch(_motor) {
    case motorA:
      if (getTaskState(_FLACcontrolTaskA) == taskStateRunning) {
        StopTask(_FLACcontrolTaskA);
        while(getTaskState(_FLACcontrolTaskA) != taskStateStopped) EndTimeSlice();
        wait1Msec(50);
        motor[motorA] = 0;
      }
      _motorHighPower[_motor] = _highPower;
      _encoderTarget[_motor] = _encTarget;

      StartTask(_FLACcontrolTaskA);
      while(getTaskState(_FLACcontrolTaskA) != taskStateRunning) EndTimeSlice();
      break;

    case motorB:
      if (getTaskState(_FLACcontrolTaskB) == taskStateRunning) {
        StopTask(_FLACcontrolTaskB);
        while(getTaskState(_FLACcontrolTaskB) != taskStateStopped) EndTimeSlice();
        wait1Msec(50);
        motor[motorB] = 0;
      }
      _motorHighPower[_motor] = _highPower;
      _encoderTarget[_motor] = _encTarget;

      StartTask(_FLACcontrolTaskB);
      while(getTaskState(_FLACcontrolTaskB) != taskStateRunning) EndTimeSlice();
      break;

    case motorC:
      if (getTaskState(_FLACcontrolTaskC) == taskStateRunning) {
        StopTask(_FLACcontrolTaskC);
        while(getTaskState(_FLACcontrolTaskC) != taskStateStopped) EndTimeSlice();
        wait1Msec(50);
        motor[motorC] = 0;
      }
      _motorHighPower[_motor] = _highPower;
      _encoderTarget[_motor] = _encTarget;

      StartTask(_FLACcontrolTaskC);
      while(getTaskState(_FLACcontrolTaskC) != taskStateRunning) EndTimeSlice();
      break;
  }
}


/**
 * Stop the current operation
 * @param _motor the motor to be stopped
 */
void FLACstopLA(tMotor _motor)
{
  switch(_motor) {
    case motorA: StopTask(_FLACcontrolTaskA); motor[_motor] = 0; break;
    case motorB: StopTask(_FLACcontrolTaskC); motor[_motor] = 0; break;
    case motorC: StopTask(_FLACcontrolTaskB); motor[_motor] = 0; break;
  }
}


/**
 * Check if the motor is done with the current operation
 * @param _motor the motor to be checked
 * @return true if the motor is done, false if it isn't
 */
bool isDone(tMotor _motor) {
  switch(_motor) {
    case motorA: return (getTaskState(_FLACcontrolTaskA) == taskStateStopped);
    case motorB: return (getTaskState(_FLACcontrolTaskB) == taskStateStopped);
    case motorC: return (getTaskState(_FLACcontrolTaskC) == taskStateStopped);
  }
  return false;
}


/**
 * Check if the motor stalled on the last operation
 * @param _motor the motor to be checked
 * @return true if the motor stalled, false if it hadn't.
 */
bool isStalled(tMotor _motor) {
  return _stalled[_motor];
}


/**
 * Extend the Linear Actuator fully until stalled.
 * @param _motor the motor to be controlled
 * @param _highPower the highest speed the motor should turn at
 */
void FLACextendLA(tMotor _motor, int _highPower) {
  int distance = (_inverted[_motor]) ? -210 : 210;
  _FLACcontrolTasks(_motor, _highPower, -210);
  _stalled[_motor] = false;
}


/**
 * Extend the Linear Actuator.
 * @param _motor the motor to be controlled
 * @param _highPower the highest speed the motor should turn at
 * @param distance the number of encoder ticks (0.5mm) the actuator should move
 */
void FLACextendLA(tMotor _motor, int _highPower, int distance) {
  distance = (_inverted[_motor]) ? distance : -distance;
  _FLACcontrolTasks(_motor, _highPower, nMotorEncoder[_motor] - distance);
  _stalled[_motor] = false;
}


/**
 * Retract the Linear Actuator fully until stalled. It is wise to reset
 * the encoder count for that motor afterwards.
 * @param _motor the motor to be controlled
 * @param _highPower the highest speed the motor should turn at
 */
void FLACretractLA(tMotor _motor, int _highPower) {
  int distance = (_inverted[_motor]) ? 210 : -210;
  _FLACcontrolTasks(_motor, _highPower, distance);
 _stalled[_motor] = false;
}


/**
 * Retract the Linear Actuator.
 * @param _motor the motor to be controlled
 * @param _highPower the highest speed the motor should turn at
 * @param distance the number of encoder ticks (0.5mm) the actuator should move
 */
void FLACtretractLA(tMotor _motor, int _highPower, int distance) {
  distance = (_inverted[_motor]) ? distance : -distance;
  _FLACcontrolTasks(_motor, _highPower, nMotorEncoder[_motor] + distance);
  _stalled[_motor] = false;
}


/**
 * Move the Linear Actuator to an absolute position
 * @param _motor the motor to be controlled
 * @param highpower the highest speed the motor should turn at
 * @param pos the exact encoder count to move to
 */
void FLACmoveLA(tMotor _motor, int highpower, int pos) {
  _FLACcontrolTasks(_motor, highpower, pos);
}


/**
 * Invert the LA's movement
 * @param _motor the motor to be inverted
 * @param invert whether or not to invert the motor
 */
void FLACsetInverse(tMotor _motor, bool invert)
{
  _inverted[_motor] = invert;
}


#endif // __FLAC_H__

/*
 * $Id: firgelli-linearact.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
