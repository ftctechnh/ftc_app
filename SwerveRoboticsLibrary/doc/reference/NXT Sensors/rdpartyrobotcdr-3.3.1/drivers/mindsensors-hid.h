/*!@addtogroup mindsensors
 * @{
 * @defgroup mshid HID Sensor
 * HID Sensor
 * @{
 */

/*
 * $Id: mindsensors-hid.h 133 2013-03-10 15:15:38Z xander $
 */

/** \file mindsensors-hid.h
 * \brief Mindsensors HID Sensor driver
 *
 * mindsensors-hid.h provides an API for the Mindsensors HID Sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Allow I2C address to be specified as an optional argument\n
 *        Added prototypes
 *
 * Credits:
 * - Big thanks to Mindsensors for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 18 December 2010
 * \version 0.2
 * \example mindsensors-hid-test1.c
 */

#pragma systemFile

#ifndef __MSHID_H__
#define __MSHID_H__

#ifndef __COMMON_H__
#include "common.h"
#endif

#define MSHID_I2C_ADDR    0x04
#define MSHID_CMD         0x41
#define MSHID_KEYBMOD     0x42
#define MSHID_KEYBDATA    0x43

#define MSHID_XMIT        0x54
#define MSHID_ASCII       0x41
#define MSHID_DDATA       0x44

// Keyboard modifyers
#define MSHID_MOD_NONE    0x00
#define MSHID_MOD_LCTRL   0x01
#define MSHID_MOD_LSHIFT  0x02
#define MSHID_MOD_LALT    0x04
#define MSHID_MOD_LGUI    0x08
#define MSHID_MOD_RCTRL   0x10
#define MSHID_MOD_RSHIFT  0x20
#define MSHID_MOD_RALT    0x40
#define MSHID_MOD_RGUI    0x80

bool MSHIDsendCommand(tSensors link, byte command, ubyte address = MSHID_I2C_ADDR);
bool MSHIDsendKeyboardData(tSensors link, byte modifier, byte keybdata, ubyte address = MSHID_I2C_ADDR);
bool MSHIDsendString(tSensors link, string data, ubyte address = MSHID_I2C_ADDR);

tByteArray MSHID_I2CRequest;       /*!< Array to hold I2C command data */


/**
 * Send a direct command to the HID sensor
 * @param link the HID port number
 * @param command the command to be sent
 * @param address the I2C address to use, optional, defaults to 0x04
 * @return true if no error occured, false if it did
 */
bool MSHIDsendCommand(tSensors link, byte command, ubyte address) {
  memset(MSHID_I2CRequest, 0, sizeof(tByteArray));
  MSHID_I2CRequest[0] = 3;
  MSHID_I2CRequest[1] = address;
  MSHID_I2CRequest[2] = MSHID_CMD;
  MSHID_I2CRequest[3] = command;

  return writeI2C(link, MSHID_I2CRequest);
}


/**
 * Send keyboard data to the HID sensor.  Must be followed by a MSHID_XMIT
 * command using MSHIDsendCommand()
 * @param link the HID port number
 * @param modifier the keyboard modifier, like shift, control. Can be OR'd together.
 * @param keybdata the keystroke to be sent to the computer
 * @param address the I2C address to use, optional, defaults to 0x04
 * @return true if no error occured, false if it did
 */
bool MSHIDsendKeyboardData(tSensors link, byte modifier, byte keybdata, ubyte address) {
  memset(MSHID_I2CRequest, 0, sizeof(tByteArray));
  MSHID_I2CRequest[0] = 4;
  MSHID_I2CRequest[1] = address;
  MSHID_I2CRequest[2] = MSHID_KEYBMOD;
  MSHID_I2CRequest[3] = modifier;
  MSHID_I2CRequest[4] = keybdata;

  return writeI2C(link, MSHID_I2CRequest);
}


/**
 * Send a string to the computer.  Can be up to 19 characters long.<br>
 * It recognises the following escaped keys:<br>
 * - \n: new line
 * - \r: carriage return
 * - \t: tab
 * - \\: a backslash
 * - \": double quote
 * @param link the HID port number
 * @param data the string to be transmitted
 * @param address the I2C address to use, optional, defaults to 0x04
 * @return true if no error occured, false if it did
 */
bool MSHIDsendString(tSensors link, string data, ubyte address) {
  byte buffer[19];
  int len = strlen(data);
  if (len < 20) {
    memcpy(buffer, data, len);
  } else {
    return false;
  }

  for (int i = 0; i < len; i++) {
		if (buffer[i] == 0x5C && i < (len - 1)) {
		  switch (buffer[i+1]) {
        case 'r':
					if (!MSHIDsendKeyboardData(link, MSHID_MOD_NONE, 0x0A))
					  return false;
					break;
        case 'n':
					if (!MSHIDsendKeyboardData(link, MSHID_MOD_NONE, 0x0D))
					  return false;
					break;
				case 't':
					if (!MSHIDsendKeyboardData(link, MSHID_MOD_NONE, 0x09))
					  return false;
					break;
				case 0x5C:
					if (!MSHIDsendKeyboardData(link, MSHID_MOD_NONE, 0x5C))
					  return false;
					break;
				case 0x22:
					if (!MSHIDsendKeyboardData(link, MSHID_MOD_NONE, 0x22))
					  return false;
					break;
        default:
					break;
			}
			i++;
		} else {
			if (!MSHIDsendKeyboardData(link, MSHID_MOD_NONE, buffer[i]))
			  return false;
	  }
		if (!MSHIDsendCommand(link, MSHID_XMIT))
		  return false;
    wait1Msec(50);
  }
  return true;
}

#endif // __MSHID_H__

/*
 * $Id: mindsensors-hid.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
