/*!@addtogroup HiTechnic
 * @{
 * @defgroup HTSPB SuperPro Prototype Board
 * HiTechnic SuperPro Prototype Board
 * @{
 */
/*
 * $Id: hitechnic-superpro.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __HTSPB_H__
#define __HTSPB_H__
/** \file hitechnic-superpro.h
 * \brief HiTechnic SuperPro Prototype Board driver
 *
 * hitechnic-superpro.h provides an API for the HiTechnic SuperPro Proto Board.
 *
 * Changelog:
 * - 0.1: Initial release
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Gustav Jansson (gus_at_hitechnic.com)
 * \date 10 October 2011
 * \version 0.1
 * \example hitechnic-superpro-exp1.c
 * \example hitechnic-superpro-exp2.c
 * \example hitechnic-superpro-exp3.c
 * \example hitechnic-superpro-exp4.c
 * \example hitechnic-superpro-exp5.c
 * \example hitechnic-superpro-exp6a.c
 * \example hitechnic-superpro-exp6b.c
 * \example hitechnic-superpro-exp7.c
 * \example hitechnic-superpro-exp8.c
 * \example hitechnic-superpro-exp9.c
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

#define HTSPB_I2C_ADDR 0x10      /*!< Protoboard I2C device address */
#define HTSPB_OFFSET   0x42
#define HTSPB_A0_U     0x00      /*!< Address of upper bits of first ADC, bits 9-2 */
#define HTSPB_A0_L     0x01      /*!< Address of lower bits of first ADC, bits 1-0 */
#define HTSPB_DIGIN    0x0A      /*!< Address of digital inputs */
#define HTSPB_DIGOUT   0x0B      /*!< Address of digital outputs */
#define HTSPB_DIGCTRL  0x0C      /*!< Controls direction of digital ports */
#define HTSPB_STROBE   0x0E      /*!< Address of strobe outputs, bits 6-0 */
#define HTSPB_LED      0x0F      /*!< Address of on-board led outputs, bits 1-0 */
#define HTSPB_O0MODE   0x10      /*!< Address of analog output 0 mode*/
#define HTSPB_O0FREQ   0x11      /*!< Address of analog output 0 frequency*/
#define HTSPB_O0VOLT   0x13      /*!< Address of analog output 0 voltage*/
#define HTSPB_O1MODE   0x15      /*!< Address of analog output 1 mode*/
#define HTSPB_O1FREQ   0x16      /*!< Address of analog output 1 frequency*/
#define HTSPB_O1VOLT   0x18      /*!< Address of analog output 1 voltage*/

#define HTSPB_DACO0    0x10      /*!< Address of analog parameters output O0 */
#define HTSPB_DACO1    0x15      /*!< Address of analog parameters output O1 */


// SuperPro Analog output modes
#define DAC_MODE_DCOUT        0 /*!< Steady (DC) voltage output. */
#define DAC_MODE_SINEWAVE     1 /*!< Sine wave output. */
#define DAC_MODE_SQUAREWAVE   2 /*!< Square wave output. */
#define DAC_MODE_SAWPOSWAVE   3 /*!< Positive going sawtooth output. */
#define DAC_MODE_SAWNEGWAVE   4 /*!< Negative going sawtooth output. */
#define DAC_MODE_TRIANGLEWAVE 5 /*!< Triangle wave output. */
#define DAC_MODE_PWMVOLTAGE   6 /*!< PWM square wave output. */



tByteArray HTSPB_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray HTSPB_I2CReply;      /*!< Array to hold I2C reply data */

ubyte HTSPBreadIO(tSensors link, ubyte mask);
bool HTSPBwriteIO(tSensors link, ubyte mask);
bool HTSPBsetupIO(tSensors link, ubyte mask);
int HTSPBreadADC(tSensors link, byte channel, byte width);
bool HTSPBreadAllADC(tSensors link, int &adch0, int &adch1, int &adch2, int &adch3, int &adch4, byte width);
bool HTSPBsetSamplingTime(tSensors link, byte interval);

/**
 * Read the values of the digital inputs as specified by the mask.
 * @param link the HTSPB port number
 * @param mask the specified digital ports
 * @return 8 bits representing the state of the specified IOs
 */
ubyte HTSPBreadIO(tSensors link, ubyte mask) {
  memset(HTSPB_I2CRequest, 0, sizeof(tByteArray));

  HTSPB_I2CRequest[0] = 2;                         // Message size
  HTSPB_I2CRequest[1] = HTSPB_I2C_ADDR;             // I2C Address
  HTSPB_I2CRequest[2] = HTSPB_OFFSET + HTSPB_DIGIN;  // Start digital output read address

  if (!writeI2C(link, HTSPB_I2CRequest, HTSPB_I2CReply, 1))
    return 0;

  return HTSPB_I2CReply[0] & mask;
}


/**
 * Write the values the digital outpus as specified by the mask.
 * @param link the HTSPB port number
 * @param mask the specified digital ports
 * @return true if no error occured, false if it did
 */
bool HTSPBwriteIO(tSensors link, ubyte mask) {
  memset(HTSPB_I2CRequest, 0, sizeof(tByteArray));

  HTSPB_I2CRequest[0] = 3;                         // Message size
  HTSPB_I2CRequest[1] = HTSPB_I2C_ADDR;             // I2C Address
  HTSPB_I2CRequest[2] = HTSPB_OFFSET + HTSPB_DIGOUT; // Start digital output read address
  HTSPB_I2CRequest[3] = mask;                      // The specified digital ports


  return writeI2C(link, HTSPB_I2CRequest);
}


