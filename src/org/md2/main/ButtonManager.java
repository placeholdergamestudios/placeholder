package org.md2.main;

import org.md2.input.Button;

import java.util.ArrayList;

public class ButtonManager
{
    private ArrayList<Button> buttons;

    public ButtonManager()
    {
        buttons = new ArrayList<Button>();
    }

    public void registerButton(Button b)
    {
        buttons.add(b);
    }

    public void refreshButtons()
    {
        for (Button b: buttons) {
            b.refresh();
        }
    }
}
