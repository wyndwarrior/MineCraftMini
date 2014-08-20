import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class MCWoodBlock extends MCBlock{
	public static final String WOOD = "wood.png";
	public static final String WOOD_INSIDE = "wood-inside.png";
	
	public static final String[] textures = {
		WOOD_INSIDE, WOOD, WOOD, WOOD, WOOD, WOOD_INSIDE
	};
	
	public MCWoodBlock(double x, double y, double z){
		super(x,y,z, textures);
	}
	
}
