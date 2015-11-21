/*!@addtogroup mindsensors
 * @{
 * @defgroup nxtservo Servo Controller
 * Servo Controller
 * @{
 */

/*
 * $Id: mindensors-servo.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __NXTSERVO_H__
#define __NXTSERVO_H__
/** \file mindensors-servo.h
 * \brief Mindsensors NXTServo Sensor Driver
 *
 * mindensors-servo.h provides an API for the Mindsensors NXTServo Sensor
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
 * \date 30 September 2009
 * \version 0.1
 * \example mindensors-servo-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define NXTSERVO_I2C_ADDR     0xB0      /*!< NXT Servo I2C device address */

// Command register, can be written for commands and read for battery voltage level
#define NXTSERVO_CMD          0x41      /*!< Servo command register
*/
// Position registers, can be read and written
#define NXTSERVO_POS_CHAN1    0x42      /*!< Servo channel 1 position (low byte) */
#define NXTSERVO_POS_CHAN2    0x44      /*!< Servo channel 2 position (low byte) */
#define NXTSERVO_POS_CHAN3    0x46      /*!< Servo channel 3 position (low byte) */
#define NXTSERVO_POS_CHAN4    0x48      /*!< Servo channel 4 position (low byte) */
#define NXTSERVO_POS_CHAN5    0x4A      /*!< Servo channel 5 position (low byte) */
#define NXTSERVO_POS_CHAN6    0x4C      /*!< Servo channel 6 position (low byte) */
#define NXTSERVO_POS_CHAN7    0x4E      /*!< Servo channel 7 position (low byte) */
#define NXTSERVO_POS_CHAN8    0x50      /*!< Servo channel 8 position (low byte) */

// Speed registers, can be read and written
#define NXTSERVO_SPEED_CHAN1  0x52      /*!< Servo channel 1 speed */
#define NXTSERVO_SPEED_CHAN2  0x53      /*!< Servo channel 2 speed */
#define NXTSERVO_SPEED_CHAN3  0x54      /*!< Servo channel 3 speed */
#define NXTSERVO_SPEED_CHAN4  0x55      /*!< Servo channel 4 speed */
#define NXTSERVO_SPEED_CHAN5  0x56      /*!< Servo channel 5 speed */
#define NXTSERVO_SPEED_CHAN6  0x57      /*!< Servo channel 6 speed */
#define NXTSERVO_SPEED_CHAN7  0x58      /*!< Servo channel 7 speed */
#define NXTSERVO_SPEED_CHAN8  0x59      /*!< Servo channel 8 speed */

// Quick position register, can only be written
#define NXTSERVO_QPOS_CHAN1   0x42      /*!< Servo channel 1 quick position */
#define NXTSERVO_QPOS_CHAN2   0x44      /*!< Servo channel 2 quick position */
#define NXTSERVO_QPOS_CHAN3   0x46      /*!< Servo channel 3 quick position */
#define NXTSERVO_QPOS_CHAN4   0x48      /*!< Servo channel 4 quick position */
#define NXTSERVO_QPOS_CHAN5   0x4A      /*!< Servo channel 5 quick position */
#define NXTSERVO_QPOS_CHAN6   0x4C      /*!< Servo channel 6 quick position */
#define NXTSERVO_QPOS_CHAN7   0x4E      /*!< Servo channel 7 quick position */
#define NXTSERVO_QPOS_CHAN8   0x50      /*!< Servo channel 8 quick position */

/*! Some other defines. */
#define NXTSERVO_MIN_POS      500       /*!< Servo minimum pulse width in uS */
#define NXTSERVO_MAX_POS     2500       /*!< Servo maximum pulse width in uS */
#define NXTSERVO_MID_POS     1500       /*!< Servo centered pulse width in uS */

/*
<function prototypes>
*/
bool NXTServoSetSpeed(tSensors link, ubyte servochan, ubyte speed, ubyte address);
bool NXTServoSetPos(tSensors link, ubyte servochan, int position, ubyte speed, ubyte address = NXTSERVO_I2C_ADDR);
bool NXTServoQSetPos(tSensors link, ubyte servochan, ubyte position, byte speed, ubyte address = NXTSERVO_I2C_ADDR);
int NXTServoReadVoltage(tSensors link, ubyte address = NXTSERVO_I2C_ADDR);

tByteArray NXTSERVO_I2CRequest;         /*!< Array to hold I2C command data */
tByteArray NXTSERVO_I2CReply;           /*!< Array to hold I2C reply data */


