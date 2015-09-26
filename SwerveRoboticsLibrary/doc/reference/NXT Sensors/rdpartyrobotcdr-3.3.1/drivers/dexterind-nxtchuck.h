/*!@addtogroup Dexter_Industries
* @{
* @defgroup NXTCHUCK NXTChuck Sensor
* Dexter Industries NXTChuck Sensor driver
* @{
*/
/*
 * $Id: dexterind-nxtchuck.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __NXTCHUCK_H__
#define __NXTCHUCK_H__
/** \file dexterind-nxtchuck.h
 * \brief Dexter Industries NXTChuck Sensor driver
 *
 * dexterind-nxtchuck.h provides an API for the Dexter Industries NXTChuck Sensor.
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
 * \date 02 November 2012
 * \version 0.1
 * \example dexterind-nxtchuck-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define NXTCHUCK_COM_ERROR                        0
#define NXTCHUCK_COM_SUCCESS                      1

#define NXTCHUCK_I2C_ADDRESS                      0xA4

#define NXTCHUCK_DEVICE_UNKNOWN                   1
#define NXTCHUCK_DEVICE_NUNCHUK                   2
#define NXTCHUCK_DEVICE_CLASSIC_CONTROLLER        3
#define NXTCHUCK_DEVICE_GH_GUITAR                 4
#define NXTCHUCK_DEVICE_GH_DRUMS                  5
#define NXTCHUCK_DEVICE_DJH_TURNTABLE             6
#define NXTCHUCK_DEVICE_BALANCE_BOARD             7
#define NXTCHUCK_DEVICE_MOTION_PLUS_ACTIVE        8
#define NXTCHUCK_DEVICE_MOTION_PLUS_ACTIVE_N_PT   9
#define NXTCHUCK_DEVICE_MOTION_PLUS_ACTIVE_CC_PT  10
#define NXTCHUCK_DEVICE_MOTION_PLUS_INACTIVE      11
#define NXTCHUCK_DEVICE_MOTION_PLUS_NL_ACTIVE     12
#define NXTCHUCK_DEVICE_MOTION_PLUS_NL_N_PT       13
#define NXTCHUCK_DEVICE_MOTION_PLUS_NL_CC_PT      14
#define NXTCHUCK_DEVICE_NUNCHUK_BLACK             15

#define NXTCHUCK_N_BTN_Z                          0x01
#define NXTCHUCK_N_BTN_C                          0x02

#define NXTCHUCK_CC_BTN_RT 0x0002 // Right shoulder button
#define NXTCHUCK_CC_BTN_P  0x0004 // Start button
#define NXTCHUCK_CC_BTN_H  0x0008 // Home button
#define NXTCHUCK_CC_BTN_M  0x0010 // Select button
#define NXTCHUCK_CC_BTN_LT 0x0020 // Left Shoulder button
#define NXTCHUCK_CC_BTN_DD 0x0040 // d-pad down
#define NXTCHUCK_CC_BTN_DR 0x0080 // d-pad right
#define NXTCHUCK_CC_BTN_DU 0x0100 // d-pad up
#define NXTCHUCK_CC_BTN_DL 0x0200 // d-pad left
#define NXTCHUCK_CC_BTN_ZR 0x0400 // Right-Z button
#define NXTCHUCK_CC_BTN_X  0x0800 // x button
#define NXTCHUCK_CC_BTN_A  0x1000 // a button
#define NXTCHUCK_CC_BTN_Y  0x2000 // y button
#define NXTCHUCK_CC_BTN_B  0x4000 // b button
#define NXTCHUCK_CC_BTN_ZL 0x8000 // Left-Z button

tByteArray NXTCHUCK_I2CRequest;        /*!< Array to hold I2C command data */
tByteArray NXTCHUCK_I2CReply;          /*!< Array to hold I2C reply data */

bool NXTCHUCKinitialised[] = {false, false, false, false};  /*!< Has the NXTChuck been initialised yet? */

typedef struct
{
  ubyte ident;
  ubyte stickX;
  ubyte stickY;
  int accelX;
  int accelY;
  int accelZ;
  bool buttonC;
  bool buttonZ;
  ubyte buttons;
} tNunchuck;


typedef struct
{
  byte stickLX;
  byte stickLY;
  byte stickRX;
  byte stickRY;
  byte triggerR;
  byte triggerL;
  bool buttonTriggerR;
  bool buttonTriggerL;
  bool buttonDPadU;
  bool buttonDPadD;
  bool buttonDPadL;
  bool buttonDPadR;
  bool buttonX;
  bool buttonA;
  bool buttonY;
  bool buttonB;
  bool buttonHome;
  bool buttonSelect;
  bool buttonStart;
  bool buttonZL;
  bool buttonZR;
  unsigned int buttons;
} tClassicCtrl;


