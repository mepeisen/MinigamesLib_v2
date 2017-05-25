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

package de.minigameslib.mgapi.api;

import java.util.Collections;
import java.util.Set;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetType;
import de.minigameslib.mgapi.api.rules.SignRuleSetType;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetType;
import de.minigameslib.mgapi.api.stat.MutableMatchStatisticInterface;

/**
 * A proider to describe an extension.
 * 
 * @author mepeisen
 */
public interface ExtensionProvider
{
    
    /**
     * returns the extensions internal/ technical name.
     * 
     * @return extension name.
     */
    String getName();
    
    /**
     * Returns a display name for the extension.
     * 
     * @return extension display name.
     */
    LocalizedMessageInterface getDisplayName();
    
    /**
     * Returns a short single-line description of the extension
     * 
     * @return short single-line description
     */
    LocalizedMessageInterface getShortDescription();
    
    /**
     * Returns a multi-line description of the extension
     * 
     * @return multi-line description
     */
    LocalizedMessageInterface getDescription();
    
    /**
     * Returns a how-to-use manual, mainly for administrators
     * 
     * @return how-to-use manual, mainly for administrators
     */
    LocalizedMessageInterface getManual();
    
    /**
     * Processes a match after being played and overtake statistics to become persistent.
     * 
     * @param match
     *            finished arena match
     * @param stats
     *            match statistics
     */
    default void processMatchStatistics(ArenaMatchInterface match, MutableMatchStatisticInterface stats)
    {
        // empty
    }
    
    /**
     * Returns the optional arena rules for given type.
     * 
     * @param type
     *            arena type
     * @return arena rules.
     */
    default Set<ArenaRuleSetType> getOptionalArenaRules(ArenaTypeInterface type)
    {
        return Collections.emptySet();
    }
    
    /**
     * Returns all optional rule sets for this type.
     * 
     * @param arena
     *            target arena
     * @param sign
     *            target sign
     * @return all optional rule sets being available for this sign.
     */
    default Set<SignRuleSetType> getOptionalRuleSets(ArenaInterface arena, SignInterface sign)
    {
        return Collections.emptySet();
    }
    
    /**
     * Returns all optional rule sets for this type.
     * 
     * @param arena
     *            target arena
     * @param zone
     *            target zone
     * @return all optional rule sets being available for this zone.
     */
    default Set<ZoneRuleSetType> getOptionalRuleSets(ArenaInterface arena, ZoneInterface zone)
    {
        return Collections.emptySet();
    }
    
    /**
     * Returns all optional rule sets for this type.
     * 
     * @param arena
     *            target arena
     * @param component
     *            target component
     * @return all optional rule sets being available for this component.
     */
    default Set<ComponentRuleSetType> getOptionalRuleSets(ArenaInterface arena, ComponentInterface component)
    {
        return Collections.emptySet();
    }
    
}
