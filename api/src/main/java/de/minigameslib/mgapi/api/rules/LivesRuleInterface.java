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

package de.minigameslib.mgapi.api.rules;

import de.minigameslib.mclib.api.McException;

/**
 * @author mepeisen
 *
 */
public interface LivesRuleInterface
{
    
    /**
     * Returns the number of lives before losing.
     * 
     * @return number of lives before losing.
     */
    int getLives();
    
    /**
     * sets the number of lives before losing.
     * 
     * @param lives
     *            number of lives
     * @throws McException
     *             thrown if arena is in wrong state
     */
    void setLives(int lives) throws McException;
    
}
