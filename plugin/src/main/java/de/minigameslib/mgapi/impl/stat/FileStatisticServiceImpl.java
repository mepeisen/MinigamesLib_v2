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
 * Statistic service implementation for storing into file system.
 * 
 * @author mepeisen
 */
public class FileStatisticServiceImpl implements StatisticServiceInterface
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
