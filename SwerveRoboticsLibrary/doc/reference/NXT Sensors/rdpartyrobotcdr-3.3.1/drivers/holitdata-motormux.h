/*!@addtogroup other
 * @{
 * @defgroup holitdata HDS Motor MUX
 * Holit Data Systems Motor MUX
 * @{
 */

/*
 * $Id: holitdata-motormux.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HDMMUX_H__
#define __HDMMUX_H__
/** \file holitdata-motormux.h
 * \brief Holit Data Systems Motor MUX driver
 *
 * holitdata-motormux.h provides an API for the Holit Data Systems Motor MUX.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Replaced array structs with typedefs\n
 *        Uses new split off include file MMUX-common.h
 *
 * Credits:
 * - Big thanks to Holit Data Systems for providing me with the hardware necessary to write and test this.
 * - Thanks to Cheh from Holit Data Systems for the extensive testing and subsequent bug reports :)
 *
 * TODO:
 * - Add support for multiple MUXes per sensor port
 * - Ramping up and down of motors
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 20 February 2011
 * \version 0.2
 * \example holitdata-motormux-test1.c
 * \example holitdata-motormux-test2.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#ifndef __MMUX_H__
#include "common-MMUX.h"
#endif

// device address - byte 0
#define HDMMUX_I2C_ADDR         0x02  /*!< HDMMUX I2C device address */

// Command type - byte 1
#define HDMMUX_CMD_MOTOR        0x01
#define HDMMUX_CMD_ADDRCHNG     0x02
#define HDMMUX_CMD_RST_TACH_A   0x03
#define HDMMUX_CMD_RST_TACH_B   0x04
#define HDMMUX_CMD_RST_TACH_C   0x05

// motor indicator - byte 2
#define HDMMUX_MOTOR_A          0x01
#define HDMMUX_MOTOR_B          0x02
#define HDMMUX_MOTOR_C          0x03

#define HDMMUX_MOTOR_OTHER      0x04
#define HDMMUX_MOTOR_RIGHT      0x02
#define HDMMUX_MOTOR_LEFT       0x00

// rotation parameters - byte 3
#define HDMMUX_ROT_FORWARD      (0x01 << 6)
#define HDMMUX_ROT_REVERSE      (0x02 << 6)
#define HDMMUX_ROT_STOP         (0x03 << 6)

#define HDMMUX_ROT_CONSTSPEED   (0x01 << 4)
#define HDMMUX_ROT_RAMPUP       (0x02 << 4)
#define HDMMUX_ROT_RAMPDOWN     (0x03 << 4)

#define HDMMUX_ROT_UNLIMITED    (0x00 << 2)
#define HDMMUX_ROT_DEGREES      (0x01 << 2)
#define HDMMUX_ROT_ROTATIONS    (0x02 << 2)
#define HDMMUX_ROT_SECONDS      (0x03 << 2)

#define HDMMUX_ROT_POWERCONTROL (0x01 << 1)

#define HDMMUX_ROT_BRAKE        0x01
#define HDMMUX_ROT_FLOAT        0x00

tByteArray HDMMUX_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray HDMMUX_I2CReply;      /*!< Array to hold I2C reply data */

// Function prototypes
void HDMMUXinit();
bool HDMMUXreadStatus(tSensors link, ubyte &motorStatus, long &tachoA, long &tachoB, long &tachoC);
bool HDMMUXsendCommand(tSensors link, ubyte mode, ubyte channel, ubyte rotparams, long duration, byte power, byte steering);
bool HDMMotor(tMUXmotor muxmotor, byte power);
bool HDMotorStop(tMUXmotor muxmotor);
bool HDMotorStop(tMUXmotor muxmotor, bool brake);
void HDMMotorSetRotationTarget(tMUXmotor muxmotor, float rottarget);
void HDMMotorSetTimeTarget(tMUXmotor muxmotor, float timetarget);
void HDMMotorSetEncoderTarget(tMUXmotor muxmotor, long enctarget);
long HDMMotorEncoder(tMUXmotor muxmotor);
bool HDMMotorEncoderReset(tMUXmotor muxmotor);
bool HDMMotorEncoderResetAll(tSensors link);
bool HDMMotorBusy(tMUXmotor muxmotor);
void HDMMotorSetBrake(tMUXmotor muxmotor);
void HDMMotorSetFloat(tMUXmotor muxmotor);
void HDMMotorSetSpeedCtrl(tMUXmotor muxmotor, bool constspeed);
void HDMMotorSetRamping(tMUXmotor muxmotor, ubyte ramping);

