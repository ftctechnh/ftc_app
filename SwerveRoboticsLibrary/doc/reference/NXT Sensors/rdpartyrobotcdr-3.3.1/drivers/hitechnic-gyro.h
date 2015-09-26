/*!@addtogroup HiTechnic
 * @{
 * @defgroup htgyro Gyroscopic Sensor
 * HiTechnic Gyroscopic Sensor
 * @{
 */

/*
 * $Id: hitechnic-gyro.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTGYRO_H__
#define __HTGYRO_H__
/** \file hitechnic-gyro.h
 * \brief HiTechnic Gyroscopic Sensor driver
 *
 * hitechnic-gyro.h provides an API for the HiTechnic Gyroscopic Sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Renamed HTGYROgetCalibration to HTGYROreadCal<br>
 *        Renamed HTGYROsetCalibration to HTGYROsetCal<br>
 *        Renamed HTGYROcalibrate to HTGYROstartCal<br>
 *        Added SMUX functions
 * - 0.3: Removed some of the functions requiring SPORT/MPORT macros
 * - 0.4: Removed "NW - No Wait" functions\n
 *        Replaced array structs with typedefs\n
 *
 * Credits:
 * - Big thanks to HiTechnic for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 20 February 2011
 * \version 0.4
 * \example hitechnic-gyro-test1.c
 * \example hitechnic-gyro-test2.c
 * \example hitechnic-gyro-SMUX-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

float HTGYROreadRot(tSensors link);
float HTGYROstartCal(tSensors link);
float HTGYROreadCal(tSensors link);
// void HTGYROsetCal(tSensors link, int offset);

#ifdef __HTSMUX_SUPPORT__
float HTGYROreadRot(tMUXSensor muxsensor);
float HTGYROstartCal(tMUXSensor muxsensor);
float HTGYROreadCal(tMUXSensor muxsensor);
void HTGYROsetCal(tMUXSensor muxsensor, int offset);
#endif // __HTSMUX_SUPPORT__

float HTGYRO_offsets[][] = {{620.0, 620.0, 620.0, 620.0}, /*!< Array for offset values.  Default is 620 */
                          {620.0, 620.0, 620.0, 620.0},
                          {620.0, 620.0, 620.0, 620.0},
                          {620.0, 620.0, 620.0, 620.0}};

/**
 * Read the value of the gyro
 * @param link the HTGYRO port number
 * @return the value of the gyro
 */
float HTGYROreadRot(tSensors link) {
  // Make sure the sensor is configured as type sensorRawValue
  if (SensorType[link] != sensorAnalogInactive) {
    SetSensorType(link, sensorAnalogInactive);
    wait1Msec(100);
  }

  return (SensorValue[link] - HTGYRO_offsets[link][0]);
}


/**
 * Read the value of the gyro
 * @param muxsensor the SMUX sensor port number
 * @return the value of the gyro
 */
#ifdef __HTSMUX_SUPPORT__
float HTGYROreadRot(tMUXSensor muxsensor) {
  return HTSMUXreadAnalogue(muxsensor) - HTGYRO_offsets[SPORT(muxsensor)][MPORT(muxsensor)];
}
#endif // __HTSMUX_SUPPORT__


/**
 * Calibrate the gyro by calculating the average offset of 5 raw readings.
 * @param link the HTGYRO port number
 * @return the new offset value for the gyro
 */
float HTGYROstartCal(tSensors link) {
  long _avgdata = 0;

  // Make sure the sensor is configured as type sensorRawValue
  if (SensorType[link] != sensorAnalogInactive) {
    SetSensorType(link, sensorAnalogInactive);
    wait1Msec(100);
  }

  // Take 50 readings and average them out
  for (int i = 0; i < 50; i++) {
    _avgdata += SensorValue[link];
    wait1Msec(5);
  }

  // Store new offset
  HTGYRO_offsets[link][0] = (_avgdata / 50.0);

  // Return new offset value
  return HTGYRO_offsets[link][0];
}


/**
 * Calibrate the gyro by calculating the average offset of 50 raw readings.
 * @param muxsensor the SMUX sensor port number
 * @return the new offset value for the gyro
 */
#ifdef __HTSMUX_SUPPORT__
float HTGYROstartCal(tMUXSensor muxsensor) {
  long _avgdata = 0;

  // Take 5 readings and average them out
  for (int i = 0; i < 50; i++) {
    _avgdata += HTSMUXreadAnalogue(muxsensor);
    wait1Msec(50);
  }

  // Store new offset
  HTGYRO_offsets[SPORT(muxsensor)][MPORT(muxsensor)] = (_avgdata / 50.0);

  // Return new offset value
  return HTGYRO_offsets[SPORT(muxsensor)][MPORT(muxsensor)];
}
#endif // __HTSMUX_SUPPORT__


/**
 * Override the current offset for the gyro manually
 * @param link the HTGYRO port number
 * @param offset the new offset to be used
 */
//#define HTGYROsetCal(link, offset) HTGYRO_offsets[link][0] = offset
void HTGYROsetCal(tSensors link, int offset) {
  HTGYRO_offsets[link][0] = offset;
}


/**
 * Override the current offset for the gyro manually
 * @param muxsensor the SMUX sensor port number
 * @param offset the new offset to be used
 */
#ifdef __HTSMUX_SUPPORT__
//#define HTGYROsetCal(muxsensor, offset) HTGYRO_offsets[SPORT(muxsensor)][MPORT(muxsensor)] = offset
void HTGYROsetCal(tMUXSensor muxsensor, int offset) {
  HTGYRO_offsets[SPORT(muxsensor)][MPORT(muxsensor)] = offset;
}
#endif // __HTSMUX_SUPPORT__


/**
 * Retrieve the current offset for the gyro
 * @param link the HTGYRO port number
 * @return the offset value for the gyro
 */
float HTGYROreadCal(tSensors link) {
  return HTGYRO_offsets[link][0];
}


/**
 * Retrieve the current offset for the gyro
 * @param muxsensor the SMUX sensor port number
 * @return the offset value for the gyro
 */
#ifdef __HTSMUX_SUPPORT__
float HTGYROreadCal(tMUXSensor muxsensor) {
  return HTGYRO_offsets[SPORT(muxsensor)][MPORT(muxsensor)];
}
#endif // __HTSMUX_SUPPORT__

#endif // __HTGYRO_H__

/*
 * $Id: hitechnic-gyro.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
