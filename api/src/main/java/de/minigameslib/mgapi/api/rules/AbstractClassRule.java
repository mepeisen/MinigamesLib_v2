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

package de.minigameslib.mgapi.api.rules;

import java.util.ArrayList;
import java.util.Collection;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.api.util.function.McSupplier;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.CheckFailure;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;

/**
 * Abstract base class for rule sets
 * 
 * @author mepeisen
 */
public abstract class AbstractClassRule implements ClassRuleSetInterface
{
    
    /**
     * the underlying player.
     */
    protected final ArenaPlayerInterface   player;
    
    /**
     * rule set type.
     */
    protected final ClassRuleSetType type;
    
    /**
     * @param type
     * @param player
     * @throws McException
     *             thrown if config is invalid
     */
    public AbstractClassRule(ClassRuleSetType type, ArenaPlayerInterface player) throws McException
    {
        this.type = type;
        this.player = player;
    }
    
    @Override
    public ClassRuleSetType getType()
    {
        return this.type;
    }
    
    @Override
    public ArenaPlayerInterface getPlayer()
    {
        return this.player;
    }
    
    /**
     * Runs the code in new context; changes made inside the runnable will be undone.
     * 
     * @param runnable
     *            the runnable to execute.
     * @throws McException
     *             rethrown from runnable.
     */
    protected void runInNewContext(McRunnable runnable) throws McException
    {
        McLibInterface.instance().runInNewContext(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.player.getArena());
            McLibInterface.instance().setContext(McPlayerInterface.class, this.player.getMcPlayer());
            McLibInterface.instance().setContext(ArenaPlayerInterface.class, this.player);
            McLibInterface.instance().setContext(ClassRuleSetInterface.class, this);
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
    protected void runInCopiedContext(McRunnable runnable) throws McException
    {
        McLibInterface.instance().runInCopiedContext(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.player.getArena());
            McLibInterface.instance().setContext(McPlayerInterface.class, this.player.getMcPlayer());
            McLibInterface.instance().setContext(ArenaPlayerInterface.class, this.player);
            McLibInterface.instance().setContext(ClassRuleSetInterface.class, this);
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
    protected <T> T calculateInNewContext(McSupplier<T> runnable) throws McException
    {
        return McLibInterface.instance().calculateInNewContext(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.player.getArena());
            McLibInterface.instance().setContext(McPlayerInterface.class, this.player.getMcPlayer());
            McLibInterface.instance().setContext(ArenaPlayerInterface.class, this.player);
            McLibInterface.instance().setContext(ClassRuleSetInterface.class, this);
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
    protected <T> T calculateInCopiedContext(McSupplier<T> runnable) throws McException
    {
        return McLibInterface.instance().calculateInCopiedContext(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.player.getArena());
            McLibInterface.instance().setContext(McPlayerInterface.class, this.player.getMcPlayer());
            McLibInterface.instance().setContext(ArenaPlayerInterface.class, this.player);
            McLibInterface.instance().setContext(ClassRuleSetInterface.class, this);
            return runnable.get();
        });
    }
    
    @Override
    public Collection<CheckFailure> check()
    {
        return new ArrayList<>();
    }
    
}
