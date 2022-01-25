package ppPackage;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

import static ppPackage.ppSimParams.*;
import java.awt.Color;

/**
 * The ppPaddle class contains the constructor that creates the paddle moved by the user.
 * 
 * The following code contains lines taken from the assignment 4 handout written by Frank Ferrie.
 * The following code contains lines taken from the tutorials given by Katrina Poulin.
 * @author kristianmyzeqari
 *
 */

public class ppPaddle extends Thread{
	double X;
	double Y;
	double Vx;
	double Vy;
	int agentScore, playerScore;
	GRect myPaddle;
	ppTable myTable;
	GraphicsProgram GProgram;
	Color myColor;

	
	
	/**
	 * ppPaddle constructor creates an instance of the paddle and is exported to ppSim to be used.
	 * 
	 * @param X - X coordinate of Paddle
	 * @param Y - Y coordinate of Paddle
	 * @param myTable - to be able to use methods from ppTable, an instance of ppTable must be included
	 * @param GProgram - Gprogram reference
	 */

	public ppPaddle(double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) {
		this.X = X;
		this.Y = Y;
		this.myTable = myTable;
		this.GProgram = GProgram;
		this.Vx = 0;
		this.Vy = 0;
		this.myColor = myColor;

		//world coordinates
		double upperLeftX = X - ppPaddleW/2;
		double upperLeftY = Y + ppPaddleH/2;

		//screen coordinates
		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));

		//screen coordinates
		double ScrX = p.getX();
		double ScrY = p.getY();

		this.myPaddle = new GRect(ScrX, ScrY, ppPaddleW*Xs, ppPaddleH*Ys);
		myPaddle.setColor(myColor);
		myPaddle.setFilled(true);
		this.GProgram.add(myPaddle);
	}


	public void run() {
		TSCALE = controlTick.getValue();

		double lastX = X;
		double lastY = Y;

		while(true) {
			Vx=(X-lastX)/TICK;
			Vy=(Y-lastY)/TICK;
			lastX=X;
			lastY=Y;
			GProgram.pause(TICK*TSCALE);
		}
	}

	/**
	 * Method that returns the coordinates of the paddle
	 * @return - returns the coordinates of the paddle as a GPoint
	 */

	public GPoint getP() {
		return new GPoint(X, Y);
	}

	/**
	 * Set paddle location
	 * @param P - GPoint that sets the location of the paddle
	 */

	public void setP(GPoint P) {
		this.X = P.getX();
		this.Y = P.getY();

		double upperLeftX = X - ppPaddleW/2;
		double upperLeftY = Y + ppPaddleH/2;

		//screen
		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));

		//screen
		double ScrX = p.getX();
		double ScrY = p.getY();

		//move GRect
		myPaddle.setLocation(ScrX, ScrY);
	}

	/**
	 * Method to read the velocity of the paddle
	 * @return - GPoint of the velocity of the paddle (coordinate point where X is Vx and Y is Vy)
	 */

	public GPoint getV() {
		return new GPoint(Vx, Vy);
	}

	/**
	 * Method to read the sign of the velocity in y of the paddle
	 * @return returns a number +/-1 that changes the Vox in ppBall class
	 */

	public double getSgnVy() {
		if (Vy<0) return -1;
		else return 1;
	}

	/**
	 * Method that reads for when the ball touches the paddle.
	 * @param Sx - Position of the ball in X
	 * @param Sy - Position of the ball in Y
	 * @return returns a boolean value depending on if the ball touches the paddle.
	 */

	public boolean contact(double Sx, double Sy) {
		return( (Sy >= Y - ppPaddleH/2) && (Sy <= Y + ppPaddleH/2));
	}

}
