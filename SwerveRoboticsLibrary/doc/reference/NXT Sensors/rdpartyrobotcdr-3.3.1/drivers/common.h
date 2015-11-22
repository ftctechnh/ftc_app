/*!@addtogroup common_includes
 * @{
 * @defgroup common Main common include
 * Commonly used functions used by drivers
 * @{
 */

/*
 * $Id: common.h 134 2013-03-12 18:15:17Z xander $
 */

/** \file common.h
 * \brief Commonly used functions used by drivers.
 *
 * common.h provides a number of frequently used functions that are useful for writing
 * drivers.
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER.

 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Added version check to issue error when compiling with RobotC < 1.46
 * - 0.2: Added __COMMON_H_DEBUG__ to enable/disable sounds when an I2C error occurs
 * - 0.2: Removed bool waitForI2CBus(tSensors link, bool silent)
 * - 0.3: clearI2CError() added to make writeI2C more robust, I2C bus errors are now handled
 *        better.
 * - 0.4: Added HiTechnic SMUX functions
 * - 0.5: Added clip function (Tom Roach)
 * - 0.6: clearI2CBus is now conditionally compiled into the FW.  Only RobotC < 1.57 needs it.
 * - 0.7: ubyteToInt(byte byteVal) modified, works better with 1.57+
 * - 0.8: ubyte used for arrays for firmware version 770 and higher<br>
 *        added support for new colour sensor<br>
 *        added better handling for when sensor is not configured properly
 * - 0.9: added bool HTSMUXsetMode(tSensors link, byte channel, byte mode) prototype<br>
 *        added int HTSMUXreadAnalogue(tMUXSensor muxsensor)<br>
 *        added HTSMUXSensorType HTSMUXreadSensorType(tMUXSensor muxsensor)<br>
 *        added bool HTSMUXreadPort(tMUXSensor muxsensor, tByteArray &result, int numbytes, int offset)<br>
 *        added bool HTSMUXreadPort(tMUXSensor muxsensor, tByteArray &result, int numbytes)<br>
 *        added bool HTSMUXsetMode(tMUXSensor muxsensor, byte mode)<br>
 *        added bool HTSMUXsetAnalogueActive(tMUXSensor muxsensor)<br>
 *        added bool HTSMUXsetAnalogueInactive(tMUXSensor muxsensor)<br>
 *        corrected function description for HTSMUXSensorType()
 * - 0.10: Removed unnecessary read from HTSMUXsendCommand()
 * - 0.11: Added long uByteToLong(byte a1, byte a2, byte a3, byte a4);
 * - 0.12: Added HTSMUXreadPowerStatus(tSensors link)<br>
 *         Added int round(float fl)
 * - 0.13: Added motor mux types and data structs
 * - 0.14: Added check for digital sensors to prevent conflict with built-in drivers\n
 *         Changed clearI2CError to take ubyte for address, thanks Aswin
 * - 0.15: Removed motor mux and sensor mux functions and types out
 * - 0.16: Added max() and min() functions by Mike Henning, Max Bareiss
 *
 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 27 April 2011
 * \version 0.16
 */

#pragma systemFile

#ifndef __COMMON_H__
#define __COMMON_H__

#undef __COMMON_H_DEBUG__
//#define __COMMON_H_DEBUG__

/*!< define this as 0 to remove the check  */
#ifndef __COMMON_H_SENSOR_CHECK__
#define __COMMON_H_SENSOR_CHECK__ 1
#else
#warn "sensor checking disabled, I hope you know what you are doing!"
#endif

#include "firmwareVersion.h"
#if (kRobotCVersionNumeric < 359)
#error "These drivers are only supported on RobotC version 3.59 or higher"
#endif

#ifndef MAX_ARR_SIZE
/**
 * Maximum buffer size for byte_array, can be overridden in your own program.
 * It's 17 bytes big because the max I2C buffer size is 16, plus 1 byte to denote
 * packet length.
 */
#define MAX_ARR_SIZE 17
#endif


