# 2027 Technical Intelligence and Verification Register

**Established:** 2026-11-25  
**Scope:** FRC Team 7656, 2027 SystemCore/WPILib/FIRST robot and field migration; current 2026 roboRIO practice robot remains the verified baseline.  
**Operating rule:** External documentation is evidence of *published requirements or claims*, not proof that our hardware/code works. Alpha/beta instructions must be checked against the exact production versions installed before we make irreversible changes.

## Evidence hierarchy and status vocabulary

1. **Final authoritative:** 2027 FIRST Game Manual, published season field/layout, current official WPILib/SystemCore docs, current manufacturer manuals and release notes. Rules and interfaces take precedence over workshop slides or older examples.
2. **Official evolving:** current WPILib 2027 alpha/beta changelog, known issues, SystemcoreTesting compatibility tables, official vendor prerelease notes. Record exact versions and date checked.
3. **Technical presentations:** 62-slide [Systemcore Crash Course Workshop - BETA](https://docs.google.com/presentation/d/1oMvZ2o_o5drLS9BC2VHq0zPP7Y5rU7R6WFHZOGXgUsE/edit) (SoCal Showdown / Team 4201). Useful hardware/transition guidance, **not production-version authority**.
4. **Community sources / hypotheses:** Chief Delphi and individual team experience; independently reproduce before treating as verified on Team 7656.

**Source state:** Published/confirmed by source; Beta-only/outdated risk; Pending official confirmation; or Conflicting sources.  
**Team state:** Planned; Implemented but untested; Verified with physical/log evidence; or Blocked.  
**Do not use “verified” to describe an external statement when we have not tested it ourselves.** Attach relevant source URL, checked date, software/hardware versions, and test/commit reference to each completed checkpoint.

## Source watchlist (review before each migration milestone and at kickoff)

| Domain | Primary reference | Why it matters |
|---|---|---|
| WPILib changes | https://docs.wpilib.org/en/latest/docs/yearly-overview/yearly-changelog.html | Java namespaces, class removals, timing, telemetry and SystemCore behavior |
| WPILib known issues | https://docs.wpilib.org/en/latest/docs/yearly-overview/known-issues.html | Fixes, regressions and workarounds; check during debugging |
| WPILib releases | https://github.com/wpilibsuite/allwpilib/releases | Exact installer version and prerelease caveats |
| SystemCore compatibility | https://github.com/wpilibsuite/SystemcoreTesting | Image/DS/WPILib/vendor supported combinations |
| CTRE Phoenix 6 | https://github.com/wpilibsuite/SystemcoreTesting/blob/main/CTR-Phoenix.md | CANBus constructors, firmware, generated swerve code compatibility |
| REV | https://github.com/wpilibsuite/SystemcoreTesting/blob/main/REV.md | Device constructors, firmware, and bus IDs |
| Elastic | https://github.com/wpilibsuite/SystemcoreTesting/blob/main/Elastic.md | 2027 DS and NetworkTables integration |
| AdvantageScope | https://docs.advantagescope.org/ and https://docs.advantagescope.org/more-features/coordinate-systems/ | Viewer version, stale measurement rendering, coordinate conventions |
| AdvantageScope compatibility | https://github.com/wpilibsuite/SystemcoreTesting/blob/main/AdvantageScope.md | Prerelease known issues |
| SystemCore workshop | https://docs.google.com/presentation/d/1oMvZ2o_o5drLS9BC2VHq0zPP7Y5rU7R6WFHZOGXgUsE/edit | Mechanical, electrical, network and import checklist |
| Final 2027 game | FIRST 2027 competition manual and field drawings after kickoff | Only final source for 2027 game geometry, legal hardware, and field rules |

## Open technical checkpoints

| ID | Question / required validation | Source / reason | Team state |
|---|---|---|---|
| SC-01 | Verify production hardware dimensions, mounting screw limits, thermal interface and 1/8-inch aluminum plate arrangement | Workshop slide 8; manufacturer drawings; earlier simplified STEP file | Planned |
| SC-02 | Obtain **production** pinout/power/RSL connector documentation; check exact Molex SL gold/tin mating surfaces, Micro-Fit+ mating parts, polarity and latching | Workshop slides 9–12; beta plated SmartIO/I2C differently | Planned |
| SC-03 | Confirm what cables actually ship with *retail* SystemCore; beta giveaway inventory (slides 10–11) is not a promise | Workshop slides 10–11 | Planned |
| SC-04 | Validate boot, OLED, current system image, web UI, USB-C/Ethernet access, team-number configuration, logs and fault displays | Workshop slides 19–29, 31 | Planned |
| SC-05 | Configure VH-109 **SystemCore mode** and verify 10.76.56.2 addressing; test separate from active roboRIO network | Workshop slides 20–21 | Planned |
| SC-06 | Record exact **mutually compatible** SystemCore image, WPILib, 2027 DS, Phoenix 6, PathPlanner, REV, Elastic and AdvantageScope versions | SystemcoreTesting and official release/known-issue pages | Planned |
| SC-07 | Plan five physical CAN buses with bus-specific device IDs, wiring termination and failure isolation; record shared S0/S1 and S3/S4 internal bandwidth caveat | Workshop slides 13–18 | Planned |
| SC-08 | Verify real CAN FD vs classic-CAN support, effective speed and bus metrics *per device*; never assume all CAN hardware is FD-capable | Workshop slide 13 and vendor manuals | Planned |
| SC-09 | Verify Phoenix 6 explicit CANBus object and **regenerate or review TunerConstants**, CTRE feedback, motor limits, neutral mode, device firmware | Workshop slide 51 and current CTRE release | Planned |
| SC-10 | Verify Pigeon 2 versus SystemCore onboard IMU orientation, availability and drift; maintain independent absolute field-heading confidence for vision readiness | Team vision architecture | Planned |
| SC-11 | Evaluate USB-camera/SystemCore vision, supported camera list, camera latency and CPU priority *without presuming equivalence to Limelight 4* | Workshop slide 30; beta USB2 limitation may be obsolete | Planned |
| SW-01 | WPILib 2026→2027 import in **separate folder and Git branch**; preserve source, Git history and custom Gradle edits; verify no duplicate Main.java | Workshop slides 37–42 | Planned |
| SW-02 | Repair all namespace/class/HID changes and robot startup; assess Commands v2 compatibility separately from optional Commands v3 / OpMode | Workshop slides 35, 43–48 | Planned |
| SW-03 | Audit hardware I/O lost/replaced by SystemCore: SPI, relay, digital counters/interrupts, analog outputs, PWM, SmartIO, servos | Workshop slides 49–50; final production docs | Planned |
| SW-04 | Confirm 2027 controller/DS button mapping, FMS operation on Windows, joystick status and disabled/auto/teleop transitions | Workshop slides 44–45, 53; current DS release | Planned |
| SW-05 | Validate independent logging, AdvantageScope 2027 visualization, Elastic selection/readiness, NetworkTables4 and AdvantageTune mobile diagnostics | Official dashboard documentation and team code | Planned |
| VIS-01 | Finish current Blue-frame Tag 1↔6 raw MegaTag2 consistency and guarded fusion on roboRIO before converting practice setup | Existing [SystemCore plan](SYSTEMCORE_BRINGUP_PLAN.md) | Planned |
| VIS-02 | Establish 2027 Center/Red frame: field center origin, +X away from red (toward blue), +Y left of +X; confirm all transforms/angles | AdvantageScope coordinate reference; 2027 official layout pending | Planned |
| VIS-03 | Verify Limelight raw output frame, full tag map translations/rotations and timestamp-domain conversion for Phoenix estimator; no blind WPI Blue mixing | Existing vision fusion architecture and 2027 APIs | Planned |
| AUTO-01 | Verify PathPlanner 2027 coordinates, navgrid, alliance mirroring, start/target pose, and disable unsafe auto resetOdom override | [Autonomous Readiness Plan](AUTONOMOUS_READINESS_PLAN.md) | Planned |
| AUTO-02 | Physical practice/autonomous tests at known positions and opposite headings; match-ready green only after source/heading/freshness conditions truly pass | Autonomous readiness policy | Planned |
| ELEC-01 | Confirm Kraken X60 supply/stator current limits and test sudden direction changes, voltage sag/brownouts, slew-rate limits | Team power-development priority | Planned |
| FIELD-01 | Await 2027 kickoff field manual; decide blue-half build plus practical midfield buffer, surveyed AprilTag positions and permitted auto crossing | Planning discussion only; official game unknown | Planned |

## Required milestone gates

**Gate A — Current practice robot:** Known-good 2026 roboRIO code preserved, AprilTag raw pose and fusion tested independently, logs saved. No SystemCore-related changes to this robot merely to follow alpha guidance.

**Gate B — Powered SystemCore bench:** Manufacturer-compliant wiring/thermal mounting, verified boot, firmware, local network, radio SystemCore mode, DS link and NetworkTables.

**Gate C — CAN bench:** One known device per validated bus topology, termination, motor/encoder status, commanded behavior, firmware and fault metrics; then design production distribution.

**Gate D — Migrated software:** Successful clean import/build, proper 2027 vendor versions, verified controls, no unsupported I/O, fully documented CANBus assignments.

**Gate E — Field-frame confidence:** Verified Center/Red mapping across tag map, raw Limelight, heading/gyro, CTRE estimate, PathPlanner, and AdvantageScope using independently measured robot positions.

**Gate F — Match/autonomous readiness:** Repeatable cold-boot/disabled behavior, tested PathPlanner paths from intentionally varied starting poses, bad-vision/fault fallback, selected-auto readiness/LED truthfulness, power and comms reliability.

## Change-management discipline

- Before each integration session, compare the relevant current official sources with our pinned/installed versions; record version/date and any new breaking changes.
- Keep a short changelog of **what changed, implications, action, owner, due milestone and evidence of completion**. Do not convert untested theories into fact.
- Treat updates to firmware, vendor dependencies, Java APIs, path files, field coordinates and controls as separately reversible changes wherever possible.
- Capture sensor/pose timestamps, CAN diagnostics, physical measurements, code commit and conditions with every physical verification.
- **2027 production state is not yet tested.** This register documents investigation and preparation, not a guarantee of future-version compatibility.
