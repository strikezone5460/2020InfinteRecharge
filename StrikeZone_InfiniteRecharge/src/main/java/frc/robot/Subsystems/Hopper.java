/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Add your docs here.
 */
public class Hopper extends RobotMap{
    Solenoid ballStop = new Solenoid(5);

    AnalogInput ballPos1 = new AnalogInput(0);
    AnalogInput ballPos2 = new AnalogInput(1);
    AnalogInput ballPos3 = new AnalogInput(2);
    AnalogInput ballPos4 = new AnalogInput(3);



}
