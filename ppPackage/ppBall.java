package ppPackage;

import static ppPackage.ppSimParams.*;
import java.awt.Color;

import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;

/**
 * The ppBall class embeds the simulation for the movement and energy loss of the ball.
 * It extends the Thread class in order to be able to execute the run() method at the same
 * time as all the other methods in the program.
 * 
 * @author Kristian Myzeqari
 * The following code contains lines taken from the assignment 4 handout written by Frank Ferrie.
 * The following code contains lines taken from the tutorials given by Katrina Poulin.
 * The following code contains lines taken from ppBall.java (assignment 3) written by Kristian Myzeqari.
 */

public class ppBall extends Thread{

	private double Xinit;
	private double Yinit;
	private double Vo;
	private double theta;
	private double loss;
	private Color color;
	private GraphicsProgram GProgram;
	GOval myBall;
	ppTable myTable;
	ppPaddle RPaddle;
	ppPaddle LPaddle;

	double Vx;
	double Vy;
	double X, Xo;
	double Y, Yo;
	boolean falling;

	/**
	 * The constructor for the ppBall class copies parameters to instance variables, creates an
	 * instance of a GOval to represent the ping-pong ball, and adds it to the display.
	 *
	 * @param Xinit - starting position of the ball X (meters)
	 * @param Yinit - starting position of the ball Y (meters)
	 * @param Vo - initial velocity (meters/second)
	 * @param theta - initial angle to the horizontal (degrees)
	 * @param loss - loss on collision ([0,1])
	 * @param color - ball color (Color)
	 * @param GProgram - a reference to the ppSim class used to manage the display
	 */

	public ppBall(double Xinit, double Yinit, double Vo, double theta, double loss, Color color, GraphicsProgram GProgram, ppTable myTable) {
		this.Xinit=Xinit;
		this.Yinit=Yinit;
		this.Vo=Vo;
		this.theta=theta;
		this.loss=loss;
		this.color=color;
		this.GProgram=GProgram;
		this.myTable = myTable;
	}

	/**
	 * In a thread, the run method is NOT started automatically (like in Assignment 1).
	 * Instead, a start message must be sent to each instance of the ppBall class, e.g.,
	 * ppBall myBall = new ppBall (--parameters--);
	 * myBall.start();
	 * The body of the run method is essentially the simulator code you wrote for A1.
	 */

