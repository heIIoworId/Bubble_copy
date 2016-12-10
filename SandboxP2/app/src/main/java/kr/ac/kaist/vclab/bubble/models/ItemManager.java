package kr.ac.kaist.vclab.bubble.models;

import kr.ac.kaist.vclab.bubble.environment.GameEnv;

/**
 * Created by mnswpr on 11/30/2016.
 */
public class ItemManager {


    public static Item[] items;

    private static ItemManager ourInstance = new ItemManager();
    public static ItemManager getInstance() {
        return ourInstance;
    }
    private ItemManager() {
        items = new Item[GameEnv.getInstance().numOfTotalItems];
    }

    public static void setItems(Item[] _items){
        items = new Item[GameEnv.getInstance().numOfTotalItems];
        items = _items;
    }
}
