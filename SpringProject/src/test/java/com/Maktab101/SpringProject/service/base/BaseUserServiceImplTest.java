package com.Maktab101.SpringProject.service.base;

import com.Maktab101.SpringProject.model.Customer;
import com.Maktab101.SpringProject.model.User;
import com.Maktab101.SpringProject.repository.base.BaseUserRepository;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BaseUserServiceImplTest {

    @Mock
    private BaseUserRepository<User> baseRepository;
    private BaseUserServiceImpl<User> underTest;

    @BeforeEach
    void setUp() {
        underTest = new TestUserServiceImpl(baseRepository);
    }

    static class TestUserServiceImpl extends BaseUserServiceImpl<User> {
        public TestUserServiceImpl(BaseUserRepository<User> baseUserRepository) {
            super(baseUserRepository);
        }
    }

    @Test
    void testExistsByEmailAddress_ReturnsTrue_IfEmailExists() {
        // Given
        String email = "Ali123@Gmail.com";
        when(baseRepository.existsByEmail(email)).thenReturn(true);

        // When
        boolean result = underTest.existsByEmailAddress(email);

        // Then
        assertThat(result).isTrue();
        verify(baseRepository).existsByEmail(email);
        verifyNoMoreInteractions(baseRepository);
    }

    @Test
    void testExistsByEmailAddress_ReturnsFalse_WhenEmailNotExists() {
        // Given
        String email = "Ali123@Gmail.com";
        when(baseRepository.existsByEmail(email)).thenReturn(false);

        // When
        boolean result = underTest.existsByEmailAddress(email);

        // Then
        assertThat(result).isFalse();
        verify(baseRepository).existsByEmail(email);
        verifyNoMoreInteractions(baseRepository);
    }


    @Test
    void testFindByEmailAddress_IfExists_ReturnsUser() {
        // Given
        String email = "Ali123@Gmail.com";
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword("Ali1234");

        when(baseRepository.findByEmail(email)).thenReturn(Optional.of(customer));

        // When
        Optional<User> optionalUser = underTest.findByEmailAddress(email);

        // Then
        assertThat(optionalUser).isPresent();
        assertThat(optionalUser).isEqualTo(Optional.of(customer));
        verify(baseRepository).findByEmail(email);
        verifyNoMoreInteractions(baseRepository);
    }

    @Test
    void testFindByEmailAddress_IfNotExists_ReturnsEmpty() {
        // Given
        String email = "Ali123@Gmail.com";
        when(baseRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<User> optionalUser = underTest.findByEmailAddress(email);

        // Then
        assertThat(optionalUser).isEmpty();
        verify(baseRepository).findByEmail(email);
        verifyNoMoreInteractions(baseRepository);
    }

    @Test
    void testLogin_IfValidCredential_ReturnUser() {
        // Given
        String email = "Ali123@Gmail.com";
        String password = "Ali1234";
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(password);

        when(baseRepository.existsByEmailAndPassword(email, password)).thenReturn(true);
        when(baseRepository.findByEmail(email)).thenReturn(Optional.of(customer));

        // When
        User loggedUser = underTest.login(email, password);

        // Then
        assertThat(loggedUser).isEqualTo(customer);
        verify(baseRepository).findByEmail(email);
        verify(baseRepository).existsByEmailAndPassword(email, password);
        verifyNoMoreInteractions(baseRepository);
    }

    @Test
    void testLogin_IfNotFound_ThrowException() {
        // Given
        String email = "Ali123@Gmail.com";
        String password = "Ali1234";
        when(baseRepository.existsByEmailAndPassword(email, password)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> underTest.login(email, password))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: UserNotFound
                        \uD83D\uDCC3DESC:
                        Check email or password""");
        verify(baseRepository).existsByEmailAndPassword(email, password);
        verifyNoMoreInteractions(baseRepository);
    }

    @Test
    void testLogin_IfUserIsNull_ThrowException() {
        // Given
        String email = "Ali123@Gmail.com";
        String password = "Ali1234";
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(password);

        when(baseRepository.existsByEmailAndPassword(email, password)).thenReturn(true);
        when(baseRepository.findByEmail(email)).thenReturn(Optional.empty());


        // When/Then
        assertThatThrownBy(() -> underTest.login(email, password))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: UserNotFound
                        \uD83D\uDCC3DESC:
                        We can't find the user""");
        verify(baseRepository).existsByEmailAndPassword(email, password);
        verify(baseRepository).findByEmail(email);
        verifyNoMoreInteractions(baseRepository);
    }

    @Test
    void testEditPassword_IfUserExists_ChangePassword() {
        // Given
        Long id = 1L;
        String newPassword = "Test123";
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("Ali123@Gmail.com");
        customer.setPassword("Ali1234");
        when(baseRepository.findById(id)).thenReturn(Optional.of(customer));

        // When
        underTest.editPassword(id,newPassword);

        // Then
        assertThat(customer.getPassword()).isEqualTo(newPassword);
        verify(baseRepository).findById(id);
        verify(baseRepository).save(customer);
        verifyNoMoreInteractions(baseRepository);
    }

    @Test
    void testEditPassword_IfPasswordIsEmpty_ThrowException() {
        // Given
        Long id = 1L;
        String newPassword = "";
        Customer customer = new Customer();
        customer.setEmail("Ali123@Gmail.com");
        customer.setPassword("Ali1234");

        when(baseRepository.findById(id)).thenReturn(Optional.of(customer));

        // When/Then
        assertThatThrownBy(()-> underTest.editPassword(id,newPassword))
                .isInstanceOf(CustomException.class);

        verify(baseRepository).findById(id);
        verifyNoMoreInteractions(baseRepository);
    }
    @Test
    void testEditPassword_ShouldCatchPersistenceException() {
        // Given
        Long id = 1L;
        String newPassword = "Test1234";
        Customer customer = new Customer();
        customer.setEmail("Ali123@Gmail.com");
        customer.setPassword("Ali1234");

        when(baseRepository.findById(id)).thenReturn(Optional.of(customer));
        doThrow(new PersistenceException("PersistenceException Message")).when(baseRepository).save(any(User.class));

        // When/Then
        assertThatThrownBy(()-> underTest.editPassword(id,newPassword))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: PersistenceException
                        \uD83D\uDCC3DESC:
                        PersistenceException Message""");

        verify(baseRepository).findById(id);
        verify(baseRepository).save(any(User.class));
        verifyNoMoreInteractions(baseRepository);
    }

    @Test
    void testFindById_UserFound() {
        // Given
        Long id = 1L;
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId(id);
        expectedCustomer.setEmail("Ali123@Gmail.com");
        expectedCustomer.setPassword("Ali1234");
        when(baseRepository.findById(id)).thenReturn(Optional.of(expectedCustomer));

        // When
        User actualCustomer = underTest.findById(id);

        // Then
        assertThat(actualCustomer).isEqualTo(expectedCustomer);
        verify(baseRepository).findById(id);
        verifyNoMoreInteractions(baseRepository);
    }

    @Test
    void testFindById_UserNotFound() {
        // Given
        Long id = 1L;
        when(baseRepository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(()-> underTest.findById(id))
                .isInstanceOf(CustomException.class);
        verify(baseRepository).findById(id);
        verifyNoMoreInteractions(baseRepository);
    }

    @Test
    void testSave_ReturnsUser() {
        // Given
        Customer customer = new Customer();
        customer.setEmail("Ali123@Gmail.com");
        customer.setPassword("Ali1234");
        when(baseRepository.save(customer)).thenReturn(customer);

        // When
        User savedCustomer = underTest.save(customer);

        // Then
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer).isEqualTo(customer);
        assertThat(savedCustomer.getEmail()).isEqualTo(customer.getEmail());
        verify(baseRepository).save(any(User.class));
        verifyNoMoreInteractions(baseRepository);
    }
}