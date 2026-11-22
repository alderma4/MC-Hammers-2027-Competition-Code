# SystemCore Bench Bring-Up and Transition Plan

**Planning date:** 2026-11-22  
**Status:** Planned. SystemCore purchase expected December 3, 2026; arrival around mid-December is an estimate, **not a shipping commitment**.  
**Objective:** Learn, test, and validate the new controller without interrupting working roboRIO-based swerve, PathPlanner, and Limelight development.

## Guiding decisions

- **Bench first.** Keep the existing roboRIO practice robot operational and its current code checkpointed in GitHub; do not decommission it just because SystemCore arrives.
- **Thermal mounting:** Mount SystemCore firmly to an adequately sized **1/8-inch aluminum plate** as previously planned, with good metal-to-metal thermal contact and sensible standoffs/fasteners according to the manufacturer's current mounting instructions. Do not test it loose on wood or suspended in air as its permanent thermal arrangement. Avoid shorts, restricted ventilation, or unintended contact with powered conductors.
- **Safety and electrical:** Follow the current manufacturer's electrical specifications for power, wiring, breakers, and data interfaces. Do not assume roboRIO connectors/pinouts, power requirements or peripheral support transfer unchanged. Confirm the official documentation and supported WPILib/vendor software versions before wiring or deploying.
- **Separate hardware network:** Bench components should not conflict with the live robot's CAN IDs, bus termination, radio settings, or NetworkTables instance. Start with isolated devices and only connect to the live robot under a planned controlled integration test.
- **Scope by December 31:** Successful boot, reliable communication, basic deployment and sample sensor/CAN telemetry are meaningful milestones. **Full drivetrain migration is not a December requirement.**

## Schedule and verification checklist

| Target | Action | Evidence / success criterion | Status |
| --- | --- | --- | --- |
| Before Dec. 3 | Review current SystemCore installation/power/thermal requirements and vendor/WPILib compatibility; collect aluminum mounting plate, approved power components, cabling, network connection, and a safe test fixture | Written power/network plan and bill of materials; nothing removed from practice robot | Planned |
| Dec. 3 | Purchase one SystemCore | Order receipt and actual estimated delivery date recorded when available | Planned |
| Early Dec. (parallel) | Continue AprilTag stands and rebuilt Limelight mount, selected-auto readiness logic, and persistent Elastic diagnostics on roboRIO | Working practice robot remains available for testing | Planned |
| Arrival + first session | Inspect unit, record product/firmware details and serial for **internal records only**, mount to aluminum heat spreader, verify wiring polarity/fusing, power safely | Boots reliably; no abnormal heat, wiring or boot faults | Waiting for delivery |
| Mid-Dec., if delivered | Establish supported network connection to development PC, robot radio/Driver Station setup as appropriate; verify NetworkTables and dashboard communications | Connection and telemetry survive cold boot and reconnect | Planned |
| Mid/late Dec. | Install supported WPILib environment and Phoenix 6/vendor dependencies; deploy a minimal program; review logs/diagnostics | Repeatable clean build/deployment and a basic published status in Elastic | Planned |
| Late Dec. | Test supported CAN interface on an **isolated bench bus** with correct 120-ohm termination at each bus end and one known CTRE device (e.g. spare CANcoder if electrically supported) | Stable detected device, correct CAN ID, firmware/status telemetry, no repeated CAN errors | Planned |
| Late Dec. (stretch) | Validate Pigeon 2 or other sensor on a spare/test bus; publish sensor health for readiness dashboard | Sensor telemetry and startup behavior documented; no interruption to current drivetrain | Planned |
| January 2027 | Build a cautious staged migration plan for swerve, CAN-bus segmentation, cameras, radio, and production readiness features, after kickoff rules/software versions are known | Documented rollback, preserved calibration, hardware mapping, and full regression plan | Planned |

If shipping is delayed, keep the milestone order but slide SystemCore tasks into January; **do not sacrifice vision/Elastic readiness progress or rush wiring to meet an arbitrary date**.

## Initial bench setup notes

- Bench components: SystemCore, aluminum mounting plate, appropriate current-limited/fused power source and manufacturer-required connectors, network cable/adapter, supported radio/network setup, and laptop with Driver Station, Elastic and development tools. Use a known-good cable and verify supported IP addressing rather than guessing.
- An isolated **spare** CANcoder or other test device is preferable to borrowing from the operational drivetrain. Check which SystemCore CAN channel, physical interface, adapters and termination are supported before connecting.
- Do not use an unverified CAN topology. When evaluating multiple SystemCore CAN loops, create an explicit diagram with which devices are on which bus, termination and device-ID namespace, then test one bus at a time.
- Capture boot time, firmware, deployed robot software version, connection behavior, bus errors, sensor heartbeat and basic thermal observations under representative bench conditions.
- Store evolving instructions in public GitHub documentation, but **do not publish secrets, passwords, private network credentials or device serials unnecessarily**.

## Transition checks: competition robot

1. Preserve the currently verified `TunerConstants.java`, CAN ID table, camera-to-robot transform and drivetrain physical dimensions. Confirm all still apply to the new 2027 chassis rather than blindly copying.
2. Explicitly document which CAN devices and channels are connected after migration; account for multiple buses and 120-ohm end termination.
3. Check Phoenix 6, WPILib, PathPlanner, AdvantageScope/Elastic, Limelight/NetworkTables and any LED dependencies against the actual new controller.
4. Reverify encoder offsets, gyro reference and field heading; test straight 0.5 m and 2 m motion, heading and strafe; compare physical tape measures and estimated pose. Avoid carrying over old custom-practice-field settings unreviewed.
5. Revalidate continuous vision fusion, automatic pose initialization, selected-auton readiness, live Elastic warnings, and CANdle indication on the final robot.
6. Require a documented successful cold boot, safe disabled behavior, full match-start process and rollback plan before retiring the roboRIO test environment.

## Relationship to other plans

- [Autonomous Readiness and Match Startup Plan](AUTONOMOUS_READINESS_PLAN.md): December readiness/Elastic/LED goals remain the primary **robot-facing** development path, while SystemCore is a parallel bench-only hardware workstream.
- [Development Log](DEVELOPMENT_LOG.md): Record actual arrival, wiring, deployed versions, test results and issues with dated entries as each is verified.
- [README](../README.md): Current hardware reference remains the **roboRIO practice robot** until SystemCore is truly integrated and verified.

**Current status:** Planning only; no SystemCore hardware, firmware, network, CAN or robot-code test has been reported as performed here.
