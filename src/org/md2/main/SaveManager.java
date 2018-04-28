package org.md2.main;

import org.md2.common.In;

import javax.sound.sampled.Line;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveManager
{
    private static final int SAVETYPEINT = 1;
    private static final int SAVETYPESTRING = 2;
    private static final int SAVETYPEFLOAT = 3;
    private static final int VERSION = 1;

    public SaveManager()
    {

    }

    // Determine in which order which information gets saved/loaded
    private Information [] getInformationArray()
    {
        Information [] infos = new Information[3];
        infos [0] = new Information(VERSION);
        infos [1] = new Information("Testen des Save Systems");
        infos [2] = new Information(1.234f);
        return infos;
    }

    // Determine how the information read from the file should be reimplanted to the gamememory (Gegenstück zu getTypeLayout)
    private void setInformation(Information [] i)
    {
        /*System.out.println(i [1].getSTRING());
        System.out.println(i [2].getFLOAT());*/
    }

    public void save(String filename)
    {
        Information [] infos = getInformationArray();
        try
        {
            PrintWriter writer = new PrintWriter(filename + ".placeholder", "UTF-8");
            for(Information i : infos)
            {
                switch(i.getId())
                {
                    case SAVETYPEINT:{writer.print(i.getINT() + " ");break;}
                    case SAVETYPESTRING:{writer.print(i.getSTRING() + " ");break;}
                    case SAVETYPEFLOAT:{writer.print(i.getFLOAT() + " ");break;}
                }
            }
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void load(String filename)
    {
        Information [] infos = getInformationArray();
        In.open(filename + ".placeholder");
        for(Information i : infos)
        {
            if(!i.equals(infos[0]))
             {
                switch(i.getId())
                {
                    case SAVETYPEINT:{
                        int buffer = In.readInt();
                        if(In.done())
                        {
                            i.setINT(buffer);
                        }
                        break;
                    }
                    case SAVETYPESTRING:{
                        String buffer = In.readString();
                        if(In.done())
                        {
                            i.setSTRING(buffer);
                        }
                        break;
                    }
                    case SAVETYPEFLOAT:{
                        Float buffer = In.readFloat();
                        if(In.done())
                        {
                            i.setFLOAT(buffer);
                        }
                        break;
                    }
                }
            }
            else
            {
                if(In.readInt()!=VERSION)
                    break;
            }

        }
        In.close();
        setInformation(infos);
    }

    private class Information
    {
        private int INT;
        private float FLOAT;
        private String STRING;
        private int id;

        public Information(String settonull, int type)
        {
            id = type;
        }

        public Information(int INT)
        {
            this.INT = INT;
            id = SAVETYPEINT;
        }

        public Information(float FLOAT)
        {
            this.FLOAT = FLOAT;
            id = SAVETYPEFLOAT;
        }

        public Information(String STRING)
        {
            this.STRING = STRING;
            id = SAVETYPESTRING;

        }

        public void setFLOAT(float FLOAT) {
            if(id == SAVETYPEFLOAT)
                this.FLOAT = FLOAT;
        }

        public void setINT(int INT) {
            if(id == SAVETYPEINT)
                this.INT = INT;
        }

        public void setSTRING(String STRING) {
            if(id == SAVETYPESTRING)
             this.STRING = STRING;
        }

        public int getId() {
            return id;
        }

        public int getINT() {
            return INT;
        }

        public float getFLOAT() {
            return FLOAT;
        }

        public String getSTRING() {
            return STRING;
        }
    }
}
