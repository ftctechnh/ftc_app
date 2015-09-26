/*!@addtogroup HiTechnic
 * @{
 * @defgroup htpb Prototype Board
 * HiTechnic Prototype Board
 * @{
 */
/*
 * $Id: hitechnic-protoboard.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTPB_H__
#define __HTPB_H__
/** \file hitechnic-protoboard.h
 * \brief HiTechnic Prototype Board driver
 *
 * hitechnic-protoboard.h provides an API for the HiTechnic Proto Board.
 *
 * Changelog:
 * - 0.1: Initial release
 * - 0.2: Minor bug fixes
 * - 0.3: Major rewrite to make use of common.h for common API
 * - 0.4: Work around no longer needed and removed, error handling is now done in common.h
 * - 0.5: Fixed bugs in HTPBReadIO()
 * - 0.6: Added SMUX functions
 *        Renamed HTPBReadIO to HTPBreadIO
 *        Renamed HTPBWriteIO to HTPBwriteIO
 *        Renamed HTPBSetupIO to HTPBsetupIO
 *        Renamed HTPBReadADC to HTPBreadADC
 *        Renamed HTPBReadAllADC to HTPBreadAllADC
 *        Renamed HTPBSetSamplingTime to HTPBsetSamplingTime
 * - 0.7: Changed HTPBreadAllADC to use pass by reference instead of tIntArray to reduce memory overhead.<br>
 *        Removed HTPB_SMUXData, reuses HTPB_I2CReply to reduce memory overhead.
 * - 0.8: Changed type of masks from signed byte to unsigned byte to prevent truncation in ROBOTC 1.9x
 * - 0.9: Replaced functions requiring SPORT/MPORT macros
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 24 September 2009
 * \version 0.9
 * \example hitechnic-protoboard-test1.c
 * \example hitechnic-protoboard-test2.c
 * \example hitechnic-protoboard-test3.c
 * \example hitechnic-protoboard-SMUX-test1.c
 * \example hitechnic-protoboard-SMUX-test2.c
 * \example hitechnic-protoboard-exp1.c
 * \example hitechnic-protoboard-exp2.c
 * \example hitechnic-protoboard-exp3.c
 * \example hitechnic-protoboard-exp4.c
 * \example hitechnic-protoboard-exp5.c
 * \example hitechnic-protoboard-exp6a.c
 * \example hitechnic-protoboard-exp6b.c
 * \example hitechnic-protoboard-exp7.c
 * \example hitechnic-protoboard-exp8.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define HTPB_I2C_ADDR 0x02      /*!< Protoboard I2C device address */
#define HTPB_OFFSET   0x42
#define HTPB_A0_U     0x00      /*!< Address of upper bits of first ADC, bits 9-2 */
#define HTPB_A0_L     0x01      /*!< Address of lower bits of first ADC, bits 1-0 */
#define HTPB_DIGIN    0x0A      /*!< Address of digital inputs */
#define HTPB_DIGOUT   0x0B      /*!< Address of digital outputs */
#define HTPB_DIGCTRL  0x0C      /*!< Controls direction of digital ports */
#define HTPB_SRATE    0x0D      /*!< Controls sample rate, default set to 10ms */

tByteArray HTPB_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray HTPB_I2CReply;      /*!< Array to hold I2C reply data */

ubyte HTPBreadIO(tSensors link, ubyte mask);
bool HTPBwriteIO(tSensors link, ubyte mask);
bool HTPBsetupIO(tSensors link, ubyte mask);
int HTPBreadADC(tSensors link, byte channel, byte width);
bool HTPBreadAllADC(tSensors link, int &adch0, int &adch1, int &adch2, int &adch3, int &adch4, byte width);
bool HTPBsetSamplingTime(tSensors link, byte interval);

