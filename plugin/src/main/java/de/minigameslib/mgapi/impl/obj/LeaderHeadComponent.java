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

import java.util.Collection;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.events.ArenaStatisticEvent;
import de.minigameslib.mgapi.api.obj.AbstractArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.LeaderHeadComponentHandler;
import de.minigameslib.mgapi.api.stat.PlayerStatisticId;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * @author mepeisen
 *
 */
public class LeaderHeadComponent extends AbstractArenaComponentHandler<LeaderHeadComponentData> implements LeaderHeadComponentHandler
{
    
    @Override
    protected Class<LeaderHeadComponentData> getDataClass()
    {
        return LeaderHeadComponentData.class;
    }
    
    @Override
    protected LeaderHeadComponentData createData()
    {
        return new LeaderHeadComponentData();
    }
    
    @Override
    protected Plugin getPlugin()
    {
        return MinigamesPlugin.instance().getPlugin();
    }
    
    @Override
    public Collection<CheckFailure> check()
    {
        final Collection<CheckFailure> result = super.check();
        
        // final Location loc = this.getComponent().getLocation();
        //
        // final Collection<ZoneIdInterface> myZones = this.arena.getZones(loc, BasicZoneTypes.Spectator);
        // if (myZones.size() == 0)
        // {
        // result.add(new CheckFailure(CheckSeverity.Warning, Messages.NotWithinSpectatorZone, new Serializable[]{this.getName()}, Messages.NotWithinSpectatorZone_Description));
        // }
        
        return result;
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
        this.update();
    }
    
    /**
     * update this component and display the correct head.
     */
    private void update()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void setRecentCount(int count) throws McException
    {
        this.checkModifications();
        this.data.setRecentCount(count);
        this.saveData();
        this.update();
    }
    
    @Override
    public void setScope(StatisticScope scope) throws McException
    {
        this.checkModifications();
        this.data.setScope(scope);
        this.saveData();
        this.update();
    }
    
    @Override
    public void setInterval(StatisticInterval interval) throws McException
    {
        this.checkModifications();
        this.data.setInterval(interval);
        this.saveData();
        this.update();
    }
    
    @Override
    public void setPlayerStatisticId(PlayerStatisticId statistic) throws McException
    {
        this.checkModifications();
        this.data.setPlayerStatisticId(statistic);
        this.saveData();
        this.update();
    }
    
    @Override
    public void setPlace(int place) throws McException
    {
        this.checkModifications();
        this.data.setPlace(place);
        this.saveData();
        this.update();
    }
    
    @Override
    public void setIsAscending(boolean flg) throws McException
    {
        this.checkModifications();
        this.data.setAscending(flg);
        this.saveData();
        this.update();
    }
    
}
