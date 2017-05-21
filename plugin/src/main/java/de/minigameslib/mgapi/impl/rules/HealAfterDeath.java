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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedEvent;
import de.minigameslib.mgapi.api.events.ArenaStateChangedEvent;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.HealAfterDeathConfig;
import de.minigameslib.mgapi.api.rules.HealAfterDeathRuleInterface;

/**
 * @author mepeisen
 *
 */
public class HealAfterDeath extends AbstractArenaRule implements HealAfterDeathRuleInterface
{
    
    /**
     * The heal per ticks.
     */
    private float healPerTick;
    
    /**
     * the max heal ticks.
     */
    private int   healTicks;
    
    /**
     * the current heal task.
     */
    private HealTask task;
    
    /**
     * @param type
     * @param arena
     * @throws McException
     *             thrown if config is invalid
     */
    public HealAfterDeath(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        super(type, arena);
        this.runInCopiedContext(() -> {
            HealAfterDeathConfig.HealPerTick.verifyConfig();
            this.healPerTick = HealAfterDeathConfig.HealPerTick.getFloat();
            this.healTicks = HealAfterDeathConfig.MaxHealTicks.getInt();
        });
    }
    
    @Override
    public ArenaRuleSetType getType()
    {
        return this.type;
    }
    
    @Override
    public float getHealPerTick()
    {
        return this.healPerTick;
    }
    
    @Override
    public int getMaxHealTicks()
    {
        return this.healTicks;
    }
    
    @Override
    public void setHealPerTick(float amount) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            HealAfterDeathConfig.HealPerTick.setFloat(amount);
            try
            {
                HealAfterDeathConfig.HealPerTick.verifyConfig();
                HealAfterDeathConfig.HealPerTick.saveConfig();
            }
            catch (McException ex)
            {
                HealAfterDeathConfig.HealPerTick.rollbackConfig();
                throw ex;
            }
        });
        this.arena.reconfigureRuleSets(this.type);
    }
    
    @Override
    public void setMaxHealTicks(int ticks) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            HealAfterDeathConfig.MaxHealTicks.setInt(ticks);
            try
            {
                HealAfterDeathConfig.MaxHealTicks.verifyConfig();
                HealAfterDeathConfig.MaxHealTicks.saveConfig();
            }
            catch (McException ex)
            {
                HealAfterDeathConfig.MaxHealTicks.rollbackConfig();
                throw ex;
            }
        });
        this.arena.reconfigureRuleSets(this.type);
    }
    
    /**
     * arena state event
     * 
     * @param evt
     */
    @McEventHandler
    public void onStateChanged(ArenaStateChangedEvent evt)
    {
        if (evt.getNewState() == ArenaState.Match)
        {
            if (this.task != null)
            {
                this.task.cancel();
            }
            this.task = new HealTask(this.healPerTick, this.healTicks);
            this.task.runTaskTimer((Plugin) MinigamesLibInterface.instance(), 1, 1);
            this.arena.getActivePlayers().forEach(p -> this.task.playerJoins(p.getBukkitPlayer()));
        }
        else
        {
            if (this.task != null)
            {
                this.task.cancel();
                this.task = null;
            }
        }
    }
    
    /**
     * Arena player dies.
     * 
     * @param evt event
     */
    @McEventHandler
    public void onPlayerJoins(ArenaPlayerJoinedEvent evt)
    {
        if (this.task != null)
        {
            this.task.playerJoins(evt.getPlayer().getBukkitPlayer());
        }
    }
    
    /**
     * Tasks for propagating heal.
     */
    private static final class HealTask extends BukkitRunnable
    {
        
        /** the target player. */
        private Set<Player>              players = new HashSet<>();
        
        /** number of ticks a player recived healing. */
        private Map<UUID, Integer>       healTicksReceived = new HashMap<>();
        
        /**
         * The heal per ticks.
         */
        private float                    healPerTick;
        
        /**
         * the max heal ticks.
         */
        private int                      healTicks;
        
        /**
         * Constructor.
         * 
         * @param healPerTick
         * @param healTicks
         */
        public HealTask(float healPerTick, int healTicks)
        {
            this.healPerTick = healPerTick;
            this.healTicks = healTicks;
        }
        
        /**
         * A player joins.
         * 
         * @param player
         *            player that joined.
         */
        public void playerJoins(Player player)
        {
            this.players.add(player);
            final UUID uuid = player.getUniqueId();
            this.healTicksReceived.put(uuid, 0);
        }
        
        @SuppressWarnings("deprecation")
        @Override
        public void run()
        {
            final Iterator<Player> iter = this.players.iterator();
            while (iter.hasNext())
            {
                final Player player = iter.next();
                final UUID uuid = player.getUniqueId();
                int ticks = this.healTicksReceived.get(uuid);
                if (ticks >= this.healTicks)
                {
                    continue;
                }
                final double health = player.getHealth();
                final double maxHealth = player.getMaxHealth();
                ticks++;
                this.healTicksReceived.put(uuid, ticks);
                if (maxHealth < health)
                {
                    player.setHealth(Math.min(health + this.healPerTick, health));
                }
                if (ticks == this.healTicks)
                {
                    iter.remove();
                }
            }
        }
        
    }
    
}
