/*!@addtogroup mindsensors
 * @{
 * @defgroup PSP4 PSP-Nx V4
 * Playstation 2 Controller Sensor
 * @{
 */

/*
 * $Id: mindsensors-ps2ctrl-v4.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __PSPV4_H__
#define __PSPV4_H__
/** \file mindsensors-ps2ctrl-v4.h
 * \brief Playstation 2 Controller Sensor
 *
 * mindsensors-ps2ctrl-v4.h provides an API for the Mindsensors PS2 controller sensor
 * with referee signal receiver.
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
 * \date 30 July 2012
 * \version 0.1
 * \example mindsensors-ps2ctrl-v4-test1.c
 * \example mindsensors-ps2ctrl-v4-test2.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define PSPV4_I2C_ADDR              0x02    /*!< PSP-Nx V4 I2C device address */

#define PSPV4_REG_CMD               0x41    /*!< Command register */

#define PSPV4_CMD_INITIALISE        0x49    /*!< Initialisation command */

#define PSPV4_REG_BTN_SET_1         0x42    /*!< First button set register */
#define PSPV4_REG_BTN_SET_2         0x43    /*!< Second button set register */
#define PSPV4_REG_JLEFT             0x44    /*!< Left joystick register */
#define PSPV4_REG_JRIGHT            0x46    /*!< Right joystick register */

#define PSPV4_REG_PRES_JPAD_UP      0x4A    /*!< Joypad up button register */
#define PSPV4_REG_PRES_JPAD_RIGHT   0x4B    /*!< Joypad right button register */
#define PSPV4_REG_PRES_JPAD_DOWN    0x4C    /*!< Joypad down button register */
#define PSPV4_REG_PRES_JPAD_LEFT    0x4D    /*!< Joypad left button register */
#define PSPV4_REG_PRES_BTN_L2       0x4E    /*!< L2 button register */
#define PSPV4_REG_PRES_BTN_R2       0x4F    /*!< R2 button register */
#define PSPV4_REG_PRES_BTN_L1       0x50    /*!< L1 button register */
#define PSPV4_REG_PRES_BTN_R1       0x51    /*!< R1 button register */
#define PSPV4_REG_PRES_BTN_TRIANGLE 0x52    /*!< Triangle button button register */
#define PSPV4_REG_PRES_BTN_SQUARE   0x53    /*!< Square button register */
#define PSPV4_REG_PRES_BTN_CIRCLE   0x54    /*!< Circle button register */
#define PSPV4_REG_PRES_BTN_CROSS    0x55    /*!< Cross button register */

#define PSPV4_REG_REF_SIGNAL_CODE   0x56    /*!< Referee Signal Code register */
#define PSPV4_REG_REF_TX_TYPE       0x57    /*!< Referee Transmitter Type register */
#define PSPV4_REG_REF_SIGNAL_RAW    0x58    /*!< Referee Signal Raw Value register */
#define PSPV4_REG_REF_SIGNAL_COUNT  0x5C    /*!< Referee Signal Count register */

#define PSPV4_SIGNAL_FAST_REWIND    0x32    /*!< Referee Signal 'fast rewind' */
#define PSPV4_SIGNAL_FAST_FORWARD   0x34    /*!< Referee Signal 'fast forward' */
#define PSPV4_SIGNAL_PLAY           0x35    /*!< Referee Signal 'play' */
#define PSPV4_SIGNAL_STOP           0x36    /*!< Referee Signal 'stop' */

tByteArray PSPV4_I2CRequest;     /*!< Array to hold I2C command data */
tByteArray PSPV4_I2CReply;       /*!< Array to hold I2C reply data */

bool PSPV4initalised[4] = {false, false, false, false};

typedef struct
{
  // computed button states
  ubyte   left1Btn;           /*!< L1 button value */
  ubyte   left2Btn;           /*!< L2 button value */
  ubyte   right1Btn;          /*!< R1 button value */
  ubyte   rigth2Btn;          /*!< R2 button value */
  ubyte   joypadLeft;         /*!< Joypad left button value */
  ubyte   joypadDown;         /*!< Joypad down button value */
  ubyte   joypadRight;        /*!< Joypad right button value */
  ubyte   joypadUp;           /*!< Joypad up button value */
  ubyte   triangleBtn;        /*!< Triangle button button value */
  ubyte   squareBtn;          /*!< Square button button value */
  ubyte   circleBtn;          /*!< Circle button button value */
  ubyte   crossBtn;           /*!< Cross button button value */
  ubyte   joystickLeftBtn;    /*!< Left joystick button value */
  ubyte   joystickRightBtn;   /*!< Right joystick button value */
  ubyte   selectBtn;          /*!< Select button value */
  ubyte   startBtn;           /*!< Start button value */
  int   joystickLeft_x;       /*!< Left joystick X value, scaled from 0 to 100 */
  int   joystickLeft_y;       /*!< Left joystick X value, scaled from 0 to 100 */
  int   joystickRight_x;      /*!< Left joystick X value, scaled from 0 to 100 */
  int   joystickRight_y;      /*!< Left joystick X value, scaled from 0 to 100 */
} tPSP;

