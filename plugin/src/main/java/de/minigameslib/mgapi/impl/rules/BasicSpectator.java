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

package de.minigameslib.mgapi.impl.rules;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.BasicArenaRuleSets;
import de.minigameslib.mgapi.api.rules.BasicSpectatorConfig;
import de.minigameslib.mgapi.api.rules.BasicSpectatorRuleInterface;

/**
 * The implementation of BasicSpectator rule set
 * 
 * @see BasicArenaRuleSets#BasicSpectator
 * 
 * @author mepeisen
 */
public class BasicSpectator extends AbstractArenaRule implements BasicSpectatorRuleInterface
{
    
    /**
     * Is spectating without pending match allowed?
     */
    private boolean isSpectatingWithoutMatch;
    
    /**
     * Is free spectating within pending match allowed?
     */
    private boolean isFreeSpectatingWithinMatch;
    
    /**
     * Losing and winning players join spectators?
     */
    private boolean isSpectatingAfterWinOrLose;
    
    /**
     * @param type
     * @param arena
     * @throws McException
     *             thrown if config is invalid
     */
    public BasicSpectator(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        super(type, arena);
        this.runInCopiedContext(() -> {
            this.isSpectatingWithoutMatch = BasicSpectatorConfig.SpectatingWithoutMatch.getBoolean();
            this.isFreeSpectatingWithinMatch = BasicSpectatorConfig.FreeSpectatingWithinMatch.getBoolean();
            this.isSpectatingAfterWinOrLose = BasicSpectatorConfig.SpectatingAfterWinOrLose.getBoolean();
        });
    }
    
    @Override
    public ArenaRuleSetType getType()
    {
        return this.type;
    }
    
    @Override
    public ArenaInterface getArena()
    {
        return this.arena;
    }

    @Override
    public boolean isSpectatingWithoutMatch()
    {
        return this.isSpectatingWithoutMatch;
    }

    @Override
    public boolean isFreeSpectatingWithinMatch()
    {
        return this.isFreeSpectatingWithinMatch;
    }

    @Override
    public boolean isSpectatingAfterWinOrLose()
    {
        return this.isSpectatingAfterWinOrLose;
    }

    @Override
    public void setSpectatingWithoutMatch(boolean flag) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            BasicSpectatorConfig.SpectatingWithoutMatch.setBoolean(flag);
            BasicSpectatorConfig.SpectatingWithoutMatch.saveConfig();
        });
        this.arena.reconfigureRuleSets(this.type);
    }

    @Override
    public void setFreeSpectatingWithinMatch(boolean flag) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            BasicSpectatorConfig.FreeSpectatingWithinMatch.setBoolean(flag);
            BasicSpectatorConfig.FreeSpectatingWithinMatch.saveConfig();
        });
        this.arena.reconfigureRuleSets(this.type);
    }

    @Override
    public void setSpectatingAfterWinOrLose(boolean flag) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            BasicSpectatorConfig.SpectatingAfterWinOrLose.setBoolean(flag);
            BasicSpectatorConfig.SpectatingAfterWinOrLose.saveConfig();
        });
        this.arena.reconfigureRuleSets(this.type);
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "rules.BasicSpectator")
    public enum Messages implements LocalizedMessageInterface
    {
        
        // TODO
        
    }
    
}
