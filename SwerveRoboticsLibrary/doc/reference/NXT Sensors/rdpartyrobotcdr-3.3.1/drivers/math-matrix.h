/*!@addtogroup other
 * @{
 * @defgroup matrixfloat Matrix Library
 * matrix Math Library
 * @{
 */

/*
 * $Id: math-matrix.h 133 2013-03-10 15:15:38Z xander $
 */

#ifndef __MATH_MATRIX_FLOAT_H__
#define __MATH_MATRIX_FLOAT_H__
/** \file math-matrix.h
 * \brief Matrix library
 *
 * math-matrix.h provides a number of frequently used functions that are useful for
 * doing math with matrices.
 * Taken from http://playground.arduino.cc/Code/MatrixMath and ported to ROBOTC.
 * License: You may use this code as you wish, provided you give credit where its due.
 *
 * THIS CODE WILL ONLY WORK WITH ROBOTC VERSION 3.59 AND HIGHER. 

 *
 * Changelog:
 * - 0.1: Initial release
 *
 * \author Charlie Matlack
 * \author RobH45345
 * \author Xander Soldaat (xander_at_botbench.com)
 * \date 2 March 2013
 * \version 0.1
 */

#pragma systemFile

#define NR_END 1

#define MATRIX_MAX_SIZE 10


/**
 * Prints a nicely formatted version of the matrix to the debugstream
 *
 * This function is for floats
 * @param matrix the matrix to be printed
 * @param numRows the number of rows in the matrix
 * @param numCols the number of columns in the matrix
 * @param label the label to use when printing the matrix
 */
void matrixPrintF(float* matrix, int numRows, int numCols, char* label){
  // matrixA = input matrix (numRowsA x n)
  int i,j;
  writeDebugStreamLine(label);
  for (i=0; i<numRows; i++){
    for (j=0;j<numCols;j++){
      writeDebugStream("%f", matrix[numCols*i+j]);
      writeDebugStream("\t");
    }
    writeDebugStreamLine("");
  }
}


/**
 * Prints a nicely formatted version of the matrix to the debugstream
 *
 * This function is for longs
 * @param matrix the matrix to be printed
 * @param numRows the number of rows in the matrix
 * @param numCols the number of columns in the matrix
 * @param label the label to use when printing the matrix
 */
void matrixPrintL(long* matrix, int numRows, int numCols, char* label){
  // matrixA = input matrix (numRowsA x n)
  int i,j;
  writeDebugStreamLine(label);
  for (i=0; i<numRows; i++){
    for (j=0;j<numCols;j++){
      writeDebugStream("%d", matrix[numCols*i+j]);
      writeDebugStream("\t");
    }
    writeDebugStreamLine("");
  }
}


/**
 * Copies all the values from one matrix into another
 *
 * This function is for floats
 * @param source the matrix which is to be copied
 * @param numRows the number of rows in the source matrix
 * @param numCols the number of columns in the source matrix
 * @param destination the matrix to copy to
 */
void matrixCopyF(float* source, int numRows, int numCols, float* destination)
{
  memcpy(destination, source, numRows * numCols * sizeof(float));
}


/**
 * Copies all the values from one matrix into another
 *
 * This function is for longs
 * @param source the matrix which is to be copied
 * @param numRows the number of rows in the source matrix
 * @param numCols the number of columns in the source matrix
 * @param destination the matrix to copy to
 */
void matrixCopyL(long* source, int numRows, int numCols, long* destination)
{
  memcpy(destination, source, numRows * numCols * sizeof(long));
}


/**
 * Copies all the values from one matrix into another
 *
 * This function is for floats
 * @param matrixA the first matrix to be multiplied
 * @param matrixB the second matrix to be multiplied
 * @param numRowsA the number of rows in the first matrix
 * @param numColsA the number of columns in the first matrix
 * @param numColsB the number of columns in the second matrix
 * @param matrixC the resulting matrix
 */
void matrixMultF(float* matrixA, float* matrixB, int numRowsA, int numColsA, int numColsB, float* matrixC)
{
  int i, j, k;
  for (i = 0; i < numRowsA; i++)
    for(j = 0; j < numColsB; j++)
  {
    matrixC[numColsB * i + j] = 0;
    for (k = 0; k < numColsA; k++)
      matrixC[numColsB*i+j]= matrixC[numColsB*i+j]+matrixA[numColsA*i+k]*matrixB[numColsB*k+j];
  }
}


/**
 * Copies all the values from one matrix into another
 *
 * This function is for longs
 * @param matrixA the first matrix to be multiplied
 * @param matrixB the second matrix to be multiplied
 * @param numRowsA the number of rows in the first matrix
 * @param numColsA the number of columns in the first matrix
 * @param numColsB the number of columns in the second matrix
 * @param matrixC the resulting matrix
 */
