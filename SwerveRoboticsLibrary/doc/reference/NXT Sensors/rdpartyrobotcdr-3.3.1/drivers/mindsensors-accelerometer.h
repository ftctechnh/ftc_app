/*!@addtogroup mindsensors
 * @{
 * @defgroup accelnx ACCEL-nx Sensor
 * ACCEL-nx Sensor
 * @{
 */

/*
 * $Id: mindsensors-accelerometer.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSAC_H__
#define __MSAC_H__
/** \file mindsensors-accelerometer.h
 * \brief Mindsensors ACCEL-nx driver
 *
 * mindsensors-accelerometer.h provides an API for the Mindsensors ACCEL-nx driver
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Added defines for ranges (MSAC_RANGE_2_5 ... MSAC_RANGE_10)<br>
 *        Removed ubyteToInt() calls.
 *
 * Credits:
 * - Big thanks to Mindsensors for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 28 November 2009
 * \version 0.2
 * \example mindsensors-accelerometer-test1.c
 * \example mindsensors-accelerometer-test2.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define MSAC_I2C_ADDR       0x02  /*!< MSAC I2C device address */

#define MSAC_CMD            0x41  /*!< MSAC command register */

#define MSAC_X_TILT         0x42  /*!< MSAC X tilt data */
#define MSAC_Y_TILT         0x43  /*!< MSAC Y tilt data */
#define MSAC_Z_TILT         0x44  /*!< MSAC Z tilt data */

#define MSAC_X_ACCEL        0x45  /*!< MSAC Z acceleration data */
#define MSAC_Y_ACCEL        0x47  /*!< MSAC Z acceleration data */
#define MSAC_Z_ACCEL        0x49  /*!< MSAC Z acceleration data */

#define MSAC_RANGE_2_5      1     /*!< Acceleration up to 2.5G */
#define MSAC_RANGE_3_3      2     /*!< Acceleration up to 3.3G */
#define MSAC_RANGE_6_7      3     /*!< Acceleration up to 6.7G */
#define MSAC_RANGE_10       4     /*!< Acceleration up to 10G */

bool MSACreadTilt(tSensors link, int &x_tilt, int &y_tilt, int &z_tilt);
bool MSACreadAccel(tSensors link, int &x_accel, int &y_accel, int &z_accel);
bool MSACsendCmd(tSensors link, byte command);
bool MSACsetRange(tSensors link, int range);

tByteArray MSAC_I2CRequest;       /*!< Array to hold I2C command data */
tByteArray MSAC_I2CReply;         /*!< Array to hold I2C reply data */


/**
 * Read tilt data from the sensor
 * @param link the sensor port number
 * @param x_tilt X tilt data
 * @param y_tilt Y tilt data
 * @param z_tilt Z tilt data
 * @return true if no error occured, false if it did
 */
bool MSACreadTilt(tSensors link, int &x_tilt, int &y_tilt, int &z_tilt) {
  memset(MSAC_I2CRequest, 0, sizeof(tByteArray));

  MSAC_I2CRequest[0] = 2;               // Number of bytes in I2C command
  MSAC_I2CRequest[1] = MSAC_I2C_ADDR;   // I2C address of accel sensor
  MSAC_I2CRequest[2] = MSAC_X_TILT;     // Set write address to sensor mode register

  if (!writeI2C(link, MSAC_I2CRequest, MSAC_I2CReply, 3))
    return false;

  x_tilt = MSAC_I2CReply[0] - 128;
	y_tilt = MSAC_I2CReply[1] - 128;
	z_tilt = MSAC_I2CReply[2] - 128;

  return true;
}


/**
 * Read tilt data from the sensor
 * @param link the sensor port number
 * @param x_accel X acceleration data
 * @param y_accel Y acceleration data
 * @param z_accel Z acceleration data
 * @return true if no error occured, false if it did
 */
bool MSACreadAccel(tSensors link, int &x_accel, int &y_accel, int &z_accel) {
  memset(MSAC_I2CRequest, 0, sizeof(tByteArray));

  MSAC_I2CRequest[0] = 2;               // Number of bytes in I2C command
  MSAC_I2CRequest[1] = MSAC_I2C_ADDR;   // I2C address of accel sensor
  MSAC_I2CRequest[2] = MSAC_X_ACCEL;    // Set write address to sensor mode register

  if (!writeI2C(link, MSAC_I2CRequest, MSAC_I2CReply, 6))
    return false;

  // Each result is made up of two bytes.
	x_accel = MSAC_I2CReply[0] + (MSAC_I2CReply[1] << 8);
	y_accel = MSAC_I2CReply[2] + (MSAC_I2CReply[3] << 8);
	z_accel = MSAC_I2CReply[4] + (MSAC_I2CReply[5] << 8);
  return true;
}


/**
 * Send a command to the sensor
 * @param link the sensor port number
 * @param command the command to be sent
 * @return true if no error occured, false if it did
 */
bool MSACsendCmd(tSensors link, byte command) {
  memset(MSAC_I2CRequest, 0, sizeof(tByteArray));

  MSAC_I2CRequest[0] = 3;               // Number of bytes in I2C command
  MSAC_I2CRequest[1] = MSAC_I2C_ADDR;   // I2C address of accel sensor
  MSAC_I2CRequest[2] = MSAC_CMD;        // Set write address to sensor mode register
  MSAC_I2CRequest[3] = command;         // Command to be sent to the sensor

  return writeI2C(link, MSAC_I2CRequest);
}


/**
 * Set sensitivity range of sensor.
 * @param link the sensor port number
 * @param range 1 = 2.5G, 2 = 3.3G, 3 = 6.7G, 4 = 10G
 * @return true if no error occured, false if it did
 */
bool MSACsetRange(tSensors link, int range) {
  byte command = 0;

  switch (range) {
    case 1: command = '1'; break;
    case 2: command = '2'; break;
    case 3: command = '3'; break;
    case 4: command = '4'; break;
    default: return false; break;
  }
  return MSACsendCmd(link, command);
}

#endif //__MSAC_H__

/*
 * $Id: mindsensors-accelerometer.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
