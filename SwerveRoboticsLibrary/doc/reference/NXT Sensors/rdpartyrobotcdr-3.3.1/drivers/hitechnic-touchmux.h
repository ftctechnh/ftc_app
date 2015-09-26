/*!@addtogroup HiTechnic
 * @{
 * @defgroup httmux Touch Sensor MUX
 * HiTechnic Touch Sensor MUX
 * @{
 */

/*
 * $Id: hitechnic-touchmux.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTTMUX_H__
#define __HTTMUX_H__
/** \file hitechnic-touchmux.h
 * \brief HiTechnic Touch Sensor Multiplexer Sensor driver
 *
 * hitechnic-touchmux.h provides an API for the HiTechnic Touch Sensor Multiplexer Sensor.
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
 * \date 15 March 2009
 * \version 0.1
 * \example hitechnic-touchmux-test1.c
 */

#pragma systemFile

int HTTMUXgetActive(tSensors link);
bool HTTMUXisActive(tSensors link, int touch);

/**
 * Read the value of all of the currently connected touch sensors.  The status is logically OR'd
 * together. Touch 1 = 1, Touch 2 = 2, Touch 3 = 4, Touch 4 = 8.  If Touch 1 and 3 are active,
 * the return value will be 1 + 4 == 5.
 * @param link the HTTMUX port number
 * @return the value of the switches status
 */
int HTTMUXgetActive(tSensors link) {
  long muxvalue = 0;
  long switches = 0;

  // Make sure the sensor is configured as type sensorRawValue
  if (SensorType[link] != sensorRawValue) {
    SetSensorType(link, sensorRawValue);
    wait1Msec(100);
  }

  // Voodoo magic starts here.  This is taken straight from the Touch MUX pamphlet.
  // No small furry animals were hurt during the calculation of this algorithm.
  muxvalue = 1023 - SensorRaw[link];
  switches = 339 * muxvalue;
  switches /= (1023 - muxvalue);
  switches += 5;
  switches /= 10;

  return (int)switches;
}

/**
 * Read the value of specific touch sensor.
 * @param link the HTTMUX port number
 * @param touch the touch sensor to be checked, numbered 1 to 4.
 * @return the value of the switches status
 */
bool HTTMUXisActive(tSensors link, int touch) {
  if (HTTMUXgetActive(link) & (1 << (touch - 1)))
    return true;
  else
    return false;
}

#endif // __HTTMUX_H__

/*
 * $Id: hitechnic-touchmux.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
