package ru.practicum.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.BaseClient;
import ru.practicum.item.comment.NewCommentDto;
import ru.practicum.user.NewUserDto;
import ru.practicum.user.UpdateUserDto;

import java.util.List;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareIt-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Long userId, NewItemDto request) {
        return post("", userId, request);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, UpdateItemDto request) {
        return patch("/" + itemId, userId, request);
    }

    public ResponseEntity<Object> getUserItem(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> searchItems(String text, Long userId) {
        return get("/search?text=" + text, userId);
    }

    public ResponseEntity<Object> postComment(Long userId, Long itemId, NewCommentDto comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }

    public ResponseEntity<Object> getAllUserItems(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> deleteItem(Long userId, Long itemId) {
        return delete("/" + itemId, userId);
    }
}