/*
 * Initialise the mmuxData array needed for keeping track of motor settings
 */
void HDMMUXinit(){
  for (int i = 0; i < 4; i++) {
    memset(mmuxData[i].runToTarget[0], false, 4);
    memset(mmuxData[i].brake[0], true, 4);
    memset(mmuxData[i].pidcontrol[0], true, 4);
    memset(mmuxData[i].target[0], 0, 4*4);
    memset(mmuxData[i].ramping[0], HDMMUX_ROT_CONSTSPEED, 4);
    memset(mmuxData[i].targetUnit[0], HDMMUX_ROT_UNLIMITED, 4);
    mmuxData[i].initialised = true;
  }
}


/**
 * Read the status of the motors and tacho counts of the MMUX
 *
 * motorStatus is made of 3 bits, 1: motor is running, 0: motor is idle\n
 * bit 0: motor A\n
 * bit 1: motor B\n
 * bit 2: motor C\n
 *
 * @param link the MMUX port number
 * @param motorStatus status of the motors
 * @param tachoA Tacho count for motor A
 * @param tachoB Tacho count for motor B
 * @param tachoC Tacho count for motor C
 * @return true if no error occured, false if it did
 */
bool HDMMUXreadStatus(tSensors link, ubyte &motorStatus, long &tachoA, long &tachoB, long &tachoC) {
  memset(HDMMUX_I2CRequest, 0, sizeof(tByteArray));

  HDMMUX_I2CRequest[0]  = 10;               // Message size
  HDMMUX_I2CRequest[1]  = HDMMUX_I2C_ADDR; // I2C Address

  if (!writeI2C(link, HDMMUX_I2CRequest, HDMMUX_I2CReply, 13))
    return false;

  motorStatus = HDMMUX_I2CReply[0];

  // Assemble and assign the encoder values
  tachoA = (HDMMUX_I2CReply[1] << 24) + (HDMMUX_I2CReply[2] << 16) + (HDMMUX_I2CReply[3] << 8) + (HDMMUX_I2CReply[4] << 0);
  tachoB = (HDMMUX_I2CReply[5] << 24) + (HDMMUX_I2CReply[6] << 16) + (HDMMUX_I2CReply[7] << 8) + (HDMMUX_I2CReply[8] << 0);
  tachoC = (HDMMUX_I2CReply[9] << 24) + (HDMMUX_I2CReply[10] << 16) + (HDMMUX_I2CReply[11] << 8) + (HDMMUX_I2CReply[12] << 0);

  return true;
}


/**
 * Send a command to the MMUX.
 *
 * Note: this is an internal function and shouldn't be used directly
 * @param link the MMUX port number
 * @param mode the mode the MMX should operate in, controlling motors, resetting tachos or settings new address
 * @param channel the motors the command applies to
 * @param rotparams the additional parameters that make up the command
 * @param duration the number of units (can be seconds, rotations or degrees) the command should be run for
 * @param power the amount of power to be applied to the motor(s)
 * @param steering used for syncronised movement to control the amount of steering
 * @return true if no error occured, false if it did
 */
bool HDMMUXsendCommand(tSensors link, ubyte mode, ubyte channel, ubyte rotparams, long duration, byte power, byte steering) {
  memset(HDMMUX_I2CRequest, 0, sizeof(tByteArray));

  HDMMUX_I2CRequest[0]  = 10;               // Message size
  HDMMUX_I2CRequest[1]  = HDMMUX_I2C_ADDR; // I2C Address
  HDMMUX_I2CRequest[2]  = mode;
  HDMMUX_I2CRequest[3]  = channel;
  HDMMUX_I2CRequest[4]  = rotparams;
  HDMMUX_I2CRequest[5]  = (duration >> 24) & 0xFF;
  HDMMUX_I2CRequest[6]  = (duration >> 16) & 0xFF;
  HDMMUX_I2CRequest[7]  = (duration >>  8) & 0xFF;
  HDMMUX_I2CRequest[8]  = (duration >>  0) & 0xFF;
  HDMMUX_I2CRequest[9]  = power;
  HDMMUX_I2CRequest[10] = (byte)(steering & 0xFF);

  return writeI2C(link, HDMMUX_I2CRequest);
}


/**
 * Run motor with specified speed.
 *
 * @param muxmotor the motor-MUX motor
 * @param power power the amount of power to apply to the motor, value between -100 and +100
 * @return true if no error occured, false if it did
 */
