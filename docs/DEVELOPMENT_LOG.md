# MC Hammers 2027 Development Log

This log distinguishes planned work, code/hardware changes, and results verified on the robot. Entries should cite a commit or concrete measurement where possible.

## 2026-11-16 — Continuous MegaTag2 fusion: stationary, translation, tag loss and reacquisition verified

- **Status:** Continuous MegaTag2 XY fusion implemented, compiled, deployed and tested on the practice robot. The current implementation is **practice-only** and requires manual pose initialization plus an explicit fusion-enable switch; autonomous operation with fusion is **not yet tested**.
- **Code:** `src/main/java/frc/robot/subsystems/CommandSwerveDrivetrain.java`; user reported committing and pushing the tested fusion implementation. The Phoenix 6 swerve **pose estimator** (not a separate CTRE device) receives timestamped MegaTag2 poses via `addVisionMeasurement(...)`.
- **Fusion safeguards:** `Vision/EnableFusion` explicitly OFF at robot-code startup; `Vision/FusionArmed` becomes true only after disabled-mode `limelight/initialize_pose` is accepted; new NT update, visible tag, finite and recent measurement timestamp/latency, practice-field bounds [0,4] m for X/Y, and less than 0.50 m separation from current estimated position. Vision measurement standard deviations `(0.30 m, 0.30 m, 9999999 rad)` give negligible influence to camera heading. `FusionAcceptedCount` counts calls submitted to the estimator, **not independent internal estimator acceptance**. No separate raw odometry-only pose is currently published.
- **Starting configuration:** Robot physically placed at approximately (2.0, 1.0) m facing AprilTag 6 at 0°. After deploy, heading was near zero; MegaTag2 reported approximately (2.061, 0.943) m. Disabled-mode initialization was accepted and the pose estimator reset to the camera estimate. Immediately afterward: estimator (2.060, 0.943) m, MegaTag2 (2.065, 0.942) m.
- **Stationary fusion test:** With fusion enabled, measurement submission count increased; video showed 688 submissions, 0 rejected, and stable nearly coincident poses. A later stationary reading showed estimator (2.062, 0.942) m versus MegaTag2 (2.065, 0.943) m.
- **Odometry-only translation control test:** With fusion accidentally OFF, robot drove approximately +0.5 m along X. Estimator (2.543, 0.945) m versus MegaTag2 (2.518, 0.943) m, a ~25 mm X discrepancy. Upon enabling fusion without moving or reinitializing, estimator moved to (2.522, 0.942) m while MegaTag2 was (2.520, 0.942) m. Submission count 1,407; rejection count 0. This **directly demonstrates a correction toward the vision estimate**, though it does not establish absolute physical accuracy.
- **Tag loss and reacquisition:** Slow in-place rotation removed AprilTag from camera view. `MegaTag2HasTag` and `MegaTag2Valid` went false, and submission count stopped at 5,545 despite fusion remaining enabled. Upon turning back toward the tag, fusion resumed automatically without reinitialization; recording showed 5,886 submitted and 0 rejected. A retained last-known plotted MegaTag2 pose is not a valid current detection.
- **Multidirectional teleop test:** With the tag kept visible, the robot drove forward/backward, sideways, and with gentle rotation. Submissions rose from 14,670 to 16,470 (+1,800); rejection count stayed 0. At end with fusion switched OFF, MegaTag2 reported (1.120, 1.284) m and 0.067 rad; estimator reported (1.126, 1.283) m and 0.067 rad, approximately 6.1 mm XY disagreement. The two estimates are **not independent**, and the result is not a claim of 6 mm field-position accuracy.
- **Timing observed:** MegaTag2 measurement ages of approximately 0.087–0.114 s during representative tests; these are snapshots, not a full timing validation.
- **Outstanding limitations / next tests:** (1) deliberate automatic starting pose **and field heading** from selected autonomous path, removing the manual startup ritual; (2) PathPlanner autonomous test with fusion active; (3) verify performance during faster combined translation/rotation, tag transitions, temporary occlusion, and outlier cases; (4) add rejection-reason diagnostics and independent odometry-only pose logging; (5) confirm true physical accuracy against surveyed points; (6) replace practice-only 4 m bounds, custom AprilTag layout, camera mounting transform, and temporary speed settings for the 2027 field/SystemCore transition. The currently reported 0 rejections reflects only tested conditions, not proof the rejection logic catches all bad measurements.

## 2026-11-16 — MegaTag2 diagnostics, pose initialization, and odometry verification

