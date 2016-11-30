package kr.ac.kaist.vclab.bubble;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avantgarde on 2016-11-24.
 */

public class ItemGenerator {
    // basic params
    private int count;
    private float minDist;
    private float heightOffset;

    // params from mMapGenerator
    private float unit;
    private float[][] heightMap;

    public ItemGenerator(
            int count,
            float minDist,
            float heightOffset,
            MapGenerator mMapGenerator) {
        // from arguments
        this.count = count;
        this.minDist = minDist;
        this.heightOffset = heightOffset;

        // from MapGenerator instance
        this.unit = mMapGenerator.unit;
        this.heightMap = mMapGenerator.heightMap;
    }

    public float[][] getPositions() {
        float[][] posList = new float[count][3];

        for (int i = 0; i < count; i++) {
            posList[i][0] = 5.0f * i;
            posList[i][1] = 20.0f;
            posList[i][2] = 6.0f * i;
        }

        return posList;
    }
}
