/*!@addtogroup common_includes
 * @{
 * @defgroup light-common_h Light Functions
 * Functions common to light sensor drivers
 * @{
 */

/*
 * $Id: common-light.h 108 2012-09-16 09:06:13Z xander $
 */

#ifndef __LIGHT_COMMON_H__
#define __LIGHT_COMMON_H__

#ifndef __COMMON_H__
#include "common.h"
#endif

/** \file common-light.h
 * \brief Functions common to light sensor drivers
 *
 * common-light.h holds functions common to light sensor drivers.
 * Contains code to convert from RGB colors to HSV colors.
 *
 * \author Mike Henning, Max Bareiss
 * \author Xander Soldaat (minor modifications)
 * \date 27 April 2011
 */

#pragma systemFile


/**
 * Convert RGB colors to HSV
 * @param red the red input value
 * @param green the green input value
 * @param blue the blue input value
 * @param hue the hue output value (from 0 to 365, or -1 if n/a)
 * @param sat the saruration output value (from 0 to 100, or -1 if n/a)
 * @param value the value output value (from 0 to 100)
 * @return void
 */
void RGBtoHSV(float red, float green, float blue, float &hue, float &sat, float &value)
{
	hue = 0;
	sat = 0;
	value = 0;

  //   Value
  float rgb_max = max3(red, green, blue);
  float rgb_min;
  value = rgb_max / 2.56;
  if (value == 0){
    hue = -1;
    sat = -1;
    return;
  }

  //   Saturation
  red /= rgb_max;
  green /= rgb_max;
  blue /= rgb_max;

  rgb_max = max3(red, green, blue);
  rgb_min = min3(red, green, blue);
  sat = (rgb_max - rgb_min) * 100;
  if (sat == 0){
    hue = -1;
    return;
  }

  //   Hue

  red = (red - rgb_min) / (rgb_max - rgb_min);
  green = (green - rgb_min) / (rgb_max - rgb_min);
  blue = (blue - rgb_min) / (rgb_max - rgb_min);

  rgb_max = max3(red, green,blue);
  rgb_min = min3(red, green,blue);

  if (rgb_max == red){
    hue = 0.0 + 60.0*(green-blue);
    if (hue < 0.0){
      hue += 360.0;
    }
  } else if (rgb_max == green){
    hue = 120.0 + 60.0 * (blue-red);
  } else {
    hue = 240.0 + 60.0 * (red-green);
  }
}

#endif __LIGHT_COMMON_H__

/*
 * $Id: common-light.h 108 2012-09-16 09:06:13Z xander $
 */
/* @} */
/* @} */
