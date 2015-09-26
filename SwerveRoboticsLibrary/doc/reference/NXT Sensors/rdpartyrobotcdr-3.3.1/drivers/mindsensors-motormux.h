/*!@addtogroup mindsensors
 * @{
 * @defgroup msmmux NXT Motor MUX
 * NXT Motor MUX
 * @{
 */

/*
 * $Id: mindsensors-motormux.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MSMMUX_H__
#define __MSMMUX_H__
/** \file mindsensors-motormux.h
 * \brief Mindsensors Motor MUX driver
 *
 * mindsensors-motormux.h provides an API for the Mindsensors Motor MUX.
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
 * \date 05 April 2010
 * \version 0.1
 * \example mindsensors-motormux-test1.c
 * \example mindsensors-motormux-test2.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#ifndef __MMUX_H__
#include "common-MMUX.h"
#endif

#define MSMMUX_I2C_ADDR         0x06  /*!< MSMMUX I2C device address */

// Motor control registers
#define MSMMUX_REG_CMD          0x41  /*!< Command register */

#define MSMMUX_MOT_OFFSET       0x42  /*!< Motor regiser offset */
#define MSMMUX_TARG_ENC         0x00  /*!< Target encoder value */
#define MSMMUX_POWER            0x04  /*!< Motor power */
#define MSMMUX_TARG_TIME        0x05  /*!< Time target value */
#define MSMMUX_CMD_B            0x06  /*!< Command B register - for future use */
#define MSMMUX_CMD_A            0x07  /*!< Command A regiser */
#define MSMMUX_ENTRY_SIZE       0x08  /*!< Number of registers per motor channel */
#define MSMMUX_TACHO_MOT1       0x62  /*!< Tacho count for motor 1 */
#define MSMMUX_TACHO_MOT2       0x66  /*!< Tacho count for motor 2 */
#define MSMMUX_STATUS_MOT1      0x72  /*!< Status for motor 1 */
#define MSMMUX_STATUS_MOT2      0x73  /*!< Status for motor 2 */

// PID registers
#define MSMMUX_KP_TACHO         0x7A  /*!< Kp for Tachometer Position Control */
#define MSMMUX_KI_TACHO         0x7C  /*!< Ki for Tachometer Position Control */
#define MSMMUX_KD_TACHO         0x7E  /*!< Kd for Tachometer Position Control */
#define MSMMUX_KP_SPEED         0x80  /*!< Kp for Speed Control */
#define MSMMUX_KI_SPEED         0x82  /*!< Ki for Speed Control */
#define MSMMUX_KD_SPEED         0x84  /*!< Kd for Speed Control */
#define MSMMUX_PASSCOUNT        0x86  /*!< Encoder count tolerance when motor is moving */
#define MSMMUX_TOLERANCE        0x87  /*!< Encoder count tolerance when motor is getting close to target */

// Motor mode commands
#define MSMMUX_CMD_RESET_ALL    0x52  /*!< Reset the tachos and motor values */
#define MSMMUX_CMD_START_BOTH   0x53  /*!< Start both motors with parameters in motor registers */
#define MSMMUX_CMD_FLOAT_MOT1   0x61  /*!< Stop motor 1 and allow to float */
#define MSMMUX_CMD_FLOAT_MOT2   0x62  /*!< Stop motor 2 and allow to float */
#define MSMMUX_CMD_FLOAT_BOTH   0x63  /*!< Stop both motors and allow to float */
#define MSMMUX_CMD_BRAKE_MOT1   0x41  /*!< Stop motor 1 and brake */
#define MSMMUX_CMD_BRAKE_MOT2   0x42  /*!< Stop motor 2 and brake */
#define MSMMUX_CMD_BRAKE_BOTH   0x43  /*!< Stop both motors and brake */
#define MSMMUX_CMD_RESET_MOT1   0x72  /*!< Reset the encoder count for motor 1 */
#define MSMMUX_CMD_RESET_MOT2   0x73  /*!< Reset the encoder count for motor 2 */

