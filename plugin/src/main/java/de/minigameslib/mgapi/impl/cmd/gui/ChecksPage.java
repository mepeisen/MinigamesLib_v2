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

package de.minigameslib.mgapi.impl.cmd.gui;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.items.CommonItems;
import de.minigameslib.mclib.api.items.ItemServiceInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.CheckFailure;

/**
 * @author mepeisen
 *
 */
public class ChecksPage extends AbstractPage<CheckFailure> implements Comparator<CheckFailure>
{

    /** prev page. */
    private ClickGuiPageInterface prev;
    
    /** gui checks. */
    private List<CheckFailure> checks;
    
    /** the arena. */
    private ArenaInterface arena;
    
    /** underlying player. */
    private McPlayerInterface player;

    /**
     * Constructor.
     * 
     * @param player
     * @param arena
     * @param prev
     * @param checks
     */
    public ChecksPage(McPlayerInterface player, ArenaInterface arena, ClickGuiPageInterface prev, Collection<CheckFailure> checks)
    {
        this.arena = arena;
        this.prev = prev;
        this.checks = checks.stream().sorted(this).collect(Collectors.toList());
        this.player = player;
    }

    @Override
    public Serializable getPageName()
    {
        return Messages.Title.toArg(this.arena.getInternalName(), this.arena.getDisplayName());
    }

    @Override
    public int compare(CheckFailure o1, CheckFailure o2)
    {
        int result = Integer.compare(o1.getSeverity().ordinal(), o2.getSeverity().ordinal());
        if (result == 0)
        {
            result = o1.getTitle().toString().compareTo(o2.getTitle().toString());
        }
        return result;
    }

    @Override
    protected int count()
    {
        return this.checks.size();
    }

    @Override
    protected List<CheckFailure> getElements(int start, int limit)
    {
        return this.checks.stream().skip(start).limit(limit).collect(Collectors.toList());
    }

    @Override
    protected ClickGuiItem map(int line, int col, int index, CheckFailure elm)
    {
        CommonItems item;
        switch (elm.getSeverity())
        {
            case Error:
                item = CommonItems.App_Error;
                break;
            default:
            case Info:
                item = CommonItems.App_Info;
                break;
            case Warning:
                item = CommonItems.App_Warning;
                break;
            
        }
        final ItemStack itemobj = ItemServiceInterface.instance().createItem(item);
        ItemServiceInterface.instance().setDescription(itemobj, this.player.encodeMessage(Messages.ElementDescription, elm.getDetails()));
        return new ClickGuiItem(
                itemobj,
                Messages.ElementTitle,
                null,
                elm.getTitle());
    }

    @Override
    protected ClickGuiItem[] firstLine()
    {
        return new ClickGuiItem[]{
                Main.itemHome(),
                Main.itemBack(this::onBack, Messages.IconBack),
                Main.itemRefresh(this::onRefresh),
                this.itemPrevPage(),
                this.itemNextPage(),
                null,
                null,
                null,
                Main.itemCloseGui()
                };
    }
    
    /**
     * back to previous gui
     * @param p
     * @param session
     * @param gui
     */
    private void onBack(McPlayerInterface p, GuiSessionInterface session, ClickGuiInterface gui)
    {
        session.setNewPage(this.prev);
    }
    
    /**
     * The arena create messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.checks")
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Gui title (checks)
         */
        @LocalizedMessage(defaultMessage = "Checks for arena %1$s - %2$s")
        @MessageComment(value = {"Gui title (checks)"}, args = {@Argument("internal name"), @Argument("display name")})
        Title,
        
        /**
         * Gui element title
         */
        @LocalizedMessage(defaultMessage = "%1$s")
        @MessageComment(value = {"Gui element title"}, args = {@Argument("check title")})
        ElementTitle,
        
        /**
         * Gui element description
         */
        @LocalizedMessage(defaultMessage = "%1$s")
        @MessageComment(value = {"Gui element description"}, args = {@Argument("check description")})
        ElementDescription,
        
        /**
         * back to arena
         */
        @LocalizedMessage(defaultMessage = "Back to arena")
        @MessageComment({"back to arena"})
        IconBack,
    }
    
}
