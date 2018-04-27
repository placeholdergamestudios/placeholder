package org.md2.worldmanagement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.md2.common.Texture;
import org.md2.common.Tools;
import org.md2.gameobjects.DecoObject;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.Entity;
import org.md2.gameobjects.entity.living.Dummy;
import org.md2.gameobjects.entity.living.Player;
import org.md2.gameobjects.item.Apple;
import org.md2.gameobjects.item.Herb;
import org.md2.gameobjects.structure.Glue;
import org.md2.gameobjects.structure.Multistructure;


public abstract class Level 
{
	protected boolean[][] mapArray;
	protected float itemSpawnRate;
	protected float structureSpawnRate;
	protected float entitySpawnRate;
	protected WorldObject standardWall;
	protected DecoObject standardFloor;
	protected HashMap<WorldObject, Float>itemSpawnList;
	protected HashMap<WorldObject, Float>structureSpawnList;
	protected HashMap<WorldObject, Float>entitySpawnList;
	
	public Level(float itemSpawnAmplifier, float structureSpawnAmplifier, float entitySpawnAmplifier, float itemSpawnRate, float structureSpawnRate, float entitySpawnRate)
	{
		
		this.itemSpawnRate = itemSpawnRate*itemSpawnAmplifier;
		this.structureSpawnRate = structureSpawnRate*structureSpawnAmplifier;
		this.entitySpawnRate = entitySpawnRate*entitySpawnAmplifier;
		itemSpawnList = new HashMap<WorldObject, Float>();
		structureSpawnList = new HashMap<WorldObject, Float>();
		entitySpawnList = new HashMap<WorldObject, Float>();
		mapArray = generateNewMapArray();
		initSpawnLists();
		initProperties();
	}
	
	protected abstract void initProperties();
	
	protected void initSpawnLists()
	{
		//items
		itemSpawnList.put(new Apple(), 1F);
    	itemSpawnList.put(new Herb(), 1F);
    	//structures
    	structureSpawnList.put(new Glue(), 1F);
    	//entities
    	entitySpawnList.put(new Dummy(), 1F);
	}
	
	
	//sets the rough form of the map
	public abstract boolean[][] generateNewMapArray();
	
	public ArrayList<DecoObject> generateDecoObjects()
	{
		ArrayList<DecoObject>decos = new ArrayList<DecoObject>();
		for (int x = 0; x < mapArray.length ; x++)
        { 
            for (int y = 0; y < mapArray[0].length; y++)
            {
            	if(mapArray[x][y] == true){
            		DecoObject deco = (DecoObject) Tools.getNewInstance(standardFloor);
            		deco.setTransform(new Vec2(x, y), 0);
            		decos.add(deco);
            	}
            }
        }
		return decos;
	}
	
	
	/**
	 * 
	 * Places WorldObjects in the world depending on the levels properties
	 * @param w World that the objects will be placed in
	 * @return	A list containing all the WorldObjects that were placed in the world
	 */
	public void generateObjects(World w, ArrayList<WorldObject> worldObjects, ArrayList<DecoObject> decoObjects)
	{
		ArrayList<WorldObject> list1 = generateStructures(w);
		ArrayList<WorldObject> list2 = generateItems(w);
		ArrayList<WorldObject> list3 = generateLivingEntities(w);
		worldObjects.addAll(list1);
		worldObjects.addAll(list2);
		worldObjects.addAll(list3);
		decoObjects.addAll(generateDecoObjects());
		decoObjects.addAll(generateShadows(list3));
	}
	

	private ArrayList<DecoObject> generateShadows(ArrayList<WorldObject> es) {
		ArrayList<DecoObject>shadows = new ArrayList<DecoObject>();
		for(WorldObject o: es){
			Entity l = (Entity)o;
			shadows.add(l.initShadow());
		}
		return shadows;
	}

	protected ArrayList<WorldObject> generateStructures(World w)
	{
		ArrayList<WorldObject>structures = new ArrayList<WorldObject>();
		for (int x = 0; x < mapArray.length ; x++)
        { 
			Multistructure ms = new Multistructure();
            for (int y = 0; y < mapArray[0].length; y++)
            {
            	if(mapArray[x][y] == true){
            		ms = new Multistructure();
            		if(Math.random() < structureSpawnRate){
	            		WorldObject randomStructure = getRandomObject(structureSpawnList);
	                	if(randomStructure == null){
	                		continue;
	                	}
	                	WorldObject newWorldObject = (WorldObject)Tools.getNewInstance(randomStructure);
	                	deployObjectAt(w, newWorldObject, new Vec2(x,y));
	                	structures.add(newWorldObject);
            		}
            	}
            	else{
            		WorldObject newWorldObject = (WorldObject)Tools.getNewInstance(standardWall);
					deployObjectAt(w, newWorldObject, new Vec2(x,y));
            		calcTexture(x, y, mapArray, newWorldObject);
            		structures.add(newWorldObject);
            	}
            }
        }
		return structures;
	}
	
