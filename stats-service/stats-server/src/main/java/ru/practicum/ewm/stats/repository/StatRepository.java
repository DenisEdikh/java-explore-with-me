package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.stats.model.EndpointHit;
import ru.practicum.ewm.stats.ViewStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Integer> {
    @Query(value = """
            select new ru.practicum.ewm.stats.ViewStatDto(h.app, h.uri, count(h.ip))
            from EndpointHit as h
            where h.timestamp between :start and :end
            group by h.app, h.uri
            order by count(h.ip) desc
            """)
    List<ViewStatDto> findAllEndpointWithOutUniqueIpAndWithOutUris(@Param(value = "start") LocalDateTime start,
                                                                   @Param(value = "end") LocalDateTime end);

    @Query(value = """
            select new ru.practicum.ewm.stats.ViewStatDto(h.app, h.uri, count(distinct(h.ip)))
            from EndpointHit as h
            where h.timestamp between :start and :end
            group by h.app, h.uri
            order by count(distinct(h.ip)) desc
            """)
    List<ViewStatDto> findAllEndpointWithUniqueIpAndWithOutUris(@Param(value = "start") LocalDateTime start,
                                                                @Param(value = "end") LocalDateTime end);

    @Query(value = """
            select new ru.practicum.ewm.stats.ViewStatDto(h.app, h.uri, count(h.ip))
            from EndpointHit as h
            where h.uri in :uris and h.timestamp between :start and :end
            group by h.app, h.uri
            order by count(h.ip) desc
            """)
    List<ViewStatDto> findAllEndpointWithOutUniqueIpAndWitUris(@Param(value = "start") LocalDateTime start,
                                                               @Param(value = "end") LocalDateTime end,
                                                               @Param(value = "uris") List<String> uris);

    @Query(value = """
            select new ru.practicum.ewm.stats.ViewStatDto(h.app, h.uri, count(distinct(h.ip)))
            from EndpointHit as h
            where h.uri in :uris and h.timestamp between :start and :end
            group by h.app, h.uri
            order by count(distinct(h.ip)) desc
            """)
    List<ViewStatDto> findAllEndpointWithUniqueIpAndWithUris(@Param(value = "start") LocalDateTime start,
                                                             @Param(value = "end") LocalDateTime end,
                                                             @Param(value = "uris") List<String> uris);
}
