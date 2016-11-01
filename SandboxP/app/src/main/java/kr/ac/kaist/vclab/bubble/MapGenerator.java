package kr.ac.kaist.vclab.bubble;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avantgarde on 2016-11-02.
 */

public class MapGenerator {
    // size
    float sizeX;
    float sizeY;
    float sizeZ;

    // dimension
    int dimX;
    int dimZ;

    // unit length
    float unit;

    // height map (2D array of y values)
    float[][] heightMap;

    // flag for filling the faces
    boolean fill;

    public MapGenerator(float sizeX, float sizeY, float sizeZ, float unit, boolean fill) {
        this.unit = unit;
        this.fill = fill;

        this.dimX = (int) (sizeX / this.unit);
        this.dimZ = (int) (sizeZ / this.unit);

        this.sizeX = this.dimX * this.unit;
        this.sizeZ = this.dimZ * this.unit;
        this.sizeY = sizeY;

        heightMap = new float[this.dimX + 1][this.dimZ + 1];

        for (int i = 0; i <= this.dimX; i++) {
            for (int j = 0; j <= this.dimZ; j++) {
                heightMap[i][j] = 0;
            }
        }
    }

    public float[] getVertices() {
        List<float[]> buffer = new ArrayList<>();

        // simple smooth surface for test
        for (int i = 0; i <= dimX; i++) {
            for (int j = 0; j <= dimZ; j++) {
                heightMap[i][j] = (float) Math.sqrt(Math.sin(3.1415f / (2.0f * (i + j + 1))));
            }
        }

        if (fill) {
            // top
            for (int i = 0; i < dimX; i++) {
                for (int j = 0; j < dimZ; j++) {
                    // lower triangle
                    buffer.add(new float[]{
                            unit * i, heightMap[i][j], unit * j,
                            unit * i, heightMap[i][j + 1], unit * (j + 1),
                            unit * (i + 1), heightMap[i + 1][j + 1], unit * (j + 1)
                    });

                    // upper triangle
                    buffer.add(new float[]{
                            unit * i, heightMap[i][j], unit * j,
                            unit * (i + 1), heightMap[i + 1][j + 1], unit * (j + 1),
                            unit * (i + 1), heightMap[i + 1][j], unit * j
                    });
                }
            }

            // bottom
            buffer.add(new float[]{
                    sizeX, -sizeY, sizeZ,
                    0, -sizeY, 0,
                    sizeX, -sizeY, 0
            });

            buffer.add(new float[]{
                    0, -sizeY, 0,
                    sizeX, -sizeY, sizeZ,
                    0, -sizeY, sizeZ
            });

            // front
            buffer.add(new float[]{
                    0, heightMap[0][dimZ], sizeZ,
                    0, -sizeY, sizeZ,
                    sizeX, -sizeY, sizeZ
            });

            buffer.add(new float[]{
                    0, heightMap[0][dimZ], sizeZ,
                    sizeX, -sizeY, sizeZ,
                    sizeX, heightMap[dimX][dimZ], sizeZ
            });

            // back
            buffer.add(new float[]{
                    0, heightMap[0][0], 0,
                    sizeX, heightMap[dimX][0], 0,
                    sizeX, -sizeY, 0
            });

            buffer.add(new float[]{
                    0, heightMap[0][0], 0,
                    sizeX, -sizeY, 0,
                    0, -sizeY, 0
            });

            // left
            buffer.add(new float[]{
                    0, heightMap[0][dimZ], sizeZ,
                    0, heightMap[0][0], 0,
                    0, -sizeY, sizeZ
            });

            buffer.add(new float[]{
                    0, -sizeY, sizeZ,
                    0, heightMap[0][0], 0,
                    0, -sizeY, 0
            });

            // right
            buffer.add(new float[]{
                    sizeX, heightMap[dimX][dimZ], sizeZ,
                    sizeX, -sizeY, sizeZ,
                    sizeX, heightMap[dimX][0], 0
            });

            buffer.add(new float[]{
                    sizeX, heightMap[dimX][0], 0,
                    sizeX, -sizeY, sizeZ,
                    sizeX, -sizeY, 0
            });

            return listToArray(buffer, 9);
        } else {
            // top
            for (int i = 0; i <= dimX; i++) {
                for (int j = 0; j <= dimZ; j++) {
                    // rows
                    if (i != dimX) {
                        buffer.add(new float[]{
                                unit * i, heightMap[i][j], unit * j,
                                unit * (i + 1), heightMap[i + 1][j], unit * j
                        });
                    }

                    // columns
                    if (j != dimZ) {
                        buffer.add(new float[]{
                                unit * i, heightMap[i][j], unit * j,
                                unit * i, heightMap[i][j + 1], unit * (j + 1)
                        });
                    }

                    // diagonals
                    if ((i != dimX) && (j != dimZ)) {
                        buffer.add(new float[]{
                                unit * i, heightMap[i][j], unit * j,
                                unit * (i + 1), heightMap[i + 1][j + 1], unit * (j + 1)
                        });
                    }
                }
            }

            // bottom
            buffer.add(new float[]{
                    0, -sizeY, 0,
                    sizeX, -sizeY, 0
            });

            buffer.add(new float[]{
                    sizeX, -sizeY, 0,
                    sizeX, -sizeY, sizeZ
            });

            buffer.add(new float[]{
                    sizeX, -sizeY, sizeZ,
                    0, -sizeY, sizeZ
            });

            buffer.add(new float[]{
                    0, -sizeY, sizeZ,
                    0, -sizeY, 0
            });

            // sides
            buffer.add(new float[]{
                    0, heightMap[0][0], 0,
                    0, -sizeY, 0
            });

            buffer.add(new float[]{
                    sizeX, heightMap[dimX][0],
                    0, sizeX, -sizeY, 0
            });

            buffer.add(new float[]{
                    0, heightMap[0][dimZ],
                    sizeZ, 0, -sizeY, sizeZ
            });

            buffer.add(new float[]{
                    sizeX, heightMap[dimX][dimZ],
                    sizeZ, sizeX, -sizeY, sizeZ
            });

            return listToArray(buffer, 6);
        }
    }

