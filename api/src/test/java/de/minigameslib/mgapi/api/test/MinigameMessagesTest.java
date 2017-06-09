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

package de.minigameslib.mgapi.api.test;

import org.junit.Test;

import de.minigameslib.mclib.spigottest.CommonTestUtil;
import de.minigameslib.mgapi.api.MinigameMessages;

/**
 * test case for {@link MinigameMessages}
 * 
 * @author mepeisen
 */
public class MinigameMessagesTest
{
    
    /**
     * Tests the enum
     */
    @Test
    public void enumTest()
    {
        CommonTestUtil.testEnumClass(MinigameMessages.class);
    }
    
}
