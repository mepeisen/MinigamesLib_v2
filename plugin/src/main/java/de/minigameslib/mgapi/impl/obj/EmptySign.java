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

import de.minigameslib.mgapi.api.obj.AbstractArenaSignHandler;
import de.minigameslib.mgapi.api.obj.EmptySignHandler;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * @author mepeisen
 *
 */
public class EmptySign extends AbstractArenaSignHandler<EmptySignData> implements EmptySignHandler
{
    
    @Override
    protected String[] getLines()
    {
        // TODO join lines
        return new String[]{
                "EMPTY",
                this.getArena().getInternalName(),
                String.valueOf(System.currentTimeMillis())
        };
    }

    @Override
    protected Class<EmptySignData> getDataClass()
    {
        return EmptySignData.class;
    }

    @Override
    protected EmptySignData createData()
    {
        return new EmptySignData();
    }

    @Override
    protected Plugin getPlugin()
    {
        return MinigamesPlugin.instance().getPlugin();
    }
    
}
