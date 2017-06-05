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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.arena.ArenaState;
import de.minigameslib.mgapi.api.obj.AbstractArenaSignHandler;
import de.minigameslib.mgapi.api.obj.ArenaSignHandler;
import de.minigameslib.mgapi.api.obj.LineConfig;
import de.minigameslib.mgapi.impl.MinigamesPlugin;

/**
 * @author mepeisen
 * @param <D>
 *            config class
 *
 */
public abstract class AbstractProgrammableSign<D extends AbstractProgrammableSignData> extends AbstractArenaSignHandler<D>
{
    
    /**
     * Returns the lines.
     * 
     * @return lines.
     */
    public Collection<LineConfig> getLines()
    {
        return new ArrayList<>(this.data.getLines());
    }
    
    /**
     * Sets the line config with given state in {@link LineConfig#getState()}.
     * 
     * @param config
     *            config
     * @throws McException
     *             thrown if there are problems saving the config
     */
    public void setLine(LineConfig config) throws McException
    {
        final Iterator<LineConfig> iter = this.data.getLines().iterator();
        while (iter.hasNext())
        {
            if (iter.next().getState() == config.getState())
            {
                iter.remove();
                break;
            }
        }
        this.data.getLines().add(config);
        this.sign.saveConfig();
        
        this.updateSign();
    }
    
    @Override
    protected Serializable[] getCurrentLines()
    {
        return new Serializable[] { line(0), line(1), line(2), line(3) };
    }
    
    /**
     * Serialize line with given index.
     * 
     * @param index
     * @return line as serializable.
     */
    private Serializable line(int index)
    {
        return McLibInterface.instance().calculateInCopiedContextUnchecked(() -> {
            McLibInterface.instance().setContext(ArenaInterface.class, this.getArena());
            McLibInterface.instance().setContext(ArenaSignHandler.class, this);
            final Function<Supplier<String>, String> func = McLibInterface.instance().createContextSupplier();
            return (LocalizedMessageInterface.DynamicArg) (locale, isAdmin) -> func.apply(() -> {
                final ArenaState state = this.getArena().getState();
                final LineConfig config = this.getConfig(state);
                String[] result = null;
                if (config == null)
                {
                    result = isAdmin ? getDefault(state).toAdminMessageLine(locale) : getDefault(state).toUserMessageLine(locale);
                }
                else
                {
                    result = isAdmin ? config.toAdminMessageLine(locale) : config.toUserMessageLine(locale);
                }
                if (result.length <= index)
                    return ""; //$NON-NLS-1$
                return result[index];
            });
        });
    }
    
    /**
     * Returns the default lines for this sign.
     * 
     * @param state
     * 
     * @return default lines.
     */
    protected abstract LocalizedMessageInterface getDefault(ArenaState state);
    
    /**
     * Returns the line config for given state.
     * @param state
     * @return Line config
     */
    private LineConfig getConfig(ArenaState state)
    {
        final Optional<LineConfig> result = this.data.getLines().stream().filter(r -> r.getState() == state).findAny();
        return result.isPresent() ? result.get() : null;
    }
    
    @Override
    protected Plugin getPlugin()
    {
        return MinigamesPlugin.instance().getPlugin();
    }
    
}
