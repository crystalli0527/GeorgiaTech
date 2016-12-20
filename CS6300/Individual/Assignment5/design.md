None of the intefaces shall be used by the Customer, only the manager. 

Credit Card is not a class because information should not be stored, therefore the information on the credit card simply flows from the scanner to the processing company. Only the manager uses the credit card scanner.

Transactions produce a reciept, which are interfaced to the email server. 

A customer has a transacation, but each transaction only has 1 customer.

The videocam is only used by the Manager to read cards. 

The card printer is only used by the Manager to print cards using the Card Printer. 

VIP Customer is a subclass of Customer in this example so that the 10% discount can be tracked and applied easily.

Emails are sent when a customer spends $300 in a calendar year (VIP status) or $30 or more in a transcation ($3 credit). These are done via the transaction, interfaced to the email server. The transaction will inherit from the Customer the current amount spent in the current year for VIP status calculations. 
