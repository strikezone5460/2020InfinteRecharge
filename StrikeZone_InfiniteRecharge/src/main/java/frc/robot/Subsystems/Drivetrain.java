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

    public Solenoid shiftHigh = new Solenoid(0);
    public Solenoid shiftLow = new Solenoid(1);

    ADXRS450_Gyro gyro = new ADXRS450_Gyro(Port.kOnboardCS0);//might change

    public int leftEncPos(){return leftDriveMaster.getSelectedSensorPosition(0);}
    public int rightEncPos(){return rightDriveMaster.getSelectedSensorPosition(0);}
    public int leftEncVel(){return leftDriveMaster.getSelectedSensorVelocity(0);}
    public int rightEncVel(){return rightDriveMaster.getSelectedSensorVelocity(0);}

    double leftDriveCurrent = leftDriveMaster.getStatorCurrent();
    public void Init(){
        leftDriveMaster.configOpenloopRamp(.35);
        rightDriveMaster.configOpenloopRamp(.35);
        leftDriveMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
        rightDriveMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
        leftDriveMaster.setSelectedSensorPosition(0);
        rightDriveMaster.setSelectedSensorPosition(0);
    }

    public void arcadeDrive(double speed, double rotate) {
        leftDriveMaster.set(ControlMode.PercentOutput,speed - rotate);
        leftDriveSlave.follow(leftDriveMaster);
        rightDriveMaster.set(ControlMode.PercentOutput,-speed - rotate);
        rightDriveSlave.follow(rightDriveMaster);
    }


    public void velocityDrive(double speed, double rotate, boolean isHigh){
        if(isHigh){
            leftDriveMaster.set(ControlMode.Velocity, (speed - rotate) * kMaxHighDriveVel);
            leftDriveSlave.follow(leftDriveMaster);
            rightDriveMaster.set(ControlMode.Velocity, (-speed - rotate) * kMaxHighDriveVel);
            rightDriveSlave.follow(rightDriveMaster);
        }else{
            leftDriveMaster.set(ControlMode.Velocity, (speed - rotate) * kMaxLowDriveVel);
            leftDriveSlave.follow(leftDriveMaster);
            rightDriveMaster.set(ControlMode.Velocity, (-speed - rotate) * kMaxLowDriveVel);
            rightDriveSlave.follow(rightDriveMaster);
        }
        

    }

    public double Deadband(double in) {
        if(in > .2 ){
            return ((in*1.2)- .2)*.75;
        }else if(in < -.2){
            return ((in * 1.2)+ .2)*.75;
        }
         else{
            return 0.0;
        }


        
    }
   





}
