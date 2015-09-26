/*!@addtogroup lego
 * @{
 * @defgroup legols Light Sensor
 * Light Sensor
 * @{
 */

/*
 * $Id: lego-light.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __LEGOLS_H__
#define __LEGOLS_H__
/** \file lego-light.h
 * \brief Lego Light Sensor driver
 *
 * lego-light.h provides an API for the Lego Light Sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Make use of new calls for analogue SMUX sensors in common.h
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 25 November 2009
 * \version 0.2
 * \example lego-light-test1.c
 * \example lego-light-test2.c
 * \example lego-light-SMUX-test1.c
 * \example lego-light-SMUX-test2.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define LEGOLSDAT "legols.dat"    /*!< Datafile for Light Sensor calibration info */

int lslow[16];
int lshigh[16];

// Globals
//int lslow = 0;                    /*!< Low calibration value */
//int lshigh = 1023;                /*!< High calibration value */
bool legols_calibrated = false;   /*!< Has the sensor been calibrated yet */

// Function prototypes
int LSvalRaw(tSensors link);
int LSvalNorm(tSensors link);
void LScalLow(tSensors link);
void LScalHigh(tSensors link);
void LSsetActive(tSensors link);
void LSsetInactive(tSensors link);

#ifdef __HTSMUX_SUPPORT__
int LSvalNorm(tMUXSensor muxsensor);
void LScalLow(tMUXSensor muxsensor);
int LSvalRaw(tMUXSensor muxsensor);
void LScalHigh(tMUXSensor muxsensor);
void LSsetActive(tMUXSensor muxsensor);
void LSsetInactive(tMUXSensor muxsensor);
#endif // __HTSMUX_SUPPORT__

void _LScheckSensor(tSensors link);
void _LSwriteCalVals();
void _LSreadCalVals();


/**
 * Read the raw value of the Light Sensor.
 * @param link the Light Sensor port number
 * @return the raw value of the Light Sensor
 */
int LSvalRaw(tSensors link) {
  _LScheckSensor(link);

  return SensorRaw[link];
}


/**
 * Read the raw value of the Light Sensor.
 * @param muxsensor the SMUX sensor port number
 * @return the raw value of the Light Sensor
 */
#ifdef __HTSMUX_SUPPORT__
int LSvalRaw(tMUXSensor muxsensor) {
  return 1023 - HTSMUXreadAnalogue(muxsensor);
}
#endif // __HTSMUX_SUPPORT__

/**
 * Read the normalised value of the Light Sensor, based on the low and high values.
 * @param link the Light Sensor port number
 * @return the normalised value
 */
int LSvalNorm(tSensors link) {
  long currval = 0;

  _LScheckSensor(link);

  if (!legols_calibrated) {
    _LSreadCalVals();
  }

  currval = LSvalRaw(link);

  if (currval <= lslow[link * 4])
    return 0;
  else if (currval >= lshigh[link * 4])
    return 100;

  return ((currval - lslow[link * 4]) * 100) / (lshigh[link * 4] - lslow[link * 4]);
}


/**
 * Read the normalised value of the Light Sensor, based on the low and high values.
 * @param muxsensor the SMUX sensor port number
 * @return the normalised value
 */
 #ifdef __HTSMUX_SUPPORT__
int LSvalNorm(tMUXSensor muxsensor) {
  long currval = 0;

  if (!legols_calibrated) {
    _LSreadCalVals();
  }

  currval = LSvalRaw(muxsensor);

  if (currval <= lslow[muxsensor])
    return 0;
  else if (currval >= lshigh[muxsensor])
    return 100;

  return ((currval - lslow[muxsensor]) * 100) / (lshigh[muxsensor] - lslow[muxsensor]);
}
#endif // __HTSMUX_SUPPORT__

/**
 * Calibrate the Light Sensor's low calibration value with the current raw sensor reading.
 * @param link the Light Sensor port number
 */
void LScalLow(tSensors link) {
  _LScheckSensor(link);

  lslow[link * 4] = SensorRaw[link];
  _LSwriteCalVals();
}


/**
 * Calibrate the Light Sensor's low calibration value with the current raw sensor reading.
 * @param muxsensor the SMUX sensor port number
 */
#ifdef __HTSMUX_SUPPORT__
void LScalLow(tMUXSensor muxsensor) {
  lslow[muxsensor] = LSvalRaw(muxsensor);
  _LSwriteCalVals();
}
#endif // __HTSMUX_SUPPORT__


/**
 * Calibrate the Light Sensor's high calibration value with the current raw sensor reading.
 * @param link the Light Sensor port number
 */
void LScalHigh(tSensors link) {
  _LScheckSensor(link);

  lshigh[link * 4] = SensorRaw[link];
  _LSwriteCalVals();
}


/**
 * Calibrate the Light Sensor's high calibration value with the current raw sensor reading.
 * @param muxsensor the SMUX sensor port number
 */
#ifdef __HTSMUX_SUPPORT__
void LScalHigh(tMUXSensor muxsensor) {
  lshigh[muxsensor] = LSvalRaw(muxsensor);
  _LSwriteCalVals();
}
#endif // __HTSMUX_SUPPORT__


/**
 * Configure the sensor as a LightActive sensor
 * @param link the Light Sensor port number
 */
void LSsetActive(tSensors link) {
  SensorType[link] = sensorLightActive;
  SensorMode[link] = modeRaw;
  wait1Msec(5);
}


