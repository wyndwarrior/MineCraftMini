package minecraftmini;

import java.util.*;
import static org.lwjgl.opengl.GL11.*;

public class MCWaterGrid extends MCGrid {

	boolean[][][] vis;
	MCGrid g2;

	MCTimer timer = new MCTimer();

	public MCWaterGrid(int lx, int ly, int lz) {
		super(lx, ly, lz);
		vis = new boolean[lx][ly][lz];
		g2 = MineCraft.m.g;
	}

	public void floodWater() {

		if (timer.getDelta() < 1000 / 3) {
			return;
		}
		timer.update();

		ArrayList<MCBlock> add = new ArrayList();

		for (int x = 0; x < lx; x++) {
			for (int y = 0; y < ly; y++) {
				for (int z = 0; z < lz; z++) {
					if (g[x][y][z] instanceof MCWaterBlock) {
						vis[x][y][z] = true;
						if (ib(x, y, z - 1) && !vis[x][y][z - 1] && get(x, y, z - 1) == null && g2.get(x, y, z - 1) == null) {
							add.add(MineCraft.loadBlock('w', x, y, z - 1));
							vis[x][y][z - 1] = true;
						} else {
							for (int i = 0; i < 4; i++) {
								int tx = x + mx[i];
								int ty = y + my[i];
								int tz = z + mz[i];
								int tzz = tz - 1;

								if (ib(tx, ty, tz) && !vis[tx][ty][tz] && get(tx, ty, tz) == null && g2.get(tx, ty,
										tz) == null/* && (!ib(x,y, tzz) || g2.get(x,y,tzz) != null)*/) {
									add.add(MineCraft.loadBlock('w', tx, ty, tz));
									vis[tx][ty][tz] = true;
								}
							}
						}
						for (int i = 0; i < mx.length; i++) {
							int tx = x + mx[i];
							int ty = y + my[i];
							int tz = z + mz[i];

							if (g2.get(tx, ty, tz) != null) {
								g2.get(tx, ty, tz)
										.setWater(sides[i], true);
							}

							get(x, y, z).renderBack(oppSides[i], g2.get(tx, ty, tz) == null && get(tx, ty, tz) == null);
						}
					}
				}
			}
		}
		for (MCBlock b : add) {
			add(b);
		}

	}

	@Override
	public void unrender(MCBlock b) {
		super.unrender(b);
		int x = (int) (b.x / MCBlock.SIDE);
		int y = (int) (b.y / MCBlock.SIDE);
		int z = (int) (b.z / MCBlock.SIDE);
		vis[x][y][z] = false;
		for (int i = 0; i < mx.length; i++) {
			int tx = x + mx[i];
			int ty = y + my[i];
			int tz = z + mz[i];

			if (g2.get(tx, ty, tz) != null) {
				g2.get(tx, ty, tz)
						.setWater(sides[i], false);
			}
		}
	}

	@Override
	public void realRender(MCPerson p, MCPlane plane) {
		if (!p.underWater()) {
			r.render(plane, p, true);
		} else {
			glFrontFace(GL_CCW);
			r.render(plane, p, true);
			glFrontFace(GL_CW);
		}
	}

}
