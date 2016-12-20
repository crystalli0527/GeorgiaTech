# **Design Clarification Document** #

1. Purchase Processing
	- The Transaction class is responsible for purchase processing
2. Purchase and Reward Tracking 
	- The Transaction, CreditDiscount, and VIP_Status classes provide per customer purchase and reward tracking 
3. CustomerID
	- The customerID within Customer class represents 8-digit unique hexadecimal ID
	- The customer class will posesss a method to auto generate the customerID
4. CardPrinter and VideoCamera External Libraries
	- CustomerManager's dependency on CardPrinter and VideoCamera external libraries reflect a relationship
		necessary for printing and scanning customer cards. The external libraries are expected to possess 
		the methods needed for this functionality.  
5. CreditCardScanner and PaymentSystem External Libraries
	- PaymentManager's dependency on CreditCardScanner and PaymentSystem external libraries reflect a relationship
		necessary for scanning a credit card and submitting card info to payment system. The external libraries are expected to possess 
		the methods needed for this functionality.
6. Email Utility
	- The Transaction class depends on the Email utility for sending emails per the three
		requirments: every transaction, earned $3 discount, and achieved VIP status
7. CreditCard no persistence
	- The CreditCard class posessess a method to delete the credit card object upon successful
		submission of credit card info to external payment system.
8. 	CreditDiscount
	- Upon achieving credit discount, the creditExpirationDate class attribute will reflect a month from transaction date. 
	- The Transaction class will update the remaining discount amount per customer
9.	VIP_Status
	- The Transaction class will update a customer's annual purchase totals via the VIP_Status class dependency. 
	