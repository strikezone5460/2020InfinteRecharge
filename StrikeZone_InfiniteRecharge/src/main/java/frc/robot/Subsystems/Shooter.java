package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.CANifier;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PWM;


public class Shooter{

////DEVICES
    TalonFX shooterMaster = new TalonFX(5);
    TalonFX shooterFollower = new TalonFX(6);

    TalonSRX turret = new TalonSRX(11);

    PWM hoodAdjust = new PWM(0);

    Solenoid hood = new Solenoid(1, 0);
    Solenoid lightBlue = new Solenoid(1, 5);
    Solenoid lightGreen = new Solenoid(1, 6);
    Solenoid lightRed = new Solenoid(1, 7);

    CANifier canifier = new CANifier(0);


////METHODS
    public void init(){

        turret.config_kP(0,4);
        turret.config_kI(0,0.05);
        turret.config_kD(0,148);
        turret.configMaxIntegralAccumulator(0, 100);

        canifier.setQuadraturePosition(0,10);

        toggleHood(true);
        setTurretEnc(0);
        setHoodPos(0);
    }

////Shooter
    public void setShooterPower(double power){
        // shooterMaster.set(ControlMode.Velocity,power);
        // shooterFollower.set(ControlMode.Velocity,-power);
        shooterMaster.set(ControlMode.PercentOutput,power);
        shooterFollower.set(ControlMode.PercentOutput,-power);
        // shooterFollower.set(ControlMode.Follower,shooterMaster.getDeviceID());
    }

    public int getShooterVel(){
        return shooterMaster.getSelectedSensorVelocity();
    }

////Turret
    public void setTurretPower(double power){
        turret.set(ControlMode.PercentOutput, power);
    }

    public void setTurretPos(double target){
        turret.set(ControlMode.Position, target);
    }

    public void setTurretEnc(int value){
        turret.setSelectedSensorPosition(value);
    }

    public int getTurretEnc(){
        return turret.getSelectedSensorPosition(0);
    }

////Hood
    public void toggleHood(boolean toggle){
        hood.set(toggle);
    }

    public void setHoodPos(double target){
        hoodAdjust.setSpeed((target - getHoodEnc())/200);
    }

    public void setHoodPower(double speed){
        hoodAdjust.setSpeed(speed);
    }

    public void setHoodEnc(int position){
        canifier.setQuadraturePosition(position, 10);
    }

    public double getHoodEnc(){
        return canifier.getQuadraturePosition();
    }

////Lights
    public void setLight(char light, boolean toggle){
        if(light == 'b') lightBlue.set(toggle);
        if(light == 'g') lightGreen.set(toggle);
        if(light == 'r') lightRed.set(toggle);
    }
}