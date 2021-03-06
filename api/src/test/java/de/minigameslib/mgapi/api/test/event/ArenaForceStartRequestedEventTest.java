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

import static org.junit.Assert.assertSame;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.events.ArenaForceStartRequestedEvent;

/**
 * @author mepeisen
 *
 */
public class ArenaForceStartRequestedEventTest
{
    
    /**
     * simple test.
     * @throws ClassNotFoundException 
     */
    @Test
    public void testMe() throws ClassNotFoundException
    {
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ObjectInterface obj = mock(ObjectInterface.class);
        when(arena.getObject()).thenReturn(obj);
        final ArenaForceStartRequestedEvent evt = new ArenaForceStartRequestedEvent(arena);
        
        assertSame(evt, evt.getBukkitEvent());
        assertSame(evt.getHandlers(), ArenaForceStartRequestedEvent.getHandlerList());
        assertSame(obj, evt.getObject());
        
        assertSame(arena, evt.getArena());
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
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ObjectInterface obj = mock(ObjectInterface.class);
        when(arena.getObject()).thenReturn(obj);
        final ArenaForceStartRequestedEvent evt = new ArenaForceStartRequestedEvent(arena);
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
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ObjectInterface obj = mock(ObjectInterface.class);
        when(arena.getObject()).thenReturn(obj);
        final ArenaForceStartRequestedEvent evt = new ArenaForceStartRequestedEvent(arena);
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
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ObjectInterface obj = mock(ObjectInterface.class);
        when(arena.getObject()).thenReturn(obj);
        final ArenaForceStartRequestedEvent evt = new ArenaForceStartRequestedEvent(arena);
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
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ObjectInterface obj = mock(ObjectInterface.class);
        when(arena.getObject()).thenReturn(obj);
        final ArenaForceStartRequestedEvent evt = new ArenaForceStartRequestedEvent(arena);
        evt.when(p -> false).thenThrow(CommonMessages.InvokeIngame);
    }
    
}
