package ru.practicum.ewm.service.compilation;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.QCompilation;
import ru.practicum.ewm.param.PublicRequestParam;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationMapper compMapper;
    private final CompilationRepository compRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto dto) {
        final List<Event> events = eventRepository.findByIdIn(dto.getEvents());
        final Compilation storedCompilation = compRepository.save(compMapper.toCompilation(dto, events));
        return compMapper.toDto(storedCompilation);
    }

    @Transactional
    @Override
    public void delete(Long compId) {
        checkExistenceComp(compId);
        compRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto update(UpdateCompilationRequest request, Long compId) {
        final Compilation storedComp = compRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(Compilation.class, compId));

        Optional.ofNullable(request.getEvents())
                .ifPresent(e -> storedComp.getEvents().addAll(eventRepository.findByIdIn(e)));
        Optional.ofNullable(request.getPinned()).ifPresent(storedComp::setPinned);
        Optional.ofNullable(request.getTitle()).ifPresent(storedComp::setTitle);

        final Compilation updatedComp = compRepository.save(storedComp);
        return compMapper.toDto(updatedComp);
    }

    @Override
    public List<CompilationDto> getAll(PublicRequestParam param) {
        final Pageable page = PageRequest.of(param.getPage(), param.getSize());

        final BooleanBuilder builder = new BooleanBuilder();
        Optional.ofNullable(param.getPinned()).ifPresent(p -> builder.and(QCompilation.compilation.pinned.eq(p)));

        final List<Compilation> comps = compRepository.findAll(builder, page).toList();
        return compMapper.toDto(comps);
    }

    @Override
    public CompilationDto getById(Long compId) {
        final Compilation storedComp = compRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(Compilation.class, compId));
        return compMapper.toDto(storedComp);
    }

    private void checkExistenceComp(Long compId) {
        log.debug("Start checking contains category with catId {}", compId);
        if (!compRepository.existsById(compId)) {
            log.warn("Compilation with compId {} does not exist", compId);
            throw new NotFoundException(Compilation.class, compId);
        }
    }
}
