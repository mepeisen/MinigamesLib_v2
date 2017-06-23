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
import org.bukkit.GameMode;
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
 * Tests player initialization.
 * 
 * @author mepeisen
 *
 */
public class InitPlayerTest
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
    public void testInitBeforeMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).storePlayerData();
        verify(player1.getBukkitPlayer().getInventory(), times(1)).clear();
        verify(player1.getBukkitPlayer(), times(1)).setGameMode(GameMode.SURVIVAL);
        verify(player1.getBukkitPlayer(), times(1)).setFoodLevel(20);
        verify(player1.getBukkitPlayer(), times(1)).setSaturation(5.0f);
        verify(player1.getBukkitPlayer(), times(1)).setExhaustion(0.0f);
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
        spmatch.spectate(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).storePlayerData();
        verify(player1.getBukkitPlayer().getInventory(), times(1)).clear();
        verify(player1.getBukkitPlayer(), times(1)).setGameMode(GameMode.SURVIVAL);
        verify(player1.getBukkitPlayer(), times(1)).setFoodLevel(20);
        verify(player1.getBukkitPlayer(), times(1)).setSaturation(5.0f);
        verify(player1.getBukkitPlayer(), times(1)).setExhaustion(0.0f);
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
        spmatch.start();
        spmatch.join(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).storePlayerData();
        verify(player1.getBukkitPlayer().getInventory(), times(1)).clear();
        verify(player1.getBukkitPlayer(), times(1)).setGameMode(GameMode.SURVIVAL);
        verify(player1.getBukkitPlayer(), times(1)).setFoodLevel(20);
        verify(player1.getBukkitPlayer(), times(1)).setSaturation(5.0f);
        verify(player1.getBukkitPlayer(), times(1)).setExhaustion(0.0f);
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
        spmatch.start();
        spmatch.spectate(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).storePlayerData();
        verify(player1.getBukkitPlayer().getInventory(), times(1)).clear();
        verify(player1.getBukkitPlayer(), times(1)).setGameMode(GameMode.SURVIVAL);
        verify(player1.getBukkitPlayer(), times(1)).setFoodLevel(20);
        verify(player1.getBukkitPlayer(), times(1)).setSaturation(5.0f);
        verify(player1.getBukkitPlayer(), times(1)).setExhaustion(0.0f);
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
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.spectate(player1);
        
        verify((ArenaPlayerImpl)player1, times(1)).storePlayerData();
        verify(player1.getBukkitPlayer().getInventory(), times(1)).clear();
        verify(player1.getBukkitPlayer(), times(1)).setGameMode(GameMode.SURVIVAL);
        verify(player1.getBukkitPlayer(), times(1)).setFoodLevel(20);
        verify(player1.getBukkitPlayer(), times(1)).setSaturation(5.0f);
        verify(player1.getBukkitPlayer(), times(1)).setExhaustion(0.0f);
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
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.spectate(player1);
        spmatch.leave(player1);
        spmatch.start();
        final ArenaPlayerInterface player2 = MglibTestHelper.createPlayer(player1.getPlayerUUID());
        spmatch.join(player2);
        
        verify((ArenaPlayerImpl)player1, times(1)).storePlayerData();
        verify(player1.getBukkitPlayer().getInventory(), times(1)).clear();
        verify(player1.getBukkitPlayer(), times(1)).setGameMode(GameMode.SURVIVAL);
        verify(player1.getBukkitPlayer(), times(1)).setFoodLevel(20);
        verify(player1.getBukkitPlayer(), times(1)).setSaturation(5.0f);
        verify(player1.getBukkitPlayer(), times(1)).setExhaustion(0.0f);
        
        verify((ArenaPlayerImpl)player2, times(1)).storePlayerData();
        verify(player2.getBukkitPlayer().getInventory(), times(1)).clear();
        verify(player2.getBukkitPlayer(), times(1)).setGameMode(GameMode.SURVIVAL);
        verify(player2.getBukkitPlayer(), times(1)).setFoodLevel(20);
        verify(player2.getBukkitPlayer(), times(1)).setSaturation(5.0f);
        verify(player2.getBukkitPlayer(), times(1)).setExhaustion(0.0f);
    }
    
}
