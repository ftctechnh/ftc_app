/*!@addtogroup mindsensors
 * @{
 * @defgroup msll LineLeader Sensor
 * LineLeader Sensor
 * @{
 */

/*
 * $Id: mindsensors-lightsensorarray.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSLSA_H__
#define __MSLSA_H__

/** \file mindsensors-lightsensorarray.h
 * \brief Mindsensors LigthSensorArray
 *
 * mindsensors-lightsensorarray.h provides an API for the Mindsensors LightSensorArray
 *
 * Changelog:
 *  - 0.1 Initial	release<br>
 *
 * License: You may use this code as you wish, provided you give credit where it's due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat
 * \date 29 September 2012
 * \version 0.1
 * \example mindsensors-ligthsensorarray-test1.c
 * \example mindsensors-ligthsensorarray-test3.c
 */

#pragma systemFile

#ifndef __COMMON_H__
	#include "common.h"
#endif

#define MSLSA_I2C_ADDR   		    0x14  /*!< I2C address used by the MSLSA */
#define MSLSA_CMD_REG    		    0x41  /*!< Register used for issuing commands */

#define MSLSA_CALIBRATED        0x42  /*!< Calibrated Sensor reading */
#define MSLSA_WHITE_LIMIT  	    0x4A  /*!< White Reading Limit */
#define MSLSA_BLACK LIMIT   	  0x52  /*!< Black Reading Limit */
#define MSLSA_WHITE_CALIB_DATA  0x5A  /*!< White Calibration data */
#define MSLSA_BLACK_CALIB_DATA  0x62  /*!< Black Calibration data */
#define MSLSA_UNCALIBRATED 		  0x6A  /*!< Uncalibrated sensor voltage  */

#define MSLSA_CMD_FREQ_US       'A'   /*!< American frequency compensation  */
#define MSLSA_CMD_CALIB_BLACK   'B'   /*!< Calibrate black values  */
#define MSLSA_CMD_POWERDOWN     'D'   /*!< Power down the sensor array  */
#define MSLSA_CMD_FREQ_EU       'E'   /*!< European frequency compensation  */
#define MSLSA_CMD_POWERUP       'P'   /*!< Power up the sensor array  */
#define MSLSA_CMD_FREQ_UNI      'U'   /*!< Universal frequency compensation (default) */
#define MSLDA_CMD_CALIB_WHITE   'W'   /*!< Calibrate white values  */

tByteArray MSLSA_I2CRequest;       /*!< Array to hold I2C command data */
tByteArray MSLSA_I2CReply;         /*!< Array to hold I2C reply data */

#define MSLSAwakeUp(X)    _MSLSAsendCommand(X, MSLSA_CMD_POWERUP)     /*!< Wake sensor from sleep mode */
#define MSLSASleep(X)     _MSLSAsendCommand(X, MSLSA_CMD_POWERDOWN)   /*!< Put sensor into sleep mode */
#define MSLSAcalWhite(X)  _MSLSAsendCommand(X, MSLDA_CMD_CALIB_WHITE) /*!< Calibrate the white value */
#define MSLSAcalBlack(X)  _MSLSAsendCommand(X, MSLSA_CMD_CALIB_BLACK) /*!< Calibrate the black value */
#define MSLSAsetEU(X)     _MSLSAsendCommand(X, MSLSA_CMD_FREQ_EU)     /*!< Calibrate the black value */
#define MSLSAsetUS(X)     _MSLSAsendCommand(X, MSLSA_CMD_FREQ_US)     /*!< Calibrate the black value */
#define MSLSAsetUni(X)    _MSLSAsendCommand(X, MSLSA_CMD_FREQ_UNI)    /*!< Calibrate the black value */


bool MSLSAreadSensors(tSensors link, ubyte *values);
bool MSLSAreadRawSensors(tSensors link, int *values);
bool _MSLSAsendCommand(tSensors link, ubyte cmd);                      /*!< Send a command to the LightSensorArray */

/**
 * This function sends a command to the lightsensorarray.
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the sensor port number
 * @param cmd the command to be sent
 * @return true if no error occured, false if it did

PRELIMINARY COMMANDS FROM NXC LIB CODE
 - A American frequency compensation
 - B for black calibration
 - D sensor power down
 - E European frequency compensation
 - P power on sensor
 - U Universal frequency compensation (default)
 - W White balance
 */
bool _MSLSAsendCommand(tSensors link, ubyte cmd) {
  MSLSA_I2CRequest[0] = 3;             // Message size
  MSLSA_I2CRequest[1] = MSLSA_I2C_ADDR;   // I2C Address
  MSLSA_I2CRequest[2] = MSLSA_CMD_REG;    // Register used for issuing commands
  MSLSA_I2CRequest[3] = cmd;           // Command to be executed

  return writeI2C(link, MSLSA_I2CRequest);
}


/**
 * Return the calibrated values from the sensors
 * @param link the Mindsensors LightSensorArray port number
 * @param values pointer to unsigned byte array to hold the sensor values
 * @return true if no error occured, false if it did
 */
bool MSLSAreadSensors(tSensors link, ubyte *values)
{
  MSLSA_I2CRequest[0] = 2;
  MSLSA_I2CRequest[1] = MSLSA_I2C_ADDR;
  MSLSA_I2CRequest[2] = MSLSA_CALIBRATED;

  if (!writeI2C(link, MSLSA_I2CRequest, MSLSA_I2CReply, 8))
    return false;

  // clear out the old values
  memset(values, 0, sizeof(values));

  // copy the new values (first 8 bytes)
  memcpy(values, MSLSA_I2CReply, 8);

  return true;
}


/**
 * Return the uncalibrated values from the sensors
 * @param link the Mindsensors LightSensorArray port number
 * @param values pointer to integer array to hold the uncalibrated sensor values
 * @return true if no error occured, false if it did
 */
bool MSLSAreadRawSensors(tSensors link, int *values)
{
  MSLSA_I2CRequest[0] = 2;
  MSLSA_I2CRequest[1] = MSLSA_I2C_ADDR;
  MSLSA_I2CRequest[2] = MSLSA_UNCALIBRATED;

  if (!writeI2C(link, MSLSA_I2CRequest, MSLSA_I2CReply, 16))
    return false;

  // clear out the old values
  memset(values, 0, sizeof(values));

  for (int i = 0; i < 8; i++)
  {
    values[i] = MSLSA_I2CReply[i * 2] + (MSLSA_I2CReply[(i * 2) + 1] << 8);
  }

  // copy the new values (first 8 bytes)
  memcpy(values, MSLSA_I2CReply, 8);

  return true;
}

#endif // __MSLSA_H__

/*
 * $Id: mindsensors-lightsensorarray.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
