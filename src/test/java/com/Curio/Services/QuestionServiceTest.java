package com.Curio.Services;

import com.Curio.DTOs.EditQuestionDTO;
import com.Curio.DTOs.PostQuestionDTO;
import com.Curio.DTOs.QuestionResponse;
import com.Curio.Models.Question;
import com.Curio.Models.Tags;
import com.Curio.Models.User;
import com.Curio.Repositories.QuestionRepository;
import com.Curio.Repositories.TagsRepository;
import com.Curio.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagsService tagsService;

    @Mock
    private TagsRepository tagsRepository;

    @Test
    void postQuestion_success() {
        // Arrange
        PostQuestionDTO dto = PostQuestionDTO.builder()
                .userId(1L)
                .title("How to unit test?")
                .body("Explain step by step.")
                .tags(Set.of("testing"))
                .build();

        User user = new User();
        user.setId(1L);

        Tags tag = new Tags();
        tag.setTagName("testing");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(tagsRepository.findByTagName("testing")).thenReturn(Optional.of(tag));

        when(questionRepository.save(any(Question.class))).thenAnswer(inv -> {
            Question q = inv.getArgument(0);
            q.setId(100L);
            return q;
        });

        // Act
        Question saved = questionService.postQuestion(dto);

        // Assert
        assertNotNull(saved);
        assertEquals("How to unit test?", saved.getTitle());
        assertEquals(user, saved.getUser());
        assertEquals(100L, saved.getId());
        assertTrue(saved.getTags().stream().anyMatch(t -> t.getTagName().equals("testing")));
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void postQuestion_userNotFound_throws() {
        // Arrange
//        PostQuestionDTO dto = new PostQuestionDTO();
        PostQuestionDTO dto = PostQuestionDTO.builder()
                .userId(2L)
                .build();

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> questionService.postQuestion(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("user"));
        verify(questionRepository, never()).save(any());
    }

    @Test
    void editQuestion_success() {
        // Arrange
        Long questionId = 5L;
        Long userId = 1L;

        User owner = new User();
        owner.setId(userId);

        Question existing = new Question();
        existing.setId(questionId);
        existing.setUser(owner);
        existing.setTitle("old");
        existing.setBody("oldbody");

        EditQuestionDTO dto = EditQuestionDTO.builder()
                .quesId(questionId)
                .userId(userId)
                .title("new title")
                .body("new body")
                .build();

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(existing));
        when(questionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Question updated = questionService.editQuestion(dto);

        // Assert
        assertEquals("new title", updated.getTitle());
        assertEquals("new body", updated.getBody());
        verify(questionRepository, times(1)).save(existing);
    }

    @Test
    void editQuestion_unauthorized_throws() {
        // Arrange
        Long questionId = 6L;
        Long userId = 1L;

        User other = new User();
        other.setId(999L);

        Question existing = new Question();
        existing.setId(questionId);
        existing.setUser(other);

        EditQuestionDTO dto = EditQuestionDTO.builder()
                .quesId(questionId)
                .userId(userId)
                .title("x")
                .body("y")
                .build();

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(existing));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> questionService.editQuestion(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("unauthorized"));
        verify(questionRepository, never()).save(any());
    }

    @Test
    void deleteQuestion_success() {
        // Arrange
        Long qid = 7L; Long uid = 1L;
        User owner = new User(); owner.setId(uid);
        Question existing = new Question(); existing.setId(qid); existing.setUser(owner);

        when(questionRepository.findById(qid)).thenReturn(Optional.of(existing));

        // Act
        questionService.deleteQuestion(qid, uid);

        // Assert
        verify(questionRepository, times(1)).delete(existing);
    }

    @Test
    void getQuestionsByUser_success() {
        // Arrange
        Long uid = 1L;
        User user = new User(); user.setId(uid);
        when(userRepository.findById(uid)).thenReturn(Optional.of(user));

        Question q1 = new Question();
        q1.setTitle("t1");
        when(questionRepository.findAllQuestionsByUserId(uid)).thenReturn(List.of(q1));

        // Act
        List<Question> results = questionService.getQuestionsByUser(uid);

        // Assert
        assertEquals(1, results.size());
        assertEquals("t1", results.get(0).getTitle());
    }

    @Test
    void getQuestionsByTags_success() {
        // Arrange
        Question q1 = new Question();
        q1.setTitle("Spring Boot Testing");
        q1.setBody("Unit testing with JUnit");

        Question q2 = new Question();
        q2.setTitle("Mockito Guide");
        q2.setBody("Mocking dependencies");

        when(questionRepository.findByTagNames(Set.of("java", "spring")))
                .thenReturn(Set.of(q1, q2));

        // Act
        Set<QuestionResponse> responses = questionService.getQuestionsByTags(Set.of("java", "spring"));

        // Assert
        assertEquals(2, responses.size());
        assertTrue(responses.stream().anyMatch(r -> r.getTitle().equals("Spring Boot Testing")));
        assertTrue(responses.stream().anyMatch(r -> r.getTitle().equals("Mockito Guide")));
        verify(questionRepository, times(1)).findByTagNames(Set.of("java", "spring"));
    }

    @Test
    void searchQuestions_success() {
        // Arrange
        Question q1 = new Question();
        q1.setTitle("How to test services?");
        q1.setBody("Use Mockito for mocking.");

        Question q2 = new Question();
        q2.setTitle("Search in Spring Data");
        q2.setBody("Using derived query methods.");

        when(questionRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase("test", "test"))
                .thenReturn(List.of(q1, q2));

        // Act
        List<QuestionResponse> responses = questionService.searchQuestions("test");

        // Assert
        assertEquals(2, responses.size());
        assertEquals("How to test services?", responses.get(0).getTitle());
        assertEquals("Search in Spring Data", responses.get(1).getTitle());
        verify(questionRepository, times(1))
                .findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase("test", "test");
    }

    @Test
    void getAllQuestions_success() {
        // Arrange
        Question q1 = new Question();
        q1.setTitle("Intro to Spring");
        q1.setBody("Spring basics");

        Question q2 = new Question();
        q2.setTitle("Unit Testing");
        q2.setBody("JUnit and Mockito");

        when(questionRepository.findAll()).thenReturn(List.of(q1, q2));

        // Act
        List<QuestionResponse> responses = questionService.getAllQuestions();

        // Assert
        assertEquals(2, responses.size());
        assertEquals("Intro to Spring", responses.get(0).getTitle());
        assertEquals("Unit Testing", responses.get(1).getTitle());
        verify(questionRepository, times(1)).findAll();
    }

}
