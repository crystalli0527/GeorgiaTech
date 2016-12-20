# Use Case Model



**Author**: Team 52
<table>
<tr>
<td>Version</td><td>Description</td>
</tr>
<tr>
<td>V1</td><td>Initial version</td>
</tr>
<tr>
<td>V2</td><td>Updated version (Deliverable 2)</td>
</tr>
<tr>
<td>V3</td><td>Final version (Deliverable 3)</td>
</tr>
</table>


## 1 Use Case Diagram



![](http://i.imgur.com/lLhhQ2Z.png)

## 2 Use Case Descriptions

**Use Case Name:** Create Customer

**UC ID:** Create Customer

- **Requirement Summary:** Use case that allows the user 'manager' to create new customers in the TCCart system.
- **Basic Path:**
	1. Manager opens customer 'create/edit customer' menu from 'main menu'
	2. Enters customer name and email
	3. Manager clicks 'create'
	4. The system accepts the information and creates a Customer account
	5. Customer ID is generated and sent to printer to Print Card
- **Alternative Paths** 
	- If in step 2 the manager cancels the use case by selecting 'clear'
- **Exception Paths:** 
	1. 'Create/Edit customer' menu fails to open
	2. Customer information already exists in step 2
	3. If required fields in step 2 are blank or have invalid characters the system throws error message
	4. External library does not initiate for print card
- **Extension Points:** Use case 'Print Card' is started
- **Triggers:** Manager opens 'Create/Edit customer'	
- **Assumption:** 'Create/Edit' customer is available from the main screen
- **Pre-conditions:** Customer must provide name and email
- **Post-conditions:** 
	1. 8 Hex Customer ID is generated and sent to printer interface
	2. Customer account is created
	3. Customer Card is Printed
- **Scenarios:** Manager has a new customer and adds their information into the TCCart application

----------

**Use Case Name:** Read Customer Card

**UC ID:** Read Customer Card QR code

- **Requirement Summary:** Use case that allows Customer card to be read by QR Code using a Video Camera
- **Basic Path:**
	1. Customer Card QR Code is placed in front of Video Camera
	2. Manager selects option to 'Scan QR'
	2. QR Code is read by Video Camera
	3. External library is initiated
	4. 8 Hex Customer ID is identified
- **Alternative Paths**
	
- **Exception Paths:**
	1. Customer Card is damaged and QR Code cannot be read
	2. Incorrect side of card is read
	3. Video Camera is off or offline
	4. External library does not initiate
- **Extension Points:** Once customer card QR code has been read then
	1. Edit customer information is available
	2. Viewing customer history is available	
- **Triggers:** Card is swiped in front of Video Camera
- **Assumption:** Customer Card exists
- **Pre-conditions:** 
	1. Customer must have existing card 
	2. Card must be provided for scan
- **Post-conditions:** Video Camera reads the the QR Code and interprets the 8 Hex Customer ID
- **Scenarios:** Manager scans the customer card before a transaction so as to associate the purchase to the customer

----------

**Use Case Name:** Edit Customer

**UC ID:** Edit Customer

- **Requirement Summary:** Use case that allows the user 'Manager' to edit customer information
- **Basic Path:**
	1. Manager opens 'Create/Edit customer' menu 
	2. Manager selects to edit any of the following information:
		1. Customer name
		2. Customer email address
	3. Manager saves edited information by clicking 'update'
- **Alternative Paths**
	1. Manager decides to cancel the edit process before saving by selecting 'clear'
- **Exception Paths:**
	1. 'Create/Edit customer' menu fails to open
	2. Incorrect input type in either the name or email fields leads to error
	3. Manager forgets to complete and closes losing the updated information
- **Extension Points:**
- **Triggers:** 'Create/Edit customer' is selected by user 'manager'
- **Assumption:** Customer information already exists
- **Pre-conditions:** 
	1. Customer Card QR Code and Hex ID already scanned from card
	2. Customer account brought up on interface
- **Post-conditions:** Manager edits customer account information (Name, and/or Email)
- **Scenarios:** Customer has changed names or email addresses so the manager edits their information in the application

----------

**Use Case Name:** View Customer Purchase and Reward History

**UC ID:** View Customer Purchase and Reward History

- **Requirement Summary:** Use case that allows user 'Manager' to view customer  purchase and reward history information
- **Basic Path:**
	1. Manager selects to view customer purchase and reward history by clicking on 'View History' from main menu
	2. The manager clicks to 'scan QR' and scans for customer information
		a) Date of the Purchase is displayed
		b) Pre discount amount is displayed
		c) VIP discount is displayed
		d) Credit is displayed
		e) Post Discount amount is displayed

- **Alternative Paths**
	1. Customer has no prior purchases yet to view
	2. Customer has no achieved discounts or credits yet to view
