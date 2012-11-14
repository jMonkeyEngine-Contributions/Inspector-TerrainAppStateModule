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
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
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

    public JMETerrainGridInspector(TerrainGrid terrainGrid) {
        this.terrainGrid = terrainGrid;
    }

    @Override
    public void gridMoved(Vector3f newCenter) {
    }

    @Override
    public void tileAttached(Vector3f cell, TerrainQuad quad) {
        // Do nothing. We'll keep this callback for now, it'll be used to track
        // statistics about attached/detached quads
    }

    @Override
    public void tileDetached(Vector3f cell, TerrainQuad quad) {
    }

    @Override
    public TerrainQuadInformation getTerrainQuadInformation() {

        final Vector3f cell = this.terrainGrid.getCurrentCell();
        if (cell != null) {
            final Terrain terrain =
                    this.terrainGrid.getTerrainAtCell(cell);
            if (terrain != null) {
                String name = "<null>";
                if (terrain instanceof Spatial) {
                    name = ((Spatial) terrain).getName();
        }
                TerrainQuadInformation information = new TerrainQuadInformation(
                        terrain.getHeightMap(),
                cell,
                        terrain.getTerrainSize(),
                        name);
        return information;
            } else {
                log.log(Level.WARNING, "Grid returned null for cell {0}", cell);
                return null;
    }

        } else {
            log.log(Level.WARNING, "Grid returned null for current cell");
            return null;
        }
    }
}
