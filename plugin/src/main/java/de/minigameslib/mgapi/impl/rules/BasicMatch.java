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

package de.minigameslib.mgapi.impl.rules;

import java.util.Collection;
import java.util.logging.Level;

import org.bukkit.scheduler.BukkitTask;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.arena.CheckSeverity;
import de.minigameslib.mgapi.api.events.ArenaForceStartRequestedEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerLeftEvent;
import de.minigameslib.mgapi.api.events.ArenaStateChangedEvent;
import de.minigameslib.mgapi.api.match.ArenaMatchInterface;
import de.minigameslib.mgapi.api.obj.BasicComponentTypes;
import de.minigameslib.mgapi.api.obj.BasicZoneTypes;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.BasicArenaRuleSets;
import de.minigameslib.mgapi.api.rules.BasicMatchConfig;
import de.minigameslib.mgapi.api.rules.BasicMatchRuleInterface;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * The implementation of BasicMatch rule set
 * 
 * @see BasicArenaRuleSets#BasicMatch
 * 
 * @author mepeisen
 */
public class BasicMatch extends AbstractArenaRule implements BasicMatchRuleInterface
{
    
    /**
     * Min players
     */
    private int        minPlayers;
    
    /**
     * Max players
     */
    private int        maxPlayers;
    
    /**
     * Lobby countdown in seconds
     */
    private int        lobbyCountdown;
    
    /**
     * The countdown timer
     */
    private int        countdownTimer;
    
    /**
     * The countdown task
     */
    private BukkitTask countdownTask;
    
    /**
     * @param type
     * @param arena
     * @throws McException
     *             thrown if config is invalid
     */
    public BasicMatch(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        super(type, arena);
        this.runInCopiedContext(() -> {
            this.minPlayers = BasicMatchConfig.MinPlayers.getInt();
            this.maxPlayers = BasicMatchConfig.MaxPlayers.getInt();
            this.lobbyCountdown = BasicMatchConfig.LobbyCountdown.getInt();
        });
        if (this.minPlayers <= 0)
        {
            throw new McException(Messages.InvalidConfigMinPlayers, this.minPlayers);
        }
        if (this.maxPlayers > 100) // TODO Query from global config (maybe someone likes more than 100 players)
        {
            throw new McException(Messages.InvalidConfigMaxPlayers, this.maxPlayers);
        }
        if (this.minPlayers > this.maxPlayers)
        {
            throw new McException(Messages.InvalidConfigMinMaxPlayers, this.minPlayers, this.maxPlayers);
        }
        if (this.lobbyCountdown <= 1)
        {
            throw new McException(Messages.InvalidConfigLobbyCountdown, this.lobbyCountdown);
        }
        if (this.lobbyCountdown > 600) // TODO Query from global config (maybe someone likes more than 60 seconds)
        {
            throw new McException(Messages.InvalidConfigLobbyCountdown, this.lobbyCountdown);
        }
    }
    
    @Override
    public ArenaRuleSetType getType()
    {
        return this.type;
    }
    
    @Override
    public ArenaInterface getArena()
    {
        return this.arena;
    }
    
    /**
     * Arena state change
     * @param evt
     */
    @McEventHandler
    public void onArenaState(ArenaStateChangedEvent evt)
    {
        if (evt.getNewState() != ArenaState.Join && this.countdownTask != null)
        {
            this.countdownTask.cancel();
            this.countdownTask = null;
        }
        
        if (evt.getNewState() == ArenaState.Disabled || evt.getNewState() == ArenaState.Maintenance || evt.getNewState() == ArenaState.Restarting)
        {
            // port all participants back to main lobby by force them leaving the arena
            final ArenaMatchInterface match = evt.getArena().getCurrentMatch();
            if (match != null)
            {
                match.getParticipants(true).forEach(uuid -> {
                    final ArenaPlayerInterface player = MinigamesLibInterface.instance().getPlayer(uuid);
                    if (player.getArena() == evt.getArena())
                    {
                        try
                        {
                            evt.getArena().leave(player);
                        }
                        catch (McException e)
                        {
                            // TODO logging
                        }
                    }
                });
            }
        }
    }
    
    /**
     * Invoked once a player tries to join.
     * 
     * @param evt
     */
    @McEventHandler
    public void onPlayerJoin(ArenaPlayerJoinEvent evt)
    {
        // check max players
        if (this.arena.getPlayerCount() >= this.maxPlayers)
        {
            evt.setCancelled(Messages.MaxPlayersReached, this.maxPlayers);
        }
    }
    
