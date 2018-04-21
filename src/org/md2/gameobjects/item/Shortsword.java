package org.md2.gameobjects.item;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;
import org.md2.common.VAOType;

public class Shortsword extends SwordItem
{
	public Shortsword()
	{
		super(new Texture[]{Texture.SHORTSWORD}, 3, 2f);
	}
	
	
	protected void initVars()
	{
		varOnUse = 0;
		varOnAttack = 5;
		varOnThrow = 0;
	}

}