ubyte NXTChuckIdentLookup[][] = {
  {0x00,0x00,0xA4,0x20,0x00,0x00}, // Nunchuk
  {0x00,0x00,0xA4,0x20,0x01,0x01}, // Classic Controller
  {0x00,0x00,0xA4,0x20,0x01,0x03}, // GH3 or GHWT Guitar
  {0x01,0x00,0xA4,0x20,0x01,0x03}, // Guitar Hero World Tour Drums
  {0x03,0x00,0xA4,0x20,0x01,0x03}, // DJ Hero Turntable
  {0x00,0x00,0xA4,0x20,0x04,0x02}, // Wii Balance Board
  {0x00,0x00,0xA4,0x20,0x04,0x05}, // Activated Wii Motion Plus
  {0x00,0x00,0xA4,0x20,0x05,0x05}, // Activated Wii Motion Plus in Nunchuck passthrought mode
  {0x00,0x00,0xA4,0x20,0x07,0x05}, // Activated Wii Motion Plus in Classic Controller passthrought mode
  {0x00,0x00,0xA6,0x20,0x00,0x05}, // Inactive Wii Motion Plus
  {0x00,0x00,0xA6,0x20,0x04,0x05}, // No-longer active Wii Motion Plus
  {0x00,0x00,0xA6,0x20,0x05,0x05}, // No-longer nunchuk-passthrough Wii Motion Plus
  {0x00,0x00,0xA6,0x20,0x07,0x05}, // No-longer classic-passthrough Wii Motion Plus
  {0xFF,0x00,0xA4,0x20,0x00,0x00}  // Seems to the ID of a black nunchuck
};

ubyte _NXTChuckDataInit1[] = {3, 0xA4, 0xF0, 0x55}; /*!< First of two arrays written to the extension to initialize */
ubyte _NXTChuckDataInit2[] = {3, 0xA4, 0xFB, 0x00}; /*!< Second of two arrays written to the extension to initialize */

/**
 * It's a nunchuck, innit? Initialise the nunchuck sensor.
 *
 * Please note that this is an internal function and should not called directly.
 * @param link the NXTChuck port number
 * @return true if no error occured, false if it did
 */
bool _NXTChuckInit(tSensors link){
  writeDebugStreamLine("initialising");
    memcpy(NXTCHUCK_I2CRequest, _NXTChuckDataInit1, sizeof(_NXTChuckDataInit1));
    if (!writeI2C(link, NXTCHUCK_I2CRequest))
      return false;

    memcpy(NXTCHUCK_I2CRequest, _NXTChuckDataInit2, sizeof(_NXTChuckDataInit2));
    return writeI2C(link, NXTCHUCK_I2CRequest);
}

/**
 * Read 6 bytes from the nunchuck and return it.
 *
 * Please note this is an internal function and should not be used directly.
 * @param link the nunchuck port number
 * @param _reg the register to read
 * @param data the tByteArray to hold the returned data
 * @return true if no error occured, false if it did
 */
bool __NXTChuckReadRaw(tSensors link, ubyte _reg, tByteArray &data){
  if (!NXTCHUCKinitialised[link])
  {
    if (!_NXTChuckInit(link))
      return false;

    NXTCHUCKinitialised[link] = true;
  }

  NXTCHUCK_I2CRequest[0] = 2;
  NXTCHUCK_I2CRequest[1] = NXTCHUCK_I2C_ADDRESS;
  NXTCHUCK_I2CRequest[2] = _reg;

  if (!writeI2C(link, NXTCHUCK_I2CRequest))
    return false;

  NXTCHUCK_I2CRequest[0] = 1;
  NXTCHUCK_I2CRequest[1] = NXTCHUCK_I2C_ADDRESS;

  return writeI2C(link, NXTCHUCK_I2CRequest, data, 6);
}


/**
 * Identify the type of nunchuck sensor connected to the NXT.
 * @param link the nunchuck port number
 * @param nunchuck the tNunchuck that holds the nunchuck related data.
 * @return true if no error occured, false if it did
 */
bool NXTChuckreadIdent(tSensors link, tNunchuck &nunchuck){
  if(__NXTChuckReadRaw(link, 0xFA, NXTCHUCK_I2CReply)){

#ifdef __NUNHUCK__DEBUG__
    for (int i = 0; i < 6; i++)
    {
      writeDebugStream("0x%02X ", NXTCHUCK_I2CReply[i]);
    }

    writeDebugStreamLine(" ");
#endif // __NUNHUCK__DEBUG__

    for(byte i = 0; i < 14; i++){

#ifdef __NUNHUCK__DEBUG__
      writeDebugStream("Comparing: ");
      for (int j = 0; j < 6; j++)
      {
        writeDebugStream("0x%02X ", NXTCHUCK_I2CReply[j]);
      }
      writeDebugStream("   and   ");
      for (int j = 0; j < 6; j++)
      {
        writeDebugStream("0x%02X ", NXTChuckIdentLookup[i][j]);
      }
      writeDebugStreamLine(": %d", memcmp(&NXTChuckIdentLookup[i][0], &NXTCHUCK_I2CReply[0], 6));
#endif // __NUNHUCK__DEBUG__

      if (memcmp(&NXTChuckIdentLookup[i][0], &NXTCHUCK_I2CReply[0], 6) == 0)
      {
        nunchuck.ident = i + 2;
        return true;
      }
    }
    nunchuck.ident = NXTCHUCK_DEVICE_UNKNOWN;
    return true;
  }
  return false;                                                    // Communication error
}


