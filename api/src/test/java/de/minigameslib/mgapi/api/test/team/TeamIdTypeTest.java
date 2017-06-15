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

package de.minigameslib.mgapi.api.test.team;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * Test for {@link TeamIdType}.
 * 
 * @author mepeisen
 *
 */
public class TeamIdTypeTest
{
    
    /**
     * Test for delegating method
     * @throws ClassNotFoundException 
     */
    @Test
    public void testMe() throws ClassNotFoundException
    {
        final MinigamesLibInterface mglib = mock(MinigamesLibInterface.class);
        when(mglib.isSpecialTeam(CommonTeams.Unknown)).thenReturn(true);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mgapi.api.MglibCache"), "SERVICES", mglib); //$NON-NLS-1$ //$NON-NLS-2$
        
        assertTrue(CommonTeams.Unknown.isSpecial());
        assertFalse(CommonTeams.Black.isSpecial());
    }
    
}
