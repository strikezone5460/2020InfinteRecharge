/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * Add your docs here.
 */
public class Intakes extends RobotMap{

    Solenoid intakeOut = new Solenoid(3);
      //Solenoid intakeIn = new Solenoid(4);

    private boolean isIntakeIn = false;
    private boolean toggle = false;


    public void intakesOut(){
          //intakeIn.set(false);
        intakeOut.set(true);
    }

    public void intakesIn(){
        intakeOut.set(false);
        //intakeIn.set(true);
    }

    public boolean intakesIO(boolean input){
        if(input) toggle = !toggle; 
        
        if(toggle == true){
            intakeOut.set(true);
            isIntakeIn = false;
            return true; 
        }else{
            intakeOut.set(false);
            isIntakeIn = true;
            return false;  
        }
    }

    public void intakesOn(){
        if(!isIntakeIn){
            intake.set(ControlMode.PercentOutput, .55);
        }else{
            intake.set(ControlMode.PercentOutput, 0);
        }
    }
    public void intakeOut(){
        if(!isIntakeIn){
            intake.set(ControlMode.PercentOutput, -.5);
        }else{
            intake.set(ControlMode.PercentOutput, 0);
        }  
    }
    public void intakesOff(){
        if(!isIntakeIn){
            intake.set(ControlMode.PercentOutput, 0);
        }
    }


}
