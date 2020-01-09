# newspringattempt
Spring state machine - middleware for SPaT capstone

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
* **Erin L Cook** - [erinlorelle](https://github.com/erinlorelle)

## Acknowledgments
This is part of ETSU's Capstone GLOSA SpaT project.  This file is the middleware between the Android UI and the Java backend.  This was created solo, but colleagues are expected to assist in Spring 2020.
* Inspiration
* etc

