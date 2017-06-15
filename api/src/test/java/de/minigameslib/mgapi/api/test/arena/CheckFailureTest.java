/*
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
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Test;

import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface.DynamicArg;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface.DynamicListArg;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.arena.CheckSeverity;

/**
 * test case for {@link CheckFailure}
 * 
 * @author mepeisen
 */
public class CheckFailureTest
{
    
    /**
     * Tests the constructor
     */
    @Test
    public void test1()
    {
        final CheckFailure failure = new CheckFailure(CheckSeverity.Error, Msg.SingleLine, Msg.MultiLine);
        
        assertEquals(CheckSeverity.Error, failure.getSeverity());
        assertTrue(failure.getTitle() instanceof DynamicArg);
        assertTrue(failure.getDetails() instanceof DynamicListArg);
    }
    
    /**
     * Tests the constructor
     */
    @Test
    public void test2()
    {
        final CheckFailure failure = new CheckFailure(CheckSeverity.Error, Msg.SingleLine, Msg.SingleLine);
        
        assertEquals(CheckSeverity.Error, failure.getSeverity());
        assertTrue(failure.getTitle() instanceof DynamicArg);
        assertTrue(failure.getDetails() instanceof DynamicArg);
    }
    
    /**
     * Tests the constructor
     */
    @Test
    public void test3()
    {
        final CheckFailure failure = new CheckFailure(CheckSeverity.Error, Msg.SingleLine, new Serializable[]{"FOO"}, Msg.MultiLine, "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        
        assertEquals(CheckSeverity.Error, failure.getSeverity());
        assertTrue(failure.getTitle() instanceof DynamicArg);
        assertTrue(failure.getDetails() instanceof DynamicListArg);
    }
    
    /**
     * Tests the constructor
     */
    @Test
    public void test4()
    {
        final CheckFailure failure = new CheckFailure(CheckSeverity.Error, Msg.SingleLine, new Serializable[]{"FOO"}, Msg.SingleLine, "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        
        assertEquals(CheckSeverity.Error, failure.getSeverity());
        assertTrue(failure.getTitle() instanceof DynamicArg);
        assertTrue(failure.getDetails() instanceof DynamicArg);
    }
    
    /**
     * Msg
     */
    @LocalizedMessages("core")
    private enum Msg implements LocalizedMessageInterface
    {
        /** dummy msg. */
        @LocalizedMessage(defaultMessage = "single line")
        SingleLine,
        /** dummy msg. */
        @LocalizedMessageList({"line 1", "line 2"})
        MultiLine,
    }
    
}
