# OLA_Backend_In_Springboot

Welcome to the Ola Cab Booking System backend repository! This system is developed using Spring Boot and Spring Data JPA, with MySQL as the database.

## Features

### User Role

#### Register as a User
To use Ola Cab services, users need to register by creating an account. The registration process involves providing necessary information to create a user profile.

#### Book a Ride
Users can book a ride through the system. They can specify the pickup location, destination, and other relevant details. The system processes the request and assigns a driver to the user.

### Driver Role

#### Register as a Driver
Individuals who wish to become Ola Cab drivers can create an account by registering as a driver. The registration process collects essential information about the driver.

#### Accept Ride Requests
Once a user books a ride, the system assigns the ride to an available driver. Drivers can view incoming ride requests and accept them based on their availability.

#### Arrive at User's Location
After accepting a ride request, the driver is expected to arrive at the user's location promptly. The system facilitates smooth communication between the user and the driver.

## Getting Started

To run the Ola Cab Booking System backend locally, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/ola-cab-backend.git
   
2. Navigate to the project directory:
   ```bash
   cd ola-cab-backend

3. Configure the database:
Update the application.properties file with your MySQL database configuration.

Configure the database:
Open the src/main/resources/application.properties file and update the following properties with your MySQL database configuration:


    spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    
Replace your_database_name, your_mysql_username, and your_mysql_password with your specific MySQL database details
    

4. Build and run the application:

       ./mvnw spring-boot:run

The backend will be accessible at http://localhost:8080  
    


