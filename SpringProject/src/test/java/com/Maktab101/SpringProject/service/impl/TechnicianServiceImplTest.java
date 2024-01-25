package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.model.enums.TechnicianStatus;
import com.Maktab101.SpringProject.repository.TechnicianRepository;
import com.Maktab101.SpringProject.service.dto.RegisterDto;
import com.Maktab101.SpringProject.utils.CustomException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnicianServiceImplTest {


    @Mock
    private TechnicianRepository technicianRepository;
    @Mock
    private Validator validator;
    private TechnicianServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new TechnicianServiceImpl(technicianRepository, validator);
    }

    @Test
    void testRegister() {
        // Given
        // When
        // Then
    }

    @Test
    @Disabled
    void testValidateImage_ValidImage_DoesNothing() throws IOException {
        // Given
        String imageAddress =
                "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\Untitled.jpg";

        File imageFile = new File(imageAddress);

        // When
        underTest.validateImage(imageAddress);

//        // Then
//        verifyStatic(ImageIO.class, times(1));
//        ImageIO.createImageInputStream(imageFile);
//
//        // Verify that the ImageInputStream is closed
//        verify(imageInputStream).close();
    }

    @Test
    void testConfirmTechnician_IdFound_SaveChanges() {
        // Given
        Long id = 1L;
        TechnicianStatus status = TechnicianStatus.CONFIRMED;
        Technician technician = new Technician();
        technician.setId(id);
        technician.setEmail("Ali@gmail.com");
        technician.setPassword("Ali1234");

        when(technicianRepository.findById(id)).thenReturn(Optional.of(technician));

        // When
        underTest.confirmTechnician(id);

        // Then
        assertThat(technician.getStatus()).isEqualTo(status);
        verify(technicianRepository).findById(id);
        verify(technicianRepository).save(technician);
        verifyNoMoreInteractions(technicianRepository);
    }

    @Test
    void testConfirmTechnician_IdNotFound_ThrowsException() {
        // Given
        Long id = 1L;
        TechnicianStatus status = TechnicianStatus.CONFIRMED;
        Technician technician = new Technician();
        technician.setId(id);
        technician.setEmail("Ali@gmail.com");
        technician.setPassword("Ali1234");

        when(technicianRepository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> underTest.confirmTechnician(id))
                .isInstanceOf(CustomException.class);

        // Then
        assertThat(technician.getStatus()).isNotEqualTo(status);
        verify(technicianRepository).findById(id);
        verifyNoMoreInteractions(technicianRepository);
    }

    @Test
    void testConfirmTechnician_CatchesPersistenceException_WhenThrown() {
        // Given
        Long id = 1L;
        Technician technician = new Technician();
        technician.setId(id);
        technician.setEmail("Ali@gmail.com");
        technician.setPassword("Ali1234");

        when(technicianRepository.findById(id)).thenReturn(Optional.of(technician));
        doThrow(new PersistenceException("PersistenceException Message")).when(technicianRepository).save(any(Technician.class));


        // When/Then
        assertThatThrownBy(() -> underTest.confirmTechnician(id))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: PersistenceException
                        \uD83D\uDCC3DESC:
                        PersistenceException Message""");

        verify(technicianRepository).save(any(Technician.class));
        verify(technicianRepository).findById(id);
        verifyNoMoreInteractions(technicianRepository);
    }

    @Test
    void testSaveImage_Successful() {
        // Given
        Long technicianId = 1L;
        String imageAddress =
                "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\Untitled1.jpg";

        Technician technician = new Technician();
        technician.setId(technicianId);
        technician.setEmail("technician@example.com");

        byte[] bytes = {1, 2, 3};

        TechnicianServiceImpl technicianServiceSpy = Mockito.spy(new TechnicianServiceImpl(technicianRepository,validator));

        when(technicianRepository.findById(technicianId)).thenReturn(Optional.of(technician));
        doReturn(bytes).when(technicianServiceSpy).imageToBytes(imageAddress);

        // When
        technicianServiceSpy.saveImage(technicianId, imageAddress);

        // Then
        verify(technicianRepository).findById(technicianId);
        verify(technicianServiceSpy).imageToBytes(imageAddress);
        verify(technicianRepository).save(technician);
        verifyNoMoreInteractions(technicianRepository);
    }
    @Test
    void testSaveImage_ThrowsException() {
        // Given
        Long id = 1L;
        String imageAddress =
                "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\Untitled1.jpg";

        Technician technician = new Technician();
        technician.setId(id);
        technician.setEmail("technician@example.com");

        when(technicianRepository.findById(id)).thenReturn(Optional.of(technician));
        doThrow(new PersistenceException("PersistenceException Message")).when(technicianRepository).save(any(Technician.class));

        // When/Then
        assertThatThrownBy(() -> underTest.saveImage(id,imageAddress))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: PersistenceException
                        \uD83D\uDCC3DESC:
                        PersistenceException Message""");

        verify(technicianRepository).save(any(Technician.class));
        verify(technicianRepository).findById(id);
        verifyNoMoreInteractions(technicianRepository);
    }

    @Test
    void testImageToBytes_ConvertsImageToBytes() throws IOException {
        // Given
        String imagePath =
                "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\Untitled.jpg";
        byte[] expectedBytes = Files.readAllBytes(new File(imagePath).toPath());

        // When
        byte[] actualBytes = underTest.imageToBytes(imagePath);

        // Then
        assertThat(actualBytes).isEqualTo(expectedBytes);
        verifyNoInteractions(technicianRepository);
    }

    @Test
    @Disabled
    void testImageToBytes_IOExceptionOccurs() {

    }



    @Test
    void testGetViolationMessages() {
        // Given
        Set<ConstraintViolation<RegisterDto>> violations = new HashSet<>();
        ConstraintViolation<RegisterDto> mockedViolation1 = mock(ConstraintViolation.class);
        when(mockedViolation1.getMessage()).thenReturn("Violation1");
        violations.add(mockedViolation1);

        ConstraintViolation<RegisterDto> mockedViolation2 = mock(ConstraintViolation.class);
        when(mockedViolation2.getMessage()).thenReturn("Violation2");
        violations.add(mockedViolation2);

        // When
        String violationMessages = underTest.getViolationMessages(violations);

        // Then
        assertThat(violationMessages).contains("Violation1", "Violation2");
        verifyNoInteractions(technicianRepository);
    }

    @Test
    void testCheckCondition_ConditionsAreMet() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Ali1234");

        when(technicianRepository.existsByEmail(registerDto.getEmailAddress())).thenReturn(false);

        // When
        underTest.checkCondition(registerDto);

        // Then
        verify(technicianRepository).existsByEmail(registerDto.getEmailAddress());
        verifyNoMoreInteractions(technicianRepository);
    }

    @Test
    void testCheckCondition_ConditionsNotMet_ThrowsException() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Ali1234");

        when(underTest.existsByEmailAddress(registerDto.getEmailAddress())).thenReturn(true);

        // When/Then
        assertThatThrownBy(() -> underTest.checkCondition(registerDto))
                .isInstanceOf(CustomException.class);
        verify(technicianRepository).existsByEmail(registerDto.getEmailAddress());
        verifyNoMoreInteractions(technicianRepository);
    }

    @Test
    void testMapDtoValues_ReturnsTechnician() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstname("Ali");
        registerDto.setLastname("Alavi");
        registerDto.setEmailAddress("Ali@gmail.com");
        registerDto.setPassword("Ali1234");

        // When
        Technician technician = underTest.mapDtoValues(registerDto);

        // Then
        assertThat(technician).isNotNull();
        assertThat(technician.getFirstname()).isEqualTo(registerDto.getFirstname());
        assertThat(technician.getLastname()).isEqualTo(registerDto.getLastname());
        assertThat(technician.getEmail()).isEqualTo(registerDto.getEmailAddress());
        assertThat(technician.getPassword()).isEqualTo(registerDto.getPassword());
        assertThat(technician.getStatus()).isEqualTo(TechnicianStatus.NEW);
        verifyNoInteractions(technicianRepository);
    }
}