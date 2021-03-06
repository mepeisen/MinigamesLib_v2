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

import java.util.UUID;

import de.minigameslib.mclib.api.McException;

/**
 * Interface for classes rule sets that define selectable rule sets.
 * 
 * @author mepeisen
 */
public interface ClassRuleSetSelectableInterface extends ClassRuleSetInterface
{
    
    /**
     * Checks if player can select the class, f.e. if he has enough money.
     * 
     * If multiple rules are applied to class and at least one rule throws McException,
     * the class cannot be chosen by player.
     * 
     * Notice: The default class is always be selectable to players.
     * 
     * @param player
     * @throws McException thrown if player does not have enough money.
     */
    void checkSelection(UUID player) throws McException;
    
}
