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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.config.ConfigItemStackData;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.FalseStub;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.api.util.function.TrueStub;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.events.ArenaPlayerDieEvent;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.impl.MglibConfig;

/**
 * @author mepeisen
 */
public class ArenaPlayerImpl extends AnnotatedDataFragment implements ArenaPlayerInterface
{
    
    /**
     * Player back reference.
     */
    @PersistentField
    protected McPlayerInterface       player;
    
    /**
     * Persistent data storage.
     */
    private ArenaPlayerPersistentData persistent;
    
    /**
     * Constructor
     */
    public ArenaPlayerImpl()
    {
        // constructor
    }
    
    /**
     * @param player
     * @param persistent
     */
    public ArenaPlayerImpl(McPlayerInterface player, ArenaPlayerPersistentData persistent)
    {
        this.player = player;
        this.persistent = persistent;
    }
    
    /**
     * Returns persistent data storage.
     * 
     * @return persistent player data
     */
    private ArenaPlayerPersistentData getData()
    {
        if (this.persistent == null)
        {
            this.persistent = this.player.getPersistentStorage().get(ArenaPlayerPersistentData.class);
            if (this.persistent == null)
            {
                this.persistent = new ArenaPlayerPersistentData();
                this.player.getPersistentStorage().set(ArenaPlayerPersistentData.class, this.persistent);
            }
        }
        return this.persistent;
    }
    
    /**
     * Stores player data into persistence; will be invoked before joining an arena.
     */
    public void storePlayerData()
    {
        this.getData().setPlayerData(this.getBukkitPlayer());
        this.saveData();
    }
    
    /**
     * Reset player data from persistence; will be invoked after leaving an arena.
     * @param regularLeave {@code true} if player leaves arena regularly
     */
    @SuppressWarnings("deprecation")
    public void resetPlayerData(boolean regularLeave)
    {
        final Player bukkit = this.player.getBukkitPlayer();
        final ArenaPlayerPersistentData data = this.getData();
        if (regularLeave)
        {
            if (MglibConfig.RestoreCompassTargetOnLeave.getBoolean())
            {
                bukkit.setCompassTarget(new Location(
                        Bukkit.getWorld(data.getCompassTarget().getWorld()),
                        data.compassTarget.getX(),
                        data.compassTarget.getY(),
                        data.compassTarget.getZ(),
                        data.compassTarget.getYaw(),
                        data.compassTarget.getPitch()));
            }
            if (MglibConfig.RestoreExperienceOnLeave.getBoolean())
            {
                bukkit.setExp(data.getExp());
                bukkit.setLevel(data.getLevel());
            }
            if (MglibConfig.RestoreFlyOnLeave.getBoolean())
            {
                bukkit.setAllowFlight(data.isAllowFlight());
                bukkit.setFlying(data.isFlying());
            }
            if (MglibConfig.RestoreFoodOnLeave.getBoolean())
            {
                bukkit.setSaturation(data.getSaturation());
                bukkit.setExhaustion(data.getExhaustion());
                bukkit.setFoodLevel(data.getFoodLevel());
            }
            if (MglibConfig.RestoreGameModeOnLeave.getBoolean())
            {
                bukkit.setGameMode(data.getGameMode());
            }
            if (MglibConfig.RestoreHealthOnLeave.getBoolean())
            {
                bukkit.setMaxHealth(data.getMaxHealth());
                bukkit.setHealthScale(data.getHealthScale());
                bukkit.setHealth(data.getHealth());
            }
            if (MglibConfig.RestoreInventoryOnLeave.getBoolean())
            {
                final ItemStack[] items = data.getInventory().stream().map(i -> ((ConfigItemStackData)i).toBukkit()).toArray(ItemStack[]::new);
                bukkit.getInventory().setStorageContents(items);
            }
            if (MglibConfig.RestoreSpeedOnLeave.getBoolean())
            {
                bukkit.setWalkSpeed(data.getWalkSpeed());
                bukkit.setFlySpeed(data.getFlySpeed());
            }
        }
        else
        {
            if (MglibConfig.RestoreCompassTargetOnCrash.getBoolean())
            {
                bukkit.setCompassTarget(new Location(
                        Bukkit.getWorld(data.getCompassTarget().getWorld()),
                        data.compassTarget.getX(),
                        data.compassTarget.getY(),
                        data.compassTarget.getZ(),
                        data.compassTarget.getYaw(),
                        data.compassTarget.getPitch()));
            }
            if (MglibConfig.RestoreExperienceOnCrash.getBoolean())
            {
                bukkit.setExp(data.getExp());
                bukkit.setLevel(data.getLevel());
            }
            if (MglibConfig.RestoreFlyOnCrash.getBoolean())
            {
                bukkit.setAllowFlight(data.isAllowFlight());
                bukkit.setFlying(data.isFlying());
            }
            if (MglibConfig.RestoreFoodOnCrash.getBoolean())
            {
                bukkit.setSaturation(data.getSaturation());
                bukkit.setExhaustion(data.getExhaustion());
                bukkit.setFoodLevel(data.getFoodLevel());
            }
            if (MglibConfig.RestoreGameModeOnCrash.getBoolean())
            {
                bukkit.setGameMode(data.getGameMode());
            }
            if (MglibConfig.RestoreHealthOnCrash.getBoolean())
            {
                bukkit.setMaxHealth(data.getMaxHealth());
                bukkit.setHealthScale(data.getHealthScale());
                bukkit.setHealth(data.getHealth());
            }
            if (MglibConfig.RestoreInventoryOnCrash.getBoolean())
            {
                final ItemStack[] items = data.getInventory().stream().map(i -> ((ConfigItemStackData)i).toBukkit()).toArray(ItemStack[]::new);
                bukkit.getInventory().setStorageContents(items);
            }
            if (MglibConfig.RestoreSpeedOnCrash.getBoolean())
            {
                bukkit.setWalkSpeed(data.getWalkSpeed());
                bukkit.setFlySpeed(data.getFlySpeed());
            }
        }
    }
    
