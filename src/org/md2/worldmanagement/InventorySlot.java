package org.md2.worldmanagement;

import org.jbox2d.common.Vec2;
import org.joml.Vector4f;
import org.md2.common.Tools;
import org.md2.gameobjects.item.Item;
import org.md2.input.Button;
import org.md2.main.Game;
import org.md2.main.GraphicRendererV2;
import org.md2.rendering.Texture;

public class InventorySlot extends Button
{

    private Item storedItem;

    public InventorySlot(Vec2 coordinates, Vec2 size, Texture slotTexture)
    {
        super(coordinates, size, Game.M_INVENTORY,  new Texture[]{Texture.INVENTORY_SLOT});
    }

    @Override
    protected void performAction() {

    }

    public boolean addItem(Item i)
    {
        if(storedItem == null)
        {
            storedItem = i;
            return true;
        }
        else if(storedItem.addItemToStack(i))
        {
            return true;
        }
        return false;
    }

    public Item getItem()
    {
        return storedItem;
    }

    public Item removeItem()
    {
        Item buffer = storedItem;
        storedItem = null;
        return buffer;
    }

    public Item switchItem(Item i)
    {
        if(i != null && storedItem != null && storedItem.addItemToStack(i))
        {
            return null;
        }
        Item buffer = storedItem;
        storedItem = i;
        return buffer;
    }

}
