package org.md2.worldmanagement;
import java.util.ArrayList;
import java.util.Iterator;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.md2.common.Tools;
import org.md2.gameobjects.DecoObject;
import org.md2.gameobjects.GameObject;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.entity.living.Player;
import org.md2.gameobjects.item.weapons.Shortsword;
import org.md2.gameobjects.item.weapons.WoodenBoomerang;
import org.md2.gameobjects.item.weapons.WoodenBow;
import org.md2.gameobjects.item.weapons.TestWand;
import org.md2.main.Game;
import org.md2.main.MechanicManager;
/**
 * Beschreiben Sie hier die Klasse World.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class WorldManager implements ContactListener
{
	public World world;
    private ArrayList<WorldObject> worldObjects;
    private ArrayList<DecoObject> decoObjects;
    private Player player;    
    private int worldTickCounter = 0;
    
    public WorldManager()
    {
    	world = new World(new Vec2(0, 0.0f));
    	world.setContactListener(this);
    	WorldObject.worldManager = this;
    	setup();
    }

    private void setup()
	{
		Level lvl = new NormalLevel(1, 1, 1);
		worldObjects = new ArrayList<>();
		decoObjects = new ArrayList<>();
		lvl.generateObjects(world, worldObjects, decoObjects);
		player = new Player();
		lvl.deployPlayer(world, player);
		worldObjects.add(player);
		player.pickUpItem(new WoodenBoomerang());
		player.pickUpItem(new Shortsword());
		player.pickUpItem(new WoodenBow());
		player.pickUpItem(new TestWand());
	}

    public void tick()
    {
    	Game.requestLock();
    	worldTickCounter++;
    	world.step(MechanicManager.tickTime/1000f, 6, 2);
    	Iterator<WorldObject> it1 = worldObjects.iterator();
    	while(it1.hasNext()){
    		WorldObject o = it1.next();
    		if(o.willBeRemoved()){
    			world.destroyBody(o.getBody());
    			it1.remove();
    		}
    	}

		decoObjects.removeIf((DecoObject d) -> d.willBeRemoved());

		for(WorldObject w : worldObjects)
    		w.performTick();
    	for(DecoObject d : decoObjects)
    		d.performTick();
    	if(worldTickCounter == 5){
    		worldTickCounter = 0;
    		for(WorldObject wO: worldObjects){
        		wO.animationUpdate();
        	}
    	}
    	Game.releaseLock();
    }

    public Player getPlayer()
    {
        return player;
    }
    
    public void spawnObjectAt(WorldObject o, Vec2 coords)
    {
    	this.spawnObjectAt(o, coords, 0);
    }
    
    public void spawnObjectAt(WorldObject o, Vec2 coords, float angle)
    {
    	Level.deployObjectAt(world, o, coords, angle);
    	worldObjects.add(o);
    }
    
    public void spawnDecorationAt(DecoObject d)
    {
    	decoObjects.add(d);
    }

    public boolean isPositionBlocked(Vec2 coords)
    {
    	for(WorldObject o : worldObjects){
    		if(!o.isSensor() && Tools.vec2InsideRect(o.getPosition(), o.getSize(), coords)){
    			return true;
    		}
    	}
    	return false;
    }

	@Override
	public void beginContact(Contact contact) 
	{
		WorldObject a = (WorldObject) contact.getFixtureA().getBody().getUserData();
		WorldObject b = (WorldObject) contact.getFixtureB().getBody().getUserData();
		if(a.willBeRemoved() || b.willBeRemoved())
			return;
		a.onCollision(b);
		b.onCollision(a);
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}



	private ArrayList<GameObject> getAllObjects()
	{
		ArrayList<GameObject>ret = new ArrayList<>();
		ret.addAll(decoObjects);
		ret.addAll(worldObjects);
		return ret;
	}

	public ArrayList<WorldObject> getWorldObjects(Vec2 center, Vec2 renderRange)
	{
		ArrayList<WorldObject> ret = new ArrayList<>();
		for(WorldObject o: worldObjects){
			if(Tools.vec2InsideRect(center, renderRange, o.getPosition()))
				ret.add(o);
		}
		return ret;
	}

	public ArrayList<WorldObject> getAllWorldObjects()
	{
		return worldObjects;
	}

	public ArrayList<GameObject> getGameObjects(Vec2 center, Vec2 renderRange)
	{
		ArrayList<GameObject> objects = getAllObjects();
		ArrayList<GameObject> ret = new ArrayList<>();
		ArrayList<GameObject> d3 = new ArrayList<>();
		for(GameObject o: objects){
			if(Tools.vec2InsideRect(center, renderRange, o.getPosition())){
				if(o.getRenderType() == GameObject.RENDER_TYPE_FLAT){
					ret.add(o);
				}
				else if (o.getRenderType() == GameObject.RENDER_TYPE_3D) {
					d3.add(o);
				}
			}
		}
		Tools.sortByRenderPrio(ret);
		Tools.sortByYValue(d3);
		ret.addAll(d3);
		return ret;

	}

}
