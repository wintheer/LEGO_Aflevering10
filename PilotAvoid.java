import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;

public class PilotAvoid
{
    private SlaveIOStreams PC;
    
    private double wheelDiameterLeft = 5.55, wheelDiameterRight = 5.51, trackWidth = 15.725;
    private double travelSpeed = 5, rotateSpeed = 45;
    NXTRegulatedMotor left = Motor.B;
    NXTRegulatedMotor right = Motor.C;
	   
    DifferentialPilot pilot = new DifferentialPilot(wheelDiameterLeft, wheelDiameterRight , trackWidth, left, right, false);
   
	public PilotAvoid(boolean usb) 
	{
    	PC = new SlaveIOStreams(usb);
    	PC.open();
 	   
 	    pilot.setTravelSpeed(travelSpeed);
 	    pilot.setRotateSpeed(rotateSpeed);
	}
	
	private void sendMove(Move move)
	{	
		PC.output((move.getMoveType() == Move.MoveType.TRAVEL? 0:1 ));
		PC.output(move.getDistanceTraveled());
		PC.output(move.getAngleTurned());
	}
	
	private void travel(double distance)
	{	
		pilot.travel(distance);
		sendMove(pilot.getMovement());
	}
	
	private void rotate(double angle)
	{	
		pilot.rotate(angle);
		sendMove(pilot.getMovement());
	}
	
	public void go()
	{				
		Behavior b1 = new FollowRoute();
	    Behavior b2 = new AvoidObstacles();
	   

	    
	    Behavior[] behaviorList =
	    {
	      b1, b2
	    };
	    Arbitrator arbitrator = new Arbitrator(behaviorList);
	   
	    Button.ENTER.waitForPress();
	    try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    arbitrator.start();
	  }
		
		
	
	
	public static void main(String[] args) 
	{
		PilotRoute route = new PilotRoute(false);
		
		LCD.clear();
		LCD.drawString("Pilot route", 0, 0);
		while(! Button.ESCAPE.isDown()){
		route.go();		
		Button.waitForAnyPress();
		}
	}
}
//behavior for moving along route
class FollowRoute implements Behavior
{

private boolean _suppressed = false;

public int takeControl()
{
  return 10;  // this behavior always wants control.
}

public void suppress()
{
  _suppressed = true;// standard practice for suppress methods
}

public void action()
{
  _suppressed = false;
  
  // write route here
 
  while (!_suppressed)
  {
    Thread.yield(); //don't exit till suppressed
  }
  Motor.A.stop(); // not strictly necessary, but good programming practice
  Motor.C.stop();
  LCD.drawString("Drive stopped",0,2);
}
}

//behavior to avoid obstacles
class AvoidObstacles extends Thread implements Behavior
{
private boolean _suppressed = false;
private boolean active = false;
private int distance = 255;

public AvoidObstacles()
{
  sonar = new UltrasonicSensor(SensorPort.S3);
  this.setDaemon(true);
  this.start();
}

//the ultrasonic sensor is run in a thread so no method needs to wait for it to get a response
public void run()
{
  while ( true ) distance = sonar.getDistance();
}


public int takeControl()
{
  if (distance < 40)
     return 80;
  if ( active )
     return 40;
  return 0;
}

public void suppress()
{
  _suppressed = true;// standard practice for suppress methods  
}

public void action()
{
  _suppressed = false;
  active = true;
	

  
  while (!_suppressed )
  {
     Thread.yield(); //don't exit till suppressed
  }
 
  
  Motor.A.stop(); 
  Motor.C.stop();
  LCD.drawString("Stopped       ",0,3);
  active = false;
  
}
private UltrasonicSensor sonar;
}

