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
public class Auton_2 {    
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
                SH.basicServo(.5);
                SH.velocityShooter(SH.kShooterVel[1]);
                SH.limeLightTurret();
                if(autoCounter == 45){
                    autoState = 1;
                    autoCounter = 0;
                }
                break;
            
        
            default:
                break;
        }
    }
}
