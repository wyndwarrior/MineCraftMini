package minecraftmini;

import java.util.*;
import java.nio.*;

import org.newdawn.slick.opengl.*;

import static org.lwjgl.opengl.GL11.*;

public class MCGridRender {

	private HashMap<Texture, HashSet<MCTexturedQuad>> map;

	public MCGridRender() {
		map = new HashMap<>();
	}

	public void add(MCTexturedQuad q) {
		Texture t = q.getTexture();

		if (!map.containsKey(t)) {
			map.put(t, new HashSet<MCTexturedQuad>());
		}

		map.get(t)
				.add(q);
	}

	public static final double realRenderDist = MCBlock.SIDE * 40;
	public static final double renderDist = Math.pow(realRenderDist, 2);
	public static final double realFadeDist = realRenderDist * .93;
	public static final double fadeDist = Math.pow(realFadeDist, 2);
	public static final double dDist = realRenderDist - realFadeDist;

	public static final HashMap<FloatBuffer, ArrayList<MCTexturedQuad>> fadeMap = new HashMap<>();

	static {
		for (FloatBuffer fb : MCMaterial.cache) {
			fadeMap.put(fb, new ArrayList<>());
		}
		for (FloatBuffer fb : MCMaterial.wcache) {
			fadeMap.put(fb, new ArrayList<>());
		}
	}

	public void remove(MCTexturedQuad q) {
		Texture t = q.getTexture();
		if (map.containsKey(t)) {
			map.get(t)
					.remove(q);
		}
	}

	public void render(MCPlane p, MCPerson pp, boolean back) {


		for (Map.Entry<Texture, HashSet<MCTexturedQuad>> e : map.entrySet()) {
			e.getValue()
					.iterator()
					.next()
					.bind();

			boolean underWater = MCPerson.underWaterS();

			MCMaterial.setMaterial(MCMaterial.NORM);
			FloatBuffer underWaterNorm = MCMaterial.findBuf(MCMaterial.NORM, true);
			glBegin(GL_QUADS);
			for (MCTexturedQuad q : e.getValue()) {
				if (q.back() != back) {
					continue;
				}
				//System.out.println (q.getX() + " " + q.getY() + " " + q.getZ());
				if (p.test(q.getX(), q.getY(), q.getZ())) {

					double dist = Math.pow(q.getX() - pp.x, 2) + Math.pow(q.getY() - pp.y, 2) + Math.pow(q.getZ() - pp.z, 2);
					if (dist > renderDist) {
						continue;
					}

					boolean water = underWater ^ q.water();

					if (dist > fadeDist) {
						FloatBuffer fb;
						if (!q.water()) {
							fb = MCMaterial.findBuf(MCMaterial.NORM / (water ? 3 : 1) * (1 - (Math.sqrt(dist) - realFadeDist) / dDist), water);
						} else {
							fb = MCMaterial.findBuf(MCMaterial.NORM * (1 - (Math.sqrt(dist) - realFadeDist) / dDist), true);
						}
						fadeMap.get(fb)
								.add(q);
					} else {
						if (water || q.water()) {
							if (!q.water()) {
								fadeMap.get(MCMaterial.findBuf(MCMaterial.NORM * 2 / 3, true))
										.add(q);
							} else {
								fadeMap.get(underWaterNorm)
										.add(q);
							}
						} else {
							q.render();
						}
					}
					//count++;
				}
			}
			glEnd();

			for (Map.Entry<FloatBuffer, ArrayList<MCTexturedQuad>> ee : fadeMap.entrySet()) {
				MCMaterial.setBuf(ee.getKey());
				glBegin(GL_QUADS);

				for (MCTexturedQuad q : ee.getValue()) {
					q.render();
				}

				glEnd();
			}
			clearMap();

		}

		//System.out.println (count + " rendered");

	}

	public void clearMap() {
		for (Map.Entry<FloatBuffer, ArrayList<MCTexturedQuad>> e : fadeMap.entrySet()) {
			e.getValue()
					.clear();
		}
	}

}
