import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class MCSandBlock extends MCBlock{
	public static final String SAND = "sand.png";
	
	public MCSandBlock(double x, double y, double z){
		super(x,y,z, SAND);
	}
	
}
