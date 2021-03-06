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

import java.util.Collections;
import java.util.List;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.cmd.SubCommandHandlerInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mgapi.impl.MglibPerms;

/**
 * @author mepeisen
 *
 */
public class AdminSGuiCommand implements SubCommandHandlerInterface
{
    
    @Override
    public boolean visible(CommandInterface command)
    {
        return command.isOnline() && command.checkOpPermission(MglibPerms.CommandAdminSGui);
    }
    
    @Override
    public void handle(CommandInterface command) throws McException
    {
        command.permOpThrowException(MglibPerms.CommandAdminSGui, command.getCommandPath());
        command.checkOnline();
        
        // TODO Support gui
    }
    
    @Override
    public List<String> onTabComplete(CommandInterface command, String lastArg) throws McException
    {
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
    @LocalizedMessages(value = "cmd.mg2_admin_sgui")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Short description of /mg2 admin sgui
         */
        @LocalizedMessage(defaultMessage = "Display advanced admin gui")
        @MessageComment({"Short description of /mg2 admin sgui"})
        ShortDescription,
        
        /**
         * Long description of /mg2 admin sgui
         */
        @LocalizedMessage(defaultMessage = "Display advanced admin gui")
        @MessageComment({"Long description of /mg2 admin sgui"})
        Description,
        
        /**
         * Usage of /mg2 admin sgui
         */
        @LocalizedMessage(defaultMessage = "Usage: " + LocalizedMessage.CODE_COLOR + "/mg2 admin sgui")
        @MessageComment({"Usage of /mg2 admin sgui"})
        Usage
        
    }
    
}
