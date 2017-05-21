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

/**
 * Basic arena rule sets
 * 
 * @author mepeisen
 */
public enum BasicArenaRuleSets implements ArenaRuleSetType
{
    
    /**
     * A basic match rule containing:
     * - min player handling
     * - max player handling
     * @see BasicMatchRuleInterface
     */
    @RuleSetConfigurable(config = BasicMatchConfig.class)
    BasicMatch,
    
    /**
     * Spawn modes during matches
     * @see BasicSpawnsRuleInterface
     */
    @RuleSetConfigurable(config = BasicSpawnsConfig.class)
    BasicSpawns,
    
    /**
     * Maximum timer for matches
     * @see BasicMatchTimerRuleInterface
     */
    @RuleSetConfigurable(config = BasicMatchTimerConfig.class)
    BasicMatchTimer,
    
    /**
     * Spectator handling
     * @see BasicSpectatorRuleInterface
     */
    @RuleSetConfigurable(config = BasicSpectatorConfig.class)
    BasicSpectator,
    
    /**
     * Healing at match begin
     * @see HealAtBeginRuleInterface
     */
    @RuleSetConfigurable(config = HealAtBeginConfig.class)
    HealAtBegin,
    
    /**
     * Healing after death
     * @see HealAfterDeathRuleInterface
     */
    @RuleSetConfigurable(config = HealAfterDeathConfig.class)
    HealAfterDeath,
    
    /**
     * Points for killing
     * @see PointsForKillRuleInterface
     */
    @RuleSetConfigurable(config = PointsForKillConfig.class)
    PointsForKill,
    
    /**
     * Points for death
     * @see PointsForDeathRuleInterface
     */
    @RuleSetConfigurable(config = PointsForDeathConfig.class)
    PointsForDeath,
    
    /**
     * Count kills in statistics
     */
    CountKills,
    
    /**
     * Count deaths in statistics
     */
    CountDeaths,
    
}
