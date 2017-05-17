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

import java.io.Serializable;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.arena.CheckSeverity;
import de.minigameslib.mgapi.api.obj.AbstractArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.BasicZoneTypes;
import de.minigameslib.mgapi.api.obj.SpawnComponentHandler;
import de.minigameslib.mgapi.api.team.TeamIdType;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * @author mepeisen
 *
 */
public class SpawnComponent extends AbstractArenaComponentHandler<SpawnComponentData> implements SpawnComponentHandler
{

    @Override
    protected Class<SpawnComponentData> getDataClass()
    {
        return SpawnComponentData.class;
    }

    @Override
    protected SpawnComponentData createData()
    {
        return new SpawnComponentData();
    }

    @Override
    public TeamIdType getTeam()
    {
        return this.data.team;
    }

    @Override
    public void setTeam(TeamIdType team) throws McException
    {
        this.checkModifications();
        this.data.setTeam(team);
        this.saveData();
    }

    @Override
    public int getOrdering()
    {
        return this.data.getSpawnOrdering();
    }

    @Override
    public void setOrdering(int order) throws McException
    {
        this.checkModifications();
        this.data.setSpawnOrdering(order);
        this.saveData();
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
        
        final Location loc = this.getComponent().getLocation();
        
        final Collection<ZoneIdInterface> myZones = this.arena.getZones(loc, BasicZoneTypes.Battle);
        if (myZones.size() == 0)
        {
            result.add(new CheckFailure(CheckSeverity.Error, Messages.NotWithinBattleZone, new Serializable[]{this.getName()}, Messages.NotWithinBattleZone_Description));
        }
    
        return result;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "components.Spawn")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * not within battle zone.
         */
        @LocalizedMessage(defaultMessage = "spawn '%1$s' not within battle zone!", severity = MessageSeverityType.Error)
        @MessageComment(value = "not within battle zone", args = {@Argument("component name")})
        NotWithinBattleZone,
        
        /**
         * not within battle zone.
         */
        @LocalizedMessageList({
            "Your spawn is not within a battle zone.",
            "Only spawning inside battle zones will work."})
        @MessageComment("not within battle zone")
        NotWithinBattleZone_Description,
        
    }
    
}