/**
 * This define returns the smaller of the two numbers
 */
#define min2(a, b) (a < b ? a : b)


/**
 * This define returns the smallest of the three numbers
 */
#define min3(a, b, c) (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c)


/**
 * This function returns the bigger of the two numbers
 */
#define max2(a, b) (a > b ? a : b)


/**
 * This function returns the biggest of the three numbers
 */
#define max3(a, b, c) (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c)


/**
 * Returns x if it is between min and max. If outside the range,
 * it returns min or max.
 */
#define clip(a, b, c) min2(c, max2(b, a))


/**
 * Array of bytes as a struct, this is a work around for RobotC's inability to pass an array to
 * a function.
 */
typedef ubyte tByteArray[MAX_ARR_SIZE];
typedef sbyte tsByteArray[MAX_ARR_SIZE];
typedef ubyte tMassiveArray[128];             /*!< 128 byte array for very large blobs of data */
typedef ubyte tHugeByteArray[64];             /*!< 64 byte array for very large blobs of data */
typedef ubyte tBigByteArray[32];              /*!< 32 byte array for large blobs of data */
typedef ubyte tIPaddr[4];                     /*!< Struct for holding an IP address */

/**
 * Array of ints as a struct, this is a work around for RobotC's inability to pass an array to
 * a function.
 */
typedef int tIntArray[MAX_ARR_SIZE];

void clearI2CError(tSensors link, ubyte address);
void clearI2Cbus(tSensors link);
bool waitForI2CBus(tSensors link);
bool writeI2C(tSensors link, tByteArray &request, tByteArray &reply, int replylen);
bool writeI2C(tSensors link, tByteArray &request);


/**
 * Clear out the error state on I2C bus by sending a bunch of dummy
 * packets.
 * @param link the port number
 * @param address the I2C address we're sending to
 */
void clearI2CError(tSensors link, ubyte address) {
  ubyte error_array[2];
  error_array[0] = 1;           // Message size
  error_array[1] = address; // I2C Address

#ifdef __COMMON_H_DEBUG__
  eraseDisplay();
  nxtDisplayTextLine(3, "rxmit: %d", ubyteToInt(error_array[1]));
  wait1Msec(2000);
#endif // __COMMON_H_DEBUG__

  for (int i = 0; i < 5; i++) {
    sendI2CMsg(link, &error_array[0], 0);
    wait1Msec(5);
  }
}


/**
 * Wait for the I2C bus to be ready for the next message
 * @param link the port number
 * @return true if no error occured, false if it did
 */
bool waitForI2CBus(tSensors link)
{
  //TI2CStatus i2cstatus;
  while (true)
  {
    //i2cstatus = nI2CStatus[link];
    switch (nI2CStatus[link])
    //switch(i2cstatus)
    {
	    case NO_ERR:
	      return true;

	    case STAT_COMM_PENDING:
	      break;

	    case ERR_COMM_CHAN_NOT_READY:
	      break;

	    case ERR_COMM_BUS_ERR:
	#ifdef __COMMON_H_DEBUG__
	      PlaySound(soundLowBuzz);
	      while (bSoundActive) {}
	#endif // __COMMON_H_DEBUG__
        return false;
    }
    EndTimeSlice();
  }
}


/**
 * Write to the I2C bus. This function will clear the bus and wait for it be ready
 * before any bytes are sent.
 * @param link the port number
 * @param request the data to be sent
 * @return true if no error occured, false if it did
 */
