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
import com.jme3.terrain.geomipmap.LRUCache;
import com.jme3.terrain.geomipmap.TerrainGrid;
import com.jme3.terrain.geomipmap.TerrainGridListener;
import com.jme3.terrain.geomipmap.TerrainQuad;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.NotificationBroadcasterSupport;

/**
 *
 * @author Johan Maasing <johan@zoom.nu>
 */
public class JMETerrainGridInspector extends NotificationBroadcasterSupport implements JMETerrainGridInspectorMBean, TerrainGridListener {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final TerrainGrid terrainGrid;
    private final LRUCache<String, TerrainQuadInformation> cache =
            new LRUCache<String, TerrainQuadInformation>(20);

    public JMETerrainGridInspector(TerrainGrid terrainGrid) {
        this.terrainGrid = terrainGrid;
    }

    @Override
    public void gridMoved(Vector3f newCenter) {
    }

    @Override
    public void tileAttached(Vector3f cell, TerrainQuad quad) {
        final String cacheKey = createCacheKey(cell);
        createCacheEntry(cell, quad, cacheKey);
        log.log(Level.FINER,
                "Cache key {0} used to put quad {1} in cache",
                new Object[]{cacheKey, quad});
    }

    @Override
    public void tileDetached(Vector3f cell, TerrainQuad quad) {
    }

    @Override
    public TerrainQuadInformation getTerrainQuadInformation() {

        final Vector3f tileCell = this.terrainGrid.getCurrentCell();
        final String cacheKey = createCacheKey(tileCell);
        TerrainQuadInformation cachedTile = this.cache.get(cacheKey);
        if (cachedTile == null) {
            log.log(Level.FINER,
                    "Cache key {0} not found in cache, trying to read directly from grid",
                    cacheKey);
            cachedTile = readQuadFromGridAndCacheIt(cacheKey, tileCell);
        }
        log.log(Level.FINER,
                "Cache key {0} retrieved cached array {1}",
                new Object[]{cacheKey, cachedTile});
        return cachedTile;
    }

    private String createCacheKey(final Vector3f tileCell) {
        return tileCell.x + "|" + tileCell.z;
    }

    private TerrainQuadInformation createCacheEntry(
            final Vector3f cell,
            final TerrainQuad quad,
            final String cacheKey) {
        TerrainQuadInformation information =
                new TerrainQuadInformation(
                quad.getHeightMap(),
                cell,
                quad.getTerrainSize());
        this.cache.put(cacheKey, information);
        return information;
    }

    private TerrainQuadInformation readQuadFromGridAndCacheIt(
            final String cacheKey,
            final Vector3f tileCell) {
        TerrainQuad terrainQuadAt =
                this.terrainGrid.getGridTileLoader().getTerrainQuadAt(tileCell);
        return createCacheEntry(tileCell, terrainQuadAt, cacheKey);
    }
}
