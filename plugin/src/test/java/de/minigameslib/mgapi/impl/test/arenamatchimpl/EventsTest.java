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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.events.ArenaLoseEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinSpectatorsEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedSpectatorsEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedTeamEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerLeftEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerLeftSpectatorsEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerLeftTeamEvent;
import de.minigameslib.mgapi.api.events.ArenaWinEvent;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.team.CommonTeams;
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
        
        final CheckedCaptor<ArenaPlayerJoinSpectatorsEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        final CheckedCaptor<ArenaPlayerJoinedSpectatorsEvent> evt2 = CheckedCaptor.forClass(ArenaPlayerJoinedSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt2.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        assertFalse(evt1.getValue().isPlayedBefore());
        
        assertSame(player1, evt2.getValue().getArenaPlayer());
        assertSame(arena, evt2.getValue().getArena());
        assertFalse(evt2.getValue().isPlayedBefore());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     */
    @Test
    public void joinBeforeMatchFired() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        
        final CheckedCaptor<ArenaPlayerJoinEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        final CheckedCaptor<ArenaPlayerJoinedEvent> evt2 = CheckedCaptor.forClass(ArenaPlayerJoinedEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt2.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        
        assertSame(player1, evt2.getValue().getArenaPlayer());
        assertSame(arena, evt2.getValue().getArena());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void vetoBeforeMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                invocation.getArgumentAt(0, ArenaPlayerJoinEvent.class).setCancelled(CommonMessages.InternalError);
                return null;
            }}).when(this.pluginManager).callEvent(isA(ArenaPlayerJoinEvent.class));
        
        spmatch.join(player1);
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     */
    @Test
    public void joinUnknownTeamBeforeMatchFired() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        
        final CheckedCaptor<ArenaPlayerJoinedTeamEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinedTeamEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        assertEquals(CommonTeams.Unknown, evt1.getValue().getTeam());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinTeamBeforeMatch() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl teammatch = new ArenaMatchImpl(arena, true);
        teammatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib();
        teammatch.join(player1);
        
        final CheckedCaptor<ArenaPlayerJoinedTeamEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinedTeamEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        assertEquals(CommonTeams.Aqua, evt1.getValue().getTeam());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void switchTeamBeforeMatch() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl teammatch = new ArenaMatchImpl(arena, true);
        teammatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib();
        teammatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        teammatch.getOrCreate(CommonTeams.Black);
        teammatch.switchTeam(player1, CommonTeams.Black);
        
        final CheckedCaptor<ArenaPlayerJoinedTeamEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinedTeamEvent.class);
        verify(this.pluginManager, times(2)).callEvent(evt1.capture());
        
        final CheckedCaptor<ArenaPlayerLeftTeamEvent> evt2 = CheckedCaptor.forClass(ArenaPlayerLeftTeamEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt2.capture());
        
        assertSame(player1, evt1.getAllValues().get(0).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(0).getArena());
        assertEquals(CommonTeams.Aqua, evt1.getAllValues().get(0).getTeam());
        
        assertSame(player1, evt1.getAllValues().get(1).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(1).getArena());
        assertEquals(CommonTeams.Black, evt1.getAllValues().get(1).getTeam());
        
        assertSame(player1, evt2.getValue().getArenaPlayer());
        assertSame(arena, evt2.getValue().getArena());
        assertEquals(CommonTeams.Aqua, evt2.getValue().getTeam());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinTeamBeforeMatchOverride() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl teammatch = new ArenaMatchImpl(arena, true);
        teammatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib();
        
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                invocation.getArgumentAt(0, ArenaPlayerJoinEvent.class).setPreSelectedTeam(CommonTeams.Black);
                return null;
            }}).when(this.pluginManager).callEvent(isA(ArenaPlayerJoinEvent.class));
        
        teammatch.join(player1);
        
        final CheckedCaptor<ArenaPlayerJoinedTeamEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinedTeamEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        assertEquals(CommonTeams.Black, evt1.getValue().getTeam());
    }
    
    /**
     * Checks if event is not fired for unknown players
     * @throws McException 
     */
    @Test
    public void unknownPlayerLeaves() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        try
        {
            spmatch.leave(player1);
            fail("McExcepion not thrown"); //$NON-NLS-1$
        }
        catch (@SuppressWarnings("unused") McException ex)
        {
            // ignore (expected exception)
        }
        
        final CheckedCaptor<ArenaPlayerLeftEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerLeftEvent.class);
        verify(this.pluginManager, times(0)).callEvent(evt1.capture());
    }
    
    /**
     * Checks if event is fired for leaving players
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
        
        final CheckedCaptor<ArenaPlayerLeftEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerLeftEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
    }
    
    /**
     * Checks if event is fired for leaving players
     * @throws McException 
     */
    @Test
    public void leaveSpectatorsBeforeMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.spectate(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        final CheckedCaptor<ArenaPlayerLeftEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerLeftEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
    }
    
    /**
     * Checks if event is fired for leaving players
     * @throws McException 
     */
    @Test
    public void leaveInsideMatchAndPlaying() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.start();
        spmatch.leave(player1);
        
        final CheckedCaptor<ArenaPlayerLeftEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerLeftEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        
        final CheckedCaptor<ArenaLoseEvent> evt2 = CheckedCaptor.forClass(ArenaLoseEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt2.capture());
        
        assertArrayEquals(new UUID[]{player1.getPlayerUUID()}, evt2.getValue().getMatchResult().getPlayers().toArray(new UUID[0]));
        assertSame(arena, evt2.getValue().getArena());
    }
    
    /**
     * Checks if event is fired for leaving players
     * @throws McException 
     */
    @Test
    public void leaveInsideMatchAndSpectating() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.spectate(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.start();
        spmatch.leave(player1);
        
        final CheckedCaptor<ArenaPlayerLeftSpectatorsEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerLeftSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
    }
    
    /**
     * Checks if event is fired for leaving players
     * @throws McException 
     */
    @Test
    public void leaveInsideMatchAndPlayingAndSpectating() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.start();
        spmatch.spectate(player1);
        spmatch.leave(player1);
        
        final CheckedCaptor<ArenaPlayerLeftEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerLeftEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        
        final CheckedCaptor<ArenaPlayerLeftSpectatorsEvent> evt2 = CheckedCaptor.forClass(ArenaPlayerLeftSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt2.capture());
        
        assertSame(player1, evt2.getValue().getArenaPlayer());
        assertSame(arena, evt2.getValue().getArena());
    }
    
    /**
     * Checks if join spectators event is fired and vetoed
     * @throws McException expected (caused by veto)
     */
    @Test(expected = McException.class)
    public void vetoSpectateBeforeMatch() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                invocation.getArgumentAt(0, ArenaPlayerJoinSpectatorsEvent.class).setCancelled(CommonMessages.InternalError);
                return null;
            }}).when(this.pluginManager).callEvent(isA(ArenaPlayerJoinSpectatorsEvent.class));
        
        spmatch.spectate(player1);
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinWithinMatchFired2() throws McException, ClassNotFoundException
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
        spmatch.join(player2);
        
        final CheckedCaptor<ArenaPlayerJoinEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinEvent.class);
        verify(this.pluginManager, times(2)).callEvent(evt1.capture());
        
        final CheckedCaptor<ArenaPlayerJoinedEvent> evt2 = CheckedCaptor.forClass(ArenaPlayerJoinedEvent.class);
        verify(this.pluginManager, times(2)).callEvent(evt2.capture());
        
        assertSame(player1, evt1.getAllValues().get(0).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(0).getArena());
        assertSame(player2, evt1.getAllValues().get(1).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(1).getArena());

        assertSame(player1, evt2.getAllValues().get(0).getArenaPlayer());
        assertSame(arena, evt2.getAllValues().get(0).getArena());
        assertSame(player2, evt2.getAllValues().get(1).getArenaPlayer());
        assertSame(arena, evt2.getAllValues().get(1).getArena());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinWithinMatchFired() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.start();
        
        spmatch.join(player1);
        
        final CheckedCaptor<ArenaPlayerJoinEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        final CheckedCaptor<ArenaPlayerJoinedEvent> evt2 = CheckedCaptor.forClass(ArenaPlayerJoinedEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt2.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        
        assertSame(player1, evt2.getValue().getArenaPlayer());
        assertSame(arena, evt2.getValue().getArena());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test(expected = McException.class)
    public void vetoWithinMatch() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.start();
        
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                invocation.getArgumentAt(0, ArenaPlayerJoinEvent.class).setCancelled(CommonMessages.InternalError);
                return null;
            }}).when(this.pluginManager).callEvent(isA(ArenaPlayerJoinEvent.class));
        
        spmatch.join(player1);
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test(expected = McException.class)
    public void vetoWithinMatchFired2() throws McException, ClassNotFoundException
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
        
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                invocation.getArgumentAt(0, ArenaPlayerJoinEvent.class).setCancelled(CommonMessages.InternalError);
                return null;
            }}).when(this.pluginManager).callEvent(isA(ArenaPlayerJoinEvent.class));
        
        spmatch.join(player2);
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinUnknownTeamWithinMatchFired() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.start();
        
        spmatch.join(player1);
        
        final CheckedCaptor<ArenaPlayerJoinedTeamEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinedTeamEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        assertEquals(CommonTeams.Unknown, evt1.getValue().getTeam());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinTeamWithinMatchFired() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.start();
        
        spmatch.join(player1);
        
        final CheckedCaptor<ArenaPlayerJoinedTeamEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinedTeamEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        assertEquals(CommonTeams.Aqua, evt1.getValue().getTeam());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void switchTeamWithinMatchFired() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.start();
        
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.switchTeam(player1, CommonTeams.Black);
        
        final CheckedCaptor<ArenaPlayerJoinedTeamEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinedTeamEvent.class);
        verify(this.pluginManager, times(2)).callEvent(evt1.capture());
        
        final CheckedCaptor<ArenaPlayerLeftTeamEvent> evt2 = CheckedCaptor.forClass(ArenaPlayerLeftTeamEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt2.capture());
        
        assertSame(player1, evt1.getAllValues().get(0).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(0).getArena());
        assertEquals(CommonTeams.Aqua, evt1.getAllValues().get(0).getTeam());
        
        assertSame(player1, evt1.getAllValues().get(1).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(1).getArena());
        assertEquals(CommonTeams.Black, evt1.getAllValues().get(1).getTeam());
        
        assertSame(player1, evt2.getValue().getArenaPlayer());
        assertSame(arena, evt2.getValue().getArena());
        assertEquals(CommonTeams.Aqua, evt2.getValue().getTeam());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void switchWithinMatchFired2() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.switchTeam(player1, CommonTeams.Black);
        spmatch.leave(player1);
        spmatch.start();
        
        final ArenaPlayerInterface player2 = MglibTestHelper.createPlayer(player1.getPlayerUUID());
        spmatch.join(player2);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.switchTeam(player2, CommonTeams.Black);
        
        final CheckedCaptor<ArenaPlayerJoinedTeamEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinedTeamEvent.class);
        verify(this.pluginManager, times(4)).callEvent(evt1.capture());
        
        final CheckedCaptor<ArenaPlayerLeftTeamEvent> evt2 = CheckedCaptor.forClass(ArenaPlayerLeftTeamEvent.class);
        verify(this.pluginManager, times(2)).callEvent(evt2.capture());
        
        assertSame(player1, evt1.getAllValues().get(0).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(0).getArena());
        assertEquals(CommonTeams.Aqua, evt1.getAllValues().get(0).getTeam());
        
        assertSame(player1, evt1.getAllValues().get(1).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(1).getArena());
        assertEquals(CommonTeams.Black, evt1.getAllValues().get(1).getTeam());
        
        assertSame(player2, evt1.getAllValues().get(2).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(2).getArena());
        assertEquals(CommonTeams.Aqua, evt1.getAllValues().get(2).getTeam());
        
        assertSame(player2, evt1.getAllValues().get(3).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(3).getArena());
        assertEquals(CommonTeams.Black, evt1.getAllValues().get(3).getTeam());
        
        assertSame(player1, evt2.getAllValues().get(0).getArenaPlayer());
        assertSame(arena, evt2.getAllValues().get(0).getArena());
        assertEquals(CommonTeams.Aqua, evt2.getAllValues().get(0).getTeam());
        
        assertSame(player2, evt2.getAllValues().get(1).getArenaPlayer());
        assertSame(arena, evt2.getAllValues().get(1).getArena());
        assertEquals(CommonTeams.Aqua, evt2.getAllValues().get(1).getTeam());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinTeamWithinMatchFiredOverride() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.start();
        
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                invocation.getArgumentAt(0, ArenaPlayerJoinEvent.class).setPreSelectedTeam(CommonTeams.Black);
                return null;
            }}).when(this.pluginManager).callEvent(isA(ArenaPlayerJoinEvent.class));
        
        spmatch.join(player1);
        
        final CheckedCaptor<ArenaPlayerJoinedTeamEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinedTeamEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        assertEquals(CommonTeams.Black, evt1.getValue().getTeam());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinUnknownTeamWithinMatchFired2() throws McException, ClassNotFoundException
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
        spmatch.join(player2);
        
        final CheckedCaptor<ArenaPlayerJoinedTeamEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinedTeamEvent.class);
        verify(this.pluginManager, times(2)).callEvent(evt1.capture());

        assertSame(player1, evt1.getAllValues().get(0).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(0).getArena());
        assertEquals(CommonTeams.Unknown, evt1.getAllValues().get(0).getTeam());
        assertSame(player2, evt1.getAllValues().get(1).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(1).getArena());
        assertEquals(CommonTeams.Unknown, evt1.getAllValues().get(1).getTeam());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinTeamWithinMatchFired2() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        spmatch.start();
        
        final ArenaPlayerInterface player2 = MglibTestHelper.createPlayer(player1.getPlayerUUID());
        spmatch.join(player2);
        
        final CheckedCaptor<ArenaPlayerJoinedTeamEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinedTeamEvent.class);
        verify(this.pluginManager, times(2)).callEvent(evt1.capture());

        assertSame(player1, evt1.getAllValues().get(0).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(0).getArena());
        assertEquals(CommonTeams.Aqua, evt1.getAllValues().get(0).getTeam());
        assertSame(player2, evt1.getAllValues().get(1).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(1).getArena());
        assertEquals(CommonTeams.Aqua, evt1.getAllValues().get(1).getTeam());
    }
    
    /**
     * Checks if join event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void joinTeamWithinMatchFired2Override() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        spmatch.start();
        
        final ArenaPlayerInterface player2 = MglibTestHelper.createPlayer(player1.getPlayerUUID());
        
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                invocation.getArgumentAt(0, ArenaPlayerJoinEvent.class).setPreSelectedTeam(CommonTeams.Black);
                return null;
            }}).when(this.pluginManager).callEvent(isA(ArenaPlayerJoinEvent.class));
        
        spmatch.join(player2);
        
        final CheckedCaptor<ArenaPlayerJoinedTeamEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinedTeamEvent.class);
        verify(this.pluginManager, times(2)).callEvent(evt1.capture());

        assertSame(player1, evt1.getAllValues().get(0).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(0).getArena());
        assertEquals(CommonTeams.Aqua, evt1.getAllValues().get(0).getTeam());
        assertSame(player2, evt1.getAllValues().get(1).getArenaPlayer());
        assertSame(arena, evt1.getAllValues().get(1).getArena());
        assertEquals(CommonTeams.Black, evt1.getAllValues().get(1).getTeam());
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
        
        final CheckedCaptor<ArenaPlayerJoinSpectatorsEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        final CheckedCaptor<ArenaPlayerJoinedSpectatorsEvent> evt2 = CheckedCaptor.forClass(ArenaPlayerJoinedSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt2.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        assertTrue(evt1.getValue().isPlayedBefore());
        
        assertSame(player1, evt2.getValue().getArenaPlayer());
        assertSame(arena, evt2.getValue().getArena());
        assertTrue(evt2.getValue().isPlayedBefore());
    }
    
    /**
     * Checks if join spectators event is fired.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test(expected = McException.class)
    public void vetoSpectatorsWithinMatchFired() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        spmatch.join(player1);
        spmatch.start();

        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                invocation.getArgumentAt(0, ArenaPlayerJoinSpectatorsEvent.class).setCancelled(CommonMessages.InternalError);
                return null;
            }}).when(this.pluginManager).callEvent(isA(ArenaPlayerJoinSpectatorsEvent.class));
        
        spmatch.spectate(player1);
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
        
        final CheckedCaptor<ArenaPlayerJoinSpectatorsEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        final CheckedCaptor<ArenaPlayerJoinedSpectatorsEvent> evt2 = CheckedCaptor.forClass(ArenaPlayerJoinedSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt2.capture());
        
        assertSame(player1, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        assertFalse(evt1.getValue().isPlayedBefore());
        
        assertSame(player1, evt2.getValue().getArenaPlayer());
        assertSame(arena, evt2.getValue().getArena());
        assertFalse(evt2.getValue().isPlayedBefore());
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
        
        final CheckedCaptor<ArenaPlayerJoinSpectatorsEvent> evt1 = CheckedCaptor.forClass(ArenaPlayerJoinSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        final CheckedCaptor<ArenaPlayerJoinedSpectatorsEvent> evt2 = CheckedCaptor.forClass(ArenaPlayerJoinedSpectatorsEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt2.capture());
        
        assertSame(player2, evt1.getValue().getArenaPlayer());
        assertSame(arena, evt1.getValue().getArena());
        assertFalse(evt1.getValue().isPlayedBefore());
        
        assertSame(player2, evt2.getValue().getArenaPlayer());
        assertSame(arena, evt2.getValue().getArena());
        assertFalse(evt2.getValue().isPlayedBefore());
    }
    
    /**
     * Tests if winner is fired
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void winnerFired() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.start();
        MglibTestHelper.createLib(player1);
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        
        spmatch.setWinner(player1.getPlayerUUID());
        
        final CheckedCaptor<ArenaWinEvent> evt1 = CheckedCaptor.forClass(ArenaWinEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertEquals(player1.getPlayerUUID(), evt1.getValue().getMatchResult().getPlayers().iterator().next());
        assertSame(arena, evt1.getValue().getArena());
        assertTrue(evt1.getValue().getMatchResult().isWin());
        assertEquals(1, evt1.getValue().getMatchResult().getPlace());
    }
    
    /**
     * Tests if winner is fired
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void winnerGroupFired() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.start();
        MglibTestHelper.createLib(player1);
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        
        spmatch.setWinner(CommonTeams.Aqua);
        
        final CheckedCaptor<ArenaWinEvent> evt1 = CheckedCaptor.forClass(ArenaWinEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertEquals(player1.getPlayerUUID(), evt1.getValue().getMatchResult().getPlayers().iterator().next());
        assertSame(arena, evt1.getValue().getArena());
        assertTrue(evt1.getValue().getMatchResult().isWin());
        assertEquals(1, evt1.getValue().getMatchResult().getPlace());
    }
    
    /**
     * Tests if loser is fired
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void loserFired() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.start();
        MglibTestHelper.createLib(player1);
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        
        spmatch.setLoser(player1.getPlayerUUID());
        
        final CheckedCaptor<ArenaLoseEvent> evt1 = CheckedCaptor.forClass(ArenaLoseEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertEquals(player1.getPlayerUUID(), evt1.getValue().getMatchResult().getPlayers().iterator().next());
        assertSame(arena, evt1.getValue().getArena());
        assertFalse(evt1.getValue().getMatchResult().isWin());
        assertEquals(1, evt1.getValue().getMatchResult().getPlace());
    }
    
    /**
     * Tests if loser is fired
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void loserGroupFired() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, true);
        spmatch.getOrCreate(CommonTeams.Aqua);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        spmatch.start();
        MglibTestHelper.createLib(player1);
        spmatch.join(player1);
        when(player1.inArena()).thenReturn(true);
        when(player1.getArena()).thenReturn(arena);
        
        spmatch.setLoser(CommonTeams.Aqua);
        
        final CheckedCaptor<ArenaLoseEvent> evt1 = CheckedCaptor.forClass(ArenaLoseEvent.class);
        verify(this.pluginManager, times(1)).callEvent(evt1.capture());
        
        assertEquals(player1.getPlayerUUID(), evt1.getValue().getMatchResult().getPlayers().iterator().next());
        assertSame(arena, evt1.getValue().getArena());
        assertFalse(evt1.getValue().getMatchResult().isWin());
        assertEquals(1, evt1.getValue().getMatchResult().getPlace());
    }
    
}
