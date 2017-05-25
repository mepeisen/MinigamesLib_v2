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

package de.minigameslib.mgapi.api.obj;

import de.minigameslib.mgapi.api.stat.PlayerStatisticId;

/**
 * The leader head.
 * 
 * @author mepeisen
 *
 */
public interface LeaderHeadHandler extends ArenaComponentHandler
{
    
    /**
     * The scope of statistics.
     */
    enum StatisticScope
    {
        /** the arena. */
        Arena,
        /** the arena type. */
        ArenaType,
    }
    
    /**
     * The type of statistic interval.
     */
    enum StatisticInterval
    {
        /** all time statistics. */
        AllTime,
        /**
         * last month.
         */
        LastMonth,
        /**
         * last day.
         */
        LastDay,
        /**
         * current month.
         */
        CurrentMonth,
        /**
         * current day.
         */
        CurrentDay,
        /**
         * last x matches.
         */
        Recent,
    }
    
    /**
     * Returns the count of matches for {@link StatisticInterval#Recent}.
     * 
     * @return count of matches.
     */
    int getRecentCount();
    
    /**
     * Returns the statistic scope.
     * @return statistic scope.
     */
    StatisticScope getScope();
    
    /**
     * returns the statistic interval.
     * @return statistic interval.
     */
    StatisticInterval getInterval();
    
    /**
     * Returns the player statistic id.
     * @return player statistic to use.
     */
    PlayerStatisticId getPlayerStatisticId();
    
    /**
     * Returns the place to display.
     * @return place
     */
    int place();
    
    /**
     * Returns the ascending flag.
     * @return ascending flag.
     */
    boolean isAscending();
    
}
