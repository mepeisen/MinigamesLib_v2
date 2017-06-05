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

import de.minigameslib.mclib.api.enums.ChildEnum;
import de.minigameslib.mclib.api.gui.ClickGuiId;
import de.minigameslib.mgapi.impl.cmd.gui.admin.ArenaCreateChooseArenaType;
import de.minigameslib.mgapi.impl.cmd.gui.admin.ArenaCreateChooseMinigame;
import de.minigameslib.mgapi.impl.cmd.gui.admin.ArenasPage;
import de.minigameslib.mgapi.impl.cmd.gui.admin.ChecksPage;
import de.minigameslib.mgapi.impl.cmd.gui.admin.Main;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.ArenaEdit;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.ComponentEdit;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.ComponentsCreateChooseType;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.ComponentsPage;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.RuleEdit;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.RulesPage;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.SelectMarkerPage;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.SignEdit;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.SignsCreateChooseType;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.SignsPage;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.ZoneEdit;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.ZonesCreateChooseType;
import de.minigameslib.mgapi.impl.cmd.gui.admin.adv.ZonesPage;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.AbstractWizard;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.arenacreate.ArenaCreateWizard;

/**
 * Enumeration to identify guis.
 * 
 * @author mepeisen
 */
@ChildEnum({
    Main.Messages.class,
    ArenasPage.Messages.class,
    ArenaCreateChooseMinigame.Messages.class,
    ArenaCreateChooseArenaType.Messages.class,
    ArenaEdit.Messages.class,
    
    YesNoQuestion.Messages.class,
    SelectMarkerPage.Messages.class,
    
    RulesPage.Messages.class,
    RuleEdit.Messages.class,
    
    SignsPage.Messages.class,
    SignEdit.Messages.class,
    SignsCreateChooseType.Messages.class,
    
    ComponentsPage.Messages.class,
    ComponentEdit.Messages.class,
    ComponentsCreateChooseType.Messages.class,
    
    ZonesPage.Messages.class,
    ZoneEdit.Messages.class,
    ZonesCreateChooseType.Messages.class,
    
    ChecksPage.Messages.class,
    
    AbstractWizard.Messages.class,
    ArenaCreateWizard.Messages.class,
})
public enum MgClickGuis implements ClickGuiId
{
    
    /** the main gui. */
    Main
    
}
