package org.md2.gameobjects.item.weapons;

import org.md2.rendering.Texture;

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
