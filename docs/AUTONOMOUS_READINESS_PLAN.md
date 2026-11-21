# 2027 Autonomous Readiness and Match Startup Plan

**Planning date:** 2026-11-21  
**Status:** PLANNED — design requirements and proposed milestones, not implemented or verified  
**Scope:** Team 7656 offseason practice robot first; transfer and revalidation on the 2027 competition robot

## 1. Match-day operating concept (agreed goal)

1. Drive team keeps Driver Station laptop powered on, brings powered-off robot to field on cart.
2. In Elastic, driver chooses the intended autonomous routine before placement (selection must remain visible and must be confirmed in robot code).
3. Field crew places robot *approximately* in the chosen autonomous's legal starting region and roughly correct heading, powers it on, then waits for the robot and Driver Station to connect.
4. Robot boots and automatically checks drivetrain sensor communication, Pigeon 2 status, pose-reference validity, camera freshness/health, and the chosen autonomous's startup requirements. No students need to push a robot calibration button or manually arm vision.
5. Camera(s) and sensor fusion establish the best trustworthy field pose when sufficient measurements are available, **without driving or turning while disabled**. Student-facing LEDs display actual readiness; detailed reason and data are on Elastic.
6. Field crew leaves once the field permits; match-start autonomous uses a trusted initial pose, not a blind pose reset to the ideal PathPlanner anchor.
7. If checks fail, clearly display not ready and invoke an intentional, safe, tested policy at auton start. Never equate mere communications or tag visibility with autonomous-ready status.

**Green means:** For the *currently selected* auto and current alliance interpretation, sensors are healthy, an adequately fresh and trustworthy starting pose/field heading is available (or routine-specific verified fallback is explicitly allowed), and the physical pose lies within that routine's *validated* start tolerance. It does **not** guarantee match success.

## 2. Missing requirements and failure modes to design explicitly

- **Selection binding:** The SendableChooser/Elastic choice must be readable before auto starts and changes should immediately recompute readiness. Maintain a safe default / do-nothing option; show exact currently selected command. Check that selected auto paths/configurations exist. Distinguish selected from running auto.
- **Alliance handling:** On red/blue, compare target pose and heading in a single consistent WPILib field coordinate system using the same mirroring convention PathPlanner uses. Handle alliance unknown or late arrival conservatively. Avoid double-flipping poses.
- **Pose sources:** Separate raw vision pose, fused CTRE pose estimator, and (if added) independent wheel/gyro-only odometry. A MegaTag2 pose that uses the Pigeon-supplied heading cannot independently certify that the gyro's field orientation is correct. Establish and validate field-relative heading through an intentional, independently credible approach (e.g., robust/tag-based absolute-heading initialization such as valid MegaTag1/multi-tag or surveyed physical starting reference). Reject ambiguous or inconsistent headings. Do not continually reset the gyro on any tag sighting.
- **Boot and disabled mode:** Sensors and vision may initialize while disabled; LED state must remain meaningful during robot boot, network reconnections and NT delays. Do not drive motors or move chassis while disabled. Verify CANdle LED behavior within competition robot/field safety rules; LEDs must not substitute for FMS/Driver Station readiness.
- **Confidence, freshness, and stability:** Validate CAN/gyro status, rates, timestamp/freshness, camera connection, actual detection (not last-seen pose), tag count/geometry, pose bounds, estimator-vs-vision disagreement, and consistent multiple recent samples. Thresholds and hold times are initial engineering parameters to empirically tune, not promises of accuracy.
- **Routine-specific tolerances:** Each auto declares expected start pose, legal *region* (accounting for bumper size/field restrictions), allowed translation/heading deviation, whether vision is mandatory and acceptable fallback. A wide starting region is appropriate only if the auton truly accommodates start offsets. A change to the routine or selected alliance invalidates the previous ready state.
- **PathPlanner reset interaction:** Existing `.auto` configurations use `resetOdom=true`. Blindly resetting pose to ideal anchor at autonomous start discards real location; evaluate disabling the reset and providing verified initialization via the existing Phoenix 6 estimator/AutoBuilder configuration. Ensure PathPlanner path execution and robot-control transforms remain consistent and verify all existing auto routines.
- **Moving robot while disabled:** When field crew repositions or rotates the robot, readiness should update; invalidate when gyro/vision pose cannot confidently follow a carried robot (wheel encoders do not observe lifting). No stale or latched green. Pose may need automatic reinitialization *while disabled*, but only with independently trustworthy observations and controlled checks.
- **Final recheck and fallback:** Revalidate on auto-init after field enable, including any stale-data or gyro faults. Define testable policies by auto: refuse to drive / safe do-nothing if requirements fail, or run a deliberately verified non-vision fallback where safe. **Never silently run a precise auto from a fabricated start pose.**
- **Mechanics/vision:** Replace broken Limelight mount, measure camera XYZ and roll/pitch/yaw again, rigidly secure it, verify tag map/physical tag placement and coverage at legal starting positions. Plan additional cameras only if coverage demands it. Old mounting calibration is not trustworthy after a mechanical change.
- **Communication/UI:** LED feedback must not mislead. Elastic should show selected auto, alliance, expected vs measured pose, XY/heading error, camera freshness, source and confidence, gyro condition, startup/fallback state, reason code, and readiness transitions. Rate-limit/hysteresis to avoid flashing on brief marginal readings; readiness itself must still become invalid promptly on real failures.
- **Validation conditions:** Test cold boot, missing/stale tags, bad or wrong tag map, wrong selected auto, alliance switching, camera unplug/reconnect, gyro absence or mismatched heading, moving/repositioning robot disabled, last-second pose change, unfavorable tag angle, temporary lost tags, robot stopped at auto-init, and a safe fallback. Document physical measurements independently of robot-reported pose.
- **Robot power/rules:** Check 2027 robot and event rules when released; ensure lighting, wiring, power budget, field access and network interactions comply. Robot powered off on cart is planned workflow; camera startup time and Driver Station/FMS synchronization must fit actual pre-match field time.

