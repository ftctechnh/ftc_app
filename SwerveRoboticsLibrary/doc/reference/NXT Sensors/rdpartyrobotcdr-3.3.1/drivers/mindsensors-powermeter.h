/*!@addtogroup mindsensors
 * @{
 * @defgroup mspm Power Meter Sensor
 * Power Meter Sensor
 * @{
 */

/*
 * $Id: mindsensors-powermeter.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSPM_H__
#define __MSPM_H__

/** \file mindsensors-powermeter.h
 * \brief Mindsensors Power Meter Sensor
 *
 * mindsensors-powermeter.h provides an API for the Mindsensors Power Meter Sensor.
 *
 * Changelog:
 *  - 0.1 Initial	release.
 *
 * Credits:
 * - Big thanks to Mindsensors for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where it's due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat
 * \date 16 December 2009
 * \version 0.1
 * \example mindsensors-powermeter-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
	#include "common.h"
#endif

//*******************************************************************************
// REGISTER LOCATIONS AND COMMANDS for the Power Meter sensor
//*******************************************************************************
#define MSPM_I2C_ADDR     0x12  /*!< I2C address used by the PM */
#define MSPM_CMD_REG    	0x41  /*!< Register used for issuing commands */

#define MSPM_PCURRENT     0x42  /*!< Present current reading in mA - 2 bytes */
#define MSPM_PVOLTAGE 		0x44  /*!< Present current reading in mV - 2 bytes */
#define MSPM_CAPUSED 			0x46  /*!< Capacity used since last reset in mAh - 2 bytes */
#define MSPM_TIME         0x56  /*!< Time since last reset  in ms - 4 bytes (long) */

tByteArray MSPM_I2CRequest;       /*!< Array to hold I2C command data */
tByteArray MSPM_I2CReply;         /*!< Array to hold I2C reply data */


//*******************************************************************************
// PUBLIC Power Meter functions
//*******************************************************************************
int MSPMreadCurrent(tSensors link, ubyte address = MSPM_I2C_ADDR);
int MSPMreadVoltage(tSensors link, ubyte address = MSPM_I2C_ADDR);
bool MSPMreadVoltageCurrent(tSensors link, int &voltage, int &current, ubyte address = MSPM_I2C_ADDR);
long MSPMreadTime(tSensors link, ubyte address = MSPM_I2C_ADDR);
bool MSPMresetCounters(tSensors link, ubyte address = MSPM_I2C_ADDR);

//*******************************************************************************
// INTERNAL USE ONLY - used by the above
//*******************************************************************************
bool _MSPMsendCommand(tSensors link, byte command, ubyte address);


/**
 * This function sends a command to the Power Meter.
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the sensor port number
 * @param command the command to be sent
 * @param address the I2C address to use, optional, defaults to 0x12
 * @return true if no error occured, false if it did
 */
bool _MSPMsendCommand(tSensors link, byte command, ubyte address) {
  MSPM_I2CRequest[0] = 3;               // Message size
  MSPM_I2CRequest[1] = address;         // I2C Address
  MSPM_I2CRequest[2] = MSPM_CMD_REG;    // Register used for issuing commands
  MSPM_I2CRequest[3] = command;         // Command to be executed

  return writeI2C(link, MSPM_I2CRequest);
}


/**
 * Return the present current measured.
 * @param link the MSPM port number
 * @param address the I2C address to use, optional, defaults to 0x12
 * @return the present current measured or -1 if an error occurred.
 */
int MSPMreadCurrent(tSensors link, ubyte address) {
  memset(MSPM_I2CRequest, 0, sizeof(tByteArray));

  MSPM_I2CRequest[0] = 2;             // Message size
  MSPM_I2CRequest[1] = address;       // I2C Address
  MSPM_I2CRequest[2] = MSPM_PCURRENT; // Present current register

  if (!writeI2C(link, MSPM_I2CRequest, MSPM_I2CReply, 2))
    return -1;

  return (MSPM_I2CReply[0] + (MSPM_I2CReply[1]<<8));
}


/**
 * Return the present voltage measured.
 * @param link the MSPM port number
 * @param address the I2C address to use, optional, defaults to 0x12
 * @return the present voltage measured or -1 if an error occurred.
 */
int MSPMreadVoltage(tSensors link, ubyte address) {
  memset(MSPM_I2CRequest, 0, sizeof(tByteArray));

  MSPM_I2CRequest[0] = 2;             // Message size
  MSPM_I2CRequest[1] = address;       // I2C Address
  MSPM_I2CRequest[2] = MSPM_PVOLTAGE; // Present voltage register

  if (!writeI2C(link, MSPM_I2CRequest, MSPM_I2CReply, 2))
    return -1;

  return (MSPM_I2CReply[0] + (MSPM_I2CReply[1]<<8));
}


/**
 * Return the present voltage and current measured.  This is a much more
 * efficient method of fetching both.
 * @param link the MSPM port number
 * @param voltage present voltage being measured
 * @param current present current being measured
 * @param address the I2C address to use, optional, defaults to 0x12
 * @return the present voltage measured or -1 if an error occurred.
 */
bool MSPMreadVoltageCurrent(tSensors link, int &voltage, int &current, ubyte address) {
  memset(MSPM_I2CRequest, 0, sizeof(tByteArray));

  MSPM_I2CRequest[0] = 2;             // Message size
  MSPM_I2CRequest[1] = address;       // I2C Address
  MSPM_I2CRequest[2] = MSPM_PCURRENT; // Present current register

  if (!writeI2C(link, MSPM_I2CRequest, MSPM_I2CReply, 4))
    return false;

  current = MSPM_I2CReply[0] + (MSPM_I2CReply[1]<<8);
  voltage = MSPM_I2CReply[2] + (MSPM_I2CReply[3]<<8);
  return true;
}

/**
 * Return the time elapsed in ms since the last reset.
 * @param link the MSPM port number
 * @param address the I2C address to use, optional, defaults to 0x12
 * @return the time elapsed in ms since the last reset.
 */
long MSPMreadTime(tSensors link, ubyte address) {
  memset(MSPM_I2CRequest, 0, sizeof(tByteArray));

  MSPM_I2CRequest[0] = 2;             // Message size
  MSPM_I2CRequest[1] = address;       // I2C Address
  MSPM_I2CRequest[2] = MSPM_TIME;     // Present voltage register

  if (!writeI2C(link, MSPM_I2CRequest, MSPM_I2CReply, 4))
    return -1;

  return (((long)MSPM_I2CReply[3] << 24) + ((long)MSPM_I2CReply[2] << 16) + ((long)MSPM_I2CReply[1] <<  8) + (long)MSPM_I2CReply[0]);
  // return uByteToLong(MSPM_I2CReply[3], MSPM_I2CReply[2], MSPM_I2CReply[1], MSPM_I2CReply[0]);
}


/**
 * Reset all the counters.
 * @param link the MSPM port number
 * @param address the I2C address to use, optional, defaults to 0x12
 * @return true if no error occured, false if it did
 */
bool MSPMresetCounters(tSensors link, ubyte address) {
  return _MSPMsendCommand(link, 'R', address);
}


#endif // __MSLL_H__

/*
 * $Id: mindsensors-powermeter.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
