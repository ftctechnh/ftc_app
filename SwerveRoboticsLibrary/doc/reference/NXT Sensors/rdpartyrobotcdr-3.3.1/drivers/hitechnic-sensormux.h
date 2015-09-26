/*!@addtogroup HiTechnic
 * @{
 * @defgroup htsmux Sensor MUX
 * HiTechnic Sensor MUX Sensor
 * @{
 */

/*
 * $Id: hitechnic-sensormux.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTSMUX_H__
#define __HTSMUX_H__
/** \file hitechnic-sensormux.h
 * \brief Commonly used functions used by drivers.
 *
 * common.h provides a number of frequently used functions that are useful for writing
 * drivers.
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 *
 * Changelog:
 * - 0.1: Initial release, split off from common.h
 *
 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 18 January 2011
 * \version 0.1
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define __HTSMUX_SUPPORT__

#ifndef SPORT
#define SPORT(X)  (X / 4)         /*!< Convert tMUXSensor to sensor port number */
#endif

#ifndef MPORT
#define MPORT(X)  (X % 4)         /*!< Convert tMUXSensor to MUX port number */
#endif

#ifndef MAX_ARR_SIZE
/**
 * Maximum buffer size for byte_array, can be overridden in your own program.
 * It's 17 bytes big because the max I2C buffer size is 16, plus 1 byte to denote
 * packet length.
 */
#define MAX_ARR_SIZE 17
#endif

#define HTSMUX_I2C_ADDR         0x10  /*!< HTSMUX I2C device address */
#define HTSMUX_COMMAND          0x20  /*!< Command register */
#define HTSMUX_STATUS           0x21  /*!< Status register */

// Registers
#define HTSMUX_MODE             0x00  /*!< Sensor mode register */
#define HTSMUX_TYPE             0x01  /*!< Sensor type register */
#define HTSMUX_I2C_COUNT        0x02  /*!< I2C byte count register */
#define HTSMUX_I2C_DADDR        0x03  /*!< I2C device address register */
#define HTSMUX_I2C_MADDR        0x04  /*!< I2C memory address register */
#define HTSMUX_CH_OFFSET        0x22  /*!< Channel register offset */
#define HTSMUX_CH_ENTRY_SIZE    0x05  /*!< Number of registers per sensor channel */

#define HTSMUX_ANALOG           0x36  /*!< Analogue upper 8 bits register */
#define HTSMUX_AN_ENTRY_SIZE    0x02  /*!< Number of registers per analogue channel */

#define HTSMUX_I2C_BUF          0x40  /*!< I2C buffer register offset */
#define HTSMUX_BF_ENTRY_SIZE    0x10  /*!< Number of registers per buffer */


// Command fields
#define HTSMUX_CMD_HALT         0x00  /*!< Halt multiplexer command */
#define HTSMUX_CMD_AUTODETECT   0x01  /*!< Start auto-detect function command */
#define HTSMUX_CMD_RUN          0x02  /*!< Start normal multiplexer operation command */

// Status
#define HTSMUX_STAT_NORMAL      0x00  /*!< Nothing going on, everything's fine */
#define HTSMUX_STAT_BATT        0x01  /*!< No battery voltage detected status */
#define HTSMUX_STAT_BUSY        0x02  /*!< Auto-dected in progress status */
#define HTSMUX_STAT_HALT        0x04  /*!< Multiplexer is halted status */
#define HTSMUX_STAT_ERROR       0x08  /*!< Command error detected status */
#define HTSMUX_STAT_NOTHING     0xFF  /*!< Status hasn't really been set yet */

// Channel modes
#define HTSMUX_CHAN_NONE        0x00  /*!< Nothing configured - analogue sensor */
#define HTSMUX_CHAN_I2C         0x01  /*!< I2C channel present channel mode */
#define HTSMUX_CHAN_9V          0x02  /*!< Enable 9v supply on analogue pin channel mode */
#define HTSMUX_CHAN_DIG0_HIGH   0x04  /*!< Drive pin 0 high channel mode */
#define HTSMUX_CHAN_DIG1_HIGH   0x08  /*!< Drive pin 1 high channel mode */
#define HTSMUX_CHAN_I2C_SLOW    0x10  /*!< Set slow I2C rate channel mode */


/*!< Sensor types as detected by SMUX */
typedef enum {
  HTSMUXAnalogue = 0x00,
  HTSMUXLegoUS = 0x01,
  HTSMUXCompass = 0x02,
  HTSMUXColor = 0x03,
  HTSMUXAccel = 0x04,
  HTSMUXIRSeeker = 0x05,
  HTSMUXProto = 0x06,
  HTSMUXColorNew = 0x07,
  HTSMUXAngle = 0x08,
  HTSMUXIRSeekerNew = 0x09,
  HTSMUXSensorCustom = 0x0E,
  HTSMUXSensorNone = 0x0F
} HTSMUXSensorType;

