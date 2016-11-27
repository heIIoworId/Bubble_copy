package kr.ac.kaist.vclab.bubble.environment;

import java.util.ArrayList;

import kr.ac.kaist.vclab.bubble.environment.GameEnv;
import kr.ac.kaist.vclab.bubble.models.Item;

/**
 * Created by mnswpr on 11/26/2016.
 */

public class ItemManager {

    private ArrayList<Item> items;

    public ItemManager(ArrayList<Item> _items){
        items = _items;
    }

    public void updateNumOfCurrentItem(){
        int num = 0;
        for(int i = 0; i<items.size(); i++){
            if(items.get(i).getHitstatus()){
                num++;
            }
        }
        GameEnv.getInstance().numOfAchievedItems = num;
    }
}
