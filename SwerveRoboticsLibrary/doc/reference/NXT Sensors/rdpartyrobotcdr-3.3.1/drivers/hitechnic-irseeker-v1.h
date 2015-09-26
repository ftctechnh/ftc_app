/*!@addtogroup HiTechnic
 * @{
 * @defgroup htirs IR Seeker V1
 * HiTechnic Color IR Seeker V1
 * @{
 */

/*
 * $Id: hitechnic-irseeker-v1.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTIRS_H__
#define __HTIRS_H__
/** \file hitechnic-irseeker-v1.h
 * \brief HiTechnic IR Seeker driver
 *
 * hitechnic-irseeker-v1.h provides an API for the HiTechnic IR Seeker.
 *
 * NOTE: THIS DRIVER WILL NOT WORK WITH THE IR SEEKER V2!<br>
 * Use the HTDIR driver for the V2 sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Changed comments on HTIRReadDir()
 * - 0.3: Reduced MAX_BUFSIZE to 5 to reduce overhead
 * - 0.4: Major rewrite of code, uses common.h for most functions
 * - 0.5: Fixed incorrect register numbers for signal strength registers
 * - 0.6: Added SMUX functions
 * - 0.7: HTIRSreadAllStrength() is now pass by reference to reduce memory<br>
 *        SMUX tByteArray removed, reuses HTIRS_I2CReply
 * - 0.8: Use new calls in common.h that don't require SPORT/MPORT macros
 * - 0.9: Removed HTIRSreadStrength(), use HTIRSreadAllStrength() instead
 *
 * Credits:
 * - Big thanks to HiTechnic for providing me with the hardware necessary to write and test this.
 * - JamesD for testing and finding bugs.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 25 November 2009
 * \version 0.9
 * \example hitechnic-irseeker-v1-test1.c
 * \example hitechnic-irseeker-v1-SMUX-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define HTIRS_I2C_ADDR 0x02      /*!< IR Seeker I2C device address */
#define HTIRS_OFFSET   0x42      /*!< Offset for data registers */
#define HTIRS_DIR      0x00      /*!< Address of Direction data */
#define HTIRS_SSTR1    0x01      /*!< Address of Sensor 0 signal strength */
#define HTIRS_SSTR2    0x02      /*!< Address of Sensor 1 signal strength */
#define HTIRS_SSTR3    0x03      /*!< Address of Sensor 2 signal strength */
#define HTIRS_SSTR4    0x04      /*!< Address of Sensor 3 signal strength */
#define HTIRS_SSTR5    0x05      /*!< Address of Sensor 4 signal strength */

int HTIRSreadDir(tSensors link);
bool HTIRSreadAllStrength(tSensors link, int &dcS1, int &dcS2, int &dcS3, int &dcS4, int &dcS5);

#ifdef __HTSMUX_SUPPORT__
int HTIRSreadDir(tMUXSensor muxsensor);
bool HTIRSreadAllStrength(tMUXSensor muxsensor, int &dcS1, int &dcS2, int &dcS3, int &dcS4, int &dcS5);

tConfigParams HTIRS_config = {HTSMUX_CHAN_I2C, 7, 0x02, 0x42}; /*!< Array to hold SMUX config data for sensor */
#endif // __HTSMUX_SUPPORT__

tByteArray HTIRS_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray HTIRS_I2CReply;      /*!< Array to hold I2C reply data */

/**
 * Read the value of the Direction data register and return it.
 * @param link the HTIRS port number
 * @return value of 0-9, the direction index of the detected IR signal or -1 if an error occurred.
 */
int HTIRSreadDir(tSensors link) {
  memset(HTIRS_I2CRequest, 0, sizeof(tByteArray));

  HTIRS_I2CRequest[0] = 2;                        // Message size
  HTIRS_I2CRequest[1] = HTIRS_I2C_ADDR;           // I2C Address
  HTIRS_I2CRequest[2] = HTIRS_OFFSET + HTIRS_DIR; // Start direction register

  if (!writeI2C(link, HTIRS_I2CRequest, HTIRS_I2CReply, 1))
    return -1;

  return (int)HTIRS_I2CReply[0];
}


/**
 * Read the value of the Direction data register and return it.
 * @param muxsensor the SMUX sensor port number
 * @return value of 0-9, the direction index of the detected IR signal or -1 if an error occurred.
 */
#ifdef __HTSMUX_SUPPORT__
int HTIRSreadDir(tMUXSensor muxsensor) {
	memset(HTIRS_I2CReply, 0, sizeof(tByteArray));

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, HTIRS_config);

  if (!HTSMUXreadPort(muxsensor, HTIRS_I2CReply, 1, HTIRS_DIR))
    return -1;

  return (int)HTIRS_I2CReply[0];
}
#endif // __HTSMUX_SUPPORT__


/**
 * Read the value of the all of the internal sensors.
 * @param link the HTIRS port number
 * @param dcS1 data from internal sensor nr 1
 * @param dcS2 data from internal sensor nr 2
 * @param dcS3 data from internal sensor nr 3
 * @param dcS4 data from internal sensor nr 4
 * @param dcS5 data from internal sensor nr 5
 * @return true if no error occured, false if it did
 */
bool HTIRSreadAllStrength(tSensors link, int &dcS1, int &dcS2, int &dcS3, int &dcS4, int &dcS5) {
  memset(HTIRS_I2CRequest, 0, sizeof(tByteArray));

  HTIRS_I2CRequest[0] = 2;                          // Message size
  HTIRS_I2CRequest[1] = HTIRS_I2C_ADDR;             // I2C Address
  HTIRS_I2CRequest[2] = HTIRS_OFFSET + HTIRS_SSTR1; // Address of Sensor 0 signal strength

  if (!writeI2C(link, HTIRS_I2CRequest, HTIRS_I2CReply, 5))
    return false;

  dcS1 = (int)HTIRS_I2CReply[0];
  dcS2 = (int)HTIRS_I2CReply[1];
  dcS3 = (int)HTIRS_I2CReply[2];
  dcS4 = (int)HTIRS_I2CReply[3];
  dcS5 = (int)HTIRS_I2CReply[4];

  return true;
}


/**
 * Read the value of the all of the internal sensors.
 * @param muxsensor the SMUX sensor port number
 * @param dcS1 data from internal sensor nr 1
 * @param dcS2 data from internal sensor nr 2
 * @param dcS3 data from internal sensor nr 3
 * @param dcS4 data from internal sensor nr 4
 * @param dcS5 data from internal sensor nr 5
 * @return true if no error occured, false if it did
 */
#ifdef __HTSMUX_SUPPORT__
bool HTIRSreadAllStrength(tMUXSensor muxsensor, int &dcS1, int &dcS2, int &dcS3, int &dcS4, int &dcS5) {
  memset(HTIRS_I2CReply, 0, sizeof(tByteArray));

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, HTIRS_config);

  if (!HTSMUXreadPort(muxsensor, HTIRS_I2CReply, 5, HTIRS_SSTR1))
    return false;

  dcS1 = (int)HTIRS_I2CReply[0];
  dcS2 = (int)HTIRS_I2CReply[1];
  dcS3 = (int)HTIRS_I2CReply[2];
  dcS4 = (int)HTIRS_I2CReply[3];
  dcS5 = (int)HTIRS_I2CReply[4];
  return true;
}
#endif // __HTSMUX_SUPPORT__

#endif // __HTIRS_H__

/*
 * $Id: hitechnic-irseeker-v1.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
