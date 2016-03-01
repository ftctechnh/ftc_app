/*!@addtogroup HiTechnic
 * @{
 * @defgroup HTBM Barometric Sensor
 * HiTechnic Barometric Sensor
 * @{
 */

/*
 * $Id: hitechnic-barometer.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTBM_H__
#define __HTBM_H__
/** \file hitechnic-barometer.h
 * \brief HiTechnic Barometric Sensor driver
 *
 * hitechnic-barometer.h provides an API for the HiTechnic Barometric Sensor.\n
 * Pressure values are between 0 and approx 31 inHG.\n
 * Temperature values are between -25C and +85C.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Replaced array structs with typedefs
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
 * \example hitechnic-barometer-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define HTBM_I2C_ADDR  0x02      /*!< Barometric sensor device address */
#define HTBM_OFFSET    0x42      /*!< Offset for data registers */
#define HTBM_TEMP_HIGH 0x00      /*!< Temperature high byte */
#define HTBM_TEMP_LOW  0x01      /*!< Temperature low byte */
#define HTBM_PRES_HIGH 0x02      /*!< Pressure high byte */
#define HTBM_PRES_LOW  0x03      /*!< Pressure low byte */

const float mInHgtohPa = 0.03386389;
const float mInHgtoPsi = 0.491098/1000;

long HTBMreadMInHg(tSensors link);
float HTBMreadhPa(tSensors link);
float HTBMreadPsi(tSensors link);
float HTBMreadTemp(tSensors link);
float HTBMreadTempF(tSensors link);

tByteArray HTBM_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray HTBM_I2CReply;      /*!< Array to hold I2C reply data */

/**
 * Read the current atmospheric pressure in 1/1000th inch Hg (mercury)
 * @param link the HTBM port number
 * @return pressure in 1/1000th inch Hg (mercury), -255 if something went wrong
 */
long HTBMreadMInHg(tSensors link) {
  memset(HTBM_I2CRequest, 0, sizeof(tByteArray));

  HTBM_I2CRequest[0] = 2;                       // Message size
  HTBM_I2CRequest[1] = HTBM_I2C_ADDR;           // I2C Address
  HTBM_I2CRequest[2] = HTBM_OFFSET + HTBM_PRES_HIGH; // Pressure high byte

  if (!writeI2C(link, HTBM_I2CRequest, HTBM_I2CReply, 2))
    return -255;

  return (HTBM_I2CReply[0] <<  8) + HTBM_I2CReply[1];
}


/**
 * Read the current atmospheric pressure in hecto Pascal
 * @param link the HTBM port number
 * @return pressure in hecto Pascal, -255 if something went wrong
 */
float HTBMreadhPa(tSensors link) {
  return HTBMreadMInHg(link) * mInHgtohPa;
}


/**
 * Read the current atmospheric pressure in pounds per square inch
 * @param link the HTBM port number
 * @return pressure in pounds per square inch, -255 if something went wrong
 */
float HTBMreadPsi(tSensors link) {
  return (float)HTBMreadMInHg(link) * mInHgtoPsi;
}


/**
 * Read the current air temperature in degrees Celcius
 * @param link the HTBM port number
 * @return current air temperature in degrees Celcius, -255 if something went wrong
 */
float HTBMreadTemp(tSensors link) {
  float temp = 0.0;
  memset(HTBM_I2CRequest, 0, sizeof(tByteArray));

  HTBM_I2CRequest[0] = 2;                       // Message size
  HTBM_I2CRequest[1] = HTBM_I2C_ADDR;           // I2C Address
  HTBM_I2CRequest[2] = HTBM_OFFSET + HTBM_TEMP_HIGH; // Pressure high byte

  // Send the request
  if (!writeI2C(link, HTBM_I2CRequest, HTBM_I2CReply, 2))
    return -255;

  temp = (HTBM_I2CReply[0] <<  8) + HTBM_I2CReply[1];
  temp /= 10;
  return temp;
}


/**
 * Read the current air temperature in degrees Fahrenheit
 * @param link the HTBM port number
 * @return the current air temperature in degrees Fahrenheit, -255 if something went wrong
 */
float HTBMreadTempF(tSensors link) {
  return (HTBMreadTemp(link) * 1.8) + 32;
}


#endif // __HTBM_H__

/*
 * $Id: hitechnic-barometer.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
