/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.Solenoid;   

/**
 * Add your docs here.
 */
public class Hopper extends RobotMap{
    //Solenoid ballStop = new Solenoid(5);

    TalonFX hopperHorizontal = new TalonFX(8);
    VictorSPX hopperVertical = new VictorSPX(9);

    DigitalInput ball0 = new DigitalInput(3);
    DigitalInput ball1 = new DigitalInput(4);
    DigitalInput ball2 = new DigitalInput(5);

    boolean getDebouncedBall0()
    {
      return false;
    }
    boolean getDebouncedBall1()
    {
      return false;
    }
    boolean getDebouncedBall2()
    {
      return false;
    }

    public void hopperLogic(boolean override){  


      if (!override){
        
 //       int theState = (ball0.get()?0:1)+(ball1.get()?0:2)+(ball2.get()?0:4);
        int theState = (getDebouncedBall0()?0:1)+(getDebouncedBall1()?0:2)+(getDebouncedBall2()?0:4);

        



        switch (theState){
        // Might need to apply delays to moto  rs stopping, to accomodate the balls moving from position to position
         case 0:
           //no balls in hopper
           hopperHorizontal.set(ControlMode.PercentOutput, -1);
           hopperVertical.set(ControlMode.PercentOutput, 0);
           break;
         case 1:
           // ball in horizontal position, no where else
           hopperHorizontal.set(ControlMode.PercentOutput, -1);
           hopperVertical.set(ControlMode.PercentOutput, -1);
           break;
         case 2:
           // ball in bottom vertical position, no where else
           // make it at some point that there's a delay before this activates, in case a ball is coming
           hopperHorizontal.set(ControlMode.PercentOutput, -1);
            hopperVertical.set(ControlMode.PercentOutput, 0);
           break;
          case 3:
            // balls in horizontal and bottom vertical positions
            hopperHorizontal.set(ControlMode.PercentOutput, -1);
            hopperVertical.set(ControlMode.PercentOutput, -1);
            break;
          case 4:
            // ball in top vertical position
           hopperHorizontal.set(ControlMode.PercentOutput, 0);
           hopperVertical.set(ControlMode.PercentOutput, 1);
           break;
          case 5:
           // balls in top vertical and horizontal positions
            hopperHorizontal.set(ControlMode.PercentOutput, 0);
            hopperVertical.set(ControlMode.PercentOutput, 1);
            break;
          case 6:
            // balls in top and bottom vertical positions
            hopperHorizontal.set(ControlMode.PercentOutput, -1);
            hopperVertical.set(ControlMode.PercentOutput, 0);
            break;
          case 7:
            // full hopper
            hopperHorizontal.set(ControlMode.PercentOutput, 0);
            hopperVertical.set(ControlMode.PercentOutput, 0);
            break;
         }
       }
    else { 
      hopperHorizontal.set(ControlMode.PercentOutput, -1);
      hopperVertical.set(ControlMode.PercentOutput, -1);
      
    }
    }    
    public void hopperBasic(){
      hopperHorizontal.set(ControlMode.PercentOutput, -.5);
      hopperVertical.set(ControlMode.PercentOutput, -.5);
    }
    public void hopperBasicOff(){
      hopperHorizontal.set(ControlMode.PercentOutput, 0);
      hopperVertical.set(ControlMode.PercentOutput, 0);
    }
    
}
