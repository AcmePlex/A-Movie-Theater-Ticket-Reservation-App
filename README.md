# AcmePlex Movie Theater Ticket Reservation App

This is a movie theater ticket reservation app for AcmePlex, allowing users to search for movies, view seat availability, make reservations, and manage bookings. Registered users (RUs) enjoy additional benefits, including early booking access and waived cancellation fees.

## Features
- Search for movies and view showtimes.
- Select seats and make reservations with payment.
- Receive email confirmations and manage cancellations.
- Registered Users (RUs):
  - Early access to 10% of seats.
  - No cancellation fees.

This is templated with start.spring.io initializer.

## Setting up the database
1. Setup a mySQL database with port 3306 and enter your local database connection information. (e.g. password `123456` and username is 'root'.)
2. Navigate to the folder that contains the application.
3. Copy the script in the acmeplex-populated.sql file and paste it onto a query window in mySQL.
4. Run the query.

## Start the Backend
1. Install gradle and java onto your computer.
2. From the main folder, navigate to the Backend folder in the terminal by `cd Backend`
3. Refresh the dependency on your local computer by using commend `gradle build --refresh-dependencies`
4. Launch the backend with commend `./gradlew bootRun`

## Start the Frontend
1. Install node js onto your computer
2. From the main folder, navigate to the Frontend folder in the terminal by `cd Frontend`
3. From the Frontend folder, navigate to the moive_ticket_reservation_app folder in the terminal by `cd moive_ticket_reservation_app`
4. Install the required packages using `npm install` in the terminal
5. Launch the frontend with commend `npm start
