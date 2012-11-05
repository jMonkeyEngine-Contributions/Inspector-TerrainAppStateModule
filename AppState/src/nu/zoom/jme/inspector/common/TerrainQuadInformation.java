/*
 * Copyright (c) 2012, "Johan Maasing" <johan@zoom.nu>
 * All rights reserved.

 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package nu.zoom.jme.inspector.common;

import com.jme3.math.Vector3f;
import java.io.Serializable;

/**
 * Information extracted from a TerrainQuad and sent from the server to the
 * client.
 *
 * @author Johan Maasing
 */
public class TerrainQuadInformation implements Serializable {

    private static final long serialVersionUID = -1;
    private final float[] heightmap;
    private final Vector3f cell;
    private final int size;

    public TerrainQuadInformation(
            final float[] heightmap,
            final Vector3f cell,
            final int size) {
        this.heightmap = heightmap;
        this.cell = cell;
        this.size = size;
    }

    /**
     * Get the heightmap used by the terrain quad.
     *
     * @see com.jme3.terrain.geomipmap.TerrainQuad#getHeightMap()
     *
     * @return The heightmap, might be null or empty.
     */
    public float[] getHeightmap() {
        return heightmap;
    }

    /**
     * The terrain quad "ID" in the terrain grid. This is retreived from the
     * TerrainGridListener when a new terrain quad is loaded by the terrain
     * grid.
     *
     * @see
     * com.jme3.terrain.geomipmap.TerrainGridListener#tileAttached(com.jme3.math.Vector3f,
     * com.jme3.terrain.geomipmap.TerrainQuad)
     * @return The grid cell. Might be null.
     */
    public Vector3f getCell() {
        return cell;
    }

    /**
     * The number of data points in a column in the heightmap. The heightmap is
     * a float array of size*size number of elements.
     *
     * @see com.jme3.terrain.geomipmap.TerrainQuad#getTerrainSize()
     * @return
     */
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "TerrainQuadInformation{cell=" + cell
                + ", size= " + size
                + ", heightmap=" + heightmap + "}";
    }
}