/**
 * Configure the sensor as a LightActive sensor
 * @param muxsensor the SMUX sensor port number
 */
#ifdef __HTSMUX_SUPPORT__
void LSsetActive(tMUXSensor muxsensor) {
  HTSMUXsetAnalogueActive(muxsensor);
}
#endif // __HTSMUX_SUPPORT__


/**
 * Configure the sensor as a LightInactive sensor
 * @param link the Light Sensor port number
 */
void LSsetInactive(tSensors link) {
  SensorType[link] = sensorLightInactive;
  SensorMode[link] = modeRaw;
  wait1Msec(5);
}


/**
 * Configure the sensor as a LightInactive sensor
 * @param muxsensor the SMUX sensor port number
 */
#ifdef __HTSMUX_SUPPORT__
void LSsetInactive(tMUXSensor muxsensor) {
  HTSMUXsetAnalogueInactive(muxsensor);
}
#endif // __HTSMUX_SUPPORT__


/**
 * Check if the sensor is set to raw and that it's been configured as a
 * LightActive or Inactive sensor.  If not, set the default to sensorLightInActive.
 *
 * Note: this is an internal function and should not be called directly
 * @param link the Light Sensor port number
 */
void _LScheckSensor(tSensors link) {
  if (SensorMode[link] != modeRaw &&
    ((SensorType[link] != sensorLightActive) ||
     (SensorType[link] != sensorLightInactive))) {
      LSsetInactive(link);
    }
}


/**
 * Write the low and high calibration values to a data file.
 *
 * Note: this is an internal function and should not be called directly
 */
void _LSwriteCalVals() {
  TFileHandle hFileHandle;
  TFileIOResult nIoResult;
  short nFileSize = 64;

  // Delete the old data file and open a new one for writing
  Delete(LEGOLSDAT, nIoResult);
  OpenWrite(hFileHandle, nIoResult, LEGOLSDAT, nFileSize);
  if (nIoResult != ioRsltSuccess) {
    Close(hFileHandle, nIoResult);
    eraseDisplay();
    nxtDisplayTextLine(3, "W:can't cal file");
    PlaySound(soundException);
    while(bSoundActive) EndTimeSlice();
    wait1Msec(5000);
    StopAllTasks();
  }

  // Write the low calibration value
  for (int i = 0; i < 16; i++) {
	  WriteShort(hFileHandle, nIoResult, lslow[i]);
	  // writeDebugStreamLine("W: lslow[%d]: %d", i, lslow[i]);
	  if (nIoResult != ioRsltSuccess) {
	    eraseDisplay();
	    nxtDisplayTextLine(3, "can't write lowval");
	    PlaySound(soundException);
	    while(bSoundActive) EndTimeSlice();
	    wait1Msec(5000);
	    StopAllTasks();
	  }
	}

  // Write the low calibration value
  for (int i = 0; i < 16; i++) {
	  WriteShort(hFileHandle, nIoResult, lshigh[i]);
	  // writeDebugStreamLine("W lshigh[%d]: %d", i, lshigh[i]);
	  if (nIoResult != ioRsltSuccess) {
	    eraseDisplay();
	    nxtDisplayTextLine(3, "can't write highval");
	    PlaySound(soundException);
	    while(bSoundActive) EndTimeSlice();
	    wait1Msec(5000);
	    StopAllTasks();
	  }
	}

  // Close the file
  Close(hFileHandle, nIoResult);
  if (nIoResult != ioRsltSuccess) {
    eraseDisplay();
    nxtDisplayTextLine(3, "Can't close");
    PlaySound(soundException);
    while(bSoundActive) EndTimeSlice();
    wait1Msec(5000);
    StopAllTasks();
  }
}

/**
 * Read the low and high calibration values from a data file.
 *
 * Note: this is an internal function and should not be called directly
 */
void _LSreadCalVals() {
  TFileHandle hFileHandle;
  TFileIOResult nIoResult;
  short nFileSize;

  // Open the data file for reading
  legols_calibrated = true;
  OpenRead(hFileHandle, nIoResult, LEGOLSDAT, nFileSize);
  if (nIoResult != ioRsltSuccess) {
    Close(hFileHandle, nIoResult);
    // Assign default values
		memset(&lslow[0], 0, sizeof(lslow));
		for (int i = 0; i < 16; i++) {
		  lshigh[i] = 1023;
		}
		_LSwriteCalVals();
    return;
  }

  // Read the low calibration value
  for (int i = 0; i < 16; i++) {
	  ReadShort(hFileHandle, nIoResult, lslow[i]);
    // writeDebugStreamLine("R: lslow[%d]: %d", i, lslow[i]);
	  if (nIoResult != ioRsltSuccess) {
			memset(&lslow[0], 0, sizeof(lslow));
			for (int i = 0; i < 16; i++) {
			  lshigh[i] = 1023;
			}
			_LSwriteCalVals();
			return;
	  }
	}

  for (int i = 0; i < 16; i++) {
	  ReadShort(hFileHandle, nIoResult, lshigh[i]);
	  // writeDebugStreamLine("R lshigh[%d]: %d", i, lshigh[i]);
	  if (nIoResult != ioRsltSuccess) {
			memset(&lslow[0], 0, sizeof(lslow));
			for (int i = 0; i < 16; i++) {
			  lshigh[i] = 1023;
			}
			_LSwriteCalVals();
			return;
	  }
	}

  Close(hFileHandle, nIoResult);
}

#endif // __LEGOLS_H__

/*
 * $Id: lego-light.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
