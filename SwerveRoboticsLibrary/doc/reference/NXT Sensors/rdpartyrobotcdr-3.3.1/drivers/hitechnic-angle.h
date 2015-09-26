/*!@addtogroup HiTechnic
 * @{
 * @defgroup htang Angle Sensor
 * HiTechnic Angle Sensor
 * @{
 */

/*
 * $Id: hitechnic-angle.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTANG_H__
#define __HTANG_H__
/** \file hitechnic-angle.h
 * \brief HiTechnic Angle Sensor driver
 *
 * hitechnic-angle.h provides an API for the HiTechnic Angle Sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Replaced array structs with typedefs
 *
 *
 * Credits:
 * - Big thanks to HiTechnic for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 20 February 2011
 * \version 0.2
 * \example hitechnic-angle-test1.c
 * \example hitechnic-angle-SMUX-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define HTANG_I2C_ADDR         0x02      /*!< HTCS2 I2C device address */
#define HTANG_CMD_REG          0x41      /*!< Command register */
#define HTANG_OFFSET           0x42      /*!< Offset for data registers */

// Values contained by registers in active mode
#define HTANG_ANG2             0x00      /*!< Current angle (2 deg increments) */
#define HTANG_ANG1             0x01      /*!< Current angle (1 deg adder) */
#define HTANG_ACC_ANG_B4       0x02      /*!< 32 bit accumulated angle 4th byte */
#define HTANG_ACC_ANG_B3       0x03      /*!< 32 bit accumulated angle 3rd byte */
#define HTANG_ACC_ANG_B2       0x04      /*!< 32 bit accumulated angle 2nd byte */
#define HTANG_ACC_ANG_B1       0x05      /*!< 32 bit accumulated angle 1st byte */
#define HTANG_RPM_H            0x06      /*!< 16 bit rpms, high byte */
#define HTANG_RPM_L            0x07      /*!< 16 bit rpms, low byte */

// Various commands
#define HTANG_CMD_MEASURE      0x00      /*!< Normal angle measurement mode */
#define HTANG_CMD_RST_ANG      0x43      /*!< Resets 0 position to current shaft angle, non-volatile setting */
#define HTANG_CMD_RST_ACC_ANG  0x52      /*!< Resets the accumulated angle */

int HTANGreadAngle(tSensors link);
long HTANGreadAccumulatedAngle(tSensors link);
int HTANGreadRPM(tSensors link);
bool HTANGresetAngle(tSensors link);
bool HTANGresetAccumulatedAngle(tSensors link);
bool _HTANGsendCommand(tSensors link, byte command);

#ifdef __HTSMUX_SUPPORT__
int HTANGreadAngle(tMUXSensor muxsensor);
long HTANGreadAccumulatedAngle(tMUXSensor muxsensor);
int HTANGreadRPM(tMUXSensor muxsensor);

tConfigParams HTANG_config = {HTSMUX_CHAN_I2C, 8, 0x02, 0x42};  /*!< Array to hold SMUX config data for sensor */
#endif // __HTSMUX_SUPPORT__

tByteArray HTANG_I2CRequest;             /*!< Array to hold I2C command data */
tByteArray HTANG_I2CReply;               /*!< Array to hold I2C reply data */


/**
 * Return the current angle
 * @param link the HTANG port number
 * @return current angle or -1 if an error occurred.
 */
int HTANGreadAngle(tSensors link) {
  memset(HTANG_I2CRequest, 0, sizeof(tByteArray));

  HTANG_I2CRequest[0] = 2;                         // Message size
  HTANG_I2CRequest[1] = HTANG_I2C_ADDR;            // I2C Address
  HTANG_I2CRequest[2] = HTANG_OFFSET + HTANG_ANG2; // Start Current angle

  if (!writeI2C(link, HTANG_I2CRequest, HTANG_I2CReply, 2))
    return -1;

  return (HTANG_I2CReply[0] * 2) + HTANG_I2CReply[1];
}


/**
 * Return the current angle
 * @param muxsensor the SMUX sensor port number
 * @return current angle or -1 if an error occurred.
 */
#ifdef __HTSMUX_SUPPORT__
int HTANGreadAngle(tMUXSensor muxsensor) {
	memset(HTANG_I2CRequest, 0, sizeof(tByteArray));

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, HTANG_config);

  if (!HTSMUXreadPort(muxsensor, HTANG_I2CReply, 2, HTANG_ANG2)) {
    return -1;
  }

  return (HTANG_I2CReply[0] * 2) + HTANG_I2CReply[1];
}
#endif // __HTSMUX_SUPPORT__