bool HDMMotor(tMUXmotor muxmotor, byte power) {
  ubyte command = 0;
  bool retval = true;
  long target = mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)];

  command |= (mmuxData[SPORT(muxmotor)].brake[MPORT(muxmotor)]) ? HDMMUX_ROT_BRAKE : HDMMUX_ROT_FLOAT;
  command |= mmuxData[SPORT(muxmotor)].ramping[MPORT(muxmotor)];
  command |= (mmuxData[SPORT(muxmotor)].pidcontrol[MPORT(muxmotor)]) ? HDMMUX_ROT_POWERCONTROL : 0;
  command |= (power > 0) ? HDMMUX_ROT_FORWARD : HDMMUX_ROT_REVERSE;
  command |= mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)];

  retval = HDMMUXsendCommand((tSensors)SPORT(muxmotor), HDMMUX_CMD_MOTOR, (ubyte)MPORT(muxmotor) + 1, command, target, abs(power), 0);

  // Reset the data
  mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)] = HDMMUX_ROT_UNLIMITED;
  mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)] = 0;

  return retval;
}


/**
 * Stop the motor. Uses the brake method specified with HDMMotorSetBrake or HDMMotorSetFloat.
 * The default is to use braking.
 *
 * @param muxmotor the motor-MUX motor
 * @return true if no error occured, false if it did
 */
bool HDMotorStop(tMUXmotor muxmotor) {
  ubyte command = 0;
  bool retval = true;

  command |= (mmuxData[SPORT(muxmotor)].brake[MPORT(muxmotor)]) ? HDMMUX_ROT_BRAKE : HDMMUX_ROT_FLOAT;
  command |= HDMMUX_ROT_STOP;

  retval = HDMMUXsendCommand((tSensors)SPORT(muxmotor), HDMMUX_CMD_MOTOR, (ubyte)MPORT(muxmotor) + 1, command, 0, 0, 0);

  // Reset the data
  mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)] = HDMMUX_ROT_UNLIMITED;
  mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)] = 0;

  return retval;
}


/**
 * Stop the motor. This function overrides the preconfigured braking method.
 *
 * @param muxmotor the motor-MUX motor
 * @param brake when set to true: use brake, false: use float
 * @return true if no error occured, false if it did
 */
bool HDMotorStop(tMUXmotor muxmotor, bool brake) {
  ubyte command = 0;
  bool retval = true;

  command |= (brake) ? HDMMUX_ROT_BRAKE : HDMMUX_ROT_FLOAT;
  command |= HDMMUX_ROT_STOP;

  retval = HDMMUXsendCommand((tSensors)SPORT(muxmotor), HDMMUX_CMD_MOTOR, (ubyte)MPORT(muxmotor) + 1, command, 0, 0, 0);

  // Reset the data
  mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)] = HDMMUX_ROT_UNLIMITED;
  mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)] = 0;

  return retval;
}


/**
 * Set rotation target for specified mux motor. Rotations can be specified in
 * increments of 0.01.  To rotate the motor 10.54 degrees, specify a value of 10.54.
 *
 * @param muxmotor the motor-MUX motor
 * @param rottarget the rotation target value
 * @return true if no error occured, false if it did
 */
void HDMMotorSetRotationTarget(tMUXmotor muxmotor, float rottarget) {
  mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)] = (long)(rottarget * 100);
  mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)] = HDMMUX_ROT_ROTATIONS;
}


/**
 * Set time target for specified mux motor. Seconds can be specified in
 * increments of 0.01.  To rotate the motor for 10.21 seconds, specify a value of 10.21.
 *
 * @param muxmotor the motor-MUX motor
 * @param timetarget the time target value in seconds.
 * @return true if no error occured, false if it did
 */
void HDMMotorSetTimeTarget(tMUXmotor muxmotor, float timetarget) {
  mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)] = (long)(timetarget * 100);
  mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)] = HDMMUX_ROT_SECONDS;
}


/**
 * Set encoder target for specified mux motor.
 *
 * @param muxmotor the motor-MUX motor
 * @param enctarget the encoder target value in degrees.
 * @return true if no error occured, false if it did
 */
void HDMMotorSetEncoderTarget(tMUXmotor muxmotor, long enctarget) {
  mmuxData[SPORT(muxmotor)].target[MPORT(muxmotor)] = enctarget;
  mmuxData[SPORT(muxmotor)].targetUnit[MPORT(muxmotor)] = HDMMUX_ROT_DEGREES;
}


