package kr.ac.kaist.vclab.bubble.generators;

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

    // params from mMapGenerator
    private float unit;
    private float[][] heightMap;
    private int dimX;
    private int dimZ;

    public ItemGenerator(
            int count,
            float radius,
            float minDist,
            float heightOffset,
            MapGenerator mMapGenerator) {
        // from arguments
        this.count = count;
        this.radius = radius;
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
        Random mRandomY = new Random();
        Random mRandomZ = new Random();
        int search = (int) (minDist / unit);

        for (int index = 0; index < count; index++) {
            int i = dimX / 8 + mRandomX.nextInt((dimX * 6) / 8);
            int j = dimZ / 8 + mRandomZ.nextInt((dimZ * 6) / 8);

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
