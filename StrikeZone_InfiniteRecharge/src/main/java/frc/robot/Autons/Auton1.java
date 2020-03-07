/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Autons;

import frc.robot.Subsystems.*;

/**
 * Add your docs here.
 */
public class Auton1 {
     
    Drivetrain DT;
    Shooter SH;
    Hopper HO;
    Intakes IN;
    
    int autoCounter = 0;
    int autoState = 0;

  public void Init(Drivetrain dt, Shooter sh, Hopper ho, Intakes in){
        DT = dt;
        SH = sh;
        HO = ho;
        IN = in;
    }

    public void Periodic(){
        autoCounter++;
        switch (autoState) {
            case 0:
            //intake prep\\
                IN.intakesExtend();
                IN.intakeOn(true);
                if(autoCounter == 10){
                    autoState = 1;
                    autoCounter = 0;
                }
                break;
            case 1:
            //drive to rival's trench\\
                DT.gyroDrive(0.5, 0);
                if(autoCounter == 70){
                    autoState = 2;  
                    autoCounter = 0;
                }
                break;
            case 2: 
                DT.gyroDrive(0.35, 30);
                if(autoCounter == 40){
                    autoState = 3;  
                    autoCounter = 0;
                } 
                break;
            case 3:
            //drive out of trench\\
                DT.gyroDrive(-0.5, 0);
                if(autoCounter == 70){
                    autoState = 4;  
                    autoCounter = 0;
                } 
                break;
            case 4: 
            //prep shooter\\
                DT.gyroDrive(0, 0);
                SH.hoodToggle(1);
                SH.velocityShooter(SH.kShooterVel[1]);
                if(autoCounter == 80){
                    autoState = 5;  
                    autoCounter = 0;
                }
                break;
            case 5:
            //actually shoots\\
                SH.hoodToggle(1);
                SH.velocityShooter(SH.kShooterVel[1]);
                HO.hopperBasic();
                if (autoCounter == 200){
                    autoState = 6;
                    autoCounter = 0;
                }
                break;
            case 6:
            //stop shooting\\
                SH.velocityShooter(0);
                HO.hopperBasicOff();
                break;
            default:
                break;
        }
    }
}