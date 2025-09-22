# DAT250 – Assignment 4

## Report by Endre

### Technical Problems Encountered

**Persistence unit not found**  
   *Fix:* Added `src/test/resources/META-INF/persistence.xml` with `<persistence-unit name="polls">` and listed the four entities.


### Database Tables
- **users**: `id `, `username`, `email`
- **polls**: `id`, `question`, `published_at`, `valid_until`, `creator_id`
- **vote_options**: `id`, `caption`, `presentation_order`, `poll_id`
- **votes**: `id`, `published_at`, `voter_id`, `option_id`


### Pending Issues

- Methods (`createPoll`, `addVoteOption`, `voteFor`). I used the already existing methods and added extra steps in populate setup for PollsTest

### How to Run
```
./gradlew test
```
### Repository
[GitHub Repository](https://github.com/endrehj/poll-app-spring-boot-project)