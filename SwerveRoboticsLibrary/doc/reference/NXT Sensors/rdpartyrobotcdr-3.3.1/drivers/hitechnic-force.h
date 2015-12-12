/*!@addtogroup HiTechnic
 * @{
 * @defgroup htforce Force Sensor
 * HiTechnic Force Sensor
 * @{
 */

/*
 * $Id: hitechnic-force.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef HTF
#define __HTF_H__
/** \file hitechnic-force.h
 * \brief HiTechnic Force Sensor driver
 *
 * hitechnic-force.h provides an API for the HiTechnic Force sensor.
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
 * \date 30 September 2012
 * \version 0.1
 * \example hitechnic-force-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

int HTFreadSensor(tSensors link);

#ifdef __HTSMUX_SUPPORT__
int HTFreadSensor(tMUXSensor muxsensor);
#endif

/**
 * Get the raw value from the sensor
 * @param link the HTF port number
 * @return raw value of the sensor
 */
int HTFreadSensor(tSensors link) {
  return 1023 - SensorRaw[link];
}


/**
 * Get the raw value from the sensor
 * @param muxsensor the SMUX sensor port number
 * @return raw value of the sensor
 */
#ifdef __HTSMUX_SUPPORT__
int HTFreadSensor(tMUXSensor muxsensor) {
  return 1023 - HTSMUXreadAnalogue(muxsensor);
}
#endif // __HTSMUX_SUPPORT__


#endif // __HTF_H__

/*
 * $Id: hitechnic-force.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
