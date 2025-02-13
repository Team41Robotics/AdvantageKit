// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.subsystems.launcher;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.util.Units;

public class LauncherIOSparkMax implements LauncherIO {
  private final CANSparkMax launchMotor = new CANSparkMax(10, MotorType.kBrushless);
  private final CANSparkMax feedMotor = new CANSparkMax(11, MotorType.kBrushless);
  private final RelativeEncoder launchEncoder = launchMotor.getEncoder();
  private final RelativeEncoder feedEncoder = feedMotor.getEncoder();

  public LauncherIOSparkMax() {
    launchMotor.restoreFactoryDefaults();
    feedMotor.restoreFactoryDefaults();

    launchMotor.setCANTimeout(250);
    feedMotor.setCANTimeout(250);

    launchMotor.setInverted(false);
    feedMotor.setInverted(false);
    launchMotor.enableVoltageCompensation(12.0);
    feedMotor.enableVoltageCompensation(12.0);
    launchMotor.setSmartCurrentLimit(80);
    feedMotor.setSmartCurrentLimit(80);

    launchMotor.burnFlash();
    feedMotor.burnFlash();
  }

  @Override
  public void updateInputs(LauncherIOInputs inputs) {
    inputs.launchPositionRad = Units.rotationsToRadians(launchEncoder.getPosition());
    inputs.launchVelocityRadPerSec =
        Units.rotationsPerMinuteToRadiansPerSecond(launchEncoder.getVelocity());
    inputs.launchAppliedVolts = launchMotor.getAppliedOutput() * launchMotor.getBusVoltage();
    inputs.launchCurrentAmps = new double[] {launchMotor.getOutputCurrent()};

    inputs.feedPositionRad = Units.rotationsToRadians(feedEncoder.getPosition());
    inputs.feedVelocityRadPerSec =
        Units.rotationsPerMinuteToRadiansPerSecond(feedEncoder.getVelocity());
    inputs.feedAppliedVolts = feedMotor.getAppliedOutput() * feedMotor.getBusVoltage();
    inputs.feedCurrentAmps = new double[] {feedMotor.getOutputCurrent()};
  }

  @Override
  public void setLaunchVoltage(double volts) {
    launchMotor.setVoltage(volts);
  }

  @Override
  public void setFeedVoltage(double volts) {
    feedMotor.setVoltage(volts);
  }
}
