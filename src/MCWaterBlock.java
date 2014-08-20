import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class MCWaterBlock extends MCBlock{
	public static final String WATER = "water.png";

	public MCWaterBlock(double x, double y, double z){
		super(x,y,z, WATER);
	}

}
