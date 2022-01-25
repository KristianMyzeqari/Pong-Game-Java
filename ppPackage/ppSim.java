package ppPackage;

import static ppPackage.ppSimParams.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

/**
 * The ppSim class is the main entry point of the program. 
 * The ppSim class contains the main(String[] args) method and the run() method which allow the program to run
 * and to start the simulation.
 * 
 * @author Kristian Myzeqari
 * The following code contains lines taken from the assignment 4 handout written by Frank Ferrie.
 * The following code contains lines taken from the tutorials given by Katrina Poulin.
 * The following code contains lines taken from Bounce.java (assignment 1) written by Kristian Myzeqari.
 */

public class ppSim extends GraphicsProgram {

	ppBall myBall;
	ppPaddle RPaddle;
	ppPaddleAgent LPaddle;
	ppTable myTable;
	Color myColor;
	RandomGenerator rgen;


	/**
	 * The main method allows the program to run in the acm environment. It defines
	 * a main class for the program.
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		new ppSim().start(args);
	}

	/**
	 * The init method generates the table and the different buttons and sliders used to control the simulation.
	 */

	public void init(){
		this.setSize(ppSimParams.WIDTH+OFFSET,ppSimParams.HEIGHT+OFFSET);		//Sets the size of the screen

		// Score board for simulation
		JLabel agent = new JLabel ("Agent:   ");
		scrA = new JLabel("0");
		JLabel player = new JLabel ("    Player:   ");
		scrP = new JLabel("0");
		
		//Labels used for sliders
		JLabel lagMin = new JLabel ("-lag");
		JLabel lagMax = new JLabel ("+lag");
		JLabel tickMin = new JLabel ("-t");
		JLabel tickMax = new JLabel ("+t");
		
		//Buttons used in simulation
		JButton clearButton = new JButton("Clear");							// Clears the entire screen and resets the score board to 0-0
		JButton newServeButton = new JButton("New Serve");					// Clears the entire screen and starts a new game
		JButton quitButton = new JButton("Quit");							// Terminates simulation and closes console
		traceButton = new JToggleButton("Trace", false);					// Button to enable or disable trace function
		JButton resetLag = new JButton("rlag");								// Button used to reset agent lag
		JButton resetTick = new JButton("rtime");							// Button used to reset simulation speed

		//sliders used in simulation
		lag = new JSlider(0, 15, 7);										// Slider used to control the lag
		controlTick = new JSlider(500, 7000, 2000);							// Slider used to control the speed of the simulation

		TSCALE = controlTick.getValue();									// Gets value of controlTick slider
		
		
		//adding all of the labels, buttons and sliders to the simulation
		add(agent, NORTH);
		add(scrA, NORTH);
		add(player, NORTH);
		add(scrP, NORTH);
		
		add(clearButton, SOUTH);
		add(newServeButton, SOUTH);
		add(traceButton, SOUTH);
		add(quitButton, SOUTH);
		
		add(tickMin, SOUTH);
		add(controlTick, SOUTH);
		add(tickMax, SOUTH);
		add(resetTick, SOUTH);
		
		add(lagMin, SOUTH);
		add(lag, SOUTH);
		add(lagMax, SOUTH);
		add(resetLag, SOUTH);

		addMouseListeners();									//add mouse listeners for RPaddle
		addActionListeners();									//add action listeners for buttons and sliders

		rgen = RandomGenerator.getInstance();					// Use random number generator for initial conditions of the ball.
		rgen.setSeed(RSEED);

		myTable = new ppTable(this);
		newGame();
	}

	
	/**
	 * The newBall class is used to quickly be able to generate a new ball for the simulation.
	 * 
	 * @return - returns an instance of ppBall
	 */
	ppBall newBall() {
		//generate parameters for simulation
		Color iColor = Color.RED;
		double iYinit = rgen.nextDouble(YinitMIN, YinitMAX);
		double iLoss = rgen.nextDouble(EMIN, EMAX);
		double iVel = rgen.nextDouble(VoMIN, VoMAX);
		double iTheta = rgen.nextDouble(ThetaMIN, ThetaMAX);

		return new ppBall(Xinit, iYinit, iVel, iTheta, iLoss, iColor, this, myTable);
	}

	
	/**
	 * The newGame class does not return anything, but it resets the screen, sets a new table, new paddles, and a ball.
	 * The newGame class automatically restarts a game when it is called.
	 */
	public void newGame() {
		if(myBall != null) myBall.kill();

		myTable.newScreen();

		myTable = new ppTable(this);
		myBall = newBall();
		RPaddle = new ppPaddle(ppPaddleXinit, ppPaddleYinit, Color.GREEN, myTable, this);		//create instance of RPaddle
		LPaddle = new ppPaddleAgent(LPaddleXinit, LPaddleYinit, Color.BLUE, myTable, this);		//create instance of LPaddle
		LPaddle.attachBall(myBall);																//use attachball method on LPaddle to follow movement

		myBall.setRightPaddle(RPaddle);
		myBall.setLeftPaddle(LPaddle);
		pause(STARTDELAY);

		myBall.start();
		LPaddle.start();
		RPaddle.start();
	}
	
	/**
	 * The clear method is used to empty out the screen and set a new table and paddles, but does not restart the simulation on its own.
	 */
	public void clear() {
		if(myBall != null) myBall.kill();

		myTable.newScreen();

		myTable = new ppTable(this);
		myBall = newBall();
		RPaddle = new ppPaddle(ppPaddleXinit, ppPaddleYinit, Color.GREEN, myTable, this);		//create instance of RPaddle
		LPaddle = new ppPaddleAgent(LPaddleXinit, LPaddleYinit, Color.BLUE, myTable, this);		// create instance of LPaddle
		LPaddle.attachBall(myBall);																// attach LPaddle to myBall so it can follow the ball

		myBall.setRightPaddle(RPaddle);
		myBall.setLeftPaddle(LPaddle);
		pause(STARTDELAY);

		playerScore = 0;
		scrA.setText(playerScore.toString());					// reset player score

		agentScore = 0;
		scrA.setText(agentScore.toString());					// reset agent score
	}

	/**
	 * The mouseMoved method converts the coordinates of the mouse and changes the simulation values into real world
	 * values that can be used in the code. It then proceeds to assign those values to RPaddle so it can follow the mouse.
	 */
	public void mouseMoved(MouseEvent e) {
		if (myTable ==null || RPaddle == null) return;
		GPoint Pm = myTable.S2W(new GPoint(e.getX(), e.getY()));
		double PaddleX = RPaddle.getP().getX();
		double PaddleY = Pm.getY();
		RPaddle.setP(new GPoint (PaddleX, PaddleY));
	}

	/**
	 * The actionPerformed method is used to check if any of the buttons are pressed. It executes the action told by each button.
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Clear")) {					//check if "Clear" button is selected call clear() method if selected
			clear();
		}
		else if (command.equals("New Serve")) {			//check if "New Serve" button is selected. Call newGame() method if selected
			newGame();
		}
		else if (command.equals("Quit")) {				//check if "Quit" button is selected. End simulation if selected
			System.exit(0);
		}
		else if (command.equals("rlag")) {				//check if "rlag" button is selected. Reset lag value if selected
			lag.setValue(7);
		}
		else if (command.equals("rtime")) {				//check if"rtime" is selected. Reset time increment value if selected
			controlTick.setValue(2000);
		}
	}
}
