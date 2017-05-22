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

import org.bukkit.Sound;

import de.minigameslib.mclib.api.McException;

/**
 * @author mepeisen
 *
 */
public interface BasicMatchRuleInterface extends ArenaRuleSetInterface
{
    
    /**
     * Returns the minimum players when the match countdown starts
     * 
     * @return the minPlayers
     */
    int getMinPlayers();
    
    /**
     * Returns the maximum players allowed in a match
     * 
     * @return the maxPlayers
     */
    int getMaxPlayers();
    
    /**
     * Returns the lobby countdown in seconds
     * 
     * @return the lobbyCountdown
     */
    int getLobbyCountdown();
    
    /**
     * Returns the sound to play for lobby countdown.
     * 
     * @return sound to play for lobby countdown.
     */
    Sound getLobbyCountdownSound();
    
    /**
     * Returns the flag to play for lobby countdown.
     * 
     * @return flag to play for lobby countdown.
     */
    boolean isPlayLobbyCountdownSound();
    
    /**
     * Returns the pre match countdown in seconds
     * 
     * @return the preMatchCountdown
     */
    int getPreMatchCountdown();
    
    /**
     * Returns the sound to play for pre match countdown.
     * 
     * @return sound to play for pre match countdown.
     */
    Sound getPreMatchCountdownSound();
    
    /**
     * Returns the flag to play for pre match countdown.
     * 
     * @return flag to play for pre match countdown.
     */
    boolean isPlayPreMatchCountdownSound();
    
    /**
     * Sets the min and max players
     * 
     * @param minPlayers
     * @param maxPlayers
     * @throws McException
     *             thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setPlayers(int minPlayers, int maxPlayers) throws McException;
    
    /**
     * Sets the lobby countdown
     * 
     * @param seconds
     * @throws McException
     *             thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setLobbyCountdown(int seconds) throws McException;
    
    /**
     * Sets the sound to play for lobby countdown.
     * 
     * @param sound
     *            sound to play for lobby countdown.
     * @throws McException
     *             thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setLobbyCountdownSound(Sound sound) throws McException;
    
    /**
     * Returns the flag to play for lobby countdown.
     * 
     * @param flag
     *            flag to play for lobby countdown.
     * @throws McException
     *             thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setPlayLobbyCountdownSound(boolean flag) throws McException;
    
    /**
     * Sets the pre match countdown
     * 
     * @param seconds
     * @throws McException
     *             thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setPreMatchCountdown(int seconds) throws McException;
    
    /**
     * Sets the sound to play for pre match countdown.
     * 
     * @param sound
     *            sound to play for pre match countdown.
     * @throws McException
     *             thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setPreMatchCountdownSound(Sound sound) throws McException;
    
    /**
     * Returns the flag to play for pre match countdown.
     * 
     * @param flag
     *            flag to play for pre match countdown.
     * @throws McException
     *             thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setPreMatchLobbyCountdownSound(boolean flag) throws McException;
    
    /**
     * The initial movement speed.
     * 
     * @return initial movement speed.
     */
    float getMovementSpeed();
    
    /**
     * The initial fly speed.
     * 
     * @return initial fly speed.
     */
    float getFlySpeed();
    
    /**
     * The initial max health.
     * 
     * @return max health.
     */
    double getMaxHealth();
    
    /**
     * The initial health.
     * 
     * @return health.
     */
    double getHealth();
    
    /**
     * Sets initial movement speed.
     * 
     * @param speed
     * @throws McException
     *             thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setMovementSpeed(float speed) throws McException;
    
    /**
     * Sets initial fly speed.
     * 
     * @param speed
     * @throws McException
     *             thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setFlySpeed(float speed) throws McException;
    
    /**
     * Sets initial max health.
     * 
     * @param maxHealth
     * @throws McException
     *             thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setMaxHealth(double maxHealth) throws McException;
    
    /**
     * Sets initial health.
     * 
     * @param health
     * @throws McException
     *             thrown if the config is invalid or if arena is not in maintenance mode
     */
    void setHealth(double health) throws McException;
    
}
