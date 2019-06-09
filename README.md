# Bachelor-Thesis
## Simple Forum Example

### Used technologies:

---
* Spring 5
* Spring-Boot 2
* Spring Security 5
* Hibernate 5.2.17
* JPA 2.0.9
* Thymeleaf 5
* Groovy 2.4.15
* Bootstrap 4.3.1
* JUnit 4
* PostgreSQL
* Gradle
***

Application require loggin for watch and add posts or comments.
Application also contain vote feature for logged in users.
If you logged in user, you can add post with question and next you are able to select one or more
good answers. All logged in users can vote up or down posts and answers. You can see actual score for votes 
on your user panel window.

***

For correct run this application you need to be connected with your PostgreSQL database.
Data about database connection (username and password) are added as a environment properties.
You can simply edit this by customization `application.properties` file with setting up `spring.datasource.*` paths. 