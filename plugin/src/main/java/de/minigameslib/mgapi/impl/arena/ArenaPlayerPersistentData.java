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

package de.minigameslib.mgapi.impl.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.ItemStackDataFragment;
import de.minigameslib.mclib.shared.api.com.LocationData;
import de.minigameslib.mclib.shared.api.com.LocationDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Persistent player data.
 * 
 * @author mepeisen
 */
public class ArenaPlayerPersistentData extends AnnotatedDataFragment
{
    
    /**
     * The arena this player has joined
     */
    @PersistentField
    protected String                      arenaName;
    
    /**
     * Spectator flag.
     */
    @PersistentField
    protected boolean                     isSpectator;
    
    // player data before joining arena
    
    /**
     * the players last gamemode before joining.
     */
    @PersistentField
    protected GameMode                    gameMode;
    
    /**
     * the last compass target.
     */
    @PersistentField
    protected LocationDataFragment        compassTarget;
    
    /**
     * players exchaustion.
     */
    @PersistentField
    protected float                       exhaustion;
    
    /**
     * players experience.
     */
    @PersistentField
    protected float                       exp;
    
    /**
     * players fly speed.
     */
    @PersistentField
    protected float                       flySpeed;
    
    /**
     * players walk speed.
     */
    @PersistentField
    protected float                       walkSpeed;
    
    /**
     * players food level.
     */
    @PersistentField
    protected int                         foodLevel;
    
    /**
     * players health scale.
     */
    @PersistentField
    protected double                      healthScale;
    
    /**
     * players level.
     */
    @PersistentField
    protected int                         level;
    
    /**
     * players saturation.
     */
    @PersistentField
    protected float                       saturation;
    
    /**
     * players flying flag.
     */
    @PersistentField
    protected boolean                     isFlying;
    
    /**
     * players inventory.
     */
    @PersistentField
    protected List<ItemStackDataFragment> inventory = new ArrayList<>();

    /**
     * players health.
     */
    @PersistentField
    protected double health;

    /**
     * players max health.
     */
    @PersistentField
    protected double maxHealth;

    /**
     * allow flight flag.
     */
    @PersistentField
    protected boolean allowFlight;
    
    /**
     * @return the arenaName
     */
    public String getArenaName()
    {
        return this.arenaName;
    }
    
    /**
     * @param arenaName
     *            the arenaName to set
     */
    public void setArenaName(String arenaName)
    {
        this.arenaName = arenaName;
    }
    
    /**
     * @return the isSpectator
     */
    public boolean isSpectator()
    {
        return this.isSpectator;
    }
    
    /**
     * @param isSpectator
     *            the isSpectator to set
     */
    public void setSpectator(boolean isSpectator)
    {
        this.isSpectator = isSpectator;
    }
    
