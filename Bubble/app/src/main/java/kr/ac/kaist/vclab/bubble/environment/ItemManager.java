package kr.ac.kaist.vclab.bubble.environment;

import java.util.ArrayList;

import kr.ac.kaist.vclab.bubble.environment.GameEnv;
import kr.ac.kaist.vclab.bubble.models.Item;

/**
 * Created by mnswpr on 11/26/2016.
 */

public class ItemManager {

    private ArrayList<Item> items;

    // FIXME SG (ItemGenerator과 연결해서 renderer에서 구현)
    public ItemManager(float[] positions){
        items = new ArrayList<>();
        for(int i = 0; i<positions.length; i = i+3){
            float[] center = new float[3];
            center[0] = positions[i];
            center[1] = positions[i+1];
            center[2] = positions[i+2];
            Item tempItem  = new Item(center);
            items.add(tempItem);
        }
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
