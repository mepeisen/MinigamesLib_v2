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

package de.minigameslib.mgapi.api.arena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.obj.ArenaZoneHandler;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.BasicArenaRuleSets;
import de.minigameslib.mgapi.api.rules.BasicLosingRuleSets;
import de.minigameslib.mgapi.api.rules.BasicWinningRuleSets;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetInterface;
import de.minigameslib.mgapi.api.rules.SignRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetInterface;

/**
 * Basic class for classic arenas behaving similar to minigameslib version 1.
 * 
 * @author mepeisen
 */
public abstract class ClassicSinglePlayerArena implements ArenaTypeProvider
{

    @Override
    public Set<ArenaRuleSetType> getFixedArenaRules()
    {
        return new HashSet<>(Arrays.asList(
                BasicArenaRuleSets.BasicMatch,
                BasicArenaRuleSets.BasicSpawns,
                BasicArenaRuleSets.BasicSpectator,
                BasicWinningRuleSets.LastManStanding,
                BasicLosingRuleSets.LoseOnDeath));
    }

    @Override
    public Set<ArenaRuleSetType> getOptionalArenaRules()
    {
        return new HashSet<>(Arrays.asList(
                BasicArenaRuleSets.BasicMatchTimer,
                BasicArenaRuleSets.HealAfterDeath,
                BasicArenaRuleSets.HealAtBegin,
                BasicWinningRuleSets.Kills,
                BasicWinningRuleSets.Points,
                BasicLosingRuleSets.Lives
                ));
    }
    
    @Override
    public Collection<CheckFailure> check(ArenaInterface arena)
    {
        // no checkup in standard single player arena
        return new ArrayList<>();
    }

    @Override
    public void configure(ArenaRuleSetInterface ruleSet) throws McException
    {
        // no configuration
    }

    @Override
    public void configure(ComponentRuleSetInterface ruleSet) throws McException
    {
        // no configuration
    }

    @Override
    public void configure(ZoneRuleSetInterface ruleSet) throws McException
    {
        // no configuration
    }

    @Override
    public void configure(SignRuleSetInterface ruleSet) throws McException
    {
        // no configuration
    }

    @Override
    public void configure(ArenaComponentHandler handler) throws McException
    {
        // no configuration
    }

    @Override
    public void configure(ArenaZoneHandler handler) throws McException
    {
        // no configuration
    }

    @Override
    public void configure(ArenaSignHandler handler) throws McException
    {
        // no configuration
    }
    
}
