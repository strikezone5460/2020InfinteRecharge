/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

// import edu.wpi.first.wpilibj.ADXRS450_Gyro;
//import edu.wpi.first.wpilibj.Solenoid;
// import edu.wpi.first.wpilibj.SPI.Port;

/**
 * intantiates all Motorcontrolers to be used throught the program
 */
public class RobotMap {
    TalonFX leftDriveMaster = new TalonFX(1);
    TalonFX leftDriveSlave = new TalonFX(2);
    TalonFX rightDriveMaster = new TalonFX(3);
    TalonFX rightDriveSlave = new TalonFX(4);

    TalonFX shooterMaster = new TalonFX(5);
    TalonFX shooterSlave = new TalonFX(6);

    VictorSPX intake = new VictorSPX(7);

    TalonSRX hopperHorizontal = new TalonSRX(8);
    VictorSPX hopperVertical = new VictorSPX(9);

    TalonSRX wheelSpin = new TalonSRX(10);

    TalonSRX turretRotation = new TalonSRX(11);

    CANifier canifier = new CANifier(0);


// 
//  Constants
//     
    static double kShooterPct = 1.0;
    static double kShooterOff = 0.0;
    public int kShooterVel[] = {15200, 15300, 15500, 16000, 16500, 16800 ,16300 ,16300 ,16300 ,16700 ,16800 ,17300 ,17700};
    static double kHoodPos[] = {0.475, .55, .675, .825, .9, .95, .875, .875, .85, .775, .775, .875, .9};
    static double kLimelightty[] = {18, 12.6, 4.3, 1.3, -1.3, -3.2, -5.4, -6.9, -8.5, -10.5, -11, -12.3, -13.3};


    int kMaxLowDriveVel = 19000;
    int kMaxHighDriveVel = 19500;


}
