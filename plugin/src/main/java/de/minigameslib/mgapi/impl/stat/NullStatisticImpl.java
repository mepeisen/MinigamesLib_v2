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

package de.minigameslib.mgapi.impl.stat;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.stat.GameStatisticId;
import de.minigameslib.mgapi.api.stat.MatchStatisticInterface;
import de.minigameslib.mgapi.api.stat.PlayerStatisticId;
import de.minigameslib.mgapi.api.stat.StatisticInterface;
import de.minigameslib.mgapi.api.stat.StatisticServiceInterface;
import de.minigameslib.mgapi.api.stat.TeamStatisticId;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * Null statistics.
 * 
 * @author mepeisen
 */
public class NullStatisticImpl implements StatisticServiceInterface
{

    /**
     * @author mepeisen
     *
     */
    public class DummyStatistics implements StatisticInterface
    {
        
        @Override
        public int getMatchCount()
        {
            return 0;
        }
        
        @Override
        public List<MatchStatisticInterface> getMatches(int start, int limit) throws McException
        {
            return Collections.emptyList();
        }
        
        @Override
        public int getBestPlace(UUID playerUuid) throws McException
        {
            return -1;
        }
        
        @Override
        public Long getBestValue(UUID playerUuid, PlayerStatisticId statistic) throws McException
        {
            return null;
        }
        
        @Override
        public Long getWorstValue(UUID playerUuid, PlayerStatisticId statistic) throws McException
        {
            return null;
        }
        
        @Override
        public Long getStatistic(UUID playerUuid, PlayerStatisticId statistic) throws McException
        {
            return null;
        }
        
        @Override
        public int getPlayerCount(PlayerStatisticId statistic) throws McException
        {
            return 0;
        }
        
        @Override
        public int getPlace(UUID player, PlayerStatisticId statistic, boolean ascending) throws McException
        {
            return -1;
        }
        
        @Override
        public List<UUID> getPlayerLeaders(PlayerStatisticId statistic, int start, int limit, boolean ascending) throws McException
        {
            return Collections.emptyList();
        }
        
        @Override
        public Long getBestValue(TeamIdType team, TeamStatisticId statistic) throws McException
        {
            return null;
        }
        
        @Override
        public Long getWorstValue(TeamIdType team, TeamStatisticId statistic) throws McException
        {
            return null;
        }
        
        @Override
        public Long getStatistic(TeamIdType team, TeamStatisticId statistic) throws McException
        {
            return null;
        }
        
        @Override
        public int getTeamCount(TeamStatisticId statistic) throws McException
        {
            return 0;
        }
        
        @Override
        public int getPlace(TeamIdType team, TeamStatisticId statistic, boolean ascending) throws McException
        {
            return -1;
        }
        
        @Override
        public List<TeamIdType> getTeamLeaders(TeamStatisticId statistic, int start, int limit, boolean ascending) throws McException
        {
            return Collections.emptyList();
        }
        
        @Override
        public Long getStatistic(GameStatisticId statistic) throws McException
        {
            return null;
        }
        
        @Override
        public Long getBestValue(GameStatisticId statistic) throws McException
        {
            return null;
        }
        
        @Override
        public Long getWorstValue(GameStatisticId statistic) throws McException
        {
            return null;
        }
        
    }

    @Override
    public StatisticInterface getAllTimeStatistic(ArenaInterface arena)
    {
        return new DummyStatistics();
    }

    @Override
    public StatisticInterface getMonthlyStatistic(ArenaInterface arena, YearMonth month)
    {
        return new DummyStatistics();
    }

    @Override
    public StatisticInterface getDailyStatistic(ArenaInterface arena, LocalDate date)
    {
        return new DummyStatistics();
    }

    @Override
    public StatisticInterface getRecentStatistic(ArenaInterface arena, int count)
    {
        return new DummyStatistics();
    }

    @Override
    public StatisticInterface getAllTimeStatistic(ArenaTypeInterface type)
    {
        return new DummyStatistics();
    }

    @Override
    public StatisticInterface getMonthlyStatistic(ArenaTypeInterface type, YearMonth month)
    {
        return new DummyStatistics();
    }

    @Override
    public StatisticInterface getDailyStatistic(ArenaTypeInterface type, LocalDate date)
    {
        return new DummyStatistics();
    }

    @Override
    public StatisticInterface getRecentStatistic(ArenaTypeInterface type, int count)
    {
        return new DummyStatistics();
    }
    
    
    
}
