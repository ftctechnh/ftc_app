/*!@addtogroup mindsensors
 * @{
 * @defgroup mptp TouchPanel
 * TouchPanel
 * @{
 */

/*
 * $Id: mindsensors-touchpanel.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSTP_H__
#define __MSTP_H__

/** \file mindsensors-touchpanel.h
 * \brief Mindsensors TouchPanel
 *
 * mindsensors-touchpanel.h provides an API for the Mindsensors TouchPanel.
 *
 * Changelog:
 *  - 0.1 Initial	release.
 *
 * Credits:
 * - Big thanks to Mindsensors for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where it's due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat
 * \date 30 November 2011
 * \version 0.1
 * \example mindsensors-touchpanel-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
	#include "common.h"
#endif

#define MSTP_I2C_ADDR     0x04
#define MSTP_REG_TOUCH_X  0x42
#define MSTP_REG_TOUCH_Y  0x43
#define MSTP_REG_BUTTONS  0x44

#define MSTP_REG_CAL_XD1  0x45
#define MSTP_REG_CAL_YD1  0x46
#define MSTP_REG_CAL_XT1  0x47
#define MSTP_REG_CAL_YT1  0x48
#define MSTP_REG_CAL_XD2  0x49
#define MSTP_REG_CAL_YD2  0x4A
#define MSTP_REG_CAL_XT2  0x4B
#define MSTP_REG_CAL_YT2  0x4C
#define MSTP_REG_G_AVAIL  0x4D
#define MSTP_REG_G_NEXTX  0x4E
#define MSTP_REG_G_NEXTY  0x4F

#define BUTTON_L1         0
#define BUTTON_L2         1
#define BUTTON_L3         2
#define BUTTON_L4         3
#define BUTTON_R1         4
#define BUTTON_R2         5
#define BUTTON_R3         6
#define BUTTON_R4         7

// raw ubyte count
#define MSTP_REG_RAW_BC 0x4D
#define MSTP_REG_RAW_X  0x4E
#define MSTP_REG_RAW_Y  0x4F

// typedef ubyte tHugeArray[220];

tByteArray MSTP_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray MSTP_I2CReply;      /*!< Array to hold I2C reply data */
// tHugeArray MSTP_GestureData;

#define isButtonTouched(X, Y) (X >> Y) & 0x01         /*!< Check if the specified button is pressed */

#define isButtonL1Touched(X) (X >> BUTTON_L1) & 0x01  /*!< Check if the L1 button is pressed */
#define isButtonL2Touched(X) (X >> BUTTON_L2) & 0x01  /*!< Check if the L2 button is pressed */
#define isButtonL3Touched(X) (X >> BUTTON_L3) & 0x01  /*!< Check if the L3 button is pressed */
#define isButtonL4Touched(X) (X >> BUTTON_L4) & 0x01  /*!< Check if the L4 button is pressed */
#define isButtonR1Touched(X) (X >> BUTTON_R1) & 0x01  /*!< Check if the R1 button is pressed */
#define isButtonR2Touched(X) (X >> BUTTON_R2) & 0x01  /*!< Check if the R2 button is pressed */
#define isButtonR3Touched(X) (X >> BUTTON_R3) & 0x01  /*!< Check if the R3 button is pressed */
#define isButtonR4Touched(X) (X >> BUTTON_R4) & 0x01  /*!< Check if the R4 button is pressed */


bool MSTPgetTouch(tSensors link, int &x, int &y, ubyte &buttons, ubyte addr = MSTP_I2C_ADDR);
bool MSTPsendCmd(tSensors link, ubyte cmd, ubyte addr = MSTP_I2C_ADDR);

#define MSTPresetCalibration(x) MSTPsendCmd(tSensors link, ubyte cmd, ubyte addr = MSTP_I2C_ADDR);


/**
 * Fetch all the information from the TouchPanel, including
 * coordinates of current touch and button state.
 * by configuring the sensor as sensorLightInactive
 * @param link the TouchPanel port number
 * @param x the X coordinate of the current pen position, 0 if nothing is detected
 * @param y the Y coordinate of the current pen position, 0 if nothing is detected
 * @param buttons the currently touched buttons, use isButtonXXTouched() to check the individual status.
 * @param addr the I2C address of the TouchPanel, is optional, defaults to 0x04
 * @return true if no error has occured, false if it did
 */
bool MSTPgetTouch(tSensors link, int &x, int &y, ubyte &buttons, ubyte addr) {
  memset(MSTP_I2CRequest, 0, sizeof(tByteArray));

  MSTP_I2CRequest[0] = 2;                      // Message size
  MSTP_I2CRequest[1] = addr;         // I2C Address
  MSTP_I2CRequest[2] = MSTP_REG_TOUCH_X;

  if (!writeI2C(link, MSTP_I2CRequest, MSTP_I2CReply, 3))
    return false;

  x = MSTP_I2CReply[0];
  y = MSTP_I2CReply[1];
  buttons = MSTP_I2CReply[2];

  return true;
}


/**
 * Send a command to the TouchPanel
 * @param link the TouchPanel port number
 * @param cmd the command to be sent
 * @param addr the I2C address of the TouchPanel, is optional, defaults to 0x04
 * @return true if no error has occured, false if it did
 */
bool MSTPsendCmd(tSensors link, ubyte cmd, ubyte addr)
{
  memset(MSTP_I2CRequest, 0, sizeof(tByteArray));

  MSTP_I2CRequest[0] = 3;                      // Message size
  MSTP_I2CRequest[1] = MSTP_I2C_ADDR;         // I2C Address
  MSTP_I2CRequest[2] = 0x41;         //
  MSTP_I2CRequest[3] = cmd;

  return writeI2C(link, MSTP_I2CRequest);
}


/*
int MSTPgetGesture(tSensors link)
{
  memset(MSTP_I2CRequest, 0, sizeof(tByteArray));

  MSTP_I2CRequest[0] = 2;                      // Message size
  MSTP_I2CRequest[1] = addr;         // I2C Address
  MSTP_I2CRequest[2] = MSTP_REG_G_AVAIL;

  if (!writeI2C(link, MSTP_I2CRequest, 1))
    return false;

  if (!readI2C(link, MSTP_I2CReply, 1))
    return false;

  if (MSTP_I2CReply[0] == 0)
    return 0;


}
*/
#endif // __MSTP_H__

/*
 * $Id: mindsensors-touchpanel.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
