package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Subsystems.*;

// import org.graalvm.compiler.core.common.type.ArithmeticOpTable.UnaryOp.Abs;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;


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
  final double SHOOTER_VOLTAGE = 16000;
  final double SHOOTER_Y = 17;
  final double SHOOTER_X = 0;


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
  double limelightX, limelightY, limelightD;
  double hIndex, vIndex;
  double turretTarget = 0;

  boolean shooterToggle = false;

  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-turret");
  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry ty = table.getEntry("ty");
  NetworkTableEntry td = table.getEntry("td");


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

    ////CONTROLS
    leftTrigger = applyDeadband(Driver.getTriggerAxis(Hand.kLeft),0.01);
    rightTrigger = applyDeadband(Driver.getTriggerAxis(Hand.kRight),0.01);
    leftJoystickX = WHEEL_DRIVE ? Driver.getX(Hand.kLeft) : applyDeadband(Driver.getX(Hand.kLeft));
    leftJoystickY = applyDeadband(Driver.getY(Hand.kLeft));
    rightJoystickX = applyDeadband(Driver.getX(Hand.kRight));
    rightJoystickY = applyDeadband(Driver.getY(Hand.kRight));

    ////SENSORS
    limelightX = tx.getDouble(0.0);
    limelightY = ty.getDouble(0.0);
    limelightD = td.getDouble(0.0);

    ////DRIVETRAIN
    if(ROCKET_DRIVE){
      speed = rightTrigger - leftTrigger;
      rotate = squareVal((speed >= 0) ? leftJoystickX : -leftJoystickX);
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

    if(Driver.getBButtonPressed()){
      drivetrain.shift();
    }

    if(Driver.getYButton()){
      drivetrain.targetDrive(limelightX - SHOOTER_X, limelightY - SHOOTER_Y);
    }
    else{
      drivetrain.standardDrive(speed, rotate);
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
    if(Driver.getXButtonPressed()){
      shooterToggle = !shooterToggle;
      shooter.setShooterPower(shooterToggle ? SHOOTER_VOLTAGE : 0);
    }

    if(true){

      if((turretTarget > -1000 && limelightX > 0) || (turretTarget < 1000 && limelightX < 0)){
        turretTarget = shooter.getTurretPos() + (-limelightX*25);
      }

      if(turretTarget < -1000){
        turretTarget = -1000;
      }
      else if(turretTarget > 1000){
        turretTarget = 1000;
      }

      

      // shooter.setTurretPower(10.0);

      // System.out.println(turretTarget);
      shooter.moveTurretPos(turretTarget);
      // shooter.moveTurretPos(1000);
      // shooter.moveTurretVision(-limelightX);
    }
    // else if(Driver.getBackButton()){
    //   shooter.moveTurretPos(-1000);
    // }
    // else{
    //   shooter.setTurretPower(0);
    // }

    if(true){
      // shooter.moveHoodPos(2150);
      // System.out.println("MOVING");

      shooter.moveHoodPos(Math.abs(limelightY - 15) * 67);

      if(shooter.getHoodPos() < 100){
        shooter.moveHoodPos(100);
      }
      else if(shooter.getHoodPos() > 2100){
        shooter.moveHoodPos(2100);
      }
    }
    // else{
    //   shooter.setHoodSpeed(0.0);
    // }

    // if(shooterToggle && false){
    //   shooter.moveTurretVision(limelightX);
    //   shooter.setTurretPower(limelightD);
    //   shooter.moveHoodPos(limelightY);
    //   // if(limelightX == 0 && hoodAdjusted && shooterVelGood){
    //     //set goodToGo = true;
    //   // }

    //   // if(timeout || indexEmpty || joysticksMoved){
    //     //set goodToGo = false;
    //   // }
    //   // if(goodToGo){
    //     // index routine
    //   // }
    // }

    ////ETC.
    indexer.indexerHPower(hIndex);
    indexer.indexerVPower(vIndex);
    // System.out.print("HOOD POS:");
    // System.out.println(shooter.getHoodPos());
    shooter.getShooterVel();
  }


  @Override
  public void disabledInit() {
  }


  @Override
  public void disabledPeriodic() {
    System.out.println(shooter.getHoodPos());
  }


  @Override
  public void testInit() {
  }


  @Override
  public void testPeriodic() {
  }
}
