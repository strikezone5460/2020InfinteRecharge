/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * Add your docs here.
 */
public class Climber extends RobotMap{
    Solenoid PTO = new Solenoid(0, 2);
    Solenoid armsRelease = new Solenoid(0, 5);
    
    Drivetrain DT;

    private int climbState = 0;
    private int counter = 0;
    boolean extended = false;
    public void Init(Drivetrain dt){
        DT = dt;
    }
    public void robotClimb(boolean noDrive,boolean armsUp,boolean goBack, double speed, double rotate){
        counter++;
    switch (climbState) {
        case 0:
            PTO.set(true);
            //DT.arcadeDrive(speed, rotate, true);
            if(counter == 25){
                climbState = 1;
                counter=0;
            }
                break;
        case 1:
            PTO.set(false);
            DT.arcadeDrive(DT.Deadband(speed), DT.Deadband(rotate)*.7, false);
            if(armsUp){
                extended = true;
            }
            armsRelease.set(extended);
            if(noDrive){
                climbState = 2;
                counter=0;
            }
                break;
        case 2:
            PTO.set(true);
            if(goBack){
                PTO.set(false);
                DT.arcadeDrive(DT.Deadband(speed), DT.Deadband(rotate)*.7, false);
                if(armsUp){
                    extended = true;
                }
                armsRelease.set(extended);
            }else{
            if(armsUp){
                extended = true;
            }
            armsRelease.set(extended);
            DT.arcadeDrive(Math.abs(speed), 0, true);
        }
        default:
            break;
    }
    }

    public void foldUp(boolean toggle){
        int thingy;

    }



}
