package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;


public class Indexer{
    DigitalInput PosA = new DigitalInput(3);
    DigitalInput PosB = new DigitalInput(4);
    DigitalInput PosC = new DigitalInput(5);
    
    TalonSRX indexerHorizontal = new TalonSRX(8);
    VictorSPX indexerVertical = new VictorSPX(9);

    public void indexerHPower(double power){
        indexerHorizontal.set(ControlMode.PercentOutput,power);
    }

    public void indexerVPower(double power){
        indexerVertical.set(ControlMode.PercentOutput,power);
    }

    public boolean isABlocked(){
        return(PosA.get());
    }

    public boolean isBBlocked(){
        return(PosB.get());
    }

    public boolean isCBlocked(){
        return(PosC.get());
    }
}