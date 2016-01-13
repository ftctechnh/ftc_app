/*!@addtogroup HiTechnic
 * @{
 * @defgroup HTRCX IR Link RCX comms
 * HiTechnic IR Link RCX Comms Driver
 * @{
 */

/*
 * $Id: hitechnic-irlink-rcx.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef _HTRCX_H_
#define _HTRCX_H_
/** \file hitechnic-irlink-rcx.h
 * \brief HiTechnic IR Link RCX Comms Driver
 *
 * hitechnic-irlink-rcx.h provides an API for the HiTechnic IR Link Sensor to allow
 * communication between the NXT and RCX.
 *
 * Changelog:
 * - 1.0: Initial release
 * - 1.1: HTRCXreadResp now clears entire IR read buffer after read
 *
 * Credits:
 * - Big thanks to HiTechnic for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 31 October 2010
 * \version 1.1
 * \example hitechnic-irlink-rcx-test1.c
 */

#pragma systemFile

#ifndef _COMMON_H_
#include "common.h"
#endif

tByteArray HTRCXI2CRequest;
tByteArray HTRCXI2CReply;
tByteArray HTRCXIRMsg;

byte HTRCXCmdToggle = 0;

// Function prototypes
bool HTRCXsendHeader(tSensors link);
void HTRCXencode(tSensors link, tByteArray &iBuffer, tByteArray &oBuffer);
bool HTRCXreadResp(tSensors link, tByteArray &response);
bool HTRCXplaySound(tSensors link, unsigned byte sound);
bool HTRCXsendByte(tSensors link, unsigned byte data);
bool HTRCXsendWord(tSensors link, short data);
bool HTRCXmotorOn(tSensors link, unsigned byte _motor);
bool HTRCXmotorOff(tSensors link, unsigned byte _motor);
bool HTRCXmotorFwd(tSensors link, unsigned byte _motor);
bool HTRCXmotorRev(tSensors link, unsigned byte _motor);
bool HTRCXmotorPwr(tSensors link, unsigned byte _motor, unsigned byte power);


/**
 * Sends the RCX IR message header with all the trimmings.
 *
 * Note: this is an internal function and should be not be called directly.
 * @param link the sensor port number
 * @return true if no error occured, false if it did
 */
bool HTRCXsendHeader(tSensors link) {
  memset(HTRCXI2CRequest, 0, sizeof(tByteArray));

  // Send the 0x55 0x55 0x00 IR message header
  HTRCXI2CRequest[0] = 8;
  HTRCXI2CRequest[1] = 0x02;
  HTRCXI2CRequest[2] = 0x4A;
  HTRCXI2CRequest[3] = 0x55;
  HTRCXI2CRequest[4] = 0xFF;
  HTRCXI2CRequest[5] = 0x00;
  HTRCXI2CRequest[6] = 0x03;
  HTRCXI2CRequest[7] = 0x00;
  HTRCXI2CRequest[8] = 0x01;

  return writeI2C(link, HTRCXI2CRequest);
}


/**
 * This encodes the message into the standard RCX format with
 * opcodes, data and their complements, followed by a checksum.\n
 * The output is an I2C message that can be sent to the IR Link.\n
 * This does NOT send the IR message header.
 *
 * Note: this is an internal function and should be not be called directly.
 * @param link the sensor port number
 * @param iBuffer the IR message that is to be sent by the IR Link to the RCX
 * @param oBuffer the I2C message to be sent to the IR Link
 */
void HTRCXencode(tSensors link, tByteArray &iBuffer, tByteArray &oBuffer) {
  int checksum = 0;
  int msgsize = iBuffer[0];

  // Max size of an RCX message is 5 bytes due to the encoding and additional
  // info needed for the IR Link.
  if (msgsize > 5) return;

  // Calculate the total size of the I2C packet.
  // I2C packet info + msgsize (with inverse) + checksum + IR Link info
  oBuffer[0] = 2 + (msgsize * 2) + 2 + 3;
  oBuffer[1] = 0x02;
  oBuffer[2] = 0x4D - ((msgsize * 2) + 2);

  // Build the outgoing IR message and inverse
  // Keep track of checksum
  for (int i = 0; i < msgsize; i++) {
    checksum += iBuffer[i + 1];
    oBuffer[3 + (i * 2)] =  iBuffer[i + 1];
    oBuffer[4 + (i * 2)] = ~iBuffer[i + 1];
    //oBuffer[4 + (i * 2)] = ~oBuffer[3 + (i * 2)];
  }

  // Add checksum to outoing packet
  oBuffer[3 + (msgsize * 2)] =   checksum & 0xFF;
  oBuffer[4 + (msgsize * 2)] = ~(checksum & 0xFF);

  // Generate IR Link info
  oBuffer[3 + (msgsize * 2) + 2] = (msgsize * 2) + 2;
  oBuffer[4 + (msgsize * 2) + 2] = 0x00;
  oBuffer[5 + (msgsize * 2) + 2] = 0x01;
}


