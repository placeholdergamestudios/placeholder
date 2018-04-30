package org.md2.gameobjects.structure;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.rendering.Texture;


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
        size = new Vec2(1F, 1F);
    }

	@Override
	public float getRenderAngle()
	{
		return super.getAngle();
	}

	@Override
	public Vec2 getRenderSize()
	{
		return new Vec2(2,2);
	}


}
