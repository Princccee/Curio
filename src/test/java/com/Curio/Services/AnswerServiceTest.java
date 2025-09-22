package com.Curio.Services;

import com.Curio.DTOs.PostAnswerDTO;
import com.Curio.Models.Answer;
import com.Curio.Models.Question;
import com.Curio.Models.User;
import com.Curio.Repositories.AnswerRepository;
import com.Curio.Repositories.QuestionRepository;
import com.Curio.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private AnswerService answerService;

    private User user;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        question = new Question();
        question.setId(10L);
        question.setUser(user);

        answer = Answer.builder()
                .body("Old Answer")
                .user(user)
                .question(question)
                .isAccepted(false)
                .build();
        answer.setId(100L);
    }

    // ---------- postAnswer ----------
    @Test
    void postAnswer_success() {
        PostAnswerDTO dto = new PostAnswerDTO();
        dto.setQuesId(10L);
        dto.setUserId(1L);
        dto.setAnswer("This is my answer");

        when(questionRepository.findById(10L)).thenReturn(Optional.of(question));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(answerRepository.save(any(Answer.class))).thenAnswer(inv -> inv.getArgument(0));

        Answer saved = answerService.postAnswer(dto);

        assertNotNull(saved);
        assertEquals("This is my answer", saved.getBody());
        assertFalse(saved.isAccepted());
        assertEquals(question, saved.getQuestion());
        assertEquals(user, saved.getUser());
    }

    @Test
    void postAnswer_questionNotFound() {
        PostAnswerDTO dto = new PostAnswerDTO();
        dto.setQuesId(999L);
        dto.setUserId(1L);

        when(questionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> answerService.postAnswer(dto));
    }

    // ---------- editAnswer ----------
    @Test
    void editAnswer_success() {
        when(answerRepository.findById(100L)).thenReturn(Optional.of(answer));
        when(answerRepository.save(any(Answer.class))).thenAnswer(inv -> inv.getArgument(0));

        Answer updated = answerService.editAnswer(100L, 1L, "Updated Body");

        assertEquals("Updated Body", updated.getBody());
    }

    @Test
    void editAnswer_unauthorized() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        answer.setUser(anotherUser);

        when(answerRepository.findById(100L)).thenReturn(Optional.of(answer));

        assertThrows(RuntimeException.class, () -> answerService.editAnswer(100L, 1L, "Hack Update"));
    }

    // ---------- deleteAnswer ----------
    @Test
    void deleteAnswer_success() {
        when(answerRepository.findById(100L)).thenReturn(Optional.of(answer));

        answerService.deleteAnswer(100L, 1L);

        verify(answerRepository, times(1)).delete(answer);
    }

    @Test
    void deleteAnswer_unauthorized() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        answer.setUser(anotherUser);

        when(answerRepository.findById(100L)).thenReturn(Optional.of(answer));

        assertThrows(RuntimeException.class, () -> answerService.deleteAnswer(100L, 1L));
    }

    // ---------- getAnswersByQuestion ----------
    @Test
    void getAnswersByQuestion_success() {
        when(questionRepository.findById(10L)).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionId(10L)).thenReturn(List.of(answer));

        List<Answer> answers = answerService.getAnswersByQuestion(10L);

        assertEquals(1, answers.size());
        assertEquals("Old Answer", answers.get(0).getBody());
    }

    @Test
    void getAnswersByQuestion_questionNotFound() {
        when(questionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> answerService.getAnswersByQuestion(999L));
    }

    // ---------- markAsAccepted ----------
    @Test
    void markAsAccepted_success() {
        when(answerRepository.findById(100L)).thenReturn(Optional.of(answer));
        when(answerRepository.findByQuestionId(10L)).thenReturn(List.of(answer));
        when(answerRepository.save(any(Answer.class))).thenAnswer(inv -> inv.getArgument(0));

        Answer accepted = answerService.markAsAccepted(100L, 1L);

        assertTrue(accepted.isAccepted());
    }

    @Test
    void markAsAccepted_unauthorized() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        question.setUser(anotherUser);

        when(answerRepository.findById(100L)).thenReturn(Optional.of(answer));

        assertThrows(RuntimeException.class, () -> answerService.markAsAccepted(100L, 1L));
    }
}
