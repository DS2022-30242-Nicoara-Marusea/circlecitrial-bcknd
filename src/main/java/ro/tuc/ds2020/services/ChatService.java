package ro.tuc.ds2020.services;

import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.dtos.ChatDTO;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.entities.Chat;
import ro.tuc.ds2020.repositories.ChatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AutoJsonRpcServiceImpl
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @JsonRpcMethod("insertMessage")
    public ChatDTO insert(ChatDTO chatDTO) {

        chatRepository.save(Chat.builder()
                .addresser(chatDTO.getAddresser())
                .receptor(chatDTO.getReceptor())
                .content(chatDTO.getContent())
                .state(chatDTO.getState()).build());

        return chatDTO;
    }

    @JsonRpcMethod("getMessage")
    public ChatDTO getChat(ChatDTO chatDTO) {

        List<Chat> chats = chatRepository.findAll();
        chats.stream().map(chat -> ChatDTO.builder()
                .addresser(chat.getAddresser())
                .receptor(chat.getReceptor())
                .content(chat.getContent())
                .state(chat.getState()).build()).collect(Collectors.toList());
        for(Chat c: chats)
        {
            if(c.getAddresser().equals(chatDTO.getAddresser()) && c.getReceptor().equals(chatDTO.getReceptor())
            && c.getContent().equals(chatDTO.getContent()))
            {
                return new ChatDTO(c.getAddresser(), c.getReceptor(), c.getContent(), c.getState());
            }

        }
        return null;
    }
}
