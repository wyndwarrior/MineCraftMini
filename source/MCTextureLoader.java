import org.lwjgl.*;
import org.lwjgl.opengl.*;
import java.io.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.*;
import org.newdawn.slick.util.*;
import java.util.*;

public class MCTextureLoader{
	private static HashMap<String, Texture> map;
	
	static {
		map = new HashMap<String, Texture>();
	}
	
	public static Texture getTexture(String s){
        s = "res/"+s;
		if(map.containsKey(s)) return map.get(s);
		try{
			map.put(s, TextureLoader.getTexture(s.split("\\.")[s.split("\\.").length-1], ResourceLoader.getResourceAsStream(s)));
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		return map.get(s);
	}
	
}