tPSP pspController;


/**
 * Initialise the sensor.
 *
 * Note: This is an internal function and should not be used directly.
 * @param link the PSP sensor port
 * @param address I2C address, optional, defaults to PSPV4_I2C_ADDR
 * @return true if no error occured, false if it did
 */
bool _PSPV4init(tSensors link, ubyte address = PSPV4_I2C_ADDR)
{
  PSPV4_I2CRequest[0] = 3;                   // Message size
  PSPV4_I2CRequest[1] = address ;            // I2C Address
  PSPV4_I2CRequest[2] = 0x41;                // Register address
  PSPV4_I2CRequest[3] = PSPV4_CMD_INITIALISE; // Register address

  if (!writeI2C(link, PSPV4_I2CRequest))
  {
    return false;
  }

	wait1Msec(100);                         // wait for the dongle to initialize
	PSPV4initalised[link] = true;

	return true;
}


/**
 * Read a byte from the sensor
 *
 * Note: This is an internal function and should not be used directly.
 * @param link the PSP sensor port
 * @param reg the register to read
 * @param address I2C address, optional, defaults to PSPV4_I2C_ADDR
 * @return the ubyte value or 0 if an error occured
 */
ubyte _PSPV4readByte (tSensors link, ubyte reg, ubyte address = PSPV4_I2C_ADDR)
{
  // Check if the controller has been initialised
  if (!PSPV4initalised[link])
  {
    _PSPV4init(link, address);
  }

  memset(PSPV4_I2CRequest, 0, sizeof(PSPV4_I2CRequest));

  PSPV4_I2CRequest[0] = 2;                   // Message size
  PSPV4_I2CRequest[1] = address;  // I2C Address
  PSPV4_I2CRequest[2] = reg;            // Register address

  if (!writeI2C(link, PSPV4_I2CRequest, PSPV4_I2CReply, 1))
    return 0;

  return PSPV4_I2CReply[0];
}


/**
 * Read the button states.  Use PSPV4readButtonPressure() if you want
 * additional pressure values as well.
 * @param link the PSP sensor port
 * @param controllerState struct to hold all the button and joystick states and values
 * @param address I2C address, optional, defaults to PSPV4_I2C_ADDR
 * @return true if no error occured, false if it did
 */
bool PSPV4readButtons(tSensors link, tPSP &controllerState, ubyte address = PSPV4_I2C_ADDR)
{
  ubyte b0;
  ubyte b1;

  // Check if the controller has been initialised
  if (!PSPV4initalised[link])
  {
    _PSPV4init(link, address);
  }

  memset(PSPV4_I2CRequest, 0, sizeof(PSPV4_I2CRequest));
  memset(controllerState, 0, sizeof(controllerState));

  PSPV4_I2CRequest[0] = 2;                   // Message size
  PSPV4_I2CRequest[1] = address;             // I2C Address
  PSPV4_I2CRequest[2] = PSPV4_REG_BTN_SET_1;      // Register address

  if (!writeI2C(link, PSPV4_I2CRequest, PSPV4_I2CReply, 6))
    return false;

	b0 = ~PSPV4_I2CReply[0];
	b1 = ~PSPV4_I2CReply[1];

	controllerState.selectBtn         = (b0 >> 0) & 0x01;
	controllerState.joystickLeftBtn   = (b0 >> 1) & 0x01;
	controllerState.joystickRightBtn  = (b0 >> 2) & 0x01;
	controllerState.startBtn          = (b0 >> 3) & 0x01;
	controllerState.joypadUp          = (b0 >> 4) & 0x01;
	controllerState.joypadRight       = (b0 >> 5) & 0x01;
	controllerState.joypadDown        = (b0 >> 6) & 0x01;
	controllerState.joypadLeft        = (b0 >> 7) & 0x01;

	controllerState.left2Btn          = (b1 >> 0) & 0x01;
	controllerState.rigth2Btn         = (b1 >> 1) & 0x01;
	controllerState.left1Btn          = (b1 >> 2) & 0x01;
	controllerState.right1Btn         = (b1 >> 3) & 0x01;
	controllerState.triangleBtn       = (b1 >> 4) & 0x01;
	controllerState.circleBtn         = (b1 >> 5) & 0x01;
	controllerState.crossBtn          = (b1 >> 6) & 0x01;
	controllerState.squareBtn         = (b1 >> 7) & 0x01;

	controllerState.joystickLeft_x    = (((long)PSPV4_I2CReply[2] - 128) * 100)/128;
	controllerState.joystickLeft_y    = (((long)PSPV4_I2CReply[3] - 128) * 100)/128;
	controllerState.joystickRight_x   = (((long)PSPV4_I2CReply[4] - 128) * 100)/128;
	controllerState.joystickRight_y   = (((long)PSPV4_I2CReply[5] - 128) * 100)/128;

	return true;
}


/**
 * Read the button states with additional pressure values.  Use PSPV4readButtons() if you don't
 * care about how hard someone is pressing a button.
 * @param link the PSP sensor port
 * @param controllerState struct to hold all the button and joystick states and values
 * @param address I2C address, optional, defaults to PSPV4_I2C_ADDR
 * @return true if no error occured, false if it did
 */
