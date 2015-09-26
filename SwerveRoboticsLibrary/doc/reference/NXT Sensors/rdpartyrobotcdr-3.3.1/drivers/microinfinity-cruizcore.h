/*!@addtogroup other
 * @{
 * @defgroup cruizcore CruizCore XG1300L Sensor
 * CruizCore XG1300L
 * @{
 */

/*
 * $Id: microinfinity-cruizcore.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MICC_H__
#define __MICC_H__
/** \file microinfinity-cruizcore.h
 * \brief MicroInfinity CruizCore XG1300L driver
 *
 * microinfinity-cruizcore.h provides an API for the MicroInfinity CruizCore XG1300L sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 *
 * Credits:
 * - Big thanks to MicroInfinity for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 29 May 2011
 * \version 0.1
 * \example microinfinity-cruizcore-test1.c
 * \example microinfinity-cruizcore-test2.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define MICC_I2C_ADDR       0x02  /*!< MICC I2C device address */

#define MICC_ACC_ANG        0x42  /*!< MICC Accumulated angle (2 bytes) */

#define MICC_TURN_RATE      0x44  /*!< MICC Rate of Turn (2 bytes) */

#define MICC_X_ACCEL        0x46  /*!< MICC X acceleration data (2 bytes) */
#define MICC_Y_ACCEL        0x48  /*!< MICC Y acceleration data (2 bytes) */
#define MICC_Z_ACCEL        0x4A  /*!< MICC Z acceleration data (2 bytes) */

#define MICC_CMD_RESET      0x60  /*!< MICC Reset the device */
#define MICC_CMD_RANGE_2G   0x61  /*!< MICC Acceleration up to 2G */
#define MICC_CMD_RANGE_4G   0x62  /*!< MICC Acceleration up to 4G */
#define MICC_CMD_RANGE_8G   0x63  /*!< MICC Acceleration up to 8G */

int MICCreadRelativeHeading(tSensors link);
int MICCreadTurnRate(tSensors link);
bool MICCreadAccel(tSensors link, int &x_accel, int &y_accel, int &z_accel);
bool MICCsendCmd(tSensors link, ubyte command);

#define MICCsetRange2G(x) MICCsendCmd(x, MICC_CMD_RANGE_2G) /*!< Macro for setting sensor to 2G range */
#define MICCsetRange4G(x) MICCsendCmd(x, MICC_CMD_RANGE_4G) /*!< Macro for setting sensor to 4G range */
#define MICCsetRange8G(x) MICCsendCmd(x, MICC_CMD_RANGE_8G) /*!< Macro for setting sensor to 8G range */
#define MICCreset(x)      MICCsendCmd(x, MICC_CMD_RESET)    /*!< Macro for resetting sensor */

tByteArray MICC_I2CRequest;       /*!< Array to hold I2C command data */
tByteArray MICC_I2CReply;         /*!< Array to hold I2C reply data */


/**
 * Return the current relative heading, value between -179 and 180 degrees.<br>
 * Angle is measured in 100th degrees.  So 12899 = 128.99 degrees.
 * @return the relative heading
 */
int MICCreadRelativeHeading(tSensors link) {
  memset(MICC_I2CRequest, 0, sizeof(tByteArray));

  MICC_I2CRequest[0] = 2;               // Number of bytes in I2C command
  MICC_I2CRequest[1] = MICC_I2C_ADDR;   // I2C address of accel sensor
  MICC_I2CRequest[2] = MICC_ACC_ANG;    // Set write address to sensor mode register

  if (!writeI2C(link, MICC_I2CRequest, MICC_I2CReply, 2))
    return 0;

  // Each result is made up of two bytes.
	return (MICC_I2CReply[1] << 8) + MICC_I2CReply[0];
}


/**
 * Return the Rate of Turn in degrees per second
 * @return the current rate of turn
 */
int MICCreadTurnRate(tSensors link) {
  memset(MICC_I2CRequest, 0, sizeof(tByteArray));

  MICC_I2CRequest[0] = 2;               // Number of bytes in I2C command
  MICC_I2CRequest[1] = MICC_I2C_ADDR;   // I2C address of accel sensor
  MICC_I2CRequest[2] = MICC_TURN_RATE;    // Set write address to sensor mode register

  if (!writeI2C(link, MICC_I2CRequest, MICC_I2CReply, 2))
    return 0;

  // Each result is made up of two bytes.
	return (MICC_I2CReply[1] << 8) + MICC_I2CReply[0];
}

/**
 * Read acceleration data from the sensor
 * @param link the sensor port number
 * @param x_accel X acceleration data
 * @param y_accel Y acceleration data
 * @param z_accel Z acceleration data
 * @return true if no error occured, false if it did
 */
bool MICCreadAccel(tSensors link, int &x_accel, int &y_accel, int &z_accel) {
  memset(MICC_I2CRequest, 0, sizeof(tByteArray));

  MICC_I2CRequest[0] = 2;               // Number of bytes in I2C command
  MICC_I2CRequest[1] = MICC_I2C_ADDR;   // I2C address of accel sensor
  MICC_I2CRequest[2] = MICC_X_ACCEL;    // Set write address to sensor mode register

  if (!writeI2C(link, MICC_I2CRequest, MICC_I2CReply, 6))
    return false;

  // Each result is made up of two bytes.
	x_accel = (MICC_I2CReply[1] << 8) + MICC_I2CReply[0];
	y_accel = (MICC_I2CReply[3] << 8) + MICC_I2CReply[2];
	z_accel = (MICC_I2CReply[5] << 8) + MICC_I2CReply[4];
  return true;
}


/**
 * Send a command to the sensor
 * @param link the sensor port number
 * @param command the command to be sent
 * @return true if no error occured, false if it did
 */
bool MICCsendCmd(tSensors link, ubyte command) {
  memset(MICC_I2CRequest, 0, sizeof(tByteArray));

  MICC_I2CRequest[0] = 2;               // Number of bytes in I2C command
  MICC_I2CRequest[1] = MICC_I2C_ADDR;   // I2C address of accel sensor
  MICC_I2CRequest[2] = command;         // Set write address to sensor mode register

  return writeI2C(link, MICC_I2CRequest);
}

#endif //__MICC_H__

/*
 * $Id: microinfinity-cruizcore.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
