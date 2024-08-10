package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.entity.Character;
import com.tuyenngoc.army2forum.repository.CharacterRepository;
import com.tuyenngoc.army2forum.service.CharacterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CharacterServiceImpl implements CharacterService {

    CharacterRepository characterRepository;

    @Override
    public void initCharacters() {
        if (characterRepository.count() > 0) {
            return;
        }

        List<Character> characters = List.of(
                new Character("Gunner", 0, 0, 80, -45, 280, 28, 1),
                new Character("Miss 6", 0, 0, 50, -45, 290, 14, 2),
                new Character("Electician", 0, 0, 80, -45, 300, 10, 3),
                new Character("King Kong", 40000, 32, 40, 30, 420, 10, 4),
                new Character("Rocketer", 30000, 24, 50, 20, 321, 12, 3),
                new Character("Granos", 20000, 16, 30, 30, 310, 7, 5),
                new Character("Chicky", 100000, 80, 20, -45, 450, 23, 2),
                new Character("Tarzan", 50000, 40, 10, 1, 340, 30, 1),
                new Character("Apache", 70000, 64, 30, -45, 431, 11, 4),
                new Character("Magenta", 120000, 96, 40, 20, 410, 35, 1)
        );

        characterRepository.saveAll(characters);
        log.info("Saved character size: " + characters.size());
    }

}
