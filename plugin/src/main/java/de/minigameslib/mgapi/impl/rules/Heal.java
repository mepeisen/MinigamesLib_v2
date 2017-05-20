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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.mcevent.PlayerEnteredZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerLeftZoneEvent;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.events.ArenaStateChangedEvent;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.rules.AbstractZoneRule;
import de.minigameslib.mgapi.api.rules.HealConfig;
import de.minigameslib.mgapi.api.rules.HealRuleInterface;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetType;

/**
 * @author mepeisen
 *
 */
public class Heal extends AbstractZoneRule implements HealRuleInterface
{
    
    /**
     * the cooldown in seconds
     */
    private int   cooldown;
    
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
     * @param zone
     * @throws McException
     *             thrown if config is invalid
     */
    public Heal(ZoneRuleSetType type, ArenaZoneHandler zone) throws McException
    {
        super(type, zone);
        this.runInCopiedContext(() -> {
            this.cooldown = HealConfig.Cooldown.getInt();
            this.healPerTick = HealConfig.HealPerTick.getFloat();
            this.healTicks = HealConfig.MaxHealTicks.getInt();
        });
    }
    
    @Override
    public ZoneRuleSetType getType()
    {
        return this.type;
    }
    
    @Override
    public int getHealCooldown()
    {
        return this.cooldown;
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
    public void setHealCooldown(int seconds) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            HealConfig.Cooldown.setInt(seconds);
            HealConfig.Cooldown.saveConfig();
        });
        this.zone.reconfigureRuleSets(this.type);
    }
    
    @Override
    public void setHealPerTick(float amount) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            HealConfig.HealPerTick.setFloat(amount);
            HealConfig.HealPerTick.saveConfig();
        });
        this.zone.reconfigureRuleSets(this.type);
    }
    
    @Override
    public void setMaxHealTicks(int ticks) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            HealConfig.MaxHealTicks.setInt(ticks);
            HealConfig.MaxHealTicks.saveConfig();
        });
        this.zone.reconfigureRuleSets(this.type);
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
            this.task = new HealTask(this.cooldown, this.healPerTick, this.healTicks);
            this.task.runTaskTimer((Plugin) MinigamesLibInterface.instance(), 1, 1);
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
     * Arena player enters zone.
     * 
     * @param evt event
     */
    @McEventHandler
    public void onPlayerEntersZone(PlayerEnteredZoneEvent evt)
    {
        if (this.task != null)
        {
            this.task.playerEntersZone(evt.getPlayer().getBukkitPlayer());
        }
    }
    
    /**
     * Arena player leaves zone.
     * 
     * @param evt event
     */
    @McEventHandler
    public void onPlayerLeavesZone(PlayerLeftZoneEvent evt)
    {
        if (this.task != null)
        {
            this.task.playerLeavesZone(evt.getPlayer().getBukkitPlayer());
        }
    }
    
    /**
     * Tasks for propagating heal.
     */
    private static final class HealTask extends BukkitRunnable
    {
        
        /** the target player. */
        private Set<Player>              playerInsideZone = new HashSet<>();
        
        /** the cooldowns. */
        private Map<UUID, LocalDateTime> cooldowns        = new HashMap<>();
        
        /** number of ticks a player recived healing. */
        private Map<UUID, Integer>       healTicksReceived = new HashMap<>();
        
        /**
         * the cooldown in seconds
         */
        private int                      cooldown;
        
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
         * @param cooldown
         * @param healPerTick
         * @param healTicks
         */
        public HealTask(int cooldown, float healPerTick, int healTicks)
        {
            this.cooldown = cooldown;
            this.healPerTick = healPerTick;
            this.healTicks = healTicks;
        }
        
        /**
         * A player enters the healing zone.
         * 
         * @param player
         *            player that enters the zone.
         */
        public void playerEntersZone(Player player)
        {
            this.playerInsideZone.add(player);
            final UUID uuid = player.getUniqueId();
            this.healTicksReceived.put(uuid, 0);
        }
        
        /**
         * A player leaves the healing zone.
         * 
         * @param player
         *            player that left the zone.
         */
        public void playerLeavesZone(Player player)
        {
            this.playerInsideZone.remove(player);
            final UUID uuid = player.getUniqueId();
            this.cooldowns.put(uuid, LocalDateTime.now().plus(this.cooldown, ChronoUnit.SECONDS));
        }
        
        @SuppressWarnings("deprecation")
        @Override
        public void run()
        {
            for (final Player player : this.playerInsideZone)
            {
                final UUID uuid = player.getUniqueId();
                int ticks = this.healTicksReceived.get(uuid);
                if (ticks == 0)
                {
                    // check for pending cooldown
                    final LocalDateTime cd = this.cooldowns.get(uuid);
                    if (cd != null && cd.isAfter(LocalDateTime.now()))
                    {
                        continue;
                    }
                }
                if (ticks >= this.healTicks)
                {
                    continue;
                }
                final double health = player.getHealth();
                final double maxHealth = player.getMaxHealth();
                if (maxHealth < health)
                {
                    ticks++;
                    this.healTicksReceived.put(uuid, ticks);
                    player.setHealth(Math.min(health + this.healPerTick, health));
                    if (ticks == this.healTicks)
                    {
                        this.cooldowns.put(player.getUniqueId(), LocalDateTime.now().plus(this.cooldown, ChronoUnit.SECONDS));
                        this.healTicksReceived.put(uuid, 0);
                    }
                }
                else
                {
                    this.cooldowns.put(player.getUniqueId(), LocalDateTime.now().plus(this.cooldown, ChronoUnit.SECONDS));
                    this.healTicksReceived.put(uuid, 0);
                }
            }
        }
        
    }
    
}
