/*!@addtogroup HiTechnic
 * @{
 * @defgroup HTMAG Magnetic Field Sensor
 * HiTechnic Magnetic Field Sensor
 * @{
 */

/*
 * $Id: hitechnic-magfield.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTMAG_H__
#define __HTMAG_H__
/** \file hitechnic-magfield.h
 * \brief HiTechnic Magnetic Field Sensor driver
 *
 * hitechnic-magfield.h provides an API for the HiTechnic Magnetic Field Sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 *
 * Credits:
 * - Big thanks to HiTechnic for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 27 July 2010
 * \version 0.1
 * \example hitechnic-magfield-test1.c
 * \example hitechnic-magfield-SMUX-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

int HTMAGreadVal(tSensors link);
int HTMAGreadRaw(tSensors link);
int HTMAGstartCal(tSensors link);
int HTMAGreadCal(tSensors link);
void HTMAGsetCal(tSensors link, int bias);

#ifdef __HTSMUX_SUPPORT__
int HTMAGreadVal(tMUXSensor muxsensor);
int HTMAGreadRaw(tMUXSensor muxsensor);
int HTMAGstartCal(tMUXSensor muxsensor);
int HTMAGreadCal(tMUXSensor muxsensor);
void HTMAGsetCal(tMUXSensor muxsensor, int bias);
#endif

int HTMAG_bias[][] = {{512, 512, 512, 512}, /*!< Array for bias values.  Default is 512 */
                          {512, 512, 512, 512},
                          {512, 512, 512, 512},
                          {512, 512, 512, 512}};

/**
 * Read the value of the Magnetic Field Sensor
 * @param link the HTMAG port number
 * @return the value of the Magnetic Field Sensor (-200 to +200)
 */
int HTMAGreadVal(tSensors link) {
  // Make sure the sensor is configured as type sensorRawValue
  if (SensorType[link] != sensorRawValue) {
    SetSensorType(link, sensorRawValue);
    wait1Msec(100);
  }

  return (SensorValue[link] - HTMAG_bias[link][0]);
}


/**
 * Read the value of the Magnetic Field Sensor
 * @param muxsensor the SMUX sensor port number
 * @return the value of the Magnetic Field Sensor (-200 to +200)
 */
#ifdef __HTSMUX_SUPPORT__
int HTMAGreadVal(tMUXSensor muxsensor) {
  return HTSMUXreadAnalogue(muxsensor) - HTMAG_bias[SPORT(muxsensor)][MPORT(muxsensor)];
}
#endif // __HTSMUX_SUPPORT__


/**
 * Read the raw value of the Magnetic Field Sensor
 * @param link the HTMAG port number
 * @return the value of the Magnetic Field Sensor (approx 300 to 700)
 */
int HTMAGreadRaw(tSensors link) {
  // Make sure the sensor is configured as type sensorRawValue
  if (SensorType[link] != sensorRawValue) {
    SetSensorType(link, sensorRawValue);
    wait1Msec(100);
  }

  return SensorValue[link];
}


/**
 * Read the raw value of the Magnetic Field Sensor
 * @param muxsensor the SMUX sensor port number
 * @return the value of the Magnetic Field Sensor (approx 300 to 700)
 */
#ifdef __HTSMUX_SUPPORT__
int HTMAGreadRaw(tMUXSensor muxsensor) {
  return HTSMUXreadAnalogue(muxsensor);
}
#endif // __HTSMUX_SUPPORT__


/**
 * Calibrate the sensor by calculating the average bias of 5 raw readings.
 * @param link the HTMAG port number
 * @return the new bias value for the sensor
 */
int HTMAGstartCal(tSensors link) {
  int _avgdata = 0;

  // Make sure the sensor is configured as type sensorRawValue
  if (SensorType[link] != sensorRawValue) {
    SetSensorType(link, sensorRawValue);
    wait1Msec(100);
  }

  // Take 5 readings and average them out
  for (int i = 0; i < 5; i++) {
    _avgdata += SensorValue[link];
    wait1Msec(50);
  }

  // Store new bias
  HTMAG_bias[link][0] = (_avgdata / 5);

  // Return new bias value
  return HTMAG_bias[link][0];
}


/**
 * Calibrate the Magnetic Field Sensor by calculating the average bias of 5 raw readings.
 * @param muxsensor the SMUX sensor port number
 * @return the new bias value for the Magnetic Field Sensor
 */
#ifdef __HTSMUX_SUPPORT__
int HTMAGstartCal(tMUXSensor muxsensor) {
  int _avgdata = 0;

  // Take 5 readings and average them out
  for (int i = 0; i < 5; i++) {
    _avgdata += HTSMUXreadAnalogue(muxsensor);
    wait1Msec(50);
  }

  // Store new bias
  HTMAG_bias[SPORT(muxsensor)][MPORT(muxsensor)] = (_avgdata / 5);

  // Return new bias value
  return HTMAG_bias[SPORT(muxsensor)][MPORT(muxsensor)];
}
#endif // __HTSMUX_SUPPORT__


/**
 * Override the current bias for the sensor manually
 * @param link the HTMAG port number
 * @param bias the new bias to be used
 */
void HTMAGsetCal(tSensors link, int bias) {
  HTMAG_bias[link][0] = bias;
}


/**
 * Override the current bias for the sensor manually
 * @param muxsensor the SMUX sensor port number
 * @param bias the new bias to be used
 */
#ifdef __HTSMUX_SUPPORT__
void HTMAGsetCal(tMUXSensor muxsensor, int bias) {
  HTMAG_bias[SPORT(muxsensor)][MPORT(muxsensor)] = bias;
}
#endif // __HTSMUX_SUPPORT__


/**
 * Retrieve the current bias for the sensor
 * @param link the HTMAG port number
 * @return the bias value for the sensor
 */
int HTMAGreadCal(tSensors link) {
  return HTMAG_bias[link][0];
}


/**
 * Retrieve the current bias for the sensor
 * @param muxsensor the SMUX sensor port number
 * @return the bias value for the sensor
 */
#ifdef __HTSMUX_SUPPORT__
int HTMAGreadCal(tMUXSensor muxsensor) {
  return HTMAG_bias[SPORT(muxsensor)][MPORT(muxsensor)];
}
#endif // __HTSMUX_SUPPORT__


#endif // __HTMAG_H__

/*
 * $Id: hitechnic-magfield.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
