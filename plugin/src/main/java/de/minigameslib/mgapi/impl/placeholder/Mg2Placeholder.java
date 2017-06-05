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

package de.minigameslib.mgapi.impl.placeholder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mclib.api.locale.MessageServiceInterface.Placeholder;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.match.MatchStatisticId;
import de.minigameslib.mgapi.api.rules.BasicArenaRuleSets;
import de.minigameslib.mgapi.api.rules.BasicMatchRuleInterface;

/**
 * @author mepeisen
 *
 */
public class Mg2Placeholder implements Placeholder
{
    
    @Override
    public String resolve(Locale locale, String[] placeholder)
    {
        if (placeholder != null && placeholder.length > 0)
        {
            final List<String> args = new ArrayList<>(Arrays.asList(placeholder));
            switch (args.remove(0))
            {
                case "arena": //$NON-NLS-1$
                    return resolveArena(locale, args);
                case "mpstat": //$NON-NLS-1$
                    return resolveMpstat(locale, args);
                default:
                    // ignore
                    break;
            }
        }
        return null;
    }

    /**
     * @param locale
     * @param args
     * @return result
     */
    private String resolveMpstat(Locale locale, List<String> args)
    {
        final ArenaInterface arena = McLibInterface.instance().getContext(ArenaInterface.class);
        final ArenaMatchInterface match = arena == null ? null : arena.getCurrentMatch();
        final McPlayerInterface player = McLibInterface.instance().getContext(McPlayerInterface.class);
        if (player != null && match != null && args.size() >= 2)
        {
            final MatchStatisticId statid = EnumServiceInterface.instance().getEnumValue(MatchStatisticId.class, args.remove(0), args.remove(1));
            if (statid != null)
            {
                return String.valueOf(match.getStatistic(player.getPlayerUUID(), statid));
            }
        }
        return null;
    }

    /**
     * @param locale
     * @param args
     * @return result
     */
    private String resolveArena(Locale locale, List<String> args)
    {
        final ArenaInterface arena = McLibInterface.instance().getContext(ArenaInterface.class);
        if (arena != null && !args.isEmpty())
        {
            switch (args.remove(0))
            {
                case "name": //$NON-NLS-1$
                    return resolveArenaName(locale, arena);
                case "curplayers": //$NON-NLS-1$
                    return resolveArenaCurplayers(locale, arena);
                case "maxplayers": //$NON-NLS-1$
                    return resolveArenaMaxplayers(locale, arena);
                default:
                    // ignore
                    break;
            }
        }
        return null;
    }

    /**
     * @param locale
     * @param arena
     * @return arena max players
     */
    private String resolveArenaMaxplayers(Locale locale, ArenaInterface arena)
    {
        if (arena.isApplied(BasicArenaRuleSets.BasicMatch))
        {
            return String.valueOf(((BasicMatchRuleInterface) arena.getRuleSet(BasicArenaRuleSets.BasicMatch)).getMaxPlayers());
        }
        return "0"; //$NON-NLS-1$
    }

    /**
     * @param locale
     * @param arena
     * @return arena cur players
     */
    private String resolveArenaCurplayers(Locale locale, ArenaInterface arena)
    {
        return String.valueOf(arena.getActivePlayerCount());
    }

    /**
     * @param locale
     * @param arena
     * @return arena name
     */
    private String resolveArenaName(Locale locale, ArenaInterface arena)
    {
        final LocalizedConfigString str = arena.getDisplayName();
        if (str != null)
        {
            final String res = str.toUserMessage(locale);
            if (res != null && res.trim().length() > 0)
            {
                return res;
            }
        }
        return arena.getInternalName();
    }
    
}
