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

    final int INNER_ZONE = 10;
    final int OUTER_ZONE = 60;
    final double INNER_OFFSET = 0.1;
    
    final int TURRET_ENC_TO_DEG = 26;

////DEVICES
    TalonFX shooterMaster = new TalonFX(5);
    TalonFX shooterFollower = new TalonFX(6);

    TalonSRX turret = new TalonSRX(11);

    PWM hoodAdjust = new PWM(0);

    Solenoid hood = new Solenoid(1, 0);
    Solenoid lightGreen = new Solenoid(1, 5);
    Solenoid lightBlue = new Solenoid(1, 6);
    Solenoid lightRed = new Solenoid(1, 7);

    CANifier canifier = new CANifier(0);


////VARIABLES
    double shooterTarget = 0;
    double turretTarget = 0;
    double hoodTarget = 0;

    double limelightX, limelightY, limelightD, limelightS;
    boolean limelightV;
    double gyro;


    int scanState = 0;


    boolean readyForFeed;
    boolean autoTargeted = false;
    boolean lockdown = false;
    boolean scanStateToggle = false;
    boolean turretReady = false;


////METHODS
    public void init(){

        turret.config_kP(0,5);
        turret.config_kI(0,0.05);
        turret.config_kD(0,148);
        turret.configMaxIntegralAccumulator(0, 100);

        canifier.setQuadraturePosition(0,10);

        toggleHood(true);
        setTurretEnc(0);
        setHoodEnc(0);
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
        System.out.printf("Shooter vel: %f, Shooter vel target: %f\n", getShooterVel(), vel);
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
        System.out.printf("Hood pos: %f, Hood target: %f\n", getHoodEnc(), target);
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
    public void setLight(char light, boolean toggle, int mode){
        //will change to mode- 0:off, 1:on, 2:blink)
        if(light == 'b') lightBlue.set(toggle);
        else if(light == 'g') lightGreen.set(toggle);
        else if(light == 'r') lightRed.set(toggle);
        else if(light == 'a') {
            lightBlue.set(toggle);
            lightRed.set(toggle);
            lightGreen.set(toggle);
        }
    }

////Logic
    public void updateValues(double llX, double llY, double llD, double llS, boolean llV, double theGyro){
        limelightX = llX;
        limelightY = llY;
        limelightD = llD;
        limelightS = llS;
        limelightV = llV;

        gyro = theGyro % 360;
        if(gyro > 180) gyro -= 360;
        else if(gyro < -180) gyro += 360;
    }

    public void keepShooterIdle(){

    }

    public boolean autoShooterVel(){
        return false;
    }

    public double doTurretMath(){
        //Returns the target value of the turret
        System.out.println(limelightS);
        if(limelightS < -85 || limelightS > -5){
            turretTarget = (getTurretEnc() + (-limelightX*25)) - (limelightS + (limelightS < -45 ? 90 : 0) * INNER_OFFSET); //(limelightA * TURRET_ENC_TO_DEG)
            setLight('g', true, 1);
        }
        // else if(Math.abs(limelightA) > OUTER_ZONE) turretTarget = (getTurretEnc() + (-limelightX*25)) + (limelightW / (limelightA > 0 ? 2 : -2));
        else{
            System.out.println("FALSE");
            turretTarget = getTurretEnc() + (-limelightX*26);
            setLight('g', false, 0);
        }

        if(turretTarget < MIN_TURRET) turretTarget = MIN_TURRET;
        else if(turretTarget > MAX_TURRET) turretTarget = MAX_TURRET;

        return turretTarget;
    }

    public void turretScan(){
        switch(scanState){
            case 0:
                if(getTurretEnc() < 200) turretTarget = MIN_TURRET;
                else turretTarget = MAX_TURRET;
                scanState = 1;
                break;
            case 1:
                if(Math.abs(getTurretEnc() - turretTarget) < TURRET_GOAL){
                    turretTarget = turretTarget == MIN_TURRET ? MAX_TURRET : MIN_TURRET;
                    scanState = 2;
                }
                break;
            case 2:
                if(Math.abs(getTurretEnc() - turretTarget) < TURRET_GOAL) scanState = 3;
                break;
            case 3:
                break;
            default:
                break;
        }
        if(turretTarget < MIN_TURRET) turretTarget = MIN_TURRET;
        else if(turretTarget > MAX_TURRET) turretTarget = MAX_TURRET;

        setTurretPos(turretTarget);
    }

    public void turretZero(){
        System.out.printf("The gyro: %f\n",gyro);
        turretTarget = (0 + gyro) * TURRET_ENC_TO_DEG;

        if(turretTarget < MIN_TURRET) turretTarget = MIN_TURRET;
        else if(turretTarget > MAX_TURRET) turretTarget = MAX_TURRET;

        setTurretPos(turretTarget);
    }

    public boolean autoTurretPos(boolean targetLocated, boolean tryToLock){
        scanStateToggle = false;
        turretReady = false;

        if(targetLocated && !lockdown){
            if(setTurretPos(doTurretMath())){
                setLight('b', true, 1);
                turretReady = true;
                if(tryToLock) lockdown = true;
            }
            else{
                setLight('b', true, 2);
                if(tryToLock){
                    /////move chassis code here///////
                }
            }
        }
        else if(targetLocated && lockdown){
            turretReady = true;
            if(!tryToLock) lockdown = false;
        }
        else{
            if(tryToLock){
                turretScan();
                scanStateToggle = true;
            }
            else turretZero();
        }

        if(!scanStateToggle && scanState != 0) scanState = 0;

        return turretReady;
    }

    public boolean autoHoodPos(double offsetY){
        hoodTarget = Math.abs(offsetY - 16) * 67;

        if(hoodTarget < MIN_HOOD){
            hoodTarget = MIN_HOOD;
        }
        else if(hoodTarget > MAX_HOOD){
            hoodTarget = MAX_HOOD;
        }
        return setHoodPos(hoodTarget);
    }

    public boolean autoTarget(){
        // if(lockdown) System.out.println("Locked down");
        // if(lockdown){
        //     autoTargeted = autoShooterVel();
        //     autoTargeted = setTurretPos(turretTarget) && autoTargeted;
        //     autoTargeted =  setHoodPos(hoodTarget) && autoTargeted;
        //     return (autoTargeted);
        // }
        // else{
        //     autoTargeted = autoTurretPos(limelightX);
        //     autoTargeted = autoHoodPos(limelightY) && autoTargeted;
        //     System.out.println(autoTargeted);
        //     return (autoTargeted);
        // }
        return true;
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


    /* Auto shoot routine
    Goal - To make shots anywhere on the field automatically

     * (Turret)Any possible inner goal shots need to be identified and adjusted for (shots slightly off the
    center line of the goal need to compensate, shooting at the center of target will not hit inner goal)
     * Hood will have to go over shooter wheel when down, shooter wheel should always be spinning slowly
    unless hood needs to go down, then only started back up when getting ready to shoot
     * Turret should constantly be adjusting no matter what
      -If no target, try to acheive 0 degrees (best pos to find target)
      -If no target while shoot attempt, scan from closest side to other side or if on 0, left to right, if no target blink red
      -If target seen but out of reach, blink blue. If on target, solid blue
      -If target out of reach while shoot attempt, move chassis to center target
      -If target is within certain degrees of goal, aim towards inner goal
      -If target is at sharp angle, aim off center to bounce off opposite wall
      -If targeted while attempt shoot, lock shooter unless detect chassis movement (after chassis is locked)
    */

    public boolean autoShoot(boolean brake){
        readyForFeed = false;
        if(brake && autoTarget()){
            lockdown = true;
            readyForFeed = true;
        }
        return readyForFeed;
    }

    public void resetAutoTarget(){
        lockdown = false;
        // setHoodPower(0);
        scanState = 0;
    }
}