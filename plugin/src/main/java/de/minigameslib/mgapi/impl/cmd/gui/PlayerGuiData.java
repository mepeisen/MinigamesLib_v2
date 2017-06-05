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

import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * @author mepeisen
 *
 */
public class PlayerGuiData extends AnnotatedDataFragment
{
    
    /**
     * Flag whether the advanced admin gui is displayed.
     */
    @PersistentField
    protected boolean isAdvancedAdminGui = false;
    
    /**
     * @return the isAdvancedAdminGui
     */
    public boolean isAdvancedAdminGui()
    {
        return this.isAdvancedAdminGui;
    }
    
    /**
     * @param isAdvancedAdminGui
     *            the isAdvancedAdminGui to set
     */
    public void setAdvancedAdminGui(boolean isAdvancedAdminGui)
    {
        this.isAdvancedAdminGui = isAdvancedAdminGui;
    }
    
    /**
     * Returns persistent data storage.
     * 
     * @param player
     *            target player
     * 
     * @return persistent player data
     */
    public static PlayerGuiData getData(McPlayerInterface player)
    {
        PlayerGuiData result = player.getPersistentStorage().get(PlayerGuiData.class);
        if (result == null)
        {
            result = new PlayerGuiData();
            player.getPersistentStorage().set(PlayerGuiData.class, result);
        }
        return result;
    }
    
}
