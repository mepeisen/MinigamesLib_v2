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

package de.minigameslib.mgapi.api.test;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;

import org.junit.Test;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mgapi.api.ExtensionProvider;
import de.minigameslib.mgapi.api.arena.ArenaClassInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.stat.MutableMatchStatisticInterface;

/**
 * Tests for {@link ExtensionProvider}.
 * 
 * @author mepeisen
 *
 */
public class ExtensionProviderTest
{
    
    /**
     * test me.
     */
    @Test
    public void testMe()
    {
        @SuppressWarnings("synthetic-access")
        final Provider provider = new Provider();

        assertEquals(0, provider.getOptionalArenaRules(mock(ArenaTypeInterface.class)).size());
        assertEquals(0, provider.getOptionalRuleSets(mock(ArenaInterface.class), mock(ComponentInterface.class)).size());
        assertEquals(0, provider.getOptionalRuleSets(mock(ArenaInterface.class), mock(SignInterface.class)).size());
        assertEquals(0, provider.getOptionalRuleSets(mock(ArenaInterface.class), mock(ZoneInterface.class)).size());
        assertEquals(0, provider.getOptionalRuleSets(mock(ArenaInterface.class), mock(ArenaClassInterface.class)).size());
        
        // only for code coverage. Method does nothing
        provider.processMatchStatistics(mock(ArenaMatchInterface.class), mock(MutableMatchStatisticInterface.class));
    }
    
    /**
     * extension provider for testing.
     */
    private static final class Provider implements ExtensionProvider
    {

        @Override
        public String getName()
        {
            return null;
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

        @Override
        public LocalizedMessageInterface getManual()
        {
            return null;
        }
        
    }

}
