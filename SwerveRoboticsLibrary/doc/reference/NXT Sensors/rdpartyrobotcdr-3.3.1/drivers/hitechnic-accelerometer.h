/*!@addtogroup HiTechnic
 * @{
 * @defgroup htacc Acceleration Sensor
 * HiTechnic Acceleration Sensor
 * @{
 */

/*
 * $Id: hitechnic-accelerometer.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTAC_H__
#define __HTAC_H__
/** \file hitechnic-accelerometer.h
 * \brief HiTechnic Acceleration Sensor driver
 *
 * hitechnic-accelerometer.h provides an API for the HiTechnic Acceleration Sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Fixed bad registers<br>
 *        Added HTACreadAllAxes(tSensors link, int &x, int &y, int &z)<br>
 *        Removed HTACreadAllAxes(tSensors link, tIntArray &data)<br>
 *        Changed HTACreadX, Y, Z to use by reference instead of as return value<br>
 * - 0.3: SMUX functions added.
 * - 0.4: Removed HTAC_SMUXData, reused HTAC_I2CReply to save memory
 * - 0.5: Use new calls in common.h that don't require SPORT/MPORT macros<br>
 *        Fixed massive bug in HTACreadAllAxes() in the way values are calculated
 * - 0.6: Removed single axis functions
 * - 0.7: Replaced array structs with typedefs
 *
 * Credits:
 * - Big thanks to HiTechnic for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 20 February 2011
 * \version 0.7
 * \example hitechnic-accelerometer-test1.c
 * \example hitechnic-accelerometer-SMUX-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define HTAC_I2C_ADDR  0x02      /*!< IR Seeker I2C device address */
#define HTAC_OFFSET    0x42      /*!< Offset for data registers */
#define HTAC_X_UP      0x00      /*!< X axis upper 8 bits */
#define HTAC_Y_UP      0x01      /*!< Y axis upper 8 bits */
#define HTAC_Z_UP      0x02      /*!< Z axis upper 8 bits */
#define HTAC_X_LOW     0x03      /*!< X axis lower 2 bits */
#define HTAC_Y_LOW     0x04      /*!< Y axis lower 2 bits */
#define HTAC_Z_LOW     0x05      /*!< Z axis lower 2 bits */

bool HTACreadAllAxes(tSensors link, int &x, int &y, int &z);

#ifdef __HTSMUX_SUPPORT__
bool HTACreadAllAxes(tMUXSensor muxsensor, int &x, int &y, int &z);

tConfigParams HTAC_config = {HTSMUX_CHAN_I2C, 6, 0x02, 0x42}; /*!< Array to hold SMUX config data for sensor */
#endif

tByteArray HTAC_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray HTAC_I2CReply;      /*!< Array to hold I2C reply data */

/**
 * Read the value of all the axes registers return by reference
 * @param link the HTAC port number
 * @param x x axis
 * @param y y axis
 * @param z z axis
 * @return true if no error occured, false if it did
 */
bool HTACreadAllAxes(tSensors link, int &x, int &y, int &z) {
  memset(HTAC_I2CRequest, 0, sizeof(tByteArray));

  HTAC_I2CRequest[0] = 2;                       // Message size
  HTAC_I2CRequest[1] = HTAC_I2C_ADDR;           // I2C Address
  HTAC_I2CRequest[2] = HTAC_OFFSET + HTAC_X_UP; // X axis upper 8 bits register

  if (!writeI2C(link, HTAC_I2CRequest, HTAC_I2CReply, 6))
    return false;

  // Convert 2 bytes into a signed 10 bit value.  If the 8 high bits are more than 127, make
  // it a signed value before combing it with the lower 2 bits.
  // Gotta love conditional assignments!
  x = (HTAC_I2CReply[0] > 127) ? (HTAC_I2CReply[0] - 256) * 4 + HTAC_I2CReply[3]
                                   : HTAC_I2CReply[0] * 4 + HTAC_I2CReply[3];

  y = (HTAC_I2CReply[1] > 127) ? (HTAC_I2CReply[1] - 256) * 4 + HTAC_I2CReply[4]
                                   : HTAC_I2CReply[1] * 4 + HTAC_I2CReply[4];

  z = (HTAC_I2CReply[2] > 127) ? (HTAC_I2CReply[2] - 256) * 4 + HTAC_I2CReply[5]
                                   : HTAC_I2CReply[2] * 4 + HTAC_I2CReply[5];

  return true;
}


/**
 * Read the value of all the axes registers return by reference
 * @param muxsensor the SMUX sensor port number
 * @param x x axis
 * @param y y axis
 * @param z z axis
 * @return true if no error occured, false if it did
 */
#ifdef __HTSMUX_SUPPORT__
bool HTACreadAllAxes(tMUXSensor muxsensor, int &x, int &y, int &z) {
  memset(HTAC_I2CReply, 0, sizeof(tByteArray));

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, HTAC_config);

  if (!HTSMUXreadPort(muxsensor, HTAC_I2CReply, 6, HTAC_X_UP)) {
    return false;
  }

  // Convert 2 bytes into a signed 10 bit value.  If the 8 high bits are more than 127, make
  // it a signed value before combing it with the lower 2 bits.
  // Gotta love conditional assignments!
  x = (HTAC_I2CReply[0] > 127) ? (HTAC_I2CReply[0] - 256) * 4 + HTAC_I2CReply[3]
                                   : HTAC_I2CReply[0] * 4 + HTAC_I2CReply[3];

  y = (HTAC_I2CReply[1] > 127) ? (HTAC_I2CReply[1] - 256) * 4 + HTAC_I2CReply[4]
                                   : HTAC_I2CReply[1] * 4 + HTAC_I2CReply[4];

  z = (HTAC_I2CReply[2] > 127) ? (HTAC_I2CReply[2] - 256) * 4 + HTAC_I2CReply[5]
                                   : HTAC_I2CReply[2] * 4 + HTAC_I2CReply[5];

  return true;
}
#endif // __HTSMUX_SUPPORT__

#endif // __HTAC_H__

/*
 * $Id: hitechnic-accelerometer.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
