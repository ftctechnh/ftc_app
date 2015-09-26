/*!@addtogroup Dexter_Industries
 * @{
 * @defgroup DLIGHT dLight Sensor
 * Dexter Industries dLight Sensor Driver
 * @{
 */

/*
 * $Id: dexterind-dlight.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __DLIGHT_H__
#define __DLIGHT_H__
/** \file dexterind-dlight.h
 * \brief Dexter Industries dLight Sensor Driver
 *
 * dexterind-dlight.h provides an API for the Dexter Industries dLight Sensor Driver
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
 * \date 09 March 2013
 * \version 0.1
 * \example dexterind-dlight-test1.c
 */

#pragma systemFile


#ifndef __COMMON_H__
#include "common.h"
#endif

#ifndef __COMMON_LIGHT_H__
#include "drivers/common-light.h"
#endif

#define DLIGHT_I2C_ADDR_ALL 0xE0        /*!< dLight I2C device address for all connected devices */
#define DLIGHT_I2C_ADDR_1   0x04        /*!< dLight I2C device address for device 1 (the NXT adapter) */
#define DLIGHT_I2C_ADDR_2   0x14        /*!< dLight I2C device address for device 2 */
#define DLIGHT_I2C_ADDR_3   0x24        /*!< dLight I2C device address for device 3 */
#define DLIGHT_I2C_ADDR_4   0x34        /*!< dLight I2C device address for device 4 */

#define DLIGHT_REG_MODE1    0x80        /*!< dLight register to configure autoincrement, must be set to 0x01 */
#define DLIGHT_REG_MODE2    0x81        /*!< dLight register to configure blinking, must be set to 0x25 */
#define DLIGHT_REG_RED      0x82        /*!< dLight register to configure the amount of red, 0-0xFF */
#define DLIGHT_REG_GREEN    0x83        /*!< dLight register to configure the amount of green, 0-0xFF */
#define DLIGHT_REG_BLUE     0x84        /*!< dLight register to configure the amount of blue, 0-0xFF */
#define DLIGHT_REG_EXTERNAL 0x85        /*!< dLight register to configure the external LED, 0-0xFF */
#define DLIGHT_REG_BPCT     0x86        /*!< dLight register to configure the blinking duty cycle */
#define DLIGHT_REG_BFREQ    0x87        /*!< dLight register to configure the blinking frequency */
#define DLIGHT_REG_LEDOUT   0x88        /*!< dLight register to configure enable/disable the LEDs and their blinking  */

#define DLIGHT_CMD_DISABLE_LEDS   0x00  /*!< dLight cmmand to disable the LEDs completely */
#define DLIGHT_CMD_DISABLE_BLINK  0xAA  /*!< dLight cmmand to disable blinking */
#define DLIGHT_CMD_ENABLE_BLINK   0xFF  /*!< dLight cmmand to enable blinking  */


tByteArray DLIGHT_I2CRequest;             /*!< Array to hold I2C command data */

/**
 * Initialise the dLight sensor.  Turns off blinking.
 * @param link the dLight port number
 * @param addr the dLight I2C address
 * @return true if no error occured, false if it did
 */
bool DLIGHTinit(tSensors link, ubyte addr){
  DLIGHT_I2CRequest[0] = 4;
  DLIGHT_I2CRequest[1] = addr;
  DLIGHT_I2CRequest[2] = DLIGHT_REG_MODE1;
  DLIGHT_I2CRequest[3] = 0x01;
  DLIGHT_I2CRequest[4] = 0x25;

  if (!writeI2C(link, DLIGHT_I2CRequest))
    return false;

  wait1Msec(50);

  DLIGHT_I2CRequest[0] = 3;
  DLIGHT_I2CRequest[1] = addr;
  DLIGHT_I2CRequest[2] = DLIGHT_REG_LEDOUT;
  DLIGHT_I2CRequest[3] = DLIGHT_CMD_DISABLE_BLINK;

  return writeI2C(link, DLIGHT_I2CRequest);
}


/**
 * Set the dLight to the specified RGB colour
 * @param link the dLight port number
 * @param addr the dLight I2C address
 * @param r the red LED brightness value (0-255)
 * @param g the green LED brightness value (0-255)
 * @param b the blue LED brightness value (0-255)
 * @return true if no error occured, false if it did
 */