/**
 * Read a message sent by the RCX.  You will need to poll frequently to
 * check if a message has been sent. The number of bytes received is
 * in the first element of the response array.  If it is non-zero, a message
 * was received.
 *
 * @param link the sensor port number
 * @param response the IR message that was received from the RCX
 * @return true if no error occured, false if it did
 */
bool HTRCXreadResp(tSensors link, tByteArray &response) {
  memset(HTRCXI2CRequest, 0, sizeof(tByteArray));
  memset(HTRCXI2CReply, 0, sizeof(tByteArray));
  memset(response, 0, sizeof(tByteArray));

  HTRCXI2CRequest[0] = 2;
  HTRCXI2CRequest[1] = 0x02;
  HTRCXI2CRequest[2] = 0x50;

  if (!writeI2C(link, HTRCXI2CRequest, HTRCXI2CReply, 16))
    return false;

  // Print the contents
  // printBuffer(HTRCXI2CReply);

  if ((HTRCXI2CReply[0] > 0) && (HTRCXI2CReply[0] < 16)) {
    memcpy(response, HTRCXI2CReply, HTRCXI2CReply[0]);
  }

  memset(HTRCXI2CRequest, 0, sizeof(tByteArray));
  // Clear the buffer count
  HTRCXI2CRequest[0] = 16;
  HTRCXI2CRequest[1] = 0x02;
  HTRCXI2CRequest[2] = 0x50;
  return writeI2C(link, HTRCXI2CRequest);
}


/**
 * Tell the RCX to play a sound.  Sounds are numbered 0-5
 *
 * @param link the sensor port number
 * @param sound the sound to play, numbered 0-6
 * @return true if no error occured, false if it did
 */
bool HTRCXplaySound(tSensors link, unsigned byte sound) {
  // Toggle the toggle bit for dupe command detection
  HTRCXCmdToggle ^= 0x08;
  HTRCXIRMsg[0] = 2;
  HTRCXIRMsg[1] = 0x51 + HTRCXCmdToggle;
  HTRCXIRMsg[2] = sound;

  HTRCXsendHeader(link);
  wait1Msec(12);
  HTRCXencode(link, HTRCXIRMsg, HTRCXI2CRequest);
  writeI2C(link, HTRCXI2CRequest);
  wait1Msec(12);

  return true;
}


/**
 * Send a single byte message to the RCX
 *
 * @param link the sensor port number
 * @param data the data to be sent to the RCX
 * @return true if no error occured, false if it did
 */
bool HTRCXsendByte(tSensors link, unsigned byte data) {
  HTRCXIRMsg[0] = 2;
  HTRCXIRMsg[1] = 0xF7;   // No need to toggle this, apparently.
  HTRCXIRMsg[2] = data;

  HTRCXsendHeader(link);
  wait1Msec(12);
  HTRCXencode(link, HTRCXIRMsg, HTRCXI2CRequest);
  writeI2C(link, HTRCXI2CRequest);
  wait1Msec(12);

  return true;
}

/**
 * Send 2 bytes message to the RCX as a short.
 *
 * NOTE: This only works with the ROBOTC firmware on the RCX
 * @param link the sensor port number
 * @param data the data to be sent to the RCX
 * @return true if no error occured, false if it did
 */
bool HTRCXsendWord(tSensors link, short data) {
  HTRCXIRMsg[0] = 3;
  HTRCXIRMsg[1] = 0xA2;
  HTRCXIRMsg[2] = data & 0xFF;
  HTRCXIRMsg[3] = (data >> 8) & 0xFF;

  HTRCXsendHeader(link);
  wait1Msec(12);
  HTRCXencode(link, HTRCXIRMsg, HTRCXI2CRequest);
  writeI2C(link, HTRCXI2CRequest);
  wait1Msec(12);

  return true;
}


/**
 * Turn the specified motor on
 *
 * NOTE: This does not currently work with the ROBOTC firmware on the RCX
 * @param link the sensor port number
 * @param _motor the motor channel to turn on
 * @return true if no error occured, false if it did
 */
