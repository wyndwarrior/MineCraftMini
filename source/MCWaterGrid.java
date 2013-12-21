import java.util.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class MCWaterGrid extends MCGrid {


	boolean[][][] vis;
	MCGrid g2;

	MCTimer timer = new MCTimer();

	public MCWaterGrid(int lx, int ly, int lz){
		super(lx,ly,lz);
		vis  = new boolean[lx][ly][lz];
		g2 = MineCraft.m.g;
	}

	public void floodWater (  ){

		if(timer.getDelta()< 1000/3)
			return;
		timer.update();
		//vis  = new boolean[lx][ly][lz];

		ArrayList<MCBlock> add = new ArrayList<MCBlock>();

		for(int x = 0; x<lx; x++)
		for(int y = 0; y<ly; y++)
		for(int z = 0; z<lz; z++)
			if( g[x][y][z] instanceof MCWaterBlock ){
				vis[x][y][z] = true;
				if(ib(x,y,z-1) && !vis[x][y][z-1] &&  get(x,y,z-1) == null && g2.get(x,y,z-1)==null){
					add.add(MineCraft.m.loadBlock('w',x,y,z-1));
					vis[x][y][z-1] = true;
				}else{
					for(int i = 0; i<4; i++){
						int tx = x +mx[i];
						int ty = y +my[i];
						int tz = z +mz[i];

						if(ib(tx,ty,tz) && !vis[tx][ty][tz] && get(tx,ty,tz) == null && g2.get(tx,ty,tz)==null){
							add.add(MineCraft.m.loadBlock('w',tx,ty,tz));
							vis[tx][ty][tz] = true;
						}
					}
				}
				for(int i = 0; i<mx.length; i++){
					int tx = x +mx[i];
					int ty = y +my[i];
					int tz = z +mz[i];

					if(g2.get(tx,ty,tz)!=null){
						g2.get(tx,ty,tz).setWater( sides[i], true );
					}
					
					if(g2.get(tx,ty,tz)==null && get(tx,ty,tz) == null){
						get(x,y,z).renderBack( oppSides[i], true );
					}else
						get(x,y,z).renderBack( oppSides[i], false );
				}
			}
		for(MCBlock b : add)
			add(b);

	}

	public void unrender( MCBlock b ){
		super.unrender(b);
		int x = (int)(b.x/MCBlock.SIDE);
		int y = (int)(b.y/MCBlock.SIDE);
		int z = (int)(b.z/MCBlock.SIDE);
		vis[x][y][z] = false;
		for(int i = 0; i<mx.length; i++){
			int tx = x +mx[i];
			int ty = y +my[i];
			int tz = z +mz[i];

			if(g2.get(tx,ty,tz)!=null){
				g2.get(tx,ty,tz).setWater( sides[i], false );
			}
		}
	}
	
	public void realRender( MCPerson p, MCPlane plane){
		if( ! p.underWater())
		r.render(plane, p, true);
		else{
			glFrontFace(GL_CCW);
			r.render(plane, p, true);
			glFrontFace(GL_CW);
		}
	}

}