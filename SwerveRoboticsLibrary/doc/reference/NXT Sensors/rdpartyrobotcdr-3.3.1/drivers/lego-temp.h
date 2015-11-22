/*!@addtogroup lego
 * @{
 * @defgroup legotmp Temperature Sensor
 * Temperature Sensor
 * @{
 */

/*
 * $Id: lego-temp.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __LEGOTMP_DRIVER_H__
#define __LEGOTMP_DRIVER_H__

/** \file lego-temp.h
 * \brief RobotC New Temperature Sensor Driver
 *
 * lego-temp.h provides an API for the Lego Temperature Sensor.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Partial rewrite by Xander Soldaat to simplify API
 *
 * Credits :
 * - Based on http://focus.ti.com/lit/ds/symlink/tmp275.pdf (Thank to Xander Soldaat who found the internal design)
 * - Based on Xander Soldaat's RobotC driver template
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Sylvain CACHEUX (sylcalego@cacheux.info)
 * \author Xander Soldaat (mightor@gmail.com), version 0.2
 * \date 15 february 2010
 * \version 0.2
 * \example lego-temp-test1.c
 * \example lego-temp-test2.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*
<Address definitions>
*/
#define LEGOTMP_I2C_ADDR 0x98 /*!< New LEGO Temperature Sensor I2C device address                                                */
#define LEGOTMP_TEMP     0x00 /*!< Temperature value (return on 2 bytes, 1rst : most significant byte, 2nd  byte only half used) */
#define LEGOTMP_CONFIG   0x01 /*!< Configuration registry (see http://focus.ti.com/lit/ds/symlink/tmp275.pdf)                    */



// ---------------------------- Definitions --------------------------------------
/*!< Command modes definition */
typedef enum {
  A_MIN   = 8, // 0.5,   * 16 (typedef needs integer...)
  A_MEAN1 = 4, // 0.25   * 16 (so *16 here...)
  A_MEAN2 = 2, // 0.125  * 16 (... and /16 in functions)
  A_MAX   = 1  // 0.0625 * 16 (I'll have to improve that later...)
} tLEGOTMPAccuracy; // The 4 available accuracy of the sensor


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Function prototypes
bool LEGOTMPreadTemp(tSensors link, float &temp);
bool LEGOTMPreadAccuracy(tSensors link, tLEGOTMPAccuracy &accuracy);
bool LEGOTMPsetAccuracy(tSensors link, tLEGOTMPAccuracy accuracy);
bool LEGOTMPsetSingleShot(tSensors link);
bool LEGOTMPsetContinuous(tSensors link);
bool _LEGOTMPreadConfig(tSensors link, ubyte &config);
tLEGOTMPAccuracy _LEGOTMPconvertAccuracy(ubyte config);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// global variables
tByteArray       LEGOTMP_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray       LEGOTMP_I2CReply;      /*!< Array to hold I2C reply data   */

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/**
 * Read the current configuration register
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the LEGO Temp Sensor port number
 * @param config the contents of the register
 * @return true if no error occured, false if it did
 */
bool _LEGOTMPreadConfig(tSensors link, ubyte &config) {
  memset(LEGOTMP_I2CRequest, 0, sizeof(tByteArray));

  LEGOTMP_I2CRequest[0] = 2;                // Message size
  LEGOTMP_I2CRequest[1] = LEGOTMP_I2C_ADDR; // I2C Address
  LEGOTMP_I2CRequest[2] = LEGOTMP_CONFIG;   // Value address

  if (!writeI2C(link, LEGOTMP_I2CRequest, LEGOTMP_I2CReply, 1))
    return false;

  config = LEGOTMP_I2CReply[0];

  return true;
}


/**
 * Set the configuration register
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the LEGO Temp Sensor port number
 * @param config the contents of the register
 * @return true if no error occured, false if it did
 */
bool _LEGOTMPsetConfig(tSensors link, ubyte &config) {
  memset(LEGOTMP_I2CRequest, 0, sizeof(tByteArray));

  LEGOTMP_I2CRequest[0] = 3;                // Message size
  LEGOTMP_I2CRequest[1] = LEGOTMP_I2C_ADDR; // I2C Address
  LEGOTMP_I2CRequest[2] = LEGOTMP_CONFIG;   // Value address
  LEGOTMP_I2CRequest[3] = config;           // Value to be set

  return writeI2C(link, LEGOTMP_I2CRequest);
}


