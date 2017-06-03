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

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.obj.LeaveSignHandler;

/**
 * @author mepeisen
 *
 */
public class LeaveSign extends AbstractProgrammableSign<LeaveSignData> implements LeaveSignHandler
{

    @Override
    protected LocalizedMessageInterface getDefault(ArenaState state)
    {
        switch (state)
        {
            case Booting:
                return Messages.DefaultBooting;
            default:
            case Disabled:
                return Messages.DefaultDisabled;
            case Join:
                return Messages.DefaultJoin;
            case Maintenance:
                return Messages.DefaultMaintenance;
            case Match:
                return Messages.DefaultMatch;
            case PostMatch:
                return Messages.DefaultPostMatch;
            case PreMatch:
                return Messages.DefaultPreMatch;
            case Restarting:
                return Messages.DefaultRestarting;
            case Starting:
                return Messages.DefaultStarting;
        }
    }

    @Override
    protected Class<LeaveSignData> getDataClass()
    {
        return LeaveSignData.class;
    }

    @Override
    protected LeaveSignData createData()
    {
        return new LeaveSignData();
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "signs.leave")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /** Default text for booting arena. */
        @LocalizedMessageList(value = {
                "LEAVE",
                "{mg2_arena_name}"
            }, severity = MessageSeverityType.Warning)
        @MessageComment({"Default text for booting arena."})
        DefaultBooting,
        
        /** Default text for disabled arena. */
        @LocalizedMessageList(value = {
                "LEAVE",
                "{mg2_arena_name}"
            }, severity = MessageSeverityType.Error)
        @MessageComment({"Default text for disabled arena."})
        DefaultDisabled,
        
        /** Default text for starting arena. */
        @LocalizedMessageList(value = {
                "LEAVE",
                "{mg2_arena_name}"
            }, severity = MessageSeverityType.Warning)
        @MessageComment({"Default text for starting arena."})
        DefaultStarting,
        
        /** Default text for join arena. */
        @LocalizedMessageList(value = {
                "LEAVE",
                "{mg2_arena_name}"
            }, severity = MessageSeverityType.Success)
        @MessageComment({"Default text for join arena."})
        DefaultJoin,
        
        /** Default text for pre-match arena. */
        @LocalizedMessageList(value = {
                "LEAVE",
                "{mg2_arena_name}"
            }, severity = MessageSeverityType.Winner)
        @MessageComment({"Default text for pre-match arena."})
        DefaultPreMatch,
        
        /** Default text for match arena. */
        @LocalizedMessageList(value = {
                "LEAVE",
                "{mg2_arena_name}"
            }, severity = MessageSeverityType.Winner)
        @MessageComment({"Default text for match arena."})
        DefaultMatch,
        
        /** Default text for post-match arena. */
        @LocalizedMessageList(value = {
                "LEAVE",
                "{mg2_arena_name}"
            }, severity = MessageSeverityType.Winner)
        @MessageComment({"Default text for post-match arena."})
        DefaultPostMatch,
        
        /** Default text for restarting arena. */
        @LocalizedMessageList(value = {
                "LEAVE",
                "{mg2_arena_name}"
            }, severity = MessageSeverityType.Warning)
        @MessageComment({"Default text for restarting arena."})
        DefaultRestarting,
        
        /** Default text for maintenance arena. */
        @LocalizedMessageList(value = {
                "LEAVE",
                "{mg2_arena_name}"
            }, severity = MessageSeverityType.Error)
        @MessageComment({"Default text for maintenance arena."})
        DefaultMaintenance,
    }
    
}
