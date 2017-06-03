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

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.stat.StatisticInterface;
import de.minigameslib.mgapi.api.stat.StatisticServiceInterface;

/**
 * Statistic service implementation for storing into file system.
 * 
 * @author mepeisen
 */
public class FileStatisticServiceImpl implements StatisticServiceInterface
{
    
    /**
     * folder to store statistics.
     */
    protected File folder;
    
    /**
     * the registry file.
     */
    private File registryFile;

    /**
     * file statistics registry.
     */
    private FileStatRegistry statRegistry;
    
    // TODO put maximumSize to config.
    // TODO put expireAfterAccess to config.
    /**
     * file cache.
     */
    private LoadingCache<FileStatRegistryEntry, FileMatchStatisticImpl> fileCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<FileStatRegistryEntry, FileMatchStatisticImpl>() {
                @Override
                public FileMatchStatisticImpl load(FileStatRegistryEntry key) throws Exception
                {
                    final File file = new File(
                            FileStatisticServiceImpl.this.folder,
                            key.getMatchDate().getYear() + "/" + key.getMatchDate().getMonthValue() + "/" + key.getMatchDate().getDayOfMonth() + "/" + key.getMatchId() + ".yml"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    final FileMatchStatisticImpl result = new FileMatchStatisticImpl();
                    result.read(McLibInterface.instance().readYmlFile(file));
                    return result;
                }
            });

    /**
     * Statistics
     * @param folder
     */
    public FileStatisticServiceImpl(File folder)
    {
        this.folder = folder;
        if (!this.folder.exists())
        {
            this.folder.mkdirs();
        }
        this.registryFile = new File(this.folder, "statregistry.yml"); //$NON-NLS-1$
        this.statRegistry = new FileStatRegistry();
        if (this.registryFile.exists())
        {
            try
            {
                this.statRegistry.read(McLibInterface.instance().readYmlFile(this.registryFile));
            }
            catch (IOException e)
            {
                // TODO logging
            }
        }
    }
    
    @Override
    public StatisticInterface getAllTimeStatistic(ArenaInterface arena)
    {
        return new FileStatisticImpl(
                this.fileCache,
                this.statRegistry.getEntries().stream()
                    .filter(p -> p.getArenaName().equals(arena.getInternalName()) && p.getArenaType().equals(arena.getType().name()) && p.getArenaPlugin().equals(arena.getType().getPluginName()))
                    .collect(Collectors.toList())
                );
    }
    
    @Override
    public StatisticInterface getMonthlyStatistic(ArenaInterface arena, YearMonth month)
    {
        final List<FileStatRegistryEntry> entries = new ArrayList<>();
        final TreeMap<MonthDay, TreeSet<FileStatRegistryEntry>> map = this.statRegistry.getEntriesByDate().get(month);
        if (map != null)
        {
            map.values().forEach(m -> m.stream()
                    .filter(p -> p.getArenaName().equals(arena.getInternalName()) && p.getArenaType().equals(arena.getType().name()) && p.getArenaPlugin().equals(arena.getType().getPluginName()))
                    .forEach(entries::add));
        }
        return new FileStatisticImpl(this.fileCache, entries);
    }
    
    @Override
    public StatisticInterface getDailyStatistic(ArenaInterface arena, LocalDate date)
    {
        final List<FileStatRegistryEntry> entries = new ArrayList<>();
        final TreeMap<MonthDay, TreeSet<FileStatRegistryEntry>> map = this.statRegistry.getEntriesByDate().get(YearMonth.from(date));
        if (map != null)
        {
            final TreeSet<FileStatRegistryEntry> set = map.get(MonthDay.from(date));
            if (set != null)
            {
                set.stream()
                    .filter(p -> p.getArenaName().equals(arena.getInternalName()) && p.getArenaType().equals(arena.getType().name()) && p.getArenaPlugin().equals(arena.getType().getPluginName()))
                    .forEach(entries::add);
            }
        }
        return new FileStatisticImpl(this.fileCache, entries);
    }
    
