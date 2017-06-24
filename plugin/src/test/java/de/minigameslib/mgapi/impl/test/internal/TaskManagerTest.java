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

package de.minigameslib.mgapi.impl.test.internal;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.impl.internal.TaskManager;

/**
 * @author mepeisen
 *
 */
public class TaskManagerTest
{
    
    /**
     * test me.
     */
    @Test
    public void testCommon()
    {
        final TaskManager manager = new TaskManager();
        
        final AtomicBoolean bool = new AtomicBoolean(false);
        synchronized (bool)
        {
            manager.queue(() -> {
                synchronized (bool)
                {
                    bool.set(true);
                    bool.notifyAll();
                }
            });
            try
            {
                bool.wait(500);
            }
            catch (@SuppressWarnings("unused") InterruptedException ex)
            {
                // silently ignore
            }
        }
        
        assertTrue(bool.get());
    }
    
    /**
     * test me.
     */
    @Test
    public void testException()
    {
        final TaskManager manager = new TaskManager();
        final Logger logger = Logger.getLogger(manager.getClass().getName());
        logger.setLevel(Level.OFF);
        
        final AtomicBoolean bool = new AtomicBoolean(false);
        synchronized (bool)
        {
            manager.queue(() -> {
                synchronized (bool)
                {
                    bool.set(true);
                    bool.notifyAll();
                    throw new McException(CommonMessages.InternalError);
                }
            });
            try
            {
                bool.wait(500);
            }
            catch (@SuppressWarnings("unused") InterruptedException ex)
            {
                // silently ignore
            }
        }
        
        assertTrue(bool.get());
    }
    
}
