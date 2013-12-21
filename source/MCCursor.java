import org.lwjgl.opengl.*;
import org.newdawn.slick.*;
import static org.lwjgl.opengl.GL11.*;

public class MCCursor{
	MCWindow w;
	public MCCursor( MCWindow w ){
		this.w = w;
	}
	public void render(){
		
		int width = w.getWidth();
		int height = w.getHeight();
		
		glMatrixMode(GL11.GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, 0, height, 1, -1);
		glMatrixMode(GL11.GL_MODELVIEW);
		
		glColor4d(1,1,1,.8f);
		
		int mx = width/2;
		int my = height/2;
		int lenShort = 1;
		int lenLong = 15;
		
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_LIGHTING);
		
		glBegin(GL_QUADS);
			glVertex2f(mx-lenShort,my-lenLong);
			glVertex2f(mx-lenShort,my+lenLong);
			glVertex2f(mx+lenShort,my+lenLong);
			glVertex2f(mx+lenShort,my-lenLong);
			
			glVertex2f(mx-lenLong,my-lenShort);
			glVertex2f(mx-lenLong,my+lenShort);
			glVertex2f(mx+lenLong,my+lenShort);
			glVertex2f(mx+lenLong,my-lenShort);
		glEnd();
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_LIGHTING);
	}
}