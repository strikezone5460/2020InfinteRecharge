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
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Code For the Shooter Subsytem
 */
public class Shooter extends RobotMap{

    
    public Solenoid hoodMain = new Solenoid(6);
    public Solenoid hoodSub = new Solenoid(7);//TODO Figure out what number this actually is
    //TODO Add a servo for hood
    

    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-midDistance");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");


    public void shooterInit(){
        shooterMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
        shooterSlave.setInverted(TalonFXInvertType.OpposeMaster);
        shooterMaster.configClosedloopRamp(.25, 0);
        shooterMaster.configClosedLoopPeakOutput(0, .95);
        //shooterMaster.configAllowableClosedloopError(0, allowableCloseLoopError, timeoutMs)
    }
    public void percentShooter(double setpoint){
        shooterMaster.set(ControlMode.PercentOutput, -setpoint);
        shooterSlave.follow(shooterMaster);
    }
    int shooterVel(){return shooterMaster.getSelectedSensorVelocity(0);}

    public void velocityShooter(double setpoint){
        shooterVel();
        if(shooterMaster.getClosedLoopError(0)>0){
        shooterMaster.set(ControlMode.Velocity, setpoint);
        }
        shooterSlave.follow(shooterMaster);


    }

    
}
