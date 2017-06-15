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

package de.minigameslib.mgapi.api.test.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Iterator;

import org.junit.Test;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.events.ArenaPlayerStatisticEvent;
import de.minigameslib.mgapi.api.match.CommonMatchStatistics;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;

/**
 * @author mepeisen
 *
 */
public class ArenaPlayerStatisticEventTest
{
    
    /**
     * simple test.
     * @throws ClassNotFoundException 
     */
    @Test
    public void testMe() throws ClassNotFoundException
    {
        final McPlayerInterface player1 = mock(McPlayerInterface.class);
        final ArenaPlayerInterface arenaplayer1 = mock(ArenaPlayerInterface.class);
        when(arenaplayer1.getMcPlayer()).thenReturn(player1);
        
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ObjectInterface obj = mock(ObjectInterface.class);
        when(arena.getObject()).thenReturn(obj);
        final ArenaPlayerStatisticEvent evt = new ArenaPlayerStatisticEvent(arena, arenaplayer1, CommonMatchStatistics.Deaths, 1, 2);
        
        assertSame(evt, evt.getBukkitEvent());
        assertSame(evt.getHandlers(), ArenaPlayerStatisticEvent.getHandlerList());
        assertSame(obj, evt.getObject());
        
        assertSame(player1, evt.getPlayer());
        final Iterator<McPlayerInterface> iter = evt.getPlayers().iterator();
        assertTrue(iter.hasNext());
        assertSame(player1, iter.next());
        assertFalse(iter.hasNext());
        assertSame(arenaplayer1, evt.getArenaPlayer());
        
        assertSame(arena, evt.getArena());
        assertEquals(CommonMatchStatistics.Deaths, evt.getId());
        assertEquals(1, evt.getOldValue());
        assertEquals(2, evt.getNewValue());
    }
    
    /**
     * simple true stub test.
     * 
     * @throws McException
     *             thrown on error
     */
    @Test(expected = McException.class)
    public void testTrueStub() throws McException
    {
        final McPlayerInterface player1 = mock(McPlayerInterface.class);
        final ArenaPlayerInterface arenaplayer1 = mock(ArenaPlayerInterface.class);
        when(arenaplayer1.getMcPlayer()).thenReturn(player1);
        
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ObjectInterface obj = mock(ObjectInterface.class);
        when(arena.getObject()).thenReturn(obj);
        final ArenaPlayerStatisticEvent evt = new ArenaPlayerStatisticEvent(arena, arenaplayer1, CommonMatchStatistics.Deaths, 1, 2);
        evt.when(p -> true).thenThrow(CommonMessages.InvokeIngame);
    }
    
    /**
     * simple true stub test.
     * 
     * @throws McException
     *             thrown on error
     */
    @Test
    public void testTrueStub2() throws McException
    {
        final McPlayerInterface player1 = mock(McPlayerInterface.class);
        final ArenaPlayerInterface arenaplayer1 = mock(ArenaPlayerInterface.class);
        when(arenaplayer1.getMcPlayer()).thenReturn(player1);
        
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ObjectInterface obj = mock(ObjectInterface.class);
        when(arena.getObject()).thenReturn(obj);
        final ArenaPlayerStatisticEvent evt = new ArenaPlayerStatisticEvent(arena, arenaplayer1, CommonMatchStatistics.Deaths, 1, 2);
        evt.when(p -> true)._elseThrow(CommonMessages.InvokeIngame);
    }
    
    /**
     * simple true stub test.
     * 
     * @throws McException
     *             thrown on error
     */
    @Test(expected = McException.class)
    public void testFalseStub() throws McException
    {
        final McPlayerInterface player1 = mock(McPlayerInterface.class);
        final ArenaPlayerInterface arenaplayer1 = mock(ArenaPlayerInterface.class);
        when(arenaplayer1.getMcPlayer()).thenReturn(player1);
        
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ObjectInterface obj = mock(ObjectInterface.class);
        when(arena.getObject()).thenReturn(obj);
        final ArenaPlayerStatisticEvent evt = new ArenaPlayerStatisticEvent(arena, arenaplayer1, CommonMatchStatistics.Deaths, 1, 2);
        evt.when(p -> false)._elseThrow(CommonMessages.InvokeIngame);
    }
    
    /**
     * simple true stub test.
     * 
     * @throws McException
     *             thrown on error
     */
    @Test
    public void testFalseStub2() throws McException
    {
        final McPlayerInterface player1 = mock(McPlayerInterface.class);
        final ArenaPlayerInterface arenaplayer1 = mock(ArenaPlayerInterface.class);
        when(arenaplayer1.getMcPlayer()).thenReturn(player1);
        
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ObjectInterface obj = mock(ObjectInterface.class);
        when(arena.getObject()).thenReturn(obj);
        final ArenaPlayerStatisticEvent evt = new ArenaPlayerStatisticEvent(arena, arenaplayer1, CommonMatchStatistics.Deaths, 1, 2);
        evt.when(p -> false).thenThrow(CommonMessages.InvokeIngame);
    }
    
}
