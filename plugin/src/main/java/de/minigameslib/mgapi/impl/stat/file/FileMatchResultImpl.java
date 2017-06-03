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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;
import de.minigameslib.mgapi.api.match.MatchResult;

/**
 * @author mepeisen
 *
 */
public class FileMatchResultImpl extends AnnotatedDataFragment implements MatchResult
{
    
    /**
     * the place.
     */
    @PersistentField
    protected int place;
    
    /**
     * the players.
     */
    @PersistentField
    protected List<UUID> players = new ArrayList<>();
    
    /**
     * winning flag.
     */
    @PersistentField
    protected boolean isWin;
    
    @Override
    public int getPlace()
    {
        return this.place;
    }
    
    @Override
    public Collection<UUID> getPlayers()
    {
        return new ArrayList<>(this.players);
    }
    
    @Override
    public boolean isWin()
    {
        return this.isWin;
    }
    
}
