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

package de.minigameslib.mgapi.api.rules;

import java.util.Collection;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.obj.LineConfig;

/**
 * @author mepeisen
 *
 */
public interface ScoreboardRuleInterface extends ZoneRuleSetInterface
{
    
    /**
     * Returns the match visible flag.
     * @return Visible within matches?
     */
    boolean isInMatchVisible();
    
    /**
     * Sets the match visible flag.
     * @param flg Visible within matches?
     * @throws McException thrown if there are problems saving the config 
     */
    void setInMatchVisible(boolean flg) throws McException;
    
    /**
     * Returns the lobby visible flag.
     * @return Visible within lobby phase?
     */
    boolean isLobbyVisible();
    
    /**
     * Sets the lobby visible flag.
     * @param flg Visible within lobby phase?
     * @throws McException thrown if there are problems saving the config 
     */
    void setLobbyVisible(boolean flg) throws McException;
    
    /**
     * Returns the maintenance visible flag.
     * @return Visible during maintenance?
     */
    boolean isMaintenanceVisible();
    
    /**
     * Sets the maintenance visible flag.
     * @param flg Visible during maintenance?
     * @throws McException thrown if there are problems saving the config 
     */
    void setMaintenanceVisible(boolean flg) throws McException;
    
    /**
     * Returns the admin only flag.
     * @return Visible only for admins?
     */
    boolean isAdminOnly();
    
    /**
     * Sets the admin only flag.
     * @param flg Visible only for admins?
     * @throws McException thrown if there are problems saving the config 
     */
    void setAdminOnly(boolean flg) throws McException;

    /**
     * Returns the lines.
     * @return lines.
     */
    Collection<LineConfig> getLines();
    
    /**
     * Sets the line config with given state in {@link LineConfig#getState()}.
     * @param config config
     * @throws McException thrown if there are problems saving the config 
     */
    void setLine(LineConfig config) throws McException;

}
