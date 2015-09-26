/*!@addtogroup HiTechnic
 * @{
 * @defgroup htcs Color Sensor V1
 * HiTechnic Color Sensor V1
 * @{
 */

/*
 * $Id: hitechnic-colour-v1.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTCS_H__
#define __HTCS_H__
/** \file hitechnic-colour-v1.h
 * \brief HiTechnic Color Sensor driver
 *
 * hitechnic-colour-v1.h provides an API for the HiTechnic Color Sensor driver.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Added SMUX functions
 * - 0.3: All functions that uses tIntArray are now pass by reference to reduce memory usage.<br>
 *        Removed SMUX data array
 * - 0.4: Use new calls in common.h that don't require SPORT/MPORT macros <br>
 *        Removed calls to ubyteToInt()
 * - 0.5: Replaced array structs with typedefs
 *
 * Credits:
 * - Big thanks to HiTechnic for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 20 February 2011
 * \version 0.5
 * \example hitechnic-colour-v1-test1.c
 * \example hitechnic-colour-v1-test2.c
 * \example hitechnic-colour-v1-SMUX-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#ifndef __LIGHT_COMMON_H__
#include "common-light.h"
#endif

#define HTCS_I2C_ADDR       0x02      /*!< HTCS I2C device address */
#define HTCS_CMD_REG        0x41      /*!< Command register */
#define HTCS_OFFSET         0x42      /*!< Offset for data registers */
#define HTCS_COLNUM_REG     0x00      /*!< Color number */
#define HTCS_RED_REG        0x01      /*!< Red reading */
#define HTCS_GREEN_REG      0x02      /*!< Green reading */
#define HTCS_BLUE_REG       0x03      /*!< Blue reading */
#define HTCS_RED_RAW_REG    0x04      /*!< Raw red reading (2 bytes) */
#define HTCS_GREEN_RAW_REG  0x05      /*!< Raw green reading (2 bytes) */
#define HTCS_BLUE_RAW_REG   0x06      /*!< Raw blue reading (2 bytes) */
#define HTSC_COL_INDEX_REG  0x07      /*!< Color index number */
#define HTSC_RED_NORM_REG   0x08      /*!< Normalised red reading */
#define HTSC_GREEN_NORM_REG 0x09      /*!< Normalised green reading */
#define HTSC_BLUE_NORM_REG  0x0A      /*!< Normalised blue reading */

#define HTCS_CAL_WHITE      0x43      /*!< Command to calibrate white */

int HTCSreadColor(tSensors link);
bool HTCSreadRGB(tSensors link, int &red, int &green, int &blue);
bool HTCSreadNormRGB(tSensors link, int &red, int &green, int &blue);
bool HTCSreadRawRGB(tSensors link, int &red, int &green, int &blue);
bool HTCScalWhite(tSensors link);

#ifdef __HTSMUX_SUPPORT__
int HTCSreadColor(tMUXSensor muxsensor);
bool HTCSreadRGB(tMUXSensor muxsensor, int &red, int &green, int &blue);

tConfigParams HTCS_config = {HTSMUX_CHAN_I2C, 4, 0x02, 0x42}; /*!< Array to hold SMUX config data for sensor */
#endif

tByteArray HTCS_I2CRequest;           /*!< Array to hold I2C command data */
tByteArray HTCS_I2CReply;             /*!< Array to hold I2C reply data */

/**
 * Return the color number currently detected.
 * @param link the HTCS port number
 * @return color index number or -1 if an error occurred.
 */
int HTCSreadColor(tSensors link) {
  memset(HTCS_I2CRequest, 0, sizeof(tByteArray));

  HTCS_I2CRequest[0] = 2;                             // Message size
  HTCS_I2CRequest[1] = HTCS_I2C_ADDR;                 // I2C Address
  HTCS_I2CRequest[2] = HTCS_OFFSET + HTCS_COLNUM_REG; // Start colour number register

  if (!writeI2C(link, HTCS_I2CRequest, HTCS_I2CReply, 1))
    return -1;

  return HTCS_I2CReply[0];
}


/**
 * Return the color number currently detected.
 * @param muxsensor the SMUX sensor port number
 * @return color index number or -1 if an error occurred.
 */
#ifdef __HTSMUX_SUPPORT__
int HTCSreadColor(tMUXSensor muxsensor) {
	memset(HTCS_I2CRequest, 0, sizeof(tByteArray));

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, HTCS_config);

  if (!HTSMUXreadPort(muxsensor, HTCS_I2CReply, 1, HTCS_COLNUM_REG)) {
    return -1;
  }

  return HTCS_I2CReply[0];
}
#endif // __HTSMUX_SUPPORT__


/**
 * Get the detection levels for the three color components.
 * @param link the HTCS port number
 * @param red the red value
 * @param green the green value
 * @param blue the blue value
 * @return true if no error occured, false if it did
 */
bool HTCSreadRGB(tSensors link, int &red, int &green, int &blue) {
  memset(HTCS_I2CRequest, 0, sizeof(tByteArray));

  HTCS_I2CRequest[0] = 2;                           // Message size
  HTCS_I2CRequest[1] = HTCS_I2C_ADDR;               // I2C Address
  HTCS_I2CRequest[2] = HTCS_OFFSET + HTCS_RED_REG;  // Start red sensor value

  if (!writeI2C(link, HTCS_I2CRequest, HTCS_I2CReply, 3))
    return false;

  red = HTCS_I2CReply[0];
  green = HTCS_I2CReply[1];
  blue = HTCS_I2CReply[2];

  return true;
}


/**
 * Get the detection levels for the three color components.
 * @param muxsensor the SMUX sensor port number
 * @param red the red value
 * @param green the green value
 * @param blue the blue value
 * @return true if no error occured, false if it did
 */
