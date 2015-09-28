/*!@addtogroup mindsensors
 * @{
 * @defgroup pfmate PFMate Sensor
 * PFMate Sensor
 * @{
 */

/*
 * $Id: mindsensors-pfmate.h 133 2013-03-10 15:15:38Z xander $
 */

/** \file mindsensors-pfmate.h
 * \brief Mindsensors PFMate Sensor driver
 *
 * mindsensors-pfmate.h provides an API for the Mindsensors PFMate Sensor driver
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
 * \date 22 July 2009
 * \version 0.1
 * \example mindsensors-pfmate-test1.c
 */

#pragma systemFile

#ifndef __MSPFM_H__
#define __MSPFM_H__

#ifndef __COMMON_H__
#include "common.h"
#endif

#define MSPFM_I2C_ADDR    0x48      /*!< I2C address for sensor */
#define MSPFM_CMD         0x41      /*!< Command register */
#define MSPFM_IRCHAN      0x42      /*!< IR Channel selection register */
#define MSPFM_MSELECT     0x43      /*!< Motor selection register */
#define MSPFM_MOTOPA      0x44      /*!< Motor A operation register */
#define MSPFM_MOTSPA      0x45      /*!< Motor A speed register */
#define MSPFM_MOTOPB      0x46      /*!< Motor B operation register */
#define MSPFM_MOTSPB      0x47      /*!< Motor B speed register */

#define MSPFM_GOCMD       0x47      /*!< "Go" command to start sending IR signals to PF receiver */

#define MSPFM_MOTORAB     0x00
#define MSPFM_MOTORA      0x01
#define MSPFM_MOTORB      0x02

#define MSPFM_FLOAT       0x00
#define MSPFM_FORWARD     0x01
#define MSPFM_REVERSE     0x02
#define MSPFM_BRAKE       0x03
#define MSPFM_NOOP        0x0F


bool MSPFMcontrolMotorA(tSensors link, byte chan, byte motor_op, byte motor_speed, ubyte address = MSPFM_I2C_ADDR);
bool MSPFMcontrolMotorB(tSensors link, byte chan, byte motor_op, byte motor_speed, ubyte address = MSPFM_I2C_ADDR);
bool MSPFMcontrolMotorAB(tSensors link, byte chan, byte motorA_op, byte motorA_speed, byte motorB_op, byte motorB_speed, ubyte address = MSPFM_I2C_ADDR);


tByteArray MSPFM_I2CRequest;       /*!< Array to hold I2C command data */


/**
 * Control motor A with the PFMate.
 *
 * @param link the PFMate port number
 * @param chan the channel of the IR receiver, value of 1-4
 * @param motor_op motor A operation, 0: float, 1: forward, 2: reverse, 3: brake
 * @param motor_speed the speed at which motor A should turn, value between 1-7
 * @param address the I2C address to use, optional, defaults to 0x48
 * @return true if no error occured, false if it did
 */
bool MSPFMcontrolMotorA(tSensors link, byte chan, byte motor_op, byte motor_speed, ubyte address) {
  return MSPFMcontrolMotorAB(link, chan, motor_op, motor_speed, MSPFM_NOOP, 0);
}


/**
 * Control motor B with the PFMate.
 *
 * @param link the PFMate port number
 * @param chan the channel of the IR receiver, value of 1-4
 * @param motor_op motor B operation, 0: float, 1: forward, 2: reverse, 3: brake
 * @param motor_speed the speed at which motor B should turn, value between 1-7
 * @param address the I2C address to use, optional, defaults to 0x48
 * @return true if no error occured, false if it did
 */
bool MSPFMcontrolMotorB(tSensors link, byte chan, byte motor_op, byte motor_speed, ubyte address) {
  return MSPFMcontrolMotorAB(link, chan, MSPFM_NOOP, 0, motor_op, motor_speed);
}


/**
 * Control motors A and B with the PFMate.
 *
 * @param link the PFMate port number
 * @param chan the channel of the IR receiver, value of 1-4
 * @param motorA_op motor A operation, 0: float, 1: forward, 2: reverse, 3: brake
 * @param motorA_speed the speed at which motor A should turn, value between 1-7
 * @param motorB_op motor B operation, 0: float, 1: forward, 2: reverse, 3: brake
 * @param motorB_speed the speed at which motor B should turn, value between 1-7
 * @param address the I2C address to use, optional, defaults to 0x48
 * @return true if no error occured, false if it did
 */
bool MSPFMcontrolMotorAB(tSensors link, byte chan, byte motorA_op, byte motorA_speed, byte motorB_op, byte motorB_speed, ubyte address) {
  byte mselect = MSPFM_MOTORAB;

  if (motorA_op == MSPFM_NOOP)
    mselect = MSPFM_MOTORB;
  else if (motorB_op == MSPFM_NOOP)
    mselect = MSPFM_MOTORA;

  memset(MSPFM_I2CRequest, 0, sizeof(tByteArray));
  MSPFM_I2CRequest[0] = 8;
  MSPFM_I2CRequest[1] = address;
  MSPFM_I2CRequest[2] = MSPFM_IRCHAN;
  MSPFM_I2CRequest[3] = chan;
  MSPFM_I2CRequest[4] = mselect;
  MSPFM_I2CRequest[5] = motorA_op;
  MSPFM_I2CRequest[6] = motorA_speed;
  MSPFM_I2CRequest[7] = motorB_op;
  MSPFM_I2CRequest[8] = motorB_speed;

  if (!writeI2C(link, MSPFM_I2CRequest))
    return false;

  MSPFM_I2CRequest[0] = 3;
  MSPFM_I2CRequest[1] = address;
  MSPFM_I2CRequest[2] = MSPFM_CMD;
  MSPFM_I2CRequest[3] = MSPFM_GOCMD;

  return writeI2C(link, MSPFM_I2CRequest);
}


#endif // __MSPFM_H__

/*
 * $Id: mindsensors-pfmate.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
