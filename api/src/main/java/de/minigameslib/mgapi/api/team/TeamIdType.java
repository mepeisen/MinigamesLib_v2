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

package de.minigameslib.mgapi.api.team;

import de.minigameslib.mclib.api.enums.McUniqueEnumInterface;
import de.minigameslib.mgapi.api.MinigamesLibInterface;

/**
 * Use this interface on ernumerations to declare team types.
 * 
 * @author mepeisen
 */
public interface TeamIdType extends McUniqueEnumInterface
{
    
    /**
     * Checks if this team is a special team.
     * A "special" team is a team not being a real party on team arenas. "special" teams cannnot be confiugured as teams on arenas.
     * Instead they are used for special features on arenas, for example the "Winners" and "Spectators" have their own team.
     * @return {@code true} for special teams
     */
    default boolean isSpecial()
    {
        return MinigamesLibInterface.instance().isSpecialTeam(this);
    }
    
}