/**
 * Retrieve the accuracy level from the config bytes
 *
 * Note: this is an internal function and should not be called directly.
 * @param config the contents of the register
 * @return true if no error occured, false if it did
 */
tLEGOTMPAccuracy _LEGOTMPconvertAccuracy(ubyte config) {
  /* the accuracy is in bits 6 & 5<br>
   * Config registry :<br>
   * BYTE D7  <D6 D5> D4 D3 D2  D1 D0<br>
   * 1    OS  <R1 R0> F1 F0 POL TM SD<br>
   * so shifting bits to have the bits 6 and 5 at the right of the byte<br>
   * then doing a AND with 11 (3) to know the value of the accuracy.<br>
   */

  config = (config >> 5) & 3;
  switch (config) {
    case 0:
      // bits are 0 0, so the minimum accuracy
      return A_MIN;
      break;
    case 1:
      // bits are 0 1, so the mean1 accuracy
      return A_MEAN1;
      break;
    case 2:
      // bits are 1 0, so the mean2 accuracy
      return A_MEAN2;
      break;
    case 3:
      // bits are 1 1, so the maximum accuracy
      return A_MAX;
      break;
  }
  return A_MIN;
}


/**
 * Read the temperature.
 * @param link the LEGO Temp Sensor port number
 * @param temp the temperature value,
 * @return true if no error occured, false if it did
 */
bool LEGOTMPreadTemp(tSensors link, float &temp) {
  memset(LEGOTMP_I2CRequest, 0, sizeof(tByteArray));
  int b1;
  float b2;
  ubyte config;

  //reading current accuracy
  if (!_LEGOTMPreadConfig(link, config))
    return false;

  // Check if we're in shutdown mode, if so, we're doing
  // one-shotted readings.
  if (config & 1 == 1) {
    config |= (1<<7); // set bit 7 for one-shot mode

    if (!_LEGOTMPsetConfig(link, config))
      return false;

    // Now we need to wait a specific amount of time, depending on the
    // accuracy level.

    switch (_LEGOTMPconvertAccuracy(config)) {
      case A_MIN:
        // conversion takes 27.5ms
        wait1Msec(28);
        break;
      case A_MEAN1:
        // conversion takes 55ms
        wait1Msec(55);
        break;
      case A_MEAN2:
        // conversion takes 110ms
        wait1Msec(110);
        break;
      case A_MAX:
        // conversion takes 220ms
        wait1Msec(220);
        break;
    }
  }

  // clear the array again
  memset(LEGOTMP_I2CRequest, 0, sizeof(tByteArray));
  LEGOTMP_I2CRequest[0] = 2;                // Message size
  LEGOTMP_I2CRequest[1] = LEGOTMP_I2C_ADDR; // I2C Address
  LEGOTMP_I2CRequest[2] = LEGOTMP_TEMP;     // Value address

  if (!writeI2C(link, LEGOTMP_I2CRequest, LEGOTMP_I2CReply, 2))
    return false;

  b1 = (int)LEGOTMP_I2CReply[0];

  switch (_LEGOTMPconvertAccuracy(config)) {
    case A_MIN:
      ///128 to have only the most significant bit (9 bits accuracy - 8 (b1) = 1 bit to keep)
      b2 = ((int)LEGOTMP_I2CReply[1] >> 7) * 0.5;
      break;
    case A_MEAN1:
      ///64 to have only the 2 most significant bits
      b2 = ((int)LEGOTMP_I2CReply[1] >> 6) * 0.25;
      break;
    case A_MEAN2:
      ///32 to have only the 3 most significant bits
      b2 = ((int)LEGOTMP_I2CReply[1] >> 5) * 0.125;
      break;
    case A_MAX:
      ///16 to have only the 4 most significant bits
      b2 = ((int)LEGOTMP_I2CReply[1] >> 4) * 0.0625;
      break;
  }

  if (b1 < 128) temp = b1 + b2;         // positive temp
  if (b1 > 127) temp = (b1 - 256) + b2; // negative temp

  return true;
}


/**
 * Read the temperature sensor's accuracy
 * @param link the LEGO Temp Sensor port number,
 * @param accuracy the accuracy value to be read
 * @return true if no error occured, false if it did
 */