    /**
     * Invoked once a player successfully joined the arena
     * 
     * @param evt
     */
    @McEventHandler
    public void onPlayerJoined(ArenaPlayerJoinedEvent evt)
    {
        // check min players
        if (this.arena.getPlayerCount() >= this.minPlayers && this.countdownTask == null)
        {
            startCountdown();
        }
        
        // port to a lobby
        evt.getArena().teleportRandom(evt.getArenaPlayer(), evt.getArena().getComponents(BasicComponentTypes.LobbySpawn));
    }

    /**
     * Starts game countdown
     */
    private void startCountdown()
    {
        // start lobby countdown
        this.countdownTimer = this.lobbyCountdown - 1;
        this.countdownTask = McLibInterface.instance().runTaskTimer(MinigamesPlugin.instance().getPlugin(), 20, 20, this::onCountdown);
        
        // notify all players
        this.arena.getPlayers().forEach(p -> p.getMcPlayer().sendMessage(Messages.CountdownStarted, this.lobbyCountdown));
        this.arena.getSpectators().forEach(p -> p.getMcPlayer().sendMessage(Messages.CountdownStarted, this.lobbyCountdown));
    }
    
    /**
     * Invoked to force arena start
     * 
     * @param evt
     */
    @McEventHandler
    public void onForceStart(ArenaForceStartRequestedEvent evt)
    {
        if (this.countdownTask == null)
        {
            startCountdown();
        }
    }
    
    /**
     * Invoked once a player left.
     * 
     * @param evt
     */
    @McEventHandler
    public void onPlayerLeft(ArenaPlayerLeftEvent evt)
    {
        // check min players
        if (this.arena.getPlayerCount() < this.minPlayers && this.countdownTask != null)
        {
            this.countdownTask.cancel();
            this.countdownTask = null;
            
            // notify all players
            this.arena.getPlayers().forEach(p -> p.getMcPlayer().sendMessage(Messages.CountdownAborted));
            this.arena.getSpectators().forEach(p -> p.getMcPlayer().sendMessage(Messages.CountdownAborted));
        }
        
        // port back to main lobby
        evt.getArena().teleportRandom(evt.getArenaPlayer(), evt.getArena().getComponents(BasicComponentTypes.MainLobbySpawn));
    }
    
    /**
     * On lobby countdown
     * 
     * @param task
     */
    private void onCountdown(BukkitTask task)
    {
        if (this.countdownTimer <= 0)
        {
            this.countdownTask.cancel();
            this.countdownTask = null;
            try
            {
                this.arena.start();
            }
            catch (McException e)
            {
                // should never happen because the countdown gets cancelled before all players are left.
                this.arena.getLogger().log(Level.WARNING, "Problems starting arena match", e); //$NON-NLS-1$
            }
        }
        
        if (this.countdownTimer <= 10 || this.countdownTimer % 10 == 0)
        {
            this.arena.getPlayers().forEach(p -> p.getMcPlayer().sendMessage(Messages.CountdownTick, this.countdownTimer));
            this.arena.getSpectators().forEach(p -> p.getMcPlayer().sendMessage(Messages.CountdownTick, this.countdownTimer));
        }
        
        this.countdownTimer--;
    }
    
    @Override
    public int getMinPlayers()
    {
        return this.minPlayers;
    }
    
