package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Solenoid;


public class Intake{
    
    VictorSPX intakeMotor = new VictorSPX(7);

    Solenoid intakeInOut = new Solenoid(0, 3);


    boolean intakeToggle = false;

    public void toggleIntake(){
        intakeToggle = !intakeToggle;
        intakeInOut.set(intakeToggle);
    }

    public void setPower(double power){
        intakeMotor.set(ControlMode.PercentOutput,power);
    }

}