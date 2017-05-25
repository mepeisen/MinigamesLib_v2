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

package de.minigameslib.mgapi.impl.stat;

import java.time.LocalDate;
import java.time.YearMonth;

import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;
import de.minigameslib.mgapi.api.stat.StatisticInterface;
import de.minigameslib.mgapi.api.stat.StatisticServiceInterface;

/**
 * Statistic service implementations for jdbc.
 * 
 * <h3>Table _prefix_arenatypedef:</h3>
 * <ul>
 * <li>arenatypeid UNIQUE ID</li>
 * <li>arenatypemod VARCHAR</li>
 * <li>arenatypename VARCHAR</li>
 * <li>
 * </ul>
 *  
 * <h3>Table _prefix_arenadef:</h3>
 * <ul>
 * <li>arenaid UNIQUE ID</li>
 * <li>arenatypeid FOREIGN ID</li>
 * <li>arenaname VARCHAR</li>
 * <li>
 * </ul>
 *  
 * <h3>Table _prefix_gamestatdef:</h3>
 * <ul>
 * <li>gamestatid UNIQUE ID</li>
 * <li>gamestatmod VARCHAR</li>
 * <li>gamestatname VARCHAR</li>
 * <li>
 * </ul>
 *  
 * <h3>Table _prefix_playerstatdef:</h3>
 * <ul>
 * <li>playerstatid UNIQUE ID</li>
 * <li>playerstatmod VARCHAR</li>
 * <li>playerstatname VARCHAR</li>
 * <li>
 * </ul>
 *  
 * <h3>Table _prefix_mplayerstatdef:</h3>
 * <ul>
 * <li>mplayerstatid UNIQUE ID</li>
 * <li>playerstatmod VARCHAR</li>
 * <li>playerstatname VARCHAR</li>
 * <li>
 * </ul>
 * 
 * <h3>Table _prefix_teamstatdef:</h3>
 * <ul>
 * <li>teamstatid UNIQUE ID</li>
 * <li>teamstatmod VARCHAR</li>
 * <li>teamstatname VARCHAR</li>
 * <li>
 * </ul>
 * 
 * <h3>Table _prefix_mteamstatdef:</h3>
 * <ul>
 * <li>mteamstatid UNIQUE ID</li>
 * <li>teamstatmod VARCHAR</li>
 * <li>teamstatname VARCHAR</li>
 * <li>
 * </ul>
 *  
 * <h3>Table _prefix_matchstatdef:</h3>
 * <ul>
 * <li>matchstatid UNIQUE ID</li>
 * <li>matchstatmod VARCHAR</li>
 * <li>matchstatname VARCHAR</li>
 * <li>
 * </ul>
 *  
 * <h3>Table _prefix_playerdef:</h3>
 * <ul>
 * <li>playerid UNIQUE ID</li>
 * <li>uuid VARCHAR</li>
 * <li>name VARCHAR</li>
 * <li>
 * </ul>
 *  
 * <h3>Table _prefix_teamdef:</h3>
 * <ul>
 * <li>teamid UNIQUE ID</li>
 * <li>teammod VARCHAR</li>
 * <li>teamname VARCHAR</li>
 * <li>
 * </ul>
 * 
 * <h3>Table _prefix_gamestats:</h3>
 * <ul>
 * <li>statid UNIQUE ID</li>
 * <li>arenaid FOREIGN ID</li>
 * <li>gamestatid FOREIGN ID</li>
 * <li>year INT</li>
 * <li>month INT</li>
 * <li>day INT</li>
 * <li>datetime TIMESTAMP</li>
 * <li>value LONG</li>
 * </ul>
 * 
 * <h3>Table _prefix_playerstats:</h3>
 * <ul>
 * <li>statid UNIQUE ID</li>
 * <li>arenaid FOREIGN ID</li>
 * <li>playerid FOREIGN ID</li>
 * <li>playerstatid FOREIGN ID</li>
 * <li>year INT</li>
 * <li>month INT</li>
 * <li>day INT</li>
 * <li>datetime TIMESTAMP</li>
 * <li>value LONG</li>
 * </ul>
 * 
 * <h3>Table _prefix_teamstats:</h3>
 * <ul>
 * <li>statid UNIQUE ID</li>
 * <li>arenaid FOREIGN ID</li>
 * <li>teamid FOREIGN ID</li>
 * <li>teamstatid FOREIGN ID</li>
 * <li>year INT</li>
 * <li>month INT</li>
 * <li>day INT</li>
 * <li>datetime TIMESTAMP</li>
 * <li>value LONG</li>
 * </ul>
 * 
 * <h3>Table _prefix_matches:</h3>
 * <ul>
 * <li>matchid UNIQUE ID</li>
 * <li>arenaid FOREIGN ID</li>
 * <li>started TIMESTAMP</li>
 * <li>finished TIMESTAMP</li>
 * </ul>
 * 
 * <h3>Table _prefix_matchplayers:</h3>
 * <ul>
 * <li>matchplayerid UNIQUE ID</li>
 * <li>matchid FOREIGN ID</li>
 * <li>playerid FOREIGN ID</li>
 * <li>primaryteamid FOREIGN ID</li>
 * <li>place INT</li>
 * <li>win BOOL</li>
 * <li>joined TIMESTAMP</li>
 * <li>left TIMESTAMP</li>
 * <li>playtime LONG</li>
 * </ul>
 * 
 * <h3>Table _prefix_matchteams:</h3>
 * <ul>
 * <li>matchteamid UNIQUE ID</li>
 * <li>matchid FOREIGN ID</li>
 * <li>teamid FOREIGN ID</li>
 * </ul>
 * 
 * <h3>Table _prefix_matchgamestats:</h3>
 * <ul>
 * <li>statid UNIQUE ID</li>
 * <li>matchid FOREIGN ID</li>
 * <li>gamestatid FOREIGN ID</li>
 * <li>value LONG</li>
 * </ul>
 * 
 * <h3>Table _prefix_matchplayerstats:</h3>
 * <ul>
 * <li>statid UNIQUE ID</li>
 * <li>matchplayerid FOREIGN ID</li>
 * <li>playerstatid FOREIGN ID</li>
 * <li>value LONG</li>
 * </ul>
 * 
 * <h3>Table _prefix_matchteamstats:</h3>
 * <ul>
 * <li>statid UNIQUE ID</li>
 * <li>matchteamid FOREIGN ID</li>
 * <li>teamstatid FOREIGN ID</li>
 * <li>value LONG</li>
 * </ul>
 * 
 * <h3>Table _prefix_matchmplayerstats:</h3>
 * <ul>
 * <li>statid UNIQUE ID</li>
 * <li>matchplayerid FOREIGN ID</li>
 * <li>mplayerstatid FOREIGN ID</li>
 * <li>value LONG</li>
 * </ul>
 * 
 * <h3>Table _prefix_matchmteamstats:</h3>
 * <ul>
 * <li>statid UNIQUE ID</li>
 * <li>matchteamid FOREIGN ID</li>
 * <li>mteamstatid FOREIGN ID</li>
 * <li>value LONG</li>
 * </ul>
 * 
 * @author mepeisen
 */
