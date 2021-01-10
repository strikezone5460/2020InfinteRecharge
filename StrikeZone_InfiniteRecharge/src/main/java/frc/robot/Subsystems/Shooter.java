package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.ctre.phoenix.CANifier;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;


import edu.wpi.first.wpilibj.PWM;


public class Shooter{
    ////DEVICES
    TalonFX shooterMaster = new TalonFX(5);
    TalonFX shooterFollower = new TalonFX(6);

    TalonSRX turret = new TalonSRX(11);

    Solenoid hood = new Solenoid(1, 0);
    Solenoid lightOne = new Solenoid(1, 5);
    Solenoid lightTwo = new Solenoid(1, 6);
    Solenoid lightThree = new Solenoid(1, 7);

    PWM hoodAdjust = new PWM(0);

    CANifier canifier = new CANifier(0);

    ////VARIABLES
    boolean hoodToggle = true;

    ////METHODS
    public void init(){
        hood.set(true);
        setTurretEnc(0);
        setHoodPos(0);
        turret.config_kP(0,4);
        turret.config_kI(0,0.05);
        turret.config_kD(0,148);
        turret.configMaxIntegralAccumulator(0, 100);
        canifier.setQuadraturePosition(0,10);
    }

    public void toggleHood(){
        hoodToggle = !hoodToggle;
        hood.set(hoodToggle);
    }

    public void setShooterPower(double power){
        shooterMaster.set(ControlMode.Velocity,power);
        shooterFollower.set(ControlMode.Velocity,-power);
    }

    public void getShooterVel(){
        System.out.println(shooterMaster.getSelectedSensorVelocity());
        System.out.println(shooterFollower.getSelectedSensorVelocity());
    }

    public void setTurretPower(double power){
        turret.set(ControlMode.PercentOutput, power);
    }

    public void moveTurretPos(double target){
        // if(getTurretPos() > 1000 || target > 1000){
        //     turret.set(ControlMode.Position, 1000);
        // }
        // else if(getTurretPos() < -1000 || target < -1000){
        //     turret.set(ControlMode.Position, -1000);
        // }
        // else if(getTurretPos() < 1000 && getTurretPos() > -1000){
        //     turret.set(ControlMode.Position, target);
        // }
        turret.set(ControlMode.Position, target);
    }

    public void moveTurretVision(double error){
        if((getTurretPos() < 1000 && error > 0) || (getTurretPos() > -1000 && error < 0)){
            turret.set(ControlMode.PercentOutput, error/40);
        }
    }
    
    public int getTurretPos(){
        return turret.getSelectedSensorPosition(0);
    }

    public void setTurretEnc(int pos){
        turret.setSelectedSensorPosition(pos);
    }

    public void moveHoodPos(double target){
        hoodAdjust.setSpeed((target - getHoodPos())/50);
        // System.out.print("HOOD TARGET:");
        // System.out.println((target - getHoodPos())/50);
    }

    public void setHoodSpeed(double speed){
        hoodAdjust.setSpeed(speed);
    }

    public double getHoodPos(){
        // return hoodAdjust.getAngle();
        return canifier.getQuadraturePosition();
    }

    public void setHoodPos(int position){
        canifier.setQuadraturePosition(position, 10);
    }

    public void setLight(int light, boolean onOff){
        if(light == 0) lightOne.set(onOff);
        if(light == 1) lightTwo.set(onOff);
        if(light == 2) lightThree.set(onOff);
    }
}