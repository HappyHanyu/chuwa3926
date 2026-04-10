package com.chuwa.redbook;

import com.chuwa.redbook.dao.CommentRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.entity.Comment;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.CommentDto;
import com.chuwa.redbook.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Post post;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1L);

        comment = new Comment();
        comment.setId(10L);
        comment.setName("Tom");
        comment.setEmail("tom@test.com");
        comment.setBody("hello");
        comment.setPost(post);

        commentDto = new CommentDto();
        commentDto.setId(10L);
        commentDto.setName("Tom");
        commentDto.setEmail("tom@test.com");
        commentDto.setBody("hello");
    }

    @Test
    void createComment_shouldReturnSavedCommentDto() {
        Comment mappedComment = new Comment();
        mappedComment.setName(commentDto.getName());
        mappedComment.setEmail(commentDto.getEmail());
        mappedComment.setBody(commentDto.getBody());

        when(modelMapper.map(commentDto, Comment.class)).thenReturn(mappedComment);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.save(mappedComment)).thenReturn(comment);
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);

        CommentDto result = commentService.createComment(1L, commentDto);

        assertNotNull(result);
        assertEquals("Tom", result.getName());
        assertEquals("tom@test.com", result.getEmail());
        assertEquals("hello", result.getBody());

        assertEquals(post, mappedComment.getPost());
        verify(postRepository).findById(1L);
        verify(commentRepository).save(mappedComment);
        verify(modelMapper).map(commentDto, Comment.class);
        verify(modelMapper).map(comment, CommentDto.class);
    }

    @Test
    void createComment_shouldThrowResourceNotFoundException_whenPostNotFound() {
        when(modelMapper.map(commentDto, Comment.class)).thenReturn(new Comment());
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.createComment(1L, commentDto));

        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void getCommentsByPostId_shouldReturnCommentDtoList() {
        Comment comment2 = new Comment();
        comment2.setId(11L);
        comment2.setName("Jerry");
        comment2.setEmail("jerry@test.com");
        comment2.setBody("second");
        comment2.setPost(post);

        CommentDto dto2 = new CommentDto();
        dto2.setId(11L);
        dto2.setName("Jerry");
        dto2.setEmail("jerry@test.com");
        dto2.setBody("second");

        when(commentRepository.findByPostId(1L)).thenReturn(Arrays.asList(comment, comment2));
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);
        when(modelMapper.map(comment2, CommentDto.class)).thenReturn(dto2);

        List<CommentDto> result = commentService.getCommentsByPostId(1L);

        assertEquals(2, result.size());
        assertEquals("Tom", result.get(0).getName());
        assertEquals("Jerry", result.get(1).getName());

        verify(commentRepository).findByPostId(1L);
        verify(modelMapper).map(comment, CommentDto.class);
        verify(modelMapper).map(comment2, CommentDto.class);
    }

    @Test
    void getCommentsByPostId_shouldReturnEmptyList() {
        when(commentRepository.findByPostId(1L)).thenReturn(Collections.emptyList());

        List<CommentDto> result = commentService.getCommentsByPostId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentRepository).findByPostId(1L);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void getCommentById_shouldReturnCommentDto_whenCommentBelongsToPost() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);

        CommentDto result = commentService.getCommentById(1L, 10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Tom", result.getName());

        verify(postRepository).findById(1L);
        verify(commentRepository).findById(10L);
        verify(modelMapper).map(comment, CommentDto.class);
    }

    @Test
    void getCommentById_shouldThrowResourceNotFoundException_whenPostNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.getCommentById(1L, 10L));

        verify(commentRepository, never()).findById(anyLong());
    }

    @Test
    void getCommentById_shouldThrowResourceNotFoundException_whenCommentNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.getCommentById(1L, 10L));
    }

    @Test
    void getCommentById_shouldThrowBlogAPIException_whenCommentDoesNotBelongToPost() {
        Post anotherPost = new Post();
        anotherPost.setId(999L);
        comment.setPost(anotherPost);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        BlogAPIException ex = assertThrows(BlogAPIException.class,
                () -> commentService.getCommentById(1L, 10L));

        assertTrue(ex.getMessage().contains("Comment does not belong to post"));
        verify(modelMapper, never()).map(any(Comment.class), eq(CommentDto.class));
    }

    @Test
    void updateComment_shouldUpdateAndReturnCommentDto() {
        CommentDto requestDto = new CommentDto();
        requestDto.setName("Updated Name");
        requestDto.setEmail("updated@test.com");
        requestDto.setBody("updated body");

        Comment updatedComment = new Comment();
        updatedComment.setId(10L);
        updatedComment.setName("Updated Name");
        updatedComment.setEmail("updated@test.com");
        updatedComment.setBody("updated body");
        updatedComment.setPost(post);

        CommentDto updatedDto = new CommentDto();
        updatedDto.setId(10L);
        updatedDto.setName("Updated Name");
        updatedDto.setEmail("updated@test.com");
        updatedDto.setBody("updated body");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(updatedComment);
        when(modelMapper.map(updatedComment, CommentDto.class)).thenReturn(updatedDto);

        CommentDto result = commentService.updateComment(1L, 10L, requestDto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@test.com", result.getEmail());
        assertEquals("updated body", result.getBody());

        assertEquals("Updated Name", comment.getName());
        assertEquals("updated@test.com", comment.getEmail());
        assertEquals("updated body", comment.getBody());

        verify(commentRepository).save(comment);
        verify(modelMapper).map(updatedComment, CommentDto.class);
    }

    @Test
    void updateComment_shouldThrowResourceNotFoundException_whenPostNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.updateComment(1L, 10L, commentDto));

        verify(commentRepository, never()).findById(anyLong());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void updateComment_shouldThrowResourceNotFoundException_whenCommentNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.updateComment(1L, 10L, commentDto));

        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void updateComment_shouldThrowBlogAPIException_whenCommentDoesNotBelongToPost() {
        Post anotherPost = new Post();
        anotherPost.setId(999L);
        comment.setPost(anotherPost);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        BlogAPIException ex = assertThrows(BlogAPIException.class,
                () -> commentService.updateComment(1L, 10L, commentDto));

        assertTrue(ex.getMessage().contains("Comment does not belong to post"));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void deleteComment_shouldDeleteComment_whenCommentBelongsToPost() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L, 10L);

        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteComment_shouldThrowResourceNotFoundException_whenPostNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.deleteComment(1L, 10L));

        verify(commentRepository, never()).findById(anyLong());
        verify(commentRepository, never()).delete(any(Comment.class));
    }

    @Test
    void deleteComment_shouldThrowResourceNotFoundException_whenCommentNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.deleteComment(1L, 10L));

        verify(commentRepository, never()).delete(any(Comment.class));
    }

    @Test
    void deleteComment_shouldThrowBlogAPIException_whenCommentDoesNotBelongToPost() {
        Post anotherPost = new Post();
        anotherPost.setId(999L);
        comment.setPost(anotherPost);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(10L)).thenReturn(Optional.of(comment));

        BlogAPIException ex = assertThrows(BlogAPIException.class,
                () -> commentService.deleteComment(1L, 10L));

        assertTrue(ex.getMessage().contains("Comment does not belong to post"));
        verify(commentRepository, never()).delete(any(Comment.class));
    }
}
