package frc.robot;

import frc.robot.Subsystems.*;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.networktables.*;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

////NOTES
  /*
  Test shooter follower mode
  Test lights
  Set talon pid for shooter vel
  Test hood p loop
  Shooter routine
  */

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();


////DEFINES
  final double DEFAULT_DEADBAND = 0.2;
  final double MAX_VALUE = 1.0;

////Drivetrain
  final boolean ROCKET_DRIVE = true;
  final boolean ARCADE_DRIVE = false;
  final boolean WHEEL_DRIVE = false;

////Intake
  final double INTAKE_POWER = 0.8;

////Indexer
  final double INDEXER_H_POWER = 0.5;
  final double INDEXER_V_POWER = 0.6;

////Shooter
// final double SHOOTER_VEL = 18500;
  final double SHOOTER_VEL = 1;
  final double SHOOTER_Y = 17;
  final double SHOOTER_X = 0;

  final boolean AUTO_TURRET = true;
  final boolean AUTO_HOOD = true;


////SUBSYSTEMS
  Drivetrain drivetrain = new Drivetrain();
  Intake intake = new Intake();
  Indexer indexer = new Indexer();
  Shooter shooter = new Shooter();


////HIDs
  XboxController Driver = new XboxController(0);
  XboxController Operator = new XboxController(1);
  XboxController Board = new XboxController(2);


////VARIABLES
  double leftJoystickX, leftJoystickY, rightJoystickX, rightJoystickY;
  double leftTrigger, rightTrigger;

  double speed, rotate;

  double hIndex, vIndex;

  double turretTarget = 0;
  double hoodTarget;
  boolean hoodToggle = false;
  boolean shooterToggle = false;

  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-turret");
  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry ty = table.getEntry("ty");
  NetworkTableEntry th = table.getEntry("thor");
  NetworkTableEntry tv = table.getEntry("tvert");
  NetworkTableEntry ts = table.getEntry("ts");
  NetworkTableEntry tf = table.getEntry("tv");

  double limelightX, limelightY, limelightH, limelightV, limelightS, limelightA;
  boolean limelightF;

////METHODS
  public double applyDeadband(double input,double deadband){
    return(Math.abs(input) > deadband ? (input - (input > 0 ? deadband : -deadband)) * (MAX_VALUE / (MAX_VALUE - deadband)) : 0);
  }
  public double applyDeadband(double input){
    return applyDeadband(input, DEFAULT_DEADBAND);
  }

  public double squareVal(double input){
    return input * input * (input > 0 ? 1 : -1);
  }


  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    drivetrain.init();
    shooter.init();
  }


  @Override
  public void robotPeriodic() {
  }


  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }


  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }


  @Override
  public void teleopInit() {
  }


  @Override
  public void teleopPeriodic() {

////VARIABLE UPDATES
////Controls
    leftTrigger = applyDeadband(Driver.getTriggerAxis(Hand.kLeft),0.01);
    rightTrigger = applyDeadband(Driver.getTriggerAxis(Hand.kRight),0.01);
    leftJoystickX = WHEEL_DRIVE ? Driver.getX(Hand.kLeft) : applyDeadband(Driver.getX(Hand.kLeft));
    leftJoystickY = applyDeadband(Driver.getY(Hand.kLeft));
    rightJoystickX = applyDeadband(Driver.getX(Hand.kRight));
    rightJoystickY = applyDeadband(Driver.getY(Hand.kRight));

////Sensors
    limelightX = tx.getDouble(0.0);
    limelightY = ty.getDouble(0.0);
    limelightH = th.getDouble(0.0);
    limelightV = tv.getDouble(0.0);
    limelightS = ts.getDouble(0.0);
    limelightF = tf.getDouble(0.0) == 1;

    limelightA = limelightS;
    if(limelightH > limelightV){
      if(limelightS < -45) limelightA += 90;
    }
    else if(limelightS > -45) limelightA += 90;

////DRIVETRAIN
    if(ROCKET_DRIVE){
      speed = rightTrigger - leftTrigger;
      rotate = squareVal(leftJoystickX);
    }
    else if(ARCADE_DRIVE){
      speed = rightJoystickY;
      rotate = leftJoystickX;
    }
    else if(WHEEL_DRIVE){
      speed = -(Operator.getY(Hand.kLeft));
      rotate = leftJoystickX * (1.0-(0.7 * Math.abs(speed)));
    }
    else{
      speed = 0;
      rotate = 0;
    }
    drivetrain.standardDrive(speed, rotate);

    if(Driver.getBButtonPressed()){
      drivetrain.shift();
    }


////INDEXER
    if(Driver.getPOV() == 180){
      hIndex = INDEXER_H_POWER;
      vIndex = INDEXER_V_POWER;
    }
    else if(Driver.getPOV() == 0){
      hIndex = -INDEXER_H_POWER;
      vIndex = -INDEXER_V_POWER;
    }
    else{
      vIndex = 0;
      hIndex = 0;
    }

////INTAKES
    if(Driver.getBumperPressed(Hand.kRight)){
      intake.toggleIntake();
    }

    if(Driver.getAButton()){
      intake.setPower(INTAKE_POWER);
      hIndex = -INDEXER_H_POWER;
    }    
    else if(Driver.getBumper(Hand.kLeft)){
      intake.setPower(-INTAKE_POWER);
      hIndex = INDEXER_H_POWER;
    }
    else{
      intake.setPower(0);
    }

    if(!shooterToggle)indexer.autoIndex();

////SHOOTER
    shooter.updateValues(limelightX, limelightY, limelightA, limelightF, drivetrain.getGyro());

    // if(Driver.getXButtonPressed()){
    //   shooterToggle = !shooterToggle;
    //   shooter.setShooterPower(shooterToggle ? SHOOTER_VEL : 0);
    // }

    if(Driver.getXButtonReleased()){
      shooter.resetAutoTarget();
    }
    else if(Driver.getXButton()){
      shooter.turretScan();
    }
    else
    {
      // shooter.autoTurretPos(false, limelightV == 1);
      shooter.setTurretPos(shooter.doTurretMath());
    }

    if(AUTO_HOOD){
      // hoodTarget = Math.abs(limelightY - 21) * 70;

      // if(hoodTarget < 20){
      //   hoodTarget = 20;
      // }
      // else if(hoodTarget > 2100){
      //   hoodTarget = 2100;
      // }
      // shooter.setHoodPos(hoodTarget);
    }


////ETC.
    indexer.indexerHPower(hIndex);
    indexer.indexerVPower(vIndex);
  }


  @Override
  public void disabledInit() {
  }


  @Override
  public void disabledPeriodic() {
    limelightS = ts.getDouble(0.0);
    System.out.println(limelightS);
  }


  @Override
  public void testInit() {
  }


  @Override
  public void testPeriodic() {
  }
}
