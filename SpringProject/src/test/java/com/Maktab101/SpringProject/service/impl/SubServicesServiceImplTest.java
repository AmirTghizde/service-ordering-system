package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.MainServices;
import com.Maktab101.SpringProject.model.SubServices;
import com.Maktab101.SpringProject.model.Technician;
import com.Maktab101.SpringProject.model.enums.TechnicianStatus;
import com.Maktab101.SpringProject.repository.SubServicesRepository;
import com.Maktab101.SpringProject.service.MainServicesService;
import com.Maktab101.SpringProject.service.TechnicianService;
import com.Maktab101.SpringProject.utils.CustomException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubServicesServiceImplTest {
    @Mock
    private MainServicesService mainServicesService;
    @Mock
    private TechnicianService technicianService;
    @Mock
    private SubServicesRepository subServicesRepository;
    private SubServicesServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new SubServicesServiceImpl(
                mainServicesService,technicianService,subServicesRepository
        );
    }

    @Test
    void testAddService() {
        // Given
        // When
        // Then
    }

    @Test
    void testFindByName() {
        // Given
        // When
        // Then
    }

    @Test
    void testExistsByName() {
        // Given
        // When
        // Then
    }

    @Test
    void testEditBaseWage() {
        // Given
        // When
        // Then
    }

    @Test
    void testEditDescription() {
        // Given
        // When
        // Then
    }

    @Test
    void testAddToSubService_ValidInfo_AddsTechnician() {
        // Given
        Long subServiceId= 1L;
        SubServices subServices = new SubServices();
        subServices.setName("HouseCleaning");

        Long technicianId= 2L;
        Technician technician = new Technician();
        technician.setEmail("Ali@gamil.com");
        technician.setPassword("Ali1234");
        technician.setStatus(TechnicianStatus.CONFIRMED);
        when(technicianService.findById(technicianId)).thenReturn(technician);
        when(subServicesRepository.findById(subServiceId)).thenReturn(Optional.of(subServices));

        // When
        underTest.addToSubService(technicianId,subServiceId);

        // Then
        assertThat(subServices.getTechnicians()).contains(technician);
        assertThat(technician.getSubServices()).contains(subServices);
        verify(subServicesRepository).save(subServices);
        verify(subServicesRepository).findById(subServiceId);
        verify(technicianService).save(technician);
        verify(technicianService).findById(technicianId);
        verifyNoMoreInteractions(subServicesRepository);
        verifyNoMoreInteractions(technicianService);
    }
    @Test
    void testAddToSubService_TechnicalNotConfirmed_ThrowsException() {
        // Given
        Long subServiceId= 1L;
        SubServices subServices = new SubServices();
        subServices.setName("HouseCleaning");

        Long technicianId= 2L;
        Technician technician = new Technician();
        technician.setEmail("Ali@gamil.com");
        technician.setPassword("Ali1234");
        technician.setStatus(TechnicianStatus.NEW);
        when(technicianService.findById(technicianId)).thenReturn(technician);
        when(subServicesRepository.findById(subServiceId)).thenReturn(Optional.of(subServices));

        // When/Then
        assertThatThrownBy(()->underTest.addToSubService(technicianId,subServiceId))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: InvalidTechnician
                        \uD83D\uDCC3DESC:
                        Technician must be confirmed first""");
        verify(subServicesRepository).findById(subServiceId);
        verify(technicianService).findById(technicianId);
        verifyNoMoreInteractions(subServicesRepository);
        verifyNoMoreInteractions(technicianService);
    }
    @Test
    void testAddToSubService_TechnicianExists_ThrowsException() {
        // Given
        Long subServiceId= 1L;
        SubServices subServices = new SubServices();
        subServices.setName("HouseCleaning");

        Long technicianId= 2L;
        Technician technician = new Technician();
        technician.setEmail("Ali@gamil.com");
        technician.setPassword("Ali1234");
        technician.setStatus(TechnicianStatus.CONFIRMED);
        technician.getSubServices().add(subServices);
        subServices.getTechnicians().add(technician);
        when(technicianService.findById(technicianId)).thenReturn(technician);
        when(subServicesRepository.findById(subServiceId)).thenReturn(Optional.of(subServices));

        // When/Then
        assertThatThrownBy(()->underTest.addToSubService(technicianId,subServiceId))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: TechnicianAlreadyExists
                        \uD83D\uDCC3DESC:
                        You already added this technician before""");
        verify(subServicesRepository).findById(subServiceId);
        verify(technicianService).findById(technicianId);
        verifyNoMoreInteractions(subServicesRepository);
        verifyNoMoreInteractions(technicianService);
    }
    @Test
    void testAddToSubService_CatchesPersistenceException_WhenThrown() {
        // Given
        Long subServiceId= 1L;
        SubServices subServices = new SubServices();
        subServices.setName("HouseCleaning");

        Long technicianId= 2L;
        Technician technician = new Technician();
        technician.setEmail("Ali@gamil.com");
        technician.setPassword("Ali1234");
        technician.setStatus(TechnicianStatus.CONFIRMED);
        when(technicianService.findById(technicianId)).thenReturn(technician);
        when(subServicesRepository.findById(subServiceId)).thenReturn(Optional.of(subServices));
        doThrow(new PersistenceException("PersistenceException Message")).when(subServicesRepository).save(any(SubServices.class));

        // When/Then
        assertThatThrownBy(()->underTest.addToSubService(technicianId,subServiceId))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: PersistenceException
                        \uD83D\uDCC3DESC:
                        PersistenceException Message""");
        verify(subServicesRepository).save(subServices);
        verify(subServicesRepository).findById(subServiceId);
        verify(technicianService).findById(technicianId);
        verifyNoMoreInteractions(subServicesRepository);
        verifyNoMoreInteractions(technicianService);
    }

    @Test
    void testDeleteFromSubService_ValidInfo_DeletesTechnician() {
        // Given
        Long subServiceId= 1L;
        SubServices subServices = new SubServices();
        subServices.setName("HouseCleaning");

        Long technicianId= 2L;
        Technician technician = new Technician();
        technician.setEmail("Ali@gamil.com");
        technician.setPassword("Ali1234");
        subServices.getTechnicians().add(technician);
        technician.getSubServices().add(subServices);
        when(technicianService.findById(technicianId)).thenReturn(technician);
        when(subServicesRepository.findById(subServiceId)).thenReturn(Optional.of(subServices));

        // When
        underTest.deleteFromSubService(technicianId,subServiceId);

        // Then
        assertThat(subServices.getTechnicians()).doesNotContain(technician);
        assertThat(technician.getSubServices()).doesNotContain(subServices);
        verify(subServicesRepository).save(subServices);
        verify(subServicesRepository).findById(subServiceId);
        verify(technicianService).save(technician);
        verify(technicianService).findById(technicianId);
        verifyNoMoreInteractions(subServicesRepository);
        verifyNoMoreInteractions(technicianService);
    }
    @Test
    void testDeleteFromSubService_IfDoesntExist_ThrowException() {
        // Given
        Long subServiceId= 1L;
        SubServices subServices = new SubServices();
        subServices.setName("HouseCleaning");

        Long technicianId= 2L;
        Technician technician = new Technician();
        technician.setEmail("Ali@gamil.com");
        technician.setPassword("Ali1234");
        when(technicianService.findById(technicianId)).thenReturn(technician);
        when(subServicesRepository.findById(subServiceId)).thenReturn(Optional.of(subServices));

        // When/Then
        assertThatThrownBy(()-> underTest.deleteFromSubService(technicianId,subServiceId))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: TechnicianDoesntExist
                        \uD83D\uDCC3DESC:
                        Sub service doesn't have that technician""");
        verify(subServicesRepository).findById(subServiceId);
        verify(technicianService).findById(technicianId);
        verifyNoMoreInteractions(subServicesRepository);
        verifyNoMoreInteractions(technicianService);
    }
    @Test
    void testDeleteFromSubService_CatchesPersistenceException_WhenThrown() {
        // Given
        Long subServiceId= 1L;
        SubServices subServices = new SubServices();
        subServices.setName("HouseCleaning");

        Long technicianId= 2L;
        Technician technician = new Technician();
        technician.setEmail("Ali@gamil.com");
        technician.setPassword("Ali1234");
        subServices.getTechnicians().add(technician);
        technician.getSubServices().add(subServices);
        when(technicianService.findById(technicianId)).thenReturn(technician);
        when(subServicesRepository.findById(subServiceId)).thenReturn(Optional.of(subServices));
        doThrow(new PersistenceException("PersistenceException Message")).when(subServicesRepository).save(any(SubServices.class));

        // When/Then
        assertThatThrownBy(()->underTest.deleteFromSubService(technicianId,subServiceId))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: PersistenceException
                        \uD83D\uDCC3DESC:
                        PersistenceException Message""");
        verify(subServicesRepository).save(subServices);
        verify(subServicesRepository).findById(subServiceId);
        verify(technicianService).findById(technicianId);
        verifyNoMoreInteractions(subServicesRepository);
        verifyNoMoreInteractions(technicianService);
    }

    @Test
    void testFindById_IfFound_ReturnsOptionalSubServices() {
        // Given
        Long subServiceId= 1L;
        SubServices expectedSubService = new SubServices();
        expectedSubService.setName("HouseCleaning");
        when(subServicesRepository.findById(subServiceId)).thenReturn(Optional.of(expectedSubService));

        // When
        SubServices actualSubService = underTest.findById(subServiceId);

        // Then
        assertThat(actualSubService).isEqualTo(expectedSubService);
        verify(subServicesRepository).findById(subServiceId);
        verifyNoMoreInteractions(subServicesRepository);
    }
    @Test
    void testFindById_IfNotFound_ThrowsException() {
        // Given
        Long subServiceId= 1L;
        SubServices expectedSubService = new SubServices();
        expectedSubService.setName("HouseCleaning");
        when(subServicesRepository.findById(subServiceId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(()-> underTest.findById(subServiceId))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: SubServiceNotFound
                        \uD83D\uDCC3DESC:
                        We can not find the sub service""");
        verify(subServicesRepository).findById(subServiceId);
        verifyNoMoreInteractions(subServicesRepository);
    }

    @Test
    void testSave_ReturnsSubServices() {
        // Given
        SubServices expectedSubService = new SubServices();
        expectedSubService.setName("HouseCleaning");
        when(subServicesRepository.save(expectedSubService)).thenReturn(expectedSubService);

        // When
        SubServices actualSubService = underTest.save(expectedSubService);

        // Then
        assertThat(actualSubService).isEqualTo(expectedSubService);
        verify(subServicesRepository).save(expectedSubService);
        verifyNoMoreInteractions(subServicesRepository);
    }

    @Test
    void testCheckConditions_ConditionsAreMet() {
        // Given
        String mainServiceName = "Cleaning";
        String subServiceName = "HouseCleaning";
        when(subServicesRepository.existsByName(subServiceName)).thenReturn(false);
        when(mainServicesService.existsByName(mainServiceName)).thenReturn(true);

        // When
        underTest.checkConditions(subServiceName,mainServiceName);

        // Then
        verify(subServicesRepository).existsByName(subServiceName);
        verify(mainServicesService).existsByName(mainServiceName);
        verifyNoMoreInteractions(subServicesRepository);
        verifyNoMoreInteractions(mainServicesService);
    }
    @Test
    void testCheckConditions_SubServiceNameExists_ThrowsException() {
        // Given
        String mainServiceName = "Cleaning";
        String subServiceName = "HouseCleaning";
        when(subServicesRepository.existsByName(subServiceName)).thenReturn(true);

        // When/Then
        assertThatThrownBy(()-> underTest.checkConditions(subServiceName,mainServiceName))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: DuplicateSubService
                        \uD83D\uDCC3DESC:
                        Sub service already exists in the database""");

        verify(subServicesRepository).existsByName(subServiceName);
        verifyNoMoreInteractions(subServicesRepository);
        verifyNoInteractions(mainServicesService);
    }
    @Test
    void testCheckConditions_MainServiceNameNotFound_ThrowsException() {
        // Given
        String mainServiceName = "Cleaning";
        String subServiceName = "HouseCleaning";
        when(subServicesRepository.existsByName(subServiceName)).thenReturn(false);
        when(mainServicesService.existsByName(mainServiceName)).thenReturn(false);

        // When/Then
        assertThatThrownBy(()-> underTest.checkConditions(subServiceName,mainServiceName))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: MainServiceNotFound
                        \uD83D\uDCC3DESC:
                        We can not find the main service""");

        verify(subServicesRepository).existsByName(subServiceName);
        verify(mainServicesService).existsByName(mainServiceName);
        verifyNoMoreInteractions(subServicesRepository);
        verifyNoMoreInteractions(mainServicesService);
    }

    @Test
    void testSetValues_ReturnsSubServices() {
        // Given
        String serviceName = "Cleaning";
        String description = "Blah Blah";
        double baseWage = 50;


        // When
        SubServices subServices = underTest.setValues(serviceName, baseWage, description);

        // Then
        assertThat(subServices.getName()).isEqualTo(serviceName);
        assertThat(subServices.getBaseWage()).isEqualTo(baseWage);
        assertThat(subServices.getDescription()).isEqualTo(description);
        verifyNoInteractions(subServicesRepository);
    }
}