/**
 * Set the speed register for the specified servo.  This is the amount to increase
 * the current position by every 24ms when the servo position is changed.
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the NXTServo port number
 * @param servochan the servo channel to use
 * @param speed the amount to increase the position by every 24ms
 * @param address the I2C address to use, optional, defaults to 0xB0
 * @return true if no error occured, false if it did
 */
bool NXTServoSetSpeed(tSensors link, ubyte servochan, ubyte speed, ubyte address) {
  memset(NXTSERVO_I2CRequest, 0, sizeof(tByteArray));
  NXTSERVO_I2CRequest[0] = 3;
  NXTSERVO_I2CRequest[1] = address;
  NXTSERVO_I2CRequest[2] = NXTSERVO_SPEED_CHAN1 + (servochan - 1);
  NXTSERVO_I2CRequest[3] = speed;

  return writeI2C(link, NXTSERVO_I2CRequest);
}


/**
 * Tell the servo to move to the specified position using the specified speed.
 * @param link the NXTServo port number
 * @param servochan the servo channel to use
 * @param position the position to move the servo to
 * @param speed the amount to increase the position by every 24ms
 * @param address the I2C address to use, optional, defaults to 0xB0
 * @return true if no error occured, false if it did
 */
bool NXTServoSetPos(tSensors link, ubyte servochan, int position, ubyte speed, ubyte address) {
  memset(NXTSERVO_I2CRequest, 0, sizeof(tByteArray));
  if (!NXTServoSetSpeed(link, servochan, speed, address))
    return false;

  position = clip(position, 500, 2500);

  // set the position register and tell NXTServo to move the servo
  memset(NXTSERVO_I2CRequest, 0, sizeof(tByteArray));
  NXTSERVO_I2CRequest[0] = 4;
  NXTSERVO_I2CRequest[1] = address;
  NXTSERVO_I2CRequest[2] = NXTSERVO_POS_CHAN1 + ((servochan - 1) * 2);
  NXTSERVO_I2CRequest[3] = position & 0x00FF;
  NXTSERVO_I2CRequest[4] = (position >> 8) & 0x00FF;

  return writeI2C(link, NXTSERVO_I2CRequest);
}



/**
 * Tell the servo to move to the specified position using the specified speed.
 * This uses the Quick Position register which is less accurate.  It only accepts
 * values from 50 to 250
 * @param link the NXTServo port number
 * @param servochan the servo channel to use
 * @param position the position to move the servo to
 * @param speed the amount to increase the position by every 24ms
 * @param address the I2C address to use, optional, defaults to 0xB0
 * @return true if no error occured, false if it did
 */
bool NXTServoQSetPos(tSensors link, ubyte servochan, ubyte position, byte speed, ubyte address) {
  memset(NXTSERVO_I2CRequest, 0, sizeof(tByteArray));

  position = clip(position, 50, 250);

  if (!NXTServoSetSpeed(link, servochan, speed, address))
    return false;

  // set the position register and tell NXTServo to move the servo

  NXTSERVO_I2CRequest[0] = 3;
  NXTSERVO_I2CRequest[1] = address;
  NXTSERVO_I2CRequest[2] = NXTSERVO_QPOS_CHAN1 + (servochan - 1);
  NXTSERVO_I2CRequest[3] = position;

  return writeI2C(link, NXTSERVO_I2CRequest);
}


/**
 * Get the voltage level of the battery pack.
 * @param link the NXTServo port number
 * @param address the I2C address to use, optional, defaults to 0xB0
 * @return the voltage of the battery pack or -1 if an error occurred
 */
int NXTServoReadVoltage(tSensors link, ubyte address) {
  long mvs = 0;

  memset(NXTSERVO_I2CRequest, 0, sizeof(tByteArray));

  NXTSERVO_I2CRequest[0] = 2;                 // Message size
  NXTSERVO_I2CRequest[1] = address;           // I2C Address
  NXTSERVO_I2CRequest[2] = NXTSERVO_CMD;      // Start red sensor value

  if (!writeI2C(link, NXTSERVO_I2CRequest, NXTSERVO_I2CReply, 1))
    return -1;

  mvs = ((long)NXTSERVO_I2CReply[0] * 3886) / 100;

  return mvs;
}

#endif // __NXTSERVO_H__
/*
 * $Id: mindensors-servo.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