/*!< Sensor and SMUX port combinations */
typedef enum {
  msensor_S1_1 = 0,
  msensor_S1_2 = 1,
  msensor_S1_3 = 2,
  msensor_S1_4 = 3,
  msensor_S2_1 = 4,
  msensor_S2_2 = 5,
  msensor_S2_3 = 6,
  msensor_S2_4 = 7,
  msensor_S3_1 = 8,
  msensor_S3_2 = 9,
  msensor_S3_3 = 10,
  msensor_S3_4 = 11,
  msensor_S4_1 = 12,
  msensor_S4_2 = 13,
  msensor_S4_3 = 14,
  msensor_S4_4 = 15
} tMUXSensor;


/*!< array to hold SMUX status info */
ubyte HTSMUXstatus[4] = {HTSMUX_STAT_NOTHING, HTSMUX_STAT_NOTHING, HTSMUX_STAT_NOTHING, HTSMUX_STAT_NOTHING};

/*!< Array for holding sensor types */
HTSMUXSensorType HTSMUXSensorTypes[16] = {HTSMUXSensorNone, HTSMUXSensorNone, HTSMUXSensorNone, HTSMUXSensorNone,
                                          HTSMUXSensorNone, HTSMUXSensorNone, HTSMUXSensorNone, HTSMUXSensorNone,
                                          HTSMUXSensorNone, HTSMUXSensorNone, HTSMUXSensorNone, HTSMUXSensorNone,
                                          HTSMUXSensorNone, HTSMUXSensorNone, HTSMUXSensorNone, HTSMUXSensorNone};

tByteArray HTSMUX_I2CRequest;     /*!< Array to hold I2C command data */
tByteArray HTSMUX_I2CReply;       /*!< Array to hold I2C reply data */

typedef ubyte tConfigParams[4];   /*!< Array to hold SMUX channel info */

tConfigParams Analogue_config = {HTSMUX_CHAN_NONE, 0, 0, 0}; /*!< Array to hold SMUX config data for sensor */

byte HTSMUXreadStatus(tSensors link);
HTSMUXSensorType HTSMUXreadSensorType(tMUXSensor muxsensor);
bool HTSMUXsendCommand(tSensors link, byte command);
bool HTSMUXreadPort(tMUXSensor muxsensor, tByteArray &result, int numbytes, int offset = 0);
bool HTSMUXsetMode(tMUXSensor muxsensor, byte mode);
bool HTSMUXsetAnalogueActive(tMUXSensor muxsensor);
bool HTSMUXsetAnalogueInactive(tMUXSensor muxsensor);
int HTSMUXreadAnalogue(tMUXSensor muxsensor);
bool HTSMUXreadPowerStatus(tSensors link);
bool HTSMUXconfigChannel(tMUXSensor muxsensor, tConfigParams &configparams);


/**
 * Read the status of the SMUX
 *
 * The status byte is made up of the following bits:\n
 *
 * | D7 | D6 | D4 | D3 | D2 | D1 | D1 |\n
 * - D1 - HTSMUX_STAT_BATT: No battery voltage detected
 * - D2 - HTSMUX_STAT_BUSY: Auto-dected in progress status
 * - D3 - HTSMUX_STAT_HALT: Multiplexer is halted
 * - D4 - HTSMUX_STAT_ERROR: Command error detected
 * @param link the SMUX port number
 * @return the status byte
 */
byte HTSMUXreadStatus(tSensors link) {
  memset(HTSMUX_I2CRequest, 0, sizeof(tByteArray));

  HTSMUX_I2CRequest[0] = 2;               // Message size
  HTSMUX_I2CRequest[1] = HTSMUX_I2C_ADDR; // I2C Address
  HTSMUX_I2CRequest[2] = HTSMUX_STATUS;

  if (!writeI2C(link, HTSMUX_I2CRequest, HTSMUX_I2CReply, 1))
    return -1;

  return HTSMUX_I2CReply[0];
}


/**
 * Get the sensor type attached to specified SMUX port
 * @param muxsensor the SMUX sensor port number
 * @return the status byte
 */
HTSMUXSensorType HTSMUXreadSensorType(tMUXSensor muxsensor) {
  return HTSMUXSensorTypes[muxsensor];
}


/**
 * Set the mode of a SMUX channel.
 *
 * Mode can be one or more of the following:
 * - HTSMUX_CHAN_I2C
 * - HTSMUX_CHAN_9V
 * - HTSMUX_CHAN_DIG0_HIGH
 * - HTSMUX_CHAN_DIG1_HIGH
 * - HTSMUX_CHAN_I2C_SLOW
 * @param muxsensor the SMUX sensor port number
 * @param mode the mode to set the channel to
 * @return true if no error occured, false if it did
 */
