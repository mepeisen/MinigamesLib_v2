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
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.AbstractArenaZoneHandler;
import de.minigameslib.mgapi.api.obj.BasicZoneTypes;
import de.minigameslib.mgapi.impl.cmd.gui.MgClickGuis;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.AbstractWizard;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.WizardStepInterface;
import de.minigameslib.mgapi.impl.obj.LobbyZone;

/**
 * Lobby zone step
 * 
 * @author mepeisen
 */
public class LobbyZoneStep implements WizardStepInterface<ArenaInterface>
{
    
    /** the low location. */
    private Location lowLoc;
    /** the high location. */
    private Location highLow;
    
    /** the arena. */
    @SuppressWarnings("unused")
    private ArenaInterface arena;
    
    /**
     * @param arena
     */
    public LobbyZoneStep(ArenaInterface arena)
    {
        this.arena = arena;
    }

    @Override
    public ClickGuiItem[][] getItems(AbstractWizard<ArenaInterface> wizard)
    {
        return new ClickGuiItem[][]{
            {
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Pinion), Messages.LowLocation, (p, s, g) -> this.onLowLocation(wizard, p, s, g)),
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_Pinion), Messages.HighLocation, (p, s, g) -> this.onHighLocation(wizard, p, s, g))
            }
        };
    }

    
    /**
     * edit low location.
     * @param wizard 
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onLowLocation(AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        session.close();
        final ItemServiceInterface itemService = ItemServiceInterface.instance();
        itemService.prepareTool(CommonItems.App_Pinion, player, Messages.LowLocation)
            .onLeftClick((p, evt) -> onLowLocation(evt, wizard, player, session, gui))
            .onRightClick((p, evt) -> onLowLocation(evt, wizard, player, session, gui))
            .description(Messages.LowLocationDescription)
            .singleUse()
            .build();
        player.sendMessage(Messages.LowLocationDescription);
    }

    /**
     * edit low location.
     * @param evt
     * @param wizard 
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onLowLocation(McPlayerInteractEvent evt, AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.lowLoc = evt.getBukkitEvent().getClickedBlock().getLocation();
        if (this.lowLoc != null && this.highLow != null)
        {
            try
            {
                this.checkLocation(wizard);
            }
            catch (McException ex)
            {
                player.sendMessage(ex.getErrorMessage(), ex.getArgs());
            }
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
        if (this.lowLoc == null)
        {
            throw new McException(Messages.LowLocationMissing);
        }
        if (this.highLow == null)
        {
            throw new McException(Messages.HighLocationMissing);
        }
        
        final MainZoneStep main = wizard.getStep(MainZoneStep.class);
        main.checkLocation();
        
        final BattleZoneStep battle = wizard.getStep(BattleZoneStep.class);
        battle.checkLocation(wizard);
        
        final Cuboid cub = new Cuboid(this.lowLoc, this.highLow);
        if (!cub.isChild(main.getCuboid()))
        {
            throw new McException(AbstractArenaZoneHandler.Messages.NotWithinMainZone, "lobby"); //$NON-NLS-1$
        }
        if (cub.isOverlapping(battle.getCuboid()))
        {
            throw new McException(LobbyZone.Messages.OverlapsBattleZone, "lobby", "battle"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    
    /**
     * Returns the cuboid.
     * @return the cuboid or {@code null} if the locations are not set
     */
    public Cuboid getCuboid()
    {
        if (this.lowLoc == null || this.highLow == null)
        {
            return null;
        }
        return new Cuboid(this.lowLoc, this.highLow);
    }
    
    /**
     * edit high location.
     * @param wizard 
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onHighLocation(AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        session.close();
        final ItemServiceInterface itemService = ItemServiceInterface.instance();
        itemService.prepareTool(CommonItems.App_Pinion, player, Messages.HighLocation)
            .onLeftClick((p, evt) -> onHighLocation(evt, wizard, player, session, gui))
            .onRightClick((p, evt) -> onHighLocation(evt, wizard, player, session, gui))
            .description(Messages.HighLocationDescription)
            .singleUse()
            .build();
        player.sendMessage(Messages.HighLocationDescription);
    }

    /**
     * edit high location.
     * @param evt
     * @param wizard 
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onHighLocation(McPlayerInteractEvent evt, AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.highLow = evt.getBukkitEvent().getClickedBlock().getLocation();
        if (this.lowLoc != null && this.highLow != null)
        {
            try
            {
                this.checkLocation(wizard);
            }
            catch (McException ex)
            {
                player.sendMessage(ex.getErrorMessage(), ex.getArgs());
            }
        }
        player.openClickGui(new SimpleClickGui(MgClickGuis.Main, wizard, 6));
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
        arena.createZone(new Cuboid(this.lowLoc, this.highLow), BasicZoneTypes.Lobby).setName("lobby"); //$NON-NLS-1$
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
    @LocalizedMessages(value = "admingui.wizard.arena_create.LobbyZoneStep")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * help button
         */
        @LocalizedMessageList({"Select the lobby zone.", "This is the area where players are waiting before the match begins."})
        @MessageComment({"Help button"})
        Help,
        
        /**
         * low location.
         */
        @LocalizedMessage(defaultMessage = "Select low location")
        @MessageComment({"low location"})
        LowLocation,
        
        /**
         * low location missing.
         */
        @LocalizedMessage(defaultMessage = "Select low location for lobby zone")
        @MessageComment({"low location missing"})
        LowLocationMissing,
        
        /**
         * low location.
         */
        @LocalizedMessageList("Use the pinion and click the lower edge of your lobby zone.")
        @MessageComment({"low location"})
        LowLocationDescription,
        
        /**
         * high location.
         */
        @LocalizedMessage(defaultMessage = "Select high location")
        @MessageComment({"high location"})
        HighLocation,
        
        /**
         * high location missing.
         */
        @LocalizedMessage(defaultMessage = "Select high location for lobby zone")
        @MessageComment({"high location missing"})
        HighLocationMissing,
        
        /**
         * high location.
         */
        @LocalizedMessageList("Use the pinion and click the higher edge of your lobby zone.")
        @MessageComment({"high location"})
        HighLocationDescription,
    }
    
}
