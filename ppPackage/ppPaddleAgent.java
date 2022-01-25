package ppPackage;

import java.awt.Color;

import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import static ppPackage.ppSimParams.*;

/**
 * The ppPaddleAgent class holds the constructor for ppPaddleAgent and is exported to ppSim in order to make the LPaddle.
 * 
 * The following code contains lines taken from the assignment 4 handout written by Frank Ferrie.
 * The following code contains lines taken from the tutorials given by Katrina Poulin.
 * @author kristianmyzeqari
 */

public class ppPaddleAgent extends ppPaddle {

	// same instance variables
	//methods
	//constructor

	ppBall myBall;

	/**
	 * Constructor for the agent paddle, refers to ppPaddle class, is exported to ppSim
	 * @param X - X coordinate of paddle
	 * @param Y - Y coordinate of paddle
	 * @param myColor - Colour of the paddle
	 * @param myTable - refers to the ppTable class
	 * @param GProgram - refers to GProgram
	 */
	public ppPaddleAgent(double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) {
		//error: means we need to use the ppPaddle constructor
		super(X, Y, myColor, myTable, GProgram);
		//create a ppPaddle
	}

	/**
	 * The run method in ppPaddleAgent deals with the lag that we need for the agent.
	 */
	public void run() {
		int lagA = lag.getValue();

		int AgentLag = lagA;
		int ballSkip = 0;

		while (true) {

			if(ballSkip++ >= AgentLag) {
				// code to update paddle position
				//get y position of ball
				double Y = myBall.getP().getY();
				//set paddle position to Y
				this.setP(new GPoint(this.getP().getX(), Y));

				ballSkip = 0;

			}	
			TSCALE = controlTick.getValue();					// get value of controlTick slider and apply it to TSCALE
			GProgram.pause(TICK*TSCALE);
		}
	}

	/**
	 * takes myBall instance variable and allows us to use it with LPaddle to make it follow the ball.
	 * 
	 * @param myBall - uses instance variable myBall
	 */
	public void attachBall (ppBall myBall) {
		this.myBall = myBall;
	}
}








