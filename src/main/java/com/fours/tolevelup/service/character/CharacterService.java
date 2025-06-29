package com.fours.tolevelup.service.character;

import com.fours.tolevelup.controller.request.UserCharacterRequest;
import com.fours.tolevelup.model.entity.Character;
import com.fours.tolevelup.model.entity.UserCharacter;
import com.fours.tolevelup.repository.CharacterRepository;
import com.fours.tolevelup.repository.UserCharacterRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CharacterService {
    private final UserCharacterRepository userCharacterRepository;
    private final CharacterRepository characterRepository;

    @Transactional
    public void changeCharacterName(String user_id, String character_id, UserCharacterRequest request) {
        UserCharacter userCharacter = userCharacterRepository.findByIdAndUserId(character_id, user_id);
        userCharacter.update(request.getCharacter_name());
    }

    public List<CharacterDTO.CharacterData> getCharacterData() {
        List<Character> characters= characterRepository.findAll();
        List<CharacterDTO.CharacterData> characterDTOList = new ArrayList<>();

        for(Character character : characters) {
            CharacterDTO.CharacterData characterDTO = new CharacterDTO.CharacterData();
            characterDTO.setId(character.getId());
            characterDTO.setLevel(character.getLevel());
            characterDTO.setInfo(character.getInfo());
            characterDTOList.add(characterDTO);
        }

        return characterDTOList;
    }

    public List<CharacterDTO.UserCharacterInfo> getUserCharacterData(String user_id) {
        List<UserCharacter> userCharacterList = userCharacterRepository.findUserCharacterByUserId(user_id);
        List<CharacterDTO.UserCharacterInfo> userCharacterDataList = new ArrayList<>();

        for (UserCharacter userCharacter : userCharacterList) {
            int theme_id = userCharacterRepository.getThemeId(userCharacter.getCharacter().getId());
            userCharacterDataList.add(CharacterDTO.UserCharacterInfo.builder()
                    .userCharacter(userCharacter)
                    .exp(userCharacterRepository.getExp(user_id, theme_id))
                    .level(userCharacterRepository.getLevel(userCharacter.getId()))
                    .build());

        }

        return userCharacterDataList;
    }
}