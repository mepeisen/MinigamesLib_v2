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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.mcevent.PlayerEnteredZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerLeftZoneEvent;
import de.minigameslib.mclib.shared.api.com.DataFragment;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.LineConfig;
import de.minigameslib.mgapi.api.rules.AbstractComponentRule;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetType;
import de.minigameslib.mgapi.api.rules.HologramConfig;
import de.minigameslib.mgapi.api.rules.HologramRuleInterface;

/**
 * @author mepeisen
 *
 */
public class Hologram extends AbstractComponentRule implements HologramRuleInterface
{
    
    /**
     * Visible within matches?
     */
    private boolean isInMatchVisible;
    
    /**
     * Visible within lobby phase?
     */
    private boolean isLobbyVisible;
    
    /**
     * Visible during maintenance?
     */
    private boolean isMaintenanceVisible;
    
    /**
     * Visible only for admins?
     */
    private boolean isAdminOnly;
    
    /**
     * The localized lines (content).
     */
    private List<LineConfig> lines;
    
    /**
     * @param type
     * @param component
     * @throws McException thrown if config is invalid
     */
    public Hologram(ComponentRuleSetType type, ArenaComponentHandler component) throws McException
    {
        super(type, component);

        this.runInCopiedContext(() -> {
            HologramConfig.AdminOnly.verifyConfig();
            this.isInMatchVisible = HologramConfig.InMatchVisible.getBoolean();
            this.isLobbyVisible = HologramConfig.LobbyVisible.getBoolean();
            this.isMaintenanceVisible = HologramConfig.MaintenanceVisible.getBoolean();
            this.isAdminOnly = HologramConfig.AdminOnly.getBoolean();
            this.lines = new ArrayList<>(Arrays.asList(HologramConfig.Lines.getObjectList(LineConfig.class)));
        });
    }
    
    /**
     * Event on player zone leave
     * @param evt
     * @throws McException 
     */
    @McEventHandler
    public void onLeave(PlayerLeftZoneEvent evt) throws McException
    {
        // TODO
    }
    
    /**
     * Event on player entered leave
     * @param evt
     * @throws McException 
     */
    @McEventHandler
    public void onEnter(PlayerEnteredZoneEvent evt) throws McException
    {
        // TODO
    }

    @Override
    public boolean isInMatchVisible()
    {
        return this.isInMatchVisible;
    }

    @Override
    public void setInMatchVisible(boolean flg) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            HologramConfig.InMatchVisible.setBoolean(flg);
            try
            {
                HologramConfig.InMatchVisible.verifyConfig();
                HologramConfig.InMatchVisible.saveConfig();
            }
            catch (McException ex)
            {
                HologramConfig.InMatchVisible.rollbackConfig();
                throw ex;
            }
        });
        this.component.reconfigureRuleSets(this.type);
    }

    @Override
    public boolean isLobbyVisible()
    {
        return this.isLobbyVisible;
    }

    @Override
    public void setLobbyVisible(boolean flg) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            HologramConfig.LobbyVisible.setBoolean(flg);
            try
            {
                HologramConfig.LobbyVisible.verifyConfig();
                HologramConfig.LobbyVisible.saveConfig();
            }
            catch (McException ex)
            {
                HologramConfig.LobbyVisible.rollbackConfig();
                throw ex;
            }
        });
        this.component.reconfigureRuleSets(this.type);
    }

    @Override
    public boolean isMaintenanceVisible()
    {
        return this.isMaintenanceVisible;
    }

    @Override
    public void setMaintenanceVisible(boolean flg) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            HologramConfig.MaintenanceVisible.setBoolean(flg);
            try
            {
                HologramConfig.MaintenanceVisible.verifyConfig();
                HologramConfig.MaintenanceVisible.saveConfig();
            }
            catch (McException ex)
            {
                HologramConfig.MaintenanceVisible.rollbackConfig();
                throw ex;
            }
        });
        this.component.reconfigureRuleSets(this.type);
    }

    @Override
    public boolean isAdminOnly()
    {
        return this.isAdminOnly;
    }

    @Override
    public void setAdminOnly(boolean flg) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            HologramConfig.AdminOnly.setBoolean(flg);
            try
            {
                HologramConfig.AdminOnly.verifyConfig();
                HologramConfig.AdminOnly.saveConfig();
            }
            catch (McException ex)
            {
                HologramConfig.AdminOnly.rollbackConfig();
                throw ex;
            }
        });
        this.component.reconfigureRuleSets(this.type);
    }

    @Override
    public Collection<LineConfig> getLines()
    {
        return new ArrayList<>(this.lines);
    }

    @Override
    public void setLine(LineConfig config) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            final List<LineConfig> result = new ArrayList<>(this.lines);
            result.removeIf(p -> p.getState() == config.getState());
            result.add(config);
            HologramConfig.Lines.setObjectList(result.toArray(new DataFragment[result.size()]));
            try
            {
                HologramConfig.Lines.verifyConfig();
                HologramConfig.Lines.saveConfig();
            }
            catch (McException ex)
            {
                HologramConfig.Lines.rollbackConfig();
                throw ex;
            }
        });
        this.component.reconfigureRuleSets(this.type);
    }
    
}