#ifdef __HTSMUX_SUPPORT__
ubyte HTPBreadIO(tMUXSensor muxsensor, ubyte mask);
int HTPBreadADC(tMUXSensor muxsensor, byte channel, byte width);
bool HTPBreadAllADC(tMUXSensor muxsensor, int &adch0, int &adch1, int &adch2, int &adch3, int &adch4, byte width);

tConfigParams HTPB_config = {HTSMUX_CHAN_I2C + HTSMUX_CHAN_9V, 14, 0x02, 0x42}; /*!< Array to hold SMUX config data for sensor */
#endif // __HTSMUX_SUPPORT__

/**
 * Read the values of the digital inputs as specified by the mask.
 * @param link the HTPB port number
 * @param mask the specified digital ports
 */
ubyte HTPBreadIO(tSensors link, ubyte mask) {
  memset(HTPB_I2CRequest, 0, sizeof(tByteArray));

  HTPB_I2CRequest[0] = 2;                         // Message size
  HTPB_I2CRequest[1] = HTPB_I2C_ADDR;             // I2C Address
  HTPB_I2CRequest[2] = HTPB_OFFSET + HTPB_DIGIN;  // Start digital output read address

  writeI2C(link, HTPB_I2CRequest, HTPB_I2CReply, 1);

  return HTPB_I2CReply[0] & mask;
}


/**
 * Read the values of the digital inputs as specified by the mask.
 * @param muxsensor the SMUX sensor port number
 * @param mask the specified digital ports
 */
#ifdef __HTSMUX_SUPPORT__
ubyte HTPBreadIO(tMUXSensor muxsensor, ubyte mask) {
	memset(HTPB_I2CReply, 0, sizeof(tByteArray));

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, HTPB_config);

  if (!HTSMUXreadPort(muxsensor, HTPB_I2CReply, 1, HTPB_DIGIN))
    return 0;

  return HTPB_I2CReply[0] & mask;
}
#endif // __HTSMUX_SUPPORT__


/**
 * Write the values the digital outpus as specified by the mask.
 * @param link the HTPB port number
 * @param mask the specified digital ports
 * @return true if no error occured, false if it did
 */
bool HTPBwriteIO(tSensors link, ubyte mask) {
  memset(HTPB_I2CRequest, 0, sizeof(tByteArray));

  HTPB_I2CRequest[0] = 3;                         // Message size
  HTPB_I2CRequest[1] = HTPB_I2C_ADDR;             // I2C Address
  HTPB_I2CRequest[2] = HTPB_OFFSET + HTPB_DIGOUT; // Start digital output read address
  HTPB_I2CRequest[3] = mask;                      // The specified digital ports


  return writeI2C(link, HTPB_I2CRequest);
}


/**
 * Configure the ports for input or output according to the mask.
 * @param link the HTPB port number
 * @param mask the specified digital ports, 0 = input, 1 = output
 * @return true if no error occured, false if it did
 */
bool HTPBsetupIO(tSensors link, ubyte mask) {
  memset(HTPB_I2CRequest, 0, sizeof(tByteArray));

  HTPB_I2CRequest[0] = 3;                           // Message size
  HTPB_I2CRequest[1] = HTPB_I2C_ADDR;               // I2C Address
  HTPB_I2CRequest[2] = HTPB_OFFSET + HTPB_DIGCTRL;  // Start digital input/output control address
  HTPB_I2CRequest[3] = mask;                        // The specified digital ports

  return writeI2C(link, HTPB_I2CRequest);
}


/**
 * Read the value of the specified analogue channel.
 * @param link the HTPB port number
 * @param channel the specified ADC channel
 * @param width the bit width of the result, can be either 8 or 10
 * @return the value of the ADC channel, or -1 if an error occurred
 */
