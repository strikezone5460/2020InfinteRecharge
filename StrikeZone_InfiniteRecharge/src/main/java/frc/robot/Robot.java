/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Autons.*;
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
  XboxController XBOpp = new XboxController(1);
  
  // Solenoid shiftHigh = new Solenoid(0);
  // Solenoid shiftLow = new Solenoid(1);
  Shooter SH = new Shooter();
  Drivetrain DT = new Drivetrain();
  Intakes IN = new Intakes();
  Hopper HO = new Hopper();
  Climber CL = new Climber();
  ColorWheel DJ = new ColorWheel();

  Auton_0 a0 = new Auton_0();

  int pos = 4100;
  
  int shiftState = 0;
  int shooterState = 0;
  int counter = 0;
  int hoodState = 0;
  int shooterCounter =0;
  int shooterSetpoint = 0;
  int hoodCounter = 0;

  boolean isClimbing = false;


  int autonIndex = 0;
  int cycle = 0;
  String autonStrings[] = {
    "Move 5 feet",
    "Shoot 3 then Move 5 feet",
    "Shoot 3, get 2 behind, move 5 feet",
    "Undefined",
    "Undefined",
    "Undefined",
    "Undefined",
    "Undefined",
    "Undefined",
    "Undefined",
    "Undefined",
    "Undefined",
    "Undefined",
    "Undefined",
    "Undefined",
    "Undefined",
    "Undefined"
  };

  @Override
  public void robotInit() {
    DT.Init();
    //DriveTrain Initalization
    SH.shooterInit();
    CL.Init(DT);

  }

  @Override
  public void disabledPeriodic(){
    //System.out.println("left: " + DT.leftEncPos + "right: " + DT.rightEncPos);
    SH.limeLightToggle(XBDriver.getBButtonPressed());
    //Limelight on Shooter

    if ((XBDriver.getBackButtonPressed())&&(autonIndex < 16)){
      autonIndex++;
    }
    else if ((XBDriver.getStartButtonPressed())&&(autonIndex > 0)){
      autonIndex--;
    }
    if ((cycle++ & 0x0F)==0) System.out.println("Auton: "+autonIndex+" - "+autonStrings[autonIndex]);
  }

  @Override
  public void autonomousInit() {
    switch(autonIndex){
      case 0:
        a0.Init(DT, SH, HO, IN);
      break;
      case 1:

    }
  }

  @Override
  public void autonomousPeriodic() {

    switch(autonIndex){
      case 0:
        a0.Periodic();
        break;
    }
  
  }

  @Override
  public void teleopInit() {
    DT.Init();
  
    //Initalization for Shooter and Drivetrain
  }

  @Override
  public void teleopPeriodic() {
    double speed = XBDriver.getY(Hand.kLeft);
    double rotate = XBDriver.getX(Hand.kRight);
    // isHigh =  DT.shiftHigh.get();
    double yOffset = SH.ty.getDouble(0.0);


     DT.leftEncPos();
     DT.rightEncPos();

    counter++;
    if((counter%8)==0){
      System.out.println("left: " + (DT.leftEncPos()) + " right: " + (DT.rightEncPos()) + " Gyro " + DT.gyroVal());
      // System.out.println("isHome1: " + SH.isHome1 + " isHome: " + SH.isHome2);
      // System.out.println("shooter Vel: " + SH.shooterVel() + " hoodState " + hoodState);
      //System.out.println("turret pos: " + SH.turretPos);
    }
    //####################################################################
    //##################   Opperator Controls   ##########################
    //####################################################################

    // SH.limeLightToggle(XBOpp.getTriggerAxis(Hand.kRight)>.25);
    if(XBOpp.getTriggerAxis(Hand.kRight)>.1){
       SH.velocityShooter(SH.shooterSpeed(yOffset));
       SH.hoodToggle(1);
       hoodState = 1;
       if(shooterCounter++ < 10 && shooterCounter > 0){ //shooter warmup period TODO Adjust
        HO.hopperBasicRev(.25);
       }else if(shooterCounter > 10 && shooterCounter < 50){
          HO.hopperBasicOff();
       }else if(shooterCounter >= 60){
        HO.hopperLogic(true);
       }
    }else if(XBOpp.getAButton()){
      HO.hopperLogic(false);
    }else if(XBOpp.getBButton()){
      HO.hopperBasicRev(.5);
    }else if(XBOpp.getYButtonPressed()){
      hoodState++;
    }else{
      SH.percentShooter(0);
      shooterCounter = 0;
      HO.hopperBasicOff();
    }

    // if(XBOpp.getBumper(Hand.kLeft)){
    //   isClimbing = true;
    //   CL.robotClimb(XBOpp.getBumperPressed(Hand.kRight), DT.Deadband(speed), 0);
    // }


    //####################################################################
    //####################   Driver Controls   ###########################
    //####################################################################
    
    if(XBDriver.getTriggerAxis(Hand.kRight) > .1){
      IN.intakesOn(false);
      // HO.hopperLogic(false);
    }else if(XBDriver.getBButton()){
      IN.intakeOut();
    }else{
      IN.intakesOff();
    }
    IN.intakesIO(XBDriver.getBumperPressed(Hand.kRight));
    if(!isClimbing){
    DT.arcadeDrive(DT.Deadband(speed), DT.Deadband(rotate)*.7, false);//TODO Replace with nuke
    }
    // SH.basicServo(XBOpp.getTriggerAxis(Hand.kLeft));

    //Shifter toggle
    if(XBDriver.getBumperPressed(Hand.kLeft)) shiftState++;
    if(shiftState == 1){
      DT.shiftHigh.set(false);
      DT.shiftLow.set(true);
  
    }else if(shiftState >= 1){
      DT.shiftLow.set(false);
      DT.shiftHigh.set(true);
      shiftState = 0;
    }
    if(XBDriver.getYButton()){
      DJ.djUp();
      if(XBDriver.getBButton()){
        DJ.djSpin();
      }else{
        DJ.djSpinOff();
      }
    }else{
      DJ.djDown();
      DJ.djSpinOff();
    }

    //####################################################################
    //####################   Periodic Updates   ##########################
    //####################################################################
    
    if(hoodState == 1){
      SH.hoodLogic(false);
    }else{
      SH.hoodLogic(true);
    }
      SH.hoodToggle(hoodState);
      if(hoodState >= 2)  hoodCounter++;
      if(hoodCounter >= 45){ 
        hoodCounter = 0;
        hoodState = 0;
      }
      SH.limeLightTurret();

  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
