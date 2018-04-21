package org.md2.gameobjects.item;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.FixtureDef;
import org.md2.common.Texture;

public class WoodenBoomerang extends BoomerangItem
{
	public WoodenBoomerang()
	{
		super(new Texture[]{Texture.BOOMERANG});
	}
	
	protected void initVars()
	{
		varOnUse = 0;
		varOnAttack = 0;
		varOnThrow = 10;
	}
}
