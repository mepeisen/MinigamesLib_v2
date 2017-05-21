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
import de.minigameslib.mclib.api.config.ConfigurationDouble;
import de.minigameslib.mclib.api.config.ConfigurationFloat;
import de.minigameslib.mclib.api.config.ConfigurationInt;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;
import de.minigameslib.mclib.api.config.ValidateFMax;
import de.minigameslib.mclib.api.config.ValidateFMin;
import de.minigameslib.mclib.api.config.ValidateLMax;
import de.minigameslib.mclib.api.config.ValidateLMin;

/**
 * Basic match configurations
 * 
 * @author mepeisen
 * 
 * @see BasicArenaRuleSets#BasicMatch
 */
@ConfigurationValues(path = "core")
public enum BasicMatchConfig implements ConfigurationValueInterface
{
    
    /**
     * The minimum players to start a match
     */
    @ConfigurationInt(defaultValue = 2)
    @ConfigComment({"The minimum players to start a match"})
    @ValidateLMin(2)
    @ValidateLMax(100)
    MinPlayers, // TODO validate min <= max
    
    /**
     * The maximum players inside an arena
     */
    @ConfigurationInt(defaultValue = 10)
    @ConfigComment({"The maximum players allowed per match"})
    @ValidateLMin(2)
    @ValidateLMax(100)
    MaxPlayers, // TODO validate min <= max
    
    /**
     * The seconds for lobby count down before match starts
     */
    @ConfigurationInt(defaultValue = 30)
    @ConfigComment({"The seconds for lobby count down before match starts"})
    @ValidateLMin(5)
    @ValidateLMax(600)
    LobbyCountdown,
    
    /**
     * The pre match countdown
     */
    @ConfigurationInt(defaultValue = 5)
    @ConfigComment({"The seconds for pre match phase before match starts"})
    @ValidateLMin(5)
    @ValidateLMax(600)
    PreMatchCountdown,
    
    /**
     * The starting movement speed
     */
    @ConfigurationFloat(defaultValue = 0.2f)
    @ConfigComment({"The movement speed"})
    @ValidateFMin(-1.0d)
    @ValidateFMax(1.0d)
    MovementSpeed,
    
    /**
     * The starting fly speed
     */
    @ConfigurationFloat(defaultValue = 0.1f)
    @ConfigComment({"The fly speed"})
    @ValidateFMin(-1.0d)
    @ValidateFMax(1.0d)
    FlySpeed,
    
    /**
     * The starting max health
     */
    @ConfigurationDouble(defaultValue = 20d)
    @ConfigComment({"The max health"})
    @ValidateFMin(0.00000001d)
    @ValidateFMax(20d)
    MaxHealth,
    
    /**
     * The starting health
     */
    @ConfigurationDouble(defaultValue = 20d)
    @ConfigComment({"The health"})
    @ValidateFMin(0.00000001d)
    @ValidateFMax(20d)
    Health,
    
}
