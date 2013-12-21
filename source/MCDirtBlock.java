import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class MCDirtBlock extends MCBlock{
	public static final String DIRT = "dirt.png";
	
	public MCDirtBlock(double x, double y, double z){
		super(x,y,z, DIRT);
	}
	
}