int HTPBreadADC(tSensors link, byte channel, byte width) {
  memset(HTPB_I2CRequest, 0, sizeof(tByteArray));

  int _adcVal = 0;
  HTPB_I2CRequest[0] = 2;                                       // Message size
  HTPB_I2CRequest[1] = HTPB_I2C_ADDR;                           // I2C Address
  HTPB_I2CRequest[2] = HTPB_OFFSET + HTPB_A0_U + (channel * 2); // Start digital output read address
                                                                    // with channel offset
  if (!writeI2C(link, HTPB_I2CRequest, HTPB_I2CReply, 2))
    return -1;

  // Convert the bytes into and int
  // 1st byte contains bits 9-2 of the channel's value
  // 2nd byte contains bits 1-0 of the channel's value
  // We'll need to shift the 1st byte left by 2 and or 2nd byte onto it.
  // If 8 bits is all we want, we just return the first byte and be done with it.
  if (width == 8)
    _adcVal = HTPB_I2CReply[0];
  else
    _adcVal = (HTPB_I2CReply[0] * 4) + HTPB_I2CReply[1];

  return _adcVal;
}


/**
 * Read the value of the specified analogue channel.
 * @param muxsensor the SMUX sensor port number
 * @param channel the specified ADC channel
 * @param width the bit width of the result, can be either 8 or 10
 * @return the value of the ADC channel, or -1 if an error occurred
 */
#ifdef __HTSMUX_SUPPORT__
int HTPBreadADC(tMUXSensor muxsensor, byte channel, byte width) {
  int _adcVal = 0;
	memset(HTPB_I2CReply, 0, sizeof(tByteArray));

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, HTPB_config);

  if (!HTSMUXreadPort(muxsensor, HTPB_I2CReply, 2, HTPB_A0_U + (channel * 2)))
    return -1;

  // Convert the bytes into and int
  // 1st byte contains bits 9-2 of the channel's value
  // 2nd byte contains bits 1-0 of the channel's value
  // We'll need to shift the 1st byte left by 2 and or 2nd byte onto it.
  // If 8 bits is all we want, we just return the first byte and be done with it.
  if (width == 8)
    _adcVal = HTPB_I2CReply[0];
  else
    _adcVal = (HTPB_I2CReply[0] * 4) + HTPB_I2CReply[1];

  return _adcVal;
}
#endif // __HTSMUX_SUPPORT__


/**
 * This function read the value of all of the analogue channels.
 * @param link the HTPB port number
 * @param adch0 parameter to hold value for ad channel 0
 * @param adch1 parameter to hold value for ad channel 1
 * @param adch2 parameter to hold value for ad channel 2
 * @param adch3 parameter to hold value for ad channel 3
 * @param adch4 parameter to hold value for ad channel 4
 * @param width the bit width of the result, can be either 8 or 10
 * @return true if no error occured, false if it did
 */
bool HTPBreadAllADC(tSensors link, int &adch0, int &adch1, int &adch2, int &adch3, int &adch4, byte width) {
  memset(HTPB_I2CRequest, 0, sizeof(tByteArray));

  HTPB_I2CRequest[0] = 2;                       // Message size
  HTPB_I2CRequest[1] = HTPB_I2C_ADDR;           // I2C Address
  HTPB_I2CRequest[2] = HTPB_OFFSET + HTPB_A0_U; // Start digital output read address

  if (!writeI2C(link, HTPB_I2CRequest, HTPB_I2CReply, 10))
    return false;

  // Convert the bytes into and int
  // 1st byte contains bits 9-2 of the channel's value
  // 2nd byte contains bits 1-0 of the channel's value
  // We'll need to shift the 1st byte left by 2 and or 2nd byte onto it.
  // If 8 bits is all we want, we just return the first byte and be done with it.
  if (width == 8) {
    adch0 = (int)HTPB_I2CReply[0];
    adch1 = (int)HTPB_I2CReply[2];
    adch2 = (int)HTPB_I2CReply[4];
    adch3 = (int)HTPB_I2CReply[6];
    adch4 = (int)HTPB_I2CReply[8];
  } else {
    adch0 = ((int)HTPB_I2CReply[0] << 2) + (int)HTPB_I2CReply[1];
    adch1 = ((int)HTPB_I2CReply[2] << 2) + (int)HTPB_I2CReply[3];
    adch2 = ((int)HTPB_I2CReply[4] << 2) + (int)HTPB_I2CReply[5];
    adch3 = ((int)HTPB_I2CReply[6] << 2) + (int)HTPB_I2CReply[7];
    adch4 = ((int)HTPB_I2CReply[8] << 2) + (int)HTPB_I2CReply[9];
  }
  return true;
}


