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
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.obj.LineConfig;
import de.minigameslib.mgapi.api.rules.AbstractZoneRule;
import de.minigameslib.mgapi.api.rules.ScoreboardConfig;
import de.minigameslib.mgapi.api.rules.ScoreboardRuleInterface;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetType;

/**
 * @author mepeisen
 *
 */
public class Scoreboard extends AbstractZoneRule implements ScoreboardRuleInterface
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
     * @param zone
     * @throws McException thrown if config is invalid
     */
    public Scoreboard(ZoneRuleSetType type, ArenaZoneHandler zone) throws McException
    {
        super(type, zone);

        this.runInCopiedContext(() -> {
            ScoreboardConfig.AdminOnly.verifyConfig();
            this.isInMatchVisible = ScoreboardConfig.InMatchVisible.getBoolean();
            this.isLobbyVisible = ScoreboardConfig.LobbyVisible.getBoolean();
            this.isMaintenanceVisible = ScoreboardConfig.MaintenanceVisible.getBoolean();
            this.isAdminOnly = ScoreboardConfig.AdminOnly.getBoolean();
            this.lines = new ArrayList<>(Arrays.asList(ScoreboardConfig.Lines.getObjectList(LineConfig.class)));
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
            ScoreboardConfig.InMatchVisible.setBoolean(flg);
            try
            {
                ScoreboardConfig.InMatchVisible.verifyConfig();
                ScoreboardConfig.InMatchVisible.saveConfig();
            }
            catch (McException ex)
            {
                ScoreboardConfig.InMatchVisible.rollbackConfig();
                throw ex;
            }
        });
        this.zone.reconfigureRuleSets(this.type);
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
            ScoreboardConfig.LobbyVisible.setBoolean(flg);
            try
            {
                ScoreboardConfig.LobbyVisible.verifyConfig();
                ScoreboardConfig.LobbyVisible.saveConfig();
            }
            catch (McException ex)
            {
                ScoreboardConfig.LobbyVisible.rollbackConfig();
                throw ex;
            }
        });
        this.zone.reconfigureRuleSets(this.type);
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
            ScoreboardConfig.MaintenanceVisible.setBoolean(flg);
            try
            {
                ScoreboardConfig.MaintenanceVisible.verifyConfig();
                ScoreboardConfig.MaintenanceVisible.saveConfig();
            }
            catch (McException ex)
            {
                ScoreboardConfig.MaintenanceVisible.rollbackConfig();
                throw ex;
            }
        });
        this.zone.reconfigureRuleSets(this.type);
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
            ScoreboardConfig.AdminOnly.setBoolean(flg);
            try
            {
                ScoreboardConfig.AdminOnly.verifyConfig();
                ScoreboardConfig.AdminOnly.saveConfig();
            }
            catch (McException ex)
            {
                ScoreboardConfig.AdminOnly.rollbackConfig();
                throw ex;
            }
        });
        this.zone.reconfigureRuleSets(this.type);
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
            ScoreboardConfig.Lines.setObjectList(result.toArray(new DataFragment[result.size()]));
            try
            {
                ScoreboardConfig.Lines.verifyConfig();
                ScoreboardConfig.Lines.saveConfig();
            }
            catch (McException ex)
            {
                ScoreboardConfig.Lines.rollbackConfig();
                throw ex;
            }
        });
        this.zone.reconfigureRuleSets(this.type);
    }
    
}
