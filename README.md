# ParamSameTime
This program simulates parameterizing of electronic-control-units (ecu's). 
Three ecu's are shown here which can be parameterized at the same time in an asynchronous way. 
A single param-process can be stopped if the related crash-button has been clicked. Return-value for this ecu is then 999.
If no crash occured, process runs to the end with return-value 0. 
After all ecu's ended, main-result will be printout and simulation stops.
In main-result the result of all single ecu's are reported.
