# Development and Documentation Process

This repository is the technical record for FRC Team 7656's 2027 robot development, beginning with the offseason practice robot.

## Working agreement

For every meaningful hardware or software change:

1. Record the **purpose** and the starting configuration.
2. Record the **change** (parts, dimensions, wiring, code files, configuration values, or commit).
3. Record the **test procedure** and observed results, including measurements when available.
4. Assign a **status**:
   - **Planned:** not yet implemented.
   - **Implemented:** built or coded, but not yet confirmed by the intended test.
   - **Verified:** tested with documented evidence and conditions.
5. Record known limitations, next steps, and anything that must be **reverted, replaced, or rechecked** for the 2027 competition robot.

A code commit is not proof of robot behavior. Avoid calling something verified until it has been tested on the relevant hardware.

## Documentation structure

- `README.md`: overview, drivetrain hardware, CAN IDs, and links to detailed records.
- `docs/DEVELOPMENT_PROCESS.md`: this workflow and conventions.
- `docs/DEVELOPMENT_LOG.md`: chronological record of changes, tests, results, and next steps.
- Additional hardware, vision, and testing documents can be added when needed rather than making the README too large.

## What to capture for physical builds

Part numbers and quantities; measured mounting locations and orientations; wiring and CAN IDs; photos or drawings where useful; calibration values; mechanical changes; and open questions.

## What to capture for code

Relevant files, library versions when known, changes and their reasons, commit links, controls/behavior affected, deployment status, and rollback instructions for temporary settings.

## Practice-to-competition transition

Mark offseason-only changes explicitly. Before deploying to the 2027 competition robot, review all such items, update the official field and AprilTag configuration, check WPILib and vendor libraries, validate new hardware, and repeat relevant tests. Do not assume practice-robot results transfer automatically.
