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
import java.util.UUID;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * A single entries of game statistics within registry.
 * 
 * @author mepeisen
 */
public class FileStatRegistryEntry extends AnnotatedDataFragment implements Comparable<FileStatRegistryEntry>
{
    
    /**
     * Date of the match (finish date).
     */
    @PersistentField
    protected LocalDateTime matchDate;
    
    /**
     * arena plugin name.
     */
    @PersistentField
    protected String arenaPlugin;
    
    /**
     * arena type name.
     */
    @PersistentField
    protected String arenaType;
    
    /**
     * arena name.
     */
    @PersistentField
    protected String arenaName;
    
    /**
     * Unique id to identify the match.
     */
    @PersistentField
    protected UUID matchId;

    /**
     * Constructor.
     */
    public FileStatRegistryEntry()
    {
        super();
    }

    /**
     * Constructor.
     * @param matchDate
     * @param arenaPlugin
     * @param arenaType
     * @param arenaName
     */
    public FileStatRegistryEntry(LocalDateTime matchDate, String arenaPlugin, String arenaType, String arenaName)
    {
        this.matchDate = matchDate;
        this.arenaPlugin = arenaPlugin;
        this.arenaType = arenaType;
        this.arenaName = arenaName;
        this.matchId = UUID.randomUUID();
    }

    @Override
    public int compareTo(FileStatRegistryEntry o)
    {
        int result = this.matchDate.compareTo(o.matchDate);
        if (result == 0)
        {
            result = this.matchId.compareTo(o.matchId);
        }
        return result;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.matchDate == null) ? 0 : this.matchDate.hashCode());
        result = prime * result + ((this.matchId == null) ? 0 : this.matchId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileStatRegistryEntry other = (FileStatRegistryEntry) obj;
        if (this.matchDate == null)
        {
            if (other.matchDate != null)
                return false;
        }
        else if (!this.matchDate.equals(other.matchDate))
            return false;
        if (this.matchId == null)
        {
            if (other.matchId != null)
                return false;
        }
        else if (!this.matchId.equals(other.matchId))
            return false;
        return true;
    }

    /**
     * @return the matchDate
     */
    public LocalDateTime getMatchDate()
    {
        return this.matchDate;
    }

    /**
     * @return the arenaPlugin
     */
    public String getArenaPlugin()
    {
        return this.arenaPlugin;
    }

    /**
     * @return the arenaType
     */
    public String getArenaType()
    {
        return this.arenaType;
    }

    /**
     * @return the arenaName
     */
    public String getArenaName()
    {
        return this.arenaName;
    }

    /**
     * @return the matchId
     */
    public UUID getMatchId()
    {
        return this.matchId;
    }
    
}
