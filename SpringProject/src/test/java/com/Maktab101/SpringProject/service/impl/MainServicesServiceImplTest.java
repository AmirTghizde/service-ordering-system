package com.Maktab101.SpringProject.service.impl;

import com.Maktab101.SpringProject.model.MainServices;
import com.Maktab101.SpringProject.model.Manager;
import com.Maktab101.SpringProject.model.SubServices;
import com.Maktab101.SpringProject.repository.MainServicesRepository;
import com.Maktab101.SpringProject.service.dto.RegisterDto;
import com.Maktab101.SpringProject.utils.CustomException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainServicesServiceImplTest {

    @Mock
    private MainServicesRepository mainServicesRepository;

    private MainServicesServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new MainServicesServiceImpl(mainServicesRepository);
    }

    @Test
    void testAddService_SavesService() {
        // Given
        String serviceName = "Cleaning";

        // When
        underTest.addService(serviceName);

        // Then
        verify(mainServicesRepository, times(1)).save(any(MainServices.class));
        verify(mainServicesRepository).existsByName(serviceName);
        verifyNoMoreInteractions(mainServicesRepository);
    }
    @Test
    void testAddService_CatchesPersistenceException_WhenThrown() {
        // Given
        String serviceName = "Cleaning";
        MainServices mainServices = new MainServices();
        mainServices.setName(serviceName);
        doThrow(new PersistenceException("PersistenceException Message")).when(mainServicesRepository).save(any(MainServices.class));

        // When/Then
        assertThatThrownBy(() -> underTest.addService(serviceName))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: PersistenceException
                        \uD83D\uDCC3DESC:
                        PersistenceException Message""");

        verify(mainServicesRepository).save(any(MainServices.class));
        verify(mainServicesRepository).existsByName(serviceName);
        verifyNoMoreInteractions(mainServicesRepository);
    }

    @Test
    void testFindByName_ReturnsServiceOptional() {
        // Given
        String serviceName="Cleaning";
        MainServices expectedService = new MainServices();
        expectedService.setName(serviceName);
        when(mainServicesRepository.findByName(serviceName)).thenReturn(Optional.of(expectedService));

        // When
        Optional<MainServices> actualService = underTest.findByName(serviceName);

        // Then
        assertThat(actualService).isEqualTo(Optional.of(expectedService));
        verify(mainServicesRepository).findByName(serviceName);
        verifyNoMoreInteractions(mainServicesRepository);
    }
    @Test
    void testFindByName_ReturnsEmptyOptional() {
        // Given
        String serviceName = "UnSavedService";
        when(mainServicesRepository.findByName(serviceName)).thenReturn(Optional.empty());

        // When
        Optional<MainServices> actualService = underTest.findByName(serviceName);

        // Then
        assertThat(actualService).isEmpty();
        verify(mainServicesRepository).findByName(serviceName);
        verifyNoMoreInteractions(mainServicesRepository);
    }

    @Test
    void testExistsByName_ReturnsTrue() {
        // Given
        String serviceName = "Cleaning";
        when(mainServicesRepository.existsByName(serviceName)).thenReturn(true);

        // When
        boolean result = underTest.existsByName(serviceName);

        // Then
        assertThat(result).isEqualTo(true);
        verify(mainServicesRepository).existsByName(serviceName);
        verifyNoMoreInteractions(mainServicesRepository);
    }

    @Test
    void testExistsByName_ReturnsFalse() {
        // Given
        String serviceName = "Cleaning";
        when(mainServicesRepository.existsByName(serviceName)).thenReturn(false);

        // When
        boolean result = underTest.existsByName(serviceName);

        // Then
        assertThat(result).isEqualTo(false);
        verify(mainServicesRepository).existsByName(serviceName);
        verifyNoMoreInteractions(mainServicesRepository);
    }

    @Test
    void testSave_ReturnsMainServices() {
        // Given
        MainServices expectedService = new MainServices();
        expectedService.setName("Cleaning");
        when(mainServicesRepository.save(expectedService)).thenReturn(expectedService);

        // When
        MainServices actualService = underTest.save(expectedService);

        // Then
        assertThat(actualService).isEqualTo(expectedService);
        verify(mainServicesRepository).save(expectedService);
        verifyNoMoreInteractions(mainServicesRepository);
    }

    @Test
    void testFindAll_ReturnsListOfMainServices() {
        // Given
        List<MainServices> excpectedList = new ArrayList<>();

        MainServices mainServices1 = new MainServices();
        mainServices1.setName("Cleaning");
        excpectedList.add(mainServices1);
        MainServices mainServices2 = new MainServices();
        mainServices2.setName("Moving");
        excpectedList.add(mainServices2);

        when(mainServicesRepository.findAll()).thenReturn(excpectedList);

        // When
        List<MainServices> actualList = underTest.findAll();

        // Then
        assertThat(actualList).isEqualTo(excpectedList);
        verify(mainServicesRepository).findAll();
        verifyNoMoreInteractions(mainServicesRepository);
    }

    @Test
    void testFindSubServiceNames_ReturnsListOfSubServicesNames() {
        // Given
        Long mainServiceId = 1L;
        MainServices expectedService = new MainServices();
        expectedService.setId(mainServiceId);
        expectedService.setName("Cleaning");

        SubServices subService1 = new SubServices();
        subService1.setName("House Cleaning");
        expectedService.getSubServices().add(subService1);

        SubServices subService2 = new SubServices();
        subService2.setName("Carpet Cleaning");
        expectedService.getSubServices().add(subService2);

        when(mainServicesRepository.findById(mainServiceId)).thenReturn(Optional.of(expectedService));

        // When
        List<String> subServiceList = underTest.findSubServiceNames(mainServiceId);

        // Then
        assertThat(subServiceList).containsExactly("House Cleaning", "Carpet Cleaning");
        verify(mainServicesRepository).findById(mainServiceId);
        verifyNoMoreInteractions(mainServicesRepository);
    }

    @Test
    void testFindSubServiceNames_ServiceNotFound() {
        // Given
        Long mainServiceId = 1L;
        MainServices expectedService = new MainServices();
        expectedService.setId(mainServiceId);
        expectedService.setName("Cleaning");

        when(mainServicesRepository.findById(mainServiceId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(()->underTest.findSubServiceNames(mainServiceId))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: MainServiceNotFound
                        \uD83D\uDCC3DESC:
                        We cannot find the main service""");

        verify(mainServicesRepository).findById(mainServiceId);
        verifyNoMoreInteractions(mainServicesRepository);
    }
    @Test
    void testCheckConditions_ConditionsAreMet() {
        // Given
        String serviceName = "Cleaning";
        when(underTest.existsByName(serviceName)).thenReturn(false);

        // When
        underTest.checkConditions(serviceName);

        // Then
        verify(mainServicesRepository).existsByName(serviceName);
        verifyNoMoreInteractions(mainServicesRepository);
    }
    @Test
    void testCheckConditions_IfNameExists_ThrowsException() {
        // Given
        String serviceName = "Cleaning";
        when(underTest.existsByName(serviceName)).thenReturn(true);

        // When/Then
        assertThatThrownBy(()->underTest.checkConditions(serviceName))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: DuplicateMainService
                        \uD83D\uDCC3DESC:
                        Main service already exists in the database""");

        verify(mainServicesRepository).existsByName(serviceName);
        verifyNoMoreInteractions(mainServicesRepository);
    }
    @Test
    void testCheckConditions_IfNameIsEmpty_ThrowsException() {
        // Given
        String serviceName = "";

        // When/Then
        assertThatThrownBy(()->underTest.checkConditions(serviceName))
                .isInstanceOf(CustomException.class)
                .hasMessage("""
                        (×_×;）
                        ❗ERROR: InvalidServiceName
                        \uD83D\uDCC3DESC:
                        Main service must not be blank""");

        verify(mainServicesRepository).existsByName(serviceName);
        verifyNoMoreInteractions(mainServicesRepository);
    }

    @Test
    void testSetValues_ReturnsMainServices() {
        // Given
        String serviceName = "Cleaning";
        // When
        MainServices mainServices = underTest.setValues(serviceName);

        // Then
        assertThat(mainServices.getName()).isEqualTo(serviceName);
        verifyNoInteractions(mainServicesRepository);
    }
}