// Motor status fields
#define MSMMUX_STAT_SPEED_CTRL  (0x01 << 0) /*!< Motor is programmed to move at a fixed speed. */
#define MSMMUX_STAT_RAMPING     (0x01 << 1) /*!< Motor is currently ramping up or down */
#define MSMMUX_STAT_POWERED     (0x01 << 2) /*!< Motor is powered, does not imply movement */
#define MSMMUX_STAT_POS_CTRL    (0x01 << 3) /*!< Motor is either moving towards target or holding position. */
#define MSMMUX_STAT_BRAKED      (0x01 << 4) /*!< Motor is braked.  0 means motor is floating */
#define MSMMUX_STAT_OVERLOADED  (0x01 << 5) /*!< Set to 1 when motor can't achieve desired speed */
#define MSMMUX_STAT_TIMED       (0x01 << 6) /*!< Motor is running for a specified duration */
#define MSMMUX_STAT_STALLED     (0x01 << 7) /*!< Motor is stalled */

// commandA fields
#define MSMMUX_CMD_SPEED        0x01  /*!< Speed control of your motor */
#define MSMMUX_CMD_RAMP         0x02  /*!< Ramp the speed up or down */
#define MSMMUX_CMD_RELATIVE     0x04  /*!< Make encoder target relative to current position */
#define MSMMUX_CMD_TACHO        0x08  /*!< Use the encoder target to control motor */
#define MSMMUX_CMD_BRK          0x10  /*!< Whether to brake (1) or float (0) when motor has reached its target */
#define MSMMUX_CMD_HOLDPOS      0x20  /*!< Motor will hold position when this is enabled and push back when moved */
#define MSMMUX_CMD_TIME         0x40  /*!< Use the time target to control the motor */
#define MSMMUX_CMD_GO           0x80  /*!< Instruct the MUX to start the motors using the current registers */

#define MSMMUX_RAMP_NONE        0x00  /*!< Use no ramping at all */
#define MSMMUX_RAMP_UP_DOWN     0x03  /*!< Use ramping to bring motor up to speed or to a halt */

#define MSMMUX_ROT_UNLIMITED    0x00  /*!< Allow motor to rotate forever (or until batteries run out, of course) */
#define MSMMUX_ROT_DEGREES      0x01  /*!< Use encoder target to control motor (ie rotate motor X number of degrees) */
//#define MSMMUX_ROT_ROTATIONS    0x02
#define MSMMUX_ROT_SECONDS      0x03  /*!< Use time target to control motor (ie run for X seconds) */


tByteArray MSMMUX_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray MSMMUX_I2CReply;      /*!< Array to hold I2C reply data */

// Function prototypes
void MSMMUXinit();
bool MSMMUXreadStatus(tMUXmotor muxmotor, ubyte &motorStatus);
bool MSMMUXsendCommand(tSensors link, ubyte channel, long setpoint, byte speed, ubyte seconds, ubyte commandA, ubyte address = MSMMUX_I2C_ADDR);
bool MSMMUXsendCommand(tSensors link, ubyte command, ubyte address = MSMMUX_I2C_ADDR);
bool MSMMUXsetPID(tSensors link, unsigned int kpTacho, unsigned int kiTacho, unsigned int kdTacho, unsigned int kpSpeed, unsigned int kiSpeed, unsigned int kdSpeed, ubyte passCount, ubyte tolerance, ubyte address = MSMMUX_I2C_ADDR);
// bool MSMMUXsetPID(tSensors link, int kpTacho, int kiTacho, int kdTacho, int kpSpeed, int kiSpeed, int kdSpeed, ubyte passCount, ubyte tolerance, ubyte address = MSMMUX_I2C_ADDR);
bool MSMMotor(tMUXmotor muxmotor, byte power, ubyte address = MSMMUX_I2C_ADDR);
bool MSMotorStop(tMUXmotor muxmotor, ubyte address = MSMMUX_I2C_ADDR);
bool MSMotorStop(tMUXmotor muxmotor, bool brake, ubyte address = MSMMUX_I2C_ADDR);
void MSMMotorSetRotationTarget(tMUXmotor muxmotor, long target);
void MSMMotorSetTimeTarget(tMUXmotor muxmotor, int target);
void MSMMotorSetEncoderTarget(tMUXmotor muxmotor, long target);
void MSMMotorSetEncoderTarget(tMUXmotor muxmotor, long target, bool relative);
long MSMMotorEncoder(tMUXmotor muxmotor, ubyte address = MSMMUX_I2C_ADDR);
bool MSMMotorEncoderReset(tMUXmotor muxmotor, ubyte address = MSMMUX_I2C_ADDR);
bool MSMMotorEncoderResetAll(tSensors link, ubyte address = MSMMUX_I2C_ADDR);
bool MSMMotorBusy(tMUXmotor muxmotor, ubyte address = MSMMUX_I2C_ADDR);
bool MSMMotorStalled(tMUXmotor muxmotor, ubyte address = MSMMUX_I2C_ADDR);
void MSMMotorSetBrake(tMUXmotor muxmotor);
void MSMMotorSetFloat(tMUXmotor muxmotor);
void MSMMotorSetSpeedCtrl(tMUXmotor muxmotor, bool constspeed);
void MSMMotorSetRamping(tMUXmotor muxmotor, bool ramping);