void matrixMultL(long* matrixA, long* matrixB, int numRowsA, int numColsA, int numColsB, long* matrixC)
{
  int i, j, k;
  for (i = 0; i < numRowsA; i++)
    for(j = 0; j < numColsB; j++)
  {
    matrixC[numColsB * i + j] = 0;
    for (k = 0; k < numColsA; k++)
      matrixC[numColsB*i+j]= matrixC[numColsB*i+j]+matrixA[numColsA*i+k]*matrixB[numColsB*k+j];
  }
}


/**
 * Adds values of two matrices
 *
 * This function is for floats
 * @param matrixA the first matrix to be added
 * @param matrixB the second matrix to be added
 * @param numRowsA the number of rows of both matrices
 * @param numColsA the number of columns of both matrices
 * @param matrixC the resulting matrix
 */
void matrixAddF(float* matrixA, float* matrixB, int numRowsA, int numColsA, float* matrixC)
{
  int i, j;
  for (i = 0; i < numRowsA; i++)
    for(j = 0; j < numColsA; j++)
    matrixC[numColsA * i + j] = matrixA[numColsA * i + j] + matrixB[numColsA * i + j];
}


/**
 * Adds values of two matrices
 *
 * This function is for longs
 * @param matrixA the first matrix to be added
 * @param matrixB the second matrix to be added
 * @param numRowsA the number of rows of both matrices
 * @param numColsA the number of columns of both matrices
 * @param matrixC the resulting matrix
 */
void matrixAddL(long* matrixA, long* matrixB, int numRowsA, int numColsA, long* matrixC)
{
  int i, j;
  for (i = 0; i < numRowsA; i++)
    for(j = 0; j < numColsA; j++)
    matrixC[numColsA * i + j] = matrixA[numColsA * i + j] + matrixB[numColsA * i + j];
}


/**
 * Adds values of two matrices
 *
 * This function is for floats
 * @param matrixA the first matrix to be added
 * @param matrixB the second matrix to be added
 * @param numRowsA the number of rows of both matrices
 * @param numColsA the number of columns of both matrices
 * @param matrixC the resulting matrix
 */
void matrixSubtractF(float* matrixA, float* matrixB, int numRowsA, int numColsA, float* matrixC)
{
  int i, j;
  for (i = 0; i < numRowsA; i++)
    for(j = 0; j < numColsA; j++)
    matrixC[numColsA * i + j] = matrixA[numColsA * i +j ] - matrixB[numColsA * i + j];
}


/**
 * Adds values of two matrices
 *
 * This function is for longs
 * @param matrixA the first matrix to be added
 * @param matrixB the second matrix to be added
 * @param numRowsA the number of rows of both matrices
 * @param numColsA the number of columns of both matrices
 * @param matrixC the resulting matrix
 */
void matrixSubtractL(long* matrixA, long* matrixB, int numRowsA, int numColsA, long* matrixC)
{
  int i, j;
  for (i = 0; i < numRowsA; i++)
    for(j = 0; j < numColsA; j++)
    matrixC[numColsA * i + j] = matrixA[numColsA * i +j ] - matrixB[numColsA * i + j];
}


/**
 * Transpose a matrix
 *
 * This function is for floats
 * @param matrixA the first matrix to be transposed
 * @param numRowsA the number of rows of matrixA
 * @param numColsA the number of columns matrixA
 * @param matrixC the resulting matrix
 */
void matrixTransposeF(float* matrixA, int numRowsA, int numColsA, float* matrixC)
{
  int i, j;
  for (i = 0;i < numRowsA; i++)
    for(j = 0; j < numColsA; j++)
    matrixC[numRowsA * j + i] = matrixA[numColsA * i + j];
}


/**
 * Transpose a matrix
 *
 * This function is for longs
 * @param matrixA the first matrix to be transposed
 * @param numRowsA the number of rows of matrixA
 * @param numColsA the number of columns matrixA
 * @param matrixC the resulting matrix
 */
void matrixTransposeL(long* matrixA, int numRowsA, int numColsA, long* matrixC)
{
  int i, j;
  for (i = 0;i < numRowsA; i++)
    for(j = 0; j < numColsA; j++)
    matrixC[numRowsA * j + i] = matrixA[numColsA * i + j];
}


#endif // __MATH_MATRIX_FLOAT_H__
/*
 * $Id: math-matrix.h 133 2013-03-10 15:15:38Z xander $
 */
/* @} */
/* @} */
