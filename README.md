#Bamboo specification by example in Kotlin
Official documentation [Bamboo Java Spec documentation](https://docs.atlassian.com/bamboo-specs-docs/latest/specs.html?java#project)

Based on the documentation - there are 2 possibilities to implement Bamboo pipeline specification: 
- `yml` file in `/bamboo-specs` folder
- As java sub-module inside your project

But after digging the source codes becomes clearly that possible to implement generation of bamboo 
specification in any `JVM` language and built it even via `Gradle` based on `com.atlassian.bamboo:bamboo-specs` 
artifact, even as still `java` class

This is an example project with implementation of Bamboo Spec file based on Kotlin to run `JUnit5` tests inside 
`gradle docker` image and reporting results into Allure

`bamboo.yml` file it's fully analogical to `PlanSpec.kotlin` implementation in `Kotlin` 

##Ways of using:
1. As sub-module of your project
   1. Put this sample repo into your project as sub-module
   2. Rewrite all what you need to define your own Plan, Stages, Jobs, Tasks, etc.
   3. Run `main` method
2. Just as runnable Main class inside your project
   1. Add artifacts: `com.atlassian.bamboo:bamboo-specs` and `-api` with appropriate version (look at `build.gradle.kts`)
   2. Implement by example as in `PlanSpec.kotlin` file definitions of your Plan, Jobs, Tasks, etc.
   3. Run `main` method

Running of the `main` method performing generation of `bamboo.yml` file and publishing one into your Bamboo instance

P.S. put `.credentials` file with your bamboo creds (ignore it in .gitignore) to publish Bamboo pipeline specification