	public void run() {

		TSCALE = controlTick.getValue();

		GPoint p = myTable.W2S(new GPoint(Xinit,Yinit)); 				//graph ball
		double ScrX = p.getX();									//convert simulation to screen coordinates
		double ScrY = p.getY();
		GOval myBall = new GOval(ScrX, ScrY, 2*bSize*Xs,2*bSize*Ys); 
		myBall.setColor(color);
		myBall.setFilled(true);
		GProgram.add(myBall);
		GProgram.pause(1000);									//pause to see the ball before it starts moving



		Xo = Xinit + bSize;										//initial position for X
		Yo = Yinit;										//initial position for Y
		double time = 0;										//starting the timer at t=0s
		double Vt = bMass*g / (4*Pi*bSize*bSize*k);				//calculating terminal velocity
		double Vox = Vo * Math.cos(theta*Pi/180);				//initial velocity in "x" based on the given value
		double Voy = Vo * Math.sin(theta*Pi/180);				//the initial velocity in "y" based on the given value

		falling = true;

		while (falling) {										
			X = Vox*Vt/g*(1-Math.exp(-g*time/Vt));				//initial position of the ball in X and in Y (pixel values)
			Y = Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time;
			Vx = Vox*Math.exp(-g*time/Vt);
			Vy = (Voy+Vt)*Math.exp(-g*time/Vt)-Vt;

			double KEx = 0.5*bMass*Vx*Vx*(1-loss);
			double KEy = 0.5*bMass*Vy*Vy*(1-loss);
			double PE = g*bMass*(Y+Yo);

			GProgram.pause(TICK*TSCALE);						//delay for SLEEP microseconds


			if (Y+Yo >= Ymax) {
				if(Vox < 0) {
					agentScore++;
					scrA.setText(agentScore.toString());
				}
				if(Vox > 0) {
					playerScore++;
					scrP.setText(playerScore.toString());
				}

				falling = false;					//terminate simulation when ball hits ceiling
			}


			if (Y+Yo <= bSize) {								//check for when the ball hits the ground
				KEx = 0.5*bMass*Vx*Vx*(1-loss);					//reduce the kinetic energy in x by a factor of "loss".
				KEy = 0.5*bMass*Vy*Vy*(1-loss);					//reduce the kinetic energy in y by a factor of "loss".
				PE = 0;

				Vox = Math.sqrt(2*KEx/bMass);					//calculate new value for velocity in x
				if (Vx < 0) {									//keep direction for velocity of x 
					Vox = -Vox;
				}
				Voy = Math.sqrt(2*KEy/bMass);					//calculate new value for velocity in y

				time = 0;										//reinitialize variables
				Xo += X;
				Yo = bSize;
				X = 0;
				Y = 0;

				if (KEx + KEy + PE < ETHR) {						//check if total energy is lower than threshold value (break)
					falling = false;
					Vox = 0;
					Y = 0;
					X = 0;
					Yo = bSize;
				}

			}
			else if (Vx>0 && X + Xo >= (RPaddle.getP().getX() - ppPaddleW/2 - bSize)) {		//check for when the ball collides with right wall

				if (RPaddle.contact(X + Xo, Y + Yo) == true) {
					KEx = 0.5*bMass*Vx*Vx*(1-loss);					//reduce the kinetic energy in x by a factor of "loss".
					KEy = 0.5*bMass*Vy*Vy*(1-loss);					//reduce the kinetic energy in y by a factor of "loss".
					PE = g*bMass*(Y+Yo);

					Vox = Math.sqrt(2*KEx/bMass);					//calculate new value for velocity in x
					Vox = -Vox;										//invert velocity in x for the ball to return to the left wall
					Voy = Math.sqrt(2*KEy/bMass);					//calculate new value for velocity in y

					Vox = Vox*ppPaddleXgain;
					if (Vox >= VoxMAX) Vox=VoxMAX;
					Voy = Voy*ppPaddleYgain*RPaddle.getSgnVy();

					time = 0;										//reinitialize variables
					Xo = RPaddle.getP().getX() - ppPaddleW/2 - bSize;
					Yo += Y;
					X = 0;
					Y = 0;
				}

				else {
					falling = false;
					agentScore ++;
					scrA.setText(agentScore.toString());
				}
			}

			else if (Vx < 0 && X + Xo <= (LPaddle.getP().getX() + ppPaddleW/2 + bSize)) {		//check for when the ball collides with left wall

				if (LPaddle.contact(X + Xo,  Y + Yo) == true) {
					KEx = 0.5*bMass*Vx*Vx*(1-loss);					//reduce the kinetic energy in x by a factor of "loss".
					KEy = 0.5*bMass*Vy*Vy*(1-loss);					//reduce the kinetic energy in y by a factor of "loss".
					PE = g*bMass*(Y+Yo);

					Vox = Math.sqrt(2*KEx/bMass);					//calculate new value for velocity in x
					Voy = Math.sqrt(2*KEy/bMass);					//calculate new value for velocity in y

					Vox = Vox*LPaddleXgain;
					if (Vox >= VoxMAX) Vox=VoxMAX;
					Voy = Voy*LPaddleYgain*LPaddle.getSgnVy();

					time = 0;										//reinitialize variables
					Xo = LPaddle.getP().getX() + ppPaddleW/2 + bSize;
					Yo += Y;
					X += 0;
					Y = 0;
				}
				else {
					falling = false;
					playerScore ++;
					scrP.setText(playerScore.toString());
				}
			}

			p = myTable.W2S(new GPoint(Xo+X-bSize, Yo+Y+bSize));		// Get current position in screen coordinates
			ScrX = p.getX();
			ScrY = p.getY();
			myBall.setLocation(ScrX, ScrY);						// change position of ball in display


			if(traceButton.isSelected()) trace(ScrX,ScrY);									// place a marker on the current position
			time += TICK;										// update time
		}
	}

	/**
	 * A simple method to plot a dot at the current location in screen coordinates
	 * @param scrX - X location of point (world coordinates)
	 * @param scrY - Y location of point (world coordinates)
	 */

	private void trace(double ScrX, double ScrY) {
		GOval trace1 = new GOval(ScrX+ballS, ScrY+ballS,PD,PD);
		trace1.setColor(Color.BLACK);
		trace1.setFilled(true);
		GProgram.add(trace1);
		trace1.sendToBack();
	}

	/**
	 * Assigns an instance variable to the myPaddle object (right paddle)
	 * 
	 * @param myPaddle - Instance variable for myPaddle
	 */
	public void setRightPaddle(ppPaddle myPaddle) {
		this.RPaddle = myPaddle;		
	}
	
	/**
	 * assigns an instance variable to the myPaddle object (left paddle)
	 * 
	 * @param myPaddle - instance variable for myPaddle 
	 */
	public void setLeftPaddle(ppPaddle myPaddle) {
		this.LPaddle = myPaddle;
	}

	/**
	 * getV method is used to get the velocity of the ball and allows the selection of velocity in x or in y.
	 * 
	 * @return - method returns the velocity of the ball as a GPoint
	 */
	public GPoint getV() {
		return new GPoint (Vx, Vy);
	}

	/**
	 * getP method is used to get the coordinates of the ball and allows the selection of x or y coordinates individually
	 * 
	 * @return - returns the coordinates of the ball
	 */
	public GPoint getP() {
		return new GPoint (Xo + X,Yo + Y);
	}

	/**
	 * kill method ends the simulation when it is called
	 */
	void kill() {
		falling = false;
	}
}