/*
 * Initialise the mmuxData array needed for keeping track of motor settings
 */
void MSMMUXinit(){
  for (int i = 0; i < 4; i++) {
    memset(mmuxData[i].runToTarget[0], false, 4);
    memset(mmuxData[i].brake[0], true, 4);
    memset(mmuxData[i].pidcontrol[0], true, 4);
    memset(mmuxData[i].target[0], 0, 4*4);
    memset(mmuxData[i].ramping[0], MSMMUX_RAMP_NONE, 4);
    memset(mmuxData[i].targetUnit[0], MSMMUX_ROT_UNLIMITED, 4);
    mmuxData[i].initialised = true;
  }
}


/**
 * Read the status byte of the specified motor
 *
 * @param muxmotor the motor-MUX motor
 * @param motorStatus status of the motor
 * @param address I2C address of the sensor (optional)
 * @return true if no error occured, false if it did
 */
bool MSMMUXreadStatus(tMUXmotor muxmotor, ubyte &motorStatus, ubyte address) {

  memset(MSMMUX_I2CRequest, 0, sizeof(tByteArray));

  MSMMUX_I2CRequest[0] = 2;               // Message size
  MSMMUX_I2CRequest[1] = MSMMUX_I2C_ADDR; // I2C Address

  switch ((byte)MPORT(muxmotor)) {
    case 0: MSMMUX_I2CRequest[2] = MSMMUX_STATUS_MOT1; break;
    case 1: MSMMUX_I2CRequest[2] = MSMMUX_STATUS_MOT2; break;
  }

  if (!writeI2C((tSensors)SPORT(muxmotor), MSMMUX_I2CRequest, MSMMUX_I2CReply, 1))
    return false;

  motorStatus = MSMMUX_I2CReply[0];

  return true;
}


/**
 * Send a command to the MMUX.
 *
 * Note: this is an internal function and shouldn't be used directly
 * @param link the MMUX port number
 * @param channel the channel the command should apply to
 * @param setpoint the encoder count the motor should move to
 * @param speed the speed the motor should move at
 * @param seconds the number of seconds the motor should run for.  Note that this takes precedence over the encoder target
 * @param commandA the command to be sent to the motor
 * @param address I2C address of the sensor (optional)
 * @return true if no error occured, false if it did
 */
