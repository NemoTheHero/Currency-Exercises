This project uses JAVA 20 and runs with Spring Boot.

Make sure to run "mvn clean install" on the project to get all dependencies.

On run the project will create a H2 Database using a file
on your computer. It should be located at
For Windows
C:\Users\{User}\data\currencydemo.mv.db
Running Tests will create a H2Database file as well located at
C:\Users\{User}\data\currencydemotest.mv.db

For Mac this will be placed in Users/{User}/data

You can update this in the application.properties in the resources directory.

You can see the Database while running the application locally
at http://localhost:{portNumber}/h2-console
portNumber is usually 8080.


There are 3 exercises in 3 classes defined in src/java/com.gossamer.voyant.interviews package.

They are meant to be straightforward.  There are no gotchas.

The idea is to provide your implementation in the corresponding class, and then verify your code with test(s) in
the corresponding JUnit Test class provided.

PLEASE DO THESE EXERCISES IN AN OBJECT ORIENTED MANNER!

We recommend trying these in the following order.  If you get stuck at some place, move on to the next, and return back.

1) IntRange - implement a basic range Object to represent a range of integers, and then implement
the overlaps method on the class (specifying the correct signature for this method), in order to determine if a given
IntRange overlaps with another IntRange.
Please do not use any 3rd party libraries to implement this exercise.
Please implement the unit test to show your solution works.  You may use JUnit or TestNG.
There are no gotchas to this exercise.  It should be very simple.
The expected difficulty should be in the overlaps method implementation.
(Avg. Time Estimate 5-20 min)

2) IncomeTaxSystem - see class JavaDoc for explanation  (Time Estimate 10-45 min)
Open source library usage is fine to implement any data structures you would like to use.
Please implement the unit test to show your solution works.  You may use JUnit or TestNG.

3) CurrencyConverter - see class JavaDoc for explanation (Time Estimate 1-3 hrs)
Open source library usage is fine to implement any data structures and/or algorithms you would like to use.
Please implement the unit test to show your solution works.  You may use JUnit or TestNG.
