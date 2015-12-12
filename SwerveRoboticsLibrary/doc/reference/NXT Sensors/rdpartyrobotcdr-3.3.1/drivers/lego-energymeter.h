/*!@addtogroup lego
 * @{
 * @defgroup legoem Energy Meter
 * Energy Meter
 * @{
 */

/*
 * $Id: lego-energymeter.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __LEGOEM_DRIVER_H__
#define __LEGOEM_DRIVER_H__

/** \file lego-energymeter.h
 * \brief RobotC Energy Meter Driver
 *
 * lego-energymeter.h provides an API for the Lego Energy Meter.
 *
 * Changelog:
 * - 0.1: Initial release
 *
 * Credits :
 * - David Cosimano for sending me one of these.
 * - John Hansen for providing me with the specs.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (mightor@gmail.com)
 * \date 22 August 2010
 * \version 0.1
 * \example lego-energymeter-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*
<Address definitions>
*/
#define LEGOEM_I2C_ADDR 0x04 /*!< Energy Meter I2C device address */
#define LEGOEM_I2C_REG  0x0A /*!< Start of I2C registers that need to be read */
#define LEGOEM_I2C_SIZE   14 /*!< Number of registers to read at once */


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Function prototypes
bool LEGOEMreadData(tSensors link, float &voltageIn, float &currentIn, float &voltageOut, float &currentOut, int &joule, float &wattIn, float &wattOut);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// global variables
tByteArray       LEGOEM_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray       LEGOEM_I2CReply;      /*!< Array to hold I2C reply data   */

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/**
 * Read a snapshot of the current register values.  They must all be read at once to
 * ensure data coherency.
 *
 * @param link the LEGO Energy Meter port number
 * @param voltageIn voltage level on input
 * @param currentIn current supplied on input
 * @param voltageOut voltage on output
 * @param currentOut current drawn on output
 * @param joule number of Joules stored on E-Meter
 * @param wattIn amount of Watts coming in
 * @param wattOut amount of Watts being consumed
 * @return true if no error occured, false if it did
 */
bool LEGOEMreadData(tSensors link, float &voltageIn, float &currentIn, float &voltageOut, float &currentOut, int &joule, float &wattIn, float &wattOut) {
  memset(LEGOEM_I2CRequest, 0, sizeof(tByteArray));

  LEGOEM_I2CRequest[0] = 2;                // Message size
  LEGOEM_I2CRequest[1] = LEGOEM_I2C_ADDR;  // I2C Address
  LEGOEM_I2CRequest[2] = LEGOEM_I2C_REG;   // Value address

  if (!writeI2C(link, LEGOEM_I2CRequest, LEGOEM_I2CReply, LEGOEM_I2C_SIZE))
    return false;

  voltageIn  = (float)(LEGOEM_I2CReply[0]  + (LEGOEM_I2CReply[1]  << 8)) / 1000;
  currentIn  = (float)(LEGOEM_I2CReply[2]  + (LEGOEM_I2CReply[3]  << 8)) / 1000;
  voltageOut = (float)(LEGOEM_I2CReply[4]  + (LEGOEM_I2CReply[5]  << 8)) / 1000;
  currentOut = (float)(LEGOEM_I2CReply[6]  + (LEGOEM_I2CReply[7]  << 8)) / 1000;
  joule      =         LEGOEM_I2CReply[8]  + (LEGOEM_I2CReply[9]  << 8);
  wattIn     = (float)(LEGOEM_I2CReply[10] + (LEGOEM_I2CReply[11] << 8)) / 1000;
  wattOut    = (float)(LEGOEM_I2CReply[12] + (LEGOEM_I2CReply[13] << 8)) / 1000;
  return true;
}

#endif // __LEGOEM_DRIVER_H__

/*
 * $Id: lego-energymeter.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
