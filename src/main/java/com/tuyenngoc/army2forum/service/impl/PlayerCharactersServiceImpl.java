package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.PlayerCharacters;
import com.tuyenngoc.army2forum.repository.CharacterRepository;
import com.tuyenngoc.army2forum.repository.PlayerCharacterRepository;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.service.PlayerCharactersService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerCharactersServiceImpl implements PlayerCharactersService {

    CharacterRepository characterRepository;

    PlayerRepository playerRepository;

    PlayerCharacterRepository playerCharacterRepository;

    @Override
    public void initiatePlayerCharacterDefaults(Player player) {
        PlayerCharacters gunner = new PlayerCharacters();
        gunner.setCharacter(characterRepository.findByName("Gunner"));
        gunner.setPlayer(player);
        playerCharacterRepository.save(gunner);

        PlayerCharacters aka = new PlayerCharacters();
        aka.setCharacter(characterRepository.findByName("Miss 6"));
        aka.setPlayer(player);
        playerCharacterRepository.save(aka);

        PlayerCharacters electician = new PlayerCharacters();
        electician.setCharacter(characterRepository.findByName("Electician"));
        electician.setPlayer(player);
        playerCharacterRepository.save(electician);

        player.setActiveCharacter(gunner);
        playerRepository.save(player);
    }
}
