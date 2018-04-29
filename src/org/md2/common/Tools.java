package org.md2.common;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.md2.gameobjects.GameObject;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;


public class Tools 
{
	
	public static Vector4f getTransformationInventoryV1(int index, int maxIndex, float invSize, float slotSize)
	{
		int itemsPerRow = (int)Math.ceil(Math.sqrt(maxIndex));
		float corner;
		if ( (itemsPerRow & 1) == 0 )
			corner = itemsPerRow/2- 0.5f;
		else
			corner = itemsPerRow/2;
		float offset =  (invSize - slotSize*itemsPerRow)/(itemsPerRow+1) + slotSize;
		
		return new Vector4f((index-index/itemsPerRow*itemsPerRow-corner)*offset, (-index/itemsPerRow+corner)*offset, 0 , 1);
	}
	
	public static Vector4f getTransformationInventory(int x, int y, int slotsPerRow)
	{
		float corner = (slotsPerRow-1)/2;
		return new Vector4f(2*(x-corner), -2*(y-corner), 0, 1);
	}
	
	public static Vector2i getRevTransformationInventory(float xPos, float yPos, int slotsPerRow)
	{
		float corner = (slotsPerRow-1)/2;
		int x = Math.round(xPos/2+corner);
		int y = Math.round(-yPos/2+corner);
		
		if(x >= slotsPerRow || y >= slotsPerRow || x < 0 || y < 0)
			return new Vector2i();
		return new Vector2i(x, y);
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
	
	public static ByteBuffer loadTexture(String fileName)
	{
		try{
			PNGDecoder decoder = new PNGDecoder(Tools.class.getClass().getResourceAsStream(fileName));
			ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
			buffer.flip();
			return buffer;
		}
		catch(Exception e){
			System.out.println("Loading texture at "+fileName+" failed");
		}
			return null;
		
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
