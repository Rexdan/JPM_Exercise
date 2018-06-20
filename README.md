# JPM_Exercise

## Report Printer
*This was written in Java 1.8.0_144 and built on an embedded maven environment--version 3.3.9/1.8.2.20171007-0216--for Eclipse*

## Instructions For Running Program
1. Add the data files in csv format and with the same header that was provided in the assessment document to /src/main/resources/data_sources
2. Run a maven build with "clean", "install", and "skip tests" selected (did not create Test application for the purpose of this assessment).
3. Upon successfully building the project, simply run the compiled program.

### Assumptions
1. SGP is not a recognized currency according to this website and the code *will* throw an exception when it encounters this: https://www.xe.com/iso4217.php
2. "Weekend" means days that are *not* in the work week for AED or SAR.
3. All that this report would show is all of the information pertaining to that record with two additional fields (TradePrice and Rank).
4. Rather than generating these and then writing to a file, they are simply printed in the system console as per the instructions.

### Notes
1. If using your own data files, please use the same header/file format in the included sample (CSV) files.
2. The library that I chose to go with is apache.commons because it is fairly standardized.
3. There are custom exceptions that get thrown letting you know that there is something off with a field or two.
4. Those exceptions are caught and do not stop the code flow.
5. Would have liked to make the printout more "pretty" and add more documentation, but I got home not too long ago and was told that the sooner this got done the better.

### Parting Thoughts
As was said in the exercise, "Treat this exercise as if it was a task you were implementing as part of a normal working day," and that is what I aimed to do :)
