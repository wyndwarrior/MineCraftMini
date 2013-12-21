import java.util.*;

public class MCPlane{
	
	public double[] norm;
	public double x,y,z;
	double dist;
	
	public void setPlane(double x, double y, double z, double[] n){
		this.x = x;
		this.y = y;
		this.z = z;
		norm = n;
	}
	
	public boolean test(double xx, double yy, double zz){
		return  norm[0] * (xx-x) + 
				norm[1] * (yy-y) +
				norm[2] * (zz-z) >= 0;
	}
	
	/*public static void main (String[] args) {
		MCPlane p = new MCPlane(0,0,0, new double[]{0,1,0});
		System.out.println (p.test( 0,1,0 ));
		System.out.println (p.test( 0,1,1 ));
		System.out.println (p.test( 1,1,1 ));
		System.out.println (p.test( 1,-1,1 ));
		System.out.println (p.test( 1,-1,-1 ));
		System.out.println (p.test( 0,-1,0 ));
	}*/
	
}
