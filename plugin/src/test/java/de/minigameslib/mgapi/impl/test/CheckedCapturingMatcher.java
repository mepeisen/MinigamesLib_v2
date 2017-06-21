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

package de.minigameslib.mgapi.impl.test;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.VarargMatcher;

/**
 * @author mepeisen
 * @param <T>
 *
 */
public class CheckedCapturingMatcher<T> extends ArgumentMatcher<T> implements CapturesArguments, VarargMatcher
{
    
    /** serial version uid. */
    private static final long   serialVersionUID = 4274067078639307295L;
    /** arguments. */
    private final LinkedList<T> arguments        = new LinkedList<>();
    
    /** class. */
    private final Class<T> clazz;
    
    /**
     * @param clazz
     */
    public CheckedCapturingMatcher(final Class<T> clazz)
    {
        this.clazz = clazz;
    }
    
    @Override
    public boolean matches(Object argument)
    {
        return this.clazz.isInstance(argument);
    }
    
    @Override
    public void describeTo(Description description)
    {
        description.appendText("<Capturing argument:" + this.clazz.getName() + ">"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Returns last value.
     * @return last value
     */
    public T getLastValue()
    {
        if (this.arguments.isEmpty())
        {
            new Reporter().noArgumentValueWasCaptured();
            return null;
        }
        return this.arguments.getLast();
    }
    
    /**
     * returns all values.
     * @return all values.
     */
    public List<T> getAllValues()
    {
        return this.arguments;
    }
    
    @Override
    public void captureFrom(Object argument)
    {
        this.arguments.add(this.clazz.cast(argument));
    }
}
