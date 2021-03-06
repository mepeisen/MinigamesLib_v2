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

import java.util.logging.Level;
import java.util.logging.Logger;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.events.ArenaPlayerDieEvent;
import de.minigameslib.mgapi.api.match.CommonMatchStatistics;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.PointsForDeathConfig;
import de.minigameslib.mgapi.api.rules.PointsForDeathRuleInterface;

/**
 * @author mepeisen
 *
 */
public class PointsForDeath extends AbstractArenaRule implements PointsForDeathRuleInterface
{
    
    /**
     * The number of points.
     */
    private int points;
    
    /** logger. */
    private static final Logger LOGGER    = Logger.getLogger(PointsForDamage.class.getName());
    
    /**
     * @param type
     * @param arena
     * @throws McException
     *             thrown if config is invalid
     */
    public PointsForDeath(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        super(type, arena);
        this.runInCopiedContext(() -> {
            PointsForDeathConfig.Points.verifyConfig();
            this.points = PointsForDeathConfig.Points.getInt();
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
            PointsForDeathConfig.Points.setInt(points);
            try
            {
                PointsForDeathConfig.Points.verifyConfig();
                PointsForDeathConfig.Points.saveConfig();
            }
            catch (McException ex)
            {
                PointsForDeathConfig.Points.rollbackConfig();
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
            try
            {
                this.arena.getCurrentMatch().addStatistic(evt.getArenaPlayer().getPlayerUUID(), CommonMatchStatistics.Points, this.points);
            }
            catch (McException e)
            {
                LOGGER.log(Level.WARNING, "Problems setting points", e); //$NON-NLS-1$
            }
        }
    }
    
}
