/*
 * $Id: firgelli-linearact-ramping.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __FLAC_H__
#define __FLAC_H__

/** \file firgelli-linearact.h
 * \brief Firgelli Linear Actuator driver
 *
 * firgelli-linearact.h provides an API for the Firgelli Linear Actuator, this driver supports ramping.
 *
 * Changelog:
 * - 0.1: Initial release
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


#define STALL_TIME_SLOW   2000             /*!< Counter to check if motor is stalled when motor speeds are < 50 */
#define STALL_TIME_FAST   1000             /*!< Counter to check if motor is stalled when motor speeds are >= 50 */

long _encoderTarget[3] = {0, 0, 0};       /*!< Motor encoder targets - INTERNAL */
long _motorLowPower[3] = {0, 0, 0};       /*!< Low Power - lowest speed a motor is allowed to turn when ramping - INTERNAL */
long _motorHighPower[3] = {0, 0, 0};      /*!< High Power - top speed of the motor - INTERNAL */
bool _ramping[3] = {true, true, true };   /*!< Are we ramping? - INTERNAL */
bool _stalled[3] = {false, false, false}; /*!< Are we stalling? - INTERNAL */


// tasks
task _FLACcontrolTaskA();
task _FLACcontrolTaskB();
task _FLACcontrolTaskC();

// Functions
void _FLACcontrolTasks(tMotor _motor, int _highPower, long _encTarget, bool _ramp);

bool isDone(tMotor _motor);
void FLACextendLA(tMotor _motor, int _highPower);
void FLACextendLA(tMotor _motor, int _highPower, int distance);
void FLACextendLA(tMotor _motor, int _highPower, int distance, bool ramp);
void FLACtretractLA(tMotor _motor, int _highPower);
void FLACtretractLA(tMotor _motor, int _highPower, int distance);
void FLACtretractLA(tMotor _motor, int _highPower, int distance, bool ramp);
void FLACmoveLA(tMotor _motor, int highpower, int pos);


// Task to control motor A
task _FLACcontrolTaskA() {
  int _motorPower = 0;                // power
  long _currentEncVal;                // _currentEncVal encoder value
  long _initialEncVal = 0;
  long _rampDownEncCount = 0;
  long _rampUpEncCount = 0;
  bool _reverse = false;
  bool _done = false;
  long _lastEncoderCount = 0;
  long _timerA = 0;
  int _rampDist = 0;

  bMotorReflected[motorA] = true;

  // This has to be done to prevent the PID regulator from
  // messing with the motor speeds
  nMotorPIDSpeedCtrl[motorA] = mtrNoReg;

  // we're not stalled just yet, so reset the variable.
  _stalled[motorA] = false;

  _initialEncVal = nMotorEncoder[motorA];
  _timerA = 0;

  // Flip it and reverse it
  if (_encoderTarget[motorA] < _initialEncVal)
    _reverse = true;

  // Don't ramp if the low speed is equal to the high speed.
  // We're not going to ramp up and down if we're below 40 speed, there's no point.
  // Also, for very short distances there is also no point.
  if (_motorLowPower[motorA] == _motorHighPower[motorA]) {
    _ramping[motorA] = false;
  } else if ((abs(_initialEncVal - _encoderTarget[motorA]) >= 50) && (_motorHighPower[motorA] > 40)) {
    _rampDist = _encoderTarget[motorA] - _initialEncVal;
    if (_rampDist > 10)  _rampDist = 10;
    else if(_rampDist < -10) _rampDist = -10;

		_rampUpEncCount =   _initialEncVal + _rampDist;
		_rampDownEncCount = _encoderTarget[motorA] - _rampDist;
	  _ramping[motorA] = true;
	} else {
	  _ramping[motorA] = false;
	}

  while (!_done) {
    _currentEncVal = nMotorEncoder[motorA];

    // Ramp up
    if (_ramping[motorA] && (((_reverse && (_currentEncVal > _rampUpEncCount)) ||
       (!_reverse && (_currentEncVal < _rampUpEncCount))))) {

      _motorPower = _motorHighPower[motorA] -
                   ((_rampUpEncCount - _currentEncVal) *
                    (_motorHighPower[motorA] - _motorLowPower[motorA]) /
                    (_rampUpEncCount - _initialEncVal));

    // Ramp down
    } else if (_ramping[motorA] && (((_reverse && (_currentEncVal < _rampDownEncCount)) ||
              (!_reverse && (_currentEncVal > _rampDownEncCount))))) {

      _motorPower = _motorHighPower[motorA] -
                   ((_rampDownEncCount - _currentEncVal) *
                    (_motorLowPower[motorA] - _motorHighPower[motorA]) /
                    (_encoderTarget[motorA] - _rampDownEncCount));
    // Bit between ramping up and down
    } else {
        _motorPower = _motorHighPower[motorA];
    }

    // Are we there yet?
    if (_reverse && (_currentEncVal <= _encoderTarget[motorA]))
      _done = true;
    else if (!_reverse && (_currentEncVal >= _encoderTarget[motorA]))
      _done = true;

    // Stall detection magic bits happening here.
    if (!_done && (abs(_lastEncoderCount - _currentEncVal) > 0)) {
      _timerA = 0;
    } else if (!_done && (_timerA > STALL_TIME_SLOW) && _motorPower < 50) {
      _stalled[motorA] = true;
      _done = true;
    } else if (!_done && (_timerA > STALL_TIME_SLOW) && _motorPower >= 50) {
      _stalled[motorA] = true;
      _done = true;
    } else {
      _timerA++;
    }

    // Only change the motor speed if we're not done yet
    if (!_done)
      motor[motorA] = (_reverse) ? -_motorPower : _motorPower;
    // update the _lastEncoderCount for the stall detection
    _lastEncoderCount = _currentEncVal;
    EndTimeSlice();
  }
  motor[motorA] = 0; //turn motor off
}


