# **Design Document -- TCCart a Payment and Rewards Management System for Tea and Coffee Carts **

Author: Nathaniel Morcos

##1 Purpose

The design of TCCart system is to create a system for customer information entry and management of each customers purchases and rewards.

This document details all system assumptions and system rationale used.

##2 Assumptions and Rationale

###1.1 User Interfaces

**Customer Entry:**

The cart manager will have an interface for entering and editing new customer information. 

The following customer information can be entered and edited:

-	Name
-	Email address

Upon entering the above information the following will be automatically generated:

-	8-digit unique hexadecimal ID

**Customer History Display:**

The cart manager will have an interface for viewing customer information. 

- Complete list of customer transactions and rewards 
	- tranDate for each transaction date
	- tranAmount for each purchase amount before discounts
	- rewardAmount of discounts or credits applied
	- rewardType for which reward type applied
	- rewardID for which historical reward or applied

###1.2 External Libraries Interfaces

TCCart operates by utilizing several pieces of hardware attached to the system. 

- These will be accessed through system interfaces to external libraries. 
- Each interface has a specific use which does not overlap.

Here is the list of system interfaces with

	- Interface name and their main system use
	- Data sent to the interfaces external library
	- Method of initialization
	- Timing of initialization

- **Scanner:** run scanner to read credit card information to payment processor
	- Data
		- Card holder name
		- Card account number
		- Card expiration date
		- Card security code
	- Initialization
		- Each purchase with credit card scan
	- Timing
		- Immediate
- **VideoCam:** operate videocam to read customer card
	- Data
		- QR code
		- Customer ID
	- Initialization
		- Each Card scan
	- Timing
		- Immediate
- **Printer:** run card printer to print card
	- Data
		- QR code
		- Customer ID
	- Initialization
		- Each new customer entry
	- Timing
		- Immediate
- **Email distribution:** run email distribution
	- Data
		- Customer email 
		- Email type
	- Initialization
		 - Each purchase
		 - Each reward milestone
		 - Each VIP Status change 
	- Timing
		- Immediate


  
###1.3 Reward Rules

Rewards have certain rules governing how they they are to be firstly 1.) awarded and then 2.) applied. 


- **Reward: $3 credit**
	- Each (purchase price - less [credits+discounts]) >= $30
	- Create and associate to *customer ID* the following:
		- *rewardID* and associate
			- *rewardType* = "$3 credit"
			- *rewardValue* = 3
		- *startDate* based on current date
		- *endDate* expiry is in 1 calendar month
	- Send reward email (initialize email distribution interface)
	- If current date is before endDate apply "current credit value" to next purchase
		- "current Credit value" is based on  1) new credit of $3 OR 2) if the next purchase was less than $3 and we needed to apply purchase price and keep reward with credit difference
		
 
- **Reward: VIP Status**
	- Current calendar year total purchases >= $300
	- Create and associate to customer ID
		- *rewardID* and associate
			- *rewardType* = "10% discount"
			- *rewardValue* = 10
		- January 1 of following year for startDate
		- expiry endDate of December 31 of following year
	- Send reward email (initialize email distribution interface)
	- If current date is within following year startDate and endDate apply 10% discount to all purchases

- **Reward Order**
	- RewardType order if both types exist for a customer ID
		1. VIP Status apply 10% discount to purchase
		2. $3 Credit apply to purchase (see reward rule on if purchase is less than $3)