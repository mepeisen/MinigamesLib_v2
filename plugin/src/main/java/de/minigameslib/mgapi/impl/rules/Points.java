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
import de.minigameslib.mgapi.api.events.ArenaPlayerStatisticEvent;
import de.minigameslib.mgapi.api.match.CommonMatchStatistics;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.PointsConfig;
import de.minigameslib.mgapi.api.rules.PointsRuleInterface;

/**
 * @author mepeisen
 *
 */
public class Points extends AbstractArenaRule implements PointsRuleInterface
{
    
    /**
     * The number of points to win.
     */
    private int points;
    
    /**
     * @param type
     * @param arena
     * @throws McException
     *             thrown if config is invalid
     */
    public Points(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        super(type, arena);
        this.runInCopiedContext(() -> {
            PointsConfig.PointsForWin.verifyConfig();
            this.points = PointsConfig.PointsForWin.getInt();
        });
    }
    
    @Override
    public ArenaRuleSetType getType()
    {
        return this.type;
    }
    
    @Override
    public int getPointsForWin()
    {
        return this.points;
    }
    
    @Override
    public void setPointsForWin(int points) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            PointsConfig.PointsForWin.setInt(points);
            try
            {
                PointsConfig.PointsForWin.verifyConfig();
                PointsConfig.PointsForWin.saveConfig();
            }
            catch (McException ex)
            {
                PointsConfig.PointsForWin.rollbackConfig();
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
        if (evt.getId() == CommonMatchStatistics.Points)
        {
            if (evt.getNewValue() >= this.points)
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
