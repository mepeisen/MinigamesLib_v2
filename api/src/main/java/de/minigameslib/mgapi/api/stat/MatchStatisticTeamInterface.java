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

package de.minigameslib.mgapi.api.stat;

import java.util.List;

import de.minigameslib.mgapi.api.match.MatchStatisticId;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * A player participating in a match.
 * 
 * @author mepeisen
 */
public interface MatchStatisticTeamInterface
{
    
    /**
     * Returns the team id.
     * 
     * @return team id.
     */
    TeamIdType getTeamId();
    
    /**
     * Statistic function
     * @param statistic
     * @return current statistic; {@code null} if statistic was not used
     */
    Integer getStatistic(MatchStatisticId statistic);
    
    /**
     * Returns the available statistics for this team.
     * @return statistics.
     */
    List<MatchStatisticId> getStatistics();
    
    /**
     * Returns the statistic for this player and given statistic id.
     * @param statistic
     * @return the statistic value;  {@code null} if statistic was not used
     */
    Long getStatistic(TeamStatisticId statistic);
    
}
