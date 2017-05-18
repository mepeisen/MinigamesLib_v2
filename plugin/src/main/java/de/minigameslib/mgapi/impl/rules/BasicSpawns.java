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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.arena.CheckSeverity;
import de.minigameslib.mgapi.api.events.ArenaStateChangedEvent;
import de.minigameslib.mgapi.api.obj.BasicComponentTypes;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.BasicArenaRuleSets;
import de.minigameslib.mgapi.api.rules.BasicMatchRuleInterface;
import de.minigameslib.mgapi.api.rules.BasicSpawnsConfig;
import de.minigameslib.mgapi.api.rules.BasicSpawnsConfig.SpawnType;
import de.minigameslib.mgapi.api.rules.BasicSpawnsRuleInterface;

/**
 * The implementation of BasicSpawns rule set
 * 
 * @see BasicArenaRuleSets#BasicSpawns
 * 
 * @author mepeisen
 */
public class BasicSpawns extends AbstractArenaRule implements BasicSpawnsRuleInterface
{
    
    /** the spawn type. */
    private SpawnType spawnType;
    
    /**
     * the current spawns
     */
    private List<ComponentIdInterface> currentSpawns = new ArrayList<>();
    
    /** random number generator. */
    private final Random random = new Random();
    
    /** logger. */
    private static final Logger LOGGER = Logger.getLogger(BasicSpawns.class.getName());
    
    /**
     * @param type
     * @param arena
     * @throws McException thrown if config is invalid
     */
    public BasicSpawns(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        super(type, arena);
        this.runInCopiedContext(() -> {
            this.spawnType = BasicSpawnsConfig.SpawnOption.getEnum(SpawnType.class);
            if (this.spawnType == null)
            {
                this.spawnType = SpawnType.RandomAtStart;
            }
        });
    }

    @Override
    public SpawnType getSpawnType()
    {
        return this.spawnType;
    }

    @Override
    public void setSpawnType(SpawnType type) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            BasicSpawnsConfig.SpawnOption.setEnum(this.spawnType);
            BasicSpawnsConfig.SpawnOption.saveConfig();
        });
        this.arena.reconfigureRuleSets(this.type);
    }
    
    /**
     * Arena state change
     * @param evt
     */
    @McEventHandler
    public void onArenaState(ArenaStateChangedEvent evt)
    {
        if (evt.getNewState() == ArenaState.PreMatch)
        {
            this.shuffleSpawns();
        }
    }

    /**
     * Shuffle spawns
     */
    private void shuffleSpawns()
    {
        switch (this.spawnType)
        {
            case Fixed:
                this.fillSpawns();
                this.setPlayerSpawns();
                break;
            default:
            case RandomAtStart:
                this.fillShuffledSpawns();
                this.setPlayerSpawns();
                break;
            case FullyRandom:
                this.fillShuffledSpawns();
                this.setPlayerSpawnsAndReshuffle();
                break;
        }
    }

    /**
     * Sets the players spawns using the current spawns list
     */
    private void setPlayerSpawns()
    {
        int i = 0;
        for (final UUID player : this.arena.getCurrentMatch().getPlayers())
        {
            try
            {
                this.arena.getCurrentMatch().selectSpawn(player, this.currentSpawns.get(i));
            }
            catch (McException e)
            {
                LOGGER.log(Level.WARNING, "Unable to select player spawn", e); //$NON-NLS-1$
            }
            i++;
            if (i >= this.currentSpawns.size())
            {
                i = 0;
            }
        }
    }

    /**
     * Sets the players spawns using the current spawns list, reshuffle if spawns are exceeded
     */
    private void setPlayerSpawnsAndReshuffle()
    {
        for (final UUID player : this.arena.getCurrentMatch().getPlayers())
        {
            try
            {
                this.arena.getCurrentMatch().selectSpawn(player, this.currentSpawns.remove(0));
            }
            catch (McException e)
            {
                LOGGER.log(Level.WARNING, "Unable to select player spawn", e); //$NON-NLS-1$
            }
            if (this.currentSpawns.size() == 0)
            {
                this.setPlayerSpawnsAndReshuffle();
            }
        }
    }

    /**
     * Fills the spawns in order
     */
    private void fillSpawns()
    {
        this.currentSpawns.clear();
        this.currentSpawns.addAll(this.arena.getComponents(BasicComponentTypes.Spawn));
    }

    /**
     * Fills the spawns in order
     */
    private void fillShuffledSpawns()
    {
        this.fillSpawns();
        Collections.shuffle(this.currentSpawns, this.random);
    }

    @Override
    public Collection<CheckFailure> check()
    {
        final Collection<CheckFailure> result = super.check();
        
        // check for at least one spawn
        if (this.getArena().getComponents(BasicComponentTypes.Spawn).isEmpty())
        {
            result.add(new CheckFailure(CheckSeverity.Error, Messages.NoSpawn, Messages.NoSpawn_Description));
        }
        
        // check for enough spawns for max players
        final BasicMatchRuleInterface basicMatchRule = (BasicMatchRuleInterface) this.getArena().getRuleSet(BasicArenaRuleSets.BasicMatch);
        if (basicMatchRule != null && this.getArena().getComponents(BasicComponentTypes.Spawn).size() < basicMatchRule.getMaxPlayers())
        {
            result.add(new CheckFailure(CheckSeverity.Warning, Messages.NotEnoughSpawns, Messages.NotEnoughSpawns_Description));
        }
        
        return result;
    }

    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "rules.BasicSpawns")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * no spawn found.
         */
        @LocalizedMessage(defaultMessage = "No spawn found!", severity = MessageSeverityType.Error)
        @MessageComment("no spawn found")
        NoSpawn,
        
        /**
         * no spawn found.
         */
        @LocalizedMessageList({
            "You did not create any spawn point.",
            "You will have to create at least one spawn point within one of your battle zones.",
            "It is recommended to create at least one spawn point for each player."})
        @MessageComment("no spawn found")
        NoSpawn_Description,
        
        /**
         * not enough spawns.
         */
        @LocalizedMessage(defaultMessage = "Nit enough spawns!", severity = MessageSeverityType.Error)
        @MessageComment("not enough spawns")
        NotEnoughSpawns,
        
        /**
         * not enough spawns.
         */
        @LocalizedMessageList({
            "You did not create enough spawn points.",
            "It is recommended to create at least one spawn point for each player.",
            "The arena will work but some players may share spawn points and that may be a bad experience on game startup."})
        @MessageComment("not enough spawns")
        NotEnoughSpawns_Description,
        
    }
    
    // TODO watch for player die event and re-select a spawn
    
    // TODO implement spawns for Team mode
    
    
    
}