bool MSMMUXsendCommand(tSensors link, ubyte channel, long setpoint, byte speed, ubyte seconds, ubyte commandA, ubyte address) {
  memset(MSMMUX_I2CRequest, 0, sizeof(tByteArray));

  MSMMUX_I2CRequest[0] = 10;               // Message size
  MSMMUX_I2CRequest[1] = address;          // I2C Address
  MSMMUX_I2CRequest[2] = MSMMUX_MOT_OFFSET + (channel * MSMMUX_ENTRY_SIZE);
  MSMMUX_I2CRequest[3] = (setpoint >>  0) & 0xFF;
  MSMMUX_I2CRequest[4] = (setpoint >>  8) & 0xFF;
  MSMMUX_I2CRequest[5] = (setpoint >> 16) & 0xFF;
  MSMMUX_I2CRequest[6] = (setpoint >> 24) & 0xFF;
  MSMMUX_I2CRequest[7] = (speed & 0xFF);
  MSMMUX_I2CRequest[8] = seconds;
  MSMMUX_I2CRequest[9] = 0;
  MSMMUX_I2CRequest[10] = commandA;

  // make sure the targetUnit is reset for the next time
  mmuxData[link].targetUnit[channel] = MSMMUX_ROT_UNLIMITED;

  // send the command to the mmux
  return writeI2C(link, MSMMUX_I2CRequest);

}


/**
 * Send a command to the MMUX.
 *
 * Note: this is an internal function and shouldn't be used directly
 * @param link the MMUX port number
 * @param command the command to be sent to the motor
 * @param address I2C address of the sensor (optional)
 * @return true if no error occured, false if it did
 */
bool MSMMUXsendCommand(tSensors link, ubyte command, ubyte address) {
  memset(MSMMUX_I2CRequest, 0, sizeof(tByteArray));

  MSMMUX_I2CRequest[0] = 3;               // Message size
  MSMMUX_I2CRequest[1] = address; // I2C Address
  MSMMUX_I2CRequest[2] = MSMMUX_REG_CMD;
  MSMMUX_I2CRequest[3] = command;

  return writeI2C(link, MSMMUX_I2CRequest);
}


/**
 * Configure the internal PID controller.  Tweaking these values will change the
 * behaviour of the motors, how they approach their target, how they maintain speed, etc.
 * These settings do not persist, disconnecting the MUX will reset these values to their
 * defaults.  These settings are MUX-wide, so will apply to how BOTH motors are controlled.
 *
 * Please refer to the User Guide for more detailed information on how these parameters
 * should be used.
 *
 * @param link the MMUX port number
 * @param kpTacho Kp for Tachometer Position Control
 * @param kiTacho Ki for Tachometer Position Control
 * @param kdTacho Kd for Tachometer Position Control
 * @param kpSpeed Kp for Speed Control
 * @param kiSpeed Ki for Speed Control
 * @param kdSpeed Kd for Speed Control
 * @param passCount Encoder count tolerance when motor is moving
 * @param tolerance Encoder count tolerance when motor is getting close to target
 * @param address I2C address of the sensor (optional)
 * @return true if no error occured, false if it did
 */
bool MSMMUXsetPID(tSensors link, unsigned int kpTacho, unsigned int kiTacho, unsigned int kdTacho, unsigned int kpSpeed, unsigned int kiSpeed, unsigned int kdSpeed, ubyte passCount, ubyte tolerance, ubyte address) {
  memset(MSMMUX_I2CRequest, 0, sizeof(tByteArray));

  MSMMUX_I2CRequest[0] = 16;               // Message size
  MSMMUX_I2CRequest[1] = address; // I2C Address
  MSMMUX_I2CRequest[2] = MSMMUX_KP_TACHO;
  MSMMUX_I2CRequest[3] = kpTacho & 0xFF;
  MSMMUX_I2CRequest[4] = (kpTacho >> 8) & 0xFF;
  MSMMUX_I2CRequest[5] = kiTacho & 0xFF;
  MSMMUX_I2CRequest[6] = (kiTacho >> 8) & 0xFF;
  MSMMUX_I2CRequest[7] = kdTacho & 0xFF;
  MSMMUX_I2CRequest[8] = (kdTacho >> 8) & 0xFF;
  MSMMUX_I2CRequest[9] = kpSpeed & 0xFF;
  MSMMUX_I2CRequest[10] = (kpSpeed >> 8) & 0xFF;
  MSMMUX_I2CRequest[11] = kiSpeed & 0xFF;
  MSMMUX_I2CRequest[12] = (kiSpeed >> 8) & 0xFF;
  MSMMUX_I2CRequest[13] = kdSpeed & 0xFF;
  MSMMUX_I2CRequest[14] = (kdSpeed >> 8) & 0xFF;
  MSMMUX_I2CRequest[15] = passCount;
  MSMMUX_I2CRequest[16] = tolerance;

  return writeI2C(link, MSMMUX_I2CRequest);
}


