package gridWorld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import burlap.oomdp.core.states.State;
import burlap.oomdp.visualizer.StaticPainter;

public class WallPainter implements StaticPainter {
	
	private final GridWorldDomain domain;
	
	public WallPainter(GridWorldDomain domain) {
		this.domain = domain;
	}

	@Override
	public void paint(Graphics2D g2, State s, float cWidth, float cHeight) {

		//walls will be filled in black
		g2.setColor(Color.BLACK);

		//set up floats for the width and height of our domain
		float fWidth = domain.map.length;
		float fHeight = domain.map[0].length;

		//determine the width of a single cell
		//on our canvas such that the whole map can be painted
		float width = cWidth / fWidth;
		float height = cHeight / fHeight;

		//pass through each cell of our map and if it's a wall, paint a black rectangle on our
		//cavas of dimension widthxheight
		for(int i = 0; i < domain.map.length; i++){
			for(int j = 0; j < domain.map[0].length; j++){

				//is there a wall here?
				if(domain.map[i][j] == 1){

					//left coordinate of cell on our canvas
					float rx = i*width;

					//top coordinate of cell on our canvas
					//coordinate system adjustment because the java canvas
					//origin is in the top left instead of the bottom right
					float ry = cHeight - height - j*height;

					//paint the rectangle
					g2.fill(new Rectangle2D.Float(rx, ry, width, height));

				}
			}
		}
	}


}