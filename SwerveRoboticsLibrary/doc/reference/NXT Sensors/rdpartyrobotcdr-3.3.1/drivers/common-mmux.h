/*!@addtogroup common_includes
 * @{
 * @defgroup MMUX-common_h MMUX Functions
 * Commonly used types and defines used by Motor MUX drivers.
 * @{
 */


/*
 * $Id: common-mmux.h 133 2013-03-10 15:15:38Z xander $
 */

/** \file common-MMUX.h
 * \brief Commonly used types and defines used by Motor MUX drivers.
 *
 * common-MMUX.h provides a number of frequently used types and defines that are useful for writing
 * Motor MUX drivers.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 *
 * Changelog:
 * - 0.1: Initial release split from common.h
 *
 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 18 January 2011
 * \version 0.1
 */

#pragma systemFile

#ifndef __MMUX_H__
#define __MMUX_H__

#ifndef SPORT
#define SPORT(X)  X / 4         /*!< Convert tMUXSensor to sensor port number */
#endif

#ifndef MPORT
#define MPORT(X)  X % 4         /*!< Convert tMUXSensor to MUX port number */
#endif

/*!< Struct to hold MMUX info */
typedef struct {
  bool initialised;     /*!< Has the MMUX been initialised yet? */
  bool runToTarget[4];  /*!< Indicate whether or not the motor is running to a target */
  long target[4];       /*!< Target we want the motor to run to */
  byte targetUnit[4];   /*!< Type of target we're running to, could be rotation, encoder or seconds */
  bool relTarget[4];    /*!< Whether or not the target is relative to current position */
  bool brake[4];        /*!< Whether or not to use braking or floating to stop motor */
  bool pidcontrol[4];   /*!< Use constant speed or just power control */
  byte ramping[4];      /*!< Ramp the motors, can be up, down, both */
} mmuxDataT;


/*!< MUXmotor type, one for each permutation
 *
 * - mmotor_S1_1 means motor 1 connected to MMUX attached to sensor port 1
 * - mmotor_S4_2 means motor 2 connedted to MMUX attached to sensor port 4
 */
typedef enum {
  mmotor_S1_1 = 0,
  mmotor_S1_2 = 1,
  mmotor_S1_3 = 2,
  mmotor_S1_4 = 3,
  mmotor_S2_1 = 4,
  mmotor_S2_2 = 5,
  mmotor_S2_3 = 6,
  mmotor_S2_4 = 7,
  mmotor_S3_1 = 8,
  mmotor_S3_2 = 9,
  mmotor_S3_3 = 10,
  mmotor_S3_4 = 11,
  mmotor_S4_1 = 12,
  mmotor_S4_2 = 13,
  mmotor_S4_3 = 14,
  mmotor_S4_4 = 15
} tMUXmotor;

mmuxDataT mmuxData[4];  /*!< Holds all the MMUX info, one for each sensor port */

#endif // __MMUX_H__

/*
 * $Id: common-mmux.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