    @Override
    public McPlayerInterface getMcPlayer()
    {
        return this.player;
    }
    
    @Override
    public ArenaInterface getArena()
    {
        final ArenaPlayerPersistentData data = this.getData();
        if (data.getArenaName() == null)
        {
            return null;
        }
        return MinigamesLibInterface.instance().getArena(data.getArenaName());
    }
    
    /**
     * Saves persistent data
     */
    private void saveData()
    {
        this.player.getPersistentStorage().set(ArenaPlayerPersistentData.class, this.persistent);
    }
    
    /**
     * Switches arena name or mode
     * 
     * @param arena
     * @param isSpectating
     */
    void switchArenaOrMode(String arena, boolean isSpectating)
    {
        final ArenaPlayerPersistentData data = this.getData();
        data.setArenaName(arena);
        data.setSpectator(isSpectating);
        this.saveData();
    }
    
    @Override
    public McOutgoingStubbing<ArenaPlayerInterface> when(McPredicate<ArenaPlayerInterface> test) throws McException
    {
        if (test.test(this))
        {
            return new TrueStub<>(this);
        }
        return new FalseStub<>(this);
    }
    
    @Override
    public boolean isSpectating()
    {
        return this.getData().isSpectator();
    }
    
    @Override
    public boolean isPlaying()
    {
        return this.getData().getArenaName() != null;
    }
    
    @Override
    public void die() throws McException
    {
        this.die(null);
    }
    
    @Override
    public void die(ArenaPlayerInterface killer) throws McException
    {
        if (this.inArena())
        {
            final ArenaMatchInterface match = this.getArena().getCurrentMatch();
            if (match != null)
            {
                if (killer != null)
                {
                    match.trackDamageForKill(this.getPlayerUUID(), killer.getPlayerUUID());
                }
                
                final ArenaPlayerDieEvent event = new ArenaPlayerDieEvent(this.getArena(), this);
                Bukkit.getPluginManager().callEvent(event);
                
                if (this.isPlaying() || this.isSpectating())
                {
                    this.getArena().teleport(this, this.getArena().getCurrentMatch().getSpawn(this.getPlayerUUID()));
                }
                return;
            }
        }
        
        throw new McException(ArenaImpl.Messages.InvalidModificationBeforeStart, "?"); //$NON-NLS-1$
    }
    
    @Override
    public void lose() throws McException
    {
        if (this.inArena())
        {
            final ArenaMatchInterface match = this.getArena().getCurrentMatch();
            if (match != null)
            {
                match.setLoser(this.getPlayerUUID());
                return;
            }
        }
        
        throw new McException(ArenaImpl.Messages.InvalidModificationBeforeStart, "?"); //$NON-NLS-1$
    }
    
    @Override
    public void win() throws McException
    {
        if (this.inArena())
        {
            final ArenaMatchInterface match = this.getArena().getCurrentMatch();
            if (match != null)
            {
                match.setWinner(this.getPlayerUUID());
                return;
            }
        }
        
        throw new McException(ArenaImpl.Messages.InvalidModificationBeforeStart, "?"); //$NON-NLS-1$
    }
    
}