	private void calcTexture(int x, int y, boolean[][] mapArray, WorldObject o)
	{
		if(x == 0 || y == 0 || x == mapArray.length-1 ||  y == mapArray[0].length-1) {
			o.setTexture(Texture.STONE);
			return;
		}
		boolean xp, xm, yp, ym, xpyp, xmym, xpym, xmyp;
		xp = !mapArray[x + 1][y];
		xm = !mapArray[x - 1][y];
		yp = !mapArray[x][y+1];
		ym = !mapArray[x][y-1];
		xpyp = mapArray[x + 1][y+1];
		xmym = mapArray[x - 1][y-1];
		xpym = mapArray[x + 1][y-1];
		xmyp = mapArray[x - 1][y+1];

		if(xp && xm && yp && ym) {
			if(xpyp) {
				o.setTexture(Texture.STONE_INVCORNER);
				o.setTransform(o.getPosition(), (float) Math.PI * 0.5F);
			}
			else if(xmym){
				o.setTexture(Texture.STONE_INVCORNER);
				o.setTransform(o.getPosition(), (float) Math.PI * 1.5F);
			}
			else if(xpym){
				o.setTexture(Texture.STONE_INVCORNER);
			}
			else if(xmyp){
				o.setTexture(Texture.STONE_INVCORNER);
				o.setTransform(o.getPosition(), (float) Math.PI);
			}
			else{o.setTexture(Texture.STONE);}
		}
		else if(!xp && xm && yp && ym){
			o.setTexture(Texture.STONE_SIDE);
		}
		else if(xp && !xm && yp && ym){
			o.setTexture(Texture.STONE_SIDE);
			o.setTransform(o.getPosition(), (float)Math.PI);
		}
		else if(xp && xm && !yp && ym){
			o.setTexture(Texture.STONE_SIDE);
			o.setTransform(o.getPosition(), (float)Math.PI*0.5F);
		}
		else if(xp && xm && yp && !ym){
			o.setTexture(Texture.STONE_SIDE);
			o.setTransform(o.getPosition(), (float)Math.PI*1.5F);
		}
		else if(!xp && xm && !yp && ym){
			o.setTexture(Texture.STONE_CORNER);
			o.setTransform(o.getPosition(), (float)Math.PI*0.5F);
		}
		else if(xp && !xm && yp && !ym){
			o.setTexture(Texture.STONE_CORNER);
			o.setTransform(o.getPosition(), (float)Math.PI*1.5F);
		}
		else if(xp && !xm && !yp && ym){
			o.setTexture(Texture.STONE_CORNER);
			o.setTransform(o.getPosition(), (float)Math.PI);
		}
		else if(!xp && xm && yp && !ym){
			o.setTexture(Texture.STONE_CORNER);
		}

	}

	protected ArrayList<WorldObject> generateItems(World w)
	{
		ArrayList<WorldObject>items = new ArrayList<WorldObject>();
		for (int x = 0; x < mapArray.length ; x++)
        { 
            for (int y = 0; y < mapArray[0].length; y++)
            {
            	if(mapArray[x][y] == true){
            		if(Math.random() < itemSpawnRate){
	            		WorldObject randomItem = getRandomObject(itemSpawnList);
	                	if(randomItem == null){
	                		continue;
	                	}
	                	WorldObject newWorldObject = (WorldObject)Tools.getNewInstance(randomItem);
	                	deployObjectAt(w, newWorldObject, new Vec2(x,y));
	            		items.add(newWorldObject);
            		}
            	}
            }
        }
		return items;
			
	}
	protected ArrayList<WorldObject> generateLivingEntities(World w)
	{
		ArrayList<WorldObject>entities = new ArrayList<WorldObject>();
		for (int x = 0; x < mapArray.length ; x++)
        { 
            for (int y = 0; y < mapArray[0].length; y++)
            {
            	if(mapArray[x][y] == true){
            		if(Math.random() < entitySpawnRate){
	            		WorldObject randomEntity = getRandomObject(entitySpawnList);
	                	if(randomEntity == null){
	                		continue;
	                	}
	                	WorldObject newWorldObject = (WorldObject)Tools.getNewInstance(randomEntity);
	                	deployObjectAt(w, newWorldObject, new Vec2(x,y));
	            		entities.add(newWorldObject);
            		}
            	}
            }
        }
		return entities;
	}
	
	protected WorldObject getRandomObject(HashMap<WorldObject, Float>spawnList)
	{
		double totalWeight = 0.0d;
		for (WorldObject o : spawnList.keySet())
    	{
    	    totalWeight += spawnList.get(o);
    	}
    	
    	WorldObject randomObject = null;
    	double random = Math.random() * totalWeight;
    	for (WorldObject o : spawnList.keySet())
    	{
    	    random -= spawnList.get(o);
    	    if (random <= 0.0d)
    	    {
    	        randomObject = o;
    	        break;
    	    }
    	}
    	return randomObject;
	}
	
	/**
	 * Employs the given WorldObject at the given position in the given world
	 * @param world World that the object will be employed in
	 * @param o Object that will be employed
	 * @param coords Position that the object will be employed at
	 */
	public static void deployObjectAt(World world, WorldObject o, Vec2 coords)
    {
    	deployObjectAt(world, o, coords, 0);
    }
	
	public static void deployObjectAt(World world, WorldObject o, Vec2 coords, float angle)
    {
    	Body newBody =  world.createBody(o.getBodyDef());
    	newBody.setTransform(coords, angle);
    	newBody.createFixture(o.getFixtureDef());
        o.addBody(newBody);
        newBody.setUserData(o);
        o.afterDeploySetup();
    }
	
	public void deployPlayer(World world, Player p)
    {
    	deployObjectAt(world, p, getPlayerStart());
    }
	
	protected Vec2 getPlayerStart()
	{
		Random rdm = new Random();
		while(true){
            int x = rdm.nextInt(mapArray.length);
            int y = rdm.nextInt(mapArray[0].length);
            if(mapArray[x][y] == true){
            	return new Vec2(x, y);
            }
        }
	}
}
