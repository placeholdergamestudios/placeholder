package org.md2.common;

import org.joml.Vector3f;

public enum VAOType 
{
	//FLOOR(1, 1), DECORATION(1, 1),STRUCTURE(1, 1), SHADOW(1, 0.5F), STRUCTURE_TALL(1, 1F),  ITEM(1, 1), ENTITY_SMALL(1, 1), ENTITY_TALL(1, 1.25F),
	UNITSQUARE(1, 1), SHADOW(1, 0.5F), ENTITY_TALL(1, 1.25F),
	HEALTHBAR(1F, 0.1F), LIVEBAR(0.8F,0.03125F),
	FULLSCREEN(1, 1), INVENTORY(1, 1), INVENTORY_ITEM(1, 1), INVENTORY_SLOT(1.5f, 1.5f), INVENTORY_CURSOR(1.5f, 1.5f), CURSOR(0.25F, 0.25F),
	CHAR(1, 1), LOWERCASECHAR(0.5F, 0.5F);
	
	private final float[] coords;
//	private final float renderPrio;
	private final Vector3f size;
	
	private VAOType(float width, float height)//renderPrio between 0 and  20 inclusive
	{
//		renderPrio = this.ordinal();
		float zpos = 0;
		coords = new float[]{
	                -0.5f*width, -0.5f*height, zpos,
	                0.5f*width, -0.5f*height, zpos,
	                0.5f*width, 0.5f*height, zpos,
	                -0.5f*width, 0.5f*height, zpos	
	    			};
		
		size = new Vector3f(width, height, zpos);
	}
	
//	public float getRenderPrio()
//	{
//		return renderPrio;
//		
//	}
	
	public Vector3f getSize()
	{
		return size;
	}
	
	public float[] getCoords()
	{
		return coords;
	}
}
