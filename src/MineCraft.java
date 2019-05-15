import org.lwjgl.*;
import java.nio.*;
import java.util.*;
import org.lwjgl.input.*;
import static org.lwjgl.input.Keyboard.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class MineCraft extends MCWindow {

	static MineCraft m;

	MCLight light;
	public MCGrid g;
	public MCWaterGrid trans;
	private MCPerson p;
	MCCursor cursor;
    MCMapGen gen;

	public MineCraft(){
		super(800, 500);
		m = this;
	}

	protected static final int LX = 120, LY = LX, LZ = 100;

	protected void init() {

		MCBlock.init();

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        float clear = 0f;
		glClearColor(clear, clear, clear, 1);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST );
		//glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST );
		//glEnable( GL_POLYGON_SMOOTH );
		glEnable(GL_CULL_FACE);
		glFrontFace(GL_CW);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		/*glFogi (GL_FOG_MODE, GL_LINEAR );
		FloatBuffer fb = BufferUtils.createFloatBuffer(4);
		fb.put(0).put(0).put(0).put(1).flip();
		glFog(GL_FOG_COLOR, fb);
		glFogf(GL_FOG_DENSITY, .1f);
		glHint(GL_FOG_HINT, GL_DONT_CARE);
		glFogf(GL_FOG_START, (float)MCBlock.SIDE * ((LZ-G)*2/3+G) );
		glFogf(GL_FOG_END, (float)MCBlock.SIDE * (LZ*1.33f));
		glEnable (GL_FOG);*/

		//MCMaterial.setMaterial( MCMaterial.NORM );
        
        gen = new MCMapGen(LX, LY, LZ);
        genMap();
        
		light = new MCLight( LX/2 * MCBlock.SIDE, LY/2 * MCBlock.SIDE , LZ* MCBlock.SIDE , GL_LIGHT1);
		light.enable();

		cursor = new MCCursor(this);

		//p.hideMouse();
        
		update(1);
		display();
		//System.gc();
		//setFullscreen(true);

	}
    
    public void genMap(){
        
        /*double GL = 0.1 + 0.4*Math.random();
        double PE = 0.25 + 0.5*Math.random();
        int OCT = (int)(2+20*Math.random());
        
        System.out.println(GL + " " + PE + " " + OCT);*/
        
        //, (int)(LZ*GL), 7, 0.4);
		gen.genMap();
        g = gen.getGrid();
        trans = gen.getWater();
		trans.g2 = g;
        
        p = new MCPerson(LX/2.*MCBlock.SIDE,LY/2.*MCBlock.SIDE, (LZ)*MCBlock.SIDE - MCPerson.HEIGHT , 0,90, this);

    }


	public static MCBlock loadBlock(char c, int x, int y, int z){
		switch(c){
			case 'g' : return new MCGrassBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
			case 'd' : return new MCDirtBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
			case 'w' : return new MCWaterBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
			case 'o' : return new MCWoodBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
			case 'l' : return new MCLeavesBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
			case 's' : return new MCSandBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
			case 't' : return new MCStoneBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
		}
		return null;
	}

	long[] keyTime = new long[256];
	boolean mouse;
	protected void update(int delta){
		long time = MCTimer.getTime();

		if( isKeyDown( KEY_ESCAPE ) ){
			if(mouse)
				p.showMouse();
			mouse = false;
		}

		double moveConst = 5e-3 * MCBlock.SIDE;
		switch (isKeyDown){

		case 'KEY_W':
			p.move(delta * moveConst);
		case 'KEY_A':
			p.moveLeft(delta * moveConst);
		case 'KEY_S':
			p.moveBack(delta * moveConst);
		case 'KEY_D':
			p.moveRight(delta * moveConst);
		case 'KEY_SPACE':
			if( p.inWater() ){
				p.jump( delta* moveConst);
			}else if (  time - keyTime[KEY_SPACE] >  100 ){
				p.jump(0 );
				keyTime[KEY_SPACE] = time;
			}
		}
        if( isKeyDown( KEY_M ) ){
            genMap();
        }
		if( isKeyDown( KEY_V ) )
			p.shadowJump();

		while( Mouse.next() ){
			int btn = Mouse.getEventButton();
			if(btn == 0){
				if(!mouse){
					mouse = true;
					p.hideMouse();
				}
			}
			if(btn == 1 && Mouse.getEventButtonState()){
				p.place('d');
			}
		}

		while( Keyboard.next() ){
			if( Keyboard.getEventKey() == KEY_F && !Keyboard.getEventKeyState()){
				setFullscreen(!isFullscreen());
			}
		}

		if( Mouse.isButtonDown(0))
			p.work(delta * moveConst);
		else
			p.resetSelected();

		p.captureMovement();
		p.applyForces(delta);
		p.updateSelected();
	}
	public void display() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glColor4d(0,1,0,1);

		p.setCamera();
		light.setPos(p.x,p.y, 2 * LZ * MCBlock.SIDE);
		//light.update();
 		g.render(p);
 		trans.render(p);
		trans.floodWater();
		cursor.render();
	}

	public static void main(String[] argv) {
		final MineCraft m = new MineCraft();

		m.start();

		/*new Thread(new Runnable(){public void run(){m.start();}}).start();
		System.out.println(m.getHeight() + " " + m.getWidth());
		m.setFullscreen(true);
		System.out.println(m.getHeight() + " " + m.getWidth());*/
	}
}