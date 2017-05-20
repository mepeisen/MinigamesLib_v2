/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.minigameslib.mgapi.impl.test;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.withSettings;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.HashMap;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.junit.Before;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.impl.MinigamesPlugin;
import de.minigameslib.mgapi.impl.cmd.Mg2Command;

/**
 * @author mepeisen
 *
 */
public class AbstractMinigamesTest
{
    
    /**
     * the plugin manager mock.
     */
    protected PluginManager pluginManager;
    
    /**
     * Initializing.
     */
    @Before
    public void init()
    {
        final Server server = mock(Server.class);
        Whitebox.setInternalState(Bukkit.class, "server", server); //$NON-NLS-1$
        final ServicesManager servicesManager = mock(ServicesManager.class);
        when(server.getServicesManager()).thenReturn(servicesManager);
        this.pluginManager = mock(PluginManager.class);
        when(server.getPluginManager()).thenReturn(this.pluginManager);
        
        final MinigamesPlugin mglib = mock(MinigamesPlugin.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        Whitebox.setInternalState(mglib, "minigamesPerPlugin", new HashMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "minigamesPerName", new TreeMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "extensionsPerPlugin", new HashMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "extensionsPerName", new TreeMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "mg2Command", new Mg2Command()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "arenasPerName", new TreeMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "ruleSetsPerPlugin", new HashMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "arenaRuleSetTypes", new HashMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "zoneRuleSetTypes", new HashMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "componentRuleSetTypes", new HashMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "signRuleSetTypes", new HashMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "componentsPerPlugin", new HashMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "components", new HashMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "zonesPerPlugin", new HashMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "zones", new HashMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "signsPerPlugin", new HashMap<>()); //$NON-NLS-1$
        Whitebox.setInternalState(mglib, "signs", new HashMap<>()); //$NON-NLS-1$
        when(servicesManager.load(MinigamesLibInterface.class)).thenReturn(mglib);
        
       //  mglib.onEnable();
    }
    
}
