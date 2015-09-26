/*!@addtogroup Dexter_Industries
 * @{
 * @defgroup dTemp Temp Probe
 * Dexter Industries Temperature Probe Sensor driver
 * @{
 */

/*
 * $Id: dexterind-temp.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __DTMP_DRIVER_H__
#define __DTMP_DRIVER_H__

/** \file dexterind-temp.h
 * \brief ROBOTC DI Temp Probe Driver
 *
 * dexterind-temp.h provides an API for the Dexter Industries Temperature Probe.
 *
 * Changelog:
 * - 0.1: Initial release
 *
 * Credits :
 * - Big thanks to Dexter Industries for providing me with the hardware necessary to write and test this.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (mightor@gmail.com)
 * \date 13 June 2010
 * \version 0.1
 * \example dexterind-temp-test1.c
 */

#pragma systemFile

// internal lookup tables used for resistance to temp conversions
const float _a[] = {0.003357042,         0.003354017,        0.0033530481,       0.0033536166};
const float _b[] = {0.00025214848,       0.00025617244,      0.00025420230,      0.000253772};
const float _c[] = {0.0000033743283,     0.0000021400943,    0.0000011431163,    0.00000085433271};
const float _d[] = {-0.000000064957311, -0.000000072405219, -0.000000069383563, -0.000000087912262};

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Function prototypes
bool DTMPreadTemp(tSensors link, float &temp);
bool DTMPreadTempK(tSensors link, float &temp);
bool DTMPreadTempF(tSensors link, float &temp);


/**
 * Read the temperature in degrees Celcius.
 * @param link the DI Temp Sensor port number
 * @param temp the temperature value in degrees Celcius,
 * @return true if no error occured, false if it did
 */
bool DTMPreadTemp(tSensors link, float &temp) {
  float _tempK = 0.0;
  if (!DTMPreadTempK(link, _tempK))
    return false;

  temp = _tempK - 273;
  return true;
}


/**
 * Read the temperature in Kelvin.
 * @param link the DI Temp Sensor port number
 * @param temp the temperature value in Kelvin,
 * @return true if no error occured, false if it did
 */
bool DTMPreadTempK(tSensors link, float &temp) {
  // local vars
  byte i = 0;
  int val = 0;
  float RtRt25 = 0.0;
  float lnRtRt25 = 0.0;

  // Temp sensor type must absolutely be set to sensorAnalogInactive
  if (SensorType[link] != sensorAnalogInactive)
    return false;

  // Get the basic factors for equation
  val = SensorValue[link];
  RtRt25 = (float)val / (1023 - val);
  lnRtRt25 = log(RtRt25);

  if (RtRt25 > 3.277)
    i = 0;
  else if (RtRt25 > 0.3599)
    i = 1;
  else if (RtRt25 > 0.06816)
    i = 2;
  else
    i = 3;

  temp =  1.0 / (_a[i] + (_b[i] * lnRtRt25) + (_c[i] * lnRtRt25 * lnRtRt25) + (_d[i] * lnRtRt25 * lnRtRt25 * lnRtRt25));

  return true;
}


/**
 * Read the temperature in Fahrenheit.
 * @param link the DI Temp Sensor port number
 * @param temp the temperature value in Fahrenheit,
 * @return true if no error occured, false if it did
 */
bool DTMPreadTempF(tSensors link, float &temp) {
  float _tempK = 0.0;
  if (!DTMPreadTempK(link, _tempK))
    return false;

  temp = 32 + ((_tempK - 273) * 9) / 5;

  return true;
}

#endif // __DTMP_DRIVER_H__

/*
 * $Id: dexterind-temp.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
