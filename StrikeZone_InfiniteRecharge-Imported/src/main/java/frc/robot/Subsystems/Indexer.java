package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;


public class Indexer{
    ////DEVICES
    DigitalInput PosA = new DigitalInput(3);
    DigitalInput PosB = new DigitalInput(4);
    DigitalInput PosC = new DigitalInput(5);
    
    TalonSRX indexerHorizontal = new TalonSRX(8);
    VictorSPX indexerVertical = new VictorSPX(9);

    ////VARIABLES
    int indexState = 0;
    long savedTime = 0;
    int timeoutTimer = 0;

    ////METHODS
    public void indexerHPower(double power){
        if(indexState == 0 || indexState == 4) indexerHorizontal.set(ControlMode.PercentOutput,power);
    }

    public void indexerVPower(double power){
        if(indexState == 0 || indexState == 4) indexerVertical.set(ControlMode.PercentOutput,power);
    }

    public void indexerPower(double power){
        indexerVertical.set(ControlMode.PercentOutput,power);
        indexerHorizontal.set(ControlMode.PercentOutput,power);
    }

    public boolean timer(int timeTarget){
        if(savedTime == 0)savedTime = System.currentTimeMillis();
        if(System.currentTimeMillis() - savedTime >= timeTarget){
            savedTime = 0;
            timeTarget = 0;
            return true;
        }
        else{
            return false;
        }
    }

    public boolean aBlocked(){
        return(!PosA.get());
    }

    public boolean bBlocked(){
        return(!PosB.get());
    }

    public boolean cBlocked(){
        return(!PosC.get());
    }

    public void autoIndex(){
        if(aBlocked() && !cBlocked() && indexState == 0){
            indexState = bBlocked() ? 2 : 1;
        }

        switch(indexState){
            case 0: ///Disabled
                break;
            case 1: ///Move cell from A to B
                if(bBlocked()){
                    indexState = 0;
                    indexerPower(0);
                    savedTime = 0;
                }
                else if(timer(1000)){
                    indexState = 4;
                    indexerPower(0);
                    savedTime = 0;
                    // shooter.setLight(0,true);
                }
                else{
                    indexerPower(-.65);
                }
                break;
            case 2: ///Move cells from A,B to B,C
                if(!timer(300)){
                    indexerPower(-.6);
                }
                else{
                    indexState = 3;
                }
                break;
            case 3: ///Back cells away from shooter wheel
                if(!timer(60)){
                    indexerPower(.6);
                }
                else{
                    indexState = 0;
                    indexerPower(0);
                }
                break;
            case 4: ///Jammed, wait until empty
                if(!aBlocked() && !bBlocked() && !cBlocked()){
                    if(timer(1000)){
                        indexState = 0;
                        // shooter.setLight(0,false);
                    }
                }
                else{
                    savedTime = 0;
                }
                break;
            default:
                break;
        }
    }
}