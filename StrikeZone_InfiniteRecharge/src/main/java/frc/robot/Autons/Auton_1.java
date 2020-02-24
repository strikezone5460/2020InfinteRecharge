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
public class Auton_1 {
    
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
                DT.gyroDrive(1, 0);
                IN.intakesOut();
                if(autoCounter == 20){
                    autoCounter = 0;
                    autoState = 1;
                }     
                break;
            case 1:
                DT.gyroDrive(.85, 0);
                IN.intakesOn(true);
                if(autoCounter == 20){
                    autoCounter = 0;
                    autoState = 2;
                }
                break;
            case 2:
                DT.gyroDrive(.5, 0);
                IN.intakesOn(true);
                if(autoCounter == 20){
                    autoCounter = 0;
                    autoState = 3;
                }
                break;
            default:
                break;
        }


    }

}