/**
 * Run motor with specified speed.
 *
 * @param muxmotor the motor-MUX motor
 * @param power power the amount of power to apply to the motor, value between -100 and +100
 * @param address I2C address of the sensor (optional)
 * @return true if no error occured, false if it did
 */
bool MSMMotor(tMUXmotor muxmotor, byte power, ubyte address) {
  ubyte commandA = 0;
  commandA += (mmuxData[SPORT(muxmotor)].pidcontrol[MPORT(muxmotor)]) ? MSMMUX_CMD_SPEED : 0;
  commandA += (mmuxData[SPORT(muxmotor)].ramping[MPORT(muxmotor)] != MSMMUX_RAMP_NONE) ? MSMMUX_CMD_RAMP : 0;
  commandA += (mmuxData[SPORT(muxmotor)].brake[MPORT(muxmotor)]) ? MSMMUX_CMD_BRK : 0;
  commandA += (mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)] == MSMMUX_ROT_DEGREES) ? MSMMUX_CMD_TACHO : 0;
  commandA += (mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)] == MSMMUX_ROT_SECONDS) ? MSMMUX_CMD_TIME : 0;
  commandA += (mmuxData[SPORT(muxmotor)].relTarget[MPORT(muxmotor)]) ? MSMMUX_CMD_RELATIVE : 0;
  commandA += MSMMUX_CMD_GO;

  switch (mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)]) {
    case MSMMUX_ROT_UNLIMITED: return MSMMUXsendCommand((tSensors)SPORT(muxmotor), (ubyte)MPORT(muxmotor), 0, power, 0, commandA, address);
    case MSMMUX_ROT_DEGREES:   return MSMMUXsendCommand((tSensors)SPORT(muxmotor), (ubyte)MPORT(muxmotor), mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)], power, 0, commandA, address);
    case MSMMUX_ROT_SECONDS:   return MSMMUXsendCommand((tSensors)SPORT(muxmotor), (ubyte)MPORT(muxmotor), 0, power, mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)], commandA, address);
  }
  return true;
}


/**
 * Stop the motor. Uses the brake method specified with MSMMotorSetBrake or MSMMotorSetFloat.
 * The default is to use braking.
 *
 * @param muxmotor the motor-MUX motor
 * @param address I2C address of the sensor (optional)
 * @return true if no error occured, false if it did
 */
bool MSMotorStop(tMUXmotor muxmotor, ubyte address) {
  if (MPORT(muxmotor) == 0)
    return MSMotorStop(muxmotor, mmuxData[SPORT(muxmotor)].brake[MPORT(muxmotor)], address);
  else if (MPORT(muxmotor) == 1)
    return MSMotorStop(muxmotor, mmuxData[SPORT(muxmotor)].brake[MPORT(muxmotor)], address);
  return true;
}


/**
 * Stop the motor. This function overrides the preconfigured braking method.
 *
 * @param muxmotor the motor-MUX motor
 * @param brake when set to true: use brake, false: use float
 * @param address I2C address of the sensor (optional)
 * @return true if no error occured, false if it did
 */
bool MSMotorStop(tMUXmotor muxmotor, bool brake, ubyte address) {
  if (MPORT(muxmotor) == 0)
    return MSMMUXsendCommand((tSensors)SPORT(muxmotor), brake ? MSMMUX_CMD_BRAKE_MOT1 : MSMMUX_CMD_FLOAT_MOT1, address);
  else if (MPORT(muxmotor) == 1)
    return MSMMUXsendCommand((tSensors)SPORT(muxmotor), brake ? MSMMUX_CMD_BRAKE_MOT2 : MSMMUX_CMD_FLOAT_MOT2, address);
  return true;
}


