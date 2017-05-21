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
import de.minigameslib.mgapi.api.events.ArenaPlayerDieEvent;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.match.CommonMatchStatistics;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.PointsForKillConfig;
import de.minigameslib.mgapi.api.rules.PointsForKillRuleInterface;

/**
 * @author mepeisen
 *
 */
public class PointsForKill extends AbstractArenaRule implements PointsForKillRuleInterface
{
    
    /**
     * The number of points.
     */
    private int points;
    
    /**
     * @param type
     * @param arena
     * @throws McException
     *             thrown if config is invalid
     */
    public PointsForKill(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        super(type, arena);
        this.runInCopiedContext(() -> {
            PointsForKillConfig.Points.verifyConfig();
            this.points = PointsForKillConfig.Points.getInt();
        });
    }
    
    @Override
    public ArenaRuleSetType getType()
    {
        return this.type;
    }
    
    @Override
    public int getPoints()
    {
        return this.points;
    }
    
    @Override
    public void setPoints(int points) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            PointsForKillConfig.Points.setInt(points);
            try
            {
                PointsForKillConfig.Points.verifyConfig();
                PointsForKillConfig.Points.saveConfig();
            }
            catch (McException ex)
            {
                PointsForKillConfig.Points.rollbackConfig();
                throw ex;
            }
        });
        this.arena.reconfigureRuleSets(this.type);
    }
    
    /**
     * Die event.
     * 
     * @param evt event
     */
    @McEventHandler
    public void onPlayerDie(ArenaPlayerDieEvent evt)
    {
        if (this.arena.isMatch())
        {
            final ArenaMatchInterface.KillerTracking killer = this.arena.getCurrentMatch().getKillerTracking(evt.getPlayer().getPlayerUUID());
            if (killer != null && killer.getLastDamager() != null)
            {
                // TODO check timestamp and get some limit from config. Only dmg after xxx seconds counts.
                try
                {
                    this.arena.getCurrentMatch().addStatistic(killer.getLastDamager(), CommonMatchStatistics.Points, this.points);
                }
                catch (McException e)
                {
                    // TODO logging
                }
            }
        }
    }
    
}
