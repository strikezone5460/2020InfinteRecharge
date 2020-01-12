/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SPI.Port;

// import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// import com.ctre.phoenix.motorcontrol.can.VictorSPX;

/**
 * Add your docs here.
 */
public class Drivetrain extends RobotMap{

    Solenoid shiftHigh = new Solenoid(0);
    Solenoid shiftLow = new Solenoid(1);

    ADXRS450_Gyro gyro = new ADXRS450_Gyro(Port.kOnboardCS0);//might change

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
            return ((in*1.25)- .25)*.35;
        }
    }
   





}
