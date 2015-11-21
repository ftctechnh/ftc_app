/*!@addtogroup other
 * @{
 * @defgroup pcf8574 Philips PCF8574
 * Philips PCF8574
 * @{
 */

/*
 * $Id: philips-pcf8574.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __PCF8574_H__
#define __PCF8574_H__
/** \file philips-pcf8574.h
 * \brief Philips PCF8574 IO MUX driver
 *
 * philips-pcf8574.h provides an API for the Philips PCF8574 IO MUX.
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
 * \date 30 March 2010
 * \version 0.1
 * \example philips-pcf8574-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define PCF8574_I2C_ADDR         0x70  /*!< HDMMUX I2C device address */


tByteArray PCF8574_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray PCF8574_I2CReply;      /*!< Array to hold I2C reply data */

// Function prototypes
bool PCF8574sendBytes(tSensors link, ubyte _byte);
bool PCF8574readBytes(tSensors link, ubyte &_byte);

/**
 * Send a byte to the PCF8574 to set the IO ports
 *
 * @param link the PCF8574 port number
 * @param _byte the byte to be sent
 * @return true if no error occured, false if it did
 */
bool PCF8574sendBytes(tSensors link, ubyte _byte) {
  memset(PCF8574_I2CRequest, 0, sizeof(tByteArray));

  PCF8574_I2CRequest[0] = 2;               // Message size
  PCF8574_I2CRequest[1] = PCF8574_I2C_ADDR; // I2C Address
  PCF8574_I2CRequest[2] = _byte;

  return writeI2C(link, PCF8574_I2CRequest);
}


/**
 * Read the current state of the ports on the PCF8574
 *
 * @param link the PCF8574 port number
 * @param _byte the byte thats been read
 * @return true if no error occured, false if it did
 */
bool PCF8574readBytes(tSensors link, ubyte &_byte) {
  memset(PCF8574_I2CRequest, 0, sizeof(tByteArray));

  PCF8574_I2CRequest[0] = 1;               // Message size
  PCF8574_I2CRequest[1] = PCF8574_I2C_ADDR; // I2C Address
  PCF8574_I2CRequest[2] = _byte;

  if (!writeI2C(link, PCF8574_I2CRequest, PCF8574_I2CReply, 1))
    return false;

  _byte = PCF8574_I2CReply[0];

  return true;

}


#endif //  __PCF8574_H__

/*
 * $Id: philips-pcf8574.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
