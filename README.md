# newspringattempt
Spring state machine - middleware for SPaT capstone

Spring state machine was selected as the middleware framework for the Capstone project, Green Light Optimal Speed Advisory (GLOSA) application.  The main application is to notify drivers of the speed required to safely drive through an intersection, or to stop of not safe.  The purpose is to increase safety and improve mileage and efficiency.  Spring's state machine provides concurrency which is required for the petri-net design of decision making process between the Android UI and the Java backend.  

This application is still in progress, but shows 3 threads running in concurrency with actions being called to trigger the next state of events.  Sleep is implemented in the threads to immulate 3 processes running simultaneously.  To simulate changes from the UI (or otherwise), one of the threads goes in 1 of 2 paths - a random number is created to determine the path while the other threads are still running unaffected.  


## Getting Started
* Download an IDE such as [IntelliJ by JetBrains](https://www.jetbrains.com/idea/)
* You must import lombok.extern.java.LogcompileOnly
* You must add the Spring state machine plugin into your IDE.  
* The following must be included in the build-gradle file:
   (1)'org.projectlombok:lombok:1.18.10' 
   annotationProcessor 'org.projectlombok:lombok:1.18.10' 
   (2) implementation 'org.springframework.boot:spring-boot-starter'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    compile 'org.springframework.statemachine:spring-statemachine-core:2.1.3.RELEASE'
    
## Bugs and Issues
* SPaT Standby state isn't called when triggered by an event.  Current workaround is to call it automatically from the previous state (the event is commented out allowing the transition to occur automatically).


## Built With
* [IntelliJ](https://www.jetbrains.com/idea/) - IDE
* [Spring state machine](https://projects.spring.io/spring-statemachine/) - Framework for concurrency
* [Gradle](https://maven.apache.org/) - Dependency Management

## Versioning
We use [BitBucket](http://bitbucket.org/) for versioning. A copy has been uploaded to GitHub. For the versions available, see the [newspringattempt](https://github.com/erinlorelle/newspringattempt). 

## Authors
newspringattempt was created by **Erin L Cook**, a graduate student in the computing department at East Tennessee State University
* [https://github.com/erinlorelle](https://github.com/erinlorelle)
* [https://erinlorelle.com](https://erinlorelle)

## Acknowledgments
This is part of ETSU's Capstone GLOSA SpaT project.  This file is the middleware between the Android UI and the Java backend.  This was created solo, but colleagues are expected to assist in Spring 2020.


