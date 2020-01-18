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

    boolean isIntakeIn = false;

    public void intakesOut(){
          //intakeIn.set(false);
        intakeOut.set(true);
    }

    public void intakesIn(){
        intakeOut.set(false);
        //intakeIn.set(true);
    }

    public boolean intakesIO(boolean input){
        boolean toggle = false;

        if(input) toggle = !toggle; 
        
        if(toggle == true){
           // intakeIn.set(false);
            intakeOut.set(true);
            isIntakeIn = false;
            return true; 
        }else{
            intakeOut.set(false);
           // intakeIn.set(true);
            isIntakeIn = true;
            return false;  
        }
    }

    public void intakesOn(){
        if(!isIntakeIn){
            intakeMaster.set(ControlMode.PercentOutput, .75);
            intakeSlave.set(ControlMode.PercentOutput, -.75);
        }
    }


}
