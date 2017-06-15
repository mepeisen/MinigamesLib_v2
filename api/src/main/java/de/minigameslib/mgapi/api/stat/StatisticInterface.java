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

package de.minigameslib.mgapi.api.stat;

import java.util.List;
import java.util.UUID;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * Interface to access statistics.
 * 
 * @author mepeisen
 */
public interface StatisticInterface
{
    
    // matches
    
    /**
     * Returns the number of matches associated with this statistics.
     * 
     * @return number of matches.
     */
    int getMatchCount();
    
    /**
     * Returns the matches associated with this statistics.
     * 
     * @param start
     *            start index
     * @param limit
     *            limit
     * @return list of match statistics
     * @throws McException thrown if there are problems fetching the data.
     */
    List<MatchStatisticInterface> getMatches(int start, int limit) throws McException;
    
    // player statistics
    
    /**
     * Returns the best place in one match for given player.
     * 
     * @param playerUuid
     *            player uuid.
     * @return best place; {@code -1} if player did not play a match
     * @throws McException thrown if there are problems fetching the data.
     */
    int getBestPlace(UUID playerUuid) throws McException;
    
    /**
     * Returns the best value in one match for given player.
     * 
     * @param playerUuid
     *            player uuid.
     * @param statistic
     * @return best value; {@code null} if player was not playing
     * @throws McException thrown if there are problems fetching the data.
     */
    Long getBestValue(UUID playerUuid, PlayerStatisticId statistic) throws McException;
    
    /**
     * Returns the best value in one match for given player.
     * 
     * @param playerUuid
     *            player uuid.
     * @param statistic
     * @return worst value; {@code null} if player was not playing
     * @throws McException thrown if there are problems fetching the data.
     */
    Long getWorstValue(UUID playerUuid, PlayerStatisticId statistic) throws McException;
    
    /**
     * Statistic function by adding all player values.
     * 
     * @param playerUuid
     * @param statistic
     * @return current statistic; {@code null} if player was not playing
     * @throws McException thrown if there are problems fetching the data.
     */
    Long getStatistic(UUID playerUuid, PlayerStatisticId statistic) throws McException;
    
    /**
     * Returns the number of players having statistics.
     * 
     * @param statistic
     * @return number of players
     * @throws McException thrown if there are problems fetching the data.
     */
    int getPlayerCount(PlayerStatisticId statistic) throws McException;
    
    /**
     * Returns the place of given player with given statistics.
     * 
     * @param player
     *            player
     * @param statistic
     * @param ascending
     *            {@code true} for returning the players with less points at first.
     * @return place (starting with 1); -1 for unknown place
     * @throws McException thrown if there are problems fetching the data.
     */
    int getPlace(UUID player, PlayerStatisticId statistic, boolean ascending) throws McException;
    
    /**
     * Returns the players by position
     * 
     * @param statistic
     *            statistic function
     * @param start
     *            starting index (starts with 0)
     * @param limit
     *            maximum limit to return
     * @param ascending
     *            {@code true} for returning the players with less points at first.
     * @return player list in order
     * @throws McException thrown if there are problems fetching the data.
     */
    List<UUID> getPlayerLeaders(PlayerStatisticId statistic, int start, int limit, boolean ascending) throws McException;
    
    // team statistics
    
    /**
     * Returns the best value in one match for given team.
     * 
     * @param team
     * @param statistic
     * @return best value; {@code null} if team was not playing
     * @throws McException thrown if there are problems fetching the data.
     */
    Long getBestValue(TeamIdType team, TeamStatisticId statistic) throws McException;
    
    /**
     * Returns the best value in one match for given team.
     * 
     * @param team
     * @param statistic
     * @return worst value; {@code null} if team was not playing
     * @throws McException thrown if there are problems fetching the data.
     */
    Long getWorstValue(TeamIdType team, TeamStatisticId statistic) throws McException;
    
    /**
     * Statistic function by adding all player values.
     * 
     * @param team
     * @param statistic
     * @return current statistic; {@code null} if team was not playing
     * @throws McException thrown if there are problems fetching the data.
     */
    Long getStatistic(TeamIdType team, TeamStatisticId statistic) throws McException;
    
    /**
     * Returns the number of teams having statistics.
     * 
     * @param statistic
     * @return number of teams
     * @throws McException thrown if there are problems fetching the data.
     */
    int getTeamCount(TeamStatisticId statistic) throws McException;
    
    /**
     * Returns the place of given team with given statistics.
     * 
     * @param team
     *            team
     * @param statistic
     * @param ascending
     *            {@code true} for returning the teams with less points at first.
     * @return place (starting with 1); -1 for unknown place
     * @throws McException thrown if there are problems fetching the data.
     */
    int getPlace(TeamIdType team, TeamStatisticId statistic, boolean ascending) throws McException;
    
    /**
     * Returns the teams by position
     * 
     * @param statistic
     *            statistic function
     * @param start
     *            starting index (starts with 0)
     * @param limit
     *            maximum limit to return
     * @param ascending
     *            {@code true} for returning the teams with less points at first.
     * @return team list in order
     * @throws McException thrown if there are problems fetching the data.
     */
    List<TeamIdType> getTeamLeaders(TeamStatisticId statistic, int start, int limit, boolean ascending) throws McException;
    
    // game statistics
    
    /**
     * Statistic function by adding all values.
     * 
     * @param statistic
     * @return current statistic; {@code null} if statistic was not used
     * @throws McException thrown if there are problems fetching the data.
     */
    Long getStatistic(GameStatisticId statistic) throws McException;
    
    /**
     * Returns the best value in one match.
     * 
     * @param statistic
     * @return current statistic; {@code null} if statistic was not used
     * @throws McException thrown if there are problems fetching the data.
     */
    Long getBestValue(GameStatisticId statistic) throws McException;
    
    /**
     * Returns the worst value in one match for given team.
     * 
     * @param statistic
     * @return current statistic; {@code null} if statistic was not used
     * @throws McException thrown if there are problems fetching the data.
     */
    Long getWorstValue(GameStatisticId statistic) throws McException;
    
}
