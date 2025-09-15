# DAT250 – Assignment 2

## Report by Endre

### Technical Problems Encountered

- **Null fields in responses**:  
  Initially, `username` and `email` returned as `null`.  
  **Fix**: Ensured that getters and setters were properly defined in the `User` class so that Jackson could serialize/deserialize the fields correctly.


- **Circular JSON references**:  
  When returning `Poll` objects, JSON produced infinite recursion between `Poll` and `VoteOption`.  
  **Fix**: I used `@JsonManagedReference` on `Poll.options` and `@JsonBackReference` on `VoteOption.poll` to break the cycle.


- **Automated test variables not being set**:  
  The HTTP client tests failed because variables were never assigned.  
  **Fix**: Added response-handlers (`client.global.set(...)`) in the `.http` file to capture IDs automatically when creating users, polls, and options.


### Pending Issues
- The automated tests rely on IntelliJ’s HTTP Client, and are not integrated into Gradle/JUnit.
- I have not implemented the optional steps as I was already late on delivery

### Repository 
[GitHub Repository](https://github.com/endrehj/poll-app-spring-boot-project)

