import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class MCGrassBlock extends MCBlock{
	public static final String DIRT = "dirt.png";
	public static final String GRASS = "grass.png";
	public static final String GRASS_SIDE = "grass-side.png";
	
	public static final String[] textures = {
		DIRT, GRASS_SIDE, GRASS_SIDE, GRASS_SIDE, GRASS_SIDE, GRASS
	};
	
	public MCGrassBlock(double x, double y, double z){
		super(x,y,z, textures);
	}
	
}
