import java.util.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class MCGrid {
	
	protected MCBlock[][][] g;
	protected  MCPlane plane;
	
	public final int lx, ly, lz;
	
	public MCGrid(int lx, int ly, int lz){
		g = new MCBlock[this.lx=lx][this.ly=ly][this.lz=lz];
		plane = new MCPlane();
	}
	
	public MCBlock set(int x, int y, int z, MCBlock block){
		if(!ib(x,y,z)) return null;
		
		MCBlock ret = g[x][y][z];
		g[x][y][z] = block;
		return ret;
	}
	
	public MCBlock get(int x, int y, int z){
		if(!ib(x,y,z)) return null;
		
		return g[x][y][z];
	}
	
	public MCBlock get(double x, double y, double z){
		int xx = (int)(x / MCBlock.SIDE);
		int yy = (int)(y / MCBlock.SIDE);
		int zz = (int)(z / MCBlock.SIDE);
		
		return get(xx,yy,zz);
	}
	
	public boolean ib(int x, int y, int z){
		return !(x<0 || y<0 || z<0 || x>=lx || y>=ly || z>=lz);
	}
	
	protected  MCGridRender r;
	
	int[] mx = {-1,1,0,0,0,0};
	int[] my = {0,0,-1,1,0,0};
	int[] mz = {0,0,0,0,-1,1};
	int[] sides = { MCBlock.RIGHT, MCBlock.LEFT,
					MCBlock.BACK, MCBlock.FRONT,
					MCBlock.TOP, MCBlock.BOT};
	int[] oppSides = { MCBlock.LEFT, MCBlock.RIGHT,
					MCBlock.FRONT, MCBlock.BACK, 
					MCBlock.BOT, MCBlock.TOP};
	
	boolean vis[][][];
	
	public void render( MCPerson p ){
		double[] vec = p.toVec3D();
		plane.setPlane(p.x+(vec[0]*-20),p.y+(vec[1]*-20),p.z+(vec[2]*-20), vec);
		
		if(r == null){
			r = new MCGridRender();
			
			if (vis == null)
				vis = new boolean[g.length][g[0].length][g[0][0].length];
			
			int x = (int)(p.x/MCBlock.SIDE);
			int y = (int)(p.y/MCBlock.SIDE);
			int z = (int)(p.z/MCBlock.SIDE);
			if(x < 0) x = 0;
			if(x >= lx) x = lx-1;
			if(y < 0) y = 0;
			if(y >= ly) y = ly-1;
			if(z < 0) z = 0;
			if(z >= lz) z = lz-1;
			
			flood(x,y,z);
		}
		
		realRender(p, plane);
	}
	
	public void realRender( MCPerson p, MCPlane plane){
		r.render(plane, p, false);
	}
	
	public void unrender( MCBlock b ){
		
		for(MCTexturedQuad q : b.getQuads())
			r.remove(q);
		int x = (int)(b.x/MCBlock.SIDE);
		int y = (int)(b.y/MCBlock.SIDE);
		int z = (int)(b.z/MCBlock.SIDE);
		set(x,y,z, null);
		
		flood(x,y,z);
	}
	
	public void add( MCBlock b ){
		int x = (int)(b.x/MCBlock.SIDE);
		int y = (int)(b.y/MCBlock.SIDE);
		int z = (int)(b.z/MCBlock.SIDE);
		for(int i = 0; i<mx.length; i++){
			int tx = x +mx[i];
			int ty = y +my[i];
			int tz = z +mz[i];
			MCBlock bb = get(tx,ty,tz);
			if( bb == null)
				r.add( b.getQuads()[oppSides[i]] );
			else
				r.remove( bb.getQuads()[sides[i]] );
		}
		set(x,y,z, b);
	}
	
	public void flood(int x, int y, int z){
		Queue<int[]> que = new LinkedList<int[]>();
		que.add(new int[]{x,y,z});
		
		while(!que.isEmpty()){
			//iter++;
			int[] c = que.remove();
			int xx = c[0]; int yy = c[1]; int zz = c[2];
			if(g[xx][yy][zz] == null)
				for(int i = 0; i<mx.length; i++){
					int tx = xx +mx[i];
					int ty = yy +my[i];
					int tz = zz +mz[i];
	
					if(ib(tx,ty,tz)){
						if(g[tx][ty][tz] != null)
							r.add(g[tx][ty][tz].getQuads()[sides[i]]);
						if(vis[tx][ty][tz])
							continue;
						que.add(new int[]{tx,ty,tz});
						vis[tx][ty][tz] = true;
					}
				}
		}
	}
	
}
