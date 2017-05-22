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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.events.ArenaStateChangedEvent;
import de.minigameslib.mgapi.api.rules.AbstractArenaRule;
import de.minigameslib.mgapi.api.rules.ArenaRuleSetType;
import de.minigameslib.mgapi.api.rules.BasicArenaRuleSets;
import de.minigameslib.mgapi.api.rules.BasicMatchTimerConfig;
import de.minigameslib.mgapi.api.rules.BasicMatchTimerRuleInterface;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * The implementation of BasicMatchTime rule set
 * 
 * @see BasicArenaRuleSets#BasicMatchTimer
 * 
 * @author mepeisen
 */
public class BasicMatchTimer extends AbstractArenaRule implements BasicMatchTimerRuleInterface
{
    
    /** the sound to play for ticks. */
    private Sound               sound;
    
    /** flag to control sound. */
    private boolean             playSound;
    
    /**
     * maximum seconds
     */
    private int                 seconds;
    
    /**
     * the current match duration in millis
     */
    private long                matchDuration;
    
    /**
     * the current match maxmimum time
     */
    private long                matchTime;
    
    /**
     * Flag for paused or running timers; {@code true} if timer is paused
     */
    private boolean             paused;
    
    /**
     * Last start of timer
     */
    private LocalDateTime       lastStart = LocalDateTime.now();
    
    /**
     * The timer task
     */
    private BukkitTask          timerTask;
    
    /** logger. */
    private static final Logger LOGGER    = Logger.getLogger(BasicMatchTimer.class.getName());
    
    /** {@code true} for warning about 60 seconds left */
    private boolean             warn60    = true;
    
    /** {@code true} for warning about 30 seconds left */
    private boolean             warn30    = true;
    
    /** {@code true} for warning about 20 seconds left */
    private boolean             warn20    = true;
    
    /** {@code true} for warning about 10 seconds left */
    private boolean             warn10    = true;
    
    /** {@code true} for warning about 5 seconds left */
    private boolean             warn5     = true;
    
    /** {@code true} for warning about 4 seconds left */
    private boolean             warn4     = true;
    
    /** {@code true} for warning about 3 seconds left */
    private boolean             warn3     = true;
    
    /** {@code true} for warning about 2 seconds left */
    private boolean             warn2     = true;
    
    /** {@code true} for warning about 1 seconds left */
    private boolean             warn1     = true;
    
    /**
     * @param type
     * @param arena
     * @throws McException
     *             thrown if config is invalid
     */
    public BasicMatchTimer(ArenaRuleSetType type, ArenaInterface arena) throws McException
    {
        super(type, arena);
        this.runInCopiedContext(() -> {
            BasicMatchTimerConfig.MaxSeconds.verifyConfig();
            this.seconds = BasicMatchTimerConfig.MaxSeconds.getInt();
            this.matchTime = this.seconds * 1000L;
            this.sound = BasicMatchTimerConfig.CountdownSound.getJavaEnum(Sound.class);
            if (this.sound == null)
            {
                this.sound = Sound.BLOCK_GRASS_HIT; // TODO check if this exists in 1.8
            }
            this.playSound = BasicMatchTimerConfig.CountdownPlaySound.getBoolean();
        });
    }
    
    @Override
    public int getMaxSeconds()
    {
        return this.seconds;
    }
    
