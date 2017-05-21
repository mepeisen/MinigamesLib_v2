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
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerStatisticEvent;
import de.minigameslib.mgapi.api.events.ArenaStateChangedEvent;
import de.minigameslib.mgapi.api.match.CommonMatchStatistics;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.LivesConfig;
import de.minigameslib.mgapi.api.rules.LivesRuleInterface;

/**
 * @author mepeisen
 *
 */
public class Lives extends AbstractArenaRule implements LivesRuleInterface
{
    
    /**
     * The number of lives beore lose.
     */
    private int lives;
    
    /**
     * @param type
     * @param arena
     * @throws McException
     *             thrown if config is invalid
     */
    public Lives(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        super(type, arena);
        this.runInCopiedContext(() -> {
            LivesConfig.Lives.verifyConfig();
            this.lives = LivesConfig.Lives.getInt();
        });
    }
    
    @Override
    public ArenaRuleSetType getType()
    {
        return this.type;
    }
    
    @Override
    public int getLives()
    {
        return this.lives;
    }
    
    @Override
    public void setLives(int lives) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            LivesConfig.Lives.setInt(lives);
            try
            {
                LivesConfig.Lives.verifyConfig();
                LivesConfig.Lives.saveConfig();
            }
            catch (McException ex)
            {
                LivesConfig.Lives.rollbackConfig();
                throw ex;
            }
        });
        this.arena.reconfigureRuleSets(this.type);
    }
    
    /**
     * Event handler
     * @param evt
     */
    @McEventHandler
    public void onArenaState(ArenaStateChangedEvent evt)
    {
        if (evt.getNewState() == ArenaState.PreMatch)
        {
            this.getArena().getActivePlayers().forEach(p -> {
                try
                {
                    this.arena.getCurrentMatch().setStatistic(p.getPlayerUUID(), CommonMatchStatistics.Lives, this.lives);
                }
                catch (McException e)
                {
                    // TODO logging
                }
            });
        }
    }
    
    /**
     * Event handler
     * @param evt
     */
    @McEventHandler
    public void onPlayerJoin(ArenaPlayerJoinedEvent evt)
    {
        if (evt.getArena().isMatch() && !evt.getArenaPlayer().isSpectating())
        {
            try
            {
                this.arena.getCurrentMatch().setStatistic(evt.getArenaPlayer().getPlayerUUID(), CommonMatchStatistics.Lives, this.lives);
            }
            catch (McException e)
            {
                // TODO logging
            }
        }
    }
    
    /**
     * Player statistics.
     * 
     * @param evt event
     */
    @McEventHandler
    public void onPlayerStats(ArenaPlayerStatisticEvent evt)
    {
        if (evt.getId() == CommonMatchStatistics.Lives)
        {
            if (evt.getNewValue() <= 0)
            {
                try
                {
                    evt.getArenaPlayer().lose();
                }
                catch (McException e)
                {
                    // TODO logging
                }
            }
        }
    }
    
}
