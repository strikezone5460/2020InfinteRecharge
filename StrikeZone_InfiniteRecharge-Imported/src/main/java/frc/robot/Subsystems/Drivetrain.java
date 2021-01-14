package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SPI.Port;


public class Drivetrain{
////DEVICES
    TalonFX leftDriveMaster = new TalonFX(1);
    TalonFX leftDriveFollower = new TalonFX(2);
    TalonFX rightDriveMaster = new TalonFX(3);
    TalonFX rightDriveFollower = new TalonFX(4);

    Solenoid shiftHigh = new Solenoid(0, 0);
    Solenoid shiftLow = new Solenoid(0, 1);

    ADXRS450_Gyro gyro = new ADXRS450_Gyro(Port.kOnboardCS0);

////VARIABLES
    double gyroLock = -1;

    boolean shiftToggle = false;

////METHODS
    public void init(){
        leftDriveMaster.setSelectedSensorPosition(0);
        rightDriveMaster.setSelectedSensorPosition(0);
        gyro.reset();

        // leftDriveMaster.
        // turret.config_kI(0,0.05);
        // turret.config_kD(0,148);
        // turret.configMaxIntegralAccumulator(0, 100);
    }

    public void shift(){
        shiftToggle = !shiftToggle;
        shiftHigh.set(shiftToggle);
        shiftLow.set(!shiftToggle);
    }
    
    public void standardDrive(double speed, double rotate){
        leftDriveMaster.set(ControlMode.PercentOutput,-speed - rotate);
        rightDriveMaster.set(ControlMode.PercentOutput,speed - rotate);
        leftDriveFollower.follow(leftDriveMaster);
        rightDriveFollower.follow(rightDriveMaster);
    }

    public void targetDrive(double x, double y){
        standardDrive(-y/55.0, x/120.0);
    }

    public double getLeftEncPos(){
        return leftDriveMaster.getSelectedSensorPosition(0);
    }

    public double getLeftEncVel(){
        return leftDriveMaster.getSelectedSensorVelocity();
    }

    public double getRightEncPos(){
        return rightDriveMaster.getSelectedSensorPosition();
    }

    public double getRightEncVel(){
        return rightDriveMaster.getSelectedSensorVelocity();
    }

    public double getGyro(){
        return gyro.getAngle();
    }

////LOGIC
    public boolean lock(){
        if(gyroLock == -1){
            gyroLock = getGyro();
        }
        standardDrive(0, (gyroLock - getGyro()) / 50);
        return Math.abs(gyroLock - getGyro()) < 5;
    }

    public void resetLock(){
        gyroLock = -1;
    }
}