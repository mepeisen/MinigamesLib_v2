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

package de.minigameslib.mgapi.api.obj;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;

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
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.EntityIdInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.arena.CheckSeverity;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerJoinedSpectatorsEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerLeftEvent;
import de.minigameslib.mgapi.api.events.ArenaPlayerLeftSpectatorsEvent;
import de.minigameslib.mgapi.api.rules.SignRuleSetInterface;
import de.minigameslib.mgapi.api.rules.SignRuleSetType;

/**
 * Base class for sign handlers.
 * 
 * @author mepeisen
 * @param <D>
 *            data class
 */
public abstract class AbstractArenaSignHandler<D extends AbstractObjectData<SignRuleSetType>> extends AbstractBaseArenaObjectHandler<SignRuleSetType, SignRuleSetInterface, D>
        implements ArenaSignHandler
{
    
    /** the underlying sign. */
    protected SignInterface sign;
    
    @Override
    public void initArena(ArenaInterface a) throws McException
    {
        super.initArena(a);
        this.dataFile = new File(a.getDataFolder(), "/" + this.sign.getSignId() + ".yml"); //$NON-NLS-1$ //$NON-NLS-2$
        if (this.dataFile.exists())
        {
            this.loadData();
        }
        else
        {
            this.saveData();
        }
        this.updateSign();
    }
    
    // TODO clear out which events cause a sign update
    
    /**
     * Player joined event
     * 
     * @param evt
     */
    @McEventHandler
    public void onPlayerJoin(ArenaPlayerJoinedEvent evt)
    {
        if (evt.getArena() == this.getArena())
        {
            this.updateSign();
        }
    }
    
    /**
     * Player joined event
     * 
     * @param evt
     */
    @McEventHandler
    public void onPlayerSpecsJoin(ArenaPlayerJoinedSpectatorsEvent evt)
    {
        if (evt.getArena() == this.getArena())
        {
            this.updateSign();
        }
    }
    
    /**
     * Player left event
     * 
     * @param evt
     */
    @McEventHandler
    public void onPlayerLeft(ArenaPlayerLeftEvent evt)
    {
        if (evt.getArena() == this.getArena())
        {
            this.updateSign();
        }
    }
    
    /**
     * Player left event
     * 
     * @param evt
     */
    @McEventHandler
    public void onPlayerLeftSpecs(ArenaPlayerLeftSpectatorsEvent evt)
    {
        if (evt.getArena() == this.getArena())
        {
            this.updateSign();
        }
    }
    
    /**
     * Returns the sign text to set.
     * 
     * @return sign text
     */
    protected abstract String[] getLines();
    
    /**
     * Updates the sign text.
     */
    protected void updateSign()
    {
        final String[] lines = this.getLines();
        for (int i = 0; i < 4; i++)
        {
            if (i < lines.length)
            {
                this.sign.setLine(i, lines[i]);
            }
            else
            {
                this.sign.setLine(i, ""); //$NON-NLS-1$
            }
        }
    }
    
    @Override
    public void onCreate(SignInterface c) throws McException
    {
        this.sign = c;
    }
    
    @Override
    public void onResume(SignInterface c) throws McException
    {
        this.sign = c;
    }
    
    @Override
    public void onPause(SignInterface c)
    {
        // do nothing
    }
    
    @Override
    public void canDelete() throws McException
    {
        this.checkModifications();
    }
    
    @Override
    public void onDelete()
    {
        if (this.dataFile.exists())
        {
            this.dataFile.delete();
        }
    }
    
    @Override
    public void read(DataSection section)
    {
        // no additional data in mclib files
    }
    
    @Override
    public void write(DataSection section)
    {
        // no additional data in mclib files
    }
    
    @Override
    public boolean test(DataSection section)
    {
        // no additional data in mclib files
        return true;
    }
    
    @Override
    protected void applyListeners(SignRuleSetInterface listeners)
    {
        this.sign.registerHandlers(this.getPlugin(), listeners);
    }
    
    @Override
    protected void removeListeners(SignRuleSetInterface listeners)
    {
        this.sign.unregisterHandlers(this.getPlugin(), listeners);
    }
    
    @Override
    public Collection<SignRuleSetType> getAvailableRuleSetTypes()
    {
        final Set<SignRuleSetType> result = MinigamesLibInterface.instance().getOptionalRuleSets(this.getArena(), this.getSign());
        result.removeAll(this.getAppliedRuleSetTypes());
        return result;
    }

    @Override
    protected SignRuleSetInterface create(SignRuleSetType ruleset) throws McException
    {
        return calculateInCopiedContext(() -> {
            return MinigamesLibInterface.instance().creator(ruleset).apply(ruleset, this);
        });
    }
    
    /**
     * Runs the code in new context; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @throws McException
     *             rethrown from runnable.
     */
    void runInNewContext(McRunnable runnable) throws McException
    {
        McLibInterface.instance().runInNewContext(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.arena);
            McLibInterface.instance().setContext(ArenaSignHandler.class, this);
            runnable.run();
        });
    }
    
    /**
     * Runs the code in new context but copies all context variables before; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @throws McException
     *             rethrown from runnable.
     */
    void runInCopiedContext(McRunnable runnable) throws McException
    {
        McLibInterface.instance().runInCopiedContext(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.arena);
            McLibInterface.instance().setContext(ArenaSignHandler.class, this);
            runnable.run();
        });
    }
    
    /**
     * Runs the code in new context; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @return result from runnable
     * @throws McException
     *             rethrown from runnable.
     * @param <T>
     *            Type of return value
     */
    <T> T calculateInNewContext(McSupplier<T> runnable) throws McException
    {
        return McLibInterface.instance().calculateInNewContext(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.arena);
            McLibInterface.instance().setContext(ArenaSignHandler.class, this);
            return runnable.get();
        });
    }
    
    /**
     * Runs the code but copies all context variables before; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @return result from runnable
     * @throws McException
     *             rethrown from runnable.
     * @param <T>
     *            Type of return value
     */
    <T> T calculateInCopiedContext(McSupplier<T> runnable) throws McException
    {
        return McLibInterface.instance().calculateInCopiedContext(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.arena);
            McLibInterface.instance().setContext(ArenaSignHandler.class, this);
            return runnable.get();
        });
    }
    
    @Override
    public SignInterface getSign()
    {
        return this.sign;
    }
    
    @Override
    public void canChangeLocation(Location newValue) throws McException
    {
        this.checkModifications();
    }
    
    @Override
    public void onLocationChange(Location newValue)
    {
        // do nothing
    }
    
    @Override
    public Collection<CheckFailure> check()
    {
        final Collection<CheckFailure> result = super.check();
        
        final Location loc = this.getSign().getLocation();
        
        final Collection<ZoneIdInterface> myMainZones = this.getArena().getZones(loc, BasicZoneTypes.Main);
        final Collection<SignIdInterface> mySigns = this.getArena().getSigns(loc)
                // filter myself
                .stream().filter(z -> !z.equals(this.getSign().getSignId())).collect(Collectors.toList());
        
        final Collection<ZoneIdInterface> foreignZones = this.getArena().getForeignZones(loc);
        final Collection<ComponentIdInterface> foreignComponents = this.getArena().getForeignComponents(loc);
        final Collection<SignIdInterface> foreignSigns = this.getArena().getForeignSigns(loc);
        final Collection<EntityIdInterface> foreignEntities = this.getArena().getForeignEntities(loc);
        
        if (myMainZones.size() == 0)
        {
            result.add(new CheckFailure(CheckSeverity.Warning, Messages.NotWithinMainZone, new Serializable[] { this.getName() }, Messages.NotWithinMainZone_Description));
        }
        for (final SignIdInterface id : mySigns)
        {
            final ArenaSignHandler handler = this.getArena().getHandler(id);
            result.add(new CheckFailure(CheckSeverity.Error, Messages.SharedSign, new Serializable[] { this.getName(), handler.getName() }, Messages.SharedSign_Description));
        }
        
        for (final ZoneIdInterface id : foreignZones)
        {
            final ArenaZoneHandler handler = this.getArena().getHandler(id);
            result.add(new CheckFailure(CheckSeverity.Error, Messages.ForeignZone, new Serializable[] { this.getName(), handler.getName(), handler.getArena().getInternalName() },
                    Messages.ForeignZone_Description));
        }
        for (final ComponentIdInterface id : foreignComponents)
        {
            final ArenaComponentHandler handler = this.getArena().getHandler(id);
            result.add(new CheckFailure(CheckSeverity.Error, Messages.ForeignComponent, new Serializable[] { this.getName(), handler.getName(), handler.getArena().getInternalName() },
                    Messages.ForeignComponent_Description));
        }
        for (final SignIdInterface id : foreignSigns)
        {
            final ArenaSignHandler handler = this.getArena().getHandler(id);
            result.add(new CheckFailure(CheckSeverity.Error, Messages.ForeignSign, new Serializable[] { this.getName(), handler.getName(), handler.getArena().getInternalName() },
                    Messages.ForeignSign_Description));
        }
        for (final EntityIdInterface id : foreignEntities)
        {
            // TODO support arena entities
            // final ArenaEntityHandler handler = this.getArena().getHandler(id);
            // result.add(new CheckFailure(
            // CheckSeverity.Error,
            // Messages.ForeignEntity,
            // new Serializable[]{this.getName(), handler.getName(), handler.getArena().getInternalName()},
            // Messages.ForeignEntity_Description));
        }
        
        return result;
    }
    
    /**
     * Parses given line, replaces variables and returns a line to be used by signs.
     * 
     * @param lineFromConfig
     *            the line stored in config data.
     * @param defaultValue
     *            the default value to be used for this line.
     * @return the resulting line text.
     */
    protected String parseLine(String lineFromConfig, String defaultValue)
    {
        // TODO move to mclib, a generic lightweight placeholder api
        final String src = lineFromConfig == null ? defaultValue : lineFromConfig;
        if ("~".equals(src)) return "----------"; //$NON-NLS-1$ //$NON-NLS-2$
        int pos = 0;
        int len = src.length();
        final StringBuilder result = new StringBuilder();
        while (pos < len)
        {
            char c = src.charAt(pos);
            pos++;
            switch (c)
            {
                case '%':
                    int indexOf = src.indexOf('%', pos);
                    if (indexOf == -1)
                    {
                        indexOf = src.length();
                    }
                    final String var = src.substring(pos, indexOf - 1);
                    pos = indexOf + 1;
                    switch (var)
                    {
                        case "arena.name": //$NON-NLS-1$
                            result.append(this.getArena().getDisplayName());
                            break;
                        case "sys.datetime": //$NON-NLS-1$
                            result.append(LocalDateTime.now().toString());
                            break;
                        default:
                            result.append('?').append(var).append('?');
                            break;
                    }
                    break;
                default:
                    result.append(c);
                    break;
            }
        }
        return result.toString();
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "signs.Base")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * not within main zone.
         */
        @LocalizedMessage(defaultMessage = "sign '%1$s' not within main zone!", severity = MessageSeverityType.Warning)
        @MessageComment(value = "not within main zone", args = { @Argument("this sign name") })
        NotWithinMainZone,
        
        /**
         * not within main zone.
         */
        @LocalizedMessageList({ "Your sign is not within a main zone.", "The arena will work but if you decide to export or relocate your arena this will not affect this component.", "After importing the arena it may be broken." })
        @MessageComment("not within main zone")
        NotWithinMainZone_Description,
        
        /**
         * Shared sign detected.
         */
        @LocalizedMessage(defaultMessage = "sign '%1$s' shares location with sign '%2$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "Shared sign detected", args = { @Argument("this sign name"), @Argument("other sign name") })
        SharedSign,
        
        /**
         * Shared sign detected.
         */
        @LocalizedMessageList({ "Your sign shares location with second sign.", "Although technical possible two signs cannot share the same location.", "It will be very confusing to choose which sign is responsible for updating the sign text." })
        @MessageComment("shared sign detected.")
        SharedSign_Description,
        
        /**
         * foreign zone detected.
         */
        @LocalizedMessage(defaultMessage = "sign '%1$s' is placed within foreign zone '%2$s@%3$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "foreign zone detected", args = { @Argument("this sign name"), @Argument("foreign zone name"), @Argument("foreign arena name") })
        ForeignZone,
        
        /**
         * foreign zone detected.
         */
        @LocalizedMessageList({ "Your sign is set inside another zone from some foreign arena.", "Arena signs must not be located inside foreign arena zones because the rules can get confused." })
        @MessageComment("foreign zone detected")
        ForeignZone_Description,
        
        /**
         * foreign component detected.
         */
        @LocalizedMessage(defaultMessage = "sign '%1$s' shares location with component '%2$s@%3$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "foreign component detected", args = { @Argument("this sign name"), @Argument("foreign component name"), @Argument("foreign arena name") })
        ForeignComponent,
        
        /**
         * foreign component detected.
         */
        @LocalizedMessageList({ "Your sign shares location with component from some foreign arena.", "Arena signs must not share their location with foreign components because the rules can get confused." })
        @MessageComment("foreign component detected")
        ForeignComponent_Description,
        
        /**
         * foreign sign detected.
         */
        @LocalizedMessage(defaultMessage = "sign '%1$s' shares location with sign '%2$s@%3$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "foreign sign detected", args = { @Argument("this sign name"), @Argument("foreign sign name"), @Argument("foreign arena name") })
        ForeignSign,
        
        /**
         * foreign sign detected.
         */
        @LocalizedMessageList({ "Your sign shares location with sign from some foreign arena.", "Arena signs must not share their location with foreign signs because the rules can get confused." })
        @MessageComment("foreign sign detected")
        ForeignSign_Description,
        
        /**
         * foreign entity detected.
         */
        @LocalizedMessage(defaultMessage = "sign '%1$s' shares location with entity '%2$s@%3$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "foreign entity detected", args = { @Argument("this sign name"), @Argument("foreign entity name"), @Argument("foreign arena name") })
        ForeignEntity,
        
        /**
         * foreign entity detected.
         */
        @LocalizedMessageList({ "Your sign shares location with entity from some foreign arena.", "Arena signs must not share their location with foreign entities because the rules can get confused." })
        @MessageComment("foreign entity detected")
        ForeignEntity_Description,
        
    }
    
}
