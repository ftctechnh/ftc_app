/*!@addtogroup Dexter_Industries
 * @{
 * @defgroup dPress dPressure Sensor
 * Dexter Industries dPressure Sensor driver
 * @{
 */

/*
 * $Id: dexterind-pressure.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __DPRESS_DRIVER_H__
#define __DPRESS_DRIVER_H__

/** \file dexterind-pressure.h
 * \brief ROBOTC dPressure Sensor Driver
 *
 * DPRESS-driver.h provides an API for the Dexter Industries dPressure Sensor.
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
 * \example dexterind-pressure-test1.c
 */

#pragma systemFile

#define DPRESS_VREF 4.85 /*!< The voltage reference is assumed to be around 4V85 */

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Function prototypes
bool DPRESSreadPress250kPa(tSensors link, float &pressure);
bool DPRESSreadPress250PSI(tSensors link, float &pressure);
bool DPRESSreadPress500kPa(tSensors link, float &pressure);
bool DPRESSreadPress500PSI(tSensors link, float &pressure);

/**
 * Read the pressure in kiloPascals\n
 * Note: This function is for the dPressure 250
 * @param link the dPressure Sensor port number
 * @param pressure the pressure in kiloPascals
 * @return true if no error occured, false if it did
 */

bool DPRESSreadPress250kPa(tSensors link, float &pressure) {
  float Vout = 0.0;

  int val = 0;

  // dPressure sensor type must absolutely be set to sensorAnalogInactive
  if (SensorType[link] != sensorAnalogInactive)
    return false;

  // Pressure is calculated using Vout = VS x (0.00369 x P + 0.04)
  // => P
  // Where Vs is assumed to be equal to around 4.85 on the NXT

  // Get raw sensor value
  val = SensorValue[link];

  // Calculate Vout
  Vout = ((val * DPRESS_VREF) / 1023);

  pressure = ((Vout / DPRESS_VREF) - 0.04) / 0.00369;
  return true;
}


/**
 * Read the pressure in kiloPascals\n
 * Note: This function is for the dPressure 250
 * @param link the dPressure Sensor port number
 * @param pressure the pressure in Pounds per Square Inch
 * @return true if no error occured, false if it did
 */
bool DPRESSreadPress250PSI(tSensors link, float &pressure) {
  return true;
}


/**
 * Read the pressure in kiloPascals\n
 * Note: This function is for the dPressure 500
 * @param link the dPressure 500 Sensor port number
 * @param pressure the pressure in kiloPascals
 * @return true if no error occured, false if it did
 */
bool DPRESSreadPress500kPa(tSensors link, float &pressure) {
  float Vout = 0.0;

  int val = 0;

  // dPressure sensor type must absolutely be set to sensorAnalogInactive
  if (SensorType[link] != sensorAnalogInactive)
    return false;

  // Pressure is calculated using Vout = VS x (0.0018 x P + 0.04)
  // => P
  // Where Vs is assumed to be equal to around 4.85 on the NXT

  // Get raw sensor value
  val = SensorValue[link];

  // Calculate Vout
  Vout = ((val * DPRESS_VREF) / 1023);

  pressure = ((Vout / DPRESS_VREF) - 0.04) / 0.0018;
  return true;
}


/**
 * Read the pressure in kiloPascals\n
 * Note: This function is for the dPressure 500
 * @param link the dPressure 500 Sensor port number
 * @param pressure the pressure in Pounds per Square Inch
 * @return true if no error occured, false if it did
 */
bool DPRESSreadPress500PSI(tSensors link, float &pressure) {
  return true;
}


#endif // __DPRESS_DRIVER_H__

/*
 * $Id: dexterind-pressure.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
