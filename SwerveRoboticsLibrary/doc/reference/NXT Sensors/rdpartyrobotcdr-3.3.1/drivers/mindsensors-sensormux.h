/*!@addtogroup mindsensors
 * @{
 * @defgroup MSSMUX SensorMUX Sensor
 * Mindsensors SensorMUX Sensor (MSSMUX) driver
 * @{
 */

/*
 * $Id: mindsensors-sensormux.h 136 2013-03-13 18:52:29Z xander $
 */

#ifndef __MSSMUX_H__
#define __MSSMUX_H__
/** \file mindsensors-sensormux.h
 * \brief Mindsensors SensorMUX Sensor driver
 *
 * mindsensors-sensormux.h provides an API for the Mindsensors SensorMUX Sensor.
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
 * \date 02 March 2013
 * \version 0.1
 * \example mindsensors-sensormux-test1.c
 * \example mindsensors-sensormux-test2.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define MSMX_I2C_ADDR     0x24
#define MSMX_REG_CHANSEL  0x42
#define MSMX_REG_VOLTAGE  0x43


tByteArray MSMX_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray MSMX_I2CReply;      /*!< Array to hold I2C reply data */

void MSSMUXsetChan(tSensors link, int channel);
int MSSMUXreadBattery(tSensors link);


/**
 * Read the voltage level of the external battery.
 * @param link the port number
 * @return the battery voltage in mV
 */
int MSSMUXreadBattery(tSensors link)
{
  // Switch to the virtual channel (0)
  MSSMUXsetChan(link, 0);
  wait1Msec(50);

  SensorType[link] = sensorI2CCustomFastSkipStates;
  memset(MSMX_I2CRequest, 0, sizeof(tByteArray));

  MSMX_I2CRequest[0] = 2;                      // Message size
  MSMX_I2CRequest[1] = MSMX_I2C_ADDR;         // I2C Address
  MSMX_I2CRequest[2] = MSMX_REG_VOLTAGE;

  if (!writeI2C(link, MSMX_I2CRequest, MSMX_I2CReply, 1))
  {
    writeDebugStreamLine("error with i2c");
    return false;
  }

  return (0x00FF & MSMX_I2CReply[0]) * 37;
}

/**
 * Switch to the specified channel
 * @param link the port number
 * @param channel the sensor mux channel number
 */
void MSSMUXsetChan(tSensors link, int channel)
{
  static int currChannel[4] = {-1, -1, -1, -1};

  // Message to send to SMUX
  ubyte MUXmsg[] = {0x55, 0xAA, 0x30 + (channel & 0xFF)};

  // Do nothing if the channel is already set correctly
  if (currChannel[link] == channel)
    return;

  currChannel[link] = channel;

  TSensorTypes previous = SensorType[link];
  writeDebugStreamLine("channel type: %d", previous);

  // Set the sensor type to sensorCustom so we can
  // start sending messages
  SensorType[link] = sensorCustom;

  // If the sensor was previously configured as a colour sensor
  // give it a little time
  switch (previous)
  {
    case sensorCOLORFULL:
    case sensorCOLORRED:
    case sensorCOLORGREEN:
    case sensorCOLORBLUE:
    case sensorCOLORNONE: wait1Msec(12); break;
  }

  // Set both pins as output
  DigitalPinDirection[link] = 0x03;
  wait1Msec(1);

  for (int i = 0; i < 3; i++)
  {
    DigitalPinValue[link] = 0x00;
    wait1Msec(1);

    for (int j = 0; j < 8; j++) {
      if ((MUXmsg[i] >> j) & 0x01)
        DigitalPinValue[link] = 0x00;
      else
        DigitalPinValue[link] = 0x01;
      wait1Msec(1);
    }
    DigitalPinValue[link] = 0x01;
    wait1Msec(1);
  }
}

#endif // __MSSMUX_H__

/*
 * $Id: mindsensors-sensormux.h 136 2013-03-13 18:52:29Z xander $
 */
/* @} */
/* @} */
