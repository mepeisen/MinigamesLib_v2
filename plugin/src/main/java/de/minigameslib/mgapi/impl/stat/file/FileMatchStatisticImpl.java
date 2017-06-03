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

package de.minigameslib.mgapi.impl.stat.file;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;
import de.minigameslib.mgapi.api.match.MatchResult;
import de.minigameslib.mgapi.api.stat.GameStatisticId;
import de.minigameslib.mgapi.api.stat.MatchStatisticInterface;
import de.minigameslib.mgapi.api.stat.MatchStatisticPlayerInterface;
import de.minigameslib.mgapi.api.stat.MatchStatisticTeamInterface;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * File match statistics (single match).
 * 
 * @author mepeisen
 */
public class FileMatchStatisticImpl extends AnnotatedDataFragment implements MatchStatisticInterface
{
    
    /**
     * start timestamp.
     */
    @PersistentField
    protected LocalDateTime started;
    
    /**
     * finish timestamp.
     */
    @PersistentField
    protected LocalDateTime finished;

    /**
     * winners (in order).
     */
    @PersistentField
    protected List<UUID> winners = new ArrayList<>();
    
    /**
     * losers (in order).
     */
    @PersistentField
    protected List<UUID> losers = new ArrayList<>();
    
    /**
     * player stats.
     */
    @PersistentField
    protected Map<UUID, FileMatchStatisticPlayerImpl> playerStats = new HashMap<>();
    
    /**
     * team stats.
     */
    @PersistentField
    protected Map<String, FileMatchStatisticTeamImpl> teamStats = new HashMap<>();
    
    /**
     * the match results.
     */
    @PersistentField
    protected List<FileMatchResultImpl> results = new ArrayList<>();
    
    /**
     * match stats.
     */
    @PersistentField
    protected Map<String, Long> stats = new HashMap<>();
    
    @Override
    public LocalDateTime getStarted()
    {
        return this.started;
    }
    
    @Override
    public LocalDateTime getFinished()
    {
        return this.finished;
    }
    
    @Override
    public int getPlayerCount()
    {
        return this.playerStats.size();
    }
    
    @Override
    public MatchStatisticPlayerInterface getPlayer(UUID uuid)
    {
        return this.playerStats.get(uuid);
    }
    
    @Override
    public List<MatchStatisticPlayerInterface> getPlayers(int start, int limit)
    {
        return this.playerStats.values().stream().skip(start).limit(limit).collect(Collectors.toList());
    }
    
    @Override
    public int getTeamCount()
    {
        return this.teamStats.size();
    }
    
    @Override
    public MatchStatisticTeamInterface getTeam(TeamIdType type)
    {
        return this.teamStats.get(type.getPluginName() + "$" + type.name()); //$NON-NLS-1$
    }
    
    @Override
    public List<MatchStatisticTeamInterface> getTeams(int start, int limit)
    {
        return this.teamStats.values().stream().skip(start).limit(limit).collect(Collectors.toList());
    }
    
    @Override
    public Collection<UUID> getWinners(int start, int limit)
    {
        return this.winners.stream().skip(start).limit(limit).collect(Collectors.toList());
    }
    
    @Override
    public int getWinnerCount()
    {
        return this.winners.size();
    }
    
    @Override
    public Collection<UUID> getLosers(int start, int limit)
    {
        return this.losers.stream().skip(start).limit(limit).collect(Collectors.toList());
    }
    
    @Override
    public int getLoserCount()
    {
        return this.losers.size();
    }
    
    @Override
    public Collection<MatchResult> getResults(int start, int limit)
    {
        return this.results.stream().skip(start).limit(limit).collect(Collectors.toList());
    }
    
    @Override
    public int getResultCount()
    {
        return this.results.size();
    }
    
    @Override
    public MatchResult getResult(int place)
    {
        return this.results.get(place - 1);
    }
    
    @Override
    public Long getStatistic(GameStatisticId statistic)
    {
        return this.stats.get(statistic.getPluginName() + "$" + statistic.name()); //$NON-NLS-1$
    }
    
}
