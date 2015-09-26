/*!@addtogroup HiTechnic
 * @{
 * @defgroup htirl IR Link Sensor
 * HiTechnic IR Link Sensor driver
 * @{
 */

/*
 * $Id: hitechnic-irlink.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef _HTIRL_H_
#define _HTIRL_H_
/** \file hitechnic-irlink.h
 * \brief HiTechnic IR Link Sensor driver
 *
 * hitechnic-irlink.h provides an API for the HiTechnic IR Link Sensor.
 *
 * Changelog:
 * - 1.0: Initial release
 * - 1.1: Minor changes
 * - 1.2: Rewrite to make use of the new common.h API
 * - 1.3: Clarified port numbering
 * - 1.4: Removed inline functions
 * - 1.5: Added PFsinglePinOutputMode() functionality to control motors without a timeout\n
 *        Added PFmotor() as a wrapper for PFsinglePinOutputMode()\n
 *        eCPMMotorCommand has been replaced with more generic ePWMMotorCommand\n
 *        transmitIR() now works according to the specs\n
 *
 * Credits:
 * - Big thanks to HiTechnic for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 25 May 2010
 * \version 1.4
 * \example hitechnic-irlink-test1.c
 */

#pragma systemFile

//#define _DEBUG_DRIVER_    /*!< Compile the driver with debugging code */

#ifdef _DEBUG_DRIVER_
#warn "-----------------------------------------"
#warn "HTIRL DRIVER COMPILED WITH DEBUGGING CODE"
#warn "-----------------------------------------"
#endif

#ifndef _COMMON_H_
#include "common.h"
#endif

#define BUF_HEADSIZE    3   /*!< I2C buff size, address and register */
#define BUF_DATASIZE    11  /*!< max size of encoded buffer */
#define BUF_TAILSIZE    3   /*!< IR data length, IR Link mode and start transmission */
#define START_HEAD      0   /*!< index of start of header */
#define START_DATA      3   /*!< index of start of data payload */
#define START_TAIL      15  /*!< index of start of tail */

#define PFSPORT(X) X / 8
#define PFCHAN(X) (X % 8) / 2
#define PFMOT(X) X % 2

byte toggle[4] = {0, 0, 0, 0};

/*!< Motor connections */
typedef enum {
  pfmotor_S1_C1_A = 0,  /*!< Motor A, Channel 1, IR Link connected to S1 */
  pfmotor_S1_C1_B,      /*!< Motor B, Channel 1, IR Link connected to S1 */
  pfmotor_S1_C2_A,      /*!< Motor A, Channel 2, IR Link connected to S1 */
  pfmotor_S1_C2_B,      /*!< Motor B, Channel 2, IR Link connected to S1 */
  pfmotor_S1_C3_A,      /*!< Motor A, Channel 3, IR Link connected to S1 */
  pfmotor_S1_C3_B,      /*!< Motor B, Channel 3, IR Link connected to S1 */
  pfmotor_S1_C4_A,      /*!< Motor A, Channel 4, IR Link connected to S1 */
  pfmotor_S1_C4_B,      /*!< Motor B, Channel 4, IR Link connected to S1 */
  pfmotor_S2_C1_A,      /*!< Motor A, Channel 1, IR Link connected to S2 */
  pfmotor_S2_C1_B,      /*!< Motor B, Channel 1, IR Link connected to S2 */
  pfmotor_S2_C2_A,      /*!< Motor A, Channel 2, IR Link connected to S2 */
  pfmotor_S2_C2_B,      /*!< Motor B, Channel 2, IR Link connected to S2 */
  pfmotor_S2_C3_A,      /*!< Motor A, Channel 3, IR Link connected to S2 */
  pfmotor_S2_C3_B,      /*!< Motor B, Channel 3, IR Link connected to S2 */
  pfmotor_S2_C4_A,      /*!< Motor A, Channel 4, IR Link connected to S2 */
  pfmotor_S2_C4_B,      /*!< Motor B, Channel 4, IR Link connected to S2 */
  pfmotor_S3_C1_A,      /*!< Motor A, Channel 1, IR Link connected to S3 */
  pfmotor_S3_C1_B,      /*!< Motor B, Channel 1, IR Link connected to S3 */
  pfmotor_S3_C2_A,      /*!< Motor A, Channel 2, IR Link connected to S3 */
  pfmotor_S3_C2_B,      /*!< Motor B, Channel 2, IR Link connected to S3 */
  pfmotor_S3_C3_A,      /*!< Motor A, Channel 3, IR Link connected to S3 */
  pfmotor_S3_C3_B,      /*!< Motor B, Channel 3, IR Link connected to S3 */
  pfmotor_S3_C4_A,      /*!< Motor A, Channel 4, IR Link connected to S3 */
  pfmotor_S3_C4_B,      /*!< Motor B, Channel 4, IR Link connected to S3 */
  pfmotor_S4_C1_A,      /*!< Motor A, Channel 1, IR Link connected to S4 */
  pfmotor_S4_C1_B,      /*!< Motor B, Channel 1, IR Link connected to S4 */
  pfmotor_S4_C2_A,      /*!< Motor A, Channel 2, IR Link connected to S4 */
  pfmotor_S4_C2_B,      /*!< Motor B, Channel 2, IR Link connected to S4 */
  pfmotor_S4_C3_A,      /*!< Motor A, Channel 3, IR Link connected to S4 */
  pfmotor_S4_C3_B,      /*!< Motor B, Channel 3, IR Link connected to S4 */
  pfmotor_S4_C4_A,      /*!< Motor A, Channel 4, IR Link connected to S4 */
  pfmotor_S4_C4_B,      /*!< Motor B, Channel 4, IR Link connected to S4 */
} tPFmotor;


