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

package de.minigameslib.mgapi.impl.cmd.gui.admin.adv;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiServiceInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.objects.ComponentTypeId;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McBiConsumer;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.obj.ArenaComponentHandler;
import de.minigameslib.mgapi.impl.cmd.gui.AbstractPage;
import de.minigameslib.mgapi.impl.cmd.gui.admin.Main;

/**
 * Page with arena components; choose type for new component
 * 
 * @author mepeisen
 */
public class ComponentsCreateChooseType extends AbstractPage<ComponentTypeId>
{
    
    /** the arena */
    private McBiConsumer<ComponentTypeId, String> onSave;
    
    /** previous page */
    private ClickGuiPageInterface prev;

    /** the underlying arena. */
    private ArenaInterface arena;

    /**
     * @param arena 
     * @param onSave
     * @param prev
     */
    public ComponentsCreateChooseType(ArenaInterface arena, McBiConsumer<ComponentTypeId, String> onSave, ClickGuiPageInterface prev)
    {
        this.arena = arena;
        this.onSave = onSave;
        this.prev = prev;
    }

    @Override
    public Serializable getPageName()
    {
        return Messages.Title.toArg(this.arena.getInternalName(), this.page(), this.totalPages());
    }

    @Override
    protected int count()
    {
        return EnumServiceInterface.instance().getEnumValues(ComponentTypeId.class).size();
    }
    
    /**
     * Converts component type to string
     * @param compType
     * @return component type
     */
    private String toString(ComponentTypeId compType)
    {
        return compType.getPluginName() + "/" + compType.name(); //$NON-NLS-1$
    }

    @Override
    protected List<ComponentTypeId> getElements(int start, int limit)
    {
        final Set<ComponentTypeId> result = new TreeSet<>((a, b) -> toString(a).compareTo(toString(b)));
        result.addAll(EnumServiceInterface.instance().getEnumValues(ComponentTypeId.class));
        return result.
                stream().
                skip(start).limit(limit).
                collect(Collectors.toList());
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, ComponentTypeId elm)
    {
        final ItemStack item = ItemServiceInterface.instance().createItem(CommonItems.App_Component);
        return new ClickGuiItem(item, Messages.IconComponent, (p, s, g) -> this.onChoose(p, s, g, elm), toString(elm));
    }

    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[]{
                null,
                null,
                this.itemPrevPage(),
                this.itemNextPage(),
                null,
                null,
                null,
                null,
                Main.itemCancel((p, s, g) -> s.setNewPage(this.prev), Messages.IconCancel)
                };
    }
    
    /**
     * Returns a free name with given prefix.
     * @param prefix
     * @return free name
     */
    private String getFreeName(String prefix)
    {
        int i = 1;
        while (true)
        {
            final String name = i == 1 ? prefix : prefix + "-" + i; //$NON-NLS-1$

            @SuppressWarnings("cast")
            final Optional<ArenaComponentHandler> handler = this.arena.getComponents().stream().
                    map(s -> (ArenaComponentHandler) this.arena.getHandler(s)).
                    filter(s -> name.equals(s.getName())).
                    findFirst();
            
            if (!handler.isPresent())
            {
                return name;
            }
            
            i++;
        }
    }
    
    /**
     * type chosen
     * @param player
     * @param session
     * @param gui
     * @param type
     * @throws McException 
     */
    private void onChoose(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, ComponentTypeId type) throws McException
    {
        final String text = this.getFreeName(type.name().toLowerCase());

        GuiServiceInterface.instance().openTextEditor(
                player,
                text,
                () -> {player.openClickGui(new Main(this));},
                (s) -> this.onName(player, session, gui, type, s),
                false,
                Messages.TextDescription);
    }
    
    /**
     * name selected
     * @param player
     * @param session
     * @param gui
     * @param type
     * @param name 
     * @throws McException 
     */
    private void onName(McPlayerInterface player, GuiSessionInterface session, ClickGuiInterface gui, ComponentTypeId type, String name) throws McException
    {
        this.onSave.accept(type, name);
    }
    
    /**
     * The arenas messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.component_create_choose_type")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (component types page)
         */
        @LocalizedMessage(defaultMessage = "Type for new component in arena %1$s (page %2$d from %3$d)")
        @MessageComment(value = {"Gui title (component types page)"}, args = {@Argument("arena name"), @Argument("page number"), @Argument("total pages")})
        Title,
        
        /**
         * The Cancel
         */
        @LocalizedMessage(defaultMessage = "Cancel creation")
        @MessageComment({"cancel icon"})
        IconCancel,
        
        /**
         * The component icon
         */
        @LocalizedMessage(defaultMessage = "type %1$s")
        @MessageComment(value = {"component type icon"}, args=@Argument("type name"))
        IconComponent,
        
        /**
         * Text description
         */
        @LocalizedMessageList({"Enter the name of the new component.", "The name is only used internal."})
        @MessageComment("Text description for component name")
        TextDescription,
    }
    
}
