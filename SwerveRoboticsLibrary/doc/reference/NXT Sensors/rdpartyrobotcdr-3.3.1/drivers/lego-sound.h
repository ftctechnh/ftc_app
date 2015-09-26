/*!@addtogroup lego
 * @{
 * @defgroup legosnd Sound Sensor
 * Sound Sensor
 * @{
 */

/*
 * $Id: lego-sound.h 133 2013-03-10 15:15:38Z xander $
 */

/** \file lego-sound.h
 * \brief SMUX driver for the Lego Sound sensor.
 *
 * lego-sound.h provides an API for the Lego Sound driver.
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
 * \example lego-sound-SMUX-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#ifdef __HTSMUX_SUPPORT__
int SNDreadRaw(tMUXSensor muxsensor);
int SNDreadNorm(tMUXSensor muxsensor);
void SNDsetDBA(tMUXSensor muxsensor);
void SNDsetDB(tMUXSensor muxsensor);

/**
 * Get the raw value from the sensor
 * @param muxsensor the SMUX sensor port number
 * @return the raw value of the Sound Sensor
 */
int SNDreadRaw(tMUXSensor muxsensor) {
  return 1023 - HTSMUXreadAnalogue(muxsensor);
}


/**
 * Get the processed value from the sensor.
 * @param muxsensor the SMUX sensor port number
 * @return processed value of the sensor or -1 if no valid range has been specified.
 */
int SNDreadNorm(tMUXSensor muxsensor) {
  int _val = ((long)SNDreadRaw(muxsensor) * (long)100) / (long)1024;
  return _val;
}


/**
 * Set the mode of the sensor to DBA, this is done
 * by switching off DIG0.
 * @param muxsensor the SMUX sensor port number
 */
void SNDsetDBA(tMUXSensor muxsensor) {
  HTSMUXsetAnalogueInactive(muxsensor);
}


/**
 * Set the mode of the sensor to DB, this is done
 * by switching on DIG0.
 * @param muxsensor the SMUX sensor port number
 */
void SNDsetDB(tMUXSensor muxsensor) {
  HTSMUXsetAnalogueActive(muxsensor);
}
#endif // __HTSMUX_SUPPORT__

/*
 * $Id: lego-sound.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
