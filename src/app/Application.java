package app;

import main.Gui;

/**
 * Created by hrant on 3/5/16.
 */
public class Application {

    private static Gui gGui;
    private static int mNo = 0;

    public static boolean createGUI() {
        gGui = new Gui();
        return true;
    }

    public static Gui getGui() {
        return gGui;
    }

    public static int getNo(){
        return mNo++;
    }
}
