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

package de.minigameslib.mgapi.api.test.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mgapi.api.arena.ArenaInterface;
import de.minigameslib.mgapi.api.player.ArenaPlayerInterface;

/**
 * Tests for {@link ArenaPlayerInterface}
 * 
 * @author mepeisen
 *
 */
public class ArenaPlayerInterfaceTest
{
    
    /**
     * test me.
     */
    @Test
    public void testOnline()
    {
        final McPlayerInterface mcplayer = mock(McPlayerInterface.class);
        final Player bukkit = mock(Player.class);
        final OfflinePlayer offline = mock(OfflinePlayer.class);
        when(mcplayer.getBukkitPlayer()).thenReturn(bukkit);
        when(mcplayer.getOfflinePlayer()).thenReturn(offline);
        when(mcplayer.getDisplayName()).thenReturn("FOO"); //$NON-NLS-1$
        final UUID uuid = UUID.randomUUID();
        when(mcplayer.getPlayerUUID()).thenReturn(uuid);
        final ArenaPlayer player = new ArenaPlayer(mcplayer, mock(ArenaInterface.class));
        
        assertTrue(player.isOnline());
        assertSame(bukkit, player.getBukkitPlayer());
        assertSame(offline, player.getOfflinePlayer());
        assertEquals("FOO", player.getName()); //$NON-NLS-1$
        assertEquals(uuid, player.getPlayerUUID());
        assertTrue(player.inArena());
    }
    
    /**
     * test me.
     */
    @Test
    public void testOffline()
    {
        final McPlayerInterface mcplayer = mock(McPlayerInterface.class);
        final OfflinePlayer offline = mock(OfflinePlayer.class);
        when(mcplayer.getOfflinePlayer()).thenReturn(offline);
        when(mcplayer.getDisplayName()).thenReturn("FOO"); //$NON-NLS-1$
        final UUID uuid = UUID.randomUUID();
        when(mcplayer.getPlayerUUID()).thenReturn(uuid);
        final ArenaPlayer player = new ArenaPlayer(mcplayer, null);
        
        assertFalse(player.isOnline());
        assertNull(player.getBukkitPlayer());
        assertSame(offline, player.getOfflinePlayer());
        assertEquals("FOO", player.getName()); //$NON-NLS-1$
        assertEquals(uuid, player.getPlayerUUID());
        assertFalse(player.inArena());
    }
    
    /**
     * test impl.
     */
    private static final class ArenaPlayer implements ArenaPlayerInterface
    {

        /** mc player. */
        private McPlayerInterface mcplayer;
        /** arena. */
        private ArenaInterface arena;

        /**
         * @param mcplayer
         * @param arena 
         */
        public ArenaPlayer(McPlayerInterface mcplayer, ArenaInterface arena)
        {
            this.mcplayer = mcplayer;
            this.arena = arena;
        }

        @Override
        public McPlayerInterface getMcPlayer()
        {
            return this.mcplayer;
        }

        @Override
        public ArenaInterface getArena()
        {
            return this.arena;
        }

        @Override
        public void die() throws McException
        {
            // empty
        }

        @Override
        public void die(ArenaPlayerInterface killer) throws McException
        {
            // empty
        }

        @Override
        public void lose() throws McException
        {
            // empty
        }

        @Override
        public void win() throws McException
        {
            // empty
        }

        @Override
        public McOutgoingStubbing<ArenaPlayerInterface> when(McPredicate<ArenaPlayerInterface> test) throws McException
        {
            return null;
        }

        @Override
        public boolean isSpectating()
        {
            return false;
        }

        @Override
        public boolean isPlaying()
        {
            return false;
        }

        @Override
        public void allowMovement()
        {
            // empty
        }

        @Override
        public void lockMovement()
        {
            // empty
        }

        @Override
        public float getCurrentMoveSpeed()
        {
            return 0;
        }

        @Override
        public void setCurrentMoveSpeed(float currentMoveSpeed)
        {
            // empty
        }

        @Override
        public float getCurrentFlySpeed()
        {
            return 0;
        }

        @Override
        public void setCurrentFlySpeed(float currentFlySpeed)
        {
            // empty
        }
        
    }
    
}
