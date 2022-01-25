package ppPackage;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

/**
 * The ppSimParams class defines the variables that will be used in the ppBall, ppTable and ppSim classes.
 * The ppSimParams class allows the different classes in the package to have access to the same variables
 * without having to include all of them in each class. The variables used in this class are available publicly 
 * in order to be able to use them in other classes.
 * 
 * @author Kristian Myzeqari
 *
 *The following code contains lines taken from the assignment 4 handout written by Frank Ferrie.
 *The following code contains lines taken from ppSimParams (assignment 3) written by Kristian Myzeqari.
 */

public class ppSimParams {

	//toggle
	public static JToggleButton traceButton;					//Create instance variable for toggle trace button
	public static Integer playerScore = 0;						//Accumulated score player
	public static Integer agentScore = 0;						//Accumulated score agent
	public static JLabel scrP;									//Create instance variable for label
	public static JLabel scrA;									
	public static JSlider lag;									//Create instance variable for agent lag slider
	public static JSlider controlTick;							//Create instance variable for time control slider

	//1. Parameters defined in screen coordinates

	public static final int WIDTH = 1280;						//width of the screen
	public static final int HEIGHT = 600;						//height of the screen
	public static final int OFFSET = 200;				

	//2. Ping-pong table parameters

	public static final double ppTableXlen = 2.74;				//length
	public static final double ppTableHgt = 1.52;				//ceiling
	public static final double  XwallL = 0.05;					//position of the left wall (real life values)
	public static final double XwallR = 2.69; 					//position of the right wall (real life values)

	//3. Parameters defined in simulation coordinates

	public static final double Xmin = 0.0;						//smallest value for x (real life values)
	public static final double Xmax = ppTableXlen;				//largest value for x (real life values)
	public static final double Ymin = 0.0;						//smallest value for y (real life values)
	public static final double Ymax = ppTableHgt;				//largest value for y (real life values)
	public static final int xmin = 0;							//smallest value for x (pixel values)
	public static final int xmax = WIDTH;						//largest value for x (pixel values)
	public static final int ymin = 0;							//smallest value for y (pixel values)
	public static final int ymax = HEIGHT;						//largest value for y (pixel values)
	public static final double Xs = (xmax-xmin)/(Xmax-Xmin);	//Scale factor (x)
	public static final double Ys = (ymax-ymin)/(Ymax-Ymin);	//Scale factor (y)
	public static final double Xinit = XwallL;					//initial position of the ball in x
	public static final double Yinit = Ymax/2;					//initial position of the ball in y
	public static final double PD = 1;							//diameter trace point
	public static int TSCALE;

	public static final double g = 9.8;							//gravitational constant
	public static final double k = 0.1316;						//air friction
	public static final double Pi = 3.1416;						//numerical value of pi
	public static final double bSize = 0.02;					//radius of ball (real life values)
	public static final double bMass = 0.0027;					//mass of the ball in grams
	public static final double ETHR = 0.001;					//minimum total energy (threshold for movement)
	public static final double TICK = 0.01;						//clock increment at each iteration

	//4. Paddle Parameters

	public static final double ppPaddleH = 8*2.54/100;			//Paddle height
	public static final double ppPaddleW = 0.5*2.54/100;				//Paddle width
	public static final double ppPaddleXinit = XwallR-ppPaddleW/2;		//Initial position right paddle in X
	public static final double ppPaddleYinit = Yinit;					//Initial position right paddle in Y
	public static final double ppPaddleXgain = 1.5;						//Vx gain on paddle hit
	public static final double ppPaddleYgain = 1.5;						//Vy gain on paddle hit
	public static final double LPaddleXinit = XwallL - ppPaddleW/2;		// Initial position left paddle in X
	public static final double LPaddleYinit = Yinit;					// Initial position left pddle in Y
	public static final double LPaddleXgain = 1.5;				//Vx gain on paddle hit
	public static final double LPaddleYgain = 1.5;				//Vy gain on paddle hit
	public static final double VoxMAX = 10.0;					// maximum velocity in X direction

	//5. Parameters used by the ppSim class

	public static final double YinitMAX = 0.75*Ymax;			//Max initial height at 75% of range
	public static final double YinitMIN = 0.25*Ymax;			//Min initial height at 25% of range
	public static final double EMIN = 0.2;						//Minimum loss coefficient
	public static final double EMAX = 0.2;						//Maximum loss coefficient
	public static final double VoMIN = 5.0;						//Minimum velocity
	public static final double VoMAX = 5.0;						//Maximum velocity
	public static final double ThetaMIN = 0.0;					//Minimum launch angle
	public static final double ThetaMAX = 20.0;					//Maximum launch angle
	public static final long RSEED = 8976232;					//Random numver gen. seed value


	//6. miscellaneous
	public static final double ballS = (bSize)*(Xs); 			//ball size in pixels

	public static final double lWall = (XwallL)*(Xs);  			//position of the left wall in pixel values
	public static final double rWall = (XwallR)*(Xs);			//position of the right wall in pixel values
	public static final int STARTDELAY = 1000;					//delay between setup and start

}
