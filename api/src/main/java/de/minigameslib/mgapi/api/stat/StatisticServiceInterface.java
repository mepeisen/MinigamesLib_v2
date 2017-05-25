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

import java.time.LocalDate;
import java.time.YearMonth;

import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaTypeInterface;

/**
 * An interface to access the statistic services.
 * 
 * @author mepeisen
 */
public interface StatisticServiceInterface
{
    
    /**
     * Returns the statistic service instance.
     * 
     * @return statistic service instance.
     */
    static StatisticServiceInterface instance()
    {
        return StatisticServiceCache.get();
    }
    
    /**
     * Returns the overall statistic for all time.
     * 
     * @param arena
     *            target arena.
     * @return statistic interface
     */
    StatisticInterface getAllTimeStatistic(ArenaInterface arena);
    
    /**
     * Returns the statistic for given month.
     * 
     * @param arena
     *            target arena.
     * @param month
     *            target month
     * @return statistic interface
     */
    StatisticInterface getMonthlyStatistic(ArenaInterface arena, YearMonth month);
    
    /**
     * Returns the overall statistic for given day.
     * 
     * @param arena
     *            target arena.
     * @param date
     *            target date
     * @return statistic interface
     */
    StatisticInterface getDailyStatistic(ArenaInterface arena, LocalDate date);
    
    /**
     * Returns the recent x matches.
     * 
     * @param arena
     *            target type.
     * @param count
     *            number of matches.
     * @return statistic interface
     */
    StatisticInterface getRecentStatistic(ArenaInterface arena, int count);
    
    /**
     * Returns the overall statistic for all time.
     * 
     * @param type
     *            target type.
     * @return statistic interface
     */
    StatisticInterface getAllTimeStatistic(ArenaTypeInterface type);
    
    /**
     * Returns the statistic for given month.
     * 
     * @param type
     *            target type.
     * @param month
     *            target month
     * @return statistic interface
     */
    StatisticInterface getMonthlyStatistic(ArenaTypeInterface type, YearMonth month);
    
    /**
     * Returns the overall statistic for given day.
     * 
     * @param type
     *            target type.
     * @param date
     *            target date
     * @return statistic interface
     */
    StatisticInterface getDailyStatistic(ArenaTypeInterface type, LocalDate date);
    
    /**
     * Returns the recent x matches.
     * 
     * @param type
     *            target type.
     * @param count
     *            number of matches.
     * @return statistic interface
     */
    StatisticInterface getRecentStatistic(ArenaTypeInterface type, int count);
    
}
