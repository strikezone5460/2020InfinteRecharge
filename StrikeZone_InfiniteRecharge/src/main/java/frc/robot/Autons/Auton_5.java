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
public class Auton_5 {
    Drivetrain DT;
    Hopper HO;
    Shooter SH;
    Intakes IN;
    int autonState = 0;
    int autonCounter = 0;


    public void Init(Drivetrain dt, Hopper ho, Shooter sh, Intakes in){
        DT = dt;
        HO = ho;
        SH = sh;
        IN = in;

                // DT.gyroDrive(speed, angle);
                // HO.hopperLogic();
                // IN.intakesExtend();
                // IN.intakeOn(bypass);
                // SH.hoodToggle(1, 0);
                // SH.velocityShooter(setpoint);
                // SH.limeLightTurret();
                // SH.hoodLogic(closed);
                // DT.leftEncPos();
                // DT.rightEncPos();
                // HO.hopperBasicRev(power);
    }
    public void Periodic(){
        switch (autonState) {
            case 0:
                SH.hoodToggle(1);
                SH.velocityShooter(0.5);
                autonCounter++;
                if(autonCounter >= 100){
                    autonState++;
                    autonCounter = 0;
                }
            break;
            case 1:
                HO.hopperLogic(true);
                SH.velocityShooter(0.5);
                autonCounter++;
                if(autonCounter >= 100){
                    autonState++;
                    autonCounter = 0;
                }
            break;
            case 2:
            HO.hopperLogic(false);
            SH.velocityShooter(0);
            DT.gyroDrive(0.5, 20);
            autonCounter++;
            if(autonCounter >= 50){
                autonState++;
                autonCounter = 0;
            }
            break;
            case 3:
                SH.velocityShooter(0.5);
                DT.gyroDrive(0, 20);
                autonCounter++;
                if(autonCounter >= 50){
                    autonState++;
                    autonCounter = 0;
                }
            break;
            case 4:
                SH.velocityShooter(0.5);
                HO.hopperLogic(true);
                if(autonCounter >= 100){
                    autonState++;
                    autonCounter = 0;
                }
            break;
        
            default:
                DT.gyroPosDrive(0, 0);
                SH.velocityShooter(0);
                HO.hopperLogic(false);
            break;
        }
    }
}
