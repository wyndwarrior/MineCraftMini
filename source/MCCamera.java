import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;


public class MCCamera{
	
	public static void setCamera(  int width, int height, 
							double degx, double degz, 
							double x, double y, double z){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		float widthHeightRatio = (float) width / height;
		
		gluPerspective(45, widthHeightRatio, 0.1f, (float)(1e3 * MCBlock.SIDE));
		
		glRotated(-degz ,1,0,0);
		glRotated(-degx +90 ,0,0,1);
		glTranslated(-x,-y,-z);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
}