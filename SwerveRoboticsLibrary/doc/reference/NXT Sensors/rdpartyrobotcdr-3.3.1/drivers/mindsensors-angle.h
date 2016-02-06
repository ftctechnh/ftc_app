/*!@addtogroup mindsensors
 * @{
 * @defgroup msang Angle Sensor
 * GlideWheel-AS Angle Sensor
 * @{
 */

/*
 * $Id: mindsensors-angle.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSANG_H__
#define __MSANG_H__
/** \file mindsensors-angle.h
 * \brief Mindsensors Angle Sensor driver
 *
 * mindsensors-angle.h provides an API for the Mindsensors Angle Sensor.
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
 * \date 20 February 2011
 * \version 0.1
 * \example mindsensors-angle-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define MSANG_I2C_ADDR         0x30      /*!< HTCS2 I2C device address */
#define MSANG_CMD_REG          0x41      /*!< Command register */
#define MSANG_OFFSET           0x42      /*!< Offset for data registers */

// Values contained by registers in active mode
#define MSANG_ANG              0x00      /*!< Current angle */
#define MSANG_RAW              0x04      /*!< Current sensor raw value (0.5 deg accuracy) */
#define MSANG_RPM              0x08      /*!< rpms */

// Various commands
#define MSANG_CMD_RST_ANG      0x72      /*!< Resets 0 position to current shaft angle */

int MSANGreadAngle(tSensors link);
int MSANGreadRPM(tSensors link);
int MSANGreadRaw(tSensors link);
bool MSANGresetAngle(tSensors link);

tByteArray MSANG_I2CRequest;             /*!< Array to hold I2C command data */
tByteArray MSANG_I2CReply;               /*!< Array to hold I2C reply data */


/**
 * Return the current angle
 * @param link the MSANG port number
 * @return current angle or -1 if an error occurred.
 */
int MSANGreadAngle(tSensors link) {
  memset(MSANG_I2CRequest, 0, sizeof(tByteArray));

  MSANG_I2CRequest[0] = 2;                         // Message size
  MSANG_I2CRequest[1] = MSANG_I2C_ADDR;            // I2C Address
  MSANG_I2CRequest[2] = MSANG_OFFSET + MSANG_ANG; // Start Current angle

  if (!writeI2C(link, MSANG_I2CRequest, MSANG_I2CReply, 4))
    return -1;

  return MSANG_I2CReply[0] + (MSANG_I2CReply[1] << 8) + (MSANG_I2CReply[2] << 16) + (MSANG_I2CReply[3] << 24);
}


/**
 * Return the current raw sensor value
 * @param link the MSANG port number
 * @return current raw value or -1 if an error occurred.
 */
int MSANGreadRaw(tSensors link) {
  memset(MSANG_I2CRequest, 0, sizeof(tByteArray));

  MSANG_I2CRequest[0] = 2;                         // Message size
  MSANG_I2CRequest[1] = MSANG_I2C_ADDR;            // I2C Address
  MSANG_I2CRequest[2] = MSANG_OFFSET + MSANG_RAW; // Start Current angle

  if (!writeI2C(link, MSANG_I2CRequest, MSANG_I2CReply, 4))
    return -1;

  return MSANG_I2CReply[0] + (MSANG_I2CReply[1] << 8) + (MSANG_I2CReply[2] << 16) + (MSANG_I2CReply[3] << 24);
}


/**
 * Return the rpm that the shaft is currently rotating at
 * @param link the MSANG port number
 * @return the current rpm of the shaft or -1 if an error occurred.
 */
int MSANGreadRPM(tSensors link) {
  memset(MSANG_I2CRequest, 0, sizeof(tByteArray));

  MSANG_I2CRequest[0] = 2;                           // Message size
  MSANG_I2CRequest[1] = MSANG_I2C_ADDR;              // I2C Address
  MSANG_I2CRequest[2] = MSANG_OFFSET + MSANG_RPM;    // Start of rpm

  if (!writeI2C(link, MSANG_I2CRequest, MSANG_I2CReply, 2))
    return -1;

  return (MSANG_I2CReply[1] <<  8) + MSANG_I2CReply[0];
}


/**
 * Reset the 0 position to the current shaft angle.
 * @param link the MSANG port number
 * @return true if no error occured, false if it did
 */
bool MSANGresetAngle(tSensors link) {
  memset(MSANG_I2CRequest, 0, sizeof(tByteArray));

  MSANG_I2CRequest[0] = 3;                 // Message size
  MSANG_I2CRequest[1] = MSANG_I2C_ADDR;    // I2C Address
  MSANG_I2CRequest[2] = MSANG_CMD_REG;     // Command register
  MSANG_I2CRequest[3] = MSANG_CMD_RST_ANG; // Command to be sent

  return writeI2C(link, MSANG_I2CRequest);
}


#endif // __MSANG_H__

 /*
 * $Id: mindsensors-angle.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
