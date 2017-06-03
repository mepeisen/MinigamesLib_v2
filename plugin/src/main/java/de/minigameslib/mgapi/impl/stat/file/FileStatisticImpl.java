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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.google.common.cache.LoadingCache;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.util.function.McFunctionUtils;
import de.minigameslib.mgapi.api.stat.GameStatisticId;
import de.minigameslib.mgapi.api.stat.MatchStatisticInterface;
import de.minigameslib.mgapi.api.stat.PlayerStatisticId;
import de.minigameslib.mgapi.api.stat.StatisticInterface;
import de.minigameslib.mgapi.api.stat.TeamStatisticId;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * @author mepeisen
 *
 */
public class FileStatisticImpl implements StatisticInterface
{
    
    /** the entries for this statistics. */
    private final List<FileStatRegistryEntry> entries;
    
    /**
     * file cache.
     */
    private LoadingCache<FileStatRegistryEntry, FileMatchStatisticImpl> fileCache;
    
    /**
     * Constructor.
     * @param fileCache 
     * @param entries
     */
    public FileStatisticImpl(LoadingCache<FileStatRegistryEntry, FileMatchStatisticImpl> fileCache, List<FileStatRegistryEntry> entries)
    {
        this.entries = entries;
        this.fileCache = fileCache;
    }

    @Override
    public int getMatchCount()
    {
        return this.entries.size();
    }
    
    /**
     * mapping for reading entry from cache.
     * @param e entry
     * @return match statistics
     * @throws McException
     */
    private MatchStatisticInterface map(FileStatRegistryEntry e) throws McException
    {
        try
        {
            return this.fileCache.get(e);
        }
        catch (ExecutionException ex)
        {
            throw new McException(CommonMessages.InternalError, ex, ex.getMessage());
        }
    }
    
