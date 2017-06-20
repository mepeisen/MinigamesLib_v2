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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.arena.ArenaClassInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.rules.ClassRuleSetInterface;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.impl.arena.ArenaMatchImpl;
import de.minigameslib.mgapi.impl.test.MglibTestHelper;

/**
 * Tests the ArenaImpl with classes features.
 * 
 * @author mepeisen
 *
 */
public class ArenaWithClassesTest
{
    
    /**
     * init surroundings.
     * @throws ClassNotFoundException 
     */
    @Before
    public void init() throws ClassNotFoundException
    {
        MglibTestHelper.initPlaceholdersDummy();
        MglibTestHelper.initEnumServicesDummy();
    }

    /**
     * get the classes.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void selectClassForPlayerDuringStart() throws McException, ClassNotFoundException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(mock(ArenaInterface.class), false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        final ClassRuleSetInterface rule = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass(rule);
        
        spmatch.selectClass(player1.getPlayerUUID(), clazz1);
        spmatch.join(player1);
        
        spmatch.start();
        
        verify(rule, times(1)).onChoose(player1);
    }

    /**
     * get the classes.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void changeClassForPlayerDuringStart() throws McException, ClassNotFoundException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(mock(ArenaInterface.class), false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        final ClassRuleSetInterface rule1 = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass(rule1);
        final ClassRuleSetInterface rule2 = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz2 = MglibTestHelper.createClass(rule2);
        
        spmatch.selectClass(player1.getPlayerUUID(), clazz1);
        spmatch.join(player1);
        spmatch.selectClass(player1.getPlayerUUID(), clazz2);
        
        spmatch.start();
        
        verify(rule1, times(0)).onChoose(player1);
        verify(rule1, times(0)).onRemove(player1);
        verify(rule2, times(1)).onChoose(player1);
    }

    /**
     * get the classes.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void selectClassForPlayerDuringMatch() throws McException, ClassNotFoundException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(mock(ArenaInterface.class), false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        final ClassRuleSetInterface rule = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass(rule);
        
        spmatch.join(player1);
        spmatch.start();
        spmatch.selectClass(player1.getPlayerUUID(), clazz1);
        
        verify(rule, times(1)).onChoose(player1);
    }

    /**
     * get the classes.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void changeClassForPlayerDuringMatch() throws McException, ClassNotFoundException
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(mock(ArenaInterface.class), false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        final ClassRuleSetInterface rule1 = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass(rule1);
        final ClassRuleSetInterface rule2 = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz2 = MglibTestHelper.createClass(rule2);
        
        spmatch.selectClass(player1.getPlayerUUID(), clazz1);
        spmatch.join(player1);
        
        spmatch.start();
        
        verify(rule1, times(1)).onChoose(player1);
        spmatch.selectClass(player1.getPlayerUUID(), clazz2);
        
        verify(rule1, times(1)).onRemove(player1);
        verify(rule2, times(1)).onChoose(player1);
    }

    /**
     * get the classes.
     * @throws McException 
     */
    @Test
    public void selectDefaultClassForPlayer() throws McException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass();
        when(arena.getDefaultArenaClass()).thenReturn(clazz1);
        
        spmatch.join(player1);
        
