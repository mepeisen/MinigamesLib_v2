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

package de.minigameslib.mgapi.api.test.stat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import de.minigameslib.mgapi.api.match.MatchResult;
import de.minigameslib.mgapi.api.stat.CommonPlayerStatistics;
import de.minigameslib.mgapi.api.stat.CommonTeamStatistics;
import de.minigameslib.mgapi.api.stat.GameStatisticId;
import de.minigameslib.mgapi.api.stat.MatchStatisticInterface;
import de.minigameslib.mgapi.api.stat.MatchStatisticPlayerInterface;
import de.minigameslib.mgapi.api.stat.MatchStatisticTeamInterface;
import de.minigameslib.mgapi.api.stat.PlayerStatisticId;
import de.minigameslib.mgapi.api.stat.TeamStatisticId;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * Tests for {@link MatchStatisticInterface}.
 * 
 * @author mepeisen
 *
 */
public class MatchStatisticInterfaceTest
{
    
    /**
     * simple test case.
     */
    @Test
    public void testGetPlace()
    {
        final UUID unknown = UUID.randomUUID();
        final UUID player1 = UUID.randomUUID();
        final UUID player2 = UUID.randomUUID();
        final MatchResult place1 = new ResultImpl(1, Arrays.asList(player1, UUID.randomUUID(), UUID.randomUUID()), true);
        final MatchResult place2 = new ResultImpl(2, Arrays.asList(UUID.randomUUID(), UUID.randomUUID()), false);
        final MatchResult place3 = new ResultImpl(3, Arrays.asList(player2, UUID.randomUUID(), UUID.randomUUID()), false);
        
        final MatchStat stat = mock(MatchStat.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        when(stat.getResultCount()).thenReturn(3);
        when(stat.getResults(anyInt(), anyInt())).thenReturn(Arrays.asList(place1, place2, place3));
        
        assertEquals(1, stat.getPlace(player1));
        assertEquals(3, stat.getPlace(player2));
        assertEquals(-1, stat.getPlace(unknown));
    }
    
    /**
     * simple test case.
     */
    @Test
    public void testGetPStat()
    {
        final UUID unknown = UUID.randomUUID();
        final UUID player1 = UUID.randomUUID();
        
        final MatchStatisticPlayerInterface pstat = mock(MatchStatisticPlayerInterface.class);
        when(pstat.getStatistic(any(PlayerStatisticId.class))).thenReturn(123l);
        final MatchStat stat = mock(MatchStat.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        when(stat.getPlayer(player1)).thenReturn(pstat);
        
        assertNull(stat.getPlayerStat(unknown, CommonPlayerStatistics.Deaths));
        assertEquals(Long.valueOf(123), stat.getPlayerStat(player1, CommonPlayerStatistics.Deaths));
    }
    
    /**
     * simple test case.
     */
    @Test
    public void testGetTStat()
    {
        final TeamIdType unknown = CommonTeams.Aqua;
        final TeamIdType team1 = CommonTeams.Black;
        
        final MatchStatisticTeamInterface tstat = mock(MatchStatisticTeamInterface.class);
        when(tstat.getStatistic(any(TeamStatisticId.class))).thenReturn(123l);
        final MatchStat stat = mock(MatchStat.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        when(stat.getTeam(team1)).thenReturn(tstat);
        
        assertNull(stat.getTeamStat(unknown, CommonTeamStatistics.Deaths));
        assertEquals(Long.valueOf(123), stat.getTeamStat(team1, CommonTeamStatistics.Deaths));
    }
    
    /**
     * statistics.
     */
    private static class MatchStat implements MatchStatisticInterface
    {

        @Override
        public LocalDateTime getStarted()
        {
            return null;
        }

        @Override
        public LocalDateTime getFinished()
        {
            return null;
        }

        @Override
        public int getPlayerCount()
        {
            return 0;
        }

        @Override
        public MatchStatisticPlayerInterface getPlayer(UUID uuid)
        {
            return null;
        }

        @Override
        public List<MatchStatisticPlayerInterface> getPlayers(int start, int limit)
        {
            return null;
        }

        @Override
        public int getTeamCount()
        {
            return 0;
        }

        @Override
        public MatchStatisticTeamInterface getTeam(TeamIdType type)
        {
            return null;
        }

        @Override
        public List<MatchStatisticTeamInterface> getTeams(int start, int limit)
        {
            return null;
        }

        @Override
        public Collection<UUID> getWinners(int start, int limit)
        {
            return null;
        }

        @Override
        public int getWinnerCount()
        {
            return 0;
        }

        @Override
        public Collection<UUID> getLosers(int start, int limit)
        {
            return null;
        }

        @Override
        public int getLoserCount()
        {
            return 0;
        }

        @Override
        public Collection<MatchResult> getResults(int start, int limit)
        {
            return null;
        }

        @Override
        public int getResultCount()
        {
            return 0;
        }

        @Override
        public MatchResult getResult(int place)
        {
            return null;
        }

        @Override
        public Long getStatistic(GameStatisticId statistic)
        {
            return null;
        }
        
    }
    
    /**
     * result.
     */
    private static final class ResultImpl implements MatchResult
    {
        
        /**
         * place.
         */
        private final int place;
        
        /**
         * players.
         */
        private final Collection<UUID> players;
        
        /**
         * win flag.
         */
        private final boolean isWin;

        /**
         * Constructor.
         * @param place
         * @param players
         * @param isWin
         */
        public ResultImpl(int place, Collection<UUID> players, boolean isWin)
        {
            this.place = place;
            this.players = players;
            this.isWin = isWin;
        }

        @Override
        public int getPlace()
        {
            return this.place;
        }

        @Override
        public Collection<UUID> getPlayers()
        {
            return this.players;
        }

        @Override
        public boolean isWin()
        {
            return this.isWin;
        }
        
    }
    
}
