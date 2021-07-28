package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceTests {
    @Test
    public void testHighTemperature() {
        String id = "954b3e37-98a2-48a1-a153-8d16003e1577";
        PatientInfo patientInfo = new PatientInfo(id, "Иван", "Петров",
                LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("38.2"), new BloodPressure(120, 80)));
        System.out.println("High temperature!");

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature(id, new BigDecimal("36.6"));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(sendAlertService, Mockito.atLeastOnce())
                .send(argumentCaptor.capture());

        String expected = "Warning, patient with id: " + id + ", need help";
        Assertions.assertEquals(expected, argumentCaptor.getValue());
    }

    @Test
    public void testAbnormalBloodPressure() {
        String id = "954b3e37-98a2-48a1-a153-8d16003e1577";
        PatientInfo patientInfo = new PatientInfo(id, "Иван", "Петров",
                LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(140, 90)));
        System.out.println("Abnormal blood pressure!");

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure(id, new BloodPressure(120, 80));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendAlertService, Mockito.atLeastOnce())
                .send(argumentCaptor.capture());

        System.out.println(argumentCaptor.getValue());

        String expected = "Warning, patient with id: " + id + ", need help";
        Assertions.assertEquals(expected, argumentCaptor.getValue());
    }

    @Test
    public void testHealthOK() {
        int numberOfMessages = 0;
        String id = "954b3e37-98a2-48a1-a153-8d16003e1577";
        PatientInfo patientInfo = new PatientInfo(id, "Иван", "Петров",
                LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature(id, new BigDecimal("36.6"));
        medicalService.checkBloodPressure(id, new BloodPressure(120, 80));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendAlertService, Mockito.times(numberOfMessages))
                .send("Warning, patient with id: " + id + ", need help");

        int expected = 0;
        Assertions.assertEquals(expected, numberOfMessages);
    }
}