/**
 * This function read the value of all of the analogue channels.
 * @param muxsensor the SMUX sensor port number
 * @param adch0 parameter to hold value for ad channel 0
 * @param adch1 parameter to hold value for ad channel 1
 * @param adch2 parameter to hold value for ad channel 2
 * @param adch3 parameter to hold value for ad channel 3
 * @param adch4 parameter to hold value for ad channel 4
 * @param width the bit width of the result, can be either 8 or 10
 * @return true if no error occured, false if it did
 */
#ifdef __HTSMUX_SUPPORT__
bool HTPBreadAllADC(tMUXSensor muxsensor, int &adch0, int &adch1, int &adch2, int &adch3, int &adch4, byte width) {
	memset(HTPB_I2CReply, 0, sizeof(tByteArray));

  if (HTSMUXSensorTypes[muxsensor] != HTSMUXSensorCustom)
    HTSMUXconfigChannel(muxsensor, HTPB_config);

  if (!HTSMUXreadPort(muxsensor, HTPB_I2CReply, 10, HTPB_A0_U))
    return false;

  // Convert the bytes into and int
  // 1st byte contains bits 9-2 of the channel's value
  // 2nd byte contains bits 1-0 of the channel's value
  // We'll need to shift the 1st byte left by 2 and or 2nd byte onto it.
  // If 8 bits is all we want, we just return the first byte and be done with it.
  if (width == 8) {
    adch0 = (int)HTPB_I2CReply[0];
    adch1 = (int)HTPB_I2CReply[2];
    adch2 = (int)HTPB_I2CReply[4];
    adch3 = (int)HTPB_I2CReply[6];
    adch4 = (int)HTPB_I2CReply[8];
  } else {
    adch0 = ((int)HTPB_I2CReply[0] << 2) + (int)HTPB_I2CReply[1];
    adch1 = ((int)HTPB_I2CReply[2] << 2) + (int)HTPB_I2CReply[3];
    adch2 = ((int)HTPB_I2CReply[4] << 2) + (int)HTPB_I2CReply[5];
    adch3 = ((int)HTPB_I2CReply[6] << 2) + (int)HTPB_I2CReply[7];
    adch4 = ((int)HTPB_I2CReply[8] << 2) + (int)HTPB_I2CReply[9];
  }
  return true;
}
#endif // __HTSMUX_SUPPORT__


/**
 * This function configured the time between samples. This value is not stored permanently.
 * @param link the HTPB port number
 * @param interval a value between 4 and 100ms, default is 10ms
 * @return true if no error occured, false if it did
 */
bool HTPBsetSamplingTime(tSensors link, byte interval) {
  memset(HTPB_I2CRequest, 0, sizeof(tByteArray));

  // Correct the value of the interval if it is out of bounds
  if (interval < 4) interval = 4;
  if (interval > 100) interval = 100;

  HTPB_I2CRequest[0] = 3;                       // Message size
  HTPB_I2CRequest[1] = HTPB_I2C_ADDR;           // I2C Address
  HTPB_I2CRequest[2] = HTPB_OFFSET + HTPB_A0_U; // Start sampling time address
  HTPB_I2CRequest[3] = interval;                // Sample time interval

  // Correct the value of the interval if it is out of bounds
  if (interval < 4) HTPB_I2CRequest[3] = 4;
  if (interval > 100) HTPB_I2CRequest[3] = 100;

  if (!writeI2C(link, HTPB_I2CRequest))
    return false;

  return true;
}

#endif // __HTPB_H__

/*
 * $Id: hitechnic-protoboard.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