// Task to control motor B
task _FLACcontrolTaskB() {
  int _motorPower = 0;                // power
  long _currentEncVal;               // _currentEncVal encoder value
  long _initialEncVal = 0;
  long _rampDownEncCount = 0;
  long _rampUpEncCount = 0;
  bool _reverse = false;
  bool _done = false;
  long _lastEncoderCount = 0;
  long _timerA = 0;
  int _rampDist = 0;

  bMotorReflected[motorB] = true;

  // This has to be done to prevent the PID regulator from
  // messing with the motor speeds
  nMotorPIDSpeedCtrl[motorB] = mtrNoReg;

  // we're not stalled just yet, so reset the variable.
  _stalled[motorB] = false;

  _initialEncVal = nMotorEncoder[motorB];
  _timerA = nPgmTime;

  // Flip it and reverse it
  if (_encoderTarget[motorB] < _initialEncVal)
    _reverse = true;

  // Don't ramp if the low speed is equal to the high speed.
  // We're not going to ramp up and down if we're below 40 speed, there's no point.
  // Also, for very short distances there is also no point.
  if (_motorLowPower[motorB] == _motorHighPower[motorB]) {
    _ramping[motorB] = false;
  } else if ((abs(_initialEncVal - _encoderTarget[motorB]) >= 50) && (_motorHighPower[motorB] > 40)) {
    _rampDist = _encoderTarget[motorB] - _initialEncVal;
    if (_rampDist > 10)  _rampDist = 10;
    else if(_rampDist < -10) _rampDist = -10;

		_rampUpEncCount =   _initialEncVal + _rampDist;
		_rampDownEncCount = _encoderTarget[motorB] - _rampDist;
	  _ramping[motorB] = true;
	} else {
	  _ramping[motorB] = false;
	}

  while (!_done) {
    _currentEncVal = nMotorEncoder[motorB];

    // Ramp up
    if (_ramping[motorA] && (((_reverse && (_currentEncVal > _rampUpEncCount)) ||
       (!_reverse && (_currentEncVal < _rampUpEncCount))))) {

      _motorPower = _motorHighPower[motorB] -
                   ((_rampUpEncCount - _currentEncVal) *
                    (_motorHighPower[motorB] - _motorLowPower[motorB]) /
                    (_rampUpEncCount - _initialEncVal));

    // Ramp down
    } else if (_ramping[motorB] && (((_reverse && (_currentEncVal < _rampDownEncCount)) ||
              (!_reverse && (_currentEncVal > _rampDownEncCount))))) {

      _motorPower = _motorHighPower[motorB] -
                   ((_rampDownEncCount - _currentEncVal) *
                    (_motorLowPower[motorB] - _motorHighPower[motorB]) /
                    (_encoderTarget[motorB] - _rampDownEncCount));

    // Bit between ramping up and down
    } else {
        _motorPower = _motorHighPower[motorB];
    }

    // Are we there yet?
    if (_reverse && (_currentEncVal <= _encoderTarget[motorB]))
      _done = true;
    else if (!_reverse && (_currentEncVal >= _encoderTarget[motorB]))
      _done = true;

    // Stall detection magic bits happening here.
    if (!_done && (abs(_lastEncoderCount - _currentEncVal) > 0)) {
      _timerA = nPgmTime;
    } else if (!_done && (abs(nPgmTime - _timerA) > STALL_TIME_SLOW) && _motorPower < 50) {
      _stalled[motorB] = true;
      _done = true;
    } else if (!_done && (abs(nPgmTime - _timerA) > STALL_TIME_FAST) && _motorPower >= 50) {
      _stalled[motorB] = true;
      _done = true;
    }

    // Only change the motor speed if we're not done yet
    if (!_done)
      motor[motorB] = (_reverse) ? -_motorPower : _motorPower;

    // update the _lastEncoderCount for the stall detection
    _lastEncoderCount = _currentEncVal;
    EndTimeSlice();
  }
  motor[motorB] = 0; //turn motor off
}


