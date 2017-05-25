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

package de.minigameslib.mgapi.api.match;

import java.util.Collection;
import java.util.UUID;

/**
 * Match result interface.
 * 
 * Each invocation of {@link ArenaMatchInterface#setWinner(UUID...)} or {@link ArenaMatchInterface#setLoser(UUID...)} will create a new place
 * and match result. The first winner will be on place #1 followed by later on winners and the first loser will be on last place preceded by
 * later losers.
 */
public interface MatchResult
{
    
    /**
     * Returns the place number
     * @return the place starting with 1 for the best winner
     */
    int getPlace();
    
    /**
     * Players sharing this place
     * @return the players sharing this place
     */
    Collection<UUID> getPlayers();
    
    /**
     * Returns {@code true} for a winning place
     * @return {@code true} for winning place.
     */
    boolean isWin();
    
}