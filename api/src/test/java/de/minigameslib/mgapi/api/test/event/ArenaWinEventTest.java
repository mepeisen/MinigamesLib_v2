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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.events.ArenaWinEvent;
import de.minigameslib.mgapi.api.match.MatchResult;

/**
 * @author mepeisen
 *
 */
public class ArenaWinEventTest
{
    
    /**
     * simple test.
     * @throws ClassNotFoundException 
     */
    @Test
    public void testMe() throws ClassNotFoundException
    {
        final ObjectServiceInterface osi = mock(ObjectServiceInterface.class);
        final UUID uuid1 = UUID.randomUUID();
        final UUID uuid2 = UUID.randomUUID();
        final McPlayerInterface player1 = mock(McPlayerInterface.class);
        final McPlayerInterface player2 = mock(McPlayerInterface.class);
        when(osi.getPlayer(uuid1)).thenReturn(player1);
        when(osi.getPlayer(uuid2)).thenReturn(player2);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.objects.ObjectServiceCache"), "SERVICES", osi); //$NON-NLS-1$ //$NON-NLS-2$
        
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ObjectInterface obj = mock(ObjectInterface.class);
        when(arena.getObject()).thenReturn(obj);
        final ArenaWinEvent evt = new ArenaWinEvent(arena, new MatchResultImpl(5, true, uuid1, uuid2));
        
        assertSame(evt, evt.getBukkitEvent());
        assertSame(evt.getHandlers(), ArenaWinEvent.getHandlerList());
        assertSame(obj, evt.getObject());
        
        assertSame(player1, evt.getPlayer());
        final Iterator<McPlayerInterface> iter = evt.getPlayers().iterator();
        assertTrue(iter.hasNext());
        assertSame(player1, iter.next());
        assertTrue(iter.hasNext());
        assertSame(player2, iter.next());
        assertFalse(iter.hasNext());
        
        assertSame(arena, evt.getArena());
        assertEquals(5, evt.getMatchResult().getPlace());
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
        final UUID uuid1 = UUID.randomUUID();
        final UUID uuid2 = UUID.randomUUID();
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaWinEvent evt = new ArenaWinEvent(arena, new MatchResultImpl(5, true, uuid1, uuid2));
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
        final UUID uuid1 = UUID.randomUUID();
        final UUID uuid2 = UUID.randomUUID();
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaWinEvent evt = new ArenaWinEvent(arena, new MatchResultImpl(5, true, uuid1, uuid2));
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
        final UUID uuid1 = UUID.randomUUID();
        final UUID uuid2 = UUID.randomUUID();
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaWinEvent evt = new ArenaWinEvent(arena, new MatchResultImpl(5, true, uuid1, uuid2));
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
        final UUID uuid1 = UUID.randomUUID();
        final UUID uuid2 = UUID.randomUUID();
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaWinEvent evt = new ArenaWinEvent(arena, new MatchResultImpl(5, true, uuid1, uuid2));
        evt.when(p -> false).thenThrow(CommonMessages.InvokeIngame);
    }
    
    /**
     * Implementation of match results.
     */
    private final static class MatchResultImpl implements MatchResult
    {
        
        /** the place this result represents. */
        private final int        place;
        
        /** winning flag. */
        private final boolean    isWin;
        
        /** players on this place. */
        private final List<UUID> players = new ArrayList<>();
        
        /**
         * @param place
         * @param isWin
         * @param uuids
         */
        public MatchResultImpl(int place, boolean isWin, UUID... uuids)
        {
            this.place = place;
            this.isWin = isWin;
            for (final UUID player : uuids)
            {
                this.players.add(player);
            }
        }
        
        @Override
        public int getPlace()
        {
            return this.place;
        }
        
        @Override
        public Collection<UUID> getPlayers()
        {
            return this.players;
        }
        
        @Override
        public boolean isWin()
        {
            return this.isWin;
        }
        
    }
    
}
