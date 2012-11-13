Servlets-JSP
============
`Sunday November 16th by 11:55 pm`

Description
-----------
In this assignment you will utilize  a suppliers/parts/jobs/shipments database 

(creation/population script available on the course assignment page) as the back-end database. Frontend access to this database by the client will occur through a simple page displayed in the client’s web 

browser. The schema of this database consists of four tables with the following schemas for each table:

suppliers (snum, sname, status, city)  //information about suppliers

parts (pnum, pname, color, weight, city)  //information about parts

jobs (jnum, jname, numworkers, city)  //information about jobs

shipments (snum, pnum, jnum, quantity)  //suppliers ship parts to jobs in specific quantities

The first-tier (client-level front-end) of your application will be a simple XHTML/HTML page that 

allows the client to enter SQL commands into a window (i.e. a form) and submit them to the server 

application for processing.  The front-end can utilize JSP technology if you wish.   The client front-end 

will provide the user  a simple form in which they will enter a SQL command (any DML, DDL, or 

DCL command could be entered by the user).  The front-end will provide only two buttons for the 

user, and Execute button that will cause the execution of the SQL command they enter, and a Reset 

button that simply blanks out the form input area.  The client front-end will run on any web-based 

browser that you would like to use.  You can elect to have a default query or not, it is entirely your 

decision.

The second-tier servlet, in addition to handling the SQL command interface will also implement the 

business/application logic that will increment by 5, the status of any supplier who has a shipment with 

a quantity >= 100, anytime a shipment in which the quantity is greater than or equal to 100 is entered 

into the database through insertion or update.  Note that any update of quantity >= 100 will affect any 

supplier involved in a shipment with a quantity >= 100.  The example screen shots illustrate this case.  

An insert of a shipment tuple (S5, P6, J7, 400) will cause the status of every supplier who has a 

shipment with a quantity of 100 or greater to be increased by 5.  In other words, even if a supplier’s 

shipment is not directly affected by the update, their status will be affected if they have any shipment 

with quantity >= 100.  (See page 8 for a bonus problem that implements a modified version of this

business rule.)  The business logic of the second tier will reside in the servlet on the Tomcat webapplication server (server-side application).    This means that the business logic is not to be 

implemented in the DBMS via a trigger.

The third-tier (back-end) is the persistent MySQL database described above and is under control of the 

MySQL server.  
