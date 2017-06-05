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

import java.io.Serializable;

import de.minigameslib.mclib.api.enums.ChildEnum;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.rules.BasicArenaRuleSets;
import de.minigameslib.mgapi.impl.cmd.gui.admin.wizard.AbstractWizard;

/**
 * @author mepeisen
 *
 */
public class ArenaCreateWizard extends AbstractWizard<ArenaInterface>
{

    /**
     * @param player
     * @param arena
     */
    public ArenaCreateWizard(McPlayerInterface player, ArenaInterface arena)
    {
        super(player, arena);
        
        // TODO support generic wizards through provider class.
        
        if (arena.isApplied(BasicArenaRuleSets.BasicMatch))
        {
            // classic single player arenas
            this.addStep(new BasicMatchStep(arena));
            this.addStep(new MainZoneStep(arena));
            this.addStep(new BattleZoneStep(arena));
            this.addStep(new SpawnsStep(arena));
            this.addStep(new LobbyZoneStep(arena));
            this.addStep(new LobbySpawnsStep(arena));
            this.addStep(new MainSpawnStep(arena));
            this.addStep(new JoinSignStep(arena));
            this.addStep(new FinishStep(arena));
        }
    }

    @Override
    public Serializable getPageName()
    {
        return Messages.Title;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "admingui.wizard.arena_create")
    @ChildEnum({
        BasicMatchStep.Messages.class,
        MainZoneStep.Messages.class,
        LobbyZoneStep.Messages.class,
        LobbySpawnsStep.Messages.class,
        MainSpawnStep.Messages.class,
        JoinSignStep.Messages.class,
        BattleZoneStep.Messages.class,
        SpawnsStep.Messages.class,
        FinishStep.Messages.class,
    })
    public enum Messages implements LocalizedMessageInterface
    {
        /**
         * Title
         */
        @LocalizedMessage(defaultMessage = "Arena creation")
        @MessageComment({"Title"})
        Title,
    }
    
}
