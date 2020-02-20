/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

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

    VictorSPX wheelSpin = new VictorSPX(10);

    TalonSRX turretRotation = new TalonSRX(11);


// 
//  Constants
//     
    double kShooterPct = 1.0;
    double kShooterOff = 0.0;
    int kShooterVel = 19000; 

    int kMaxLowDriveVel = 19000;
    int kMaxHighDriveVel = 19500;


}
