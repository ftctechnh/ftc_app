/*!@addtogroup other
 * @{
 * @defgroup mcp23008 Microchip MCP23008
 * Microchip MCP23008
 * @{
 */

/*
 * $Id: microchip-mcp23008.h 133 2013-03-10 15:15:38Z xander $
 */


#ifndef __MCP23008_H__
#define __MCP23008_H__
/** \file microchip-mcp23008.h
 * \brief Microchip MCP23008 driver
 *
 * microchip-mcp23008.h provides an API for the MCP23008 8 port IO expander.
 *
 * Changelog:
 * - 0.3 Rewrite to make use of standard common.h framework.
 * - 0.4 Renamed functions to be inline with new naming standard
 *
 * License: You may use this code as you wish, provided you give credit where it's due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat
 * \date 18 June 2009
 * \version 0.4
 */

#pragma systemFile

#ifndef __COMMON_H__
#include "common.h"
#endif

// Registers
#define MCP_REG_IODIR   0x00    /*!< I/O DIRECTION REGISTER */
#define MCP_REG_IPOL    0x01    /*!< INPUT POLARITY REGISTER */
#define MCP_REG_GPINTEN 0x02    /*!< INTERRUPT-ON-CHANGE CONTROL REGISTER */
#define MCP_REG_DEFVAL  0x03    /*!< DEFAULT COMPARE REGISTER FOR INTERRUPT-ONCHANGE */
#define MCP_REG_INTCON  0x04    /*!< INTERRUPT CONTROL REGISTER */
#define MCP_REG_IOCON   0x05    /*!< CONFIGURATION REGISTER */
#define MCP_REG_GPPU    0x06    /*!< PULL-UP RESISTOR CONFIGURATION REGISTER */
#define MCP_REG_INTF    0x07    /*!< INTERRUPT FLAG (INTF)REGISTER */
#define MCP_REG_INTCAP  0x08    /*!< INTERRUPT CAPTURE REGISTER */
#define MCP_REG_GPIO    0x09    /*!< PORT REGISTER */
#define MCP_REG_OLAT    0x0A    /*!< OUTPUT LATCH REGISTER */

// Special bits in the IOCON register
#define MCP_BIT_INTPOL  1       /*!< This bit sets the polarity of the INT output pin */
#define MCP_BIT_ODR     2       /*!< This bit configures the INT pin as an open-drain output */
#define MCP_BIT_DISSLW  4       /*!< Slew Rate control bit for SDA output */
#define MCP_BIT_SREAD   5       /*!< Sequential Operation mode bit */

#define MCP_I2C_ADDR    0x40    /*!< Default base address (A0-A2 tied to gnd) */

tByteArray MCP23008_I2CRequest;    /*!< Array to hold I2C command data */
tByteArray MCP23008_I2CReply;      /*!< Array to hold I2C reply data */

bool MCP23008setupIO(tSensors link, byte addr, byte mask, byte pullup);
bool MCP23008setupIO(tSensors link, byte addr, byte mask);
byte MCP23008readIO(tSensors link, byte addr, byte mask);
byte MCP23008readIO(tSensors link, byte addr);
bool MCP23008writeIO(tSensors link, byte addr, byte mask);

bool MCP23008writeReg(tSensors link, byte addr, byte reg, byte data);
byte MCP23008readReg(tSensors link, byte addr, byte reg);

/**
 * Write data to the device at the specified register.
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the sensor port number
 * @param addr the address of the MCP23008
 * @param reg the register to write to
 * @param data the data to be written (single byte)
 * @return true if no error occured, false if it did
 */
bool MCP23008writeReg(tSensors link, byte addr, byte reg, byte data) {
  memset(MCP23008_I2CRequest, 0, sizeof(tByteArray));

  MCP23008_I2CRequest[0] = 3;     // Message size
  MCP23008_I2CRequest[1] = addr;  //I2C Address
  MCP23008_I2CRequest[1] = reg;   // Register address
  MCP23008_I2CRequest[1] = data;  // Data to be sent

  return writeI2C(link, MCP23008_I2CRequest, 0);
}

/**
 * Read from the device at the specified register.
 *
 * Note: this is an internal function and should not be called directly.
 * @param link the sensor port number
 * @param addr the address of the MCP23008
 * @param reg the register to write to
 * @return true if no error occured, false if it did
 */
byte MCP23008readReg(tSensors link, byte addr, byte reg) {
  memset(MCP23008_I2CRequest, 0, sizeof(tByteArray));

  MCP23008_I2CRequest[0] = 2;     // Message size
  MCP23008_I2CRequest[1] = addr;  //I2C Address
  MCP23008_I2CRequest[1] = reg;   // Register address

  writeI2C(link, MCP23008_I2CRequest, 0);
  readI2C(link, MCP23008_I2CReply, 1);

  return MCP23008_I2CReply[0];
}

/**
 * Setup the pins as either inputs or outputs as specified by the mask.
 * 0 is input, 1 is output
 * @param link the sensor port number
 * @param addr the address of the MCP23008
 * @param mask the pins to change the configuration for
 * @param pullup the pins to change the internal pullup resistor for
 * @return true if no error occured, false if it did
 */
bool MCP23008setupIO(tSensors link, byte addr, byte mask, byte pullup) {
  if (!MCP23008writeReg(link, addr, MCP_REG_IODIR, mask))
    return false;

  if (!MCP23008writeReg(link, addr, MCP_REG_GPPU, pullup))
    return false;

  return true;
}

/**
 * Setup the pins as either inputs or outputs as specified by the mask.
 * 0 is input, 1 is output
 * @param link the sensor port number
 * @param addr the address of the MCP23008
 * @param mask the pins to change the configuration for
 * @return true if no error occured, false if it did
 */
bool MCP23008setupIO(tSensors link, byte addr, byte mask) {
  if (!MCP23008writeReg(link, addr, MCP_REG_IODIR, mask))
    return false;

  return true;
}

/**
 * Read the states of the pins as specified by the mask
 *
 * @param link the sensor port number
 * @param addr the address of the MCP23008
 * @param mask the pins to get the status for
 * @return the value of the pins
 */
byte MCP23008readIO(tSensors link, byte addr, byte mask) {
  return MCP23008readReg(link, addr, MCP_REG_GPIO) & mask;
}

/**
 * Read the states of all of the pins
 *
 * @param link the sensor port number
 * @param addr the address of the MCP23008
 * @return the value of the pins
 */
byte MCP23008readIO(tSensors link, byte addr) {
  return MCP23008readReg(link, addr, MCP_REG_GPIO);
}

/**
 * Write to the pins specified specified by the mask.
 *
 * @param link the sensor port number
 * @param addr the address of the MCP23008
 * @param mask the state of the pins that is to be written
 * @return the value of the pins
 */
bool MCP23008writeIO(tSensors link, byte addr, byte mask) {
  if (!MCP23008writeReg(link, addr, MCP_REG_OLAT, mask))
    return false;

  return true;
}

#endif // __MCP23008_H__

/*
 * $Id: microchip-mcp23008.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
