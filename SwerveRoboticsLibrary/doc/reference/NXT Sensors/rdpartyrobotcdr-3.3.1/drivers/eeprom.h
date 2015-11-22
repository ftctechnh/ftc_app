/*!@addtogroup other
 * @{
 * @defgroup EEPROM Generic I2C EEPROM
 * Generic I2C EEPROM
 * @{
 */

/*
 * $Id: eeprom.h 133 2013-03-10 15:15:38Z xander $
 */

/** \file eeprom.h
 * \brief RobotC EEPROM Driver
 *
 * eeprom.h provides an API for the AT24C512, 24AA512 and 24AA1025 EEPROMs.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Removed array structs, replaced with typedefs
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 20 February 2011
 * \version 0.2
 * \example eeprom-test1.c
 * \example eeprom-test2.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define EEPROM_I2C_ADDR 0xA0      /*!< EEPROM I2C device address */

/*! Page for the AT24C512 */
#define EEPROM_PAGE_SIZE   128

tByteArray EEPROM_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray EEPROM_I2CReply;      /*!< Array to hold I2C reply data */

/*
<function prototypes>
*/
bool EEPROMreadBytes(tSensors link, long address, tByteArray &data, int numbytes);
bool EEPROMwriteBytes(tSensors link, long address, tByteArray &data, int numbytes);
bool _EEPROMwriteDummy(tSensors link, long address);


/**
 * Read a number of bytes from address
 * @param link the EEPROM
 * @param address the address to read from
 * @param data the byte array to store read result in
 * @param numbytes the number of bytes to read (limited to 16 at a time)
 * @return true if no error occured, false if it did
 */
bool EEPROMreadBytes(tSensors link, long address, tByteArray &data, int numbytes) {
  if (!_EEPROMwriteDummy(link, address))
    return false;

  memset(EEPROM_I2CRequest, 0, sizeof(tByteArray));

  EEPROM_I2CRequest[0] = 1;               // Message size
  EEPROM_I2CRequest[1] = EEPROM_I2C_ADDR; //I2C Address

  if (!writeI2C(link, EEPROM_I2CRequest, EEPROM_I2CReply, numbytes))
    return false;

  memcpy(data, EEPROM_I2CReply, sizeof(tByteArray));
  return true;
}


/**
 * Perform a dummy write to set address for next read
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the EEPROM
 * @param address the address to set
 * @return true if no error occured, false if it did
 */
bool _EEPROMwriteDummy(tSensors link, long address) {
  memset(EEPROM_I2CRequest, 0, sizeof(tByteArray));

  EEPROM_I2CRequest[0] = 3;                             // Message size
  EEPROM_I2CRequest[1] = EEPROM_I2C_ADDR;               // I2C Address
  EEPROM_I2CRequest[2] = (byte)((address >> 8) & 0xFF); // upper 8 bits of address word
  EEPROM_I2CRequest[3] = (byte)(address & 0xFF);        // lower 8 bits of address word

  return writeI2C(link, EEPROM_I2CRequest);
}


/**
 * Write a single byte to address
 * @param link the EEPROM
 * @param address the address to write to
 * @param data the byte array to write
 * @param numbytes the number of bytes to write (limited to 13 at a time)
 * @return true if no error occured, false if it did
 */
bool EEPROMwriteBytes(tSensors link, long address, tByteArray &data, int numbytes) {
  memset(EEPROM_I2CRequest, 0, sizeof(tByteArray));

  EEPROM_I2CRequest[0] = 3 + numbytes;                  // Message size
  EEPROM_I2CRequest[1] = EEPROM_I2C_ADDR;               // I2C Address
  EEPROM_I2CRequest[2] = (byte)((address >> 8) & 0xFF); // upper 8 bits of address word
  EEPROM_I2CRequest[3] = (byte)(address & 0xFF);        // lower 8 bits of address word

  if (numbytes > 13)
    numbytes = 13;

  if (numbytes < 0)
    numbytes = 0;

  memcpy(&EEPROM_I2CRequest[4], &data[0], numbytes);
  return writeI2C(link, EEPROM_I2CRequest);
}

/*
 * $Id: eeprom.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
