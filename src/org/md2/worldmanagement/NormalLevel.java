package org.md2.worldmanagement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.jbox2d.common.Vec2;
import org.md2.gameobjects.deco.NormalSoil;
import org.md2.gameobjects.structure.Stone;


public class NormalLevel extends Level
{
	public NormalLevel(float itemSpawnAmplifier, float structureSpawnAmplifier, float entitySpawnAmplifier)
	{
		super(itemSpawnAmplifier, structureSpawnAmplifier, entitySpawnAmplifier, 0.01F, 0.01F, 0.01F);
	}
	
	@Override
	protected void initProperties() 
	{
		standardWall = new Stone();
		standardFloor = new NormalSoil();
	}

	@Override
	public boolean[][] generateNewMapArray() {
		int minRoom = 2;
    	int maxRoom = 4;
    	int roomFrequency = 10;
        int size = 25;
        Random rdm = new Random();
        ArrayList<Vec2> connectionVec2s = new ArrayList<Vec2>();
        boolean[][] map = new boolean[size][size];

        for (int x = 1; x < map.length ; x++) // '1' so that a room wont be generated directly at the map border
        { 
            for (int y = 1; y < map[0].length; y++)
            {
                if (false == map[x][y]) // tests if there is already a room at this position
                {
                    if (rdm.nextInt(roomFrequency) == 0)
                    {
                        int roomSize = rdm.nextInt(maxRoom-minRoom) + minRoom;
                        if(generateRoom(x, y, roomSize , map)){ //tries to generate a room at the given position
                            connectionVec2s.add(new Vec2(rdm.nextInt(roomSize) + x , rdm.nextInt(roomSize) + y)); // random position in the room, from where a connection to another one will be build
                        }
                    }
                }
            }
        }
        generateConnections(connectionVec2s, map);
        boolean [][] ret = new boolean[map.length * 2][map[0].length * 2];
        for (int x = 0; x < map.length ; x++) {
            for (int y = 0; y < map[0].length; y++) {
                ret[2*x][2*y] = map[x][y];
                ret[2*x+1][2*y] = map[x][y];
                ret[2*x][2*y+1] = map[x][y];
                ret[2*x+1][2*y+1] = map[x][y];
            }
        }
        return ret;
	}
	
	public void generateConnections(ArrayList<Vec2> connectionVec2s, boolean[][]map)
    {

        Iterator <Vec2> it = connectionVec2s.iterator();
        while (it.hasNext()) // go through all the Vec2s and pick one
        {
            Vec2 p1 = it.next();
            int nearestX = -1;  
            int nearestY = -1;
            int startX = (int)p1.x;  //coords of the Vec2 
            int startY = (int)p1.y;
            it.remove(); // remove that Vec2, so that it wont connect to itself
            double distance = 1000000;
            for (Vec2 p2 : connectionVec2s){  //loop through the remaning Vec2s
                int x = (int)p2.x;    // get the coords
                int y = (int)p2.y;     
                int disX = x - startX;  
                int disY = y - startY;
                double newDis = Math.sqrt(disX*disX+disY*disY); // get the distance between the starting Vec2 and this one
                if(newDis < distance){ // if the dis is small enough, set this Vec2 to the neareast of the starting Vec2
                    distance = newDis;
                    nearestX = x;
                    nearestY = y;
                }
            }
            // generates a connection to the nearest Vec2
            // if there is no other connection Vec2 , dont generate one
            if(nearestX != -1){
                generateConnection(startX, startY, nearestX, nearestY, map);
            }

        }
    }

    public void generateConnection(int x1, int y1, int x2, int y2, boolean[][]map) 
    {
        Random rdm = new Random();
        if(rdm.nextInt(2) == 0){
            if(x1<x2){
                for(;x1 < x2; x1++){
                    map[x1][y1] = true;
                }
            }
            else if(x1>x2) {
                for(;x1 > x2; x1--){
                    map[x1][y1] = true;
                }
            }
            if(y1<y2){
                for(;y1 < y2; y1++){
                    map[x2][y1] = true;
                }
            }
            else if(y1>y2) {
                for(;y1 > y2; y1--){
                    map[x2][y1] = true;
                }
            }
        }
        else{
            if(y1<y2){
                for(;y1 < y2; y1++){
                    map[x1][y1] = true;
                }
            }
            else if(y1>y2) {
                for(;y1 > y2; y1--){
                    map[x1][y1] = true;
                }
            }
            if(x1<x2){
                for(;x1 < x2; x1++){
                    map[x1][y2] = true;
                }
            }
            else if(x1>x2) {
                for(;x1 > x2; x1--){
                    map[x1][y2] = true;
                }
            }
        }
    }

    /**
     *  Tries to generate a room at the given position
     */
    private boolean generateRoom(int x1, int y1, int i, boolean[][]map)
    {
        int x2 = 0;
        int y2 = 0;
        //tests if this room will fit into the map
        if (x1 >= map.length - i - 1 || y1 >= map[0].length - i - 1){ // '- 1' so that a room wont be generated directly at the map border
            return false;
        }

        //tests if this room will interfere with an other room
        while (x2 < i) {
            while (y2 < i) {            
                int xCoord = x1 + x2;
                int yCoord = y1 + y2;
                if(map[xCoord][yCoord]){
                    return false;
                }
                y2++;
            }
            y2 = 0;
            x2++;
        }

        x2 = 0;
        y2 = 0; 

        //builds the room
        while (x2 < i) {
            while (y2 < i) {            
                int xCoord = x1 + x2;
                int yCoord = y1 + y2;
                map[xCoord][yCoord] = true;
                y2++;
            }
            y2 = 0;
            x2++;
        }
        return true;
    }
}
