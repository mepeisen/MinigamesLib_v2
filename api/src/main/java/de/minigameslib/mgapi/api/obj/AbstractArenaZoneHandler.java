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
import java.util.stream.Collectors;

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
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.EntityIdInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface.CuboidMode;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mgapi.api.MinigamesLibInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.arena.CheckSeverity;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetInterface;
import de.minigameslib.mgapi.api.rules.ZoneRuleSetType;

/**
 * Base class for arena zone handlers.
 * 
 * @author mepeisen
 * @param <D> data class
 */
public abstract class AbstractArenaZoneHandler<D extends AbstractObjectData<ZoneRuleSetType>> extends AbstractBaseArenaObjectHandler<ZoneRuleSetType, ZoneRuleSetInterface, D> implements ArenaZoneHandler
{
    
    /** the underlying zone. */
    protected ZoneInterface zone;

    @Override
    public void initArena(ArenaInterface a) throws McException
    {
        super.initArena(a);
        this.dataFile = new File(a.getDataFolder(), "/" + this.zone.getZoneId() + ".yml"); //$NON-NLS-1$ //$NON-NLS-2$
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
    public void onCreate(ZoneInterface c) throws McException
    {
        this.zone = c;
    }

    @Override
    public void onResume(ZoneInterface c) throws McException
    {
        this.zone = c;
    }

    @Override
    public void onPause(ZoneInterface c)
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
    public void canChangeCuboid(Cuboid newValue) throws McException
    {
        this.checkModifications();
    }

    @Override
    public void onCuboidChange(Cuboid newValue)
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
    protected void applyListeners(ZoneRuleSetInterface listeners)
    {
        this.zone.registerHandlers(this.getPlugin(), listeners);
    }

    @Override
    protected void removeListeners(ZoneRuleSetInterface listeners)
    {
        this.zone.unregisterHandlers(this.getPlugin(), listeners);
    }
    
    @Override
    public Collection<ZoneRuleSetType> getAvailableRuleSetTypes()
    {
        final Set<ZoneRuleSetType> result = MinigamesLibInterface.instance().getOptionalRuleSets(this.getArena(), this.getZone());
        result.removeAll(this.getAppliedRuleSetTypes());
        return result;
    }

    @Override
    protected ZoneRuleSetInterface create(ZoneRuleSetType ruleset) throws McException
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
            McLibInterface.instance().setContext(ArenaZoneHandler.class, this);
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
            McLibInterface.instance().setContext(ArenaZoneHandler.class, this);
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
            McLibInterface.instance().setContext(ArenaZoneHandler.class, this);
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
            McLibInterface.instance().setContext(ArenaZoneHandler.class, this);
            return runnable.get();
        });
    }

    @Override
    public ZoneInterface getZone()
    {
        return this.zone;
    }
    
    @Override
    public Collection<CheckFailure> check()
    {
        final Collection<CheckFailure> result = super.check();
        
        final Cuboid cuboid = this.getZone().getCuboid();

        final Collection<ZoneIdInterface> myMainZones = this.getArena().getZones(cuboid, CuboidMode.FindParents, BasicZoneTypes.Main)
                // filter myself (relevant only for main zones to not return themselves)
                .stream().filter(z -> !z.equals(this.getZone().getZoneId())).collect(Collectors.toList());
        
        final Collection<ZoneIdInterface> foreignZones = this.getArena().getForeignZones(cuboid, CuboidMode.FindShared);
        final Collection<ComponentIdInterface> foreignComponents = this.getArena().getForeignComponents(cuboid);
        final Collection<SignIdInterface> foreignSigns = this.getArena().getForeignSigns(cuboid);
        final Collection<EntityIdInterface> foreignEntities = this.getArena().getForeignEntities(cuboid);

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
    @LocalizedMessages(value = "zones.Base")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * not within main zone.
         */
        @LocalizedMessage(defaultMessage = "zone '%1$s' not within main zone!", severity = MessageSeverityType.Warning)
        @MessageComment(value = "not within main zone", args = {@Argument("this zone name")})
        NotWithinMainZone,
        
        /**
         * not within main zone.
         */
        @LocalizedMessageList({
            "Your zone is not within a main zone.",
            "The arena will work but if you decide to export or relocate your arena this will not affect this zone.",
            "After importing the arena it may be broken."})
        @MessageComment("not within main zone")
        NotWithinMainZone_Description,
        
        /**
         * foreign zone detected.
         */
        @LocalizedMessage(defaultMessage = "zone '%1$s' shares locations with foreign zone '%2$s@%3$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "foreign zone detected", args = {@Argument("this zone name"), @Argument("foreign zone name"), @Argument("foreign arena name")})
        ForeignZone,
        
        /**
         * foreign zone detected.
         */
        @LocalizedMessageList({
            "Your zone is set inside another zone from some foreign arena.",
            "Arena zones must not be located inside foreign arena zones because the rules can get confused."})
        @MessageComment("foreign zone detected")
        ForeignZone_Description,
        
        /**
         * foreign component detected.
         */
        @LocalizedMessage(defaultMessage = "zone '%1$s' contains foreign component '%2$s@%3$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "foreign component detected", args = {@Argument("this zone name"), @Argument("foreign component name"), @Argument("foreign arena name")})
        ForeignComponent,
        
        /**
         * foreign component detected.
         */
        @LocalizedMessageList({
            "Your zone contains a component from some foreign arena.",
            "Arena zones must not contain foreign arena components because the rules can get confused."})
        @MessageComment("foreign component detected")
        ForeignComponent_Description,
        
        /**
         * foreign sign detected.
         */
        @LocalizedMessage(defaultMessage = "zone '%1$s' contains foreign sign '%2$s@%3$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "foreign sign detected", args = {@Argument("this zone name"), @Argument("foreign sign name"), @Argument("foreign arena name")})
        ForeignSign,
        
        /**
         * foreign sign detected.
         */
        @LocalizedMessageList({
            "Your zone contains a sign from some foreign arena.",
            "Arena zones must not contain foreign arena signs because the rules can get confused."})
        @MessageComment("foreign sign detected")
        ForeignSign_Description,
        
        /**
         * foreign entity detected.
         */
        @LocalizedMessage(defaultMessage = "zone '%1$s' contains foreign entity '%2$s@%3$s'!", severity = MessageSeverityType.Error)
        @MessageComment(value = "foreign entity detected", args = {@Argument("this zone name"), @Argument("foreign entity name"), @Argument("foreign arena name")})
        ForeignEntity,
        
        /**
         * foreign entity detected.
         */
        @LocalizedMessageList({
            "Your zone contains an entity from some foreign arena.",
            "Arena zones must not contain foreign arena entities because the rules can get confused."})
        @MessageComment("foreign entity detected")
        ForeignEntity_Description,
        
    }
    
}
