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

package de.minigameslib.mgapi.impl.cmd.gui.admin.wizard;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;

/**
 * A single wizard step.
 * 
 * @author mepeisen
 * @param <T> underlying object
 */
public interface WizardStepInterface<T>
{
    
    /**
     * The gui items for this wizard step (maximum of your lines)
     * @param wizard 
     * @return gui items.
     */
    ClickGuiItem[][] getItems(AbstractWizard<T> wizard);
    
    /**
     * The creation tasks.
     * @param wizard 
     * @return creation tasks.
     */
    McConsumer<T> getFinishTask(AbstractWizard<T> wizard);
    
    /**
     * Returns a help page.
     * @return help page.
     */
    LocalizedMessageInterface getHelp();
    
    /**
     * checks if wizard can be finished.
     * @param wizard 
     * @throws McException thrown if there are problems finishing the wizard.
     */
    void checkFinish(AbstractWizard<T> wizard) throws McException;
    
    /**
     * Invoked on wizard step invocation.
     * @param wizard
     * @param player
     * @param session
     * @param gui
     */
    void onActivate(AbstractWizard<T> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui);
    
}
