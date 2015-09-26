/*!@addtogroup HiTechnic
 * @{
 * @defgroup hteopd EOPD Sensor
 * HiTechnic EOPD Sensor
 * @{
 */

/*
 * $Id: hitechnic-eopd.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTEOPD_H__
#define __HTEOPD_H__
/** \file hitechnic-eopd.h
 * \brief HiTechnic EOPD Sensor driver
 *
 * hitechnic-eopd.h provides an API for the HiTechnic EOPD sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Removed HTEOPDsetRange() and HTEOPDgetRange(), not really necessary
 *        Changed the way raw value is calculated due to sensor type change
 * - 0.3: Renamed HTEOPDgetRaw to HTEOPDreadRaw
 *        Renamed HTEOPDgetProcessed to HTEOPDreadProcessed
 *        Added SMUX functions
 * - 0.4: Added No Wait versions of HTEOPDsetShortRange and HTEOPDsetLongRange for non-SMUX functions
 *        Changed the underlying sensor types for RobotC 1.57A and higher.
 * - 0.5: Now only supports ROBOTC 2.00<br>
 *        Make use of the new analogue sensor calls for SMUX sensors in common.h
 * - 0.6: Replaced array structs with typedefs
 *
 * Credits:
 * - Big thanks to HiTechnic for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 20 February 2011
 * \version 0.6
 * \example hitechnic-eopd-test1.c
 * \example hitechnic-eopd-SMUX-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

// This ensures the correct sensor types are used.
TSensorTypes HTEOPDLRType = sensorAnalogActive;
TSensorTypes HTEOPDSRType = sensorAnalogInactive;

int HTEOPDreadRaw(tSensors link);
int HTEOPDreadProcessed(tSensors link);
void HTEOPDsetShortRange(tSensors link);
void HTEOPDsetLongRange(tSensors link);

#ifdef __HTSMUX_SUPPORT__
int HTEOPDreadRaw(tMUXSensor muxsensor);
int HTEOPDreadProcessed(tMUXSensor muxsensor);
void HTEOPDsetShortRange(tMUXSensor muxsensor);
void HTEOPDsetLongRange(tMUXSensor muxsensor);
#endif

/**
 * Get the raw value from the sensor
 * @param link the HTEOPD port number
 * @return raw value of the sensor
 */
int HTEOPDreadRaw(tSensors link) {
  return 1023 - SensorRaw[link];
}


/**
 * Get the raw value from the sensor
 * @param muxsensor the SMUX sensor port number
 * @return raw value of the sensor
 */
#ifdef __HTSMUX_SUPPORT__
int HTEOPDreadRaw(tMUXSensor muxsensor) {
  return 1023 - HTSMUXreadAnalogue(muxsensor);
}
#endif // __HTSMUX_SUPPORT__


/**
 * Get the processed value from the sensor. This is obtained by using sqrt(raw value * 10)
 * @param link the HTEOPD port number
 * @return processed value of the sensor
 */
int HTEOPDreadProcessed(tSensors link) {
  int _val = sqrt(HTEOPDreadRaw(link) * 10);
  return _val;
}


/**
 * Get the processed value from the sensor. This is obtained by using sqrt(raw value * 10)
 * @param muxsensor the SMUX sensor port number
 * @return processed value of the sensor
 */
#ifdef __HTSMUX_SUPPORT__
int HTEOPDreadProcessed(tMUXSensor muxsensor) {
  int _val = sqrt((long)HTEOPDreadRaw(muxsensor) * (long)10);
  return _val;
}
#endif // __HTSMUX_SUPPORT__


/**
 * Set the range of the sensor to short range, this is done
 * by configuring the sensor as sensorRawValue
 * @param link the HTEOPD port number
 */
void HTEOPDsetShortRange(tSensors link) {
  SetSensorType(link, HTEOPDSRType);
}


/**
 * Set the range of the sensor to short range, this is done
 * by switching off dig0
 * @param muxsensor the SMUX sensor port number
 */
#ifdef __HTSMUX_SUPPORT__
void HTEOPDsetShortRange(tMUXSensor muxsensor) {
  HTSMUXsetAnalogueInactive(muxsensor);
}
#endif // __HTSMUX_SUPPORT__


/**
 * Set the range of the sensor to long range, this is done
 * by configuring the sensor as sensorLightActive and setting
 * it to modeRaw
 * @param link the HTEOPD port number
 */
void HTEOPDsetLongRange(tSensors link) {
  SetSensorType(link, HTEOPDLRType);
}


/**
 * Set the range of the sensor to long range, this is done
 * by setting dig0 high (1).
 * @param muxsensor the SMUX sensor port number
 */
#ifdef __HTSMUX_SUPPORT__
void HTEOPDsetLongRange(tMUXSensor muxsensor) {
  HTSMUXsetAnalogueActive(muxsensor);
}
#endif // __HTSMUX_SUPPORT__

#endif // __HTEOPD_H__

/*
 * $Id: hitechnic-eopd.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
