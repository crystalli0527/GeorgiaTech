To compile/run:
	Open project in Android Studio and run in emulator or export .apk to run on device.

The following customers are hard coded into the system's DB and will be available on the first app run:
Ralph Hapschatt (ID: 7c86ffee)
Betty Monroe (ID: b59441af)
Everett Scott (ID: cd0f0e05)

To restore DB to a clean state (with hard coded customers):
1. Click/tap on the "Main Menu" label on the main menu to open the on device testing screen.
2. Click/tap on the "CLEAN DB" button.

"CUSTOMER" button lists all customers in the system's DB. "TRANSACTION" button lists all transactions.

Alternatively, you can connect to the SQLite DB on emulator:
	adb -s emulator-5554 shell
	cd /data/data/edu.gatech.seclass.tccart/databases
	sqlite3 TCCart.db

and run SQL queries such as:
	select * from customer;
	select * from transaction_t;
