package org.md2.gameobjects;
import org.jbox2d.common.Vec2;
import org.md2.common.Texture;
import org.md2.common.VAOType;


public abstract class DecoObject extends GameObject 
{
	protected Vec2 position;
	protected float angle;
    
    
    public DecoObject(Texture[] texture)
    {    
    	super(texture);
    }

	public float getAngle()
    {
    	return angle;
    }
    
    public void setTransform(Vec2 pos, float angle)
    {
    	position = pos;
    	this.angle = angle;
    }
    
	@Override
	public Vec2 getPosition() 
	{
		return position.clone();
	}
}
