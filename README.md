# WeShare-Project

"WeShare" Expense Tracker Project
WeShare is a web application that is meant to make keeping track of one's money easier. This application allows a user to keep track of all of his expenses, the money he is owed and the money that he owes to others. A user can claim money from other users and they can settle claims that were claimed from them. Expenses and claims are accompanied by descriptions and dates. The application can be used by friends to keep track of their spending as a group during group activities.
Login/logout:
User can log in and out of the application using their email address
View Nett Expenses:
Shows the logged in user:

My expenses
People I owe
People that owe me
The totals for each of the above mentioned sections
The user's nett expenses

Capture Expense:
Provides user with a form to create a new expense with a field for a description of the expense, the amount and the date of the expense.
Claim expense:
Clicking on an expense description navigates user to a page where they can capture a claim or multiple claims on that expense.
Settle a Claim:
When a user clicks on the description of an expense that was claimed from them they are navigated to a page where they can settle that claim.

Architecture:
The application follows the MVC application architecture and makes use of an in-memory datastore using a singleton class.
Responsibilities:


Controllers:
Handle an input from the user interface or view and actions the input on the model and return the appropriate response.


Models:
Person, expense, claim and settlement models are used to represent these elements in the application and for the purpose of using the datastore.


Views:
The html templates are used to create the view on the home, capture expense, capture claim and settle claim pages.


Interfaces:
An instance of the data repository interface is used to access the datastore.



Getting Started

Prerequisites

Java
Maven


Installation
Install Java Development Kit and Java Runtime env.
Install Java on Windows
Install Java on Linux:

sudo apt install default-jre
sudo apt install default-jdk


Install maven on windows
Install maven on Linux:

sudo apt install maven



Application Setup

Clone expenser repository to personal computer
Start application by running the WeShareServer:

Application server can be started in an IDE such as intelliJ or using the command line




Open web application url on "http://localhost:7070/"
Login using email address


Running the tests
The project has model and user tests. Model tests test if the project's object models' data access methods work correctly. While the user tests make use of selenium to test the web application by means of asserting the presence of html/css elements on the web application page.
The tests test the nett expenses, capture expense, capture claim, and settle claim pages.
Run the following command in the project's root directory to run the project tests

mvn test



Built with


Java - The web framework used

Maven - Dependency Management


Authors

Chantel Skosana
Nobantu Gumbi
Lloyd Ndhlovu

See also the list of contributors who participated in this project.
