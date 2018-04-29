package org.md2.rendering;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureObject 
{
	private int textureID;

    public TextureObject(String fileName)
    {
    	loadTexture(fileName);
    }


    public void bind() 
    {
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
    }

    public int getTextureID() {
        return textureID;
    }

    private void loadTexture(String textureName) 
    {
        // Load Texture file
        PNGDecoder decoder = null; 
        try{
        	decoder = new PNGDecoder(TextureObject.class.getResourceAsStream("/" + textureName+".png"));
        }
        catch(Exception e){
            System.out.println("The texture with name '"+textureName+"' could not be loaded. Check ENUM Texture and folder 'Texture'");
        	e.printStackTrace();
        }

        // Load texture contents into a byte buffer
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        try{
        	decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        buf.flip();

        // Create a new OpenGL texture 
        textureID = GL11.glGenTextures();
        // Bind the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        // Upload the texture data
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0,
        		GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
        // Generate Mip Map
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
    }

    public void cleanup() 
    {
    	GL11.glDeleteTextures(textureID);
    }
}
