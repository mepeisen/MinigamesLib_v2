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

package de.minigameslib.mgapi.impl.arena;

import java.util.Collection;
import java.util.UUID;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.locale.LocalizedConfigLine;
import de.minigameslib.mclib.api.locale.LocalizedConfigString;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mgapi.api.arena.ArenaClassInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.rules.ClassRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ClassRuleSetType;

/**
 * Arena classes.
 * 
 * @author mepeisen
 */
public class ArenaClassImpl extends AnnotatedDataFragment implements ArenaClassInterface
{
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#getRuleSet(de.minigameslib.mgapi.api.rules.RuleSetType)
     */
    @Override
    public ClassRuleSetInterface getRuleSet(ClassRuleSetType type)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#getAppliedRuleSetTypes()
     */
    @Override
    public Collection<ClassRuleSetType> getAppliedRuleSetTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#getAvailableRuleSetTypes()
     */
    @Override
    public Collection<ClassRuleSetType> getAvailableRuleSetTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#isFixed(de.minigameslib.mgapi.api.rules.RuleSetType)
     */
    @Override
    public boolean isFixed(ClassRuleSetType ruleset)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#isOptional(de.minigameslib.mgapi.api.rules.RuleSetType)
     */
    @Override
    public boolean isOptional(ClassRuleSetType ruleset)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#isApplied(de.minigameslib.mgapi.api.rules.RuleSetType)
     */
    @Override
    public boolean isApplied(ClassRuleSetType ruleset)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#isAvailable(de.minigameslib.mgapi.api.rules.RuleSetType)
     */
    @Override
    public boolean isAvailable(ClassRuleSetType ruleset)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#reconfigureRuleSets(de.minigameslib.mgapi.api.rules.RuleSetType[])
     */
    @Override
    public void reconfigureRuleSets(ClassRuleSetType... rulesets) throws McException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#reconfigureRuleSet(de.minigameslib.mgapi.api.rules.RuleSetType)
     */
    @Override
    public void reconfigureRuleSet(ClassRuleSetType ruleset) throws McException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#applyRuleSets(de.minigameslib.mgapi.api.rules.RuleSetType[])
     */
    @Override
    public void applyRuleSets(ClassRuleSetType... rulesets) throws McException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#applyRuleSet(de.minigameslib.mgapi.api.rules.RuleSetType)
     */
    @Override
    public void applyRuleSet(ClassRuleSetType ruleset) throws McException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#removeRuleSets(de.minigameslib.mgapi.api.rules.RuleSetType[])
     */
    @Override
    public void removeRuleSets(ClassRuleSetType... rulesets) throws McException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.rules.RuleSetContainerInterface#removeRuleSet(de.minigameslib.mgapi.api.rules.RuleSetType)
     */
    @Override
    public void removeRuleSet(ClassRuleSetType ruleset) throws McException
    {
        // TODO Auto-generated method stub
        
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaClassInterface#getInternalName()
     */
    @Override
    public String getInternalName()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaClassInterface#getDisplayName()
     */
    @Override
    public LocalizedConfigString getDisplayName()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaClassInterface#getShortDescription()
     */
    @Override
    public LocalizedConfigString getShortDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaClassInterface#getDescription()
     */
    @Override
    public LocalizedConfigLine getDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaClassInterface#getManual()
     */
    @Override
    public LocalizedConfigLine getManual()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaClassInterface#isDefault()
     */
    @Override
    public boolean isDefault()
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaClassInterface#isVisible(java.util.UUID)
     */
    @Override
    public boolean isVisible(UUID player)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaClassInterface#checkSelection(java.util.UUID)
     */
    @Override
    public void checkSelection(UUID player) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mgapi.api.arena.ArenaClassInterface#getArena()
     */
    @Override
    public ArenaInterface getArena()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
