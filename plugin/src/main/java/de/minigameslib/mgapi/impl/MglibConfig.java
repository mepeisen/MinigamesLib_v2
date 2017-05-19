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

package de.minigameslib.mgapi.impl;

import de.minigameslib.mclib.api.config.ConfigComment;
import de.minigameslib.mclib.api.config.ConfigurationBool;
import de.minigameslib.mclib.api.config.ConfigurationStringList;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;

/**
 * Common configuration
 * 
 * @author mepeisen
 */
@ConfigurationValues(path = "core")
public enum MglibConfig implements ConfigurationValueInterface
{
 
    /**
     * Debug flag.
     */
    @ConfigurationBool(defaultValue = false)
    @ConfigComment({
        "Set this to true to enable some debugging output within minigames.",
        "This can be useful to find problems and errors.",
        "But be aware that debugging slows down the system."
        })
    Debug,
    
    /**
     * Arena names.
     */
    @ConfigurationStringList
    @ConfigComment({
        "Contains the internal/ technical names of arenas. Each arena is listed here.",
        "The arena core data is stored within arenas configuration folder."
    })
    Arenas,
    
    // restore options
    
    /**
     * Restore player inventory on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player inventories on regular arena leave.",
        "If you have multiple worlds and experience problems restoring player inventories try set to set this value to false."
    })
    RestoreInventoryOnLeave,
    
    /**
     * Restore player inventory on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player inventories on arena leave during crashes/ disconnects.",
        "If you have multiple worlds and experience problems restoring player inventories try set to set this value to false."
    })
    RestoreInventoryOnCrash,
    
    /**
     * Restore player game mode on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player game modes on regular arena leave.",
        "If you have multiple worlds and experience problems restoring player game modes try set to set this value to false."
    })
    RestoreGameModeOnLeave,
    
    /**
     * Restore player game mode on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player game modes on arena leave during crashes/ disconnects.",
        "If you have multiple worlds and experience problems restoring player game modes try set to set this value to false."
    })
    RestoreGameModeOnCrash,
    
    /**
     * Restore player compass target on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player compass target on regular arena leave.",
        "If you have multiple worlds and experience problems restoring player compass targets try set to set this value to false."
    })
    RestoreCompassTargetOnLeave,
    
    /**
     * Restore player compass target on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player compass target on arena leave during crashes/ disconnects.",
        "If you have multiple worlds and experience problems restoring player compass targets try set to set this value to false."
    })
    RestoreCompassTargetOnCrash,
    
    /**
     * Restore player food level on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player food level on regular arena leave.",
        "If you have multiple worlds and experience problems restoring player food levels try set to set this value to false."
    })
    RestoreFoodOnLeave,
    
    /**
     * Restore player food level on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player food level on arena leave during crashes/ disconnects.",
        "If you have multiple worlds and experience problems restoring player food levels try set to set this value to false."
    })
    RestoreFoodOnCrash,
    
    /**
     * Restore player experience on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player experience on regular arena leave.",
        "If you have multiple worlds and experience problems restoring player experiences try set to set this value to false."
    })
    RestoreExperienceOnLeave,
    
    /**
     * Restore player experience on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player experience on arena leave during crashes/ disconnects.",
        "If you have multiple worlds and experience problems restoring player experiences try set to set this value to false."
    })
    RestoreExperienceOnCrash,
    
    /**
     * Restore player health on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player health on regular arena leave.",
        "If you have multiple worlds and experience problems restoring player healths try set to set this value to false."
    })
    RestoreHealthOnLeave,
    
    /**
     * Restore player health on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player health on arena leave during crashes/ disconnects.",
        "If you have multiple worlds and experience problems restoring player healths try set to set this value to false."
    })
    RestoreHealthOnCrash,
    
    /**
     * Restore player speed on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player speed on regular arena leave.",
        "If you have multiple worlds and experience problems restoring player speed try set to set this value to false."
    })
    RestoreSpeedLeave,
    
    /**
     * Restore player speed on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player speed on arena leave during crashes/ disconnects.",
        "If you have multiple worlds and experience problems restoring player speed try set to set this value to false."
    })
    RestoreSpeedOnCrash,
    
    /**
     * Restore player fly options on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player fly options on regular arena leave.",
        "If you have multiple worlds and experience problems restoring player fly options try set to set this value to false."
    })
    RestoreFlyOnLeave,
    
    /**
     * Restore player fly options on arena leave.
     */
    @ConfigurationBool(defaultValue = true)
    @ConfigComment({
        "Flag to restore player fly options on arena leave during crashes/ disconnects.",
        "If you have multiple worlds and experience problems restoring player fly options try set to set this value to false."
    })
    RestoreFlyOnCrash,
    
}
