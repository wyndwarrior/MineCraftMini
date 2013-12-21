import org.lwjgl.opengl.*;
import org.lwjgl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
import java.nio.*;
import java.util.*;

public class MCMaterial{

	protected static int PRECISION = 20;
	public static FloatBuffer[] cache = new FloatBuffer[PRECISION];
	public static FloatBuffer[] wcache = new FloatBuffer[PRECISION];
	protected static double[] vals = new double[PRECISION];
	protected static double NORM = 1;
	
	public static FloatBuffer cursorBuf = create(1,1,1);

	static{
		double d = NORM/ PRECISION;
		double x = 0;
		for(int i = 0; i<PRECISION; i++){
			float s = (float)x;
			
			vals[i] = x;
			cache[i] = create(s,s,s);
			wcache[i] = create(s/2,s/2,s);
			x+=d;
		}
	}
	
	public static FloatBuffer create(float f1, float f2, float f3){
		FloatBuffer buf = BufferUtils.createFloatBuffer(4);
		buf.put(f1).put(f2).put(f3).put(1).flip();
		return buf;
	}

	public static FloatBuffer findBuf( double str, boolean water ){
		int x = Arrays.binarySearch( vals , str );
		if( x < 0)
			x = -x-1;
		if(x >= cache.length)
			x = cache.length-1;
		if( water )
			return wcache[x];
		else
			return cache[x];

	}

	public static void setMaterial( double str ){
		setBuf(findBuf(str, false));
	}

	public static void setBuf( FloatBuffer buf ){
		glMaterial(GL_FRONT, GL_SPECULAR, buf);
		glMaterial(GL_FRONT, GL_AMBIENT, buf);
		glMaterialf(GL_FRONT, GL_SHININESS, 0.1f);
	}

}