package org.md2.gameobjects.structure;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.common.VAOType;




/**
 * Ein Stein
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */


public class Stone extends Structure
{
	
	public Stone()
    {
        super(new Texture[]{Texture.STONE});
        //setDeltaY(0.25F);
    }

	@Override
	public float getRenderAngle()
	{
		return super.getAngle();
	}

	//	protected void initRenderType()
//	{
//		renderType = RENDER_TYPE_3D;
//	}
	
//	public float getRenderOrderPosition()
//	{
//		if(multistructure == null)
//			return super.getRenderOrderPosition();
//		Vec2 multiPos = new Vec2(0, multistructure.getRenderOrderPos());
//		Vec2 thisPos = this.body.getPosition();
//		Vec2 dif = multiPos.sub(thisPos);
//		dif.negateLocal();
//		dif.mulLocal(0.001F);
//		return multiPos.addLocal(dif).y;
//	}
    
    public FixtureDef getFixtureDef()
    {
    	PolygonShape cs = new PolygonShape();
    	cs.setAsBox(0.5f, 0.5f);  

    	FixtureDef fd = new FixtureDef();
    	fd.shape = cs;
    	fd.density = 0.0f;
    	fd.friction = 0.0f;        
    	fd.restitution = 0.0f;
  	
    	return fd;
  	}

}
