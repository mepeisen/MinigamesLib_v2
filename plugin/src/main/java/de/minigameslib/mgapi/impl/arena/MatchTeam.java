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

package de.minigameslib.mgapi.impl.arena;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;

import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.events.ArenaTeamStatisticEvent;
import de.minigameslib.mgapi.api.match.MatchStatisticId;
import de.minigameslib.mgapi.api.match.MatchTeamInterface;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * Data class for teams being present in a match
 * 
 * @author mepeisen
 *
 */
class MatchTeam implements MatchTeamInterface
{
    
    /** the team id. */
    private final TeamIdType teamId;
    
    /** registered team members. */
    private final Set<UUID> teamMembers = new HashSet<>();
    
    /** underlying arena. */
    private ArenaInterface arena;
    
    /**
     * the match statistics.
     */
    private final Map<MatchStatisticId, Integer> statistics = new HashMap<>();

    /**
     * Constructor
     * @param arena
     * @param teamId
     */
    public MatchTeam(ArenaInterface arena, TeamIdType teamId)
    {
        this.teamId = teamId;
        this.arena = arena;
    }

    @Override
    public TeamIdType getTeamId()
    {
        return this.teamId;
    }
    
    @Override
    public Set<UUID> getMembers()
    {
        return Collections.unmodifiableSet(this.teamMembers);
    }

    /**
     * @return the teamMembers
     */
    public Set<UUID> getTeamMembers()
    {
        return this.teamMembers;
    }

    @Override
    public int getStatistic(MatchStatisticId statistic)
    {
        final Integer result = this.statistics.get(statistic);
        return result == null ? 0 : result.intValue();
    }
    
    @Override
    public void setStatistic(MatchStatisticId statistic, int newValue)
    {
        final int oldValue = this.getStatistic(statistic);
        this.statistics.put(statistic, Integer.valueOf(newValue));
        
        final ArenaTeamStatisticEvent event = new ArenaTeamStatisticEvent(this.arena, this.teamId, statistic, oldValue, newValue);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    @Override
    public int addStatistic(MatchStatisticId statistic, int amount)
    {
        final int oldValue = this.getStatistic(statistic);
        final int newValue = oldValue + amount;
        this.setStatistic(statistic, newValue);
        
        final ArenaTeamStatisticEvent event = new ArenaTeamStatisticEvent(this.arena, this.teamId, statistic, oldValue, newValue);
        Bukkit.getPluginManager().callEvent(event);
        
        return newValue;
    }
    
    @Override
    public int decStatistic(MatchStatisticId statistic, int amount)
    {
        final int oldValue = this.getStatistic(statistic);
        final int newValue = oldValue - amount;
        this.setStatistic(statistic, newValue);
        
        final ArenaTeamStatisticEvent event = new ArenaTeamStatisticEvent(this.arena, this.teamId, statistic, oldValue, newValue);
        Bukkit.getPluginManager().callEvent(event);
        
        return newValue;
    }
    
}
