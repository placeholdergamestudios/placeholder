package org.md2.worldmanagement;

import org.jbox2d.common.Vec2;
import org.md2.gameobjects.item.Item;
import org.md2.gameobjects.item.weapons.BowItem;
import org.md2.main.Game;
import org.md2.main.SoundManager;

import java.util.ArrayList;

public class Inventory
{
	private ArrayList<InventorySlot> allSlots;

	private ArrayList<InventorySlot> normalSlots;
	private ArrayList<InventorySlot> hotbarSlots;
	private InventorySlot armourSlot;
	private InventorySlot trinketSlot;

	private InventorySlot cursor;
	private Item heldInMouse;

	public static final int NUMBER_OF_HOTBAR_SLOTS = 3;

	private float sizeOfSlots = 1.5F;
	private float gapBetweenSlots = 2;

	private int size;
	private float sideLength;

	public Inventory(int size)
	{
		normalSlots = new ArrayList<>();
		hotbarSlots = new ArrayList<>();
		allSlots = new ArrayList<>();
		this.size = size;

		float slotsPerRow = (float)Math.ceil(Math.sqrt(this.size));
		sideLength = slotsPerRow*gapBetweenSlots;

		Vec2 var1 =  new Vec2(sizeOfSlots/2, sizeOfSlots/2);
		float var2 = sideLength/2-gapBetweenSlots/2;

		int index = 0;
		for(int y = 0; y < slotsPerRow; y++){
			for(int x = 0; x < slotsPerRow; x++){
				normalSlots.add(new InventorySlot(new Vec2(gapBetweenSlots*x-var2, -(gapBetweenSlots*y-var2)), var1, null));
				index++;
				if(index == size) break;
			}
		}

		for(index = 0; index < NUMBER_OF_HOTBAR_SLOTS; index++)
		{
			hotbarSlots.add(new InventorySlot(new Vec2(var2+gapBetweenSlots, var2-index*gapBetweenSlots), var1, null));
		}
		armourSlot = new InventorySlot(new Vec2(var2+gapBetweenSlots, var2-3*gapBetweenSlots), var1, null);
		trinketSlot = new InventorySlot(new Vec2(var2+gapBetweenSlots, var2-4*gapBetweenSlots), var1, null);
		allSlots.addAll(normalSlots);
		allSlots.addAll(hotbarSlots);
		allSlots.add(armourSlot);
		allSlots.add(trinketSlot);
	}

	public ArrayList<InventorySlot> getAllSlots()
	{
		return allSlots;
	}

	public ArrayList<InventorySlot> getHotbar()
	{
		return hotbarSlots;
	}

	public InventorySlot getCursor()
	{
		return cursor;
	}

	public void onClick()
	{
		for(InventorySlot invSlot: allSlots){
			if(invSlot.wasClicked()){
				if(cursor != null && cursor.equals(invSlot))
				{
					if(heldInMouse instanceof BowItem && isHotbarSlot(invSlot))
						Game.getGame().getSoundManager().playSoundID(SoundManager.SOUNDBOWEQUIP);
					heldInMouse = invSlot.switchItem(heldInMouse);
				}
				else
					cursor = invSlot;
			}
		}
	}

	public boolean add(Item i)
	{
		for(InventorySlot invSlot: normalSlots){
			if(invSlot.addItem(i))
				return true;
		}
		return false;
	}

	public boolean isItemInHotbar(Item i)
	{
		for(InventorySlot invSlot: hotbarSlots)
		{
			if(i.equals(invSlot.getItem())){
				return true;
			}
		}
		return false;
	}

	public boolean isHotbarSlot(InventorySlot i)
	{
		for(InventorySlot invSlot: hotbarSlots)
			if (i.equals(invSlot)) return true;
		return false;
	}

	public Item getHotbarItem(int index)
	{
		return hotbarSlots.get(index).getItem();
	}

	public float getInventoryRenderSize()
	{
		return sideLength;
	}

	public Item getHeldInMouse()
	{
		return heldInMouse;
	}

	public void removeItem(Item i)
	{
		for(InventorySlot invSlot: allSlots)
		{
			if(i.equals(invSlot.getItem())){
				invSlot.removeItem();
			}
		}
	}
}
