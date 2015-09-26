/*!@addtogroup mindsensors
 * @{
 * @defgroup msdist DIST-Nx Sensor
 * DIST-Nx Sensor
 * @{
 */

/*
 * $Id: mindsensors-irdist.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSDIST_H__
#define __MSDIST_H__
/** \file mindsensors-irdist.h
 * \brief Mindsensors DIST-Nx driver
 *
 * mindsensors-irdist.h provides an API for the Mindsensors DIST-Nx sensor
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: More comments
 * - 0.3: Sensor now auto-configures the type
 * - 0.4: Allow I2C address to be specified as an optional argument
 *
 * Credits:
 * - Big thanks to Mindsensors for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 18 December 2010
 * \version 0.4
 * \example mindsensors-irdist-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define MSDIST_I2C_ADDR   0x02  /*!< MSDIST I2C device address */

#define MSDIST_CMD        0x41  /*!< MSDIST command register */

#define MSDIST_DIST       0x42  /*!< MSDIST distance data */
#define MSDIST_VOLT       0x44  /*!< MSDIST voltage data */
#define MSDIST_MOD_TYPE   0x50  /*!< MSDIST Sharp module type */
#define MSDIST_MINDIST    0x52  /*!< MSDIST minimum distance in mm */
#define MSDIST_MAXDIST    0x54  /*!< MSDIST maximum distance in mm */

#define MSDIST_GP2D12     0x31  /*!< Sharp IR module GP2D12 */
#define MSDIST_GP2D120    0x32  /*!< Sharp IR module GP2D120 */
#define MSDIST_GP2YA21    0x33  /*!< Sharp IR module GP2YA21 */
#define MSDIST_GP2YA02    0x34  /*!< Sharp IR module GP2YA02 */
#define MSDIST_CUSTOM     0x35  /*!< Custom IR module */

int MSDISTreadDist(tSensors link, ubyte address = MSDIST_I2C_ADDR);
int MSDISTreadVoltage(tSensors link, ubyte address = MSDIST_I2C_ADDR);
int MSDISTreadMinDist(tSensors link, ubyte address = MSDIST_I2C_ADDR);
int MSDISTreadMaxDist(tSensors link, ubyte address = MSDIST_I2C_ADDR);
int MSDISTreadModuleType(tSensors link, ubyte address = MSDIST_I2C_ADDR);
bool MSDISTsendCmd(tSensors link, byte command, ubyte address = MSDIST_I2C_ADDR);

tByteArray MSDIST_I2CRequest;       /*!< Array to hold I2C command data */
tByteArray MSDIST_I2CReply;         /*!< Array to hold I2C reply data */

bool MSDISTcalibrated[] = {false, false, false, false};  /*!< Has the sensor been calibrated yet? */

/**
 * Read the distance from the sensor
 * @param link the sensor port number
 * @param address the I2C address to use, optional, defaults to 0x02
 * @return distance to object or -1 if an error occurred
 */
int MSDISTreadDist(tSensors link, ubyte address) {

  // Configure the sensor
  if (!MSDISTcalibrated[link]) {
    if (!MSDISTsendCmd(link, MSDISTreadModuleType(link),address))
    // if (!MSDISTsendCmd(link, MSDISTreadModuleType(link)))
      return -1;
    else
      MSDISTcalibrated[link] = true;
  }

  memset(MSDIST_I2CRequest, 0, sizeof(tByteArray));

  MSDIST_I2CRequest[0] = 2;               // Number of bytes in I2C command
  MSDIST_I2CRequest[1] = address;         // I2C address of sensor
  MSDIST_I2CRequest[2] = MSDIST_DIST;     // Set write address to sensor mode register

  if (!writeI2C(link, MSDIST_I2CRequest, MSDIST_I2CReply, 2))
    return -1;

  return (0x00FF & MSDIST_I2CReply[0]) + ((0x00FF & MSDIST_I2CReply[1]) <<8);
}


/**
 * Read tilt data from the sensor
 * @param link the sensor port number
 * @param address the I2C address to use, optional, defaults to 0x02
 * @return voltage reading from IR Sensor -1 if an error occurred
 */
