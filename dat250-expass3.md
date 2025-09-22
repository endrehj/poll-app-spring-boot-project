# DAT250 – Assignment 3

## Report by Endre

### Technical Problems Encountered

1. **Voting UX**  
   “upvote/downvote” both match backend semantics (downvote replaces vote).  
   *Fix:* Simplified to a single vote button per option (re-clicking another option re-casts)

2. **UI updates after voting**  
   Counts seemed to change only on page refresh.  
   *Fix:* After `POST /votes`, refresh `/votes` and recompute counts per option.

w**CORS in development**  
   Frontend on `5173` calling backend on `8080` failed with CORS.  
   *Fix:* Added `@CrossOrigin(origins = "http://localhost:5173", methods = …)` on controllers.

### Pending Issues
- I have not implemented any of the optional extensions

### Repository
[GitHub Repository](https://github.com/endrehj/poll-app-spring-boot-project)
