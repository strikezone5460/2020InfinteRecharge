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

/**
 * Add your docs here.
 */
public class RobotMap {
    TalonFX leftDriveMaster = new TalonFX(1);
    TalonFX leftDriveSlave = new TalonFX(2);
    TalonFX rightDriveMaster = new TalonFX(3);
    TalonFX rightDriveSlave = new TalonFX(4);

    TalonSRX shooterMaster = new TalonSRX(5);
    VictorSPX shooterSlave = new VictorSPX(6);

    VictorSPX intake = new VictorSPX(7);

    TalonSRX hopperMaster = new TalonSRX(8);
    VictorSPX hopperSlave = new VictorSPX(9);

    VictorSPX climber = new VictorSPX(10);

    VictorSPX wheelSpinner = new VictorSPX(11);

    TalonSRX turretRotation = new TalonSRX(12);


    
    

}
