/*!@addtogroup mindsensors
 * @{
 * @defgroup mstmux Touc Sensor MUX
 * Touc Sensor MUX
 * @{
 */

/*
 * $Id: mindsensors-touchmux.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSTMUX_HIGH___
#define __MSTMUX_HIGH___
/** \file mindsensors-touchmux.h
 * \brief Mindsensors Touch Multiplexer Sensor driver
 *
 * mindsensors-touchmux.h provides an API for the Mindsensors Touch Multiplexer Sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Added support for HiTechnic Sensor MUX
 *
 * Credits:
 * - Big thanks to HiTechnic and Mindsensors for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 24 March 2009
 * \version 0.2
 * \example mindsensors-touchmux-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

// Sensor values for each combo of buttons pressed
#define MSTMUX_LOW_1     60
#define MSTMUX_HIGH_1   160
#define MSTMUX_LOW_2    210
#define MSTMUX_HIGH_2   290
#define MSTMUX_LOW_12   370
#define MSTMUX_HIGH_12  460
#define MSTMUX_LOW_3    500
#define MSTMUX_HIGH_3   599
#define MSTMUX_LOW_13   600
#define MSTMUX_HIGH_13  658
#define MSTMUX_LOW_23   659
#define MSTMUX_HIGH_23  723
#define MSTMUX_LOW_123  724
#define MSTMUX_HIGH_123 800

#define MSTMUX_SMUX_LOW_1    163
#define MSTMUX_SMUX_HIGH_1   203
#define MSTMUX_SMUX_LOW_2    263
#define MSTMUX_SMUX_HIGH_2   303
#define MSTMUX_SMUX_LOW_12   373
#define MSTMUX_SMUX_HIGH_12  413
#define MSTMUX_SMUX_LOW_3    473
#define MSTMUX_SMUX_HIGH_3   513
#define MSTMUX_SMUX_LOW_13   523
#define MSTMUX_SMUX_HIGH_13  563
#define MSTMUX_SMUX_LOW_23   568
#define MSTMUX_SMUX_HIGH_23  598
#define MSTMUX_SMUX_LOW_123  603
#define MSTMUX_SMUX_HIGH_123 643


int MSTMUXgetActive(tSensors link);
bool MSTMUXisActive(tSensors link, int touch);

#ifdef __HTSMUX_SUPPORT__
int MSTMUXgetActive(tMUXSensor muxsensor);
bool MSTMUXisActive(tMUXSensor muxsensor, int touch);
#endif

/**
 * Read the value of all of the currently connected touch sensors.  The status is logically OR'd
 * together. Touch 1 = 1, Touch 2 = 2, Touch 3 = 4, Touch 4 = 8.  If Touch 1 and 3 are active,
 * the return value will be 1 + 4 == 5.
 * @param link the MSTMUX port number
 * @return the value of the switches status
 */
int MSTMUXgetActive(tSensors link) {

  // Make sure the sensor is configured as type sensorLightInactive
  if (SensorType[link] != sensorLightInactive) {
    SetSensorType(link, sensorLightInactive);
    wait1Msec(10);
  }

	int s;
  s = SensorRaw[link];

  if ( MSTMUX_LOW_1 < s && s < MSTMUX_HIGH_1 ) {
	  return 1;
	} else if ( MSTMUX_LOW_2 < s && s < MSTMUX_HIGH_2 ) {
	  return 2;
	} else if ( MSTMUX_LOW_3 < s && s < MSTMUX_HIGH_3 ) {
	  return 4;
	} else if ( MSTMUX_LOW_12 < s && s < MSTMUX_HIGH_12 ) {
	  return 1 + 2;
	} else if ( MSTMUX_LOW_13 < s && s < MSTMUX_HIGH_13 ) {
	  return 1 + 4;
	} else if ( MSTMUX_LOW_23 < s && s < MSTMUX_HIGH_23 ) {
	  return 2 + 4;
	} else if ( MSTMUX_LOW_123 < s && s < MSTMUX_HIGH_123 ) {
    return 1 + 2 + 4;
  } else {
    return 0;
  }
}


/**
 * Read the value of all of the currently connected touch sensors.  The status is logically OR'd
 * together. Touch 1 = 1, Touch 2 = 2, Touch 3 = 4, Touch 4 = 8.  If Touch 1 and 3 are active,
 * the return value will be 1 + 4 == 5.
 * @param muxsensor the SMUX sensor port number
 * @return the value of the switches status
 */
#ifdef __HTSMUX_SUPPORT__
int MSTMUXgetActive(tMUXSensor muxsensor) {

	int s;
  s = 1023 - HTSMUXreadAnalogue(muxsensor);

  if ( MSTMUX_SMUX_LOW_1 < s && s < MSTMUX_SMUX_HIGH_1 ) {
	  return 1;
	} else if ( MSTMUX_SMUX_LOW_2 < s && s < MSTMUX_SMUX_HIGH_2 ) {
	  return 2;
	} else if ( MSTMUX_SMUX_LOW_3 < s && s < MSTMUX_SMUX_HIGH_3 ) {
	  return 4;
	} else if ( MSTMUX_SMUX_LOW_12 < s && s < MSTMUX_SMUX_HIGH_12 ) {
	  return 1 + 2;
	} else if ( MSTMUX_SMUX_LOW_13 < s && s < MSTMUX_SMUX_HIGH_13 ) {
	  return 1 + 4;
	} else if ( MSTMUX_SMUX_LOW_23 < s && s < MSTMUX_SMUX_HIGH_23 ) {
	  return 2 + 4;
	} else if ( MSTMUX_SMUX_LOW_123 < s && s < MSTMUX_SMUX_HIGH_123 ) {
    return 1 + 2 + 4;
  } else {
    return 0;
  }
}
#endif // __HTSMUX_SUPPORT__


/**
 * Read the value of specific touch sensor.
 * @param link the MSTMUX port number
 * @param touch the touch sensor to be checked, numbered 1 to 4.
 * @return the value of the switches status
 */
bool MSTMUXisActive(tSensors link, int touch) {
  if (MSTMUXgetActive(link) & (1 << (touch - 1)))
    return true;
  else
    return false;
}


/**
 * Read the value of specific touch sensor.
 * @param muxsensor the SMUX sensor port number
 * @param touch the touch sensor to be checked, numbered 1 to 4.
 * @return the value of the switches status
 */
#ifdef __HTSMUX_SUPPORT__
bool MSTMUXisActive(tMUXSensor muxsensor, int touch) {
  if (MSTMUXgetActive(muxsensor) & (1 << (touch - 1)))
    return true;
  else
    return false;
}
#endif // __HTSMUX_SUPPORT__


#endif // __MSTMUX_HIGH___

/*
 * $Id: mindsensors-touchmux.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
