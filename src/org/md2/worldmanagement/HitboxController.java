package org.md2.worldmanagement;
import java.util.ArrayList;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.md2.gameobjects.WorldObject;


public class HitboxController implements QueryCallback, RayCastCallback
{
	private ArrayList<WorldObject>objectsAABB;
	private ArrayList<WorldObject>objectsRayCast;
	
	public HitboxController()
	{
		objectsAABB = new ArrayList<WorldObject>();
	}
	
	public ArrayList<WorldObject> getObjectsInRect(World w, Vec2 bottomleft, Vec2 topright)
	{
		objectsAABB.clear();
		w.queryAABB(this, new AABB(bottomleft, topright));
		return objectsAABB;
	}

	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) 
	{
		return 0;
	}

	public boolean reportFixture(Fixture fixture) 
	{
		objectsAABB.add((WorldObject) fixture.getBody().m_userData);
		return true;
	}

}