- **Exception Paths:**
	1. Customer 'view history' fails to display	
- **Extension Points:**
- **Triggers:** Manager selects to 'view history'
- **Assumption:** Customer has an account and card
- **Pre-conditions:** Customer QR code on card scanned to open Customer history
- **Post-conditions:** Customer should have previous transactions for there to be a transaction or reward history
- **Scenarios:** Customer requests to view their transaction or rewards history so the Manager selects to show it to the customer

----------

**Use Case Name:** Enter Purchase

**UC ID:** Enter Purchase

- **Requirement Summary:** Use case that allows user 'Manager' to enter purchase and purchase price of transaction
- **Basic Path:**
	1. Manager selects menu option to 'Enter Purchase'
	2. Manager enters purchase price
	1. Manager scans customer card to associate purchase to their account by clicking 'Scan QR'
- **Alternative Paths**
	1. Customer declines to pay and manager cancels purchase
- **Exception Paths:**
 	1. 'Enter Purchase' menu does not open
	2. Manager enters incorrect characters in the amount field
	3. 'Scan QR' fails to read customer information
- **Extension Points:**
	1. Use case Apply VIP Discount is checked
	2. Use case Apply $3 Credit is checked
- **Triggers:** Manager selects to enter purchase
- **Assumption:** Customer has provided items for purchase price determination
- **Pre-conditions:** Customer has selected items to purchase
- **Post-conditions:** Transaction amount is entered into Transactions Interface
- **Scenarios:** Customer purchases 2 teas totaling $8 and the Manager inputs this purchase amount into the system

----------

**Use Case Name:** Calculate Reward Achievements

**UC ID:** Calculate Reward Achievements

- **Requirement Summary:**  Use case that allows determination of if customer achieved either VIP Status or Credit Reward if > $30 purchase has occurred to determine $3 credit achievement OR total yearly purchases > $300 for VIP status achievement 
- **Basic Path:**
	1. Customer purchase amount is checked against > $30 threshold
	
		a) Customer purchase is above threshold and credit is created

		b) Customer purchase is below threshold and credit is not created

	2. Customer yearly purchases are checked against > $300 threshold
	
		a) Customer yearly purchases are above threshold and VIP status is created

		b) Customer yearly purchases are below threshold and VIP status is not created

	3. System displays if credit or discount rewards have been achieved

- **Alternative Paths** None
- **Exception Paths:**
	1. Calculation fails to load and credit is not determined
	2. Calculation fails to load and VIP status is not determined
- **Extension Points:**
	1. Use case Send email for VIP status earned is started
	2. Use case Send email for $3 credit earned is started
- **Triggers:** Purchase amount is input
- **Assumption:** Customer is making a purchase
- **Pre-conditions:** Transaction needs to occur
- **Post-conditions:** $3 Credit Achieved OR VIP Status achieved
- **Scenarios:** Customer makes a purchase and the system needs to determine if VIP or a $3 credit needs to be awarded the system runs this calculation and displays determination

----------

**Use Case Name:** Apply VIP Discount

**UC ID:** Apply VIP Discount

- **Requirement Summary:** Use case for applying a VIP discount
- **Basic Path:**
	1. VIP Status is checked by system
	2. VIP discount is applied to purchase
	3. New Purchase total is determined
- **Alternative Paths**
	1. VIP Status does not exist for customer
- **Exception Paths:**
	1. VIP Status exists but fails to apply
	2. VIP status applies in incorrect order (after $3 credit though should apply prior)
- **Extension Points:** Use case Apply $3 Credit is then checked
- **Triggers:** Purchase total is finalized
- **Assumption:** VIP Discount is applied prior to $3 credit if any exists
- **Pre-conditions:** Customer must be a VIP member
- **Post-conditions:** Customer receives a 10% discount
- **Scenarios:** The VIP customer's transaction receives a 10% discount

----------

**Use Case Name:** Apply $3 Credit

**UC ID:** Apply $3 Credit

- **Requirement Summary:** Use case for applying $3 credit to their purchase
- **Basic Path:**
	1. System applies $3 Credit
	2. Purchase is updated to reflect $3 credit amount
	3. Purchase is finalized
- **Alternative Paths**
	- Customer does not have full $3 credit to apply and partial credit is applied
	- Customer has credit to apply but sale is less than $3
		- Partial credit is applied
		- Remaining balance is kept as credit in customers rewards
- **Exception Paths:**
	1. Credit exists and is selected but fails to apply
	2. Credit exists and is selected but applies in incorrect order (prior to VIP discount but apply after)
