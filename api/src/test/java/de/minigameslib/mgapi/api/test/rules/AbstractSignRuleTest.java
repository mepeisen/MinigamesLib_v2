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
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.rules.AbstractSignRule;
import de.minigameslib.mgapi.api.rules.SignRuleSetInterface;
import de.minigameslib.mgapi.api.rules.SignRuleSetType;

/**
 * Tests for {@link AbstractSignRule}.
 * 
 * @author mepeisen
 *
 */
public class AbstractSignRuleTest
{
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testCheck() throws McException
    {
        final SignRuleSetType type = mock(SignRuleSetType.class);
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaSignHandler sign = mock(ArenaSignHandler.class);
        when(sign.getArena()).thenReturn(arena);
        final Rule rule = new Rule(type, sign);
        
        assertEquals(0, rule.check().size());
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testConstructor() throws McException
    {
        final SignRuleSetType type = mock(SignRuleSetType.class);
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaSignHandler sign = mock(ArenaSignHandler.class);
        final SignInterface signobj = mock(SignInterface.class);
        when(sign.getArena()).thenReturn(arena);
        when(sign.getSign()).thenReturn(signobj);
        final Rule rule = new Rule(type, sign);
        
        assertSame(type, rule.getType());
        assertSame(signobj, rule.getSign());
    }
    
    /**
     * test me.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void testRunInCopiedContext() throws McException, ClassNotFoundException
    {
        final SignRuleSetType type = mock(SignRuleSetType.class);
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaSignHandler sign = mock(ArenaSignHandler.class);
        when(sign.getArena()).thenReturn(arena);
        final Rule rule = new Rule(type, sign);
        
        final McLibInterface mclib = mock(McLibInterface.class);
        final Map<Object, Object> context = prepareMclib(mclib);
        
        doAnswer(new Answer<Void>(){

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                invocation.getArgumentAt(0, McRunnable.class).run();
                return null;
            }
            
        }).when(mclib).runInCopiedContext(any(McRunnable.class));
        
        // test
        rule.runInCopiedContext(() -> {
            // empty
        });
        
        assertEquals(3, context.size());
        assertSame(arena, context.get(ArenaInterface.class));
        assertSame(sign, context.get(ArenaSignHandler.class));
        assertSame(rule, context.get(SignRuleSetInterface.class));
    }
    
    /**
     * test me.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void testRunInNewContext() throws McException, ClassNotFoundException
    {
        final SignRuleSetType type = mock(SignRuleSetType.class);
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaSignHandler sign = mock(ArenaSignHandler.class);
        when(sign.getArena()).thenReturn(arena);
        final Rule rule = new Rule(type, sign);
        
        final McLibInterface mclib = mock(McLibInterface.class);
        final Map<Object, Object> context = prepareMclib(mclib);
        
        doAnswer(new Answer<Void>(){

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                invocation.getArgumentAt(0, McRunnable.class).run();
                return null;
            }
            
        }).when(mclib).runInNewContext(any(McRunnable.class));
        
        // test
        rule.runInNewContext(() -> {
            // empty
        });
        
        assertEquals(3, context.size());
        assertSame(arena, context.get(ArenaInterface.class));
        assertSame(sign, context.get(ArenaSignHandler.class));
        assertSame(rule, context.get(SignRuleSetInterface.class));
    }
    
    /**
     * test me.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testCalculateInCopiedContext() throws McException, ClassNotFoundException
    {
        final SignRuleSetType type = mock(SignRuleSetType.class);
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaSignHandler sign = mock(ArenaSignHandler.class);
        when(sign.getArena()).thenReturn(arena);
        final Rule rule = new Rule(type, sign);
        
        final McLibInterface mclib = mock(McLibInterface.class);
        final Map<Object, Object> context = prepareMclib(mclib);
        
        doAnswer(new Answer<Object>(){

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                return invocation.getArgumentAt(0, McSupplier.class).get();
            }
            
        }).when(mclib).calculateInCopiedContext(any(McSupplier.class));
        
        // test
        rule.calculateInCopiedContext(() -> {
            return null;
        });
        
        assertEquals(3, context.size());
        assertSame(arena, context.get(ArenaInterface.class));
        assertSame(sign, context.get(ArenaSignHandler.class));
        assertSame(rule, context.get(SignRuleSetInterface.class));
    }
    
    /**
     * test me.
     * @throws McException 
     * @throws ClassNotFoundException 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testCalculateInNewContext() throws McException, ClassNotFoundException
    {
        final SignRuleSetType type = mock(SignRuleSetType.class);
        final ArenaInterface arena = mock(ArenaInterface.class);
        final ArenaSignHandler sign = mock(ArenaSignHandler.class);
        when(sign.getArena()).thenReturn(arena);
        final Rule rule = new Rule(type, sign);
        
        final McLibInterface mclib = mock(McLibInterface.class);
        final Map<Object, Object> context = prepareMclib(mclib);
        
        doAnswer(new Answer<Object>(){

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                return invocation.getArgumentAt(0, McSupplier.class).get();
            }
            
        }).when(mclib).calculateInNewContext(any(McSupplier.class));
        
        // test
        rule.calculateInNewContext(() -> {
            return null;
        });
        
        assertEquals(3, context.size());
        assertSame(arena, context.get(ArenaInterface.class));
        assertSame(sign, context.get(ArenaSignHandler.class));
        assertSame(rule, context.get(SignRuleSetInterface.class));
    }

    /**
     * @param mclib
     * @return context map
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private Map<Object, Object> prepareMclib(final McLibInterface mclib) throws ClassNotFoundException
    {
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.McLibCache"), "SERVICES", mclib); //$NON-NLS-1$ //$NON-NLS-2$
        final Map<Object, Object> context = new HashMap<>();
        doAnswer(new Answer<Void>(){

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                context.put(invocation.getArgumentAt(0, Object.class), invocation.getArgumentAt(1, Object.class));
                return null;
            }
            
        }).when(mclib).setContext(any(Class.class), any(Object.class));
        return context;
    }
    
    /**
     * rule impl.
     */
    private static final class Rule extends AbstractSignRule
    {

        /**
         * @param type
         * @param sign
         * @throws McException
         */
        public Rule(SignRuleSetType type, ArenaSignHandler sign) throws McException
        {
            super(type, sign);
        }

        @Override
        public void runInNewContext(McRunnable runnable) throws McException
        {
            super.runInNewContext(runnable);
        }

        @Override
        public void runInCopiedContext(McRunnable runnable) throws McException
        {
            super.runInCopiedContext(runnable);
        }

        @Override
        public <T> T calculateInNewContext(McSupplier<T> runnable) throws McException
        {
            return super.calculateInNewContext(runnable);
        }

        @Override
        public <T> T calculateInCopiedContext(McSupplier<T> runnable) throws McException
        {
            return super.calculateInCopiedContext(runnable);
        }
        
        
        
    }
    
}