    @Override
    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }
    
    @Override
    public int getLobbyCountdown()
    {
        return this.lobbyCountdown;
    }
    
    @Override
    public void setPlayers(int minPlayers, int maxPlayers) throws McException
    {
        this.arena.checkModifications();
        if (minPlayers <= 0)
        {
            throw new McException(Messages.InvalidConfigMinPlayers, this.minPlayers);
        }
        if (maxPlayers > 100) // TODO Query from global config (maybe someone likes more than 100 players)
        {
            throw new McException(Messages.InvalidConfigMaxPlayers, this.maxPlayers);
        }
        if (minPlayers > maxPlayers)
        {
            throw new McException(Messages.InvalidConfigMinMaxPlayers, this.minPlayers, this.maxPlayers);
        }
        this.runInCopiedContext(() -> {
            BasicMatchConfig.MinPlayers.setInt(minPlayers);
            BasicMatchConfig.MaxPlayers.setInt(maxPlayers);
            BasicMatchConfig.MaxPlayers.saveConfig();
        });
        this.arena.reconfigureRuleSets(this.type);
    }
    
    @Override
    public void setLobbyCountdown(int lobbyCountdown) throws McException
    {
        this.arena.checkModifications();
        if (lobbyCountdown <= 1)
        {
            throw new McException(Messages.InvalidConfigLobbyCountdown, this.lobbyCountdown);
        }
        if (lobbyCountdown > 60) // TODO Query from global config (maybe someone likes more than 60 seconds)
        {
            throw new McException(Messages.InvalidConfigLobbyCountdown, this.lobbyCountdown);
        }
        this.runInCopiedContext(() -> {
            BasicMatchConfig.LobbyCountdown.setInt(lobbyCountdown);
            BasicMatchConfig.LobbyCountdown.saveConfig();
        });
        this.arena.reconfigureRuleSets(this.type);
    }
    
    @Override
    public Collection<CheckFailure> check()
    {
        final Collection<CheckFailure> result = super.check();
        
        // at least one lobby zone
        if (this.getArena().getZones(BasicZoneTypes.Lobby).isEmpty())
        {
            result.add(new CheckFailure(CheckSeverity.Error, Messages.NoLobby, Messages.NoLobby_Description));
        }
        // check for lobby spawn
        if (this.getArena().getComponents(BasicComponentTypes.LobbySpawn).isEmpty())
        {
            result.add(new CheckFailure(CheckSeverity.Error, Messages.NoLobbySpawn, Messages.NoLobbySpawn_Description));
        }
        
        // at least one battle zone
        if (this.getArena().getZones(BasicZoneTypes.Battle).isEmpty())
        {
            result.add(new CheckFailure(CheckSeverity.Error, Messages.NoBattleZone, Messages.NoBattleZone_Description));
        }
        
        // check for spectator zones
        if (this.getArena().getZones(BasicZoneTypes.Spectator).isEmpty())
        {
            result.add(new CheckFailure(CheckSeverity.Warning, Messages.NoSpectatorZone, Messages.NoSpectatorZone_Description));
        }
        // check for spectator spawns
        if (this.getArena().getComponents(BasicComponentTypes.SpectatorSpawn).isEmpty())
        {
            result.add(new CheckFailure(CheckSeverity.Warning, Messages.NoSpectatorSpawn, Messages.NoSpectatorSpawn_Description));
        }
        
        // check for main lobby
        if (this.getArena().getComponents(BasicComponentTypes.MainLobbySpawn).isEmpty())
        {
            result.add(new CheckFailure(CheckSeverity.Error, Messages.NoMainLobbySpawn, Messages.NoMainLobbySpawn_Description));
        }
        
        return result;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "rules.BasicMatch")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Max player count reached
         */
        @LocalizedMessage(defaultMessage = "You cannot join because there are already %1$d players in arena.", severity = MessageSeverityType.Error)
        @MessageComment(value = { "Max player count reached" }, args = { @Argument(type = "Numeric", value = "configured max players.") })
        MaxPlayersReached,
        
        /**
         * Countdown started
         */
        @LocalizedMessage(defaultMessage = "Countdown started. Match will start in " + LocalizedMessage.CODE_COLOR + "%1$d " + LocalizedMessage.INFORMATION_COLOR + "seconds.", severity = MessageSeverityType.Information)
        @MessageComment(value = { "Lobby countdown started" }, args = { @Argument(type = "Numeric", value = "countdown seconds") })
        CountdownStarted,
        
        /**
         * Countdown tick
         */
        @LocalizedMessage(defaultMessage = "Match will start in " + LocalizedMessage.CODE_COLOR + "%1$d " + LocalizedMessage.INFORMATION_COLOR + "seconds.", severity = MessageSeverityType.Information)
        @MessageComment(value = { "countdown tick" }, args = { @Argument(type = "Numeric", value = "countdown seconds") })
        CountdownTick,
        
        /**
         * Countdown aborted
         */
        @LocalizedMessage(defaultMessage = "Countdown aborted because too many players left the arena.", severity = MessageSeverityType.Error)
        @MessageComment(value = { "countdown aborted" })
        CountdownAborted,
        
        /**
         * Invalid config value (min players)
         */
        @LocalizedMessage(defaultMessage = "Invalid config value (min players): " + LocalizedMessage.CODE_COLOR + "%1$d", severity = MessageSeverityType.Information)
        @MessageComment(value = { "Invalid config value (min players)" }, args = { @Argument(type = "Numeric", value = "min players config value") })
        InvalidConfigMinPlayers,
        
        /**
         * Invalid config value (min players &gt; max players)
         */
        @LocalizedMessage(defaultMessage = "Invalid config value (min players > max players): " + LocalizedMessage.CODE_COLOR + "%1$d > %2$d", severity = MessageSeverityType.Information)
        @MessageComment(value = { "Invalid config value (min players > max players)" }, args = { @Argument(type = "Numeric", value = "min players config value"), @Argument(type = "Numeric", value = "max players config value") })
        InvalidConfigMinMaxPlayers,
        
        /**
         * Invalid config value (max players)
         */
        @LocalizedMessage(defaultMessage = "Invalid config value (max players): " + LocalizedMessage.CODE_COLOR + "%1$d", severity = MessageSeverityType.Information)
        @MessageComment(value = { "Invalid config value (max players)" }, args = { @Argument(type = "Numeric", value = "max players config value") })
        InvalidConfigMaxPlayers,
        
        /**
         * Invalid config value (lobby countdown)
         */
        @LocalizedMessage(defaultMessage = "Invalid config value (lobby countdown): " + LocalizedMessage.CODE_COLOR + "%1$d", severity = MessageSeverityType.Information)
        @MessageComment(value = { "Invalid config value (lobby countdown)" }, args = { @Argument(type = "Numeric", value = "lobby countdown config value") })
        InvalidConfigLobbyCountdown,
        
        /**
         * no lobby found.
         */
        @LocalizedMessage(defaultMessage = "No lobby found!", severity = MessageSeverityType.Error)
        @MessageComment("no lobby found")
        NoLobby,
        
        /**
         * no lobby found.
         */
        @LocalizedMessageList({
            "You did not create any lobby zone.",
            "The game needs at least one lobby zone.",
            "The lobby is the area where players are ported if they wait for a match to start."})
        @MessageComment("no lobby found")
        NoLobby_Description,
        
        /**
         * no lobby found.
         */
        @LocalizedMessage(defaultMessage = "No lobby spawn found!", severity = MessageSeverityType.Error)
        @MessageComment("no lobby spawn found")
        NoLobbySpawn,
        
        /**
         * no lobby found.
         */
        @LocalizedMessageList({
            "You did not create any lobby spawn.",
            "The game needs at least one lobby spawn.",
            "The lobby is the area where players are ported if they wait for a match to start."})
        @MessageComment("no lobby found")
        NoLobbySpawn_Description,
        
        /**
         * no battle zone found.
         */
        @LocalizedMessage(defaultMessage = "No battle zone found!", severity = MessageSeverityType.Error)
        @MessageComment("no battle zone found")
        NoBattleZone,
        
        /**
         * no battle zone found.
         */
        @LocalizedMessageList({
            "You did not create any battle zone.",
            "The game needs at least one battle zone.",
            "The battle zone is the arena wheere all the action goes on and where the players will fight."})
        @MessageComment("no battle zone found")
        NoBattleZone_Description,
        
        /**
         * no spectator zone found.
         */
        @LocalizedMessage(defaultMessage = "No spectator zone found!", severity = MessageSeverityType.Warning)
        @MessageComment("no spectator zone found")
        NoSpectatorZone,
        
        /**
         * no spectator zone found.
         */
        @LocalizedMessageList({
            "You did not create any spectator zone.",
            "The arena will work but it will be impossible to watch the game."})
        @MessageComment("no spectator zone found")
        NoSpectatorZone_Description,
        
        /**
         * no spectator spawn found.
         */
        @LocalizedMessage(defaultMessage = "No spectator spawn found!", severity = MessageSeverityType.Warning)
        @MessageComment("no spectator spawn found")
        NoSpectatorSpawn,
        
        /**
         * no spectator spawn found.
         */
        @LocalizedMessageList({
            "You did not create any spectator spawn.",
            "The arena will work but it will be impossible to watch the game."})
        @MessageComment("no spectator spawn found")
        NoSpectatorSpawn_Description,
        
        /**
         * no main lobby spawn found.
         */
        @LocalizedMessage(defaultMessage = "No main lobby spawn found!", severity = MessageSeverityType.Error)
        @MessageComment("no main lobby spawn found")
        NoMainLobbySpawn,
        
        /**
         * no main lobby spawn found.
         */
        @LocalizedMessageList({
            "You did not create any main lobby spawn.",
            "After leaving a match the players cannot be ported to any location without main lobby spawns."})
        @MessageComment("no main lobby spawn found")
        NoMainLobbySpawn_Description,
        
    }
    
}
