# MC Hammers 2027 Development Log

This log distinguishes planned work, code/hardware changes, and results verified on the robot. Entries should cite a commit or concrete measurement where possible.

## 2026-11-15 — Classroom teleop speed limits

- **Status:** Implemented in GitHub; **not yet verified on robot**
- **Purpose:** Make the swerve practice robot manageable on the small classroom carpet during vision testing.
- **File:** `src/main/java/frc/robot/RobotContainer.java`
- **Change:** Teleop maximum translation set to **0.50 m/s** and maximum rotation set to **45 degrees/s**.
- **Commit:** [e4bc0cc](https://github.com/alderma4/MC-Hammers-2027-Competition-Code/commit/e4bc0cc53334a82f23b6630cc15bf9961e72688e)
- **Next test:** Pull, deploy, drive slowly, and check control feel and stopping behavior.
- **Competition transition:** These are **temporary practice limits**. Reassess or remove before competition use.
- **Not changed:** Autonomous speed constraints and slew-rate limiting.

## 2026-11-15 — Hardware inventory documentation

- **Status:** Documented; drivetrain CAN IDs and dimensions cross-checked against generated constants.
- **Change:** Added drivetrain hardware inventory and 14-device CAN ID table to the README.
- **Commit:** [f4148c1](https://github.com/alderma4/MC-Hammers-2027-Competition-Code/commit/f4148c149698c20a0fab0e08d806157efeb6ed34)
- **Next step:** Keep the inventory current as hardware is added, replaced, or moved to new CAN buses.

## Vision localization — work in progress

- **Status:** AprilTag vision experiments conducted; continuous vision fusion **planned, not yet implemented**.
- **Hardware/setup reported:** Limelight 2+ on front-left of practice robot; custom AprilTag practice layout.
- **Prior stationary tests reported:** Mean MegaTag2 position error approximately **5.5 cm** across five tests after camera calibration, compared with approximately **17.9 cm** across three earlier tests. These results are specific to the practice setup, not a general accuracy guarantee.
- **Current distinction:** Raw MegaTag2 pose is displayed for comparison with drivetrain pose. The one-time pose initialization command is not continuous fusion.
- **Next work:** Add continuous, timestamped vision measurements with quality checks; separately log raw odometry, raw vision, and fused estimate; test stationary, translation, rotation, tag loss/recovery, and autonomous driving.
- **Competition transition:** Replace custom field/tag geometry with official 2027 configuration and remeasure camera mounting after robot construction.

## Next-session checklist

- [ ] Pull latest code and deploy classroom speed limits
- [ ] Verify slow teleop driving and document observations
- [ ] Decide whether slew-rate limiting is needed
- [ ] Inspect and implement continuous vision fusion in small, testable steps
- [ ] Log test results and any new competition-transition tasks