bool writeI2C(tSensors link, tByteArray &request) {

#if (__COMMON_H_SENSOR_CHECK__ == 1)
  //TSensorTypes type = SensorType[link];

  switch (SensorType[link])
  {
    case sensorI2CCustom:                 break;
    case sensorI2CCustom9V:               break;
    case sensorI2CCustomFast:             break;
    case sensorI2CCustomFast9V:           break;
    case sensorI2CCustomFastSkipStates9V: break;
    case sensorI2CCustomFastSkipStates:   break;
    default:
	    hogCPU();
	    PlaySound(soundException);
	    eraseDisplay();
	    nxtDisplayCenteredTextLine(0, "3rd Party Driver");
	    nxtDisplayCenteredTextLine(1, "ERROR");
	    nxtDisplayCenteredTextLine(2, "You have not");
	    nxtDisplayCenteredTextLine(3, "setup the sensor");
	    nxtDisplayCenteredTextLine(4, "port correctly. ");
	    nxtDisplayCenteredTextLine(5, "Please refer to");
	    nxtDisplayCenteredTextLine(6, "one of the");
	    nxtDisplayCenteredTextLine(7, "examples.");
	    wait1Msec(10000);
	    StopAllTasks();
  }
#endif

  if (!waitForI2CBus(link)) {
    clearI2CError(link, request[1]);

    // Let's try the bus again, see if the above packets flushed it out
    // clearI2CBus(link);
    if (!waitForI2CBus(link))
      return false;
  }

  sendI2CMsg(link, &request[0], 0);

  if (!waitForI2CBus(link)) {
    clearI2CError(link, request[1]);
    sendI2CMsg(link, &request[0], 0);
    if (!waitForI2CBus(link))
      return false;
  }
  return true;
}


/**
 * Write to the I2C bus. This function will clear the bus and wait for it be ready
 * before any bytes are sent.
 * @param link the port number
 * @param request the data to be sent
 * @param reply array to hold received data
 * @param replylen the number of bytes (if any) expected in reply to this command
 * @return true if no error occured, false if it did
 */
bool writeI2C(tSensors link, tByteArray &request, tByteArray &reply, int replylen) {
  // clear the input data buffer

#if (__COMMON_H_SENSOR_CHECK__ == 1)
  //TSensorTypes type = SensorType[link];

  switch (SensorType[link])
  {
    case sensorI2CCustom:                 break;
    case sensorI2CCustom9V:               break;
    case sensorI2CCustomFast:             break;
    case sensorI2CCustomFast9V:           break;
    case sensorI2CCustomFastSkipStates9V: break;
    case sensorI2CCustomFastSkipStates:   break;
    default:
	    hogCPU();
	    PlaySound(soundException);
	    eraseDisplay();
	    nxtDisplayCenteredTextLine(0, "3rd Party Driver");
	    nxtDisplayCenteredTextLine(1, "ERROR");
	    nxtDisplayCenteredTextLine(2, "You have not");
	    nxtDisplayCenteredTextLine(3, "setup the sensor");
	    nxtDisplayCenteredTextLine(4, "port correctly. ");
	    nxtDisplayCenteredTextLine(5, "Please refer to");
	    nxtDisplayCenteredTextLine(6, "one of the");
	    nxtDisplayCenteredTextLine(7, "examples.");
	    wait1Msec(10000);
	    StopAllTasks();
  }
#endif

  if (!waitForI2CBus(link)) {
    clearI2CError(link, request[1]);

    // Let's try the bus again, see if the above packets flushed it out
    // clearI2CBus(link);
    if (!waitForI2CBus(link))
      return false;
  }

  sendI2CMsg(link, &request[0], replylen);

  if (!waitForI2CBus(link)) {
    clearI2CError(link, request[1]);
    sendI2CMsg(link, &request[0], replylen);
    if (!waitForI2CBus(link))
      return false;
  }

  // ask for the input to put into the data array
  readI2CReply(link, &reply[0], replylen);

  return true;
}

