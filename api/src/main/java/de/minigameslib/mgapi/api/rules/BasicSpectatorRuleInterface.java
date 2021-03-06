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
public interface BasicSpectatorRuleInterface extends ArenaRuleSetInterface
{
    
    /**
     * Is spectating without pending match allowed?
     * @return Is spectating without pending match allowed?
     */
    boolean isSpectatingWithoutMatch();
    
    /**
     * Is free spectating within pending match allowed?
     * @return Is free spectating within pending match allowed?
     */
    boolean isFreeSpectatingWithinMatch();
    
    /**
     * Losing and winning players join spectators?
     * @return Losing and winning players join spectators?
     */
    boolean isSpectatingAfterWinOrLose();

    /**
     * Sets the spectating without match flag
     * @param flag
     * @throws McException thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setSpectatingWithoutMatch(boolean flag) throws McException;

    /**
     * Sets the free spectating within match flag
     * @param flag
     * @throws McException thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setFreeSpectatingWithinMatch(boolean flag) throws McException;

    /**
     * Sets the spatating after winning or losing flag
     * @param flag
     * @throws McException thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setSpectatingAfterWinOrLose(boolean flag) throws McException;

}
