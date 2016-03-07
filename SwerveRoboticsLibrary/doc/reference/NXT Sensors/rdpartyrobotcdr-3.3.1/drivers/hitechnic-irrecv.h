/*!@addtogroup HiTechnic
 * @{
 * @defgroup htirr IR Receiver Sensor
 * HiTechnic IR Receiver Sensor
 * @{
 */

/*
 * $Id: hitechnic-irrecv.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTIRR_H__
#define __HTIRR_H__
/** \file hitechnic-irrecv.h
 * \brief HiTechnic IR Receiver Sensor driver
 *
 * HTIRR2-driver.h provides an API for the IR Receiver Sensor driver.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Changed HTIRRreadChannel() proto to use signed bytes like function.
 *
 * Credits:
 * - Big thanks to HiTechnic for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 03 November 2009
 * \version 0.2
 * \example hitechnic-irrecv-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define HTIRR_I2C_ADDR        0x02      /*!< HTIRR I2C device address */
#define HTIRR_OFFSET          0x42      /*!< Offset for data registers */

// Values contained by registers in active mode
#define HTIRR_MOTOR_1A        0x00      /*!< Color number */
#define HTIRR_MOTOR_1B        0x01      /*!< Color number */
#define HTIRR_MOTOR_2A        0x02      /*!< Color number */
#define HTIRR_MOTOR_2B        0x03      /*!< Color number */
#define HTIRR_MOTOR_3A        0x04      /*!< Color number */
#define HTIRR_MOTOR_3B        0x05      /*!< Color number */
#define HTIRR_MOTOR_4A        0x06      /*!< Color number */
#define HTIRR_MOTOR_4B        0x07      /*!< Color number */

/*! Some define. */
#define MOTOR_BRAKE           -128      /*!< Motor brake */

/*
<function prototypes>
*/
bool HTIRRreadChannel(tSensors link, byte channel, sbyte &motA, sbyte &motB);
bool HTIRRreadAllChannels(tSensors link, tsByteArray &motorSpeeds);

tByteArray HTIRR_I2CRequest;           /*!< Array to hold I2C command data */
tByteArray HTIRR_I2CReply;             /*!< Array to hold I2C reply data */


/**
 * Get the speeds of the motors for a given channel.
 * @param link the HTIRR port number
 * @param channel the channel number (1 to 4)
 * @param motA the speed for Motor A (-100 to +100, -128 = brake)
 * @param motB the speed for Motor B (-100 to +100, -128 = brake)
 * @return true if no error occured, false if it did
 */
bool HTIRRreadChannel(tSensors link, byte channel, sbyte &motA, sbyte &motB) {
  memset(HTIRR_I2CRequest, 0, sizeof(tByteArray));

  HTIRR_I2CRequest[0] = 2;                                // Message size
  HTIRR_I2CRequest[1] = HTIRR_I2C_ADDR;                   // I2C Address
  HTIRR_I2CRequest[2] = HTIRR_OFFSET + ((channel - 1) * 2); // Start of speed registry

  if (!writeI2C(link, HTIRR_I2CRequest, HTIRR_I2CReply, 2))
    return false;

  motA = (HTIRR_I2CReply[0] >= 128) ? (int)HTIRR_I2CReply[0] - 256 : (int)HTIRR_I2CReply[0];
  motB = (HTIRR_I2CReply[1] >= 128) ? (int)HTIRR_I2CReply[1] - 256 : (int)HTIRR_I2CReply[1];

  return true;
}


/**
 * Get the speeds of the motors for all channels.
 * @param link the HTIRR port number
 * @param motorSpeeds the speeds for all the motors (-100 to +100, -128 = brake)
 * @return true if no error occured, false if it did
 */
bool HTIRRreadAllChannels(tSensors link, tsByteArray &motorSpeeds){
  memset(motorSpeeds, 0, sizeof(tsByteArray));
  memset(HTIRR_I2CRequest, 0, sizeof(tByteArray));

  HTIRR_I2CRequest[0] = 2;                // Message size
  HTIRR_I2CRequest[1] = HTIRR_I2C_ADDR;   // I2C Address
  HTIRR_I2CRequest[2] = HTIRR_OFFSET;     // Start of speed registry

  if (!writeI2C(link, HTIRR_I2CRequest, HTIRR_I2CReply, 8))
    return false;

  memcpy(motorSpeeds, HTIRR_I2CReply, 8);
  return true;
}

#endif // __HTIRR_H__
/*
 * $Id: hitechnic-irrecv.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