    public float[] getNormals() {
        List<float[]> buffer = new ArrayList<>();

        if (fill) {
            // top
            for (int i = 0; i < dimX * dimZ * 6; i++) {
                buffer.add(new float[]{0.0f, 1.0f, 0.0f});
            }

            // bottom
            for (int i = 0; i < 2; i++) {
                buffer.add(new float[]{0.0f, -1.0f, 0.0f});
            }

            // front
            for (int i = 0; i < 2; i++) {
                buffer.add(new float[]{0.0f, 0.0f, 1.0f});
            }

            // back
            for (int i = 0; i < 2; i++) {
                buffer.add(new float[]{0.0f, 0.0f, -1.0f});
            }

            // left
            for (int i = 0; i < 2; i++) {
                buffer.add(new float[]{-1.0f, 0.0f, 0.0f});
            }

            // right
            for (int i = 0; i < 2; i++) {
                buffer.add(new float[]{1.0f, 0.0f, 0.0f});
            }
        } else {
            // top
            for (int i = 0; i < (dimX * (dimZ + 1) + (dimX + 1) * dimZ + dimX * dimZ) * 3; i++) {
                buffer.add(new float[]{0.0f, 1.0f, 0.0f});
            }

            // bottom
            for (int i = 0; i < 8; i++) {
                buffer.add(new float[]{0.0f, -1.0f, 0.0f});
            }

            // sides
            for (int i = 0; i < 8; i++) {
                buffer.add(new float[]{0.0f, 1.0f, 0.0f});
            }
        }

        return listToArray(buffer, 3);
    }

    public int getMode() {
        if (fill) {
            return GLES20.GL_TRIANGLES;
        } else {
            return GLES20.GL_LINES;
        }
    }

    private float[] listToArray(List<float[]> buffer, int elemSize) {
        int bufferSize = buffer.size();
        float[] result = new float[bufferSize * elemSize];

        for (int i = 0; i < bufferSize; i++) {
            float[] elem = buffer.get(i);

            for (int j = 0; j < elemSize; j++) {
                result[i * elemSize + j] = elem[j];
            }
        }

        return result;
    }
}
