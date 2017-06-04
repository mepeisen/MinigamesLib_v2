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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.stat.PlayerStatisticId;

/**
 * The leader head.
 * 
 * @author mepeisen
 *
 */
public interface LeaderHeadComponentHandler extends ArenaComponentHandler
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
     * Sets the count of matches for {@link StatisticInterval#Recent}.
     * 
     * @param count
     *            count of matches.
     * 
     * @throws McException
     *             thrown if config cannot be saved.
     */
    void setRecentCount(int count) throws McException;
    
    /**
     * Returns the statistic scope.
     * 
     * @return statistic scope.
     */
    StatisticScope getScope();
    
    /**
     * Returns the statistic scope.
     * 
     * @param scope
     *            statistic scope.
     * 
     * @throws McException
     *             thrown if config cannot be saved.
     */
    void setScope(StatisticScope scope) throws McException;
    
    /**
     * returns the statistic interval.
     * 
     * @return statistic interval.
     */
    StatisticInterval getInterval();
    
    /**
     * returns the statistic interval.
     * 
     * @param interval statistic interval.
     * 
     * @throws McException thrown if config cannot be saved.
     */
    void setInterval(StatisticInterval interval) throws McException;
    
    /**
     * Returns the player statistic id.
     * 
     * @return player statistic to use.
     */
    PlayerStatisticId getPlayerStatisticId();
    
    /**
     * Returns the player statistic id.
     * 
     * @param statistic player statistic to use.
     * 
     * @throws McException thrown if config cannot be saved.
     */
    void setPlayerStatisticId(PlayerStatisticId statistic) throws McException;
    
    /**
     * Returns the place to display.
     * 
     * @return place
     */
    int place();
    
    /**
     * Returns the place to display.
     * 
     * @param place place
     * 
     * @throws McException thrown if config cannot be saved.
     */
    void setPlace(int place) throws McException;
    
    /**
     * Returns the ascending flag.
     * 
     * @return ascending flag.
     */
    boolean isAscending();
    
    /**
     * Returns the ascending flag.
     * 
     * @param flg ascending flag.
     * 
     * @throws McException thrown if config cannot be saved.
     */
    void setIsAscending(boolean flg) throws McException;
    
}
