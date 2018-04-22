package org.md2.gameobjects.item;

import org.md2.common.Texture;

public class WoodenBow extends BowItem
{
	public WoodenBow()
	{
		super(new Texture[]{Texture.WOODENBOW}, new Texture[]{Texture.WOODENBOW1, Texture.WOODENBOW2}, Texture.ARROW, 2);
	}
	
	protected void initVars()
	{
		varOnUse = 0;
		varOnAttack = 0;
		varOnThrow = 10;
	}
}