- **Status:** Verified on the practice robot for the tests below. **Continuous fusion was not yet implemented at the time of these earlier tests; see the later 2026-11-16 entry above.**
- **Code checkpoint:** [8f6930f](https://github.com/alderma4/MC-Hammers-2027-Competition-Code/commit/8f6930f) — `Verify MegaTag2 pose initialization and vision diagnostics`.
- **Setup:** Custom 4 m × 4 m practice field; robot physically placed at (2.0 m, 1.0 m), heading 180°, viewing AprilTag 1. Robot was facing approximately 0° at code deployment, then rotated physically; the Pigeon 2 tracked the rotation, allowing CTRE to report approximately 177.7°.
- **NetworkTables freshness test:** `Vision/MegaTag2AgeSeconds` remained small while connected; disconnecting Limelight Ethernet caused age to increase; reconnecting returned it to small values. An initial jump into the hundreds of seconds was observed and remains unexplained; do not treat clock/timestamp validation as exhaustive.
- **Target-validity test:** With no visible tag, `MegaTag2HasTag` and `MegaTag2Valid` were false while updates continued. With Tag 1 visible, the camera reported a valid pose. When the robot later rotated out of tag view using slow teleop, both validity indicators again went false; the last displayed vision pose could remain visible but must not be fused.
- **Initial position comparison:** With the robot physically at (2.000, 1.000) m and 180°, MegaTag2 reported approximately (1.983, 0.988) m and 177.7°. This is approximately 2.1 cm XY error against the stated physical placement; physical placement precision was not independently measured.
- **One-time initialization:** Pressing the `initialize_pose` button while disabled was accepted; CTRE `DriveState/Pose` subsequently reported approximately (1.982, 0.988) m, 177.7°. This verifies a successful one-time CTRE pose reset under the tested conditions, **not continuous fusion**.
- **Translation test:** Slow teleop produced a CTRE change from approximately (1.982, 0.988) m to (2.452, 0.986) m, i.e. +0.470 m X and -0.002 m Y, with heading changing from approximately 177.67° to 177.50°. Exact physical travel distance was not independently measured.
- **Post-translation vision comparison:** CTRE (2.452, 0.986) m versus MegaTag2 (2.440, 0.977) m; approximately 1.5 cm separation in XY. MegaTag2 receives CTRE's heading, so matching headings are **not independent confirmation** of heading accuracy.
- **Rotation/tag-loss test:** Slow teleop rotation yielded CTRE approximately (2.465, 0.991) m and 119.0° while the camera lost the tag. CTRE continued tracking rotation with minimal reported XY change.
- **Troubleshooting lesson:** Earlier startup/orientation tests showed misleading MegaTag1/MegaTag2 disagreement when the field heading was not established correctly. Do not assume robot physical heading and CTRE heading agree after startup. MegaTag2 relies on the heading supplied by CTRE.
- **Scope of verification:** Single-tag, small-field, short-distance tests only. No proof yet of robustness across headings, speeds, longer paths, tag occlusion/recovery during fusion, or competition field geometry.
- **Next steps:** (1) Establish an intentional and repeatable starting heading/pose workflow; (2) validate timestamp units and latency/clock conversion before injecting measurements; (3) implement gated continuous MegaTag2 fusion with new-sample, tag-count, freshness and plausibility checks, plus conservative XY uncertainty and negligible vision heading influence; (4) log raw odometry, raw vision, and fused pose separately; (5) test stationary, translation, rotation, tag loss/recovery, and auton.
- **2027 transition reminders:** Replace custom AprilTag map/field bounds with official 2027 geometry; confirm camera mounting transform and orientation after robot rebuild; remove or reassess practice-only teleop speed limits; update WPILib/vendor libraries and hardware configuration for SystemCore.

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

- **Status:** Continuous MegaTag2 XY fusion implemented and tested on practice robot on 2026-11-16; see dated entry above.
- **Hardware/setup reported:** Limelight 2+ on front-left of practice robot; custom AprilTag practice layout.
- **Prior stationary tests reported:** Mean MegaTag2 position error approximately **5.5 cm** across five tests after camera calibration, compared with approximately **17.9 cm** across three earlier tests. These results are specific to the practice setup, not a general accuracy guarantee.
- **Current distinction:** Raw MegaTag2 pose and the fused Phoenix 6 pose are displayed; one-time pose initialization is separate from continuous fusion. An independent odometry-only estimate is not yet logged.
- **Next work:** Test autonomous driving with fusion; implement automatic starting heading/pose; add independent odometry-only logging and further outlier/physical accuracy tests.
- **Competition transition:** Replace custom field/tag geometry with official 2027 configuration and remeasure camera mounting after robot construction.

## Next-session checklist

- [ ] Pull latest code and deploy classroom speed limits
- [ ] Verify slow teleop driving and document observations
- [ ] Decide whether slew-rate limiting is needed
- [x] Implement and test guarded continuous vision fusion (stationary, teleop, tag loss/recovery)
- [ ] Test autonomous driving with fusion and deliberate starting heading/pose
- [ ] Log test results and any new competition-transition tasks

## 2026-11-15 — Required opposite-facing AprilTag localization test

- **Status:** Planned; **not yet verified**.
- **Observation:** Recent localization testing had the robot facing only AprilTag 6.
- **Required next test:** Orient the robot toward **AprilTag 1**, at the approximately **180° robot heading** identified for the practice setup, and verify localization.
- **Record:** Known physical heading and position, Limelight reported MegaTag2 pose and heading, drivetrain/gyro heading, any MegaTag1-vs-MegaTag2 disagreement, and whether the estimate remains stable when rotating between orientations.
- **Why:** Check for orientation-dependent errors, including a possible 180° discrepancy seen in earlier-season vision work. Do not declare vision fusion verified until this direction is tested.
- **Note:** Confirm the actual measured robot heading at test time; the 180° value is the planned test orientation, not a measurement from this session.
