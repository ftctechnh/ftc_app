/*!@addtogroup other
 * @{
 * @defgroup stats Statistics Library
 * Statistics Library
 * @{
 */

/*
 * $Id: stats.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __STATS_H__
#define __STATS_H__
/** \file stats.h
 * \brief Statistics functions for ROBOTC.
 *
 * stats.h provides some statiscal functions for ROBOTC.
 *
 * Taken from http://www.cs.princeton.edu/introcs/21function/MyMath.java.html
 * and modified to compile under ROBOTC.
 *
 * License: You may use this code as you wish, provided you give credite where its due.
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 *
 * Changelog:
 * - 0.1: Initial release
 *
 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 10 October 2010
 * \version 0.1
 * \example stats-test1.c
 */


/**
 * Error Function, subject to catastrophic cancellation when z
 * is very close to 0
 * @param z the number to be, ehm, error checked.
 * @return the value
 */
float erf(float z) {
	float t = 1.0 / (1.0 + 0.5 * abs(z));

	// use Horner's method
	float ans = 1 - t * exp( -z*z   -   1.26551223 +
																					t * ( 1.00002368 +
																					t * ( 0.37409196 +
																					t * ( 0.09678418 +
																					t * (-0.18628806 +
																					t * ( 0.27886807 +
																					t * (-1.13520398 +
																					t * ( 1.48851587 +
																					t * (-0.82215223 +
																					t * ( 0.17087277))))))))));
	if (z >= 0) return  ans;
	else        return -ans;
}

/**
 * Cumulative normal distribution
 * @param z the number to check
 * @return the probability
 */
float Phi(float z) {
  return 0.5 * (1.0 + erf(z / (sqrt(2.0))));
}

/**
 * Cumulative normal distribution with mean mu and std deviation sigma
 * @param z the number to check
 * @param mu mean value for x
 * @param sigma std dev for x
 * @return the probability
 */
float Phi(float z, float mu, float sigma) {
  return Phi((z - mu) / sigma);
}


/**
 * Random number with standard Gaussian distribution
 * @return random number with standard Gaussian distribution
 */
float gaussian() {
  float U = random[32767]/32767;   // should be a number between 0 and 1.0
  float V = random[32767]/32767;   // should be a number between 0 and 1.0
  return sin(2 * PI * V) * sqrt((-2 * log(1 - U)));  // sin() returns radians, should this be degrees?
}

// random number with Gaussian distribution of mean mu and stddev sigma
float gaussian(float mu, float sigma) {
  return mu + sigma * gaussian();
}


#endif // __STATS_H__

/*
 * $Id: stats.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
