package org.md2.main;

import org.md2.input.KeyboardInput;
/**
 * Beschreiben Sie hier die Klasse Game.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 * 
 * Menüs
 * karteOffen : k
 * inventarOffen : i
 * deathScreen : d
 * inGame : g
 * startmenue : s
 */

public class Game
{
    private boolean running;
    public int menue;
    private KeyboardInput ki;
    private GraphicRendererV2 gr;
    private MechanicManager mm;
    private static Game game;
    
    private static boolean threadsLocked;

    public static final int M_INGAME = 0;
    public static final int M_START = 1;
    public static final int M_ESC = 2;
    public static final int M_INVENTORY = 3;
    
    
    private Game()
    {
        //nothing, look for init()
    }

    private void init()
    {
        menue = M_INGAME;
        gr = new GraphicRendererV2();
        ki = new KeyboardInput();
        mm = new MechanicManager();
        gr.setInput(ki);
        setRunning(true); 
        mm.start();
        ki.unlockKeyboard(true);
        gr.run();
        
        
    }

    public static void main (String args[])
    {
        if(Game.getGame() == null){
            game = new Game();
            game.init();
        }
        else{
            System.out.println("There is already an instance of this game running");
        }
    }

    public boolean isRunning()
    {
        return running;
    }
    
    public void setRunning(boolean b)
    {
    	running = b;
    }

    public int getMenue()
    {
        return menue;
    }
    
    public void setMenue(int m)
    {
    	menue = m;
    }

    public KeyboardInput getKeyboardInput()
    {
        return ki;
    }

    public GraphicRendererV2 getGraphicRenderer()
    {
        return gr;
    }

    public MechanicManager getMechanicManager()
    {
        return mm;
    }

    public static Game getGame()
    {
        return game;
    }
    
    public static void requestLock()
    {
    	while(threadsLocked){
    		try{
    			Thread.sleep(1);
    		}catch(InterruptedException e){
    			e.printStackTrace();
    		}
    	}
    	threadsLocked = true;
    }
    
    public static void releaseLock()
    {
    	threadsLocked = false;
    }

}
