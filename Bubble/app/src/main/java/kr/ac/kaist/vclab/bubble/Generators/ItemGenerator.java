package kr.ac.kaist.vclab.bubble.Generators;

import java.util.Random;

/**
 * Created by avantgarde on 2016-11-24.
 */

public class ItemGenerator {
    // basic params
    private int count;
    private float radius;
    private float minDist;
    private float heightOffset;
    private float margin;

    // params from mMapGenerator
    private float unit;
    private float[][] heightMap;
    private int dimX;
    private int dimZ;

    public ItemGenerator(
            int count, // num. of items
            float radius, // items' radius
            float minDist, // min. distance between item and wall (recommended : minDist >= radius * 2)
            float heightOffset, // items' height are between (min. safe height) ~ (min. safe height) + heightOffset
            float margin, // items are generated within [margin * mapSizeX ~ (1 - margin) * mapSizeX, margin * mapSizeZ ~ (1 - margin) * mapSizeZ]
            MapGenerator mMapGenerator) {
        // from arguments
        this.count = count;
        this.radius = radius;
        this.minDist = minDist;
        this.heightOffset = heightOffset;
        this.margin = margin;

        // from MapGenerator instance
        this.unit = mMapGenerator.unit;
        this.heightMap = mMapGenerator.heightMap;
        this.dimX = mMapGenerator.dimX;
        this.dimZ = mMapGenerator.dimZ;
    }

    public float[][] getPositions() {
        float[][] posList = new float[count][3];
        Random mRandomX = new Random();
        Random mRandomY = new Random();
        Random mRandomZ = new Random();
        int search = (int) (minDist / unit);

        for (int index = 0; index < count; index++) {
            int i = (int) (dimX * margin) + mRandomX.nextInt((int) (dimX * (1.0f - margin * 2.0f)));
            int j = (int) (dimZ * margin) + mRandomZ.nextInt((int) (dimZ * (1.0f - margin * 2.0f)));

            // set x, z coors
            posList[index][0] = unit * i;
            posList[index][2] = unit * j;

            // find 'safe' y coor
            float safeY = getHeight(i, j);

            for (int k = 0; k <= search; k++) {
                for (int l = 0; l <= search; l++) {
                    safeY = Math.max(safeY, getHeight(i + search / 2 - k, j + search / 2 - l));
                }
            }

            if (safeY < 0) {
                safeY = 0;
            }

            posList[index][1] = safeY + radius + mRandomY.nextFloat() * heightOffset;
        }

        return posList;
    }

    private float getHeight(int i, int j) {
        if ((i < 0) || (j < 0) || (i >= dimX) || (j >= dimZ)) {
            return 0;
        } else {
            return heightMap[i][j];
        }
    }
}
