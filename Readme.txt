This program keeps a record of payments,each payment includes a currency and an amount.
It should output a list of all the currency and amounts to the console once per minute.

=> To setup
To setup the running environment you can type "setup.bat" in windows system, or type "setup.sh" in linux system.

=> To start
You can type "java -Xmx256M -Xms256M -cp target/CurrencyProcess.jar com.hsbc.data.CurrencyProcess" to run the program.

=> When running
The input can be typed into the command line, and optionally also be loaded from a file
when starting up.

You can type 1 for colsole input and 2 for file and console input.

In the colsole input mode, you can input currencies according to \"currency amount\" format,
separate by one space char.If you enters invalid input, you'll get the error message below:

(1) If the currency type length is not 3 characters, you will get messages:
    Please use 3 large characters for short name of currency.

(2) If the amount is not a numeric, you will get messages:
    The amount should be numeric.

(3) If the count of separate spaces is not one, you will get messages:
    You should input according to the \"currency amount\" format, separate by one space.

In the file input mode, you can input the filename that contains \"currency amount\" format
currencies. If you enters invalid fileName or the file contains invalid format currencies,
you'll get the error message below:

(1) If you input a invalid filename, you will get messages:
    Input error,file not exist.

(2) If the file contains currency type error, you will get messages:
    Currency type error in line x.(x is the line no.)

(3) If the amount is not a numeric, you will get messages:
    The amount should be numeric in x.(x is the line no.)

(4) If the file contains currency type error, you will get messages:
    There's no currency amount in line x.(x is the line no.)

