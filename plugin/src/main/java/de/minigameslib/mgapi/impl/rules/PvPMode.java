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
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.rules.AbstractZoneRule;
import de.minigameslib.mgapi.api.rules.PvPModeRuleInterface;
import de.minigameslib.mgapi.api.rules.PvpModeConfig;
import de.minigameslib.mgapi.api.rules.PvpModeConfig.PvpModes;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetType;

/**
 * @author mepeisen
 *
 */
public class PvPMode extends AbstractZoneRule implements PvPModeRuleInterface
{
    
    /** 
     * the current pvp mode.
     */
    private PvpModes mode;
    
    /**
     * @param type
     * @param zone
     * @throws McException thrown if config is invalid
     */
    public PvPMode(ZoneRuleSetType type, ArenaZoneHandler zone) throws McException
    {
        super(type, zone);
        this.runInCopiedContext(() -> {
            PvpModeConfig.PvpOption.verifyConfig();
            this.mode = PvpModeConfig.PvpOption.getEnum(PvpModes.class);
            if (this.mode == null)
            {
                this.mode = PvpModes.NoPvp;
            }
        });
    }

    @Override
    public ZoneRuleSetType getType()
    {
        return this.type;
    }

    @Override
    public PvpModes getPvpMode()
    {
        return this.mode;
    }

    @Override
    public void setPvpMode(PvpModes mode) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            PvpModeConfig.PvpOption.setEnum(mode);
            try
            {
                PvpModeConfig.PvpOption.verifyConfig();
                PvpModeConfig.PvpOption.saveConfig();
            }
            catch (McException ex)
            {
                PvpModeConfig.PvpOption.rollbackConfig();
                throw ex;
            }
        });
        this.zone.reconfigureRuleSets(this.type);
    }
    
    /**
     * Player dmg event
     * @param evt
     */
    @McEventHandler
    public void onPlayerDmg(McEntityDamageByEntityEvent evt)
    {
        if (evt.getPlayer() != null && evt.getBukkitEvent().getDamager() instanceof Player)
        {
            final Player damager = (Player) evt.getBukkitEvent().getDamager();
            if (!ObjectServiceInterface.instance().isHuman(damager))
            {
                switch (this.mode)
                {
                    default:
                    case NoPvp:
                        evt.getBukkitEvent().setCancelled(true);
                        break;
                    case Normal:
                        // allow everything
                        break;
                    case PvpDuringMatch:
                        if (this.arena.getState() != ArenaState.Match)
                        {
                            evt.getBukkitEvent().setCancelled(true);
                        }
                        break;
                }
            }
        }
    }
    
}
