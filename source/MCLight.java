import org.lwjgl.opengl.*;
import org.lwjgl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
import java.nio.*;


public class MCLight{
	
	private float x,y,z;
	public float str;
	
	private FloatBuffer pos, lit;
	public static final float GLOW = 1f, NORM = .45f;
	public static MCLight light;
	
	private static FloatBuffer amb;
	
	public int lightNum;
	
	static {
		amb = BufferUtils.createFloatBuffer(4);
		float ambStr = .1f;
		amb.put(ambStr).put(ambStr).put(ambStr).put(0).flip();
	}
	
	public MCLight(double x, double y, double z, int num){
		lightNum = num;
		
		light = this;
		setPos(x,y,z);
		setStr(NORM);
	}
	
	public void setStr(double s){
		str = (float)s;
		
		lit = BufferUtils.createFloatBuffer(4);
		lit.put(str).put(str).put(str).put(1).flip();
		
		glLight(lightNum, GL_SPECULAR, lit);
		glLight(lightNum, GL_AMBIENT, amb);
	}
	
	public void update(){
		glLight(lightNum, GL_POSITION, pos);
	}
	
	public void setPos(double x, double y, double z){
		this.x = (float)x;
		this.y = (float)y;
		this.z = (float)z;
		
		pos = BufferUtils.createFloatBuffer(4);
		pos.put(this.x).put(this.y).put(this.z).put(1).flip();
		
		glLight(lightNum, GL_POSITION, pos);
	}
	
	public void enable(){
		glEnable(GL_LIGHTING);
		glEnable(lightNum);
		glLightf(lightNum, GL_CONSTANT_ATTENUATION, 1f);
		//glLightf(lightNum, GL_LINEAR_ATTENUATION, 1e-4f);
		//glLightf(lightNum, GL_QUADRATIC_ATTENUATION, 1e-6f);
	}
	
}
