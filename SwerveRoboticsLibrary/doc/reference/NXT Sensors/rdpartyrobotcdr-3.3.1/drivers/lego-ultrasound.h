/*!@addtogroup lego
 * @{
 * @defgroup legous Ultrasonic Sensor
 * Ultrasonic Sensor
 * @{
 */

/*
 * $Id: lego-ultrasound.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __LEGOUS_H__
#define __LEGOUS_H__
/** \file lego-ultrasound.h
 * \brief SMUX driver for the Lego US sensor.
 *
 * lego-ultrasound.h provides an API for the Lego US driver.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Added support for additional commands
 *
 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 10 December 2010
 * \version 0.2
 * \example lego-ultrasound-SMUX-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define LEGOUS_I2C_ADDR    0x02      /*!< Lego US I2C address */
#define LEGOUS_REG_CMD     0x41      /*!< Command register */
#define LEGOUS_REG_DATA    0x42      /*!< Start of measurement data registers */

#define LEGOUS_CMD_OFF    0x00      /*!< Command to switch US off */
#define LEGOUS_CMD_SSHOT  0x01      /*!< Command to turn on Single Shot mode */
#define LEGOUS_CMD_CONT   0x02      /*!< Command to turn on Continuous Mode */
#define LEGOUS_CMD_ECAPT  0x03      /*!< Command to turn on Event Capture Mode */
#define LEGOUS_CMD_RST    0x04      /*!< Command to request a warm reset */

// Prototypes
int USreadDist(tSensors link);
bool USreadDistances(tSensors link, tByteArray &distances);
bool _USsendCmd(tSensors link, ubyte command);
bool USsetSingleMode(tSensors link);
bool USsetContinuousMode(tSensors link);
bool USsetOff(tSensors link);
bool USsetEventCapture(tSensors link);
bool USreset(tSensors link);

#ifdef __HTSMUX_SUPPORT__
int USreadDist(tMUXSensor muxsensor);

tConfigParams LEGOUS_config = {HTSMUX_CHAN_I2C + HTSMUX_CHAN_9V + HTSMUX_CHAN_I2C_SLOW, 1, 0x02, 0x42}; /*!< Array to hold SMUX config data for sensor */
#endif // __HTSMUX_SUPPORT__

tByteArray LEGOUS_I2CRequest;
tByteArray LEGOUS_I2CReply;

/**
 * Get the distance value from the sensor
 * @param muxsensor the SMUX sensor port number
 * @return distance from the sensor or 255 if no valid range has been specified.
 */
#ifdef __HTSMUX_SUPPORT__
int USreadDist(tMUXSensor muxsensor) {

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, LEGOUS_config);


  if (!HTSMUXreadPort(muxsensor, LEGOUS_I2CReply, 1, 0)) {
    return 255;
  }

  return (int)LEGOUS_I2CReply[0];
}
#endif


/**
 * Get the distance values from the sensor
 * @param link the US port number
 * @return distance from the sensor or 255 if no valid range has been specified.
 */
int USreadDist(tSensors link) {
  memset(LEGOUS_I2CRequest, 0, sizeof(tByteArray));

  LEGOUS_I2CRequest[0] = 2;                // Message size
  LEGOUS_I2CRequest[1] = LEGOUS_I2C_ADDR;  // I2C Address
  LEGOUS_I2CRequest[2] = LEGOUS_REG_DATA;  // Start direction register

  if (!writeI2C(link, LEGOUS_I2CRequest, LEGOUS_I2CReply, 1))
    return -1;

  return LEGOUS_I2CReply[0];
}


/**
 * Get the distance values from the sensor. The distances to the
 * 8 closest objects are returned.
 * @param link the US port number
 * @param distances array holding data on last 8 echos received
 * @return distance from the sensor or 255 if no valid range has been specified.
 */
bool USreadDistances(tSensors link, tByteArray &distances) {
  memset(LEGOUS_I2CRequest, 0, sizeof(tByteArray));

  LEGOUS_I2CRequest[0] = 2;                // Message size
  LEGOUS_I2CRequest[1] = LEGOUS_I2C_ADDR;  // I2C Address
  LEGOUS_I2CRequest[2] = LEGOUS_REG_DATA;  // Start direction register

  if (!writeI2C(link, LEGOUS_I2CRequest, LEGOUS_I2CReply, 8))
    return false;

  memcpy(distances, LEGOUS_I2CReply, sizeof(tByteArray));
  return true;
}


/**
 * Send a command to the US Sensor
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the US port number
 * @param command the command to be sent to the sensor
 * @return true if no error occured, false if it did
 */
bool _USsendCmd(tSensors link, ubyte command) {
  memset(LEGOUS_I2CRequest, 0, sizeof(tByteArray));

  LEGOUS_I2CRequest[0] = 3;                // Message size
  LEGOUS_I2CRequest[1] = LEGOUS_I2C_ADDR;  // I2C Address
  LEGOUS_I2CRequest[2] = LEGOUS_REG_CMD;   // command register
  LEGOUS_I2CRequest[3] = command;          // command

  return writeI2C(link, LEGOUS_I2CRequest);
}


/**
 * Configure the US sensor for Single Shot mode
 * @param link the US port number
 * @return true if no error occured, false if it did
 */
bool USsetSingleMode(tSensors link) {
  return _USsendCmd(link, LEGOUS_CMD_SSHOT);
}


/**
 * Configure the US sensor for Continuous Mode.  This is the default.
 * @param link the US port number
 * @return distance from the sensor or 255 if no valid range has been specified.
 */
bool USsetContinuousMode(tSensors link) {
  return _USsendCmd(link, LEGOUS_CMD_CONT);
}


/**
 * Turn the sensor off.
 * @param link the US port number
 * @return true if no error occured, false if it did
 */
bool USsetOff(tSensors link){
  return _USsendCmd(link, LEGOUS_CMD_OFF);
}


/**
 * Configure the US sensor for Event Capture mode
 * @param link the US port number
 * @return true if no error occured, false if it did
 */
bool USsetEventCapture(tSensors link) {
  return _USsendCmd(link, LEGOUS_CMD_ECAPT);
}


/**
 * Request a warm reset of the sensor.
 * @param link the US port number
 * @return true if no error occured, false if it did
 */
bool USreset(tSensors link) {
  return _USsendCmd(link, LEGOUS_CMD_RST);
}

#endif // __LEGOSNR_H__

/*
 * $Id: lego-ultrasound.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
