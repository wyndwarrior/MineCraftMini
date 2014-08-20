import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;
import java.io.*;
import java.util.*;
import java.nio.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class MCPerson{
	
	private MineCraft window;
	private MCGrid g;
	private MCWaterGrid trans;
	
	private double degx, degz;
	public double x, y, z;
	public static MCPerson p;
	
	public MCPerson(double x, double y, double z, double degx, double degz, MineCraft window){
		this.x = x;
		this.y = y;
		this.z = z;
		this.degx = degx;
		this.degz = degz;
		this.window = window;
		g = window.g;
		trans = window.trans;
		
		forces.add(new MCGravity());
		p = this;
	}
	
	public void setCamera(){
		MCCamera.setCamera(window.getWidth(), window.getHeight(), degx, degz, x,y,z);
	}
	
	/* Mouse Handling */
	
	public void resetCapture(){
		resetMouse();
		Mouse.getDX();
		Mouse.getDY();
	}
	public void captureMovement(  ){
		
		if(!Mouse.isGrabbed()) return;
		
		int dx = Mouse.getDX(), dy = Mouse.getDY();
		resetMouse();
		
		degx -= .15 * dx;
		degz += .15 * dy;
		
		if(degx < 0) degx += 360;
		if(degz < 0) degz = 0;
		if(degx >= 360) degx -= 360;
		if(degz > 180) degz = 180;
		
	}

	public void resetMouse(){
		Mouse.setCursorPosition( window.getWidth()/2, window.getHeight()/2 );
		//Mouse.getDX();Mouse.getDY();
	}
	
	private static Cursor HIDDEN_MOUSE =null;
	
	static {
		try{
			HIDDEN_MOUSE = new Cursor(1,1,0,0,1,BufferUtils.createIntBuffer(16*16),null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static final Cursor ORIG_MOUSE = null;
	
	public void hideMouse(){
		
		Mouse.setGrabbed(true);
		
		try{
		Mouse.setNativeCursor(HIDDEN_MOUSE);
		}catch(Exception e){e.printStackTrace();}
		resetCapture();
	}
	public void showMouse(){
		
		Mouse.setGrabbed(false);
		
		try{
		Mouse.setNativeCursor(ORIG_MOUSE);
		}catch(Exception e){e.printStackTrace();}
	}
	
	
	/* Movement */
	
	public double[] toVec2D(){
		return new double[]{Math.cos(Math.toRadians( degx )),
							Math.sin(Math.toRadians( degx ))};
	}
	
	public double[] toVec3D(){
		double cosA = Math.sin(Math.toRadians( degz ));
		return new double[]{Math.cos(Math.toRadians( degx )) * cosA,
							Math.sin(Math.toRadians( degx )) * cosA,
							Math.cos(Math.toRadians( degz )) * -1  };
	}
	
	public void move( double dist ){
		
		if ( inWater()) dist /= 2;
		
		double[] vec = toVec2D();
		double dx = vec[0] * dist,
			dy = vec[1] * dist;
		
		x += dx;
		y += dy;
		
		if(! collide()) return;
		
		x -= dx;
		if(! collide()) return;
		
		x += dx;
		y -= dy;
		
		if(! collide()) return;
		
		x -= dx;
		
	}
	
	public void moveLeft( double dist ){
		degx += 90;
		move(dist);
		degx -= 90;
	}
	
	public void moveRight( double dist ){
		degx -= 90;
		move(dist);
		degx += 90;
	}
	
	public void moveBack( double dist ){
		degx += 180;
		move(dist);
		degx -= 180;
	}
	
	/* Physics */
	
	public static final double HEIGHT = MCBlock.SIDE * 1.6;
	
	public boolean collide(){
		return collide(x,y,z);
	}
	
	public boolean collide(double x, double y, double z){

		double dif = MCBlock.SIDE/3;
		double[] points = { x-dif,y+dif,z,
							x-dif,y-dif,z,
							x+dif,y+dif,z,
							x+dif,y-dif,z,

							x-dif,y+dif,z - HEIGHT,
							x-dif,y-dif,z - HEIGHT,
							x+dif,y+dif,z - HEIGHT,
							x+dif,y-dif,z - HEIGHT,
							
							x-dif,y+dif,z - HEIGHT/2,
							x-dif,y-dif,z - HEIGHT/2,
							x+dif,y+dif,z - HEIGHT/2,
							x+dif,y-dif,z - HEIGHT/2 };

		for(int i = 0; i<points.length; i+=3){
			double xx = points[i];
			double yy = points[i+1];
			double zz = points[i+2];

			if(g.get(xx,yy,zz) != null || !ib(xx,yy,zz))
				return true;
		}

		return false;
	}
	
	public boolean ib( double x, double y, double z ){
		return g.ib((int)(x/MCBlock.SIDE), (int)(y/MCBlock.SIDE), (int)(z/MCBlock.SIDE));
	}
	
	private double vx,vy,vz;
	public ArrayList<MCForce> forces = new ArrayList<MCForce>();

	public void applyForces(int delta){
		
		double t = delta / 1000.;
		
		for(int i = 0; i<forces.size(); i++){
			MCForce f = forces.get(i);
			if(f.persistant){
				vx += f.x * t;
				vy += f.y * t;
				vz += f.z * t;
			}else{
				vx += f.x;
				vy += f.y;
				vz += f.z;
				forces.remove(i--);
			}
		}

		double side = MCBlock.SIDE;
		
		//System.out.println (vz);
		if( inWater() ) vz  = -1.7 * MCBlock.SIDE;
		
		if(vz < 0 ){
			double goal = z+vz * t;
			for( ; z > goal && !collide(); z -= side);
			if( z < goal ? collide(x,y, goal) : collide() ){
				z += side;
				z = Math.floor( (z-HEIGHT) / side + 1e-8) * side + HEIGHT + 1e-8;
				vz = 0;
			}else
				z = goal;
		}else{
			if(!collide(x,y,z + vz * t))
				z += vz * t;
			else
				vz = 0;
		}
		
		if(!collide(x,y + vy * t,z))
				y += vy * t;
			else
				vy = 0;
		if(!collide(x + vx * t,y,z))
				x += vx * t;
			else
				vx = 0;
		

		vz *= 1-(.15*t);
		vx *= 1-(t*2);
		vy *= 1-(t*2);
	}
	
	public void jump( double delta ){
		if( ! inWater()){
			if( hasBelow() )
				forces.add(new MCJump());
		}else{
			delta /= 1.7;
			if( !collide(x,y,z+delta))
				z+= delta;
			if( !inWater() ){
				forces.add(new MCJump());
				vz = 0;
			}
		}
	}
	
	public void shadowJump(){
		forces.add(new MCShadowJump(toVec2D()));
	}
	public void boost(){
		forces.add(new MCBoost(toVec3D()));
	}
	
	public boolean hasBelow(){
		return vz==0 &&  collide(x,y,z-.02*MCBlock.SIDE);
	}
	
	private final int precision = 100;
	private final int dist = 5;
	
	public double[] sight(double[] vec, double x, double y, double z){
		for(int i = 0; i<precision; i++){
			x += vec[0];
			y += vec[1];
			z += vec[2];

			if( g.get(x,y,z) != null )
				return new double[]{x,y,z};
		}
		return null;
	}
	
	private MCBlock selected;
	
	public void updateSelected(){

		double[] vec = toVec3D();
		double mult = (double)dist/precision * MCBlock.SIDE;
		for(int i = 0; i<vec.length; i++)
			vec[i] *= mult;

		double[] si = sight(vec,x,y,z);

		if(si != null && selected != g.get(si[0],si[1],si[2])){
			if( selected != null )
				selected.glow(false);
			selected = g.get(si[0],si[1],si[2]);
			selected.glow(true);
		}else if( si == null && selected != null ){
			selected.glow(false);
			selected = null;
		}
		
		//System.out.println (selected);
		
	}
	
	public void place(char c){
		double[] vec = toVec3D();
		double mult = (double)dist/precision * MCBlock.SIDE;
		for(int i = 0; i<vec.length; i++)
			vec[i] *= mult;
		double[] si = sight(vec,x,y,z);
		
		if( si != null){
			int x = (int)((si[0] - vec[0])/MCBlock.SIDE);
			int y = (int)((si[1] - vec[1])/MCBlock.SIDE);
			int z = (int)((si[2] - vec[2])/MCBlock.SIDE);
			
			MCBlock b;
			g.add( b = window.loadBlock(c, x, y, z) );
			
			if( trans.get(x,y,z) != null )
				trans.unrender(trans.get(x,y,z));
			
			if(collide())
				g.unrender(b);
		}
	}
	
	public void remove(){
		if( selected != null ){
			g.unrender(selected);
			selected = null;
		}
	}
	
	public void work( double delta ){
		if( selected != null ){
			if( selected.dead() )
				remove();
			else
				selected.setWork(selected.getWork() + delta);
		}
	}
	
	public void resetSelected(){
		if( selected != null)
			selected.setWork(0);
	}
	
	/* Water */
	
	public boolean inWater(){
		
		if( underWater() ) return true;

		double dif = MCBlock.SIDE/3;
		double[] points = { x-dif,y+dif,z - HEIGHT/2,
							x-dif,y-dif,z - HEIGHT/2,
							x+dif,y+dif,z - HEIGHT/2,
							x+dif,y-dif,z - HEIGHT/2 };

		for(int i = 0; i<points.length; i+=3){
			double xx = points[i];
			double yy = points[i+1];
			double zz = points[i+2];

			if(trans.get(xx,yy,zz) instanceof MCWaterBlock  || !ib(xx,yy,zz))
				return true;
		}

		return false;
	}
	
	public boolean underWater(){
		double dif = MCBlock.SIDE/3;
		double[] points = { x-dif,y+dif,z,
							x-dif,y-dif,z,
							x+dif,y+dif,z,
							x+dif,y-dif,z};

		for(int i = 0; i<points.length; i+=3){
			double xx = points[i];
			double yy = points[i+1];
			double zz = points[i+2];

			if(trans.get(xx,yy,zz) instanceof MCWaterBlock  || !ib(xx,yy,zz))
				return true;
		}

		return false;
	}
	
	public static boolean inWaterS(){
		return p.inWater();
	}
	
	public static boolean underWaterS(){
		return p.underWater();
	}
	
}