    @Override
    public List<MatchStatisticInterface> getMatches(int start, int limit) throws McException
    {
        try
        {
            return this.entries.stream().skip(start).limit(limit).map(McFunctionUtils.wrap(this::map)).collect(Collectors.toList());
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }
    
    @Override
    public int getBestPlace(UUID playerUuid) throws McException
    {
        try
        {
            return this.entries.stream().map(McFunctionUtils.wrap(this::map))
                    .map(stat -> stat.getPlace(playerUuid))
                    .reduce(-1, (a, b) -> {
                        if (a == -1) return b;
                        if (b == -1) return a;
                        return Math.min(a, b);
                    });
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }
    
    @Override
    public Long getBestValue(UUID playerUuid, PlayerStatisticId statistic) throws McException
    {
        try
        {
            return this.entries.stream().map(McFunctionUtils.wrap(this::map))
                    .map(stat -> stat.getPlayerStat(playerUuid, statistic))
                    .reduce((Long) null, (a, b) -> {
                        if (a == null) return b;
                        if (b == null) return a;
                        return Math.max(a, b);
                    });
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }
    
    @Override
    public Long getWorstValue(UUID playerUuid, PlayerStatisticId statistic) throws McException
    {
        try
        {
            return this.entries.stream().map(McFunctionUtils.wrap(this::map))
                    .map(stat -> stat.getPlayerStat(playerUuid, statistic))
                    .reduce((Long) null, (a, b) -> {
                        if (a == null) return b;
                        if (b == null) return a;
                        return Math.min(a, b);
                    });
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }
    
    @Override
    public Long getStatistic(UUID playerUuid, PlayerStatisticId statistic) throws McException
    {
        try
        {
            return this.entries.stream().map(McFunctionUtils.wrap(this::map))
                    .map(stat -> stat.getPlayerStat(playerUuid, statistic))
                    .reduce((Long) null, (a, b) -> {
                        if (a == -1) return b;
                        if (b == -1) return a;
                        return a + b;
                    });
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }
    
    @Override
    public int getPlayerCount(PlayerStatisticId statistic) throws McException
    {
        return getPlayers(statistic).size();
    }

    /**
     * @param statistic
     * @return players using given statistics
     * @throws McException
     */
    private Set<UUID> getPlayers(PlayerStatisticId statistic) throws McException
    {
        try
        {
            final Set<UUID> result = new HashSet<>();
            this.entries.stream().map(McFunctionUtils.wrap(this::map))
                    .forEach(stat -> stat.getPlayers(0, Integer.MAX_VALUE).forEach(pstat -> {
                        if (pstat.getStatistic(statistic) != null)
                        {
                            result.add(pstat.getPlayerUUID());
                        }
                    }));
            return result;
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }
    
    @Override
    public int getPlace(UUID player, PlayerStatisticId statistic, boolean ascending) throws McException
    {
        final Long value = this.getStatistic(player, statistic);
        int place = 1;
        for (final UUID p : this.getPlayers(statistic))
        {
            final Long v2 = this.getStatistic(p, statistic);
            if (ascending && v2 < value)
            {
                place++;
            }
            else if (!ascending && v2 > value)
            {
                place++;
            }
        }
        return place;
    }
    
    /**
     * statistic helper.
     * @param <T> key class
     */
    private static final class StatHelper<T>
    {
        /** the key. */
        T key;
        /** the value. */
        Long value;
        /**
         * @param key
         * @param value
         */
        public StatHelper(T key, Long value)
        {
            this.key = key;
            this.value = value;
        }
    }
    
    @Override
    public List<UUID> getPlayerLeaders(PlayerStatisticId statistic, int start, int limit, boolean ascending) throws McException
    {
        final TreeSet<StatHelper<UUID>> board = new TreeSet<>((a, b) -> {
            int result = a.value.compareTo(b.value);
            if (result == 0)
            {
                result = a.key.compareTo(b.key);
            }
            return ascending ? result : 0-result;
        });
        for (final UUID p : this.getPlayers(statistic))
        {
            final Long v2 = this.getStatistic(p, statistic);
            board.add(new StatHelper<>(p, v2));
        }
        return board.stream().skip(start).limit(limit).map(s -> s.key).collect(Collectors.toList());
    }
    
    @Override
    public Long getBestValue(TeamIdType team, TeamStatisticId statistic) throws McException
    {
        try
        {
            return this.entries.stream().map(McFunctionUtils.wrap(this::map))
                    .map(stat -> stat.getTeamStat(team, statistic))
                    .reduce((Long) null, (a, b) -> {
                        if (a == null) return b;
                        if (b == null) return a;
                        return Math.max(a, b);
                    });
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }
    
    @Override
    public Long getWorstValue(TeamIdType team, TeamStatisticId statistic) throws McException
    {
        try
        {
            return this.entries.stream().map(McFunctionUtils.wrap(this::map))
                    .map(stat -> stat.getTeamStat(team, statistic))
                    .reduce((Long) null, (a, b) -> {
                        if (a == null) return b;
                        if (b == null) return a;
                        return Math.min(a, b);
                    });
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }
    
    @Override
    public Long getStatistic(TeamIdType team, TeamStatisticId statistic) throws McException
    {
        try
        {
            return this.entries.stream().map(McFunctionUtils.wrap(this::map))
                    .map(stat -> stat.getTeamStat(team, statistic))
                    .reduce((Long) null, (a, b) -> {
                        if (a == -1) return b;
                        if (b == -1) return a;
                        return a + b;
                    });
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }
    
    @Override
    public int getTeamCount(TeamStatisticId statistic) throws McException
    {
        return getTeams(statistic).size();
    }

    /**
     * @param statistic
     * @return teams using given statistics
     * @throws McException
     */
    private Set<TeamIdType> getTeams(TeamStatisticId statistic) throws McException
    {
        try
        {
            final Set<TeamIdType> result = new HashSet<>();
            this.entries.stream().map(McFunctionUtils.wrap(this::map))
                    .forEach(stat -> stat.getTeams(0, Integer.MAX_VALUE).forEach(tstat -> {
                        if (tstat.getStatistic(statistic) != null)
                        {
                            result.add(tstat.getTeamId());
                        }
                    }));
            return result;
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }
    
    @Override
    public int getPlace(TeamIdType team, TeamStatisticId statistic, boolean ascending) throws McException
    {
        final Long value = this.getStatistic(team, statistic);
        int place = 1;
        for (final TeamIdType p : this.getTeams(statistic))
        {
            final Long v2 = this.getStatistic(p, statistic);
            if (ascending && v2 < value)
            {
                place++;
            }
            else if (!ascending && v2 > value)
            {
                place++;
            }
        }
        return place;
    }
    
    @Override
    public List<TeamIdType> getTeamLeaders(TeamStatisticId statistic, int start, int limit, boolean ascending) throws McException
    {
        final TreeSet<StatHelper<TeamIdType>> board = new TreeSet<>((a, b) -> {
            int result = a.value.compareTo(b.value);
            if (result == 0)
            {
                result = (a.key.getPluginName() + ":" + a.key.name()).compareTo(b.key.getPluginName() + ":" + b.key.name()); //$NON-NLS-1$ //$NON-NLS-2$
            }
            return ascending ? result : 0-result;
        });
        for (final TeamIdType p : this.getTeams(statistic))
        {
            final Long v2 = this.getStatistic(p, statistic);
            board.add(new StatHelper<>(p, v2));
        }
        return board.stream().skip(start).limit(limit).map(s -> s.key).collect(Collectors.toList());
    }
    
    @Override
    public Long getStatistic(GameStatisticId statistic) throws McException
    {
        try
        {
            return this.entries.stream().map(McFunctionUtils.wrap(this::map))
                    .map(stat -> stat.getStatistic(statistic))
                    .reduce((Long) null, (a, b) -> {
                        if (a == -1) return b;
                        if (b == -1) return a;
                        return a + b;
                    });
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }

    @Override
    public Long getBestValue(GameStatisticId statistic) throws McException
    {
        try
        {
            return this.entries.stream().map(McFunctionUtils.wrap(this::map))
                    .map(stat -> stat.getStatistic(statistic))
                    .reduce((Long) null, (a, b) -> {
                        if (a == null) return b;
                        if (b == null) return a;
                        return Math.max(a, b);
                    });
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }

    @Override
    public Long getWorstValue(GameStatisticId statistic) throws McException
    {
        try
        {
            return this.entries.stream().map(McFunctionUtils.wrap(this::map))
                    .map(stat -> stat.getStatistic(statistic))
                    .reduce((Long) null, (a, b) -> {
                        if (a == null) return b;
                        if (b == null) return a;
                        return Math.min(a, b);
                    });
        }
        catch (McFunctionUtils.WrappedException ex)
        {
            throw (McException) ex.getCause();
        }
    }
    
}
