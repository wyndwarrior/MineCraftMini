import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class MCLeavesBlock extends MCBlock{
	public static final String LEAVES = "leaf.png";
	
	public MCLeavesBlock(double x, double y, double z){
		super(x,y,z, LEAVES);
	}
	
}
