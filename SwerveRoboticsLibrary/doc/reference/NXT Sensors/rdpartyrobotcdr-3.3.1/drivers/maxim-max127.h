/*!@addtogroup other
 * @{
 * @defgroup max127 MAXIM MAX127 ADC
 * MAXIM MAX127 ADC
 * @{
 */

/*
 * $Id: maxim-max127.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MAX127_H__
#define __MAX127_H__
/** \file maxim-max127.h
 * \brief MAXIM MAX127 ADC driver
 *
 * hitechnic-irseeker-v1.h provides an API for the MAXIM MAX127 ADC.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.5: Major rewrite of code, uses common.h for most functions
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 08 March 2009
 * \version 0.5
 * \example maxim-max127-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif //__COMMON_H__

#define MAX127_I2C_ADDR 0x50     /*!< MAX127 default I2C device address */

tByteArray MAX127_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray MAX127_I2CReply;      /*!< Array to hold I2C reply data */

int MAX127readChan(tSensors link, byte i2caddress, byte adcchannel);

/**
 * Returns the current analogue value as measured on the specified channel.
 * @param link the MAX127 port number
 * @param i2caddress the I2C address the MAX127 is configured for.  Use MAX127_I2C_ADDR for the default.
 * @param adcchannel the ADC channel number (0-7)
 * @return value of the ADC channel or -1 if an error occurred.
 */
int MAX127readChan(tSensors link, byte i2caddress, byte adcchannel) {
  int _chVal = 0;

  memset(MAX127_I2CRequest, 0, sizeof(tByteArray));

  MAX127_I2CRequest[0] = 2;                            // Message size
  MAX127_I2CRequest[1] = i2caddress;                   // I2C Address
  MAX127_I2CRequest[2] = (1 << 7) + (adcchannel << 4); // Control Byte
  // Control byte is calculated as follows:
  // start bit (7) + channel number bit-shifted into place

  if (!writeI2C(link, MAX127_I2CRequest, MAX127_I2CReply, 2))
    return -1;

  // Convert the bytes into ints
  // 1st byte contains bits 11-4 of the channel's value
  // 2nd byte contains bits 3-0 of the channel's value, followed by 4 0's
  // We'll need to shift the 1st byte left by 4 and the 2nd byte to the right by 4
  _chVal = ((int)(MAX127_I2CReply[0]) << 4) + ((int)MAX127_I2CReply[1] >> 4);

  return _chVal;
}
#endif // __MAX127_H__

/*
 * $Id: maxim-max127.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