/**
 * Configure the ports for input or output according to the mask.
 * @param link the HTSPB port number
 * @param mask the specified digital ports, 0 = input, 1 = output
 * @return true if no error occured, false if it did
 */
bool HTSPBsetupIO(tSensors link, ubyte mask) {
  memset(HTSPB_I2CRequest, 0, sizeof(tByteArray));

  HTSPB_I2CRequest[0] = 3;                           // Message size
  HTSPB_I2CRequest[1] = HTSPB_I2C_ADDR;               // I2C Address
  HTSPB_I2CRequest[2] = HTSPB_OFFSET + HTSPB_DIGCTRL;  // Start digital input/output control address
  HTSPB_I2CRequest[3] = mask;                        // The specified digital ports

  return writeI2C(link, HTSPB_I2CRequest);
}


/**
 * Read the value of the specified analogue channel.
 * @param link the HTSPB port number
 * @param channel the specified ADC channel
 * @param width the bit width of the result, can be either 8 or 10
 * @return the value of the ADC channel, or -1 if an error occurred
 */
int HTSPBreadADC(tSensors link, byte channel, byte width) {
  memset(HTSPB_I2CRequest, 0, sizeof(tByteArray));

  int _adcVal = 0;
  HTSPB_I2CRequest[0] = 2;                                       // Message size
  HTSPB_I2CRequest[1] = HTSPB_I2C_ADDR;                           // I2C Address
  HTSPB_I2CRequest[2] = HTSPB_OFFSET + HTSPB_A0_U + (channel * 2); // Start digital output read address
                                                                    // with channel offset
  if (!writeI2C(link, HTSPB_I2CRequest, HTSPB_I2CReply, 2))
    return -1;

  // Convert the bytes into and int
  // 1st byte contains bits 9-2 of the channel's value
  // 2nd byte contains bits 1-0 of the channel's value
  // We'll need to shift the 1st byte left by 2 and or 2nd byte onto it.
  // If 8 bits is all we want, we just return the first byte and be done with it.
  if (width == 8)
    _adcVal = HTSPB_I2CReply[0];
  else
    _adcVal = (HTSPB_I2CReply[0] * 4) + HTSPB_I2CReply[1];

  return _adcVal;
}



/**
 * This function read the value of all of the analogue channels.
 * @param link the HTSPB port number
 * @param adch0 parameter to hold value for ad channel 0
 * @param adch1 parameter to hold value for ad channel 1
 * @param adch2 parameter to hold value for ad channel 2
 * @param adch3 parameter to hold value for ad channel 3
 * @param width the bit width of the result, can be either 8 or 10
 * @return true if no error occured, false if it did
 */
bool HTSPBreadAllADC(tSensors link, int &adch0, int &adch1, int &adch2, int &adch3, byte width) {
  memset(HTSPB_I2CRequest, 0, sizeof(tByteArray));

  HTSPB_I2CRequest[0] = 2;                       // Message size
  HTSPB_I2CRequest[1] = HTSPB_I2C_ADDR;           // I2C Address
  HTSPB_I2CRequest[2] = HTSPB_OFFSET + HTSPB_A0_U; // Start digital output read address

  if (!writeI2C(link, HTSPB_I2CRequest, HTSPB_I2CReply, 10))
    return false;

  // Convert the bytes into and int
  // 1st byte contains bits 9-2 of the channel's value
  // 2nd byte contains bits 1-0 of the channel's value
  // We'll need to shift the 1st byte left by 2 and or 2nd byte onto it.
  // If 8 bits is all we want, we just return the first byte and be done with it.
  if (width == 8) {
    adch0 = (int)HTSPB_I2CReply[0];
    adch1 = (int)HTSPB_I2CReply[2];
    adch2 = (int)HTSPB_I2CReply[4];
    adch3 = (int)HTSPB_I2CReply[6];
  } else {
    adch0 = ((int)HTSPB_I2CReply[0] << 2) + (int)HTSPB_I2CReply[1];
    adch1 = ((int)HTSPB_I2CReply[2] << 2) + (int)HTSPB_I2CReply[3];
    adch2 = ((int)HTSPB_I2CReply[4] << 2) + (int)HTSPB_I2CReply[5];
    adch3 = ((int)HTSPB_I2CReply[6] << 2) + (int)HTSPB_I2CReply[7];
  }
  return true;
}



/**
 * Write to the analog output.
 * @param link the HTSPB port number
 * @param dac the specified analog port, use HTSPB_DACO0 or HTSPB_DACO1
 * @param mode the analog mode
 * @param freq the analog frequency from 1 to 8193
 * @param volt the analog voltage from 0 to 1023 (for 0 to 3.3v)
 * @return true if no error occured, false if it did
 */
bool HTSPBwriteAnalog(tSensors link, byte dac, byte mode, int freq, int volt) {
  memset(HTSPB_I2CRequest, 0, sizeof(tByteArray));

  HTSPB_I2CRequest[0] = 7;                          // Message size
  HTSPB_I2CRequest[1] = HTSPB_I2C_ADDR;             // I2C Address
  HTSPB_I2CRequest[2] = HTSPB_OFFSET + dac;         // Start analog output write address
  HTSPB_I2CRequest[3] = mode;                       // The specified analog mode
  HTSPB_I2CRequest[4] = freq/256;                   // High byte of frequency
  HTSPB_I2CRequest[5] = freq&255;                   // Low byte of frequency
  HTSPB_I2CRequest[6] = volt/4;                     // High 8 bits of voltage
  HTSPB_I2CRequest[7] = volt&3;                     // Low 2 bits of voltage

  return writeI2C(link, HTSPB_I2CRequest);
}



#endif // __HTSPB_H__

/*
 * $Id: hitechnic-superpro.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