    @Override
    public StatisticInterface getRecentStatistic(ArenaInterface arena, int count)
    {
        final List<FileStatRegistryEntry> entries = new ArrayList<>();
        final TreeMap<YearMonth, TreeMap<MonthDay, TreeSet<FileStatRegistryEntry>>> map = this.statRegistry.getEntriesByDate();
        Map.Entry<YearMonth, TreeMap<MonthDay, TreeSet<FileStatRegistryEntry>>> month = map.lastEntry();
        while (month != null && count < entries.size())
        {
            Map.Entry<MonthDay, TreeSet<FileStatRegistryEntry>> day = month.getValue().lastEntry();
            while (day != null && count < entries.size())
            {
                FileStatRegistryEntry p = day.getValue().last();
                while (p != null && count < entries.size())
                {
                    if (p.getArenaName().equals(arena.getInternalName()) && p.getArenaType().equals(arena.getType().name()) && p.getArenaPlugin().equals(arena.getType().getPluginName()))
                    {
                        entries.add(p);
                    }
                    p = day.getValue().lower(p);
                }
                day = month.getValue().lowerEntry(day.getKey());
            }
            month = map.lowerEntry(month.getKey());
        }
        return new FileStatisticImpl(this.fileCache, entries);
    }
    
    @Override
    public StatisticInterface getAllTimeStatistic(ArenaTypeInterface type)
    {
        return new FileStatisticImpl(
                this.fileCache,
                this.statRegistry.getEntries().stream()
                    .filter(p -> p.getArenaType().equals(type.name()) && p.getArenaPlugin().equals(type.getPluginName()))
                    .collect(Collectors.toList())
                );
    }
    
    @Override
    public StatisticInterface getMonthlyStatistic(ArenaTypeInterface type, YearMonth month)
    {
        final List<FileStatRegistryEntry> entries = new ArrayList<>();
        final TreeMap<MonthDay, TreeSet<FileStatRegistryEntry>> map = this.statRegistry.getEntriesByDate().get(month);
        if (map != null)
        {
            map.values().forEach(m -> m.stream()
                    .filter(p -> p.getArenaType().equals(type.name()) && p.getArenaPlugin().equals(type.getPluginName()))
                    .forEach(entries::add));
        }
        return new FileStatisticImpl(this.fileCache, entries);
    }
    
    @Override
    public StatisticInterface getDailyStatistic(ArenaTypeInterface type, LocalDate date)
    {
        final List<FileStatRegistryEntry> entries = new ArrayList<>();
        final TreeMap<MonthDay, TreeSet<FileStatRegistryEntry>> map = this.statRegistry.getEntriesByDate().get(YearMonth.from(date));
        if (map != null)
        {
            final TreeSet<FileStatRegistryEntry> set = map.get(MonthDay.from(date));
            if (set != null)
            {
                set.stream()
                    .filter(p -> p.getArenaType().equals(type.name()) && p.getArenaPlugin().equals(type.getPluginName()))
                    .forEach(entries::add);
            }
        }
        return new FileStatisticImpl(this.fileCache, entries);
    }
    
    @Override
    public StatisticInterface getRecentStatistic(ArenaTypeInterface type, int count)
    {
        final List<FileStatRegistryEntry> entries = new ArrayList<>();
        final TreeMap<YearMonth, TreeMap<MonthDay, TreeSet<FileStatRegistryEntry>>> map = this.statRegistry.getEntriesByDate();
        Map.Entry<YearMonth, TreeMap<MonthDay, TreeSet<FileStatRegistryEntry>>> month = map.lastEntry();
        while (month != null && count < entries.size())
        {
            Map.Entry<MonthDay, TreeSet<FileStatRegistryEntry>> day = month.getValue().lastEntry();
            while (day != null && count < entries.size())
            {
                FileStatRegistryEntry p = day.getValue().last();
                while (p != null && count < entries.size())
                {
                    if (p.getArenaType().equals(type.name()) && p.getArenaPlugin().equals(type.getPluginName()))
                    {
                        entries.add(p);
                    }
                    p = day.getValue().lower(p);
                }
                day = month.getValue().lowerEntry(day.getKey());
            }
            month = map.lowerEntry(month.getKey());
        }
        return new FileStatisticImpl(this.fileCache, entries);
    }
    
}
