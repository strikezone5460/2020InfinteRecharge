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
public class Auton_3 {    
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
                //Prep Shooter start driving back
                DT.gyroDrive(.2, 0);
                SH.hoodToggle(1);
                SH.basicServo(.65);
                // SH.hoodLogic(false);
                SH.velocityShooter(19000);
                SH.limeLightTurret();
                if(autoCounter == 45){
                    autoState = 1;
                    autoCounter = 0;
                }
                break;
            case 1:
                //Unload hopper while driving back
                DT.gyroDrive(.2, 0);
                SH.basicServo(.65);
                // SH.hoodLogic(false);
                SH.velocityShooter(19000);
                SH.limeLightTurret();
                HO.hopperLogic(true);
                if(autoCounter >= 60){
                    autoState = 2;
                    autoCounter = 0;
                }
            break;
            case 2:
                //Stop shooter and lower servo and intakes and speed up
                IN.intakesExtend();
                IN.intakeOn(true);
                SH.velocityShooter(0);
                SH.basicServo(0);
                // SH.hoodLogic(true);
                HO.hopperBasicOff();
                DT.gyroDrive(.65, 0);
                if(autoCounter == 40){
                    autoState = 3;
                    autoCounter = 0;
                }
            break;
            case 3:
                //drop hood, drive faster, pick up balls
                SH.hoodToggle(0);
                DT.gyroDrive(1, 0);
                IN.intakeOn(true);
                HO.hopperLogic(false);
                if(DT.leftEncPos() >= 280000){
                    autoState = 103;
                    autoCounter = 0;
                }
            break;
            case 103:
                //slow down a bit before trench 
                DT.gyroDrive(.675, 0);
                IN.intakeOn(true);
                HO.hopperLogic(false);
                if(DT.leftEncPos() >= 312500){
                    autoState = 4;
                    autoCounter = 0;
                }
            case 4:
                //Stop behind trench grab first ball
                DT.gyroDrive(0, 0);
                IN.intakeOn(true);
                HO.hopperLogic(false);
                if(autoCounter == 20){
                    autoState = 104;
                    autoCounter = 0;
                }
            break;
            case 104:
                //turn and drive forward to grab second ball
                DT.gyroDrive(.15, -20);
                IN.intakeOn(true);
                HO.hopperLogic(false);
                if(autoCounter == 50){
                    autoState = 5;
                    autoCounter = 0;  
                }
                break;
                // case 204:
                // DT.gyroDrive(0, 15);
                // IN.intakeOn(true);
                // HO.hopperLogic(false);
                // if(autoCounter == 40){
                //     autoState = 5;
                //     autoCounter = 0;  
                // }
                // break;
            case 5:
                //Drive slowly backwards as not to wheelly
                DT.gyroDrive(-.5, 0);
                IN.intakeOn(true);
                if(DT.leftEncPos() <= 300000){
                    autoState = 105;
                    autoCounter = 0;
                }
            break;
            case 105:
                //Send it
                DT.gyroDrive(-1, 0);
                IN.intakeOn(true);
                if(DT.leftEncPos() <= 200000){
                    autoState = 6;
                    autoCounter = 0;
                }
                break;
            case 6:
                //stop and prep shooter
                IN.intakeOff();
                DT.gyroDrive(0, 0);
                SH.hoodToggle(1);
                SH.limeLightTurret();
                SH.velocityShooter(19000);
                SH.basicServo(1);
                HO.hopperBasicRev(.2);
                // SH.hoodLogic(false);
                if(autoCounter == 40){
                    autoState = 7;
                    autoCounter = 0;
                }
            break;
            case 7:
                //keep prepping shooter?
                SH.limeLightTurret();
                SH.velocityShooter(19000);
                SH.basicServo(.75);
                // SH.hoodLogic(false);
                DT.gyroDrive(0, 0);
                if(autoCounter == 30){
                    autoState = 8;
                    autoCounter = 0;
                }
            break;
            case 8:
                //Shoot
                SH.velocityShooter(19000);
                HO.hopperLogic(true);
                DT.gyroDrive(0, 0);
                if(autoCounter == 150){
                    autoState = 9;
                    autoCounter = 0;
                }
            break;
            default:
                //Turn it all off
                SH.velocityShooter(0);
                HO.hopperBasicOff();
                DT.gyroDrive(0, 0);
                SH.basicServo(0);

                break;
        }
    }
}
