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

import java.util.List;

import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.internal.progress.HandyReturnValues;

/**
 * @author mepeisen
 * @param <T>
 *            argument class
 *
 */
public class CheckedCaptor<T>
{
    
    /** return values. */
    HandyReturnValues                        handyReturnValues = new HandyReturnValues();
    
    /** capturing matcher. */
    private final CheckedCapturingMatcher<T> capturingMatcher;
    /** class. */
    private final Class<T>                   clazz;
    
    /**
     * Constructor.
     * 
     * @param clazz
     */
    CheckedCaptor(Class<T> clazz)
    {
        this.clazz = clazz;
        this.capturingMatcher = new CheckedCapturingMatcher<>(this.clazz);
    }
    
    /**
     * Use it to capture the argument. This method <b>must be used inside of verification</b>.
     * <p>
     * Internally, this method registers a special implementation of an {@link ArgumentMatcher}. This argument matcher stores the argument value so that you can use it later to perform assertions.
     * <p>
     * See examples in javadoc for {@link ArgumentCaptor} class.
     * 
     * @return null or default values
     */
    public T capture()
    {
        Matchers.argThat(this.capturingMatcher);
        return this.handyReturnValues.returnFor(this.clazz);
    }
    
    /**
     * Returns the captured value of the argument. When capturing varargs use {@link #getAllValues()}.
     * <p>
     * If verified method was called multiple times then this method it returns the latest captured value.
     * <p>
     * See examples in javadoc for {@link ArgumentCaptor} class.
     * 
     * @return captured argument value
     */
    public T getValue()
    {
        return this.capturingMatcher.getLastValue();
    }
    
    /**
     * Returns all captured values. Use it when capturing varargs or when the verified method was called multiple times. When varargs method was called multiple times, this method returns merged list
     * of all values from all invocations.
     * <p>
     * Example:
     * 
     * <pre class="code">
     * <code class="java">
     *   mock.doSomething(new Person("John");
     *   mock.doSomething(new Person("Jane");
     *
     *   ArgumentCaptor&lt;Person&gt; peopleCaptor = ArgumentCaptor.forClass(Person.class);
     *   verify(mock, times(2)).doSomething(peopleCaptor.capture());
     *   
     *   List&lt;Person&gt; capturedPeople = peopleCaptor.getAllValues();
     *   assertEquals("John", capturedPeople.get(0).getName());
     *   assertEquals("Jane", capturedPeople.get(1).getName());
     * </pre>
     *
     * Example of capturing varargs:
     * <pre class="code"><code class="java">
       *   mock.countPeople(new Person("John"), new Person("Jane"); //vararg method
       *
       *   ArgumentCaptor&lt;Person&gt; peopleCaptor = ArgumentCaptor.forClass(Person.class);
       *
       *   verify(mock).countPeople(peopleCaptor.capture());
       *
       *   List expected = asList(new Person("John"), new Person("Jane"));
       *   assertEquals(expected, peopleCaptor.getAllValues());
       * </code>
     * </pre>
     * 
     * See more examples in javadoc for {@link ArgumentCaptor} class.
     * 
     * @return captured argument value
     */
    public List<T> getAllValues()
    {
        return this.capturingMatcher.getAllValues();
    }
    
    /**
     * Build a new <code>ArgumentCaptor</code>.
     * <p>
     * Note that an <code>ArgumentCaptor</code> <b>*don't do any type checks*</b>, it is only there to avoid casting in your code. This might however change (type checks could be added) in a future
     * major release.
     *
     * @param clazz
     *            Type matching the parameter to be captured.
     * @param <T>
     *            Type of clazz
     * @return A new ArgumentCaptor
     */
    public static <T> CheckedCaptor<T> forClass(Class<T> clazz)
    {
        return new CheckedCaptor<>(clazz);
    }
    
}
