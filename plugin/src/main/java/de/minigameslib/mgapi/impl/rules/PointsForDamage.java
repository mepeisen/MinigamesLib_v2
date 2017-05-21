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

import org.bukkit.entity.Player;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEntityDamageByEntityEvent;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.match.CommonMatchStatistics;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.rules.AbstractZoneRule;
import de.minigameslib.mgapi.api.rules.PointsForDamageConfig;
import de.minigameslib.mgapi.api.rules.PointsForDamageRuleInterface;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetType;

/**
 * @author mepeisen
 *
 */
public class PointsForDamage extends AbstractZoneRule implements PointsForDamageRuleInterface
{
    
    /**
     * The number of points.
     */
    private float factor;
    
    /**
     * @param type
     * @param zone
     * @throws McException
     *             thrown if config is invalid
     */
    public PointsForDamage(ZoneRuleSetType type, ArenaZoneHandler zone) throws McException
    {
        super(type, zone);
        this.runInCopiedContext(() -> {
            PointsForDamageConfig.Factor.verifyConfig();
            this.factor = PointsForDamageConfig.Factor.getFloat();
        });
    }
    
    @Override
    public ZoneRuleSetType getType()
    {
        return this.type;
    }
    
    @Override
    public float getPointFactor()
    {
        return this.factor;
    }
    
    @Override
    public void setPointFactor(float factor) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            PointsForDamageConfig.Factor.setFloat(factor);
            try
            {
                PointsForDamageConfig.Factor.verifyConfig();
                PointsForDamageConfig.Factor.saveConfig();
            }
            catch (McException ex)
            {
                PointsForDamageConfig.Factor.rollbackConfig();
                throw ex;
            }
        });
        this.zone.reconfigureRuleSets(this.type);
    }
    
    /**
     * Die event.
     * 
     * @param evt event
     */
    @McEventHandler
    public void onPlayerDmg(McEntityDamageByEntityEvent evt)
    {
        if (this.arena.isMatch())
        {
            if (evt.getBukkitEvent().getDamager() instanceof Player && evt.getBukkitEvent().getEntity() instanceof Player)
            {
                final Player damager = (Player) evt.getBukkitEvent().getDamager();
                final Player target = (Player) evt.getBukkitEvent().getEntity();
                final ArenaPlayerInterface arenaDamager = MinigamesLibInterface.instance().getPlayer(damager.getUniqueId());
                final ArenaPlayerInterface arenaTarget = MinigamesLibInterface.instance().getPlayer(target.getUniqueId());
                if (arenaDamager.getArena() == this.arena && arenaTarget.getArena() == this.arena)
                {
                    final int points = (int) Math.round(this.factor * evt.getBukkitEvent().getDamage());
                    try
                    {
                        this.arena.getCurrentMatch().addStatistic(damager.getUniqueId(), CommonMatchStatistics.Points, points);
                    }
                    catch (McException e)
                    {
                        // TODO logging
                    }
                }
            }
        }
    }
    
}
