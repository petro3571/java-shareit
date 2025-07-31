package ru.practicum.item.comment;

public class CommentMapper {
    public static Comment mapToComment(CommentDto request) {
        Comment comment = new Comment();
        comment.setText(request.getText());
        return comment;
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated());
        return dto;
    }
}