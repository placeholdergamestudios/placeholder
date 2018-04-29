package org.md2.gameobjects;
import org.jbox2d.common.Vec2;
import org.md2.rendering.RenderPrio;
import org.md2.rendering.Texture;
import org.md2.rendering.VAOType;


public abstract class GameObject 
{
	public static int RENDER_TYPE_FLAT = 0;
	public static int RENDER_TYPE_3D = 1;
	
	protected VAOType vaoType;
	protected Texture[] texture; //the textures of this object
	protected int renderType;
	protected RenderPrio renderPriorisation;
	
	
	private Vec2 renderPos;
	
	private float deltaY; // set this to non-zero if the render object should appear elevated above the ground
	
    private boolean removeFromWorld; //This world object will removed from the world before the next world tick

	
	public GameObject(Texture[] texture)
	{
		this.texture = texture;
		renderType = RENDER_TYPE_FLAT;
		vaoType = VAOType.UNITSQUARE;
		renderPriorisation = RenderPrio.TEST;
		deltaY = 0;
	}
	
	//returns whether this object is dead
	public boolean willBeRemoved()
    {
    	return removeFromWorld;
    }
    
	//call this, if you want this object to die; overwrite if you want something else to happen, when its dying
    public void removeFromWorld()
    {
    	removeFromWorld = true;
    }
	
	//
	public void performTick()
	{
		return;
	}
	
	public void lockRenderPosition()
	{
		renderPos = getPosition();
	}
	
	//getter and setter
	
	public abstract float getAngle(); //returns the physical angle of this object
	
	public abstract Vec2 getPosition(); //returns the physical position of this object
	
	public abstract void setTransform(Vec2 position, float angle); //modifies the physical angle and position of this object
	
	public VAOType getVAOType()
	{
		return vaoType;
	}
	
	public int getRenderType()
	{
		return renderType;
	}
	
	public int getRenderPrio()
	{
		return renderPriorisation.ordinal();
	}
	
	public Vec2 getRenderSize() //returns the size the texture of this object will be drawn
	{
		return new Vec2(1, 1);
	}
	
	public float getRenderAngle()  //returns the angle by which the texture of this object will rotated when drawn
	{
		return 0;
	}
	
	public Texture getTexture()
	{
		return texture[0];
	}
	
	public Texture[] getTextures()
	{
		return texture;
	}
	
	public void setTexture(Texture t)
	{
		texture[0] = t;
	}
	
	public void setTextures(Texture[] t)
	{
		texture = t;
	}
	
	protected void setDeltaY(float f)
	{
		deltaY = f;
	}
	
	public Vec2 getRenderPosition()
	{
		return renderPos.add(new Vec2(0, deltaY));
	}
}
