/*!@addtogroup mindsensors
 * @{
 * @defgroup ppsv3 Pressure Sensor V3
 * PPS-v3 Sensor
 * @{
 */

/*
 * $Id: mindsensors-pressure.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSPPS_H__
#define __MSPPS_H__
/** \file mindsensors-pressure.h
 * \brief Mindsensors PPS-v3 driver
 *
 * mindsensors-pressure.h provides an API for the Mindsensors PPS-v3 Pressure Sensor driver
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
 * \date 01 January 2012
 * \version 0.1
 * \example mindsensors-pressure-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define MSPPS_I2C_ADDR       0x18  /*!< MSPPS I2C device address */

#define MSPPS_CMD            0x41  /*!< MSPPS command register */

#define MSPPS_UNIT           0x42  /*!< Configure measurement unit */
#define MSPPS_PRESS_ABS      0x43  /*!< Read the absolute pressure */
#define MSPPS_PRESS_GAUGE    0x45  /*!< Read the gauge pressure */
#define MSPPS_PRESS_REF      0x47  /*!< Read or write the reference pressure */

#define MSPPS_UNIT_PSI       0x50   /*!< PSI unit */
#define MSPPS_UNIT_MB        0x62   /*!< Millibar unit */
#define MSPPS_UNIT_KPA       0x6B   /*!< Kilo Pascal unit */

#define MSPSS_CMD_SETREF     0x44    /*!< Set reference pressure to current absolute pressure */

// Prototypes
bool MSPPSsendCmd(tSensors link, ubyte command);
bool MSPPSsetUnit(tSensors link, ubyte unit);
long MSPPSreadPressure(tSensors link, ubyte reg);
long MSPPSreadAbsPressure(tSensors link);
long MSPPSreadGaugePressure(tSensors link);
long MSPPSreadRefPressure(tSensors link);
bool MSPPSsetRefPressure(tSensors link);
bool MSPPSsetRefPressure(tSensors link, int refpressure);

// Handy defines to shortcut some actions
#define MSPPSsetUnitPSI(X)  MSPPSsetUnit(X, MSPPS_UNIT_PSI)
#define MSPPSsetUnitmB(X)   MSPPSsetUnit(X, MSPPS_UNIT_MB)
#define MSPPSsetUnitkPa(X)  MSPPSsetUnit(X, MSPPS_UNIT_KPA)

tByteArray MSPPS_I2CRequest;       /*!< Array to hold I2C command data */
tByteArray MSPPS_I2CReply;         /*!< Array to hold I2C reply data */


/**
 * Read the pressure from the sensor and return it in the
 * unit the sensor has been configured for.<br>
 * It is better to use MSPPSreadAbsPressure(), MSPPSreadGaugePressure()
 * or MSPPSreadRefPressure() instead
 * @param link the sensor port number
 * @param reg Specific pressure register to read.
 * @return the absolute pressure.
 */
long MSPPSreadPressure(tSensors link, ubyte reg)
{
  memset(MSPPS_I2CRequest, 0, sizeof(tByteArray));

  MSPPS_I2CRequest[0] = 2;               // Number of bytes in I2C command
  MSPPS_I2CRequest[1] = MSPPS_I2C_ADDR;   // I2C address of accel sensor
  MSPPS_I2CRequest[2] = reg;     // Set write address to sensor mode register

  if (!writeI2C(link, MSPPS_I2CRequest, MSPPS_I2CReply, 2))
    return 0;

  return ((long)MSPPS_I2CReply[1] * 256) + MSPPS_I2CReply[0];
}


/**
 * Read the absolute pressure from the sensor and return it in the
 * unit the sensor has been configured for.
 * @param link the sensor port number
 * @return the absolute pressure.
 */
long MSPPSreadAbsPressure(tSensors link)
{
  return MSPPSreadPressure(link, MSPPS_PRESS_ABS);
}


/**
 * Read the gauge pressure from the sensor and return it in the
 * unit the sensor has been configured for.  This is the absolute
 * pressure minus the reference pressure.
 * @param link the sensor port number
 * @return the gauge pressure.
 */
long MSPPSreadGaugePressure(tSensors link)
{
  return MSPPSreadPressure(link, MSPPS_PRESS_GAUGE);
}


/**
 * Read the reference pressure from the sensor and return it in the
 * unit specified.
 * @param link the sensor port number
 * @return the reference pressure.
 */
long MSPPSreadRefPressure(tSensors link)
{
  return MSPPSreadPressure(link, MSPPS_PRESS_REF);
}


/**
 * Set the reference pressure to the value specified.
 * @param link the sensor port number
 * @param refpressure the value the ref pressure register should be set to.
 * @return true if no error occured, false if it did
 */
bool MSPPSsetRefPressure(tSensors link, int refpressure)
{
  memset(MSPPS_I2CRequest, 0, sizeof(tByteArray));

  MSPPS_I2CRequest[0] = 4;                         // Number of bytes in I2C command
  MSPPS_I2CRequest[1] = MSPPS_I2C_ADDR;            // I2C address of accel sensor
  MSPPS_I2CRequest[2] = MSPPS_PRESS_REF;           // Set write address to sensor mode register
  MSPPS_I2CRequest[3] = refpressure & 0xFF;         // Command to be sent to the sensor
  MSPPS_I2CRequest[4] = (refpressure << 8) & 0xFF;  // Command to be sent to the sensor

  return writeI2C(link, MSPPS_I2CRequest);
}


/**
 * Set the reference pressure to the current absolute pressure value.
 * @param link the sensor port number
 * @return true if no error occured, false if it did
 */
bool MSPPSsetRefPressure(tSensors link)
{
  return MSPPSsendCmd(link, MSPSS_CMD_SETREF);
}


/**
 * Send a command to the sensor
 * @param link the sensor port number
 * @param command the command to be sent
 * @return true if no error occured, false if it did
 */
bool MSPPSsendCmd(tSensors link, ubyte command) {
  memset(MSPPS_I2CRequest, 0, sizeof(tByteArray));

  MSPPS_I2CRequest[0] = 3;               // Number of bytes in I2C command
  MSPPS_I2CRequest[1] = MSPPS_I2C_ADDR;   // I2C address of accel sensor
  MSPPS_I2CRequest[2] = MSPPS_CMD;        // Set write address to sensor mode register
  MSPPS_I2CRequest[3] = command;         // Command to be sent to the sensor

  return writeI2C(link, MSPPS_I2CRequest);
}


/**
 * Set the unit of measurement to the one specified.
 * @param link the sensor port number
 * @param unit the unit of measurement to be used.
 * @return true if no error occured, false if it did
 */
bool MSPPSsetUnit(tSensors link, ubyte unit) {
  memset(MSPPS_I2CRequest, 0, sizeof(tByteArray));

  MSPPS_I2CRequest[0] = 3;               // Number of bytes in I2C command
  MSPPS_I2CRequest[1] = MSPPS_I2C_ADDR;   // I2C address of accel sensor
  MSPPS_I2CRequest[2] = MSPPS_UNIT;        // Set write address to sensor mode register
  MSPPS_I2CRequest[3] = unit;         // Command to be sent to the sensor

  return writeI2C(link, MSPPS_I2CRequest);
}

#endif //__MSPPS_H__

/*
 * $Id: mindsensors-pressure.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
