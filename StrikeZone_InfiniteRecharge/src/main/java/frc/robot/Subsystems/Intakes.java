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

    Solenoid intakeOut = new Solenoid(0, 3);
      //Solenoid intakeIn = new Solenoid(4);

    private boolean isIntakeIn = false;
    private boolean toggle = false;

/**
 * Puts the intakes out
 */
    public void intakesExtend(){
          //intakeIn.set(false);
        intakeOut.set(true);
    }
/**
 * Puts the intakes in
 */
    public void intakesRetract(){
        intakeOut.set(false);
        //intakeIn.set(true);
    }
/**
 * A toggle for the intake
 * @param input instantaneous boolean
 * @return the current state of the intakes (true = extended)
 */
    public boolean intakesIO(boolean input){
        //intakes in and out (toggle)
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
    /**
     * intakes balls from the field only if intakes are extended
     * @param bypass By passes the intake out check
     */
    public void intakeOn(boolean bypass){
        if(!isIntakeIn || bypass){
            intake.set(ControlMode.PercentOutput, 1);
        }else{
            intake.set(ControlMode.PercentOutput, 0);
        }
    }
    /**
     * runs intakes backwards to clear jams and balls from robot
     */
    public void intakeOut(){
        if(!isIntakeIn){
            intake.set(ControlMode.PercentOutput, -.5);
        }else{
            intake.set(ControlMode.PercentOutput, 0);
        }  
    }
    /**
     * turns off intakes
     */
    public void intakeOff(){
        if(!isIntakeIn){
            intake.set(ControlMode.PercentOutput, 0);
        }
    }


}