- **Extension Points:** Use case Scan Credit card is initiated
- **Triggers:** Manager enters purchase
- **Assumption:** Customer is making a purchase
- **Pre-conditions:** Customer must have a $3 discount credit for their account
- **Post-conditions:** Customer receives $3 off their purchase if more than $3, if less than $3 it is kept on their account for future purchases
- **Scenarios:** Customer buys a $4 tea and the final purchase price is discounted to $1

----------

**Use Case Name:** Scan Credit Card

**UC ID:** Scan Credit Card

- **Requirement Summary:** Use case for finalizing sale with credit card scan
- **Basic Path:**
	1. Manager scans a customer's credit card after a purchase and clicks 'scan cc/process' from enter purchase amount screen
	2. External library is accessed
	2. Card information is sent through to payment gateway for processing
	3. Processing message is returned 'Payment processed successfully $ ' and 'discount applied' if any applied
- **Alternative Paths**
	1. Card is expired
		- Card cannot go through
		- Purchase and amount of transaction is cancelled
		- Any rewards applications are cancelled
- **Exception Paths:**
	1. No amount was entered and error message 'no amount entered' 
	2. No QR scan was previously performed and displays 'Please scan the QR code to proceed'
	3. Card is improperly scanned
	4. Scanner fails to read card
	5. Scanner library fails to load	
	6. Processing gateway fails to process transaction and returns error
- **Extension Points:**
	1. Use case Send email for transaction is initialized
- **Triggers:** Manager scans card through scanner
- **Assumption:** Customer has made a purchase
- **Pre-conditions:** Customer needs to provide a valid credit card
- **Post-conditions:** Credit Card information is sent to payment gateway through external library
- **Scenarios:** Customer buys a tea and pays with a credit card, which the Manager scans

----------

**Use Case Name:** Send email for transaction

**UC ID:** Send email for transaction

- **Requirement Summary:** Use case for email server sending purchase confirmation emails
- **Basic Path:**
	1. Transaction is finalized with successful credit card scan and processing
	2. Email server is initialized through external library
	3. Customer email is accessed from their account
	4. Email template is accessed and filled with customer information
	5. Transaction information is updated into template
	6. Email is finalized and sent
- **Alternative Paths** None
- **Exception Paths:**
	1. Email server external library fails to load
	2. User has provided an invalid email (unconfirmed email)
	3. Users email is account is full and message fails
- **Extension Points:** None
- **Triggers:** Credit card processing gateway returns with successful message
- **Assumption:** Customer made a purchase with a transaction amount
- **Pre-conditions:** Customer credit card processing is successful
- **Post-conditions:** Manager scans item and completes transaction
- **Scenarios:** Customer receives an email detailing the transaction

----------

**Use Case Name:** Send email for VIP status earned

**UC ID:** Send email for VIP status earned for

- **Requirement Summary:** Use case for email server sending email for VIP status earned for the next year
- **Basic Path:** 
	1. VIP Status is determined from current Purchase
	2. Email server is initialized through external library
	3. Customer email is accessed from their account
	4. VIP email template is accessed and filled with customer information
	5. Email is finalized and sent
- **Alternative Paths** None
- **Exception Paths:**
	1. Email server external library fails to load
	2. User has provided an invalid email (unconfirmed email)
	3. Users email is account is full and message fails
- **Extension Points:** None
- **Triggers:** Use case Calculate Reward Achievements completes with VIP Status determination
- **Assumption:** Purchase is being made and customer does not already have VIP status in current year
- **Pre-conditions:** Customer has an account and purchase made with VIP status determined from yearly purchases being > $300 (after credits applied)
- **Post-conditions:** Customer receives an email and becomes a VIP Customer for next year
- **Scenarios:** Customer spends the threshold to become a VIP customer, so they receive an email and become a VIP customer next calendar year

----------

**Use Case Name:** Send email for $3 credit earned

**UC ID:** Send email for $3 credit earned

- **Requirement Summary:** Customer spends $30
- **Basic Path:** 
	1. $3 Credit earned is determined from current Purchase
	2. Email server is initialized through external library
	3. Customer email is accessed from their account
	4. $3 Credit earned email template is accessed and filled with customer information
	5. Email is finalized and sent
- **Alternative Paths** None
- **Exception Paths:**
	1. Email server external library fails to load
	2. User has provided an invalid email (unconfirmed email)
	3. Users email is account is full and message fails
- **Extension Points:** None
- **Triggers:** Use case Calculate Reward Achievements completes with $3 Credit earned determination
- **Assumption:** Purchase is being made
- **Pre-conditions:** Customer has an account and purchase made with $3 Credit determined from current purchases being > $30 (after all discounts and credits applied)
- **Post-conditions:** Customer receives a $3 credit and an email sent to them 
- **Scenarios:** Customer spends $42 on tea for a work meeting, then receives a $3 discount for their next purchase and is informed of this via email.






