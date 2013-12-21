import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class MCQuad{

	protected double x1, y1, z1;
	protected double x2, y2, z2;
	protected double x3, y3, z3;
	protected double x4, y4, z4;
	
	double nx,ny,nz;
	
	public MCQuad(  double _x1, double _y1, double _z1,
					double _x2, double _y2, double _z2,
					double _x3, double _y3, double _z3,
					double _x4, double _y4, double _z4,
					double _nx, double _ny, double _nz ){

		x1 = _x1; y1 = _y1; z1 = _z1;
		x2 = _x2; y2 = _y2; z2 = _z2;
		x3 = _x3; y3 = _y3; z3 = _z3;
		x4 = _x4; y4 = _y4; z4 = _z4;
		
		nx = _nx; ny = _ny; nz = _nz;
	}
	
	public double getX(){return x1;}
	public double getY(){return y1;}
	public double getZ(){return z1;}
}