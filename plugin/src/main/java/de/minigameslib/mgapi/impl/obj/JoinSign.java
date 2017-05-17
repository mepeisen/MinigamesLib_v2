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

package de.minigameslib.mgapi.impl.obj;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.McPlayerInteractEvent;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.obj.AbstractArenaSignHandler;
import de.minigameslib.mgapi.api.obj.JoinSignInterface;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * @author mepeisen
 *
 */
public class JoinSign extends AbstractArenaSignHandler<JoinSignData> implements JoinSignInterface, McListener
{
    
    // TODO clear out what variables will be shown on signs.
    // TODO clear out what events will be caught to update signs
    // TODO change the code of other signs as well
    
    /**
     * right click event
     * @param evt
     */
    @McEventHandler
    public void onRightClick(McPlayerInteractEvent evt)
    {
        try
        {
            this.arena.join(MinigamesLibInterface.instance().getPlayer(evt.getPlayer()));
        }
        catch (McException ex)
        {
            evt.getPlayer().sendMessage(ex.getErrorMessage(), ex.getArgs());
        }
    }
    
    @Override
    protected String[] getLines()
    {
        // TODO join lines
        return new String[]{
                "JOIN",
                this.getArena().getInternalName(),
                String.valueOf(System.currentTimeMillis())
        };
    }

    @Override
    protected Class<JoinSignData> getDataClass()
    {
        return JoinSignData.class;
    }

    @Override
    protected JoinSignData createData()
    {
        return new JoinSignData();
    }

    @Override
    protected Plugin getPlugin()
    {
        return MinigamesPlugin.instance().getPlugin();
    }
    
}
