/*
 * $Id: DriverTemplate.h 133 2013-03-10 15:15:38Z xander $
 */

/** \file <template>.h
 * \brief RobotC <device> Driver
 *
 * <filename>.h provides an API for the <device>.
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 * \author Xander Soldaat (xander_at_botbench.com)
 * \date <date>
 * \version <version>
 */

#pragma systemFile            

#define <DEVICE>_I2C_ADDR 0x02      /*!< <device> I2C device address */
#define <DEVICE>_A0_U     0x42      /*!< some register */

/*! Some define. */
#define DEF_SOMETHING   32

/*! arbStruct struct, <short explanation. */
typedef struct {
	int size;                 /*!< Number of bytes used */
	int arr[DEF_SOMETHING];   /*!< Array containing data */
} arbStruct;

/*
<function prototypes>
*/

/**
 * This function does something
 * @param foo a param to allow baz
 * @return some value
 */
byte arbFunc(byte foo) {
  return foo;
}

/*
 * $Id: DriverTemplate.h 133 2013-03-10 15:15:38Z xander $
 */
