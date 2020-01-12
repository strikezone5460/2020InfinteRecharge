/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

/**
 * Add your docs here.
 */
public class Shooter extends RobotMap{
    public void shooterInit(){

    }
    public void velocityShooter(double setpoint){
        shooterMaster.set(ControlMode.PercentOutput, setpoint);
        shooterSlave.set(ControlMode.PercentOutput, -setpoint);
    }
}
