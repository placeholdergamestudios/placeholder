package org.md2.common;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.md2.gameobjects.GameObject;


public class Tools 
{
	public static Vec2 vector2fToVec2(Vector2f vector)
    {
        return new Vec2(vector.x, vector.y);
    }
	
	public static float[] createRectTextureVBO()
	{
		return new float[]{
				0.0f, 1.0f,
				1.0f, 1.0f,
	    		1.0f, 0.0f,
				0.0f, 0.0f,
				};
	}
	
	public static int[] createRectIndicesVBO()
	{
		return new int[]{
				0, 2, 3, 0, 1, 2
    			};
	}
	
	public static FloatBuffer createFloatBuffer(float[] data)
	{
		FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
		fb.put(data);
		fb.flip();
		return fb;
	}
	
	public static IntBuffer createIntBuffer(int[] indices)
	{
		IntBuffer bb = BufferUtils.createIntBuffer(indices.length);
		bb.put(indices);
		bb.flip();
		return bb;
	}
	
	public static String loadResource(String fileName)
	{
        String result = "";
        try (InputStream in = Tools.class.getClass().getResourceAsStream(fileName)) {
            result = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
        }
        catch(Exception e){
        	e.printStackTrace();
        	System.out.println("An error occured when loading a file");
        }
        return result;
	}

    public static boolean vec2InsideRect(Vec2 center, Vec2 halfAxes, Vec2 vec2)
    {
        return(vec2.x >= center.x-halfAxes.x && vec2.x <= center.x+halfAxes.x &&
                vec2.y >= center.y-halfAxes.y && vec2.y <= center.y+halfAxes.y);
    }

	
	public static <T extends GameObject> T getNewInstance(T type)
	{
		@SuppressWarnings("unchecked")
		Class<T> c = (Class<T>)type.getClass();
    	Constructor<T> cons;
    	try {
    		cons = c.getConstructor();
			return cons.newInstance();
		} 
    	catch (Exception e) {
			e.printStackTrace();
			return null;
    	}
		
	}
	
	public static void sortByRenderPrio(ArrayList<GameObject> list)
	{
		qSortRP(list, 0, list.size()-1);
	}
	
	private static void qSortRP(ArrayList<GameObject> list, int low, int high)
    {
    	if (list == null || list.size() == 0){
			return;
    	}
 
		if (low >= high){
			return;
		}
 
		int middle = low + (high - low) / 2;
		GameObject pivot = list.get(middle);
 
		int i = low;
		int j = high;
		while (i <= j) {
			while (list.get(i).getRenderPrio() < pivot.getRenderPrio()) {
				i++;
			}
 
			while (list.get(j).getRenderPrio() > pivot.getRenderPrio()) {
				j--;
			}
 
			if (i <= j) {
				Collections.swap(list, i, j);
				i++;
				j--;
			}
		}
		if (low < j)
			qSortRP(list, low, j);
 
		if (high > i)
			qSortRP(list, i, high);
    }
	
	public static void sortByYValue(ArrayList<GameObject> list)
	{
		qSortY(list, 0, list.size()-1);
	}
	
	private static void qSortY(ArrayList<GameObject> list, int low, int high)
    {
    	if (list == null || list.size() == 0){
			return;
    	}
 
		if (low >= high){
			return;
		}
 
		int middle = low + (high - low) / 2;
		GameObject pivot = list.get(middle);
 
		int i = low;
		int j = high;
		while (i <= j) {
			while (list.get(i).getPosition().y > pivot.getPosition().y) {
				i++;
			}
 
			while (list.get(j).getPosition().y < pivot.getPosition().y) {
				j--;
			}
 
			if (i <= j) {
				Collections.swap(list, i, j);
				i++;
				j--;
			}
		}
		if (low < j)
			qSortY(list, low, j);
 
		if (high > i)
			qSortY(list, i, high);
    }
}
