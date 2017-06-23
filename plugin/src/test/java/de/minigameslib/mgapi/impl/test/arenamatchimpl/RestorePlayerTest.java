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
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.impl.arena.ArenaMatchImpl;
import de.minigameslib.mgapi.impl.arena.ArenaPlayerImpl;
import de.minigameslib.mgapi.impl.test.MglibTestHelper;

/**
 * Tests player restore.
 * 
 * @author mepeisen
 *
 */
public class RestorePlayerTest
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
     * Initialize players before match.
     * @throws McException 
     */
    @Test
    public void testInitBeforeMatchOffline() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        when(player1.isOnline()).thenReturn(false);
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).resetPlayerData(false);
    }
    
    /**
     * Initialize players before match.
     * @throws McException 
     */
    @Test
    public void testInitBeforeMatch2Offline() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        when(player1.isOnline()).thenReturn(false);
        spmatch.spectate(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).resetPlayerData(false);
    }
    
    /**
     * Initialize players before match.
     * @throws McException 
     */
    @Test
    public void testInitWithinMatchOffline() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        when(player1.isOnline()).thenReturn(false);
        spmatch.start();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).resetPlayerData(false);
    }
    
    /**
     * Initialize players before match.
     * @throws McException 
     */
    @Test
    public void testInitWithinMatch2Offline() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        when(player1.isOnline()).thenReturn(false);
        spmatch.start();
        spmatch.spectate(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).resetPlayerData(false);
    }
    
    /**
     * Initialize players before match.
     * @throws McException 
     */
    @Test
    public void testInitWithinMatch3Offline() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        when(player1.isOnline()).thenReturn(false);
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.spectate(player1);
        spmatch.leave(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).resetPlayerData(false);
    }
    
    /**
     * Initialize players before match.
     * @throws McException 
     */
    @Test
    public void testInitWithinMatch4Offline() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        when(player1.isOnline()).thenReturn(true);
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.spectate(player1);
        spmatch.leave(player1);
        spmatch.start();
        final ArenaPlayerInterface player2 = MglibTestHelper.createPlayer(player1.getPlayerUUID());
        when(player2.isOnline()).thenReturn(false);
        spmatch.join(player2);
        when(player2.inArena()).thenReturn(true);
        when(player2.getArena()).thenReturn(arena);
        spmatch.leave(player2);
        
        verify((ArenaPlayerImpl)player1, times(1)).resetPlayerData(true);
        
        verify((ArenaPlayerImpl)player2, times(1)).resetPlayerData(false);
    }
    
    /**
     * Initialize players before match.
     * @throws McException 
     */
    @Test
    public void testInitBeforeMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        when(player1.isOnline()).thenReturn(true);
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).resetPlayerData(true);
    }
    
    /**
     * Initialize players before match.
     * @throws McException 
     */
    @Test
    public void testInitBeforeMatch2() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        when(player1.isOnline()).thenReturn(true);
        spmatch.spectate(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).resetPlayerData(true);
    }
    
    /**
     * Initialize players before match.
     * @throws McException 
     */
    @Test
    public void testInitWithinMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        when(player1.isOnline()).thenReturn(true);
        spmatch.start();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).resetPlayerData(true);
    }
    
    /**
     * Initialize players before match.
     * @throws McException 
     */
    @Test
    public void testInitWithinMatch2() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        when(player1.isOnline()).thenReturn(true);
        spmatch.start();
        spmatch.spectate(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).resetPlayerData(true);
    }
    
    /**
     * Initialize players before match.
     * @throws McException 
     */
    @Test
    public void testInitWithinMatch3() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        when(player1.isOnline()).thenReturn(true);
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.spectate(player1);
        spmatch.leave(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).resetPlayerData(true);
    }
    
    /**
     * Initialize players before match.
     * @throws McException 
     */
    @Test
    public void testInitWithinMatch4() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        when(player1.isOnline()).thenReturn(true);
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.spectate(player1);
        spmatch.leave(player1);
        spmatch.start();
        final ArenaPlayerInterface player2 = MglibTestHelper.createPlayer(player1.getPlayerUUID());
        when(player2.isOnline()).thenReturn(true);
        spmatch.join(player2);
        when(player2.inArena()).thenReturn(true);
        when(player2.getArena()).thenReturn(arena);
        spmatch.leave(player2);
        
        verify((ArenaPlayerImpl)player1, times(1)).resetPlayerData(true);
        
        verify((ArenaPlayerImpl)player2, times(1)).resetPlayerData(true);
    }
    
}
