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

package de.minigameslib.mgapi.impl.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * @author mepeisen
 *
 */
public class AdminComponentTpCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.isOnline() && command.checkOpPermission(MglibPerms.CommandAdminComp);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.checkOnline();
        command.permOpThrowException(MglibPerms.CommandAdminComp, command.getCommandPath());
        
        final ArenaInterface arena = Mg2Command.getArena(command, Messages.Usage);
        final ComponentInterface comp = Mg2Command.getComponent(arena, command, Messages.Usage);
        command.getPlayer().getBukkitPlayer().teleport(comp.getLocation());
    }
    
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
        if (command.getArgs().length == 0)
        {
            return MinigamesLibInterface.instance().getArenas(lastArg, 0, Integer.MAX_VALUE).stream()
                    .filter(a -> a.getState() == ArenaState.Maintenance)
                    .map(ArenaInterface::getInternalName)
                    .filter(a -> a.toLowerCase().startsWith(lastArg))
                    .collect(Collectors.toList());
        }
        if (command.getArgs().length == 1)
        {
            final Set<String> result = new TreeSet<>();
            final ArenaInterface arena = Mg2Command.getArenaOptional(command, Messages.Usage);
            if (arena != null)
            {
                arena.getComponents().stream().
                    map(s -> arena.getHandler(s).getName()).
                    filter(s -> s.toLowerCase().startsWith(lastArg)).
                    forEach(result::add);
            }
            return new ArrayList<>(result);
        }
        return Collections.emptyList();
    }
    
    @Override
    public LocalizedMessageInterface getShortDescription(CommandInterface command)
    {
        return Messages.ShortDescription;
    }
    
    @Override
    public LocalizedMessageInterface getDescription(CommandInterface command)
    {
        return Messages.Description;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "cmd.mg2_admin_component_tp")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 admin comp tp
         */
        @LocalizedMessage(defaultMessage = "Teleport to a component")
        @MessageComment({"Short description of /mg2 admin comp tp"})
        ShortDescription,
        
        /**
         * Long description of /mg2 admin comp tp
         */
        @LocalizedMessage(defaultMessage = "Teleport to a sign")
        @MessageComment({"Long description of /mg2 admin comp tp"})
        Description,
        
        /**
         * Usage of /mg2 admin comp tp
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 admin comp tp <arena> <name>")
        @MessageComment({"Usage of /mg2 admin comp tp"})
        Usage,
        
    }
    
}
