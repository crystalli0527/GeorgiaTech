# Design Document

**Author**: Team 52
<table>
<tr>
<td>Version</td><td>Description</td>
</tr>
<tr>
<td>V1</td><td>Initial version</td>
</tr>
<tr>
<td>V2</td><td>Final version (Deliverable 3)</td>
</tr>
</table>

## 1 Design Considerations
### 1.1 Assumptions
The System runs on an Android device and is used by a single user at a time (Manager) to operate a tea/coffee cart. Network connectivity is required to process credit card transactions and send emails.

### 1.2 Constraints
The System's initial deliverable will not include inventory management capabilities as per vision document.

### 1.3 System Environment
The System is an application that runs on an Android device and interacts with 3rd party hardware components such as ID card printer, QR code video cam reader and credit card scanner through 3rd part APIs. The system also uses 3rd party software components to process credit card transactions and send emails. The System will use Android's SQLite database as a persistence layer.

## 2 Architectural Design
### 2.1 Component Diagram
The System is considered to be a single component autonomous system that interacts with 3rd party software components and hardware. The interaction with 3rd party software components and hardware is described in section 2.2.

### 2.2 Deployment Diagram
![](http://i.imgur.com/YWgq6fg.png)

## 3 Low-Level Design
### 3.1 Class Diagram
![](http://i.imgur.com/mtxCw11.png)


## 4 User Interface Design
<img src="http://i.imgur.com/xO4zNBZ.png" width="400" alt="Main Menu">
<img src="http://i.imgur.com/6BSxinN.png" width="400" alt="Purchase/Reward History">
<img src="http://i.imgur.com/vdpo3m8.png" width="400" alt="Enter Purchase">
<img src="http://i.imgur.com/hKL8WNv.png" width="400" alt="Create/Edit Customer">
