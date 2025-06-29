package com.fours.tolevelup.service.character;

import com.fours.tolevelup.controller.request.UserCharacterRequest;
import com.fours.tolevelup.model.entity.User;
import com.fours.tolevelup.model.entity.UserCharacter;
import com.fours.tolevelup.repository.character.CharacterRepository;
import com.fours.tolevelup.repository.character.UserCharacterRepository;
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

    public List<CharacterDTO.UserCharacterInfo> findUserCharacterList(User user) {
        List<CharacterDTO.UserCharacterInfo> userCharacterList = new ArrayList<>();
        return userCharacterList;
    }

    @Transactional
    public void changeCharacterName(String user_id, String character_id, UserCharacterRequest request) {
        UserCharacter userCharacter = userCharacterRepository.findByIdAndUserId(character_id, user_id);
        System.out.println(userCharacter.getId());
        userCharacter.update(request.getCharacter_name());
        System.out.println(userCharacter.getCharacter_name());
    }

    public List<CharacterDTO.CharacterData> getCharacterData() {
        List<Object[]> characterDataList = characterRepository.getCharacters();
        List<CharacterDTO.CharacterData> characterDTOList = new ArrayList<>();

        for (Object[] characterData : characterDataList) {
            CharacterDTO.CharacterData characterDTO = new CharacterDTO.CharacterData();
            characterDTO.setId((String) characterData[0]);
            characterDTO.setLevel((int) characterData[1]);
            characterDTO.setInfo((String) characterData[2]);
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