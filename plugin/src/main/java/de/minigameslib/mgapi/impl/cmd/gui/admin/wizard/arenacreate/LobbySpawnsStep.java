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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Location;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McPlayerInteractEvent;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.GuiServiceInterface;
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
import de.minigameslib.mgapi.api.obj.BasicComponentTypes;
import de.minigameslib.mgapi.impl.cmd.gui.MgClickGuis;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.AbstractWizard;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.WizardStepInterface;
import de.minigameslib.mgapi.impl.obj.LobbySpawnComponent;

/**
 * Basic match step
 * 
 * @author mepeisen
 */
public class LobbySpawnsStep implements WizardStepInterface<ArenaInterface>
{
    
    /** the spawns collection. */
    private List<Location> spawns = new ArrayList<>();
    
    /** the current spawns page. */
    private int page = 0;
    
    /**
     * @param arena
     */
    public LobbySpawnsStep(ArenaInterface arena)
    {
        // does nothing
    }

    @Override
    public ClickGuiItem[][] getItems(AbstractWizard<ArenaInterface> wizard)
    {
        return new ClickGuiItem[][]{
            {
                new ClickGuiItem(ItemServiceInterface.instance().createItem(CommonItems.App_New), Messages.NewSpawn, (p, s, g) -> this.onNewSpawn(wizard, p, s, g), this.spawns.size() + 1),
                null,
                this.page > 0 ? GuiServiceInterface.itemPrevPage((p, s, g) -> this.onPrevPage(wizard, p, s, g)) : null,
                this.page < this.pageCount() ? GuiServiceInterface.itemNextPage((p, s, g) -> this.onNextPage(wizard, p, s, g)) : null,
            },
            this.spawnItems(wizard, 0),
            this.spawnItems(wizard, 1),
            this.spawnItems(wizard, 2)
        };
    }

    
    /**
     * @param wizard 
     * @param line
     * @return click gui items
     */
    private ClickGuiItem[] spawnItems(AbstractWizard<ArenaInterface> wizard, int line)
    {
        final int start = this.page * 27 + line * 9;
        final AtomicInteger num = new AtomicInteger(start + 1);
        return this.spawns.stream().skip(start).limit(9)
                .map(spawn -> {
                    final int pos = num.getAndIncrement();
                    return new ClickGuiItem(
                            ItemServiceInterface.instance().createItem(CommonItems.App_Component),
                            Messages.Spawn,
                            (p, s, g) -> this.onLocate(wizard, p, s, g, pos),
                            pos);
                })
                .toArray(ClickGuiItem[]::new);
    }

    /**
     * The page count.
     * @return page count.
     */
    private int pageCount()
    {
        return this.spawns.size() / 27;
    }

    /**
     * relocate pos
     * @param wizard 
     * @param player
     * @param session
     * @param gui
     * @param pos 
     * @throws McException 
     */
    private void onLocate(AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int pos) throws McException
    {
        session.close();
        final ItemServiceInterface itemService = ItemServiceInterface.instance();
        itemService.prepareTool(CommonItems.App_Pinion, player, Messages.SpawnLocation, pos)
            .onLeftClick((p, evt) -> onLocate(evt, wizard, player, session, gui, pos))
            .onRightClick((p, evt) -> onLocate(evt, wizard, player, session, gui, pos))
            .description(Messages.SpawnLocationDescription, pos)
            .singleUse()
            .build();
        player.sendMessage(Messages.SpawnLocationDescription, pos);
    }

    /**
     * relocate pos.
     * @param evt
     * @param wizard 
     * @param player
     * @param session
     * @param gui
     * @param pos 
     * @throws McException 
     */
    private void onLocate(McPlayerInteractEvent evt, AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, int pos) throws McException
    {
        this.spawns.set(pos - 1, evt.getBukkitEvent().getClickedBlock().getLocation());
        player.openClickGui(new SimpleClickGui(MgClickGuis.Main, wizard, 6));
    }