    /**
     * Persist current player data and inventory.
     * 
     * @param player
     *            player.
     */
    @SuppressWarnings("deprecation")
    public void setPlayerData(Player player)
    {
        this.gameMode = player.getGameMode();
        final Location loc = player.getCompassTarget();
        this.compassTarget = new LocationData(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), loc.getWorld().getName());
        this.exhaustion = player.getExhaustion();
        this.exp = player.getExp();
        this.flySpeed = player.getFlySpeed();
        this.walkSpeed = player.getWalkSpeed();
        this.foodLevel = player.getFoodLevel();
        this.health = player.getHealth();
        this.healthScale = player.getHealthScale();
        this.allowFlight = player.getAllowFlight();
        this.maxHealth = player.getMaxHealth();
        this.level = player.getLevel();
        this.saturation = player.getSaturation();
        this.isFlying = player.isFlying();
        this.inventory.clear();
        final ItemServiceInterface isi = ItemServiceInterface.instance();
        final ItemStack air = new ItemStack(Material.AIR);
        for (final ItemStack stack : player.getInventory().getStorageContents())
        {
            this.inventory.add(isi.toConfigData(stack == null ? air : stack));
        }
    }

    /**
     * @return the gameMode
     */
    public GameMode getGameMode()
    {
        return this.gameMode;
    }

    /**
     * @param gameMode the gameMode to set
     */
    public void setGameMode(GameMode gameMode)
    {
        this.gameMode = gameMode;
    }

    /**
     * @return the compassTarget
     */
    public LocationDataFragment getCompassTarget()
    {
        return this.compassTarget;
    }

    /**
     * @param compassTarget the compassTarget to set
     */
    public void setCompassTarget(LocationDataFragment compassTarget)
    {
        this.compassTarget = compassTarget;
    }

    /**
     * @return the exhaustion
     */
    public float getExhaustion()
    {
        return this.exhaustion;
    }

    /**
     * @param exhaustion the exhaustion to set
     */
    public void setExhaustion(float exhaustion)
    {
        this.exhaustion = exhaustion;
    }

    /**
     * @return the exp
     */
    public float getExp()
    {
        return this.exp;
    }

    /**
     * @param exp the exp to set
     */
    public void setExp(float exp)
    {
        this.exp = exp;
    }

    /**
     * @return the flySpeed
     */
    public float getFlySpeed()
    {
        return this.flySpeed;
    }

    /**
     * @param flySpeed the flySpeed to set
     */
    public void setFlySpeed(float flySpeed)
    {
        this.flySpeed = flySpeed;
    }

    /**
     * @return the walkSpeed
     */
    public float getWalkSpeed()
    {
        return this.walkSpeed;
    }

    /**
     * @param walkSpeed the walkSpeed to set
     */
    public void setWalkSpeed(float walkSpeed)
    {
        this.walkSpeed = walkSpeed;
    }

    /**
     * @return the foodLevel
     */
    public int getFoodLevel()
    {
        return this.foodLevel;
    }

    /**
     * @param foodLevel the foodLevel to set
     */
    public void setFoodLevel(int foodLevel)
    {
        this.foodLevel = foodLevel;
    }

    /**
     * @return the healthScale
     */
    public double getHealthScale()
    {
        return this.healthScale;
    }

    /**
     * @param healthScale the healthScale to set
     */
    public void setHealthScale(double healthScale)
    {
        this.healthScale = healthScale;
    }

    /**
     * @return the level
     */
    public int getLevel()
    {
        return this.level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level)
    {
        this.level = level;
    }

    /**
     * @return the saturation
     */
    public float getSaturation()
    {
        return this.saturation;
    }

    /**
     * @param saturation the saturation to set
     */
    public void setSaturation(float saturation)
    {
        this.saturation = saturation;
    }

    /**
     * @return the isFlying
     */
    public boolean isFlying()
    {
        return this.isFlying;
    }

    /**
     * @param isFlying the isFlying to set
     */
    public void setFlying(boolean isFlying)
    {
        this.isFlying = isFlying;
    }

    /**
     * @return the inventory
     */
    public List<ItemStackDataFragment> getInventory()
    {
        return this.inventory;
    }

    /**
     * @param inventory the inventory to set
     */
    public void setInventory(List<ItemStackDataFragment> inventory)
    {
        this.inventory = inventory;
    }

    /**
     * @return the health
     */
    public double getHealth()
    {
        return this.health;
    }

    /**
     * @param health the health to set
     */
    public void setHealth(double health)
    {
        this.health = health;
    }

    /**
     * @return the maxHealth
     */
    public double getMaxHealth()
    {
        return this.maxHealth;
    }

    /**
     * @param maxHealth the maxHealth to set
     */
    public void setMaxHealth(double maxHealth)
    {
        this.maxHealth = maxHealth;
    }

    /**
     * @return the allowFlight
     */
    public boolean isAllowFlight()
    {
        return this.allowFlight;
    }

    /**
     * @param allowFlight the allowFlight to set
     */
    public void setAllowFlight(boolean allowFlight)
    {
        this.allowFlight = allowFlight;
    }
    
}
