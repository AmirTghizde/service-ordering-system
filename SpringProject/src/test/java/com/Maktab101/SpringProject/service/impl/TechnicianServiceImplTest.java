//package com.Maktab101.SpringProject.service.impl;
//
//import com.Maktab101.SpringProject.model.Technician;
//import com.Maktab101.SpringProject.model.enums.TechnicianStatus;
//import com.Maktab101.SpringProject.repository.TechnicianRepository;
//import com.Maktab101.SpringProject.dto.users.RegisterDto;
//import com.Maktab101.SpringProject.utils.exceptions.CustomException;
//import jakarta.persistence.PersistenceException;
//import jakarta.validation.ConstraintViolation;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.*;
//
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TechnicianServiceImplTest {
//
//
//    @Mock
//    private TechnicianRepository technicianRepository;
//    @Mock
//    private BCryptPasswordEncoder passwordEncoder;
//    private TechnicianServiceImpl underTest;
//
//    @BeforeEach
//    void setUp() {
//        underTest = new TechnicianServiceImpl(technicianRepository, passwordEncoder);
//    }
//
//    @Test
//    void testRegister_ValidInfo_ReturnsTechnician() {
//        // Given
//        RegisterDto registerDto = new RegisterDto();
//        registerDto.setFirstname("Ali");
//        registerDto.setLastname("Alavi");
//        registerDto.setEmailAddress("Ali@gmail.com");
//        registerDto.setPassword("Ali1234");
//        Set<ConstraintViolation<RegisterDto>> violations = new HashSet<>();
//
//        Technician expectedTechnician = underTest.mapDtoValues(registerDto);
//
//        when(technicianRepository.save(any(Technician.class))).thenReturn(expectedTechnician);
//
//        // When
//        Technician actualTechnician = underTest.register(registerDto);
//
//        // Then
//        assertThat(actualTechnician).isEqualTo(expectedTechnician);
//        verify(technicianRepository).save(any(Technician.class));
//        verify(technicianRepository).existsByEmail(registerDto.getEmailAddress());
//        verifyNoMoreInteractions(technicianRepository);
//    }
//
//    @Test
//    void testRegister_CatchesPersistenceException_WhenThrown() {
//        // Given
//        RegisterDto registerDto = new RegisterDto();
//        registerDto.setFirstname("Ali");
//        registerDto.setLastname("Alavi");
//        registerDto.setEmailAddress("Ali@gmail.com");
//        registerDto.setPassword("Ali1234");
//
//        doThrow(new PersistenceException("PersistenceException Message")).when(technicianRepository).save(any(Technician.class));
//
//        // When/Then
//        assertThatThrownBy(() -> underTest.register(registerDto))
//                .isInstanceOf(CustomException.class)
//                .hasMessage("(×_×;）\n" +
//                        "❗ERROR: PersistenceException\n" +
//                        "\uD83D\uDCC3DESC:\n" +
//                        "PersistenceException Message");
//
//        verify(technicianRepository).save(any(Technician.class));
//        verify(technicianRepository).existsByEmail(registerDto.getEmailAddress());
//        verifyNoMoreInteractions(technicianRepository);
//    }
//
//    @Test
//    void testValidateImage_ValidImage_DoesNothing() {
//        // Given
//        String validImage =
//                "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\Untitled.jpg";
//
//        // When/Then
//        assertDoesNotThrow(() -> underTest.validateImage(validImage));
//        verifyNoInteractions(technicianRepository);
//    }
//
//    @Test
//    void testValidateImage_ValidImage_ThrowsExceptionAboutSize() {
//        // Given
//        String validImage =
//                "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\Untitled4.jpg";
//
//        // When/Then
//        assertThatThrownBy(() -> underTest.validateImage(validImage))
//                .isInstanceOf(CustomException.class)
//                .hasMessage("""
//                        (×_×;）
//                        ❗ERROR: InvalidImageSize
//                        📃DESC:
//                        Max image size is 300kb""");
//        verifyNoInteractions(technicianRepository);
//    }
//
//    @Test
//    void testValidateImage_InvalidValidImage_ThrowsExceptionAboutFormat() {
//        // Given
//        String invalidImage =
//                "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\Untitled5.png";
//
//        // When/Then
//        assertThatThrownBy(() -> underTest.validateImage(invalidImage))
//                .isInstanceOf(CustomException.class)
//                .hasMessage("""
//                        (×_×;）
//                        ❗ERROR: InvalidImage
//                        📃DESC:
//                        The only supported format is JPEG""");
//        verifyNoInteractions(technicianRepository);
//    }
//
//    @Test
//    void testValidateImage_ImageNotFound_ThrowsCustomException() {
//        // Given
//        String invalidImage = "NonExistingImagePath";
//
//        // When/Then
//        assertThatThrownBy(() -> underTest.validateImage(invalidImage))
//                .isInstanceOf(CustomException.class)
//                .hasMessage("""
//                        (×_×;）
//                        ❗ERROR: ImageNotFound
//                        📃DESC:
//                        We can not find the image""");
//        verifyNoInteractions(technicianRepository);
//    }
//
//    @Test
//    @Disabled
//    void testValidateImage_ThrowsIOException() {
////        // Given
////        String validImage = "path/to/valid/image.jpg";
////
////        TechnicianServiceImpl underTest = new TechnicianServiceImpl() {
////
////        };
////
////        // When/Then
////        Assertions.assertThrows(IOException.class, () -> underTest.validateImage(validImage));
//    }
//
//    @Test
//    void testConfirmTechnician_IdFound_SaveChanges() {
//        // Given
//        Long id = 1L;
//        TechnicianStatus status = TechnicianStatus.CONFIRMED;
//        Technician technician = new Technician();
//        technician.setId(id);
//        technician.setEmail("Ali@gmail.com");
//        technician.setPassword("Ali1234");
//
//        when(technicianRepository.findById(id)).thenReturn(Optional.of(technician));
//
//        // When
//        underTest.confirmTechnician(id);
//
//        // Then
//        assertThat(technician.getStatus()).isEqualTo(status);
//        verify(technicianRepository).findById(id);
//        verify(technicianRepository).save(technician);
//        verifyNoMoreInteractions(technicianRepository);
//    }
//
//    @Test
//    void testConfirmTechnician_IdNotFound_ThrowsException() {
//        // Given
//        Long id = 1L;
//        TechnicianStatus status = TechnicianStatus.CONFIRMED;
//        Technician technician = new Technician();
//        technician.setId(id);
//        technician.setEmail("Ali@gmail.com");
//        technician.setPassword("Ali1234");
//
//        when(technicianRepository.findById(id)).thenReturn(Optional.empty());
//
//        // When/Then
//        assertThatThrownBy(() -> underTest.confirmTechnician(id))
//                .isInstanceOf(CustomException.class);
//
//        // Then
//        assertThat(technician.getStatus()).isNotEqualTo(status);
//        verify(technicianRepository).findById(id);
//        verifyNoMoreInteractions(technicianRepository);
//    }
//
//    @Test
//    void testConfirmTechnician_CatchesPersistenceException_WhenThrown() {
//        // Given
//        Long id = 1L;
//        Technician technician = new Technician();
//        technician.setId(id);
//        technician.setEmail("Ali@gmail.com");
//        technician.setPassword("Ali1234");
//
//        when(technicianRepository.findById(id)).thenReturn(Optional.of(technician));
//        doThrow(new PersistenceException("PersistenceException Message")).when(technicianRepository).save(any(Technician.class));
//
//
//        // When/Then
//        assertThatThrownBy(() -> underTest.confirmTechnician(id))
//                .isInstanceOf(CustomException.class)
//                .hasMessage("""
//                        (×_×;）
//                        ❗ERROR: PersistenceException
//                        \uD83D\uDCC3DESC:
//                        PersistenceException Message""");
//
//        verify(technicianRepository).save(any(Technician.class));
//        verify(technicianRepository).findById(id);
//        verifyNoMoreInteractions(technicianRepository);
//    }
//
////    @Test
////    void testSaveImage_Successful() {
////        // Given
////        Long technicianId = 1L;
////        String imageAddress =
////                "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\Untitled1.jpg";
////
////        Technician technician = new Technician();
////        technician.setId(technicianId);
////        technician.setEmail("technician@example.com");
////
////        byte[] bytes = {1, 2, 3};
////
////        TechnicianServiceImpl technicianServiceSpy = Mockito.spy(new TechnicianServiceImpl(technicianRepository, validator, sortFilterable, sortFilterable));
////
////        when(technicianRepository.findById(technicianId)).thenReturn(Optional.of(technician));
////        doReturn(bytes).when(technicianServiceSpy).imageToBytes(imageAddress);
////
////        // When
////        technicianServiceSpy.saveImage(technicianId, imageAddress);
////
////        // Then
////        verify(technicianRepository).findById(technicianId);
////        verify(technicianServiceSpy).imageToBytes(imageAddress);
////        verify(technicianRepository).save(technician);
////        verifyNoMoreInteractions(technicianRepository);
////    }
//
//    @Test
//    void testSaveImage_CatchesPersistenceException_WhenThrown() {
//        // Given
//        Long id = 1L;
//        String imageAddress =
//                "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\Untitled1.jpg";
//
//        Technician technician = new Technician();
//        technician.setId(id);
//        technician.setEmail("technician@example.com");
//
//        when(technicianRepository.findById(id)).thenReturn(Optional.of(technician));
//        doThrow(new PersistenceException("PersistenceException Message")).when(technicianRepository).save(any(Technician.class));
//
//        // When/Then
//        assertThatThrownBy(() -> underTest.saveImage(id, imageAddress))
//                .isInstanceOf(CustomException.class)
//                .hasMessage("""
//                        (×_×;）
//                        ❗ERROR: PersistenceException
//                        \uD83D\uDCC3DESC:
//                        PersistenceException Message""");
//
//        verify(technicianRepository).save(any(Technician.class));
//        verify(technicianRepository).findById(id);
//        verifyNoMoreInteractions(technicianRepository);
//    }
//
//    @Test
//    void testImageToBytes_ConvertsImageToBytes() throws IOException {
//        // Given
//        String imagePath =
//                "D:\\Java\\Maktab\\HW\\SpringProject\\SpringProject\\src\\main\\resources\\images\\Untitled.jpg";
//        byte[] expectedBytes = Files.readAllBytes(new File(imagePath).toPath());
//
//        // When
//        byte[] actualBytes = underTest.imageToBytes(imagePath);
//
//        // Then
//        assertThat(actualBytes).isEqualTo(expectedBytes);
//        verifyNoInteractions(technicianRepository);
//    }
//
//    @Test
//    @Disabled
//    void testImageToBytes_IOExceptionOccurs() {
//
//    }
//
//    @Test
//    void testCheckCondition_ConditionsAreMet() {
//        // Given
//        RegisterDto registerDto = new RegisterDto();
//        registerDto.setFirstname("Ali");
//        registerDto.setLastname("Alavi");
//        registerDto.setEmailAddress("Ali@gmail.com");
//        registerDto.setPassword("Ali1234");
//
//        when(technicianRepository.existsByEmail(registerDto.getEmailAddress())).thenReturn(false);
//
//        // When
//        underTest.checkCondition(registerDto);
//
//        // Then
//        verify(technicianRepository).existsByEmail(registerDto.getEmailAddress());
//        verifyNoMoreInteractions(technicianRepository);
//    }
//
//    @Test
//    void testCheckCondition_ConditionsNotMet_ThrowsException() {
//        // Given
//        RegisterDto registerDto = new RegisterDto();
//        registerDto.setFirstname("Ali");
//        registerDto.setLastname("Alavi");
//        registerDto.setEmailAddress("Ali@gmail.com");
//        registerDto.setPassword("Ali1234");
//
//        when(underTest.existsByEmailAddress(registerDto.getEmailAddress())).thenReturn(true);
//
//        // When/Then
//        assertThatThrownBy(() -> underTest.checkCondition(registerDto))
//                .isInstanceOf(CustomException.class);
//        verify(technicianRepository).existsByEmail(registerDto.getEmailAddress());
//        verifyNoMoreInteractions(technicianRepository);
//    }
//
//    @Test
//    void testMapDtoValues_ReturnsTechnician() {
//        // Given
//        RegisterDto registerDto = new RegisterDto();
//        registerDto.setFirstname("Ali");
//        registerDto.setLastname("Alavi");
//        registerDto.setEmailAddress("Ali@gmail.com");
//        registerDto.setPassword("Ali1234");
//
//        // When
//        Technician technician = underTest.mapDtoValues(registerDto);
//
//        // Then
//        assertThat(technician).isNotNull();
//        assertThat(technician.getFirstname()).isEqualTo(registerDto.getFirstname());
//        assertThat(technician.getLastname()).isEqualTo(registerDto.getLastname());
//        assertThat(technician.getEmail()).isEqualTo(registerDto.getEmailAddress());
//        assertThat(technician.getPassword()).isEqualTo(registerDto.getPassword());
//        assertThat(technician.getStatus()).isEqualTo(TechnicianStatus.NEW);
//        assertThat(technician.getBalance()).isEqualTo(0);
//        verifyNoInteractions(technicianRepository);
//    }
//}