/*
bool I2CreadInt(tSensors link, ubyte address, ubyte reg, int &retval, tByteArray request, tByteArray reply, bool msbfirst = true)
{
  request[0] = 2;            // Message size
  request[1] = address; // I2C Address
  request[2] = reg;

  if (!writeI2C(link, request, reply, 2))
    return false;

  retval = (msbfirst) ? reply[1] + (reply[0] << 8) : reply[0] + (reply[1] << 8);

  return true;
}


bool I2CreadLong(tSensors link, ubyte address, ubyte reg, int &retval, tByteArray request, tByteArray reply, bool msbfirst = true)
{
  request[0] = 2;            // Message size
  request[1] = address; // I2C Address
  request[2] = reg;

  if (!writeI2C(link, request, reply, 4))
    return false;

  if (msbfirst)
    retval = (reply[0] << 24) + (reply[1] << 16) + (reply[2] <<  8) + reply[3];
  else
    retval = (reply[3] << 24) + (reply[2] << 16) + (reply[1] <<  8) + reply[0];

  return true;
}


bool I2CreadSByte(tSensors link, ubyte address, ubyte reg, sbyte &retval, tByteArray request, tByteArray reply)
{
  request[0] = 2;            // Message size
  request[1] = address; // I2C Address
  request[2] = reg;

  if (!writeI2C(link, request, reply, 1))
    return false;

  retval =  (reply[0] >= 128) ? (int)reply[0] - 256 : (int)reply[0];

  return true;
}


bool I2CreadUByte(tSensors link, ubyte address, ubyte reg, ubyte &retval, tByteArray request, tByteArray reply)
{
  request[0] = 2;            // Message size
  request[1] = address; // I2C Address
  request[2] = reg;

  if (!writeI2C(link, request, reply, 1))
    return false;

  retval =  reply[0];

  return true;
}
*/

/**
 * Create a unique ID (UID) for an NXT.  This based on the last 3 bytes
 * of the Bluetooth address.  The first 3 bytes are manufacturer
 * specific and identical for all NXTs and are therefore not used.
 * @return a unique ID for the NXT.
 */
long getUID() {
  TBTAddress btAddr;
  getBTAddress(btAddr);

  // Only last 3 bytes are unique in the BT address, the other three are for the
  // manufacturer (LEGO):  http://www.coffer.com/mac_find/?string=lego
   return (long)btAddr[5] + ((long)btAddr[4] << 8) + ((long)btAddr[3] << 16);
}


#define STRTOK_MAX_TOKEN_SIZE 20
#define STRTOK_MAX_BUFFER_SIZE 50

/**
 * Tokenise an array of chars, using a seperator
 * @param buffer pointer to buffer we're parsing
 * @param token pointer to buffer to hold the tokens as we find them
 * @param seperator the seperator used between tokens
 * @return true if there are still tokens left, false if we're done
 */
bool strtok(char *buffer, char *token, char *seperator)
{
  int pos = StringFind(buffer, seperator);
  char t_buff[STRTOK_MAX_BUFFER_SIZE];

  // Make sure we zero out the buffer and token
  memset(token, 0, STRTOK_MAX_TOKEN_SIZE);
  memset(&t_buff[0], 0, STRTOK_MAX_BUFFER_SIZE);

  // Looks like we found a seperator
  if (pos >= 0)
  {
    // Copy the first token into the token buffer, only if the token is
    // not an empty one
    if (pos > 0)
      memcpy(token, buffer, pos);
    // Now copy characters -after- the seperator into the temp buffer
    memcpy(&t_buff[0], buffer + (pos + 1), strlen(buffer) - pos);
    // Zero out the real buffer
    memset(buffer, 0, strlen(buffer) + 1);
    // Copy the temp buffer, which now only contains everything after the previous
    // token into the buffer for the next round.
    memcpy(buffer, &t_buff[0], strlen(&t_buff[0]));
    return true;
  }
  // We found no seperator but the buffer still contains a string
  // This can happen when there is no trailing seperator
  else if(strlen(buffer) > 0)
  {
    // Copy the token into the token buffer
    memcpy(token, buffer, strlen(buffer));
    // Zero out the remainder of the buffer
    memset(buffer, 0, strlen(buffer) + 1);
    return true;
  }
  return false;
}


#endif // __COMMON_H__

/*
 * $Id: common.h 134 2013-03-12 18:15:17Z xander $
 */
/* @} */
/* @} */
