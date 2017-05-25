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
     */
    List<MatchStatisticInterface> getMatches(int start, int limit);
    
    // player statistics
    
    /**
     * Returns the best place in one match for given player.
     * 
     * @param playerUuid
     *            player uuid.
     * @return best place.
     */
    int getBestPlace(UUID playerUuid);
    
    /**
     * Returns the best value in one match for given player.
     * 
     * @param playerUuid
     *            player uuid.
     * @param statistic
     * @return best value.
     */
    long getBestValue(UUID playerUuid, PlayerStatisticId statistic);
    
    /**
     * Returns the best value in one match for given player.
     * 
     * @param playerUuid
     *            player uuid.
     * @param statistic
     * @return worst value.
     */
    long getWorstValue(UUID playerUuid, PlayerStatisticId statistic);
    
    /**
     * Statistic function
     * 
     * @param playerUuid
     * @param statistic
     * @return current statistic
     */
    long getStatistic(UUID playerUuid, PlayerStatisticId statistic);
    
    /**
     * Returns the number of players having statistics.
     * 
     * @param statistic
     * @return number of players
     */
    int getPlayerCount(PlayerStatisticId statistic);
    
    /**
     * Returns the place of given player with given statistics.
     * 
     * @param player
     *            player
     * @param statistic
     * @param ascending
     *            {@true} for returning the players with less points at first.
     * @return place (starting with 1); -1 for unknown place
     */
    int getPlace(UUID player, PlayerStatisticId statistic, boolean ascending);
    
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
     *            {@true} for returning the players with less points at first.
     * @return player list in order
     */
    List<UUID> getPlayerLeaders(PlayerStatisticId statistic, int start, int limit, boolean ascending);
    
    // team statistics
    
    /**
     * Returns the best value in one match for given team.
     * 
     * @param team
     * @param statistic
     * @return best value.
     */
    long getBestValue(TeamIdType team, PlayerStatisticId statistic);
    
    /**
     * Returns the best value in one match for given team.
     * 
     * @param team
     * @param statistic
     * @return worst value.
     */
    long getWorstValue(TeamIdType team, PlayerStatisticId statistic);
    
    /**
     * Statistic function
     * 
     * @param team
     * @param statistic
     * @return current statistic
     */
    long getStatistic(TeamIdType team, TeamStatisticId statistic);
    
    /**
     * Returns the number of teams having statistics.
     * 
     * @param statistic
     * @return number of teams
     */
    int getTeamCount(TeamStatisticId statistic);
    
    /**
     * Returns the place of given team with given statistics.
     * 
     * @param team
     *            team
     * @param statistic
     * @param ascending
     *            {@true} for returning the teams with less points at first.
     * @return place (starting with 1); -1 for unknown place
     */
    int getPlace(TeamIdType team, TeamStatisticId statistic, boolean ascending);
    
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
     *            {@true} for returning the teams with less points at first.
     * @return team list in order
     */
    List<TeamIdType> getTeamLeaders(TeamStatisticId statistic, int start, int limit, boolean ascending);
    
    // game statistics
    
    /**
     * Statistic function
     * 
     * @param statistic
     * @return current statistic
     */
    long getStatistic(GameStatisticId statistic);
    
}
