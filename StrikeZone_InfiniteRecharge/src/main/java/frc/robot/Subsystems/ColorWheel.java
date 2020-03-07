/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.util.Color;

/**
 * Add your docs here.
 */
public class ColorWheel extends RobotMap{
    Solenoid wheelEx = new Solenoid(0, 4);
     ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kOnboard); // colorSensor is color sensor
        private final ColorMatch colorMatch = new ColorMatch();
        String startColor = "";
        int upDownToggle = 0;


      
        private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429); //get numbers ourselves
        private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240); //get numbers ourselves
        private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114); //get numbers ourselves
        private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113); //get numbers ourselves
    public void djBoothInit(){
        colorMatch.addColorMatch(kBlueTarget);
        colorMatch.addColorMatch(kGreenTarget);
        colorMatch.addColorMatch(kRedTarget);
        colorMatch.addColorMatch(kYellowTarget);
        
    }

    public void djUp(){
        wheelEx.set(true);
    }
    public void djDown(){
        wheelEx.set(false);
    }
    public void djToggle(boolean toogle){
      if(toogle)upDownToggle++;
      if(upDownToggle == 1)djUp();
      else{
        djDown();
        upDownToggle = 0;
      }
    }

    public void djSpin(){
        wheelSpin.set(ControlMode.PercentOutput, .75);
    }
    public void djSpinOff(){
        wheelSpin.set(ControlMode.PercentOutput, 0);

    }
    int theWheelState = 0;
    public void djBoothRotation(){
        Color detectedColor = colorSensor.getColor(); // colorSensor is color sensor
        String nextColor;
        ColorMatchResult match = colorMatch.matchClosestColor(detectedColor);
        int count = 0;

          switch (theWheelState){
            case 0:
              if (match.color == kBlueTarget)
               startColor = "blue";
              else if (match.color == kGreenTarget)
               startColor = "green";
              else if (match.color == kRedTarget)
               startColor = "red";
              else if (match.color == kYellowTarget)
               startColor = "yellow";
              else
                startColor = "unknown";
              if (startColor != "unknown")
                theWheelState = 1;
              break;
            
            case 1:
              wheelSpin.set(ControlMode.PercentOutput, 0.5);
              if (match.color == kBlueTarget)
               nextColor = "blue";
              else if (match.color == kGreenTarget)
               nextColor = "green";
              else if (match.color == kRedTarget)
               nextColor = "red";
              else if (match.color == kYellowTarget)
               nextColor = "yellow";
              else
                nextColor = "unknown";
              if (startColor == nextColor)
               count++;
              if (count == 6)
                theWheelState = 2;
              break;
    
            case 2:
              wheelSpin.set(ControlMode.PercentOutput, 0);
              
              break;
              
          }
}
public void djBoothExact(){
    Color detectedColor = colorSensor.getColor();
    ColorMatchResult match = colorMatch.matchClosestColor(detectedColor);

        String gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData.length() > 0){
          switch (gameData.charAt(0)){
            case 'B':
             // blue case code make sure robot sees red
             if (match.color != kRedTarget){
               wheelSpin.set(ControlMode.PercentOutput, 0.5);
             }
             else {
              wheelSpin.set(ControlMode.PercentOutput, 0);
             }
             break;
            case 'G':
              // green case code make sure robot sees yellow
              if (match.color != kYellowTarget){
                wheelSpin.set(ControlMode.PercentOutput, 0.5);
              }
              else {
               wheelSpin.set(ControlMode.PercentOutput, 0);
              }
              break;
            case 'R':
              // red case code make sure robot sees blue
              if (match.color != kBlueTarget){
                wheelSpin.set(ControlMode.PercentOutput, 0.5);
              }
              else {
               wheelSpin.set(ControlMode.PercentOutput, 0);
              }
              break;
            case 'Y':
              // yellow case code make sure robot sees green
              if (match.color != kGreenTarget){
                wheelSpin.set(ControlMode.PercentOutput, 0.5);
              }
              else {
               wheelSpin.set(ControlMode.PercentOutput, 0);
              }
              break;
            default:
             // this is corrupt data and we're screwed
              System.out.println("The Game Data is Corrupt and There's No Hope.");
             break;
          }
        }
      

    }
    }
