import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class MCBlock{

	protected MCTexturedQuad [] q = new MCTexturedQuad[6];
	public  double x, y,z;
	
	public static Texture[] breaks = new Texture[10];
	public static void init(){
		for(int i = 1; i<=10; i++)
			breaks[i-1] = MCTextureLoader.getTexture("break" + i +".png");
	}
	
	protected double work;
	protected Texture curBreak;

	public static final double SIDE = 16;

	public static final int BOT = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int FRONT = 3;
	public static final int BACK = 4;
	public static final int TOP = 5;

	public MCBlock(double x, double y, double z, String texture){
		this.x = x; this.y = y; this.z = z;
		q[BOT] = 	new MCTexturedQuad(this, BOT, texture);
		q[LEFT] = 	new MCTexturedQuad(this, LEFT, texture);
		q[RIGHT] = 	new MCTexturedQuad(this, RIGHT, texture);
		q[FRONT] = 	new MCTexturedQuad(this, FRONT, texture);
		q[BACK] = 	new MCTexturedQuad(this, BACK, texture);
		q[TOP] = 	new MCTexturedQuad(this, TOP, texture);

	}

	public MCBlock(double x, double y, double z, String[] t){
		this.x = x; this.y = y; this.z = z;
		int i = 0;
		q[BOT] = 	new MCTexturedQuad(this, BOT, t[i++]);
		q[LEFT] = 	new MCTexturedQuad(this, LEFT, t[i++]);
		q[RIGHT] = 	new MCTexturedQuad(this, RIGHT, t[i++]);
		q[FRONT] = 	new MCTexturedQuad(this, FRONT, t[i++]);
		q[BACK] = 	new MCTexturedQuad(this, BACK, t[i++]);
		q[TOP] = 	new MCTexturedQuad(this, TOP, t[i++]);

	}

	public void glow(boolean glow){
		for(MCTexturedQuad qq : q)
			qq.glow = glow;
		if( !glow )
			setWork(0);
	}

	/*public void render(){
		for( MCTexturedQuad qq : q )
			qq.render();
	}*/
	public static final double E = 1e-2f;
	
	public void render( int side ){
		switch(side){
			case BOT:
				glNormal3d(0,0,-1);
				glTexCoord2f(0,0);
				glVertex3d(x+SIDE,y+SIDE,z);
				glTexCoord2f(1,0);
				glVertex3d(x,y+SIDE,z);
				glTexCoord2f(1,1);
				glVertex3d(x,y,z);
				glTexCoord2f(0,1);
				glVertex3d(x+SIDE,y,z);
				if( curBreak != null ){
					glEnd();
					curBreak.bind();
					glBegin(GL_QUADS);
					
					glNormal3d(0,0,-1);
					glTexCoord2f(0,0);
					glVertex3d(x+SIDE,y+SIDE,z-E);
					glTexCoord2f(1,0);
					glVertex3d(x,y+SIDE,z-E);
					glTexCoord2f(1,1);
					glVertex3d(x,y,z-E);
					glTexCoord2f(0,1);
					glVertex3d(x+SIDE,y,z-E);
					
					glEnd();
					q[side].bind();
					glBegin(GL_QUADS);
					
				}
				break;
			case LEFT:
				glNormal3d(-1,0,0);
				glTexCoord2f(0,0);
				glVertex3d(x,y+SIDE,z+SIDE);
				glTexCoord2f(1,0);
				glVertex3d(x,y,z+SIDE);
				glTexCoord2f(1,1);
				glVertex3d(x,y,z);
				glTexCoord2f(0,1);
				glVertex3d(x,y+SIDE,z);
				if( curBreak != null ){
					glEnd();
					curBreak.bind();
					glBegin(GL_QUADS);
					
					glNormal3d(-1,0,0);
					glTexCoord2f(0,0);
					glVertex3d(x-E,y+SIDE,z+SIDE);
					glTexCoord2f(1,0);
					glVertex3d(x-E,y,z+SIDE);
					glTexCoord2f(1,1);
					glVertex3d(x-E,y,z);
					glTexCoord2f(0,1);
					glVertex3d(x-E,y+SIDE,z);
					
					glEnd();
					q[side].bind();
					glBegin(GL_QUADS);
					
				}
				break;
			case RIGHT:
				glNormal3d(1,0,0);
				glTexCoord2f(0,0);
				glVertex3d(x+SIDE,y,z+SIDE);
				glTexCoord2f(1,0);
				glVertex3d(x+SIDE,y+SIDE,z+SIDE);
				glTexCoord2f(1,1);
				glVertex3d(x+SIDE,y+SIDE,z);
				glTexCoord2f(0,1);
				glVertex3d(x+SIDE,y,z);
				if( curBreak != null ){
					glEnd();
					curBreak.bind();
					glBegin(GL_QUADS);
					
					glNormal3d(1,0,0);
					glTexCoord2f(0,0);
					glVertex3d(x+SIDE+E,y,z+SIDE);
					glTexCoord2f(1,0);
					glVertex3d(x+SIDE+E,y+SIDE,z+SIDE);
					glTexCoord2f(1,1);
					glVertex3d(x+SIDE+E,y+SIDE,z);
					glTexCoord2f(0,1);
					glVertex3d(x+SIDE+E,y,z);
					
					glEnd();
					q[side].bind();
					glBegin(GL_QUADS);
					
				}
				break;
			case FRONT:
				glNormal3d(0,-1,0);
				glTexCoord2f(0,0);
				glVertex3d(x,y,z+SIDE);
				glTexCoord2f(1,0);
				glVertex3d(x+SIDE,y,z+SIDE);
				glTexCoord2f(1,1);
				glVertex3d(x+SIDE,y,z);
				glTexCoord2f(0,1);
				glVertex3d(x,y,z);
				if( curBreak != null ){
					glEnd();
					curBreak.bind();
					glBegin(GL_QUADS);
					
					glNormal3d(0,-1,0);
					glTexCoord2f(0,0);
					glVertex3d(x,y-E,z+SIDE);
					glTexCoord2f(1,0);
					glVertex3d(x+SIDE,y-E,z+SIDE);
					glTexCoord2f(1,1);
					glVertex3d(x+SIDE,y-E,z);
					glTexCoord2f(0,1);
					glVertex3d(x,y-E,z);
					
					glEnd();
					q[side].bind();
					glBegin(GL_QUADS);
					
				}
				break;
			case BACK:
				glNormal3d(0,1,0);
				glTexCoord2f(0,0);
				glVertex3d(x+SIDE,y+SIDE,z+SIDE);
				glTexCoord2f(1,0);
				glVertex3d(x,y+SIDE,z+SIDE);
				glTexCoord2f(1,1);
				glVertex3d(x,y+SIDE,z);
				glTexCoord2f(0,1);
				glVertex3d(x+SIDE,y+SIDE,z);
				if( curBreak != null ){
					glEnd();
					curBreak.bind();
					glBegin(GL_QUADS);
					
					glNormal3d(0,1,0);
					glTexCoord2f(0,0);
					glVertex3d(x+SIDE,y+SIDE+E,z+SIDE);
					glTexCoord2f(1,0);
					glVertex3d(x,y+SIDE+E,z+SIDE);
					glTexCoord2f(1,1);
					glVertex3d(x,y+SIDE+E,z);
					glTexCoord2f(0,1);
					glVertex3d(x+SIDE,y+SIDE+E,z);
					
					glEnd();
					q[side].bind();
					glBegin(GL_QUADS);
					
				}
				break;
			case TOP:
                double zz = (this instanceof MCWaterBlock) ? -SIDE * 0.1:0;
				glNormal3d(0,0,1);
				glTexCoord2f(0,0);
				glVertex3d(x,y+SIDE,z+SIDE+zz);
				glTexCoord2f(1,0);
				glVertex3d(x+SIDE,y+SIDE,z+SIDE+zz);
				glTexCoord2f(1,1);
				glVertex3d(x+SIDE,y,z+SIDE+zz);
				glTexCoord2f(0,1);
				glVertex3d(x,y,z+SIDE+zz);
				if( curBreak != null ){
					glEnd();
					curBreak.bind();
					glBegin(GL_QUADS);
					
					glNormal3d(0,0,1);
					glTexCoord2f(0,0);
					glVertex3d(x,y+SIDE,z+SIDE+E);
					glTexCoord2f(1,0);
					glVertex3d(x+SIDE,y+SIDE,z+SIDE+E);
					glTexCoord2f(1,1);
					glVertex3d(x+SIDE,y,z+SIDE+E);
					glTexCoord2f(0,1);
					glVertex3d(x,y,z+SIDE+E);
					
					glEnd();
					q[side].bind();
					glBegin(GL_QUADS);
					
				}
				break;
		}
	}


	boolean[] water = new boolean[6];
	boolean[] back = new boolean[6];

	public void setWater( int side, boolean water ){
		this.water[side] = water;
	}
	public void renderBack( int side, boolean back ){
		this.back[side] = back;
	}

	public boolean getWater( int side ){
		return water[side];
	}
	public boolean getBack( int side ){
		return back[side];
	}

	public MCTexturedQuad[] getQuads(){
		return q;
	}
	
	public static final double REQ_WORK = 40;
	
	public double reqWork(){
		return REQ_WORK;
	}
	
	public double getWork(){
		return work;
	}
	
	public void setWork( double work ){
		this.work = work;
		
		int per = ( (int)Math.ceil(work/ reqWork() * breaks.length) );
		if( per == 0 ){
			curBreak = null;
			return;
		}
		if( per > breaks.length) per = breaks.length;
		
		curBreak = breaks[per-1];
		
	}
	
	public boolean dead(){
		return work >= reqWork();
	}
	
}