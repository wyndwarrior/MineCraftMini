import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.*;
import static org.lwjgl.opengl.GL11.*;

public class MCTexturedQuad{

	protected Texture texture;
	protected MCBlock block;
	protected int side;

	public MCTexturedQuad( MCBlock block, int side, String text ){
		texture = MCTextureLoader.getTexture( text );
		this.block = block;
		this.side = side;
	}

	public boolean glow = false;

	public void bind(){
		texture.bind();

		//glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST );
		//glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR );
	}

	public boolean water( ){
		return block.getWater( side );
	}
	
	public boolean back(){
		return block.getBack(side);
	}

	public Texture getTexture(){
		return texture;
	}

	public void render(){
		if(glow) {
			glEnd();
			MCLight.light.setStr(MCLight.GLOW);
			glBegin(GL_QUADS);
		}
		block.render(side);
    	if(glow) {
			glEnd();
			MCLight.light.setStr(MCLight.NORM);
			glBegin(GL_QUADS);
		}
	}

	public double getX(){return block.x;}
	public double getY(){return block.y;}
	public double getZ(){return block.z;}

}