/**
 * Fetch the current encoder value for specified motor channel
 *
 * @param muxmotor the motor-MUX motor
 * @return the current value of the encoder
 */
long HDMMotorEncoder(tMUXmotor muxmotor) {
  long encA  = 0;
  long encB  = 0;
  long encC  = 0;
  ubyte dummy = 0;

  HDMMUXreadStatus((tSensors)SPORT(muxmotor), dummy, encA, encB, encC);

  switch ((ubyte)MPORT(muxmotor)) {
    case 0: return encA;
    case 1: return encB;
    case 2: return encC;
  }

  return 0;
}


/**
 * Reset target encoder for specified motor channel, use only at
 * the start of your program.  If you are using the standard NXT wheels
 * you will not run into problems with a wrap-around for the first 500kms
 * or so.
 *
 * @param muxmotor the motor-MUX motor
 * @return true if no error occured, false if it did
 */
bool HDMMotorEncoderReset(tMUXmotor muxmotor) {
  ubyte mode = 0;

  switch ((ubyte)MPORT(muxmotor)) {
    case 0: mode = HDMMUX_CMD_RST_TACH_A; break;
    case 1: mode = HDMMUX_CMD_RST_TACH_B; break;
    case 2: mode = HDMMUX_CMD_RST_TACH_C; break;
  }

  return HDMMUXsendCommand((tSensors)SPORT(muxmotor), mode, 0, 0, 0, 0, 0);
}


/**
 * Reset all encoders on the specified motor-MUX. Use only at
 * the start of your program.  If you are using the standard NXT wheels
 * you will not run into problems with a wrap-around for the first 500kms
 * or so.
 *
 * @param link the MMUX port number
 * @return true if no error occured, false if it did
 */
bool HDMMotorEncoderResetAll(tSensors link) {
  if (!HDMMUXsendCommand(link, HDMMUX_CMD_RST_TACH_A, 0, 0, 0, 0, 0))
    return false;

  if (!HDMMUXsendCommand(link, HDMMUX_CMD_RST_TACH_B, 0, 0, 0, 0, 0))
    return false;

  if (!HDMMUXsendCommand(link, HDMMUX_CMD_RST_TACH_C, 0, 0, 0, 0, 0))
    return false;

  return true;
}


/**
 * Check if the specified motor is running or not.
 *
 * @param muxmotor the motor-MUX motor
 * @return true if the motor is still running, false if it's idle
 */
bool HDMMotorBusy(tMUXmotor muxmotor) {
  long dummy  = 0;
  ubyte motorStatus = 0;

  HDMMUXreadStatus((tSensors)SPORT(muxmotor), motorStatus, dummy, dummy, dummy);

  switch ((ubyte)MPORT(muxmotor)) {
    case 0: return ((motorStatus & 0x01) == 0x01);
    case 1: return ((motorStatus & 0x02) == 0x02);
    case 2: return ((motorStatus & 0x04) == 0x04);
  }

  return true;
}


/**
 * Set the stopping method for the specified motor to brake.
 *
 * @param muxmotor the motor-MUX motor
 */
void HDMMotorSetBrake(tMUXmotor muxmotor) {
  mmuxData[SPORT(muxmotor)].brake[MPORT(muxmotor)] = true;
}


/**
 * Set the stopping method for the specified motor to float.
 *
 * @param muxmotor the motor-MUX motor
 */
void HDMMotorSetFloat(tMUXmotor muxmotor) {
  mmuxData[SPORT(muxmotor)].brake[MPORT(muxmotor)] = false;
}


/**
 * Set the motor speed control for the specified motor.
 *
 * @param muxmotor the motor-MUX motor
 * @param constspeed whether or not to use speed control
 */
void HDMMotorSetSpeedCtrl(tMUXmotor muxmotor, bool constspeed) {
  mmuxData[SPORT(muxmotor)].pidcontrol[MPORT(muxmotor)] = true;
}


/**
 * Set the motor ramping type the specified motor.
 * ramping can be one of\n
 * - HDMMUX_ROT_CONSTSPEED
 * - HDMMUX_ROT_RAMPUP
 * - HDMMUX_ROT_RAMPDOWN
 *
 * @param muxmotor the motor-MUX motor
 * @param ramping the type of ramping to be used
 */
void HDMMotorSetRamping(tMUXmotor muxmotor, ubyte ramping) {
  mmuxData[SPORT(muxmotor)].ramping[MPORT(muxmotor)] = ramping;
}

#endif //  __HDMMUX_H__

/*
 * $Id: holitdata-motormux.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
