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

package de.minigameslib.mgapi.impl.obj;

import de.minigameslib.mclib.shared.api.com.PersistentField;
import de.minigameslib.mgapi.api.obj.LeaderSignHandler.StatisticInterval;
import de.minigameslib.mgapi.api.obj.LeaderSignHandler.StatisticScope;
import de.minigameslib.mgapi.api.stat.PlayerStatisticId;

/**
 * @author mepeisen
 *
 */
public class LeaderSignData extends AbstractProgrammableSignData
{
    
    /**
     * count of matches for {@link StatisticInterval#Recent}.
     */
    @PersistentField
    protected int recentCount;
    
    /**
     * Returns the statistic scope.
     */
    @PersistentField
    protected StatisticScope scope;
    
    /**
     * returns the statistic interval.
     */
    @PersistentField
    protected StatisticInterval interval;
    
    /**
     * Returns the player statistic id.
     */
    @PersistentField
    protected PlayerStatisticId playerStatisticId;
    
    /**
     * Returns the place to display.
     */
    @PersistentField
    protected int place;
    
    /**
     * Returns the ascending flag.
     */
    @PersistentField
    protected boolean isAscending;

    /**
     * @return the recentCount
     */
    public int getRecentCount()
    {
        return this.recentCount;
    }

    /**
     * @param recentCount the recentCount to set
     */
    public void setRecentCount(int recentCount)
    {
        this.recentCount = recentCount;
    }

    /**
     * @return the scope
     */
    public StatisticScope getScope()
    {
        return this.scope;
    }

    /**
     * @param scope the scope to set
     */
    public void setScope(StatisticScope scope)
    {
        this.scope = scope;
    }

    /**
     * @return the interval
     */
    public StatisticInterval getInterval()
    {
        return this.interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(StatisticInterval interval)
    {
        this.interval = interval;
    }

    /**
     * @return the playerStatisticId
     */
    public PlayerStatisticId getPlayerStatisticId()
    {
        return this.playerStatisticId;
    }

    /**
     * @param playerStatisticId the playerStatisticId to set
     */
    public void setPlayerStatisticId(PlayerStatisticId playerStatisticId)
    {
        this.playerStatisticId = playerStatisticId;
    }

    /**
     * @return the place
     */
    public int getPlace()
    {
        return this.place;
    }

    /**
     * @param place the place to set
     */
    public void setPlace(int place)
    {
        this.place = place;
    }

    /**
     * @return the isAscending
     */
    public boolean isAscending()
    {
        return this.isAscending;
    }

    /**
     * @param isAscending the isAscending to set
     */
    public void setAscending(boolean isAscending)
    {
        this.isAscending = isAscending;
    }
    
}
