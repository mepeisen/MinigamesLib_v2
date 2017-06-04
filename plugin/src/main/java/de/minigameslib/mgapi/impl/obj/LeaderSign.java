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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.events.ArenaStatisticEvent;
import de.minigameslib.mgapi.api.obj.LeaderSignHandler;
import de.minigameslib.mgapi.api.stat.PlayerStatisticId;

/**
 * @author mepeisen
 *
 */
public class LeaderSign extends AbstractProgrammableSign<LeaderSignData> implements LeaderSignHandler
{
    
    @Override
    protected Class<LeaderSignData> getDataClass()
    {
        return LeaderSignData.class;
    }
    
    @Override
    protected LeaderSignData createData()
    {
        return new LeaderSignData();
    }
    
    @Override
    public int getRecentCount()
    {
        return this.data.getRecentCount();
    }
    
    @Override
    public StatisticScope getScope()
    {
        return this.data.getScope();
    }
    
    @Override
    public StatisticInterval getInterval()
    {
        return this.data.getInterval();
    }
    
    @Override
    public PlayerStatisticId getPlayerStatisticId()
    {
        return this.data.getPlayerStatisticId();
    }
    
    @Override
    public int place()
    {
        return this.data.getPlace();
    }
    
    @Override
    public boolean isAscending()
    {
        return this.data.isAscending();
    }
    
    /**
     * arena statistics event.
     * 
     * @param evt
     */
    @McEventHandler
    public void onStatistics(ArenaStatisticEvent evt)
    {
        this.updateSign();
    }
    
    @Override
    public void setRecentCount(int count) throws McException
    {
        this.checkModifications();
        this.data.setRecentCount(count);
        this.saveData();
        this.updateSign();
    }
    
    @Override
    public void setScope(StatisticScope scope) throws McException
    {
        this.checkModifications();
        this.data.setScope(scope);
        this.saveData();
        this.updateSign();
    }
    
    @Override
    public void setInterval(StatisticInterval interval) throws McException
    {
        this.checkModifications();
        this.data.setInterval(interval);
        this.saveData();
        this.updateSign();
    }
    
    @Override
    public void setPlayerStatisticId(PlayerStatisticId statistic) throws McException
    {
        this.checkModifications();
        this.data.setPlayerStatisticId(statistic);
        this.saveData();
        this.updateSign();
    }
    
    @Override
    public void setPlace(int place) throws McException
    {
        this.checkModifications();
        this.data.setPlace(place);
        this.saveData();
        this.updateSign();
    }
    
    @Override
    public void setIsAscending(boolean flg) throws McException
    {
        this.checkModifications();
        this.data.setAscending(flg);
        this.saveData();
        this.updateSign();
    }

    @Override
    protected LocalizedMessageInterface getDefault(ArenaState state)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "signs.LeaderSign")
    public enum Messages implements LocalizedMessageInterface
    {
        
//        /**
//         * not within spectator zone.
//         */
//        @LocalizedMessage(defaultMessage = "spawn '%1$s' not within spectator zone!", severity = MessageSeverityType.Error)
//        @MessageComment(value = "not within spectator zone", args = { @Argument("component name") })
//        NotWithinSpectatorZone,
//        
//        /**
//         * not within battle zone.
//         */
//        @LocalizedMessageList({ "Your spawn is not within a spectator zone.", "Only spawning inside spectator zones will work." })
//        @MessageComment("not within spectator zone")
//        NotWithinSpectatorZone_Description,
        
    }
    
}
