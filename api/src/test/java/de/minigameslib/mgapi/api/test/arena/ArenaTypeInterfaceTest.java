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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.plugin.Plugin;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mgapi.api.MinigameInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaType;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeProvider;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetInterface;
import de.minigameslib.mgapi.api.rules.SignRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetInterface;

/**
 * Tests for {@link ArenaTypeInterface}.
 * 
 * @author mepeisen
 *
 */
public class ArenaTypeInterfaceTest
{
    
    /**
     * test me.
     * @throws ClassNotFoundException 
     */
    @Test
    public void testGetMinigame() throws ClassNotFoundException
    {
        final MinigamesLibInterface mglib = mock(MinigamesLibInterface.class);
        final MinigameInterface mg = mock(MinigameInterface.class);
        when(mglib.getMinigame(any(Plugin.class))).thenReturn(mg);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mgapi.api.MglibCache"), "SERVICES", mglib); //$NON-NLS-1$ //$NON-NLS-2$
        
        final Plugin plugin = mock(Plugin.class);
        final EnumServiceInterface esi = mock(EnumServiceInterface.class);
        when(esi.getPlugin(MyTypes.MyType)).thenReturn(plugin);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.enums.EnumServiceCache"), "SERVICES", esi); //$NON-NLS-1$ //$NON-NLS-2$
        
        assertSame(mg, MyTypes.MyType.getMinigame());
    }
    
    /**
     * test me.
     * @throws ClassNotFoundException 
     */
    @Test
    public void testGetOptionalRules() throws ClassNotFoundException
    {
        final MinigamesLibInterface mglib = mock(MinigamesLibInterface.class);
        final ArenaRuleSetType rule1 = mock(ArenaRuleSetType.class);
        final ArenaRuleSetType rule2 = mock(ArenaRuleSetType.class);
        when(mglib.getOptionalRuleSets(MyTypes.MyType)).thenReturn(new HashSet<>(Arrays.asList(rule1, rule2)));
        Whitebox.setInternalState(Class.forName("de.minigameslib.mgapi.api.MglibCache"), "SERVICES", mglib); //$NON-NLS-1$ //$NON-NLS-2$
        
        final Set<ArenaRuleSetType> optionalRuleSets = MyTypes.MyType.getOptionalRuleSets();
        assertEquals(2, optionalRuleSets.size());
        assertTrue(optionalRuleSets.contains(rule1));
        assertTrue(optionalRuleSets.contains(rule2));
    }
    
    /**
     * test me.
     */
    @Test
    public void testGetProvider()
    {
        assertTrue(MyTypes.MyType.getProvider() == MyProvider.class);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testCreateProvider() throws McException
    {
        assertTrue(MyTypes.MyType.safeCreateProvider() instanceof MyProvider);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void testCreateProvider2() throws McException
    {
        assertTrue(MyTypes.MyTypeInvalid.safeCreateProvider() instanceof MyProvider);
    }
    
    /** my types. */
    private enum MyTypes implements ArenaTypeInterface
    {
        /** my types. */
        @ArenaType(MyProvider.class)
        MyType,
        /** my types. */
        @ArenaType(ArenaTypeProvider.class)
        MyTypeInvalid
    }
    
    /**
     * my provider impl.
     */
    public static final class MyProvider implements ArenaTypeProvider
    {

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
        public Set<ArenaRuleSetType> getFixedArenaRules()
        {
            return null;
        }

        @Override
        public Set<ArenaRuleSetType> getOptionalArenaRules()
        {
            return null;
        }

        @Override
        public void configure(ArenaRuleSetInterface ruleSet) throws McException
        {
            // empty
        }

        @Override
        public void configure(ComponentRuleSetInterface ruleSet) throws McException
        {
            // empty
        }

        @Override
        public void configure(ZoneRuleSetInterface ruleSet) throws McException
        {
            // empty
        }

        @Override
        public void configure(SignRuleSetInterface ruleSet) throws McException
        {
            // empty
        }

        @Override
        public void configure(ArenaComponentHandler handler) throws McException
        {
            // empty
        }

        @Override
        public void configure(ArenaZoneHandler handler) throws McException
        {
            // empty
        }

        @Override
        public void configure(ArenaSignHandler handler) throws McException
        {
            // empty
        }

        @Override
        public Collection<CheckFailure> check(ArenaInterface arena)
        {
            return null;
        }
        
    }
    
}