    /**
     * prev page.
     * @param wizard 
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onPrevPage(AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        if (this.page > 0)
        {
            this.page--;
        }
        wizard.onRefresh(player, session, gui);
    }

    /**
     * next page.
     * @param wizard 
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onNextPage(AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        if (this.page < this.pageCount() - 1)
        {
            this.page++;
        }
        wizard.onRefresh(player, session, gui);
    }

    /**
     * create spawn.
     * @param wizard 
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onNewSpawn(AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        session.close();
        final ItemServiceInterface itemService = ItemServiceInterface.instance();
        itemService.prepareTool(CommonItems.App_Pinion, player, Messages.SpawnLocation, this.spawns.size() + 1)
            .onLeftClick((p, evt) -> onNewSpawn(evt, wizard, player, session, gui))
            .onRightClick((p, evt) -> onNewSpawn(evt, wizard, player, session, gui))
            .description(Messages.SpawnLocationDescription, this.spawns.size() + 1)
            .singleUse()
            .build();
        player.sendMessage(Messages.SpawnLocationDescription, this.spawns.size() + 1);
    }

    /**
     * create spawn.
     * @param evt
     * @param wizard 
     * @param player
     * @param session
     * @param gui
     * @throws McException 
     */
    private void onNewSpawn(McPlayerInteractEvent evt, AbstractWizard<ArenaInterface> wizard, McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui) throws McException
    {
        this.spawns.add(evt.getBukkitEvent().getClickedBlock().getLocation());
        player.openClickGui(new SimpleClickGui(MgClickGuis.Main, wizard, 6));
        checkSpawns(wizard);
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
        for (int i = 0; i < this.spawns.size(); i++)
        {
            arena.createComponent(this.spawns.get(i), BasicComponentTypes.LobbySpawn).setName("lobbyspawn" + (i + 1)); //$NON-NLS-1$
        }
    }
    
    @Override
    public LocalizedMessageInterface getHelp()
    {
        return Messages.Help;
    }
    
    @Override
    public void checkFinish(AbstractWizard<ArenaInterface> wizard) throws McException
    {
        checkSpawns(wizard);
    }

    /**
     * @param wizard
     * @throws McException
     */
    private void checkSpawns(AbstractWizard<ArenaInterface> wizard) throws McException
    {
        final LobbyZoneStep lobby = wizard.getStep(LobbyZoneStep.class);
        lobby.checkLocation(wizard);
        final Cuboid cub = lobby.getCuboid();
        
        for (int i = 0; i < this.spawns.size(); i++)
        {
            if (!cub.containsLoc(this.spawns.get(i)))
            {
                throw new McException(LobbySpawnComponent.Messages.NotWithinLobbyZone, "lobbyspawn" + (i + 1)); //$NON-NLS-1$
            }
        }
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
    @LocalizedMessages(value = "admingui.wizard.arena_create.LobbySpawnsStep")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * help button
         */
        @LocalizedMessageList({"Create the lobby spawns, at least 1."})
        @MessageComment({"Help button"})
        Help,
        
        /**
         * new spawn button
         */
        @LocalizedMessage(defaultMessage = "Create lobby spawn #%1$d")
        @MessageComment({"New spawn button"})
        NewSpawn,
        
        /**
         * spawn button
         */
        @LocalizedMessage(defaultMessage = "Change lobby spawn #%1$d")
        @MessageComment({"spawn button"})
        Spawn,
        
        /**
         * spawn location.
         */
        @LocalizedMessage(defaultMessage = "Select lobby spawn location")
        @MessageComment({"lobby spawn location"})
        SpawnLocation,
        
        /**
         * spawn location.
         */
        @LocalizedMessageList("Use the pinion and click the block of lobby spawn #%1$d.")
        @MessageComment({"spawn location"})
        SpawnLocationDescription,
        
    }
}
