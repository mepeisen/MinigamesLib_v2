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
import de.minigameslib.mclib.api.config.ConfigurationFloat;
import de.minigameslib.mclib.api.config.ConfigurationInt;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;
import de.minigameslib.mclib.api.config.ValidateFMin;
import de.minigameslib.mclib.api.config.ValidateLMin;

/**
 * heal configurations
 * 
 * @author mepeisen
 * 
 * @see BasicArenaRuleSets#HealAtBegin
 */
@ConfigurationValues(path = "core")
public enum HealAfterDeathConfig implements ConfigurationValueInterface
{
    
    /**
     * the heal per tick
     */
    @ConfigurationFloat(defaultValue = 0.1f)
    @ConfigComment({"Heal amount per tick"})
    @ValidateFMin(0.0d)
    HealPerTick,
    
    /**
     * maximum ticks to receive heal
     */
    @ConfigurationInt(defaultValue = 40)
    @ConfigComment({"Maximum heal ticks"})
    @ValidateLMin(0)
    MaxHealTicks,
    
}
