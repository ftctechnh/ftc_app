/*!@addtogroup mindsensors
 * @{
 * @defgroup MSNP Numeric Keypad Sensor
 * Numeric Keypad Numeric Keypad Sensor
 * @{
 */

/*
 * $Id: mindsensors-numericpad.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSNP_H__
#define __MSNP_H__
/** \file mindsensors-numericpad.h
 * \brief Mindsensors Numeric Keypad Sensor driver
 *
 * mindsensors-numericpad.h provides an API for the Mindsensors Numeric Keypad Sensor.\n
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
 * \date 30 October 2010
 * \version 0.1
 * \example mindsensors-numericpad-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define MSNP_I2C_ADDR  0xB4     /*!< Numeric Pad I2C device address */
#define MSNP_DATA_REG  0x00     /*!< Data registers start at 0x00 */

tByteArray MSNP_I2CRequest;     /*!< Array to hold I2C command data */
tByteArray MSNP_I2CReply;       /*!< Array to hold I2C reply data */


#define KEY_STATUS_REG 0x00

const unsigned byte MSNP_KeyMap[] = { '#', '9', '6', '3', '0', '8', '2', '5', '*', '7', '1', '4' };
const signed byte MSNP_NumMap[] = { -1, 9, 6, 3, 0, 8, 2, 5, -2, 7, 1, 4};

const unsigned byte MSNP_ConfigGroup1[] = {10, MSNP_I2C_ADDR, 0x2B, 0x01, 0x01, 0x00, 0x00, 0x01, 0x01, 0xFF, 0x02};
const unsigned byte MSNP_ConfigGroup2[] = {16, MSNP_I2C_ADDR, 0x41, 0x0F, 0x0A, 0x0F, 0x0A, 0x0F, 0x0A, 0x0F, 0x0A, 0x0F, 0x0A, 0x0F, 0x0A, 0x0F, 0x0A};
const unsigned byte MSNP_ConfigGroup3[] = {14, MSNP_I2C_ADDR, 0x4F, 0x0F, 0x0A, 0x0F, 0x0A, 0x0F, 0x0A, 0x0F, 0x0A, 0x0F, 0x0A, 0x0F, 0x0A};
const unsigned byte MSNP_ConfigGroup4[] = { 5, MSNP_I2C_ADDR, 0x5C, 0x0B, 0x20, 0x0C};
const unsigned byte MSNP_ConfigGroup5[] = { 3, MSNP_I2C_ADDR, 0x7B, 0x0B};
const unsigned byte MSNP_ConfigGroup6[] = { 5, MSNP_I2C_ADDR, 0x7D, 0x9C, 0x65, 0x8C};

bool _MSNPinitialised[] = {false, false, false, false};


/**
 * Initialise the numeric pad sensor chip.  This must be done at the beginning
 * of every power cycle\n
 * Note: this is an internal function and should not be called directly.
 * @param link the MSNP port number
 * @return true if no error occured, false if it did
 */
bool _MSNPinit(tSensors link) {
// Must be called at the beginning of every power cycle

  memcpy(MSNP_I2CRequest, MSNP_ConfigGroup1, sizeof(MSNP_ConfigGroup1));
  if (!writeI2C(link, MSNP_I2CRequest))
    return false;

  memcpy(MSNP_I2CRequest, MSNP_ConfigGroup2, sizeof(MSNP_ConfigGroup2));
  if (!writeI2C(link, MSNP_I2CRequest))
    return false;

  memcpy(MSNP_I2CRequest, MSNP_ConfigGroup3, sizeof(MSNP_ConfigGroup3));
  if (!writeI2C(link, MSNP_I2CRequest))
    return false;

  memcpy(MSNP_I2CRequest, MSNP_ConfigGroup4, sizeof(MSNP_ConfigGroup4));
  if (!writeI2C(link, MSNP_I2CRequest))
    return false;

  memcpy(MSNP_I2CRequest, MSNP_ConfigGroup5, sizeof(MSNP_ConfigGroup5));
  if (!writeI2C(link, MSNP_I2CRequest))
    return false;

  memcpy(MSNP_I2CRequest, MSNP_ConfigGroup6, sizeof(MSNP_ConfigGroup6));
  if (!writeI2C(link, MSNP_I2CRequest))
    return false;

  return true;
}


/**
 * Scan the keypad for pressed keys.  This returns immediately.
 * @param link the MSNP port number
 * @param pressedKeys a binary representation of all keys pressed
 * @param key character representation of the first key pressed, X if no key was pressed
 * @param number the numeric equivalent, 0-9 for digits, -1 for # and -2 for *, -255 when nothing is pressed
 * @return true if no error occured, false if it did
 */
bool MSNPscanKeys(tSensors link, int &pressedKeys, unsigned byte &key, int &number) {
  //int keyPress = 0;

  if (!_MSNPinitialised[link]) {
    _MSNPinit(link);
    _MSNPinitialised[link] = true;
    wait1Msec(10);
  }

  key = 'X';
  number = -255;

  MSNP_I2CRequest[0] = 2;
  MSNP_I2CRequest[1] = MSNP_I2C_ADDR;
  MSNP_I2CRequest[2] = MSNP_DATA_REG;

	if (!writeI2C(link, MSNP_I2CRequest, MSNP_I2CReply, 2))
	  return false;

  pressedKeys = MSNP_I2CReply[0] + (MSNP_I2CReply[1] * 256);

	for (int i=0; i < 12; i++) {
	  if (pressedKeys & (1<<i)) {
	    key = MSNP_KeyMap[i];
	    number = MSNP_NumMap[i];
	    return true;
	  }
	}

	return true;
}


/**
 * Scan the keypad for pressed keys.  This returns immediately.
 * @param link the MSNP port number
 * @return number the numeric equivalent, 0-9 for digits, -1 for # and -2 for *, -255 when nothing is pressed
 */
int MSNPscanKeys(tSensors link) {
  int keyPress = 0;

  if (!_MSNPinitialised[link]) {
    _MSNPinit(link);
    _MSNPinitialised[link] = true;
    wait1Msec(10);
  }


  MSNP_I2CRequest[0] = 2;
  MSNP_I2CRequest[1] = MSNP_I2C_ADDR;
  MSNP_I2CRequest[2] = MSNP_DATA_REG;

	if (!writeI2C(link, MSNP_I2CRequest, MSNP_I2CReply, 2))
	  return -255;

  keyPress = MSNP_I2CReply[0] + (MSNP_I2CReply[1] * 256);

	for (int i=0; i < 12; i++) {
	  if (keyPress & (1<<i)) {
	    return MSNP_NumMap[i];
	  }
	}

	return -255;
}


#endif // __MSNP_H__

/*
 * $Id: mindsensors-numericpad.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
