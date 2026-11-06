// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Pose2d;
//import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class RobotContainer {
    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
        joystick.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // Reset the field-centric heading on left bumper press.
        joystick.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        drivetrain.registerTelemetry(logger::telemeterize);
    }

public Command getAutonomousCommand() {
    System.out.println("AUTONOMOUS COMMAND REQUESTED!");

    // Robot-relative drive request using closed-loop velocity control.
    final var autoDrive = new SwerveRequest.RobotCentric()
        .withDriveRequestType(DriveRequestType.Velocity);

    final var idle = new SwerveRequest.Idle();

    return Commands.sequence(
        Commands.print("AUTONOMOUS SEQUENCE STARTED!"),

        // Record pose before resetting.
        Commands.runOnce(() ->
            System.out.println("BEFORE RESET: " + drivetrain.getState().Pose)
        ),

        // Reset odometry to (0, 0, 0 degrees).
        drivetrain.runOnce(() ->
            drivetrain.resetPose(
                new Pose2d(0.0, 0.0, Rotation2d.kZero)
            )
        ),

        // Allow odometry to update before driving.
        Commands.waitSeconds(0.5),

        // Record pose after resetting.
        Commands.runOnce(() ->
            System.out.println("AFTER RESET: " + drivetrain.getState().Pose)
        ),

        Commands.print("AUTONOMOUS DRIVE STARTED!"),

        // Rotate counterclockwise at 0.5 rad/s until heading reaches 90 degrees.
        // Stop after 6 seconds if the target isn't reached.
        drivetrain.applyRequest(() ->
            autoDrive.withVelocityX(0.0)
                .withVelocityY(0.0)
                .withRotationalRate(0.5)
        )
        .until(() ->
                drivetrain.getState().Pose.getRotation().getRadians() >= Math.PI / 2.0
        )
        .withTimeout(6.0),

        // Stop immediately after the drive command finishes.
        drivetrain.runOnce(() ->
            drivetrain.setControl(idle)
        ),

        // Record the final pose.
        Commands.runOnce(() ->
            System.out.println("FINAL POSE: " + drivetrain.getState().Pose)
        ),

        Commands.print("AUTONOMOUS SEQUENCE FINISHED!"),

        // Keep the drivetrain idle for the remainder of autonomous.
        drivetrain.applyRequest(() -> idle)
    );
}
}
