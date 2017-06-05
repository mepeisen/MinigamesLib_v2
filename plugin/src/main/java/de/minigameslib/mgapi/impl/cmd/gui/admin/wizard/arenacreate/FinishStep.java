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
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.AbstractWizard;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.WizardStepInterface;

/**
 * Basic match step
 * 
 * @author mepeisen
 */
public class FinishStep implements WizardStepInterface<ArenaInterface>
{
    
    /**
     * @param arena
     */
    public FinishStep(ArenaInterface arena)
    {
        // does nothing
    }

    @Override
    public ClickGuiItem[][] getItems(AbstractWizard<ArenaInterface> wizard)
    {
        return new ClickGuiItem[][]{};
    }
    
    @Override
    public McConsumer<ArenaInterface> getFinishTask(AbstractWizard<ArenaInterface> wizard)
    {
        return this::setup;
    }

    /**
     * perform setup.
     * @param arena 
     * @throws McException thrown for config problems.
     */
    private void setup(ArenaInterface arena) throws McException
    {
        // do nothing
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
        player.sendMessage(Messages.Help);
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.wizard.arena_create.FinishStep")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * help button
         */
        @LocalizedMessageList({"Now finish the wizard by selecting 'finish'."})
        @MessageComment({"Help button"})
        Help,
    }
}
