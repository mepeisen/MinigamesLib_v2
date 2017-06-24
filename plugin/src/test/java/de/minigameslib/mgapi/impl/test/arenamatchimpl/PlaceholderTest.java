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
import de.minigameslib.mclib.api.locale.MessageServiceInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.impl.arena.ArenaMatchImpl;
import de.minigameslib.mgapi.impl.test.MglibTestHelper;

/**
 * @author mepeisen
 *
 */
public class PlaceholderTest
{
    
    /** plugin manager. */
    private PluginManager pluginManager;
    
    /** message service interface. */
    private MessageServiceInterface msi;

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

        this.msi = mock(MessageServiceInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.locale.MessageServiceCache"), "SERVICES", this.msi); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Tests updating curPlayers
     * @throws McException 
     */
    @Test
    public void testCurPlayersUpdateForSpec() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.spectate(player1);
        
        // not be fired
        verify(this.msi, times(0)).notifyPlaceholderChanges(
                MglibTestHelper.phEq(new String[][] { { "mg2", "arena", "curplayers" }})); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Tests updating curPlayers
     * @throws McException 
     */
    @Test
    public void testCurPlayersUpdateForJoin() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        
        // fired for join
        verify(this.msi, times(1)).notifyPlaceholderChanges(
                MglibTestHelper.phEq(new String[][] { { "mg2", "arena", "curplayers" }})); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Tests updating curPlayers
     * @throws McException 
     */
    @Test
    public void testCurPlayersUpdateForWin() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.start();
        spmatch.join(player1);
        spmatch.setWinner(player1.getPlayerUUID());
        
        // fired for join and for winning
        verify(this.msi, times(2)).notifyPlaceholderChanges(
                MglibTestHelper.phEq(new String[][] { { "mg2", "arena", "curplayers" }})); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Tests updating curPlayers
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void testMPCurPlayersUpdateForWin() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.getOrCreate(CommonTeams.Aqua);
        spmatch.start();
        spmatch.join(player1);
        spmatch.setWinner(CommonTeams.Aqua);
        
        // fired for join and for winning
        verify(this.msi, times(2)).notifyPlaceholderChanges(
                MglibTestHelper.phEq(new String[][] { { "mg2", "arena", "curplayers" }})); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Tests updating curPlayers
     * @throws McException 
     */
    @Test
    public void testCurPlayersUpdateForLose() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.start();
        spmatch.join(player1);
        spmatch.setLoser(player1.getPlayerUUID());
        
        // fired for join and for winning
        verify(this.msi, times(2)).notifyPlaceholderChanges(
                MglibTestHelper.phEq(new String[][] { { "mg2", "arena", "curplayers" }})); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Tests updating curPlayers
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void testMPCurPlayersUpdateForLose() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.getOrCreate(CommonTeams.Aqua);
        spmatch.start();
        spmatch.join(player1);
        spmatch.setLoser(CommonTeams.Aqua);
        
        // fired for join and for winning
        verify(this.msi, times(2)).notifyPlaceholderChanges(
                MglibTestHelper.phEq(new String[][] { { "mg2", "arena", "curplayers" }})); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Tests updating curPlayers
     * @throws McException 
     */
    @Test
    public void testCurPlayersUpdateForLeave() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        // fired for join and for leave
        verify(this.msi, times(2)).notifyPlaceholderChanges(
                MglibTestHelper.phEq(new String[][] { { "mg2", "arena", "curplayers" }})); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Tests updating curPlayers
     * @throws McException 
     */
    @Test
    public void testCurPlayersUpdateForSpecAfterJoin() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.spectate(player1);
        
        // fired for join and spec
        verify(this.msi, times(2)).notifyPlaceholderChanges(
                MglibTestHelper.phEq(new String[][] { { "mg2", "arena", "curplayers" }})); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
}