## 3. Proposed LED states (design target)

| Display | Intended meaning | Suggested Elastic details |
|---|---|---|
| Amber (initializing/checking) | Boot, calibration, gathering stable fresh measurements, or awaiting selection/alliance information | Current check and elapsed time |
| Red (not ready) | A required check failed or actual pose lies outside selected auto's allowed region | Specific failure(s), measured error, suggested reposition direction where safe |
| Green (ready for selected auton) | All *current* routine-specific readiness checks passed and have remained good over a validated hold interval | Auto name, alliance, target/estimated pose, quality metrics |
| Optional distinct fallback status | Explicitly tested fallback is available, but precision auto requirements not satisfied | Precise warning; never represent as normal green |

Define precise patterns/brightness later with CANdle hardware and event rules. Do not rely solely on color (consider blink patterns/readable dashboard).

## 4. Implementation architecture (planned, not completed)

1. **Auto metadata / registry:** centralized definitions for named autos, PathPlanner file, start pose, alliance transform, tolerance and vision dependency. Investigate best way to obtain reliable start pose from current PathPlanner auto files; do not duplicate inconsistent manual coordinates.
2. **SensorHealth:** CANcoder/module telemetry, Pigeon health and startup status; avoid declaring success from a single boolean alone.
3. **VisionStatus + localization:** camera connection, detection freshness, valid robust field pose, heading-establishment strategy, rejection reasons, fusion availability, independent evidence of field orientation.
4. **AutonReadinessEvaluator:** pure comparison of selected auto, alliance, measured pose, tolerances, sensor statuses, freshness and stable history; outputs ready/not-ready/initializing and reason codes.
5. **LED and Elastic presenters:** both consume the *same* readiness result, preventing a green light while the dashboard reports not-ready.
6. **Autonomous initialization policy:** safely choose trusted real pose vs tested fallback, configure/reset estimator when appropriate, avoid blind PathPlanner reset, require safe behavior on invalid startup.
7. **Logging:** readiness transitions, selected auto, alliance, sensor status, initial pose sources, target/current XY/theta, decisions and actual physical trial results in AdvantageScope/CTRE logs.

## 5. Milestones through December 2026

| Target | Deliverable | Evidence needed | Status |
|---|---|---|---|
| Late November | Rebuild robust Limelight mount; build AprilTag stands and verify custom map, camera pose and physical tag positions | Fixed camera transform and surveyed reference tests; known good tag visibility | Planned |
| Early December | Read selected auto and alliance; declare start pose/allowed region; implement basic sensor and camera health diagnostics | Select different autos and move robot while disabled; readiness conditions change appropriately | Planned |
| Mid December | Implement CANdle readiness indications and Elastic reason codes; stabilize fresh-pose checks | Cold-boot, no-tag, wrong-start and moved-robot tests; no falsely reassuring green | Planned |
| Late December | Prototype automatic starting heading/pose initialization and PathPlanner reset/fusion coordination for a selected practice-field auto | Repeat controlled autonomous runs from several acceptable starting poses, compare independent physical errors, confirm safe behavior when not ready | Stretch goal; planned |
| January / post-kickoff | Update 2027 field layout, real selected autos, legal start regions, new camera placement and SystemCore/WPILib/vendor versions | Full revalidation on 2027 robot and field-like geometry, plus field/lighting conditions | Required transition |
| Before first event | Match-rehearsal workflow with students, boot timing, lost tags and selected-auto changes, alliance red/blue, safe failure handling | Written checklist and documented repeated tests; event-rule compliance | Required transition |

**Priority:** Implement truthful diagnostics and selection-specific LED indication *before* promising fully automatic pose initialization. This is a staged deliverable, not a guarantee of a fully autonomous-ready competition system by December.

## 6. Decisions needed when implementing (not blockers for planning)

- Per-auto acceptable start translation, heading, and legal boundaries, based on future 2027 game constraints.
- Which independently credible heading initialization method works with the available camera geometry and the one-tag practice field; if none, **do not turn green** under a vision-dependent policy.
- How precise routines behave when vision unavailable at match start (safe do-nothing vs explicitly qualified fallback).
- Final pattern/meaning of CANdle LEDs, taking field lighting and accessibility into account.
- Whether an extra forward/rear camera or surveyed additional tags is needed for robust coverage.
- Whether to retain existing five-second stationary steering test as a chooser option for off-season only; avoid confusing it with production auto.

## 7. Relationship to existing 2026 work

- Continuous guarded MegaTag2 fusion on the practice robot was **already implemented and tested Nov 16**, but manual `initialize_pose` and explicit fusion enabling remain workflow gaps. Review existing implementation before changing it.
- Nov 19: steering offsets and carpet-loaded steering verified sufficiently to proceed; substantial physical lateral carpet drift was not reflected by CTRE estimated Y.
- Nov 20: six 4 m tile trials had considerably less sideways drift than carpet, supporting surface influence; the exact source of carpet interaction remains unconfirmed.
- **This document is a future implementation plan only. No code was changed and no readiness system is yet verified.** Maintain a dated development log entry when any feature is implemented and physically tested.
