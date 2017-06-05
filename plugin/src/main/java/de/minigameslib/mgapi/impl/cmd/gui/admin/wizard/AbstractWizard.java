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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiServiceInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * A wizard.
 * 
 * @author mepeisen
 * @param <T>
 *            underlying wizard object
 *
 */
public abstract class AbstractWizard<T> implements ClickGuiPageInterface
{
    
    /**
     * the wizard steps.
     */
    private List<WizardStepInterface<T>> steps       = new ArrayList<>();
    
    /**
     * the current step.
     */
    private int                          currentStep = 0;
    
    /**
     * player.
     */
    private McPlayerInterface            player;
    
    /**
     * the underlying object.
     */
    private T                            object;
    
    /**
     * @param player
     * @param object
     */
    public AbstractWizard(McPlayerInterface player, T object)
    {
        this.player = player;
        this.object = object;
    }
    
    @Override
    public ClickGuiItem[][] getItems()
    {
        final WizardStepInterface<T> step = this.steps.get(this.currentStep);
        
        final ItemStack helpIcon = ItemServiceInterface.instance().createItem(CommonItems.App_Help);
        ItemServiceInterface.instance().setDescription(helpIcon, this.player, step.getHelp());
        
        final ClickGuiItem prevStep = this.currentStep == 0 ? null : GuiServiceInterface.itemPrevPage(this::onPrevStep);
        final ClickGuiItem nextStep = this.currentStep == this.steps.size() - 1 ? null : GuiServiceInterface.itemNextPage(this::onNextStep);
        
        final ClickGuiItem finish = new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_OK), Messages.Finish, this::onFinish);
        
        final ClickGuiItem[][] items = step.getItems(this);
        
        return ClickGuiPageInterface
                .withFillers(new ClickGuiItem[][] { { new ClickGuiItem(helpIcon, Messages.Help, null), null, prevStep, nextStep, null, finish, null, null, GuiServiceInterface.itemCloseGui() }, null,
                        items.length > 0 ? items[0] : null, items.length > 1 ? items[1] : null, items.length > 2 ? items[2] : null, items.length > 3 ? items[3] : null, }, 6);
    }
    
    /**
     * prev step
     * 
     * @param p
     * @param session
     * @param gui
     */
    public void onPrevStep(McPlayerInterface p, GuiSessionInterface session, ClickGuiInterface gui)
    {
        if (this.currentStep > 0)
        {
            this.currentStep--;
        }
        this.steps.get(this.currentStep).onActivate(this, p, session, gui);
        session.setNewPage(this);
    }
    
    /**
     * next step
     * 
     * @param p
     * @param session
     * @param gui
     */
    public void onNextStep(McPlayerInterface p, GuiSessionInterface session, ClickGuiInterface gui)
    {
        if (this.currentStep < this.steps.size() - 1)
        {
            this.currentStep++;
        }
        this.steps.get(this.currentStep).onActivate(this, p, session, gui);
        session.setNewPage(this);
    }
    
    /**
     * refresh current page
     * 
     * @param p
     * @param session
     * @param gui
     */
    public void onRefresh(McPlayerInterface p, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(this);
    }
    
    /**
     * finish
     * 
     * @param p
     * @param session
     * @param gui
     * @throws McException
     */
    public void onFinish(McPlayerInterface p, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        for (WizardStepInterface<T> step : this.steps)
        {
            step.checkFinish(this);
        }
        for (WizardStepInterface<T> step : this.steps)
        {
            step.getFinishTask(this).accept(this.object);
        }
        session.close();
    }
    
    /**
     * @return the currentStep
     */
    public int getCurrentStep()
    {
        return this.currentStep;
    }
    
    /**
     * Returns qizard step of given type
     * 
     * @param clazz
     * @return wizard step or {@code null} if step does not exist
     */
    public <Q extends WizardStepInterface<T>> Q getStep(Class<Q> clazz)
    {
        for (final WizardStepInterface<T> step : this.steps)
        {
            if (clazz.isInstance(step))
            {
                return clazz.cast(step);
            }
        }
        return null;
    }
    
    /**
     * @param currentStep
     *            the currentStep to set
     */
    public void setCurrentStep(int currentStep)
    {
        this.currentStep = currentStep;
        if (this.currentStep <= 0)
        {
            this.currentStep = 0;
        }
        else if (this.currentStep >= this.steps.size() - 1)
        {
            this.currentStep = this.steps.size() - 1;
        }
        this.steps.get(this.currentStep).onActivate(this, this.player, this.player.getGuiSession(), this.player.getGuiSession().getClickGui());
        this.player.getGuiSession().setNewPage(this);
    }
    
    /**
     * Adds next step.
     * 
     * @param step
     */
    public void addStep(WizardStepInterface<T> step)
    {
        this.steps.add(step);
    }
    
    /**
     * @return the player
     */
    public McPlayerInterface getPlayer()
    {
        return this.player;
    }
    
    /**
     * @return the object
     */
    public T getWizardObject()
    {
        return this.object;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.wizard")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Finish button
         */
        @LocalizedMessage(defaultMessage = "Finish wizard")
        @MessageComment({ "Finish button" })
        Finish,
        
        /**
         * help button
         */
        @LocalizedMessage(defaultMessage = "Help")
        @MessageComment({ "Help button" })
        Help,
    }
    
}