bool HTSMUXsetMode(tMUXSensor muxsensor, byte mode) {
  tSensors link = (tSensors)SPORT(muxsensor);
  byte channel = MPORT(muxsensor);

  // If we're in the middle of a scan, abort this call
  if (HTSMUXstatus[link] == HTSMUX_STAT_BUSY) {
    return false;
  } else if (HTSMUXstatus[link] != HTSMUX_STAT_HALT) {
	  // Always make sure the SMUX is in the halted state
	  if (!HTSMUXsendCommand(link, HTSMUX_CMD_HALT))
	    return false;
	  wait1Msec(50);
	}

  memset(HTSMUX_I2CRequest, 0, sizeof(tByteArray));

  HTSMUX_I2CRequest[0] = 3;               // Message size
  HTSMUX_I2CRequest[1] = HTSMUX_I2C_ADDR; // I2C Address
  HTSMUX_I2CRequest[2] = HTSMUX_CH_OFFSET + HTSMUX_MODE + (HTSMUX_CH_ENTRY_SIZE * channel);
  HTSMUX_I2CRequest[3] = mode;

  return writeI2C(link, HTSMUX_I2CRequest);
}


/**
 * Set the mode of an analogue channel to Active (turn the light on)
 * @param muxsensor the SMUX sensor port number
 * @return true if no error occured, false if it did
 */
bool HTSMUXsetAnalogueActive(tMUXSensor muxsensor) {
  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, Analogue_config);

  if (!HTSMUXsetMode(muxsensor, HTSMUX_CHAN_DIG0_HIGH))
    return false;

  return HTSMUXsendCommand((tSensors)SPORT(muxsensor), HTSMUX_CMD_RUN);
}


/**
 * Set the mode of an analogue channel to Inactive (turn the light off)
 * @param muxsensor the SMUX sensor port number
 * @return true if no error occured, false if it did
 */
bool HTSMUXsetAnalogueInactive(tMUXSensor muxsensor) {
  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, Analogue_config);

  if (!HTSMUXsetMode(muxsensor, 0))
    return false;

  return HTSMUXsendCommand((tSensors)SPORT(muxsensor), HTSMUX_CMD_RUN);
}


/**
 * Send a command to the SMUX.
 *
 * command can be one of the following:
 * - HTSMUX_CMD_HALT
 * - HTSMUX_CMD_AUTODETECT
 * - HTSMUX_CMD_RUN
 *
 * in progress.
 * @param link the SMUX port number
 * @param command the command to be sent to the SMUX
 * @return true if no error occured, false if it did
 */
bool HTSMUXsendCommand(tSensors link, byte command) {
  memset(HTSMUX_I2CRequest, 0, sizeof(tByteArray));

  HTSMUX_I2CRequest[0] = 3;               // Message size
  HTSMUX_I2CRequest[1] = HTSMUX_I2C_ADDR; // I2C Address
  HTSMUX_I2CRequest[2] = HTSMUX_COMMAND;
  HTSMUX_I2CRequest[3] = command;

  switch(command) {
    case HTSMUX_CMD_HALT:
        HTSMUXstatus[link] = HTSMUX_STAT_HALT;
        break;
    case HTSMUX_CMD_AUTODETECT:
        HTSMUXstatus[link] = HTSMUX_STAT_BUSY;
        break;
    case HTSMUX_CMD_RUN:
        HTSMUXstatus[link] = HTSMUX_STAT_NORMAL;
        break;
  }

  return writeI2C(link, HTSMUX_I2CRequest);
}


/**
 * Read the value returned by the sensor attached the SMUX. This function
 * is for I2C sensors.
 * @param muxsensor the SMUX sensor port number
 * @param result array to hold values returned from SMUX
 * @param numbytes the size of the I2C reply
 * @param offset the offset used to start reading from
 * @return true if no error occured, false if it did
 */
bool HTSMUXreadPort(tMUXSensor muxsensor, tByteArray &result, int numbytes, int offset) {
  tSensors link = (tSensors)SPORT(muxsensor);
  byte channel = MPORT(muxsensor);

  memset(HTSMUX_I2CRequest, 0, sizeof(tByteArray));
  if (HTSMUXstatus[link] != HTSMUX_STAT_NORMAL)
    HTSMUXsendCommand(link, HTSMUX_CMD_RUN);

  HTSMUX_I2CRequest[0] = 2;                 // Message size
  HTSMUX_I2CRequest[1] = HTSMUX_I2C_ADDR;   // I2C Address
  HTSMUX_I2CRequest[2] = HTSMUX_I2C_BUF + (HTSMUX_BF_ENTRY_SIZE * channel) + offset;

  if (!writeI2C(link, HTSMUX_I2CRequest, HTSMUX_I2CReply, numbytes))
    return false;

  memcpy(result, HTSMUX_I2CReply, sizeof(tByteArray));

  return true;
}


