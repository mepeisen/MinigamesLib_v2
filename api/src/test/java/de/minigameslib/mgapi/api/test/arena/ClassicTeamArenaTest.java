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

package de.minigameslib.mgapi.api.test.arena;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.arena.ClassicTeamArena;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetInterface;
import de.minigameslib.mgapi.api.rules.SignRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetInterface;

/**
 * Tests for {@link ClassicTeamArena}.
 * 
 * @author mepeisen
 *
 */
public class ClassicTeamArenaTest
{
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testMe() throws McException
    {
        final Arena arena = new Arena();
        final Set<ArenaRuleSetType> fixed = arena.getFixedArenaRules();
        final Set<ArenaRuleSetType> optional = arena.getOptionalArenaRules();
        final Collection<CheckFailure> failures = arena.check(mock(ArenaInterface.class));
        
        assertNotNull(fixed);
        assertNotNull(optional);
        assertEquals(0, failures.size());
        
        // only for code coverage
        arena.configure(mock(ArenaComponentHandler.class));
        arena.configure(mock(ArenaRuleSetInterface.class));
        arena.configure(mock(ArenaSignHandler.class));
        arena.configure(mock(ArenaZoneHandler.class));
        arena.configure(mock(ComponentRuleSetInterface.class));
        arena.configure(mock(SignRuleSetInterface.class));
        arena.configure(mock(ZoneRuleSetInterface.class));
    }
    
    /**
     * Some arena.
     */
    private static final class Arena extends ClassicTeamArena
    {

        /**
         * Constructor.
         */
        public Arena()
        {
            // empty
        }

        @Override
        public LocalizedMessageInterface getDisplayName()
        {
            return null;
        }

        @Override
        public LocalizedMessageInterface getShortDescription()
        {
            return null;
        }

        @Override
        public LocalizedMessageInterface getDescription()
        {
            return null;
        }
        
    }
    
}