// Task to control motor C
task _FLACcontrolTaskC() {
  int _motorPower = 0;                // power
  long _currentEncVal;               // _currentEncVal encoder value
  long _initialEncVal = 0;
  long _rampDownEncCount = 0;
  long _rampUpEncCount = 0;
  bool _reverse = false;
  bool _done = false;
  long _lastEncoderCount = 0;
  long _timerA = 0;
  int _rampDist = 0;

  bMotorReflected[motorC] = true;

  // This has to be done to prevent the PID regulator from
  // messing with the motor speeds
  nMotorPIDSpeedCtrl[motorC] = mtrNoReg;

  // we're not stalled just yet, so reset the variable.
  _stalled[motorC] = false;

  _initialEncVal = nMotorEncoder[motorC];
  _timerA = nPgmTime;

  // Flip it and reverse it
  if (_encoderTarget[motorC] < _initialEncVal)
    _reverse = true;

  // Don't ramp if the low speed is equal to the high speed.
  // We're not going to ramp up and down if we're below 40 speed, there's no point.
  // Also, for very short distances there is also no point.
  if (_motorLowPower[motorC] == _motorHighPower[motorC]) {
    _ramping[motorC] = false;
  } else if ((abs(_initialEncVal - _encoderTarget[motorC]) >= 50) && (_motorHighPower[motorC] > 40)) {
    _rampDist = _encoderTarget[motorC] - _initialEncVal;
    if (_rampDist > 10)  _rampDist = 10;
    else if(_rampDist < -10) _rampDist = -10;

		_rampUpEncCount =   _initialEncVal + _rampDist;
		_rampDownEncCount = _encoderTarget[motorC] - _rampDist;
	  _ramping[motorC] = true;
	} else {
	  _ramping[motorC] = false;
	}

  while (!_done) {
    _currentEncVal = nMotorEncoder[motorC];

    // Ramp up
    if (_ramping[motorC] && (((_reverse && (_currentEncVal > _rampUpEncCount)) ||
       (!_reverse && (_currentEncVal < _rampUpEncCount))))) {

      _motorPower = _motorHighPower[motorC] -
                   ((_rampUpEncCount - _currentEncVal) *
                    (_motorHighPower[motorC] - _motorLowPower[motorC]) /
                    (_rampUpEncCount - _initialEncVal));

    // Ramp down
    } else if (_ramping[motorC] && (((_reverse && (_currentEncVal < _rampDownEncCount)) ||
              (!_reverse && (_currentEncVal > _rampDownEncCount))))) {

      _motorPower = _motorHighPower[motorC] -
                   ((_rampDownEncCount - _currentEncVal) *
                    (_motorLowPower[motorC] - _motorHighPower[motorC]) /
                    (_encoderTarget[motorC] - _rampDownEncCount));

    // Bit between ramping up and down
    } else {
        _motorPower = _motorHighPower[motorC];
    }

    // Are we there yet?
    if (_reverse && (_currentEncVal <= _encoderTarget[motorC]))
      _done = true;
    else if (!_reverse && (_currentEncVal >= _encoderTarget[motorC]))
      _done = true;

    // Stall detection magic bits happening here.
    if (!_done && (abs(_lastEncoderCount - _currentEncVal) > 0)) {
      _timerA = nPgmTime;
    } else if (!_done && (abs(nPgmTime - _timerA) > STALL_TIME_SLOW) && _motorPower < 50) {
      _stalled[motorC] = true;
      _done = true;
    } else if (!_done && (abs(nPgmTime - _timerA) > STALL_TIME_FAST) && _motorPower >= 50) {
      _stalled[motorC] = true;
      _done = true;
    }

    // Only change the motor speed if we're not done yet
    if (!_done)
      motor[motorC] = (_reverse) ? -_motorPower : _motorPower;

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
void _FLACcontrolTasks(tMotor _motor, int _highPower, long _encTarget, bool _ramp) {
  switch(_motor) {
    case motorA:
		  if (getTaskState(_FLACcontrolTaskA) == taskStateRunning) {
		    StopTask(_FLACcontrolTaskA);
		    while(getTaskState(_FLACcontrolTaskA) != taskStateStopped) EndTimeSlice();
		    wait1Msec(50);
		    motor[motorA] = 0;
		  }
		  if (_ramp && (_highPower > 30))
		    _motorLowPower[_motor] = 30;
		  else
		    _motorLowPower[_motor] = _highPower;

		  _motorHighPower[_motor] = _highPower;
		  _encoderTarget[_motor] = _encTarget;

		  StartTask(_FLACcontrolTaskA);
			while(getTaskState(_FLACcontrolTaskA) != taskStateRunning) EndTimeSlice();
		  break;

    case motorB:
		  if (getTaskState(_FLACcontrolTaskB) == taskStateRunning) {
		    StopTask(_FLACcontrolTaskA);
		    while(getTaskState(_FLACcontrolTaskB) != taskStateStopped) EndTimeSlice();
		    wait1Msec(50);
		    motor[motorB] = 0;
		  }
		  if (_ramp && (_highPower > 30))
		    _motorLowPower[_motor] = 30;
		  else
		    _motorLowPower[_motor] = _highPower;

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
		  if (_ramp && (_highPower > 30))
		    _motorLowPower[_motor] = 30;
		  else
		    _motorLowPower[_motor] = _highPower;

		  _motorHighPower[_motor] = _highPower;
		  _encoderTarget[_motor] = _encTarget;

		  StartTask(_FLACcontrolTaskC);
			while(getTaskState(_FLACcontrolTaskC) != taskStateRunning) EndTimeSlice();
		  break;
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
  _FLACcontrolTasks(_motor, _highPower, 210, false);
  _stalled[_motor] = false;
}


/**
 * Extend the Linear Actuator without ramping.
 * @param _motor the motor to be controlled
 * @param _highPower the highest speed the motor should turn at
 * @param distance the number of encoder ticks (0.5mm) the actuator should move
 */
void FLACextendLA(tMotor _motor, int _highPower, int distance) {
  distance += nMotorEncoder[_motor];
  _FLACcontrolTasks(_motor, _highPower, distance, false);
  _stalled[_motor] = false;
}


/**
 * Extend the Linear Actuator.
 * @param _motor the motor to be controlled
 * @param _highPower the highest speed the motor should turn at
 * @param distance the number of encoder ticks (0.5mm) the actuator should move
 * @param ramp whether or not the motor should be ramped up and down
 */
void FLACextendLA(tMotor _motor, int _highPower, int distance, bool ramp) {
  distance += nMotorEncoder[_motor];
  _FLACcontrolTasks(_motor, _highPower, distance, ramp);
  _stalled[_motor] = false;
}


/**
 * Retract the Linear Actuator fully until stalled. It is wise to reset
 * the encoder count for that motor afterwards.
 * @param _motor the motor to be controlled
 * @param _highPower the highest speed the motor should turn at
 */
void FLACtretractLA(tMotor _motor, int _highPower) {
  _FLACcontrolTasks(_motor, _highPower, -210, false);
 _stalled[_motor] = false;
}


/**
 * Retract the Linear Actuator without ramping.
 * @param _motor the motor to be controlled
 * @param _highPower the highest speed the motor should turn at
 * @param distance the number of encoder ticks (0.5mm) the actuator should move
 */
void FLACtretractLA(tMotor _motor, int _highPower, int distance) {
  distance -= nMotorEncoder[_motor];
  _FLACcontrolTasks(_motor, _highPower, distance, false);
  _stalled[_motor] = false;
}


/**
 * Retract the Linear Actuator.
 * @param _motor the motor to be controlled
 * @param _highPower the highest speed the motor should turn at
 * @param distance the number of encoder ticks (0.5mm) the actuator should move
 * @param ramp whether or not the motor should be ramped up and down
 */
void FLACtretractLA(tMotor _motor, int _highPower, int distance, bool ramp) {
  distance = nMotorEncoder[_motor] - distance;
  _FLACcontrolTasks(_motor, _highPower, distance, ramp);
  _stalled[_motor] = false;
}


/**
 * Move the Linear Actuator to an absolute position
 * @param _motor the motor to be controlled
 * @param highpower the highest speed the motor should turn at
 * @param pos the exact encoder count to move to
 * @param ramp whether or not the motor should be ramped up and down
 */
void FLACmoveLA(tMotor _motor, int highpower, int pos) {
  _FLACcontrolTasks(_motor, highpower, pos, false);
}

#endif // __FLAC_H__

/*
 * $Id: firgelli-linearact-ramping.h 133 2013-03-10 15:15:38Z xander $
 */