        assertEquals(clazz1, spmatch.getClass(player1.getPlayerUUID()));
    }

    /**
     * get the classes.
     */
    @Test
    public void getClassForPlayer()
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(mock(ArenaInterface.class), false);
        
        final UUID player1 = UUID.randomUUID();
        final UUID player2 = UUID.randomUUID();
        final UUID player3 = UUID.randomUUID();
        final UUID player4 = UUID.randomUUID();
        
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass();
        final ArenaClassInterface clazz2 = MglibTestHelper.createClass();
        
        spmatch.selectClass(player1, clazz1);
        spmatch.selectClass(player2, clazz1);
        spmatch.selectClass(player3, clazz2);
        
        assertEquals(clazz1, spmatch.getClass(player1));
        assertEquals(clazz1, spmatch.getClass(player2));
        assertEquals(clazz2, spmatch.getClass(player3));
        assertNull(spmatch.getClass(player4));
        
        spmatch.selectClass(player1, clazz2);
        
        assertEquals(clazz2, spmatch.getClass(player1));
        assertEquals(clazz1, spmatch.getClass(player2));
        assertEquals(clazz2, spmatch.getClass(player3));
        assertNull(spmatch.getClass(player4));
    }

    /**
     * get the classes.
     */
    @Test
    public void getPlayersForClass()
    {
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(mock(ArenaInterface.class), false);
        
        final UUID player1 = UUID.randomUUID();
        final UUID player2 = UUID.randomUUID();
        final UUID player3 = UUID.randomUUID();
        
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass();
        final ArenaClassInterface clazz2 = MglibTestHelper.createClass();
        final ArenaClassInterface clazz3 = MglibTestHelper.createClass();
        
        spmatch.selectClass(player1, clazz1);
        spmatch.selectClass(player2, clazz1);
        spmatch.selectClass(player3, clazz2);
        
        List<UUID> list = spmatch.getPlayers(clazz1);
        assertEquals(2, list.size());
        assertTrue(list.contains(player1));
        assertTrue(list.contains(player2));
        list = spmatch.getPlayers(clazz2);
        assertEquals(1, list.size());
        assertTrue(list.contains(player3));
        list = spmatch.getPlayers(clazz3);
        assertEquals(0, list.size());
        
        spmatch.selectClass(player1, clazz2);
        
        list = spmatch.getPlayers(clazz1);
        assertEquals(1, list.size());
        assertTrue(list.contains(player2));
        list = spmatch.getPlayers(clazz2);
        assertEquals(2, list.size());
        assertTrue(list.contains(player1));
        assertTrue(list.contains(player3));
        list = spmatch.getPlayers(clazz3);
        assertEquals(0, list.size());
    }

    /**
     * remove the classes on leave.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void removeClassForLeavingPreMatch() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        final ClassRuleSetInterface rule = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass(rule);
        
        spmatch.selectClass(player1.getPlayerUUID(), clazz1);
        spmatch.join(player1);
        when(player1.getArena()).thenReturn(arena);
        spmatch.leave(player1);
        
        spmatch.start();
        
        verify(rule, times(0)).onChoose(player1);
        verify(rule, times(0)).onRemove(player1);
    }

    /**
     * remove the classes on leave.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void removeClassForWinners() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        final ClassRuleSetInterface rule = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass(rule);
        
        spmatch.selectClass(player1.getPlayerUUID(), clazz1);
        spmatch.join(player1);
        when(player1.getArena()).thenReturn(arena);
        spmatch.start();
        spmatch.setWinner(player1.getPlayerUUID());
        
        verify(rule, times(1)).onChoose(player1);
        verify(rule, times(1)).onRemove(player1);
    }

    /**
     * remove the classes on leave.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void removeClassForLosers() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        final ClassRuleSetInterface rule = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass(rule);
        
        spmatch.selectClass(player1.getPlayerUUID(), clazz1);
        spmatch.join(player1);
        when(player1.getArena()).thenReturn(arena);
        spmatch.start();
        spmatch.setLoser(player1.getPlayerUUID());
        
        verify(rule, times(1)).onChoose(player1);
        verify(rule, times(1)).onRemove(player1);
    }

    /**
     * remove the classes on leave.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void removeClassForWinnerTeams() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl teammatch = new ArenaMatchImpl(arena, true);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        final ClassRuleSetInterface rule = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass(rule);
        
        teammatch.getOrCreate(CommonTeams.Aqua);
        teammatch.selectClass(player1.getPlayerUUID(), clazz1);
        teammatch.join(player1, CommonTeams.Aqua);
        when(player1.getArena()).thenReturn(arena);
        teammatch.start();
        teammatch.setWinner(CommonTeams.Aqua);
        
        verify(rule, times(1)).onChoose(player1);
        verify(rule, times(1)).onRemove(player1);
    }

    /**
     * remove the classes on leave.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void removeClassForLoserTeams() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl teammatch = new ArenaMatchImpl(arena, true);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        final ClassRuleSetInterface rule = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass(rule);
        
        teammatch.getOrCreate(CommonTeams.Aqua);
        teammatch.selectClass(player1.getPlayerUUID(), clazz1);
        teammatch.join(player1, CommonTeams.Aqua);
        when(player1.getArena()).thenReturn(arena);
        teammatch.start();
        teammatch.setLoser(CommonTeams.Aqua);
        
        verify(rule, times(1)).onChoose(player1);
        verify(rule, times(1)).onRemove(player1);
    }

    /**
     * remove the classes on leave.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void removeClassForLeavingInMatch() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        final ClassRuleSetInterface rule = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass(rule);
        
        spmatch.selectClass(player1.getPlayerUUID(), clazz1);
        spmatch.join(player1);
        when(player1.getArena()).thenReturn(arena);
        spmatch.start();
        spmatch.leave(player1);
        
        verify(rule, times(1)).onChoose(player1);
        verify(rule, times(1)).onRemove(player1);
    }

    /**
     * remove the classes on leave.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void removeClassForSpectatingPreMatch() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        final ClassRuleSetInterface rule = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass(rule);
        
        spmatch.selectClass(player1.getPlayerUUID(), clazz1);
        spmatch.join(player1);
        when(player1.getArena()).thenReturn(arena);
        spmatch.spectate(player1);
        
        spmatch.start();
        
        verify(rule, times(0)).onChoose(player1);
        verify(rule, times(0)).onRemove(player1);
    }

    /**
     * remove the classes on leave.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void removeClassForSpectatingInMatch() throws McException, ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaMatchImpl spmatch = new ArenaMatchImpl(arena, false);
        final ArenaPlayerInterface player1 = MglibTestHelper.createPlayer();
        MglibTestHelper.createLib(player1);
        final ClassRuleSetInterface rule = MglibTestHelper.createClassRule();
        final ArenaClassInterface clazz1 = MglibTestHelper.createClass(rule);
        
        spmatch.selectClass(player1.getPlayerUUID(), clazz1);
        spmatch.join(player1);
        when(player1.getArena()).thenReturn(arena);
        spmatch.start();
        spmatch.spectate(player1);
        
        verify(rule, times(1)).onChoose(player1);
        verify(rule, times(1)).onRemove(player1);
    }
    
}
