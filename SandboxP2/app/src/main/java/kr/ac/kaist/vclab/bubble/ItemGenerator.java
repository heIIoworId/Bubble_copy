package kr.ac.kaist.vclab.bubble;

/**
 * Created by avantgarde on 2016-11-24.
 */

public class ItemGenerator {
    // basic params
    private float count;
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
        this.count = count;
        this.minDist = minDist;
        this.heightOffset = heightOffset;

        this.unit = mMapGenerator.unit;
        this.heightMap = mMapGenerator.heightMap;
    }

    public float[] getPositions() {
        return null;
    }
}
