/*!@addtogroup Dexter_Industries
 * @{
 * @defgroup dGPS GPS Sensor
 * Dexter Industries dGPS Sensor driver
 * @{
 */

/*
 * $Id: dexterind-gps.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __DGPS_H__
#define __DGPS_H__
/** \file dexterind-gps.h
 * \brief Dexter Industries GPS Sensor driver
 *
 * DGPS-driver.h provides an API for the Dexter Industries GPS Sensor.\n
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Added DGPSreadDistToDestination()
 * - 0.3: Changed from array structs to typedefs\n
 *        Fixed typos and ommissions in commands\n
 *
 * Credits:
 * - Big thanks to Dexter Industries for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 20 February 2011
 * \version 0.3
 * \example dexterind-gps-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define DGPS_I2C_ADDR   0x06      /*!< Barometric sensor device address */
#define DGPS_CMD_UTC    0x00      /*!< Fetch UTC */
#define DGPS_CMD_STATUS 0x01      /*!< Status of satellite link: 0 no link, 1 link */
#define DGPS_CMD_LAT    0x02      /*!< Fetch Latitude */
#define DGPS_CMD_LONG   0x04      /*!< Fetch Longitude */
#define DGPS_CMD_VELO   0x06      /*!< Fetch velocity in cm/s */
#define DGPS_CMD_HEAD   0x07      /*!< Fetch heading in degrees */
#define DGPS_CMD_DIST   0x08      /*!< Fetch distance to destination */
#define DGPS_CMD_ANGD   0x09      /*!< Fetch angle to destination */
#define DGPS_CMD_ANGR   0x0A      /*!< Fetch angle travelled since last request */
#define DGPS_CMD_SLAT   0x0B      /*!< Set latitude of destination */
#define DGPS_CMD_SLONG  0x0C      /*!< Set longitude of destination */


bool DGPSreadStatus(tSensors link);
long DGPSreadUTC(tSensors link);
long DGPSreadLatitude(tSensors link);
long DGPSreadLongitude(tSensors link);
int DGPSreadVelocity(tSensors link);
int DGPSreadHeading(tSensors link);
int DGPSreadRelHeading(tSensors link);
int DGPSreadTravHeading(tSensors link);
bool DGPSsetDestination(tSensors link, long latitude, long longitude);
int DGPSreadDistToDestination(tSensors link);

tByteArray DGPS_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray DGPS_I2CReply;      /*!< Array to hold I2C reply data */

long _DGPSreadRegister(tSensors link, unsigned byte command, int replysize) {
  memset(DGPS_I2CRequest, 0, sizeof(tByteArray));

  DGPS_I2CRequest[0] = 2;               // Message size
  DGPS_I2CRequest[1] = DGPS_I2C_ADDR;   // I2C Address
  DGPS_I2CRequest[2] = command;

  if (!writeI2C(link, DGPS_I2CRequest, DGPS_I2CReply, 4))
    return -1;

  // Reassemble the messages, depending on their expected size.
  if (replysize == 4)
    return (long)DGPS_I2CReply[3] + ((long)DGPS_I2CReply[2] << 8) + ((long)DGPS_I2CReply[1] << 16) + ((long)DGPS_I2CReply[0] << 24);
  else if (replysize == 3)
    return (long)DGPS_I2CReply[2] + ((long)DGPS_I2CReply[1] << 8) + ((long)DGPS_I2CReply[0] << 16);
  else if (replysize == 2)
    return (long)DGPS_I2CReply[1] + ((long)DGPS_I2CReply[0] << 8);
  else if (replysize == 1)
    return (long)DGPS_I2CReply[0];

  return 0;
}


bool DGPSreadStatus(tSensors link) {
  return (_DGPSreadRegister(link, DGPS_CMD_STATUS, 1) == 1) ? true : false;
}


/**
 * Read the time returned by the GPS in UTC.
 * @param link the DGPS port number
 * @return the time in UTC
 */
long DGPSreadUTC(tSensors link) {
  return _DGPSreadRegister(link, DGPS_CMD_UTC, 4);
}


/**
 * Read the current location's latitude in decimal degree format
 * @param link the DGPS port number
 * @return current latitude
 */
long DGPSreadLatitude(tSensors link) {
  return _DGPSreadRegister(link, DGPS_CMD_LAT, 4);
}


/**
 * Read the current location's longitude in decimal degree format
 * @param link the DGPS port number
 * @return current longitude
 */
long DGPSreadLongitude(tSensors link) {
  return _DGPSreadRegister(link, DGPS_CMD_LONG, 4);
}


/**
 * Read the current velocity in cm/s
 * @param link the DGPS port number
 * @return current velocity in cm/s
 */
int DGPSreadVelocity(tSensors link) {
  return _DGPSreadRegister(link, DGPS_CMD_VELO, 3);
}


/**
 * Read the current heading in degrees
 * @param link the DGPS port number
 * @return current heading in degrees
 */
int DGPSreadHeading(tSensors link) {
  return _DGPSreadRegister(link, DGPS_CMD_HEAD, 2);
}


/**
 * Angle to destination
 * @param link the DGPS port number
 * @return heading in degrees
 */
int DGPSreadRelHeading(tSensors link) {
  return _DGPSreadRegister(link, DGPS_CMD_ANGD, 2);
}


/**
 * Angle travelled since last request, resets the request coordinates on the
 * GPS sensor, sends the angle of travel since the last call
 * @param link the DGPS port number
 * @return heading in degrees
 */
int DGPSreadTravHeading(tSensors link) {
  return _DGPSreadRegister(link, DGPS_CMD_ANGR, 2);
}


/**
 * Set the destination coordinates
 * @param link the DGPS port number
 * @param latitude destination's latitude in decimal degrees
 * @param longitude destination's longitude in decimal degrees
 * @return true if no error occured, false if it did
 */
bool DGPSsetDestination(tSensors link, long latitude, long longitude) {
  memset(DGPS_I2CRequest, 0, sizeof(tByteArray));

  // First we send the latitude
  DGPS_I2CRequest[0] = 2;               // Message size
  DGPS_I2CRequest[1] = DGPS_I2C_ADDR;   // I2C Address
  DGPS_I2CRequest[2] = DGPS_CMD_SLAT;
  DGPS_I2CRequest[3] = (latitude >> 24) & 0xFF;
  DGPS_I2CRequest[4] = (latitude >> 16) & 0xFF;
  DGPS_I2CRequest[5] = (latitude >>  8) & 0xFF;
  DGPS_I2CRequest[6] = (latitude >>  0) & 0xFF;
  if (!writeI2C(link, DGPS_I2CRequest))
    return false;

  wait1Msec(100);

  // Then send longitude
  DGPS_I2CRequest[0] = 2;               // Message size
  DGPS_I2CRequest[1] = DGPS_I2C_ADDR;   // I2C Address
  DGPS_I2CRequest[2] = DGPS_CMD_SLONG;
  DGPS_I2CRequest[3] = (longitude >> 24) & 0xFF;
  DGPS_I2CRequest[4] = (longitude >> 16) & 0xFF;
  DGPS_I2CRequest[5] = (longitude >>  8) & 0xFF;
  DGPS_I2CRequest[6] = (longitude >>  0) & 0xFF;

  return writeI2C(link, DGPS_I2CRequest);
}


/**
 * Distance to destination in meters
 * @param link the DGPS port number
 * @return distance to destination in meters
 */
int DGPSreadDistToDestination(tSensors link) {
  return _DGPSreadRegister(link, DGPS_CMD_DIST, 4);
}

#endif // __DGPS_H__

/*
 * $Id: dexterind-gps.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
