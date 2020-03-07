/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Add your docs here.
 */
public class Hopper extends RobotMap{
    //Solenoid ballStop = new Solenoid(5);

    DigitalInput PosA = new DigitalInput(3);
    DigitalInput PosB = new DigitalInput(4);
    DigitalInput PosC = new DigitalInput(5);

    int counter = 0;
    int theState = 0;

    public int hopperState = (PosA.get()?0:1)+(PosB.get()?0:2)+(PosC.get()?0:4);
    public void hopperLogic(boolean override){
     
      if(!override){
        if(counter++%3 == 0){
          theState = (PosA.get()?0:1)+(PosB.get()?0:2)+(PosC.get()?0:4);
        }
        switch (theState){
          // Might need to apply delays to motors stopping, to accomodate the balls moving from position to position
          case 0:
          //no balls in hopper
            hopperHorizontal.set(ControlMode.PercentOutput, -.8);
            hopperVertical.set(ControlMode.PercentOutput, 0);
            break;
          case 1:
          // ball in horizontal position, no where else
            hopperHorizontal.set(ControlMode.PercentOutput, -.8);
            hopperVertical.set(ControlMode.PercentOutput, -.8);
            break;
          case 2:
          // ball in bottom vertical position, no where else
          // make it at some point that there's a delay before this activates, in case a ball is coming
            hopperHorizontal.set(ControlMode.PercentOutput, -.8);
            hopperVertical.set(ControlMode.PercentOutput, 0);
            break;
          case 3:
          // balls in horizontal and bottom vertical positions
            hopperHorizontal.set(ControlMode.PercentOutput, -.75);
            hopperVertical.set(ControlMode.PercentOutput, -.75);
            break;
          case 4:
          // ball in top vertical position
            hopperHorizontal.set(ControlMode.PercentOutput, 0);
            hopperVertical.set(ControlMode.PercentOutput, .8 );
            break;
          case 5:
          // balls in top vertical and horizontal positions
            hopperHorizontal.set(ControlMode.PercentOutput, 0);
            hopperVertical.set(ControlMode.PercentOutput, 1);
            break;
          case 6:
          // balls in top and bottom vertical positions
            hopperHorizontal.set(ControlMode.PercentOutput, -.8);
            hopperVertical.set(ControlMode.PercentOutput, 0);
            break;
          case 7:
          // full hopper
            hopperHorizontal.set(ControlMode.PercentOutput, 0);
            hopperVertical.set(ControlMode.PercentOutput, 0);
            break;
      }
    }else{
      hopperHorizontal.set(ControlMode.PercentOutput, -.9);
      hopperVertical.set(ControlMode.PercentOutput, -.9);
    }
    }
    /**
     * Forces hopper to turn on
     */
    public void hopperBasic(){
      hopperHorizontal.set(ControlMode.PercentOutput, -.8);
      hopperVertical.set(ControlMode.PercentOutput, -.8);
    }
    /**
     * Forces hopper to turn off
     */
    public void hopperBasicOff(){
      hopperHorizontal.set(ControlMode.PercentOutput, 0);
      hopperVertical.set(ControlMode.PercentOutput, 0);
    }
    public void hopperBasicRev(double power){
      hopperHorizontal.set(ControlMode.PercentOutput, power);
      hopperVertical.set(ControlMode.PercentOutput, power);
    }
    
    /**
     * Used for turning on the hopper in a controlled manner
     * @param override if True, turn on the hopper without logic
     */
    public void hopperLogicBasic(boolean override){
      //If the logic is overridden, turn on (For shooting)
      if(!override){
        //If there is a ball in the last position, turn off
        if(!PosA.get()){
          //If there is a ball in the first position, turn on
          if(PosC.get()){
            hopperHorizontal.set(ControlMode.PercentOutput, -.8);
            hopperVertical.set(ControlMode.PercentOutput, -.8);
          }else{
            hopperHorizontal.set(ControlMode.PercentOutput, 0);
            hopperVertical.set(ControlMode.PercentOutput, 0);
          }
        }else{
          hopperHorizontal.set(ControlMode.PercentOutput, 0);
          hopperVertical.set(ControlMode.PercentOutput, 0);
        }
      }else{
        hopperHorizontal.set(ControlMode.PercentOutput, -.8);
        hopperVertical.set(ControlMode.PercentOutput, -.8);
      }
    }
    public void hopperVerticalOn(){
      hopperVertical.set(ControlMode.PercentOutput,-.8);
    }
    public void hopperHorizontalOn(){
      hopperHorizontal.set(ControlMode.PercentOutput,-.8);
    }
    
}
