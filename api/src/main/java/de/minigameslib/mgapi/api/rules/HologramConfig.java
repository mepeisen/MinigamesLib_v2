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

import de.minigameslib.mclib.api.config.ConfigComment;
import de.minigameslib.mclib.api.config.ConfigurationBool;
import de.minigameslib.mclib.api.config.ConfigurationObjectList;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;
import de.minigameslib.mgapi.api.obj.LineConfig;

/**
 * Basic spawns configurations
 * 
 * @author mepeisen
 * 
 * @see BasicArenaRuleSets#BasicSpawns
 */
@ConfigurationValues(path = "core")
public enum HologramConfig implements ConfigurationValueInterface
{
    
    /**
     * Visible within matches?
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment("Visible within matches?")
    InMatchVisible,
    
    /**
     * Visible within lobby phase?
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment("Visible within lobby phase?")
    LobbyVisible,
    
    /**
     * Visible during maintenance?
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment("Visible within maintenance phase?")
    MaintenanceVisible,
    
    /**
     * Visible only for admins?
     */
    @ConfigurationBool(defaultValue = false)
    @ConfigComment("Visible only for admins?")
    AdminOnly,
    
    /**
     * The localized lines (content).
     */
    @ConfigurationObjectList(clazz = LineConfig.class)
    @ConfigComment({"The localized lines (content)."})
    Lines,
    
}