/**
 * Set rotation target for specified mux motor.
 *
 * @param muxmotor the motor-MUX motor
 * @param target the rotation target value
 * @return true if no error occured, false if it did
 */
void MSMMotorSetRotationTarget(tMUXmotor muxmotor, long target) {
  mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)] = target * 360;
  mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)] = MSMMUX_ROT_DEGREES;
}


/**
 * Set time target for specified mux motor. Seconds can be specified in
 * increments of 1 second with an upper limit of 255 seconds.
 *
 * @param muxmotor the motor-MUX motor
 * @param target the time target value in seconds [1-255]
 * @return true if no error occured, false if it did
 */
void MSMMotorSetTimeTarget(tMUXmotor muxmotor, int target) {
  mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)] = target;
  mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)] = MSMMUX_ROT_SECONDS;
}


/**
 * Set encoder target for specified mux motor.  Target is relative to current position.
 *
 * @param muxmotor the motor-MUX motor
 * @param target the encoder target value in degrees.
 * @return true if no error occured, false if it did
 */
void MSMMotorSetEncoderTarget(tMUXmotor muxmotor, long target) {
  mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)] = target;
  mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)] = MSMMUX_ROT_DEGREES;
  mmuxData[SPORT(muxmotor)].relTarget[MPORT(muxmotor)] = true;
}


/**
 * Set encoder target for specified mux motor.
 *
 * @param muxmotor the motor-MUX motor
 * @param target the encoder target value in degrees.
 * @param relative specified target is relative to current position.
 * @return true if no error occured, false if it did
 */
void MSMMotorSetEncoderTarget(tMUXmotor muxmotor, long target, bool relative) {
  mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)] = target;
  mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)] = MSMMUX_ROT_DEGREES;
  mmuxData[SPORT(muxmotor)].relTarget[MPORT(muxmotor)] = relative;
}


/**
 * Fetch the current encoder value for specified motor channel
 *
 * @param muxmotor the motor-MUX motor
 * @param address I2C address of the sensor (optional)
 * @return the current value of the encoder
 */
long MSMMotorEncoder(tMUXmotor muxmotor, ubyte address) {
  long result;

  memset(MSMMUX_I2CRequest, 0, sizeof(tByteArray));

  MSMMUX_I2CRequest[0] = 2;               // Message size
  MSMMUX_I2CRequest[1] = address; // I2C Address

  switch ((byte)MPORT(muxmotor)) {
    case 0: MSMMUX_I2CRequest[2] = MSMMUX_TACHO_MOT1; break;
    case 1: MSMMUX_I2CRequest[2] = MSMMUX_TACHO_MOT2; break;
  }

  if (!writeI2C((tSensors)SPORT(muxmotor), MSMMUX_I2CRequest,  MSMMUX_I2CReply, 4))
    return 0;

  result = MSMMUX_I2CReply[0] + (MSMMUX_I2CReply[1]<<8) + (MSMMUX_I2CReply[2]<<16) + (MSMMUX_I2CReply[3]<<24);

  return result;
}


/**
 * Reset target encoder for specified motor channel, use only at
 * the start of your program.  If you are using the standard NXT wheels
 * you will not run into problems with a wrap-around for the first 500kms
 * or so.
 *
 * @param muxmotor the motor-MUX motor
 * @param address I2C address of the sensor (optional)
 * @return true if no error occured, false if it did
 */

bool MSMMotorEncoderReset(tMUXmotor muxmotor, ubyte address) {
  switch((byte)MPORT(muxmotor)) {
    case 0: return MSMMUXsendCommand((tSensors)SPORT(muxmotor), MSMMUX_CMD_RESET_MOT1, address); break;
    case 1: return MSMMUXsendCommand((tSensors)SPORT(muxmotor), MSMMUX_CMD_RESET_MOT2, address); break;
  }
  return false;
}


/**
 * Reset all encoders on the specified motor-MUX. Use only at
 * the start of your program.  If you are using the standard NXT wheels
 * you will not run into problems with a wrap-around for the first 500kms
 * or so.
 *
 * @param link the MMUX port number
 * @param address I2C address of the sensor (optional)
 * @return true if no error occured, false if it did
 */
