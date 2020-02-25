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
public class EightBall{
    // private Drivetrain DT = Drivetrain.getInstance();
    private Shooter SH = new Shooter();
    private Intakes IN = new Intakes();
    private Hopper HO = new Hopper();

    public int autoState = 0;
    int count = 0;
    
    public void eightBall(boolean side){
        
    }

}
