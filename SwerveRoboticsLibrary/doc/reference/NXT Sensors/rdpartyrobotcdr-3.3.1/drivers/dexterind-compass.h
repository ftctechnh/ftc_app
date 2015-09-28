/*!@addtogroup Dexter_Industries
* @{
* @defgroup DIMC 3D Compass Sensor
* Dexter Industries DIMC 3D Compass Sensor driver
* @{
*/

/*
* $Id: dexterind-compass.h 133 2013-03-10 15:15:38Z xander $
*/

#ifndef __DIMC_H__
#define __DIMC_H__
/** \file dexterind-compass.h
* \brief Dexter Industries IMU Sensor driver
*
* dexterind-compass.h provides an API for the Dexter Industries compass Sensor.\n
*
* Changelog:
* - 0.1: Initial release
*
* Credits:
* - Big thanks to Dexter Industries for providing me with the hardware necessary to write and test this.
*
* License: You may use this code as you wish, provided you give credit where its due.
*
* THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

* \author Xander Soldaat (xander_at_botbench.com)
* \date 18 March 2012
* \version 0.1
* \example dexterind-compass-test1.c
* \example dexterind-compass-test2.c
* \example dexterind-compass-test3.c
*/

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define DIMCDAT "dimc.dat"

#define DIMC_I2C_ADDR           0x3C  /*!< Compass I2C address */

#define DIMC_REG_CONFIG_A       0x00  /*!< 250 dps range */
#define DIMC_REG_CONFIG_B       0x01  /*!< 500 dps range */
#define DIMC_REG_MODE           0x02  /*!< 2000 dps range */
#define DIMC_REG_X_MSB          0x03  /*!< Register MSB X axis */
#define DIMC_REG_X_LSB          0x04  /*!< Register LSB X axis */
#define DIMC_REG_Z_MSB          0x05  /*!< Register MSB Z axis */
#define DIMC_REG_Z_LSB          0x06  /*!< Register LSB Z axis */
#define DIMC_REG_Y_MSB          0x07  /*!< Register MSB Y axis */
#define DIMC_REG_Y_LSB          0x08  /*!< Register LSB Y axis */
#define DIMC_REG_STATUS         0x09  /*!< Status register */
#define DIMC_REG_IDENT_A        0x0A  /*!< Identification Register A */
#define DIMC_REG_IDENT_B        0x0B  /*!< Identification Register B */
#define DIMC_REG_IDENT_C        0x0C  /*!< Identification Register C */

// HMC5883L status definitions
// See page 16 of HMC5883L.pdf

#define DIMC_STATUS_LOCK  2           /*!< Data output register lock active */
#define DIMC_STATUS_RDY   1           /*!< Data is ready for reading */


// HMC5883L configuration definitions
// See pages 12, 13, 14 of HMC5883L.pdf

// The Mode Register has 2 possible values for Idle mode.
#define DIMC_MODE_CONTINUOUS  0       /*!< Continuous-Measurement Mode */
#define DIMC_MODE_SINGLE      1       /*!< Single-Measurement Mode (Default) */
#define DIMC_MODE_IDLE_A      2       /*!< Idle Mode */
#define DIMC_MODE_IDLE_B      3       /*!< Idle Mode */
#define DIMC_MODE_MASK        3       /*!< Mask for setting mode */

// How many samples averaged? Default=1
#define DIMC_CONF_A_SAMPLES_1    0x00 /*!< Number of samples averaged: 1 */
#define DIMC_CONF_A_SAMPLES_2    0x20 /*!< Number of samples averaged: 2 */
#define DIMC_CONF_A_SAMPLES_4    0x40 /*!< Number of samples averaged: 4 */
#define DIMC_CONF_A_SAMPLES_8    0x60 /*!< Number of samples averaged: 8 */
#define DIMC_CONF_A_SAMPLES_MASK 0x60 /*!< Mask for setting sample number */

