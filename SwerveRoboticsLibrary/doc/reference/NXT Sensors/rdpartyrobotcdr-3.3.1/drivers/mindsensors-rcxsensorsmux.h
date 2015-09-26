/*!@addtogroup mindsensors
 * @{
 * @defgroup msrmux RCX Sensor MUX
 * RCX Sensor MUX
 * @{
 */

/*
 * $Id: mindsensors-rcxsensorsmux.h 133 2013-03-10 15:15:38Z xander $
 */

/** \file mindsensors-rcxsensorsmux.h
 * \brief Mindsensors MSRXMUX RCX Sensor MUX Sensor driver
 *
 * mindsensors-rcxsensorsmux.h provides an API for the Mindsensors MSRXMUX RCX Sensor MUX Sensor
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
 * \date 30 August 2009
 * \version 0.1
 * \example mindsensors-rcxsensorsmux-test1.c
 */

#pragma systemFile

#ifndef __MSRXMUX_H__
#define __MSRXMUX_H__

#ifndef __COMMON_H__
#include "common.h"
#endif

#define MSRXMUX_I2C_ADDR  0x7E      /*!< I2C address for sensor */
#define MSRXMUX_CHAN1     0xFE      /*!< Select MUX channel 1 */
#define MSRXMUX_CHAN2     0xFD      /*!< Select MUX channel 2 */
#define MSRXMUX_CHAN3     0xFB      /*!< Select MUX channel 3 */
#define MSRXMUX_CHAN4     0xF7      /*!< Select MUX channel 4 */
#define MSRXMUX_NONE      0xFF      /*!< Deselect all MUX channels */

tByteArray MSRXMUX_I2CRequest;       /*!< Array to hold I2C command data */

TSensorTypes RCXSensorTypes[4][4] = {{sensorNone, sensorNone, sensorNone, sensorNone},
                                     {sensorNone, sensorNone, sensorNone, sensorNone},
                                     {sensorNone, sensorNone, sensorNone, sensorNone},
                                     {sensorNone, sensorNone, sensorNone, sensorNone}};
TSensorModes RCXSensorModes[4][4] = {{modeRaw, modeRaw, modeRaw, modeRaw},
                                     {modeRaw, modeRaw, modeRaw, modeRaw},
                                     {modeRaw, modeRaw, modeRaw, modeRaw},
                                     {modeRaw, modeRaw, modeRaw, modeRaw}};
ubyte RCXSensorDelays[4][4] =        {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};

/**
 * Send a direct command to the MUX sensor
 *
 * Note: this is an internal command and should not be called directly.
 * @param link the PFMate port number
 * @param chan the channel to be configured
 * @param chantype the sensor type connected to the channel
 * @param chanmode the sensor mode of the sensor
 * @param delay the initialisation delay time
 * @return true if no error occured, false if it did
 */
void MSRXMUXsetupChan(tSensors link, ubyte chan, TSensorTypes chantype, TSensorModes chanmode, ubyte delay) {
  if (chan < 1 || chan > 4)
    return;

  RCXSensorTypes[link][chan-1] = chantype;
  RCXSensorModes[link][chan-1] = chanmode;
  RCXSensorDelays[link][chan-1] = delay;
}

int MSRXMUXreadChan(tSensors link, byte chan) {
  if (SensorType[link] != sensorI2CCustom9V) {
    SensorType[link] = sensorI2CCustom9V;
    wait1Msec(3);
  }

  MSRXMUX_I2CRequest[0] = 2;
  MSRXMUX_I2CRequest[1] = MSRXMUX_I2C_ADDR;
  switch(chan) {
    case 0: MSRXMUX_I2CRequest[2] =  MSRXMUX_NONE; break;
    case 1: MSRXMUX_I2CRequest[2] =  MSRXMUX_CHAN1; break;
    case 2: MSRXMUX_I2CRequest[2] =  MSRXMUX_CHAN2; break;
    case 3: MSRXMUX_I2CRequest[2] =  MSRXMUX_CHAN3; break;
    case 4: MSRXMUX_I2CRequest[2] =  MSRXMUX_CHAN4; break;
    default: MSRXMUX_I2CRequest[2] =  MSRXMUX_NONE;
  }

  if (!writeI2C(link, MSRXMUX_I2CRequest))
    return -1;

  wait10Msec(3+RCXSensorDelays[link][chan-1]);
  SensorType[link] = RCXSensorTypes[link][chan-1];
  SensorMode[link] = RCXSensorModes[link][chan-1];
  return(SensorValue[link]);
}

#endif // __MSRXMUX_H__

/*
 * $Id: mindsensors-rcxsensorsmux.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
