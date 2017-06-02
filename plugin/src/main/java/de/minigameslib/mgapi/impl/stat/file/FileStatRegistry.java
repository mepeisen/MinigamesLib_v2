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

import java.time.MonthDay;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Central registry for file statistics.
 * 
 * @author mepeisen
 */
public class FileStatRegistry extends AnnotatedDataFragment
{
    
    /**
     * Registry entries for file statistics.
     */
    @PersistentField
    protected List<FileStatRegistryEntry> entries = new ArrayList<>();
    
    /**
     * Sorted list of entries by date.
     */
    protected final TreeMap<YearMonth, TreeMap<MonthDay, TreeSet<FileStatRegistryEntry>>> entriesByDate = new TreeMap<>();

    @Override
    public void read(DataSection section)
    {
        super.read(section);
        // after reading rebuild the entries by date.
        this.entriesByDate.clear();
        for (final FileStatRegistryEntry entry : this.entries)
        {
            addEntryToEntriesByDate(entry);
        }
    }
    
    /**
     * adds a new entry.
     * @param entry
     */
    public void addEntry(FileStatRegistryEntry entry)
    {
        this.entries.add(entry);

        addEntryToEntriesByDate(entry);
    }

    /**
     * @param entry
     */
    private void addEntryToEntriesByDate(FileStatRegistryEntry entry)
    {
        final YearMonth month = YearMonth.from(entry.matchDate.toLocalDate());
        final MonthDay day = MonthDay.from(entry.matchDate.toLocalDate());
        this.entriesByDate.computeIfAbsent(month, m -> new TreeMap<>()).computeIfAbsent(day, d -> new TreeSet<>()).add(entry);
    }

    /**
     * @return the entriesByDate
     */
    public TreeMap<YearMonth, TreeMap<MonthDay, TreeSet<FileStatRegistryEntry>>> getEntriesByDate()
    {
        return this.entriesByDate;
    }

    /**
     * @return the entries
     */
    public List<FileStatRegistryEntry> getEntries()
    {
        return this.entries;
    }
    
}
