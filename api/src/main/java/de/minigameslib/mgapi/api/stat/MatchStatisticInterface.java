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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import de.minigameslib.mgapi.api.match.MatchResult;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * Match statistics
 * 
 * @author mepeisen
 */
public interface MatchStatisticInterface
{
    
    /**
     * Returns the match started timestamp
     * 
     * @return match started timestamp; {@code null} for non-started matches.
     */
    LocalDateTime getStarted();
    
    /**
     * Returns the match finished timestamp
     * 
     * @return match finished timestamp; {@code null} if the match did not finish
     */
    LocalDateTime getFinished();
    
    /**
     * Returns the amount of players participating in this match.
     * 
     * @return amount of players.
     */
    int getPlayerCount();
    
    /**
     * Returns the match statistics for given player.
     * 
     * @param uuid
     *            uuid.
     * @return match statistics or {@code null} if player is unknown.
     */
    MatchStatisticPlayerInterface getPlayer(UUID uuid);
    
    /**
     * Returns the players participating in this match.
     * 
     * @param start
     *            starting index (starts with 0)
     * @param limit
     *            limit
     * @return list of players
     */
    List<MatchStatisticPlayerInterface> getPlayers(int start, int limit);
    
    /**
     * Returns the amount of teams participating in this match.
     * 
     * @return amount of teams.
     */
    int getTeamCount();
    
    /**
     * Returns the match statistics for given team.
     * 
     * @param type
     *            team type.
     * @return match statistics or {@code null} if team is unknown.
     */
    MatchStatisticTeamInterface getTeam(TeamIdType type);
    
    /**
     * Returns the teams participating in this match.
     * 
     * @param start
     *            starting index (starts with 0)
     * @param limit
     *            limit
     * @return list of teams
     */
    List<MatchStatisticTeamInterface> getTeams(int start, int limit);
    
    /**
     * Returns the winners
     * 
     * @param start
     *            starting index (starts with 0)
     * @param limit
     *            limit
     * @return match winners; player uuids
     */
    Collection<UUID> getWinners(int start, int limit);
    
    /**
     * Returns the number of match winners
     * 
     * @return match winner count
     */
    int getWinnerCount();
    
    /**
     * Returns the match losers
     * 
     * @param start
     *            starting index (starts with 0)
     * @param limit
     *            limit
     * @return mosers; player uuids
     */
    Collection<UUID> getLosers(int start, int limit);
    
    /**
     * Returns the number of match loser players
     * 
     * @return match loser count
     */
    int getLoserCount();
    
    /**
     * Returns the match results, first entry is the "first place".
     * 
     * @param start
     *            starting index (starts with 0)
     * @param limit
     *            limit
     * @return match results.
     */
    Collection<MatchResult> getResults(int start, int limit);
    
    /**
     * Returns the result count
     * 
     * @return count of match results or places
     */
    int getResultCount();
    
    /**
     * Returns the match results for given place
     * 
     * @param place
     *            the place starting with 1 for the best winner
     * @return match result or {@code null} if place number is invalid
     */
    MatchResult getResult(int place);
    
    /**
     * Returns the statistic for the game and given statistic id.
     * @param statistic
     * @return the statistic value
     */
    long getStatistic(GameStatisticId statistic);
    
}
