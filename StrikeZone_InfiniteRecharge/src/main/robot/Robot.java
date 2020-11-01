package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Subsystems.*;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;


public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-turret");
  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry ty = table.getEntry("ty");


  ////DEFINES
  final double DEFAULT_DEADBAND = 0.2;
  final double MAX_VALUE = 1.0;

  ////Drivetrain
  final boolean ROCKET_DRIVE = true;
  final boolean ARCADE_DRIVE = false;

  ////Intake
  final double INTAKE_POWER = 1.0;

  ////Indexer
  final double INDEXER_H_POWER = 0.5;
  final double INDEXER_V_POWER = 0.6;

  ////Shooter
  final double SHOOTER_POWER = 0.57;
  final double SHOOTER_Y = 17;
  final double SHOOTER_X = 0;


  ////METHODS
  public double applyDeadband(double input,double deadband){
    return(Math.abs(input) > deadband ? (input - (input > 0 ? deadband : -deadband)) * (MAX_VALUE / (MAX_VALUE - deadband)) : 0);
  }
  public double applyDeadband(double input){
    return applyDeadband(input, DEFAULT_DEADBAND);
  }


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
  double limelightX, limelightY;

  boolean shooterToggle = false;


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
    leftTrigger = applyDeadband(Driver.getTriggerAxis(Hand.kLeft),0.01);
    rightTrigger = applyDeadband(Driver.getTriggerAxis(Hand.kRight),0.01);
    leftJoystickX = applyDeadband(Driver.getX(Hand.kLeft));
    leftJoystickY = applyDeadband(Driver.getY(Hand.kLeft));
    rightJoystickX = applyDeadband(Driver.getX(Hand.kRight));
    rightJoystickY = applyDeadband(Driver.getY(Hand.kRight));

    //read values periodically
    limelightX = tx.getDouble(0.0);
    limelightY = ty.getDouble(0.0);


    ////Drivetrain
    if(ROCKET_DRIVE){
      speed = rightTrigger - leftTrigger;
      rotate = (speed >= 0) ? leftJoystickX : -leftJoystickX;
    }
    else if(ARCADE_DRIVE){
      speed = rightJoystickY;
      rotate = leftJoystickX;
    }
    else{
      speed = 0;
      rotate = 0;
    }

    if(Driver.getBButtonPressed()){
      drivetrain.shift();
    }

    if(Driver.getYButton()){
      drivetrain.targetDrive(limelightX - SHOOTER_X, limelightY - SHOOTER_Y);
    }
    else{
      drivetrain.standardDrive(speed, rotate);
    }

    //Indexer
    if(Driver.getPOV() == 180){
      indexer.indexerHPower(INDEXER_H_POWER);
      indexer.indexerVPower(INDEXER_V_POWER);
    }
    else if(Driver.getPOV() == 0){
      indexer.indexerHPower(-INDEXER_H_POWER);
      indexer.indexerVPower(-INDEXER_V_POWER);
    }
    else{
      indexer.indexerHPower(0);
      indexer.indexerVPower(0);
    }

    // indexer.indexerVPower(indexer.isABlocked() ? 0 : -INDEXER_V_POWER);

    ////Intakes
    if(Driver.getBumperPressed(Hand.kRight)){
      intake.toggleIntake();
    }

    if(Driver.getAButton()){
      intake.setPower(INTAKE_POWER);
      indexer.indexerHPower(-INDEXER_H_POWER);
    }    
    else if(Driver.getBumper(Hand.kLeft)){
      intake.setPower(-INTAKE_POWER);
      indexer.indexerHPower(INDEXER_H_POWER);
    }
    // else if(!indexer.isABlocked()){
    //   indexer.indexerHPower(-INDEXER_H_POWER);
    // }
    else{
      intake.setPower(0);
      indexer.indexerHPower(0);
    }

    ////Shooter
    if(Driver.getXButtonPressed()){
      shooterToggle = !shooterToggle;
      shooter.setShooterPower(shooterToggle ? SHOOTER_POWER : 0);
    }
  }


  @Override
  public void disabledInit() {
  }


  @Override
  public void disabledPeriodic() {
  }


  @Override
  public void testInit() {
  }


  @Override
  public void testPeriodic() {
  }
}
