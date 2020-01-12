/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
//import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SPI.Port;

/**
 * Add your docs here.
 */
public class RobotMap {
    TalonFX leftDriveMaster = new TalonFX(1);
    TalonFX leftDriveSlave = new TalonFX(2);
    TalonFX rightDriveMaster = new TalonFX(3);
    TalonFX rightDriveSlave = new TalonFX(4);

    TalonFX shooterMaster = new TalonFX(5);//Might Change
    TalonFX shooterSlave = new TalonFX(6);

    VictorSPX intakeMaster = new VictorSPX(7);
    VictorSPX intakeSlave = new VictorSPX(8);

    TalonSRX hopperMaster = new TalonSRX(9);
    VictorSPX hopperSlave = new VictorSPX(10);

    VictorSPX climber = new VictorSPX(11);

    VictorSPX wheelSpinner = new VictorSPX(12);

    TalonSRX turretRotation = new TalonSRX(13);

    // ADXRS450_Gyro gyro = new ADXRS450_Gyro(Port.kOnboardCS0);//might change


    //Solenoids


    // Solenoid PTO = new Solenoid(2);

    // Solenoid intakeOut = new Solenoid(3);
    // Solenoid intakeIn = new Solenoid(4);

    // Solenoid spinnerLift = new Solenoid(5);

    // Solenoid ballStop = new Solenoid(6);

    // Solenoid hoodHolder = new Solenoid(7);//I dont know exacly what this is yet
    
    

}
