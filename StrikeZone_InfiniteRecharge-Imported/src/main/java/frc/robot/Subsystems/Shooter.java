package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.CANifier;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PWM;


public class Shooter{

////DEFINES
    final int MAX_TURRET = 1000;
    final int MIN_TURRET = -1000;

    final int MAX_HOOD = 2100;
    final int MIN_HOOD = 20;

    final int SHOOTER_GOAL = 200;
    final int TURRET_GOAL = 10;
    final int HOOD_GOAL = 10;

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


////VARIABLES
    double shooterTarget = 0;
    double turretTarget = 0;
    double hoodTarget = 0;

    double limelightX, limelightY, limelightD;


    boolean readyForFeed;
    boolean lockdown = false;


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

    public boolean setShooterVel(double vel){
        shooterMaster.set(ControlMode.Velocity,vel);
        shooterFollower.set(ControlMode.Velocity,-vel);
        // shooterFollower.set(ControlMode.Follower,shooterMaster.getDeviceID());
        return (Math.abs(getShooterVel() - vel) < SHOOTER_GOAL);
    }

    public double getShooterVel(){
        return shooterMaster.getSelectedSensorVelocity();
    }

////Turret
    public void setTurretPower(double power){
        turret.set(ControlMode.PercentOutput, power);
    }

    public boolean setTurretPos(double target){
        turret.set(ControlMode.Position, target);
        return (Math.abs(getTurretEnc() - target) < TURRET_GOAL);
    }

    public void setTurretEnc(int value){
        turret.setSelectedSensorPosition(value);
    }

    public double getTurretEnc(){
        return turret.getSelectedSensorPosition(0);
    }

////Hood
    public void toggleHood(boolean toggle){
        hood.set(toggle);
    }

    public boolean setHoodPos(double target){
        hoodAdjust.setSpeed((target - getHoodEnc())/200);
        return (Math.abs(getHoodEnc() - target) < HOOD_GOAL);
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

////Logic
    public void updateLLValues(double llX, double llY, double llD){
        limelightX = llX;
        limelightY = llY;
        limelightD = llD;
    }

    public void keepShooterIdle(){

    }

    public boolean autoShooterVel(){
        return true;
    }

    public boolean autoTurretPos(double xOffset){
        if((turretTarget > MIN_TURRET && xOffset > 0) || (turretTarget < MAX_TURRET && xOffset < 0)){
            turretTarget = getTurretEnc() + (-xOffset*25);
        }
    
        if(turretTarget < MIN_TURRET){
            turretTarget = MIN_TURRET;
        }
        else if(turretTarget > MAX_TURRET){
            turretTarget = MAX_TURRET;
        }
        return setTurretPos(turretTarget);
    }

    public boolean autoHoodPos(double offsetY){
        hoodTarget = Math.abs(offsetY - 21) * 70;

        if(hoodTarget < MIN_HOOD){
            hoodTarget = MIN_HOOD;
        }
        else if(hoodTarget > MAX_HOOD){
            hoodTarget = MAX_HOOD;
        }
        return setHoodPos(hoodTarget);
    }

    public boolean autoTarget(){
        if(lockdown) return (autoShooterVel() && setTurretPos(turretTarget) && setHoodPos(hoodTarget));
        else{
            return (autoTurretPos(limelightX) && autoHoodPos(limelightY));
        }
    }

    public boolean shooterLockdown(){
        return (setShooterVel(shooterTarget) && setTurretPos(turretTarget) && setHoodPos(hoodTarget));
    }

    //Auto shoot routine
    //Calc and execute shooter vel, hood angle and turret pos
    //Stop chassis and detect stop
    //Lockdown hood, vel, and turret and detect lock
    //Feed routine
        //Detect lockdown
        //Feed up
        //Need to detect ball shot
        //Try different hopper feed routines
            //Shake intake
            //Adjust speeds
        //If not in A,B or C, go into "shake" mode until shoot mode cancelled or 5 shots detected
    public boolean autoShoot(boolean brake){
        readyForFeed = false;
        if(brake && autoTarget()){
            lockdown = true;
            readyForFeed = true;
        }
        return readyForFeed;
    }
}