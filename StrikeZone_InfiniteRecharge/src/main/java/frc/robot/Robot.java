/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  XboxController XBDriver = new XboxController(0);

  Solenoid shiftHigh = new Solenoid(0);
  Solenoid shiftLow = new Solenoid(1);
  int shiftState = 0;


  Drivetrain DT = new Drivetrain();
  @Override
  public void robotInit() {
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    double speed = -XBDriver.getY(Hand.kLeft);
    double rotate = -XBDriver.getX(Hand.kRight);


    DT.arcadeDrive(DT.Deadband(speed), DT.Deadband(rotate));
    if(XBDriver.getBumperPressed(Hand.kLeft)) shiftState++;
    if(shiftState == 1){
      shiftHigh.set(false);
      shiftLow.set(true);
    }else if(shiftState >= 1){
      shiftLow.set(false);
      shiftHigh.set(true);
      shiftState = 0;
    }
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
