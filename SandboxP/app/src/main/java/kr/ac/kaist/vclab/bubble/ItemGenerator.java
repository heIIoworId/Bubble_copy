package kr.ac.kaist.vclab.bubble;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private int dimX;
    private int dimZ;

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
        this.dimX = mMapGenerator.dimX;
        this.dimZ = mMapGenerator.dimZ;
    }

    public float[][] getPositions() {
        float[][] posList = new float[count][3];
        Random mRandomX = new Random();
        Random mRandomZ = new Random();

        for (int i = 0; i < count; i++) {
            int j = mRandomX.nextInt(dimX);
            int k = mRandomZ.nextInt(dimZ);

            posList[i][0] = unit * j;
            posList[i][1] = Math.max(heightMap[j][k] + 4.0f, 4.0f);
            posList[i][2] = unit * k;
        }

        return posList;
    }
}
