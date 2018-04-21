package org.md2.input;

/**
 * Beschreiben Sie hier die Klasse Key.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Action
{
    private boolean pushed;
    private boolean disabledUntilNextPush; //the key needs to be released and pushed again
    public Action()
    {
        pushed = false;
        disabledUntilNextPush = false;
    }
    
    public void setPushed()
    {
        pushed = true;
    }
    
    public void setReleased()
    {
        pushed = false;
        disabledUntilNextPush = false;
    }
    
    public boolean isPushed()
    {
    	if(!disabledUntilNextPush){
        	return pushed;
    	}
    	else{
    		return false;
    	}
    }
    
    public void setDisabled()
    {
    	disabledUntilNextPush = true;
    }
}
