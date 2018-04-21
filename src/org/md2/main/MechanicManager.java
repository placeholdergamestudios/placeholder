package org.md2.main;
import java.awt.Toolkit;

import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.md2.common.Tools;
import org.md2.gameobjects.entity.living.LivingEntity;
import org.md2.gameobjects.entity.living.Player;
import org.md2.gameobjects.item.Item;
import org.md2.input.KeyboardInput;
import org.md2.worldmanagement.Inventory;
import org.md2.worldmanagement.WorldManager;


/**
 * Beschreiben Sie hier die Klasse MechanicManager.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class MechanicManager extends Thread
{
    private WorldManager worldManager;
    public static final int tickRate = 60; // in ticks per second
    public static final int tickTime = 1000/tickRate; //time it takes to perform 1 tick in ms
    
    public MechanicManager()
    {
        worldManager = new WorldManager();
    }
    
    public WorldManager getWorldManager()
    {
        return worldManager;
    }
    
    public void run()
    {
        while(Game.getGame().isRunning()){
        	double timeBefore = System.currentTimeMillis();
        	getUserAction();
            if(Game.getGame().getMenue() == Game.M_INGAME){
                worldManager.tick();
            }
            double timeDif = System.currentTimeMillis() - timeBefore;
            if(tickTime - (int)timeDif > 0){
                try{
                    Thread.sleep(tickTime - (int)timeDif);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Cant keep the mechanics up!");
            }
        }
    }
    

    
    public void getUserAction()
    {
    	Player player = worldManager.getPlayer();
    	player.resetShoulds();
        if(Game.getGame().getMenue() == Game.M_INGAME){
            if(KeyboardInput.isPushed(KeyboardInput.ACTION_UP)){
            	player.setShouldMove(1);
            }
            if(KeyboardInput.isPushed(KeyboardInput.ACTION_DOWN)){
            	player.setShouldMove(-1);
            }
            if(KeyboardInput.isPushed(KeyboardInput.ACTION_LEFT)){
            	player.setShouldRotate(1);
            }
            if(KeyboardInput.isPushed(KeyboardInput.ACTION_RIGHT)){
            	player.setShouldRotate(-1);
            }
            if(KeyboardInput.isPressed(KeyboardInput.ACTION_INVENTORY)){
                Game.getGame().setMenue(Game.M_INVENTORY);
            }
            if(KeyboardInput.isPressed(KeyboardInput.ACTION_ESC)){
                Game.getGame().setMenue(Game.M_ESC);
            }
            if(KeyboardInput.isPushed(KeyboardInput.ACTION_VOID)){
                
            }
            if(KeyboardInput.isPushed(KeyboardInput.ACTION_ENTER)){
                
            }
            if(KeyboardInput.isPressed(KeyboardInput.ACTION_QUICKUSE1)){
                player.useHotbarItem(0);
            }
            if(KeyboardInput.isPressed(KeyboardInput.ACTION_QUICKUSE2)){
                player.useHotbarItem(1);
            }
            if(KeyboardInput.isPressed(KeyboardInput.ACTION_QUICKUSE3)){
                player.useHotbarItem(2);
            }
        }
        else if(Game.getGame().getMenue() == Game.M_INVENTORY){
        	Inventory inv = player.getInventory();
        	if(KeyboardInput.isPressed(KeyboardInput.ACTION_ESC) || KeyboardInput.isPressed(KeyboardInput.ACTION_INVENTORY)){
        		Game.getGame().setMenue(Game.M_INGAME);
            }
        	if(KeyboardInput.isPressed(KeyboardInput.ACTION_UP)){
        		//inv.moveCursor(0, -1);
            }
            if(KeyboardInput.isPressed(KeyboardInput.ACTION_DOWN)){
            	//inv.moveCursor(0, 1);
            }
            if(KeyboardInput.isPressed(KeyboardInput.ACTION_LEFT)){
            	//inv.moveCursor(-1, 0);
            }
            if(KeyboardInput.isPressed(KeyboardInput.ACTION_RIGHT)){
            	//inv.moveCursor(1, 0);
            }
            if(KeyboardInput.isPressed(KeyboardInput.ACTION_ENTER)){
            	player.useItem(inv.getCursorItem());
            }
            if(KeyboardInput.isPressed(KeyboardInput.ACTION_BACK) || KeyboardInput.isPressed(KeyboardInput.ACTION_DELETE)){
            	inv.setMousePickUp(null);
            }
            if(KeyboardInput.isPressed(KeyboardInput.ACTION_SELECT)){
               if(inv.setCursorTo(GraphicRendererV2.getMousePos()) || inv.getMousePickUp() != null){
            	   	inv.switchMouseWithCursor();
               }
            }
        }
        else if(Game.getGame().getMenue() == Game.M_ESC){
        	if(KeyboardInput.isPressed(KeyboardInput.ACTION_ESC)){
        		Game.getGame().setMenue(Game.M_INGAME);
            }
        }
        
        
    }
    
    
}