// Data output rate for continuous mode. Default=15Hz
#define DIMC_CONF_A_RATE_0_75     0x00 /*!< Data Output Rate: 0.75 Hz */
#define DIMC_CONF_A_RATE_1_5      0x04 /*!< Data Output Rate: 1.5 Hz */
#define DIMC_CONF_A_RATE_3        0x08 /*!< Data Output Rate: 3 Hz */
#define DIMC_CONF_A_RATE_7_5      0x0C /*!< Data Output Rate: 7.5 Hz */
#define DIMC_CONF_A_RATE_15       0x10 /*!< Data Output Rate: 15 Hz */
#define DIMC_CONF_A_RATE_30       0x14 /*!< Data Output Rate: 30 Hz */
#define DIMC_CONF_A_RATE_75       0x18 /*!< Data Output Rate: 75 Hz */
#define DIMC_CONF_A_RATE_RESERVED 0x1C
#define DIMC_CONF_A_RATE_MASK     0x1C /*!< Mask for setting Data Output Rate */

// Measurement configuration, whether to apply bias. Default=Normal
#define DIMC_CONF_A_BIAS_NORMAL   0x00 /*!< Normal measurement configuration (Default) */
#define DIMC_CONF_A_BIAS_POSITIVE 0x01 /*!< Positive bias configuration for X, Y, and Z axes. */
#define DIMC_CONF_A_BIAS_NEGATIVE 0x02 /*!< Negative bias configuration for X, Y and Z axes. */
#define DIMC_CONF_A_BIAS_RESERVED 0x03
#define DIMC_CONF_A_BIAS_MASK     0x03 /*!< Mask for setting measurement bias */

// Gain configuration. Default=1.3Ga
#define DIMC_CONF_B_GAIN_0_88 0x00     /*!< Sensor Field Range ±0.88 Ga */
#define DIMC_CONF_B_GAIN_1_3  0x20     /*!< Sensor Field Range ±1.3 Ga */
#define DIMC_CONF_B_GAIN_1_9  0x40     /*!< Sensor Field Range ±1.9 Ga */
#define DIMC_CONF_B_GAIN_2_5  0x60     /*!< Sensor Field Range ±2.5 Ga */
#define DIMC_CONF_B_GAIN_4_0  0x80     /*!< Sensor Field Range ±4.0 Ga */
#define DIMC_CONF_B_GAIN_4_7  0xA0     /*!< Sensor Field Range ±4.7 Ga */
#define DIMC_CONF_B_GAIN_5_6  0xC0     /*!< Sensor Field Range ±5.6 Ga */
#define DIMC_CONF_B_GAIN_8_1  0xE0     /*!< Sensor Field Range ±8.1 Ga */
#define DIMC_CONF_B_GAIN_MASK 0xE0     /*!< Mask for setting Sensor Field Range */

// Digital resolution (mG/LSb) for each gain
#define DIMC_GAIN_SCALE_0_88  0.73     /*!< Ramge multiplier for ±0.88 Ga */
#define DIMC_GAIN_SCALE_1_3   0.92     /*!< Ramge multiplier for ±1.3 Ga */
#define DIMC_GAIN_SCALE_1_9   1.22     /*!< Ramge multiplier for ±1.9 Ga */
#define DIMC_GAIN_SCALE_2_5   1.52     /*!< Ramge multiplier for ±2.5 Ga */
#define DIMC_GAIN_SCALE_4_0   2.27     /*!< Ramge multiplier for ±4.0 Ga */
#define DIMC_GAIN_SCALE_4_7   2.56     /*!< Ramge multiplier for ±4.7 Ga */
#define DIMC_GAIN_SCALE_5_6   3.03     /*!< Ramge multiplier for ±5.6 Ga */
#define DIMC_GAIN_SCALE_8_1   4.35     /*!< Ramge multiplier for ±8.1 Ga */

tByteArray DIMC_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray DIMC_I2CReply;      /*!< Array to hold I2C reply data */

bool DIMCinit(tSensors link, ubyte range, bool lpfenable=true);
bool DIMCreadAxes(tSensors link, int &_x, int &_y, int &_z);
float DIMCreadHeading(tSensors link);
void DIMCstartCal(tSensors link);
void DIMCstopCal(tSensors link);
void _DIMCreadCalVals();
void _DIMCwriteCalVals();

