/*!@addtogroup mindsensors
 * @{
 * @defgroup MSSUMO Sumo Eyes Sensor
 * Mindsensors Sumo Eyes Sensor
 * @{
 */

/*
 * $Id: mindsensors-sumoeyes.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSSUMO_H__
#define __MSSUMO_H__
/** \file mindsensors-sumoeyes.h
 * \brief Mindsensors Sumo Eyes Sensor driver
 *
 * mindsensors-sumoeyes.h provides an API for the Mindsensors Sumo Eyes sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 *
 * Credits:
 * - Big thanks to Mindsensors for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 30 October 2011
 * \version 0.1
 * \example mindsensors-sumoeyes-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

// This ensures the correct sensor types are used.
TSensorTypes MSSUMOLRType = sensorLightInactive;
TSensorTypes MSSUMOSRType = sensorLightActive;

typedef enum {
  MSSUMO_NONE = 0,
  MSSUMO_FRONT = 1,
  MSSUMO_LEFT = 2,
  MSSUMO_RIGHT = 3
} tObstacleZone;

tObstacleZone MSSUMOreadZone(tSensors link);
void MSSUMOsetShortRange(tSensors link);
void MSSUMOsetLongRange(tSensors link);

/**
 * Get the raw value from the sensor
 * @param link the MSSUMO port number
 * @return raw value of the sensor
 */
tObstacleZone MSSUMOreadZone(tSensors link) {
  int sensordata = 0;

  sensordata = SensorValue[link];

 	if ( sensordata > 30 && sensordata < 36 )
 	{
	  // obstacle is on left
    return (MSSUMO_LEFT);
	}
	else if ( sensordata > 63 && sensordata < 69 )
	{
	  // obstacle is on right
    return (MSSUMO_RIGHT);
	}
	else if ( sensordata >= 74 && sensordata <= 80 )
	{
	  // obstacle is in front.
    return (MSSUMO_FRONT);
	}
	else
	{
	  return (MSSUMO_NONE);
	}
}


/**
 * Set the range of the sensor to short range, this is done
 * by configuring the sensor as sensorLightActive.
 * @param link the MSSUMO port number
 */
void MSSUMOsetShortRange(tSensors link) {
  if (SensorType[link] != MSSUMOSRType)
    SetSensorType(link, MSSUMOSRType);
}


/**
 * Set the range of the sensor to long range, this is done
 * by configuring the sensor as sensorLightInactive
 * @param link the MSSUMO port number
 */
void MSSUMOsetLongRange(tSensors link) {
  if (SensorType[link] != MSSUMOLRType)
    SetSensorType(link, MSSUMOLRType);
}


#endif // __MSSUMO_H__

/*
 * $Id: mindsensors-sumoeyes.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
