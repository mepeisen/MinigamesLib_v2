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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
import de.minigameslib.mgapi.api.match.MatchPlayerInterface;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.impl.arena.ArenaMatchImpl;
import de.minigameslib.mgapi.impl.test.MglibTestHelper;

/**
 * Tests player data (match player).
 * 
 * @author mepeisen
 *
 */
public class MatchPlayerTest
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
        MglibTestHelper.createLib();
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpSwitchBeforeMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.join(player1, CommonTeams.Black);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Black, mp.getTeam());
        assertTrue(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpJoinBeforeMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Aqua, mp.getTeam());
        assertTrue(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpLeaveBeforeMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNull(mp);
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpJoinWithinMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.start();
        spmatch.join(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Aqua, mp.getTeam());
        assertTrue(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpSwitchWithinMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.start();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.join(player1, CommonTeams.Black);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Black, mp.getTeam());
        assertTrue(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpJoinBeforeMatchAndPlay() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        spmatch.start();
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Aqua, mp.getTeam());
        assertTrue(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpJoinBeforeMatchAndPlayAndLeave() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Losers, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpPlayAndLose() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.setLoser(player1.getPlayerUUID());
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Losers, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpPlayAndLoseAndLeave() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.setLoser(player1.getPlayerUUID());
        spmatch.leave(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Losers, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpPlayAndWin() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.setWinner(player1.getPlayerUUID());
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Winners, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpPlayAndWinAndLeave() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.setWinner(player1.getPlayerUUID());
        spmatch.leave(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Winners, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpSpecBeforeMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.spectate(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Spectators, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpSpecWithinMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.start();
        spmatch.spectate(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Spectators, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpSpecBeforeMatchAndStart() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.spectate(player1);
        spmatch.start();
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Spectators, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpSpecBeforeMatch2() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.spectate(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Aqua, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpSpecBeforeMatchAndSwitch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.join(player1, CommonTeams.Black);
        spmatch.spectate(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Black, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpSpecBeforeMatchAndStart2() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.spectate(player1);
        spmatch.start();
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Aqua, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void mpSpecBeforeMatchAndStartAndSwitch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.join(player1, CommonTeams.Black);
        spmatch.spectate(player1);
        spmatch.start();
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Black, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void joinBeforeMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Unknown, mp.getTeam());
        assertTrue(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void leaveBeforeMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNull(mp);
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void joinWithinMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.start();
        spmatch.join(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Unknown, mp.getTeam());
        assertTrue(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void joinBeforeMatchAndPlay() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        spmatch.start();
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Unknown, mp.getTeam());
        assertTrue(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void joinBeforeMatchAndPlayAndLeave() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Losers, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void playAndLose() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.setLoser(player1.getPlayerUUID());
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Losers, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void playAndLoseAndLeave() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.setLoser(player1.getPlayerUUID());
        spmatch.leave(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Losers, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void playAndWin() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.setWinner(player1.getPlayerUUID());
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Winners, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void playAndWinAndLeave() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        spmatch.start();
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.setWinner(player1.getPlayerUUID());
        spmatch.leave(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Winners, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertFalse(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void specBeforeMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.spectate(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Spectators, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void specWithinMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.start();
        spmatch.spectate(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Spectators, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void specBeforeMatchAndStart() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.spectate(player1);
        spmatch.start();
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Spectators, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void specBeforeMatch2() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.spectate(player1);
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Unknown, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
    /**
     * Check match player.
     * @throws McException 
     */
    @Test
    public void specBeforeMatchAndStart2() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.spectate(player1);
        spmatch.start();
        
        final MatchPlayerInterface mp = spmatch.get(player1.getPlayerUUID());
        assertNotNull(mp);
        assertNotNull(mp.getJoined());
        assertNotNull(mp.getLeft());
        assertEquals(player1.getPlayerUUID(), mp.getPlayer());
        assertEquals(CommonTeams.Unknown, mp.getTeam());
        assertFalse(mp.isPlaying());
        assertTrue(mp.isSpec());
    }
    
}