int MSDISTreadVoltage(tSensors link, ubyte address) {
  memset(MSDIST_I2CRequest, 0, sizeof(tByteArray));

  MSDIST_I2CRequest[0] = 2;               // Number of bytes in I2C command
  MSDIST_I2CRequest[1] = address;         // I2C address of sensor
  MSDIST_I2CRequest[2] = MSDIST_VOLT;     // Set write address to sensor mode register

  if (!writeI2C(link, MSDIST_I2CRequest, MSDIST_I2CReply, 2))
    return -1;

  // Each result is made up of two bytes.
  return (0x00FF & MSDIST_I2CReply[0]) + ((0x00FF & MSDIST_I2CReply[1]) <<8);
}


/**
 * Read minumum measuring distance from the sensor
 * @param link the sensor port number
 * @param address the I2C address to use, optional, defaults to 0x02
 * @return minumum measuring distance from the sensor -1 if an error occurred
 */
int MSDISTreadMinDist(tSensors link, ubyte address) {
  memset(MSDIST_I2CRequest, 0, sizeof(tByteArray));

  MSDIST_I2CRequest[0] = 2;               // Number of bytes in I2C command
  MSDIST_I2CRequest[1] = address;         // I2C address of sensor
  MSDIST_I2CRequest[2] = MSDIST_MINDIST;  // Set write address to sensor mode register

  if (!writeI2C(link, MSDIST_I2CRequest, MSDIST_I2CReply, 2))
    return -1;

  // Each result is made up of two bytes.
  return (0x00FF & MSDIST_I2CReply[0]) + ((0x00FF & MSDIST_I2CReply[1]) <<8);
}


/**
 * Read maximum measuring distance from the sensor
 * @param link the sensor port number
 * @param address the I2C address to use, optional, defaults to 0x02
 * @return maximum measuring distance from the sensor -1 if an error occurred
 */
int MSDISTreadMaxDist(tSensors link, ubyte address) {
  memset(MSDIST_I2CRequest, 0, sizeof(tByteArray));

  MSDIST_I2CRequest[0] = 2;               // Number of bytes in I2C command
  MSDIST_I2CRequest[1] = address;         // I2C address of sensor
  MSDIST_I2CRequest[2] = MSDIST_MAXDIST;     // Set write address to sensor mode register

  if (!writeI2C(link, MSDIST_I2CRequest, MSDIST_I2CReply, 2))
    return -1;

  // Each result is made up of two bytes.
  return (0x00FF & MSDIST_I2CReply[0]) + ((0x00FF & MSDIST_I2CReply[1]) <<8);
}


/**
 * Read Sharp IR module type from the sensor
 * @param link the sensor port number
 * @param address the I2C address to use, optional, defaults to 0x02
 * @return Sharp IR module type from the sensor -1 if an error occurred
 */
int MSDISTreadModuleType(tSensors link, ubyte address) {
  memset(MSDIST_I2CRequest, 0, sizeof(tByteArray));

  MSDIST_I2CRequest[0] = 2;               // Number of bytes in I2C command
  MSDIST_I2CRequest[1] = address;         // I2C address of sensor
  MSDIST_I2CRequest[2] = MSDIST_MOD_TYPE; // Set write address to sensor mode register

  if (!writeI2C(link, MSDIST_I2CRequest, MSDIST_I2CReply, 1))
    return -1;

  return 0x00FF & MSDIST_I2CReply[0];
}


/**
 * Send a command to the sensor
 * @param link the sensor port number
 * @param command the command to be sent
 * @param address the I2C address to use, optional, defaults to 0x02
 * @return true if no error occured, false if it did
 */
bool MSDISTsendCmd(tSensors link, byte command, ubyte address) {
  memset(MSDIST_I2CRequest, 0, sizeof(tByteArray));

  MSDIST_I2CRequest[0] = 3;               // Number of bytes in I2C command
  MSDIST_I2CRequest[1] = address;         // I2C address of sensor
  MSDIST_I2CRequest[2] = MSDIST_CMD;      // Set write address to sensor mode register
  MSDIST_I2CRequest[3] = command;         // Command to be sent to the sensor

  return writeI2C(link, MSDIST_I2CRequest);
}


#endif //__MSDIST_H__

/*
 * $Id: mindsensors-irdist.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
