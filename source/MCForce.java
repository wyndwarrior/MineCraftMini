

public class MCForce{
	public double x,y,z;
	boolean persistant;
}

class MCGravity extends MCForce{
	public MCGravity(){
		z = - 9.8 * 2 * MCBlock.SIDE;
		persistant = true;
	}
}

class MCJump extends MCForce{
	public MCJump(){
		z = 7.6 * MCBlock.SIDE;
	}
}

class MCShadowJump extends MCForce{
	public MCShadowJump(double[] vec){
		z = 9 * MCBlock.SIDE;
		x = vec[0] * MCBlock.SIDE * 10;
		y = vec[1] * MCBlock.SIDE * 10;
	}
}

class MCBoost extends MCForce{
	public MCBoost(double[] vec){
		x = vec[0] * MCBlock.SIDE * 8;
		y = vec[1] * MCBlock.SIDE * 8;
		z = vec[2] * MCBlock.SIDE * 8;
	}
}