/**
 * Return the accumulated angle (signed 32 bit value)
 * @param link the HTANG port number
 * @return current angle or -1 if an error occurred.
 */
long HTANGreadAccumulatedAngle(tSensors link) {
  memset(HTANG_I2CRequest, 0, sizeof(tByteArray));

  HTANG_I2CRequest[0] = 2;                                // Message size
  HTANG_I2CRequest[1] = HTANG_I2C_ADDR;                   // I2C Address
  HTANG_I2CRequest[2] = HTANG_OFFSET + HTANG_ACC_ANG_B4;  // Start accumulated angle

  if (!writeI2C(link, HTANG_I2CRequest, HTANG_I2CReply, 4))
    return -1;

  return (HTANG_I2CReply[0] << 24) +
         (HTANG_I2CReply[1] << 16) +
         (HTANG_I2CReply[2] <<  8) +
          HTANG_I2CReply[3];
}


/**
 * Return the accumulated angle (signed 32 bit value)
 * @param muxsensor the SMUX sensor port number
 * @return current angle or -1 if an error occurred.
 */
#ifdef __HTSMUX_SUPPORT__
long HTANGreadAccumulatedAngle(tMUXSensor muxsensor) {
	memset(HTANG_I2CRequest, 0, sizeof(tByteArray));

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, HTANG_config);

  if (!HTSMUXreadPort(muxsensor, HTANG_I2CReply, 4, HTANG_ACC_ANG_B4)) {
    return -1;
  }

  return (HTANG_I2CReply[0] << 24) +
         (HTANG_I2CReply[1] << 16) +
         (HTANG_I2CReply[2] <<  8) +
          HTANG_I2CReply[3];
}
#endif // __HTSMUX_SUPPORT__


/**
 * Return the rpm that the shaft is currently rotating at
 * @param link the HTANG port number
 * @return the current rpm of the shaft or -1 if an error occurred.
 */
int HTANGreadRPM(tSensors link) {
  memset(HTANG_I2CRequest, 0, sizeof(tByteArray));

  HTANG_I2CRequest[0] = 2;                           // Message size
  HTANG_I2CRequest[1] = HTANG_I2C_ADDR;              // I2C Address
  HTANG_I2CRequest[2] = HTANG_OFFSET + HTANG_RPM_H;  // Start of rpm

  if (!writeI2C(link, HTANG_I2CRequest, HTANG_I2CReply, 2))
    return -1;

  return (HTANG_I2CReply[0] <<  8) +
          HTANG_I2CReply[1];
}


/**
 * Return the rpm that the shaft is currently rotating at
 * @param muxsensor the SMUX sensor port number
 * @return the current rpm of the shaft or -1 if an error occurred.
 */
#ifdef __HTSMUX_SUPPORT__
int HTANGreadRPM(tMUXSensor muxsensor) {
  memset(HTANG_I2CRequest, 0, sizeof(tByteArray));

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, HTANG_config);

  if (!HTSMUXreadPort(muxsensor, HTANG_I2CReply, 2, HTANG_RPM_H)) {
    return -1;
  }

  // return HTANG_I2CReply[0] * 2 + HTANG_I2CReply[1];

  return (HTANG_I2CReply[0] <<  8) +
          HTANG_I2CReply[1];
}
#endif // __HTSMUX_SUPPORT__


/**
 * Reset the 0 position to the current shaft angle.<br>
 * Note: this will also reset the accumulated angle counter
 * @param link the HTANG port number
 * @return true if no error occured, false if it did
 */
bool HTANGresetAngle(tSensors link) {
  return _HTANGsendCommand(link, HTANG_CMD_RST_ANG);
}


/**
 * Reset the accumulated angle
 * @param link the HTANG port number
 * @return true if no error occured, false if it did
 */
bool HTANGresetAccumulatedAngle(tSensors link) {
  return _HTANGsendCommand(link, HTANG_CMD_RST_ACC_ANG);
}


/**
 * Send a command to the sensor
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the HTANG port number
 * @param command the command to be sent to the sensor
 * @return true if no error occured, false if it did
 */
bool _HTANGsendCommand(tSensors link, byte command) {
  memset(HTANG_I2CRequest, 0, sizeof(tByteArray));

  HTANG_I2CRequest[0] = 3;              // Message size
  HTANG_I2CRequest[1] = HTANG_I2C_ADDR; // I2C Address
  HTANG_I2CRequest[2] = HTANG_CMD_REG;  // Command register
  HTANG_I2CRequest[3] = command;        // Command to be sent

  return writeI2C(link, HTANG_I2CRequest);
}


#endif // __HTANG_H__

 /*
 * $Id: hitechnic-angle.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
