import lejos.nxt.*;
import lejos.nxt.comm.RConsole;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Pose;
import lejos.util.Delay;
/**
 * A program that runs in a straight line and was totally not inspired by any other programs
 * @author  Troels
 * @version 13.05.15
 */
public class PilotLine 
{
   private static void show(Pose p)
   {
      LCD.clear();
       LCD.drawString("Pose X " + p.getX(), 0, 2);
       LCD.drawString("Pose Y " + p.getY(), 0, 3);
       LCD.drawString("Pose V " + p.getHeading(), 0, 4);
   }

   public static void main(String [] args)  
   throws Exception 
   {
       double wheelDiameterLeft = 5.55, wheelDiameterRight = 5.51, trackWidth = 15.725;
       double travelSpeed = 5, rotateSpeed = 45;
       NXTRegulatedMotor left = Motor.B;
       NXTRegulatedMotor right = Motor.C;
	   
       DifferentialPilot pilot = new DifferentialPilot(wheelDiameterLeft, wheelDiameterRight , trackWidth, left, right, false);
       OdometryPoseProvider poseProvider = new OdometryPoseProvider(pilot);
       Pose initialPose = new Pose(0,0,0);
       RConsole.open();
       pilot.setTravelSpeed(travelSpeed);
       pilot.setRotateSpeed(rotateSpeed);
       poseProvider.setPose(initialPose);
       
       LCD.clear();
       LCD.drawString("FredeBot", 0, 0);
       Button.waitForAnyPress();
	   
       pilot.travel(100);
       
       pilot.stop();
       LCD.drawString("Program stopped", 0, 0);
       Button.waitForAnyPress();
       RConsole.close();
       System.exit(0);
   }
}