/*!< PWM Mode commands */
typedef enum {
  MOTOR_FLOAT = 0,      /*!< Float the motor */
  MOTOR_FWD_PWM_1 = 1,  /*!< Forward speed 1 */
  MOTOR_FWD_PWM_2 = 2,  /*!< Forward speed 2 */
  MOTOR_FWD_PWM_3 = 3,  /*!< Forward speed 3 */
  MOTOR_FWD_PWM_4 = 4,  /*!< Forward speed 4 */
  MOTOR_FWD_PWM_5 = 5,  /*!< Forward speed 5 */
  MOTOR_FWD_PWM_6 = 6,  /*!< Forward speed 6 */
  MOTOR_FWD_PWM_7 = 7,  /*!< Forward speed 7 */
  MOTOR_BRAKE = 8,      /*!< Brake the motor */
  MOTOR_REV_PWM_7 = 9,  /*!< Reverse speed 7 */
  MOTOR_REV_PWM_6 = 10, /*!< Reverse speed 6 */
  MOTOR_REV_PWM_5 = 11, /*!< Reverse speed 5 */
  MOTOR_REV_PWM_4 = 12, /*!< Reverse speed 4 */
  MOTOR_REV_PWM_3 = 13, /*!< Reverse speed 3 */
  MOTOR_REV_PWM_2 = 14, /*!< Reverse speed 2 */
  MOTOR_REV_PWM_1 = 15  /*!< Reverse speed 1 */
} ePWMMotorCommand;

/*!< Combo Direct Mode commands */
typedef enum {
  CDM_MOTOR_FLOAT = 0,      /*!< Float the motor */
  CDM_MOTOR_FWD = 1,        /*!< Forward */
  CDM_MOTOR_BAK = 2,        /*!< Reverse */
  CDM_MOTOR_BRAKE = 3       /*!< Brake the motor */
} eCDMMotorCommand;

// Function prototypes
// inline void addI2CHead(tByteArray &data);
// inline void addI2CTail(tByteArray &data);
void PFcomboDirectMode(tSensors link, int channel, eCDMMotorCommand _motorB, eCDMMotorCommand _motorA);
void PFcomboPwmMode(tSensors link, int channel, ePWMMotorCommand _motorB, ePWMMotorCommand _motorA);
void encodeBuffer(tByteArray &iBuffer, tByteArray &oBuffer);
void transmitIR(tSensors link, tByteArray &oBuffer, int channel);