bool LEGOTMPreadAccuracy(tSensors link, tLEGOTMPAccuracy &accuracy) {
  memset(LEGOTMP_I2CRequest, 0, sizeof(tByteArray));
  ubyte config;

  if (!_LEGOTMPreadConfig(link, config))
    return false;

  accuracy = _LEGOTMPconvertAccuracy(config);

  return true;
}


/**
 * Set the temperature sensor's accuracy
 * @param link the LEGO Temp Sensor port number
 * @param accuracy the accuracy value to be set
 * @return true if no error occured, false if it did
 */
bool LEGOTMPsetAccuracy(tSensors link, tLEGOTMPAccuracy accuracy) {
  memset(LEGOTMP_I2CRequest, 0, sizeof(tByteArray));
  ubyte config;

  // reading the configuration registry to know the other bits values
  if (!_LEGOTMPreadConfig(link, config))
    return false;

  // setting the value to be writed in the configuration registry depending on the others bits values
	/* the accuracy is in bits 6 & 5<br>
	 * Config registry :<br>
	 * BYTE D7  <D6 D5> D4 D3 D2  D1 D0<br>
	 * 1    OS  <R1 R0> F1 F0 POL TM SD<br>
	 *      128 <64 32> 16 8  4   2  1<br>
	*/

  // Clear bits 5 and 6
  config &= 0x9F;

  switch (accuracy) {
    case A_MIN:
      // Minimum accuracy, so bits 6 5 must be 0 0, so doing an OR with 0000 0000;
      config |= 0x00;  //
      break;
    case A_MEAN1:
      // Mean accuracy 1, so bits 6 5 must be 0 1, so doing an OR with 0010 0000 (0x20)
      config |= 0x20;
      break;
    case A_MEAN2:
      // Mean accuracy 2, so bits 6 5 must be 1 0, so doing an OR with 0100 0000 (0x40
      config |= 0x40;
      break;
    case A_MAX:
      // Maximum accuracy, so bits 6 5 must be 1 1, so doing a OR with 0110 0000 (0x60
      config |= 0x60;
      break;
  }

  // Send new configuration to the sensor
  return _LEGOTMPsetConfig(link, config);
}


/**
 * Configure the sensor for Single Shot mode
 * @param link the New LEGO Sensor port number
 * @return true if no error occured, false if it did
 */
bool LEGOTMPsetSingleShot(tSensors link) {
  memset(LEGOTMP_I2CRequest, 0, sizeof(tByteArray));
  ubyte config;

  // reading the configuration registry to know the other bits values
  if (!_LEGOTMPreadConfig(link, config))
    return false;

  // setting the value to be written in the configuration registry depending on the others bits values
  /* the shutdown mode bit it bit 0<br>
	 * Config registry :<br>
	 * BYTE D7  <D6 D5> D4 D3 D2  D1 D0<br>
	 * 1    OS  <R1 R0> F1 F0 POL TM SD<br>
	 *      128 <64 32> 16 8  4   2  1<br>
	*/

  // Set bit 0 to 1
  config |= 1;

  // Send new configuration to the sensor
  return _LEGOTMPsetConfig(link, config);
}


/**
 * Configure the sensor for Continuous mode
 * @param link the New LEGO Sensor port number
 * @return true if no error occured, false if it did
 */
bool LEGOTMPsetContinuous(tSensors link) {
  memset(LEGOTMP_I2CRequest, 0, sizeof(tByteArray));
  ubyte config;

  // reading the configuration registry to know the other bits values
  if (!_LEGOTMPreadConfig(link, config))
    return false;

  // setting the value to be written in the configuration registry depending on the others bits values
  /* the shutdown mode bit it bit 0<br>
   * Config registry :<br>
   * BYTE D7  <D6 D5> D4 D3 D2  D1 D0<br>
   * 1    OS  <R1 R0> F1 F0 POL TM SD<br>
   *      128 <64 32> 16 8  4   2  1<br>
   */

  // Set bit 0 to 0
  config &= 0xFE;

  // Send new configuration to the sensor
  return _LEGOTMPsetConfig(link, config);
}

#endif // __LEGOTMP_DRIVER_H__

/*
 * $Id: lego-temp.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