/**
 * Read the data from the nunchuck.
 * @param link the nunchuck port number
 * @param nunchuck the tNunchuck that holds the nunchuck related data.
 * @return true if no error occured, false if it did
 */
bool NXTChuckreadSensor(tSensors link, tNunchuck &nunchuck){
  if(__NXTChuckReadRaw(link, 0x00, NXTCHUCK_I2CReply))
  {
    nunchuck.stickX = NXTCHUCK_I2CReply[0];
    nunchuck.stickY = NXTCHUCK_I2CReply[1];

    nunchuck.accelX = (NXTCHUCK_I2CReply[2] << 2) | ((NXTCHUCK_I2CReply[5] >> 2) & 0x03);
    nunchuck.accelY = (NXTCHUCK_I2CReply[3] << 2) | ((NXTCHUCK_I2CReply[5] >> 4) & 0x03);
    nunchuck.accelZ = (NXTCHUCK_I2CReply[4] << 2) | ((NXTCHUCK_I2CReply[5] >> 6) & 0x03);

    nunchuck.buttons = (~NXTCHUCK_I2CReply[5]) & 0x03;
    nunchuck.buttonC = (nunchuck.buttons & NXTCHUCK_N_BTN_C) ? true : false;
    nunchuck.buttonZ = (nunchuck.buttons & NXTCHUCK_N_BTN_Z) ? true : false;

    return true;                                            // Return success
  }
  return false;                                                  // Return error
}


/**
 * Read the data from the classic controller.
 * @param link the nunchuck port number
 * @param controller the tClassicCtrl that holds the Classic Controller related data.
 * @return true if no error occured, false if it did
 */
bool NXTChuckReadClassicController(tSensors link, tClassicCtrl &controller){
  if(__NXTChuckReadRaw(link, 0x00, NXTCHUCK_I2CReply))
  {
    controller.stickLX = NXTCHUCK_I2CReply[0] & 0x3F;                                        // Unpack the data into usable values
    controller.stickLY = NXTCHUCK_I2CReply[1] & 0x3F;
    controller.triggerL = ((NXTCHUCK_I2CReply[2] >> 2) & 0x18) | ((NXTCHUCK_I2CReply[3] >> 5) & 0x07);

    controller.stickRX = ((NXTCHUCK_I2CReply[0] >> 3) & 0x18) | ((NXTCHUCK_I2CReply[1] >> 5) & 0x06) | ((NXTCHUCK_I2CReply[2] >> 7) & 0x01);
    controller.stickRY = NXTCHUCK_I2CReply[2] & 0x1F;
    controller.triggerR = NXTCHUCK_I2CReply[3] & 0x1F;

    controller.buttons = ~(NXTCHUCK_I2CReply[4] | (NXTCHUCK_I2CReply[5] << 8)) & 0xFFFE;

    controller.buttonTriggerR = (controller.buttons & NXTCHUCK_CC_BTN_RT) ? true : false;
    controller.buttonTriggerL = (controller.buttons & NXTCHUCK_CC_BTN_LT) ? true : false;

    controller.buttonDPadU =    (controller.buttons & NXTCHUCK_CC_BTN_DU) ? true : false;
    controller.buttonDPadD =    (controller.buttons & NXTCHUCK_CC_BTN_DD) ? true : false;
    controller.buttonDPadL =    (controller.buttons & NXTCHUCK_CC_BTN_DL) ? true : false;
    controller.buttonDPadR =    (controller.buttons & NXTCHUCK_CC_BTN_DR) ? true : false;

    controller.buttonX =        (controller.buttons & NXTCHUCK_CC_BTN_X)  ? true : false;
    controller.buttonA =        (controller.buttons & NXTCHUCK_CC_BTN_A)  ? true : false;
    controller.buttonY =        (controller.buttons & NXTCHUCK_CC_BTN_Y)  ? true : false;
    controller.buttonB =        (controller.buttons & NXTCHUCK_CC_BTN_B)  ? true : false;

    controller.buttonHome =     (controller.buttons & NXTCHUCK_CC_BTN_H)  ? true : false;
    controller.buttonSelect =   (controller.buttons & NXTCHUCK_CC_BTN_RT) ? true : false;
    controller.buttonStart =    (controller.buttons & NXTCHUCK_CC_BTN_P)  ? true : false;

    controller.buttonZL =       (controller.buttons & NXTCHUCK_CC_BTN_RT) ? true : false;
    controller.buttonZR =       (controller.buttons & NXTCHUCK_CC_BTN_RT) ? true : false;

    return true;
  }
  return false;
}


#endif // __NXTCHUCK_H__

/*
 * $Id: dexterind-nxtchuck.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
