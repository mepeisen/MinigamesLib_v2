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

package de.minigameslib.mgapi.impl.test.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.stat.CommonPlayerStatistics;
import de.minigameslib.mgapi.api.stat.CommonTeamStatistics;
import de.minigameslib.mgapi.api.stat.GameStatisticId;
import de.minigameslib.mgapi.api.stat.StatisticInterface;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.impl.stat.NullStatisticImpl;
import de.minigameslib.mgapi.impl.stat.NullStatisticImpl.DummyStatistics;

/**
 * @author mepeisen
 *
 */
public class NullStatisticTest
{
    
    /**
     * test me.
     */
    @Test
    public void testMain()
    {
        final NullStatisticImpl main = new NullStatisticImpl();

        assertTrue(main.getAllTimeStatistic(mock(ArenaInterface.class)) instanceof DummyStatistics);
        assertTrue(main.getMonthlyStatistic(mock(ArenaInterface.class), YearMonth.now()) instanceof DummyStatistics);
        assertTrue(main.getDailyStatistic(mock(ArenaInterface.class), LocalDate.now()) instanceof DummyStatistics);
        assertTrue(main.getRecentStatistic(mock(ArenaInterface.class), 1) instanceof DummyStatistics);
        assertTrue(main.getAllTimeStatistic(mock(ArenaTypeInterface.class)) instanceof DummyStatistics);
        assertTrue(main.getMonthlyStatistic(mock(ArenaTypeInterface.class), YearMonth.now()) instanceof DummyStatistics);
        assertTrue(main.getDailyStatistic(mock(ArenaTypeInterface.class), LocalDate.now()) instanceof DummyStatistics);
        assertTrue(main.getRecentStatistic(mock(ArenaTypeInterface.class), 1) instanceof DummyStatistics);
    }
    
    /**
     * test me.
     * @throws McException 
     */
    @Test
    public void testStat() throws McException
    {
        final NullStatisticImpl main = new NullStatisticImpl();
        final StatisticInterface stat = main.getAllTimeStatistic(mock(ArenaInterface.class));
        
        assertEquals(0, stat.getMatchCount());
        assertEquals(0, stat.getMatches(0, 1).size());
        assertEquals(-1, stat.getBestPlace(UUID.randomUUID()));
        
        assertNull(stat.getBestValue(UUID.randomUUID(), CommonPlayerStatistics.Deaths));
        assertNull(stat.getWorstValue(UUID.randomUUID(), CommonPlayerStatistics.Deaths));
        assertNull(stat.getStatistic(UUID.randomUUID(), CommonPlayerStatistics.Points));
        assertEquals(0, stat.getPlayerCount(CommonPlayerStatistics.Deaths));
        assertEquals(-1, stat.getPlace(UUID.randomUUID(), CommonPlayerStatistics.Deaths, true));
        assertEquals(0, stat.getPlayerLeaders(CommonPlayerStatistics.Deaths, 0, 1, true).size());
        
        assertNull(stat.getBestValue(CommonTeams.Aqua, CommonTeamStatistics.Deaths));
        assertNull(stat.getWorstValue(CommonTeams.Aqua, CommonTeamStatistics.Deaths));
        assertNull(stat.getStatistic(CommonTeams.Aqua, CommonTeamStatistics.Points));
        assertEquals(0, stat.getTeamCount(CommonTeamStatistics.Deaths));
        assertEquals(-1, stat.getPlace(CommonTeams.Aqua, CommonTeamStatistics.Deaths, true));
        assertEquals(0, stat.getTeamLeaders(CommonTeamStatistics.Deaths, 0, 1, true).size());
        
        assertNull(stat.getBestValue(mock(GameStatisticId.class)));
        assertNull(stat.getWorstValue(mock(GameStatisticId.class)));
        assertNull(stat.getStatistic(mock(GameStatisticId.class)));
    }
    
}
