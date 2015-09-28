/*!@addtogroup mindsensors
 * @{
 * @defgroup msmtrmx RCX Motor MUX
 * RCX Motor MUX
 * @{
 */

/*
 * $Id: mindsensors-rcxmotormux.h 133 2013-03-10 15:15:38Z xander $
 */

/** \file mindsensors-rcxmotormux.h
 * \brief RobotC Mindsensors RCX Motor MUX Driver
 *
 * mindsensors-rcxmotormux.h provides an API for the Mindsensors RCX Motor MUX Driver.
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Daniel Playfair Cal (daniel.playfair.cal_at_gmail.com)
 * \date 2011-09-20
 * \version 2
 * \example mindsensors-rcxmotormux-test1.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

/*! i2c address/registers */
#define MSMTRMX_I2C_ADDR       0xB4      /*!< MSMTRMX I2C device address */
#define MSMTRMX_MOTOR_REG     0x42      /*!< Motor address */

/*! motor settings struct, each motor can be set to one of these modes */
typedef enum {
  MSMTRMX_MODE_FLOAT = 0,
  MSMTRMX_MODE_FORWARD = 1,
  MSMTRMX_MODE_REVERSE = 2,
  MSMTRMX_MODE_BRAKE = 3
} tMSMTRMXSettings;

/*! motors struct, these are the four motor channels on the MUX */
typedef enum {
  MSMTRMX_M1 = 0,
  MSMTRMX_M2 = 1,
  MSMTRMX_M3 = 2,
  MSMTRMX_M4 = 3
} tMSMTRMXMotors;

tByteArray MSMTRMX_I2CMessage;       /*!< Array to hold I2C command data */

/*! Function prototypes */
bool MSMTRMX_Control(tSensors link, tMSMTRMXMotors channel, int power, ubyte address = MSMTRMX_I2C_ADDR);
bool MSMTRMX_Brake(tSensors link, tMSMTRMXMotors channel, unsigned byte brakeForce, ubyte address = MSMTRMX_I2C_ADDR);


/**
 * This function sets the specified motor to the given power level,
 * with the motor floated at speed zero. The controller floats during
 * inactive PWM periods.
 * @param link port number
 * @param channel motor number
 * @param power speed to set the motor to (-255 to +255)
 * @param address the I2C address to use, optional, defaults to 0xB4
 * @return true if message is sent successfully
 */
bool MSMTRMX_Control(tSensors link, tMSMTRMXMotors channel, int power, ubyte address) {
  tMSMTRMXSettings dir;

  if (power == 0) {
    dir = MSMTRMX_MODE_BRAKE;
    power = 0;
  }
  else if (power < 0) {
    dir = MSMTRMX_MODE_REVERSE;
    power = -power;
  }
  else if (power > 0) {
    dir = MSMTRMX_MODE_FORWARD;
  }

  memset(MSMTRMX_I2CMessage, 0, sizeof(tByteArray));

  MSMTRMX_I2CMessage[0] = 4;
  MSMTRMX_I2CMessage[1] = address;
  MSMTRMX_I2CMessage[2] = MSMTRMX_MOTOR_REG + channel * 2;
  MSMTRMX_I2CMessage[3] = (ubyte) dir;
  MSMTRMX_I2CMessage[4] = power;

  if (!writeI2C(link, MSMTRMX_I2CMessage)) {
    return false;
  }

  return true;
}


/**
 * This function applies the specified braking power to the specified motor
 * @param link port number
 * @param channel motor number
 * @param brakeForce brake power to set the motor to (0-255, 0 == float)
 * @param address the I2C address to use, optional, defaults to 0xB4
 * @return true if message is sent successfully
 */
bool MSMTRMX_Brake(tSensors link, tMSMTRMXMotors channel, unsigned byte brakeForce, ubyte address) {

  memset(MSMTRMX_I2CMessage, 0, sizeof(tByteArray));

  MSMTRMX_I2CMessage[0] = 4;
  MSMTRMX_I2CMessage[1] = address;
  MSMTRMX_I2CMessage[2] = MSMTRMX_MOTOR_REG + channel * 2;
  MSMTRMX_I2CMessage[3] = (ubyte) MSMTRMX_MODE_BRAKE;
  MSMTRMX_I2CMessage[4] = brakeForce;

  if (!writeI2C(link, MSMTRMX_I2CMessage)) {
    return false;
  }

  return true;
}

/*
 * $Id: mindsensors-rcxmotormux.h 133 2013-03-10 15:15:38Z xander $
 */

/* @} */
/* @} */