bool DLIGHTsetColor(tSensors link, ubyte addr, ubyte r, ubyte g, ubyte b){
  DLIGHT_I2CRequest[0] = 5;
  DLIGHT_I2CRequest[1] = addr;
  DLIGHT_I2CRequest[2] = DLIGHT_REG_RED;
  DLIGHT_I2CRequest[3] = r;
  DLIGHT_I2CRequest[4] = g;
  DLIGHT_I2CRequest[5] = b;

  return writeI2C(link, DLIGHT_I2CRequest);
}


/**
 * Set the dLight to the specified RGB colour
 * @param link the dLight port number
 * @param addr the dLight I2C address
 * @param external the external LED brightness value (0-255)
 * @return true if no error occured, false if it did
 */
bool DLIGHTsetExternal(tSensors link, ubyte addr, ubyte external){
  DLIGHT_I2CRequest[0] = 3;
  DLIGHT_I2CRequest[1] = addr;
  DLIGHT_I2CRequest[2] = DLIGHT_REG_EXTERNAL;
  DLIGHT_I2CRequest[3] = external;

  return writeI2C(link, DLIGHT_I2CRequest);
}


/**
 * Set the dLight to the specified RGB colour
 * @param link the dLight port number
 * @param addr the dLight I2C address
 * @param BlinkRate the frequency at which to blink the light (Hz)
 * @param DutyCycle duty cycle of the light in percentage
 * @return true if no error occured, false if it did
 */
bool DLIGHTsetBlinking(tSensors link, ubyte addr, float BlinkRate, long DutyCycle){
  BlinkRate*= 24;
  BlinkRate--;

  DLIGHT_I2CRequest[0] = 4;
  DLIGHT_I2CRequest[1] = addr;
  DLIGHT_I2CRequest[2] = DLIGHT_REG_BPCT;
  DLIGHT_I2CRequest[3] = (255 * DutyCycle) / 100;
  DLIGHT_I2CRequest[4] = round(BlinkRate);
  writeDebugStreamLine("rate: %d, duty: %d", DLIGHT_I2CRequest[4], DLIGHT_I2CRequest[3]);
  return writeI2C(link, DLIGHT_I2CRequest);
}


/**
 * Start blinking the LED
 * @param link the dLight port number
 * @param addr the dLight I2C address
 * @return true if no error occured, false if it did
 */
bool DLIGHTstartBlinking(tSensors link, ubyte addr){
  DLIGHT_I2CRequest[0] = 3;
  DLIGHT_I2CRequest[1] = addr;
  DLIGHT_I2CRequest[2] = DLIGHT_REG_LEDOUT;
  DLIGHT_I2CRequest[3] = DLIGHT_CMD_ENABLE_BLINK;
  return writeI2C(link, DLIGHT_I2CRequest);
}


/**
 * Stop blinking the LED
 * @param link the dLight port number
 * @param addr the dLight I2C address
 * @return true if no error occured, false if it did
 */
bool DLIGHTstopBlinking(tSensors link, ubyte addr){
  DLIGHT_I2CRequest[0] = 3;
  DLIGHT_I2CRequest[1] = addr;
  DLIGHT_I2CRequest[2] = DLIGHT_REG_LEDOUT;
  DLIGHT_I2CRequest[3] = DLIGHT_CMD_DISABLE_BLINK;
  return writeI2C(link, DLIGHT_I2CRequest);
}


/**
 * Turn off the LED
 * @param link the dLight port number
 * @param addr the dLight I2C address
 * @return true if no error occured, false if it did
 */
bool DLIGHTdisable(tSensors link, ubyte addr)
{
  DLIGHT_I2CRequest[0] = 3;
  DLIGHT_I2CRequest[1] = addr;
  DLIGHT_I2CRequest[2] = DLIGHT_REG_LEDOUT;
  DLIGHT_I2CRequest[3] = DLIGHT_CMD_DISABLE_LEDS;
  return writeI2C(link, DLIGHT_I2CRequest);
}

#endif // __DLIGHT_H__

/*
 * $Id: dexterind-dlight.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
