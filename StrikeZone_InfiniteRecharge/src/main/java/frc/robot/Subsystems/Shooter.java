/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Code For the Shooter Subsytem
 */
public class Shooter extends RobotMap{

    
    public Solenoid hoodMain = new Solenoid(6);
    public Solenoid hoodSub = new Solenoid(7);
    
    Servo hoodAdjust = new Servo(0);
    
    DigitalInput homeLeft = new DigitalInput(0);
    DigitalInput homeRight = new DigitalInput(1);


    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-turret");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    NetworkTableEntry tv = table.getEntry("tv");
    NetworkTableEntry ledMode = table.getEntry("ledMode");
    NetworkTableEntry camMode = table.getEntry("camMode");
    

    public boolean isHome1 = !homeLeft.get();
    public boolean isHome2 = !homeRight.get();

    boolean LLtoggle = true;
    double setpoint = 4100;

    int turretPos = turretRotation.getSelectedSensorPosition(0);
    //NetworkTableInstance.getDefault().getTable("limelight-turret").getEntry("tv")
    double isTargeting = tv.getDouble(0);
    double xOffset = tx.getDouble(0.0);

    public void limeLightToggle(boolean input){
        if(input) LLtoggle = !LLtoggle;
        if(LLtoggle){
            ledMode.setNumber(0);
            camMode.setNumber(0);
        }else{
            ledMode.setNumber(1);
            camMode.setNumber(1);
        }
    }


    public void shooterInit(){
        shooterMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
        shooterSlave.setInverted(TalonFXInvertType.OpposeMaster);
        shooterMaster.configClosedloopRamp(.25, 0);
        shooterMaster.configClosedLoopPeakOutput(0, .95);
        turretRotation.setSelectedSensorPosition(4100);
        //shooterMaster.configAllowableClosedloopError(0, allowableCloseLoopError, timeoutMs)
    }
    public void percentShooter(double setpoint){
        shooterMaster.set(ControlMode.PercentOutput, -setpoint);
        shooterSlave.follow(shooterMaster);
    }
    public int shooterVel(){return shooterMaster.getSelectedSensorVelocity(0);}

    public void basicServo(double input){
        double pos = (input +1)/2;
        hoodAdjust.setPosition(pos);
    }

    public void velocityShooter(double setpoint){
        shooterVel();
        if(shooterMaster.getClosedLoopError(0)>0){
        shooterMaster.set(ControlMode.Velocity, setpoint);
        }
        shooterSlave.follow(shooterMaster);
    }
    public void turretLogic(double input){
        turretPos = turretRotation.getSelectedSensorPosition(0);
        isHome1 = !homeLeft.get();
        isHome2 = !homeRight.get();
        isTargeting = tv.getDouble(0.0);
        xOffset = tx.getDouble(0.0);

        if(input > 10000) input = input -10000;
        if(input < -1800) input = input +10000;

        double setpoint = input;

        if(setpoint > 8200)setpoint = 8200;
        if(setpoint < 0) setpoint = 0;

        double error = setpoint - turretPos;
        double kp = -.001;
        double output  =  error * kp;
 
        if(output >= .75) output = .75;
        else if(output <= -.75) output = -.75;
        turretRotation.set(ControlMode.PercentOutput, output);
        //if(isTargeting == 0){
            // // if(isHome1 && isHome2){
            // //     turretRotation.set(ControlMode.PercentOutput,0);
            // //     // if(turretPos < 0){
            // //     //     turretRotation.set(ControlMode.PercentOutput, -1);
            // //     // }else{
            // //     //     turretRotation.set(ControlMode.PercentOutput, 1);
            // //     // }
            // // }else{
            // //     // turretRotation.setSelectedSensorPosition(0);
            // }
        //}
    }
    public void limeLightShooter(){
        isTargeting = tv.getDouble(0.0);
        xOffset = tx.getDouble(0.0);

        turretPos = turretRotation.getSelectedSensorPosition(0);

        double kp = 0.0225;

        double error = xOffset;
        double output = kp * error;

        if(output >= .75) output = .75;
        else if(output <= -.75) output = -.75;

        if(isTargeting == 1){
            turretRotation.set(ControlMode.PercentOutput, output);
        }else{
            // turretRotation.set(ControlMode.PercentOutput,0);
            turretLogic(4100);
        }
        if(turretPos > 8100){
            turretLogic(0);
        }else if(turretPos < 0){
            turretLogic(8100);
        }
    }


    public void shortShotHood(){
        hoodMain.set(true);
        hoodSub.set(true);
    }
    
    public void longShotHood(){
        hoodMain.set(true);
        hoodSub.set(false);
    }

    public void closedHood(){
        hoodMain.set(false);
        hoodSub.set(false);
    }

    
}
