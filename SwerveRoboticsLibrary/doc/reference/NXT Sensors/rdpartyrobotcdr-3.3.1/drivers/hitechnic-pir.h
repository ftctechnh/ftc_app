/*!@addtogroup HiTechnic
 * @{
 * @defgroup HTPIR PIR Sensor
 * HiTechnic PIR Sensor
 * @{
 */

/*
 * $Id: hitechnic-pir.h 133 2013-03-10 15:15:38Z xander $
 */

/** \file hitechnic-pir.h
 * \brief HiTechnic PIR Sensor Driver
 *
 * hitechnic-pir.h provides an API for the HiTechnic PIR Sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 15 August 2012
 * \version 0.1
 * \example hitechnic-pir-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

// I2C address + registers
#define HTPIR_I2C_ADDR       0x02  /*!< HTPIR I2C device address */
#define HTPIR_DEADBAND       0x41  /*!< HTPIR Mode control */
#define HTPIR_READING        0x42  /*!< HTPIR Heading Upper bits */

#define HTPIR_DEFAULT_DEADBAND 12

bool HTPIRsetDeadband(tSensors link, int deadband);
int HTPIRreadSensor(tSensors link);

#ifdef __HTSMUX_SUPPORT__
int HTPIRreadSensor(tMUXSensor muxsensor);

tConfigParams HTPIR_config = {HTSMUX_CHAN_I2C, 1, 0x02, 0x42}; /*!< Array to hold SMUX config data for sensor */
#endif // __HTSMUX_SUPPORT__

tByteArray HTPIR_I2CRequest;       /*!< Array to hold I2C command data */
tByteArray HTPIR_I2CReply;         /*!< Array to hold I2C reply data */


/**
 * The sensor element with the PIR sensor generates continuous noise.
 * The size of the deadband is set to minimize the number of false
 * detections which will be reported by the sensor. If the deadband
 * value is too small, some of the noise fluctuations will exceed the
 * deadband threshold and will appear as actual non-zero readings. \n
 * The Deadband field may be set from 0 to 47 to define the half width
 * of the deadband. The default value is 12.
 * @param link the HTPIR port number
 * @param deadband the amount
 * @return true if no error occured, false if it did
 */
bool HTPIRsetDeadband(tSensors link, int deadband) {
  memset(HTPIR_I2CRequest, 0, sizeof(tByteArray));
  // Ensure the values are valid
  deadband = clip(deadband, 0, 47);

  HTPIR_I2CRequest[0] = 3;                  // Number of bytes in I2C command
  HTPIR_I2CRequest[1] = HTPIR_I2C_ADDR;     // I2C address of PIR sensor
  HTPIR_I2CRequest[2] = HTPIR_DEADBAND;     // Set write address to sensor mode register
  HTPIR_I2CRequest[3] = deadband;           // The calibration mode command

  // Start the calibration
  return writeI2C(link, HTPIR_I2CRequest);
}


/**
 * Read the current levels detected by the sensor
 * @param link the HTPIR port number
 * @return level detected by sensor, will be between -128 and 127.
 */
int HTPIRreadSensor(tSensors link) {
  memset(HTPIR_I2CRequest, 0, sizeof(tByteArray));

  HTPIR_I2CRequest[0] = 2;               // Number of bytes in I2C command
  HTPIR_I2CRequest[1] = HTPIR_I2C_ADDR;   // I2C address of PIR sensor
  HTPIR_I2CRequest[2] = HTPIR_READING;     // Set write address to sensor mode register

  if (!writeI2C(link, HTPIR_I2CRequest, HTPIR_I2CReply, 1))
    return -1;

  return (HTPIR_I2CReply[0] >= 128) ? (int)HTPIR_I2CReply[0] - 256 : (int)HTPIR_I2CReply[0];
}

/**
 * Read the current levels detected by the sensor
 * @param muxsensor the SMUX sensor port number
 * @return level detected by sensor, will be between -128 and 127.
 */
#ifdef __HTSMUX_SUPPORT__
int HTPIRreadSensor(tMUXSensor muxsensor) {
  memset(HTPIR_I2CReply, 0, sizeof(tByteArray));

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, HTPIR_config);

  if (!HTSMUXreadPort(muxsensor, HTPIR_I2CReply, 1)) {
    return -1;
  }

  return (HTPIR_I2CReply[0] >= 128) ? (int)HTPIR_I2CReply[0] - 256 : (int)HTPIR_I2CReply[0];
}
#endif // __HTSMUX_SUPPORT__


/*
 * $Id: hitechnic-pir.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
