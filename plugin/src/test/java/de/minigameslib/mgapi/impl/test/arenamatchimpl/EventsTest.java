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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedSpectatorsEvent;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.impl.arena.ArenaMatchImpl;
import de.minigameslib.mgapi.impl.test.CheckedCaptor;
import de.minigameslib.mgapi.impl.test.MglibTestHelper;

/**
 * Tests for several events.
 * 
 * @author mepeisen
 *
 */
public class EventsTest
{
    
    /** plugin manager. */
    private PluginManager pluginManager;

    /**
     * Mock bukkit server
     * @throws ClassNotFoundException 
     */
    @Before
    public void mockServer() throws ClassNotFoundException
    {
        final Server server = mock(Server.class);
        Whitebox.setInternalState(Bukkit.class, "server", server); //$NON-NLS-1$
        this.pluginManager = mock(PluginManager.class);
        when(server.getPluginManager()).thenReturn(this.pluginManager);
        
        MglibTestHelper.initPlaceholdersDummy();
    }
    
    /**
     * Checks if join spectators event is fired.
     * @throws McException 
     */
    @Test
    public void spectateBeforeMatchFired() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.spectate(player1);
        
        final CheckedCaptor<ArenaPlayerJoinedSpectatorsEvent> evt = CheckedCaptor.forClass(ArenaPlayerJoinedSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt.capture());
        
        assertSame(player1, evt.getValue().getArenaPlayer());
        assertSame(arena, evt.getValue().getArena());
        assertFalse(evt.getValue().isPlayedBefore());
    }
    
    /**
     * Checks if join spectators event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinSpectatorsWithinMatchFired() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.join(player1);
        spmatch.start();
        
        spmatch.spectate(player1);
        
        final CheckedCaptor<ArenaPlayerJoinedSpectatorsEvent> evt = CheckedCaptor.forClass(ArenaPlayerJoinedSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt.capture());
        
        assertSame(player1, evt.getValue().getArenaPlayer());
        assertSame(arena, evt.getValue().getArena());
        assertTrue(evt.getValue().isPlayedBefore());
    }
    
    /**
     * Checks if join spectators event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinSpectatorsWithinMatchFired2() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.start();
        
        spmatch.spectate(player1);
        
        final CheckedCaptor<ArenaPlayerJoinedSpectatorsEvent> evt = CheckedCaptor.forClass(ArenaPlayerJoinedSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt.capture());
        
        assertSame(player1, evt.getValue().getArenaPlayer());
        assertSame(arena, evt.getValue().getArena());
        assertFalse(evt.getValue().isPlayedBefore());
    }
    
    /**
     * Checks if join spectators event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinSpectatorsWithinMatchFired3() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        spmatch.start();

        final ArenaPlayerInterface player2 = MglibTestHelper.createPlayer(player1.getPlayerUUID());
        spmatch.spectate(player2);
        
        final CheckedCaptor<ArenaPlayerJoinedSpectatorsEvent> evt = CheckedCaptor.forClass(ArenaPlayerJoinedSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt.capture());
        
        assertSame(player2, evt.getValue().getArenaPlayer());
        assertSame(arena, evt.getValue().getArena());
        assertFalse(evt.getValue().isPlayedBefore());
    }
    
}