#ifdef _DEBUG_DRIVER_
void decToBin(int number, int length, string &output);
void debugIR(tByteArray &data);


/**
 * Returns a binary representation in a string of an int with specified length
 *
 * Note: this function is only available when driver is compiled with _DEBUG_DRIVER_ defined.
 * @param number the number to be converted to a binary representation
 * @param length number of bits to convert
 * @param output the number converted to binary representation
 */
void decToBin(int number, int length, string &output) {
  memset(output, 0, sizeof(output));
  output = "";

  for (int i = 0; i < length; i++) {
    output += (number & (1<< (length - 1))) >> (length - 1);
    number = number << 1;
  }
}


/**
 * Print out the buffer in question to the screen using the following format:
 *
 * @<index@> @<binary reprentation@> @<hex representation@>
 *
 * 0 11001100 0xCC
 *
 * It pauses for 10 seconds between each screenful, accompanied by a beep.
 *
 * Note: this function is only available when driver is compiled with _DEBUG_DRIVER_ defined.
 * @param data the data to be displayed as binary/hex numbers
 */
void debugIR(tByteArray &data) {
  string _output;
  for (int i = 0; i < MAX_ARR_SIZE; i++) {
    if ((i != 0) && (i % 8 == 0)) {
      wait1Msec(10000);
      PlaySound(soundBlip);
      eraseDisplay();
    }
    decToBin(data[i], 8, _output);
    StringFormat(_output, "%2d %s", i, _output);
    nxtDisplayTextLine(i % 8, "%s 0x%02x", _output, ubyteToInt(data[i]));
  }
  wait1Msec(10000);
}
#endif // _DEBUG_DRIVER_


/**
 * Control two motors using the ComboDirectMode.  This mode does not allow for fine grained
 * speed control.
 * @param link the sensor port number
 * @param channel the channel of the receiver we wish to communicate with, numbered 0-3
 * @param _motorB the command to be sent to Motor B
 * @param _motorA the command to be sent to Motor A
 */
void PFcomboDirectMode(tSensors link, int channel, eCDMMotorCommand _motorB, eCDMMotorCommand _motorA) {
  tByteArray _iBuffer;
  tByteArray _oBuffer;

  // Clear the input buffer before we start filling it
  memset(_iBuffer, 0, sizeof(tByteArray));
  memset(_oBuffer, 0, sizeof(tByteArray));

  // This is the unencoded command for the IR receiver
  _iBuffer[0] = (channel << 4) + 1;
  _iBuffer[1] = ((ubyte)_motorB << 6) + ((ubyte)_motorA << 4);
  _iBuffer[1] += 0xF ^ (_iBuffer[0] >> 4) ^ (_iBuffer[0] & 0xF) ^ (_iBuffer[1] >> 4);

  // Setup the header of the I2C packet
  _oBuffer[0] = 16;    // Total msg length
  _oBuffer[1] = 0x02;  // I2C device address
  _oBuffer[2] = 0x42;  // Internal register

  // Generate the data payload
  encodeBuffer(_iBuffer, (tByteArray)_oBuffer);                       // Encode PF command

  // Setup the tail end of the packet
  _oBuffer[BUF_HEADSIZE + BUF_DATASIZE] = 11;         // Total IR command length
  _oBuffer[BUF_HEADSIZE + BUF_DATASIZE + 1] = 0x02;   // IRLink mode 0x02 is PF motor
  _oBuffer[BUF_HEADSIZE + BUF_DATASIZE + 2] = 0x01;   // Start transmitting

  transmitIR(link, _oBuffer, channel);
}


/*
  =============================================================================
  Combo Direct Mode
        | Nib 1 | Nib 2 | Nib 3 | Nib 4 |
  start  a 1 C C B B B B A A A A L L L L stop

 */
/**
 * Control two motors using the ComboPWMMode.  This mode allows for fine grained
 * speed control.
 * @param link the sensor port number
 * @param channel the channel of the receiver we wish to communicate with, numbered 0-3
 * @param _motorB the command to be sent to Motor B
 * @param _motorA the command to be sent to Motor A
 */
