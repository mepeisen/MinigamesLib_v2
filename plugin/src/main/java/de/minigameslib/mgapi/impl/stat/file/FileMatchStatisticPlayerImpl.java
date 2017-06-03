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

package de.minigameslib.mgapi.impl.stat.file;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;
import de.minigameslib.mgapi.api.match.MatchStatisticId;
import de.minigameslib.mgapi.api.stat.MatchStatisticPlayerInterface;
import de.minigameslib.mgapi.api.stat.PlayerStatisticId;
import de.minigameslib.mgapi.api.team.TeamIdType;

/**
 * @author mepeisen
 *
 */
public class FileMatchStatisticPlayerImpl extends AnnotatedDataFragment implements MatchStatisticPlayerInterface
{
    
    /**
     * the user uuid.
     */
    @PersistentField
    protected UUID uuid;
    
    /**
     * the player stats.
     */
    @PersistentField
    private Map<String, Long> playerStat = new HashMap<>();
    
    /**
     * the match stats.
     */
    @PersistentField
    private Map<String, Integer> matchStat = new HashMap<>();
    
    /**
     * the played time.
     */
    @PersistentField
    private long playTime;
    
    /**
     * the last team.
     */
    @PersistentField
    private TeamIdType team;
    
    /**
     * the left timestamp.
     */
    @PersistentField
    private LocalDateTime left;
    
    /**
     * the joined timestamp.
     */
    @PersistentField
    private LocalDateTime joined;
    
    @Override
    public UUID getPlayerUUID()
    {
        return this.uuid;
    }
    
    @Override
    public LocalDateTime getJoined()
    {
        return this.joined;
    }
    
    @Override
    public LocalDateTime getLeft()
    {
        return this.left;
    }
    
    @Override
    public TeamIdType getTeam()
    {
        return this.team;
    }
    
    @Override
    public Integer getStatistic(MatchStatisticId statistic)
    {
        return this.matchStat.get(statistic.getPluginName() + "$" + statistic.name()); //$NON-NLS-1$
    }
    
    @Override
    public List<MatchStatisticId> getStatistics()
    {
        return this.matchStat.keySet().stream().map(s -> {
            final String[] splitted = s.split("$"); //$NON-NLS-1$
            return EnumServiceInterface.instance().getEnumValue(MatchStatisticId.class, splitted[0], splitted[1]);
        }).collect(Collectors.toList());
    }
    
    @Override
    public long getPlayTime()
    {
        return this.playTime;
    }
    
    @Override
    public Long getStatistic(PlayerStatisticId statistic)
    {
        return this.playerStat.get(statistic.getPluginName() + "$" + statistic.name()); //$NON-NLS-1$
    }
    
}