/**
 * Read the value returned by the sensor attached the SMUX. This function
 * is for analogue sensors.
 * @param muxsensor the SMUX sensor port number
 * @return the value of the sensor or -1 if an error occurred.
 */
int HTSMUXreadAnalogue(tMUXSensor muxsensor) {
  tSensors link = (tSensors)SPORT(muxsensor);
  byte channel = MPORT(muxsensor);

  memset(HTSMUX_I2CRequest, 0, sizeof(tByteArray));
  if (HTSMUXstatus[link] != HTSMUX_STAT_NORMAL)
    HTSMUXsendCommand(link, HTSMUX_CMD_RUN);

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, Analogue_config);

  HTSMUX_I2CRequest[0] = 2;               // Message size
  HTSMUX_I2CRequest[1] = HTSMUX_I2C_ADDR;   // I2C Address
  HTSMUX_I2CRequest[2] = HTSMUX_ANALOG + (HTSMUX_AN_ENTRY_SIZE * channel);

  if (!writeI2C(link, HTSMUX_I2CRequest, HTSMUX_I2CReply, 2))
    return -1;

  return ((int)HTSMUX_I2CReply[0] * 4) + HTSMUX_I2CReply[1];
}


/**
 * Return a string for the sensor type.
 *
 * @param muxsensor the SMUX sensor port number
 * @param sensorName the string to hold the name of the sensor.
 */
void HTSMUXsensorTypeToString(HTSMUXSensorType muxsensor, string &sensorName) {
  switch(muxsensor) {
    case HTSMUXAnalogue:    sensorName = "Analogue";      break;
    case HTSMUXLegoUS:      sensorName = "Ultra Sonic";   break;
    case HTSMUXCompass:     sensorName = "Compass";       break;
    case HTSMUXColor:       sensorName = "Colour";        break;
    case HTSMUXColorNew:    sensorName = "Colour New";    break;
    case HTSMUXAccel:       sensorName = "Accel";         break;
    case HTSMUXIRSeeker:    sensorName = "IR Seeker";     break;
    case HTSMUXProto:       sensorName = "Proto Board";   break;
    case HTSMUXIRSeekerNew: sensorName = "IR Seeker V2";  break;
    case HTSMUXSensorNone : sensorName = "No sensor";     break;
  }
}


/**
 * Check if the battery is low
 *
 * @param link the SMUX port number
 * @return true if there is a power source problem
 */
bool HTSMUXreadPowerStatus(tSensors link) {
  if ((HTSMUXreadStatus(link) & HTSMUX_STAT_BATT) == HTSMUX_STAT_BATT)
    return true;
  else
    return false;
}


/**
 * Configure the SMUX for a specific sensor.\n
 * The parameters are as follows:
 * - Channel mode
 * - Number of bytes to request from attached sensor
 * - I2C address
 * - I2C register to request
 *
 * @param muxsensor the SMUX sensor port number
 * @param configparams parameters for the channel's configuration
 * @return true if there is a power source problem
 */
bool HTSMUXconfigChannel(tMUXSensor muxsensor, tConfigParams &configparams) {
  memset(HTSMUX_I2CRequest, 0, sizeof(tByteArray));

  // Always make sure the SMUX is in the halted state
  if (!HTSMUXsendCommand((tSensors)SPORT(muxsensor), HTSMUX_CMD_HALT))
    return false;
	wait1Msec(50);

  HTSMUX_I2CRequest[0] = 7;               // Message size
  HTSMUX_I2CRequest[1] = HTSMUX_I2C_ADDR; // I2C Address
  HTSMUX_I2CRequest[2] = HTSMUX_CH_OFFSET + (HTSMUX_CH_ENTRY_SIZE * MPORT(muxsensor));
  HTSMUX_I2CRequest[3] = configparams[0];
  HTSMUX_I2CRequest[4] = 0x00;
  HTSMUX_I2CRequest[5] = configparams[1];
  HTSMUX_I2CRequest[6] = configparams[2];
  HTSMUX_I2CRequest[7] = configparams[3];

  if (!writeI2C((tSensors)SPORT(muxsensor), HTSMUX_I2CRequest))
    return false;

  HTSMUXSensorTypes[muxsensor] = HTSMUXSensorCustom;
  return HTSMUXsendCommand((tSensors)SPORT(muxsensor), HTSMUX_CMD_RUN);
}


#endif // __HTSMUX_H__
/*
 * $Id: hitechnic-sensormux.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