    @Override
    public void setMaxSeconds(int seconds) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            BasicMatchTimerConfig.MaxSeconds.setInt(this.seconds);
            try
            {
                BasicMatchTimerConfig.MaxSeconds.verifyConfig();
                BasicMatchTimerConfig.MaxSeconds.saveConfig();
            }
            catch (McException ex)
            {
                BasicMatchTimerConfig.MaxSeconds.rollbackConfig();
                throw ex;
            }
        });
        this.arena.reconfigureRuleSets(this.type);
    }
    
    @Override
    public Sound getCountdownSound()
    {
        return this.sound;
    }
    
    @Override
    public boolean isPlayCountdownSound()
    {
        return this.playSound;
    }
    
    @Override
    public void setCountdownSound(Sound sound) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            BasicMatchTimerConfig.CountdownSound.setJavaEnum(sound);
            try
            {
                BasicMatchTimerConfig.CountdownSound.verifyConfig();
                BasicMatchTimerConfig.CountdownSound.saveConfig();
            }
            catch (McException ex)
            {
                BasicMatchTimerConfig.CountdownSound.rollbackConfig();
                throw ex;
            }
        });
        this.arena.reconfigureRuleSets(this.type);
    }
    
    @Override
    public void setLobbyCountdownSound(boolean flag) throws McException
    {
        this.arena.checkModifications();
        this.runInCopiedContext(() -> {
            BasicMatchTimerConfig.CountdownPlaySound.setBoolean(flag);
            try
            {
                BasicMatchTimerConfig.CountdownPlaySound.verifyConfig();
                BasicMatchTimerConfig.CountdownPlaySound.saveConfig();
            }
            catch (McException ex)
            {
                BasicMatchTimerConfig.CountdownPlaySound.rollbackConfig();
                throw ex;
            }
        });
        this.arena.reconfigureRuleSets(this.type);
    }
    
    @Override
    public void pause()
    {
        if (!this.paused)
        {
            this.paused = true;
            this.matchDuration += this.lastStart.until(LocalDateTime.now(), ChronoUnit.MILLIS);
        }
    }
    
    @Override
    public void resume()
    {
        if (this.paused)
        {
            this.paused = false;
            this.lastStart = LocalDateTime.now();
        }
    }
    
    @Override
    public void resetAndResume()
    {
        this.paused = false;
        this.lastStart = LocalDateTime.now();
        this.matchDuration = 0;
        this.fixWarnings();
    }
    
    @Override
    public void resetAndPause()
    {
        this.paused = true;
        this.matchDuration = 0;
        this.fixWarnings();
    }
    
    @Override
    public long getDurationMillis()
    {
        if (this.paused)
        {
            return this.matchDuration;
        }
        return this.matchDuration + this.lastStart.until(LocalDateTime.now(), ChronoUnit.MILLIS);
    }
    
    @Override
    public long getMaxMillis()
    {
        return this.matchTime;
    }
    
    @Override
    public void addMaxMillis(long millis)
    {
        this.matchTime += millis;
        if (this.matchTime > 0 && this.timerTask == null)
        {
            this.startTimer();
        }
        this.fixWarnings();
    }
    
    @Override
    public void substractMaxMillis(long millis)
    {
        this.matchTime -= millis;
        if (this.matchTime <= 0)
        {
            this.stopTimer();
        }
        this.fixWarnings();
    }
    
    @Override
    public void setMaxMillis(long millis)
    {
        this.matchTime = millis;
        if (this.matchTime <= 0)
        {
            this.stopTimer();
        }
        this.fixWarnings();
    }
    
    @Override
    public void addDurationMillis(long millis)
    {
        this.matchDuration += millis;
        this.fixWarnings();
    }
    
    @Override
    public void substractDurationMillis(long millis)
    {
        this.matchDuration -= millis;
        this.fixWarnings();
    }
    
    @Override
    public void setDurationMillis(long millis)
    {
        this.matchDuration = millis;
        this.fixWarnings();
    }
    
    /**
     * Arena state change
     * 
     * @param evt
     */
    @McEventHandler
    public void onArenaState(ArenaStateChangedEvent evt)
    {
        if (evt.getNewState() == ArenaState.Match)
        {
            this.matchTime = this.seconds * 1000L;
            this.matchDuration = 0;
            this.lastStart = LocalDateTime.now();
            this.paused = false;
            if (this.matchTime > 0)
            {
                this.startTimer();
            }
            this.fixWarnings();
        }
        else
        {
            this.stopTimer();
        }
    }
    
    /**
     * Starts the bukkit timer task
     */
    private void startTimer()
    {
        if (this.timerTask == null)
        {
            this.timerTask = McLibInterface.instance().runTaskTimer(MinigamesPlugin.instance().getPlugin(), 20, 20, this::onTimer);
        }
    }
    
    /**
     * Stops the bukkit timer task
     */
    private void stopTimer()
    {
        if (this.timerTask != null)
        {
            this.timerTask.cancel();
            this.timerTask = null;
        }
    }
    
    /**
     * Fixes warnings depending on remaining game time
     */
    private void fixWarnings()
    {
        long delta = this.getMaxMillis() - this.getDurationMillis();
        final int sec = (int) (delta / 1000);
        this.warn60 = sec >= 60;
        this.warn30 = sec >= 30;
        this.warn20 = sec >= 20;
        this.warn10 = sec >= 10;
        this.warn5 = sec >= 5;
        this.warn4 = sec >= 4;
        this.warn3 = sec >= 3;
        this.warn2 = sec >= 2;
        this.warn1 = sec >= 1;
    }
    
    /**
     * countdown tick.
     * 
     * @param msg
     *            message to send
     * @param sec
     *            seconds to send
     */
    private void onCountdown(LocalizedMessageInterface msg, int sec)
    {
        this.arena.getPlayers().forEach(p -> {
            p.getMcPlayer().sendMessage(msg, sec);
            if (this.playSound)
            {
                p.getBukkitPlayer().playSound(p.getBukkitPlayer().getLocation(), this.sound, 1F, 0F);
            }
        });
        this.arena.getSpectators().forEach(p -> {
            p.getMcPlayer().sendMessage(msg, sec);
            if (this.playSound)
            {
                p.getBukkitPlayer().playSound(p.getBukkitPlayer().getLocation(), this.sound, 1F, 0F);
            }
        });
    }
    
    /**
     * On timer tick
     * 
     * @param task
     */
    private void onTimer(BukkitTask task)
    {
        if (!this.paused)
        {
            long delta = this.getMaxMillis() - this.getDurationMillis();
            if (delta <= 0)
            {
                this.timerTask.cancel();
                this.timerTask = null;
                try
                {
                    this.arena.getPlayers().forEach(p -> p.getMcPlayer().sendMessage(Messages.Abort));
                    this.arena.getSpectators().forEach(p -> p.getMcPlayer().sendMessage(Messages.Abort));
                    this.arena.abort();
                }
                catch (McException e)
                {
                    LOGGER.log(Level.WARNING, "Unable to abort arena after reaching maximum game time", e); //$NON-NLS-1$
                }
            }
            else
            {
                final int sec = (int) (delta / 1000);
                if (this.warn60 && sec <= 60)
                {
                    this.onCountdown(Messages.WarnSeconds, 60);
                    this.warn60 = false;
                }
                else if (this.warn30 && sec <= 30)
                {
                    this.onCountdown(Messages.WarnSeconds, 30);
                    this.warn30 = false;
                }
                else if (this.warn20 && sec <= 20)
                {
                    this.onCountdown(Messages.WarnSeconds, 20);
                    this.warn20 = false;
                }
                else if (this.warn10 && sec <= 10)
                {
                    this.onCountdown(Messages.WarnSeconds, 10);
                    this.warn10 = false;
                }
                else if (this.warn5 && sec <= 5)
                {
                    this.onCountdown(Messages.WarnSeconds, 5);
                    this.warn5 = false;
                }
                else if (this.warn4 && sec <= 4)
                {
                    this.onCountdown(Messages.WarnSeconds, 4);
                    this.warn4 = false;
                }
                else if (this.warn3 && sec <= 3)
                {
                    this.onCountdown(Messages.WarnSeconds, 3);
                    this.warn3 = false;
                }
                else if (this.warn2 && sec <= 2)
                {
                    this.onCountdown(Messages.WarnSeconds, 2);
                    this.warn2 = false;
                }
                else if (this.warn1 && sec <= 1)
                {
                    this.onCountdown(Messages.WarnSeconds, 1);
                    this.warn1 = false;
                }
            }
        }
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "rules.BasicMatchTimer")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Warning of match timer
         */
        @LocalizedMessage(defaultMessage = "The match is stopping in %1$d seconds because the maximum game time was reached.", severity = MessageSeverityType.Warning)
        @MessageComment(value = { "Warning of match timer" }, args = { @Argument(type = "Numeric", value = "Remaining seconds") })
        WarnSeconds,
        
        /**
         * match timer expired
         */
        @LocalizedMessage(defaultMessage = "The match is stopping now because the maximum game time was reached.", severity = MessageSeverityType.Loser)
        @MessageComment(value = { "match timer expired" })
        Abort,
        
    }
    
}