bool DIMCcalibrating[4] = {false, false, false, false };
bool DIMCcalibrationDataLoaded = false;
int DIMCminVals[4][3] = {{32767, 32767, 32767}, {32767, 32767, 32767}, {32767, 32767, 32767}, {32767, 32767, 32767}};
int DIMCmaxVals[4][3] = {{0,0,0}, {0,0,0}, {0,0,0}, {0,0,0}};
int DIMCoffsets[4][3] = {{0,0,0}, {0,0,0}, {0,0,0}, {0,0,0}};

/**
* Configure the Compass
* @param link the port number
* @return true if no error occured, false if it did
*/
bool DIMCinit(tSensors link)
{
  memset(DIMC_I2CRequest, 0, sizeof(DIMC_I2CRequest));

  // Setup the size and address, same for all requests.
  DIMC_I2CRequest[0] = 3;    // Sending address, register, value. Optional, defaults to true
  DIMC_I2CRequest[1] = DIMC_I2C_ADDR; // I2C Address of Compass.

  // Write CONFIG_A
  // Set to 15Hz sample rate and a 8 sample average.
  DIMC_I2CRequest[2] = DIMC_REG_CONFIG_A;
  DIMC_I2CRequest[3] = DIMC_CONF_A_SAMPLES_8 + DIMC_CONF_A_RATE_15;
  if (!writeI2C(link, DIMC_I2CRequest))
    return false;

  // Write CONFIG_B
  // Set gain to 1.9
  DIMC_I2CRequest[2] = DIMC_REG_CONFIG_B;
  DIMC_I2CRequest[3] = DIMC_CONF_B_GAIN_1_3;
  if (!writeI2C(link, DIMC_I2CRequest))
    return false;

  // Write REG_MODE
  // Set to continuous mode
  ////////////////////////////////////////////////////////////////////////////
  DIMC_I2CRequest[2] = DIMC_REG_MODE;           // Register address of CTRL_REG3
  DIMC_I2CRequest[3] = DIMC_MODE_CONTINUOUS;    // No interrupts.  Date ready.
  return writeI2C(link, DIMC_I2CRequest);
}


/**
* Read all three axes of the Compass
* @param link the port number
* @param _x data for x axis in degrees per second
* @param _y data for y axis in degrees per second
* @param _z data for z axis in degrees per second
* @return true if no error occured, false if it did
*/
bool DIMCreadAxes(tSensors link, int &_x, int &_y, int &_z)
{
  if (!DIMCcalibrationDataLoaded)
    _DIMCreadCalVals();

  DIMC_I2CRequest[0] = 2;                   // Message size
  DIMC_I2CRequest[1] = DIMC_I2C_ADDR;  // I2C Address
  DIMC_I2CRequest[2] = DIMC_REG_X_MSB;            // Register address

  if (!writeI2C(link, DIMC_I2CRequest, DIMC_I2CReply, 6)) {
    writeDebugStreamLine("error write");
    return false;
  }

  _x = (DIMC_I2CReply[0]<<8) + DIMC_I2CReply[1];
  _z = (DIMC_I2CReply[2]<<8) + DIMC_I2CReply[3];
  _y = (DIMC_I2CReply[4]<<8) + DIMC_I2CReply[5];

  if (DIMCcalibrating[link])
  {
    DIMCminVals[link][0] = min2(_x, DIMCminVals[link][0]);
    DIMCminVals[link][1] = min2(_y, DIMCminVals[link][1]);
    DIMCminVals[link][2] = min2(_z, DIMCminVals[link][2]);

    DIMCmaxVals[link][0] = max2(_x, DIMCmaxVals[link][0]);
    DIMCmaxVals[link][1] = max2(_y, DIMCmaxVals[link][1]);
    DIMCmaxVals[link][2] = max2(_z, DIMCmaxVals[link][2]);
  }

  _x -= DIMCoffsets[link][0];
  _y -= DIMCoffsets[link][1];
  _z -= DIMCoffsets[link][2];

  return true;
}


/**
* Read the current heading
* @return the heading in degrees.
*/
float DIMCreadHeading(tSensors link)
{
  float angle;
  int fx,fy,fz;
  DIMCreadAxes(link, fx, fy, fz);

  angle = atan2(fx, fy);
  if (angle < 0) angle += 2*PI;
  return angle * (180/PI);
}


