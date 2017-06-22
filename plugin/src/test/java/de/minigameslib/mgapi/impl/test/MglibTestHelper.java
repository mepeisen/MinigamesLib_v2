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

package de.minigameslib.mgapi.impl.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.locale.MessageServiceInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.shared.api.com.EnumerationValue;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaClassInterface;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.rules.ClassRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ClassRuleSetType;
import de.minigameslib.mgapi.api.team.CommonTeams;
import de.minigameslib.mgapi.impl.arena.ArenaPlayerImpl;

/**
 * Helper functions for mglib test cases.
 * 
 * @author mepeisen
 *
 */
public class MglibTestHelper
{
    
    /**
     * Creates the library mock.
     * @param players
     * @return lib
     * @throws ClassNotFoundException 
     */
    public static MinigamesLibInterface createLib(ArenaPlayerInterface... players) throws ClassNotFoundException
    {
        final MinigamesLibInterface result = mock(MinigamesLibInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mgapi.api.MglibCache"), "SERVICES", result); //$NON-NLS-1$ //$NON-NLS-2$
        for (ArenaPlayerInterface player : players)
        {
            final UUID uuid = player.getPlayerUUID();
            final McPlayerInterface mcplayer = player.getMcPlayer();
            when(result.getPlayer(mcplayer)).thenReturn(player);
            when(result.getPlayer(uuid)).thenReturn(player);
        }
        when(result.isSpecialTeam(CommonTeams.Losers)).thenReturn(true);
        when(result.isSpecialTeam(CommonTeams.Spectators)).thenReturn(true);
        when(result.isSpecialTeam(CommonTeams.Winners)).thenReturn(true);
        when(result.isSpecialTeam(CommonTeams.Unknown)).thenReturn(true);
        return result;
    }
    
    /**
     * Creates a random arena player
     * @return arena player
     */
    public static ArenaPlayerInterface createPlayer()
    {
        return createPlayer(UUID.randomUUID());
    }
    
    /**
     * Creates a random arena player
     * @param randomUUID
     * @return arena player
     */
    public static ArenaPlayerInterface createPlayer(UUID randomUUID)
    {
        final ArenaPlayerInterface result = mock(ArenaPlayerImpl.class);
        when(result.getPlayerUUID()).thenReturn(randomUUID);
        final McPlayerInterface mcplayer = mock(McPlayerInterface.class);
        when(mcplayer.getPlayerUUID()).thenReturn(randomUUID);
        when(result.getMcPlayer()).thenReturn(mcplayer);
        final Player bukkit = mock(Player.class);
        when(bukkit.getUniqueId()).thenReturn(randomUUID);
        when(mcplayer.getBukkitPlayer()).thenReturn(bukkit);
        when(mcplayer.getOfflinePlayer()).thenReturn(bukkit);
        when(result.getBukkitPlayer()).thenReturn(bukkit);
        final PlayerInventory inv = mock(PlayerInventory.class);
        when(bukkit.getInventory()).thenReturn(inv);
        return result;
    }
    
    /**
     * init placeholders dummy.
     * @throws ClassNotFoundException 
     */
    public static void initPlaceholdersDummy() throws ClassNotFoundException
    {
        final MessageServiceInterface msi = mock(MessageServiceInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.locale.MessageServiceCache"), "SERVICES", msi); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * returns a class rule.
     * @return arena class rule.
     */
    public static ClassRuleSetInterface createClassRule()
    {
        final ClassRuleSetType type = mock(ClassRuleSetType.class);
        final ClassRuleSetInterface clazz = mock(ClassRuleSetInterface.class);
        when(clazz.getType()).thenReturn(type);
        return clazz;
    }
    
    /**
     * returns a class.
     * @param rules
     * @return arena class.
     */
    public static ArenaClassInterface createClass(ClassRuleSetInterface... rules)
    {
        final ArenaClassInterface result = mock(ArenaClassInterface.class);
        final List<ClassRuleSetType> typeslist = Arrays.asList(rules).stream().map(ClassRuleSetInterface::getType).collect(Collectors.toList());
        when(result.getAppliedRuleSetTypes()).thenReturn(typeslist);
        for (final ClassRuleSetInterface rule : rules)
        {
            final ClassRuleSetType type = rule.getType();
            when(result.getRuleSet(type)).thenReturn(rule);
        }
        return result;
    }
    
    /**
     * init enum services dummy.
     * @throws ClassNotFoundException 
     */
    public static void initEnumServicesDummy() throws ClassNotFoundException
    {
        final EnumServiceInterface esi = mock(EnumServiceInterface.class);
        final Plugin plugin = mock(Plugin.class);
        when(plugin.getName()).thenReturn("MinigamesLib"); //$NON-NLS-1$
        when(esi.getPlugin(any(EnumerationValue.class))).thenReturn(plugin);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.enums.EnumServiceCache"), "SERVICES", esi); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
}
