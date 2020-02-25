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
    Drivetrain DT;

    private int climbState = 0;
    private int counter = 0;
    public void Init(Drivetrain dt){
        DT = dt;
    }
    public void robotClimb(boolean noDrive, double speed, double rotate){
        counter++;
    switch (climbState) {
        case 0:
            PTO.set(true);
            DT.arcadeDrive(speed, rotate, true);
            if(counter == 500){
                climbState = 1;
                counter=0;
                break;
            }
        case 1:
            PTO.set(false);
            DT.arcadeDrive(speed, rotate, false);

            if(noDrive){
                climbState = 2;
                counter=0;
                break;
            }
        case 2:
            PTO.set(true);
            DT.arcadeDrive(speed, rotate, true);

        default:
            break;
    }
    }



}
