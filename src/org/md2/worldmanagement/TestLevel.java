package org.md2.worldmanagement;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.deco.NormalSoil;
import org.md2.gameobjects.entity.living.Dummy;
import org.md2.gameobjects.structure.Stone;




public class TestLevel extends Level
{
	public TestLevel(float itemSpawnAmplifier, float structureSpawnAmplifier, float entitySpawnAmplifier)
	{
		super(itemSpawnAmplifier, structureSpawnAmplifier, entitySpawnAmplifier, 0.01F, 0.01F, 0.01F);
	}

	@Override
	public boolean[][] generateNewMapArray() {
		boolean[][] map = new boolean[20][20];
	    int y = 0;
	    int x = 0;    	
	    	for(x = 1; x < map.length-1; x++){
	    		for(y = 1; y < map.length-1; y++){
	        		map[x][y] = true;
	        	}
	    	}
	    	
	    	return map;
	}
	
	protected ArrayList<WorldObject> generateLivingEntities(World w)
	{
		ArrayList<WorldObject>entities = new ArrayList<WorldObject>();
		WorldObject newWorldObject = new Dummy();
		deployObjectAt(w, newWorldObject, new Vec2(10,10));
		entities.add(newWorldObject);
		return entities;
	}

	@Override
	protected void initProperties() 
	{
		standardWall = new Stone();
		standardFloor = new NormalSoil();
		
	}

}
