<h1>👨‍🔧 Service Ordering App</h1>
<h4>A Java 17 program built using Spring Boot framework for a service ordering app. This app allows customers to place orders for various services, such as house cleaning, and enables technicians to leave suggestions for the customers to accept. Once accepted, the technician can proceed with the job.</h4>

<h3 id="ComponentsOverview">🔩 Components Overview:</h3>
<details>
<summary><h2>🔧 Services</h2></summary>
  The Services section of program offers a wide range of options to cater to various needs. The services are categorized into different main service types, including cleaning, building, fixing, decoration, transportation, and more.</br></br>

Each main service is further divided into smaller parts called <b>subservices</b>. These subservices provide more specific offerings to meet customers' precise requirements. For example, under the cleaning main service, the subservices could be house cleaning, company cleaning, and more. Similarly, under the transportation main service, we have subservices like truck transportation, car transportation, and so on.Each of the declared subservices can have description and base price and they can be changed later on this allows the manager to provide specific information about each subservice and adjust the base price as needed. 

This program allows the manager to dynamically add new services and subservices at runtime. This flexibility ensures that the service offerings can be expanded and customized according to the evolving needs of customers. It's important to note that service names are unique, and subservices are associated with their corresponding main service. This ensures a well-organized and structured service hierarchy.

By providing a comprehensive range of services and subservices, It aims to fulfill a diverse set of customer requirements and provide tailored solutions for their specific needs.
</details>
<details>
  <summary><h2>👤 Users</h2></summary>
The Users section of our app consists of two roles: Technicians and Customers. Additionally, there is a predefined Manager role for system administration.</br></br>

<h3>Each user, regardless of their role, has the following data associated with their account:</h3>

<h3>Name and Surname:</h3> This information helps identify the user and personalize their experience within the app.
<h3>Email:</h3> Users provide their email address for account verification and communication purposes.
<h3>Password:</h3> To ensure secure access, users are required to set a password for their account.
<h3>Registration Date:</h3> This data captures the date and time when the user's account was created.
<br><br>
In addition to the common user data, Technicians have an additional attribute called "Status." This attribute represents the current status of the technician, such as active, inactive, or pending validation. The Manager role is responsible for validating technicians after registration. Once validated, technicians gain access to the system's functionalities. Technicians also have the ability to add an image to their account, which helps personalize their profile.
<br><br>
<h3>User data validation:</h3>
During the registration process, the server validates the provided data to ensure its accuracy and integrity. This validation helps maintain a reliable user database and ensures that only valid information is stored.

<h3>Technicains and services</h3>
Technicians can be associated with multiple subservices, indicating the specific types of services they are qualified to provide. The Manager is responsible for assigning technicians to different subservices based on their expertise and capabilities. If a technician is not validated by the Manager, they will not have access to the system.

<br><br>
By providing distinct roles and validating technicians, our app ensures a secure and controlled environment for both Customers and Technicians. This helps maintain the quality and reliability of the services provided within the system.
</details>
