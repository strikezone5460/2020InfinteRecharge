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
import frc.robot.Subsystems.*;

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
  
  // Solenoid shiftHigh = new Solenoid(0);
  // Solenoid shiftLow = new Solenoid(1);
  Shooter SH = new Shooter();
  Drivetrain DT = new Drivetrain();

  
  int shiftState = 0;
  int shooterState = 0;
  int counter = 0;

  boolean isHigh = false;
  @Override
  public void robotInit() {
    DT.Init();
  }

  @Override
  public void disabledPeriodic(){
    //System.out.println("left: " + DT.leftEncPos + "right: " + DT.rightEncPos);
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  
  }

  @Override
  public void teleopInit() {
    DT.Init();
  }

  @Override
  public void teleopPeriodic() {
    double speed = XBDriver.getY(Hand.kLeft);
    double rotate = XBDriver.getX(Hand.kRight);
    isHigh =  DT.shiftHigh.get();

     DT.leftEncPos();
     DT.rightEncPos();

    counter++;
    if((counter%5)==0){
      System.out.println("left: " + (DT.leftEncVel()) + " right: " + (DT.rightEncVel()) + "is High: "+ isHigh);
    }

    SH.turretLogic();

    //System.out.println();
    //percent output drive code
    //DT.arcadeDrive(DT.Deadband(speed), DT.Deadband(rotate)*.75);
    DT.velocityDrive(DT.Deadband(speed), DT.Deadband(rotate)*.75, isHigh);
    //Shifter toggle
    if(XBDriver.getBumperPressed(Hand.kLeft)) shiftState++;
    if(shiftState == 1){
      DT.shiftHigh.set(false);
      DT.shiftLow.set(true);
     // isHigh = !isHigh;
    }else if(shiftState >= 1){
      DT.shiftLow.set(false);
      DT.shiftHigh.set(true);
     // isHigh = !isHigh;
      shiftState = 0;
    }

    


    //
    //Toggle for the shooter
    //
    if(XBDriver.getAButtonPressed()) shooterState++;
    if(shooterState == 1){
      SH.velocityShooter(1);
    }else if(shooterState >= 1){
      SH.velocityShooter(0);
      shooterState = 0;
    }
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
