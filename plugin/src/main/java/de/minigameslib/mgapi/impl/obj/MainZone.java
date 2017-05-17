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
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface.CuboidMode;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.arena.CheckSeverity;
import de.minigameslib.mgapi.api.obj.AbstractArenaZoneHandler;
import de.minigameslib.mgapi.api.obj.BasicZoneTypes;
import de.minigameslib.mgapi.api.obj.MainZoneHandler;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * @author mepeisen
 *
 */
public class MainZone extends AbstractArenaZoneHandler<MainZoneData> implements MainZoneHandler
{

    @Override
    protected Class<MainZoneData> getDataClass()
    {
        return MainZoneData.class;
    }

    @Override
    protected MainZoneData createData()
    {
        return new MainZoneData();
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
        
        final Collection<ZoneIdInterface> myMainZones = this.getArena().getZones(cuboid, CuboidMode.FindShared, BasicZoneTypes.Main);
        
        for (final ZoneIdInterface id : myMainZones)
        {
            if (!id.equals(this.getZone().getZoneId()))
            {
                result.add(new CheckFailure(
                        CheckSeverity.Warning,
                        Messages.OverlappingMainZone,
                        new Serializable[]{this.getName(), this.getArena().getHandler(id).getName()},
                        Messages.OverlappingMainZone_Description));
            }
        }
        
        return result;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "zones.Main")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * overlapping main zone.
         */
        @LocalizedMessage(defaultMessage = "zone '%1$s' overlaps main zone '%2$s'!", severity = MessageSeverityType.Warning)
        @MessageComment(value = "overlapping main zone", args = {@Argument("this zone name"), @Argument("other zone name")})
        OverlappingMainZone,
        
        /**
         * overlapping main zone.
         */
        @LocalizedMessageList({
            "Your zone is overlapping another main zone.",
            "The arena will work but if you decide to export or relocate your arena this may result in broken arenas.",
            "After importing the arena it may be broken."})
        @MessageComment("overlapping main zone")
        OverlappingMainZone_Description,
        
    }
    
}