/**
* Start calibration.  The robot should be made to rotate
* about its axis at least twice to get an accurate result.
* Stop the calibration with DIMCstopCal()
*/
void DIMCstartCal(tSensors link)
{
  DIMCcalibrating[link] = true;
}


/**
* Stop calibration.  The appropriate offsets will be calculated for all
* the axes.
*/
void DIMCstopCal(tSensors link)
{
  DIMCcalibrating[link] = false;
  DIMCoffsets[link][0] = ((DIMCmaxVals[link][0] - DIMCminVals[link][0]) / 2) + DIMCminVals[link][0];
  DIMCoffsets[link][1] = ((DIMCmaxVals[link][1] - DIMCminVals[link][1]) / 2) + DIMCminVals[link][1];
  DIMCoffsets[link][2] = ((DIMCmaxVals[link][2] - DIMCminVals[link][2]) / 2) + DIMCminVals[link][2];
  _DIMCwriteCalVals();
}


/**
 * Write the calibration values to a data file.
 *
 * Note: this is an internal function and should not be called directly
 */
void _DIMCwriteCalVals()
{
  TFileHandle hFileHandle;
  TFileIOResult nIoResult;
  short nFileSize = sizeof(DIMCoffsets);

  // Delete the old data file and open a new one for writing
  Delete(DIMCDAT, nIoResult);
  OpenWrite(hFileHandle, nIoResult, DIMCDAT, nFileSize);
  if (nIoResult != ioRsltSuccess)
  {
    Close(hFileHandle, nIoResult);
    eraseDisplay();
    nxtDisplayTextLine(3, "W:can't cal file");
    PlaySound(soundException);
    while(bSoundActive) EndTimeSlice();
    wait1Msec(5000);
    StopAllTasks();
  }

	for (int i = 0; i < 4; i++)
	{
    for (int j = 0; j < 3; j++)
    {
		  WriteShort(hFileHandle, nIoResult, DIMCoffsets[i][j]);
		  if (nIoResult != ioRsltSuccess)
		  {
	      eraseDisplay();
		    nxtDisplayTextLine(3, "can't write lowval");
		    PlaySound(soundException);
		    while(bSoundActive) EndTimeSlice();
		    wait1Msec(5000);
		    StopAllTasks();
	    }
	  }
	}

  // Close the file
  Close(hFileHandle, nIoResult);
  if (nIoResult != ioRsltSuccess)
  {
    eraseDisplay();
    nxtDisplayTextLine(3, "Can't close");
    PlaySound(soundException);
    while(bSoundActive) EndTimeSlice();
    wait1Msec(5000);
    StopAllTasks();
  }
}



/**
 * Read the calibration values from a data file.
 *
 * Note: this is an internal function and should not be called directly
 */
void _DIMCreadCalVals()
{
  TFileHandle hFileHandle;
  TFileIOResult nIoResult;
  short nFileSize;

  // Open the data file for reading
  DIMCcalibrationDataLoaded = true;
  OpenRead(hFileHandle, nIoResult, DIMCDAT, nFileSize);
  if (nIoResult != ioRsltSuccess)
  {
    Close(hFileHandle, nIoResult);
    // Assign default values
		memset(DIMCoffsets, 0, sizeof(DIMCoffsets));
		_DIMCwriteCalVals();
    return;
  }

  for (int i = 0; i < 4; i++)
  {
    for (int j = 0; j < 3; j++)
    {
		  ReadShort(hFileHandle, nIoResult, DIMCoffsets[i][j]);
		  // writeDebugStream("R offsets[%d][%d]:", i, j);
		  // writeDebugStreamLine(" %d", DIMCoffsets[i][j]);
		  if (nIoResult != ioRsltSuccess)
		  {
		    memset(DIMCoffsets, 0, sizeof(DIMCoffsets));
			  _DIMCwriteCalVals();
			  return;
			}
	  }
	}

  Close(hFileHandle, nIoResult);
}





#endif // __DIMC_H__

/*
* $Id: dexterind-compass.h 133 2013-03-10 15:15:38Z xander $
*/
/* @} */
/* @} */
