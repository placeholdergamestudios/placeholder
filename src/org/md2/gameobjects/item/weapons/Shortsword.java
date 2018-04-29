package org.md2.gameobjects.item.weapons;

import org.md2.rendering.Texture;

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
