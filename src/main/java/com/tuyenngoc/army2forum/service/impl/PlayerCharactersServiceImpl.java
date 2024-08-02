package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.PlayerCharacters;
import com.tuyenngoc.army2forum.repository.CharacterRepository;
import com.tuyenngoc.army2forum.repository.PlayerCharacterRepository;
import com.tuyenngoc.army2forum.service.PlayerCharactersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerCharactersServiceImpl implements PlayerCharactersService {

    private final CharacterRepository characterRepository;

    private final PlayerCharacterRepository playerCharacterRepository;

    @Override
    public void initiatePlayerCharacterDefaults(Player player) {
        PlayerCharacters gunner = new PlayerCharacters();
        gunner.setCharacter(characterRepository.findByName("Gunner"));
        gunner.setPlayer(player);
        gunner.setIsUsed(true);
        gunner.setAdditionalPoints(new int[]{0, 0, 10, 10, 10});
        gunner.setData(new int[6]);
        playerCharacterRepository.save(gunner);

        PlayerCharacters aka = new PlayerCharacters();
        aka.setCharacter(characterRepository.findByName("Miss 6"));
        aka.setPlayer(player);
        aka.setIsUsed(false);
        aka.setAdditionalPoints(new int[]{0, 0, 10, 10, 10});
        aka.setData(new int[6]);
        playerCharacterRepository.save(aka);

        PlayerCharacters electician = new PlayerCharacters();
        electician.setCharacter(characterRepository.findByName("Electician"));
        electician.setPlayer(player);
        electician.setIsUsed(false);
        electician.setAdditionalPoints(new int[]{0, 0, 10, 10, 10});
        electician.setData(new int[6]);
        playerCharacterRepository.save(electician);
    }
}
