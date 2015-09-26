/*!@addtogroup Dexter_Industries
 * @{
 * @defgroup TIR Thermal Infrared Sensor
 * Dexter Industries Thermal Infrared Sensor
 * @{
 */

/*
 * $Id: dexterind-thermalir.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __TIR_H__
#define __TIR_H__
/** \file dexterind-thermalir.h
 * \brief Dexter Industries Thermal Infrared Sensor driver
 *
 * TIR2-driver.h provides an API for the Dexter Industries Thermal Infrared Sensor driver.
 *
 * Changelog:
 * - 0.1: Initial release
 *
 * Credits:
 * - Big thanks to Dexter Industries for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 10 June 2011
 * \version 0.1
 * \example dexterind-thermalir-test1.c
 * \example dexterind-thermalir-test2.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define TIR_I2C_ADDR        0x0E      /*!< TIR I2C device address */
#define TIR_AMBIENT         0x00      /*!< Thermal Infrared number */
#define TIR_OBJECT          0x01      /*!< Red reading */
#define TIR_SET_EMISSIVITY  0x02      /*!< Green reading */
#define TIR_GET_EMISSIVITY  0x03
#define TIR_CHK_EMISSIVITY  0x04
#define TIR_RESET           0x05

// Source http://www.rwc.uc.edu/koehler/biophys/8d.html
#define TIR_EM_SKIN_LIGHT   5660
#define TIR_EM_SKIN_DARK    8380
#define TIR_EM_GLASS        9200
#define TIR_EM_CANDLE_SOOT  9500

#define tempFactor 0.02


float TIRreadAmbientTemp(tSensors link);
float TIRreadObjectTemp(tSensors link);
bool TIRsetEmissivity(tSensors link, int emissivity);
bool TIRresetSensor(tSensors link);

tByteArray TIR_I2CRequest;           /*!< Array to hold I2C command data */
tByteArray TIR_I2CReply;             /*!< Array to hold I2C reply data */

/**
 * Measure the ambient temperature
 * @param link the TIR port number
 * @return the ambient temperature
 */
float TIRreadAmbientTemp(tSensors link) {
  float tempData;

  memset(TIR_I2CRequest, 0, sizeof(tByteArray));

  TIR_I2CRequest[0] = 2;            // Message size
  TIR_I2CRequest[1] = TIR_I2C_ADDR; // I2C Address
  TIR_I2CRequest[2] = TIR_AMBIENT;  // Start colour number register

  if (!writeI2C(link, TIR_I2CRequest, TIR_I2CReply, 2))
    return 0;

  wait1Msec(1);

  tempData = (float)(((TIR_I2CReply[1]) << 8) + TIR_I2CReply[0]);
  tempData = (tempData * tempFactor) - 0.01;      // Convert to celsius
  return tempData - 273.15;            // Convert from Kelvin to Celsius
}

/**
 * Measure the object temperature
 * @param link the TIR port number
 * @return The object temperature
 */
float TIRreadObjectTemp(tSensors link) {
  float tempData;

  memset(TIR_I2CRequest, 0, sizeof(tByteArray));

  TIR_I2CRequest[0] = 2;            // Message size
  TIR_I2CRequest[1] = TIR_I2C_ADDR; // I2C Address
  TIR_I2CRequest[2] = TIR_OBJECT;  // Start colour number register

  if (!writeI2C(link, TIR_I2CRequest, TIR_I2CReply, 2))
    return 0;

  wait1Msec(1);
  tempData = (float)(((TIR_I2CReply[1]) << 8) + TIR_I2CReply[0]);
  tempData = (tempData * tempFactor) - 0.01;      // Convert to celsius
  return tempData - 273.15;            // Convert from Kelvin to Celsius
}


/**
 * Read the current emissivity
 * @param link the TIR port number
 * @return The current emissivity
 */
long TIRreadEmissivity(tSensors link) {
  memset(TIR_I2CRequest, 0, sizeof(tByteArray));

  TIR_I2CRequest[0] = 2;            // Message size
  TIR_I2CRequest[1] = TIR_I2C_ADDR; // I2C Address
  TIR_I2CRequest[2] = TIR_GET_EMISSIVITY;

  if (!writeI2C(link, TIR_I2CRequest, TIR_I2CReply, 2))
    return 0;

  return (long)TIR_I2CReply[0] + ((long)TIR_I2CReply[1] << 8);
}


/**
 * Check the emissivity
 * @param link the TIR port number
 * @return The current emissivity
 */
long TIRcheckEmissivity(tSensors link) {
  memset(TIR_I2CRequest, 0, sizeof(tByteArray));

  TIR_I2CRequest[0] = 2;            // Message size
  TIR_I2CRequest[1] = TIR_I2C_ADDR; // I2C Address
  TIR_I2CRequest[2] = TIR_CHK_EMISSIVITY;

  if (!writeI2C(link, TIR_I2CRequest, TIR_I2CReply, 2))
    return 0;

  return TIR_I2CReply[0] + (TIR_I2CReply[1] << 8);
}


/**
 * Set the current emissivity
 * @param link the TIR port number
 * @param emissivity the emissivity of the object that is to be measured
 * @return true if no error occured, false if it did
 */
bool TIRsetEmissivity(tSensors link, int emissivity) {
  memset(TIR_I2CRequest, 0, sizeof(tByteArray));

  TIR_I2CRequest[0] = 4;            // Message size
  TIR_I2CRequest[1] = TIR_I2C_ADDR; // I2C Address
  TIR_I2CRequest[2] = TIR_SET_EMISSIVITY;  // Start colour number register
  TIR_I2CRequest[4] = (emissivity >> 8) & 0xFF;    // High Byte
  TIR_I2CRequest[3] = emissivity & 0xFF; // Low Byte.

  return writeI2C(link, TIR_I2CRequest);
}


/**
 * Reset the sensor
 * @param link the TIR port number
 * @return true if no error occured, false if it did
 */
bool TIRresetSensor(tSensors link) {
  memset(TIR_I2CRequest, 0, sizeof(tByteArray));

  TIR_I2CRequest[0] = 2;            // Message size
  TIR_I2CRequest[1] = TIR_I2C_ADDR; // I2C Address
  TIR_I2CRequest[2] = TIR_RESET;  // Start colour number register

  return writeI2C(link, TIR_I2CRequest);
}

#endif // __TIR_H__


/*
 * $Id: dexterind-thermalir.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
