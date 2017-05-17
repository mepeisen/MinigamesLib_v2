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

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface.CuboidMode;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.arena.CheckSeverity;
import de.minigameslib.mgapi.api.obj.AbstractArenaZoneHandler;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.obj.BasicComponentTypes;
import de.minigameslib.mgapi.api.obj.BasicZoneTypes;
import de.minigameslib.mgapi.api.obj.SpectatorZoneHandler;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * @author mepeisen
 *
 */
public class SpectatorZone extends AbstractArenaZoneHandler<SpectatorZoneData> implements SpectatorZoneHandler
{

    @Override
    protected Class<SpectatorZoneData> getDataClass()
    {
        return SpectatorZoneData.class;
    }

    @Override
    protected SpectatorZoneData createData()
    {
        return new SpectatorZoneData();
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
        
        final Cuboid cuboid = this.getZone().getCuboid();
        
        final Collection<ZoneIdInterface> myBattleZones = this.getArena().getZones(cuboid, CuboidMode.FindShared, BasicZoneTypes.Battle);
        final Collection<ComponentIdInterface> mySpectatorSpawns = this.getArena().getComponents(cuboid, BasicComponentTypes.SpectatorSpawn);

        for (final ZoneIdInterface id : myBattleZones)
        {
            final ArenaZoneHandler handler = this.getArena().getHandler(id);
            result.add(new CheckFailure(
                    CheckSeverity.Error,
                    Messages.OverlapsBattleZone,
                    new Serializable[]{this.getName(), handler.getName()},
                    Messages.OverlapsBattleZone_Description));
        }
        if (mySpectatorSpawns.size() == 0)
        {
            result.add(new CheckFailure(CheckSeverity.Warning, Messages.NoSpawns, new Serializable[]{this.getName()}, Messages.NoSpawns_Description));
        }
        
        return result;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "zones.Spectator")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * overlaps with battle zone.
         */
        @LocalizedMessage(defaultMessage = "zone '%1$s' overlaps battle zone '%2$s'!", severity = MessageSeverityType.Warning)
        @MessageComment(value = "overlaps with battle zone", args = {@Argument("this zone name"), @Argument("battle zone name")})
        OverlapsBattleZone,
        
        /**
         * overlaps with battle zone.
         */
        @LocalizedMessageList({
            "Your spectator zone is overlapping a battle zone.",
            "The arena will work but there may be some conflicting rules influencing the battle or the spectators."})
        @MessageComment("overlaps with battle zone")
        OverlapsBattleZone_Description,
        
        /**
         * no spawns.
         */
        @LocalizedMessage(defaultMessage = "zone '%1$s' does not have spawns!", severity = MessageSeverityType.Warning)
        @MessageComment(value = "no spawns", args = {@Argument("this zone name")})
        NoSpawns,
        
        /**
         * no spawns.
         */
        @LocalizedMessageList({
            "Your spectator zone does not contain spectator spawns.",
            "The arena will work but spectators may not reach your spectator zone.",
            "Is it really what you want?"})
        @MessageComment("no spawns")
        NoSpawns_Description,
        
    }
    
}
