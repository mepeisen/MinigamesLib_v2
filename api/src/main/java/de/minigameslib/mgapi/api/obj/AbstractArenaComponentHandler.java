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
import java.util.Collection;
import java.util.Set;

import org.bukkit.Location;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageList;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageComment.Argument;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.EntityIdInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.arena.CheckSeverity;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ComponentRuleSetType;

/**
 * Base class for component handlers.
 * 
 * @author mepeisen
 * @param <D> data class
 */
public abstract class AbstractArenaComponentHandler<D extends AbstractObjectData<ComponentRuleSetType>> extends AbstractBaseArenaObjectHandler<ComponentRuleSetType, ComponentRuleSetInterface, D> implements ArenaComponentHandler
{
    
    /** the underlying component. */
    protected ComponentInterface component;

    @Override
    public void initArena(ArenaInterface a) throws McException
    {
        super.initArena(a);
        this.dataFile = new File(a.getDataFolder(), "/" + this.component.getComponentId() + ".yml"); //$NON-NLS-1$ //$NON-NLS-2$
        if (this.dataFile.exists())
        {
            this.loadData();
        }
        else
        {
            this.saveData();
        }
    }

    @Override
    public void onCreate(ComponentInterface c) throws McException
    {
        this.component = c;
    }

    @Override
    public void onResume(ComponentInterface c) throws McException
    {
        this.component = c;
    }

    @Override
    public void onPause(ComponentInterface c)
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
    protected void applyListeners(ComponentRuleSetInterface listeners)
    {
        this.component.registerHandlers(this.getPlugin(), listeners);
    }

    @Override
    protected void removeListeners(ComponentRuleSetInterface listeners)
    {
        this.component.unregisterHandlers(this.getPlugin(), listeners);
    }
    
    @Override
    public Collection<ComponentRuleSetType> getAvailableRuleSetTypes()
    {
        final Set<ComponentRuleSetType> result = MinigamesLibInterface.instance().getOptionalRuleSets(this.getArena(), this.getComponent());
        result.removeAll(this.getAppliedRuleSetTypes());
        return result;
    }

