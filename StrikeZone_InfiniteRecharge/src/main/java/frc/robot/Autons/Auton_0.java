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
public class Auton_0 {

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
                SH.hoodToggle(1);
                SH.limeLightTurret();
                SH.hoodLogic(false);
                SH.velocityShooter(SH.kShooterVel[1]);
                if(autoCounter == 80){
                    autoState = 1;
                    autoCounter =0;
                }
                break;
            case 1:
                SH.limeLightTurret();
                SH.hoodLogic(false);
                SH.velocityShooter(SH.kShooterVel[1]);
                HO.hopperLogic(true);
                
                if(autoCounter == 60){
                    autoState = 2;
                    autoCounter = 0;
                }
                break;
            case 2: 
                SH.limeLightTurret();
                SH.velocityShooter(0);
                SH.hoodLogic(true);
                HO.hopperBasicOff();
                IN.intakesOut();
                DT.arcadeDrive(.25, 0, false);
                if(autoCounter == 60){
                    autoState = 3;
                    autoCounter = 0;
                }
                break;
            case 3:
                SH.hoodToggle(0);
                DT.arcadeDrive(0, 0, false);
                break;
            default:
                break;
        }
        
    }
  
}