#ifdef __HTSMUX_SUPPORT__
bool HTCSreadRGB(tMUXSensor muxsensor, int &red, int &green, int &blue) {
  memset(HTCS_I2CRequest, 0, sizeof(tByteArray));

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, HTCS_config);

  if (!HTSMUXreadPort(muxsensor, HTCS_I2CReply, 3, HTCS_RED_REG)) {
    return false;
  }

  red = HTCS_I2CReply[0];
  green = HTCS_I2CReply[1];
  blue = HTCS_I2CReply[2];

  return true;
}
#endif // __HTSMUX_SUPPORT__


/**
 * Get the detection levels for the hue, saturation, value components.
 * @param link the HTCS port number
 * @param hue the hue output value (from 0 to 365, or -1 if n/a)
 * @param saturation the saruration output value (from 0 to 100, or -1 if n/a)
 * @param value the value output value (from 0 to 100)
 * @return true if no error occured, false if it did
 */
bool HTCSreadHSV(tSensors link, float &hue, float &saturation, float &value) {

  int red,green,blue;
  bool ret = HTCSreadRGB(link, red, green, blue);
  RGBtoHSV(red,green,blue, hue, saturation, value);

  return ret;
}


/**
 * Get the detection levels for the hue, saturation, value components.
 * @param muxsensor the SMUX sensor port number
 * @param hue the hue output value (from 0 to 365, or -1 if n/a)
 * @param saturation the saruration output value (from 0 to 100, or -1 if n/a)
 * @param value the value output value (from 0 to 100)
 * @return true if no error occured, false if it did
 */
#ifdef __HTSMUX_SUPPORT__
bool HTCSreadHSV(tMUXSensor muxsensor, float &hue, float &saturation, float &value) {

  int red,green,blue;

  bool ret = HTCSreadRGB(muxsensor, red, green, blue);
  RGBtoHSV(red,green,blue, hue, saturation, value);

  return ret;
}
#endif // __HTSMUX_SUPPORT__


/**
 * Get the normalised RGB readings. The normalization sets the highest
 * value of the three Red, Green and Blue reading to 255 and adjusts the
 * other two proportionately.
 * @param link the HTCS port number
 * @param red the red value
 * @param green the green value
 * @param blue the blue value
 * @return true if no error occured, false if it did
 */
bool HTCSreadNormRGB(tSensors link, int &red, int &green, int &blue) {
  memset(HTCS_I2CRequest, 0, sizeof(tByteArray));

  HTCS_I2CRequest[0] = 2;                               // Message size
  HTCS_I2CRequest[1] = HTCS_I2C_ADDR;                   // I2C Address
  HTCS_I2CRequest[2] = HTCS_OFFSET + HTSC_RED_NORM_REG; // Start red normalised sensor values

  if (!writeI2C(link, HTCS_I2CRequest, HTCS_I2CReply, 3))
    return false;

  red = HTCS_I2CReply[0];
  green = HTCS_I2CReply[1];
  blue = HTCS_I2CReply[2];

  return true;
}

/**
 * Get the raw RGB readings, these are 10bit values.
 * @param link the HTCS port number
 * @param red the red value
 * @param green the green value
 * @param blue the blue value
 * @return true if no error occured, false if it did
 */

bool HTCSreadRawRGB(tSensors link, int &red, int &green, int &blue) {
  memset(HTCS_I2CRequest, 0, sizeof(tByteArray));

  HTCS_I2CRequest[0] = 2;                               // Message size
  HTCS_I2CRequest[1] = HTCS_I2C_ADDR;                   // I2C Address
  HTCS_I2CRequest[2] = HTCS_OFFSET + HTCS_RED_RAW_REG;  // Start red raw sensor value

  if (!writeI2C(link, HTCS_I2CRequest, HTCS_I2CReply, 6))
    return false;

  red = HTCS_I2CReply[0];
  green = HTCS_I2CReply[1];
  blue = HTCS_I2CReply[2];

  return true;
}

/**
 * Return the color index number currently detected. This is a single
 * 6 bit number color index. Bits 5 and 4 encode the red signal level,
 * bits 3 and 2 encode the green signal level and bits 1 and 0 encode
 * the blue signal levels.
 * @param link the HTCS port number
 * @return color index number or -1 if an error occurred.
 */
int HTCSreadColorIndex(tSensors link) {
  memset(HTCS_I2CRequest, 0, sizeof(tByteArray));

  HTCS_I2CRequest[0] = 2;                                // Message size
  HTCS_I2CRequest[1] = HTCS_I2C_ADDR;                    // I2C Address
  HTCS_I2CRequest[2] = HTCS_OFFSET + HTSC_COL_INDEX_REG; // Start colour index register

  if (!writeI2C(link, HTCS_I2CRequest, HTCS_I2CReply, 1))
    return -1;

  return HTCS_I2CReply[0];
}

/**
 * Calibrate the sensor for white.
 * @param link the HTCS port number
 * @return true if no error occured, false if it did
 */
bool HTCScalWhite(tSensors link) {
  memset(HTCS_I2CRequest, 0, sizeof(tByteArray));

  HTCS_I2CRequest[0] = 3;               // Message size
  HTCS_I2CRequest[1] = HTCS_I2C_ADDR;   // I2C Address
  HTCS_I2CRequest[2] = HTCS_CMD_REG;    // Command register
  HTCS_I2CRequest[3] = HTCS_CAL_WHITE;  // Command to calibrate white

  return writeI2C(link, HTCS_I2CRequest);
}

#endif // __HTCS_H__

/*
 * $Id: hitechnic-colour-v1.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