    @Override
    protected ComponentRuleSetInterface create(ComponentRuleSetType ruleset) throws McException
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
            McLibInterface.instance().setContext(ArenaComponentHandler.class, this);
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
            McLibInterface.instance().setContext(ArenaComponentHandler.class, this);
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
            McLibInterface.instance().setContext(ArenaComponentHandler.class, this);
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
            McLibInterface.instance().setContext(ArenaComponentHandler.class, this);
            return runnable.get();
        });
    }

    @Override
    public ComponentInterface getComponent()
    {
        return this.component;
    }
    
    @Override
    public Collection<CheckFailure> check()
    {
        final Collection<CheckFailure> result = super.check();
        
        final Location loc = this.getComponent().getLocation();

        final Collection<ZoneIdInterface> myMainZones = this.getArena().getZones(loc, BasicZoneTypes.Main);
        
        final Collection<ZoneIdInterface> foreignZones = this.getArena().getForeignZones(loc);
        final Collection<ComponentIdInterface> foreignComponents = this.getArena().getForeignComponents(loc);
        final Collection<SignIdInterface> foreignSigns = this.getArena().getForeignSigns(loc);
        final Collection<EntityIdInterface> foreignEntities = this.getArena().getForeignEntities(loc);
        
        if (myMainZones.size() == 0)
        {
            result.add(new CheckFailure(CheckSeverity.Warning, Messages.NotWithinMainZone, new Serializable[]{this.getName()}, Messages.NotWithinMainZone_Description));
        }
        
        for (final ZoneIdInterface id : foreignZones)
        {
            final ArenaZoneHandler handler = this.getArena().getHandler(id);
            result.add(new CheckFailure(
                    CheckSeverity.Error,
                    Messages.ForeignZone,
                    new Serializable[]{this.getName(), handler.getName(), handler.getArena().getInternalName()},
                    Messages.ForeignZone_Description));
        }
        for (final ComponentIdInterface id : foreignComponents)
        {
            final ArenaComponentHandler handler = this.getArena().getHandler(id);
            result.add(new CheckFailure(
                    CheckSeverity.Error,
                    Messages.ForeignComponent,
                    new Serializable[]{this.getName(), handler.getName(), handler.getArena().getInternalName()},
                    Messages.ForeignComponent_Description));
        }
        for (final SignIdInterface id : foreignSigns)
        {
            final ArenaSignHandler handler = this.getArena().getHandler(id);
            result.add(new CheckFailure(
                    CheckSeverity.Error,
                    Messages.ForeignSign,
                    new Serializable[]{this.getName(), handler.getName(), handler.getArena().getInternalName()},
                    Messages.ForeignSign_Description));
        }
        for (final EntityIdInterface id : foreignEntities)
        {
            // TODO support arena entities
//            final ArenaEntityHandler handler = this.getArena().getHandler(id);
//            result.add(new CheckFailure(
//                    CheckSeverity.Error,
//                    Messages.ForeignEntity,
//                    new Serializable[]{this.getName(), handler.getName(), handler.getArena().getInternalName()},
//                    Messages.ForeignEntity_Description));
        }
        
        return result;
    }
    
    /**
     * The common messages.
     * 
     * @author mepeisen
     */
    @LocalizedMessages(value = "components.Base")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * not within main zone.
         */
        @LocalizedMessage(defaultMessage = "component '%1$s' not within main zone!", severity = MessageSeverityType.Warning)
        @MessageComment(value = "not within main zone", args = {@Argument("this component name")})
        NotWithinMainZone,
        
        /**
         * not within main zone.
         */
        @LocalizedMessageList({
            "Your component is not within a main zone.",
            "The arena will work but if you decide to export or relocate your arena this will not affect this component.",
            "After importing the arena it may be broken."})
        @MessageComment("not within main zone")
        NotWithinMainZone_Description,
        
        /**
         * foreign zone detected.
         */
        @LocalizedMessage(defaultMessage = "component '%1$s' is placed within foreign zone '%2$s@%3$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "foreign zone detected", args = {@Argument("this component name"), @Argument("foreign zone name"), @Argument("foreign arena name")})
        ForeignZone,
        
        /**
         * foreign zone detected.
         */
        @LocalizedMessageList({
            "Your component is set inside another zone from some foreign arena.",
            "Arena components must not be located inside foreign arena zones because the rules can get confused."})
        @MessageComment("foreign zone detected")
        ForeignZone_Description,
        
        /**
         * foreign component detected.
         */
        @LocalizedMessage(defaultMessage = "component '%1$s' shares location with component '%2$s@%3$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "foreign component detected", args = {@Argument("this component name"), @Argument("foreign component name"), @Argument("foreign arena name")})
        ForeignComponent,
        
        /**
         * foreign component detected.
         */
        @LocalizedMessageList({
            "Your component shares location with component from some foreign arena.",
            "Arena components must not share their location with foreign components because the rules can get confused."})
        @MessageComment("foreign component detected")
        ForeignComponent_Description,
        
        /**
         * foreign sign detected.
         */
        @LocalizedMessage(defaultMessage = "component '%1$s' shares location with sign '%2$s@%3$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "foreign sign detected", args = {@Argument("this component name"), @Argument("foreign sign name"), @Argument("foreign arena name")})
        ForeignSign,
        
        /**
         * foreign sign detected.
         */
        @LocalizedMessageList({
            "Your component shares location with sign from some foreign arena.",
            "Arena components must not share their location with foreign signs because the rules can get confused."})
        @MessageComment("foreign sign detected")
        ForeignSign_Description,
        
        /**
         * foreign entity detected.
         */
        @LocalizedMessage(defaultMessage = "component '%1$s' shares location with entity '%2$s@%3$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "foreign entity detected", args = {@Argument("this component name"), @Argument("foreign entity name"), @Argument("foreign arena name")})
        ForeignEntity,
        
        /**
         * foreign entity detected.
         */
        @LocalizedMessageList({
            "Your component shares location with entity from some foreign arena.",
            "Arena components must not share their location with foreign entities because the rules can get confused."})
        @MessageComment("foreign entity detected")
        ForeignEntity_Description,
        
    }
    
}
