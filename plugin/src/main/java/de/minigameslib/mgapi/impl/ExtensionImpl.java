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

import java.util.Set;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mgapi.api.ExtensionInterface;
import de.minigameslib.mgapi.api.ExtensionProvider;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetType;
import de.minigameslib.mgapi.api.rules.SignRuleSetType;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetType;

/**
 * @author mepeisen
 *
 */
class ExtensionImpl implements ExtensionInterface
{

    /** owning plugin. */
    private final Plugin plugin;
    
    /** extension provider. */
    private final ExtensionProvider provider;

    /**
     * @param plugin
     * @param provider
     */
    public ExtensionImpl(Plugin plugin, ExtensionProvider provider)
    {
        this.plugin = plugin;
        this.provider = provider;
    }

    @Override
    public String getName()
    {
        return this.provider.getName();
    }

    @Override
    public LocalizedMessageInterface getDisplayName()
    {
        return this.provider.getDisplayName();
    }

    @Override
    public LocalizedMessageInterface getShortDescription()
    {
        return this.provider.getShortDescription();
    }

    @Override
    public LocalizedMessageInterface getDescription()
    {
        return this.provider.getDescription();
    }

    @Override
    public LocalizedMessageInterface getManual()
    {
        return this.provider.getManual();
    }

    @Override
    public Plugin getPlugin()
    {
        return this.plugin;
    }

    /**
     * Returns the optional arena rules for given type.
     * @param type arena type
     * @return arena rules.
     */
    public Set<ArenaRuleSetType> getOptionalArenaRules(ArenaTypeInterface type)
    {
        return this.provider.getOptionalArenaRules(type);
    }
    
    /**
     * Returns all optional rule sets for this type; can be influenced by extensions.
     * 
     * @param arena
     *            target arena
     * @param sign
     *            target sign
     * @return all optional rule sets being available for this sign.
     */
    public Set<SignRuleSetType> getOptionalRuleSets(ArenaInterface arena, SignInterface sign)
    {
        return this.provider.getOptionalRuleSets(arena, sign);
    }
    
    /**
     * Returns all optional rule sets for this type; can be influenced by extensions.
     * 
     * @param arena
     *            target arena
     * @param zone
     *            target zone
     * @return all optional rule sets being available for this zone.
     */
    public Set<ZoneRuleSetType> getOptionalRuleSets(ArenaInterface arena, ZoneInterface zone)
    {
        return this.provider.getOptionalRuleSets(arena, zone);
    }
    
    /**
     * Returns all optional rule sets for this type; can be influenced by extensions.
     * 
     * @param arena
     *            target arena
     * @param component
     *            target component
     * @return all optional rule sets being available for this component.
     */
    public Set<ComponentRuleSetType> getOptionalRuleSets(ArenaInterface arena, ComponentInterface component)
    {
        return this.provider.getOptionalRuleSets(arena, component);
    }
    
}
