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
  XboxController OppBox = new XboxController(2);
  Solenoid led1 = new Solenoid(1,5);
  Solenoid led2 = new Solenoid(1,6);
  Solenoid led3 = new Solenoid(1,7);
  // Solenoid shiftHigh = new Solenoid(0);
  // Solenoid shiftLow = new Solenoid(1);

  Shooter SH = new Shooter();
  Drivetrain DT = new Drivetrain();
  Intakes IN = new Intakes();
  Hopper HO = new Hopper();
  Climber CL = new Climber();
  ColorWheel DJ = new ColorWheel();

  Auton_0 a0 = new Auton_0();
  Auton_1 a1 = new Auton_1();
  Auton_3 a3 = new Auton_3();
  Auton_4 a4 = new Auton_4();

  int pos = 4100;
  
  int shiftState = 0;
  int shooterState = 0;
  int counter = 0;
  int hoodState = 0;
  int shooterCounter =0;
  int shooterSetpoint = 0;
  int hoodCounter = 0;
  int djToggle = 0;

  boolean isClimbing = false;
  boolean isLockdownEnabled = false;

  double angle = 0;
  double angleMod = 0;
  double lockdownAngle = 0;


  int autonIndex = 0;
  int cycle = 0;
  String autonStrings[] = {
    "shoot 3, Move",
    "Steal 2, Shoot 5",
    "Shoot 3, Grab 3, Shoot 3",
    "Shoot 3, Grab 5, Shoot 5",
    "Shoot 3, Grab 5, Shoot 5, Grab 2",
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
    DT.Init();
    SH.shooterInit();
    switch(autonIndex){
      case 0:
        a0.Init(DT, SH, HO, IN);
      break;
      case 1:
        
        break;
      case 3:
        a3.Init(DT, SH, HO, IN);


    }
  }

  @Override
  public void autonomousPeriodic() {

    switch(autonIndex){
      case 0:
        a0.Periodic();
        break;
      case 3:
        a3.Periodic();
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
    // led1.set(true);
    // led2.set(true);
    // led3.set(true);
    double speed = XBDriver.getY(Hand.kLeft);
    double rotate = XBDriver.getX(Hand.kRight);
    angleMod +=  DT.Deadband(rotate) * 7.5;
    angle = DT.gyroVal() + angleMod;
    // isHigh =  DT.shiftHigh.get();
    double yOffset = SH.ty.getDouble(0.0);


     DT.leftEncPos();
     DT.rightEncPos();

    counter++;
    if((counter%8)==0){
      // System.out.println("left: " + (DT.leftEncPos()) + " right: " + (DT.rightEncPos()) + " Gyro " + DT.gyroVal());
      // System.out.println("isHome1: " + SH.isHome1 + " isHome: " + SH.isHome2);
      // System.out.println("shooter Vel: " + SH.shooterVel() + " hoodState " + hoodState);
      //System.out.println("turret pos: " + SH.turretPos);
    //  System.out.println("HoodEncoder: " + SH.hoodPos());
      // System.out.println("Vel Equation: " + SH.shooterEquation(0));
      
    }
    //####################################################################
    //##################   Opperator Controls   ##########################
    //####################################################################

   
    if(XBOpp.getTriggerAxis(Hand.kRight) >= 0.1){
      SH.velocityShooter(20000);
      SH.hoodToggle(1);
    if(shooterCounter++ < 10 && shooterCounter > 0){ //shooter warmup period TODO Adjust
      HO.hopperBasicRev(.25);
    }else if(shooterCounter > 10 && shooterCounter < 50){
      HO.hopperBasicOff();
    }
    }else{
      SH.velocityShooter(0);
    }

    if(XBOpp.getTriggerAxis(Hand.kLeft) >= 0.1){
      HO.hopperLogic(true);
    }else if(XBOpp.getAButton() || XBDriver.getTriggerAxis(Hand.kRight) >= 0.1){
      HO.hopperLogic(false);
    }else if(XBOpp.getBButton()){
      HO.hopperBasicRev(.5);
    }else{
      HO.hopperBasicOff();
    }

    DJ.djToggle(XBOpp.getBumperPressed(Hand.kRight));
    if(XBOpp.getBumperPressed(Hand.kRight))djToggle++;
    if(djToggle >= 1){
      if(XBOpp.getXButton()){
        DJ.djBoothRotation();
      }else if(XBOpp.getYButton()){
        DJ.djBoothExact();
      }
    }
    if(XBOpp.getBumperPressed(Hand.kLeft)){
      hoodState++;
    }
    // SH.basicServo(DT.Deadband(XBOpp.getY(Hand.kLeft)));

    // if(XBOpp.getBumper(Hand.kLeft)){
    //   isClimbing = true;
    //   CL.robotClimb(XBOpp.getBumperPressed(Hand.kRight), DT.Deadband(speed), 0);
    // }


    //####################################################################
    //####################   Driver Controls   ###########################
    //####################################################################
    
    
    if(XBDriver.getTriggerAxis(Hand.kRight) >= .1){
      IN.intakeOn(false);
      // HO.hopperLogic(false);
    }else if(XBDriver.getTriggerAxis(Hand.kLeft) >= .1){
      IN.intakeOut();
    }else{
      IN.intakeOff();
    }
    IN.intakesIO(XBDriver.getBumperPressed(Hand.kRight));
    if(XBDriver.getBButtonPressed()){
      lockdownAngle = DT.gyroVal();
    }
    if(XBDriver.getBButton()){
      DT.gyroDrive(0, lockdownAngle);
    }else if(OppBox.getRawButton(1)){
      CL.robotClimb(XBDriver.getAButtonPressed(), XBDriver.getXButtonPressed(),XBDriver.getYButton(), DT.Deadband(speed), DT.Deadband(rotate));
    }else{
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
      if(SH.hoodPos() < 10){ 
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