void PFcomboPwmMode(tSensors link, int channel, ePWMMotorCommand _motorB, ePWMMotorCommand _motorA) {
  tByteArray _iBuffer;
  tByteArray _oBuffer;

  // Clear the input buffer before we start filling it
  memset(_iBuffer, 0, sizeof(tByteArray));
  memset(_oBuffer, 0, sizeof(tByteArray));

  // This is the unencoded command for the IR receiver
  _iBuffer[0] = (1 << 6) + (channel << 4) + _motorA;
  _iBuffer[1] = ((ubyte)_motorB << 4);
  //_iBuffer[1] = (_motorB << 4) + (0xF ^ ((1 << 2) + channel) ^ _motorA ^ _motorB);
  _iBuffer[1] += 0xF ^ (_iBuffer[0] >> 4) ^ (_iBuffer[0] & 0xF) ^ (_iBuffer[1] >> 4);

  // Setup the header of the I2C packet
  _oBuffer[0] = 16;    // Total msg length
  _oBuffer[1] = 0x02;  // I2C device address
  _oBuffer[2] = 0x42;  // Internal register
  // Generate the data payload
  encodeBuffer(_iBuffer, _oBuffer);                       // Encode PF command

  // Setup the tail end of the packet
  _oBuffer[BUF_HEADSIZE + BUF_DATASIZE] = 11;         // Total IR command length
  _oBuffer[BUF_HEADSIZE + BUF_DATASIZE + 1] = 0x02;   // IRLink mode 0x02 is PF motor
  _oBuffer[BUF_HEADSIZE + BUF_DATASIZE + 2] = 0x01;   // Start transmitting
  //_oBuffer[3] = 0x80;

  transmitIR(link, _oBuffer, channel);
}


/*
  =============================================================================
  Single Pin Output Mode
        | Nib 1 | Nib 2 | Nib 3 | Nib 4 |
  start  T 0 C C a 1 M 0 D D D D L L L L stop

 */
/**
 * Control one motor with no timeout. This mode allows for fine grained
 * speed control.
 * @param link the sensor port number
 * @param channel the channel of the receiver we wish to communicate with, numbered 0-3
 * @param _motor the motor to be controlled, 0 or 1, for A or B
 * @param _motorCmd the command to send to the motor, 0-15
 */
void PFsinglePinOutputMode(tSensors link, ubyte channel, ubyte _motor, ePWMMotorCommand _motorCmd) {
  tByteArray _iBuffer;
  tByteArray _oBuffer;

  toggle[link] ^= 1;

  // Clear the input buffer before we start filling it
  memset(_iBuffer, 0, sizeof(tByteArray));
  memset(_oBuffer, 0, sizeof(tByteArray));

  // This is the unencoded command for the IR receiver
  _iBuffer[0] = (toggle[link] <<7 ) + (channel << 4) + (1 << 2) + _motor;
  _iBuffer[1] = ((ubyte)_motorCmd << 4);
  _iBuffer[1] += 0xF ^ (_iBuffer[0] >> 4) ^ (_iBuffer[0] & 0xF) ^ (_iBuffer[1] >> 4);

  // Setup the header of the I2C packet
  _oBuffer[0] = 16;    // Total msg length
  _oBuffer[1] = 0x02;  // I2C device address
  _oBuffer[2] = 0x42;  // Internal register
  // Generate the data payload
  encodeBuffer(_iBuffer, _oBuffer);                       // Encode PF command

  // Setup the tail end of the packet
  _oBuffer[BUF_HEADSIZE + BUF_DATASIZE] = 11;         // Total IR command length
  _oBuffer[BUF_HEADSIZE + BUF_DATASIZE + 1] = 0x02;   // IRLink mode 0x02 is PF motor
  _oBuffer[BUF_HEADSIZE + BUF_DATASIZE + 2] = 0x01;   // Start transmitting

  transmitIR(link, _oBuffer, channel);
}


