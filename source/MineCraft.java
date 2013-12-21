import org.lwjgl.*;
import java.nio.*;
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
	MCPerson p;
	MCCursor cursor;

	public MineCraft(){
		super(800, 500);
		m = this;
	}

	protected static final int LX = 130, LY = LX, LZ = 100, G = LX/3;

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


		light = new MCLight( LX/2 * MCBlock.SIDE, LY/2 * MCBlock.SIDE , LZ* MCBlock.SIDE , GL_LIGHT1);
		light.enable();

		cursor = new MCCursor(this);

		g = new MCGrid(LX, LY, LZ);
		trans = new MCWaterGrid(LX, LY, LZ);
		p = new MCPerson(LX/2.*MCBlock.SIDE,LY/2.*MCBlock.SIDE, (LZ)*MCBlock.SIDE - MCPerson.HEIGHT , 0,90, this);
		//p.hideMouse();

		genMap();
		update(1);
		display();
		System.gc();
		//setFullscreen(true);

	}

	protected void genMap(){
		int[][] height = new int[LX][LY];
		//trig gen
		double HEIGHT = LX/10.;

		int[][] wavy = new int[LX][LY];
		{
			double con = HEIGHT /1.5;
			double con2 = HEIGHT/ 1.5;

			double xmanip = LX / Math.PI ;
			double ymanip = LY / Math.PI ;

			for(int k = 0; k<LX; k++){
				for(int j = 0;j<LY; j++){
					int hei = (int)((Math.sin((k)/xmanip)*con) + (Math.sin((j)/ymanip)*con2));
					//height[k][j] = hei+G;
					wavy[k][j] = hei;
				}
			}
		}


		{
			double leftXPer = 0.29;
			double leftX = leftXPer * LX;
			double rightXPer = 0.71;
			double rightX = rightXPer * LX;

			double a = HEIGHT /2. / ( LX * LX * (leftXPer * rightXPer - 0.25) );

			for(int x = 0; x<LX; x++)
			for(int y = 0; y<LY; y++)
			{
				int dz = (int) Math.min( (a * (x - leftX) * (x - rightX)),
													a * (y - leftX) * (y - rightX)) + wavy[x][y];
				if (dz< 0 ) dz/=4;
				height[x][y] = G + dz;
			}
		}

		//grass, and ground
		for(int x = 0; x<LX; x++)
		for(int y = 0; y<LY; y++)
		{

			if( height[x][y] > G +4){
				g.set(x, y, height[x][y], loadBlock('g',x,y,height[x][y]));
				for(int z = height[x][y]-1; z>=0; z--)
					g.set(x, y, z, loadBlock('d', x,y,z));
			}else{
				for(int z = height[x][y]; z>=0 && z > height[x][y]-10; z--)
					g.set(x, y, z, loadBlock('s', x,y,z));
				for(int z = height[x][y]-10; z>=0; z--)
					g.set(x, y, z, loadBlock('d', x,y,z));
			}
		}

		//add water
		for(int x = 0; x<LX; x++)
		for(int y = 0; y<LY; y++)
		{
			for(int z = G; z>height[x][y]; z--)
				trans.set(x, y, z, loadBlock('w', x,y,z));
		}

		//trans.set(LX/2, LY/2, LZ-20 ,loadBlock('w', LX/2, LY/2, LZ-20));

	}

	public static MCBlock loadBlock(char c, int x, int y, int z){
		switch(c){
			case 'g' : return new MCGrassBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
			case 'd' : return new MCDirtBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
			case 'w' : return new MCWaterBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
			case 'o' : return new MCWoodBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
			case 'l' : return new MCLeavesBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
			case 's' : return new MCSandBlock(x *MCBlock.SIDE, y*MCBlock.SIDE, z*MCBlock.SIDE);
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
		if( isKeyDown( KEY_W ) )
			p.move(delta * moveConst);
		if( isKeyDown( KEY_A ) )
			p.moveLeft(delta * moveConst);
		if( isKeyDown( KEY_S ) )
			p.moveBack(delta * moveConst);
		if( isKeyDown( KEY_D ) )
			p.moveRight(delta * moveConst);
		if( isKeyDown( KEY_SPACE )){
			if( p.inWater() ){
				p.jump( delta* moveConst);
			}else if (  time - keyTime[KEY_SPACE] >  100 ){
				p.jump(0 );
				keyTime[KEY_SPACE] = time;
			}
		}
		if( isKeyDown( KEY_V ) )
			p.boost();

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