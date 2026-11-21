# MC-Hammers-2027-Competition-Code
FRC Team 7656 competition robot code for the 2027 FRC season, "BIOCORE".

## Drivetrain Hardware

This section documents the current offseason practice robot configuration. Hardware and software may change when transitioning to the 2027 competition robot and SystemCore.

- **Swerve modules:** 4 SDS MK5n (2 A and 2 B)
- **Drive motors:** 4 CTRE Kraken X60
- **Steering motors:** 4 CTRE Kraken X44
- **Absolute encoders:** 4 CTRE CANcoders
- **Gyroscope:** CTRE Pigeon 2
- **LED controller:** CTRE CANdle
- **AprilTag/Vision Camera:** LimeLight 2+
- **Wheel diameter:** 4 inches
- **Drive gear ratio:** 5.2734375:1
- **Steering gear ratio:** 26.0909:1
- **Module spacing:** 24.25 × 24.25 inches, center to center

### CAN Device Assignments

| CAN ID | Device | Location / Function |
| ---: | --- | --- |
| 1 | Kraken X60 | Front-left drive |
| 2 | Kraken X44 | Front-left steering |
| 3 | CANcoder | Front-left encoder |
| 4 | Kraken X60 | Front-right drive |
| 5 | Kraken X44 | Front-right steering |
| 6 | CANcoder | Front-right encoder |
| 7 | Kraken X60 | Back-left drive |
| 8 | Kraken X44 | Back-left steering |
| 9 | CANcoder | Back-left encoder |
| 10 | Kraken X60 | Back-right drive |
| 11 | Kraken X44 | Back-right steering |
| 12 | CANcoder | Back-right encoder |
| 13 | Pigeon 2 | Gyroscope |
| 14 | CANdle | Addressable LEDs |

**CAN inventory:** 14 devices in the current configuration (8 motors, 4 CANcoders, 1 Pigeon 2, and 1 CANdle).

The drivetrain CAN IDs, gearing, wheel diameter, and module spacing are defined in `src/main/java/frc/robot/generated/TunerConstants.java`. The CANdle assignment is documented from the team's hardware setup, not that generated drivetrain file.

The REV Power Distribution Hub (PDH) is not included in the 14-device CAN count; it communicates separately from the CTRE CAN network.

## Development Documentation

- [Development process](docs/DEVELOPMENT_PROCESS.md): how we document hardware, code, testing, and verification.
- [Development log](docs/DEVELOPMENT_LOG.md): changes, test results, next steps, and practice-to-competition transition reminders.

**Status convention:** Planned = not built/coded; Implemented = built/coded but not yet verified; Verified = tested with recorded results.
