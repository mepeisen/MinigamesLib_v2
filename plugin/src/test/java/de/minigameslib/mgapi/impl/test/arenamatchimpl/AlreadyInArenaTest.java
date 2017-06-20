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

package de.minigameslib.mgapi.impl.test.arenamatchimpl;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.impl.arena.ArenaMatchImpl;
import de.minigameslib.mgapi.impl.test.MglibTestHelper;

/**
 * Tests for invalid switching arenas.
 * 
 * @author mepeisen
 *
 */
public class AlreadyInArenaTest
{
    
    /**
     * Mock bukkit server
     */
    @Before
    public void mockServer()
    {
        final Server server = mock(Server.class);
        Whitebox.setInternalState(Bukkit.class, "server", server); //$NON-NLS-1$
        final PluginManager pluginManager = mock(PluginManager.class);
        when(server.getPluginManager()).thenReturn(pluginManager);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void testJoinDuplicate() throws McException
    {
        final ArenaPlayerInterface player = MglibTestHelper.createPlayer();
        when(player.inArena()).thenReturn(true);
        when(player.getArena()).thenReturn(mock(ArenaInterface.class));
        
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(mock(ArenaInterface.class), false);
        spmatch.join(player);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void testSpectateDuplicate() throws McException
    {
        final ArenaPlayerInterface player = MglibTestHelper.createPlayer();
        when(player.inArena()).thenReturn(true);
        when(player.getArena()).thenReturn(mock(ArenaInterface.class));
        
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(mock(ArenaInterface.class), false);
        spmatch.spectate(player);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void testJoinSameDuplicate() throws McException
    {
        final ArenaPlayerInterface player = MglibTestHelper.createPlayer();
        when(player.inArena()).thenReturn(true);
        final ArenaInterface arena = mock(ArenaInterface.class);
        when(player.getArena()).thenReturn(arena);
        
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        spmatch.join(player);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testSpectateSameDuplicate() throws McException
    {
        final ArenaPlayerInterface player = MglibTestHelper.createPlayer();
        when(player.inArena()).thenReturn(true);
        final ArenaInterface arena = mock(ArenaInterface.class);
        when(player.getArena()).thenReturn(arena);
        
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        spmatch.spectate(player);
    }
    
}