/**
 * Control one motor with no timeout. This mode allows for fine grained
 * speed control.
 * @param pfmotor the motor to which to send the command
 * @param _motorCmd the command to send to the motor, 0-15
 */
void PFMotor(tPFmotor pfmotor, ePWMMotorCommand _motorCmd) {
  PFsinglePinOutputMode((tSensors)PFSPORT(pfmotor), (ubyte)PFCHAN(pfmotor), (ubyte)PFMOT(pfmotor), _motorCmd);
}


/**
 * Encode the input buffer into a special format for the IRLink.
 *
 * Note: this is an internal function and should not be called directly.
 * @param iBuffer the data that is be encoded
 * @param oBuffer output buffer for encoded data
 */
void encodeBuffer(tByteArray &iBuffer, tByteArray &oBuffer) {
  int _oByteIdx = 0;
  int _oBitIdx = 0;
  int _iIndex = 0;              // _iBUffer bit index
  int _oIndex = 0;              // _oBuffer bit index
  int _len = 0;

  //debugIR(iBuffer);
  // Calculate the size of the output bit index
  _oIndex = (8 * (MAX_ARR_SIZE - BUF_HEADSIZE)) - 1;

  // Start bit is a special case and is encoded as 0x80
  oBuffer[START_DATA] = 0x80; // Start bit
  _oIndex -= 8;                   // move the index along 8 bits.

  // Bits in the input buffer are encoded as follows:
  // 1 is encoded as 10000
  // 0 is encoded as 100
  // The encoded bits are tacked onto the end of the output
  // buffer, byte boundaries are ignored.
  for (_iIndex = 0; _iIndex < (2 * 8); _iIndex++) {
    _len = (iBuffer[_iIndex / 8] & 0x80) ? 5 : 3;
    _oByteIdx = (MAX_ARR_SIZE - 1) - (_oIndex / 8);
    _oBitIdx = _oIndex % 8;
    oBuffer[_oByteIdx] += (1 << _oBitIdx);
    _oIndex -= _len;
    iBuffer[_iIndex / 8] <<= 1;
  }

  // Finally, add the stop byte to the end of our command
  _oByteIdx = (MAX_ARR_SIZE - 1) - (_oIndex / 8);
  _oBitIdx = _oIndex % 8;
  oBuffer[_oByteIdx] += (1 << _oBitIdx);

}


/**
 * Send the command to the IRLink Sensor for transmission.
 *
 * Note: this is an internal function and should not be called directly.
 * If the driver is compiled with _DEBUG_DRIVER_, this function will call
 * debugIR() prior to transmitting the data for debugging purposes.
 * @param link the sensor port number
 * @param oBuffer the data that is be transmitted
 * @param channel the channel number of the receiver
 */
void transmitIR(tSensors link, tByteArray &oBuffer, int channel) {
  long starttime = 0;
#ifdef _DEBUG_DRIVER_
  debugIR(oBuffer);
#endif // _DEBUG_DRIVER_

  // Channel is assumed to be 1-4 in the specs.
  // channel++;

  // Message should be sent 5 times according to the PF specs
  // Specific timing has to be used to prevent interence with other
  // transmitters.

  // First transmission
  wait1Msec((4 - channel) * 16);
  starttime = nPgmTime;
  if (!writeI2C(link, oBuffer)) return;

  // Second transmission
  wait1Msec(5 * 16 - (nPgmTime - starttime));
  starttime = nPgmTime;
  if (!writeI2C(link, oBuffer)) return;

  // Third transmission
  wait1Msec(5 * 16 - (nPgmTime - starttime));
  starttime = nPgmTime;
  if (!writeI2C(link, oBuffer)) return;

  // Fourth transmission
  wait1Msec((6 + (2*channel) * 16) - (nPgmTime - starttime));
  starttime = nPgmTime;
  if (!writeI2C(link, oBuffer)) return;

  // Fifth transmission
  wait1Msec((6 + (2*channel) * 16) - (nPgmTime - starttime));
  if (!writeI2C(link, oBuffer)) return;
}

#endif // _HTIRL_H_

/*
 * $Id: hitechnic-irlink.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
