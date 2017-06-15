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

package de.minigameslib.mgapi.api.test.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.util.function.McFunction;
import de.minigameslib.mgapi.api.MinigameMessages;
import de.minigameslib.mgapi.api.rules.AbstractRuleSetContainer;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.BasicArenaRuleSets;

/**
 * Tests for {@link AbstractRuleSetContainer}.
 * 
 * @author mepeisen
 *
 */
public class AbstractRuleSetContainerTest
{
    
    /**
     * test me.
     */
    @Test
    public void testConstructionEmpty()
    {
        final Container container = new Container(true, null);
        assertEquals(0, container.getAppliedRuleSetTypes().size());
        assertEquals(0, container.getAppliedRuleSetTypes().size());
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testNewFixedRule() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        
        assertFalse(container.isFixed(BasicArenaRuleSets.BasicMatch));
        
        container.applyFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        
        assertTrue(container.isFixed(BasicArenaRuleSets.BasicMatch));
        assertTrue(container.getAppliedRuleSetTypes().contains(BasicArenaRuleSets.BasicMatch));
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testGetRuleSet1() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        final ArenaRuleSetInterface rule1 = container.applyFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule2 = container.getRuleSet(BasicArenaRuleSets.BasicMatch);
        assertSame(rule1, rule2);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testGetRuleSet2() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        final ArenaRuleSetInterface rule1 = container.applyOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule2 = container.getRuleSet(BasicArenaRuleSets.BasicMatch);
        assertSame(rule1, rule2);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testGetRuleSet3() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        assertNull(container.getRuleSet(BasicArenaRuleSets.BasicMatch));
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testGetRuleSet4() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        final ArenaRuleSetInterface rule1 = container.applyFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule2 = container.removeFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule3 = container.getRuleSet(BasicArenaRuleSets.BasicMatch);
        assertSame(rule1, rule2);
        assertNull(rule3);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testGetRuleSet5() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        final ArenaRuleSetInterface rule1 = container.applyOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule2 = container.removeOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule3 = container.getRuleSet(BasicArenaRuleSets.BasicMatch);
        assertSame(rule1, rule2);
        assertNull(rule3);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testGetRuleSet6() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        final ArenaRuleSetInterface rule1 = container.applyFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule2 = container.removeOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule3 = container.getRuleSet(BasicArenaRuleSets.BasicMatch);
        assertSame(rule1, rule3);
        assertNull(rule2);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testGetRuleSet7() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        final ArenaRuleSetInterface rule1 = container.applyOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule2 = container.removeFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule3 = container.getRuleSet(BasicArenaRuleSets.BasicMatch);
        assertSame(rule1, rule3);
        assertNull(rule2);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testNewFixedRuleAddsListener() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        final ArenaRuleSetInterface rule = container.applyFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        assertEquals(1, container.getListenerSet().size());
        assertTrue(container.getListenerSet().contains(rule));
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testNewOptionalRule() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        
        assertFalse(container.isOptional(BasicArenaRuleSets.BasicMatch));
        
        container.applyOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        
        assertTrue(container.isOptional(BasicArenaRuleSets.BasicMatch));
        assertTrue(container.getAppliedRuleSetTypes().contains(BasicArenaRuleSets.BasicMatch));
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testNewOptionalRuleAddsListener() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        final ArenaRuleSetInterface rule = container.applyOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        assertEquals(1, container.getListenerSet().size());
        assertTrue(container.getListenerSet().contains(rule));
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testNewFixedRuleDuplicate1() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));

        final ArenaRuleSetInterface rule1 = container.applyFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        // will not recreate it but instead return rule1
        final ArenaRuleSetInterface rule2 = container.applyFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        assertSame(rule1, rule2);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testNewFixedRuleDuplicate2() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));

        final ArenaRuleSetInterface rule1 = container.applyOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        // will not recreate it but instead return rule1
        final ArenaRuleSetInterface rule2 = container.applyFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        assertSame(rule1, rule2);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testNewOptionalRuleDuplicate1() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));

        final ArenaRuleSetInterface rule1 = container.applyOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        // will not recreate it but instead return rule1
        final ArenaRuleSetInterface rule2 = container.applyOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        assertSame(rule1, rule2);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testNewOptionalRuleDuplicate2() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));

        final ArenaRuleSetInterface rule1 = container.applyFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        // will not recreate it but instead return rule1
        final ArenaRuleSetInterface rule2 = container.applyOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        assertSame(rule1, rule2);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testReapply1() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        final ArenaRuleSetInterface rule1 = container.applyFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule2 = container.reapplyRuleSet(BasicArenaRuleSets.BasicMatch);
        assertNotSame(rule1, rule2);
        assertTrue(container.isFixed(BasicArenaRuleSets.BasicMatch));
        final ArenaRuleSetInterface rule3 = container.getRuleSet(BasicArenaRuleSets.BasicMatch);
        assertSame(rule2, rule3);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testReapply2() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        final ArenaRuleSetInterface rule1 = container.applyOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule2 = container.reapplyRuleSet(BasicArenaRuleSets.BasicMatch);
        assertNotSame(rule1, rule2);
        assertTrue(container.isOptional(BasicArenaRuleSets.BasicMatch));
        final ArenaRuleSetInterface rule3 = container.getRuleSet(BasicArenaRuleSets.BasicMatch);
        assertSame(rule2, rule3);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test(expected = McException.class)
    public void testReapply3() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        container.reapplyRuleSet(BasicArenaRuleSets.BasicMatch);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testReapplyManagesListener1() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        final ArenaRuleSetInterface rule1 = container.applyFixedRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule2 = container.reapplyRuleSet(BasicArenaRuleSets.BasicMatch);
        assertEquals(1, container.getListenerSet().size());
        assertFalse(container.getListenerSet().contains(rule1));
        assertTrue(container.getListenerSet().contains(rule2));
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testReapplyManagesListener2() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        final ArenaRuleSetInterface rule1 = container.applyOptionalRuleSet(BasicArenaRuleSets.BasicMatch);
        final ArenaRuleSetInterface rule2 = container.reapplyRuleSet(BasicArenaRuleSets.BasicMatch);
        assertEquals(1, container.getListenerSet().size());
        assertFalse(container.getListenerSet().contains(rule1));
        assertTrue(container.getListenerSet().contains(rule2));
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testNewFixedRuleSets() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        container.applyFixedRuleSets(BasicArenaRuleSets.BasicMatch, BasicArenaRuleSets.BasicMatchTimer, BasicArenaRuleSets.BasicSpawns);
        assertTrue(container.isFixed(BasicArenaRuleSets.BasicMatch));
        assertTrue(container.isFixed(BasicArenaRuleSets.BasicMatchTimer));
        assertTrue(container.isFixed(BasicArenaRuleSets.BasicSpawns));
        assertTrue(container.getAppliedRuleSetTypes().contains(BasicArenaRuleSets.BasicMatch));
        assertTrue(container.getAppliedRuleSetTypes().contains(BasicArenaRuleSets.BasicMatchTimer));
        assertTrue(container.getAppliedRuleSetTypes().contains(BasicArenaRuleSets.BasicSpawns));
        assertEquals(3, container.getListenerSet().size());
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testNewOptionalRuleSets() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        container.applyOptionalRuleSets(BasicArenaRuleSets.BasicMatch, BasicArenaRuleSets.BasicMatchTimer, BasicArenaRuleSets.BasicSpawns);
        assertTrue(container.isOptional(BasicArenaRuleSets.BasicMatch));
        assertTrue(container.isOptional(BasicArenaRuleSets.BasicMatchTimer));
        assertTrue(container.isOptional(BasicArenaRuleSets.BasicSpawns));
        assertTrue(container.getAppliedRuleSetTypes().contains(BasicArenaRuleSets.BasicMatch));
        assertTrue(container.getAppliedRuleSetTypes().contains(BasicArenaRuleSets.BasicMatchTimer));
        assertTrue(container.getAppliedRuleSetTypes().contains(BasicArenaRuleSets.BasicSpawns));
        assertEquals(3, container.getListenerSet().size());
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testClearRuleSets() throws McException
    {
        final Container container = new Container(true, rs -> mock(ArenaRuleSetInterface.class));
        container.applyFixedRuleSets(BasicArenaRuleSets.BasicMatch, BasicArenaRuleSets.BasicMatchTimer);
        container.applyOptionalRuleSets(BasicArenaRuleSets.BasicSpectator, BasicArenaRuleSets.BasicSpawns);
        assertTrue(container.isFixed(BasicArenaRuleSets.BasicMatch));
        assertTrue(container.isFixed(BasicArenaRuleSets.BasicMatchTimer));
        assertTrue(container.isOptional(BasicArenaRuleSets.BasicSpawns));
        assertTrue(container.isOptional(BasicArenaRuleSets.BasicSpectator));
        assertEquals(4, container.getListenerSet().size());
        container.clearRuleSets();
        assertFalse(container.isFixed(BasicArenaRuleSets.BasicMatch));
        assertFalse(container.isFixed(BasicArenaRuleSets.BasicMatchTimer));
        assertFalse(container.isOptional(BasicArenaRuleSets.BasicSpawns));
        assertFalse(container.isOptional(BasicArenaRuleSets.BasicSpectator));
        assertEquals(0, container.getListenerSet().size());
    }
    
    /**
     * test container.
     */
    private static class Container extends AbstractRuleSetContainer<ArenaRuleSetType, ArenaRuleSetInterface>
    {
        
        /**
         * allow modifications.
         */
        private final boolean allowModifications;
        
        /**
         * listener set.
         */
        private final Set<ArenaRuleSetInterface> listenerSet = new HashSet<>();
        
        /**
         * the creator func.
         */
        private final McFunction<ArenaRuleSetType, ArenaRuleSetInterface> creator;

        /**
         * Constructor.
         * @param allowModifications
         * @param creator
         */
        public Container(boolean allowModifications, McFunction<ArenaRuleSetType, ArenaRuleSetInterface> creator)
        {
            this.allowModifications = allowModifications;
            this.creator = creator;
        }

        @Override
        protected void checkModifications() throws McException
        {
            if (!this.allowModifications)
            {
                throw new McException(MinigameMessages.ModificationWrongState);
            }
        }

        /**
         * Returns the set of listeners.
         * @return the listenerSet
         */
        public Set<ArenaRuleSetInterface> getListenerSet()
        {
            return this.listenerSet;
        }

        @Override
        protected void applyListeners(ArenaRuleSetInterface listeners)
        {
            this.listenerSet.add(listeners);
        }

        @Override
        protected void removeListeners(ArenaRuleSetInterface listeners)
        {
            this.listenerSet.remove(listeners);
        }

        @Override
        protected ArenaRuleSetInterface create(ArenaRuleSetType ruleset) throws McException
        {
            return this.creator.apply(ruleset);
        }
        
    }
    
}
