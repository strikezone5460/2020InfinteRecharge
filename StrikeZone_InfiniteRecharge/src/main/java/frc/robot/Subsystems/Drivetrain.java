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
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
// import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// import com.ctre.phoenix.motorcontrol.can.VictorSPX;

/**
 * Code for the Drivetrian subsystem
 */
public class Drivetrain {
    TalonFX leftDriveM = new TalonFX(1);
    TalonFX leftDriveS = new TalonFX(2);
    TalonFX rightDriveM = new TalonFX(3);
    TalonFX rightDriveS = new TalonFX(4); 

    public void drive(XboxController XB1){
        double scaledSpeed;
        double scaledRotate;
        double speed = XB1.getY(Hand.kLeft);
        double rotate = XB1.getX(Hand.kLeft);

         //DeadBand
         if((speed < 0.2) && (speed > -0.2)){
               speed = 0;
            }
         else {
            if(speed>0){
               speed = (speed-.2)*1.25;
            }

            if(speed<0){
               speed = (speed+.2)*1.25;
            }

            if((rotate < 0.2) && (rotate > -0.2)){
               rotate = 0;
            }
           
            else {
               if(rotate>0){
                  rotate = (rotate-.2)*1.25;
               }
            
               if(rotate<0){
                  rotate = (rotate+.2)*1.25;
               }
            }
         }


         scaledSpeed = 0.5 * speed;
         scaledRotate = 0.5 * rotate;
             
        leftDriveM.set(ControlMode.PercentOutput, - scaledSpeed - scaledRotate);
        leftDriveS.follow(leftDriveM);
        rightDriveM.set(ControlMode.PercentOutput,  scaledSpeed - scaledRotate);
        rightDriveS.follow(rightDriveM);


   }
}
