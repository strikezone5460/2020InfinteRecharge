package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.Solenoid;


public class Shooter{

    TalonFX shooterMaster = new TalonFX(5);
    TalonFX shooterFollower = new TalonFX(6);

    Solenoid hood = new Solenoid(1, 0);
    Solenoid lightOne = new Solenoid(1, 5);
    Solenoid lightTwo = new Solenoid(1, 6);
    Solenoid lightThree = new Solenoid(1, 7);


    boolean hoodToggle = true;


    public void init(){
        hood.set(true);
        lightOne.set(true);
        lightTwo.set(true);
        lightThree.set(true);
    }

    public void toggleHood(){
        hoodToggle = !hoodToggle;
        hood.set(hoodToggle);
    }

    public void setShooterPower(double power){
        shooterMaster.set(ControlMode.PercentOutput,power);
        shooterFollower.set(ControlMode.PercentOutput,-power);
    }

}