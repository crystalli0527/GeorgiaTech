# Vision Document

<br/>

**Author**: Team 52
<table>
<tr>
<td>Version</td><td>Description</td>
</tr>
<tr>
<td>V1</td><td>Initial version</td>
</tr>
</table>


<br/>

## 1 Introduction


TCCart is Team 52' Android app for Brad and Janet to track customer transactions, rewards, and VIP members of their Tea and Coffee Cart. 

<br/>

## 2 Business Needs/Requirements


**2.1 Reason**

The client, Brad and Janet, have a cart in Piedmont Park Atlanta where they sell coffee and tea during the weekend. Their business is picking up momentum, and they want to be able to:

1. Allow their customers to pay using a credit card 
2. Reward their loyal customers

<br/>

**2.2 Problem**

The clients have a need to hire contract developers to achieve this goal. They need to to develop their **TCCart**, which is a payment and rewards management system for tea and coffee carts that runs on the Android platform. They provided a set of requirements to our team to see a possible design for the system before moving forward. 

<br/>

**2.3 Market**

The clients business for coffee and tea in Piedmont Park, Atlanta is picking up momentum. The market for the type of system needed by the client is defined by the following factors.

**Market: Product**

Both the tea & coffee markets are expected to grow in the near term, with coffee slowing down through 2018. This is according to market research from *MarketResearch.com* and *Athena Marketing International (AMI)*.

**a) Market: Product - Tea Sales Growth**

They have found for tea that:

- Tea has become more popular in recent years
- There is a +US$20 billion market for tea
- Growth of tea is outpacing coffee growth
- Previous +13% volume global growth in tea sales
- Indicative of higher value tea being consumed


**b) Market: Product - Coffee Sales Growth**

They have found for coffee that:

- Coffee sales were up +5% in 2015
- Growth is expected to slowly decrease through 2018
- Approx. 11% in value to US$41 billion in previous years
- Value increase = +demand for specialty, premium coffee

**Market: Location and Accessibility**

According to Wikipedia, Piedmont Park  is a 189-acre urban park in Atlanta Georgia, located approximately 1 mile northeast of Downtown.

The types of activities and facilities in the park vary, and are many. The park is open all year round, between the hours of 6:00AM and 11:00PM every day. This allows for a very flexible business operation, with access to potential customers all day and every day.


**Location Need: Mobility:** Since this is a public park, 1) there are no other building facilities to build a physical store and  2) the need for these types of beverages in this location will still be there. *This is where the tea and coffee cart will fill the supply for this increasing demand at these types open space locations.*


**Accessibility Need: Mobile Software:** These facts, suggest that there is potential for constant customer traffic all year round, and a need to have a system to track customer purchases and provide incentive for repeat customers through rewards with a rewards and VIP system.

<br/>

**Market: Tea and Coffee Carts Software**

As shown above, with the growth of both tea and sustained coffee consumption and the Piedmont Market location and customer growth, the market for tea and coffee carts can be established as a demand that will continue to grow. 

This establishes a large need and market not just for our clients system to manage their business, but the overall market for tea and coffee cart software to track transactions and rewards systems will increase in parallel. There will be a need for this type of software now and in the future.

Also, from a review of the Google Play store, there exists many POS mobile solutions. However, nothing found a  solution specific to Tea and Coffee cart Payment and Reward software.

<br/>

## 3 Product / Solution Overview

The software solution will satisfy the client needs based on the 

1. Growing overall tea and coffee market
2. Location demands and need for system mobility (using Android)

**Current Version:** The current version of the software is:

- Mobile on Android since the park is large and they may need to move based on customer demand and movement patterns
- Scalable for adding as many customers as their growing demand will require
- Inter-operable with whatever external hardware and software libraries are needed. Specifically for:
	- Printer
	- Video Cam
	- Scanner
	- Email Server
- This also means the system is scalable if needed in future requirements for multiple carts (Currently not a requirement)
- There is high cohesion and low coupling for high readability and maintainability of the system
- Good design patterns with leveraging system data that will allow the client to
	- Foster Customer retention with applying rewards, special status for repeat business
	- View Customer history information and use patterns 

**Future Versions:** The possible future versions of the software could:

- Include plug ins for other external libraries such as
	- Inventory management to allow for greater automation and also relate more customer use data
	- Further customer relationship management by Customer ID / QR Code
	- Allow for scalability in use with multiple tea and coffee cart management

<br/>

## 4 Major Features (Optional)


- Customer Information tracking 
	- Adding new customers
	- Editing existing customers
- Viewing customer activity
	- Purchases history
	- Rewards history: 1) Credits received and applied 2) VIP Status
- Customer Card Printing (using external library)
- QR Code integration for quick customer card scanning (using external library)
- Credit Card payment gateway (using external library)
- Tracking of all purchase activity
	- Each transaction and amount
	- Totals by year
	- Credits applied
- Tracking of all Customer Rewards
	- Credits acquired and applied
	- VIP Status
- Email server integration (using external library)
	- Purchase activity
	- Reward activity

## 5 Scope and Limitations


**Scope:** The scope of the system covers specifically the following:


*Note: see both product and solutions overview as well as features list above for full scope.*

- Customer Information (Adding, editing, history)
- Credit Card payments for purchases
- Hardware integration and use:
	- Printer
	- Video Cam
	- Scanner
	- Email Server


**Limitations:** These are foreseeable limitations

- No inclusion of inventory management
	- Not part if initial requirements but could be a separate future phase specific for inventory
	- Future inventory easily implemented since current solution only requires *purchase price* attribute
- Not able to accept other forms of payments and tracking
	- This is as per client requirements