public class SqlStatisticServiceImpl implements StatisticServiceInterface
{
    
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticServiceInterface#getAllTimeStatistic(de.minigameslib.mgapi.api.arena.ArenaInterface)
     */
    @Override
    public StatisticInterface getAllTimeStatistic(ArenaInterface arena)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticServiceInterface#getMonthlyStatistic(de.minigameslib.mgapi.api.arena.ArenaInterface, java.time.YearMonth)
     */
    @Override
    public StatisticInterface getMonthlyStatistic(ArenaInterface arena, YearMonth month)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticServiceInterface#getDailyStatistic(de.minigameslib.mgapi.api.arena.ArenaInterface, java.time.LocalDate)
     */
    @Override
    public StatisticInterface getDailyStatistic(ArenaInterface arena, LocalDate date)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticServiceInterface#getRecentStatistic(de.minigameslib.mgapi.api.arena.ArenaInterface, int)
     */
    @Override
    public StatisticInterface getRecentStatistic(ArenaInterface arena, int count)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticServiceInterface#getAllTimeStatistic(de.minigameslib.mgapi.api.arena.ArenaTypeInterface)
     */
    @Override
    public StatisticInterface getAllTimeStatistic(ArenaTypeInterface type)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticServiceInterface#getMonthlyStatistic(de.minigameslib.mgapi.api.arena.ArenaTypeInterface, java.time.YearMonth)
     */
    @Override
    public StatisticInterface getMonthlyStatistic(ArenaTypeInterface type, YearMonth month)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticServiceInterface#getDailyStatistic(de.minigameslib.mgapi.api.arena.ArenaTypeInterface, java.time.LocalDate)
     */
    @Override
    public StatisticInterface getDailyStatistic(ArenaTypeInterface type, LocalDate date)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.stat.StatisticServiceInterface#getRecentStatistic(de.minigameslib.mgapi.api.arena.ArenaTypeInterface, int)
     */
    @Override
    public StatisticInterface getRecentStatistic(ArenaTypeInterface type, int count)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
