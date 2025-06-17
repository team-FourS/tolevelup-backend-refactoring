package com.fours.tolevelup.controller.api;


import com.fours.tolevelup.controller.request.UserCharacterRequest;
import com.fours.tolevelup.controller.response.Response;
import com.fours.tolevelup.controller.response.UserCharacterResponse;
import com.fours.tolevelup.service.character.CharacterDTO;
import com.fours.tolevelup.service.character.CharacterService;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping("/character")
    public ResponseEntity<List<CharacterDTO.CharacterData>> characterData() {
        return ResponseEntity.ok(characterService.getCharacterData());
    }

    public ResponseEntity<Object> characterList() {
        return ResponseEntity.ok().build();
    }


    @PutMapping("/characterName")
    public Response<UserCharacterResponse.UserCharacter> update(Authentication authentication,
                                                                @RequestParam String character_id,
                                                                @RequestBody UserCharacterRequest userCharacterRequest) {
        CharacterDTO.UserCharacter nameChange = characterService.changeCharacterName(authentication.getName(),
                character_id, userCharacterRequest.getCharacter_name());
        return Response.success(UserCharacterResponse.UserCharacter.fromDTO(nameChange));
    }

    @GetMapping("/userCharacter")
    public ResponseEntity<List<CharacterDTO.UserCharacterInfo>> userCharacterData(Authentication authentication) {
        return ResponseEntity.ok(characterService.getUserCharacterData(authentication.getName()));
    }


    @GetMapping("/image")
    public ResponseEntity<byte[]> returnImage(@RequestParam String imageName) {
        String path = "img/" + imageName; // 클래스패스 상의 이미지 경로

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream != null) {
                byte[] imageBytes = IOUtils.toByteArray(
                        inputStream); // Apache Commons IO 라이브러리의 IOUtils를 사용하여 바이트 배열로 읽음

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", Files.probeContentType(Paths.get(path)));
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