bool HTRCXmotorOn(tSensors link, unsigned byte _motor) {
  // Toggle the toggle bit for dupe command detection
  HTRCXCmdToggle ^= 0x08;
  HTRCXIRMsg[0] = 2;
  HTRCXIRMsg[1] = 0x21 + HTRCXCmdToggle;
  HTRCXIRMsg[2] = _motor + 0x80 + 0x40;

  HTRCXsendHeader(link);
  wait1Msec(12);
  HTRCXencode(link, HTRCXIRMsg, HTRCXI2CRequest);
  writeI2C(link, HTRCXI2CRequest);
  wait1Msec(12);

  return true;
}


/**
 * Turn the specified motor off
 *
 * NOTE: This does not currently work with the ROBOTC firmware on the RCX
 * @param link the sensor port number
 * @param _motor the motor channel to turn off
 * @return true if no error occured, false if it did
 */
bool HTRCXmotorOff(tSensors link, unsigned byte _motor) {
  // Toggle the toggle bit for dupe command detection
  HTRCXCmdToggle ^= 0x08;
  HTRCXIRMsg[0] = 2;
  HTRCXIRMsg[1] = 0x21 + HTRCXCmdToggle;
  HTRCXIRMsg[2] = _motor;

  HTRCXsendHeader(link);
  wait1Msec(12);
  HTRCXencode(link, HTRCXIRMsg, HTRCXI2CRequest);
  writeI2C(link, HTRCXI2CRequest);
  wait1Msec(12);

  return true;
}


/**
 * Move the specified motor forward
 *
 * NOTE: This does not currently work with the ROBOTC firmware on the RCX
 * @param link the sensor port number
 * @param _motor the motor channel to move forward
 * @return true if no error occured, false if it did
 */
bool HTRCXmotorFwd(tSensors link, unsigned byte _motor) {
  // Toggle the toggle bit for dupe command detection
  HTRCXCmdToggle ^= 0x08;
  HTRCXIRMsg[0] = 2;
  HTRCXIRMsg[1] = 0xE1 + HTRCXCmdToggle;
  HTRCXIRMsg[2] = _motor + 0x80;

  HTRCXsendHeader(link);
  wait1Msec(12);
  HTRCXencode(link, HTRCXIRMsg, HTRCXI2CRequest);
  writeI2C(link, HTRCXI2CRequest);
  wait1Msec(12);

  return true;
}


/**
 * Move the specified motor reverse
 *
 * NOTE: This does not currently work with the ROBOTC firmware on the RCX
 * @param link the sensor port number
 * @param _motor the motor channel to move reverse
 * @return true if no error occured, false if it did
 */
bool HTRCXmotorRev(tSensors link, unsigned byte _motor) {
  // Toggle the toggle bit for dupe command detection
  HTRCXCmdToggle ^= 0x08;
  HTRCXIRMsg[0] = 2;
  HTRCXIRMsg[1] = 0xE1 + HTRCXCmdToggle;
  HTRCXIRMsg[2] = _motor;

  HTRCXsendHeader(link);
  wait1Msec(12);
  HTRCXencode(link, HTRCXIRMsg, HTRCXI2CRequest);
  writeI2C(link, HTRCXI2CRequest);
  wait1Msec(12);

  return true;
}


/**
 * Set the motor power.
 *
 * NOTE: This does not currently work with the ROBOTC firmware on the RCX
 * @param link the sensor port number
 * @param _motor the motor channel to change the power level of
 * @param power the amount of power to be applied to the motor
 * @return true if no error occured, false if it did
 */
bool HTRCXmotorPwr(tSensors link, unsigned byte _motor, unsigned byte power) {
  // Toggle the toggle bit for dupe command detection
  HTRCXCmdToggle ^= 0x08;
  HTRCXIRMsg[0] = 3;
  HTRCXIRMsg[1] = 0x13 + HTRCXCmdToggle;
  HTRCXIRMsg[2] = _motor;
  HTRCXIRMsg[3] = 2;
  HTRCXIRMsg[4] = power;

  HTRCXsendHeader(link);
  wait1Msec(12);
  HTRCXencode(link, HTRCXIRMsg, HTRCXI2CRequest);
  writeI2C(link, HTRCXI2CRequest);
  wait1Msec(12);

  return true;
}

#endif // _HTRCX_H_

/*
 * $Id: hitechnic-irlink-rcx.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
