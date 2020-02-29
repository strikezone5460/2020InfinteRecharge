/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SPI.Port;

// import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// import com.ctre.phoenix.motorcontrol.can.VictorSPX;

/**
 * Code for the Drivetrian subsystem
 */
public class Drivetrain extends RobotMap{

    public Solenoid shiftHigh = new Solenoid(0, 0);
    public Solenoid shiftLow = new Solenoid(0, 1);

    ADXRS450_Gyro gyro = new ADXRS450_Gyro(Port.kOnboardCS0);//might change

    public int leftEncPos(){return leftDriveMaster.getSelectedSensorPosition(0);}
    public int rightEncPos(){return rightDriveMaster.getSelectedSensorPosition(0);}
    public int leftEncVel(){return leftDriveMaster.getSelectedSensorVelocity(0);}
    public int rightEncVel(){return rightDriveMaster.getSelectedSensorVelocity(0);}

    double leftDriveCurrent = leftDriveMaster.getStatorCurrent();

    /**
     * Initialization function for Drivetrain
     */
    public void Init(){
        leftDriveMaster.configOpenloopRamp(.35);
        rightDriveMaster.configOpenloopRamp(.35);
        leftDriveMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
        rightDriveMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
        leftDriveMaster.setSelectedSensorPosition(0);
        rightDriveMaster.setSelectedSensorPosition(0);
        leftDriveMaster.configClosedloopRamp(.25);
        rightDriveMaster.configClosedloopRamp(.25);
    }
    /**
     * Standard Drive function for teleop
     * @param speed Percent of total speed (0-1)
     * @param rotate Rotatates robot at a constant speed (0-1)
     */
    public void arcadeDrive(double speed, double rotate, boolean isClimbing) {
        if(!isClimbing){
        leftDriveMaster.set(ControlMode.PercentOutput,speed - rotate);
        leftDriveSlave.follow(leftDriveMaster);
        rightDriveMaster.set(ControlMode.PercentOutput,-speed - rotate);
        rightDriveSlave.follow(rightDriveMaster);
        }else{
            leftDriveMaster.set(ControlMode.PercentOutput,speed - rotate);
            leftDriveSlave.follow(leftDriveMaster); 
        }
        
    }

    public double gyroVal(){return gyro.getAngle();}
    /**
     * Uses Velocity mode PID in talons to drive
     * @param speed Percent of Maximum velocity (0-1)
     * @param rotate Rotates robot (0-1)
     * @param isHigh The shifter state effects the maximum Velocity
     */
    public void velocityDrive(double speed, double rotate, boolean isHigh){
            leftDriveMaster.set(ControlMode.Velocity, (speed - rotate) * kMaxHighDriveVel);
            leftDriveSlave.follow(leftDriveMaster);
            rightDriveMaster.set(ControlMode.Velocity, (-speed - rotate) * kMaxHighDriveVel);
            rightDriveSlave.follow(rightDriveMaster);
    }

    /**
     * Scaling deadband to avoid drift
     * @param in any Joystick value 
     * @return Smooth standard out put with a deadband at +-2
     */
    public double Deadband(double in) {
        if(in > .2 ){
            return ((in-.2)* 1.25) * .9;
        }else if(in < -.2){
            return ((in + .2)* 1.25) * .9;
        }else{
            return 0.0;
        }
    }

    /**
     * Keeps the robot driving straight at a given angle
     * @param speed Speed of robot (0-1)
     * @param angle Angle that the robot will travel
     */
   public void gyroDrive(double speed, double angle){
        double kp = 0.001;

        double input = gyro.getAngle();
        double error = angle - input;
        double output = error * kp;

        arcadeDrive(speed, output, false);
   }

   /**
    * Tank drive to a given sepoint for each
    * side of the drivetrain
    * @param leftSetpoint Left Setpoint
    * @param rightSetpoint  Right Setpoint
    */
   public void posDrive(double leftSetpoint, double rightSetpoint){
       leftDriveMaster.set(ControlMode.Position, leftSetpoint);
       leftDriveSlave.follow(leftDriveMaster);
       rightDriveMaster.set(ControlMode.Position, rightSetpoint);
       rightDriveSlave.follow(rightDriveMaster);

   }

   public void gyroPosDrive(double pos, double angle){
       
       leftDriveMaster.set(ControlMode.Position, pos);
       leftDriveSlave.follow(leftDriveMaster);
       rightDriveMaster.set(ControlMode.Position, pos);
       rightDriveSlave.follow(rightDriveMaster);
   }






}