bool PSPV4readButtonPressure(tSensors link, tPSP &controllerState,  ubyte address = PSPV4_I2C_ADDR)
{
  // Check if the controller has been initialised
  if (!PSPV4initalised[link])
  {
    _PSPV4init(link, address);
  }

  if (!PSPV4readButtons(link, controllerState, address))
  {
    return false;
  }

  memset(PSPV4_I2CRequest, 0, sizeof(PSPV4_I2CRequest));
  PSPV4_I2CRequest[0] = 2;                   // Message size
  PSPV4_I2CRequest[1] = address;  // I2C Address
  PSPV4_I2CRequest[2] = PSPV4_REG_PRES_JPAD_UP;            // Register address

  if (!writeI2C(link, PSPV4_I2CRequest, PSPV4_I2CReply, 12))
    return false;

	controllerState.joypadUp          = (PSPV4_I2CReply[0]  * 100) / 255;
  controllerState.joypadRight       = (PSPV4_I2CReply[1]  * 100) / 255;
  controllerState.joypadDown        = (PSPV4_I2CReply[2]  * 100) / 255;
  controllerState.joypadLeft        = (PSPV4_I2CReply[3]  * 100) / 255;

  controllerState.left2Btn          = (PSPV4_I2CReply[4]  * 100) / 255;
  controllerState.rigth2Btn         = (PSPV4_I2CReply[5]  * 100) / 255;
  controllerState.left1Btn          = (PSPV4_I2CReply[6]  * 100) / 255;
  controllerState.right1Btn         = (PSPV4_I2CReply[7]  * 100) / 255;
  controllerState.triangleBtn       = (PSPV4_I2CReply[8]  * 100) / 255;
  controllerState.circleBtn         = (PSPV4_I2CReply[9]  * 100) / 255;
  controllerState.crossBtn          = (PSPV4_I2CReply[10] * 100) / 255;
  controllerState.squareBtn         = (PSPV4_I2CReply[11] * 100) / 255;

  return true;
}

/**
 * Read the last command sent by the referee.\n
 * Values can be:
 * - PSPV4_SIGNAL_FAST_REWIND
 * - PSPV4_SIGNAL_FAST_FORWARD
 * - PSPV4_SIGNAL_PLAY
 * - PSPV4_SIGNAL_STOP
 * @param link the PSP sensor port
 * @param address I2C address, optional, defaults to PSPV4_I2C_ADDR
 * @return true if no error occured, false if it did
 */
byte PSPV4readRefSignal(tSensors link, ubyte address = PSPV4_I2C_ADDR)
{
  return _PSPV4readByte(link, PSPV4_REG_REF_SIGNAL_CODE, address);
}


/**
 * Signal Count - increments each time the sensor receives a signal from
 * the Referee (rolls over to 0 after 255)
 * @param link the PSP sensor port
 * @param address I2C address, optional, defaults to PSPV4_I2C_ADDR
 * @return true if no error occured, false if it did
 */
byte PSPV4readRefSignalCount(tSensors link, ubyte address = PSPV4_I2C_ADDR)
{
  return _PSPV4readByte(link, PSPV4_REG_REF_SIGNAL_COUNT, address);
}


/**
 * Fetch the transmitter type.  Can be used to differentiate between
 * various transmitter type (TV remote vs Referee Transmitter)
 * @param link the PSP sensor port
 * @param address I2C address, optional, defaults to PSPV4_I2C_ADDR
 * @return true if no error occured, false if it did
 */
byte PSPV4readRefTXType(tSensors link, ubyte address = PSPV4_I2C_ADDR)
{
  return _PSPV4readByte(link, PSPV4_REG_REF_TX_TYPE, address);
}


/**
 * Get the raw data from the receiver.  This is useful if custom commands
 * are to be implemented.  Simply map the returned value to a new command.
 * @param link the PSP sensor port
 * @param address I2C address, optional, defaults to PSPV4_I2C_ADDR
 * @return the raw value from the sensor
 */
long PSPV4readRawRefTXValue(tSensors link, ubyte address = PSPV4_I2C_ADDR)
{
  // Check if the controller has been initialised
  if (!PSPV4initalised[link])
  {
    _PSPV4init(link, address);
  }

  memset(PSPV4_I2CRequest, 0, sizeof(PSPV4_I2CRequest));

  PSPV4_I2CRequest[0] = 2;                        // Message size
  PSPV4_I2CRequest[1] = address;                  // I2C Address
  PSPV4_I2CRequest[2] = PSPV4_REG_REF_SIGNAL_RAW; // Register address

  if (!writeI2C(link, PSPV4_I2CRequest, PSPV4_I2CReply, 4))
    return 0;

  return PSPV4_I2CReply[0] + (PSPV4_I2CReply[1]<<8) + (PSPV4_I2CReply[2]<<16) + (PSPV4_I2CReply[3]<<24);
}



#endif // __PSPV4_H__

/*
 * $Id: mindsensors-ps2ctrl-v4.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
