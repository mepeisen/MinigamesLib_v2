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

import org.bukkit.Location;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McPlayerInteractEvent;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.gui.SimpleClickGui;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.AbstractArenaZoneHandler;
import de.minigameslib.mgapi.api.obj.BasicComponentTypes;
import de.minigameslib.mgapi.impl.cmd.gui.MgClickGuis;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.AbstractWizard;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.WizardStepInterface;

/**
 * Main spawn step
 * 
 * @author mepeisen
 */
public class MainSpawnStep implements WizardStepInterface<ArenaInterface>
{
    
    /** the location. */
    private Location loc;
    
    /** the arena. */
    @SuppressWarnings("unused")
    private ArenaInterface arena;
    
    /**
     * @param arena
     */
    public MainSpawnStep(ArenaInterface arena)
    {
        this.arena = arena;
    }

    @Override
    public ClickGuiItem[][] getItems(AbstractWizard<ArenaInterface> wizard)
    {
        return new ClickGuiItem[][]{
            {
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Pinion), Messages.Location, (p, s, g) -> this.onLocation(wizard, p, s, g))
            }
        };
    }

    
    /**
     * edit location.
     * @param wizard 
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onLocation(AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        session.close();
        final ItemServiceInterface itemService = ItemServiceInterface.instance();
        itemService.prepareTool(CommonItems.App_Pinion, player, Messages.Location)
            .onLeftClick((p, evt) -> onLocation(evt, wizard, player, session, gui))
            .onRightClick((p, evt) -> onLocation(evt, wizard, player, session, gui))
            .description(Messages.LocationDescription)
            .singleUse()
            .build();
        player.sendMessage(Messages.LocationDescription);
    }

    /**
     * edit location.
     * @param evt
     * @param wizard 
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onLocation(McPlayerInteractEvent evt, AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.loc = evt.getBukkitEvent().getClickedBlock().getLocation();
        try
        {
            this.checkLocation(wizard);
        }
        catch (McException ex)
        {
            player.sendMessage(ex.getErrorMessage(), ex.getArgs());
        }
        player.openClickGui(new SimpleClickGui(MgClickGuis.Main, wizard, 6));
    }
    
    /**
     * Checks for valid location.
     * @param wizard wizard
     * @throws McException
     */
    public void checkLocation(AbstractWizard<ArenaInterface> wizard) throws McException
    {
        if (this.loc == null)
        {
            throw new McException(Messages.LocationMissing);
        }
        
        final MainZoneStep main = wizard.getStep(MainZoneStep.class);
        main.checkLocation();
        
        if (!main.getCuboid().containsLoc(this.loc))
        {
            throw new McException(AbstractArenaZoneHandler.Messages.NotWithinMainZone, "mainspawn"); //$NON-NLS-1$
        }
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
        arena.createComponent(this.loc, BasicComponentTypes.MainLobbySpawn).setName("mainspawn"); //$NON-NLS-1$
    }
    
    @Override
    public LocalizedMessageInterface getHelp()
    {
        return Messages.Help;
    }
    
    @Override
    public void checkFinish(AbstractWizard<ArenaInterface> wizard) throws McException
    {
        this.checkLocation(wizard);
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
    @LocalizedMessages(value = "admingui.wizard.arena_create.MainSpawnStep")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * help button
         */
        @LocalizedMessageList({"Select the main spawn.", "This is the location players are teleported after match was ended."})
        @MessageComment({"Help button"})
        Help,
        
        /**
         * location.
         */
        @LocalizedMessage(defaultMessage = "Select location")
        @MessageComment({"low location"})
        Location,
        
        /**
         * location missing.
         */
        @LocalizedMessage(defaultMessage = "Select location for main spawn")
        @MessageComment({"location missing"})
        LocationMissing,
        
        /**
         * location.
         */
        @LocalizedMessageList("Use the pinion and click the block of your main lobby spawn.")
        @MessageComment({"location"})
        LocationDescription,
        
    }
    
}
