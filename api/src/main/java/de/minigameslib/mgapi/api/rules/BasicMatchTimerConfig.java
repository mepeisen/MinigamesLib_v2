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

import org.bukkit.Sound;

import de.minigameslib.mclib.api.config.ConfigComment;
import de.minigameslib.mclib.api.config.ConfigurationBool;
import de.minigameslib.mclib.api.config.ConfigurationInt;
import de.minigameslib.mclib.api.config.ConfigurationJavaEnum;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;
import de.minigameslib.mclib.api.validate.ValidateLMax;
import de.minigameslib.mclib.api.validate.ValidateLMin;

/**
 * Basic match timer configurations
 * 
 * @author mepeisen
 * 
 * @see BasicArenaRuleSets#BasicMatchTimer
 */
@ConfigurationValues(path = "core")
public enum BasicMatchTimerConfig implements ConfigurationValueInterface
{
    /**
     * The maximum match time in seconds
     */
    @ConfigurationInt(defaultValue = 120)
    @ConfigComment({"The maximum match time in seconds"})
    @ValidateLMin(0)
    @ValidateLMax(60*60*24)
    MaxSeconds,
    
    /**
     * The sound to play for each countdown tick
     */
    @ConfigurationJavaEnum(clazz = Sound.class)
    @ConfigComment({"The sound to play for each countdown tick"})
    CountdownSound,
    
    /**
     * Flag to play the tick sound for countdown
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({"Flag to play the tick sound for countdown"})
    CountdownPlaySound,
    
}
