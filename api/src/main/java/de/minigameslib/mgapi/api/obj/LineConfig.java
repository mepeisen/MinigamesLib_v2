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

import de.minigameslib.mclib.api.locale.LocalizedConfigLine;
import de.minigameslib.mclib.shared.api.com.PersistentField;
import de.minigameslib.mgapi.api.arena.ArenaState;

/**
 * the line config.
 * 
 * @author mepeisen
 */
public class LineConfig extends LocalizedConfigLine
{
    
    /**
     * serial version uid.
     */
    private static final long serialVersionUID = 8085073904696520257L;
    
    /**
     * the arena state this line is used for.
     */
    @PersistentField
    protected ArenaState state = ArenaState.Booting;

    /**
     * @return the state
     */
    public ArenaState getState()
    {
        return this.state;
    }

    /**
     * @param state the state to set
     */
    public void setState(ArenaState state)
    {
        this.state = state;
    }
    
}
