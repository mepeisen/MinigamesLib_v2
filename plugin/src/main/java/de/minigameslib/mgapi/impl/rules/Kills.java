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
import de.minigameslib.mgapi.api.events.ArenaPlayerStatisticEvent;
import de.minigameslib.mgapi.api.match.CommonMatchStatistics;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.KillsConfig;
import de.minigameslib.mgapi.api.rules.KillsRuleInterface;

/**
 * @author mepeisen
 *
 */
public class Kills extends AbstractArenaRule implements KillsRuleInterface
{
    
    /**
     * The number of kills to win.
     */
    private int kills;
    
    /**
     * @param type
     * @param arena
     * @throws McException
     *             thrown if config is invalid
     */
    public Kills(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        super(type, arena);
        this.runInCopiedContext(() -> {
            KillsConfig.KillsForWin.verifyConfig();
            this.kills = KillsConfig.KillsForWin.getInt();
        });
    }
    
    @Override
    public ArenaRuleSetType getType()
    {
        return this.type;
    }
    
    @Override
    public int getKillsForWin()
    {
        return this.kills;
    }
    
    @Override
    public void setKillsForWin(int kills) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            KillsConfig.KillsForWin.setInt(kills);
            try
            {
                KillsConfig.KillsForWin.verifyConfig();
                KillsConfig.KillsForWin.saveConfig();
            }
            catch (McException ex)
            {
                KillsConfig.KillsForWin.rollbackConfig();
                throw ex;
            }
        });
        this.arena.reconfigureRuleSets(this.type);
    }
    
    /**
     * Player statistics.
     * 
     * @param evt event
     */
    @McEventHandler
    public void onPlayerStats(ArenaPlayerStatisticEvent evt)
    {
        if (evt.getArena().getState() == ArenaState.Match && evt.getId() == CommonMatchStatistics.Kills)
        {
            if (evt.getNewValue() >= this.kills)
            {
                try
                {
                    evt.getArenaPlayer().win();
                }
                catch (McException e)
                {
                    // TODO logging
                }
            }
        }
    }
    
}