bool MSMMotorEncoderResetAll(tSensors link, ubyte address) {
  return MSMMUXsendCommand(link, MSMMUX_CMD_RESET_ALL, address);
}


/**
 * Check if the specified motor is running or not.
 *
 * @param muxmotor the motor-MUX motor
 * @param address I2C address of the sensor (optional)
 * @return true if the motor is still running, false if it's idle
 */
bool MSMMotorBusy(tMUXmotor muxmotor, ubyte address) {
  ubyte status = 0;
  ubyte commandA = 0;

  // Fetch the last sent commandA
  memset(MSMMUX_I2CRequest, 0, sizeof(tByteArray));

  MSMMUX_I2CRequest[0] = 2;               // Message size
  MSMMUX_I2CRequest[1] = address; // I2C Address
  MSMMUX_I2CRequest[2] = MSMMUX_MOT_OFFSET + (MPORT(muxmotor) * MSMMUX_ENTRY_SIZE) + MSMMUX_CMD_A;

  if (!writeI2C((tSensors)SPORT(muxmotor), MSMMUX_I2CRequest, MSMMUX_I2CReply, 1))
    return false;

  commandA = MSMMUX_I2CReply[0];

  // If commandA is 0 then the motor can't be busy.
  if (commandA == 0)
    return false;

  if (!MSMMUXreadStatus(muxmotor, status, address))
    return false;

  if ((commandA & MSMMUX_ROT_UNLIMITED) == MSMMUX_ROT_UNLIMITED)
    return ((status & MSMMUX_STAT_POWERED) != 0);
  else if ((commandA & MSMMUX_ROT_DEGREES) == MSMMUX_ROT_DEGREES)
    return ((status & MSMMUX_STAT_POS_CTRL) != 0);
  else if ((commandA & MSMMUX_ROT_SECONDS) == MSMMUX_ROT_SECONDS)
    return ((status & MSMMUX_STAT_TIMED) != 0);

  return false;
}


/**
 * Check if the specified motor is running or not.
 *
 * @param muxmotor the motor-MUX motor
 * @param address I2C address of the sensor (optional)
 * @return true if the motor is still running, false if it's idle
 */
bool MSMMotorStalled(tMUXmotor muxmotor, ubyte address) {
  ubyte status = 0;
  if (!MSMMUXreadStatus(muxmotor, status, address))
    return false;

  return ((status & MSMMUX_STAT_STALLED) != 0);
}


/**
 * Set the stopping method for the specified motor to brake.
 *
 * @param muxmotor the motor-MUX motor
 */
void MSMMotorSetBrake(tMUXmotor muxmotor) {
  mmuxData[SPORT(muxmotor)].brake[MPORT(muxmotor)] = true;
}


/**
 * Set the stopping method for the specified motor to float.
 *
 * @param muxmotor the motor-MUX motor
 */
void MSMMotorSetFloat(tMUXmotor muxmotor) {
  mmuxData[SPORT(muxmotor)].brake[MPORT(muxmotor)] = false;
}


/**
 * Set the motor speed control for the specified motor.
 *
 * @param muxmotor the motor-MUX motor
 * @param constspeed use speed control to ensure motor speed stays constant under varying load
 */
void MSMMotorSetSpeedCtrl(tMUXmotor muxmotor, bool constspeed) {
  mmuxData[SPORT(muxmotor)].pidcontrol[MPORT(muxmotor)] = true;
}


/**
 * Set the ramping control for the specified motor.
 *
 * @param muxmotor the motor-MUX motor
 * @param ramping use ramping for starting and stopping the motor
 */
void MSMMotorSetRamping(tMUXmotor muxmotor, bool ramping) {
  mmuxData[SPORT(muxmotor)].ramping[MPORT(muxmotor)] = (ramping) ? MSMMUX_RAMP_UP_DOWN : MSMMUX_RAMP_NONE;
}

#endif //  __MSMMUX_H__

/*
 * $Id: mindsensors-motormux.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
