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

package de.minigameslib.mgapi.impl.stat.file;

import java.util.List;
import java.util.UUID;

import de.minigameslib.mgapi.api.stat.GameStatisticId;
import de.minigameslib.mgapi.api.stat.MatchStatisticInterface;
import de.minigameslib.mgapi.api.stat.PlayerStatisticId;
import de.minigameslib.mgapi.api.stat.StatisticInterface;
import de.minigameslib.mgapi.api.stat.TeamStatisticId;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * @author mepeisen
 *
 */
public class FileStatisticImpl implements StatisticInterface
{
    
    /**
     * Constructor.
     * @param collect
     */
    public FileStatisticImpl(List<FileStatRegistryEntry> collect)
    {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getMatchCount()
     */
    @Override
    public int getMatchCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getMatches(int, int)
     */
    @Override
    public List<MatchStatisticInterface> getMatches(int start, int limit)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getBestPlace(java.util.UUID)
     */
    @Override
    public int getBestPlace(UUID playerUuid)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getBestValue(java.util.UUID, de.minigameslib.mgapi.api.stat.PlayerStatisticId)
     */
    @Override
    public long getBestValue(UUID playerUuid, PlayerStatisticId statistic)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getWorstValue(java.util.UUID, de.minigameslib.mgapi.api.stat.PlayerStatisticId)
     */
    @Override
    public long getWorstValue(UUID playerUuid, PlayerStatisticId statistic)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getStatistic(java.util.UUID, de.minigameslib.mgapi.api.stat.PlayerStatisticId)
     */
    @Override
    public long getStatistic(UUID playerUuid, PlayerStatisticId statistic)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getPlayerCount(de.minigameslib.mgapi.api.stat.PlayerStatisticId)
     */
    @Override
    public int getPlayerCount(PlayerStatisticId statistic)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getPlace(java.util.UUID, de.minigameslib.mgapi.api.stat.PlayerStatisticId, boolean)
     */
    @Override
    public int getPlace(UUID player, PlayerStatisticId statistic, boolean ascending)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getPlayerLeaders(de.minigameslib.mgapi.api.stat.PlayerStatisticId, int, int, boolean)
     */
    @Override
    public List<UUID> getPlayerLeaders(PlayerStatisticId statistic, int start, int limit, boolean ascending)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getBestValue(de.minigameslib.mgapi.api.team.TeamIdType, de.minigameslib.mgapi.api.stat.PlayerStatisticId)
     */
    @Override
    public long getBestValue(TeamIdType team, PlayerStatisticId statistic)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getWorstValue(de.minigameslib.mgapi.api.team.TeamIdType, de.minigameslib.mgapi.api.stat.PlayerStatisticId)
     */
    @Override
    public long getWorstValue(TeamIdType team, PlayerStatisticId statistic)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getStatistic(de.minigameslib.mgapi.api.team.TeamIdType, de.minigameslib.mgapi.api.stat.TeamStatisticId)
     */
    @Override
    public long getStatistic(TeamIdType team, TeamStatisticId statistic)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getTeamCount(de.minigameslib.mgapi.api.stat.TeamStatisticId)
     */
    @Override
    public int getTeamCount(TeamStatisticId statistic)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getPlace(de.minigameslib.mgapi.api.team.TeamIdType, de.minigameslib.mgapi.api.stat.TeamStatisticId, boolean)
     */
    @Override
    public int getPlace(TeamIdType team, TeamStatisticId statistic, boolean ascending)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getTeamLeaders(de.minigameslib.mgapi.api.stat.TeamStatisticId, int, int, boolean)
     */
    @Override
    public List<TeamIdType> getTeamLeaders(TeamStatisticId statistic, int start, int limit, boolean ascending)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticInterface#getStatistic(de.minigameslib.mgapi.api.stat.GameStatisticId)
     */
    @Override
    public long getStatistic(GameStatisticId statistic)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
