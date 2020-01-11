/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

// import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// import com.ctre.phoenix.motorcontrol.can.VictorSPX;

/**
 * Add your docs here.
 */
public class Drivetrain extends RobotMap{
    public void arcadeDrive(double speed, double rotate) {
        leftDriveMaster.set(ControlMode.PercentOutput,speed - rotate);
        leftDriveSlave.follow(leftDriveMaster);
        rightDriveMaster.set(ControlMode.PercentOutput,-speed - rotate);
        rightDriveSlave.follow(rightDriveMaster);
    }
     

    public double Deadband(double in) {
        if(in < .25 && in > -.25){
            return 0.0;
        }else{
            return ((in*1.25)- .25)*.5;
        }
    }
   





}
