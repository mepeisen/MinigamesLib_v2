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

package de.minigameslib.mgapi.api.obj;

import java.util.Collection;

import de.minigameslib.mclib.api.McException;

/**
 * A join sign.
 * 
 * @author mepeisen
 */
public interface JoinSignHandler extends ArenaSignHandler
{

    /**
     * Returns the lines.
     * @return lines.
     */
    Collection<LineConfig> getLines();
    
    /**
     * Sets the line config with given state in {@link LineConfig#getState()}.
     * @param config config
     * @throws McException thrown if there are problems saving the config 
     */
    void setLine(LineConfig config) throws McException;
    
}
