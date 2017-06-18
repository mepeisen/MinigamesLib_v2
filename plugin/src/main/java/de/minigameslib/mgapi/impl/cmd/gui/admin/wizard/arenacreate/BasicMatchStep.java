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

package de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.arenacreate;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.GuiServiceInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.rules.BasicArenaRuleSets;
import de.minigameslib.mgapi.api.rules.BasicMatchRuleInterface;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.AbstractWizard;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.WizardStepInterface;

/**
 * Basic match step
 * 
 * @author mepeisen
 */
public class BasicMatchStep implements WizardStepInterface<ArenaInterface>
{
    
    /** the minimum players. */
    private int minPlayer;
    /** the maximum players. */
    private int maxPlayer;
    
    /** absolute minimum value. */
    private static final int LMIN = 2; // TODO configure/ take from BasicMatchRule
    
    /** absolute maximum value. */
    private static final int LMAX = 100; // TODO configure/ take from BasicMatchRule
    
    /**
     * @param arena
     */
    public BasicMatchStep(ArenaInterface arena)
    {
        final BasicMatchRuleInterface rule = (BasicMatchRuleInterface) arena.getRuleSet(BasicArenaRuleSets.BasicMatch);
        this.minPlayer = rule.getMinPlayers();
        this.maxPlayer = rule.getMaxPlayers();
    }

    @Override
    public ClickGuiItem[][] getItems(AbstractWizard<ArenaInterface> wizard)
    {
        return new ClickGuiItem[][]{
            {
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Calculator), Messages.MinimumPlayers, this::onMinimumPlayers),
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Calculator), Messages.MaximumPlayers, this::onMaximumPlayers)
            }
        };
    }

    
    /**
     * edit minimum players.
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onMinimumPlayers(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        GuiServiceInterface.instance().nestTextEditor(
                player,
                String.valueOf(this.minPlayer),
                null,
                s -> {
                    try
                    {
                        final int newVal = Integer.parseInt(s);
                        if (newVal < LMIN)
                        {
                            throw new McException(Messages.InvalidNumericFormat);
                        }
                        if (newVal > this.maxPlayer)
                        {
                            throw new McException(Messages.InvalidNumericFormat);
                        }
                        this.minPlayer = newVal;
                    }
                    catch (NumberFormatException ex)
                    {
                        throw new McException(Messages.InvalidNumericFormat, ex);
                    }
                },
                false,
                Messages.MinimumPlayersDescription);
    }

    
    /**
     * edit maximum players.
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onMaximumPlayers(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        GuiServiceInterface.instance().nestTextEditor(
                player,
                String.valueOf(this.maxPlayer),
                null,
                s -> {
                    try
                    {
                        final int newVal = Integer.parseInt(s);
                        if (newVal < this.minPlayer)
                        {
                            throw new McException(Messages.InvalidNumericFormat);
                        }
                        if (newVal > LMAX)
                        {
                            throw new McException(Messages.InvalidNumericFormat);
                        }
                        this.maxPlayer = newVal;
                    }
                    catch (NumberFormatException ex)
                    {
                        throw new McException(Messages.InvalidNumericFormat, ex);
                    }
                },
                false,
                Messages.MaximumPlayersDescription);
    }
    
    @Override
    public McConsumer<ArenaInterface> getFinishTask(AbstractWizard<ArenaInterface> wizard)
    {
        return this::setup;
    }
    
    /**
     * @return the maxPlayers
     */
    public int getMaxPlayers()
    {
        return this.maxPlayer;
    }

    /**
     * perform setup.
     * @param arena 
     * @throws McException thrown for config problems.
     */
    private void setup(ArenaInterface arena) throws McException
    {
        final BasicMatchRuleInterface rule = (BasicMatchRuleInterface) arena.getRuleSet(BasicArenaRuleSets.BasicMatch);
        rule.setPlayers(this.minPlayer, this.maxPlayer);
    }
    
    @Override
    public LocalizedMessageInterface getHelp()
    {
        return Messages.Help;
    }
    
    @Override
    public void checkFinish(AbstractWizard<ArenaInterface> wizard) throws McException
    {
        // ok
    }
    
    @Override
    public void onActivate(AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui)
    {
        // ignore
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.wizard.arena_create.BasicMatchStep")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * help button
         */
        @LocalizedMessageList({"Enter the minimum players and the maximum players for your arena."})
        @MessageComment({"Help button"})
        Help,
        
        /**
         * Minimum players.
         */
        @LocalizedMessage(defaultMessage = "Minimum players")
        @MessageComment({"Minimum players"})
        MinimumPlayers,
        
        /**
         * Minimum players.
         */
        @LocalizedMessageList("Enter the minimum players for this arena.")
        @MessageComment({"Minimum players"})
        MinimumPlayersDescription,
        
        /**
         * Maximum players.
         */
        @LocalizedMessage(defaultMessage = "Maximum players")
        @MessageComment({"Maximum players"})
        MaximumPlayers,
        
        /**
         * Maximum players.
         */
        @LocalizedMessageList("Enter the maximum players for this arena.")
        @MessageComment({"Maximum players"})
        MaximumPlayersDescription,
        
        /**
         * invalid numeric format.
         */
        @LocalizedMessage(defaultMessage = "The text you entered is not a valid number", severity = MessageSeverityType.Error)
        @MessageComment(value = { "invalid numeric format" })
        InvalidNumericFormat,
    }
}
