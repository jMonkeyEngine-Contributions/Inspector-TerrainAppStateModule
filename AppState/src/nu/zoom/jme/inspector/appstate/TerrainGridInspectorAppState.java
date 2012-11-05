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
package nu.zoom.jme.inspector.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.Camera;
import com.jme3.terrain.geomipmap.TerrainGrid;
import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import nu.zoom.jme.inspector.common.JMETerrainGridInspector;
import nu.zoom.jme.inspector.common.JMXNames;

/**
 *
 * @author Johan Maasing <johan@zoom.nu>
 */
public class TerrainGridInspectorAppState extends AbstractAppState {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final JMETerrainGridInspector inspector;

    public TerrainGridInspectorAppState(TerrainGrid terrainGrid) {
        this.inspector = new JMETerrainGridInspector(terrainGrid);
        terrainGrid.addListener(this.inspector);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName name = new ObjectName(JMXNames.TERRAIN_INSPECTOR_OBJECTNAME);
            log.log(Level.FINE, "Registering MBean with name {0}", new Object[]{name.getCanonicalName()});
            platformMBeanServer.registerMBean(this.inspector, name);
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
        }
        super.initialize(stateManager, app);
    }

    @Override
    public void cleanup() {

        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName name = new ObjectName(JMXNames.TERRAIN_INSPECTOR_OBJECTNAME);
            log.log(Level.FINE, "Un-registering MBean with name {0}", new Object[]{name.getCanonicalName()});
            platformMBeanServer.unregisterMBean(name);
        } catch (InstanceNotFoundException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (MBeanRegistrationException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (MalformedObjectNameException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        super.cleanup();
    }
}
