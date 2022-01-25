package ppPackage;

import static ppPackage.ppSimParams.*;
import java.awt.Color;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

/**
 * The goal of the ppTable class is to reduce the clutter found in the ppSim class by
 * creating the floor and the walls in a separate class that will later be imported into the ppSim class.
 * 
 * @author Kristian Myzeqari
 * The following code contains lines taken from the assignment 4 handout written by Frank Ferrie.
 * The following code contains lines taken from the tutorials given by Katrina Poulin.
 * The following code contains lines taken from the ppTable (assignment 3)  written by Kristian Myzeqari.
 */

public class ppTable{

	private GraphicsProgram GProgram;

	/**
	 * The constructor for the ppTable class copies parameters to instance variables, creates an
	 * instance of GPlane and GRect to represent the floor and the walls, and adds them to the display.
	 *
	 * @param GProgram - a reference to the ppSim class used to manage the display
	 */

	public ppTable(GraphicsProgram GProgram) {
		this.GProgram = GProgram;
		drawGroundline();
		GRect gPlane = new GRect(0, HEIGHT, WIDTH+OFFSET, 3);		//set up the coordinates and dimensions of the floor
		gPlane.setColor(Color.BLACK);								//set the color of the floor to black
		gPlane.setFilled(true);										//fill up the inside of the rectangle forming the floor
		GProgram.add(gPlane);	
	}


	/***
	 * Method to convert from world to screen coordinates.
	 * @param P a point object in world coordinates
	 * @return p the corresponding point object in screen coordinates
	 */


	public GPoint W2S (GPoint P) {
		return new GPoint((P.getX()-Xmin)*Xs,ymax-(P.getY()-Ymin)*Ys);
	}

	/**
	 * Method to convert from screen to world coordinates
	 * @param p a point object in screen coordinates
	 * @return p the corresponding point object in world coordinates
	 */

	public GPoint S2W (GPoint p) {
		return new GPoint((p.getX()/Xs)+Xmin, (ymax - p.getY())/Ys + Ymin);
	}

	/**
	 * Erase all objects on the display except the buttons and draws a new ground plane.
	 */
	public void newScreen() {
		GProgram.removeAll();
	}

	/**
	 * drawGroundline method is used to graph the floor of the table for the simulation.
	 */
	public void drawGroundline() {
		//generate floor
		GRect gPlane = new GRect(0, HEIGHT, WIDTH+OFFSET, 3);		//set up the coordinates and dimensions of the floor
		gPlane.setColor(Color.BLACK);								//set the color of the floor to black
		gPlane.setFilled(true);										//fill up the inside of the rectangle forming the floor
		GProgram.add(gPlane);										//add the floor to the program
	}
}
