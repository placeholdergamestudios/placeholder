package org.md2.worldmanagement;

import java.util.ArrayList;
import java.util.Arrays;

import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.md2.gameobjects.WorldObject;
import org.md2.gameobjects.item.Item;

public class Inventory 
{
	private int size;
	private float slotRenderSize;
	private float slotRenderDistance;
	private int hotbarItems;
	private int cursorX;
	private int cursorY;
	private int slotsPerRow;
	private Item[][] container;
	private Item [] hotbar;
	private Item mousePickUp;
	
	public Inventory(int size)
	{
		this.size = size;
		mousePickUp = null;
		slotRenderSize = 1F;
		slotRenderDistance = 2;
		hotbarItems = 3;
		slotsPerRow = (int)Math.ceil(Math.sqrt(size));
		container = new Item[slotsPerRow+1][slotsPerRow];
		cursorX = 0;
		cursorY = 0;
	}
	
	
	
	public Item[][] getContainer()
	{
		return container;
	}
	
	public void setMousePickUp(Item i)
	{
		mousePickUp = i;
	}
	
	public Item getMousePickUp()
	{
		return mousePickUp;
	}
	
	public boolean add(Item i)
	{
		for(int y = 0; y < slotsPerRow; y++){
			for(int x = 0; x < slotsPerRow; x++){
				Item slot = container[x][y];
				if(slot != null && slot.addItemToStack(i))
					return true;
			}
    	}
		for(int y = 0; y < slotsPerRow; y++){
			for(int x = 0; x < slotsPerRow; x++){
				Item slot = container[x][y];
				if(slot == null){
					container[x][y] = i;
					return true;
				}
			}
    	}
    	return false;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public float getInventoryRenderSize()
	{
		return slotRenderDistance*slotsPerRow;
	}
	
	public Vector4f getInventorySlotTransformation(int slotX, int slotY)
	{
		float corner = (slotsPerRow-1)/2;
		return new Vector4f(slotRenderDistance*(slotX-corner), -slotRenderDistance*(slotY-corner), 0, slotRenderSize);
	}
	
	public boolean isValidPosition(int x, int y)
	{
		if(x >= container.length || y >= container[0].length || x < 0 || y < 0)
			return false;
		if(x == slotsPerRow && y >= hotbarItems)
			return false;
		return true;
	}
	
	public Vector4f getCursorTransformation()
	{
		if(cursorX < 0)
			return new Vector4f(0, 0, 0, 0);
		return getInventorySlotTransformation(cursorX, cursorY);
	}
	
	public boolean setCursorTo(Vec2 coords)
	{
		int oldX = cursorX;
		int oldY = cursorY;
		float corner = (slotsPerRow-1)/2;
		int x = Math.round(coords.x/slotRenderDistance+corner);
		int y = Math.round(-coords.y/slotRenderDistance+corner);
		if(!isValidPosition(x, y)){
				x = -1;
				y = -1;
		}
		cursorX = x;
		cursorY = y;
		if(oldX == cursorX && oldY == cursorY)
			return true;
		return false;
	}
	
	
	public int getSlotsPerRow()
	{
		return slotsPerRow;
	}

	public void deleteCursorItem() 
	{
		if(cursorX < 0)
			return;
		container[cursorX][cursorY] = null;
	}
	
	public void switchMouseWithCursor()
	{
		Item buffer = mousePickUp;
		if(cursorX < 0)
			return;
		if(buffer != null && container[cursorX][cursorY] != null && container[cursorX][cursorY].addItemToStack(buffer)){
			mousePickUp = null;
			return;
		}
		mousePickUp = getCursorItem();	
		container[cursorX][cursorY] = buffer;
	}

	public Item getCursorItem() 
	{
		if(cursorX < 0)
			return null;
		return container[cursorX][cursorY];
		
	}
	
	public Item getHotbarItem(int index)
	{
		if(index >= hotbarItems || index < 0)
			return null;
		return container[slotsPerRow][index];
	}
	
	public Item[] getHotbar()
	{
		return container[slotsPerRow];
	}

	public void removeItem(Item i) 
	{
		for(int y = 0; y < container[1].length; y++){
			for(int x = 0; x < container.length; x++){
				Item slot = container[x][y];
				if(slot == i){
					container[x][y] = null;
				}
			}
    	}
	}
	public boolean isItemInHotbar(Item i)
	{
		boolean r = false;
		for(Item item : getHotbar())
		{
			if(item != null)
			{
				if(item.equals(i))
				{
					r = true;
				}
			}
		}
		return r;
	}
}
