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

    final int SHOOTER_MAX = 18000;
    final int SHOOTER_CLOSE = 16500;
    final int SHOOTER_MIN = 11500;
    final int SHOOTER_IDLE = 1000;

    final int SHOOTER_GOAL = 100;
    final int TURRET_GOAL = 10;
    final int HOOD_GOAL = 10;

    final int INNER_ZONE = 10;
    final int OUTER_ZONE = 60;
    final double INNER_OFFSET = 0.1;

    final double MAX_TARGET_Y = 21;
    final double MIN_TARGET_Y = -20;
    final double Y_PT1 = 2;
    final double Y_PT2 = 0;
    
    final int TURRET_ENC_TO_DEG = 26;
    final int HOOD_VALUE = -95; //Higher causes hood to get to max sooner

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

    double limelightX, limelightY, limelightA;
    boolean limelightF;
    double gyro;


    int scanState = 0;


    boolean readyForFeed;
    boolean autoTargeted = false;
    boolean lockdown = false;
    boolean scanStateToggle = false;
    boolean turretReady = false;
    boolean innerTarget = false;


////METHODS
    public void init(){

        turret.config_kP(0,5);
        turret.config_kI(0,0.05);
        turret.config_kD(0,148);
        turret.configMaxIntegralAccumulator(0, 100);

        shooterMaster.config_kP(0,.3);
        shooterMaster.config_kI(0,0.05);
        shooterMaster.config_kD(0,50);
        shooterMaster.config_kF(0,.049);
        shooterMaster.configMaxIntegralAccumulator(0, 100);

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
        shooterMaster.set(ControlMode.Velocity,vel);
        shooterFollower.set(ControlMode.Velocity,-vel);
        if(Math.abs(getShooterVel() - vel) > SHOOTER_GOAL) System.out.printf("Shooter vel: %f, Shooter target: %f, MATH: %f\n", getShooterVel(), vel, Math.abs(getShooterVel() - vel));
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
        hoodAdjust.setSpeed((target - getHoodEnc())/118);
        if(Math.abs(getHoodEnc() - target) > HOOD_GOAL) System.out.printf("Hood pos: %f, Hood target: %f, MATH: %f\n", getHoodEnc(), target, Math.abs(getHoodEnc() - target));
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
    public void updateValues(double llX, double llY, double llA, boolean llF, double theGyro){
        limelightX = llX;
        limelightY = llY;
        limelightA = llA;
        limelightF = llF;

        //limelightA = width > height ? 
        //limelightS < -45 then add 90

        gyro = theGyro % 360;
        if(gyro > 180) gyro -= 360;
        else if(gyro < -180) gyro += 360;
    }


    public void keepShooterIdle(){

    }

    public boolean autoShooterVel(){
        // if(limelightY > Y_PT1) shooterTarget = SHOOTER_CLOSE;
        // else if(limelightY < Y_PT2) shooterTarget = ((SHOOTER_MAX - SHOOTER_MIN) * ((limelightY - Y_PT2)/(Y_PT2 - MIN_TARGET_Y))) + SHOOTER_MIN;
        // else shooterTarget = (SHOOTER_CLOSE - SHOOTER_MIN) * (1 - (limelightY - Y_PT1)/(Y_PT1 - MIN_TARGET_Y)) + SHOOTER_MIN;

        if(limelightY > 0){
            shooterTarget = ((SHOOTER_CLOSE - SHOOTER_MIN) * ((limelightY - MAX_TARGET_Y)/-MAX_TARGET_Y) + SHOOTER_MIN);
        }
        else{
            shooterTarget = ((SHOOTER_MAX - SHOOTER_CLOSE) * (limelightY/MIN_TARGET_Y) + SHOOTER_CLOSE);
        }

        if(shooterTarget < SHOOTER_MIN) shooterTarget = SHOOTER_MIN;
        else if(shooterTarget > SHOOTER_MAX) shooterTarget = SHOOTER_MAX;

        return setShooterVel(shooterTarget);
    }


    public double doTurretMath(){
        //Returns the target value of the turret
        System.out.println(limelightA);
        if((Math.abs(limelightA) < 6 && !innerTarget) || (Math.abs(limelightA) < 9 && innerTarget)){
            turretTarget = (getTurretEnc() + (-limelightX*TURRET_ENC_TO_DEG)) - (Math.abs(limelightA) * INNER_OFFSET); //(limelightA * TURRET_ENC_TO_DEG)
            setLight('g', true, 1);
            innerTarget = true;
        }
        // else if(Math.abs(limelightA) > OUTER_ZONE) turretTarget = (getTurretEnc() + (-limelightX*25)) + (limelightW / (limelightA > 0 ? 2 : -2));
        else{
            System.out.println("FALSE");
            turretTarget = getTurretEnc() + (-limelightX*TURRET_ENC_TO_DEG);
            setLight('g', false, 0);
            innerTarget = false;
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
        if(gyro > -90 && gyro < 90) turretTarget = (0 + gyro) * TURRET_ENC_TO_DEG;

        if(turretTarget < MIN_TURRET) turretTarget = MIN_TURRET;
        else if(turretTarget > MAX_TURRET) turretTarget = MAX_TURRET;

        if(turretTarget == MAX_TURRET && gyro < -165 && gyro > -178) turretTarget = MIN_TURRET;
        else if(turretTarget == MIN_TURRET && gyro > 165 && gyro < 178) turretTarget = MAX_TURRET;

        setTurretPos(turretTarget);
    }

    public boolean autoTurretPos(boolean tryToLock){
        scanStateToggle = false;
        turretReady = false;

        if(limelightF && !lockdown){
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
        else if(limelightF && lockdown){
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


    public boolean autoHoodPos(){
        hoodTarget = limelightF ? (limelightY - MAX_TARGET_Y) * HOOD_VALUE : ((MAX_HOOD - MIN_HOOD) * .75) + MIN_HOOD;

        if(hoodTarget < MIN_HOOD) hoodTarget = MIN_HOOD;
        else if(hoodTarget > MAX_HOOD) hoodTarget = MAX_HOOD;

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