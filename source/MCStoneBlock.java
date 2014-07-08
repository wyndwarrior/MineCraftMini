import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class MCStoneBlock extends MCBlock{
	public static final String STONE = "stone.png";
	
	public MCStoneBlock(double x, double y, double z){
		super(x,y,z, STONE);
	}
	
}
