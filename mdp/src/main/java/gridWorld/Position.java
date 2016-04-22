package gridWorld;

import java.io.Serializable;

public class Position implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private final int x;
	private final int y;
	
	private boolean wall = false;
	private boolean end = false;
	private Position jumpPosition;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Position(int x, int y, Position jumpPosition) {
		this(x,y);
		this.jumpPosition = jumpPosition;
	}
	
	public Position setWall(boolean wall) {
		this.wall = wall;
		return this;
	}
	
	public Position setEnd(boolean end) {
		this.end = end;
		return this;
	}
	
	public boolean isWall() {
		return wall;
	}
	public boolean isJump() {
		return jumpPosition != null;
	}
	public Position getJump() {
		return jumpPosition;
	}
	
	public boolean isEnd() {
		return end;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	private String getXY() {
		StringBuilder sb = new StringBuilder("[x,y]=[").append(x).append(",").append(y).append("]");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getXY());
		if (isJump()) sb.append(", jump to ").append(jumpPosition.getXY());
		if (isWall()) sb.append(", wall");
		if (isEnd()) sb.append(", end");
		return sb.toString();
	}

}
