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

import java.util.Collection;
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinSpectatorsEvent;
import de.minigameslib.mgapi.api.events.ArenaStateChangedEvent;
import de.minigameslib.mgapi.api.obj.BasicComponentTypes;
import de.minigameslib.mgapi.api.obj.SpectatorSpawnComponentHandler;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.BasicArenaRuleSets;
import de.minigameslib.mgapi.api.rules.BasicSpectatorConfig;
import de.minigameslib.mgapi.api.rules.BasicSpectatorRuleInterface;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.api.team.TeamIdType;

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
    
    /**
     * Arena state changed event.
     * @param evt event
     */
    @McEventHandler
    public void onMatchState(ArenaStateChangedEvent evt)
    {
        if (evt.getNewState() == ArenaState.PreMatch)
        {
            if (!this.isFreeSpectatingWithinMatch)
            {
                // kick all spectators that are not playing the match
                evt.getArena().getCurrentMatch().getTeamMembers(CommonTeams.Spectators).forEach(uuid -> {
                    evt.getArena().kick(MinigamesLibInterface.instance().getPlayer(uuid), Messages.KickedForMatch);
                });
            }
        }
    }

    /**
     * Teleports given player to spectators
     * @param player
     */
    private void doSpectate(ArenaPlayerInterface player)
    {
        if (this.getArena().isMatch())
        {
            // match in progress
            final TeamIdType team = this.getArena().getCurrentMatch().getTeam(player.getPlayerUUID());
            if (team != null)
            {
                final ObjectServiceInterface osi = ObjectServiceInterface.instance();
                final Collection<ComponentInterface> teamspawns = this.getArena().getComponents(BasicComponentTypes.SpectatorSpawn).stream()
                        .map(osi::findComponent)
                        .filter(c -> ((SpectatorSpawnComponentHandler)c.getHandler()).getTeam() == team)
                        .collect(Collectors.toList());
                if (this.getArena().teleportRandom(player, teamspawns))
                {
                    // succeeded
                    return;
                }
            }
        }
        this.getArena().teleportRandom(player, this.getArena().getComponents(BasicComponentTypes.SpectatorSpawn));
    }
    
    /**
     * Player joins spectator event.
     * @param evt event
     */
    @McEventHandler
    public void onSpectate(ArenaPlayerJoinSpectatorsEvent evt)
    {
        final boolean inMatch = evt.getArena().isMatch();
        final boolean hasPlayedBefore = evt.getArena().getCurrentMatch().getTeam(evt.getPlayer().getPlayerUUID()) != CommonTeams.Spectators;
        if (!inMatch)
        {
            if (this.isSpectatingWithoutMatch)
            {
                this.doSpectate(evt.getArenaPlayer());
            }
            else
            {
                evt.setCancelled(Messages.OutsideMatchesDisabled);
            }
        }
        else if (!hasPlayedBefore)
        {
            if (this.isFreeSpectatingWithinMatch)
            {
                this.doSpectate(evt.getArenaPlayer());
            }
            else
            {
                evt.setCancelled(Messages.FreeDisabled);
            }
        }
        else
        {
            if (this.isSpectatingAfterWinOrLose)
            {
                this.doSpectate(evt.getArenaPlayer());
            }
            else
            {
                evt.setCancelled(Messages.Disabled);
            }
        }
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

        /**
         * Spectating outside matches disabled
         */
        @LocalizedMessage(defaultMessage = "Spectating outside matches is disabled.", severity = MessageSeverityType.Error)
        @MessageComment(value = { "Spectating outside matches disabled" })
        OutsideMatchesDisabled,
        
        /**
         * Free match spectating is disabled
         */
        @LocalizedMessage(defaultMessage = "Free match spectating is disabled.", severity = MessageSeverityType.Error)
        @MessageComment(value = { "Free match spectating is disabled" })
        FreeDisabled,
        
        /**
         * Spectating disabled
         */
        @LocalizedMessage(defaultMessage = "Spectating is disabled.", severity = MessageSeverityType.Error)
        @MessageComment(value = { "Spectating disabled" })
        Disabled,
        
        /**
         * Kicked for starting match
         */
        @LocalizedMessage(defaultMessage = "A match is starting and free spectating is disallowed.", severity = MessageSeverityType.Error)
        @MessageComment(value = { "Kicked for starting match" })
        KickedForMatch,
        
    }
    
}
