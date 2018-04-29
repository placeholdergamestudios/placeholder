package org.md2.rendering;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.md2.common.Tools;

public class VertexArrayObject 
{
	private int vaoID;
	private int vboID;
	private int tboID;
	private int iboID;
	private int vertexCount;
	public VertexArrayObject(float[] vertices, float[]textureCoords, int indices[])
	{
		vertexCount = vertices.length;
		vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		
		vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Tools.createFloatBuffer(vertices), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		tboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tboID);
        glBufferData(GL_ARRAY_BUFFER, Tools.createFloatBuffer(textureCoords), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		
		int iboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Tools.createIntBuffer(indices), GL15.GL_STATIC_DRAW);
		
		GL30.glBindVertexArray(0);
		
	}
	
	public int getVaoID()
	{
		return vaoID;
	}
	
	public int getVboID()
	{
		return vboID;
	}

	public int getVertexCount() 
	{
		return vertexCount;
	}
	
	public void cleanUp()
	{
		glDisableVertexAttribArray(0);

	    // Delete the VBOs
	    glBindBuffer(GL_ARRAY_BUFFER, 0);
	    glDeleteBuffers(vboID);
	    glDeleteBuffers(tboID);
	    glDeleteBuffers(iboID);

	    // Delete the VAO
	    glBindVertexArray(0);
	    glDeleteVertexArrays(vaoID);
	}
	
}
