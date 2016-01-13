/*
 * $Id: timer-test1.c 133 2013-03-10 15:15:38Z xander $
 */

/** \file timer.h
 * \brief Additional timers for ROBOTC.
 *
 * timer.h provides additional timers for ROBOTC.  Please note that there is no
 * roll-over checking done at all.  That means that if the program runs for more than
 * around 596 hours, it will roll over and weird stuff will happen.
 *
 * The default number of timers is 10, but this can be changed by defining MAX_TIMERS
 * before this driver is included.
 *
 * License: You may use this code as you wish, provided you give credit where its due.
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 *
 * Changelog:
 * - 0.1: Initial release
 *
 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 18 May 2010
 * \version 0.1
 * \example TMR-test1.c
 */

#include "drivers/timer.h"

task main () {

  // Create two new timer index numbers
  int timer1 = TMRnewTimer();
  int timer2 = TMRnewTimer();

  // Configure timer1 for 2000ms
  TMRsetup(timer1, 2000);

  // Configure timer2 for 5000ms
  TMRsetup(timer2, 5000);

  // Reset and start both timers
  TMRreset(timer1);
  TMRreset(timer2);

  while (true) {
    // If timer1 expires, make a small noise and reset it.
    if (TMRisExpired(timer1)) {
      PlaySound(soundBlip);
      while(bSoundActive) EndTimeSlice();
      TMRreset(timer1);
    }

    // If timer2 expires, make a small noise and reset it.
    if (TMRisExpired(timer2)) {
      PlaySound(soundShortBlip);
      while(bSoundActive) EndTimeSlice();
      TMRreset(timer2);
    }
    EndTimeSlice();
  }
}

/*
 * $Id: timer-test1.c 133 2013-03-10 15:15:38Z xander $
 */
