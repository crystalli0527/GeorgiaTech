# **Design Discussion -- TCCart a Payment and Rewards Management System for Tea and Coffee Carts **

Author: Team 52

##1 Individual Designs


###1.1 Design 1

![](http://i.imgur.com/mR7DoYR.png)

####1.1.1 Pros / Cons

**Pros**
- Manager runs everything. 
- Heavy reliance on external libraries

**Cons**
- Treats Manager and Customer as a User, and User is unused.
- VIP user is a subclass, which would be annoying to implement. 


###1.2 Design 2

![](http://i.imgur.com/NHbe0hh.png)

####1.2.1 Pros / Cons

**Pros**
- Heavy reliance on external libraries
- Shared use of interfaces for external libraries between classes
- Simple and scalable Reward Type class

**Cons**
- Not demonstrating needed use of Date or Money utilities
- Yearly transaction sum attribute in Transaction Class but can be simplified as a method and sum on transaction history

  
###1.3 Design 3

![](http://i.imgur.com/Ct5jLSo.png)

####1.3.1 Pros / Cons

**Pros**
- Reflects modular design, such as the separation of functionality found with creating PaymentManager system
- The addition of Money and Date utility classes provide helper functions for their respective data types 

**Cons**
- Adding an Item class may increase features beyond the scope of project
- Transaction class may contain too many methods, violating the 'single responsibility principle'

###1.4 Design 4

![](http://i.imgur.com/HG7NeIC.png)

####1.4.1 Pros / Cons

**Pros**
- Simple and easy to understand high level design

**Cons**
- Lack of details that leads to potential design changes during further development stages
- High degree of preference towards attributes over sub-classing
- Treating Customer class as an actor in the system

##2 Team Design


###2.1 Design

![](http://i.imgur.com/kMkqkZh.png)

###2.2 Commonalities

In our team review and discussion, the core commonalities in our individual design that were found included:

**Classes**
- Customer
- Transactions or Purchases
- Enter/ Edit Customer (some form i.e. Manager)
- Interfaces (as interface or just as separate class)
	- Video Cam
	- Printer
	- Email Server / Distribution
	- Scanner

**Methods**
- Add customer
- Generate ID
- Print Card
- Edit Customer
- Read Card
- Scan Credit Card
- Determine Credit
- Store Credit
- Apply Credit Next purchase
- Determine VIP Status
- Store VIP Status
- Apply VIP Status
- Sum Purchase
- Less Credits and Discounts
- Send Emails
	- Purchase Confirmation
	- New Credit
	- VIP Status

**Attributes**
- Customer information
	- Name
	- Email
	- Customer ID
	- Discount active status
	- Has VIP status
- Card
	- QR Code
- Transaction information
	- Date
	- Pre discount total
	- VIP discount total
	- Credit discount total
	- post discount total
- 


###2.3 Differences

Some key differences found were either as minimal as based on naming conventions or as differentiated as not having separate classes for manager activities or reward attributes and methods.

These specific differences were:

- Manager Class
	- For entering and editing customer information (i.e. Design 3)
	- Others included these methods within the customer class itself
- Rewards Class
	- This class was differentiated on two designs
	- Other designs included reward attributes and methods on the customer or transaction class
	- Additional related classes were also included on two designs for a) VIP Status and b) Credit Discount
- Use of utilities
	- Date utility not used on all designs
	- Money Utility not used on all designs

###2.4 Design Decisions

The team was had open discussion after reviewing our individual designs together. Though we presented a few alternatives we were able to build consensus in our decisions, either through further research on best practice, through relaying related professional experience, or by vote. 

This effectively allowed us to agree on the best approach.

Specific design decisions were to:

1. Maintain the common design decisions previously found
2. Where some differentiated properties were determined to either:

	a) Enhance the design with these additions

		- Inclusion of utilities
		- Inclusion of Rewards and VIP Class
		- Seperation of Rewards from VIP Class as distinct classes
		- Creation of a Manager Class for related methods
		- Creation of a Transaction Manager class for workflows

	b) Removal of superfluous or not needed items

		- Items as this is not discussed in the design by the client and considered part of inventory. Dealt with by Manager by entering purchase price directly.

3. Review the end design for particulars of each class for the important
	1. Attributes
	2. Methods
	3. Relationships

##3 Summary

The end design was iterative in approach by fulfilling our initial mandate to review first the a) commonalities and agree if they were important and needed b) the differences and if they needed to be either removed or included and then c) any aggregation or separation that may be needed and finally d) a full review of the particulars of each class. 

Our final end design is what we agree to be a best approach for building the TCCart for the client.
