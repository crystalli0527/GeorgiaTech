# Design Considerations/Comments:
## 1. Purchase processing functionality is assumed to be inside "buys" association (Transaction class).
## 2. Communication with payment-processing service provider that can process credit card transactions is assumed to be a part of the creditCardScanner class and invoked in the readCreditCard() method while a purchase is being made.
## 3. Credit card information is only accessed by creditCardScanner during purchase and not stored anywhere in the system (thus no class to represent credit card inside the system).
## 4. Transaction is an association class of the Buy association (couldn't add association class in jetUML).
## 5. The API to print ID cards is invoked during a user account creation.
