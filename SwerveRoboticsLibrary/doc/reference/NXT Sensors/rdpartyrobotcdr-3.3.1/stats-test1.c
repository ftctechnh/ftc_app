#include "drivers/stats.h"

task main () {
  float x_Phi = 0.0;   // should be 0.494233933 according to Excel
  float X_val = -20;
  float X_mean = -19.80093313;   // mu
  float X_std = 13.77254704;     // sigma

  x_Phi = Phi(X_val, X_mean, X_std);

  nxtDisplayTextLine(2, "Phi(x): %f", x_Phi);
  while(nNxtButtonPressed != kEnterButton) EndTimeSlice();
}
